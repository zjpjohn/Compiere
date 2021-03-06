/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.compiere.model.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              C_Cash (407)
 *  Document Types:     CMC
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_Cash.java 9094 2010-07-02 07:08:25Z ragrawal $
 */
public class Doc_Cash extends Doc
{
	/**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trx p_trx
	 */
	public Doc_Cash (MAcctSchema[] ass, ResultSet rs, Trx trx)
	{
		super(ass, MCash.class, rs, MDocBaseType.DOCBASETYPE_CashJournal, trx);
	}	//	Doc_Cash

	/**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
	@Override
	public String loadDocumentDetails ()
	{
		MCash cash = (MCash)getPO();
		setDateDoc(cash.getStatementDate());

		//	Amounts
		setAmount(Doc.AMTTYPE_Gross, cash.getStatementDifference());

		//  Set CashBook Org & Currency
		MCashBook cb = MCashBook.get(getCtx(), cash.getC_CashBook_ID());
		setC_CashBook_ID(cb.getC_CashBook_ID());
		setC_Currency_ID(cb.getC_Currency_ID());

		//	Contained Objects
		p_lines = loadLines(cash, cb);
		log.fine("Lines=" + p_lines.length);
		return null;
	}   //  loadDocumentDetails


	/**
	 *	Load Cash Line
	 *	@param cash journal
	 *	@param cb cash book
	 *  @return DocLine Array
	 */
	private DocLine[] loadLines(MCash cash, MCashBook cb)
	{
		ArrayList<DocLine> list = new ArrayList<DocLine>();
		MCashLine[] lines = cash.getLines(false);
		for (int i = 0; i < lines.length; i++)
		{
			MCashLine line = lines[i];
			DocLine_Cash docLine = new DocLine_Cash (line, this);
			//
			list.add(docLine);
		}

		//	Return Array
		DocLine[] dls = new DocLine[list.size()];
		list.toArray(dls);
		return dls;
	}	//	loadLines

	
	/**************************************************************************
	 *  Get Source Currency Balance - subtracts line amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
	@Override
	public BigDecimal getBalance()
	{
		BigDecimal retValue = Env.ZERO;
		StringBuffer sb = new StringBuffer (" [");
		//  Total
		retValue = retValue.add(getAmount(Doc.AMTTYPE_Gross));
		sb.append(getAmount(Doc.AMTTYPE_Gross));
		//  - Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			retValue = retValue.subtract(p_lines[i].getAmtSource());
			sb.append("-").append(p_lines[i].getAmtSource());
		}
		sb.append("]");
		//
		log.fine(toString() + " Balance=" + retValue + sb.toString());
	//	return retValue;
		return Env.ZERO;    //  Lines are balanced
	}   //  getBalance

	/**
	 *  Create Facts (the accounting logic) for
	 *  CMC.
	 *  <pre>
	 *  Expense
	 *          CashExpense     DR
	 *          CashAsset               CR
	 *  Receipt
	 *          CashAsset       DR
	 *          CashReceipt             CR
	 *  Charge
	 *          Charge          DR
	 *          CashAsset               CR
	 *  Difference
	 *          CashDifference  DR
	 *          CashAsset               CR
	 *  Invoice
	 *          CashAsset       DR
	 *          CashTransfer            CR
	 *  Transfer
	 *          BankInTransit   DR
	 *          CashAsset               CR
	 *  </pre>
	 *  @param as account schema
	 *  @return Fact
	 */
	@Override
	public ArrayList<Fact> createFacts (MAcctSchema as)
	{
		//  Need to have CashBook
		if (getC_CashBook_ID() == 0)
		{
			p_Error = "C_CashBook_ID not set";
			log.log(Level.SEVERE, p_Error);
			return null;
		}

		//  create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);

		//  Header posting amt as Invoices and Transfer could be differenet currency
		//  CashAsset Total
		BigDecimal assetAmt = Env.ZERO;

		//  Lines
		for (int i = 0; i < p_lines.length; i++)
		{
			DocLine_Cash line = (DocLine_Cash)p_lines[i];
			String CashType = line.getCashType();

			if (CashType.equals(DocLine_Cash.CASHTYPE_EXPENSE))
			{   //  amount is negative
				//  CashExpense     DR
				//  CashAsset               CR
				fact.createLine(line, line.getAccount(DocLine_Cash.CASHTYPE_EXPENSE, as),
					getC_Currency_ID(), line.getAmount(), null);
				assetAmt = assetAmt.subtract(line.getAmount());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_RECEIPT))
			{   //  amount is positive
				//  CashAsset       DR
				//  CashReceipt             CR
				assetAmt = assetAmt.add(line.getAmount());
				fact.createLine(line, line.getAccount(DocLine_Cash.CASHTYPE_RECEIPT, as),
					getC_Currency_ID(), null, line.getAmount());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_CHARGE))
			{   //  amount is negative
				//  Charge          DR
				//  CashAsset               CR
				fact.createLine(line, line.getChargeAccount(as, getAmount()),
					getC_Currency_ID(), line.getAmount(),null);
				assetAmt = assetAmt.subtract(line.getAmount());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_DIFFERENCE))
			{   //  amount is pos/neg
				//  CashDifference  DR
				//  CashAsset               CR
				fact.createLine(line, getAccount(Doc.ACCTTYPE_CashDifference, as),
					getC_Currency_ID(),line.getAmount(),null);
				assetAmt = assetAmt.subtract(line.getAmount());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_INVOICE))
			{   //  amount is pos/neg
				//  CashAsset       DR      dr      --   Invoice is in Invoice Currency !
				//  CashTransfer    cr      CR
				if (line.getC_Currency_ID() == getC_Currency_ID())
					assetAmt = assetAmt.add (line.getAmount());
				else
					fact.createLine(line,
						getAccount(Doc.ACCTTYPE_CashAsset, as),
						line.getC_Currency_ID(), line.getAmount());
				fact.createLine(line,
					getAccount(Doc.ACCTTYPE_CashTransfer, as),
					line.getC_Currency_ID(), line.getAmount().negate());
			}
			else if (CashType.equals(DocLine_Cash.CASHTYPE_TRANSFER))
			{   //  amount is pos/neg
				//  BankInTransit   DR      dr      --  Transfer is in Bank Account Currency
				//  CashAsset       dr      CR
				int temp = getC_BankAccount_ID();
				BigDecimal amt;
				setC_BankAccount_ID (line.getC_BankAccount_ID());
				if(line.getC_Currency_ID() != getC_Currency_ID())
				{
					int conversionType = line.getC_ConversionType_ID();
					amt = MConversionRate.convert(getCtx(),line.getAmount(),line.getC_Currency_ID(),
							                      getC_Currency_ID(),getDateAcct(),conversionType,getAD_Client_ID(),
							                      getAD_Org_ID());
				}
				else
					amt=line.getAmount();
					
				fact.createLine(line,
					getAccount(Doc.ACCTTYPE_BankInTransit, as),
					getC_Currency_ID(), amt.negate());
				setC_BankAccount_ID(temp);
				if (line.getC_Currency_ID() == getC_Currency_ID())
					assetAmt = assetAmt.add (line.getAmount());
				else
					fact.createLine(line,
						getAccount(Doc.ACCTTYPE_CashAsset, as),
						getC_Currency_ID(), amt);
			}
		}	//  lines

		//  Cash Asset
		fact.createLine(null, getAccount(Doc.ACCTTYPE_CashAsset, as),
			getC_Currency_ID(), assetAmt);

		//
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		return facts;
	}   //  createFact

}   //  Doc_Cash

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
package org.compiere.report;

import java.math.*;
import java.sql.*;
import java.util.logging.*;

import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *  Report Column Model
 *
 *  @author Jorg Janke
 *  @version $Id: MReportColumn.java,v 1.3 2006/08/03 22:16:52 jjanke Exp $
 */
public class MReportColumn extends X_PA_ReportColumn
{
    /** Logger for class MReportColumn */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MReportColumn.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 	Copy
	 * 	@param ctx context
	 * 	@param AD_Client_ID parent
	 * 	@param AD_Org_ID parent
	 * 	@param PA_ReportColumnSet_ID parent
	 * 	@param source copy source
	 * 	@param trx transaction
	 * 	@return Report Column
	 */
	public static MReportColumn copy (Ctx ctx, int AD_Client_ID, int AD_Org_ID, 
		int PA_ReportColumnSet_ID, MReportColumn source, Trx trx)
	{
		MReportColumn retValue = new MReportColumn (ctx, 0, trx);
		PO.copyValues(source, retValue, AD_Client_ID, AD_Org_ID);
		//
		retValue.setPA_ReportColumnSet_ID(PA_ReportColumnSet_ID);	//	parent
		retValue.setOper_1_ID(0);
		retValue.setOper_2_ID(0);
		return retValue;
	}	//	copy

	
	/**************************************************************************
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param PA_ReportColumn_ID id
	 * 	@param trx transaction
	 */
	public MReportColumn (Ctx ctx, int PA_ReportColumn_ID, Trx trx)
	{
		super (ctx, PA_ReportColumn_ID, trx);
		if (PA_ReportColumn_ID == 0)
		{
			setIsPrinted (true);
			setSeqNo (0);
		}
	}	//	MReportColumn

	/**
	 * 	Constructor
	 * 	@param ctx context
	 * 	@param rs ResultSet to load from
	 * 	@param trx transaction
	 */
	public MReportColumn (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MReportColumn

	/**************************************************************************
	 * 	Get Column SQL Select Clause.
	 * 	@param withSum with SUM() function
	 * 	@return select clause - AmtAcctCR+AmtAcctDR/etc or "null" if not defined
	 */
	public String getSelectClause (boolean withSum)
	{
		//	Amount Type = Period Balance, Period Credit
		String amountType = getAmountType().substring(0,1);	//	first character
		StringBuffer sb = new StringBuffer();
		if (withSum)
			sb.append("SUM(");
		if (AmountType_Balance.equals(amountType))
		//	sb.append("AmtAcctDr-AmtAcctCr");
			sb.append("acctBalance(Account_ID,AmtAcctDr,AmtAcctCr)");
		else if (AmountType_CR.equals(amountType))
			sb.append("AmtAcctCr");
		else if (AmountType_DR.equals(amountType))
			sb.append("AmtAcctDr");
		else if (AmountType_Qty.equals(amountType))
			sb.append("Qty");
		else
		{
			log.log(Level.SEVERE, "AmountType=" + getAmountType () + ", at=" + amountType);
			return "NULL";
		}
		if (withSum)
			sb.append(")");
		return sb.toString();
	}	//	getSelectClause

	/**
	 * 	Is it Period Info ?
	 * 	@return true if Period Amount Type
	 */
	public boolean isPeriod()
	{
		String at = getAmountType();
		if (at == null)
			return false;
		return AMOUNTTYPE_PeriodBalance.equals(at)
			|| AMOUNTTYPE_PeriodCreditOnly.equals(at)
			|| AMOUNTTYPE_PeriodDebitOnly.equals(at)
			|| AMOUNTTYPE_PeriodQuantity.equals(at);
	}	//	isPeriod

	/**
	 * 	Is it Year Info ?
	 * 	@return true if Year Amount Type
	 */
	public boolean isYear()
	{
		String at = getAmountType();
		if (at == null)
			return false;
		return AMOUNTTYPE_YearBalance.equals(at)
			|| AMOUNTTYPE_YearCreditOnly.equals(at)
			|| AMOUNTTYPE_YearDebitOnly.equals(at)
			|| AMOUNTTYPE_YearQuantity.equals(at);
	}	//	isYear

	/**
	 * 	Is it Total Info ?
	 * 	@return true if Year Amount Type
	 */
	public boolean isTotal()
	{
		String at = getAmountType();
		if (at == null)
			return false;
		return AMOUNTTYPE_TotalBalance.equals(at)
			|| AMOUNTTYPE_TotalCreditOnly.equals(at)
			|| AMOUNTTYPE_TotalDebitOnly.equals(at)
			|| AMOUNTTYPE_TotalQuantity.equals(at);
	}	//	isTotalBalance


	/**
	 * 	Get Segment Value Where Clause
	 * 	@param PA_Hierarchy_ID hierarchy
	 * 	@return where clause
	 */
	public String getWhereClause(int PA_Hierarchy_ID)
	{
		if (!isColumnTypeSegmentValue())
			return "";
		
		String et = getElementType();
		int ID = 0;
		if (X_PA_ReportColumn.ELEMENTTYPE_Organization.equals(et))
			ID = getOrg_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_BPartner.equals(et))
			ID = getC_BPartner_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_Product.equals(et))
			ID = getM_Product_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_Project.equals(et))
			ID = getC_Project_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_Activity.equals(et))
			ID = getC_Activity_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_Campaign.equals(et))
			ID = getC_Campaign_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_LocationFrom.equals(et))
			ID = getC_Location_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_LocationTo.equals(et))
			ID = getC_Location_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_OrgTrx.equals(et))
			ID = getOrg_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_SalesRegion.equals(et))
			ID = getC_SalesRegion_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_Account.equals(et))
			ID = getC_ElementValue_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_UserList1.equals(et))
			ID = getC_ElementValue_ID();
		else if (X_PA_ReportColumn.ELEMENTTYPE_UserList2.equals(et))
			ID = getC_ElementValue_ID();
	//	else if (MReportColumn.ELEMENTTYPE_UserElement1.equals(et))
	//		ID = getC_ElementValue_ID();
	//	else if (MReportColumn.ELEMENTTYPE_UserElement2.equals(et))
	//		ID = getC_ElementValue_ID();
		else
			log.warning("Unsupported Element Type=" + et);

		if (ID == 0)
		{
			log.fine("No Restrictions - No ID for EntityType=" + et);
			return "";
		}
		return " AND " + MReportTree.getWhereClause (getCtx(), PA_Hierarchy_ID, et, ID);
	}	//	getWhereClause
	
	/**
	 * 	Get String Representation
	 * 	@return	String Representation
	 */
	@Override
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("MReportColumn[")
			.append(get_ID()).append("-").append(getName()).append("-").append(getDescription())
			.append(",SeqNo=").append(getSeqNo()).append(", AmountType=").append(getAmountType())
			.append(",CurrencyType=").append(getCurrencyType()).append("/").append(getC_Currency_ID())
			.append(",ColumnType=").append(getColumnType());
		if (isColumnTypeCalculation())
			sb.append("-Calculation=").append(getCalculationType())
				.append(":").append(getOper_1_ID()).append(":").append(getOper_2_ID());
		else if (isColumnTypeRelativePeriod())
			sb.append("-Period=").append(getRelativePeriod());
		else
			sb.append("-SegmentValue ElementType=").append(getElementType());
		sb.append ("]");
		return sb.toString ();
	}	//	toString

	static final String		AmountType_Balance = "B";
	static final String		AmountType_CR = "C";
	static final String		AmountType_DR = "D";
	static final String		AmountType_Qty = "Q";
	//
	static final String		AmountType_Period = "P";
	static final String		AmountType_Year = "Y";
	static final String		AmountType_Total = "T";

	/**
	 * 	Calculation Type Range
	 *	@return true if range
	 */
	public boolean isCalculationTypeRange()
	{
		return CALCULATIONTYPE_AddRangeOp1ToOp2.equals(getCalculationType());
	}
	/**
	 * 	Calculation Type Add
	 *	@return true id add
	 */
	public boolean isCalculationTypeAdd()
	{
		return CALCULATIONTYPE_AddOp1PlusOp2.equals(getCalculationType());
	}
	/**
	 * 	Calculation Type Subtract
	 *	@return true if subtract
	 */
	public boolean isCalculationTypeSubtract()
	{
		return CALCULATIONTYPE_SubtractOp1_Op2.equals(getCalculationType());
	}
	/**
	 * 	Calculation Type Percent
	 *	@return true if percent
	 */
	public boolean isCalculationTypePercent()
	{
		return CALCULATIONTYPE_PercentageOp1OfOp2.equals(getCalculationType());
	}


	/**
	 * 	Column Type Calculation
	 *	@return true if calculation
	 */
	public boolean isColumnTypeCalculation()
	{
		return COLUMNTYPE_Calculation.equals(getColumnType());
	}
	/**
	 * 	Column Type Relative Period
	 *	@return true if relative period
	 */
	public boolean isColumnTypeRelativePeriod()
	{
		return COLUMNTYPE_RelativePeriod.equals(getColumnType());
	}
	/**
	 * 	Column Type Segment Value
	 *	@return true if segment value
	 */
	public boolean isColumnTypeSegmentValue()
	{
		return COLUMNTYPE_SegmentValue.equals(getColumnType());
	}
	
	/**
	 * 	Get Relative Period As Int
	 *	@return relative period
	 */
	public int getRelativePeriodAsInt ()
	{
		BigDecimal bd = getRelativePeriod();
		if (bd == null)
			return 0;
		return bd.intValue();
	}	//	getRelativePeriodAsInt

	/**
	 *	Get Relative Period
	 *	@return relative period
	 */
	@Override
	public BigDecimal getRelativePeriod()
	{
		if (getColumnType().equals(COLUMNTYPE_RelativePeriod)
			|| getColumnType().equals(COLUMNTYPE_SegmentValue))
			return super.getRelativePeriod();
		return null;
	}	//	getRelativePeriod
	
	/**
	 *	Get Element Type
	 */
	@Override
	public String getElementType()
	{
		if (getColumnType().equals(COLUMNTYPE_SegmentValue))
			return super.getElementType();
		return null;
	}	//	getElementType
	
	/**
	 *	Get Calculation Type
	 */
	@Override
	public String getCalculationType()
	{
		if (getColumnType().equals(COLUMNTYPE_Calculation))
			return super.getCalculationType();
		return null;
	}	//	getCalculationType
	
	/**
	 *	Before Save
	 *	@param newRecord new
	 *	@return true
	 */
	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		//	Validate Type
		String ct = getColumnType();
		if (ct.equals(COLUMNTYPE_RelativePeriod))
		{
			setElementType(null);
			setCalculationType(null);
		}
		else if (ct.equals(COLUMNTYPE_Calculation))
		{
			setElementType(null);
			setRelativePeriod(null);
		}
		else if (ct.equals(COLUMNTYPE_SegmentValue))
		{
			setCalculationType(null);
		}
		return true;
	}	//	beforeSave

}	//	MReportColumn

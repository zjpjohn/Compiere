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
package org.compiere.process;

import java.math.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Copy Product Catergory Default Accounts
 *	
 *  @author Jorg Janke
 *  @version $Id: ProductCategoryAcctCopy.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class ProductCategoryAcctCopy extends SvrProcess
{
	/** Product Categpory			*/
	private int 		p_M_Product_Category_ID = 0;
	/**	Acct Schema					*/
	private int			p_C_AcctSchema_ID = 0;


	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else if (name.equals("M_Product_Category_ID"))
				p_M_Product_Category_ID = element.getParameterAsInt();
			else if (name.equals("C_AcctSchema_ID"))
				p_C_AcctSchema_ID = element.getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	@Override
	protected String doIt () throws Exception
	{
		log.info("C_AcctSchema_ID=" + p_C_AcctSchema_ID);
		if (p_C_AcctSchema_ID == 0)
			throw new CompiereSystemException("C_AcctSchema_ID=0");
		MAcctSchema as = MAcctSchema.get(getCtx(), p_C_AcctSchema_ID);
		if (as.get_ID() == 0)
			throw new CompiereSystemException("Not Found - C_AcctSchema_ID=" + p_C_AcctSchema_ID);

		//	Update
		String sql = "UPDATE M_Product_Acct pa "
			+ "SET (P_Revenue_Acct,P_Expense_Acct,P_CostAdjustment_Acct,P_InventoryClearing_Acct,P_Asset_Acct,P_COGS_Acct,"
			+ " P_PurchasePriceVariance_Acct,P_InvoicePriceVariance_Acct,"
			+ " P_TradeDiscountRec_Acct,P_TradeDiscountGrant_Acct," 
			+ " P_Resource_Absorption_Acct, P_MaterialOverhd_Acct)="
			 + " (SELECT P_Revenue_Acct,P_Expense_Acct,P_CostAdjustment_Acct,P_InventoryClearing_Acct,P_Asset_Acct,P_COGS_Acct,"
			 + " P_PurchasePriceVariance_Acct,P_InvoicePriceVariance_Acct,"
			 + " P_TradeDiscountRec_Acct,P_TradeDiscountGrant_Acct,"
			 + " P_Resource_Absorption_Acct, P_MaterialOverhd_Acct"
			 + " FROM M_Product_Category_Acct pca"
			 + " WHERE pca.M_Product_Category_ID=" + p_M_Product_Category_ID
			 + " AND pca.C_AcctSchema_ID= pa.C_AcctSchema_ID "
			 + "), Updated=SysDate, UpdatedBy=0 "
			+ "WHERE pa.C_AcctSchema_ID= ? " 
			+ " AND EXISTS (SELECT * FROM M_Product p "
				+ "WHERE p.M_Product_ID=pa.M_Product_ID"
				+ " AND p.M_Product_Category_ID= ? )";
		int updated = DB.executeUpdate(get_TrxName(), sql,p_C_AcctSchema_ID,p_M_Product_Category_ID);
		addLog(0, null, new BigDecimal(updated), "@Updated@");

		//	Insert new Products
		sql = "INSERT INTO M_Product_Acct "
			+ "(M_Product_ID, C_AcctSchema_ID,"
			+ " AD_Client_ID, AD_Org_ID, IsActive, Created, CreatedBy, Updated, UpdatedBy,"
			+ " P_Revenue_Acct, P_Expense_Acct, P_CostAdjustment_Acct, P_InventoryClearing_Acct, P_Asset_Acct, P_CoGs_Acct,"
			+ " P_PurchasePriceVariance_Acct, P_InvoicePriceVariance_Acct,"
			+ " P_TradeDiscountRec_Acct, P_TradeDiscountGrant_Acct," 
			+ " P_Resource_Absorption_Acct, P_MaterialOverhd_Acct) "
			+ "SELECT p.M_Product_ID, acct.C_AcctSchema_ID,"
			+ " p.AD_Client_ID, p.AD_Org_ID, 'Y', SysDate, 0, SysDate, 0,"
			+ " acct.P_Revenue_Acct, acct.P_Expense_Acct, acct.P_CostAdjustment_Acct, acct.P_InventoryClearing_Acct, acct.P_Asset_Acct, acct.P_CoGs_Acct,"
			+ " acct.P_PurchasePriceVariance_Acct, acct.P_InvoicePriceVariance_Acct,"
			+ " acct.P_TradeDiscountRec_Acct, acct.P_TradeDiscountGrant_Acct," 
			+ " acct.P_Resource_Absorption_Acct, acct.P_MaterialOverhd_Acct "
			+ "FROM M_Product p"
			+ " INNER JOIN M_Product_Category_Acct acct ON (acct.M_Product_Category_ID=p.M_Product_Category_ID)"
			+ "WHERE acct.C_AcctSchema_ID= ? " 			//	#
			+ " AND p.M_Product_Category_ID= ? " //	#
			+ " AND NOT EXISTS (SELECT * FROM M_Product_Acct pa "
				+ "WHERE pa.M_Product_ID=p.M_Product_ID"
				+ " AND pa.C_AcctSchema_ID=acct.C_AcctSchema_ID)";
		int created = DB.executeUpdate(get_TrxName(), sql,p_C_AcctSchema_ID ,p_M_Product_Category_ID);
		addLog(0, null, new BigDecimal(created), "@Created@");

		return "@Created@=" + created + ", @Updated@=" + updated;
	}	//	doIt
	
}	//	ProductCategoryAcctCopy

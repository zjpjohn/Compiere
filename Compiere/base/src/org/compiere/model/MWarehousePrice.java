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
package org.compiere.model;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

import org.compiere.util.*;

/**
 *	Product Warehouse Availability and Price Model.
 *	The Ownership (Client, Org) is determined by the Warehouse
 *	Active is determined if the product is discontinued (the product/price/warehouse need to be active)
 *	Created.. is determined by the price list version 
 *	
 *  @author Jorg Janke
 *  @version $Id: MWarehousePrice.java,v 1.3 2006/07/30 00:51:02 jjanke Exp $
 */
public class MWarehousePrice extends X_RV_WarehousePrice
{
    /** Logger for class MWarehousePrice */
    private static final org.compiere.util.CLogger log = org.compiere.util.CLogger.getCLogger(MWarehousePrice.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 	Find Products in Warehouse with Price
	 * 	@param ctx context
	 *	@param M_PriceList_Version_ID mandatory price list
	 *	@param M_Warehouse_ID mandatory warehouse
	 *	@param Value optional value
	 *	@param Name optional name
	 *	@param UPC optional full match upc
	 *	@param SKU optional full match ski
	 *	@param trx transaction
	 *	@return array of product prices and warehouse availability
	 */
	public static MWarehousePrice[] find (Ctx ctx,
		int M_PriceList_Version_ID, int M_Warehouse_ID,
		String Value, String Name, String UPC, String SKU, Trx trx)
	{
		StringBuffer sql = new StringBuffer ("SELECT * FROM RV_WarehousePrice "
			+ "WHERE M_PriceList_Version_ID=? AND M_Warehouse_ID=?");
		StringBuffer sb = new StringBuffer();
		Value = getFindParameter (Value);
		if (Value != null)
			sb.append("UPPER(Value) LIKE ?");
		Name = getFindParameter (Name);
		if (Name != null)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPPER(Name) LIKE ?");
		}
		if (UPC != null && UPC.length() > 0)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("UPC=?");
		}
		if (SKU != null && SKU.length() > 0)
		{
			if (sb.length() > 0)
				sb.append(" OR ");
			sb.append("SKU=?");
		}
		if (sb.length() > 0)
			sql.append(" AND (").append(sb).append(")");
		sql.append(" ORDER BY Value");
		//
		String finalSQL = MRole.getDefault(ctx, false).addAccessSQL(sql.toString(), 
			"RV_WarehousePrice", MRole.SQL_NOTQUALIFIED, MRole.SQL_RO);
		s_log.fine("find - M_PriceList_Version_ID=" + M_PriceList_Version_ID 
			+ ", M_Warehouse_ID=" + M_Warehouse_ID
			+ " - " + finalSQL);
		ArrayList<MWarehousePrice> list = new ArrayList<MWarehousePrice>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(finalSQL, trx);
			int index = 1;
			pstmt.setInt(index++, M_PriceList_Version_ID);
			pstmt.setInt(index++, M_Warehouse_ID);
			if (Value != null)
				pstmt.setString(index++, Value);
			if (Name != null)
				pstmt.setString(index++, Name);
			if (UPC != null && UPC.length() > 0)
				pstmt.setString(index++, UPC);
			if (SKU != null && SKU.length() > 0)
				pstmt.setString(index++, SKU);
			rs = pstmt.executeQuery();
			while (rs.next())
				list.add(new MWarehousePrice(ctx, rs, trx));
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, finalSQL, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		//
		s_log.fine("find - #" + list.size());
		MWarehousePrice[] retValue = new MWarehousePrice[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	find

	/**
	 * 	Find Products in Warehouse with Price for customer
	 * 	@param bPartner business partner
	 *	@param IsSOTrx if true SO
	 *	@param valid the date the price must be valid
	 *	@param M_Warehouse_ID mandatory warehouse
	 *	@param Value optional value
	 *	@param Name optional name
	 *	@param UPC optional upc
	 *	@param SKU optional ski
	 *	@param trx transaction
	 *	@return array of product prices and warehouse availability or null
	 */
	public static MWarehousePrice[] find (MBPartner bPartner,
		boolean IsSOTrx, Timestamp valid, int M_Warehouse_ID,
		String Value, String Name, String UPC, String SKU, Trx trx)
	{
		int M_PriceList_ID = IsSOTrx ? bPartner.getM_PriceList_ID() : bPartner.getPO_PriceList_ID();
		MPriceList pl = null;
		if (M_PriceList_ID == 0)
			pl = MPriceList.getDefault(bPartner.getCtx(), IsSOTrx);
		else
			pl = MPriceList.get(bPartner.getCtx(), M_PriceList_ID, trx);
		if (pl == null)
		{
			s_log.severe ("No PriceList found");
			return null;
		}
		MPriceListVersion plv = pl.getPriceListVersion (valid);
		if (plv == null)
		{
			s_log.severe ("No PriceListVersion found for M_PriceList_ID=" + pl.getM_PriceList_ID());
			return null;
		}
		//
		return find (bPartner.getCtx(), plv.getM_PriceList_Version_ID(), M_Warehouse_ID,
			Value, Name, UPC, SKU, trx);
	}	//	find

	/**
	 * 	Get MWarehouse Price
	 *	@param product product
	 *	@param M_PriceList_Version_ID plv
	 *	@param M_Warehouse_ID wh
	 *	@param trx transaction
	 *	@return warehouse price
	 */
	public static MWarehousePrice get (MProduct product,
		int M_PriceList_Version_ID, int M_Warehouse_ID, Trx trx)
	{
		MWarehousePrice retValue = null;
		String sql = "SELECT * FROM RV_WarehousePrice "
			+ "WHERE M_Product_ID=? AND M_PriceList_Version_ID=? AND M_Warehouse_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (sql, trx);
			pstmt.setInt (1, product.getM_Product_ID());
			pstmt.setInt(2, M_PriceList_Version_ID);
			pstmt.setInt(3, M_Warehouse_ID);
			rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MWarehousePrice(product.getCtx(), rs, trx);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.closeResultSet(rs);
			DB.closeStatement(pstmt);
		}
		return retValue;
	}	//	get

	/** Static Logger					*/
	private static CLogger 	s_log = CLogger.getCLogger(MWarehousePrice.class);

	
	/*************************************************************************
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trx transaction
	 */
	public MWarehousePrice (Ctx ctx, ResultSet rs, Trx trx)
	{
		super(ctx, rs, trx);
	}	//	MWarehousePrice
	
	/**
	 * 	Is Product Available
	 *	@return true if available qty > 0
	 */
	public boolean isAvailable()
	{
		return getQtyAvailable().signum() == 1;	//	> 0
	}	//	isAvailable

}	//	MWarehousePrice

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
package org.compiere.util;

import java.io.*;
import java.util.*;

/**
 *	Compiere Statement Value Object
 *	
 *  @author Jorg Janke
 *  @version $Id: CStatementVO.java 8244 2009-12-04 23:25:29Z freyes $
 */
public class CStatementVO implements Serializable
{
	/**
	 * 	VO Constructor
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 */
	public CStatementVO (int resultSetType, int resultSetConcurrency)
	{
		setResultSetType(resultSetType);
		setResultSetConcurrency(resultSetConcurrency);
	}	//	CStatementVO

	/**
	 * 	VO Constructor
	 *  @param resultSetType - ResultSet.TYPE_FORWARD_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_SCROLL_SENSITIVE
	 *  @param resultSetConcurrency - ResultSet.CONCUR_READ_ONLY or ResultSet.CONCUR_UPDATABLE
	 * 	@param sql sql
	 */
	public CStatementVO (int resultSetType, int resultSetConcurrency, String sql)
	{
		this (resultSetType, resultSetConcurrency);
		setSql(sql);
	}	//	CStatementVO

	/**	Serialization Info	**/
	static final long serialVersionUID = -3393389471515956399L;
	
	/**	Type			*/
	private int					m_resultSetType;
	/** Concurrency		*/
	private int 				m_resultSetConcurrency;
	/** SQL Statement	*/
	private String 				m_sql;
	/** Parameters		*/
	private ArrayList<Object>	m_parameters = new ArrayList<Object>();

	/**
	 * 	String representation
	 * 	@return info
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("CStatementVO[");
		sb.append(getSql());
		for (int i = 0; i < m_parameters.size(); i++)
			sb.append("; #").append(i+1).append("=").append(m_parameters.get(i));
		sb.append("]");
		return sb.toString();
	}	//	toString

	/**
	 * 	Set Parameter
	 * 	@param index1 1 based index
	 * 	@param element element
	 */
	public void setParameter (int index1, Object element)
	{
		if (element != null && !(element instanceof Serializable))
			throw new java.lang.RuntimeException("setParameter not Serializable - " + element.getClass().toString());
		int zeroIndex = index1 - 1;
		if (m_parameters.size() == zeroIndex)
		{
			m_parameters.add(element);
		}
		else if (m_parameters.size() < zeroIndex)
		{
			while (m_parameters.size() < zeroIndex)
				m_parameters.add (null);	//	fill with nulls
			m_parameters.add(element);
		}
		else
			m_parameters.set(zeroIndex, element);
	}	//	setParametsr

	/**
	 *	Clear Parameters
	 */
	public void clearParameters()
	{
		m_parameters = new ArrayList<Object>();
	}	//	clearParameters

	/**
	 * 	Get Parameters
	 *	@return array list
	 */
	public ArrayList<?> getParameters()
	{
		return m_parameters;
	}	//	getParameters
	
	/**
	 * 	Get Parameter Count
	 *	@return array list
	 */
	public int getParameterCount()
	{
		return m_parameters.size();
	}	//	getParameterCount


	/**
	 * 	Get SQL
	 * 	@return sql
	 */
	public String getSql()
	{
		return m_sql;
	}	//	getSql

	/**
	 * 	Set SQL.
	 * 	Replace ROWID with TRIM(ROWID) for remote SQL
	 * 	to convert into String as ROWID is not serialized
	 *	@param sql sql
	 */
	public void setSql(String sql)
	{
		if (sql != null && DB.isRemoteObjects())
		{
			//	Handle RowID in the select part (not where clause)
			int pos = sql.indexOf("ROWID");
			int posTrim = sql.indexOf("TRIM(ROWID)");
			int posWhere = sql.indexOf("WHERE");
			if (pos != -1 && posTrim == -1 && (posWhere == -1 || pos < posWhere))
				m_sql = sql.substring(0, pos) + "TRIM(ROWID)" + sql.substring(pos+5);
			else
				m_sql = sql;
		}
		else
			m_sql = sql;
	}	//	setSql

	/**
	 * 	Get ResultSet Concurrency
	 *	@return rs concurrency
	 */
	public int getResultSetConcurrency()
	{
		return m_resultSetConcurrency;
	}
	/**
	 * 	Get ResultSet Type
	 *	@return rs type
	 */
	public int getResultSetType()
	{
		return m_resultSetType;
	}
	/**
	 * 	Set ResultSet Type
	 *	@param resultSetType type
	 */
	public void setResultSetType(int resultSetType)
	{
		m_resultSetType = resultSetType;
	}
	/**
	 * 	Set ResultSet Concurrency
	 *	@param resultSetConcurrency concurrency
	 */
	public void setResultSetConcurrency(int resultSetConcurrency)
	{
		m_resultSetConcurrency = resultSetConcurrency;
	}

}	//	CStatementVO

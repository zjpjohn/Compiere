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

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.logging.*;

import javax.sql.*;

import org.compiere.model.*;
import org.compiere.util.*;

/**
 * 	Remote Data Replication.
 * 	Note: requires migration technology
 *
 *  @author Jorg Janke
 *  @version $Id: ReplicationRemote.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class ReplicationRemote extends SvrProcess
{
	/**	System Record			*/
	private	MSystem			m_system = null;
	/** Local Timestamp			*/
	private Timestamp		m_startDate = new Timestamp(System.currentTimeMillis());

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	@Override
	public void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (ProcessInfoParameter element : para) {
			String name = element.getParameterName();
			if (element.getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		m_system = MSystem.get (getCtx());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
	@Override
	public String doIt() throws Exception
	{
		ProcessInfo pi = getProcessInfo();
		log.info("doIt - " + pi.getTitle());
		if (pi.getSerializableObject() instanceof RemoteSetupVO)
			return setupRemote();
		else if (pi.getSerializableObject() instanceof RemoteMergeDataVO)
			return mergeDataWithCentral();
		else if (pi.getSerializableObject() instanceof RemoteUpdateVO)
			return receiveUpdateFromCentral();
		else if (pi.getSerializableObject() instanceof Timestamp)
			return exit();
		else
			throw new Exception ("ReplicationRemote - unknown VO - " + pi.getSerializableObject());
	}	//	doIt

	/*************************************************************************

	/**
	 *	Setup Remote AD_System/AD_Table/AD_Sequence for Remote Management.
	 * 	@return "" or error message
	 *	@throws Exception
	 */
	private String setupRemote() throws Exception
	{
		ProcessInfo pi = getProcessInfo();
		RemoteSetupVO data = (RemoteSetupVO)pi.getSerializableObject();
		log.info("setupRemote Start (" + pi + ") " + data);

		RowSet rs = data.ReplicationTable;
		try
		{
			//	Update AD_System	****
			if (data.IDRangeStart == null || data.IDRangeEnd == null)
				throw new Exception ("setupRemote - IDRange cannot be null");
			if (!data.Test.booleanValue())
			{
				setupRemoteAD_System (data.IDRangeStart, data.IDRangeEnd);
				//	Update AD_Sequence	****
				setupRemoteAD_Sequence (data.IDRangeStart);
				//	Update DocNo Prefix/Suffix	****
				setupRemoteC_DocType (data.AD_Client_ID, data.Prefix, data.Suffix);
			}
			//	Update Tables	****
			while (rs.next ())
			{
				
				String ReplicationType = rs.getString(2);
				String TableName = rs.getString(3);
				log.fine("setupRemote - " + TableName + " - " + ReplicationType);
				if (!data.Test.booleanValue())
					setupRemoteAD_Table(TableName, ReplicationType);
			}
			pi.setSummary("OK - Test=" + data.Test, false);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "setupRemote", ex);
			pi.setSummary(ex.toString(), true);
		}
		Object result = doIt(ReplicationLocal.START, "init", new Object[]{m_system});
		if (result == null || !Boolean.TRUE.equals(result))
			throw new Exception("setupRemote - Init Error - " + result);
		pi.setSerializableObject(null);
		pi.addLog(0,0, m_startDate, null, null);
		log.info("setupRemote End (" + pi + ") " + data);
		return "Remote SetupRemote OK";
	}	//	setupRemote

	/**
	 * 	Update Replication Type and ID Range of AD_System
	 *	@param IDRangeStart start
	 *	@param IDRangeEnd end
	 * 	@throws Exception if sql error
	 */
	private void setupRemoteAD_System (BigDecimal IDRangeStart, BigDecimal IDRangeEnd) throws Exception
	{
		m_system.setIDRangeStart(IDRangeStart);
		m_system.setIDRangeEnd(IDRangeEnd);
		m_system.setReplicationType(X_AD_System.REPLICATIONTYPE_Merge);
		m_system.save();
	}	//	setupRemoteAD_System

	/**
	 * 	Update StartNo/CurrentNext/CurrentNextSys in AD_Sequence
	 *	@param IDRangeStart start
	 * 	@throws Exception if sql error
	 */
	private void setupRemoteAD_Sequence (BigDecimal IDRangeStart) throws Exception
	{
		String sql = "UPDATE AD_Sequence SET StartNo = " + IDRangeStart
			+ " WHERE IsTableID='Y' AND StartNo < " + IDRangeStart;
		int no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new Exception("setupRemoteAD_Sequence_Start");
		//
		sql = "UPDATE AD_Sequence SET CurrentNext = " + IDRangeStart
			+ " WHERE IsTableID='Y' AND CurrentNext < " + IDRangeStart;
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new Exception("setupRemoteAD_Sequence_Next");
		//
		sql = "UPDATE AD_Sequence SET CurrentNextSys = -1"
			+ " WHERE IsTableID='Y' AND CurrentNextSys <> -1";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new Exception("setupRemoteAD_Sequence_Sys");
	}	//	setupRemoteAD_Sequence

	/**
	 * 	Update Document Prefix/Suffix
	 *	@param AD_Client_ID client
	 *	@param Prefix prefix
	 *	@param Suffix suffix
	 * 	@throws Exception if sql error
	 */
	private void setupRemoteC_DocType (int AD_Client_ID, String Prefix, String Suffix) throws Exception
	{
		if (Prefix == null)
			Prefix = "";
		if (Suffix == null)
			Suffix = "";
		//	DocNoSequence_ID
		String sql = "UPDATE AD_Sequence SET Prefix=" + DB.TO_STRING(Prefix) + ", Suffix=" + DB.TO_STRING(Suffix)
			+ " WHERE AD_Sequence_ID IN (SELECT DocNoSequence_ID FROM C_DocType"
			+ " WHERE AD_Client_ID=" + AD_Client_ID + " AND DocNoSequence_ID IS NOT NULL)";
		int no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new Exception("setupRemoteC_DocType_DocNo");
		//	 BatchNoSequence_ID
		sql = "UPDATE AD_Sequence SET Prefix=" + DB.TO_STRING(Prefix) + ", Suffix=" + DB.TO_STRING(Suffix)
			+ " WHERE AD_Sequence_ID IN (SELECT BatchNoSequence_ID FROM C_DocType"
			+ " WHERE AD_Client_ID=" + AD_Client_ID + " AND BatchNoSequence_ID IS NOT NULL)";
		no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new Exception("setupRemoteC_DocType_Batch");
	}	//	setupRemoteC_DocType

	/**
	 * 	Set ReplicationType of AD_Table
	 *	@param TableName table name
	 *	@param ReplicationType replication type
	 * 	@throws Exception if sql error
	 */
	private void setupRemoteAD_Table(String TableName, String ReplicationType) throws Exception
	{
		String sql = "UPDATE AD_Table SET ReplicationType = '" + ReplicationType
			+ "' WHERE TableName='" + TableName + "' AND ReplicationType <> '" + ReplicationType + "'";
		int no = DB.executeUpdate(get_TrxName(), sql);
		if (no == -1)
			throw new Exception("setupRemoteAD_Table");
	}	//	setupRemoteAD_Table

	/*************************************************************************

	/**
	 * 	Send new Data from Remote.
	 * 	Transcation Data changed
	 * 	@return info
	 * 	@throws Exception
	 */
	private String mergeDataWithCentral() throws Exception
	{
		ProcessInfo pi = getProcessInfo();
		RemoteMergeDataVO data = (RemoteMergeDataVO)pi.getSerializableObject();
		log.info("mergeDataWithCentral Start (" + pi + ") " + data);
		//	Merge Data from Central
		RowSet sourceRS = data.CentralData;
		if (sourceRS == null)
			log.log(Level.SEVERE, "mergeDataWithCentral - No Data - " + data);
		else if (!data.Test.booleanValue())
		{
			RowSet targetRS = ReplicationLocal.getRowSet(data.Sql, null);
			Object result = doIt (ReplicationLocal.START, "sync", new Object[]	//	Merge
				{data.TableName, data.KeyColumns, sourceRS, targetRS, data.Test, Boolean.TRUE});
			log.fine("receiveUpdateFromCentral - " + data.TableName + " - " + result);
			pi.setSerializableObject(null);
			boolean replicated = ReplicationLocal.isReplicated(result);
			if (!replicated)
				pi.setError(true);
			if (result != null)
				pi.addLog(0,null,null, result.toString());
			if (Boolean.FALSE.equals(result))
				throw new Exception ("receiveUpdateFromCentral - " + data.TableName + " - " + result);
		}

		//	Local Remote Data
		RowSet rowset = ReplicationLocal.getRowSet(data.Sql, null);
		//	Result
		pi.setSerializableObject((Serializable)rowset);
		log.info("mergeDataWithCentral End (" + pi + ") " + data);
		return "Remote MergeDataWithCentral - " + data.TableName;
	}	//	sendNewDataToCentral

	/*************************************************************************

	/**
	 * 	Receive Update from Central
	 * 	@return info
	 * 	@throws Exception
	 */
	private String receiveUpdateFromCentral() throws Exception
	{
		ProcessInfo pi = getProcessInfo();
		RemoteUpdateVO data = (RemoteUpdateVO)pi.getSerializableObject();
		log.info("receiveUpdateFromCentral Start (" + pi + ") - " + data);
		//
		RowSet sourceRS = data.CentralData;
		if (sourceRS == null)
		{
			log.log(Level.SEVERE, "receiveUpdateFromCentral - No Data - " + data);
			pi.setSummary("NoData", true);
		}
		else if (!data.Test.booleanValue())
		{
			RowSet targetRS = ReplicationLocal.getRowSet(data.Sql, null);
			Object result = doIt (ReplicationLocal.START, "sync", new Object[]	//	Sync
				{data.TableName, data.KeyColumns, sourceRS, targetRS, data.Test, Boolean.FALSE});
			log.fine("receiveUpdateFromCentral - " + data.TableName + " - " + result);
			pi.setSerializableObject(null);
			boolean replicated = ReplicationLocal.isReplicated(result);
			if (!replicated)
				pi.setError(true);
			if (result != null)
				pi.addLog(0,null,null, result.toString());
			if (Boolean.FALSE.equals(result))
				throw new Exception ("receiveUpdateFromCentral - " + data.TableName + " - " + result);
		}
		//
		pi.setSerializableObject(null);
		log.info("receiveUpdateFromCentral End (" + pi + ") - " + data);
		return "Remote Receive Update from Central OK";
	}	//	receiveUpdateFromCentral

	/*************************************************************************/

	/**
	 * 	Clean up resources (connections)
	 * 	@return exit
	 * 	@throws Exception
	 */
	private String exit() throws Exception
	{
		log.info ("exit");
		doIt(ReplicationLocal.START, "exit", null);
		return "exit";
	}	//	exit

}	//	ReplicationRemote

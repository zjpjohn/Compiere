/*
 * Generated by XDoclet - Do not edit!
 */
package org.compiere.interfaces;

/**
 * Local interface for compiere/Server.
 */
public interface ServerLocal
   extends javax.ejb.EJBLocalObject
{
   /**
    * Get and create Window Model Value Object
    * @param ctx Environment Properties
    * @param WindowNo number of this window
    * @param AD_Window_ID the internal number of the window, if not 0, AD_Menu_ID is ignored
    * @param AD_Menu_ID ine internal menu number, used when AD_Window_ID is 0
    * @return initialized Window Model    */
   public org.compiere.model.GridWindowVO getWindowVO( org.compiere.util.Ctx ctx,int WindowNo,int AD_Window_ID,int AD_Menu_ID ) ;

   /**
    * Post Immediate
    * @param ctx Client Context
    * @param AD_Client_ID Client ID of Document
    * @param AD_Table_ID Table ID of Document
    * @param Record_ID Record ID of this document
    * @param force force posting
    * @param trx transaction
    * @return null, if success or error message    */
   public java.lang.String postImmediate( org.compiere.util.Ctx ctx,int AD_Client_ID,int AD_Table_ID,int Record_ID,boolean force,org.compiere.util.Trx trx ) ;

   /**
    * Get Prepared Statement ResultSet
    * @param info Result info
    * @return RowSet
    * @throws NotSerializableException    */
   public javax.sql.RowSet pstmt_getRowSet( org.compiere.util.CStatementVO info ) throws java.io.NotSerializableException;

   /**
    * Get Statement ResultSet
    * @param info Result info
    * @return RowSet    */
   public javax.sql.RowSet stmt_getRowSet( org.compiere.util.CStatementVO info ) ;

   /**
    * Execute Update
    * @param info Result info
    * @return row count    */
   public int stmt_executeUpdate( org.compiere.util.CStatementVO info ) ;

   /**
    * Get next number for Key column = 0 is Error.
    * @param AD_Client_ID client
    * @param TableName table name
    * @param trx optional Transaction Name
    * @return next no    */
   public int getNextID( int AD_Client_ID,java.lang.String TableName,org.compiere.util.Trx trx ) ;

   /**
    * Get Document No from table
    * @param AD_Client_ID client
    * @param TableName table name
    * @param trx optional Transaction Name
    * @return document no or null    */
   public java.lang.String getDocumentNo( int AD_Client_ID,java.lang.String TableName,org.compiere.util.Trx trx ) ;

   /**
    * Get Document No based on Document Type
    * @param C_DocType_ID document type
    * @param trx optional Transaction Name
    * @return document no or null    */
   public java.lang.String getDocumentNo( int C_DocType_ID,org.compiere.util.Trx trx ) ;

   /**
    * Process Remote
    * @param ctx Context
    * @param pi Process Info
    * @return resulting Process Info    */
   public org.compiere.process.ProcessInfo process( org.compiere.util.Ctx ctx,org.compiere.process.ProcessInfo pi ) ;

   /**
    * Run Workflow (and wait) on Server
    * @param ctx Context
    * @param pi Process Info
    * @param AD_Workflow_ID id
    * @return process info    */
   public org.compiere.process.ProcessInfo workflow( org.compiere.util.Ctx ctx,org.compiere.process.ProcessInfo pi,int AD_Workflow_ID ) ;

   /**
    * Online Payment from Server
    * @param ctx Context
    * @param C_Payment_ID payment
    * @param C_PaymentProcessor_ID processor
    * @param trx transaction
    * @return true if approved    */
   public boolean paymentOnline( org.compiere.util.Ctx ctx,int C_Payment_ID,int C_PaymentProcessor_ID,org.compiere.util.Trx trx ) ;

   /**
    * Create EMail from Server (Request User)
    * @param ctx Context
    * @param AD_Client_ID client
    * @param toEMail recipient email address
    * @param subject subject
    * @param message message
    * @return EMail    */
   public org.compiere.util.EMail createEMail( org.compiere.util.Ctx ctx,int AD_Client_ID,java.lang.String toEMail,java.lang.String toName,java.lang.String subject,java.lang.String message ) ;

   /**
    * Send EMail from Server
    * @param Email email
    * @param String user
    * @param String password
    * @return String    */
   public java.lang.String sendEMail( org.compiere.util.EMail email,java.lang.String user,java.lang.String password ) ;

   /**
    * Create EMail from Server (Request User)
    * @param ctx Context
    * @param AD_Client_ID client
    * @param AD_User_ID user to send email from
    * @param toEMail recipient email address
    * @param subject subject
    * @param message message
    * @return EMail or null    */
   public org.compiere.util.EMail createEMail( org.compiere.util.Ctx ctx,int AD_Client_ID,int AD_User_ID,java.lang.String toEMail,java.lang.String toName,java.lang.String subject,java.lang.String message ) ;

   /**
    * Create EMail from Server (Request User)
    * @param AD_Task_ID task
    * @return execution trace    */
   public java.lang.String executeTask( int AD_Task_ID ) ;

   /**
    * Cash Reset
    * @param tableName table name
    * @param Record_ID record or 0 for all
    * @return number of records reset    */
   public int cacheReset( java.lang.String tableName,int Record_ID ) ;

   /**
    * Restart Application Servers/Processes
    * @return true if restarted    */
   public boolean restartProcesses(  ) ;

   /**
    * LOB update
    * @param sql table name
    * @param displayType display type (i.e. BLOB/CLOB)
    * @param value the data
    * @return true if updated    */
   public boolean updateLOB( java.lang.String sql,int displayType,java.lang.Object value ) ;

   /**
    * Describes the instance and its content for debugging purpose
    * @return Debugging information about the instance and its content    */
   public java.lang.String getStatus(  ) ;

}

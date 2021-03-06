# $Id: DBRestore.sh,v 1.1 2006/01/11 06:55:49 jjanke Exp $
echo	Compiere Database Restore 	$Revision: 1.1 $

echo	Restoring Compiere DB from $COMPIERE_HOME/data/ExpDat.dmp

if [ $# -le 2 ] 
  then
    echo "Usage:		$0 <systemAccount> <CompiereID> <CompierePWD>"
    echo "Example:	$0 system/manager compiere compiere"
    exit 1
fi
if [ "$COMPIERE_HOME" = "" -o  "$COMPIERE_DB_NAME" = "" ]
  then
    echo "Please make sure that the environment variables are set correctly:"
    echo "	COMPIERE_HOME	e.g. /Compiere2"
    echo "	COMPIERE_DB_NAME	e.g. compiere.compiere.org"
    exit 1
fi


echo -------------------------------------
echo Re-Create DB user
echo -------------------------------------
sqlplus $1@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/CreateUser.sql $2 $3

echo -------------------------------------
echo Import ExpDat
echo -------------------------------------
imp $1@$COMPIERE_DB_NAME FILE=$COMPIERE_HOME/data/ExpDat.dmp FROMUSER=\($2\) TOUSER=$2 

echo -------------------------------------
echo Create SQLJ 
echo -------------------------------------
$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/create.sh $COMPIERE_DB_USER/$COMPIERE_DB_PASSWORD

echo -------------------------------------
echo Check System
echo Import may show some warnings. This is OK as long as the following does not show errors
echo -------------------------------------
sqlplus $2/$3@$COMPIERE_DB_NAME @$COMPIERE_HOME/utils/$COMPIERE_DB_PATH/AfterImport.sql

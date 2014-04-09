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
package org.compiere.translate;

import java.util.*;

/**
 *  Base Resource Bundle
 *
 * 	@version 	$Id: ALoginRes_da.java 8394 2010-02-04 22:55:56Z freyes $
 */
public final class ALoginRes_da extends ListResourceBundle
{
	// TODO Run native2ascii to convert to plain ASCII !! 
	
	/** Translation Content     */
	static final Object[][] contents = new String[][]
	{
	{ "Connection",         "Forbindelse" },
	{ "Defaults",           "Basis" },
	{ "Login",              "Compiere: Log p\u00c3\u00a5" },
	{ "File",               "Fil" },
	{ "Exit",               "Afslut" },
	{ "Help",               "Hj\u00c3\u00a6lp" },
	{ "About",              "Om" },
	{ "Host",               "V\u00c3\u00a6rt" },
	{ "Database",           "Database" },
	{ "User",               "Bruger-ID" },
	{ "EnterUser",          "Angiv bruger-ID til program" },
	{ "Password",           "Adgangskode" },
	{ "EnterPassword",      "Angiv adgangskode til program" },
	{ "Language",           "Sprog" },
	{ "SelectLanguage",     "V\u00c3\u00a6lg sprog" },
	{ "Role",               "Rolle" },
	{ "Client",             "Firma" },
	{ "Organization",       "Organisation" },
	{ "Date",               "Dato" },
	{ "Warehouse",          "Lager" },
	{ "Printer",            "Printer" },
	{ "Connected",          "Forbindelse OK" },
	{ "NotConnected",       "Ingen forbindelse" },
	{ "DatabaseNotFound",   "Database blev ikke fundet" },
	{ "UserPwdError",       "Forkert bruger til adgangskode" },
	{ "RoleNotFound",       "Rolle blev ikke fundet/afsluttet" },
	{ "Authorized",         "Tilladelse OK" },
	{ "Ok",                 "OK" },
	{ "Cancel",             "Annull\u00c3\u00a9r" },
	{ "VersionConflict",    "Konflikt:" },
	{ "VersionInfo",        "Server <> Klient" },
	{ "PleaseUpgrade",      "K\u00c3\u00b8r opdateringsprogram" }
	};

	/**
	 *  Get Contents
	 *  @return context
	 */
	@Override
	public Object[][] getContents()
	{
		return contents;
	}   //  getContents
}   //  ALoginRes_da
 //  ALoginRes-da
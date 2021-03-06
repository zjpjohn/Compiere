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
package org.compiere.grid.ed;

import java.util.logging.*;


import javax.swing.JFileChooser;

import org.compiere.common.*;
import org.compiere.common.constants.*;
import org.compiere.framework.*;
import org.compiere.model.*;
import org.compiere.swing.*;
import org.compiere.util.*;

/**
 *  Factory for VEditor and its Label for single Row display and multi row-editor
 *
 *  @see VCellRenderer for multi-row display
 *  @author  Jorg Janke
 *  @version $Id: VEditorFactory.java,v 1.3 2006/07/30 00:51:28 jjanke Exp $
 */
public class VEditorFactory
{
	/**
	 *  Create Editor for MField.
	 *  The Name is set to the column name for dynamic display management
	 *  @param mField MField
	 *  @param tableEditor true if table editor
	 *  @return grid editor
	 */
	public static VEditor getEditor (GridField mField, boolean tableEditor)
	{
		return getEditor (null, mField, tableEditor, false);
	}   //  getEditor

	/**
	 *  Create Editor for MField.
	 *  The Name is set to the column name for dynamic display management
	 *  @param mTab MTab
	 *  @param mField MField
	 *  @param tableEditor true if table editor
	 *  @param disableValidation show all values
	 *  @return grid editor
	 */
	public static VEditor getEditor (GridTab mTab, GridField mField, boolean tableEditor)
	{
		return getEditor (mTab, mField, tableEditor, false);
	}	//	getEditor

	/**
	 *  Create Editor for MField.
	 *  The Name is set to the column name for dynamic display management
	 *  @param mTab MTab
	 *  @param mField MField
	 *  @param tableEditor true if table editor
	 *  @param disableValidation show all values
	 *  @return grid editor
	 */
	public static VEditor getEditor (GridTab mTab, GridField mField, 
		boolean tableEditor, boolean disableValidation)
	{
		if (mField == null)
			return null;

		VEditor editor = null;
		int displayType = mField.getDisplayType();
		String columnName = mField.getColumnName();
		boolean mandatory = mField.isMandatory(false);      //  no context check
		boolean readOnly = mField.isReadOnly();
		boolean updateable = mField.isEditable( false );      //  no context check
		int WindowNo = mField.getWindowNo();

		//  Not a Field
		if (mField.isHeading())
			return null;

		//	String (clear/password)
		if (displayType == DisplayTypeConstants.String
			|| (tableEditor && (displayType == DisplayTypeConstants.Text || displayType == DisplayTypeConstants.TextLong)) )
		{
			if (mField.isEncryptedField())
			{
				VPassword vs = new VPassword (columnName, mandatory, readOnly, updateable,
					mField.getDisplayLength(), mField.getFieldLength(), mField.getVFormat());
				vs.setName (columnName);
				vs.setField (mField);
				editor = vs;
			}
			else
			{
				VString vs = new VString (columnName, mandatory, readOnly, updateable,
					mField.getDisplayLength(), mField.getFieldLength(), 
					mField.getVFormat(), mField.getObscureType());
				vs.setName (columnName);
				vs.setField (mField);
				editor = vs;
			}
		}
		//	URL
		else if (displayType == DisplayTypeConstants.URL)
		{
			VURL vs = new VURL (columnName, mandatory, readOnly, updateable,
				mField.getDisplayLength(), mField.getFieldLength());
			vs.setName (columnName);
			vs.setField (mField);
			editor = vs;
		}
		
		//	Printer Name
		else if (displayType == DisplayTypeConstants.PrinterName)
		{
			VPrinter vs = new VPrinter (columnName, mandatory, readOnly, updateable,
				mField.getFieldLength());
			vs.setName (columnName);
			vs.setField (mField);
			editor = vs;
		}
		
		//	File Path / Name
		else if (displayType == DisplayTypeConstants.FilePath || displayType == DisplayTypeConstants.FileName)
		{
			VFile file = new VFile (columnName, mField.getHeader(), mandatory, readOnly, updateable,
				displayType == DisplayTypeConstants.FileName, JFileChooser.OPEN_DIALOG);
			file.setName(columnName);
			file.setField(mField);
			editor = file;
		}

		//	Lookup
		else if (FieldType.isLookup(displayType) || displayType == DisplayTypeConstants.ID)
		{
			Lookup lookup = mField.getLookup();
			if (disableValidation && lookup != null)
				lookup.disableValidation();
			VLookup vl = new VLookup(columnName, mandatory, readOnly, updateable,
				lookup);
			vl.setName(columnName);
			vl.setField (mField);
			editor = vl;
		}

		//	Number
		else if (FieldType.isNumeric(displayType))
		{
			VNumber vn = new VNumber(columnName, mandatory, readOnly, updateable,
				displayType, mField.getHeader());
			vn.setRange(mField.getValueMin(), mField.getValueMax());
			vn.setName(columnName);
			vn.setField (mField);
			editor = vn;
		}

		//	YesNo
		else if (displayType == DisplayTypeConstants.YesNo)
		{
			VCheckBox vc = new VCheckBox(columnName, mandatory, readOnly, updateable,
				mField.getHeader(), mField.getDescription(), tableEditor);
			vc.setName(columnName);
			vc.setField (mField);
			editor = vc;
		}

		//	Text (single row)
		else if (displayType == DisplayTypeConstants.Text)
		{
			VText vt = new VText(columnName, mandatory, readOnly, updateable,
				mField.getDisplayLength(), mField.getFieldLength());
			vt.setName(columnName);
			vt.setField (mField);
			editor = vt;
		}

		//	Memo (single row)
		else if (displayType == DisplayTypeConstants.Memo)
		{
			VMemo vt = new VMemo(columnName, mandatory, readOnly, updateable,
				mField.getDisplayLength(), mField.getFieldLength());
			vt.setName(columnName);
			vt.setField (mField);
			editor = vt;
		}

		//	Date
		else if (FieldType.isDate(displayType))
		{
			if (displayType == DisplayTypeConstants.DateTime)
				readOnly = true;
			VDate vd = new VDate(columnName, mandatory, readOnly, updateable,
				displayType, mField.getHeader());
			vd.setName(columnName);
			vd.setField (mField);
			editor = vd;
		}

		//	Location
		else if (displayType == DisplayTypeConstants.Location)
		{
			VLocation loc = new VLocation (columnName, mandatory, readOnly, updateable,
				(MLocationLookup)mField.getLookup());
			loc.setName(columnName);
			loc.setField (mField);
			editor = loc;
		}

		//	Locator
		else if (displayType == DisplayTypeConstants.Locator)
		{
			VLocator loc = new VLocator (columnName, mandatory, readOnly, updateable,
				(MLocatorLookup)mField.getLookup(), WindowNo);
			loc.setName(columnName);
			loc.setField (mField);
			editor = loc;
		}

		//	Account
		else if (displayType == DisplayTypeConstants.Account)
		{
			VAccount acct = new VAccount (columnName, mandatory, readOnly, updateable,
				(MAccountLookup)mField.getLookup(), mField.getHeader());
			acct.setName(columnName);
			acct.setField (mField);
			editor = acct;
		}

		//	Button
		else if (displayType == DisplayTypeConstants.Button)
		{
			VButton button = new VButton(columnName, mandatory, readOnly, updateable,
				mField.getHeader(), mField.getDescription(), mField.getHelp(), mField.getAD_Process_ID());
			button.setName(columnName);
			button.setField (mField);
			editor = button;
		}

		//  Assignment
		else if (displayType == DisplayTypeConstants.Assignment)
		{
			VAssignment assign = new VAssignment (mandatory, readOnly, updateable);
			assign.setName(columnName);
			assign.setField (mField);
			editor = assign;
		}

		//  Color
		else if (displayType == DisplayTypeConstants.Color)
		{
			VColor color = new VColor (mTab, mandatory, readOnly);
			color.setName(columnName);
			color.setField (mField);
			editor = color;
		}

		//  Image
		else if (displayType == DisplayTypeConstants.Image)
		{
			VImage image = new VImage (columnName, WindowNo);
			image.setName(columnName);
			image.setField (mField);
			editor = image;
		}

		//  PAttribute
		else if (displayType == DisplayTypeConstants.PAttribute)
		{
			VPAttribute attrib = new VPAttribute (mandatory, readOnly, updateable, WindowNo,
				(MPAttributeLookup)mField.getLookup());
			attrib.setName(columnName);
			attrib.setField (mField);
			editor = attrib;
		}
		
		//  Long Text (CLob)
		else if (displayType == DisplayTypeConstants.TextLong)
		{
			VTextLong vt = new VTextLong (columnName, mandatory, readOnly, updateable,
				mField.getDisplayLength(), mField.getFieldLength());
			vt.setName(columnName);
			vt.setField (mField);
			editor = vt;
		}

		//  Binary (BLob)
		else if (displayType == DisplayTypeConstants.Binary)
		{
			VBinary bin = new VBinary (columnName, WindowNo);
			bin.setName(columnName);
			bin.setField (mField);
			editor = bin;
		}

		else
			log.log(Level.WARNING, columnName + " - Unknown Type: " + displayType);

		return editor;
	}	//	getEditor

	/**
	 *  Create Label for MField. (null for YesNo/Button)
	 *  The Name is set to the column name for dynamic display management
	 *
	 *  @param mField MField
	 *  @return Label
	 */
	public static CLabel getLabel (GridField mField)
	{
		if (mField == null)
			return null;

		int displayType = mField.getDisplayType();

		//	No Label for FieldOnly, CheckBox, Button
		if (mField.isFieldOnly()
				|| displayType == DisplayTypeConstants.YesNo
				|| displayType == DisplayTypeConstants.Button)
			return null;
		//
		CLabel label = new CLabel(mField.getHeader(), mField.getDescription());
		label.setName(mField.getColumnName());
	//	label.setFont(CompierePLAF.getFont_Label());
	//	label.setForeground(CompierePLAF.getTextColor_Label());
		return label;
	}   //  getLabel
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(VEditorFactory.class);

}   //  VEditorFactory

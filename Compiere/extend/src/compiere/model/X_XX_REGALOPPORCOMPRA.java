/******************************************************************************
 * Product: Compiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2008 Compiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us at *
 * Compiere, Inc., 3600 Bridge Parkway #102, Redwood City, CA 94065, USA      *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package compiere.model;

/** Generated Model - DO NOT CHANGE */
import java.sql.*;
import org.compiere.framework.*;
import org.compiere.util.*;
/** Generated Model for XX_REGALOPPORCOMPRA
 *  @author Jorg Janke (generated) 
 *  @version Release 3.6.2 - $Id: GenerateModel.java 8952 2010-06-16 07:52:26Z ragrawal $ */
public class X_XX_REGALOPPORCOMPRA extends PO
{
    /** Standard Constructor
    @param ctx context
    @param XX_REGALOPPORCOMPRA_ID id
    @param trx transaction
    */
    public X_XX_REGALOPPORCOMPRA (Ctx ctx, int XX_REGALOPPORCOMPRA_ID, Trx trx)
    {
        super (ctx, XX_REGALOPPORCOMPRA_ID, trx);
        
        /* The following are the mandatory fields for this object.
        
        if (XX_REGALOPPORCOMPRA_ID == 0)
        {
            setTipoPromocion (null);
            setXX_PROMOCIONES_ID (0);
            setXX_REGALOPPORCOMPRA_ID (0);
            
        }
        */
        
    }
    /** Load Constructor 
    @param ctx context
    @param rs result set 
    @param trx transaction
    */
    public X_XX_REGALOPPORCOMPRA (Ctx ctx, ResultSet rs, Trx trx)
    {
        super (ctx, rs, trx);
        
    }
    /** Serial Version No */
    private static final long serialVersionUID = 27529208376789L;
    /** Last Updated Timestamp 2009-07-08 15:27:40.0 */
    public static final long updatedMS = 1247083060000L;
    /** AD_Table_ID=1000017 */
    public static final int Table_ID;
    
    static
    {
        Table_ID = get_Table_ID("XX_REGALOPPORCOMPRA");
        
    }
    ;
    
    /** TableName=XX_REGALOPPORCOMPRA */
    public static final String Table_Name="XX_REGALOPPORCOMPRA";
    
    /**
     *  Get AD Table ID.
     *  @return AD_Table_ID
     */
    @Override public int get_Table_ID()
    {
        return Table_ID;
        
    }
    /** Set APROBADO.
    @param APROBADO APROBADO */
    public void setAPROBADO (boolean APROBADO)
    {
        set_Value ("APROBADO", Boolean.valueOf(APROBADO));
        
    }
    
    /** Get APROBADO.
    @return APROBADO */
    public boolean isAPROBADO() 
    {
        return get_ValueAsBoolean("APROBADO");
        
    }
    
    /** Set Articulos existentes.
    @param ArtExistentes Articulos existentes */
    public void setArtExistentes (int ArtExistentes)
    {
        set_Value ("ArtExistentes", Integer.valueOf(ArtExistentes));
        
    }
    
    /** Get Articulos existentes.
    @return Articulos existentes */
    public int getArtExistentes() 
    {
        return get_ValueAsInt("ArtExistentes");
        
    }
    
    /** Set Cantidad de productos a comprar.
    @param CantProdComprar Cantidad de productos a comprar */
    public void setCantProdComprar (int CantProdComprar)
    {
        set_Value ("CantProdComprar", Integer.valueOf(CantProdComprar));
        
    }
    
    /** Get Cantidad de productos a comprar.
    @return Cantidad de productos a comprar */
    public int getCantProdComprar() 
    {
        return get_ValueAsInt("CantProdComprar");
        
    }
    
    /** Set Description.
    @param Description Optional short description of the record */
    public void setDescription (String Description)
    {
        set_Value ("Description", Description);
        
    }
    
    /** Get Description.
    @return Optional short description of the record */
    public String getDescription() 
    {
        return (String)get_Value("Description");
        
    }
    
    /** Set Comment.
    @param Help Comment, Help or Hint */
    public void setHelp (String Help)
    {
        set_Value ("Help", Help);
        
    }
    
    /** Get Comment.
    @return Comment, Help or Hint */
    public String getHelp() 
    {
        return (String)get_Value("Help");
        
    }
    
    /** Set Hora de fin.
    @param HoraFin Hora de fin */
    public void setHoraFin (Timestamp HoraFin)
    {
        set_Value ("HoraFin", HoraFin);
        
    }
    
    /** Get Hora de fin.
    @return Hora de fin */
    public Timestamp getHoraFin() 
    {
        return (Timestamp)get_Value("HoraFin");
        
    }
    
    /** Set Hora de inicio.
    @param HoraInicio Hora de inicio */
    public void setHoraInicio (Timestamp HoraInicio)
    {
        set_Value ("HoraInicio", HoraInicio);
        
    }
    
    /** Get Hora de inicio.
    @return Hora de inicio */
    public Timestamp getHoraInicio() 
    {
        return (Timestamp)get_Value("HoraInicio");
        
    }
    
    /** Set Name.
    @param Name Alphanumeric identifier of the entity */
    public void setName (String Name)
    {
        set_Value ("Name", Name);
        
    }
    
    /** Get Name.
    @return Alphanumeric identifier of the entity */
    public String getName() 
    {
        return (String)get_Value("Name");
        
    }
    
    /** Get Record ID/ColumnName
    @return ID/ColumnName pair */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
        
    }
    
    /** Set Porcentaje de descuento.
    @param PorcDescuento Porcentaje de descuento */
    public void setPorcDescuento (java.math.BigDecimal PorcDescuento)
    {
        set_Value ("PorcDescuento", PorcDescuento);
        
    }
    
    /** Get Porcentaje de descuento.
    @return Porcentaje de descuento */
    public java.math.BigDecimal getPorcDescuento() 
    {
        return get_ValueAsBigDecimal("PorcDescuento");
        
    }
    
    /** Set Sincronizado.
    @param regactualizado Sincronizado */
    public void setregactualizado (boolean regactualizado)
    {
        set_Value ("regactualizado", Boolean.valueOf(regactualizado));
        
    }
    
    /** Get Sincronizado.
    @return Sincronizado */
    public boolean isregactualizado() 
    {
        return get_ValueAsBoolean("regactualizado");
        
    }
    
    /** Set Regalo.
    @param Regalo Regalo */
    public void setRegalo (String Regalo)
    {
        set_Value ("Regalo", Regalo);
        
    }
    
    /** Get Regalo.
    @return Regalo */
    public String getRegalo() 
    {
        return (String)get_Value("Regalo");
        
    }
    
    /** Todas las tiendas = 00 */
    public static final String TIENDA_TodasLasTiendas = X_Ref_XX_Tienda.TODAS_LAS_TIENDAS.getValue();
    /** 2 - Puente Yanes = 02 */
    public static final String TIENDA_2_PuenteYanes = X_Ref_XX_Tienda._2__PUENTE_YANES.getValue();
    /** 3 - Chacaito = 03 */
    public static final String TIENDA_3_Chacaito = X_Ref_XX_Tienda._3__CHACAITO.getValue();
    /** 7 - Tamanaco = 07 */
    public static final String TIENDA_7_Tamanaco = X_Ref_XX_Tienda._7__TAMANACO.getValue();
    /** 9 - La Granja = 09 */
    public static final String TIENDA_9_LaGranja = X_Ref_XX_Tienda._9__LA_GRANJA.getValue();
    /** 10 - Las Trinitarias = 10 */
    public static final String TIENDA_10_LasTrinitarias = X_Ref_XX_Tienda._10__LAS_TRINITARIAS.getValue();
    /** 15 - La Trinidad = 15 */
    public static final String TIENDA_15_LaTrinidad = X_Ref_XX_Tienda._15__LA_TRINIDAD.getValue();
    /** 16 - Maracaibo = 16 */
    public static final String TIENDA_16_Maracaibo = X_Ref_XX_Tienda._16__MARACAIBO.getValue();
    /** 17 - Millenium mall = 17 */
    public static final String TIENDA_17_MilleniumMall = X_Ref_XX_Tienda._17__MILLENIUM_MALL.getValue();
    /** Set Tienda.
    @param Tienda Wharehouse */
    public void setTienda (String Tienda)
    {
        if (!X_Ref_XX_Tienda.isValid(Tienda))
        throw new IllegalArgumentException ("Tienda Invalid value - " + Tienda + " - Reference_ID=1000026 - 00 - 02 - 03 - 07 - 09 - 10 - 15 - 16 - 17");
        set_Value ("Tienda", Tienda);
        
    }
    
    /** Get Tienda.
    @return Wharehouse */
    public String getTienda() 
    {
        return (String)get_Value("Tienda");
        
    }
    
    /** A1- Ahorro en compra = 1000100 */
    public static final String TIPOPROMOCION_A1_AhorroEnCompra = X_Ref_XX_TIPOSPROMOCION.A1__AHORRO_EN_COMPRA.getValue();
    /** A2- Producto gratis o en descuento = 1000200 */
    public static final String TIPOPROMOCION_A2_ProductoGratisOEnDescuento = X_Ref_XX_TIPOSPROMOCION.A2__PRODUCTO_GRATIS_O_EN_DESCUENTO.getValue();
    /** A3- Descuento en productos publicados = 1000300 */
    public static final String TIPOPROMOCION_A3_DescuentoEnProductosPublicados = X_Ref_XX_TIPOSPROMOCION.A3__DESCUENTO_EN_PRODUCTOS_PUBLICADOS.getValue();
    /** B1- Descuento en el producto X + 1 = 1000400 */
    public static final String TIPOPROMOCION_B1_DescuentoEnElProductoXPlus1 = X_Ref_XX_TIPOSPROMOCION.B1__DESCUENTO_EN_EL_PRODUCTO_X_PLUS1.getValue();
    /** C2 - Coorporativas = 1000500 */
    public static final String TIPOPROMOCION_C2_Coorporativas = X_Ref_XX_TIPOSPROMOCION.C2__COORPORATIVAS.getValue();
    /** D1- M�s se compra m�s se gana = 1000600 */
    public static final String TIPOPROMOCION_D1_M�sSeCompraM�sSeGana = X_Ref_XX_TIPOSPROMOCION.D1__M�S_SE_COMPRA_M�S_SE_GANA.getValue();
    /** D2- Transacci�n premiada = 1000700 */
    public static final String TIPOPROMOCION_D2_Transacci�nPremiada = X_Ref_XX_TIPOSPROMOCION.D2__TRANSACCI�N_PREMIADA.getValue();
    /** D3- Premio de Bono Regalo = 1000800 */
    public static final String TIPOPROMOCION_D3_PremioDeBonoRegalo = X_Ref_XX_TIPOSPROMOCION.D3__PREMIO_DE_BONO_REGALO.getValue();
    /** D4- Cup�n de descuento = 1000900 */
    public static final String TIPOPROMOCION_D4_Cup�nDeDescuento = X_Ref_XX_TIPOSPROMOCION.D4__CUP�N_DE_DESCUENTO.getValue();
    /** E1- Regalo por compra = 1001000 */
    public static final String TIPOPROMOCION_E1_RegaloPorCompra = X_Ref_XX_TIPOSPROMOCION.E1__REGALO_POR_COMPRA.getValue();
    /** E3 - Promociones Cl�sicas = 1001100 */
    public static final String TIPOPROMOCION_E3_PromocionesCl�sicas = X_Ref_XX_TIPOSPROMOCION.E3__PROMOCIONES_CL�SICAS.getValue();
    /** F1- Premio Ilusi�n = 1001200 */
    public static final String TIPOPROMOCION_F1_PremioIlusi�n = X_Ref_XX_TIPOSPROMOCION.F1__PREMIO_ILUSI�N.getValue();
    /** Set Tipo de Promoci�n.
    @param TipoPromocion Tipo de Promoci�n */
    public void setTipoPromocion (String TipoPromocion)
    {
        if (TipoPromocion == null) throw new IllegalArgumentException ("TipoPromocion is mandatory");
        if (!X_Ref_XX_TIPOSPROMOCION.isValid(TipoPromocion))
        throw new IllegalArgumentException ("TipoPromocion Invalid value - " + TipoPromocion + " - Reference_ID=1000013 - 1000100 - 1000200 - 1000300 - 1000400 - 1000500 - 1000600 - 1000700 - 1000800 - 1000900 - 1001000 - 1001100 - 1001200");
        set_ValueNoCheck ("TipoPromocion", TipoPromocion);
        
    }
    
    /** Get Tipo de Promoci�n.
    @return Tipo de Promoci�n */
    public String getTipoPromocion() 
    {
        return (String)get_Value("TipoPromocion");
        
    }
    
    /** Set Search Key.
    @param Value Search key for the record in the format required - must be unique */
    public void setValue (String Value)
    {
        set_Value ("Value", Value);
        
    }
    
    /** Get Search Key.
    @return Search key for the record in the format required - must be unique */
    public String getValue() 
    {
        return (String)get_Value("Value");
        
    }
    
    /** Set Categor�a.
    @param XX_CATEGORIA_ID Categor�a */
    public void setXX_CATEGORIA_ID (int XX_CATEGORIA_ID)
    {
        if (XX_CATEGORIA_ID <= 0) set_Value ("XX_CATEGORIA_ID", null);
        else
        set_Value ("XX_CATEGORIA_ID", Integer.valueOf(XX_CATEGORIA_ID));
        
    }
    
    /** Get Categor�a.
    @return Categor�a */
    public int getXX_CATEGORIA_ID() 
    {
        return get_ValueAsInt("XX_CATEGORIA_ID");
        
    }
    
    /** Set Departamento.
    @param XX_DEPARTAMENTO_ID Departamento */
    public void setXX_DEPARTAMENTO_ID (int XX_DEPARTAMENTO_ID)
    {
        if (XX_DEPARTAMENTO_ID <= 0) set_Value ("XX_DEPARTAMENTO_ID", null);
        else
        set_Value ("XX_DEPARTAMENTO_ID", Integer.valueOf(XX_DEPARTAMENTO_ID));
        
    }
    
    /** Get Departamento.
    @return Departamento */
    public int getXX_DEPARTAMENTO_ID() 
    {
        return get_ValueAsInt("XX_DEPARTAMENTO_ID");
        
    }
    
    /** Set L�nea.
    @param XX_LINEA_ID L�nea */
    public void setXX_LINEA_ID (int XX_LINEA_ID)
    {
        if (XX_LINEA_ID <= 0) set_Value ("XX_LINEA_ID", null);
        else
        set_Value ("XX_LINEA_ID", Integer.valueOf(XX_LINEA_ID));
        
    }
    
    /** Get L�nea.
    @return L�nea */
    public int getXX_LINEA_ID() 
    {
        return get_ValueAsInt("XX_LINEA_ID");
        
    }
    
    /** Set Monto m�nimo.
    @param XX_MontoMinimo Monto m�nimo */
    public void setXX_MontoMinimo (java.math.BigDecimal XX_MontoMinimo)
    {
        set_Value ("XX_MontoMinimo", XX_MontoMinimo);
        
    }
    
    /** Get Monto m�nimo.
    @return Monto m�nimo */
    public java.math.BigDecimal getXX_MontoMinimo() 
    {
        return get_ValueAsBigDecimal("XX_MontoMinimo");
        
    }
    
    /** Set Product.
    @param XX_Product_ID Product */
    public void setXX_Product_ID (int XX_Product_ID)
    {
        if (XX_Product_ID <= 0) set_Value ("XX_Product_ID", null);
        else
        set_Value ("XX_Product_ID", Integer.valueOf(XX_Product_ID));
        
    }
    
    /** Get Product.
    @return Product */
    public int getXX_Product_ID() 
    {
        return get_ValueAsInt("XX_Product_ID");
        
    }
    
    /** Set Promoci�n.
    @param XX_PROMOCIONES_ID Promoci�n */
    public void setXX_PROMOCIONES_ID (int XX_PROMOCIONES_ID)
    {
        if (XX_PROMOCIONES_ID < 1) throw new IllegalArgumentException ("XX_PROMOCIONES_ID is mandatory.");
        set_ValueNoCheck ("XX_PROMOCIONES_ID", Integer.valueOf(XX_PROMOCIONES_ID));
        
    }
    
    /** Get Promoci�n.
    @return Promoci�n */
    public int getXX_PROMOCIONES_ID() 
    {
        return get_ValueAsInt("XX_PROMOCIONES_ID");
        
    }
    
    /** Set XX_REGALO.
    @param XX_REGALO XX_REGALO */
    public void setXX_REGALO (String XX_REGALO)
    {
        set_Value ("XX_REGALO", XX_REGALO);
        
    }
    
    /** Get XX_REGALO.
    @return XX_REGALO */
    public String getXX_REGALO() 
    {
        return (String)get_Value("XX_REGALO");
        
    }
    
    /** Set XX_REGALOPPORCOMPRA_ID.
    @param XX_REGALOPPORCOMPRA_ID XX_REGALOPPORCOMPRA_ID */
    public void setXX_REGALOPPORCOMPRA_ID (int XX_REGALOPPORCOMPRA_ID)
    {
        if (XX_REGALOPPORCOMPRA_ID < 1) throw new IllegalArgumentException ("XX_REGALOPPORCOMPRA_ID is mandatory.");
        set_ValueNoCheck ("XX_REGALOPPORCOMPRA_ID", Integer.valueOf(XX_REGALOPPORCOMPRA_ID));
        
    }
    
    /** Get XX_REGALOPPORCOMPRA_ID.
    @return XX_REGALOPPORCOMPRA_ID */
    public int getXX_REGALOPPORCOMPRA_ID() 
    {
        return get_ValueAsInt("XX_REGALOPPORCOMPRA_ID");
        
    }
    
    /** Set Secci�n.
    @param XX_SECCION_ID Secci�n */
    public void setXX_SECCION_ID (int XX_SECCION_ID)
    {
        if (XX_SECCION_ID <= 0) set_Value ("XX_SECCION_ID", null);
        else
        set_Value ("XX_SECCION_ID", Integer.valueOf(XX_SECCION_ID));
        
    }
    
    /** Get Secci�n.
    @return Secci�n */
    public int getXX_SECCION_ID() 
    {
        return get_ValueAsInt("XX_SECCION_ID");
        
    }
    
    /** Set Transferencia.
    @param XX_Transferencia Transferencia */
    public void setXX_Transferencia (String XX_Transferencia)
    {
        set_Value ("XX_Transferencia", XX_Transferencia);
        
    }
    
    /** Get Transferencia.
    @return Transferencia */
    public String getXX_Transferencia() 
    {
        return (String)get_Value("XX_Transferencia");
        
    }
    
    
}

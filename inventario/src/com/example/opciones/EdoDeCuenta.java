package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.ui.NumberField;

import pagos.BotonMP;
import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;

import com.example.inventario.Usuario;
import mp.MP;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import conexiones.BeanConexion;
import conexiones.BeanConsulta;
import conexiones.Mysql;

public class EdoDeCuenta {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final HorizontalLayout cabecera = new HorizontalLayout();
			//cabecera.setHeight("70");
			cabecera.setWidth("50%");
			cabecera.setStyleName(ValoTheme.LAYOUT_WELL);
			cabecera.setMargin(true);
			
		final VerticalLayout cuerpo = new VerticalLayout();
			cuerpo.setMargin(true);
			
		//Variables

		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		double deuda;
		String vencido;
		Mysql sql = new Mysql();
		BeanConsulta bean;
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
	 		   bean = sql.consultaSimple("SELECT ifnull(sum(monto),0) FROM "+SqlConf.obtenerBase()+"main.edo_cuenta where custid in ("+usuario.getCustidsRelacionados()+")");
	 		    
	    		if(!bean.getRespuesta().equals("OK")){
	    			throw new Exception(bean.getRespuesta());
	    		}
	    		
	 		   deuda = Double.parseDouble(bean.getDato());
	 		   
	 		   bean = sql.consultaSimple("SELECT if(curdate()>max(vencimiento), 'SI','NO') as vencido FROM "+SqlConf.obtenerBase()+"main.edo_cuenta where custid in ("+usuario.getCustidsRelacionados()+")");
	 		    
	    		if(!bean.getRespuesta().equals("OK")){
	    			throw new Exception(bean.getRespuesta());
	    		}
	 		    
	 		    vencido = bean.getDato();
			
	 		    NumberFormat formatter = NumberFormat.getCurrencyInstance();
	 		    Label saldo = new Label("Saldo de la cuenta: "+formatter.format(deuda));
	 		    saldo.setSizeUndefined();
	 		    saldo.setStyleName("LabelDeuda");
	 		    
	 		    cabecera.addComponent(saldo);
	 		    cabecera.setComponentAlignment(cabecera.getComponent(0), Alignment.MIDDLE_LEFT);
	 		    
	 		    //Reviso si agrego bot�n de pago
	 		    
	 		    if(deuda > 0){
	 		    	
	 		    	MP mp = new MP("1945323047437243", "BEHtTxfiRPSTgmyaysCWtGHZdJ7n7jvm");
	 				//mp.sandboxMode(true);
	 				//System.out.println("Sandbox: "+mp.sandboxMode());
	 		    	
	 				try{
	 				
	 					/*String preferenceData = "{'items':"+
	 						"[{"+
	 							"'title':'Cargo TuPrograma',"+
	 							"'quantity':1,"+
	 							"'currency_id':'MXN',"+ 
	 							"'unit_price':"+deuda+""+
	 						"}],"+
 							"'external_reference':'"+usuario.getCustid()+"'"+
	 					"}";*/
	 		    	
	 					final String initPoint = mp.generarPago(mp.generarToken(), deuda, usuario.getCustid());
	 					
						Button botonPago = new Button("Pagar");
						botonPago.setStyleName(ValoTheme.BUTTON_PRIMARY);
						botonPago.addClickListener(new Button.ClickListener() {
							public void buttonClick(ClickEvent event) {
								
								UI.getCurrent().getPage().open(initPoint, "_blank");
								
							}
						});
						
						cabecera.addComponent(botonPago);
						cabecera.setComponentAlignment(botonPago, Alignment.MIDDLE_RIGHT);
					
	 				}catch(Exception e){
	 					e.printStackTrace();
	 				}finally{
	 					mp = null;
	 				}
	 		    	
	 		    }
	 		    
	 		    
	 		    //agrego tabla de movimientos
	 		    
				beanCon = sql.conexionSimple("SELECT DATE_FORMAT(fecha,'%d-%m-%Y') as fecha, movimiento, monto, pagada, DATE_FORMAT(vencimiento,'%d-%m-%Y') as vencimiento, comentarios " + 
						"FROM "+SqlConf.obtenerBase()+"main.edo_cuenta " + 
						"where custid in ('"+usuario.getCustidsRelacionados()+"') " + 
						"and estatus = 'A' and movimiento = 'FACTURA' "
						+ "order by fecha desc");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
    			
    			rs = beanCon.getRs();
    			
    			cuerpo.removeAllComponents();
    			
    			Label historial = new Label("Historial de facturas");
    				historial.setStyleName("LabelHistorial");
    			
    			cuerpo.addComponent(historial);
    			cuerpo.addComponent(CrearTablas.crearEdoCuenta(rs));
    			cuerpo.setComponentAlignment(cuerpo.getComponent(1), Alignment.MIDDLE_CENTER);
	 		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			deuda = 0;
			vencido = null;
			sql.cerrar();
			sql = null;
			bean = null;
		}
		
		respuesta.addComponent(cabecera);
			respuesta.setComponentAlignment(cabecera, Alignment.MIDDLE_LEFT);
		respuesta.addComponent(cuerpo);
			
		return respuesta;
		
	}
	
	//Empiezan m�todos externos

	
}

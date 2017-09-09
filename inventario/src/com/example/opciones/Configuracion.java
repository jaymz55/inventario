package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;

import com.example.inventario.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import conexiones.BeanConexion;
import conexiones.BeanConsulta;
import conexiones.Mysql;
import funciones.Encriptar;
import funciones.Funcion;

public class Configuracion {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final HorizontalLayout cabecera = new HorizontalLayout();
			cabecera.setHeight("70");
			cabecera.setWidth("90%");

			Label tituloLabel = new Label("Configuración");
			tituloLabel.setStyleName(ValoTheme.LABEL_H1);
			
		cabecera.addComponent(tituloLabel);
			
		final VerticalLayout cuerpo = new VerticalLayout();
			//cuerpo.setWidth("100%");
		
		final GridLayout grid = new GridLayout(3,3);
			grid.setWidth("100%");
			grid.setHeight("150");
			grid.setStyleName(ValoTheme.LAYOUT_CARD);
			
			cuerpo.addComponent(grid);
		
		respuesta.addComponent(cabecera);
		respuesta.addComponent(cuerpo);
		
		//Variables
			Mysql sql = new Mysql();
			final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
			try{
				
				//Variables
				Label correoLabel = new Label("Correo para aviso de solicitudes");
					correoLabel.setStyleName(ValoTheme.LABEL_HUGE);
				
				final TextField correoText = new TextField();
					correoText.setWidth("90%");
					
			 		   BeanConsulta bean = sql.consultaSimple("select ifnull(correo,'') as correo, count(correo) from "+SqlConf.obtenerBase()+"inventario.solicitudes_correo where custid = "+usuario.getCustid() +" limit 1");
			 		    
			    		if(!bean.getRespuesta().equals("OK")){
			    			throw new Exception(bean.getRespuesta());
			    		}
			    		
			    		correoText.setValue(bean.getDato());
			    		
				final CheckBox copia = new CheckBox("Copiarme");
				
				 		bean = sql.consultaSimple("select ifnull(copia,'NO') as copia, count(correo) from "+SqlConf.obtenerBase()+"inventario.solicitudes_correo where custid = "+usuario.getCustid() +" limit 1");
			 		    
			    		if(!bean.getRespuesta().equals("OK")){
			    			throw new Exception(bean.getRespuesta());
			    		}
			    		
				    	if(bean.getDato().equals("SI"))
				    		copia.setValue(true);
				
					
				//Botón de registro
					
				Button registrar = new Button("Guardar");
					//registrar.setStyleName("boton_simple");
					registrar.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					   
					    //Registro correo
					    	Mysql sql = new Mysql();
					    	String respuesta;
					    	
					    	try{
					    		
					    		if(correoText.getValue().equals(""))
					    			copia.setValue(false);
					    		
					    		String copiaAjustada;
					    		
					    		if(copia.getValue() == true){
					    			copiaAjustada = "SI";
					    		}else{
					    			copiaAjustada = "NO";
					    		}
					    		
					 		    int existe;
					 		    BeanConsulta beanSimple = sql.consultaSimple("select count(correo) from "+SqlConf.obtenerBase()+"inventario.solicitudes_correo where custid = "+usuario.getCustid());
					 		    
					    		if(!beanSimple.getRespuesta().equals("OK")){
					    			throw new Exception(beanSimple.getRespuesta());
					    		}
				    			
					    		existe = Integer.parseInt(beanSimple.getDato());
					    		
					    		if(existe > 0){

	    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.solicitudes_correo set "+
					    					"correo = '"+correoText.getValue()+"', copia = '"+copiaAjustada+"' where custid = "+usuario.getCustid());
					    			
	    				    		if(!respuesta.equals("OK")){
	    				    			throw new Exception(respuesta);
	    				    		}
	    				    		
	    				    		usuario.setCorreoSolicitudes(correoText.getValue());
	    				    		usuario.setCorreoSolicitudesCopia(copiaAjustada);
					    			
					    		}else{
					    			
	    				    		respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.solicitudes_correo values ("+
					    					"null, "+usuario.getCustid()+", '"+correoText.getValue()+"','"+copiaAjustada+"')");
					    			
	    				    		if(!respuesta.equals("OK")){
	    				    			throw new Exception(respuesta);
	    				    		}
	    				    		
	    				    		usuario.setCorreoSolicitudes(correoText.getValue());
	    				    		usuario.setCorreoSolicitudesCopia(copiaAjustada);
					    			
					    		}
					    		
								Notification n = new Notification("Ajustes guardados correctamente", Type.TRAY_NOTIFICATION);
								n.setDelayMsec(2000);
								n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
								n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
								n.show(UI.getCurrent().getPage());
					    		
					    	}catch(Exception e){
					    		e.printStackTrace();
					    		Notification.show("Error en sistema: "+e.toString(), Type.ERROR_MESSAGE);
					    	}finally{
					    		sql.cerrar();
					    	}

					    }
					});
					
					grid.addComponent(correoLabel,0,0);
						grid.setComponentAlignment(correoLabel, Alignment.BOTTOM_LEFT);
					grid.addComponent(correoText,0,1);
						grid.setComponentAlignment(correoText, Alignment.BOTTOM_LEFT);
					grid.addComponent(copia,1,1);
						grid.setComponentAlignment(copia, Alignment.BOTTOM_LEFT);
					
					grid.addComponent(registrar,0,2);
						grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				sql.cerrar();
			}
		
		return respuesta;
	}
	
}

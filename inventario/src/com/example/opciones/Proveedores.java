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
import conexiones.Mysql;
import funciones.Encriptar;
import funciones.Funcion;

public class Proveedores {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final HorizontalLayout cabecera = new HorizontalLayout();
			cabecera.setHeight("70");
			cabecera.setWidth("90%");
			
		final VerticalLayout tablas = new VerticalLayout();
			final VerticalLayout dos = new VerticalLayout();
			
		//Variables
			Mysql sql = new Mysql();
			final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
			try{
			
				final Button insertar = new Button("Registrar nuevo proveedor");
				insertar.setStyleName("boton_registrar_nuevo");
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
					
					final Window ventanaRegistrar = new Window("Registrar");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
				    	
					final TextField nombre = new TextField("Proveedor");
						nombre.setWidth("80%");
						nombre.setMaxLength(500);
						
					final TextField producto = new TextField("Producto");
						producto.setWidth("80%");
						producto.setMaxLength(500);
					final TextField contacto = new TextField("Contacto");
						contacto.setWidth("80%");
						contacto.setMaxLength(500);
					final TextField telefono = new TextField("Teléfono");
						telefono.setMaxLength(100);
					final TextField correo = new TextField("Correo electrónico");
						correo.setWidth("80%");
						correo.setMaxLength(500);
					final TextField pagina = new TextField("Página web");
						pagina.setWidth("80%");
						pagina.setMaxLength(100);
					final TextArea direccion = new TextArea("Dirección");
						direccion.setWidth("80%");
						direccion.setMaxLength(1000);
					final TextArea observaciones = new TextArea("Observaciones");
						observaciones.setWidth("80%");
						observaciones.setMaxLength(1000);
					
					Button registrar = new Button("Registrar");
					registrar.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					    	
						    	Mysql sql = new Mysql();
						    	String respuesta = "NO";
						    	
						    	try{
						    		
						    		respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.proveedores values ("
						    				+ "null, '"+usuario.getCustid()+"','"+Encriptar.Encriptar(nombre.getValue())+"','"+producto.getValue().replaceAll("'", "")+"',"
						    						+ "'"+Encriptar.Encriptar(contacto.getValue())+"','"+Encriptar.Encriptar(telefono.getValue())+"',"
						    								+ "'"+Encriptar.Encriptar(correo.getValue())+"','"+pagina.getValue().replaceAll("'", "")+"','"+Encriptar.Encriptar(direccion.getValue())+"',"
						    										+ "'"+observaciones.getValue().replaceAll("'", "")+"','SI')");
						    		
	    				    		if(!respuesta.equals("OK")){
	    				    			throw new Exception(respuesta);
	    				    		}else{
						    		
								    	nombre.setValue("");
										producto.setValue("");
										contacto.setValue("");
										telefono.setValue("");
										correo.setValue("");
										pagina.setValue("");
										direccion.setValue("");
										observaciones.setValue("");
				
										Notification n = new Notification("Registro correcto de proveedor", Type.TRAY_NOTIFICATION);
										n.setDelayMsec(2000);
										n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
										n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
										n.show(UI.getCurrent().getPage());
								
	    				    		}
	    				    		
						    	}catch(Exception e){
						    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
						    		e.printStackTrace();
						    	}finally{
						    		sql.cerrar();
						    	}
						    	
						    	dos.removeAllComponents();
						    	dos.addComponent(insertar);
						    	dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
						    		dos.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
						    	
						    }
						});
							
							GridLayout grid = new GridLayout(2, 6);
								grid.setMargin(true);
								grid.setWidth("100%");
								grid.setHeight("100%");
								grid.addComponent(nombre, 0, 0);
								grid.addComponent(producto, 1, 0);
								grid.addComponent(contacto, 0, 1);
								grid.addComponent(telefono, 1, 1);
								grid.addComponent(correo, 0, 3);
								grid.addComponent(pagina, 1, 3);
								grid.addComponent(direccion, 0, 4);
								grid.addComponent(observaciones, 1, 4);
								grid.addComponent(registrar, 0, 5);
									grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
							
								ventanaRegistrar.setContent(grid);
								
								UI.getCurrent().addWindow(ventanaRegistrar);
				    	
				    }
				});
				
				//Inserto tabla
				
				try{

					//Titulo
					Label titulo = new Label("Registro de proveedores");
					titulo.setStyleName(ValoTheme.LABEL_H1);
					//titulo.setStyleName("Label_titulo");
					
					cabecera.addComponent(titulo);
						cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
					
					cabecera.addComponent(insertar);
						cabecera.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
					dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));

				
				}catch(Exception e){
					Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				}finally{
					sql.cerrar();
				}
				
				respuesta.addComponent(cabecera);
				respuesta.addComponent(dos);

			}catch(Exception e){
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
			}

		return respuesta;
		
	}
	
	//Empiezan m�todos externos
	
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid){
		
		Mysql sql = new Mysql();
		
		try{

			tablas.removeAllComponents();
			
			BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre, producto, contacto, telefono, correo, pagina, direccion, observaciones FROM "+SqlConf.obtenerBase()+"inventario.proveedores FORCE INDEX (custid) where custid in ("+custid+") and activo = 'SI'");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			ResultSet rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"NOMBRE","PRODUCTO","CONTACTO","TELEFONO","CORREO","PAGINA","DIRECCION","OBSERVACIONES"};
			tablas.addComponent(crearCon2FiltrosInventario(tablas, rs, "NOMBRE", "PRODUCTO", columnasExportar));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			sql.cerrar();
		}
			
		return tablas;
		
	}
	
	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon2FiltrosInventario(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final Object[] columnasExportar) throws UnsupportedOperationException, Exception{

		VerticalLayout respuesta = new VerticalLayout();
		respuesta.setWidth("95%");
		
		HorizontalLayout interior = new HorizontalLayout();
		interior.setMargin(true);
		interior.setWidth("30%");
		// Text field for inputting a filter
		final TextField filtro = new TextField();
			filtro.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar.toLowerCase()));
			filtro.setStyleName("boton_simple");
		final TextField filtro2 = new TextField();
			filtro2.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar2.toLowerCase()));
			filtro2.setStyleName("boton_simple");
		interior.addComponent(filtro);
			interior.setComponentAlignment(filtro, Alignment.MIDDLE_LEFT);
		interior.addComponent(filtro2);
			interior.setComponentAlignment(filtro2, Alignment.MIDDLE_CENTER);
		
		
		respuesta.addComponent(interior);
		
		final Table tabla = new Table();
			//tabla.setSelectable(false);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("PRECIO") || name.toUpperCase().equals("MESES")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
			}else if(name.toUpperCase().equals("ID")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
				tabla.setColumnCollapsingAllowed(true);
				tabla.setColumnCollapsed(name.toUpperCase(), true);
			}else{
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_LEFT);
				
			}
		}
		
		
		
		int id = 0;
		while(rs.next()){
			
			if(count==2){
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2)}, id);
			}else if(count==3){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)}, id);
			}else if(count==4){
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4)}, id);
			}else if(count==5){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)}, id);
			}else if(count==6){
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getObject(5), rs.getObject(6)}, id);
			}else if(count==7){
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getObject(5), rs.getObject(6), rs.getObject(7)}, id);
			}else if(count==8){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)}, id);
			}else if(count==9){ //Usa �ste
				tabla.addItem(new Object[]{rs.getString(1), Encriptar.Desencriptar(rs.getString(2)), rs.getString(3), Encriptar.Desencriptar(rs.getString(4)), Encriptar.Desencriptar(rs.getString(5)), Encriptar.Desencriptar(rs.getString(6)), rs.getString(7), Encriptar.Desencriptar(rs.getString(8)), rs.getString(9)}, id);
			}else if(count==10){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)}, id);
			}else if(count==11){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)}, id);
			}else if(count==12){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12)}, id);
			}else if(count==13){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)}, id);
			}else if(count==14){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14)}, id);
			}else if(count==15){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15)}, id);
			}else if(count==16){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16)}, id);
			}else if(count==17){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17)}, id);
			}else if(count==18){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18)}, id);
			}else if(count==19){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19)}, id);
			}else if(count==20){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16), rs.getString(17), rs.getString(18), rs.getString(19), rs.getString(20)}, id);
			}
			id++;
		}
		tabla.setSelectable(true);
		tabla.setMultiSelect(true);
		tabla.setHeight("300");
		tabla.setWidth("95%");
				
				ExcelExporter excelExporter = new ExcelExporter(tabla);
				Date fecha = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				excelExporter.setDownloadFileName(format.format(fecha));
				excelExporter.setCaption("Excel");
				excelExporter.setVisibleColumns(columnasExportar);
		
		respuesta.addComponent(excelExporter);

		// Filter table according to typed input
		filtro.addListener(new TextChangeListener() {
		    SimpleStringFilter filter = null;

		    public void textChange(TextChangeEvent event) {
		        Filterable f = (Filterable)
		            tabla.getContainerDataSource();
		        
		        // Remove old filter
		        if (filter != null)
		            f.removeContainerFilter(filter);
		        
		        // Set new filter for the "Name" column
		        filter = new SimpleStringFilter(tituloColumnaFiltrar, event.getText(),
		                                        true, false);
		        f.addContainerFilter(filter);
		    }
		});

		filtro2.addListener(new TextChangeListener() {
		    SimpleStringFilter filter = null;

		    public void textChange(TextChangeEvent event) {
		        Filterable f = (Filterable)
		            tabla.getContainerDataSource();
		        
		        // Remove old filter
		        if (filter != null)
		            f.removeContainerFilter(filter);
		        
		        // Set new filter for the "Name" column
		        filter = new SimpleStringFilter(tituloColumnaFiltrar2, event.getText(),
		                                        true, false);
		        f.addContainerFilter(filter);
		    }
		});
		
		
		//Doble clic
		tabla.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		    @Override
		    public void itemClick(ItemClickEvent event) {
		    	if (event.isDoubleClick()){

		    		//Saco el id para evitar confusi�n cuando hay dos facturas con un mismo folio
		    		Property<String> itemProperty = event.getItem().getItemProperty("ID");
		    		final String id = itemProperty.getValue();

		    		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();

		    		final Window ventanaActualizar = new Window("Actualizar");
		    		ventanaActualizar.center();
		    		ventanaActualizar.setHeight("70%");
		    		ventanaActualizar.setWidth("85%");
		    				
		    		final Mysql sql = new Mysql();
		    		
		    		try{
		    		
		    			BeanConexion beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.proveedores where id = "+id);
		    			
		    			if(!beanCon.getRespuesta().equals("OK")){
		    				throw new Exception(beanCon.getRespuesta());
		    			}
		    			
		    			ResultSet rs = beanCon.getRs();
		    			
						final TextField nombre = new TextField("Proveedor");
						nombre.setWidth("80%");
						nombre.setMaxLength(500);
					final TextField producto = new TextField("Producto");
						producto.setWidth("80%");
						producto.setMaxLength(500);
					final TextField contacto = new TextField("Contacto");
						contacto.setWidth("80%");
						contacto.setMaxLength(500);
					final TextField telefono = new TextField("Teléfono");
						telefono.setMaxLength(100);
					final TextField correo = new TextField("Correo electrónico");
						correo.setWidth("80%");
						correo.setMaxLength(500);
					final TextField pagina = new TextField("Página web");
						pagina.setWidth("80%");
						pagina.setMaxLength(100);
					final TextArea direccion = new TextArea("Dirección");
						direccion.setWidth("80%");
						direccion.setMaxLength(1000);
					final TextArea observaciones = new TextArea("Observaciones");
						observaciones.setWidth("80%");
						observaciones.setMaxLength(1000);
		    				
		    			while(rs.next()){
		    				nombre.setValue(Encriptar.Desencriptar(rs.getString("nombre")));
		    				producto.setValue(rs.getString("producto"));
		    				contacto.setValue(Encriptar.Desencriptar(rs.getString("contacto")));
		    				telefono.setValue(Encriptar.Desencriptar(rs.getString("telefono")));
		    				correo.setValue(Encriptar.Desencriptar(rs.getString("correo")));
		    				pagina.setValue(rs.getString("pagina"));
		    				direccion.setValue(Encriptar.Desencriptar(rs.getString("direccion")));
		    				observaciones.setValue(rs.getString("observaciones"));
		    			}
		    				
		    			Button registrar = new Button("Actualizar");
		    			registrar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    Mysql sql = new Mysql();
		    				    String respuesta = "NO";
		    			    	try{
		    			    	
		    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.proveedores set nombre = '"+Encriptar.Encriptar(nombre.getValue())+"',"
		    				    				+ "producto = '"+producto.getValue().replaceAll("'", "")+"', contacto = "
		    				    						+ "'"+Encriptar.Encriptar(contacto.getValue())+"', telefono = '"+Encriptar.Encriptar(telefono.getValue())+"', correo = "
		    				    								+ "'"+Encriptar.Encriptar(correo.getValue())+"', pagina = '"+pagina.getValue().replaceAll("'", "")+"', direccion = '"+Encriptar.Encriptar(direccion.getValue())+"',"
		    				    										+ "observaciones = '"+observaciones.getValue().replaceAll("'", "")+"' where id = "+id);
		    			
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}else{
		    				    		
			    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
			    				    		
											Notification n = new Notification("Correcta actualización de proveedor", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
											
											ventanaActualizar.close();
		    				    		
		    				    		}
		    				    		
		    			    	}catch(Exception e){
		    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
		    			    		e.printStackTrace();
		    			    	}finally{
		    			    		sql.cerrar();
		    			    	}
		    				    		
		    			    }
		    			});
		    				   
		    			Button eliminar = new Button("Eliminar");
		    			eliminar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    		
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmaci�n", "�Est�s seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
				    				    Mysql sql = new Mysql();
				    				    String respuesta = "NO";
				    			    	try{
				    			    	
				    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.proveedores set activo = 'NO' where id = "+id);
				    				    		
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}else{
				    				    			
					    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
					    				    		
													Notification n = new Notification("Registro eliminado correctamente", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
					    				    		
					    				    		ventanaActualizar.close();
				    				    		
				    				    		}
				    				    		
				    			    	}catch(Exception e){
				    			    		Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
				    			    		e.printStackTrace();
				    			    	}finally{
				    			    		sql.cerrar();
				    			    	}	
		    				    		
		    			                }
		    			            }
		    			            
		    					});
				    			    	
		    			    }
		    			});
		    			
		    			GridLayout grid = new GridLayout(2, 6);
		    			grid.setMargin(true);
		    			grid.setWidth("100%");
		    			grid.setHeight("100%");
		    			grid.addComponent(nombre, 0, 0);
		    			grid.addComponent(producto, 1, 0);
		    			grid.addComponent(contacto, 0, 1);
		    			grid.addComponent(telefono, 1, 1);
		    			grid.addComponent(correo, 0, 3);
		    			grid.addComponent(pagina, 1, 3);
		    			grid.addComponent(direccion, 0, 4);
		    			grid.addComponent(observaciones, 1, 4);
		    			grid.addComponent(registrar, 0, 5);
		    				grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
		    			grid.addComponent(eliminar, 1, 5);
		    				grid.setComponentAlignment(eliminar, Alignment.BOTTOM_LEFT);
		    				
		    			ventanaActualizar.setContent(grid);
		    			
		    			UI.getCurrent().addWindow(ventanaActualizar);
		    			
		    		}catch(Exception e){
		    			Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
		    			e.printStackTrace();
		    		}finally{
		    			sql.cerrar();
		    		}
		    		
		    		
		    	}
		    }
		});
		
		return respuesta;
	}
	
}

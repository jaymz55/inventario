package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.ui.NumberField;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;

import com.example.inventario.Usuario;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import conexiones.BeanConexion;
import conexiones.BeanConsulta;
import conexiones.Mysql;
import correos.Correo;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import funciones.Encriptar;
import funciones.Funcion;

public class Almacen {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		
		final HorizontalLayout titulo = new HorizontalLayout();
		titulo.setHeight("70");
		titulo.setWidth("90%");
		
		final HorizontalLayout cabecera = new HorizontalLayout();
			cabecera.setHeight("100%");
			cabecera.setWidth("100%");
			
		final VerticalLayout cuerpo = new VerticalLayout();	
			cuerpo.setHeight("100%");
			cuerpo.setWidth("100%");
		
		final VerticalLayout tablas = new VerticalLayout();
			tablas.setHeight("100%");
			tablas.setWidth("100%");
			
			
			final DateField fechaUno = new DateField();
				fechaUno.setDateFormat("dd MMM yyyy");
				fechaUno.setValue(getFirstDateOfCurrentMonth());
				fechaUno.setStyleName("ajustado");
				
				
			final DateField fechaDos = new DateField();
				fechaDos.setDateFormat("dd MMM yyyy");
				fechaDos.setValue(new Date());
				fechaDos.setStyleName("ajustado");
				
			final Button buscar = new Button("Buscar");
				buscar.setStyleName("boton_simple");
			
				buscar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
				    	cuerpo.removeAllComponents();
				    	cuerpo.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados(), Funcion.fechaFormato(fechaUno.getValue(), "yyyy-MM-dd"), Funcion.fechaFormato(fechaDos.getValue(), "yyyy-MM-dd")));
				    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
				    	
			    }
			});

				
			final Button insertar = new Button("Producción");
			insertar.setStyleName("boton_simple");
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
						final Window ventanaRegistrar = new Window("Nueva producción");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("30%");
						ventanaRegistrar.setWidth("80%");
						
						final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						producto.setWidth("100%");
						
						final NumberField cantidad = new NumberField("Cantidad");
						
						final DateField fechaProduccion = new DateField("Fecha");
							TimeZone timeZone1 = TimeZone.getTimeZone("America/Mexico_City");
							fechaProduccion.setTimeZone(timeZone1);
							fechaProduccion.setDateFormat("dd MMMM yyyy");
							
						final DateField caducidad = new DateField("Caducidad");
							caducidad.setDateFormat("dd MMMM yyyy");
							
						final CheckBox descuenta = new CheckBox("Descuenta material");
							descuenta.setValue(true);
				    	
							
						Button registrar = new Button("Registrar");
						registrar.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {
						    	
						    	if(producto.getValue() != null && !cantidad.getValue().equals("") && fechaProduccion.getValue() != null){
						    	
						    		Mysql sql = new Mysql();
						    		Mysql sql2 = new Mysql();
						    		
						    		
						    	//Variables que deben terminar en null
						    		String respuesta = "";
						    		String caducidadAjustada = "null";
						    		String idAlmacen = "";
						    		ResultSet rs;
						    		
						    		try{
						    			
						    			
						    			//*****************Aquí debo abrir una transacción
						    			sql.transaccionAbrir();
						    			sql2.transaccionAbrir();

						    			if(caducidad.getValue() != null){
						    				caducidadAjustada = "'" + Funcion.fechaFormato(caducidad.getValue(), "yyyy-MM-dd") + "'";
						    			}
						    			
						    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.almacen values ("
						    					+ "null, '"+usuario.getCustid()+"','"+producto.getValue().toString()+"','"+Funcion.fechaFormato(fechaProduccion.getValue(), "yyyy-MM-dd")+"', "+caducidadAjustada+","+cantidad.getValue()+",'PRODUCCIÓN', null, null, 'SI', null)");
						    			
						    			if(!respuesta.equals("OK")){
						    				throw new Exception(respuesta);
						    			}
						    			
						    		//Registrar salida de material ***************************************************************
						    			
						    		//Reviso CheckBox
						    			if(descuenta.getValue() == true){
						    			
							    			//Obtengo el número de id
								 		    
								 		    BeanConsulta bean = sql.consultaSimple("SELECT LAST_INSERT_ID()");
								 		    
								 		    idAlmacen = bean.getDato();
								 		    
								    		if(!bean.getRespuesta().equals("OK")){
								    			throw new Exception(bean.getRespuesta());
								    		}
							    			
											BeanConexion beanCon = sql.conexionSimple("SELECT * FROM "+SqlConf.obtenerBase()+"inventario.produccion where custid = "+usuario.getCustid()+" and id_prod = "+producto.getValue().toString());
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
							    			
							    			rs = beanCon.getRs();
							    			
							    			double totalCantidad = 0;
							    			
							    			while(rs.next()){
							    				
							    				//Veces que marque Cantidad
							    				
							    				for (int i = 0; i < Integer.parseInt(cantidad.getValue()); i++) {
														totalCantidad = totalCantidad + (rs.getDouble("cantidad")*-1);
												}
							    				
							    				//Registro
							    				respuesta = sql2.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.inventario values (null, "+usuario.getCustid()+"," +
							    						totalCantidad + ",null, null, null, "+rs.getString("id_prod")+",'"+Funcion.fechaFormato(fechaProduccion.getValue(), "yyyy-MM-dd")+"', null,"+rs.getString("id_mat")+", null, null, null, 'SALIDA', "+idAlmacen+")");
							    			
								    			if(!respuesta.equals("OK")){
								    				throw new Exception(respuesta);
								    			}
								    			
								    			totalCantidad = 0;
							    				
							    			}
							    			
							    			totalCantidad = 0;	
						    			
						    			} //Termina check de descuenta material
						    			
						    			sql.transaccionCommit();
						    				sql.transaccionCerrar();
						    			sql2.transaccionCommit();
						    				sql2.transaccionCerrar();
						    			
						    			producto.setValue(null);
						    			cantidad.setValue("");
						    			caducidad.setValue(null);
						    			
										Notification n = new Notification("Registro de producto correcto", Type.TRAY_NOTIFICATION);
										n.setDelayMsec(2000);
										n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
										n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
										n.show(UI.getCurrent().getPage());
						    			
						    			//Notification.show("Registro de producto correcto",Type.TRAY_NOTIFICATION);
						    			
								    	cuerpo.removeAllComponents();
								    	cuerpo.addComponent(generarTablaConsolidado(tablas, usuario.getCustidsRelacionados()));
								    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
						    			
						    		}catch(Exception e){
						    			
						    			sql.transaccionRollBack();
						    				sql.transaccionCerrar();
						    				sql = null;
						    			sql2.transaccionRollBack();
						    				sql2.transaccionCerrar();
						    				sql2 = null;
						    				
							    		//Cierro las variables
								    		respuesta = null;
								    		caducidadAjustada = null;
								    		idAlmacen = null;
								    		rs = null;
						    				
						    			e.printStackTrace();
						    			Notification.show("Error en sistema: "+e.toString(), Type.ERROR_MESSAGE);
						    		}finally{
						    			sql.cerrar();
						    				sql = null;
						    			sql2.cerrar();
						    				sql2 = null;
						    				
						    			//Cierro las variables
								    		respuesta = null;
								    		caducidadAjustada = null;
								    		idAlmacen = null;
								    		rs = null;
						    				
						    		}
							    	
						    	}else{
						    		Notification.show("Se deben de ingresar todos los datos",  Type.WARNING_MESSAGE);
						    	}
							    		
							    }
							});
						
						GridLayout grid = new GridLayout(5, 2);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.addComponent(producto, 0, 0);
							grid.setComponentAlignment(producto, Alignment.MIDDLE_LEFT);
						grid.addComponent(cantidad, 1, 0);
							grid.setComponentAlignment(cantidad, Alignment.MIDDLE_CENTER);
						grid.addComponent(fechaProduccion, 2, 0);
							grid.setComponentAlignment(fechaProduccion, Alignment.MIDDLE_CENTER);
						grid.addComponent(caducidad, 3, 0);
							grid.setComponentAlignment(caducidad, Alignment.MIDDLE_CENTER);
						grid.addComponent(descuenta, 4, 0);
							grid.setComponentAlignment(descuenta, Alignment.MIDDLE_CENTER);
						grid.addComponent(registrar, 0, 1);
							grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
					
						ventanaRegistrar.setContent(grid);
						
						UI.getCurrent().addWindow(ventanaRegistrar);
						
				    }
				});
		
			final Button consolidado = new Button("Consolidar");
				consolidado.setStyleName("boton_simple");
			
			consolidado.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
				    	cuerpo.removeAllComponents();
				    	cuerpo.addComponent(generarTablaConsolidado(tablas, usuario.getCustidsRelacionados()));
				    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
				    	
			    }
			});
			
			final Button salidas = new Button("Tránsitos");
			salidas.setStyleName("boton_simple");
			
			salidas.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
				    	cuerpo.removeAllComponents();
				    	cuerpo.addComponent(generarTablaSalidas(tablas, usuario));
				    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
				    	
			    }
			});
				
			final Button merma = new Button("Merma");
				merma.setStyleName("boton_simple");
			
			merma.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
						final Window ventanaRegistrar = new Window("Merma");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("30%");
						ventanaRegistrar.setWidth("60%");
						
						final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						producto.setWidth("80%");
						
						final NumberField cantidad = new NumberField("Cantidad");
						
						final DateField fechaProduccion = new DateField("Fecha");
							TimeZone timeZone1 = TimeZone.getTimeZone("America/Mexico_City");
							fechaProduccion.setTimeZone(timeZone1);
							fechaProduccion.setDateFormat("dd MMMM yyyy");
				    	
						Button registrar = new Button("Registrar merma");
						registrar.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {
						    	
						    	if(producto.getValue() != null && !cantidad.getValue().equals("") && fechaProduccion.getValue() != null){
						    	
			    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer registrar la merma? Recuerda que debiste registrar la producción de este artículo antes",
			    							"SI", "NO", new ConfirmDialog.Listener() {

			    			            public void onClose(ConfirmDialog dialog) {
			    			                if (dialog.isConfirmed()) {
						    		
			    			                //Variables a cerrar
									    		Mysql sql = new Mysql();
									    		String respuesta = "";
									    		
									    		try{
									    			
									    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.almacen values ("
									    					+ "null, '"+usuario.getCustid()+"','"+producto.getValue().toString()+"','"+Funcion.fechaFormato(fechaProduccion.getValue(), "yyyy-MM-dd")+"', null, "+(Double.parseDouble(cantidad.getValue())*-1)+",'MERMA', null, null, 'SI', null)");
									    			
					    				    		if(!respuesta.equals("OK")){
					    				    			throw new Exception(respuesta);
					    				    		}
									    			
									    			producto.setValue(null);
									    			cantidad.setValue("");
									    			
													Notification n = new Notification("Registro de merma correcto", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
									    			
									    			//Notification.show("Registro de merma correcto",Type.TRAY_NOTIFICATION);
									    			
											    	cuerpo.removeAllComponents();
											    	cuerpo.addComponent(generarTablaConsolidado(tablas, usuario.getCustidsRelacionados()));
											    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
									    			
											    	
											    	
									    		}catch(Exception e){
									    			e.printStackTrace();
									    			Notification.show("Error en sistema: "+e.toString(), Type.ERROR_MESSAGE);
									    			
									    		}finally{
									    			sql.cerrar();
									    			
									    		//Cierro variables
										    		sql = null;
										    		respuesta = null;
										    		
									    		}
									    		
			    			                }
			    			            }
			    			            
			    					});
							    	
						    	}else{
						    		Notification.show("Se deben de ingresar todos los datos",  Type.WARNING_MESSAGE);
						    	}
							    		
							    }
							});
						
						GridLayout grid = new GridLayout(3, 2);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.addComponent(producto, 0, 0);
							grid.setComponentAlignment(producto, Alignment.MIDDLE_CENTER);
						grid.addComponent(cantidad, 1, 0);
							grid.setComponentAlignment(cantidad, Alignment.MIDDLE_CENTER);
						grid.addComponent(fechaProduccion, 2, 0);
							grid.setComponentAlignment(fechaProduccion, Alignment.MIDDLE_CENTER);
						grid.addComponent(registrar, 0, 1);
							grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
					
						ventanaRegistrar.setContent(grid);
						
						UI.getCurrent().addWindow(ventanaRegistrar);
				    	
			    }
			});
			
		//Botón para salida de producto sin venta
			
			final Button salida = new Button("Tránsito");
			salida.setStyleName("boton_simple");
	
			salida.addListener(new Button.ClickListener() {
		    public void buttonClick(ClickEvent event) {
				
				
				//Empieza trabajo
					    	
					    	final Window ventanaRegistrar = new Window("Registrar salida de producto (sin venta inmiscuida)");
							ventanaRegistrar.center();
							ventanaRegistrar.setHeight("90%");
							ventanaRegistrar.setWidth("65%");
							
							final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
								producto.setWidth("90%");
							
							final NumberField cantidad = new NumberField("Cantidad");
								cantidad.setWidth("100");
								
							final DateField fecha = new DateField("Fecha de salida");
							 	fecha.setDateFormat("dd MMMM yyyy");
							 	fecha.setValue(new Date(System.currentTimeMillis() - 3600 * 7000));
							
							final TextArea comentarios = new TextArea("Comentarios");
								comentarios.setWidth("90%");
								comentarios.setMaxLength(500);
								
								//Cuadro de componentes
								final Table tabla = new Table();
									tabla.setLocale(Locale.US);
								
								tabla.setHeight("200");
								tabla.setWidth("90%");
								tabla.setSelectable(true);
								
								tabla.addContainerProperty("ID", String.class, null);
								tabla.setColumnCollapsingAllowed(true);
								tabla.setColumnCollapsed("ID", true);
									tabla.addContainerProperty("PRODUCTO", String.class, null);
									tabla.addContainerProperty("CANTIDAD", String.class, null);

									Button añadir = new Button("Agregar");
									añadir.addListener(new Button.ClickListener() {
									    public void buttonClick(ClickEvent event) {
									    	
										    	if(producto.getValue() == null || cantidad.getValue().equals("")){
										    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
										    	}else{
										    		tabla.addItem(new Object[]{producto.getValue().toString(), producto.getItemCaption(producto.getValue()), cantidad.getValue()}, producto.getValue().toString());
										    		producto.setValue(null);
										    		cantidad.setValue("");
										    	}
									    		
										    }
										});
									
									Button eliminar = new Button("Eliminar");
									eliminar.addListener(new Button.ClickListener() {
									    public void buttonClick(ClickEvent event) {

									    	if(tabla.getValue() != null){
									    	
									    		tabla.removeItem(tabla.getValue());
									    		
									    	}else{
									    		Notification.show("Se debe escoger algún producto a eliminar", Type.WARNING_MESSAGE);
									    	}
									    	
										    }
										});
									
									Button registrar = new Button("Registrar");
									registrar.addListener(new Button.ClickListener() {
									    public void buttonClick(ClickEvent event) {
									    	
									    	if(tabla.size() == 0){
									    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE); 
									    	}else{
									    	
											    	Mysql sql = new Mysql();
											    	
											    	//Variables a cerrar
											    	String respuesta = "NO";
											    	String idSalida = "";
											    	
											    	try{
											    		
											    	//Abro transacción
											    		
											    		sql.transaccionAbrir();

											    	//Agrego componentes
											    		
											    		//Obtengo id del producto recién agregado

											 		    BeanConsulta bean = sql.consultaSimple("SELECT ifnull(max(id_salida)+1,1) from "+SqlConf.obtenerBase()+"inventario.almacen where custid in ("+usuario.getCustidsRelacionados()+")");
											 		    
											    		if(!bean.getRespuesta().equals("OK")){
											    			throw new Exception(bean.getRespuesta());
											    		}
										    			
											    		idSalida = bean.getDato();
											    		
												    	Collection<?> ids = tabla.getItemIds();
												    	Property<String> cantidad = null;
												    	
												    	//String respuesta = "NO";
												    	
												        for (Object elem : ids) {
												        	
												        	cantidad = tabla.getItem(elem).getItemProperty("CANTIDAD");
												        	respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.almacen values (null, '"+usuario.getCustid()+"',"+elem+",'"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"', null, "+Double.parseDouble(cantidad.getValue())*-1+",'TRÁNSITO',"+idSalida+",'"+comentarios.getValue()+"','SI', null)");

											    			if(!respuesta.equals("OK")){
											    				throw new Exception(respuesta);
											    			}
												        	
												        }
												        
												        ids = null;

												    //Verifico que respuesta sea SÍ para continuar
												        
												    if(respuesta.equals("OK")){
												    
												    //Cierro transacción
												    	sql.transaccionCommit();
												    	sql.transaccionCerrar();
												    	
												    	
												    	producto.setValue("");
														cantidad.setValue("");
														//fecha.setValue(null);
														comentarios.setValue("");
														tabla.removeAllItems();
								
														Notification n = new Notification("Registro de salida correcto", Type.TRAY_NOTIFICATION);
														n.setDelayMsec(2000);
														n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
														n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
														n.show(UI.getCurrent().getPage());
													
												    }

												    
											    	}catch(Exception e){
											    		
												    	//Cierro transacción
											    			sql.transaccionRollBack();
											    			sql.transaccionCerrar();
											    		
											    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
											    		e.printStackTrace();
											    		
											    	}finally{
											    		sql.cerrar();
											    		
											    		sql = null;
												    	respuesta = null;
												    	idSalida = null;
												    	
											    	}
											    	
											    	cuerpo.removeAllComponents();
											    	cuerpo.addComponent(generarTablaSalidas(tablas, usuario));
										    	
									    	    }
										    		
										    }
										});
		    	
		    	
									GridLayout grid = new GridLayout(1, 7);
									grid.setMargin(true);
									grid.setWidth("100%");
									grid.setHeight("100%");
									
									HorizontalLayout datos = new HorizontalLayout();
										datos.setWidth("90%");
										datos.setMargin(true);
										datos.setCaption("Datos");
										datos.setStyleName(ValoTheme.LAYOUT_WELL);
										
										datos.addComponent(producto);
											datos.setComponentAlignment(producto, Alignment.MIDDLE_LEFT);
										datos.addComponent(cantidad);
											datos.setComponentAlignment(cantidad, Alignment.MIDDLE_CENTER);
										datos.addComponent(fecha);
											datos.setComponentAlignment(fecha, Alignment.MIDDLE_RIGHT);
									
									grid.addComponent(datos, 0, 0);
										grid.setComponentAlignment(datos, Alignment.MIDDLE_CENTER);
									
									grid.addComponent(tabla, 0, 1);
										grid.setComponentAlignment(tabla, Alignment.MIDDLE_CENTER);
									
									HorizontalLayout botones = new HorizontalLayout();
										botones.setWidth("50%");
										botones.addComponent(añadir);
											botones.setComponentAlignment(añadir, Alignment.MIDDLE_CENTER);
										botones.addComponent(eliminar);
											botones.setComponentAlignment(eliminar, Alignment.MIDDLE_CENTER);
									
									grid.addComponent(botones, 0, 2);
										grid.setComponentAlignment(botones, Alignment.MIDDLE_CENTER);
										
									grid.addComponent(comentarios, 0, 4);
										grid.setComponentAlignment(comentarios, Alignment.MIDDLE_CENTER);
										
									grid.addComponent(registrar, 0, 5);
										grid.setComponentAlignment(registrar, Alignment.BOTTOM_RIGHT);
								
									ventanaRegistrar.setContent(grid);
									
									UI.getCurrent().addWindow(ventanaRegistrar);
		    	
					    }
			    	});
			
			Label tituloLabel = new Label("Registro de nuevos productos en almacén");
			tituloLabel.setStyleName(ValoTheme.LABEL_H1);
			
			titulo.addComponent(tituloLabel);
			
		//LayOut para reportes
			HorizontalLayout reporte = new HorizontalLayout();
				reporte.setCaption("Reportes");
				reporte.setMargin(true);
				reporte.setStyleName(ValoTheme.LAYOUT_WELL);
				reporte.setWidth("100%");
				reporte.setHeight("100");
				reporte.addComponent(fechaUno);
					reporte.setComponentAlignment(fechaUno, Alignment.BOTTOM_CENTER);
				reporte.addComponent(fechaDos);
					reporte.setComponentAlignment(fechaDos, Alignment.BOTTOM_CENTER);
				reporte.addComponent(buscar);
					reporte.setComponentAlignment(buscar, Alignment.BOTTOM_CENTER);
				reporte.addComponent(consolidado);
					reporte.setComponentAlignment(consolidado, Alignment.BOTTOM_CENTER);
				reporte.addComponent(salidas);
					reporte.setComponentAlignment(salidas, Alignment.BOTTOM_CENTER);
					
			HorizontalLayout registros = new HorizontalLayout();
				registros.setWidth("85%");
				registros.setHeight("100");
				registros.setCaption("Registros");
				registros.setMargin(true);
				registros.setStyleName(ValoTheme.LAYOUT_WELL);
				registros.addComponent(insertar);
					registros.setComponentAlignment(insertar, Alignment.MIDDLE_LEFT);
				registros.addComponent(merma);
					registros.setComponentAlignment(merma, Alignment.MIDDLE_CENTER);
				registros.addComponent(salida);
					registros.setComponentAlignment(salida, Alignment.MIDDLE_RIGHT);
				
			cabecera.addComponent(registros);
				cabecera.setExpandRatio(registros, 4f);
			cabecera.addComponent(reporte);
				cabecera.setExpandRatio(reporte, 5f);

			
			respuesta.addComponent(titulo);
			respuesta.addComponent(cabecera);
				cuerpo.addComponent(tablas);
			respuesta.addComponent(cuerpo);
			
		return respuesta;
		
	}
	
	//Empiezan métodos externos
	
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid, String fechaInicial, String fechaFinal){
		
		Mysql sql = new Mysql();
		ResultSet rs;
		BeanConexion beanCon;
		
		try{

			tablas.removeAllComponents();
			
			beanCon = sql.conexionSimple("select id, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as producto, (select total from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as precio, fecha, cantidad, movimiento, caducidad from "+SqlConf.obtenerBase()+"inventario.almacen a FORCE INDEX (custid) where custid in ("+custid+") and fecha between '"+fechaInicial+"' and '"+fechaFinal+"' and activo = 'SI' order by fecha");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			//ResultSet rs = sql.conexionSimple("select id, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as producto, (select total from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as precio, fecha, cantidad, /*if(cantidad>=0,'ENTRADA','SALIDA') as*/ movimiento from "+SqlConf.obtenerBase()+"inventario.almacen a where custid in ("+custid+") and fecha between '"+fechaInicial+"' and '"+fechaFinal+"' order by fecha");	
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"PRODUCTO","PRECIO","FECHA","CANTIDAD","MOVIMIENTO","CADUCIDAD"};
			tablas.addComponent(crearCon3FiltrosInventario(tablas, rs, "PRODUCTO", "FECHA", "MOVIMIENTO", columnasExportar, fechaInicial, fechaFinal));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			sql.cerrar();
			sql = null;
			
			rs = null;
			beanCon = null;
			
		}
			
		return tablas;
		
	}
	
	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon3FiltrosInventario(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final String tituloColumnaFiltrar3, final Object[] columnasExportar, final String fechaInicial, final String fechaFinal) throws SQLException{

		VerticalLayout respuesta = new VerticalLayout();
		respuesta.setWidth("95%");
		
		HorizontalLayout interior = new HorizontalLayout();
		interior.setMargin(true);
		interior.setWidth("50%");
		// Text field for inputting a filter
		final TextField filtro = new TextField();
			filtro.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar.toLowerCase()));
			filtro.setStyleName("boton_simple");
		final TextField filtro2 = new TextField();
			filtro2.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar2.toLowerCase()));
			filtro2.setStyleName("boton_simple");
		final TextField filtro3 = new TextField();
			filtro3.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar3.toLowerCase()));
			filtro3.setStyleName("boton_simple");
			
		interior.addComponent(filtro);
			interior.setComponentAlignment(filtro, Alignment.MIDDLE_LEFT);
		interior.addComponent(filtro2);
			interior.setComponentAlignment(filtro2, Alignment.MIDDLE_CENTER);
		interior.addComponent(filtro3);
			interior.setComponentAlignment(filtro3, Alignment.MIDDLE_CENTER);
		
		respuesta.addComponent(interior);
		
		final Table tabla = new Table();
		tabla.setLocale(Locale.US);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("PRECIO") || name.toUpperCase().equals("CANTIDAD")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
			}else if(name.toUpperCase().equals("FECHA") || name.toUpperCase().equals("CADUCIDAD")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_CENTER);
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
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getDouble(5)}, id);
			}else if(count==6){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getDouble(5), rs.getString(6)}, id);
			}else if(count==7){ //USA ESTE
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4), rs.getDouble(5), rs.getString(6), rs.getString(7)}, id);
			}else if(count==8){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)}, id);
			}else if(count==9){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)}, id);
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
		
		id = 0;
		
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

		filtro3.addListener(new TextChangeListener() {
		    SimpleStringFilter filter = null;

		    public void textChange(TextChangeEvent event) {
		        Filterable f = (Filterable)
		            tabla.getContainerDataSource();
		        
		        // Remove old filter
		        if (filter != null)
		            f.removeContainerFilter(filter);
		        
		        // Set new filter for the "Name" column
		        filter = new SimpleStringFilter(tituloColumnaFiltrar3, event.getText(),
		                                        true, false);
		        f.addContainerFilter(filter);
		    }
		});
		
		//Doble clic
		tabla.addItemClickListener(new ItemClickEvent.ItemClickListener() {
		    @Override
		    public void itemClick(ItemClickEvent event) {
		    	if (event.isDoubleClick()){

		    		//Saco el id para evitar confusión cuando hay dos facturas con un mismo folio
		    		Property<String> itemProperty = event.getItem().getItemProperty("ID");
		    		final String id = itemProperty.getValue();

		    		itemProperty = event.getItem().getItemProperty("MOVIMIENTO");
		    		final String movimiento = itemProperty.getValue();
		    		
		    		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();

		    			if(movimiento.equals("VENTA") || movimiento.equals("DEVOLUCIÓN") || movimiento.equals("TRÁNSITO")){
		    				
		    				Notification.show("No se puede editar este movimiento", Type.WARNING_MESSAGE);
		    			
		    			}else if(movimiento.equals("MERMA")){
		    			
		    				final Window ventanaActualizar = new Window("Eliminar");
				    		ventanaActualizar.center();
				    		ventanaActualizar.setHeight("20%");
				    		ventanaActualizar.setWidth("20%");
				    				
				    		//final Mysql sql = new Mysql();
				    		
				    		try{
				    				   
				    			Button eliminar = new Button("Eliminar");
				    			eliminar.addListener(new Button.ClickListener() {
				    			    public void buttonClick(ClickEvent event) {
				    				    		
				    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminar esta merma?",
				    							"SI", "NO", new ConfirmDialog.Listener() {

				    			            public void onClose(ConfirmDialog dialog) {
				    			                if (dialog.isConfirmed()) {
				    			    	
						    				    Mysql sql = new Mysql();
						    				    String respuesta;
						    				    
						    			    	try{
						    			    	
						    			    			//sql.insertarSimple("delete FROM "+SqlConf.obtenerBase()+"inventario.almacen where custid = "+usuario.getCustid()+" and fecha = (SELECT fecha FROM "+SqlConf.obtenerBase()+"inventario.ventas where id = "+id+") and movimiento = 'VENTA' and id_producto = (SELECT id_producto FROM "+SqlConf.obtenerBase()+"inventario.ventas where id = "+id+") limit 1");
						    			    			
						    				    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.almacen where id = "+id);
						    				    		
						    				    		if(!respuesta.equals("OK")){
						    				    			throw new Exception(respuesta);
						    				    		}else{
						    				    		
							    				    		generarTabla(tablas, usuario.getCustid(), fechaInicial, fechaFinal);
							    				    		
															Notification n = new Notification("Merma eliminada correctamente", Type.TRAY_NOTIFICATION);
															n.setDelayMsec(2000);
															n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
															n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
															n.show(UI.getCurrent().getPage());
							    				    		
							    				    		//Notification.show("Venta eliminada correctamente", Type.TRAY_NOTIFICATION);
							    				    		ventanaActualizar.close();
						    				    		
						    				    		}
						    				    		
						    			    	}catch(Exception e){
						    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
						    			    		e.printStackTrace();
						    			    	}finally{
						    			    		sql.cerrar();
						    			    		sql = null;
						    			    		respuesta = null;
						    			    	}	
				    				    		
				    			                }
				    			            }
				    			            
				    					});
						    			    	
				    			    }
				    			});
				    			
				    			GridLayout grid = new GridLayout(1, 1);
				    			grid.setMargin(true);
				    			grid.setWidth("100%");
				    			grid.setHeight("100%");
				    			grid.addComponent(eliminar, 0, 0);
				    				grid.setComponentAlignment(eliminar, Alignment.MIDDLE_CENTER);
				    				
				    			ventanaActualizar.setContent(grid);
				    			
				    			UI.getCurrent().addWindow(ventanaActualizar);
				    			
				    		}catch(Exception e){
				    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				    			e.printStackTrace();
				    		}finally{
				    			//sql.cerrar();
				    		}
		    				
		    			}else{
		    		
				    		final Window ventanaActualizar = new Window("Eliminar");
				    		ventanaActualizar.center();
				    		ventanaActualizar.setHeight("20%");
				    		ventanaActualizar.setWidth("20%");
				    				
				    		//final Mysql sql = new Mysql();
				    		
				    		try{
				    				   
				    			Button eliminar = new Button("Eliminar");
				    			eliminar.addListener(new Button.ClickListener() {
				    			    public void buttonClick(ClickEvent event) {
				    				    		
				    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
				    							"SI", "NO", new ConfirmDialog.Listener() {
		
				    			            public void onClose(ConfirmDialog dialog) {
				    			                if (dialog.isConfirmed()) {
				    			    	
						    				    Mysql sql = new Mysql();
						    				    String respuesta;
						    			    	
						    				    try{
						    			    	
						    			    			sql.transaccionAbrir();
							    			    		
						    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.almacen set activo = 'NO' where id = "+id);
						    			
						    				    		if(!respuesta.equals("OK")){
						    				    			throw new Exception(respuesta);
						    				    		}
						    				    		
						    				    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.inventario where id_almacen = "+id);

						    				    		if(!respuesta.equals("OK")){
						    				    			
						    				    			sql.transaccionRollBack();
						    				    			sql.transaccionCerrar();
						    				    			throw new Exception(respuesta);
						    				    		}else{
						    				    		
						    				    			sql.transaccionCommit();
						    				    			sql.transaccionCerrar();
						    				    			
							    				    		generarTabla(tablas, usuario.getCustidsRelacionados(), fechaInicial, fechaFinal);
							    				    		
															Notification n = new Notification("Registro eliminado correctamente", Type.TRAY_NOTIFICATION);
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
						    			    		sql = null;
						    			    		respuesta = null;
						    			    	}	
				    				    		
				    			                }
				    			            }
				    			            
				    					});
						    			    	
				    			    }
				    			});
				    			
				    			GridLayout grid = new GridLayout(1, 1);
				    			grid.setMargin(true);
				    			grid.setWidth("100%");
				    			grid.setHeight("100%");
				    			grid.addComponent(eliminar, 0, 0);
				    				grid.setComponentAlignment(eliminar, Alignment.MIDDLE_CENTER);
				    				
				    			ventanaActualizar.setContent(grid);
				    			
				    			UI.getCurrent().addWindow(ventanaActualizar);
				    			
				    		}catch(Exception e){
				    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				    			e.printStackTrace();
				    		}finally{
				    			//sql.cerrar();
				    		}
		    		
		    			}
		    		
		    	}
		    }
		});
		
		return respuesta;
	}
	
	private VerticalLayout generarTablaConsolidado(VerticalLayout tablas, String custid){
		
		Mysql sql = new Mysql();
		
		try{

			tablas.removeAllComponents();
			
			BeanConexion beanCon = sql.conexionSimple("select id, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as producto, (select round(avg(total),2) from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as 'precio promedio', (select ifnull(minimo,0) from "+SqlConf.obtenerBase()+"inventario.productos where custid in ("+custid+") and id = a.id_producto) as minimo, (select ifnull(maximo,0) from "+SqlConf.obtenerBase()+"inventario.productos where custid in ("+custid+") and id = a.id_producto) as maximo, sum(cantidad) as existencia, (sum(cantidad)-(select minimo)) as diferencial from "+SqlConf.obtenerBase()+"inventario.almacen a FORCE INDEX (custid) where custid in ("+custid+") and activo = 'SI' group by id_producto order by diferencial");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			ResultSet rs = beanCon.getRs();

			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"PRODUCTO","PRECIO PROMEDIO","MINIMO","MAXIMO","EXISTENCIA","DIFERENCIAL"};
			tablas.addComponent(crearCon2FiltrosConsolidado(tablas, rs, "PRODUCTO", "EXISTENCIA", columnasExportar));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			sql.cerrar();
		}
			
		return tablas;
		
	}
	
	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon2FiltrosConsolidado(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final Object[] columnasExportar) throws SQLException{

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
		
		
		respuesta.addComponent(interior);
		
		final Table tabla = new Table();
		tabla.setLocale(Locale.US);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("EXISTENCIA") || name.toUpperCase().equals("MINIMO") || name.toUpperCase().equals("MAXIMO")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
				tabla.setColumnWidth(name.toUpperCase(), 120);
			}else if(name.toUpperCase().equals("DIFERENCIAL")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
				tabla.setColumnWidth(name.toUpperCase(), 120);
			}else if(name.toUpperCase().equals("PRECIO PROMEDIO")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
			}else if(name.toUpperCase().equals("FECHA")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
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
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4)}, id);
			}else if(count==5){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)}, id);
			}else if(count==6){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)}, id);
			}else if(count==7){ //USA ESTE 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7)}, id);
			}else if(count==8){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)}, id);
			}else if(count==9){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)}, id);
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
		
		tabla.setCellStyleGenerator(new Table.CellStyleGenerator(){

			@Override
			public String getStyle(Table source, Object itemId,
					Object propertyId) {
		        if (propertyId == null) {
			          // Styling for row
			          Item item = tabla.getItem(itemId);
			          double diferencial = (Double) item.getItemProperty("DIFERENCIAL").getValue();
			          if (diferencial < 0) {
			            return "highlight-red";
			          } else if(diferencial == 0){
			        	  return "highlight-yellow";
			          } else {
			            return null;
			          }
			        } else {
			          // styling for column propertyId
			        	if(propertyId.toString().equals("DIFERENCIAL") || propertyId.toString().equals("EXISTENCIA")){
			        		return "black";
			        	}else{
			        		return null;
			        	}
			        }
			}
			
		});
		
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
		
		return respuesta;
	}

	
//Tabla para salidas sin venta
	@SuppressWarnings("deprecation")
	private VerticalLayout generarTablaSalidas(final VerticalLayout tablas, final UsuarioDTO usuario){
		
		Mysql sql = new Mysql();
		
		try{
			
			tablas.removeAllComponents();
			
			BeanConexion beanCon = sql.conexionSimple("select id, comentario, sum(cantidad) as cantidad, fecha from "+SqlConf.obtenerBase()+"inventario.almacen a FORCE INDEX (custid) where custid in ("+usuario.getCustidsRelacionados()+") and movimiento = 'TRANSITO' and activo = 'SI' group by id_salida");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			ResultSet rs = beanCon.getRs();

			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"COMENTARIO","CANTIDAD","FECHA"};
			tablas.addComponent(crearCon2FiltrosSalidas(tablas, rs, "COMENTARIO", "FECHA", columnasExportar));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			sql.cerrar();
			sql = null;
		}
			
		return tablas;
		
	}
	
	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon2FiltrosSalidas(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final Object[] columnasExportar) throws SQLException{

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
		tabla.setLocale(Locale.US);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("CANTIDAD")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
				tabla.setColumnWidth(name.toUpperCase(), 120);
			}else if(name.toUpperCase().equals("COMENTARIO")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_LEFT);
			}else if(name.toUpperCase().equals("FECHA")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
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
			}else if(count==4){ //USA ESTE 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)}, id);
			}else if(count==5){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)}, id);
			}else if(count==6){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6)}, id);
			}else if(count==7){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7)}, id);
			}else if(count==8){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)}, id);
			}else if(count==9){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)}, id);
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
		
		    		
		    		//Saco el id para evitar confusión cuando hay dos facturas con un mismo folio
		    		Property<String> itemProperty = event.getItem().getItemProperty("ID");
		    		final String id = itemProperty.getValue();

		    		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		
				
				//Empieza trabajo
		    	
		    	final Window ventanaRegistrar = new Window("Registrar salida de producto (sin venta inmiscuida)");
				ventanaRegistrar.center();
				ventanaRegistrar.setHeight("90%");
				ventanaRegistrar.setWidth("65%");
					
				final DateField fecha = new DateField("Fecha de salida");
				 	fecha.setDateFormat("dd MMMM yyyy");
				 	fecha.setValue(new Date(System.currentTimeMillis() - 3600 * 7000));
				
				final TextArea comentarios = new TextArea("Comentarios");
					comentarios.setWidth("90%");
					comentarios.setMaxLength(500);
					
					//Cuadro de componentes
					final Table tablaInterna = new Table();
					tablaInterna.setLocale(Locale.US);
					
					tablaInterna.setHeight("200");
					tablaInterna.setWidth("90%");
					tablaInterna.setSelectable(true);
					
					tablaInterna.addContainerProperty("ID", String.class, null);
					tablaInterna.setColumnCollapsingAllowed(true);
					tablaInterna.setColumnCollapsed("ID", true);
						tablaInterna.addContainerProperty("PRODUCTO", String.class, null);
						tablaInterna.addContainerProperty("CANTIDAD", String.class, null);
						
					//Lleno la tabla
						Mysql sql = new Mysql();
						
						try{
			    			
			    			//BeanConsulta bean = sql.consultaSimple("select ifnull(comentario,'') as comentario from "+SqlConf.obtenerBase()+"inventario.almacen where id_salida = "+id+" group by id_salida");
							BeanConsulta bean = sql.consultaSimple("select ifnull(comentario,'') as comentario from "+SqlConf.obtenerBase()+"inventario.almacen where id = "+id+"");
							
			    			if(!bean.getRespuesta().equals("OK")){
			    				throw new Exception(bean.getRespuesta());
			    			}else{
			    				comentarios.setValue(bean.getDato());
			    			}
			    			
			    			//bean = sql.consultaSimple("select fecha from "+SqlConf.obtenerBase()+"inventario.almacen where id_salida = "+id);
			    			bean = sql.consultaSimple("select fecha from "+SqlConf.obtenerBase()+"inventario.almacen where id = "+id);
			    			
			    			if(!bean.getRespuesta().equals("OK")){
			    				throw new Exception(bean.getRespuesta());
			    			}else{
			    				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    				String fechaString = bean.getDato();
			    				Date date = formatter.parse(fechaString);
			    				
			    				fecha.setValue(date);
			    			}
			    			
			    			//Obtengo id_salida
			    			bean = sql.consultaSimple("select id_salida from "+SqlConf.obtenerBase()+"inventario.almacen where id = "+id);
			    			
			    			if(!bean.getRespuesta().equals("OK")){
			    				throw new Exception(bean.getRespuesta());
			    			}
			    			
			    			//BeanConexion beanCon = sql.conexionSimple("select id_producto, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as nombre, cantidad from "+SqlConf.obtenerBase()+"inventario.almacen a where a.id_salida = "+id);
			    			BeanConexion beanCon = sql.conexionSimple("select id_producto, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as nombre, cantidad from "+SqlConf.obtenerBase()+"inventario.almacen a where a.id_salida = "+bean.getDato()+" and custid in ("+usuario.getCustidsRelacionados()+")");
			    			
			    			if(!beanCon.getRespuesta().equals("OK")){
			    				throw new Exception(beanCon.getRespuesta());
			    			}
			    			
			    			ResultSet rs = beanCon.getRs();
						
						
							while(rs.next()){
								
								tablaInterna.addItem(new Object[]{rs.getString("id_producto"), rs.getString("nombre"), rs.getString("cantidad")}, rs.getString("id_producto"));
								
							}
							
							rs = null;
			    			
						
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							sql.cerrar();
							sql = null;
						}
						
						Button registrar = new Button("Eliminar");
						registrar.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {
						    	
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarla?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
						    	
								    		Mysql sql = new Mysql();
								    		String respuesta;
								    		
								    		try{
								    			
								    			//Obtengo id_salida
								    			BeanConsulta bean = sql.consultaSimple("select id_salida from "+SqlConf.obtenerBase()+"inventario.almacen where id = "+id);
								    			
								    			if(!bean.getRespuesta().equals("OK")){
								    				throw new Exception(bean.getRespuesta());
								    			}
								    			
								    			respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.almacen set activo = 'NO' where id_salida = "+bean.getDato()+" and custid in ("+usuario.getCustidsRelacionados()+")");
								    			
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}else{
				    				    			
													Notification n = new Notification("Registro eliminado correctamente", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
					    				    		
					    				    		ventanaRegistrar.close();
					    				    		
					    					    	generarTablaSalidas(tablas, usuario);
					    					    	
				    				    		}
								    			
								    		}catch(Exception e){
								    			e.printStackTrace();
								    		}finally{
								    			sql.cerrar();
								    			sql = null;
								    			respuesta = null;
								    		}
		    			                }
		    			            }
		    					});
							    		
							    }
							});
		
		
						GridLayout grid = new GridLayout(1, 7);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						
						HorizontalLayout datos = new HorizontalLayout();
							datos.setWidth("90%");
							datos.setMargin(true);
							datos.setCaption("Datos");
							datos.setStyleName(ValoTheme.LAYOUT_WELL);
							
							datos.addComponent(fecha);
								datos.setComponentAlignment(fecha, Alignment.MIDDLE_RIGHT);
						
						grid.addComponent(datos, 0, 0);
							grid.setComponentAlignment(datos, Alignment.MIDDLE_CENTER);
						
						grid.addComponent(tablaInterna, 0, 1);
							grid.setComponentAlignment(tablaInterna, Alignment.MIDDLE_CENTER);
							
						grid.addComponent(comentarios, 0, 4);
							grid.setComponentAlignment(comentarios, Alignment.MIDDLE_CENTER);
							
						grid.addComponent(registrar, 0, 5);
							grid.setComponentAlignment(registrar, Alignment.BOTTOM_RIGHT);
					
						ventanaRegistrar.setContent(grid);
						
						UI.getCurrent().addWindow(ventanaRegistrar);
		
		    	}
		    }
		});
				
				
		return respuesta;
	}

	
	
	//Empiezan métodos externos
	
		private AutocompleteSuggestionProvider listaCategorias(String custid){
			
			List<String> nombres = new ArrayList<String>();
			Mysql sql = new Mysql();
			
			try{
				
				BeanConexion beanCon = sql.conexionSimple("select categoria from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+custid+"' group by categoria");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				ResultSet rs = beanCon.getRs();
				
				while(rs.next()){
					nombres.add(rs.getString("categoria"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql =  null;
			}
			
			AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(nombres, MatchMode.CONTAINS, true, Locale.US);
			return suggestionProvider;
			
		}
	
		private AutocompleteSuggestionProvider listaNombres(String custid){
			
			List<String> nombres = new ArrayList<String>();
			Mysql sql = new Mysql();
			
			try{
				
				BeanConexion beanCon = sql.conexionSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+custid+"' group by nombre");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				ResultSet rs = beanCon.getRs();
				
				while(rs.next()){
					nombres.add(rs.getString("nombre"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
			}
			
			AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(nombres, MatchMode.CONTAINS, true, Locale.US);
			return suggestionProvider;
			
		}
		
		private ComboBox llenarComboBox(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Productos");
				
				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.productos where custid in ("+custid+")");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				ResultSet rs = beanCon.getRs();
				
				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		private ComboBox llenarComboBoxCliente(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Clientes");
				
				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.cientes where custid = '"+custid+"'");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				ResultSet rs = beanCon.getRs();

				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		
		private Date getFirstDateOfCurrentMonth() {
			  Calendar cal = Calendar.getInstance();
			  cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			  return cal.getTime();
			}
		
}

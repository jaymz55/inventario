package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.ui.NumberField;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;

import com.example.inventario.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
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
import conexiones.BeanConsultaMultiple;
import conexiones.Mysql;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import funciones.Encriptar;
import funciones.Funcion;

public class VentasExpress {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
		final HorizontalLayout titulo = new HorizontalLayout();
		titulo.setHeight("70");
		titulo.setWidth("90%");
		
		Label tituloLabel = new Label("Registro de ventas");
		tituloLabel.setStyleName(ValoTheme.LABEL_H1);
		
		titulo.addComponent(tituloLabel);
		
		respuesta.addComponent(titulo);
		
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
				fechaUno.setDateFormat("dd MMMM yyyy");
				fechaUno.setValue(getFirstDateOfCurrentMonth());
				fechaUno.setStyleName("ajustado");
				
				
			final DateField fechaDos = new DateField();
			fechaDos.setDateFormat("dd MMMM yyyy");
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

				
			final Button insertar = new Button("Venta");
			insertar.setStyleName("boton_registrar_nuevo_dos");
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
						final Window ventanaRegistrar = new Window("Registrar salida / venta");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
						
						final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						producto.setWidth("80%");
				    	
						final ComboBox cliente = llenarComboBoxCliente(new ComboBox(), usuario.getCustidsRelacionados());
						cliente.setWidth("80%");
				    	
						final NumberField cantidad = new NumberField("Cantidad");
							cantidad.setNullRepresentation("");
							cantidad.setDecimalPrecision(2);
							cantidad.setErrorText("Número no válido");
							cantidad.setInvalidAllowed(false);
							cantidad.setNegativeAllowed(false);
							cantidad.setImmediate(true);
							
						final HorizontalLayout montos = new HorizontalLayout();
							montos.setWidth("80%");
							
						final NumberField precio = new NumberField("Precio");
							precio.setNullRepresentation("");
							precio.setDecimalPrecision(2);
							precio.setErrorText("Número no válido");
							precio.setInvalidAllowed(false);
							precio.setNegativeAllowed(false);
							precio.setWidth("50%");
							
						final NumberField descuento = new NumberField("Descuento");
							descuento.setNullRepresentation("");
							descuento.setDecimalPrecision(2);
							descuento.setErrorText("Número no válido");
							descuento.setInvalidAllowed(false);
							descuento.setNegativeAllowed(false);
							descuento.setWidth("50%");
							
						final TextArea comentarios = new TextArea("Comentarios");
							comentarios.setWidth("80%");
							comentarios.setMaxLength(500);
							
							
					//Pop para comentarios de descuento
						final VerticalLayout popupContent = new VerticalLayout();
								//popupContent.setWidth("400");
								popupContent.setMargin(true);
								
							final TextArea descComentario = new TextArea("Comentario");
								descComentario.setRows(5);
								descComentario.setColumns(30);
								descComentario.setMaxLength(500);
								popupContent.addComponent(descComentario);
								popupContent.setComponentAlignment(descComentario, Alignment.MIDDLE_CENTER);
								
								PopupView popup = new PopupView("Comentario del descuento", popupContent);
								popup.setHideOnMouseOut(false);
								
						montos.addComponent(precio);
							montos.setComponentAlignment(precio, Alignment.MIDDLE_LEFT);
						montos.addComponent(descuento);
							montos.setComponentAlignment(descuento, Alignment.MIDDLE_CENTER);
						montos.addComponent(popup);
							montos.setComponentAlignment(popup, Alignment.BOTTOM_RIGHT);
							
							cantidad.addTextChangeListener(new TextChangeListener() {
							    public void textChange(TextChangeEvent event) {/*addValueChangeListener(new Property.ValueChangeListener() {
							    public void valueChange(ValueChangeEvent event) {*/
								    	
								    	if(producto.getValue() != null && !event.getText().equals("")){
								    		
								    		Mysql sql = new Mysql();
								    		
								    		try{

									 		    BeanConsulta bean = sql.consultaSimple("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
									 		    
									    		if(!bean.getRespuesta().equals("OK")){
									    			throw new Exception(bean.getRespuesta());
									    		}
								    			
									    		double precioProducto = Double.parseDouble(bean.getDato());
								    			
								    			//double precioProducto = Double.parseDouble(sql.consultaSimple("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString()));
								    			precio.setValue(precioProducto * Double.parseDouble(event.getText()));
								    			
								    		}catch(Exception e){
								    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
								    			e.printStackTrace();
								    		}finally{
								    			sql.cerrar();
								    		}

								    	//}
								    	
								    }else{
								    	precio.setValue("");
								    }
								    }
								});
							
						final DateField fechaVenta = new DateField("Fecha de venta");
							fechaVenta.setDateFormat("dd MMMM yyyy");
							//fechaVenta.setStyleName(ValoTheme.DATEFIELD_SMALL);
							
						final ComboBox vendedor = llenarComboBoxVendedor(new ComboBox(), usuario.getCustidsRelacionados());
							vendedor.setWidth("60%");
							
						final NumberField existencia = new NumberField("Existencia");
							existencia.setEnabled(false);
						
						//Listener para registrar existencia cuando se escoge producto
							producto.addListener(new Property.ValueChangeListener() {
					            private static final long serialVersionUID = -5188369735622627751L;

					            public void valueChange(ValueChangeEvent event) {
							    		
							    		Mysql sql = new Mysql();
							    		
							    		try{
							    				
												BeanConexion beanCon = sql.conexionSimple("select sum(cantidad) as cantidad from "+SqlConf.obtenerBase()+"inventario.almacen where custid in ("+usuario.getCustidsRelacionados()+") and id_producto = "+producto.getValue()+"");
												
												if(!beanCon.getRespuesta().equals("OK")){
													throw new Exception(beanCon.getRespuesta());
												}
												
												ResultSet rs = beanCon.getRs();
							    				
							    				while(rs.next()){
							    					
							    					existencia.setValue(rs.getDouble("cantidad"));
							    					
							    				}
							    			
							    		}catch(Exception e){
							    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
							    			e.printStackTrace();
							    		}finally{
							    			sql.cerrar();
							    		}

							    }
							});
							
						//Listener para agregar vendedor asignado al momento de cambiar de cliente
							cliente.addListener(new Property.ValueChangeListener() {
					            private static final long serialVersionUID = -5188369735622627751L;

					            public void valueChange(ValueChangeEvent event) {
					            	
					            	if(cliente.getValue() != null){
					            	
						            	Mysql sql = new Mysql();
						            	
							    		try{
							    				
											BeanConexion beanCon = sql.conexionSimple("select vendedor_asignado from "+SqlConf.obtenerBase()+"inventario.clientes where id = "+cliente.getValue().toString());
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
											
											ResultSet rs = beanCon.getRs();
							    				
							    				while(rs.next()){
							    					
							    					vendedor.setValue(rs.getObject("vendedor_asignado"));
							    					
							    				}
							    			
							    		}catch(Exception e){
							    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
							    			e.printStackTrace();
							    		}finally{
							    			sql.cerrar();
							    		}
							    		
					            	}
					            }
							});
							
							Button registrar = new Button("Registrar");
							registrar.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							    	
							    	if(producto.getValue() != null && !cantidad.getValue().equals("") && !precio.getValue().equals("") && fechaVenta.getValue() != null){
							    	
							    		Mysql sql = new Mysql();
							    		Mysql sql2 = new Mysql();
							    		String respuesta = "NO";
							    		
							    		try{
							    			
							    			//Abro transacción
							    			sql.transaccionAbrir();
							    			
								    			String clienteAjuste = "";
								    			String vendedorAjuste = "";
								    			String descuentoAjuste = "";
								    			String descuentoDescAjuste = "";
								    			String comentariosAjuste = "";
								    			
								    			if(cliente.getValue() == null){
								    				clienteAjuste = "null";
								    			}else{
								    				clienteAjuste = cliente.getValue().toString();
								    			}
								    			
								    			if(vendedor.getValue() == null){
								    				vendedorAjuste = "null";
								    			}else{
								    				vendedorAjuste = "'" + vendedor.getItemCaption(vendedor.getValue()) + "'";
								    			}
	
								    			if(descuento.getValue().equals("")){
								    				descuentoAjuste = "0.0";
								    				descuentoDescAjuste = "null";
								    			}else{
								    				descuentoAjuste = descuento.getValue().toString();
								    				descuentoDescAjuste = "'" + descComentario.getValue() + "'";
								    			}
								    			
								    			if(comentarios.getValue().equals("")){
								    				comentariosAjuste = "null";
								    			}else{
								    				comentariosAjuste = "'"+comentarios.getValue()+"'";
								    			}
								    			
								    			Vector<String> vector = new Vector<>();
								    				vector.add("select precio from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    				vector.add("select iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    				vector.add("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    				
								    				BeanConsultaMultiple bean = sql.consultaMultiple(vector);
								    			
										    		if(!bean.getRespuesta().equals("OK")){
										    			throw new Exception(bean.getRespuesta());
										    		}
										    		
										    		Vector<String> respuestas = bean.getDatos();
										    			String precioString = respuestas.elementAt(0);
										    			String ivaString = respuestas.elementAt(1);
										    			String totalString = respuestas.elementAt(2);
								    				
								    			//String precioString = sql.consultaSimple("select precio from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    			//String ivaString = sql.consultaSimple("select iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    			//String totalString = sql.consultaSimple("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    			
										    //Grava iva
										    BeanConsulta beanCon = sql.consultaSimple("select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
										    	
										    if(!beanCon.getRespuesta().equals("OK")){
										    	throw new Exception(bean.getRespuesta());
										    }
							    			
										    
							    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.ventas values ("
							    					+ "null, '"+usuario.getCustid()+"','"+producto.getValue().toString()+"',"+clienteAjuste+","+precioString+","+ivaString+","+totalString+","+descuentoAjuste+","+descuentoDescAjuste+",'"+Funcion.fechaFormato(fechaVenta.getValue(), "yyyy-MM-dd")+"',"+cantidad.getValue()+","+vendedorAjuste+",'"+beanCon.getDato()+"',"+comentariosAjuste+",'SI')");
							    			
			    				    		if(!respuesta.equals("OK")){
			    				    			throw new Exception(respuesta);
			    				    		}
							    			
							    			//Registro salida de producto de almacén sólo si registra bien la venta
							    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.almacen values ("+
							    					"null, "+usuario.getCustid()+", "+producto.getValue().toString()+",'"+Funcion.fechaFormato(fechaVenta.getValue(), "yyyy-MM-dd")+"',"+(Double.parseDouble(cantidad.getValue())*-1)+",'VENTA')");
							    			
			    				    		if(!respuesta.equals("OK")){
			    				    			throw new Exception(respuesta);
			    				    		}
			    				    		
			    				    		if(respuesta.equals("OK")){

			    				    			//Cierro transacción
			    				    				sql.transaccionCommit();
			    				    				sql.transaccionCerrar();
			    				    			
								    			producto.setValue(null);
								    			cliente.setValue(null);
								    			precio.setValue("");
								    			cantidad.setValue("");
								    			vendedor.setValue(null);
								    			descuento.setValue("");
								    			descComentario.setValue("");
								    			existencia.setValue("");
								    			
												Notification n = new Notification("Registro correcto de venta", Type.TRAY_NOTIFICATION);
												n.setDelayMsec(2000);
												n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
												n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
												n.show(UI.getCurrent().getPage());
							    			
			    				    		}
			    				    		
							    		}catch(Exception e){
							    			
							    			//Cierro transacción
							    				sql.transaccionRollBack();
							    				sql.transaccionCerrar();
							    			
							    			e.printStackTrace();
							    			Notification.show("Error en sistema: "+e.toString(), Type.ERROR_MESSAGE);
							    		}finally{
							    			sql.cerrar();
							    			sql2.cerrar();
							    		}
							    		
								    	cuerpo.removeAllComponents();
								    	cuerpo.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados(), Funcion.fechaFormato(fechaUno.getValue(), "yyyy-MM-dd"), Funcion.fechaFormato(fechaDos.getValue(), "yyyy-MM-dd")));
								    	
							    	}else{
							    		Notification.show("Se deben de ingresar todos los datos",  Type.WARNING_MESSAGE);
							    	}
								    		
								    }
								});
							
							
						//Botón de alta de cliente
							Button clienteAlta = new Button("Alta de cliente");
								clienteAlta.setStyleName(ValoTheme.BUTTON_LINK);

								clienteAlta.addListener(new Button.ClickListener() {
								    public void buttonClick(ClickEvent event) {
								   
								    final Window ventanaRegistrar = new Window("Registrar cliente");
										ventanaRegistrar.center();
										ventanaRegistrar.setHeight("70%");
										ventanaRegistrar.setWidth("85%");
										ventanaRegistrar.setStyleName("VentanaSecundaria");
								    	
									final TextField nombre = new TextField("Cliente");
										nombre.setWidth("80%");
										nombre.setMaxLength(500);

									final TextField telefono = new TextField("Teléfono");
										telefono.setWidth("80%");
										telefono.setMaxLength(100);
									final TextField correo = new TextField("Correo electrónico");
										correo.setWidth("80%");
										correo.setMaxLength(500);
									final ComboBox vendedorAsignado = llenarComboBoxVendedoresAsignados(new ComboBox(), usuario.getCustidsRelacionados());
										vendedorAsignado.setWidth("60%");
									final TextArea direccion = new TextArea("Dirección");
										direccion.setWidth("80%");
										direccion.setMaxLength(1000);
									final TextArea observaciones = new TextArea("Observaciones");
										observaciones.setWidth("80%");
										observaciones.setMaxLength(1000);
									
									Button registrar = new Button("Registrar");
									registrar.addListener(new Button.ClickListener() {
									    public void buttonClick(ClickEvent event) {
									    	
									    	if(nombre.getValue().equals("")){
									    		Notification.show("Se debe de ingresar un nombre", Type.WARNING_MESSAGE);
									    	}else{
									    	
											    	Mysql sql = new Mysql();
											    	try{
											    		
											    		String vendedorAsignadoAjustado = "null";
											    		if(vendedorAsignado.getValue() != null)
											    			vendedorAsignadoAjustado = vendedorAsignado.getValue().toString();
											    		
											    		sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.clientes values ("
											    				+ "null, '"+usuario.getCustid()+"','"+Encriptar.Encriptar(nombre.getValue())+"','"+Encriptar.Encriptar(telefono.getValue())+"',"
											    						+ "'"+Encriptar.Encriptar(correo.getValue())+"','"+Encriptar.Encriptar(direccion.getValue())+"',"
											    										+ "'"+Funcion.quitarComillas(observaciones.getValue())+"',"+vendedorAsignadoAjustado+",'SI')");
											    		
											    	nombre.setValue("");
													telefono.setValue("");
													correo.setValue("");
													vendedorAsignado.setValue(null);
													direccion.setValue("");
													observaciones.setValue("");
							
													Notification n = new Notification("Registro correcto de cliente", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
													
													
													//Actualizo Combo de clientes
												    	cliente.removeAllItems();
														BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.clientes where custid = '"+usuario.getCustid()+"' and activo = 'SI'");
														
														if(!beanCon.getRespuesta().equals("OK")){
															throw new Exception(beanCon.getRespuesta());
														}
														
														ResultSet rs = beanCon.getRs();
														
														while(rs.next()){
															cliente.addItem(rs.getInt("id"));
															cliente.setItemCaption(rs.getInt("id"), Encriptar.Desencriptar(rs.getString("nombre")));
														}
	
											    	}catch(Exception e){
											    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
											    		e.printStackTrace();
											    	}finally{
											    		sql.cerrar();
											    	}					    	
										    	
									    	}
										    }
										});
											
											GridLayout grid = new GridLayout(2, 4);
												grid.setMargin(true);
												grid.setWidth("100%");
												grid.setHeight("100%");
												grid.addComponent(nombre, 0, 0);
												grid.addComponent(telefono, 1, 0);
												grid.addComponent(correo, 0, 1);
												grid.addComponent(vendedorAsignado, 1, 1);
												grid.addComponent(direccion, 0, 2);
												grid.addComponent(observaciones, 1, 2);
												grid.addComponent(registrar, 0, 3);
													grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
											
												ventanaRegistrar.setContent(grid);
												
												UI.getCurrent().addWindow(ventanaRegistrar);
								    	
								    }
								});
							
							GridLayout grid = new GridLayout(2, 7);
							grid.setMargin(true);
							grid.setWidth("100%");
							grid.setHeight("100%");
							grid.addComponent(producto, 0, 0);
							grid.addComponent(vendedor, 1, 0);
							grid.addComponent(cantidad, 0, 1);
							grid.addComponent(montos, 1, 1);
							grid.addComponent(fechaVenta, 0, 2);
							grid.addComponent(existencia, 1, 2);
							grid.addComponent(cliente, 0, 3);
							grid.addComponent(comentarios, 1, 3, 1, 5);
								//grid.setComponentAlignment(clienteAlta, Alignment.MIDDLE_LEFT);
							grid.addComponent(clienteAlta, 0, 4);
								grid.setComponentAlignment(clienteAlta, Alignment.MIDDLE_CENTER);
							grid.addComponent(registrar, 0, 5);
								grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
						
							ventanaRegistrar.setContent(grid);
							
							UI.getCurrent().addWindow(ventanaRegistrar);
						
				    }
				});
		
//Botón de alta de vendedor
				
				final Button salida = new Button("Salida");
					salida.setStyleName("boton_simple");
				//vendedorAlta.setStyleName(ValoTheme.BUTTON_LINK);
			
			//Empieza ventana de vendedores
					salida.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				   
				    	final Window ventanaRegistrar = new Window("Registrar vendedores");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("75%");
						
						final ComboBox vendedor = llenarComboBoxVendedor(new ComboBox(), usuario.getCustidsRelacionados());
							vendedor.setWidth("60%");
						
						final TextField nombre = new TextField("Nombre");
							nombre.setWidth("80%");
						
						final TextArea observaciones = new TextArea("Observaciones");
							observaciones.setWidth("80%");
							observaciones.setMaxLength(1000);
							
							Button registrar = new Button("Registrar vendedor");
							registrar.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							    	
							    	if(nombre.getValue().equals("")){
							    		Notification.show("Se debe de ingresar un nombre", Type.WARNING_MESSAGE);
							    	}else{
							    	
									    	Mysql sql = new Mysql();
									    	try{
									    		
									    		sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.vendedores values ("
									    				+ "null, '"+usuario.getCustid()+"','"+Funcion.quitarComillas(nombre.getValue())+"',"
									    										+ "'"+Funcion.quitarComillas(observaciones.getValue())+"')");
									    		
									    	nombre.setValue("");
											observaciones.setValue("");
					
										//Actualizar vendedor
											vendedor.removeAllItems();
											
											BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid = '"+usuario.getCustid()+"'");
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
											
											ResultSet rs = beanCon.getRs();
											
											while(rs.next()){
												vendedor.addItem(rs.getInt("id"));
												vendedor.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
											}
											
											Notification n = new Notification("Registro correcto de vendedor", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
											
									    	}catch(Exception e){
									    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
									    		e.printStackTrace();
									    	}finally{
									    		sql.cerrar();
									    	}
									    	
									    	//dos.removeAllComponents();
									    	//dos.addComponent(generarTabla(tablas, usuario.getCustid()));
								    	
							    	}
								    }
								});
									
							Button eliminar = new Button("Eliminar vendedor");
							eliminar.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							    	
							    	if(vendedor.getValue() == null){
							    		Notification.show("Se debe de ingresar un nombre", Type.WARNING_MESSAGE);
							    	}else{
							    	
				    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
				    							"SI", "NO", new ConfirmDialog.Listener() {

				    			            public void onClose(ConfirmDialog dialog) {
				    			                if (dialog.isConfirmed()) {
							    		
									    	Mysql sql = new Mysql();
									    	try{
									    		
									    		sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.vendedores where id = "+vendedor.getValue());
									    		
					
									    //Actualizo vendedor
									    	vendedor.removeAllItems();
									    	
											BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid = '"+usuario.getCustid()+"'");
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
											
											ResultSet rs = beanCon.getRs();
									    	
											while(rs.next()){
												vendedor.addItem(rs.getInt("id"));
												vendedor.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
											}
									    	
									    		
											Notification n = new Notification("Registro de vendedor eliminado correctamente", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
									    		
									    	}catch(Exception e){
									    		Notification.show("Error al eliminar: "+e.toString(), Type.ERROR_MESSAGE);
									    		e.printStackTrace();
									    	}finally{
									    		sql.cerrar();
									    	}
									    	
				    			        }
				    			     }
				    			            
				    				});
								    	
							    	}
								    }
								});
							
									GridLayout grid = new GridLayout(2, 4);
										grid.setMargin(true);
										grid.setWidth("100%");
										grid.setHeight("100%");
										grid.addComponent(nombre, 0, 0);
										grid.addComponent(vendedor, 1, 0);
										grid.addComponent(observaciones, 0, 1);
										grid.addComponent(registrar, 0, 2);
											grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
										grid.addComponent(eliminar, 1, 1);
										grid.setComponentAlignment(eliminar, Alignment.TOP_LEFT);
											
										ventanaRegistrar.setContent(grid);
										
										UI.getCurrent().addWindow(ventanaRegistrar);
				    	
				    }
		    	});
				
				
				
			//Botón de alta de vendedor
				
				final Button vendedorAlta = new Button("Vendedores");
					vendedorAlta.setStyleName("boton_simple");
				//vendedorAlta.setStyleName(ValoTheme.BUTTON_LINK);
			
			//Empieza ventana de vendedores
		    	vendedorAlta.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				   
				    	final Window ventanaRegistrar = new Window("Registrar vendedores");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("75%");
						
						final ComboBox vendedor = llenarComboBoxVendedor(new ComboBox(), usuario.getCustidsRelacionados());
							vendedor.setWidth("60%");
						
						final TextField nombre = new TextField("Nombre");
							nombre.setWidth("80%");
						
						final TextArea observaciones = new TextArea("Observaciones");
							observaciones.setWidth("80%");
							observaciones.setMaxLength(1000);
							
							Button registrar = new Button("Registrar vendedor");
							registrar.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							    	
							    	if(nombre.getValue().equals("")){
							    		Notification.show("Se debe de ingresar un nombre", Type.WARNING_MESSAGE);
							    	}else{
							    	
									    	Mysql sql = new Mysql();
									    	try{
									    		
									    		sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.vendedores values ("
									    				+ "null, '"+usuario.getCustid()+"','"+Funcion.quitarComillas(nombre.getValue())+"',"
									    										+ "'"+Funcion.quitarComillas(observaciones.getValue())+"')");
									    		
									    	nombre.setValue("");
											observaciones.setValue("");
					
										//Actualizar vendedor
											vendedor.removeAllItems();
											
											BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid = '"+usuario.getCustid()+"'");
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
											
											ResultSet rs = beanCon.getRs();
											
											while(rs.next()){
												vendedor.addItem(rs.getInt("id"));
												vendedor.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
											}
											
											Notification n = new Notification("Registro correcto de vendedor", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
											
									    	}catch(Exception e){
									    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
									    		e.printStackTrace();
									    	}finally{
									    		sql.cerrar();
									    	}
									    	
									    	//dos.removeAllComponents();
									    	//dos.addComponent(generarTabla(tablas, usuario.getCustid()));
								    	
							    	}
								    }
								});
									
							Button eliminar = new Button("Eliminar vendedor");
							eliminar.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							    	
							    	if(vendedor.getValue() == null){
							    		Notification.show("Se debe de ingresar un nombre", Type.WARNING_MESSAGE);
							    	}else{
							    	
				    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
				    							"SI", "NO", new ConfirmDialog.Listener() {

				    			            public void onClose(ConfirmDialog dialog) {
				    			                if (dialog.isConfirmed()) {
							    		
									    	Mysql sql = new Mysql();
									    	try{
									    		
									    		sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.vendedores where id = "+vendedor.getValue());
									    		
					
									    //Actualizo vendedor
									    	vendedor.removeAllItems();
									    	
											BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid = '"+usuario.getCustid()+"'");
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
											
											ResultSet rs = beanCon.getRs();
									    	
											while(rs.next()){
												vendedor.addItem(rs.getInt("id"));
												vendedor.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
											}
									    	
									    		
											Notification n = new Notification("Registro de vendedor eliminado correctamente", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
									    		
									    	}catch(Exception e){
									    		Notification.show("Error al eliminar: "+e.toString(), Type.ERROR_MESSAGE);
									    		e.printStackTrace();
									    	}finally{
									    		sql.cerrar();
									    	}
									    	
				    			        }
				    			     }
				    			            
				    				});
								    	
							    	}
								    }
								});
							
									GridLayout grid = new GridLayout(2, 4);
										grid.setMargin(true);
										grid.setWidth("100%");
										grid.setHeight("100%");
										grid.addComponent(nombre, 0, 0);
										grid.addComponent(vendedor, 1, 0);
										grid.addComponent(observaciones, 0, 1);
										grid.addComponent(registrar, 0, 2);
											grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
										grid.addComponent(eliminar, 1, 1);
										grid.setComponentAlignment(eliminar, Alignment.TOP_LEFT);
											
										ventanaRegistrar.setContent(grid);
										
										UI.getCurrent().addWindow(ventanaRegistrar);
				    	
				    }
		    	});
				
				
				HorizontalLayout insertarLay = new HorizontalLayout();
					insertarLay.setCaption("Insertar");
					insertarLay.setMargin(true);
					insertarLay.setStyleName(ValoTheme.LAYOUT_WELL);
					insertarLay.setWidth("100%");
					insertarLay.setHeight("100");
					
					
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

				HorizontalLayout registros = new HorizontalLayout();
				registros.setWidth("70%");
				registros.setHeight("100");
				registros.setCaption("Registros");
				registros.setMargin(true);
				registros.setStyleName(ValoTheme.LAYOUT_WELL);
				registros.addComponent(salida);
					registros.setComponentAlignment(salida, Alignment.MIDDLE_LEFT);
				registros.addComponent(insertar);
					registros.setComponentAlignment(insertar, Alignment.MIDDLE_CENTER);
				registros.addComponent(vendedorAlta);
					registros.setComponentAlignment(vendedorAlta, Alignment.MIDDLE_RIGHT);

				cabecera.addComponent(registros);
				cabecera.addComponent(reporte);
				
			respuesta.addComponent(cabecera);
				//respuesta.setComponentAlignment(cabecera, Alignment.BOTTOM_RIGHT);
				cuerpo.addComponent(tablas);
			respuesta.addComponent(cuerpo);
			
		return respuesta;
		
	}
	
	//Empiezan métodos externos
	
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid, String fechaInicial, String fechaFinal){
		
		Mysql sql = new Mysql();
		
		try{

			tablas.removeAllComponents();
			
			BeanConexion beanCon = sql.conexionSimple("select id,\r\n" + 
					"  (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as producto,\r\n" + 
					"  (select nombre from "+SqlConf.obtenerBase()+"inventario.clientes where id = a.id_cliente) as cliente,\r\n" + 
					"  fecha,\r\n" + 
					"  cantidad,\r\n" + 
					"  (precio*cantidad) as subtotal ,\r\n" + 
					"\r\n" + 
					"  if((select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) = 'SI',\r\n" + 
					"  round(((descuento - (descuento)*.16)),2), round(descuento,2)) as descuentos,\r\n" + 
					"\r\n" + 
					"  if((select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) = 'SI',\r\n" + 
					"  round(((precio*cantidad)-((descuento - (descuento)*.16)))*.16,2), (iva*cantidad)) as impuesto,\r\n" + 
					"\r\n" + 
					"  ((select subtotal) - (select descuentos)) + (select impuesto) as total,\r\n" + 
					"\r\n" + 
					"  concat('(',total*cantidad,')') as 'Total sin descuento',\r\n" + 
					"  concat('(',descuento,')') as 'Descuento total otorgado',\r\n" + 
					"\r\n" + 
					"  descuento_desc, \r\n" + 
					" vendedor, comentarios " +
					"from "+SqlConf.obtenerBase()+"inventario.ventas a\r\n" + 
					"where custid in ("+custid+")\r\n" + 
					"  and fecha between '"+fechaInicial+"' and '"+fechaFinal+"'");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			ResultSet rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"PRODUCTO","CLIENTE","FECHA","CANTIDAD","SUBTOTAL","DESCUENTOS","IMPUESTO","TOTAL","TOTAL SIN DESCUENTO","DESCUENTO TOTAL OTORGADO","DESCUENTO_DESC","VENDEDOR","COMENTARIOS"};
			tablas.addComponent(crearCon2FiltrosVentas(tablas, rs, "PRODUCTO", "CLIENTE", columnasExportar, fechaInicial, fechaFinal));
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
	public VerticalLayout crearCon2FiltrosVentas(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final Object[] columnasExportar, final String fechaInicial, final String fechaFinal) throws UnsupportedOperationException, Exception{

		VerticalLayout respuesta = new VerticalLayout();
		respuesta.setWidth("95%");
		
		HorizontalLayout interior = new HorizontalLayout();
		interior.setMargin(true);
		interior.setWidth("50%");
		// Text field for inputting a filter
		final TextField filtro = new TextField("Filtro por "+tituloColumnaFiltrar.toLowerCase());
		final TextField filtro2 = new TextField("Filtro por "+tituloColumnaFiltrar2.toLowerCase());
		//tf.focus();
		interior.addComponent(filtro);
			interior.setComponentAlignment(filtro, Alignment.MIDDLE_LEFT);
		interior.addComponent(filtro2);
			interior.setComponentAlignment(filtro2, Alignment.MIDDLE_CENTER);
		
		
		respuesta.addComponent(interior);
		
		final Table tabla = new Table();
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("SUBTOTAL") || name.toUpperCase().equals("CANTIDAD") || 
					name.toUpperCase().equals("DESCUENTOS") || name.toUpperCase().equals("IMPUESTO") || 
					name.toUpperCase().equals("TOTAL")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
			}else if(name.toUpperCase().equals("FECHA") || name.toUpperCase().equals("TOTAL SIN DESCUENTO") || name.toUpperCase().equals("DESCUENTO TOTAL OTORGADO")){
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
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4)}, id);
			}else if(count==5){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getString(5)}, id);
			}else if(count==6){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6)}, id);
			}else if(count==7){
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getObject(5), rs.getObject(6), rs.getObject(7)}, id);
			}else if(count==8){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), Encriptar.Desencriptar(rs.getString(3)), rs.getDouble(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8)}, id);
			}else if(count==9){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), Encriptar.Desencriptar(rs.getString(3)), rs.getDouble(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getString(9)}, id);
			}else if(count==10){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), Encriptar.Desencriptar(rs.getString(3)), rs.getDouble(4), rs.getString(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10)}, id);
			}else if(count==11){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)}, id);
			}else if(count==12){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), Encriptar.Desencriptar(rs.getString(3)), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12)}, id);
			}else if(count==13){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)}, id);
			}else if(count==14){ //Usa éste
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), Encriptar.Desencriptar(rs.getString(3)), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14)}, id);
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

		    		Property<String> itemProperty = event.getItem().getItemProperty("ID");
		    		final String id = itemProperty.getValue();

		    		final Usuario usuario = (Usuario) UI.getCurrent().getData();

		    		final Window ventanaActualizar = new Window("Eliminar");
		    		ventanaActualizar.center();
		    		ventanaActualizar.setHeight("20%");
		    		ventanaActualizar.setWidth("20%");
		    				
		    		final Mysql sql = new Mysql();
		    		
		    		try{
		    				   
		    			Button eliminar = new Button("Eliminar");
		    			eliminar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    		
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminar esta venta?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
				    				    Mysql sql = new Mysql();
				    				    String respuesta = "NO";
				    				    
				    			    	try{
				    			    	
				    			    		
				    			    		
				    			    		//*************Aquí tengo que empezar a definir relación entre ventas y almacén
				    			    		
				    			    		
				    			    		
				    			    		
				    			    			sql.insertarSimple("delete FROM "+SqlConf.obtenerBase()+"inventario.almacen where custid = "+usuario.getCustid()+" and fecha = (SELECT fecha FROM "+SqlConf.obtenerBase()+"inventario.ventas where id = "+id+") and movimiento = 'VENTA' and id_producto = (SELECT id_producto FROM "+SqlConf.obtenerBase()+"inventario.ventas where id = "+id+") limit 1");
				    			    			
				    				    		sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.ventas where id = "+id);
				    				    		generarTabla(tablas, usuario.getCustidsRelacionados(), fechaInicial, fechaFinal);
				    				    		
												Notification n = new Notification("Venta eliminada correctamente", Type.TRAY_NOTIFICATION);
												n.setDelayMsec(2000);
												n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
												n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
												n.show(UI.getCurrent().getPage());
				    				    		
				    				    		//Notification.show("Venta eliminada correctamente", Type.TRAY_NOTIFICATION);
				    				    		ventanaActualizar.close();
				    				    		
				    			    	}catch(Exception e){
				    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				    			    		e.printStackTrace();
				    			    	}finally{
				    			    		sql.cerrar();
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
		    				grid.setComponentAlignment(eliminar, Alignment.BOTTOM_LEFT);
		    				
		    			ventanaActualizar.setContent(grid);
		    			
		    			UI.getCurrent().addWindow(ventanaActualizar);
		    			
		    		}catch(Exception e){
		    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
		    			e.printStackTrace();
		    		}finally{
		    			sql.cerrar();
		    		}
		    		
		    		
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
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		private ComboBox llenarComboBoxCliente(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Clientes");
				
				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.clientes where custid in ("+custid+") and activo = 'SI'");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				ResultSet rs = beanCon.getRs();
				
				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), Encriptar.Desencriptar(rs.getString("nombre")));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		private ComboBox llenarComboBoxVendedor(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Vendedor");
				
				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid in ("+custid+")");
				
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
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		private ComboBox llenarComboBoxVendedoresAsignados(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Vendedor asignado");
				
				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid in ("+custid+") order by nombre");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				ResultSet rs = beanCon.getRs();
				
				combo.addItem(0);
				combo.setItemCaption(0, "Ninguno");
				
				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
				}
				
				combo.setNullSelectionAllowed(true);
				
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
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

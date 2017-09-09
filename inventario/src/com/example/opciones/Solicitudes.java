package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.ui.NumberField;

import pdf.Nuevo;
import sql.SqlConf;
import sql.DTO.UsuarioDTO;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
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
import com.vaadin.ui.Window.CloseEvent;
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
import correos.Correo;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import funciones.Encriptar;
import funciones.Funcion;

public class Solicitudes {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final HorizontalLayout cabecera = new HorizontalLayout();
			cabecera.setWidth("90%");
			
		final VerticalLayout tablas = new VerticalLayout();
			final VerticalLayout dos = new VerticalLayout();
			
		//Variables
			Mysql sql = new Mysql();
			final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
			try{
				
				final Button insertar = new Button("Registrar nueva solicitud");
				insertar.setStyleName("boton_registrar_nuevo");
				insertar.addListener(new Button.ClickListener() {
					
				    public void buttonClick(ClickEvent event) {
					
					final Window ventanaRegistrar = new Window("Registrar nueva solicitud");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("90%");
						ventanaRegistrar.setWidth("65%");
				    	
						
					final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						producto.setWidth("90%");
						producto.setInputPrompt("Productos");
						
					final NumberField cantidadCelda = new NumberField();
						cantidadCelda.setInputPrompt("Cantidad");
						cantidadCelda.setWidth("100");
					
					final DateField fecha = new DateField();
					 	fecha.setDateFormat("dd MMMM yyyy");
					 	fecha.setValue(new Date(System.currentTimeMillis() - 3600 * 7000));
					

					 final TextArea notaGrande = new TextArea();
					 	notaGrande.setInputPrompt("Encabezado");
					 	notaGrande.setWidth("100%");
					 	notaGrande.setHeight("100%");
					 	notaGrande.setMaxLength(2000);
					 	
					 final TextArea notaChica = new TextArea();
					 	notaChica.setInputPrompt("Texto");
					 	notaChica.setWidth("100%");
					 	notaChica.setHeight("100%");
					 	notaChica.setMaxLength(2000);
					 	
				//Popup para contener notas
					VerticalLayout popupNotas = new VerticalLayout();
						popupNotas.setWidth("600");
						popupNotas.setHeight("300");
						popupNotas.setMargin(true);
					 	
						popupNotas.addComponent(notaGrande);
						popupNotas.addComponent(notaChica);
						
					PopupView popup = new PopupView("Destinatario", popupNotas);
						popup.setHideOnMouseOut(false);
						
					 final TextField comentarios = new TextField();
					 	comentarios.setInputPrompt("Comentarios");
					 	comentarios.setWidth("90%");
					 	comentarios.setMaxLength(500);
					
					 //Agrego tabla
						final Table tabla = new Table();
						tabla.setLocale(Locale.US);
					 	
					 	
					 //Horizontal que contendr� a costo total y descuento
					 HorizontalLayout costos = new HorizontalLayout();
					 	costos.setMargin(true);
					 	costos.setWidth("90%");
					 	
					 final Label totalCostoLabel = new Label("Costo total: $0.00");
					 	totalCostoLabel.setSizeUndefined();
					 	totalCostoLabel.setStyleName("LabelCostoTotal");

					 final NumberField descuento = new NumberField();
						descuento.setInputPrompt("Descuento (%)");
						descuento.setNullRepresentation("0");
					 	
					//Agrego listener a descuento para ajustar costo total
						descuento.addListener(new TextChangeListener() {
				            public void textChange(TextChangeEvent event) {
						    	
				            	if(event.getText().equals("") || event.getText().equals("null")){
				            		
				            		descuento.clear();
				            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
							        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tabla, 0)));
				            		
				            	}else{
				            		
				            		//Funci�n para hacer descuento
				            		
				            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
							        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tabla, Double.parseDouble(event.getText()))));

				            	}
				            	
						    }
						});
						
						
					 costos.addComponent(descuento);
					 costos.addComponent(totalCostoLabel);
					 	costos.setComponentAlignment(totalCostoLabel, Alignment.MIDDLE_RIGHT);
					 
					 
					 	
					//Cuadro de componentes
					
					tabla.setHeight("200");
					tabla.setWidth("90%");
					tabla.setSelectable(true);
					
					tabla.addContainerProperty("ID", String.class, null);
					tabla.setColumnCollapsingAllowed(true);
					tabla.setColumnCollapsed("ID", true);
						tabla.addContainerProperty("PRODUCTO", String.class, null);
						tabla.addContainerProperty("CANTIDAD", String.class, null);
						tabla.addContainerProperty("PRECIO", Double.class, null);

						Button añadir = new Button("Agregar");
						añadir.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {
						    	
							    	if(producto.getValue() == null || cantidadCelda.getValue().equals("")){
							    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
							    	}else{
							    		
							    		//Busco costo de producto y multiplico por cantidad
							    		double costo = 0;
							    		
							    		Mysql sql = new Mysql();
							    		BeanConsulta bean = new BeanConsulta();
							    		
							    		try{
							    			
							    			bean = sql.consultaSimple("select ifnull(total,0) from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
							    			
							    			if(!bean.getRespuesta().equals("OK")){
							    				throw new Exception(bean.getRespuesta());
							    			}
							    			
							    			costo = Double.parseDouble(bean.getDato())*Double.parseDouble(cantidadCelda.getValue());
							    			
							    		}catch(Exception e){
							    			e.printStackTrace();
							    		}finally{
							    			sql.cerrar();
							    			sql = null;
							    			bean = null;
							    		}
							    		
							    		
							    		tabla.addItem(new Object[]{producto.getValue().toString(), producto.getItemCaption(producto.getValue()), cantidadCelda.getValue(), costo}, producto.getValue().toString());
							    		producto.setValue(null);
							    		cantidadCelda.setValue("");
							    		
							    		//Actualizo Costo Total
						            	if(descuento.getValue().equals("")){
						            		
						            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
									        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tabla, 0)));
						            		
						            	}else{
						            		
						            		//Funci�n para hacer descuento
						            		
						            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
									        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tabla, Double.parseDouble(descuento.getValue()))));
						            	}
							    		
							    	}
						    		
							    }
							});
						
						Button eliminar = new Button("Eliminar");
						eliminar.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {

						    	if(tabla.getValue() != null){
						    	
						    		tabla.removeItem(tabla.getValue());
						    		
						    		//Actualizo Costo Total
					            	if(descuento.getValue().equals("")){
					            		
					            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
								        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tabla, 0)));
					            		
					            	}else{
					            		
					            		//Funci�n para hacer descuento
					            		
					            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
								        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tabla, Double.parseDouble(descuento.getValue()))));
					            	}
						    		
						    	}else{
						    		Notification.show("Se debe escoger alg�n producto a eliminar", Type.WARNING_MESSAGE);
						    	}
						    	
							    }
							});
						
						
					
						Button registrar = new Button("Registrar");
						registrar.setStyleName("boton_simple");
						
						registrar.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {
						    	
						    	if(tabla.size() == 0){
						    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE); 
						    	}else{
						    	
								    	Mysql sql = new Mysql();
								    	try{
								    		
								    	//Abro transacci�n
								    		
								    		sql.transaccionAbrir();
								    		String respuesta = "NO";
								    			
								    			if(descuento.getValue().equals(""))								    		
									    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.solicitudes values ("
										    				+ "null, '"+usuario.getCustid()+"','"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"','"+comentarios.getValue()+"',null,'NO','NO','NO','SI',0)");
								    			
								    			else
									    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.solicitudes values ("
										    				+ "null, '"+usuario.getCustid()+"','"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"','"+comentarios.getValue()+"',null,'NO','NO','NO','SI',"+descuento.getValue()+")");
								    			
								    			if(!respuesta.equals("OK")){
								    				throw new Exception(respuesta);
								    			}
								    			
								    		
								    	//Agrego componentes
								    		
								    		//Obtengo id del producto reci�n agregado
								    		
								 		    String idSolicitud = "";
								 		    BeanConsulta bean = sql.consultaSimple("SELECT LAST_INSERT_ID()");
								 		    
								    		if(!bean.getRespuesta().equals("OK")){
								    			throw new Exception(bean.getRespuesta());
								    		}
							    			
								    		idSolicitud = bean.getDato();
								    		
									    	Collection<?> ids = tabla.getItemIds();
									    	Property<String> cantidad = null;
									    	Property<Double> precio = null;
									    	
									    	//String respuesta = "NO";
									    	
									        for (Object elem : ids) {
									        	
									        	cantidad = tabla.getItem(elem).getItemProperty("CANTIDAD");
									        	precio = tabla.getItem(elem).getItemProperty("PRECIO");
									        	respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.solicitudes_desglose values (null, '"+usuario.getCustid()+"','"+idSolicitud+"','"+elem+"','"+cantidad.getValue()+"',"+precio.getValue()+")");

								    			if(!respuesta.equals("OK")){
								    				throw new Exception(respuesta);
								    			}
									        	
									        }
									        
									        cantidad = null;
									        precio = null;
									        
									    //Verifico que respuesta sea S� para continuar
									        
									    if(respuesta.equals("OK")){
									    
									    //Cierro transacci�n
									    	sql.transaccionCommit();
									    	sql.transaccionCerrar();
									    	
									    	if(!usuario.getCorreoSolicitudes().equals("")){
									    	
										    	Correo correo = new Correo();
										    	
												String texto = "<STYLE TYPE='text/css'><!--.atxt {font-family: arial;	font-size: 12px;	color:#31309c;}a.atxt:link  {text-decoration: underline; color:#0000ff;}a.atxt:hover  {text-decoration: none; color:red;}.txt {font-family: Arial; font-size: 8pt; color: #000000}.txt2 {font-family: Arial; font-size: 9pt; font-weight: bold; color: #000000}.date {font-family: Arial; font-size: 9pt; font-weight: bold; color: #9c0000}.intro {font-family: Arial; font-size: 9pt; color: #000000}.ayuda {font-family: arial;	font-size: 11px;	color:#838283;}a.ayuda:link {font-family: arial;font-size: 11px;	color:#0000FF;}a.ayuda:hover {text-decoration: none;font-size: 11px;	color:#FF0000;}.boton {color:#FFFFFF; font-weight:bold; background-color:#333399;width: 60px; font-family:Arial; font-size:11px;}--></STYLE><table width='570' border='0' cellspacing='0' cellpadding='0' align='center'><tr><td align='center'><style type='text/css'><!--.a,.titblanco,.titazul1,.titnegro,.titnegro1,.titazul,.titbold,.tit {font-family:Arial,Helvetica,sans-serif;font-size:11px;font-style:normal;font-weight:normal;color:#0000ff}.tit {color:#000000}.titbold,.titblanco,.titazul1,.titnegro {font-size:12px;font-weight:bold;color:#32388b}.titazul,.titnegro1 {font-size:14px;font-style:normal;font-weight:bold;color:#32388b}.titnegro1 {font-size:10px;color:#000000}.titnegro,.titblanco {font-weight:normal;color:#000000}.titazul1 {font-weight:bold;color:#32388b}.titblanco {color:#ffffff}--> </style>"+
														"<table width='570' border='0' cellpadding='0' cellspacing='0' style=' text-align:left; font-family:Arial, Helvetica, sans-serif;'><tr><td width='3'></td><td width='175'><a href='http://www.tuprograma.mx'><img src='http://www.tuprograma.mx/imagenes/Tuprograma_chico.gif' width='178' height='48' border='0' / alt='TuPrograma.mx'></a></td><td width='372'></td>  </tr>	<tr><td></td><td></td><td></td></tr><tr><td colspan='3' style='border:1px solid #c1c1c1; border-bottom:0px; padding:20px 20px 10px 20px; font-size:12px; color:#333333; line-height:18px;'><p>Hola: </p><p>Se ha cargado una nueva solicitud de producci�n con n�mero <b>"+idSolicitud+"</b>.</p>"+
														"<p>Saludos,</p><p>TuPrograma.mx<br><a href='http://www.tuprograma.mx/tuinventario' target='_blank'>www.tuprograma.mx/tuinventario</a></p></td></tr><tr><td height='9' colspan='3' style='font-size:0;; border:1px solid #c1c1c1; border-top:0;'><!--<img src='http://www.mercadolibre.com/org-img/dmac/templates/generico/dmac_footer.gif' width='570' height='9' />--></td></tr><tr><td colspan='3' style='font-size:11px; color:#666666; text-align:center; padding-top:15px;'>Por favor no respondas este e-mail. Si tienes alguna duda o quieres contactarnos, ingresa al <a href='http://www.tuprograma.mx'>Portal de Ayuda.</a></td></tr></table></td></tr></table>";
												
												if(usuario.getCorreoSolicitudesCopia().equals("SI"))
													correo.nuevo(usuario.getCorreoSolicitudes(), usuario.getCorreo(),"Nueva solicitud de producci�n", texto);
												else
													correo.nuevo(usuario.getCorreoSolicitudes(), null,"Nueva solicitud de producci�n", texto);
	
									    	}
									    	
									    	producto.setValue("");
									    	cantidadCelda.setValue("");
											//fecha.setValue(null);
											comentarios.setValue("");
											tabla.removeAllItems();
											descuento.setValue("");
											totalCostoLabel.setValue("Costo total: $0.00");
					
											Notification n = new Notification("Registro de solicitud correcto", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
										
									    }

									    
								    	}catch(Exception e){
								    		
									    	//Cierro transacci�n
								    			sql.transaccionRollBack();
								    			sql.transaccionCerrar();
								    		
								    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
								    		e.printStackTrace();
								    		
								    	}finally{
								    		sql.cerrar();
								    	}
								    	
								    	dos.removeAllComponents();
								    	dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
								    	//actualizarColorTabla(tabla);
							    	
						    	    }
							    		
							    }
							});
						

						Button pdfExporter = new Button("PDF");
						pdfExporter.setStyleName("boton_simple_azul");
						
						pdfExporter.addListener(new Button.ClickListener() {
						    public void buttonClick(ClickEvent event) {
						    	
						    	//Genero el archivo
						    	Nuevo pdf = new Nuevo();
						    	
						    	//Ajusto descuento
						    	String descuentoAjustado;
	    			    		if(descuento.getValue() == null)
	    			    			descuentoAjustado = "0";
	    			    		else if(descuento.getValue().equals(""))
	    			    			descuentoAjustado = "0";
	    			    		else
	    			    			descuentoAjustado = descuento.getValue();
						    		
	    			    		String notaGrandeAjustada;
	    			    		if(notaGrande.getValue() == null)
	    			    			notaGrandeAjustada = "";
	    			    		else if(notaGrande.getValue().equals(""))
	    			    			notaGrandeAjustada = "";
	    			    		else
	    			    			notaGrandeAjustada = notaGrande.getValue();

	    			    		String notaChicaAjustada;
	    			    		if(notaChica.getValue() == null)
	    			    			notaChicaAjustada = "";
	    			    		else if(notaChica.getValue().equals(""))
	    			    			notaChicaAjustada = "";
	    			    		else
	    			    			notaChicaAjustada = notaChica.getValue();
	    			    		
						    	pdf.generarPdf(tabla, calcularDescuento(tabla, Double.parseDouble(descuentoAjustado)), notaGrandeAjustada, notaChicaAjustada);
						    	
						    	pdf = null;
						    	
						    }
						});	
						
					
							GridLayout grid = new GridLayout(1, 7);
								grid.setMargin(true);
								grid.setWidth("100%");
								grid.setHeight("100%");
								
								HorizontalLayout datos = new HorizontalLayout();
									datos.setWidth("90%");
									datos.setMargin(true);
									//datos.setCaption("Datos");
									datos.setStyleName(ValoTheme.LAYOUT_WELL);
									
									datos.addComponent(producto);
										datos.setComponentAlignment(producto, Alignment.MIDDLE_LEFT);
									datos.addComponent(cantidadCelda);
										datos.setComponentAlignment(cantidadCelda, Alignment.MIDDLE_CENTER);
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
								
								grid.addComponent(costos, 0, 3);
									grid.setComponentAlignment(costos, Alignment.MIDDLE_CENTER);
									
								grid.addComponent(comentarios, 0, 4);
									grid.setComponentAlignment(comentarios, Alignment.MIDDLE_CENTER);

								HorizontalLayout botonesLay = new HorizontalLayout();
									botonesLay.setWidth("100%");
									botonesLay.setMargin(true);
									botonesLay.addComponent(popup);
										botonesLay.setExpandRatio(popup, 2);
										botonesLay.setComponentAlignment(popup, Alignment.MIDDLE_CENTER);
									botonesLay.addComponent(pdfExporter);
										botonesLay.setExpandRatio(pdfExporter, 1);
									botonesLay.addComponent(registrar);
										botonesLay.setExpandRatio(registrar, 1);
									
								grid.addComponent(botonesLay, 0, 5);
									grid.setComponentAlignment(botonesLay, Alignment.BOTTOM_RIGHT);
							
								ventanaRegistrar.setContent(grid);
								
								UI.getCurrent().addWindow(ventanaRegistrar);
				    	
				    }
				});
				
				//Inserto tabla
				
				try{

					dos.addComponent(insertar);
					dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
						dos.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
				
				}catch(Exception e){
					Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				}finally{
					sql.cerrar();
				}
				
				//Titulo
				Label titulo = new Label("Solicitudes de producci�n");
				titulo.setStyleName(ValoTheme.LABEL_H1);
				
				cabecera.addComponent(titulo);
					cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
				
				cabecera.addComponent(insertar);
					cabecera.setComponentAlignment(insertar, Alignment.MIDDLE_RIGHT);
				respuesta.addComponent(cabecera);
					respuesta.setComponentAlignment(cabecera, Alignment.MIDDLE_LEFT);
				respuesta.addComponent(dos);

			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
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
			
			BeanConexion beanCon = sql.conexionSimple("SELECT vista, id, fecha, comentarios, completada FROM "+SqlConf.obtenerBase()+"inventario.solicitudes FORCE INDEX (custid) where custid in ("+custid+") and activo = 'SI' order by id desc");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			ResultSet rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel
			Object[] columnasExportar = {"ID","FECHA","COMENTARIOS","COMPLETADA"};
			tablas.addComponent(crearCon3FiltrosInventario(tablas, rs, "ID", "FECHA", "COMPLETADA", columnasExportar));
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
	public VerticalLayout crearCon3FiltrosInventario(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final String tituloColumnaFiltrar3, final Object[] columnasExportar) throws UnsupportedOperationException, Exception{

		VerticalLayout respuesta = new VerticalLayout();
		respuesta.setWidth("95%");
		
		HorizontalLayout interior = new HorizontalLayout();
		interior.setMargin(true);
		interior.setWidth("50%");
		// Text field for inputting a filter
		final TextField filtro = new TextField();
			filtro.setInputPrompt(tituloColumnaFiltrar);
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
			interior.setComponentAlignment(filtro2, Alignment.MIDDLE_LEFT);
		interior.addComponent(filtro3);
			interior.setComponentAlignment(filtro3, Alignment.MIDDLE_LEFT);
		
		
		respuesta.addComponent(interior);
		
		final Table tabla = new Table();
		tabla.setLocale(Locale.US);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("MINIMO") || name.toUpperCase().equals("MAXIMO")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
			}else if(name.toUpperCase().equals("COMPLETADA") || name.toUpperCase().equals("ID")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_CENTER);
				tabla.setColumnWidth(name.toUpperCase(), 150);
			}else if(name.toUpperCase().equals("FECHA")){
				tabla.addContainerProperty(name.toUpperCase(), String.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_CENTER);
				tabla.setColumnWidth(name.toUpperCase(), 150);
			}else if(name.toUpperCase().equals("VISTA")){
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
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)}, id);
			}else if(count==5){ //�ste es el que usa
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)}, id);
			}else if(count==6){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6)}, id);
			}else if(count==7){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), Encriptar.Desencriptar(rs.getString(5)), rs.getDouble(6), rs.getDouble(7)}, id);
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
		
		actualizarColorTabla(tabla);
		
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

		    		Property<String> itemProperty = event.getItem().getItemProperty("ID");
		    		final String id = itemProperty.getValue();

		    		final Property<String> itemPropertyVista = event.getItem().getItemProperty("VISTA");
		    		
		    		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		    				
					final Window ventanaActualizar = new Window("Actualizar");
					ventanaActualizar.center();
					ventanaActualizar.setHeight("95%");
					ventanaActualizar.setWidth("65%");
					
					ventanaActualizar.addListener(new Window.CloseListener() {
			            // inline close-listener
			            public void windowClose(CloseEvent e) {
			                
			            	itemPropertyVista.setValue("SI");
			            	actualizarColorTabla(tabla);

			            	
			            }
			        });
					
					final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						producto.setInputPrompt("Productos");
						producto.setWidth("90%");
						
					final NumberField cantidad = new NumberField();
						cantidad.setInputPrompt("Cantidad");
						cantidad.setWidth("100");
					
					final DateField fecha = new DateField();
					 	fecha.setDateFormat("dd MMMM yyyy");
					
					final TextField comentarios = new TextField();
						comentarios.setInputPrompt("Comentarios");
						comentarios.setWidth("90%");
						comentarios.setMaxLength(500);
		    		
						 final TextArea notaGrande = new TextArea();
						 	notaGrande.setInputPrompt("Encabezado");
						 	notaGrande.setWidth("100%");
						 	notaGrande.setHeight("100%");
						 	
						 final TextArea notaChica = new TextArea();
						 	notaChica.setInputPrompt("Texto");
						 	notaChica.setWidth("100%");
						 	notaChica.setHeight("100%");
						
							//Popup para contener notas
							VerticalLayout popupNotas = new VerticalLayout();
								popupNotas.setWidth("600");
								popupNotas.setHeight("300");
								popupNotas.setMargin(true);
							 	
								popupNotas.addComponent(notaGrande);
								popupNotas.addComponent(notaChica);
								
							PopupView popup = new PopupView("Destinatario", popupNotas);
								popup.setHideOnMouseOut(false);
								
						 	
					final Table tablaInterna = new Table();
						tablaInterna.setLocale(Locale.US);
						
					//Horizontal que contendr� a costo total y descuento
						 HorizontalLayout costos = new HorizontalLayout();
						 	costos.setMargin(true);
						 	costos.setWidth("90%");
						 	
						 final Label totalCostoLabel = new Label("Costo total: $0.00");
						 	totalCostoLabel.setSizeUndefined();
						 	totalCostoLabel.setStyleName("LabelCostoTotal");

						 final NumberField descuento = new NumberField();
							descuento.setInputPrompt("Descuento (%)");
							descuento.setCaption("Descuento (%)");
							descuento.setNullRepresentation("0");
						 	
						//Agrego listener a descuento para ajustar costo total
							descuento.addListener(new TextChangeListener() {
					            public void textChange(TextChangeEvent event) {
					            	
					            	if(event.getText().equals("") || event.getText().equals("null")){
					            		
					            		descuento.clear();
					            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
								        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, 0)));
					            		
					            	}else{
					            		
					            		//Funci�n para hacer descuento
					            		
					            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
								        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, Double.parseDouble(event.getText()))));

					            	}
					            	
							    }
							});
							
							
						 costos.addComponent(descuento);
						 costos.addComponent(totalCostoLabel);
						 	costos.setComponentAlignment(totalCostoLabel, Alignment.BOTTOM_RIGHT);
						
		    		
						//Cuadro de componentes						
						tablaInterna.setHeight("200");
						tablaInterna.setWidth("90%");
						tablaInterna.setSelectable(true);
						//tablaInterna.setCaption("Solicitud: "+id);
						
						tablaInterna.addContainerProperty("ID", String.class, null);
						tablaInterna.setColumnCollapsingAllowed(true);
						tablaInterna.setColumnCollapsed("ID", true);
							tablaInterna.addContainerProperty("PRODUCTO", String.class, null);
							tablaInterna.addContainerProperty("CANTIDAD", String.class, null);
							tablaInterna.addContainerProperty("PRECIO", Double.class, null);

							Button añadir = new Button("Agregar");
							añadir.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							    	
								    	if(producto.getValue() == null || cantidad.getValue().equals("")){
								    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
								    	}else{
								    		
								    		//Busco costo de producto y multiplico por cantidad
								    		double costo = 0;
								    		
								    		Mysql sql = new Mysql();
								    		BeanConsulta bean = new BeanConsulta();
								    		
								    		try{
								    			
								    			bean = sql.consultaSimple("select ifnull(total,0) from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString());
								    			
								    			if(!bean.getRespuesta().equals("OK")){
								    				throw new Exception(bean.getRespuesta());
								    			}
								    			
								    			costo = Double.parseDouble(bean.getDato())*Double.parseDouble(cantidad.getValue());
								    			

								    			
								    		}catch(Exception e){
								    			e.printStackTrace();
								    		}finally{
								    			sql.cerrar();
								    			sql = null;
								    			bean = null;
								    		}
								    		
								    		
								    		tablaInterna.addItem(new Object[]{producto.getValue().toString(), producto.getItemCaption(producto.getValue()), cantidad.getValue(), costo}, producto.getValue().toString());
								    		producto.setValue(null);
								    		cantidad.setValue("");
								    		
								    		//Actualizo Costo Total
							            	if(descuento.getValue().equals("")){
							            		
							            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
										        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, 0)));
							            		
							            	}else{
							            		
							            		//Funci�n para hacer descuento
							            		
							            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
										        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, Double.parseDouble(descuento.getValue()))));
							            	}
								    		
								    		
								    	}
							    		
								    }
								});
							
							Button eliminar = new Button("Eliminar");
							eliminar.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {

							    	if(tablaInterna.getValue() != null){
							    	
							    		tablaInterna.removeItem(tablaInterna.getValue());
							    		
							    		//Actualizo Costo Total
						            	if(descuento.getValue().equals("")){
						            		
						            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
									        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, 0)));
						            		
						            	}else{
						            		
						            		//Funci�n para hacer descuento
						            		
						            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
									        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, Double.parseDouble(descuento.getValue()))));
						            	}
							    		
							    	}else{
							    		Notification.show("Se debe escoger alg�n producto a eliminar", Type.WARNING_MESSAGE);
							    	}
							    	
								    }
								});
							
						
					Button pdfExporter = new Button("PDF");
					pdfExporter.setStyleName("boton_simple_azul");
					
					pdfExporter.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					    	
					    	//Genero el archivo
					    	Nuevo pdf = new Nuevo();
					    	
					    	//Ajusto descuento
					    	String descuentoAjustado;
    			    		if(descuento.getValue() == null)
    			    			descuentoAjustado = "0";
    			    		else if(descuento.getValue().equals(""))
    			    			descuentoAjustado = "0";
    			    		else
    			    			descuentoAjustado = descuento.getValue();
					    		
    			    		String notaGrandeAjustada;
    			    		if(notaGrande.getValue() == null)
    			    			notaGrandeAjustada = "";
    			    		else if(notaGrande.getValue().equals(""))
    			    			notaGrandeAjustada = "";
    			    		else
    			    			notaGrandeAjustada = notaGrande.getValue();

    			    		String notaChicaAjustada;
    			    		if(notaChica.getValue() == null)
    			    			notaChicaAjustada = "";
    			    		else if(notaChica.getValue().equals(""))
    			    			notaChicaAjustada = "";
    			    		else
    			    			notaChicaAjustada = notaChica.getValue();
    			    		
					    	pdf.generarPdf(tablaInterna, calcularDescuento(tablaInterna, Double.parseDouble(descuentoAjustado)), notaGrandeAjustada, notaChicaAjustada);
					    	
					    	pdf = null;
					    	
					    }
					});
							
		    		final Mysql sql = new Mysql();
		    		
		    		try{
		    		
		    			//Actualiza a vista = SI
		    			String respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.solicitudes set vista = 'SI' where id = "+id);
		    			
		    			if(!respuesta.equals("OK")){
		    				throw new Exception(respuesta);
		    			}
		    			
		    			BeanConsulta bean = sql.consultaSimple("select ifnull(comentarios,'') as comentarios from "+SqlConf.obtenerBase()+"inventario.solicitudes where id = "+id);
		    			
		    			if(!bean.getRespuesta().equals("OK")){
		    				throw new Exception(bean.getRespuesta());
		    			}else{
		    				comentarios.setValue(bean.getDato());
		    			}
		    			
		    			bean = sql.consultaSimple("select fecha from "+SqlConf.obtenerBase()+"inventario.solicitudes where id = "+id);
		    			
		    			if(!bean.getRespuesta().equals("OK")){
		    				throw new Exception(bean.getRespuesta());
		    			}else{
		    				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    				String fechaString = bean.getDato();
		    				Date date = formatter.parse(fechaString);
		    				
		    				fecha.setValue(date);
		    			}
		    			
		    			//Agrego descuento
		    			bean = sql.consultaSimple("select ifnull(descuento,0) as descuento from "+SqlConf.obtenerBase()+"inventario.solicitudes where id = "+id);
		    			
		    			if(!bean.getRespuesta().equals("OK")){
		    				throw new Exception(bean.getRespuesta());
		    			}else{
		    				descuento.setValue(bean.getDato());
		    			}
		    			
		    			
		    			//Hasta este punto asigno nombre al pdf ya es que cuando fecha tiene valor
		    			
		    			
		    			BeanConexion beanCon = sql.conexionSimple("select id_prod, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_prod) as nombre, cantidad, precio from "+SqlConf.obtenerBase()+"inventario.solicitudes_desglose a where a.id_solicitud = "+id);
		    			
		    			if(!beanCon.getRespuesta().equals("OK")){
		    				throw new Exception(beanCon.getRespuesta());
		    			}
		    			
		    			ResultSet rs = beanCon.getRs();
					
					
						while(rs.next()){
							
							tablaInterna.addItem(new Object[]{rs.getString("id_prod"), rs.getString("nombre"), rs.getString("cantidad"), rs.getDouble("precio")}, rs.getString("id_prod"));
							
						}
		    				
			    		//Actualizo Costo Total
		            	if(descuento.getValue().equals("")){
		            		
		            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
					        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, 0)));
		            		
		            	}else{
		            		
		            		//Funci�n para hacer descuento
		            		
		            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
					        totalCostoLabel.setValue("Costo total: "+formatter.format(calcularTotalSinDescuento(tablaInterna, Double.parseDouble(descuento.getValue()))));
		            	}
						
		    			Button actualizar = new Button("Actualizar");
		    			actualizar.setStyleName("boton_simple");
		    			actualizar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    			    	
						    	if(tablaInterna.size() == 0){
						    		Notification.show("La tabla no puede estar vac�a", Type.WARNING_MESSAGE); 
						    	}else{
		    			    	
			    				    Mysql sql = new Mysql();
			    				    String respuesta = "NO";
			    				    
			    			    	try{
			    			    	
			    			    		//Empieza transacci�n
			    			    			
			    			    		sql.transaccionAbrir();
			    			    		
			    			    		//Modifico comentarios

			    			    		String descuentoAjustado;
			    			    		if(descuento.getValue() == null)
			    			    			descuentoAjustado = "0";
			    			    		else if(descuento.getValue().equals(""))
			    			    			descuentoAjustado = "0";
			    			    		else
			    			    			descuentoAjustado = descuento.getValue();
			    			    		
			    			    		
			    			    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.solicitudes set comentarios = '"+comentarios.getValue()+"', fecha = '"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"', descuento = "+descuentoAjustado+" where id = "+id);
			    			    		
			    			    		descuentoAjustado = null;
			    			    		
			    			    		//Elimino datos anteriores de desglose
			    			    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.solicitudes_desglose where id_solicitud = "+id);
			    			    		
			    			    		if(!respuesta.equals("OK")){
			    			    			throw new Exception(respuesta);
			    			    		}
			    			    		
			    			    		//Guardo los nuevos
			    			    		
									    	Collection<?> ids = tablaInterna.getItemIds();
									    	Property<String> cantidad = null;
									    	Property<Double> precio = null;
								    	
									        for (Object elem : ids) {
									        	
									        	cantidad = tablaInterna.getItem(elem).getItemProperty("CANTIDAD");
									        	precio = tablaInterna.getItem(elem).getItemProperty("PRECIO");
									        	respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.solicitudes_desglose values (null, '"+usuario.getCustid()+"','"+id+"','"+elem+"','"+cantidad.getValue()+"', "+precio.getValue()+")");
		
								    			if(!respuesta.equals("OK")){
								    				throw new Exception(respuesta);
								    			}
									        	
									        }
			    				    		
									        cantidad = null;
									        precio = null;
									        
			    				    		if(respuesta.equals("OK")){
			    				    		
			    				    			sql.transaccionCommit();
			    				    			sql.transaccionCerrar();
			    				    			
				    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
				    				    		
												Notification n = new Notification("Correcta actualizaci�n de la solicitud", Type.TRAY_NOTIFICATION);
												n.setDelayMsec(2000);
												n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
												n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
												n.show(UI.getCurrent().getPage());
			    				    		
			    				    		}
			    				    		
			    			    	}catch(Exception e){
			    			    		
			    			    		sql.transaccionRollBack();
			    			    		sql.transaccionCerrar();
			    			    		
			    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			    			    		e.printStackTrace();
			    			    	}finally{
			    			    		sql.cerrar();
			    			    	}
		    			    	
						    	}
		    				    		
		    			    }
		    			});
		    			
		    			
		    			Button eliminarSolicitud = new Button("Eliminar");
		    			eliminarSolicitud.setStyleName("boton_simple");
		    			eliminarSolicitud.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    			    	
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmaci�n", "�Est�s seguro de querer eliminarla?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
					    			    	Mysql sql = new Mysql();
					    			    	
					    			    	try{
					    			    		
					    			    		String respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.solicitudes set activo = 'NO' where id = "+id);
					    			    		
					    			    		if(!respuesta.equals("OK")){
					    			    			throw new Exception(respuesta);
					    			    		}
					    			    		
					    			    		ventanaActualizar.close();
					    			    		generarTabla(tablas, usuario.getCustidsRelacionados());
					    			    		
					    			    	}catch(Exception e){
					    			    		e.printStackTrace();
					    			    	}finally{
					    			    		sql.cerrar();
					    			    	}
					    			    
		    			                }
		    			            }
	    			            
		    					});
		    				    		
		    			    }
		    			});

		    			Button completarSolicitud = new Button("Completada");
		    			completarSolicitud.setStyleName("boton_simple_azul");
		    			completarSolicitud.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    			    	
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmaci�n", "�Est�s seguro de marcarla como completada?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
					    			    	Mysql sql = new Mysql();
					    			    	
					    			    	try{
					    			    		
					    			    		String respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.solicitudes set completada = 'SI' where id = "+id);
					    			    		
					    			    		if(!respuesta.equals("OK")){
					    			    			throw new Exception(respuesta);
					    			    		}
					    			    		
					    			    		ventanaActualizar.close();
					    			    		generarTabla(tablas, usuario.getCustidsRelacionados());
					    			    		
					    			    	}catch(Exception e){
					    			    		e.printStackTrace();
					    			    	}finally{
					    			    		sql.cerrar();
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
							datos.setStyleName(ValoTheme.LAYOUT_WELL);
							
							datos.addComponent(producto);
								datos.setComponentAlignment(producto, Alignment.MIDDLE_LEFT);
							datos.addComponent(cantidad);
								datos.setComponentAlignment(cantidad, Alignment.MIDDLE_CENTER);
							datos.addComponent(fecha);
								datos.setComponentAlignment(fecha, Alignment.MIDDLE_RIGHT);
						
						grid.addComponent(datos, 0, 0);
							grid.setComponentAlignment(datos, Alignment.MIDDLE_CENTER);
						
						grid.addComponent(tablaInterna, 0, 1);
							grid.setComponentAlignment(tablaInterna, Alignment.MIDDLE_CENTER);
						
						HorizontalLayout botones = new HorizontalLayout();
							botones.setWidth("50%");
							botones.addComponent(añadir);
								botones.setComponentAlignment(añadir, Alignment.MIDDLE_CENTER);
							botones.addComponent(eliminar);
								botones.setComponentAlignment(eliminar, Alignment.MIDDLE_CENTER);
						
						grid.addComponent(botones, 0, 2);
							grid.setComponentAlignment(botones, Alignment.MIDDLE_CENTER);

						grid.addComponent(costos, 0, 3);
							grid.setComponentAlignment(costos, Alignment.MIDDLE_CENTER);
							
						grid.addComponent(comentarios, 0, 4);
							grid.setComponentAlignment(comentarios, Alignment.MIDDLE_CENTER);
							
						grid.addComponent(popup, 0, 5);
							grid.setComponentAlignment(popup, Alignment.MIDDLE_CENTER);
							
						HorizontalLayout botonesFinales = new HorizontalLayout();
							botonesFinales.setWidth("90%");
							botonesFinales.addComponent(actualizar);
								botonesFinales.setComponentAlignment(actualizar, Alignment.MIDDLE_CENTER);
							botonesFinales.addComponent(eliminarSolicitud);
								botonesFinales.setComponentAlignment(eliminarSolicitud, Alignment.MIDDLE_CENTER);
							botonesFinales.addComponent(pdfExporter);
								botonesFinales.setComponentAlignment(pdfExporter, Alignment.MIDDLE_CENTER);
							botonesFinales.addComponent(completarSolicitud);
								botonesFinales.setComponentAlignment(completarSolicitud, Alignment.MIDDLE_CENTER);							
								
								
						grid.addComponent(botonesFinales, 0, 6);
							grid.setComponentAlignment(botonesFinales, Alignment.BOTTOM_CENTER);
		    				
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
	
	//Empiezan m�todos externos
	
		private AutocompleteSuggestionProvider listaCategorias(String custid){
			
			List<String> nombres = new ArrayList<String>();
			Mysql sql = new Mysql();
			
			try{
				
    			BeanConexion beanCon = sql.conexionSimple("select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") group by categoria");
    			
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
				
    			BeanConexion beanCon = sql.conexionSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") group by nombre");
    			
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
				
				//combo.setCaption("Productos");
				
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
		
		private ComboBox llenarUnidadesMedida(ComboBox combo){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Unidad de medida");
				
    			BeanConexion beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.unidad_medida");
    			
    			if(!beanCon.getRespuesta().equals("OK")){
    				throw new Exception(beanCon.getRespuesta());
    			}
    			
    			ResultSet rs = beanCon.getRs();
				
				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
				}
				
				combo.setNullSelectionAllowed(false);
				combo.setValue(1);
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		private ComboBox actualizarComboBox(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			
			try{
				
				combo.setCaption("Proveedores");
				combo.removeAllItems();
				
    			BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ('"+custid+"')");
    			
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
		
		private void actualizarColorTabla(final Table tabla){
			
        	tabla.setCellStyleGenerator(new Table.CellStyleGenerator(){

    			@Override
    			public String getStyle(Table source, Object itemId,
    					Object propertyId) {
    		        if (propertyId == null) {
    			          Item item = tabla.getItem(itemId);
    			          String vista = (String) item.getItemProperty("VISTA").getValue();
    			          if (vista.equals("NO")) {
    			            return "highlight-blue";
    			          } else {
    			            return null;
    			          }
    			        } else {
    			          // styling for column propertyId
    			        	//if(propertyId.toString().equals("DIFERENCIAL") || propertyId.toString().equals("EXISTENCIA")){
    			        	//	return "black";
    			        	//}else{
    			        		return null;
    			        	//}
    			        }
    			}
    			
    		});
			
		}
		
		private double calcularTotalSinDescuento(Table tabla, double descuento){
			
			//Actualizo Costo Total
	    	Collection<?> ids = tabla.getItemIds();
	    	
	    	double totalCosto = 0.0;
	    	Property<Double> costoUnico = null;
	    	
	        for (Object elem : ids) {
	        	
	        	costoUnico = tabla.getItem(elem).getItemProperty("PRECIO");
	        	totalCosto = totalCosto + costoUnico.getValue();
	        }
	        
	        descuento = descuento/100;
	        return totalCosto - (totalCosto*descuento);

		}
		
		private double calcularDescuento(Table tabla, double descuento){
			
			//Actualizo Costo Total
	    	Collection<?> ids = tabla.getItemIds();
	    	
	    	double totalCosto = 0.0;
	    	Property<Double> costoUnico = null;
	    	
	        for (Object elem : ids) {
	        	
	        	costoUnico = tabla.getItem(elem).getItemProperty("PRECIO");
	        	totalCosto = totalCosto + costoUnico.getValue();
	        }
	        
	        descuento = descuento/100;
	        return totalCosto*descuento;

		}
		
}

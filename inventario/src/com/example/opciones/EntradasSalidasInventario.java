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

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.ui.NumberField;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;

import com.example.inventario.Usuario;
import com.ibm.icu.impl.CalendarAstronomer.Horizon;
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
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import funciones.Encriptar;
import funciones.Funcion;

public class EntradasSalidasInventario {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setHeight("100%");
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
		final HorizontalLayout titulo = new HorizontalLayout();
		titulo.setHeight("70");
		titulo.setWidth("90%");
		
		Label tituloLabel = new Label("Registro de entradas y salidas de inventario");
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
				
			final Button agregar = new Button("Entrada");
			agregar.setStyleName("boton_registrar_nuevo_dos");
				
				agregar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
						final Window ventanaRegistrar = new Window("Registrar");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
				    	
					final ComboBox proveedor = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						proveedor.setWidth("80%");
						proveedor.setEnabled(false);
						
						
					final AutocompleteTextField categoria = new AutocompleteTextField("Categoría");
						categoria.setWidth("80%");
						categoria.setCache(true); // Client side should cache suggestions
						categoria.setDelay(50); // Delay before sending a query to the server	
						categoria.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
						categoria.setMinChars(1); // The required value length to trigger a query
						categoria.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
						categoria.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
						categoria.setSuggestionProvider(listaCategorias(usuario.getCustidsRelacionados()));
						//categoria.setMaxLength(200);
						categoria.setEnabled(false);
						
					final ComboBox nombre = llenarComboBoxProductos(new ComboBox(), usuario.getCustidsRelacionados());
						nombre.setWidth("80%");

					final TextField nombreInterno = new TextField("SKU");
						nombreInterno.setWidth("80%");
						//nombreInterno.setMaxLength(200);
						nombreInterno.setEnabled(false);
						
						
					final NumberField unidades = new NumberField("Unidades");
						unidades.setNullRepresentation("");
						unidades.setDecimalAllowed(true);
						unidades.setDecimalPrecision(3);
						unidades.setErrorText("Número no válido");
						unidades.setInvalidAllowed(false);
						unidades.setNegativeAllowed(false);
					
					final NumberField costo = new NumberField("Costo por unidad");
						costo.setNullRepresentation("");
						costo.setDecimalPrecision(2);
						costo.setErrorText("Número no válido");
						costo.setInvalidAllowed(false);
						costo.setNegativeAllowed(false);
						
					final CheckBox iva = new CheckBox("¿Contiene IVA?");
						iva.setValue(true);
					
					final DateField fecha = new DateField("Fecha de ingreso");
					fecha.setDateFormat("dd MMMM yyyy");
						
					final DateField caducidad = new DateField("Caducidad");
						caducidad.setDateFormat("dd MMMM yyyy");
					//caducidad.setStyleName(ValoTheme.DATEFIELD_SMALL);
						
					//Text change para actualizar datos precargados
						nombre.addListener(new Property.ValueChangeListener() {
				            private static final long serialVersionUID = -5188369735622627751L;

				            public void valueChange(ValueChangeEvent event) {
				            	
				                if (nombre.getValue() != null) {
						    	
						    	if(!nombre.getValue().equals("")){
						    		
						    		Mysql sql = new Mysql();
						    		BeanConexion beanCon;
						    		ResultSet rs;
						    		
						    		try{
						    				
											beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.material where custid = '"+usuario.getCustid()+"' and id = '"+nombre.getValue()+"'");
											
											if(!beanCon.getRespuesta().equals("OK")){
												throw new Exception(beanCon.getRespuesta());
											}
											
											rs = beanCon.getRs();

						    				while(rs.next()){
						    					
						    					proveedor.setValue(rs.getInt("proveedor"));
						    					categoria.setValue(rs.getString("categoria"));
						    					nombreInterno.setValue(rs.getString("sku"));
						    					
						    				}
						    			
						    		}catch(Exception e){
						    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
						    			e.printStackTrace();
						    		}finally{
						    			sql.cerrar();
						    			sql = null;
						    			beanCon = null;
						    			rs = null;
						    			
						    		}

						    	//}
						    	
						    }else{
						    	proveedor.setValue(null);
		    					categoria.setValue("");
		    					nombreInterno.setValue("");
		    					costo.setValue("");
		    					//minimo.setValue("");
		    					//maximo.setValue("");
						    }
				                }
						    }
						});
					
					Button registrar = new Button("Registrar");
					registrar.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					    	
					    	if(categoria.getValue() != null && nombre.getValue() != null && !unidades.getValue().equals("") && fecha.getValue() != null && !costo.getValue().equals("")){
					    	
						    	Mysql sql = new Mysql();
						    	String respuesta = "NO";
						    	
						    	try{
						    		
						    		//Ajuste de caducidad
						    		String caducidadAjustada;
						    		
						    		if(caducidad.getValue() == null){
						    			caducidadAjustada = "null";
						    		}else{
						    			caducidadAjustada = "'" + Funcion.fechaFormato(caducidad.getValue(), "yyyy-MM-dd") + "'";
						    		}
						    		
						    		//Revisar si tiene IVA
						    		
						    		if(iva.getValue() == true){
						    			
						    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.inventario values ("
							    				+ "null, '"+usuario.getCustid()+"',"+unidades.getValue().replaceAll("'", "")+","+(Double.parseDouble(costo.getValue())/1.16)+","+(Double.parseDouble(costo.getValue())/1.16)*.16+","+Double.parseDouble(costo.getValue())+","
							    						+ "null,'"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"',"+caducidadAjustada+","+nombre.getValue()+","+proveedor.getValue()+",'"+Encriptar.Encriptar(proveedor.getItemCaption(proveedor.getValue()))+"','SI', 'ENTRADA', null)");
							    		
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}
						    			
						    		}else{
						    			
						    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.inventario values ("
							    				+ "null, '"+usuario.getCustid()+"',"+unidades.getValue().replaceAll("'", "")+","+Double.parseDouble(costo.getValue())+",0.0,"+Double.parseDouble(costo.getValue())+","
							    						+ "null,'"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"',"+caducidadAjustada+","+nombre.getValue()+","+proveedor.getValue()+",'"+Encriptar.Encriptar(proveedor.getItemCaption(proveedor.getValue()))+"','NO', 'ENTRADA', null)");
							    		
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}
						    			
						    		}
						    		
						    		if(respuesta.equals("OK")){
						    		
								    	categoria.setValue("");
								    	categoria.setSuggestionProvider(listaCategorias(usuario.getCustidsRelacionados()));
										nombre.setValue(null);
										nombreInterno.setValue("");
										unidades.setValue("");
										costo.setValue("");
										proveedor.setValue(null);
										caducidad.setValue(null);
		
										Notification n = new Notification("Registro de inventario correcto", Type.TRAY_NOTIFICATION);
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
						    		sql = null;
						    	}
						    	
						    	cuerpo.removeAllComponents();
						    	cuerpo.addComponent(generarTablaConsolidado(tablas, usuario.getCustidsRelacionados()));
						    	
					    	}else{
					    		Notification.show("Se deben de ingresar Nombre, Unidades, Costo y Fecha",  Type.WARNING_MESSAGE);
					    	}
						    		
						    }
						});
							
							GridLayout grid = new GridLayout(2, 7);
								grid.setMargin(true);
								grid.setWidth("100%");
								grid.setHeight("100%");
								grid.addComponent(nombre, 0, 0);
								grid.addComponent(nombreInterno, 1, 0);
								grid.addComponent(proveedor, 0, 1);
								grid.addComponent(categoria, 1, 1);
								
								//Agrego horizontal para tener las unidades de medida
								HorizontalLayout unidadesMedida = new HorizontalLayout();
									unidadesMedida.setWidth("60%");
									unidadesMedida.addComponent(unidades);
									
									//Hago ajuste para mostrar 
									final Label unidadMedida = new Label();
										unidadMedida.setStyleName(ValoTheme.LABEL_BOLD);
									
									nombre.addListener(new Property.ValueChangeListener() {
							            private static final long serialVersionUID = -5188369735622627751L;
							            public void valueChange(ValueChangeEvent event) {
							            	
							            	Mysql sql = new Mysql();
							            	BeanConsulta bean;
							            	
							            	try{
							            	
								            	if(nombre.getValue() == null){
								            		unidadMedida.setValue("");
								            	}else{
								            		
										 		    bean = sql.consultaSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = (select unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where id = "+nombre.getValue().toString()+")");
										 		    
										    		if(!bean.getRespuesta().equals("OK")){
										    			throw new Exception(bean.getRespuesta());
										    		}
								            		
								            		unidadMedida.setValue(bean.getDato());
								            	}
							            	
							            	}catch(Exception e){
							            		e.printStackTrace();
							            	}finally{
							            		sql.cerrar();
							            		sql = null;
							            		bean = null;
							            	}
								            	
							            }
									});
								
								unidadesMedida.addComponent(unidadMedida);
								unidadesMedida.setComponentAlignment(unidadMedida, Alignment.BOTTOM_CENTER);
									
								grid.addComponent(unidadesMedida, 0, 3);
								
								
								
								HorizontalLayout precios = new HorizontalLayout();
								precios.setWidth("60%");
								precios.addComponent(costo);
									precios.setComponentAlignment(costo, Alignment.MIDDLE_LEFT);
								precios.addComponent(iva);
									precios.setComponentAlignment(iva, Alignment.MIDDLE_RIGHT);
								
								grid.addComponent(precios, 1, 3);
								//grid.addComponent(minimo, 0, 4);
								grid.addComponent(fecha, 0, 4);
								grid.addComponent(caducidad, 1, 4);
								grid.addComponent(registrar, 0, 6);
									grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
							
								ventanaRegistrar.setContent(grid);
								
								UI.getCurrent().addWindow(ventanaRegistrar);
				    	
			    }
			});

			final Button consolidado = new Button("Consolidado");
				consolidado.setStyleName("boton_simple");
				
				consolidado.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {

				    	cuerpo.removeAllComponents();
				    	cuerpo.addComponent(generarTablaConsolidado(tablas, usuario.getCustidsRelacionados()));
				    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
				    	
			    }
			});
				
			final Button mermas = new Button("Mermas");
				mermas.setStyleName("boton_simple");
				
				mermas.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
				    	
				    	final Window ventanaActualizar = new Window("Registrar");
			    		ventanaActualizar.center();
			    		ventanaActualizar.setHeight("50%");
			    		ventanaActualizar.setWidth("50%");
		
						final ComboBox nombre = llenarComboBoxProductos(new ComboBox(), usuario.getCustidsRelacionados());
						nombre.setWidth("80%");
							
							//Hago ajuste para mostrar 
							final Label unidadMedida = new Label();
								unidadMedida.setStyleName(ValoTheme.LABEL_BOLD);
							
							nombre.addListener(new Property.ValueChangeListener() {
					            private static final long serialVersionUID = -5188369735622627751L;
					            public void valueChange(ValueChangeEvent event) {
					            	
					            	Mysql sql = new Mysql();
					            	BeanConsulta bean;
					            	
					            	try{
					            	
						            	if(nombre.getValue() == null){
						            		unidadMedida.setValue("");
						            	}else{
						            		
								 		    bean = sql.consultaSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = (select unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where id = "+nombre.getValue().toString()+")");
								 		    
								    		if(!bean.getRespuesta().equals("OK")){
								    			throw new Exception(bean.getRespuesta());
								    		}
						            		
						            		unidadMedida.setValue(bean.getDato());
						            	}
					            	
					            	}catch(Exception e){
					            		e.printStackTrace();
					            	}finally{
					            		sql.cerrar();
					            		sql = null;
					            		bean = null;
					            	}
						            	
					            }
							});
						
						final NumberField unidades = new NumberField("Unidades");
							unidades.setNullRepresentation("");
							unidades.setDecimalPrecision(2);
							unidades.setErrorText("Número no válido");
							unidades.setInvalidAllowed(false);
							unidades.setNegativeAllowed(false);
						
						final DateField fecha = new DateField("Fecha");
							fecha.setDateFormat("dd MMMM yyyy");
						
			    				
			    		final Button registrar = new Button("Registrar merma");
			    			registrar.addListener(new Button.ClickListener() {
			    			    public void buttonClick(ClickEvent event) {
			    				    
			    			    	if(nombre.getValue() == null || unidades.getValue().equals("") || fecha.getValue() == null){
			    			    		Notification.show("Se deben ingresar todos los datos", Type.WARNING_MESSAGE);
			    			    	}else{
			    			    	
				    			    	Mysql sql = new Mysql();
				    			    	String respuesta = "";
				    			    	
				    			    	try{
				    			    		
				    			    		respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.inventario values ("
								    				+ "null, '"+usuario.getCustid()+"',"+(Double.parseDouble(unidades.getValue())*-1)+",null, null, null, null"
								    						+ ",'"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"', null, "+nombre.getValue()+", null, null, null, 'MERMA', null)");
								    		
			    				    		if(!respuesta.equals("OK")){
			    				    			throw new Exception(respuesta);
			    				    		}else{
				    			    		
				    				    		generarTabla(tablas, usuario.getCustidsRelacionados(),Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd"),Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd"));
				    				    		
												Notification n = new Notification("Actualización de entrada correcta", Type.TRAY_NOTIFICATION);
												n.setDelayMsec(2000);
												n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
												n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
												n.show(UI.getCurrent().getPage());
				    				    		
					    				    	nombre.setValue(null);
					    				    	unidades.setValue("");
					    				    	
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
				    	
							GridLayout grid = new GridLayout(2, 3);
							grid.setMargin(true);
							grid.setWidth("100%");
							grid.setHeight("100%");
							grid.addComponent(nombre, 0, 0);
							
							HorizontalLayout unidadesLayOut = new HorizontalLayout();
								unidadesLayOut.setWidth("100%");
								unidadesLayOut.addComponent(unidades);
								unidadesLayOut.addComponent(unidadMedida);
								unidadesLayOut.setComponentAlignment(unidadMedida, Alignment.BOTTOM_CENTER);
							
							grid.addComponent(unidadesLayOut, 1, 0);

							grid.addComponent(fecha, 1, 1);
							grid.addComponent(registrar, 0, 2);
								grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);

								
			    			ventanaActualizar.setContent(grid);
			    			
			    			UI.getCurrent().addWindow(ventanaActualizar);
				    	
			    }
			});
				
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
						reporte.setComponentAlignment(consolidado, Alignment.BOTTOM_RIGHT);

			
			HorizontalLayout registros = new HorizontalLayout();
			registros.setWidth("70%");
			registros.setHeight("100");
			registros.setCaption("Registros");
			registros.setMargin(true);
			registros.setStyleName(ValoTheme.LAYOUT_WELL);
			registros.addComponent(agregar);
				registros.setComponentAlignment(agregar, Alignment.MIDDLE_LEFT);
			//registros.addComponent(consolidado);
			registros.addComponent(mermas);
				registros.setComponentAlignment(mermas, Alignment.MIDDLE_RIGHT);

			cabecera.addComponent(registros);
				cabecera.setExpandRatio(registros, 4f);
			cabecera.addComponent(reporte);
				cabecera.setExpandRatio(reporte, 5f);
			
			respuesta.addComponent(cabecera);
				respuesta.setComponentAlignment(cabecera, Alignment.MIDDLE_LEFT);
				cuerpo.addComponent(tablas);
			respuesta.addComponent(cuerpo);
			
		return respuesta;
		
	}
	
	//Empiezan métodos externos
	
	//Generar tabla de búsqueda
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid, String fechaInicial, String fechaFinal){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{

			tablas.removeAllComponents();
			
			//BeanConexion beanCon = sql.conexionSimple("SELECT id, (select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre, ifnull((select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material),'-') as categoria, ifnull((select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material),'-') as sku, proveedor, unidades, costo, iva, total, movimiento, fecha, caducidad FROM "+SqlConf.obtenerBase()+"inventario.inventario a where a.custid in ("+custid+") and fecha between '"+fechaInicial+"' and '"+fechaFinal+"' order by a.fecha, a.id");
			beanCon = sql.conexionSimple("SELECT\r\n" + 
					"  id,\r\n" + 
					"  ifnull((select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material),'-') as categoria,\r\n" + 
					"  (select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre,\r\n" + 
					"  ifnull((select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material),'-') as sku,\r\n" + 
					"  proveedor,\r\n" + 
					"  unidades,\r\n" + 
					"  round(costo * unidades,2) as costo,\r\n" + 
					"  round(iva * unidades,2) as iva,\r\n" + 
					"  round(total * unidades,2) as total,\r\n" + 
					"  movimiento,\r\n" + 
					"  fecha,\r\n" + 
					"  caducidad\r\n" + 
					"FROM "+SqlConf.obtenerBase()+"inventario.inventario a FORCE INDEX (custid)\r\n" + 
					"where a.custid in ("+custid+")\r\n" + 
					"  and fecha between '"+fechaInicial+"' and '"+fechaFinal+"'\r\n" + 
					"order by a.fecha, a.id");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();

			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"CATEGORIA","NOMBRE","SKU","PROVEEDOR","UNIDADES","COSTO","IVA","TOTAL","MOVIMIENTO","FECHA","CADUCIDAD"};
			tablas.addComponent(crearCon3FiltrosInventario(tablas, rs, "CATEGORIA", "NOMBRE", "SKU", columnasExportar, fechaInicial, fechaFinal));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			sql.cerrar();
			sql = null;
			beanCon = null;
			rs = null;
		}
			
		return tablas;
		
	}
	
	//Crear tabla con 3 filtros para Búsqueda
	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon3FiltrosInventario(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final String tituloColumnaFiltrar3, final Object[] columnasExportar, final String fechaInicial, final String fechaFinal) throws UnsupportedOperationException, Exception{

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
			interior.setComponentAlignment(filtro3, Alignment.MIDDLE_RIGHT);
		
		
		respuesta.addComponent(interior);
		
		final Table tabla = new Table();
		tabla.setLocale(Locale.US);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		
		for (int i = 1; i < count + 1; i++ ) {
			String name = meta.getColumnName(i);
			
			if(name.toUpperCase().equals("UNIDADES") || name.toUpperCase().equals("COSTO") || name.toUpperCase().equals("MINIMO") || name.toUpperCase().equals("MAXIMO") || name.toUpperCase().equals("IVA") || name.toUpperCase().equals("TOTAL")){
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
			}else if(count==7){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6), rs.getString(7)}, id);
			}else if(count==8){  
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7), rs.getString(8)}, id);
			}else if(count==9){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), Encriptar.Desencriptar(rs.getString(5)), rs.getDouble(6), rs.getDouble(7), rs.getString(8), rs.getString(9)}, id);
			}else if(count==10){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10)}, id);
			}else if(count==11){ 
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), Encriptar.Desencriptar(rs.getString(5)), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10), rs.getString(11)}, id);
			}else if(count==12){ //Éste es el que usa
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), Encriptar.Desencriptar(rs.getString(5)), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8), rs.getDouble(9), rs.getString(10), rs.getString(11), rs.getString(12)}, id);
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

		    		Property<String> itemProperty = event.getItem().getItemProperty("MOVIMIENTO");
		    		final String movimiento = itemProperty.getValue();
		    		
		    		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();

		    		if(movimiento.equals("SALIDA")){

		    			//Ventana para saber de dónde proviene el movimiento
		    			
		    			Mysql sql = new Mysql();
		    			BeanConexion beanCon;
		    			ResultSet rs;
		    			
			    		itemProperty = event.getItem().getItemProperty("ID");
			    		
			    		final Window ventanaInfo = new Window("Proveniente de...");
			    		ventanaInfo.center();
			    		ventanaInfo.setHeight("20%");
			    		ventanaInfo.setWidth("50%");
			    		
			    		VerticalLayout campos = new VerticalLayout();
			    			campos.setMargin(true);
		    			
			    		System.out.println("dato "+itemProperty.getValue());
			    			
			    		try{
			    		
						beanCon = sql.conexionSimple("select a.fecha, a.movimiento, b.nombre, a.cantidad\r\n" + 
								"from "+SqlConf.obtenerBase()+"inventario.almacen a,\r\n" + 
								""+SqlConf.obtenerBase()+"inventario.productos b\r\n" + 
								"where a.id_producto = b.id\r\n" + 
								"and a.id = (select id_almacen from "+SqlConf.obtenerBase()+"inventario.inventario where id = "+itemProperty.getValue()+")");
						
						if(!beanCon.getRespuesta().equals("OK")){
							throw new Exception(beanCon.getRespuesta());
						}
						
						rs = beanCon.getRs();
			    		
						while(rs.next()){
							
							campos.addComponent(new Label("Movimiento: "+rs.getString("movimiento")));
							campos.getComponent(0).setStyleName("LabelProveniente");
							
							campos.addComponent(new Label("Fecha: "+rs.getString("fecha")));
							campos.getComponent(1).setStyleName("LabelProveniente");
							
							campos.addComponent(new Label("Producto: "+rs.getString("nombre")));
							campos.getComponent(2).setStyleName("LabelProveniente");
							
							campos.addComponent(new Label("Cantidad de productos: "+rs.getString("cantidad")));
							campos.getComponent(3).setStyleName("LabelProveniente");
							
							
						}
		    			
						ventanaInfo.setContent(campos);
						UI.getCurrent().addWindow(ventanaInfo);
		    			
			    		}catch(Exception e){
			    			
			    		}finally{
			    			sql.cerrar();
			    			sql = null;
			    			beanCon = null;
			    			rs = null;
			    		}
		    			
		    			
		    			
		    			
		    			
		    		}else if(movimiento.equals("MERMA")){
		    			
			    		itemProperty = event.getItem().getItemProperty("ID");
			    		final String id = itemProperty.getValue();
			    		
			    		final Window ventanaActualizar = new Window("Eliminar");
			    		ventanaActualizar.center();
			    		ventanaActualizar.setHeight("20%");
			    		ventanaActualizar.setWidth("20%");			    		
			    		
		    			Button eliminar = new Button("Eliminar entrada");
		    			eliminar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    			
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
					    			    	Mysql sql = new Mysql();
					    			    	String respuesta = "";
					    			    	
					    			    	try{
					    			    		
					    			    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+usuario.getCustid()+"' and id = "+id);
					    			    		
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}else{
					    			    		
						    			    		generarTabla(tablas, usuario.getCustidsRelacionados(), fechaInicial, fechaFinal);
						    			    		ventanaActualizar.close();
						    			    		
													Notification n = new Notification("Merma eliminada correctamente", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
					    			    		
				    				    		}
				    				    		
					    			    	}catch(Exception e){
					    			    		Notification.show("Error en la aplicación: ", Type.ERROR_MESSAGE);
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
			    		
		    			
		    		}else{
		    		
			    		itemProperty = event.getItem().getItemProperty("ID");
			    		final String id = itemProperty.getValue();
		    			
			    		final Window ventanaActualizar = new Window("Actualizar");
			    		ventanaActualizar.center();
			    		ventanaActualizar.setHeight("50%");
			    		ventanaActualizar.setWidth("40%");
			    				
			    		Mysql sql = new Mysql();
			    		BeanConexion beanCon;
			    		ResultSet rs;
			    		
			    		try{
			    		
			    			beanCon = sql.conexionSimple("select id, unidades, total, fecha, contiene_iva from "+SqlConf.obtenerBase()+"inventario.inventario a where id = "+id);
			    			
			    			if(!beanCon.getRespuesta().equals("OK")){
			    				throw new Exception(beanCon.getRespuesta());
			    			}
			    			
			    			rs = beanCon.getRs();

						final NumberField unidades = new NumberField("Unidades");
							unidades.setNullRepresentation("");
							unidades.setDecimalPrecision(2);
							unidades.setErrorText("Número no válido");
							unidades.setInvalidAllowed(false);
							unidades.setNegativeAllowed(false);
							
						
						final NumberField costo = new NumberField("Costo por unidad");
							costo.setNullRepresentation("");
							costo.setDecimalPrecision(2);
							costo.setErrorText("Número no válido");
							costo.setInvalidAllowed(false);
							costo.setNegativeAllowed(false);
							
						//Agregar IVA
						final CheckBox iva = new CheckBox("¿Tiene IVA?");
							
						final DateField fecha = new DateField("Desde");
							fecha.setDateFormat("dd MMMM yyyy");
						
						//fecha.setValue(new Date());
			    				
			    			while(rs.next()){

			    				unidades.setValue(Funcion.decimales(rs.getString("unidades")));
			    				costo.setValue(rs.getString("total"));
			    				
			    				if(rs.getString("contiene_iva").equals("SI")){
			    					iva.setValue(true);
			    				}else{
			    					iva.setValue(false);
			    				}
			    				
			    				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			    				String fechaString = rs.getString("fecha");
			    				Date date = formatter.parse(fechaString);
			    				
			    				fecha.setValue(date);
	
			    			}
			    				
			    			Button registrar = new Button("Actualizar entrada");
			    			registrar.addListener(new Button.ClickListener() {
			    			    public void buttonClick(ClickEvent event) {
			    				    Mysql sql = new Mysql();
			    				    String respuesta = "NO";
			    			    	try{
			    			    		
			    			    			String ivaAjustado = "NO";
			    			    			
			    			    			if(iva.getValue() == true)
			    			    				ivaAjustado = "SI";
			    			    		
			    			    			if(ivaAjustado.equals("SI")){
			    			    			
				    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.inventario set unidades = "+unidades.getValue()+", costo = "+(Double.parseDouble(costo.getValue())/1.16)+", iva = "+(Double.parseDouble(costo.getValue())/1.16)*.16+", total = "+costo.getValue()+","
				    				    				+ "fecha = '"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"', contiene_iva = '"+ivaAjustado+"' where id = "+id);
				    			
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}
				    				    		
			    			    			}else{
			    			    				
			    			    				respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.inventario set unidades = "+unidades.getValue()+", costo = "+costo.getValue()+", iva = 0.0, total = "+costo.getValue()+","
				    				    				+ "fecha = '"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"', contiene_iva = '"+ivaAjustado+"' where id = "+id);
			    			    			
			    			    				if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}
			    			    				
			    			    			}
			    				    		
			    				    		if(!respuesta.equals("OK")){
			    				    			throw new Exception(respuesta);
			    				    		}else{
			    				    		
				    				    		generarTabla(tablas, usuario.getCustidsRelacionados(),fechaInicial,fechaFinal);
				    				    		
												Notification n = new Notification("Actualización de entrada correcta", Type.TRAY_NOTIFICATION);
												n.setDelayMsec(2000);
												n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
												n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
												n.show(UI.getCurrent().getPage());
			    				    		
			    				    		}
			    				    		
			    				    		ventanaActualizar.close();
			    				    		
			    			    	}catch(Exception e){
			    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			    			    		e.printStackTrace();
			    			    	}finally{
			    			    		sql.cerrar();
			    			    		sql = null;
			    			    		respuesta = null;
			    			    	}
			    				    		
			    			    }
			    			});
			    			
			    			Button eliminar = new Button("Eliminar entrada");
			    			eliminar.addListener(new Button.ClickListener() {
			    			    public void buttonClick(ClickEvent event) {
			    			
			    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
			    							"SI", "NO", new ConfirmDialog.Listener() {

			    			            public void onClose(ConfirmDialog dialog) {
			    			                if (dialog.isConfirmed()) {
			    			    	
						    			    	Mysql sql = new Mysql();
						    			    	String respuesta = "";
						    			    	
						    			    	try{
						    			    		
						    			    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+usuario.getCustid()+"' and id = "+id);
						    			    		
					    				    		if(!respuesta.equals("OK")){
					    				    			throw new Exception(respuesta);
					    				    		}else{
						    			    		
							    			    		generarTabla(tablas, usuario.getCustidsRelacionados(), fechaInicial, fechaFinal);
							    			    		ventanaActualizar.close();
							    			    		
														Notification n = new Notification("Entrada eliminada correctamente", Type.TRAY_NOTIFICATION);
														n.setDelayMsec(2000);
														n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
														n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
														n.show(UI.getCurrent().getPage());
						    			    		
					    				    		}
					    				    		
						    			    	}catch(Exception e){
						    			    		Notification.show("Error en la aplicación: ", Type.ERROR_MESSAGE);
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
			    			    	
							GridLayout grid = new GridLayout(2, 3);
							grid.setMargin(true);
							grid.setWidth("100%");
							grid.setHeight("100%");
							grid.addComponent(unidades, 0, 0);
							
							HorizontalLayout costos = new HorizontalLayout();
								costos.setWidth("100%");
								costos.addComponent(costo);
									costo.setWidth("80%");
								costos.addComponent(iva);
									iva.setWidth("80%");
									costos.setComponentAlignment(iva, Alignment.MIDDLE_RIGHT);
							
							grid.addComponent(costos, 1, 0);
							grid.addComponent(fecha, 1, 1);
							grid.addComponent(registrar, 0, 2);
								grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
							grid.addComponent(eliminar, 1, 2);
								grid.setComponentAlignment(eliminar, Alignment.BOTTOM_LEFT);
								
			    			ventanaActualizar.setContent(grid);
			    			
			    			UI.getCurrent().addWindow(ventanaActualizar);
		    			
		    		}catch(Exception e){
		    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
		    			e.printStackTrace();
		    		}finally{
		    			sql.cerrar();
		    			sql = null;
		    			beanCon = null;
		    			rs = null;
		    		}
		    		
		    		
		    	}
		    		
		    	}
		    }
		});
		
		return respuesta;
	}

	//Crear tabla para consolidado
	private VerticalLayout generarTablaConsolidado(VerticalLayout tablas, String custid){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{

			tablas.removeAllComponents();
			
			beanCon = sql.conexionSimple("SELECT id, (select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as categoria, "
					+ "(select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre, (select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as sku, (select ifnull(minimo,0) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as minimo, (select ifnull(maximo,0) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as maximo, sum(unidades) as existencia, (sum(unidades)-(select minimo)) as diferencial FROM "+SqlConf.obtenerBase()+"inventario.inventario a FORCE INDEX (custid) where a.custid in ("+custid+") group by id_material order by diferencial");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"CATEGORIA","NOMBRE","SKU","MINIMO","MAXIMO","EXISTENCIA","DIFERENCIAL"};
			tablas.addComponent(crearCon2FiltrosInventario(tablas, rs, "CATEGORIA", "NOMBRE", columnasExportar));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			sql.cerrar();
			sql = null;
			beanCon = null;
			rs = null;
		}
			
		return tablas;
		
	}
	
	//Crear tabla con 2 filtros para consolidado
	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon2FiltrosInventario(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final Object[] columnasExportar) throws SQLException{

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
			
			if(name.toUpperCase().equals("EXISTENCIA") || name.toUpperCase().equals("MINIMO") || name.toUpperCase().equals("DIFERENCIAL") || name.toUpperCase().equals("MAXIMO")){
				tabla.addContainerProperty(name.toUpperCase(), Double.class, null);
				tabla.setColumnAlignment(name.toUpperCase(),
		                Table.ALIGN_RIGHT);
				tabla.setColumnWidth(name.toUpperCase(), 120);
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
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7)}, id);
			}else if(count==8){ //Éste es el que usa (haciendo cambio de orden para existencia y diferencial)
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble("minimo"), rs.getDouble("maximo"), rs.getDouble("existencia"), rs.getDouble("diferencial")}, id);
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
	
		private AutocompleteSuggestionProvider listaCategorias(String custid){
			
			List<String> nombres = new ArrayList<String>();
			Mysql sql = new Mysql();
			BeanConexion beanCon;
			ResultSet rs;
			
			try{
				
				beanCon = sql.conexionSimple("select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid = '"+custid+"' group by categoria");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				rs = beanCon.getRs();

				while(rs.next()){
					nombres.add(rs.getString("categoria"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
				beanCon = null;
				rs = null;
			}
			
			AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(nombres, MatchMode.CONTAINS, true, Locale.US);
			return suggestionProvider;
			
		}
	
		private AutocompleteSuggestionProvider listaNombres(String custid){
			
			List<String> nombres = new ArrayList<String>();
			Mysql sql = new Mysql();
			BeanConexion beanCon;
			ResultSet rs;
			
			try{
				
				beanCon = sql.conexionSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid = '"+custid+"' group by nombre");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				rs = beanCon.getRs();
				
				while(rs.next()){
					nombres.add(rs.getString("nombre"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
				beanCon = null;
				rs = null;
			}
			
			AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(nombres, MatchMode.CONTAINS, true, Locale.US);
			return suggestionProvider;
			
		}
		
		private ComboBox llenarComboBox(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			BeanConexion beanCon;
			ResultSet rs;
			
			try{
				
				combo.setCaption("Proveedores");
				
				beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+custid+") order by nombre");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				rs = beanCon.getRs();

				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), Encriptar.Desencriptar(rs.getString("nombre")) + " ("+rs.getString("producto")+")");
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
				beanCon = null;
				rs = null;
			}
			
			combo.setNullSelectionAllowed(false);
			
			return combo;
			
		}
		
		private ComboBox llenarComboBoxProductos(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			BeanConexion beanCon;
			ResultSet rs;
			
			try{
				
				combo.setCaption("Nombre");
				
				beanCon = sql.conexionSimple("SELECT id, concat(nombre,' (',sku,')') as nombre FROM "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and activo = 'SI' order by nombre");
				
				if(!beanCon.getRespuesta().equals("OK")){
					throw new Exception(beanCon.getRespuesta());
				}
				
				rs = beanCon.getRs();
				
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
				beanCon = null;
				rs = null;
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

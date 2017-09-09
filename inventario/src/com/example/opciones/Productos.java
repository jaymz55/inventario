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

import org.json.JSONObject;
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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import funciones.Encriptar;
import funciones.Funcion;

public class Productos {

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
			
			//Cuadro de componentes
			final Table tabla = new Table();
			tabla.setLocale(Locale.US);
			
			double totalCosto = 0.0;
	    	//NumberFormat myFormat = NumberFormat.getInstance();
	        //myFormat.setGroupingUsed(true);
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			
			final Label totalCostoLabel = new Label("Costo total: "+formatter.format(totalCosto));
			
			
			//Listener para la tabla
			/*tabla.addValueChangeListener(new Property.ValueChangeListener() {
			    public void valueChange(ValueChangeEvent event) {
			        

			    	
			    }
			});*/
			
			final NumberField cantidad = new NumberField("Cantidad");
				cantidad.setNullRepresentation("");
				cantidad.setDecimalAllowed(true);
				cantidad.setDecimalPrecision(3);
				cantidad.setErrorText("Número no válido");
				cantidad.setInvalidAllowed(false);
				cantidad.setNegativeAllowed(false);
				
			final ComboBox material = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
			
			try{
			
				final Button insertar = new Button("Registrar nuevo producto");
				insertar.setStyleName("boton_registrar_nuevo");
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
					
						final Window ventanaRegistrar = new Window("Registrar producto");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("90%");
						ventanaRegistrar.setWidth("60%");
				    	
					TabSheet tab = new TabSheet();
						tab.setSizeFull();
					final VerticalLayout info = new VerticalLayout();
						info.setHeight("100%");
						info.setWidth("100%");
						info.setMargin(true);
						tab.addTab(info,"Informaci�n");
						
					final TextField nombre = new TextField("Nombre de producto");
						nombre.setWidth("80%");
						nombre.setMaxLength(500);
						
					final NumberField precio = new NumberField("Precio al p�blico");
						precio.setNullRepresentation("");
						precio.setDecimalPrecision(2);
						precio.setErrorText("Número no válido");
						precio.setInvalidAllowed(false);
						precio.setNegativeAllowed(false);
						
					final CheckBox iva = new CheckBox("¿Grava IVA?");
						iva.setValue(true);
						
					final TextArea descripcion = new TextArea("Descripción");
						descripcion.setWidth("100%");
						descripcion.setMaxLength(500);
					
					final NumberField minimo = new NumberField("Mínimo");
						minimo.setNullRepresentation("");
						minimo.setDecimalAllowed(true);
						minimo.setDecimalPrecision(3);
						minimo.setErrorText("Número no válido");
						minimo.setInvalidAllowed(false);
						minimo.setNegativeAllowed(false);
						minimo.setStyleName("boton_simple");
						
					final NumberField maximo = new NumberField("Máximo");
						maximo.setNullRepresentation("");
						maximo.setDecimalAllowed(true);
						maximo.setDecimalPrecision(3);
						maximo.setErrorText("Número no válido");
						maximo.setInvalidAllowed(false);
						maximo.setNegativeAllowed(false);
						maximo.setStyleName("boton_simple");
						
					HorizontalLayout minimosMaximos = new HorizontalLayout();
						minimosMaximos.setWidth("50%");
						minimosMaximos.addComponent(minimo);
						minimosMaximos.setComponentAlignment(minimo, Alignment.BOTTOM_LEFT);
						minimosMaximos.addComponent(maximo);
						minimosMaximos.setComponentAlignment(maximo, Alignment.BOTTOM_RIGHT);
						
					Button registrar = new Button("Registrar");
					registrar.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					    	
					    	if(nombre.getValue().equals("") || precio.getValue().equals("") || tabla.size() == 0){
					    		Notification.show("Se debe ingresar nombre, precio y componentes ", Type.WARNING_MESSAGE); 
					    	}else{
					    	
							    	Mysql sql = new Mysql();
							    	try{
							    		
							    		//Ajuste de Iva
							    		
							    		/*String minimoAjustado = null;
							    		String maximoAjustado = null;
							    		
							    		if(!minimo.getValue().equals(""))
							    			minimoAjustado = minimo.getValue();
							    		
							    		if(!maximo.getValue().equals(""))
							    			maximoAjustado = maximo.getValue();*/
							    		
							    	//Abro transacci�n
							    		
							    		sql.transaccionAbrir();
							    		String respuesta = "NO";
							    		
							    		if(iva.getValue() == true){
							    			
							    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.productos values ("
								    				+ "null, '"+usuario.getCustid()+"','"+Funcion.quitarComillas(nombre.getValue())+"',"+(Double.parseDouble(precio.getValue())/1.16)+","+(Double.parseDouble(precio.getValue())/1.16)*.16+","+Double.parseDouble(precio.getValue())+","
								    						+ "'"+Funcion.quitarComillas(descripcion.getValue())+"','SI',"+((minimo.getValue().equals(""))?null:minimo.getValue())+","+((maximo.getValue().equals(""))?null:maximo.getValue())+",'SI')");
							    			
							    			if(!respuesta.equals("OK")){
							    				throw new Exception(respuesta);
							    			}
							    			
							    		}else{
							    			
							    			respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.productos values ("
								    				+ "null, '"+usuario.getCustid()+"','"+Funcion.quitarComillas(nombre.getValue())+"',"+Double.parseDouble(precio.getValue())+",0.0,"+Double.parseDouble(precio.getValue())+","
								    						+ "'"+Funcion.quitarComillas(descripcion.getValue())+"','NO',"+((minimo.getValue().equals(""))?null:minimo.getValue())+","+((maximo.getValue().equals(""))?null:maximo.getValue())+",'SI')");
							    			
							    			if(!respuesta.equals("OK")){
							    				throw new Exception(respuesta);
							    			}
							    			
							    		}
							    		
							    	//Agrego componentes
							    		
							    		//Obtengo id del producto reci�n agregado
							    		
							 		    String idProducto = "";
							 		    BeanConsulta bean = sql.consultaSimple("SELECT LAST_INSERT_ID()");
							 		    
							    		if(!bean.getRespuesta().equals("OK")){
							    			throw new Exception(bean.getRespuesta());
							    		}
							    		
							    		idProducto = bean.getDato();
							    		
							    		//String idProducto = sql.consultaSimple("SELECT LAST_INSERT_ID()");
							    		
							    		//Genero JSON object para hist�rico
							    		JSONObject json = new JSONObject();
							    		
								    	Collection<?> ids = tabla.getItemIds();
								    	Property<String> cantidad = null;
								    	
								    	//String respuesta = "NO";
								    	
								        for (Object elem : ids) {
								        	
								        	cantidad = tabla.getItem(elem).getItemProperty("CANTIDAD");
								        	json.put(elem.toString(), Double.parseDouble(cantidad.getValue()));
								        	respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.produccion values ('"+usuario.getCustid()+"','"+idProducto+"','"+elem+"','"+cantidad.getValue()+"')");

							    			if(!respuesta.equals("OK")){
							    				throw new Exception(respuesta);
							    			}
								        	
								        }
								        
								       //Inserto hist�rico
								        respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.produccion_historico values ("+usuario.getCustid()+","+idProducto+",'"+json.toString()+"', now())");
						    			
								        if(!respuesta.equals("OK")){
						    				throw new Exception(respuesta);
						    			}

								    //Verifico que respuesta sea S� para continuar
								        
								    if(respuesta.equals("OK")){
								    
								    //Cierro transacci�n
								    	sql.transaccionCommit();
								    	sql.transaccionCerrar();
								    	
								    	nombre.setValue("");
										precio.setValue("");
										descripcion.setValue("");
										cantidad.setValue("");
										material.setValue(null);
										minimo.setValue("");
										maximo.setValue("");
										tabla.removeAllItems();
				
										Notification n = new Notification("Registro de producto correcto", Type.TRAY_NOTIFICATION);
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
							    		sql = null;
							    	}
							    	
							    	dos.removeAllComponents();
							    	dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
						    	
					    	    }
						    		
						    }
						});
							
							GridLayout grid = new GridLayout(1, 5);
								grid.setMargin(true);
								grid.setWidth("100%");
								grid.setHeight("100%");
								grid.addComponent(nombre, 0, 0);
								
								HorizontalLayout precios = new HorizontalLayout();
								precios.setWidth("50%");
								precios.addComponent(precio);
									precios.setComponentAlignment(precio, Alignment.MIDDLE_LEFT);
								precios.addComponent(iva);
									precios.setComponentAlignment(iva, Alignment.MIDDLE_RIGHT);
								
								grid.addComponent(precios, 0, 1);
								grid.addComponent(descripcion, 0, 2);
								grid.addComponent(minimosMaximos, 0, 3);
									grid.setComponentAlignment(minimosMaximos, Alignment.MIDDLE_LEFT);
								grid.addComponent(registrar, 0, 4);
									grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
							
								info.addComponent(grid);
								
							//Agrego tab de componentes
								final VerticalLayout compo = new VerticalLayout();
									compo.setHeight("100%");
									compo.setWidth("100%");
									compo.setMargin(true);
									tab.addTab(compo,"Componentes");

									material.setWidth("80%");

									tabla.setHeight("200");
									tabla.setWidth("100%");
									tabla.setSelectable(true);

										tabla.addContainerProperty("ID", String.class, null);
											tabla.setColumnCollapsingAllowed(true);
											tabla.setColumnCollapsed("ID", true);
										tabla.addContainerProperty("MATERIAL", String.class, null);
										tabla.addContainerProperty("CANTIDAD", String.class, null);
										tabla.addContainerProperty("COSTO", Double.class, null);
										tabla.addContainerProperty("MEDIDA", String.class, null);
									
								Button añadir = new Button("Agregar");
								añadir.addListener(new Button.ClickListener() {
								    public void buttonClick(ClickEvent event) {
								    	
									    	if(material.getValue() == null || cantidad.getValue().equals("")){
									    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
									    	}else{
									    		
									    		Mysql sql = new Mysql();
									    		BeanConsulta bean;
									    		
									    		try{
									    			
									    			//Busco costo
									    			bean = sql.consultaSimple("select ifnull(total, 0.0) as total, count(custid) from "+SqlConf.obtenerBase()+"inventario.inventario where custid in ("+usuario.getCustidsRelacionados()+") and id_material = "+material.getValue().toString()+" and movimiento = 'ENTRADA' and fecha = (select max(fecha) from "+SqlConf.obtenerBase()+"inventario.inventario force index (custid) where custid in ("+usuario.getCustidsRelacionados()+") and id_material = "+material.getValue().toString()+" and movimiento = 'ENTRADA')");
									    			
										    		if(!bean.getRespuesta().equals("OK")){
										    			throw new Exception(bean.getRespuesta());
										    		}
								            		
										    		double monto = Double.parseDouble(bean.getDato());
									    			
									    			//Busco unidad de medida
										 		    bean = sql.consultaSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = (select unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where id = "+material.getValue().toString()+")");
										    		
								            		String unidadMedida = bean.getDato();
									    			
									    			if(!bean.getRespuesta().equals("OK")){
									    				throw new Exception(bean.getRespuesta());
									    			}
									    			
									    			
									    			
										    		tabla.addItem(new Object[]{material.getValue().toString(), material.getItemCaption(material.getValue()), cantidad.getValue(), (monto*Double.parseDouble(cantidad.getValue())), unidadMedida},material.getValue().toString());
										    		material.setValue(null);
										    		cantidad.setValue("");
										    		
										    		//Actualizo Costo Total
											    	Collection<?> ids = tabla.getItemIds();
											    	Property<String> cantidad = null;
											    	
											    	double totalCosto = 0.0;
											    	Property<Double> costoUnico = null;
											    	
											        for (Object elem : ids) {
											        	
											        	costoUnico = tabla.getItem(elem).getItemProperty("COSTO");
											        	totalCosto = totalCosto + costoUnico.getValue();
											        }
											        
											        NumberFormat formatter = NumberFormat.getCurrencyInstance();
											        totalCostoLabel.setValue("Costo total: "+formatter.format(totalCosto));
										    		
										    		
									    		}catch(Exception e){
									    			e.printStackTrace();
									    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
									    		}finally{
									    			sql.cerrar();
									    			sql = null;
									    			bean = null;
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
									    	Collection<?> ids = tabla.getItemIds();
									    	Property<String> cantidad = null;
									    	
									    	double totalCosto = 0.0;
									    	Property<Double> costoUnico = null;
									    	
									        for (Object elem : ids) {
									        	
									        	costoUnico = tabla.getItem(elem).getItemProperty("COSTO");
									        	totalCosto = totalCosto + costoUnico.getValue();
									        }
									        
									        NumberFormat formatter = NumberFormat.getCurrencyInstance();
									        totalCostoLabel.setValue("Costo total: "+formatter.format(totalCosto));
								    		
								    	}else{
								    		Notification.show("Se debe escoger alg�n material a eliminar", Type.WARNING_MESSAGE);
								    	}
								    	
									    }
									});
								
								
							//Bot�n de agregar material
								Button botonMaterial = new Button("Alta de material");
									botonMaterial.setStyleName(ValoTheme.BUTTON_LINK);
									botonMaterial.addListener(new Button.ClickListener() {
									    public void buttonClick(ClickEvent event) {
									   
			//***************************************************************************
									    	
										final Window ventanaRegistrar = new Window("Registrar material");
											ventanaRegistrar.setStyleName("VentanaSecundaria");
											ventanaRegistrar.center();
											ventanaRegistrar.setHeight("70%");
											ventanaRegistrar.setWidth("85%");
									    	
										final ComboBox proveedor = llenarComboBoxProveedor(new ComboBox(), usuario.getCustidsRelacionados());
											proveedor.setWidth("80%");
											
											
										final AutocompleteTextField categoria = new AutocompleteTextField("Categor�a");
											categoria.setWidth("80%");
											categoria.setCache(true); // Client side should cache suggestions
											categoria.setDelay(50); // Delay before sending a query to the server
											categoria.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
											categoria.setMinChars(1); // The required value length to trigger a query
											categoria.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
											categoria.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
											categoria.setSuggestionProvider(listaCategorias(usuario.getCustidsRelacionados()));
											categoria.setMaxLength(200);
											
											
										final AutocompleteTextField nombre = new AutocompleteTextField("Nombre");
											nombre.setWidth("80%");
											nombre.setCache(true); // Client side should cache suggestions
											nombre.setDelay(50); // Delay before sending a query to the server
											nombre.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
											nombre.setMinChars(1); // The required value length to trigger a query
											nombre.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
											nombre.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
											nombre.setSuggestionProvider(listaNombres(usuario.getCustidsRelacionados()));
											nombre.setMaxLength(200);

										final TextField nombreInterno = new TextField("SKU");
											nombreInterno.setWidth("80%");
											nombreInterno.setMaxLength(200);

										final NumberField minimo = new NumberField("Cantidad m�nima");
											//minimo.setWidth("80%");
										final NumberField maximo = new NumberField("Cantidad m�xima");
											
										final ComboBox unidad = llenarUnidadesMedida(new ComboBox());
										unidad.setNullSelectionAllowed(false);
										
											
										//Text change para actualizar datos precargados
											nombre.addValueChangeListener(new Property.ValueChangeListener() {
											    public void valueChange(ValueChangeEvent event) {/*.addTextChangeListener(new TextChangeListener() {
											    public void textChange(TextChangeEvent event) {*/
											    	
											    	if(!nombre.getValue().equals("")){
											    		
											    		Mysql sql = new Mysql();
											    		BeanConsulta bean;
											    		BeanConexion beanCon;
											    		ResultSet rs;
											    		
											    		try{

												 		    int existe = 0;
												 		    bean = sql.consultaSimple("select count(custid) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+usuario.getCustidsRelacionados()+") and nombre = '"+Funcion.quitarComillas(nombre.getValue())+"'");
												 		    
												    		if(!bean.getRespuesta().equals("OK")){
												    			throw new Exception(bean.getRespuesta());
												    		}
											    			
												    		existe = Integer.parseInt(bean.getDato());
											    			
											    			//int existe = Integer.parseInt(sql.consultaSimple("select count(custid) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+usuario.getCustidsRelacionados()+") and nombre = '"+Funcion.quitarComillas(nombre.getValue())+"'")); //event.getText()
											    			
											    			if(existe > 0){
											    				
											    				beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+usuario.getCustidsRelacionados()+") and nombre = '"+nombre.getValue()+"'");
											    				
											    				if(!beanCon.getRespuesta().equals("OK")){
											    					throw new Exception(beanCon.getRespuesta());
											    				}
											    				
											    				rs = beanCon.getRs();
											    				
											    				while(rs.next()){
											    					
											    					proveedor.setValue(rs.getInt("proveedor"));
											    					categoria.setValue(rs.getString("categoria"));
											    					nombreInterno.setValue(rs.getString("sku"));
											    					//costo.setValue(rs.getString("costo"));
											    					minimo.setValue(rs.getString("minimo"));
											    					maximo.setValue(rs.getString("maximo"));
											    					
											    				}
											    				
											    			}else{
														    	proveedor.setValue(null);
										    					categoria.setValue("");
										    					nombreInterno.setValue("");
										    					//costo.setValue("");
										    					minimo.setValue("");
										    					maximo.setValue("");
											    			}
											    			
											    		}catch(Exception e){
											    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
											    			e.printStackTrace();
											    		}finally{
											    			sql.cerrar();
											    			sql = null;
											    			bean = null;
											    			beanCon = null;
											    			rs = null;
											    		}

											    	//}
											    	
											    }else{
											    	proveedor.setValue(null);
							    					categoria.setValue("");
							    					nombreInterno.setValue("");
							    					//costo.setValue("");
							    					minimo.setValue("");
							    					maximo.setValue("");
											    }
											    }
											});
										
										Button registrar = new Button("Registrar");
										registrar.addListener(new Button.ClickListener() {
										    public void buttonClick(ClickEvent event) {
										    	
										    	if(!categoria.getValue().equals("") && !nombre.getValue().equals("")){
										    	
											    	Mysql sql = new Mysql();
											    	try{

											    		String minimoNull = "null";
											    		String maximoNull = "null";
											    		String unidadNull = "null";

											    		if(!minimo.getValue().equals("")){
											    			minimoNull = minimo.getValue().replaceAll("'", "");
											    		}
											    		
											    		if(!maximo.getValue().equals("")){
											    			maximoNull = maximo.getValue().replaceAll("'", "");
											    		}
											    		
											    		if(!unidad.getValue().equals("")){
											    			unidadNull = unidad.getValue().toString();
											    		}
											    		
											    		String respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.material values ("
											    				+ "null, '"+usuario.getCustid()+"','"+categoria.getValue().replaceAll("'", "")+"','"+nombre.getValue().replaceAll("'", "")+"',"
											    						+ "'"+nombreInterno.getValue().replaceAll("'", "")+"',"+minimoNull+","+maximoNull+","+proveedor.getValue()+","+unidadNull+", 'SI')");
											    		
											    		if(!respuesta.equals("OK")){
											    			throw new Exception(respuesta);
											    		}
											    		
											    	categoria.setValue("");
											    	categoria.setSuggestionProvider(listaCategorias(usuario.getCustidsRelacionados()));
													nombre.setValue("");
													nombre.setSuggestionProvider(listaNombres(usuario.getCustidsRelacionados()));
													nombreInterno.setValue("");
													minimo.setValue("");
													maximo.setValue("");
													tabla.clear();
													totalCostoLabel.setValue("");
													proveedor.setValue(null);

											    	
											//Actualiza material
											    	material.removeAllItems();
											    	
								    				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.material a where custid in ("+usuario.getCustidsRelacionados()+") group by nombre");
								    				
								    				if(!beanCon.getRespuesta().equals("OK")){
								    					throw new Exception(beanCon.getRespuesta());
								    				}
								    				
								    				ResultSet rs = beanCon.getRs();
													
													while(rs.next()){
														material.addItem(rs.getInt("id"));
														material.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
													}
													
													
													
													Notification n = new Notification("Registro de material correcto", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
							
													
													
													
											    	}catch(Exception e){
											    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
											    		e.printStackTrace();
											    	}finally{
											    		sql.cerrar();
											    		sql = null;
											    	}
											    	
											    	
										    	}else{
										    		Notification.show("Se deben de ingresar Categor�a, Nombre, Unidades y Fecha",  Type.WARNING_MESSAGE);
										    	}
											    		
											    }
											});
												
										
									//Bot�n para registrar proveedores (copiado de Proveedores.java)
											Button botonProveedor = new Button("Agregar proveedor");
												botonProveedor.setStyleName(ValoTheme.BUTTON_LINK);
												botonProveedor.addListener(new Button.ClickListener() {
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
													final TextField telefono = new TextField("Tel�fono");
														telefono.setMaxLength(100);
													final TextField correo = new TextField("Correo electr�nico");
														correo.setWidth("80%");
														correo.setMaxLength(500);
													final TextField pagina = new TextField("P�gina web");
														pagina.setWidth("80%");
														pagina.setMaxLength(100);
													final TextArea direccion = new TextArea("Direcci�n");
														direccion.setWidth("80%");
														direccion.setMaxLength(1000);
													final TextArea observaciones = new TextArea("Observaciones");
														observaciones.setWidth("80%");
														observaciones.setMaxLength(1000);
													
													Button registrar = new Button("Registrar");
													registrar.addListener(new Button.ClickListener() {
													    public void buttonClick(ClickEvent event) {
													    	
														    	Mysql sql = new Mysql();
														    	try{
														    		
														    		String respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.proveedores values ("
														    				+ "null, '"+usuario.getCustid()+"','"+Encriptar.Encriptar(nombre.getValue())+"','"+producto.getValue().replaceAll("'", "")+"',"
														    						+ "'"+Encriptar.Encriptar(contacto.getValue())+"','"+Encriptar.Encriptar(telefono.getValue())+"',"
														    								+ "'"+Encriptar.Encriptar(correo.getValue())+"','"+pagina.getValue().replaceAll("'", "")+"','"+Encriptar.Encriptar(direccion.getValue())+"',"
														    										+ "'"+observaciones.getValue().replaceAll("'", "")+"', 'SI')");
														    		
														    		if(!respuesta.equals("OK")){
														    			throw new Exception(respuesta);
														    		}
														    		
														    	nombre.setValue("");
																producto.setValue("");
																contacto.setValue("");
																telefono.setValue("");
																correo.setValue("");
																pagina.setValue("");
																direccion.setValue("");
																observaciones.setValue("");
										
															    //Actualizo ComboBox de proveedores
																	proveedor.removeAllItems();
																	
												    				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+usuario.getCustidsRelacionados()+")");
												    				
												    				if(!beanCon.getRespuesta().equals("OK")){
												    					throw new Exception(beanCon.getRespuesta());
												    				}
												    				
												    				ResultSet rs = beanCon.getRs();
																	
																	while(rs.next()){
																		proveedor.addItem(rs.getInt("id"));
																		proveedor.setItemCaption(rs.getInt("id"), Encriptar.Desencriptar(rs.getString("nombre")) + " (" + rs.getString("producto") + ")");
																	}
																
																Notification n = new Notification("Registro correcto de proveedor", Type.TRAY_NOTIFICATION);
																n.setDelayMsec(2000);
																n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
																n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
																n.show(UI.getCurrent().getPage());
																	
														    	}catch(Exception e){
														    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
														    		e.printStackTrace();
														    	}finally{
														    		sql.cerrar();
														    		sql = null;
														    	}
														    	
														    	
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
												
												
												
										
												GridLayout grid = new GridLayout(2, 7);
													grid.setMargin(true);
													grid.setWidth("100%");
													grid.setHeight("100%");
													grid.addComponent(nombre, 0, 0);
													grid.addComponent(nombreInterno, 1, 0);
													grid.addComponent(proveedor, 0, 1);
													grid.addComponent(categoria, 1, 1);
													grid.addComponent(minimo, 0, 3);
													grid.addComponent(botonProveedor, 1,3);
													grid.addComponent(maximo, 0, 4);
													grid.addComponent(unidad, 1, 4);
													grid.addComponent(registrar, 0, 5);
														grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
												
													ventanaRegistrar.setContent(grid);
													
													UI.getCurrent().addWindow(ventanaRegistrar);
									    	
									    	
			//***************************************************************************
									    	
									    }
									});
								
									
								//Hago ajuste para mostrar 
									final Label unidadMedida = new Label();
										unidadMedida.setStyleName(ValoTheme.LABEL_BOLD);
									
									material.addListener(new Property.ValueChangeListener() {
							            private static final long serialVersionUID = -5188369735622627751L;
							            public void valueChange(ValueChangeEvent event) {
							            	
							            	Mysql sql = new Mysql();
							            	BeanConsulta bean;
							            	
							            	try{
							            	
								            	if(material.getValue() == null){
								            		unidadMedida.setValue("");
								            	}else{
								            		
										 		    bean = sql.consultaSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = (select unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where id = "+material.getValue().toString()+")");
										 		    
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
									
								HorizontalLayout cantidadYUnidad = new HorizontalLayout();
								cantidadYUnidad.setWidth("50%");
									cantidadYUnidad.addComponent(cantidad);
										cantidadYUnidad.setComponentAlignment(cantidad, Alignment.MIDDLE_LEFT);
									cantidadYUnidad.addComponent(unidadMedida);
										cantidadYUnidad.setComponentAlignment(unidadMedida, Alignment.BOTTOM_RIGHT);
										
								GridLayout grid2 = new GridLayout(1, 6);
								grid2.setMargin(true);
								grid2.setWidth("100%");
								grid2.setHeight("100%");
								grid2.addComponent(material, 0, 0);
								grid2.addComponent(botonMaterial,0,1);
									grid2.setComponentAlignment(botonMaterial, Alignment.MIDDLE_RIGHT);
								grid2.addComponent(cantidadYUnidad, 0, 2);
								grid2.addComponent(tabla, 0, 3);
								
								//Agrego el Label de total costo
								grid2.addComponent(totalCostoLabel, 0, 4);
									grid2.setComponentAlignment(totalCostoLabel, Alignment.TOP_RIGHT);
								
								HorizontalLayout botones = new HorizontalLayout();
								botones.setWidth("100%");
								botones.addComponent(añadir);
									botones.setComponentAlignment(añadir, Alignment.MIDDLE_LEFT);
								botones.addComponent(eliminar);
									botones.setComponentAlignment(eliminar, Alignment.MIDDLE_RIGHT);
								grid2.addComponent(botones, 0, 5);

								
								compo.addComponent(grid2);
								
								ventanaRegistrar.setContent(tab);
								
								UI.getCurrent().addWindow(ventanaRegistrar);
				    	
				    }
				});
				
				//Inserto tabla
				
				try{
					
					//Titulo
					Label titulo = new Label("Catálogo de productos");
					titulo.setStyleName(ValoTheme.LABEL_H1);
					
					cabecera.addComponent(titulo);
						cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
					
					cabecera.addComponent(insertar);
						cabecera.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
					//dos.addComponent(insertar);
					dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
						
				
				}catch(Exception e){
					Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				}/*finally{
					sql.cerrar();
					sql = null;
				}*/
				
				respuesta.addComponent(cabecera);
				respuesta.addComponent(dos);

			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql = null;
			}

		return respuesta;
		
	}
	
	//Empiezan m�todos externos
	
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{

			tablas.removeAllComponents();
			
			beanCon = sql.conexionSimple("SELECT id, nombre, precio, iva, total, descripcion, minimo, maximo FROM "+SqlConf.obtenerBase()+"inventario.productos FORCE INDEX (custid) where custid in ("+custid+") and activo = 'SI' order by nombre");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"NOMBRE","PRECIO","IVA","TOTAL","DESCRIPCION","MINIMO","MAXIMO"};
			tablas.addComponent(crearCon2FiltrosInventario(tablas, rs, "NOMBRE", "DESCRIPCION", columnasExportar));
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
			
			if(name.toUpperCase().equals("PRECIO") || name.toUpperCase().equals("IVA") || name.toUpperCase().equals("TOTAL") || name.toUpperCase().equals("MESES") || name.toUpperCase().equals("MINIMO") || name.toUpperCase().equals("MAXIMO")){
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
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getString(4)}, id);
			}else if(count==5){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)}, id);
			}else if(count==6){
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6)}, id);
			}else if(count==7){
				tabla.addItem(new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getObject(5), rs.getObject(6), rs.getObject(7)}, id);
			}else if(count==8){ //Usa esta
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getString(6), rs.getDouble(7), rs.getDouble(8)}, id);
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
		    @SuppressWarnings("serial")
			@Override
		    public void itemClick(ItemClickEvent event) {
		    	if (event.isDoubleClick()){

		    		//Variables
		    		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
					final Table tabla = new Table();
						tabla.setLocale(Locale.US);
						
					double totalCosto = 0.0;
			        NumberFormat formatter = NumberFormat.getCurrencyInstance();

						final Label totalCostoLabel = new Label("Costo total: "+formatter.format(totalCosto));
						
					final NumberField cantidad = new NumberField("Cantidad");
					final ComboBox material = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());		    		
		    		
					
				//Agregar listener para mostrar unidad de medida
					
					
				//Hago ajuste para mostrar unidad de medida
					
					final Label unidadMedida = new Label();
						unidadMedida.setStyleName(ValoTheme.LABEL_BOLD);
					
					material.addListener(new Property.ValueChangeListener() {
			            private static final long serialVersionUID = -5188369735622627751L;
			            public void valueChange(ValueChangeEvent event) {
			            	
			            	Mysql sql = new Mysql();
			            	BeanConsulta bean;
			            	
			            	try{
			            	
				            	if(material.getValue() == null){
				            		unidadMedida.setValue("");
				            	}else{
				            		
						 		    bean = sql.consultaSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = (select unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where id = "+material.getValue().toString()+")");
						 		    
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
					
				HorizontalLayout cantidadYUnidad = new HorizontalLayout();
				cantidadYUnidad.setWidth("50%");
					cantidadYUnidad.addComponent(cantidad);
						cantidadYUnidad.setComponentAlignment(cantidad, Alignment.MIDDLE_LEFT);
					cantidadYUnidad.addComponent(unidadMedida);
						cantidadYUnidad.setComponentAlignment(unidadMedida, Alignment.BOTTOM_RIGHT);
						
					
					
					
				//***** Termina listener
					
					
		    		Property<String> itemProperty = event.getItem().getItemProperty("ID");
		    		final String id = itemProperty.getValue();

		    		final Window ventanaActualizar = new Window("Actualizar producto");
		    		ventanaActualizar.center();
		    		ventanaActualizar.setHeight("85%");
		    		ventanaActualizar.setWidth("60%");
		    				
		    		Mysql sql = new Mysql();
		    		
		    		try{
		    		
		    			TabSheet tab = new TabSheet();
						tab.setSizeFull();
					final VerticalLayout info = new VerticalLayout();
						info.setHeight("100%");
						info.setWidth("100%");
						info.setMargin(true);
						tab.addTab(info,"Informaci�n");
						
					final TextField nombre = new TextField("Nombre de producto");
						nombre.setWidth("80%");
						nombre.setMaxLength(500);
		    			
						BeanConexion beanCon = sql.conexionSimple("select *, ifnull(minimo,0) as minimo, ifnull(maximo,0) as maximo from "+SqlConf.obtenerBase()+"inventario.productos where id = "+id);
						
						if(!beanCon.getRespuesta().equals("OK")){
							throw new Exception(beanCon.getRespuesta());
						}
						
						ResultSet rs = beanCon.getRs();
						
						final NumberField precio = new NumberField("Precio");
							//precio.setEnabled(false);
						
						//Agregar check de IVA
						final CheckBox iva = new CheckBox("�Grava IVA?");
						//iva.setValue(true);
						
						
						final TextArea descripcion = new TextArea("Descripci�n");
							descripcion.setWidth("100%");
							descripcion.setMaxLength(500);
		    				
						final NumberField minimo = new NumberField("M�nimo");
							minimo.setNullRepresentation("");
							minimo.setDecimalAllowed(true);
							minimo.setDecimalPrecision(3);
							minimo.setErrorText("N�mero no v�lido");
							minimo.setInvalidAllowed(false);
							minimo.setNegativeAllowed(false);
							
						final NumberField maximo = new NumberField("M�ximo");
							maximo.setNullRepresentation("");
							maximo.setDecimalAllowed(true);
							maximo.setDecimalPrecision(3);
							maximo.setErrorText("N�mero no v�lido");
							maximo.setInvalidAllowed(false);
							maximo.setNegativeAllowed(false);
							
						HorizontalLayout minimosMaximos = new HorizontalLayout();
							minimosMaximos.setWidth("50%");
							minimosMaximos.addComponent(minimo);
							minimosMaximos.setComponentAlignment(minimo, Alignment.BOTTOM_LEFT);
							minimosMaximos.addComponent(maximo);
							minimosMaximos.setComponentAlignment(maximo, Alignment.BOTTOM_RIGHT);
							
		    			while(rs.next()){
		    				nombre.setValue(rs.getString("nombre"));
		    				precio.setValue(rs.getString("total"));
		    				descripcion.setValue(rs.getString("descripcion"));
		    				minimo.setValue(Funcion.decimales(rs.getString("minimo")));
		    				maximo.setValue(Funcion.decimales(rs.getString("maximo")));
		    				
		    				if(rs.getString("grava_iva").equals("SI")){
		    					iva.setValue(true);
		    				}else{
		    					iva.setValue(false);
		    				}
		    				
		    			}
		    				
		    			//Actualizo tabla

		    			Button registrar = new Button("Actualizar producto");
		    			registrar.addListener(new Button.ClickListener() {
		    			    @SuppressWarnings("unchecked")
							public void buttonClick(ClickEvent event) {
		    				    Mysql sql = new Mysql();
		    				    String respuesta = "NO";
		    				    
		    			    	try{
		    			    	
							    	if(nombre.getValue().equals("") || precio.getValue().equals("") || tabla.size() == 0){
							    		Notification.show("Se debe ingresar nombre, precio y componentes ", Type.WARNING_MESSAGE); 
							    	}else{
		    			    		
							    		
							    		
							    		/*String minimoAjustado = "null";
							    		String maximoAjustado = "null";
							    		
							    		if(!minimo.getValue().equals("")){
							    			minimoAjustado = minimo.getValue();
							    		}
							    		
							    		if(!maximo.getValue().equals("")){
							    			maximoAjustado = maximo.getValue();
							    		}*/
							    		
							    	//Abrir transacci�n
							    		
							    		sql.transaccionAbrir();
							    		
							    		if(iva.getValue() == true){

							    			respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.productos set "
								    				+ "nombre = '"+Funcion.quitarComillas(nombre.getValue())+"', precio = "+(Double.parseDouble(precio.getValue())/1.16)+", iva = "+(Double.parseDouble(precio.getValue())/1.16)*.16+", total = "+Double.parseDouble(precio.getValue())+","
								    						+ "descripcion = '"+Funcion.quitarComillas(descripcion.getValue())+"', grava_iva = 'SI', minimo = "+(minimo.getValue().equals("")?"null":minimo.getValue())+", maximo = "+(maximo.getValue().equals("")?"null":maximo.getValue())+" where id = "+id);
							    			
							    			if(!respuesta.equals("OK")){
							    				throw new Exception(respuesta);
							    			}
							    			
							    		}else{
							    			
							    			respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.productos set "
								    				+ "nombre = '"+Funcion.quitarComillas(nombre.getValue())+"', precio = "+Double.parseDouble(precio.getValue())+", iva = 0.0, total = "+Double.parseDouble(precio.getValue())+","
								    						+ "descripcion = '"+Funcion.quitarComillas(descripcion.getValue())+"', grava_iva = 'NO', minimo = "+(minimo.getValue().equals("")?"null":minimo.getValue())+", maximo = "+(maximo.getValue().equals("")?"null":maximo.getValue())+" where id = "+id);
							    			
							    			if(!respuesta.equals("OK")){
							    				throw new Exception(respuesta);
							    			}
							    			
							    		}
		    				    		
		    				    	//Actualizo producci�n
		    				    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.produccion where custid = '"+usuario.getCustid()+"' and id_prod = '"+id+"'");
		    				    		
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}
		    				    		
		    				    		JSONObject json = new JSONObject();
		    				    		
								    	Collection<?> ids = tabla.getItemIds();
								    	Property<String> cantidad = null;
								    	
								        for (Object elem : ids) {
								        	
								        	cantidad = tabla.getItem(elem).getItemProperty("CANTIDAD");
								        	json.put(elem.toString(), Double.parseDouble(cantidad.getValue()));
								        	respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.produccion values ('"+usuario.getCustid()+"','"+id+"','"+elem+"','"+cantidad.getValue()+"')");
			    				    		
								        	if(!respuesta.equals("OK")){
			    				    			throw new Exception(respuesta);
			    				    		}
								        }
								        
								        //Inserto hist�rico
								        respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.produccion_historico values ("+usuario.getCustid()+","+id+",'"+json.toString()+"', now())");
								        
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}
								        
								    //Cerrar transacci�n
		    				    		
		    				    		if(respuesta.equals("OK")){
		    				    			
		    				    			//Cierro transacci�n
		    				    				sql.transaccionCommit();
		    				    				sql.transaccionCerrar();
		    				    		
			    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
			    				    		
											Notification n = new Notification("Actualizaci�n correcta del producto", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
		    				    		
		    				    		}
		    				    		
							    	}
		    				    		
		    			    	}catch(Exception e){
		    			    		
		    			    		//Cierro transacci�n
		    			    			sql.transaccionRollBack();
		    			    			sql.transaccionCerrar();
		    			    		
		    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
		    			    		e.printStackTrace();
		    			    		
		    			    	}finally{
		    			    		sql.cerrar();
		    			    	}
		    				    		
		    			    }
		    			});
		    				   
		    			Button eliminarProducto = new Button("Eliminar producto");
		    			eliminarProducto.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    		
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmaci�n", "�Est�s seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
				    				    Mysql sql = new Mysql();
				    			    	String respuesta = "NO";
				    				    
				    				    try{
				    			    	
				    			    		//Abro transacci�n
				    			    		sql.transaccionAbrir();
				    			    		
				    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.productos set activo = 'NO' where id = "+id);
				    				    		
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}
				    				    		
				    				    		respuesta = sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.produccion where id_prod = "+id);
				    				    		
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}
				    				    		
				    				    		if(respuesta.equals("OK")){
				    				    			
				    				    			//Cierro transacci�n
				    				    				sql.transaccionCommit();
				    				    				sql.transaccionCerrar();
				    				    		
					    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
					    				    		
													Notification n = new Notification("Producto eliminado correctamente", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent().getPage());
					    				    		
					    				    		//Notification.show("Producto eliminado correctamente", Type.TRAY_NOTIFICATION);
					    				    		ventanaActualizar.close();
				    				    		
				    				    		}
				    				    		
				    			    	}catch(Exception e){
				    			    		
				    			    		//Cierro transacci�n
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
				    			    	
		    			    }
		    			});
		    			
						GridLayout grid = new GridLayout(1, 5);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.addComponent(nombre, 0, 0);
						
						HorizontalLayout precios = new HorizontalLayout();
						precios.setWidth("40%");
						precios.addComponent(precio);
							precios.setComponentAlignment(precio, Alignment.MIDDLE_LEFT);
						precios.addComponent(iva);
							precios.setComponentAlignment(iva, Alignment.MIDDLE_RIGHT);
						
						grid.addComponent(precios, 0, 1);
						grid.addComponent(descripcion, 0, 2);
						grid.addComponent(minimosMaximos, 0, 3);
							grid.setComponentAlignment(minimosMaximos, Alignment.TOP_LEFT);
						
						HorizontalLayout botonesProducto = new HorizontalLayout();
							botonesProducto.setWidth("100%");
						botonesProducto.addComponent(registrar);
						botonesProducto.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
						botonesProducto.addComponent(eliminarProducto);
						botonesProducto.setComponentAlignment(eliminarProducto, Alignment.BOTTOM_RIGHT);
						
						grid.addComponent(botonesProducto, 0, 4);
							grid.setHeight("100%");
							grid.setWidth("100%");
		    				
						info.addComponent(grid);
						
						//Agrego tabla
						
						final VerticalLayout compo = new VerticalLayout();
						compo.setHeight("100%");
						compo.setWidth("100%");
						compo.setMargin(true);
						tab.addTab(compo,"Componentes");

						material.setWidth("80%");

						tabla.setHeight("200");
						tabla.setWidth("100%");
						tabla.setSelectable(true);

							tabla.addContainerProperty("ID", String.class, null);
								tabla.setColumnCollapsingAllowed(true);
								tabla.setColumnCollapsed("ID", true);
							tabla.addContainerProperty("MATERIAL", String.class, null);
							tabla.addContainerProperty("CANTIDAD", String.class, null);
							tabla.addContainerProperty("COSTO", Double.class, null);
							tabla.addContainerProperty("MEDIDA", String.class, null);

							
							//Lleno tabla
							
							beanCon = sql.conexionSimple("SELECT b.id as id, b.nombre as nombre, a.cantidad as cantidad, (select nombre from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = b.unidad_medida) as medida, a.id_mat "+
									"FROM "+SqlConf.obtenerBase()+"inventario.produccion a, "+SqlConf.obtenerBase()+"inventario.material b "+
									"where a.id_prod = "+id+ " and " +
									"a.id_mat = b.id");
							
							if(!beanCon.getRespuesta().equals("OK")){
								throw new Exception(beanCon.getRespuesta());
							}
							
							rs = beanCon.getRs();
							
							while(rs.next()){
							
								//Obtengo costo
								Mysql sqlInterno = new Mysql();
								
								try{
								
					    			BeanConsulta bean = sqlInterno.consultaSimple("select ifnull(total, 0.0) as total, count(custid) from "+SqlConf.obtenerBase()+"inventario.inventario where custid in ("+usuario.getCustidsRelacionados()+") and id_material = "+rs.getString("id_mat")+" and movimiento = 'ENTRADA' and fecha = (select max(fecha) from "+SqlConf.obtenerBase()+"inventario.inventario force index (custid) where custid in ("+usuario.getCustidsRelacionados()+") and id_material = "+rs.getString("id_mat")+" and movimiento = 'ENTRADA')");
					    			
					    			if(!bean.getRespuesta().equals("OK")){
					    				throw new Exception(bean.getRespuesta());
					    			}
					    			
					    			double monto = Double.parseDouble(bean.getDato());
									
									tabla.addItem(new Object[]{rs.getString("id"), rs.getString("nombre"), Funcion.decimales(rs.getString("cantidad")), (rs.getDouble("cantidad")*monto), rs.getString("medida")}, rs.getString("id"));
								
									totalCosto = totalCosto + (rs.getDouble("cantidad")*monto);
									
								}catch(Exception e){
									e.printStackTrace();
								}finally{
									sqlInterno.cerrar();
								}
								
						        totalCostoLabel.setValue("Costo total: "+formatter.format(totalCosto));
								
							}
						
					Button añadir = new Button("Agregar");
					añadir.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {

					    	if(material.getValue() == null || cantidad.getValue().equals("")){
					    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
					    	}else{
					    		
					    		Mysql sql = new Mysql();
					    		
					    		try{
					    			
					    			//Busco costo
					    			BeanConsulta bean = sql.consultaSimple("select ifnull(total, 0.0) as total, count(custid) from "+SqlConf.obtenerBase()+"inventario.inventario where custid in ("+usuario.getCustidsRelacionados()+") and id_material = "+material.getValue().toString()+" and movimiento = 'ENTRADA' and fecha = (select max(fecha) from "+SqlConf.obtenerBase()+"inventario.inventario force index (custid) where custid in ("+usuario.getCustidsRelacionados()+") and id_material = "+material.getValue().toString()+" and movimiento = 'ENTRADA')");
					    			
					    			if(!bean.getRespuesta().equals("OK")){
					    				throw new Exception(bean.getRespuesta());
					    			}
					    			
					    			double monto = Double.parseDouble(bean.getDato());
					    			
						    		tabla.addItem(new Object[]{material.getValue().toString(), material.getItemCaption(material.getValue()), cantidad.getValue(), (monto*Double.parseDouble(cantidad.getValue())), unidadMedida.getValue()},material.getValue().toString());
						    		material.setValue(null);
						    		cantidad.setValue("");
						    		
						    		//Actualizo Costo Total
							    	Collection<?> ids = tabla.getItemIds();
							    	Property<String> cantidad = null;
							    	
							    	double totalCosto = 0.0;
							    	NumberFormat myFormat = NumberFormat.getInstance();
							        myFormat.setGroupingUsed(true);
							    	Property<Double> costoUnico = null;
							    	
							        for (Object elem : ids) {
							        	
							        	costoUnico = tabla.getItem(elem).getItemProperty("COSTO");
							        	totalCosto = totalCosto + costoUnico.getValue();
							        }
							        
							        NumberFormat formatter = NumberFormat.getCurrencyInstance();
							        totalCostoLabel.setValue("Costo total: "+formatter.format(totalCosto));
						    		
						    		
					    		}catch(Exception e){
					    			e.printStackTrace();
					    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
					    		}finally{
					    			sql.cerrar();
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
						    	Collection<?> ids = tabla.getItemIds();
						    	Property<String> cantidad = null;
						    	
						    	double totalCosto = 0.0;
						    	Property<Double> costoUnico = null;
						    	
						        for (Object elem : ids) {
						        	
						        	costoUnico = tabla.getItem(elem).getItemProperty("COSTO");
						        	totalCosto = totalCosto + costoUnico.getValue();
						        }
						        
						        NumberFormat formatter = NumberFormat.getCurrencyInstance();
						        totalCostoLabel.setValue("Costo total: "+formatter.format(totalCosto));
					    		
					    	}else{
					    		Notification.show("Se debe escoger algún material a eliminar", Type.WARNING_MESSAGE);
					    	}
					    	
						    }
						});
					
					
					GridLayout grid2 = new GridLayout(1, 5);
					grid2.setMargin(true);
					grid2.setWidth("100%");
					grid2.setHeight("100%");
					grid2.addComponent(material, 0, 0);
					
					grid2.addComponent(cantidadYUnidad);
					//grid2.addComponent(cantidad, 0, 1);
					grid2.addComponent(tabla, 0, 2);
					grid2.addComponent(totalCostoLabel, 0, 3);
					
					HorizontalLayout botones = new HorizontalLayout();
					botones.setWidth("100%");
					botones.addComponent(añadir);
						botones.setComponentAlignment(añadir, Alignment.MIDDLE_LEFT);
					botones.addComponent(eliminar);
						botones.setComponentAlignment(eliminar, Alignment.MIDDLE_RIGHT);
					grid2.addComponent(botones, 0, 4);

					
					compo.addComponent(grid2);
						
						
		    			ventanaActualizar.setContent(tab);
							
							
		    			UI.getCurrent().addWindow(ventanaActualizar);
		    			
		    		}catch(Exception e){
		    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
		    			e.printStackTrace();
		    		}finally{
		    			sql.cerrar();
		    			sql = null;
		    		}
		    		
		    		
		    	}
		    }
		});
		
		return respuesta;
	}

	private ComboBox llenarComboBox(ComboBox combo, String custid){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
			combo.setCaption("Material");
			
			beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.material a where custid in ("+custid+") group by nombre");
			
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
		}
		
		combo.setNullSelectionAllowed(false);
		
		return combo;
		
	}
	
	private ComboBox llenarComboBoxProveedor(ComboBox combo, String custid){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
			combo.setCaption("Proveedores");
			
			beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+custid+")");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();

			while(rs.next()){
				combo.addItem(rs.getInt("id"));
				combo.setItemCaption(rs.getInt("id"), Encriptar.Desencriptar(rs.getString("nombre")) + " (" + rs.getString("producto") + ")");
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
	
	private AutocompleteSuggestionProvider listaCategorias(String custid){
		
		List<String> nombres = new ArrayList<String>();
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
			beanCon = sql.conexionSimple("select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") group by categoria");
			
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
			
			beanCon = sql.conexionSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") group by nombre");
			
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
	
	private ComboBox llenarUnidadesMedida(ComboBox combo){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
			combo.setCaption("Unidad de medida");
			
			beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.unidad_medida");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
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
			sql = null;
			beanCon = null;
			rs = null;
		}
		
		combo.setNullSelectionAllowed(false);
		
		return combo;
		
	}
	
}

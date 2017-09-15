package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class Material {

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
				
				final Button insertar = new Button("Registrar nuevo material");
				insertar.setStyleName("boton_registrar_nuevo");
				insertar.addListener(new Button.ClickListener() {
					
				    public void buttonClick(ClickEvent event) {
					
					final Window ventanaRegistrar = new Window("Registrar");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
				    	
					final ComboBox proveedor = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						proveedor.setWidth("80%");
						
						
					final AutocompleteTextField categoria = new AutocompleteTextField("Categoría");
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

					final NumberField minimo = new NumberField("Cantidad mínima");
						minimo.setDecimalAllowed(true);
						minimo.setDecimalPrecision(3);
						//minimo.setWidth("80%");
					final NumberField maximo = new NumberField("Cantidad máxima");
						maximo.setDecimalAllowed(true);
						maximo.setDecimalPrecision(3);
						
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
						    			Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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
						    	String respuesta = "";
						    	
						    	try{

						    		String minimoNull = "null";
						    		String maximoNull = "null";
						    		String unidadNull = "null";
						    		
						    		if(minimo.getValue() != null){
							    		if(!minimo.getValue().equals("")){
							    			minimoNull = minimo.getValue().replaceAll("'", "");
							    		}
						    		}
						    		
						    		if(maximo.getValue() != null){
						    			if(!maximo.getValue().equals("")){
							    			maximoNull = maximo.getValue().replaceAll("'", "");
							    		}
						    		}
						    		
						    		if(unidad.getValue() != null){
							    		if(!unidad.getValue().equals("")){
							    			unidadNull = unidad.getValue().toString();
							    		}
						    		}
						    		
						    		respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.material values ("
						    				+ "null, '"+usuario.getCustid()+"','"+categoria.getValue().replaceAll("'", "")+"','"+nombre.getValue().replaceAll("'", "")+"',"
						    						+ "'"+nombreInterno.getValue().replaceAll("'", "")+"',"+minimoNull+","+maximoNull+","+proveedor.getValue()+","+unidadNull+",'SI')");
						    		
	    				    		if(!respuesta.equals("OK")){
	    				    			throw new Exception(respuesta);
	    				    		}
	    				    		
	    				    		if(respuesta.equals("OK")){
						    		
								    	categoria.setValue("");
								    	categoria.setSuggestionProvider(listaCategorias(usuario.getCustidsRelacionados()));
										nombre.setValue("");
										nombre.setSuggestionProvider(listaNombres(usuario.getCustidsRelacionados()));
										nombreInterno.setValue("");
										minimo.setValue("");
										maximo.setValue("");
										proveedor.setValue(null);
		
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
						    		respuesta = null;
						    	}
						    	
						    	dos.removeAllComponents();
						    	//dos.addComponent(insertar);
						    	dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
						    		//dos.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
						    	
					    	}else{
					    		Notification.show("Se deben de ingresar Categoría, Nombre, Unidades y Fecha",  Type.WARNING_MESSAGE);
					    	}
						    		
						    }
						});
							
					
				//Bot�n para registrar proveedores (copiado de Proveedores.java
						Button botonProveedor = new Button("Agregar proveedor");
							botonProveedor.setStyleName(ValoTheme.BUTTON_LINK);
							botonProveedor.addListener(new Button.ClickListener() {
							    public void buttonClick(ClickEvent event) {
							   
							    	final Window ventanaRegistrar = new Window("Registrar proveedor");
							    		ventanaRegistrar.setStyleName("VentanaSecundaria");
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
				    				    		}
				    				    		
				    				    		if(respuesta.equals("OK")){
				    				    				
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
														
									    				BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+usuario.getCustidsRelacionados()+") and activo = 'SI'");
									    				
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
												
									    	}
											
									    	}catch(Exception e){
									    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
									    		e.printStackTrace();
									    	}finally{
									    		sql.cerrar();
									    		sql = null;
									    		respuesta = null;
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
				    	
				    }
				});
				
				//Inserto tabla
				
				try{

					dos.addComponent(insertar);
					dos.addComponent(generarTabla(tablas, usuario.getCustidsRelacionados()));
						dos.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
				
				}catch(Exception e){
					Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				}/*finally{
					sql.cerrar();
					sql = null;
				}*/
				
				//Titulo
				Label titulo = new Label("Catálogo de material");
				titulo.setStyleName(ValoTheme.LABEL_H1);
				
				cabecera.addComponent(titulo);
					cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
				
				cabecera.addComponent(insertar);
					cabecera.setComponentAlignment(insertar, Alignment.MIDDLE_RIGHT);
				respuesta.addComponent(cabecera);
					respuesta.setComponentAlignment(cabecera, Alignment.MIDDLE_LEFT);
				respuesta.addComponent(dos);

			}catch(Exception e){
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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
			
			beanCon = sql.conexionSimple("SELECT id, categoria, nombre, sku, (select nombre from "+SqlConf.obtenerBase()+"inventario.proveedores where id = a.proveedor) as proveedor, minimo, maximo FROM "+SqlConf.obtenerBase()+"inventario.material a FORCE INDEX (custid) where a.custid in ("+custid+") and activo = 'SI' group by a.categoria, a.nombre");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"CATEGORIA","NOMBRE","SKU","PROVEEDOR","MINIMO","MAXIMO"};
			tablas.addComponent(crearCon2FiltrosInventario(tablas, rs, "CATEGORIA", "NOMBRE", columnasExportar));
			tablas.setComponentAlignment(tablas.getComponent(0), Alignment.TOP_CENTER);
		
		}catch(Exception e){
			Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6)}, id);
			}else if(count==7){ //�ste es el que usa
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
		    				
		    		Mysql sql = new Mysql();
		    		BeanConexion beanCon;
		    		ResultSet rs;
		    		
		    		try{
		    		
		    			beanCon = sql.conexionSimple("select ifnull(categoria,'-') as categoria, nombre, ifnull(sku,'-') as sku, ifnull(minimo,0) as minimo, ifnull(maximo,0) as maximo, ifnull(proveedor,0) as proveedor, unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where id = "+id);
		    			
		    			if(!beanCon.getRespuesta().equals("OK")){
		    				throw new Exception(beanCon.getRespuesta());
		    			}
		    			
		    			rs = beanCon.getRs();

						final ComboBox proveedor = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
						proveedor.setWidth("80%");
						
						final AutocompleteTextField categoria = new AutocompleteTextField("Categoría");
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

					final NumberField minimo = new NumberField("Cantidad mínima");
						minimo.setDecimalAllowed(true);
						minimo.setDecimalPrecision(3);

					final NumberField maximo = new NumberField("Cantidad máxima");
		    			maximo.setDecimalAllowed(true);
		    			maximo.setDecimalPrecision(3);
					
					final ComboBox unidadActualiza = llenarUnidadesMedida(new ComboBox());
					
					
		    			while(rs.next()){
		    				proveedor.setValue(rs.getInt("proveedor"));
		    				categoria.setValue(rs.getString("categoria"));
		    				nombre.setValue(rs.getString("nombre"));
		    				if(rs.getString("sku") == null)
		    					nombreInterno.setValue("");
		    				else
		    					nombreInterno.setValue(rs.getString("sku"));
		    				if(rs.getString("minimo") == null)
		    					minimo.setValue("");
		    				else
		    					minimo.setValue(Funcion.decimales(rs.getString("minimo")));
		    				if(rs.getString("maximo") == null)
		    					maximo.setValue("");
		    				else
		    					maximo.setValue(Funcion.decimales(rs.getString("maximo")));

		    				unidadActualiza.setValue(rs.getInt("unidad_medida"));

		    			}
		    				
		    			Button registrar = new Button("Actualizar");
		    			registrar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    Mysql sql = new Mysql();
		    				    String respuesta = "NO";
		    				    
		    			    	try{
		    			    	
		    			    		String nombreInterno2 = "";
		    			    		double minimoLong = 0;
		    			    		double maximoLong = 0;
		    			    		
		    			    		if(!nombreInterno.getValue().equals(""))
		    			    			nombreInterno2 = nombreInterno.getValue();
		    			    		if(!minimo.getValue().equals(""))
		    			    			minimoLong = Double.parseDouble(minimo.getValue());
		    			    		if(!maximo.getValue().equals(""))
		    			    			maximoLong = Double.parseDouble(maximo.getValue());
		    			    			
		    			    		
		    			    		
		    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.material set categoria = '"+categoria.getValue()+"', nombre = '"+nombre.getValue().replaceAll("'", "")+"',"
		    				    				+ "sku = '"+nombreInterno2.replaceAll("'", "")+"', minimo = "
		    				    								+minimoLong+", maximo = "+maximoLong+","
		    				    										+ " proveedor = "+proveedor.getValue()+", unidad_medida = "+unidadActualiza.getValue().toString()+" where id = "+id);
		    			
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}
		    				    		
		    				    		if(respuesta.equals("OK")){
		    				    		
			    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
			    				    		
											Notification n = new Notification("Correcta actualización de material", Type.TRAY_NOTIFICATION);
											n.setDelayMsec(2000);
											n.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
											n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
											n.show(UI.getCurrent().getPage());
		    				    		
		    				    		}
		    				    		
		    			    	}catch(Exception e){
		    			    		Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
		    			    		e.printStackTrace();
		    			    	}finally{
		    			    		sql.cerrar();
		    			    		sql = null;
		    			    		respuesta = null;
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
				    			    		
				    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.material set activo = 'NO' where id = "+id);
				    			
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}
				    				    		
				    				    		if(respuesta.equals("OK")){
				    				    		
					    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
					    				    		
													Notification n = new Notification("Eliminaci�n correcta de material", Type.TRAY_NOTIFICATION);
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
				    			    		sql = null;
				    			    		respuesta = null;
				    			    	}
		    				    
		    			                }
		    			            }
		    			            
		    					});
		    			    	
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
						grid.addComponent(minimo, 0, 4);
						grid.addComponent(maximo, 0, 5);
						grid.addComponent(unidadActualiza, 1, 5);
						grid.addComponent(registrar, 0, 6);
							grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
						grid.addComponent(eliminar, 1, 6);
							grid.setComponentAlignment(eliminar, Alignment.BOTTOM_LEFT);
		    				
		    			ventanaActualizar.setContent(grid);
		    			
		    			UI.getCurrent().addWindow(ventanaActualizar);
		    			
		    		}catch(Exception e){
		    			Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
		    			e.printStackTrace();
		    		}finally{
		    			sql.cerrar();
		    			sql = null;
		    			beanCon = null;
		    			rs = null;
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
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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
				
    			beanCon = sql.conexionSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and activo = 'SI' group by nombre");
    			
    			if(!beanCon.getRespuesta().equals("OK")){
    				throw new Exception(beanCon.getRespuesta());
    			}
    			
    			rs = beanCon.getRs();
    			
				while(rs.next()){
					nombres.add(rs.getString("nombre"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
				sql =  null;
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
				
    			beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+custid+") and activo = 'SI'");
    			
    			if(!beanCon.getRespuesta().equals("OK")){
    				throw new Exception(beanCon.getRespuesta());
    			}
    			
    			rs = beanCon.getRs();
				
				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), Encriptar.Desencriptar(rs.getString("nombre")) + " (" + rs.getString("producto") + ")");
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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
		
		private ComboBox actualizarComboBox(ComboBox combo, String custid){
			
			Mysql sql = new Mysql();
			BeanConexion beanCon;
			ResultSet rs;
			
			try{
				
				combo.setCaption("Proveedores");
				combo.removeAllItems();
				
    			beanCon = sql.conexionSimple("SELECT id, nombre, producto FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ('"+custid+"')");
    			
    			if(!beanCon.getRespuesta().equals("OK")){
    				throw new Exception(beanCon.getRespuesta());
    			}
    			
    			rs = beanCon.getRs();
				
				while(rs.next()){
					combo.addItem(rs.getInt("id"));
					combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
				}
				
			}catch(Exception e){
				Notification.show("Error en la aplicaci�n: "+e.toString(), Type.ERROR_MESSAGE);
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

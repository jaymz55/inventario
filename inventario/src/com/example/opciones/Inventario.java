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
import funciones.Funcion;

public class Inventario {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setMargin(true);
		
		final HorizontalLayout titulo = new HorizontalLayout();
			titulo.setHeight("70");
			titulo.setWidth("90%");
			
			Label tituloLabel = new Label("Registro de entradas y salidas de inventario");
			tituloLabel.setStyleName(ValoTheme.LABEL_H1);
			
			titulo.addComponent(tituloLabel);
			
			respuesta.addComponent(titulo);
			
		final VerticalLayout tablas = new VerticalLayout();
			final VerticalLayout dos = new VerticalLayout();
			
		//Variables
			Mysql sql = new Mysql();
			final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
			try{
			
				final Button insertar = new Button("Registrar nuevo material");
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
					
						final Window ventanaRegistrar = new Window("Registrar");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
				    	
					final ComboBox proveedor = llenarComboBox(new ComboBox(), usuario.getCustid());
						proveedor.setWidth("80%");
						
						
					final AutocompleteTextField categoria = new AutocompleteTextField("Categoría");
						categoria.setWidth("80%");
						categoria.setCache(true); // Client side should cache suggestions
						categoria.setDelay(50); // Delay before sending a query to the server	
						categoria.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
						categoria.setMinChars(1); // The required value length to trigger a query
						categoria.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
						categoria.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
						categoria.setSuggestionProvider(listaCategorias(usuario.getCustid()));
						categoria.setMaxLength(200);
						
						
					final AutocompleteTextField nombre = new AutocompleteTextField("Nombre");
						nombre.setWidth("80%");
						nombre.setCache(true); // Client side should cache suggestions
						nombre.setDelay(50); // Delay before sending a query to the server
						nombre.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
						nombre.setMinChars(1); // The required value length to trigger a query
						nombre.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
						nombre.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
						nombre.setSuggestionProvider(listaNombres(usuario.getCustid()));
						nombre.setMaxLength(200);

					final TextField nombreInterno = new TextField("SKU");
						nombreInterno.setWidth("80%");
						nombreInterno.setMaxLength(200);
					final NumberField unidades = new NumberField("Unidades");
						unidades.setDecimalAllowed(true);
						unidades.setDecimalPrecision(3);
					
					final NumberField costo = new NumberField("Costo por unidad");
						//costo.setWidth("80%");
					//final NumberField minimo = new NumberField("Cantidad mínima");
						//minimo.setWidth("80%");
					//final NumberField maximo = new NumberField("Cantidad máxima");
					
					final DateField fecha = new DateField("Fecha");
					fecha.setDateFormat("dd MMMM yyyy");
					//fecha.setValue(new Date());
						
						
					//Text change para actualizar datos precargados
						nombre.addValueChangeListener(new Property.ValueChangeListener() {
						    public void valueChange(ValueChangeEvent event) {/*.addTextChangeListener(new TextChangeListener() {
						    public void textChange(TextChangeEvent event) {*/
						    	
						    	if(!nombre.getValue().equals("")){
						    		
						    		Mysql sql = new Mysql();
						    		
						    		try{

							 		    int existe = 0;
							 		    BeanConsulta bean = sql.consultaSimple("select count(custid) from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+usuario.getCustid()+"' and nombre = '"+Funcion.quitarComillas(nombre.getValue())+"'");
							 		    
							    		if(!bean.getRespuesta().equals("OK")){
							    			throw new Exception(bean.getRespuesta());
							    		}
						    			
							    		existe = Integer.parseInt(bean.getDato());
							    		
						    			//int existe = Integer.parseInt(sql.consultaSimple("select count(custid) from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+usuario.getCustid()+"' and nombre = '"+Funcion.quitarComillas(nombre.getValue())+"'")); //event.getText()
						    			
						    			if(existe > 0){
						    				
						    				BeanConexion beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"+usuario.getCustid()+"' and nombre = '"+nombre.getValue()+"'");
						    				
						    				if(!beanCon.getRespuesta().equals("OK")){
						    					throw new Exception(beanCon.getRespuesta());
						    				}
						    				
						    				ResultSet rs = beanCon.getRs();

						    				while(rs.next()){
						    					
						    					proveedor.setValue(rs.getInt("proveedor"));
						    					categoria.setValue(rs.getString("categoria"));
						    					nombreInterno.setValue(rs.getString("sku"));
						    					costo.setValue(rs.getString("costo"));
						    					//minimo.setValue(rs.getString("minimo"));
						    					//maximo.setValue(rs.getString("maximo"));
						    					
						    				}
						    				
						    			}else{
									    	proveedor.setValue(null);
					    					categoria.setValue("");
					    					nombreInterno.setValue("");
					    					costo.setValue("");
					    					//minimo.setValue("");
					    					//maximo.setValue("");
						    			}
						    			
						    		}catch(Exception e){
						    			Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
						    			e.printStackTrace();
						    		}finally{
						    			sql.cerrar();
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
						});
					
					Button registrar = new Button("Registrar");
					registrar.addListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					    	
					    	if(!categoria.getValue().equals("") && !nombre.getValue().equals("") && !unidades.getValue().equals("") && fecha.getValue() != null){
					    	
						    	Mysql sql = new Mysql();
						    	try{

						    		String minimoNull = "null";
						    		String maximoNull = "null";

						    		/*if(!minimo.getValue().equals("")){
						    			minimoNull = minimo.getValue().replaceAll("'", "");
						    		}
						    		
						    		if(!maximo.getValue().equals("")){
						    			maximoNull = maximo.getValue().replaceAll("'", "");
						    		}*/
						    		
						    		sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.inventario values ("
						    				+ "null, '"+usuario.getCustid()+"','"+categoria.getValue().replaceAll("'", "")+"','"+nombre.getValue().replaceAll("'", "")+"',"
						    						+ "'"+nombreInterno.getValue().replaceAll("'", "")+"',"+unidades.getValue().replaceAll("'", "")+","
						    								+ ""+costo.getValue().replaceAll("'", "")+","+minimoNull+","+maximoNull+",null, '"+Funcion.fechaFormato(fecha.getValue(), "yyyy-MM-dd")+"',"+proveedor.getValue()+")");
						    		
						    	categoria.setValue("");
						    	categoria.setSuggestionProvider(listaCategorias(usuario.getCustid()));
								nombre.setValue("");
								nombre.setSuggestionProvider(listaNombres(usuario.getCustid()));
								nombreInterno.setValue("");
								unidades.setValue("");
								costo.setValue("");
								//minimo.setValue("");
								//maximo.setValue("");
								proveedor.setValue(null);

		
								Notification.show("Registro de inventario correcto", Type.TRAY_NOTIFICATION);
						    		
						    	}catch(Exception e){
						    		Notification.show("Error al registrar: "+e.toString(), Type.ERROR_MESSAGE);
						    		e.printStackTrace();
						    	}finally{
						    		sql.cerrar();
						    	}
						    	
						    	dos.removeAllComponents();
						    	dos.addComponent(insertar);
						    	dos.addComponent(generarTabla(tablas, usuario.getCustid()));
						    		dos.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
						    	
					    	}else{
					    		Notification.show("Se deben de ingresar Categoría, Nombre, Unidades y Fecha",  Type.WARNING_MESSAGE);
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
								

								
								
								grid.addComponent(costo, 1, 3);
								//grid.addComponent(minimo, 0, 4);
								grid.addComponent(fecha, 1, 4);
								//grid.addComponent(maximo, 0, 5);
								grid.addComponent(registrar, 0, 6);
									grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
							
								ventanaRegistrar.setContent(grid);
								
								UI.getCurrent().addWindow(ventanaRegistrar);
				    	
				    }
				});
				
				//Inserto tabla
				
				try{

					dos.addComponent(insertar);
					dos.addComponent(generarTabla(tablas, usuario.getCustid()));
						dos.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
				
				}catch(Exception e){
					Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				}finally{
					sql.cerrar();
				}
				
				respuesta.addComponent(dos);

			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
				sql.cerrar();
			}

		return respuesta;
		
	}
	
	//Empiezan métodos externos
	
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid){
		
		Mysql sql = new Mysql();
		
		try{

			tablas.removeAllComponents();
			
			BeanConexion beanCon = sql.conexionSimple("SELECT id, (select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid = "+custid+" and id = a.id_material) as categoria, "
					+ "(select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid = "+custid+" and id = a.id_material) as nombre, (select sku from "+SqlConf.obtenerBase()+"inventario.material where custid = "+custid+" and id = a.id_material) as sku, sum(unidades) as existencia, (select minimo from "+SqlConf.obtenerBase()+"inventario.material where custid = "+custid+" and id = a.id_material) as minimo, (select (sum(a.unidades))-minimo from "+SqlConf.obtenerBase()+"inventario.material where custid = 15 and id = a.id_material) as diferencial FROM "+SqlConf.obtenerBase()+"inventario.inventario a where a.custid = '"+custid+"' group by id_material order by diferencial");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			ResultSet rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"CATEGORIA","NOMBRE","SKU","EXISTENCIA","MINIMO","DIFERENCIAL"};
			tablas.addComponent(crearCon2FiltrosInventario(tablas, rs, "CATEGORIA", "NOMBRE", columnasExportar));
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
	public VerticalLayout crearCon2FiltrosInventario(final VerticalLayout tablas, ResultSet rs, final String tituloColumnaFiltrar, final String tituloColumnaFiltrar2, final Object[] columnasExportar) throws SQLException{

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
			
			if(name.toUpperCase().equals("EXISTENCIA") || name.toUpperCase().equals("MINIMO") || name.toUpperCase().equals("DIFERENCIAL")){
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
			}else if(count==7){ //Éste es el que usa
				tabla.addItem(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7)}, id);
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

		    		final Window ventanaActualizar = new Window("Actualizar");
		    		ventanaActualizar.center();
		    		ventanaActualizar.setHeight("70%");
		    		ventanaActualizar.setWidth("85%");
		    				
		    		final Mysql sql = new Mysql();
		    		
		    		try{
		    		
		    			BeanConexion beanCon = sql.conexionSimple("select ifnull(categoria,'-') as categoria, nombre, ifnull(sku,'-') as nombre_interno, unidades, costo, ifnull(minimo,0) as minimo, ifnull(maximo,0) as maximo, id_producto, fecha, proveedor from "+SqlConf.obtenerBase()+"inventario.inventario where id = "+id);
		    			
		    			if(!beanCon.getRespuesta().equals("OK")){
		    				throw new Exception(beanCon.getRespuesta());
		    			}
		    			
		    			ResultSet rs = beanCon.getRs();

						final ComboBox proveedor = llenarComboBox(new ComboBox(), usuario.getCustid());
						proveedor.setWidth("80%");
						
						final TextField categoria = new TextField("Categoría");
						categoria.setWidth("80%");
						categoria.setEnabled(false);
						/*categoria.setCache(true); // Client side should cache suggestions
						categoria.setDelay(50); // Delay before sending a query to the server
						categoria.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
						categoria.setMinChars(1); // The required value length to trigger a query
						categoria.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
						categoria.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
						categoria.setSuggestionProvider(listaCategorias(usuario.getCustid()));*/
						
						
						
					final TextField nombre = new TextField("Nombre");
						nombre.setWidth("80%");
						nombre.setEnabled(false);
						/*nombre.setCache(true); // Client side should cache suggestions
						nombre.setDelay(50); // Delay before sending a query to the server
						nombre.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
						nombre.setMinChars(1); // The required value length to trigger a query
						nombre.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
						nombre.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
						nombre.setSuggestionProvider(listaNombres(usuario.getCustid()));*/
						

					final TextField sku = new TextField("SKU");
						sku.setWidth("80%");
						sku.setEnabled(false);

					final NumberField minimo = new NumberField("Cantidad mínima");
						minimo.setEnabled(false);

					final NumberField maximo = new NumberField("Cantidad máxima");
						maximo.setEnabled(false);
		    				
		    			while(rs.next()){
		    				proveedor.setValue(rs.getInt("proveedor"));
		    				categoria.setValue(rs.getString("categoria"));
		    				nombre.setValue(rs.getString("nombre"));
		    				if(rs.getString("sku") == null)
		    					sku.setValue("");
		    				else
		    					sku.setValue(rs.getString("sku"));
		    				if(rs.getString("minimo") == null)
		    					minimo.setValue("");
		    				else
		    					minimo.setValue(rs.getString("minimo"));
		    				if(rs.getString("maximo") == null)
		    					maximo.setValue("");
		    				else
		    					maximo.setValue(rs.getString("maximo"));

		    			}
		    				
		    			Button registrar = new Button("Actualizar");
		    			registrar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    Mysql sql = new Mysql();
		    			    	try{
		    			    	
		    			    		String nombreInterno2 = "";
		    			    		long minimoLong = 0;
		    			    		long maximoLong = 0;
		    			    		
		    			    		if(!sku.getValue().equals(""))
		    			    			nombreInterno2 = sku.getValue();
		    			    		if(!minimo.getValue().equals(""))
		    			    			minimoLong = Long.parseLong(minimo.getValue());
		    			    		if(!maximo.getValue().equals(""))
		    			    			maximoLong = Long.parseLong(maximo.getValue());
		    			    			
		    			    		
		    			    		
		    				    		sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.inventario set categoria = '"+categoria.getValue()+"', nombre = '"+nombre.getValue().replaceAll("'", "")+"',"
		    				    				+ "sku = '"+nombreInterno2.replaceAll("'", "")+"', minimo = "
		    				    								+minimoLong+", maximo = "+maximoLong+","
		    				    										+ " proveedor = "+proveedor.getValue()+" where id = "+id);
		    			
		    				    		generarTabla(tablas, usuario.getCustid());
		    				    		Notification.show("Correcta actualización de inventario", Type.TRAY_NOTIFICATION);
		    				    		
		    			    	}catch(Exception e){
		    			    		Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
		    			    		e.printStackTrace();
		    			    	}finally{
		    			    		sql.cerrar();
		    			    	}
		    				    		
		    			    }
		    			});
		    			
						GridLayout grid = new GridLayout(2, 7);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.addComponent(nombre, 0, 0);
						grid.addComponent(sku, 1, 0);
						grid.addComponent(proveedor, 0, 1);
						grid.addComponent(categoria, 1, 1);
						grid.addComponent(minimo, 0, 4);
						grid.addComponent(maximo, 0, 5);
						grid.addComponent(registrar, 0, 6);
							grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
		    				
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
				
				combo.setCaption("Proveedores");
				
    			BeanConexion beanCon = sql.conexionSimple("SELECT id, concat(nombre,' (',producto,')') as nombre FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid = '"+custid+"'");
    			
    			if(!beanCon.getRespuesta().equals("OK")){
    				throw new Exception(beanCon.getRespuesta());
    			}
    			
    			ResultSet rs = beanCon.getRs();
				
				//ResultSet rs = sql.conexionSimple("SELECT id, concat(nombre,' (',producto,')') as nombre FROM "+SqlConf.obtenerBase()+"inventario.proveedores where custid = '"+custid+"'");
				
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
		
}

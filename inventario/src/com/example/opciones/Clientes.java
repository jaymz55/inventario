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
import com.vaadin.ui.ComboBox;
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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import conexiones.BeanConexion;
import conexiones.Mysql;
import funciones.Encriptar;
import funciones.Funcion;

public class Clientes {

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
			
				final Button insertar = new Button("Registrar nuevo cliente");
				insertar.setStyleName("boton_registrar_nuevo");
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
					
					final Window ventanaRegistrar = new Window("Registrar");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
				    	
					final TextField nombre = new TextField("Cliente");
						nombre.setWidth("80%");
						nombre.setMaxLength(400);

					final TextField telefono = new TextField("Teléfono");
						telefono.setWidth("80%");
						telefono.setMaxLength(80);
					final TextField correo = new TextField("Correo electrónico");
						correo.setWidth("80%");
						correo.setMaxLength(400);
						
					final ComboBox vendedorAsignado = llenarComboBoxVendedoresAsignados(new ComboBox(), usuario.getCustidsRelacionados());
						vendedorAsignado.setWidth("60%");
						
					final TextArea direccion = new TextArea("Dirección");
						direccion.setWidth("80%");
						direccion.setMaxLength(800);
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
							    	String respuesta = "NO";
							    	try{
							    		
							    		String vendedorAsignadoString = "null";
							    		
							    		if(vendedorAsignado.getValue() != null){
							    			vendedorAsignadoString = vendedorAsignado.getValue().toString();
							    		}
							    		
							    		respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.clientes values ("
							    				+ "null, '"+usuario.getCustid()+"','"+Encriptar.Encriptar(nombre.getValue())+"','"+Encriptar.Encriptar(telefono.getValue())+"',"
							    						+ "'"+Encriptar.Encriptar(correo.getValue())+"','"+Encriptar.Encriptar(direccion.getValue())+"',"
							    										+ "'"+Funcion.quitarComillas(observaciones.getValue())+"',"+vendedorAsignadoString+",'SI')");
							    		
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}else{
							    		
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
									
		    				    		}
		    				    		
							    	}catch(Exception e){
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
				
				//Inserto tabla
				
				try{
					
					//Titulo
					Label titulo = new Label("Registro de clientes");
					titulo.setStyleName(ValoTheme.LABEL_H1);
					
					cabecera.addComponent(titulo);
						cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
					

					cabecera.addComponent(insertar);
						cabecera.setComponentAlignment(insertar, Alignment.BOTTOM_RIGHT);
						
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
	
	//Empiezan métodos externos
	
	private VerticalLayout generarTabla(VerticalLayout tablas, String custid){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{

			tablas.removeAllComponents();
			
			beanCon = sql.conexionSimple("SELECT id, nombre, telefono, correo, direccion, observaciones, (select nombre from "+SqlConf.obtenerBase()+"inventario.vendedores where id = vendedor_asignado) as vendedor FROM "+SqlConf.obtenerBase()+"inventario.clientes FORCE INDEX (custid) where custid in ("+custid+") and activo = 'SI'");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			tablas.removeAllComponents();
			//Genero el array con las columnas a exportar en Excel (no incluyendo ID)
			Object[] columnasExportar = {"NOMBRE","TELEFONO","CORREO","DIRECCION","OBSERVACIONES","VENDEDOR"};
			tablas.addComponent(crearCon2FiltrosInventario(tablas, rs, "NOMBRE", "CORREO", columnasExportar));
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
				tabla.addItem(new Object[]{rs.getString(1), Encriptar.Desencriptar(rs.getString(2)), Encriptar.Desencriptar(rs.getString(3)), Encriptar.Desencriptar(rs.getString(4)), Encriptar.Desencriptar(rs.getString(5)), Encriptar.Desencriptar(rs.getString(6))}, id);
			}else if(count==7){ //USA ESTE
				tabla.addItem(new Object[]{rs.getString(1), Encriptar.Desencriptar(rs.getString(2)), Encriptar.Desencriptar(rs.getString(3)), Encriptar.Desencriptar(rs.getString(4)), Encriptar.Desencriptar(rs.getString(5)), rs.getString(6), rs.getString("vendedor")}, id);
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
		    				
		    		Mysql sql = new Mysql();
		    		BeanConexion beanCon;
		    		ResultSet rs;
		    		
		    		try{
		    		
		    			beanCon = sql.conexionSimple("select * from "+SqlConf.obtenerBase()+"inventario.clientes where id = "+id);
		    			
		    			if(!beanCon.getRespuesta().equals("OK")){
		    				throw new Exception(beanCon.getRespuesta());
		    			}
		    			
		    			rs = beanCon.getRs();
		    			
					final TextField nombre = new TextField("Cliente");
						nombre.setWidth("80%");
						nombre.setMaxLength(400);
					final TextField telefono = new TextField("Teléfono");
						telefono.setWidth("80%");
						telefono.setMaxLength(80);
					final TextField correo = new TextField("Correo electrónico");
						correo.setWidth("80%");
						correo.setMaxLength(400);
					final ComboBox vendedorAsignado = llenarComboBoxVendedoresAsignados(new ComboBox(), usuario.getCustidsRelacionados());
						vendedorAsignado.setWidth("60%");
					final TextArea direccion = new TextArea("Dirección");
						direccion.setWidth("80%");
						direccion.setMaxLength(800);
					final TextArea observaciones = new TextArea("Observaciones");
						observaciones.setWidth("80%");
						observaciones.setMaxLength(1000);
		    				
		    			while(rs.next()){
		    				nombre.setValue(Encriptar.Desencriptar(rs.getString("nombre")));
		    				telefono.setValue(Encriptar.Desencriptar(rs.getString("telefono")));
		    				correo.setValue(Encriptar.Desencriptar(rs.getString("correo")));
		    				vendedorAsignado.setValue(rs.getInt("vendedor_asignado"));
		    				direccion.setValue(Encriptar.Desencriptar(rs.getString("direccion")));
		    				observaciones.setValue(rs.getString("observaciones"));
		    			}
		    				
		    			Button registrar = new Button("Actualizar");
		    			registrar.addListener(new Button.ClickListener() {
		    			    public void buttonClick(ClickEvent event) {
		    				    Mysql sql = new Mysql();
		    				    String respuesta = "NO";
		    			    	try{
		    			    	
		    			    		String vendedorAjustado = "null";
		    			    		if(!vendedorAsignado.getValue().toString().equals("")){
		    			    			vendedorAjustado = vendedorAsignado.getValue().toString();
		    			    		}
		    			    		
		    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.clientes set nombre = '"+Encriptar.Encriptar(nombre.getValue())+"',"
		    				    				+ "telefono = '"+Encriptar.Encriptar(telefono.getValue())+"', correo = "
		    				    								+ "'"+Encriptar.Encriptar(correo.getValue())+"', direccion = '"+Encriptar.Encriptar(direccion.getValue())+"',"
		    				    										+ "observaciones = '"+observaciones.getValue().replaceAll("'", "")+"', vendedor_asignado = "+vendedorAjustado+" where id = "+id);
		    			
		    				    		if(!respuesta.equals("OK")){
		    				    			throw new Exception(respuesta);
		    				    		}else{
		    				    		
			    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
			    				    		
											Notification n = new Notification("Correcta actualización de cliente", Type.TRAY_NOTIFICATION);
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
		    				    		
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {

		    			            public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			    	
				    				    Mysql sql = new Mysql();
				    				    String respuesta = "NO";
				    			    	try{
				    			    	
				    				    		respuesta = sql.insertarSimple("update "+SqlConf.obtenerBase()+"inventario.clientes set activo = 'NO' where id = "+id);
				    				    		
				    				    		if(!respuesta.equals("OK")){
				    				    			throw new Exception(respuesta);
				    				    		}else{
				    				    		
					    				    		generarTabla(tablas, usuario.getCustidsRelacionados());
					    				    		
													Notification n = new Notification("Registro eliminado correctamente", Type.TRAY_NOTIFICATION);
													n.setDelayMsec(5000);
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
		    			            }
		    			            
		    					});
				    			    	
		    			    }
		    			});
		    			
		    			GridLayout grid = new GridLayout(2, 6);
		    			grid.setMargin(true);
		    			grid.setWidth("100%");
		    			grid.setHeight("100%");
						grid.addComponent(nombre, 0, 0);
						grid.addComponent(telefono, 1, 0);
						grid.addComponent(correo, 0, 1);
						grid.addComponent(vendedorAsignado, 1, 1);
						grid.addComponent(direccion, 0, 2);
						grid.addComponent(observaciones, 1, 2);
		    			grid.addComponent(registrar, 0, 5);
		    				grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
		    			grid.addComponent(eliminar, 1, 5);
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
		});
		
		return respuesta;
	}
	
	private ComboBox llenarComboBoxVendedoresAsignados(ComboBox combo, String custid){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
			combo.setCaption("Vendedor asignado");
			
			beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid in ("+custid+") order by nombre");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();

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
			sql = null;
			beanCon = null;
			rs = null;
		}
		
		combo.setNullSelectionAllowed(false);
		
		return combo;
		
	}
	
}

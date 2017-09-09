package com.example.opciones;

import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;
import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;

import com.example.inventario.Usuario;
//import com.itextpdf.text.log.SysoLogger;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import conexiones.BeanConexion;
import conexiones.Mysql;
import graficas.Area;
import graficas.Barras;
import graficas.Lineal;
import graficas.Pie;



public class GraficasProductos {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		//Tablas
			VerticalLayout vertical = new VerticalLayout();
			vertical.setWidth("100%");
			vertical.setHeight("100%");
			vertical.setMargin(true);
			//vertical.setStyleName(ValoTheme.LAYOUT_WELL);
			
		//Para cabecera
			final HorizontalLayout campos = new HorizontalLayout();
				campos.setWidth("50%");
				campos.setHeight("50");
				//campos.setStyleName(ValoTheme.LAYOUT_WELL);
			
		//Para gráfica lineal
			final HorizontalLayout graficasLayLineal = new HorizontalLayout();
			graficasLayLineal.setWidth("90%");
			graficasLayLineal.setHeight("400");
			//graficasLayLineal.setStyleName(ValoTheme.LAYOUT_WELL);
			
		//Para contener los cuadros de abajo
			final HorizontalLayout contenedorBajo = new HorizontalLayout();
			contenedorBajo.setHeight("300");
			contenedorBajo.setWidth("90%");
			//contenedorBajo.setStyleName(ValoTheme.LAYOUT_WELL);
			
			
		//Para datos fijos
			final HorizontalLayout graficasLayDatos = new HorizontalLayout();
			graficasLayDatos.setWidth("100%");
			graficasLayDatos.setHeight("300");
			
		//Para gráfica circular
			final HorizontalLayout graficasLayPie = new HorizontalLayout();
			//graficasLayPie.setStyleName(ValoTheme.LAYOUT_WELL);
			graficasLayPie.setWidth("100%");
			graficasLayPie.setHeight("300");
			
			contenedorBajo.addComponent(graficasLayDatos);
			contenedorBajo.addComponent(graficasLayPie);
				contenedorBajo.setExpandRatio(graficasLayDatos, 2);
				contenedorBajo.setExpandRatio(graficasLayPie, 1);
				
	//Variables
			
			final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
				
			final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
				producto.setWidth("50%");
				
			final ComboBox año = new ComboBox("Año");
				año.setWidth("100");
				año.setNullSelectionAllowed(false);
				
				Calendar now = Calendar.getInstance();   // Gets the current date and time
				int year = now.get(Calendar.YEAR);
				
				for (int i = 2016; i <= year; i++) {
					año.addItem(String.valueOf(i));
				}
				
				año.setValue(String.valueOf(year));				
				
			//Cambio de tipo de gráfico
				
				producto.addValueChangeListener(new Property.ValueChangeListener() {
				    public void valueChange(ValueChangeEvent event) {
				   
				    	if(!producto.getValue().toString().equals("0")){
				    	
					    	Mysql sql = new Mysql();
					    	Mysql sql2 = new Mysql();
					    	BeanConexion beanCon;
					    	BeanConexion beanCon2;
					    	ResultSet rs;
					    	ResultSet rs2;
							Lineal grafica = new Lineal();
							Pie graficaPie = new Pie();
							Barras graficaBarras = new Barras();
							Vector<ResultSet> vector;
					    	
					    	try{
					    	
						    	beanCon  = sql.conexionSimple(query(usuario.getCustidsRelacionados(), producto.getValue().toString(), año.getValue().toString()));
						    	
								if(!beanCon.getRespuesta().equals("OK")){
									throw new Exception(beanCon.getRespuesta());
								}
								
								rs = beanCon.getRs();
								
						    	
						    	beanCon2  = sql2.conexionSimple(query2(usuario.getCustidsRelacionados(), producto.getValue().toString(), año.getValue().toString()));
						    	
								if(!beanCon2.getRespuesta().equals("OK")){
									throw new Exception(beanCon2.getRespuesta());
								}
								
								rs2 = beanCon2.getRs();
								
						    	vector = new Vector<ResultSet>();
						    	vector.add(rs);
						    	vector.add(rs2);
					    		
						    	//Defino colores
						    	List<Color> colores = new ArrayList<Color>();
						    		colores.add(new Color(109, 232, 162));
						    		//colores.add(new Color(249, 151, 151));
						    		colores.add(new Color(242, 151, 4));
						    	
						    	graficasLayLineal.removeAllComponents();
					    		graficasLayLineal.addComponent(grafica.Graficar(vector, "Producto - "+producto.getItemCaption(producto.getValue()), null, "Monto", colores));
					    		graficasLayLineal.setComponentAlignment(graficasLayLineal.getComponent(0), Alignment.MIDDLE_CENTER);
					    		
					    	//Agrego Barras
					    		
					    		beanCon2  = sql2.conexionSimple(queryBarras(usuario.getCustidsRelacionados(), producto.getValue().toString(), año.getValue().toString()));
						    	
								if(!beanCon2.getRespuesta().equals("OK")){
									throw new Exception(beanCon2.getRespuesta());
								}
								
								rs2 = beanCon2.getRs();
								
						    	vector = new Vector<ResultSet>();
						    	vector.add(rs2);
					    		
					    		
					    		graficasLayDatos.removeAllComponents();
					    		graficasLayDatos.addComponent(graficaBarras.GraficarClientes(vector, "Mejores clientes", null, "# de compras"));
					    		graficasLayDatos.setComponentAlignment(graficasLayDatos.getComponent(0), Alignment.MIDDLE_CENTER);
					    		
					    	//Agrego Pie
					    		
					    		graficasLayPie.removeAllComponents();
					    		graficasLayPie.addComponent(graficaPie.GraficarProducto(usuario.getCustidsRelacionados(), "Ganancia", producto.getItemCaption(producto.getValue()), producto.getValue().toString(), colores, 90));
					    		graficasLayPie.setComponentAlignment(graficasLayPie.getComponent(0), Alignment.MIDDLE_CENTER);
					    		
					    		
					    	}catch(Exception e){
					    		e.printStackTrace();
					    	}finally{
					    		sql.cerrar();
					    		sql = null;
					    		sql2.cerrar();
					    		sql2 = null;
					    		beanCon = null;
					    		rs = null;
					    		rs = null;
					    		grafica = null;
					    		graficaPie = null;
					    		graficaBarras = null;
					    		vector = null;
					    	}
				    	
				    	
				    	}
				    	
				    }
				});

			
		campos.addComponent(producto);
			campos.setComponentAlignment(producto, Alignment.BOTTOM_LEFT);
		vertical.addComponent(campos);
			vertical.setComponentAlignment(campos, Alignment.TOP_LEFT);
			
		vertical.addComponent(graficasLayLineal);
			vertical.setComponentAlignment(graficasLayLineal, Alignment.MIDDLE_CENTER);
		
		vertical.addComponent(contenedorBajo);
			vertical.setComponentAlignment(contenedorBajo, Alignment.MIDDLE_CENTER);


		return vertical;
	}
	
	
//Empiezan métodos externos
	
	private String query(String custid, String producto, String año){
		
			
			return "SELECT 'Ventas' as nombreSerie, concat(LPAD(month(str_to_date(a.mnth,'%M')),2,0),'-', '"+año+"') as mes, ifnull(SUM((b.total*b.cantidad) - b.descuento),0) as dato\r\n" + 
					"FROM   (\r\n" + 
					"            SELECT 'January' mnth, 1 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'February' mnth, 2 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'March' mnth, 3 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'April' mnth,4 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'May' mnth,5 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'June' mnth,6 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'July' mnth,7 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'August' mnth,8 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'September' mnth,9 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'October' mnth,10 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'November' mnth,11 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'December' mnth,12 rn\r\n" + 
					"        ) a\r\n" + 
					"        LEFT JOIN "+SqlConf.obtenerBase()+"inventario.ventas b force index (custid)\r\n" + 
					"            ON a.mnth = DATE_FORMAT(b.fecha, '%M') AND\r\n" + 
					"               year(b.fecha) =  '"+año+"' AND\r\n" + 
					"               DATE_FORMAT(b.fecha, '%M') IN ('January', 'February','March','April','May','June','July','August', 'September', 'October', 'November', 'December')\r\n" + 
					"              and b.custid in ("+custid+") and b.id_producto = "+producto+"\r\n" + 
					"GROUP  BY a.mnth, year(b.fecha),month(b.fecha)\r\n" + 
					"ORDER  BY a.rn, b.fecha ASC";
			
		
		
	}
	
	private String query2(String custid, String producto, String año){
		
			
			return "SELECT 'Costos' as nombreSerie, concat(LPAD(month(str_to_date(a.mnth,'%M')),2,0),'-', '"+año+"') as mes, ifnull(SUM(b.total*b.unidades),0) as dato\r\n" + 
					"FROM   (\r\n" + 
					"            SELECT 'January' mnth, 1 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'February' mnth, 2 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'March' mnth, 3 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'April' mnth,4 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'May' mnth,5 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'June' mnth,6 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'July' mnth,7 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'August' mnth,8 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'September' mnth,9 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'October' mnth,10 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'November' mnth,11 rn\r\n" + 
					"            UNION ALL\r\n" + 
					"            SELECT 'December' mnth,12 rn\r\n" + 
					"        ) a\r\n" + 
					"        LEFT JOIN "+SqlConf.obtenerBase()+"inventario.inventario b force index (custid)\r\n" + 
					"            ON a.mnth = DATE_FORMAT(b.fecha, '%M') AND\r\n" + 
					"               year(b.fecha) =  '"+año+"' AND\r\n" + 
					"               DATE_FORMAT(b.fecha, '%M') IN ('January', 'February','March','April','May','June','July','August', 'September', 'October', 'November', 'December')\r\n" + 
					"              and b.custid in ("+custid+")\r\n" + 
					"			   and b.movimiento = 'ENTRADA' and b.id_producto = "+producto+" " +
					"GROUP  BY a.mnth, year(b.fecha),month(b.fecha)\r\n" + 
					"ORDER  BY a.rn, b.fecha ASC";
		
		
	}
	
	private String queryBarras(String custid, String producto, String año){
		
		
		return "SELECT 'Cliente' as nombreSerie, id_cliente, (select nombre from "+SqlConf.obtenerBase()+"inventario.clientes where id = a.id_cliente) as nombre, sum(cantidad) as dato\r\n" + 
				"FROM "+SqlConf.obtenerBase()+"inventario.ventas a force index (custid)\r\n" + 
				"where custid in ("+custid+")\r\n" + 
				//"and year(fecha) = "+año+"\r\n" + 
				"and movimiento = 'VENTA'\r\n" + 
				"and id_cliente is not null\r\n" + 
				"and id_producto = "+producto+"\r\n" + 
				"group by id_cliente\r\n" + 
				"order by sum(cantidad) desc\r\n" + 
				"limit 10";
		
	
	
	}

	
	
	private ComboBox llenarComboBox(ComboBox combo, String custid){
		
		Mysql sql = new Mysql();
		
		try{
			
			combo.setCaption("Productos");
			combo.addItem(0);
			combo.setItemCaption(0, "Seleccionar");
			
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
		combo.setValue(combo.getItemIds().iterator().next());
		
		return combo;
		
	}
	
}

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



public class Graficas {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo(){
		
		//Tablas
			VerticalLayout vertical = new VerticalLayout();
			vertical.setWidth("100%");
			vertical.setHeight("100%");
			vertical.setMargin(true);
			
			final HorizontalLayout campos = new HorizontalLayout();
				campos.setWidth("50%");
				campos.setMargin(true);
				campos.setStyleName(ValoTheme.LAYOUT_WELL);
			
			final HorizontalLayout cuerpo = new HorizontalLayout();
			
			cuerpo.setWidth("100%");
			cuerpo.setHeight("100%");
			
			final HorizontalLayout graficasLay = new HorizontalLayout();
			graficasLay.setMargin(true);
			graficasLay.setStyleName(ValoTheme.LAYOUT_WELL);
			graficasLay.setWidth("80%");
			//graficasLay.setHeight("100%");
			
	//Variables
			
			final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			
			final ComboBox tipo = new ComboBox("Tipo de reporte");
				tipo.setWidth("95%");
				tipo.setNullSelectionAllowed(false);
				tipo.addItem("Ventas vs Costos (Con IVA)");
				tipo.addItem("Ventas vs Costos (Sin IVA)");
				tipo.addItem("Productos m�s vendidos");
				tipo.addItem("Mejores clientes");
				//tipo.addItem("Ganancia por producto");
				
				tipo.setValue("Ventas vs Costos (Con IVA)");
				tipo.setStyleName("boton_simple");
				
			final ComboBox año = new ComboBox("año");
				año.setWidth("100");
				año.setNullSelectionAllowed(false);
				
				Calendar now = Calendar.getInstance();   // Gets the current date and time
				int year = now.get(Calendar.YEAR);
				
				for (int i = 2016; i <= year; i++) {
					año.addItem(String.valueOf(i));
				}
				
				año.setValue(String.valueOf(year));
				
			final ComboBox producto = llenarComboBox(new ComboBox(), usuario.getCustidsRelacionados());
				producto.setWidth("95%");
				producto.setVisible(false);
				
			//Cambio de tipo de gr�fico
				
				tipo.addValueChangeListener(new Property.ValueChangeListener() {
				    public void valueChange(ValueChangeEvent event) {
				   
				    	if(tipo.getValue().toString().equals("Ganancia por producto")){
				    		producto.setVisible(true);
				    		año.setVisible(false);
				    	}else{
				    		producto.setVisible(false);
				    		año.setVisible(true);
				    	}
				    	
				    }
				});
				
				
			//Condicional de valor 
				
			final Button graficar = new Button("Graficar");
				graficar.setStyleName(ValoTheme.BUTTON_PRIMARY);
				graficar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {

				    	Lineal grafica = new Lineal();
				    	Barras graficaBarras = new Barras();
				    	Pie graficaPie = new Pie();
				    	Area graficaArea = new Area();
				    	
				    	Mysql sql = new Mysql();
				    	Mysql sql2 = new Mysql();
				    	
					    	try{
					    	
						    	BeanConexion beanCon  = sql.conexionSimple(query(usuario.getCustidsRelacionados(), tipo.getValue().toString(), año.getValue().toString()));
						    	
								if(!beanCon.getRespuesta().equals("OK")){
									throw new Exception(beanCon.getRespuesta());
								}
								
								ResultSet rs = beanCon.getRs();
								
						    	
						    	BeanConexion beanCon2  = sql2.conexionSimple(query2(usuario.getCustidsRelacionados(), tipo.getValue().toString(), año.getValue().toString()));
						    	
								if(!beanCon2.getRespuesta().equals("OK")){
									throw new Exception(beanCon2.getRespuesta());
								}
								
								ResultSet rs2 = beanCon2.getRs();
								
						    	Vector<ResultSet> vector = new Vector<ResultSet>();
						    	vector.add(rs);
						    	
						    	if(!tipo.getValue().toString().equals("Productos m�s vendidos") && !tipo.getValue().toString().equals("Ganancia por producto") && !tipo.getValue().toString().equals("Mejores clientes"))
						    		vector.add(rs2);
						    	
						    	//Defino colores
						    	List<Color> colores = new ArrayList<Color>();
						    		colores.add(new Color(109, 232, 162));
						    		//colores.add(new Color(249, 151, 151));
						    		colores.add(new Color(242, 151, 4));
					    	
						    //Cargo en LayOut
						    	graficasLay.removeAllComponents();
					    		
						    	if(tipo.getValue().toString().equals("Productos m�s vendidos")){
						    		graficasLay.addComponent(graficaBarras.Graficar(vector, "Productos m�s vendidos - "+año.getValue().toString(), null, "# de ventas"));
						    	}else if (tipo.getValue().toString().equals("Mejores clientes")){
						    		graficasLay.addComponent(graficaBarras.GraficarClientes(vector, "Mejores clientes - "+año.getValue().toString(), null, "# de compras"));						    		
						    	}else if (tipo.getValue().toString().equals("Ganancia por producto")){
						    		graficasLay.addComponent(graficaPie.GraficarProducto(usuario.getCustidsRelacionados(), "An�lisis por producto - "+producto.getItemCaption(producto.getValue()), producto.getItemCaption(producto.getValue()), producto.getValue().toString(), colores, 80));						    		
						    	}else if (tipo.getValue().toString().equals("Area")){
						    		graficasLay.addComponent(graficaArea.Graficar(vector, "Productos m�s vendidos - "+año.getValue().toString(), null, "# de ventas", colores));						    		
						    	}else{
						    		graficasLay.addComponent(grafica.Graficar(vector, "Ventas vs Costos - "+año.getValue().toString(), null, null, colores));
						    	}
						    	
						    	graficasLay.setComponentAlignment(graficasLay.getComponent(0), Alignment.MIDDLE_CENTER);
						    	
						    	cuerpo.removeAllComponents();
						    	cuerpo.addComponent(graficasLay);
						    	cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
					    		
					            
					    	} catch (Exception e) {
								e.printStackTrace();
							}finally{
								sql.cerrar();
									sql = null;
								sql2.cerrar();
									sql2 = null;
									
								grafica = null;
								graficaBarras = null;
							}
				    	
					    }
					});
			
		campos.addComponent(tipo);
			campos.setComponentAlignment(tipo, Alignment.BOTTOM_CENTER);
		campos.addComponent(año);
			campos.setComponentAlignment(año, Alignment.BOTTOM_CENTER);
		campos.addComponent(producto);
			campos.setComponentAlignment(producto, Alignment.BOTTOM_CENTER);
		campos.addComponent(graficar);
			campos.setComponentAlignment(graficar, Alignment.BOTTOM_CENTER);
		vertical.addComponent(campos);
		
	//Agrego segunda parte
		//dos.addComponent(tablas);
		cuerpo.setMargin(true);
		vertical.addComponent(cuerpo);

		return vertical;
	}
	
	
//Empiezan m�todos externos
	
	private String query(String custid, String reporte, String año){
		
		if(reporte.equals("Ventas vs Costos (Con IVA)")){
			
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
					"              and b.custid in ("+custid+")\r\n" + 
					"GROUP  BY a.mnth, year(b.fecha),month(b.fecha)\r\n" + 
					"ORDER  BY a.rn, b.fecha ASC";
			
			/*return "SELECT 'Ventas' as nombreSerie, concat(LPAD(month(fecha), 2, '0'),'/', year(fecha)) as mes, sum(total) as dato\r\n" + 
	    			"FROM "+SqlConf.obtenerBase()+"inventario.ventas force index(custid)\r\n" + 
	    			"where custid in ("+custid+")\r\n" + 
	    			"  and datediff(curdate(), fecha) < 366\r\n" + 
	    			"group by month(fecha)\r\n" + 
	    			"order by fecha";*/
			
		}else if(reporte.equals("Ventas vs Costos (Sin IVA)")){
			
			return "SELECT 'Ventas' as nombreSerie, concat(LPAD(month(str_to_date(a.mnth,'%M')),2,0),'-', '"+año+"') as mes, ifnull(SUM((b.precio*b.cantidad) - b.descuento),0) as dato\r\n" + 
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
					"              and b.custid in ("+custid+")\r\n" + 
					"GROUP  BY a.mnth, year(b.fecha),month(b.fecha)\r\n" + 
					"ORDER  BY a.rn, b.fecha ASC";
			
		}else if(reporte.equals("Productos m�s vendidos")){
			
			return "SELECT 'Ventas' as nombreSerie, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as nombre, sum(cantidad) as dato\r\n" + 
					"FROM "+SqlConf.obtenerBase()+"inventario.ventas a force index (custid)\r\n" + 
					"where custid in ("+custid+")\r\n" + 
					"and year(fecha) = " +año+
					" and movimiento = 'VENTA'" +
					" group by id_producto\r\n" + 
					"order by sum(cantidad) desc\r\n" + 
					"limit 10";
			
		}else if(reporte.equals("Mejores clientes")){
			
			return "SELECT 'Cliente' as nombreSerie, id_cliente, (select nombre from "+SqlConf.obtenerBase()+"inventario.clientes where id = a.id_cliente) as nombre, sum(cantidad) as dato\r\n" + 
					"FROM "+SqlConf.obtenerBase()+"inventario.ventas a force index (custid)\r\n" + 
					"where custid in ("+custid+")\r\n" + 
					"and year(fecha) = "+año+"\r\n" + 
					"and movimiento = 'VENTA'\r\n" + 
					"and id_cliente is not null\r\n" + 
					"group by id_cliente\r\n" + 
					"order by sum(cantidad) desc\r\n" + 
					"limit 10";
			
		}else if(reporte.equals("Ganancia por producto")){
			
			return "select 'Ganancia' as nombre, 10 as 'dato'\r\n";  
					/*"union\r\n" + 
					"select 'Costo' as nombre, 5 as 'dato'";*/
			
		}else{
			
			return "Reporte no encontrado";
		
		}
		
	}
	
	private String query2(String custid, String reporte, String año){
		
		if(reporte.equals("Ventas vs Costos (Con IVA)")){
			
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
					"			   and b.movimiento = 'ENTRADA' " +
					"GROUP  BY a.mnth, year(b.fecha),month(b.fecha)\r\n" + 
					"ORDER  BY a.rn, b.fecha ASC";
			
			/*return "SELECT 'Costos' as nombreSerie, concat(LPAD(month(fecha), 2, '0'),'/', year(fecha)) as mes, sum(total) as dato\r\n" + 
	    			"FROM "+SqlConf.obtenerBase()+"inventario.inventario FORCE INDEX(custid)\r\n" + 
	    			"where custid in ("+custid+")\r\n" + 
	    			"  and datediff(curdate(), fecha) < 366\r\n" + 
	    			"group by month(fecha)\r\n" + 
	    			"order by fecha";*/
			
		}else if(reporte.equals("Ventas vs Costos (Sin IVA)")){
			
			return "SELECT 'Costos' as nombreSerie, concat(LPAD(month(str_to_date(a.mnth,'%M')),2,0),'-', '"+año+"') as mes, ifnull(SUM(b.costo*b.unidades),0) as dato\r\n" + 
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
					"			   and b.movimiento = 'ENTRADA' " +
					"GROUP  BY a.mnth, year(b.fecha),month(b.fecha)\r\n" + 
					"ORDER  BY a.rn, b.fecha ASC";
			
		}else if(reporte.equals("Productos m�s vendidos")){
			
			return "SELECT 'Ventas' as nombreSerie, (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as nombre, sum(cantidad) as dato\r\n" + 
					"FROM "+SqlConf.obtenerBase()+"inventario.ventas a force index (custid)\r\n" + 
					"where custid in ("+custid+")\r\n" + 
					"group by id_producto\r\n" + 
					"order by sum(cantidad) desc\r\n" + 
					"limit 10";
			
		}else if(reporte.equals("Mejores clientes")){
			
			return "SELECT 'Cliente' as nombreSerie, id_cliente, (select nombre from "+SqlConf.obtenerBase()+"inventario.clientes where id = a.id_cliente) as nombre, sum(cantidad) as dato\r\n" + 
					"FROM "+SqlConf.obtenerBase()+"inventario.ventas a force index (custid)\r\n" + 
					"where custid in ("+custid+")\r\n" + 
					"and year(fecha) = "+año+"\r\n" + 
					"and movimiento = 'VENTA'\r\n" + 
					"and id_cliente is not null\r\n" + 
					"group by id_cliente\r\n" + 
					"order by sum(cantidad) desc\r\n" + 
					"limit 10";
			
		}else if(reporte.equals("Ganancia por producto")){
			
			return "select 'Ganancia' as nombre, 10 as 'dato'\r\n" + 
					"union\r\n" + 
					"select 'Costo' as nombre, 5 as 'dato'";
			
		}else{
			
			return "Reporte no encontrado";
		
		}
		
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
		combo.setValue(combo.getItemIds().iterator().next());
		
		return combo;
		
	}
	
}

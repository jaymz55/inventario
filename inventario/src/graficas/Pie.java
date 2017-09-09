package graficas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.colorpicker.Color;
import com.vaadin.ui.Alignment;

import conexiones.BeanConexion;
import conexiones.BeanConsulta;
import conexiones.Mysql;
import sql.SqlConf;
import at.downdrown.vaadinaddons.highchartsapi.Colors;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.NoChartTypeException;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Margin;
import at.downdrown.vaadinaddons.highchartsapi.model.ZoomType;
import at.downdrown.vaadinaddons.highchartsapi.model.data.PieChartData;
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.HighChartsPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.BarChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.LineChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.PieChartSeries;

public class Pie {

	public HighChart GraficarProducto(String custid, String tituloGrafica, String tituloProducto, String idProducto, List<Color> colores, int porcentaje) throws SQLException{
		
		//Se debe recibir en los ResultSets el dato y el mes
		
		ChartConfiguration pieConfiguration = new ChartConfiguration();
		
		if(tituloGrafica != null)
			pieConfiguration.setTitle(tituloGrafica);
		
		pieConfiguration.setChartType(ChartType.PIE);
		pieConfiguration.setColors(colores);

		//barConfiguration.setBackgroundColor(Colors.WHITE);
        //lineConfiguration.setBackgroundColor(Colors.WHITESMOKE);
	
	//Esto es para mostrar cuadro al poner el mouse
		pieConfiguration.setTooltipEnabled(true);
        //Margin margin = new Margin(1, 1, 1, 1);
        //lineConfiguration.setChartMargin(margin);
		pieConfiguration.setLegendEnabled(true);
		//barConfiguration.setTitleFontSize(20);

		//lineConfiguration.setTitleFont("Broadway");
        //ZoomType zoom = ZoomType.XY;
        //lineConfiguration.setZoomType(zoom);
       
		double precio = obtenerPrecio(custid, idProducto);
		double costos = obtenerCosto(custid, idProducto);
		double porcentajeCosto = ((costos/precio)*100);
        	
            PieChartSeries pieData = new PieChartSeries(tituloProducto);
            PieChartData ganancia = new PieChartData("Precio", precio);
            PieChartData costo = new PieChartData("Costo - "+Math.round(porcentajeCosto)+"%", costos);
        	
            pieData.getData().add(ganancia);
            pieData.getData().add(costo);
        	
            pieConfiguration.getSeriesList().add(pieData);

            
            precio = 0;
            costos = 0;
            porcentajeCosto = 0;

        try {
        	
            HighChart pieChart = HighChartFactory.renderChart(pieConfiguration);
            pieChart.setHeight(porcentaje, Unit.PERCENTAGE);
            pieChart.setWidth(porcentaje, Unit.PERCENTAGE);
            
            return pieChart;
            
        } catch (NoChartTypeException e) {
            e.printStackTrace();
            return null;
        } catch (HighChartsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public HighChart Graficar12Meses(Vector<ResultSet> datos, String tituloGrafica, String tituloX, String tituloY) throws SQLException{
		
	//Se debe recibir en los ResultSets el dato y el mes
	
		ChartConfiguration lineConfiguration = new ChartConfiguration();
        lineConfiguration.setTitle(tituloGrafica);
        lineConfiguration.setChartType(ChartType.BAR);
        lineConfiguration.setBackgroundColor(Colors.WHITE);
        //lineConfiguration.setBackgroundColor(Colors.WHITESMOKE);
        lineConfiguration.setTooltipEnabled(false);
        //Margin margin = new Margin(1, 1, 1, 1);
        //lineConfiguration.setChartMargin(margin);

		//lineConfiguration.setTitleFont("Broadway");
        //ZoomType zoom = ZoomType.XY;
        //lineConfiguration.setZoomType(zoom);
        
	        //Configuraci�n
	        List<String> meses = new ArrayList<String>();
	       	/*meses.add("Enero");
	       	meses.add("Febrero");
	       	meses.add("Marzo");
	       	meses.add("Abril");
	       	meses.add("Mayo");
	       	meses.add("Junio");
	       	meses.add("Julio");
	       	meses.add("Agosto");
	       	meses.add("Septiembre");
	       	meses.add("Octubre");
	       	meses.add("Noviembre");
	       	meses.add("Diciembre");*/

        for(int a = 0; a < datos.size(); a++){
        	
        	List<Object> datoValues = new ArrayList<Object>();
        	String año = "";
        	
        	while(datos.elementAt(a).next()){
        		año = datos.elementAt(a).getString("nombreSerie");
                datoValues.add(datos.elementAt(a).getString("dato"));
                meses.add(datos.elementAt(a).getString("mes"));
        	}
        	
        	LineChartSeries datoLine = new LineChartSeries(año, datoValues);
        	datoLine = new LineChartSeries(año, datoValues);
        	
            lineConfiguration.getSeriesList().add(datoLine);
        	
        }
        
 	   //Horizontal	
	       Axis horizontal = new Axis();
		   horizontal.setAxisType(AxisType.xAxis);
		   
		   if(tituloX != null)
			   horizontal.setTitle(tituloX);
		   
		   horizontal.setAllowDecimals(false);
	       horizontal.setCategories(meses);
	       lineConfiguration.setxAxis(horizontal);
	       
	   //Vertical
	       Axis vertical = new Axis();
	       vertical.setAxisType(AxisType.yAxis);
	       
	       if(tituloY != null)
	    	   vertical.setTitle(tituloY);
	       
	       lineConfiguration.setyAxis(vertical);

            /*List<Object> sweetValues = new ArrayList<Object>();
            sweetValues.add(33.65);
            sweetValues.add(63.24);
            sweetValues.add(21.52);

            LineChartSeries choclateLine = new LineChartSeries("Choclate", sweetValues);*/

        //Margin chartMargin = new Margin(1, 1, 1, 1);
        //lineConfiguration.setChartMargin(chartMargin);
        

        try {
            HighChart lineChart = HighChartFactory.renderChart(lineConfiguration);
            lineChart.setHeight(80, Unit.PERCENTAGE);
            lineChart.setWidth(80, Unit.PERCENTAGE);
            
            return lineChart;
            
        } catch (NoChartTypeException e) {
            e.printStackTrace();
            return null;
        } catch (HighChartsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
        
        
		
	}

	
	public HighChart Graficar12MesesMini(Vector<ResultSet> datos, String tituloGrafica, String tituloX, String tituloY) throws SQLException{
		
	//Se debe recibir en los ResultSets el dato y el mes
	
		ChartConfiguration barConfiguration = new ChartConfiguration();
		barConfiguration.setTitle(tituloGrafica);
		barConfiguration.setChartType(ChartType.COLUMN);
		barConfiguration.setBackgroundColor(Colors.WHITE);
        //lineConfiguration.setBackgroundColor(Colors.WHITESMOKE);
		barConfiguration.setTooltipEnabled(false);
        //Margin margin = new Margin(1, 1, 1, 1);
        //lineConfiguration.setChartMargin(margin);
		barConfiguration.setLegendEnabled(false);
		barConfiguration.setTitleFontSize(10);

		//lineConfiguration.setTitleFont("Broadway");
        //ZoomType zoom = ZoomType.XY;
        //lineConfiguration.setZoomType(zoom);
        
	        //Configuraci�n
	        List<String> meses = new ArrayList<String>();
	       	/*meses.add("Enero");
	       	meses.add("Febrero");
	       	meses.add("Marzo");
	       	meses.add("Abril");
	       	meses.add("Mayo");
	       	meses.add("Junio");
	       	meses.add("Julio");
	       	meses.add("Agosto");
	       	meses.add("Septiembre");
	       	meses.add("Octubre");
	       	meses.add("Noviembre");
	       	meses.add("Diciembre");*/

        for(int a = 0; a < datos.size(); a++){
        	
        	List<Object> datoValues = new ArrayList<Object>();
        	String año = "";
        	
        	while(datos.elementAt(a).next()){
        		año = datos.elementAt(a).getString("nombreSerie");
                datoValues.add(datos.elementAt(a).getString("dato"));
                meses.add(obtenerMes(datos.elementAt(a).getString("mes")));
        	}
        	
        	
        	ColumnChartSeries datoLine = new ColumnChartSeries(año, datoValues);
        	datoLine = new ColumnChartSeries(año, datoValues);
        	
        	barConfiguration.getSeriesList().add(datoLine);
        	
        }
        
 	   //Horizontal	
	       Axis horizontal = new Axis();
		   horizontal.setAxisType(AxisType.xAxis);
		   
		   if(tituloX != null)
			   horizontal.setTitle(tituloX);
		   
		   horizontal.setAllowDecimals(false);
	       horizontal.setCategories(meses);
	       barConfiguration.setxAxis(horizontal);
	       
	   //Vertical
	       Axis vertical = new Axis();
	       vertical.setAxisType(AxisType.yAxis);
	       
	       if(tituloY != null)
	    	   //vertical.setTitle(tituloY);
	       
	    barConfiguration.setyAxis(vertical);
        

        try {
            HighChart barChart = HighChartFactory.renderChart(barConfiguration);
            barChart.setHeight(80, Unit.PERCENTAGE);
            barChart.setWidth(80, Unit.PERCENTAGE);
            
            
            return barChart;
            
        } catch (NoChartTypeException e) {
            e.printStackTrace();
            return null;
        } catch (HighChartsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
        
        
		
	}
	
	public String obtenerMes(String mes){
		
		String respuesta = "";
		
		switch (mes) {
			case "1":
				respuesta = "Enero";
				break;
			case "2":
				respuesta = "Febrero";
				break;
			case "3":
				respuesta = "Marzo";
				break;
			case "4":
				respuesta = "Abril";
				break;
			case "5":
				respuesta = "Mayo";
				break;
			case "6":
				respuesta = "Junio";
				break;
			case "7":
				respuesta = "Julio";
				break;
			case "8":
				respuesta = "Agosto";
				break;
			case "9":
				respuesta = "Septiembre";
				break;
			case "10":
				respuesta = "Octubre";
				break;
			case "11":
				respuesta = "Noviembre";
				break;
			case "12":
				respuesta = "Diciembre";
				break;
				
		
		}
		
		return respuesta;
		
	}
	
	private double obtenerCosto(String custid, String id){
		
		Mysql sql = new Mysql();
		BeanConexion beanCon;
		ResultSet rs;
		double costo = 0.0;
		
		try{
			
			/*beanCon = sql.conexionSimple("SELECT id_mat FROM tupro_inventario.produccion where id_prod = "+id);
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			while(rs.next()){
			
				bean = sql2.consultaSimple("select ifnull(total, 0.0) as total from tupro_inventario.inventario force index(custid) where custid in ("+custid+") and id_material = "+rs.getString("id_mat")+" and movimiento = 'ENTRADA' and fecha = (select max(fecha) from tupro_inventario.inventario force index (custid) where custid in ("+custid+") and id_material = "+rs.getString("id_mat")+" and movimiento = 'ENTRADA')");
				
	    		if(!bean.getRespuesta().equals("OK")){
	    			throw new Exception(bean.getRespuesta());
	    		}
    		
	    		System.out.println("Dato: "+bean.getDato());
	    		
	    		costo = costo + Double.parseDouble(bean.getDato());
	    		
			}*/
			
			beanCon = sql.conexionSimple("select round(a.total*b.cantidad,2) as total\r\n" + 
					"from "+SqlConf.obtenerBase()+"inventario.inventario a force index (custid), "+SqlConf.obtenerBase()+"inventario.produccion b force index (custid)\r\n" + 
					"where a.custid in ("+custid+")\r\n" + 
					"  and b.custid in ("+custid+")\r\n" + 
					"  and a.id_material = b.id_mat\r\n" + 
					"  and b.id_prod = "+id+"\r\n" + 
					"  and a.id = (select max(id) from "+SqlConf.obtenerBase()+"inventario.inventario force index (custid) where custid in ("+custid+") and id_material = b.id_mat and movimiento = 'ENTRADA')\r\n" + 
					"  and a.id_material = any (SELECT id_mat FROM "+SqlConf.obtenerBase()+"inventario.produccion where custid in ("+custid+") and id_prod = "+id+")");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			while(rs.next()){
				
				costo = costo + rs.getDouble("total");
				
			}
			
    		return Math.round(costo);
			
		}catch(Exception e){
			e.printStackTrace();
			return 0.0;
		}finally{
			sql.cerrar();
				sql = null;
			beanCon = null;
			rs = null;
			costo = 0;
		}
		
	}
	
	private double obtenerPrecio(String custid, String id){
		
		Mysql sql = new Mysql();
		BeanConsulta bean;
		
		try{
			
			bean = sql.consultaSimple("select total from "+SqlConf.obtenerBase()+"inventario.productos where custid in ("+custid+") and id = "+id);
			
    		if(!bean.getRespuesta().equals("OK")){
    			throw new Exception(bean.getRespuesta());
    		}
    		
    		return Double.parseDouble(bean.getDato());
			
		}catch(Exception e){
			e.printStackTrace();
			return 0.0;
		}finally{
			sql.cerrar();
			sql = null;
			bean = null;
		}
		
	}
	
}

package graficas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;

import funciones.Encriptar;
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
import at.downdrown.vaadinaddons.highchartsapi.model.plotoptions.HighChartsPlotOptions;
import at.downdrown.vaadinaddons.highchartsapi.model.series.BarChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnChartSeries;
import at.downdrown.vaadinaddons.highchartsapi.model.series.LineChartSeries;

public class Barras {

	public HighChart Graficar(Vector<ResultSet> queries, String tituloGrafica, String tituloX, String tituloY) throws SQLException{
		
		//Se debe recibir en los ResultSets el dato y el mes
		
		ChartConfiguration barConfiguration = new ChartConfiguration();
		barConfiguration.setTitle(tituloGrafica);
		barConfiguration.setChartType(ChartType.COLUMN);
		//barConfiguration.setBackgroundColor(Colors.WHITE);
        //lineConfiguration.setBackgroundColor(Colors.WHITESMOKE);
	
	//Esto es para mostrar cuadro al poner el mouse
		barConfiguration.setTooltipEnabled(false);
        //Margin margin = new Margin(1, 1, 1, 1);
        //lineConfiguration.setChartMargin(margin);
		barConfiguration.setLegendEnabled(false);
		//barConfiguration.setTitleFontSize(20);

		//lineConfiguration.setTitleFont("Broadway");
        //ZoomType zoom = ZoomType.XY;
        //lineConfiguration.setZoomType(zoom);
        
	    //Configuración
	    List<String> nombres = new ArrayList<String>();

        for(int a = 0; a < queries.size(); a++){
        	
        	List<Object> datoValues = new ArrayList<Object>();
        	String año = "";
        	
        	while(queries.elementAt(a).next()){
        		//año = queries.elementAt(a).getString("nombreSerie");
                datoValues.add(queries.elementAt(a).getString("dato"));
                nombres.add(queries.elementAt(a).getString("nombre"));
        	}
        	
        	
        	ColumnChartSeries datoBar = new ColumnChartSeries(año, datoValues);
        	datoBar = new ColumnChartSeries(año, datoValues);
        	
        	barConfiguration.getSeriesList().add(datoBar);
        	
        }
        
 	   //Horizontal	
	       Axis horizontal = new Axis();
		   horizontal.setAxisType(AxisType.xAxis);
		   
		   if(tituloX != null)
			   horizontal.setTitle(tituloX);
		   
		   horizontal.setAllowDecimals(false);
	       horizontal.setCategories(nombres);
	       barConfiguration.setxAxis(horizontal);
	       
	   //Vertical
	       Axis vertical = new Axis();
	       vertical.setAxisType(AxisType.yAxis);
	       
	       if(tituloY != null)
	    	   vertical.setTitle(tituloY);
	       
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
	
	public HighChart GraficarClientes(Vector<ResultSet> queries, String tituloGrafica, String tituloX, String tituloY) throws Exception{
		
		//Se debe recibir en los ResultSets el dato y el mes
		
		ChartConfiguration barConfiguration = new ChartConfiguration();
		barConfiguration.setTitle(tituloGrafica);
		barConfiguration.setChartType(ChartType.COLUMN);
		//barConfiguration.setBackgroundColor(Colors.WHITE);
        //lineConfiguration.setBackgroundColor(Colors.WHITESMOKE);
	
	//Esto es para mostrar cuadro al poner el mouse
		barConfiguration.setTooltipEnabled(false);
        //Margin margin = new Margin(1, 1, 1, 1);
        //lineConfiguration.setChartMargin(margin);
		barConfiguration.setLegendEnabled(false);
		//barConfiguration.setTitleFontSize(20);

		//lineConfiguration.setTitleFont("Broadway");
        //ZoomType zoom = ZoomType.XY;
        //lineConfiguration.setZoomType(zoom);
        
	    //Configuración
	    List<String> nombres = new ArrayList<String>();

        for(int a = 0; a < queries.size(); a++){
        	
        	List<Object> datoValues = new ArrayList<Object>();
        	String año = "";
        	
        	while(queries.elementAt(a).next()){
        		//año = queries.elementAt(a).getString("nombreSerie");
                datoValues.add(queries.elementAt(a).getString("dato"));
                nombres.add(Encriptar.Desencriptar(queries.elementAt(a).getString("nombre")));
        	}
        	
        	
        	ColumnChartSeries datoBar = new ColumnChartSeries(año, datoValues);
        	datoBar = new ColumnChartSeries(año, datoValues);
        	
        	barConfiguration.getSeriesList().add(datoBar);
        	
        }
        
 	   //Horizontal	
	       Axis horizontal = new Axis();
		   horizontal.setAxisType(AxisType.xAxis);
		   
		   if(tituloX != null)
			   horizontal.setTitle(tituloX);
		   
		   horizontal.setAllowDecimals(false);
	       horizontal.setCategories(nombres);
	       barConfiguration.setxAxis(horizontal);
	       
	   //Vertical
	       Axis vertical = new Axis();
	       vertical.setAxisType(AxisType.yAxis);
	       
	       if(tituloY != null)
	    	   vertical.setTitle(tituloY);
	       
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
        
	        //Configuración
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
        
	        //Configuración
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
	
}

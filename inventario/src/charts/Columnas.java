package charts;

import java.util.ArrayList;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.series.ColumnChartSeries;

public class Columnas extends HighChart{

	//Variables
		private static final long serialVersionUID = 1L;
		ChartConfiguration barConfiguration;
		ColumnChartSeries datoColumnas;
		Axis horizontal;
		Axis vertical;
		ArrayList<String> nombresCategorias;
		ArrayList<ArrayList<Object>> valores;
		
		String tituloGrafica;
		String tituloX;
		String tituloY;
	
	//Const
		public Columnas(String tituloGrafica, String tituloX, String tituloY){
			formatos(tituloGrafica, tituloX, tituloY);
		}
	
	//Metodos
		private void formatos(String tituloGrafica, String tituloX, String tituloY){
			barConfiguration = new ChartConfiguration();
			barConfiguration.setTitle(tituloGrafica);
			barConfiguration.setChartType(ChartType.COLUMN);
			
			//Esto es para mostrar cuadro al poner el mouse
				barConfiguration.setTooltipEnabled(false);
			
				//Para Series visibles
				barConfiguration.setLegendEnabled(true);
				//barConfiguration.setTitleFontSize(20);
				
			//Titulos
				this.tituloGrafica = tituloGrafica;
				this.tituloX = tituloX;
				this.tituloY = tituloY;
				
		}
		
		private void axis(){
				
			//Axis
				horizontal = new Axis();	
				horizontal.setAxisType(AxisType.xAxis);
				if(tituloX != null)
					horizontal.setTitle(tituloX);
				horizontal.setAllowDecimals(false);
			    horizontal.setCategories(nombresCategorias);
			    
			barConfiguration.setxAxis(horizontal);
				
			    vertical = new Axis();
			    vertical.setAxisType(AxisType.yAxis);
			    if(tituloY != null)
			    	vertical.setTitle(tituloY);
			       
			barConfiguration.setyAxis(vertical);
			       
		}
		
		public void cargarDatos(ArrayList<String> nombresCategorias, ArrayList<String> nombresSeries, ArrayList<ArrayList<Object>> valores){
			
			this.nombresCategorias = nombresCategorias;
			this.valores = valores;
			
			for (int i = 0; i < valores.size(); i++) {
				
	        	datoColumnas = new ColumnChartSeries(nombresSeries.get(i), valores.get(i));
	        	barConfiguration.getSeriesList().add(datoColumnas);
				
			}
			
			/*for (List<Object> lista : valores) {
				
				//El primer valor es el anio (o cualquier nombre de serie
	        	datoColumnas = new ColumnChartSeries("", lista);
	        	barConfiguration.getSeriesList().add(datoColumnas);
				
			}*/
			
			//Cargo datos de axis
				axis();

		}
		
		public HighChart crearGrafica(int altoPorcentaje, int anchoPorcentaje){
			
			try {
			
				Columnas.this.setHeight(altoPorcentaje, Unit.PERCENTAGE);
	            Columnas.this.setWidth(anchoPorcentaje, Unit.PERCENTAGE);
				return HighChartFactory.renderChart(barConfiguration);
				
			} catch (HighChartsException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			
		}
		
}

package charts;

import java.util.ArrayList;
import at.downdrown.vaadinaddons.highchartsapi.HighChart;
import at.downdrown.vaadinaddons.highchartsapi.HighChartFactory;
import at.downdrown.vaadinaddons.highchartsapi.exceptions.HighChartsException;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartConfiguration;
import at.downdrown.vaadinaddons.highchartsapi.model.ChartType;
import at.downdrown.vaadinaddons.highchartsapi.model.Axis.AxisType;
import at.downdrown.vaadinaddons.highchartsapi.model.series.AreaChartSeries;

public class Areas extends HighChart{

	//Variables
		private static final long serialVersionUID = 1L;
		ChartConfiguration areaConfiguration;
		AreaChartSeries datoColumnas;
		Axis horizontal;
		Axis vertical;
		ArrayList<String> nombresCategorias;
		ArrayList<ArrayList<Object>> valores;
		
		String tituloGrafica;
		String tituloX;
		String tituloY;
	
	//Const
		public Areas(String tituloGrafica, String tituloX, String tituloY){
			formatos(tituloGrafica, tituloX, tituloY);
		}
	
	//Metodos
		private void formatos(String tituloGrafica, String tituloX, String tituloY){
			areaConfiguration = new ChartConfiguration();
			areaConfiguration.setTitle(tituloGrafica);
			areaConfiguration.setChartType(ChartType.AREA);
			
			//Esto es para mostrar cuadro al poner el mouse
				areaConfiguration.setTooltipEnabled(false);
			
				//Para Series visibles
				areaConfiguration.setLegendEnabled(true);
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
			    
			areaConfiguration.setxAxis(horizontal);
				
			    vertical = new Axis();
			    vertical.setAxisType(AxisType.yAxis);
			    if(tituloY != null)
			    	vertical.setTitle(tituloY);
			       
			areaConfiguration.setyAxis(vertical);
			       
		}
		
		public void cargarDatos(ArrayList<String> nombresCategorias, ArrayList<String> nombresSeries, ArrayList<ArrayList<Object>> valores){
			
			this.nombresCategorias = nombresCategorias;
			this.valores = valores;
			
			for (int i = valores.size()-1; i >= 0; i--) {
				
	        	datoColumnas = new AreaChartSeries(nombresSeries.get(i), valores.get(i));
	        	areaConfiguration.getSeriesList().add(datoColumnas);
				
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
			
				Areas.this.setHeight(altoPorcentaje, Unit.PERCENTAGE);
	            Areas.this.setWidth(anchoPorcentaje, Unit.PERCENTAGE);
				return HighChartFactory.renderChart(areaConfiguration);
				
			} catch (HighChartsException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			
		}
		
}

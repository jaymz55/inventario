package apps;

import java.util.ArrayList;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import at.downdrown.vaadinaddons.highchartsapi.HighChart;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import charts.Areas;
import charts.Columnas;
import charts.Lineas;
import comboBox.ComboMateriales;
import pdf.PdfQRCodigo;

public class AppGraficas extends App{

	//Variables
	
		Button btnGenerar;
		//Button btnReiniciar;
		ComboMateriales comboMateriales;
		PdfQRCodigo pdf;
	
		
	//Const
		public AppGraficas(){
			super();
			elementosFormato();
			elementosAgregar();
			listeners();
		}
	
	//Metodos
	
	public VerticalLayout cuerpo(){
			
			try{
			
				
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
			}

			return respuesta;
		
	}

	
	//Metodos	
		protected void elementosFormato() {
			
			titulo.setValue("Tabla de control");
			
			pdf = new PdfQRCodigo();
			
			btnGenerar = new Button("Generar");
			
			comboMateriales = new ComboMateriales();
				comboMateriales.setWidth("60%");

		}
	
		
		protected void elementosAgregar() {
			
			cabecera.addComponent(titulo);
				cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);

			cuerpo.removeAllComponents();
			cuerpo.addComponent(graficar());
			cuerpo.addComponent(graficar2());
			cuerpo.setComponentAlignment(cuerpo.getComponent(0), Alignment.MIDDLE_CENTER);
				
		}
	
		protected void listeners(){
			
		}
		
		private HighChart graficar() {
				
			    	try {
						
			    		ArrayList<String> nombres = new ArrayList<String>();
			    		
			    		nombres.add("Enero");
			    		nombres.add("Febrero");
			    		nombres.add("Marzo");
			    		nombres.add("Abril");
			    		nombres.add("Mayo");
			    		
			    		ArrayList<Object> parcial = new ArrayList<Object>();
			    		parcial.add("120");
			    		parcial.add("140");
			    		parcial.add("150");
			    		parcial.add("145");
			    		parcial.add("186");
			    		
			    		ArrayList<ArrayList<Object>> listas = new ArrayList<ArrayList<Object>>();
			    		
			    		listas.add(parcial);
			    		
			    		parcial = new ArrayList<Object>();
			    		parcial.add("200");
			    		parcial.add("220");
			    		parcial.add("210");
			    		parcial.add("260");
			    		parcial.add("295");
			    		
			    		listas.add(parcial);
			    		
			    		parcial = new ArrayList<Object>();
			    		parcial.add("300");
			    		parcial.add("310");
			    		parcial.add("330");
			    		parcial.add("340");
			    		parcial.add("380");

			    		
			    		listas.add(parcial);
			    		
			    		ArrayList<String> nombresSeries = new ArrayList<String>();
			    		nombresSeries.add("2015");
			    		nombresSeries.add("2016");
			    		nombresSeries.add("2017");
			    		
			    		
			    		Lineas columnas = new Lineas("Prueba", "horizontal", "vertical");
			    		columnas.cargarDatos(nombres, nombresSeries, listas);
			    		
			    		return columnas.crearGrafica(50, 50);
			    		
					} catch (IllegalArgumentException
							| NullPointerException e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
			
		}
	
		private HighChart graficar2() {
			
	    	try {
	    		
	    		ArrayList<String> nombres = new ArrayList<String>();
	    		
	    		nombres.add("Enero");
	    		nombres.add("Febrero");
	    		nombres.add("Marzo");
	    		nombres.add("Abril");
	    		nombres.add("Mayo");
	    		
	    		ArrayList<Object> parcial = new ArrayList<Object>();
	    		parcial.add("120");
	    		parcial.add("140");
	    		parcial.add("150");
	    		parcial.add("145");
	    		parcial.add("186");
	    		
	    		ArrayList<ArrayList<Object>> listas = new ArrayList<ArrayList<Object>>();
	    		
	    		listas.add(parcial);
	    		
	    		parcial = new ArrayList<Object>();
	    		parcial.add("200");
	    		parcial.add("220");
	    		parcial.add("210");
	    		parcial.add("260");
	    		parcial.add("295");
	    		
	    		listas.add(parcial);
	    		
	    		parcial = new ArrayList<Object>();
	    		parcial.add("300");
	    		parcial.add("310");
	    		parcial.add("330");
	    		parcial.add("340");
	    		parcial.add("380");

	    		
	    		listas.add(parcial);
	    		
	    		ArrayList<String> nombresSeries = new ArrayList<String>();
	    		nombresSeries.add("Jaime Reyes");
	    		nombresSeries.add("Francisco Mora");
	    		nombresSeries.add("Héctor Betancourt");
	    		
	    		
	    		Columnas columnas = new Columnas("Prueba", "horizontal", "vertical");
	    		columnas.cargarDatos(nombres, nombresSeries, listas);
	    		
	    		return columnas.crearGrafica(50, 50);
	    		
			} catch (IllegalArgumentException
					| NullPointerException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
	
		}
		
}

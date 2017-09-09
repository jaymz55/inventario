package tablas;

import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import excel.BotonExcel;
import facade.Facade;
import funciones.Funcion;

public abstract class Tabla extends Table{

	//Variables
		private static final long serialVersionUID = 1L;
		Facade facade = new Facade();
		final protected String alto = "350";
		final protected String ancho = "100%";
		
	//Constructores
		public Tabla(){
			Tabla.this.setSelectable(true);
			Tabla.this.setMultiSelect(true);
		}
		
	//Metodos abstractos
		public abstract void generarTabla();
		public abstract VerticalLayout generarTablaCompleta();
		public abstract void actualizarTabla(Tabla tabla, String custid);
		public abstract void dobleClic(String id);
		
		
	//Metodos
		@SuppressWarnings("serial")
		protected HorizontalLayout generar2Filtros(final Tabla tabla, final String tituloUno, final String tituloDos){
			
			HorizontalLayout respuesta = new HorizontalLayout();
				respuesta.setWidth("30%");
				respuesta.setMargin(true);
			
			final TextField filtro = new TextField();
				filtro.setInputPrompt(Funcion.primeraLetraMayuscula(tituloUno.toLowerCase()));
				filtro.setStyleName("boton_simple");
			final TextField filtro2 = new TextField();
				filtro2.setInputPrompt(Funcion.primeraLetraMayuscula(tituloDos.toLowerCase()));
				filtro2.setStyleName("boton_simple");
			
				filtro.addTextChangeListener(new TextChangeListener(){
				    SimpleStringFilter filter = null;
	
				    public void textChange(TextChangeEvent event) {
				        Filterable f = (Filterable)
				            tabla.getContainerDataSource();
				        
				        // Remove old filter
				        if (filter != null)
				            f.removeContainerFilter(filter);
				        
				        // Set new filter for the "Name" column
				        filter = new SimpleStringFilter(tituloUno, event.getText(),
				                                        true, false);
				        f.addContainerFilter(filter);
				    }
				});
	
				filtro2.addTextChangeListener(new TextChangeListener(){
				    SimpleStringFilter filter = null;
	
				    public void textChange(TextChangeEvent event) {
				        Filterable f = (Filterable)
				            tabla.getContainerDataSource();
				        
				        // Remove old filter
				        if (filter != null)
				            f.removeContainerFilter(filter);
				        
				        // Set new filter for the "Name" column
				        filter = new SimpleStringFilter(tituloDos, event.getText(),
				                                        true, false);
				        f.addContainerFilter(filter);
				    }
				});
				
			respuesta.addComponent(filtro);
				respuesta.setComponentAlignment(filtro, Alignment.MIDDLE_LEFT);
			respuesta.addComponent(filtro2);
				respuesta.setComponentAlignment(filtro2, Alignment.MIDDLE_CENTER);
				
			return respuesta;
				
		}

		@SuppressWarnings("serial")
		protected HorizontalLayout generar3Filtros(final Tabla tabla, final String tituloUno, final String tituloDos, final String tituloTres){
			
			HorizontalLayout respuesta = new HorizontalLayout();
				respuesta.setWidth("50%");
				respuesta.setMargin(true);
			
			final TextField filtro = new TextField();
				filtro.setInputPrompt(Funcion.primeraLetraMayuscula(tituloUno.toLowerCase()));
				filtro.setStyleName("boton_simple");
			final TextField filtro2 = new TextField();
				filtro2.setInputPrompt(Funcion.primeraLetraMayuscula(tituloDos.toLowerCase()));
				filtro2.setStyleName("boton_simple");	
			final TextField filtro3 = new TextField();
				filtro3.setInputPrompt(Funcion.primeraLetraMayuscula(tituloTres.toLowerCase()));
				filtro3.setStyleName("boton_simple");
			
				filtro.addTextChangeListener(new TextChangeListener(){
				    SimpleStringFilter filter = null;
	
				    public void textChange(TextChangeEvent event) {
				        Filterable f = (Filterable)
				            tabla.getContainerDataSource();
				        
				        // Remove old filter
				        if (filter != null)
				            f.removeContainerFilter(filter);
				        
				        // Set new filter for the "Name" column
				        filter = new SimpleStringFilter(tituloUno, event.getText(),
				                                        true, false);
				        f.addContainerFilter(filter);
				    }
				});
	
				filtro2.addTextChangeListener(new TextChangeListener(){
				    SimpleStringFilter filter = null;
	
				    public void textChange(TextChangeEvent event) {
				        Filterable f = (Filterable)
				            tabla.getContainerDataSource();
				        
				        // Remove old filter
				        if (filter != null)
				            f.removeContainerFilter(filter);
				        
				        // Set new filter for the "Name" column
				        filter = new SimpleStringFilter(tituloDos, event.getText(),
				                                        true, false);
				        f.addContainerFilter(filter);
				    }
				});
				
				filtro3.addTextChangeListener(new TextChangeListener(){
				    SimpleStringFilter filter = null;
	
				    public void textChange(TextChangeEvent event) {
				        Filterable f = (Filterable)
				            tabla.getContainerDataSource();
				        
				        // Remove old filter
				        if (filter != null)
				            f.removeContainerFilter(filter);
				        
				        // Set new filter for the "Name" column
				        filter = new SimpleStringFilter(tituloTres, event.getText(),
				                                        true, false);
				        f.addContainerFilter(filter);
				    }
				});
				
			respuesta.addComponent(filtro);
				respuesta.setComponentAlignment(filtro, Alignment.MIDDLE_LEFT);
			respuesta.addComponent(filtro2);
				respuesta.setComponentAlignment(filtro2, Alignment.MIDDLE_CENTER);
			respuesta.addComponent(filtro3);
				respuesta.setComponentAlignment(filtro3, Alignment.MIDDLE_CENTER);
				
			return respuesta;
				
		}

		protected BotonExcel generarExcel(final Tabla tabla, String[] columnasAExportar){
			
			return new BotonExcel(tabla, columnasAExportar);
			
		}
		
}

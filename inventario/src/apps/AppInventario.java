package apps;

import java.util.Date;

import tablas.TablaInventarioBusqueda;
import tablas.TablaInventarioConsolidado;
import ventanas.VentanaMaterialEntrada;
import ventanas.VentanaMerma;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import funciones.Funcion;

public class AppInventario extends App{

	//Variables
		//Layouts
			HorizontalLayout layoutRegistros = new HorizontalLayout();
			HorizontalLayout layoutReportes = new HorizontalLayout();
		//Botones
			Button btnEntrada = new Button("Entrada");
			Button btnMerma = new Button("Merma");
			Button btnBuscar = new Button("Buscar");
			Button btnConsolidado = new Button("Consolidado");
		//Fechas
			DateField fechaUno = new DateField();
			DateField fechaDos = new DateField();
		//Tablas
			TablaInventarioBusqueda tablaBusqueda;
			TablaInventarioConsolidado tablaConsolidado;
			
		//Otros
			
			
	
	//Constructores
		public AppInventario(){
			super();
			elementosFormato();
			elementosAgregar();
			listeners();
		}
	
	//Metodos
	public VerticalLayout cuerpo(){

			try{
			
				
				
				
				/*final Button insertar = new Button("Registrar nuevo proveedor");
				insertar.setStyleName("boton_registrar_nuevo");
				
				insertar.addListener(new Button.ClickListener() {
				    public void buttonClick(ClickEvent event) {
					
				    	try {
							UI.getCurrent().addWindow(new VentanaRegistrarProveedor(false, tabla, false, null));
						} catch (IllegalArgumentException
								| NullPointerException | SQLException e) {
							e.printStackTrace();
						}
				    	
				    }
				});*/
				


			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}

		return respuesta;
		
	}
	
	//Metodos
		protected void elementosFormato(){
			//Titulo
				titulo.setValue("Control de inventario");
			
			//Layouts
				layoutRegistros.setMargin(true);
				layoutRegistros.setWidth("90%");
				layoutRegistros.setHeight("100");
					layoutRegistros.setStyleName(ValoTheme.LAYOUT_WELL);
					layoutRegistros.setCaption("Registros");
				layoutReportes.setMargin(true);
				layoutReportes.setWidth("90%");
				layoutReportes.setHeight("100");
					layoutReportes.setStyleName(ValoTheme.LAYOUT_WELL);
					layoutReportes.setCaption("Reportes");
			//Botones
				btnEntrada.setStyleName("boton_simple");
				btnMerma.setStyleName("boton_simple");
				btnBuscar.setStyleName("boton_simple");
				btnConsolidado.setStyleName("boton_simple");
			//Fechas
				fechaUno.setDateFormat("dd MMMM yyyy");
				fechaUno.setValue(getFirstDateOfCurrentMonth());
				fechaUno.setStyleName("ajustado");			
				fechaDos.setDateFormat("dd MMMM yyyy");
				fechaDos.setValue(new Date());
				fechaDos.setStyleName("ajustado");
		}
		
		protected void elementosAgregar(){
			
			cabecera.addComponent(layoutRegistros);
				cabecera.setComponentAlignment(layoutRegistros, Alignment.MIDDLE_CENTER);
				cabecera.setExpandRatio(layoutRegistros, 2);
			cabecera.addComponent(layoutReportes);
				cabecera.setComponentAlignment(layoutReportes, Alignment.MIDDLE_CENTER);
				cabecera.setExpandRatio(layoutReportes, 3);
			
			layoutRegistros.addComponent(btnEntrada);
				layoutRegistros.setComponentAlignment(btnEntrada, Alignment.MIDDLE_CENTER);
			layoutRegistros.addComponent(btnMerma);
				layoutRegistros.setComponentAlignment(btnMerma, Alignment.MIDDLE_CENTER);
			
			layoutReportes.addComponent(fechaUno);
				layoutReportes.setComponentAlignment(fechaUno, Alignment.MIDDLE_CENTER);
			layoutReportes.addComponent(fechaDos);
				layoutReportes.setComponentAlignment(fechaDos, Alignment.MIDDLE_CENTER);
			layoutReportes.addComponent(btnBuscar);
				layoutReportes.setComponentAlignment(btnBuscar, Alignment.MIDDLE_CENTER);
			layoutReportes.addComponent(btnConsolidado);
				layoutReportes.setComponentAlignment(btnConsolidado, Alignment.MIDDLE_CENTER);
			
		}
	
		protected void listeners(){
			
			//Boton entrada
				btnEntrada.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							UI.getCurrent().addWindow(new VentanaMaterialEntrada(false, cuerpo, false, null));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});

			//Boton merma
				btnMerma.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							UI.getCurrent().addWindow(new VentanaMerma(false, cuerpo, false, null));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
				
			//Boton buscar movimientos
				btnBuscar.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							tablaBusqueda = new TablaInventarioBusqueda(usuario.getCustidsRelacionados(), Funcion.fechaFormato(fechaUno.getValue(), "yyyy-MM-dd"), Funcion.fechaFormato(fechaDos.getValue(), "yyyy-MM-dd"));
							agregarACuerpo(tablaBusqueda.generarTablaCompleta());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
				
			//Boton consolidado
				btnConsolidado.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							tablaConsolidado = new TablaInventarioConsolidado(usuario.getCustid());
							agregarACuerpo(tablaConsolidado.generarTablaCompleta());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
			
		}
}

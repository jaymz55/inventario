package apps;

import java.util.Date;

import tablas.TablaVentas;
import ventanas.VentanaVendedor;
import ventanas.VentanaVenta;

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

public class AppVentas extends App{

	//Variables
		//Layouts
			HorizontalLayout layoutRegistros = new HorizontalLayout();
			HorizontalLayout layoutReportes = new HorizontalLayout();
		//Botones
			Button btnVenta = new Button("Venta");
			Button btnDevolucion = new Button("Devolución");
			Button btnVendedores = new Button("Vendedores");
			Button btnBuscar = new Button("Buscar");
		//Fechas
			DateField fechaUno = new DateField();
			DateField fechaDos = new DateField();
		//Tablas
			TablaVentas tabla;
			
		//Otros
			
			
	
	//Constructores
		public AppVentas(){
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
				titulo.setValue("Control de ventas");
			
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
				btnVenta.setStyleName("boton_simple");
				btnDevolucion.setStyleName("boton_simple");
				btnVendedores.setStyleName("boton_simple");
				btnBuscar.setStyleName("boton_simple");
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
			cabecera.addComponent(layoutReportes);
				cabecera.setComponentAlignment(layoutReportes, Alignment.MIDDLE_CENTER);
			
			layoutRegistros.addComponent(btnVenta);
				layoutRegistros.setComponentAlignment(btnVenta, Alignment.MIDDLE_CENTER);
			layoutRegistros.addComponent(btnDevolucion);
				layoutRegistros.setComponentAlignment(btnDevolucion, Alignment.MIDDLE_CENTER);
			layoutRegistros.addComponent(btnVendedores);
				layoutRegistros.setComponentAlignment(btnVendedores, Alignment.MIDDLE_CENTER);
			
			layoutReportes.addComponent(fechaUno);
				layoutReportes.setComponentAlignment(fechaUno, Alignment.MIDDLE_CENTER);
			layoutReportes.addComponent(fechaDos);
				layoutReportes.setComponentAlignment(fechaDos, Alignment.MIDDLE_CENTER);
			layoutReportes.addComponent(btnBuscar);
				layoutReportes.setComponentAlignment(btnBuscar, Alignment.MIDDLE_CENTER);
			
			
		}
	
		protected void listeners(){
			
			//Boton ventas
				btnVenta.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							UI.getCurrent().addWindow(new VentanaVenta(false, null, false, null));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
			
			//Boton vendedor
				btnVendedores.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							UI.getCurrent().addWindow(new VentanaVendedor(false, null, false, null));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
				
			//Boton buscar ventas
				btnBuscar.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							tabla = new TablaVentas(usuario.getCustid(), Funcion.fechaFormato(fechaUno.getValue(), "yyyy-MM-dd"), Funcion.fechaFormato(fechaDos.getValue(), "yyyy-MM-dd"));
							agregarACuerpo(tabla.generarTablaCompleta());
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
			
		}
}

package apps;

import java.sql.SQLException;

import tablas.TablaProductos;
import ventanas.VentanaProductos;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;

import com.vaadin.ui.Button.ClickEvent;

public class AppProductos extends App{

	//Variables
		//Botones
			Button btnInsertar = new Button("Registrar nuevo producto");
		//tablas
			TablaProductos tabla;
	
			
	//Constructor
		public AppProductos() {
			super();
			elementosFormato();
			elementosAgregar();
			listeners();	
		}
	
		
	//Metodos
		public VerticalLayout cuerpo(){
				
				try{
				
					tabla = new TablaProductos(usuario.getCustid());
	
					//Agrego tabla
						cuerpo.removeAllComponents();
						cuerpo.addComponent(tabla.generarTablaCompleta());
	
				}catch(Exception e){
					NotificacionError.mostrar(e.toString(), 5000);
					e.printStackTrace();
				}
	
			return respuesta;
			
		}
	
	//Metodos abstractos
		@Override
		protected void elementosFormato() {
			
			titulo.setValue("Catálogo de productos");
			btnInsertar.setStyleName("boton_registrar_nuevo");
			
		}
	
		@Override
		protected void elementosAgregar() {
			
			cabecera.addComponent(titulo);
				cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
		
			cabecera.addComponent(btnInsertar);
				cabecera.setComponentAlignment(btnInsertar, Alignment.BOTTOM_CENTER);
			
		}
	
		protected void listeners(){
			
			//Boton insertar
				btnInsertar.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
					
					public void buttonClick(ClickEvent event) {
					
				    	try {
							UI.getCurrent().addWindow(new VentanaProductos(false, tabla, false, null));
						} catch (IllegalArgumentException
								| NullPointerException | SQLException e) {
							e.printStackTrace();
						}
				    	
				    }
				});
			
		}

}

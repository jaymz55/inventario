package apps;

import java.sql.SQLException;

import tablas.TablaMateriales;
import ventanas.VentanaMateriales;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;

import com.vaadin.ui.Button.ClickEvent;

public class AppMateriales extends App{

	//Variables
		//Botones
			Button btnInsertar = new Button("Registrar nuevo material");
		//tablas
			TablaMateriales tabla;
	
			
	//Constructor
		public AppMateriales() {
			super();
			elementosFormato();
			elementosAgregar();
			listeners();	
		}
	
		
	//Metodos
		public VerticalLayout cuerpo(){
				
				try{
				
					tabla = new TablaMateriales(usuario.getCustidsRelacionados());
	
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
			
			titulo.setValue("Catálogo de materiales");
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
							UI.getCurrent().addWindow(new VentanaMateriales(false, tabla, false, null));
						} catch (IllegalArgumentException
								| NullPointerException | SQLException e) {
							e.printStackTrace();
						}
				    	
				    }
				});
			
		}

}

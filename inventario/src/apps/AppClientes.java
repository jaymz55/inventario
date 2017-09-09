package apps;

import java.sql.SQLException;

import tablas.TablaClientes;
import ventanas.VentanaCliente;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class AppClientes extends App{

	//Variables
		//Botones
			Button btnInsertar = new Button("Registrar nuevo cliente");
		//tablas
			TablaClientes tabla;
	
			
	//Constructor
		public AppClientes() {
			super();
			elementosFormato();
			elementosAgregar();
			listeners();	
		}
	
		
	//Metodos
		public VerticalLayout cuerpo(){
				
				try{
				
					tabla = new TablaClientes(usuario.getCustidsRelacionados());
	
					//Agrego tabla
						cuerpo.removeAllComponents();
						cuerpo.addComponent(tabla.generarTablaCompleta());
	
				}catch(Exception e){
					Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				}
	
			return respuesta;
			
		}
	
	//Metodos abstractos
		@Override
		protected void elementosFormato() {
			
			titulo.setValue("Control de clientes");
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
							UI.getCurrent().addWindow(new VentanaCliente(false, tabla, false, null));
						} catch (IllegalArgumentException
								| NullPointerException | SQLException e) {
							e.printStackTrace();
						}
				    	
				    }
				});
			
		}

	
}

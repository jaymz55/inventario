package apps;

import java.sql.SQLException;

import tablas.TablaProveedores;
import ventanas.VentanaProveedor;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;

import com.vaadin.ui.Button.ClickEvent;

public class AppProveedores extends App{

	//Variables
		Button btnInsertar = new Button("Registrar nuevo proveedor");
		TablaProveedores tabla;
	
	//Constructor
		public AppProveedores() {
			super();
			elementosFormato();
			elementosAgregar();
			listeners();	
		}
		
	public VerticalLayout cuerpo(){
			
			try{
			
				tabla = new TablaProveedores(usuario.getCustid());
				
			//Agrego tabla
				cuerpo.removeAllComponents();
				cuerpo.addComponent(tabla.generarTablaCompleta());

			}catch(Exception e){
				NotificacionError.mostrar(e.toString(), 5000);
				e.printStackTrace();
			}

		return respuesta;
		
	}

	@Override
	protected void elementosFormato() {
		
		titulo.setValue("Control de proveedores");
		btnInsertar.setStyleName("boton_registrar_nuevo");
		
	}

	@Override
	protected void elementosAgregar() {
		
		cabecera.addComponent(titulo);
			cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);
	
		cabecera.addComponent(btnInsertar);
			cabecera.setComponentAlignment(btnInsertar, Alignment.BOTTOM_CENTER);
		
	}

	@Override
	protected void listeners() {
		
		btnInsertar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
			
		    	try {
					UI.getCurrent().addWindow(new VentanaProveedor(false, tabla, false, null));
				} catch (IllegalArgumentException
						| NullPointerException | SQLException e) {
					e.printStackTrace();
				}
		    	
		    }
		});
		
	}
	
	//Metodos

	
}

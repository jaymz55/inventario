package notificaciones;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter.OnClose;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

public class Confirmar extends Window{

	//Variables
	private static final long serialVersionUID = 1L;
		HorizontalLayout cuerpo;
		Button aceptar = new Button("Aceptar");
		Button cancelar = new Button("Cancelar");
		boolean decision;
	
	//Constructor
	public Confirmar(){
		llenarInfo();
	}
	//Metodos
		private void llenarInfo(){
			
			Confirmar.this.center();
			Confirmar.this.setHeight("50%");
			Confirmar.this.setWidth("50%");
			
			aceptar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
			        decision = true;
			        Confirmar.this.close();
			    }
			});

			cancelar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
			        decision = false;
			        Confirmar.this.close();
			    }
			});
			
			cuerpo = new HorizontalLayout();
				cuerpo.setHeight("100%");
				cuerpo.setWidth("100%");
			
			cuerpo.addComponent(aceptar);
			cuerpo.addComponent(cancelar);
				
			Confirmar.this.setContent(cuerpo);
			
			
		}
	

		
		
	//Clases internas

	
}

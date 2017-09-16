package apps;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import comboBox.ComboMateriales;
import notificaciones.NotificacionCorrecta;
import pdf.PdfQRCodigo;

public class AppQRCodes extends App{

	//Variables
		Button btnGenerar;
		Button btnReiniciar;
		ComboMateriales comboMateriales;
		PdfQRCodigo pdf;
	
		
	//Const
		public AppQRCodes(){
			super();
			elementosFormato();
			elementosAgregar();
			listeners();
		}
	
	//Metodos
	
	public VerticalLayout cuerpo(){
			
			try{
			
				Notification.show("Conforme generes códigos, se irán acumulando en el archivo PDF", Type.TRAY_NOTIFICATION);

			}catch(Exception e){
				e.printStackTrace();
			}finally{
			}

			return respuesta;
		
	}

	
	//Metodos	
		protected void elementosFormato() {
			
			titulo.setValue("Generador de códigos QR");
			
			pdf = new PdfQRCodigo();
			
			btnGenerar = new Button("Generar");
			btnReiniciar = new Button("Reiniciar PDF");
			
			comboMateriales = new ComboMateriales();
				comboMateriales.setWidth("60%");

		}
	
		
		protected void elementosAgregar() {
			
			cabecera.addComponent(titulo);
				cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);

			cuerpo.addComponent(comboMateriales);
				cuerpo.setComponentAlignment(comboMateriales, Alignment.BOTTOM_LEFT);
				
			cuerpo.addComponent(btnGenerar);
				cuerpo.setComponentAlignment(btnGenerar, Alignment.BOTTOM_LEFT);
			
			cuerpo.addComponent(btnReiniciar);
				cuerpo.setComponentAlignment(btnReiniciar, Alignment.BOTTOM_LEFT);
				
		}
	
		
		protected void listeners() {
			
			btnGenerar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				
				public void buttonClick(ClickEvent event) {
				
			    	try {
						
			    		//Revisar si hay valor
			    		if(comboMateriales.getValue() == null){
			    			Notification.show("Se debe ingresar algún material", Type.WARNING_MESSAGE);
			    		}else{
			    			pdf.generarPdf(comboMateriales.getItemCaption(comboMateriales.getValue()).toString(), comboMateriales.getValue().toString());
			    		}
			    		
					} catch (IllegalArgumentException
							| NullPointerException e) {
						e.printStackTrace();
					}
			    	
			    }
			});
			
			btnReiniciar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;
				
				public void buttonClick(ClickEvent event) {
				
			    	try {
						
			    		pdf.reiniciarPdf();
			    		NotificacionCorrecta.mostrar("PDF reiniciado correctamente");
			    		
					} catch (IllegalArgumentException
							| NullPointerException e) {
						e.printStackTrace();
					}
			    	
			    }
			});
			
		}
	
}

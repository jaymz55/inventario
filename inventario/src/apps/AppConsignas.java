package apps;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import contenedores.ContenedorConsignas;

public class AppConsignas extends App{

	//Variables
		Button btnRegistrar;
		Button btnAgregar;
		Button btnEliminar;
		

		
		
	//Const
		public AppConsignas(){
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
			
			titulo.setValue("Control de consignas");
			
			


		}
	
		
		protected void elementosAgregar() {
			
			/*cabecera.addComponent(titulo);
				cabecera.setComponentAlignment(titulo, Alignment.BOTTOM_LEFT);

			cuerpo.addComponent(comboMateriales);
				cuerpo.setComponentAlignment(comboMateriales, Alignment.BOTTOM_LEFT);
				
			cuerpo.addComponent(btnGenerar);
				cuerpo.setComponentAlignment(btnGenerar, Alignment.BOTTOM_LEFT);
			
			cuerpo.addComponent(btnReiniciar);
				cuerpo.setComponentAlignment(btnReiniciar, Alignment.BOTTOM_LEFT);*/
			
			cuerpo.addComponent(new ContenedorConsignas("Consignas a clientes"));
			
		}
	
		
		protected void listeners() {
			
			/*btnGenerar.addClickListener(new Button.ClickListener() {
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
			});*/
			
		}
	
		// Here is a bean

		
}

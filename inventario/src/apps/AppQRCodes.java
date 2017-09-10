package apps;

import com.vaadin.annotations.Widgetset;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AppQRCodes{

	public static VerticalLayout cuerpo(){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setWidth("100%");
			respuesta.setHeight("100%");
			respuesta.setMargin(true);
		
		final HorizontalLayout cabecera = new HorizontalLayout();
			cabecera.setHeight("70");
			cabecera.setWidth("90%");

		final VerticalLayout cuerpo = new VerticalLayout();
			cuerpo.setMargin(true);
			
		//Variables
			final HorizontalLayout layOut = new HorizontalLayout();
			Label texto = new Label();
			
			try{
			
				texto.setValue("Cajas de kleenex - 435");

				layOut.addComponent(texto);
				
				cuerpo.addComponent(layOut);
				
		    	BrowserWindowOpener opener =
		    	        new BrowserWindowOpener(PrintUI.class);
		    	opener.setFeatures("height=200,width=400,resizable");
		    
		    	Button print = new Button("Click to Print");
		    	opener.extend(print);
				

				
				cuerpo.addComponent(print);

			}catch(Exception e){
				Notification.show("Error en la aplicación: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}finally{
			}

			
		respuesta.addComponent(cabecera);
		respuesta.addComponent(cuerpo);
		return respuesta;
		
	}
	
	//Metodos

	
	//Clases internas
	@Widgetset("com.example.inventario.widgetset.InventarioWidgetset")
	public static class PrintUI extends UI {
	    @Override
	    protected void init(VaadinRequest request) {
	        // Have some content to print
	    	
	    	HorizontalLayout layOut = new HorizontalLayout();
	    	
	    	Label label = new Label("Producto de importación");

			layOut.addComponent(label);
			
	        setContent(layOut);

	        // Print automatically when the window opens
	        JavaScript.getCurrent().execute(
	            "setTimeout(function() {" +
	            "  print(); self.close();}, 0);");
	    }
	}
	
}

package caratulas;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import conexiones.Mysql;
import funciones.Encriptar;

public class ContraseñaNueva {

	
	@SuppressWarnings("deprecation")
	public VerticalLayout personalizar(final VerticalLayout principal, final String correo){
		final VerticalLayout respuesta = new VerticalLayout();
			respuesta.setHeight("100%");
			respuesta.setWidth("100%");
		
			final VerticalLayout cabecera = new VerticalLayout();
				cabecera.setHeight("250");
				cabecera.setWidth("30%");
				
				
			final HorizontalLayout cuerpo = new HorizontalLayout();
				//cuerpo.setMargin(true);
				cuerpo.setHeight("90%");
				cuerpo.setWidth("100%");

			//Variables
				final Label label = new Label("Ingresa tu nueva contrase�a");
					label.setStyleName(ValoTheme.LABEL_H1);
				
				final PasswordField contraseña = new PasswordField("Contrase�a");
					contraseña.setWidth("300");
					
				final PasswordField contraseñaRepetir = new PasswordField("Repetir");
					contraseñaRepetir.setWidth("300");
					
				Button enviar = new Button("Modificar");
					
				enviar.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
				   
				    	if(contraseña.getValue().equals("") || contraseñaRepetir.getValue().equals("")){
				    		Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
				    	}else if(!contraseña.getValue().equals(contraseñaRepetir.getValue())){
				    		Notification.show("Las contrase�as ingresadas no coinciden", Type.WARNING_MESSAGE);
				    	}else if(contraseña.getValue().length() < 8){
				    		Notification.show("La contrase�a debe contener por lo menos 8 caracteres", Type.WARNING_MESSAGE);
				    	}else{
				    		
				    		//Todo correcto
				    		Mysql sql = new Mysql();
				    		try{
				    		
					    		String respuesta = sql.insertarSimple("update tupro_main.usuarios_datos set pass = '"+Encriptar.Encriptar(contraseña.getValue())+"' where correo = '"+Encriptar.Encriptar(correo)+"'");
					    		
					    		if(!respuesta.equals("OK")){
					    			throw new Exception(respuesta);
					    		}else{
					    			
					    			principal.removeAllComponents();
					    			
					    			Principal perso = new Principal();
					    			principal.addComponent(perso.personalizar(correo));
					    			
					    		}
				    			
				    		}catch(Exception e){
				    			e.printStackTrace();
				    		}finally{
				    			sql.cerrar();
				    			
				    		}
				    		
				    	}
				    	
				    }
				});
				
				cabecera.addComponent(label);
				cabecera.addComponent(contraseña);
				cabecera.addComponent(contraseñaRepetir);
				cabecera.addComponent(enviar);
					cabecera.setComponentAlignment(enviar, Alignment.BOTTOM_RIGHT);
				
			
			respuesta.addComponent(cabecera);
			respuesta.addComponent(cuerpo);

			//cuerpo.addComponent(cal.cuerpo());
			
		return respuesta;
		
	}
}

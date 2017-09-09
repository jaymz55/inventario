package caratulas;


import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import conexiones.BeanConsulta;
import conexiones.Mysql;
import funciones.Funcion;

public class Correo_recuperar {

	public VerticalLayout generar(final String custid, final String random){
		
		VerticalLayout respuesta = new VerticalLayout();
			respuesta.setMargin(true);
		
		final Mysql sql = new Mysql();
		
		try{
			
			//Reviso si la info es correcta
			
			if(sql.consultaSimple("SELECT count(custid) FROM tupro_main.usuarios_datos where custid = '"+custid+"'").equals("1")){
				
				if(sql.consultaSimple("SELECT random FROM tupro_main.pass_recover where id = (select max(id) from tupro_main.pass_recover where custid = '"+custid+"')").equals(random)){
					
					//Todo es correcto, mando info para renovar contraseña
					
		 		    BeanConsulta bean = sql.consultaSimple("select nombre from tupro_main.usuarios_datos where custid = '"+custid+"'");
		 		    
		    		if(!bean.getRespuesta().equals("OK")){
		    			throw new Exception(bean.getRespuesta());
		    		}
	    			
		    		String nombre = bean.getDato().toUpperCase();
					
					respuesta.addComponent(new Label("<h2>Hola "+nombre+", por favor ingresa tu nueva contraseña:</h2>", ContentMode.HTML));
					final PasswordField uno = new PasswordField("Introdúcela");
					final PasswordField dos = new PasswordField("Corrobórala");
					
					VerticalLayout camposTexto = new VerticalLayout();
						camposTexto.setHeight("250");
						
						camposTexto.addComponent(uno);
						camposTexto.addComponent(dos);
						
					System.out.println("llega a boton contraseña");
						
					Button modificarPassword = new Button("Modificar");
					modificarPassword.addClickListener(new Button.ClickListener() {
					    public void buttonClick(ClickEvent event) {
					    	
					    	if(uno.getValue().equals("") && dos.getValue().equals("")){
					    		Notification.show("Debes ingresar los datos completos", Type.WARNING_MESSAGE);
					    	}else{
					    		if(!uno.getValue().equals(dos.getValue())){
					    			Notification.show("Las contraseñas ingresadas no coinciden", Type.WARNING_MESSAGE);
					    		}else{
					    			
					    			//Modifico la contraseña
					    			Mysql sqlInterno = new Mysql();
					    			try{
						    			sqlInterno.insertarSimple("update tupro_main.usuarios_datos set pass = '"+uno.getValue()+"' where custid = '"+custid+"'");
						    			Page.getCurrent().setLocation("http://www.tuprograma.mx/tupaciente");
						    			
					    			}catch(Exception e){
					    				e.printStackTrace();
					    			}finally{
					    				sqlInterno.cerrar();
					    			}
					    		}
					    	}
					    	
					    }
					});
					
					camposTexto.addComponent(modificarPassword);
					
					respuesta.addComponent(camposTexto);
					respuesta.addComponent(new Label("<i>*Después de actualizar la contraseña, serás enviado a la página principal.</i>", ContentMode.HTML));
					
				}else{
					respuesta.addComponent(new Label("<h2>Los datos proporcionados no son correctos.</h2>", ContentMode.HTML));
				}
				
			}else{
				respuesta.addComponent(new Label("<h2>Los datos proporcionados no son correctos.</h2>", ContentMode.HTML));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sql.cerrar();
		}
		
		
		return respuesta;
		
	}
	
}

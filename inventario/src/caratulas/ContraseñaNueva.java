package caratulas;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;

import com.example.opciones.Almacen;
import com.example.opciones.Clientes;
import com.example.opciones.EdoDeCuenta;
import com.example.opciones.EntradasSalidasInventario;
import com.example.opciones.Inventario;
import com.example.opciones.Material;
import com.example.opciones.Productos;
import com.example.opciones.Proveedores;
import com.example.opciones.Ventas;
import com.example.inventario.Usuario;
import com.vaadin.event.Action;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarDateRange;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.themes.ValoTheme;

import conexiones.Mysql;
import cookies.Cookies;
import funciones.Encriptar;
import funciones.Funcion;

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
				final Label label = new Label("Ingresa tu nueva contraseña");
					label.setStyleName(ValoTheme.LABEL_H1);
				
				final PasswordField contraseña = new PasswordField("Contraseña");
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
				    		Notification.show("Las contraseñas ingresadas no coinciden", Type.WARNING_MESSAGE);
				    	}else if(contraseña.getValue().length() < 8){
				    		Notification.show("La contraseña debe contener por lo menos 8 caracteres", Type.WARNING_MESSAGE);
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

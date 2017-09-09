package com.example.inventario;

import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import caratulas.ContraseñaNueva;
import caratulas.Correo_recuperar;
import caratulas.Principal;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.Page;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import conexiones.BeanConsulta;
import conexiones.Mysql;
import cookies.Cookies;
import facade.Facade;
import funciones.Encriptar;

@SuppressWarnings("serial")
@Theme("inventario")
public class InventarioUI extends UI {
	
	//@Widgetset("com.example.inventario.widgetset.InventarioWidgetset")
	
	@Override
	protected void init(VaadinRequest request) {

		//Configuracion de pagina
		Page.getCurrent().setTitle("Inventario");
		VaadinService.getCurrent().setSystemMessagesProvider(new SystemMessagesProvider() {
            
            public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
                CustomizedSystemMessages messages = new CustomizedSystemMessages();
                
                messages.setSessionExpiredCaption("Sesión expirada");
                messages.setSessionExpiredMessage("Haz clic aquí para reiniciarla");
                messages.setCommunicationErrorCaption("Servidor fuera de servicio");
                messages.setCommunicationErrorMessage("Problemas de conectividad, revisa tu conexión.");
                messages.setCommunicationErrorNotificationEnabled(true);
                messages.setCommunicationErrorURL("about:blank");
                return messages;
            }
        });
	
		//Variables
			final VerticalLayout principal = new VerticalLayout();
			principal.setMargin(true);
			setContent(principal);
		
			final Facade facade = new Facade();
		
	//Revisar si hay parametro para mandar a página específica
	String parametro = Page.getCurrent().getUriFragment();
	
	if(parametro == null){
			
	//No hay par�metros, empieza la carga normal
		
	//Variables
		final VerticalLayout login = new VerticalLayout();
			login.setHeight("320");
			if(Page.getCurrent().getWebBrowser().isAndroid() || Page.getCurrent().getWebBrowser().isIOS() || Page.getCurrent().getWebBrowser().isWindowsPhone()){
				login.setWidth("80%");
			}else{
				login.setWidth("25%");
			}
			
			final TextField correo = new TextField("Correo");
				correo.setWidth("100%");
			final PasswordField password = new PasswordField("Contraseña");
				password.setWidth("100%");
			final CheckBox sesion = new CheckBox("No cerrar sesión");
			final Button recuperar = new Button("Olvidé mi contraseña");
	
			recuperar.addStyleName(ValoTheme.BUTTON_LINK);
	
			recuperar.addClickListener(new Button.ClickListener() {
			    public void buttonClick(ClickEvent event) {
			    	
			        if(correo.getValue().equals("")){
			        	Notification.show("Ingresa tu correo electrónico en el campo correspondiente", Type.WARNING_MESSAGE);
			        	correo.focus();
			        }else{
			        	String enviado = recuperarContraseña(correo.getValue());
			        	if(enviado.equals("SI"))
			        		Notification.show("Te hemos mandado un correo con las instrucciones", Type.TRAY_NOTIFICATION);
			        	else
			        		Notification.show("El correo ingresado no existe en nuestras bases de datos", Type.WARNING_MESSAGE);
			        }
			    	
			    }
			});
		
			String sesionActiva = "";
	
		//Reviso si hay sesión activa
		//Leo las cookies activas
			Cookie[] nameCookie = VaadinService.getCurrentRequest().getCookies();
			
			if(nameCookie != null){
	    		for(int a = 0; a < nameCookie.length; a++){
	
	    			if(nameCookie[a].getName().equals("usuario")){
	    				sesionActiva = nameCookie[a].getValue();
	    			}
	    		}
			}
		
	if(sesionActiva.equals("")){
		
			Button entrar = new Button("Ingresar");
				entrar.setClickShortcut(KeyCode.ENTER);
			
			login.addComponent(correo);
			login.addComponent(password);
			login.addComponent(sesion);
				login.setComponentAlignment(sesion, Alignment.MIDDLE_RIGHT);
			login.addComponent(entrar);
				login.setComponentAlignment(entrar, Alignment.MIDDLE_RIGHT);
	
	
			entrar.addClickListener(new Button.ClickListener() {
			    public void buttonClick(ClickEvent event) {
			    	
			    try{
			    	
			        if(correo.getValue().equals("") && password.getValue().equals("")){
			        	Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
			        }else{
		
			        	//Revisar si es contrase�a comod�n
			        	
			        	UsuarioDTO usuario = facade.verificarUsuario(correo.getValue(), password.getValue());
			        	
			        	if(usuario.getComodin()){ //Es usuario comodin
			        		
			        		UI.getCurrent().setData(usuario);
			        		principal.removeAllComponents();
			        		ContraseñaNueva nueva = new ContraseñaNueva();
			        		principal.addComponent(nueva.personalizar(principal, correo.getValue()));
			        		
			        	}else if(usuario.getAutenticado()){ //Es usuario autenticado
			        		
			        		//Reviso si se mantiene sesi�n
				        		if(sesion.getValue() == true){
				        			Cookies sesion = new Cookies();
				        			sesion.createCookie("usuario", correo.getValue(), 60 * 60 * 24 * 120);
				        		}
			        			
				        	UI.getCurrent().setData(usuario);	
			        		principal.removeAllComponents();
			        		Principal perso = new Principal();
			        		principal.addComponent(perso.personalizar(correo.getValue()));
			        		
			        	}else{  //No esta autenticado correctamente
			        		
			        		Notification.show("Usuario incorrecto", Type.WARNING_MESSAGE);
			        		
			        	}

			        }
			        
			    }catch(Exception e){
			    	e.printStackTrace();
			    }finally{
			    	
			    	//Verificar si necesito cerrar algo aqui
			    	
			    }
			        
			    }
			});
	
		//Agrego a página principal
			principal.addComponent(login);
	
	}else{ //Hay una sesión activa

		try{
		
			UsuarioDTO usuario = facade.obtenerUsuarioSesion(sesionActiva);
			UI.getCurrent().setData(usuario);
			principal.removeAllComponents();
			Principal perso = new Principal();
			principal.addComponent(perso.personalizar(usuario.getCorreo()));
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			//Revisar si necesito cerrar algo
			
		}
		
	}

	
	}else{//Sí tiene parámetros, procesa la petición
	
		StringTokenizer st = new StringTokenizer(Page.getCurrent().getUriFragment(), "&");
		String parametroMandado = st.nextElement().toString();
		
		if(parametroMandado.equals("recuperar_mail")){
			
			String custid = st.nextElement().toString();
			String random = st.nextElement().toString();
			
			Correo_recuperar recupera = new Correo_recuperar();
			principal.addComponent(recupera.generar(custid, random));
			
		}else if(parametroMandado.equals("impersonalizar")){
			
			String custid = st.nextElement().toString();
			String random = st.nextElement().toString();
			
			Mysql sql = new Mysql();
			
			try{
				
				sql.transaccionAbrir();
				
				BeanConsulta bean = sql.consultaSimple("select count(custid) from "+SqlConf.obtenerBase()+"main.impersonalizar "
						+ "where custid = "+custid+" and llave = '"+random+"' and usado = 'NO'");
				
				if(!bean.getRespuesta().equals("OK")){
					throw new Exception(bean.getRespuesta());
				}
				
				if(bean.getDato().equals("1")){
					
					//Inhabilito el random
					String respuesta = sql.insertarSimple("update tupro_main.impersonalizar set usado = 'SI' where llave = '"+random+"'");
					
					if(!respuesta.equals("OK")){
						throw new Exception(respuesta);
					}
					
					//Obtengo el correo
					bean = sql.consultaSimple("select correo from tupro_main.usuarios_datos where custid = "+custid);
					
					if(!bean.getRespuesta().equals("OK")){
						throw new Exception(bean.getRespuesta());
					}
					
	        		Principal perso = new Principal();
	        		principal.addComponent(perso.personalizar(Encriptar.Desencriptar(bean.getDato())));
					
	        		sql.transaccionCommit();
	        		sql.transaccionCerrar();
	        		
				}else{
					
					Notification.show("La información proporcionada no es válida", Type.WARNING_MESSAGE);
					
				}
				
			}catch(Exception e){
				
				sql.transaccionRollBack();
				sql.transaccionCerrar();
				
				Notification.show(e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
				
			}finally{
				sql.cerrar();
			}
			
		}else if(parametroMandado.equals("demo")){
			
			try {
				UsuarioDTO usuario = facade.verificarUsuario("demo", "demo");
				UI.getCurrent().setData(usuario);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			//Entra con demo
    		principal.removeAllComponents();
    		
    		Principal perso = new Principal();
    		principal.addComponent(perso.personalizar("demo"));
			
		}

	}
	}
	
	private String recuperarContraseña(String correo){
		
		String resultado = "Revisar este punto";
		
		/*Correo mail = new Correo();
		resultado = mail.recovery(correo);*/
		
		return resultado;
		
	}

}
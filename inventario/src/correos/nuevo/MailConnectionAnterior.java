package correos.nuevo;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MailConnectionAnterior{

	//variables
		private static Session sesion = null;
		private static Properties props = new Properties();
		
		private static String smtpHost;
		private static String user;
		private static String password;
		
		
	//Metodos
		
		public static Session getSession(){
		
		try{
			
			if(sesion == null){
				
				System.out.println("Genera una sesion nueva");
				
				Runtime.getRuntime().addShutdownHook(new MiShDwHook());
				
				ResourceBundle rb = ResourceBundle.getBundle("conf/mail");
				smtpHost = rb.getString("smtpHost");
				user = rb.getString("user");
				password = rb.getString("password");
				
			//Conecto
				props = System.getProperties();
				props.put("mail.smtp.host",smtpHost);
				props.put("mail.smtp.port","587");
				props.put("mail.smtp.auth", "true");
				Authenticator auth = new MiAutenticador();
				sesion = Session.getInstance(props,auth);
				
			}
			
			return sesion;
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error al crear la sesion ", e);
		}
	}
	
	static class MiShDwHook extends Thread{
		
		//Justo antes de finalizar el programa, la JVM invocará a este método
		
		public void run(){
			
			try{
				sesion = null;
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	//Clases internas
	private static class MiAutenticador extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
		}
	}
	
}

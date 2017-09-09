package correos.nuevo;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

public class MailConnection{

	//variables
    private static Properties props = System.getProperties();
    private static Session session = Session.getInstance(props);
    private static final int PORT = 587;
    
    private static Transport transport = null;
		
	//Metodos

		public static Session getSession(){
				
			try{
				
				return session;
			
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException("Error al crear la sesion ", e);
			}
		}
    
		public static Transport getTransport(){
		
		try{
			
			if(transport == null || !transport.isConnected()){
				
				System.out.println("Genera una sesion nueva");
				
				Runtime.getRuntime().addShutdownHook(new MiShDwHook());
				
				ResourceBundle rb = ResourceBundle.getBundle("conf/mail");
				String smtpHost = rb.getString("smtpHost");
				String user = rb.getString("user");
				String password = rb.getString("password");
				
			//Conecto
		        props.put("mail.transport.protocol", "smtp");
		        props.put("mail.smtp.port", PORT);
		        props.put("mail.smtp.auth", "true");
		        transport = session.getTransport();
		        transport.connect(smtpHost, user, password);
				
			}
			
			return transport;
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error al crear la sesion ", e);
		}
	}
	
	static class MiShDwHook extends Thread{
		
		//Justo antes de finalizar el programa, la JVM invocará a este método
		
		public void run(){
			
			try{
				transport.close();
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
}

package correos.nuevo;

import java.io.File;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import notificaciones.NotificacionError;

public class Correo extends MimeMessage{

	//Variables
		static Session sesion = MailConnection.getSession();
		static MimeBodyPart messageBodyPart;
		static MimeBodyPart attachPart;
		static Multipart multipart;
		
		
	//Constructor
		public Correo(String titulo, String cuerpo) {
			super(sesion);
		}

	//Metodos
		public static boolean enviar(String titulo, String cuerpo, String destinatario, String cc, String cco) throws MessagingException{
			
			//Nuevo
			MimeMessage msg = new MimeMessage(sesion);

	        try {
			
		        msg.setFrom(new InternetAddress("hector@tuprograma.mx"));
		        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
		        if(cc != null)
		        	msg.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		        if(cco != null)
		        	msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(cco));
		        
		        msg.setSubject(titulo);
		        msg.setContent(cuerpo, "text/html; charset=utf-8");
	
	            MailConnection.getTransport().sendMessage(msg, msg.getAllRecipients());
	            return true;
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	return false;
	            
	        } finally {
	            msg = null;
	        }
			
		}
		
		public static boolean enviarConAdjunto(String titulo, String cuerpo, String destinatario, String cc, String cco, Vector<File> archivos) throws MessagingException{
			
			//Nuevo
			MimeMessage msg = new MimeMessage(sesion);

	        try {
				
		        msg.setFrom(new InternetAddress("hector@tuprograma.mx"));
		        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
		        if(cc != null)
		        	msg.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));
		        if(cco != null)
		        	msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(cco));
		        
		        msg.setSubject(titulo);
	        	
		         //Archivo
		         // creates message part
		         messageBodyPart = new MimeBodyPart();
		         messageBodyPart.setContent(cuerpo, "text/html; charset=UTF-8");

		         // creates multi-part
		         multipart = new MimeMultipart();
		         multipart.addBodyPart(messageBodyPart);

		         // adds attachments
		            
		            for (int a = 0; a < archivos.size(); a++){
		            	attachPart = new MimeBodyPart();
		            	attachPart.attachFile(archivos.elementAt(a));
		            	multipart.addBodyPart(attachPart);
					}
		            
		            //attachPart.attachFile("C:\\Users\\Héctor\\Desktop\\Resume.pdf");
		            

		         msg.setContent(multipart);
		        
	            MailConnection.getTransport().sendMessage(msg, msg.getAllRecipients());
	            return true;
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	NotificacionError.mostrar(e.toString(), 20000);
	        	return false;
	            
	        } finally {
	            msg = null;
	        }

		}
	
}

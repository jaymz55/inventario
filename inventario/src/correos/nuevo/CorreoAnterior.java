package correos.nuevo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import notificaciones.NotificacionError;

public class CorreoAnterior extends MimeMessage{

	//Variables

	
	//Constructores
		public CorreoAnterior(String titulo, String cuerpo, String remitente, String destinatario, String copia) throws MessagingException {
			super(MailConnection.getSession());
			generar(titulo, cuerpo, remitente, destinatario, copia);
			enviar();
		}

		
	//Metodos
		
		//Para mandar a varias direcciones https://stackoverflow.com/questions/12515420/javamail-transport-send-very-slow
		
		private void generar(String titulo, String cuerpo, String remitente, String destinatario, String copia) throws MessagingException{
			
			System.out.println("uno");
			
			try{
				CorreoAnterior.this.setSubject(titulo);
				CorreoAnterior.this.setContent(cuerpo,"text/html; charset=utf-8");
				CorreoAnterior.this.setFrom(new InternetAddress(remitente));
				CorreoAnterior.this.addRecipient(Message.RecipientType.TO,
						new InternetAddress(destinatario));
				if(copia != null)
					CorreoAnterior.this.addRecipient(Message.RecipientType.CC,
							new InternetAddress(copia));
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar("Error al enviar el correo. Contactar al administrador", 10000);
				throw new RuntimeException(e);
			}
			
		}
		
		private void enviar(){
			
			System.out.println("dos");
			
			try{
				
				Transport.send(CorreoAnterior.this);
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar("Error al enviar el correo. Contactar al administrador", 10000);
				throw new RuntimeException(e);
			}
			
		}
		
	
}

package correos;

import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.vaadin.ui.Table;

import conexiones.BeanConsultaMultiple;
import conexiones.Mysql;


public class Correo {

	//Variables
	public String from = "";
	public String to = "";
	public String cc = "";
	public String bcc = "";

	private static String user = "no-responder@tuprograma.mx";
	private static String password = "TuPro#123456";
	
	public Session sesion;
	public Properties props = new Properties();
	public MimeMessage mensaje;
	
	
	//Constructor
	public Correo(){

		try{

				from = "no-responder@tuprograma.mx";

				// Obtener las propiedades del sistema y establecer el servidor
				// SMTP que se va a usar
				String smtpHost = "mail.tuprograma.mx";
				//String smtpHost = "ch.astrahosting.com";
				props = System.getProperties();
				props.put("mail.smtp.host",smtpHost);

				//Cambiar el puerto
				props.put("mail.smtp.port","587");//587 - 465
				//props.put("mail.smtp.starttls.enable", "true");

				// Esta línea indica que vamos a autenticarnos en el servidor SMTP
				props.put("mail.smtp.auth", "true");
				Authenticator auth = new MiAutenticador();

				// Obtener una sesión con las propiedades anteriormente definidas
				sesion = Session.getInstance(props,auth);

			}catch(Exception e) {
				e.printStackTrace();
			}

		}

		public String nuevo(String correo, String copia, String titulo, String texto){
	
			String existe = "NO";
			
			to = correo;
			cc = copia;

			try {
	
					// Crear un mensaje vacío
					mensaje = new MimeMessage(sesion);
	
					// Rellenar los atributos y el contenido
					// Asunto
					//mensaje.setSubject("Nueva solicitud de producción","utf-8");
					mensaje.setSubject(titulo,"utf-8");
					
					// Emisor del mensaje
					mensaje.setFrom(new InternetAddress(from));
	
					// Receptor del mensaje
					mensaje.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));
	
					if(copia != null)
					mensaje.addRecipient(Message.RecipientType.CC,
					new InternetAddress(cc));
					
					// Receptor del mensaje en bcc
					//mensaje.addRecipient(Message.RecipientType.BCC,
					//new InternetAddress(bcc));
	
					 //Certificados - Rechazo
					/*String fichero = "<STYLE TYPE='text/css'><!--.atxt {font-family: arial;	font-size: 12px;	color:#31309c;}a.atxt:link  {text-decoration: underline; color:#0000ff;}a.atxt:hover  {text-decoration: none; color:red;}.txt {font-family: Arial; font-size: 8pt; color: #000000}.txt2 {font-family: Arial; font-size: 9pt; font-weight: bold; color: #000000}.date {font-family: Arial; font-size: 9pt; font-weight: bold; color: #9c0000}.intro {font-family: Arial; font-size: 9pt; color: #000000}.ayuda {font-family: arial;	font-size: 11px;	color:#838283;}a.ayuda:link {font-family: arial;font-size: 11px;	color:#0000FF;}a.ayuda:hover {text-decoration: none;font-size: 11px;	color:#FF0000;}.boton {color:#FFFFFF; font-weight:bold; background-color:#333399;width: 60px; font-family:Arial; font-size:11px;}--></STYLE><table width='570' border='0' cellspacing='0' cellpadding='0' align='center'><tr><td align='center'><style type='text/css'><!--.a,.titblanco,.titazul1,.titnegro,.titnegro1,.titazul,.titbold,.tit {font-family:Arial,Helvetica,sans-serif;font-size:11px;font-style:normal;font-weight:normal;color:#0000ff}.tit {color:#000000}.titbold,.titblanco,.titazul1,.titnegro {font-size:12px;font-weight:bold;color:#32388b}.titazul,.titnegro1 {font-size:14px;font-style:normal;font-weight:bold;color:#32388b}.titnegro1 {font-size:10px;color:#000000}.titnegro,.titblanco {font-weight:normal;color:#000000}.titazul1 {font-weight:bold;color:#32388b}.titblanco {color:#ffffff}--> </style>"+
						"<table width='570' border='0' cellpadding='0' cellspacing='0' style=' text-align:left; font-family:Arial, Helvetica, sans-serif;'><tr><td width='3'></td><td width='175'><a href='http://www.tuprograma.mx'><img src='http://www.tuprograma.mx/imagenes/Tuprograma_chico.gif' width='178' height='48' border='0' / alt='TuPrograma.mx'></a></td><td width='372'></td>  </tr>	<tr><td></td><td></td><td></td></tr><tr><td colspan='3' style='border:1px solid #c1c1c1; border-bottom:0px; padding:20px 20px 10px 20px; font-size:12px; color:#333333; line-height:18px;'><p>Hola: </p><br><p>Se ha cargado una nueva solicitud de producción en TuPrograma<br>"+
						"<br><br>"+
						"<p>Saludos,</p><p>TuPrograma.mx<br><a href='http://www.tuprograma.mx/' target='_blank'>www.tuprograma.mx</a></p></td></tr><tr><td height='9' colspan='3' style='font-size:0;; border:1px solid #c1c1c1; border-top:0;'><!--<img src='http://www.mercadolibre.com/org-img/dmac/templates/generico/dmac_footer.gif' width='570' height='9' />--></td></tr><tr><td colspan='3' style='font-size:11px; color:#666666; text-align:center; padding-top:15px;'>Por favor no respondas este e-mail. Si tienes alguna duda o quieres contactarnos, ingresa al <a href='http://www.tuprograma.mx/ayuda'>Portal de Ayuda.</a></td></tr></table></td></tr></table>";*/
	
	
					mensaje.setContent(texto,"text/html; charset=utf-8");
	
					// Enviar el mensaje
					Transport.send(mensaje);
	
					to = "";
					
					existe = "SI";
	
				
				
	
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					
				}
	
			return existe;
			
		}
	
		public String recovery(String correo){

			String existe = "NO";
			
			to = correo;
			Mysql sql = new Mysql();
			
			try {

				//Reviso si existe el correo
				if(sql.consultaSimple("select count(custid) from usuarios_datos where correo = '"+correo+"'").equals("1")){
				
					//Obtengo datos
					Vector<String> vector = new Vector<String>();
						vector.add("select custid from tupro_main.usuarios_datos where correo = '"+correo+"'");
						vector.add("select nombre from tupro_main.usuarios_datos where correo = '"+correo+"'");
						
					BeanConsultaMultiple bean = sql.consultaMultiple(vector);
					
		    		if(!bean.getRespuesta().equals("OK")){
		    			throw new Exception(bean.getRespuesta());
		    		}
					
					//String custid = sql.consultaSimple("select custid from tupro_main.usuarios_datos where correo = '"+correo+"'");
					//String nombre = sql.consultaSimple("select nombre from tupro_main.usuarios_datos where correo = '"+correo+"'");
		    		Vector<String> respuestas = bean.getDatos();
		    			String custid = respuestas.elementAt(0);
		    			String nombre = respuestas.elementAt(1);
		    		
		    		Random randomGenerator = new Random();
					int random = randomGenerator.nextInt(1000000);
					
					sql.insertarSimple("insert into tupro_main.pass_recover values(null,'"+custid+"','"+random+"',curdate())");
					
					// Crear un mensaje vacío
					mensaje = new MimeMessage(sesion);
	
					// Rellenar los atributos y el contenido
					// Asunto
					mensaje.setSubject("Recupera tu contraseña");
	
					// Emisor del mensaje
					mensaje.setFrom(new InternetAddress(from));
	
					// Receptor del mensaje
					mensaje.addRecipient(Message.RecipientType.TO,
					new InternetAddress(to));
	
					// Receptor del mensaje en bcc
					//mensaje.addRecipient(Message.RecipientType.BCC,
					//new InternetAddress(bcc));
	
					 //Certificados - Rechazo
					String fichero = "<STYLE TYPE='text/css'><!--.atxt {font-family: arial;	font-size: 12px;	color:#31309c;}a.atxt:link  {text-decoration: underline; color:#0000ff;}a.atxt:hover  {text-decoration: none; color:red;}.txt {font-family: Arial; font-size: 8pt; color: #000000}.txt2 {font-family: Arial; font-size: 9pt; font-weight: bold; color: #000000}.date {font-family: Arial; font-size: 9pt; font-weight: bold; color: #9c0000}.intro {font-family: Arial; font-size: 9pt; color: #000000}.ayuda {font-family: arial;	font-size: 11px;	color:#838283;}a.ayuda:link {font-family: arial;font-size: 11px;	color:#0000FF;}a.ayuda:hover {text-decoration: none;font-size: 11px;	color:#FF0000;}.boton {color:#FFFFFF; font-weight:bold; background-color:#333399;width: 60px; font-family:Arial; font-size:11px;}--></STYLE><table width='570' border='0' cellspacing='0' cellpadding='0' align='center'><tr><td align='center'><style type='text/css'><!--.a,.titblanco,.titazul1,.titnegro,.titnegro1,.titazul,.titbold,.tit {font-family:Arial,Helvetica,sans-serif;font-size:11px;font-style:normal;font-weight:normal;color:#0000ff}.tit {color:#000000}.titbold,.titblanco,.titazul1,.titnegro {font-size:12px;font-weight:bold;color:#32388b}.titazul,.titnegro1 {font-size:14px;font-style:normal;font-weight:bold;color:#32388b}.titnegro1 {font-size:10px;color:#000000}.titnegro,.titblanco {font-weight:normal;color:#000000}.titazul1 {font-weight:bold;color:#32388b}.titblanco {color:#ffffff}--> </style>"+
						"<table width='570' border='0' cellpadding='0' cellspacing='0' style=' text-align:left; font-family:Arial, Helvetica, sans-serif;'><tr><td width='3'></td><td width='175'><a href='http://www.tuprograma.mx'><img src='http://www.tuprograma.mx/imagenes/logos/Tuprograma_chico.gif' width='178' height='48' border='0' / alt='TuPrograma.mx'></a></td><td width='372'></td>  </tr>	<tr><td></td><td></td><td></td></tr><tr><td colspan='3' style='border:1px solid #c1c1c1; border-bottom:0px; padding:20px 20px 10px 20px; font-size:12px; color:#333333; line-height:18px;'><p>Hola "+nombre+": </p><br><p><strong>Haz clic en el siguiente link para renovar tu contraseña en TuPrograma</strong><br>"+
						"<br><a href='http://www.tuprograma.mx/#recuperar_mail&"+custid+"&"+random+"'>Clic aquí</a><br><br>"+
						"Si no haz solicitado la renovación de tu contraseña, por favor haz caso omiso de este correo. <br><br>"+
						"<p>Saludos,</p><p>TuPrograma.mx<br><a href='http://www.tuprograma.mx/' target='_blank'>www.tuprograma.mx</a></p></td></tr><tr><td height='9' colspan='3' style='font-size:0;; border:1px solid #c1c1c1; border-top:0;'><!--<img src='http://www.mercadolibre.com/org-img/dmac/templates/generico/dmac_footer.gif' width='570' height='9' />--></td></tr><tr><td colspan='3' style='font-size:11px; color:#666666; text-align:center; padding-top:15px;'>Por favor no respondas este e-mail. Si tienes alguna duda o quieres contactarnos, ingresa al <a href='http://www.tuprograma.mx/ayuda'>Portal de Ayuda.</a></td></tr></table></td></tr></table>";
	
	
					mensaje.setContent(fichero,"text/html; charset=utf-8");
	
					// Enviar el mensaje
					Transport.send(mensaje);
	
					to = "";
					
					existe = "SI";

				}
				

				}catch(Exception e){
					e.printStackTrace();
				}finally{
					sql.cerrar();
				}

			return existe;
			
		}

		private static class MiAutenticador extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
		}
		}
	
}

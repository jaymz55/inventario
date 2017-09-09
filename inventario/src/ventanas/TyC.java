package ventanas;

import caratulas.Principal;

import com.example.inventario.Usuario;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

import conexiones.Mysql;
import funciones.Encriptar;

public class TyC extends Window{
	
	//Variables
	private boolean acepta;
	private String correo;
	
	
	//Getters y Setters
		public boolean isAcepta() {
			return acepta;
		}
	
		public void setAcepta(boolean acepta) {
			this.acepta = acepta;
		}

	
	//Constructores
		public TyC(){
			acepta = false;
		}
		
		public TyC(String correo, boolean secundaria){
			this.correo = correo;
		}
	
	//M�todos
	@SuppressWarnings("deprecation")
	public boolean cargarVentana(final String correo, final VerticalLayout pantalla){
		
		Mysql sql = new Mysql();
		
		try{
		
			final Window ventana = new Window("T�rminos y Condiciones");
				ventana.setHeight("80%");
				ventana.setWidth("90%");
				ventana.center();
				
			VerticalLayout principal = new VerticalLayout();
				principal.setHeight("100%");
				principal.setWidth("100%");
				principal.setMargin(true);
				
			HorizontalLayout layoutTexto = new HorizontalLayout();
				layoutTexto.setMargin(true);
				layoutTexto.setHeight("90%");
				layoutTexto.setWidth("90%");
				layoutTexto.setStyleName(ValoTheme.LAYOUT_WELL);
				
			HorizontalLayout layoutAcepta = new HorizontalLayout();
				layoutAcepta.setMargin(true);
				layoutAcepta.setHeight("90%");
				layoutAcepta.setWidth("20%");
				//layoutAcepta.setStyleName(ValoTheme.LAYOUT_WELL);
				
			//Variables
				String texto = "CONTRATO DE PRESTACION DE SERVICIOS QUE CELEBRAN POR UNA PARTE �USTED� QUE EN LO SUCESIVO SE LE DENOMINARA �EL USUARIO� Y POR LA OTRA PARTE �TUPROGRAMA.MX� QUE EN LOS SUCESIVO SE LE DENOMINARA �LA EMPRESA� LAS CUALES SE SUJETAN AL TENOR DE LAS SIGUIENTES DECLARACIONES Y CLAUSULAS.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"DECLARACIONES\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Declara �LA EMPRESA�\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"a) Que es una persona moral debidamente constituida conforme a la legislaci�n mexicana.\r\n" + 
						"b) Que tiene la capacidad legal, tecnol�gica y humana para celebrar el presente contrato, facultades que no han sido restringidas o modificadas en forma alguna.\r\n" + 
						"c) Se encuentra inscrita en el Registro Federal de Contribuyentes bajo el n�mero XAXX010101000.\r\n" + 
						"\r\n" + 
						"Declara �EL USUARIO�\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"a) Que los datos proporcionados en la orden de compra son fidedignos, por lo que manifiesta que la informaci�n que proporciona es real y comprobable, relevando a �LA EMPRESA� de cualquier responsabilidad por la falsedad con la que se conduzca, por la falsificaci�n de documentos o suplantaci�n de personas, nombres, direcciones o datos de identificaci�n que utilice para contratar los servicios que presta �LA EMPRESA�.\r\n" + 
						"b) Que se encuentra en pleno uso de sus facultades f�sicas, mentales y legales para poder celebrar este contrato.\r\n" + 
						"c) En el caso de ser usuario individual, �EL USUARIO� garantiza que al menos tiene 18 a�os de edad y en el caso de ser usuario de una compa��a, �EL USUARIO� garantiza que el servicio no ser� usado por una persona menor a los 18 a�os de edad.\r\n" + 
						"d) Que es el �nico y exclusivo responsable por los datos, marcas, logos, patentes o s�mbolos y colores de identificaci�n que proporciona.\r\n" + 
						"\r\n" + 
						"En virtud de las declaraciones anteriores, las Partes convienen sujetarse a lo dispuesto en las siguientes cl�usulas.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"CLAUSULAS\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"PRIMERA. Objeto\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Por el presente contrato �EL USUARIO� contrata el servicio de acceso a la aplicaci�n de punto de venta el cual se encuentra en un servidor compartido provisto por �LA EMPRESA� para su uso, as� como tambi�n la prestaci�n del servicio de timbrado, asignaci�n de folio e incorporaci�n del sello digital a los Comprobantes Fiscales Digitales (CFDi).\r\n" + 
						"\r\n" + 
						"\r\n" + 
						//"�EL USUARIO� deber� de realizar las configuraciones necesarias para la correcta utilizaci�n del servicio de timbrado, asignaci�n de folio e incorporaci�n del sello digital a los Comprobantes Fiscales Digitales (CFDi).\r\n" + 
						//"\r\n" + 
						//"\r\n" + 
						"SEGUNDA. Restricciones del contrato\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� no podr� consumir de manera excesiva los recursos del sistema, incluyendo pero no limitado, a ciclos del procesador, memoria y transferencia, en caso de violentar este punto �LA EMPRESA� puede restringir , limitar o suspender el servicio contratado por el uso excesivo de recursos sin ninguna responsabilidad para esta �ltima.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� deber� mantener de forma segura cualquier identificaci�n, contrase�a y otro tipo de informaci�n confidencial relacionado con su cuenta y deber� notificar inmediatamente a �LA EMPRESA� del conocimiento o sospecha del uso no autorizado de su cuenta o brecha de seguridad, incluyendo perdida, robo o uso no autorizado de su contrase�a o cualquier otro tipo de informaci�n de seguridad.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� no podr� realizar alguna acci�n que pudiera da�ar nuestra reputaci�n de negocios, o la de alguna compa��a o negocio relacionado con �LA EMPRESA�, ello incluye comentarios denigrantes o de desprestigio que se efect�en en p�ginas, blogs, posts o cualquier otro medio en los que �EL USUARIO� pueda verter estos comentarios en contra de �LA EMPRESA�, sus directivos, empleados o personal, por lo que nos reservaremos el derecho de remover la cuenta de �EL USUARIO� as� como si realizar comentarios que denoten los servicios, productos, red, empresas afiliadas, socios estrat�gicos, empleados, directores o personal de �LA EMPRESA�.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� que realice comentarios de desprestigio o que inicien campa�as denigrantes de los servicios o productos que ofrece �LA EMPRESA�, ser�n responsables de las determinaciones que emitan las autoridades competentes en materia civil y/o penal as� como aceptar el pago de da�os y perjuicios que llegare ocasionar a �LA EMPRESA�.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"TERCERA. Disponibilidad del servicio\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�LA EMPRESA� har� o aplicar� el mayor esfuerzo razonable para hacer que el servidor y el servicio est� disponible para �EL USUARIO� todo el tiempo, sin embargo ante cualquier eventualidad, �LA EMPRESA� no ser� responsable por interrupciones del servicio o por el tiempo que el servidor este fuera de l�nea.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�LA EMPRESA� tiene el derecho de suspender el servicio en cualquier momento de serlo necesario para garantizar la preservaci�n del servidor o por cualquier raz�n que as� lo creyere conveniente tratando siempre de dar aviso a �EL USUARIO�, de no ser posible el �EL USUARIO� se retracta de cualquier acci�n legal en contra de �LA EMPRESA�, exoner�ndola de cualquier responsabilidad por ello.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� libera a �LA EMPRESA� de cualquier responsabilidad por la p�rdida de informaci�n o datos almacenados en el servidor de �LA EMPRESA�, sin embargo �LA EMPRESA� pondr� especial cuidado en la conservaci�n e integridad de la informaci�n de �EL USUARIO�, y si la informaci�n se llegare a perder, ello no ser� responsabilidad de �LA EMPRESA�. Es responsabilidad de �EL USUARIO� mantener sus propios respaldos de su informaci�n.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"CUARTA. Pagos\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Todo cargo pagadero que �EL USUARIO� deba realizar por los servicios ser� de acuerdo con la escala de cargos y las tarifas publicadas por nosotros en nuestro sitio web y ser� debido y pagadero por adelantado a la disposici�n de nuestros servicios. Nos reservamos el derecho de cambiar los precios en cualquier momento y sin previo aviso, sin embargo todos los precios quedan garantizados a �EL USUARIO� por el periodo de tiempo que haya realizado su prepago.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El pago de los servicios se realiza por el tiempo acordado en la orden de compra.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Todos los pagos deber�n ser en pesos mexicanos o su equivalente en moneda extranjera.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si el pago de cualquier suma que �EL USUARIO� deba cubrir no se realiza antes de la fecha l�mite, nosotros tendremos el total derecho de suspender los servicios contratados.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"QUINTA. Terminaci�n y autorenovaci�n del contrato\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si �EL USUARIO� incumple con el pago de cualquier renovaci�n que tenga con �LA EMPRESA�, esta �ltima podr� suspender el servicio y/o terminar este acuerdo de voluntades de manera inmediata sin previo aviso a �EL USUARIO�, ello sin ninguna responsabilidad para �LA EMPRESA�.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si �EL USUARIO� falla en el cumplimiento de cualquiera de los t�rminos, condiciones o restricciones de este acuerdo, no importando el orden o exclusi�n, �LA EMPRESA� podr� suspender el servicio y/o terminar este acuerdo de manera inmediata sin que medie aviso previo.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Cuando �LA EMPRESA� reciba una solicitud de terminaci�n o rescisi�n de la cuenta contratada o de algunos de los servicios, a la misma se le dar� tramite y ser� aceptada siempre y cuando �LA EMPRESA� cuente con pruebas bastantes y suficientes que se trata de una solicitud realizada por �EL USUARIO�, quien ser� el �nico autorizado para realizar la cancelaci�n o la rescisi�n de la cuenta. Tal informaci�n requerida es de manera enunciativa mas no limitativa: correo electr�nico, usuario, contrase�a y toda aquella informaci�n que �LA EMPRESA� considere pertinente para verificar su identidad.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� que desee cancelar alg�n servicio deber� enviar su solicitud por medio de correo electr�nico a �LA EMPRESA�, proporcionando datos de identificaci�n, n�mero de cuenta, identificaci�n de usuario, el servicio que pretenda cancelar y una exposici�n de motivos por los cuales pretenda cancelar el servicio.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"SEXTA. Ley aplicable y jurisdicci�n\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de disputa entre �EL USUARIO� y �LA EMPRESA�, las partes est�n conscientes y de acuerdo en someterse a los Tribunales y las leyes sustantivas y adjetivas del Estado de Nuevo Le�n, M�xico, para dirimir sus diferencias. Por lo que renuncian a cualquier fuero y legislaci�n que les pudiera ser aplicable por raz�n de su domicilio presente o futuro o bien por cualquier otra causa.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Los t�tulos o definiciones que se incluyen en este acuerdo son para conveniencia solamente y no afectar�n la construcci�n o la interpretaci�n de este acuerdo.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El usuario al momento de registrarse como nuevo usuario en el m�dulo \"Contratar\" y levantar su orden compra, est� de acuerdo a que estos actos se consideren como su consentimiento expreso, aceptando y conociendo los significados y alcances de los t�rminos y condiciones aqu� plasmados, as� como la pol�tica por spam, validando su consentimiento con la firma electr�nica.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de incurrir en abusos y/o actividades ilegales que requieran procedimientos legales, estos deber�n ser realizados en primera instancia, sometiendo expresamente jurisdicci�n a los tribunales y autoridades competentes en el Estado de Nuevo Le�n, M�xico.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"�EL USUARIO� ser� el �nico responsable de las actividades que desarrolle al amparo de nuestros servicios o productos, relevando a �LA EMPRESA� de cualquier responsabilidad por ello.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de que �LA EMPRESA� se vea involucrada en procedimientos judiciales o administrativos por las conductas o actividades que despliegue �EL USUARIO�, este �ltimo se har� responsable de las costas y gastos que ello origine.";
				
				
				String AvisoPrivacidad = "Responsable de la protecci�n de tus datos personales\r\n" + 
						"\r\n" + 
						"PROVEEDOR DE SERVICIOS TI, S.A. DE C.V., y/o cualquiera de sus subsidiarias y/o afiliadas (Softseti), con domicilio en Fil�sofos 221 203, Colonia Tecnol�gico, Monterrey, Nuevo Le�n, C.P. 64700, M�xico, y portal de internet http://softseti.net, en cumplimiento a lo establecido por la Ley Federal de Protecci�n de Datos personales en Posesi�n de los Particulares (\"Ley\") y con la finalidad de garantizar la privacidad y el derecho a la autodeterminaci�n informativa de sus clientes y usuarios, hace de su conocimiento la pol�tica de privacidad y manejo de datos personales.\r\n" + 
						"\r\n" + 
						"El uso o navegaci�n por parte de cualquier persona del sitio antes mencionado le concede la calidad de Usuario y/o Cliente.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Recolecci�n de datos.\r\n" + 
						"\r\n" + 
						"Personales. En la prestaci�n de servicios, Softseti requerir� a sus clientes s�lo de datos personales, ya sea de identificaci�n y/o financieros y/o profesionales proporcionados por usted de manera directa o por cualquier medio de contacto y/o foro p�blico de conexi�n en l�nea relacionados con los servicios que presta Softseti, destacando los siguientes:\r\n" + 
						"\r\n" + 
						"Nombre\r\n" + 
						"Raz�n social\r\n" + 
						"Registro Federal de Contribuyentes(RFC)\r\n" + 
						"Domicilio\r\n" + 
						"Telefono particular\r\n" + 
						"Correo electr�nico\r\n" + 
						"Datos de identificaci�n\r\n" + 
						"Con los fines se�alados en este aviso de privacidad, podemos recabar sus datos personales de distintas formas: durante el proceso de registro, inscripci�n a alguna promoci�n o programa o cuando nos lo proporcione directamente.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Es decir toda informaci�n personal que el usuario haya proporcionado a Softseti de manera directa por medio de este sitio, aquella informaci�n personal proporcionada por el usuario al momento de utilizar nuestros servicios en l�nea o cuando Softseti obtiene la informaci�n personal del usuario l�citamente por cualquier otro medio o fuentes permitidas por la ley, ser�n utilizados con las siguientes finalidades: Proveer servicios y productos requeridos en el sitio, proveer y garantizar las operaciones de generaci�n y certificaci�n de Comprobantes Fiscales Digitales por Internet (CFDI), informar sobre nuevos productos o servicios que est�n relacionados con el contratado o adquirido por el usuario, dar cumplimiento con obligaciones contra�das con el usuario, realizar estudios internos sobre h�bitos de consumo, promover los productos y servicios que Softseti ofrece, informarle sobre cambios en los mismos, para fines publicitarios, promocionales, telemarketing, operaciones, administraci�n del sitio web de Softseti, administraci�n de los servicios de Softseti, desarrollo de nuevos productos y servicios, encuestas de calidad del servicio o de producto contratados por el usuario y satisfacci�n del usuario, an�lisis de uso de productos, servicios y sitio web, para el env�o de avisos acerca de productos y servicios operados por Softseti y/o por sus afiliadas, subsidiarias y/o por sus socios de negocio; cuando la ley o alguna autoridad lo requiera, para solicitarle actualizaci�n de sus datos y documentos de identificaci�n, y en general para hacer cumplir nuestros t�rminos, condiciones y la operaci�n, funcionamiento y administraci�n de nuestros negocios.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El concepto de \"datos personales\" en este aviso de privacidad se refiere a toda aquella informaci�n de car�cter personal que pueda ser usada para identificaci�n del Usuario.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Softseti no recaba ni trata datos personales sensibles.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Uso de cookies y scripts\r\n" + 
						"\r\n" + 
						"Los datos personales que recabamos cuando visitas nuestro sitio de Internet o utilizas nuestros servicios en l�nea son la Direcci�n IP y Cookies.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Softseti podr� recabar datos personales por el uso de cookies. La p�gina de nuestro sitio Web utiliza cookies, los cuales son peque�os ficheros de datos que se descargan autom�ticamente y almacenados en la computadora del Usuario, las cuales graban sus datos personales cuando se conecta al sitio, entre ellos sus preferencias para la visualizaci�n de las p�ginas de este servidor, modific�ndose al abandonar el sitio y las cuales permiten obtener la informaci�n siguiente:\r\n" + 
						"\r\n" + 
						"P�ginas de internet que visita el usuario.\r\n" + 
						"La fecha y hora de la �ltima vez que el usuario visit� nuestro sitio Web.\r\n" + 
						"El dise�o de contenidos o preferencias que el usuario escogi� en su primera visita a nuestro sitio web.\r\n" + 
						"Elementos de seguridad que intervienen en el control de acceso a las �reas restringidas.\r\n" + 
						"Reconocer a los usuarios.\r\n" + 
						"Detectar su ancho de banda.\r\n" + 
						"Medir par�metros de tr�fico.\r\n" + 
						"Su tipo de navegador y sistema operativo.\r\n" + 
						"Su direcci�n IP.\r\n" + 
						"La resoluci�n de su monitor.\r\n" + 
						"Los cookies son an�nimos. El acceso a la informaci�n por medio de los cookies, permite ofrecer al Usuario un servicio personalizado, ya que almacenan no s�lo sus datos personales sino tambi�n la frecuencia de utilizaci�n del servicio y las secciones de la red visitadas, reflejando as� sus h�bitos y preferencias.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El Usuario tiene la opci�n de impedir la generaci�n de cookies, mediante la selecci�n de la correspondiente opci�n en la configuraci�n de su navegador de Internet. Sin embargo Softseti no se responsabiliza de que la desactivaci�n de los mismos, ya que impedir�an el buen funcionamiento del sitio de Internet.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Los cookies son archivos de texto descargados autom�ticamente y almacenados en el disco duro del equipo de c�mputo del usuario al navegar en una p�gina de Internet espec�fica, que permiten recordar al servidor de Internet algunos datos sobre este usuario, entre ellos, sus preferencias para la visualizaci�n de las p�ginas en ese servidor, nombre y contrase�a.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"As� mismo las scripts son piezas de c�digo que nos permiten obtener informaci�n como la direcci�n IP del usuario, duraci�n del tiempo de interacci�n en una p�gina y el tipo de navegador utilizado, entre otros.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Estas cookies y otras tecnolog�as pueden ser deshabilitadas. Para conocer c�mo hacerlo, consulte la documentaci�n de su navegador.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Finalidad del tratamiento de datos.\r\n" + 
						"\r\n" + 
						"Los datos personales que el cliente nos proporcione con fines comerciales para contactarlo y/o enviarle informaci�n respecto de los servicios contratados, as� como con fines estad�sticos sujetos a un proceso de disociaci�n.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Los datos personales tambi�n son utilizados para realizar estudios internos sobre los datos demogr�ficos, intereses y comportamiento de los usuarios; con la finalidad de proporcionarles productos, servicios, contenidos y publicidad acordes a sus necesidades, as� como proporcionar notificaciones e informaci�n de manera confidencial sobre su servicio y contactar a los usuarios cuando sea necesario, por tel�fono o correo electr�nico en caso de que se requieran datos adicionales para completar alguna transacci�n.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Transferencia de datos.\r\n" + 
						"\r\n" + 
						"Softseti podr� tratar los datos personales o los podr� poner a disposici�n de terceros dentro o fuera del pa�s, en este sentido, su informaci�n puede ser compartida a otros tercero a consecuencia de una relaci�n contractual o bien en aquellos casos en que la divulgaci�n sea necesaria para la eficaz operaci�n de los servicios proporcionados.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si usted no manifiesta oposici�n alguna por escrito para que sus datos sean tratados y/o transferidos a terceros, se entender� que ha otorgado su consentimiento para ello una vez que haya otorgado la aceptaci�n electr�nica y/o al ingresar sus datos dentro del contenido y funciones del sitio.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Softseti podr� transmitir lo datos que recabe de sus clientes a cualquiera de las subsidiarias y/o afiliadas y/o socios de negocios y/o terceros, quienes quedar�n obligadas a resguardar y utilizar la informaci�n en t�rminos de este Aviso de Privacidad y por cualquiera de las razones previstas en la �Ley� Softseti se reserva el derecho de transmitir los datos de sus clientes.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de vender o traspasar la totalidad o parte de su negocio y/o activos, Softseti le comunicar� la obligaci�n al siguiente propietario para que utilice los datos de los clientes con apego a est� Aviso de Privacidad, as� mismo le informar� al titular que pretende transferir los datos a terceros. Si el titular no desea que sus datos sean transferidos al tercero deber� manifestarlo al nuevo propietario.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Medios para ejercer los derechos de acceso, rectificaci�n, cancelaci�n u oposici�n.\r\n" + 
						"\r\n" + 
						"Usted tiene derecho a conocer qu� datos personales que tenemos suyos, para qu� los utilizamos y las condiciones del uso que les damos (Acceso). Asimismo, es su derecho solicitar la correcci�n de su informaci�n personal en caso de que est� desactualizada, sea inexacta o incompleta (Rectificaci�n); que la eliminemos de nuestros registros o bases de datos cuando considere que la misma no est� siendo utilizada adecuadamente (Cancelaci�n); as� como oponerse al uso de sus datos personales para fines espec�ficos (Oposici�n). Estos derechos se conocen como derechos ARCO.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Todo cliente de Softseti interesado en ejecutar los derechos ARCO previstos en la �Ley�, podr� hacerlo poni�ndose en contacto con El�as C�rdova y/o Eduardo Garza y/o Enrique Gorian, responsables de nuestro Departamento de Protecci�n de Datos Personales, en el domicilio ubicado en Fil�sofos 221 203, Col. Tecnol�gico, Monterrey, Nuevo Le�n, CP 64700, M�xico, o bien, se comunique al tel�fono 01 81 1935 9838 o v�a correo electr�nico soporte@softseti.net, el cual solicitamos confirme v�a telef�nica para garantizar su correcta recepci�n.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Con relaci�n al procedimiento y requisitos para el ejercicio de sus derechos ARCO, le informamos lo siguiente:\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"a) �A trav�s de qu� medios pueden acreditar su identidad el titular y, en su caso, su representante, as� como la personalidad este �ltimo? Con el original y copia de identificaci�n oficial vigente; en el caso de personas morales, se debe agregar original y copia del instrumento notarial en el que consten sus facultades.\r\n" + 
						"b) �Qu� informaci�n y/o documentaci�n deber� contener la solicitud? Los pormenores respecto de los derechos ARCO, y la precisi�n o intenci�n que busca el titular, narrando con amplitud los hechos en que basa su pedido.\r\n" + 
						"c) �En cu�ntos d�as le daremos respuesta a su solicitud? En diez d�as h�biles.\r\n" + 
						"d) �Por qu� medio le comunicaremos la respuesta a su solicitud? Por correo electr�nico.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Usted puede revocar el consentimiento que, en su caso, nos haya otorgado para el tratamiento de sus datos personales. Sin embargo, es importante que tenga en cuenta que no en todos los casos podremos atender su solicitud o concluir el uso de forma inmediata, ya que es posible que por alguna obligaci�n legal requiramos seguir tratando sus datos personales. Asimismo, usted deber� considerar que para ciertos fines, la revocaci�n de su consentimiento implicar� que no le podamos seguir prestando el servicio que nos solicit�, o la conclusi�n de su relaci�n con nosotros.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Cambios al Aviso de Privacidad\r\n" + 
						"\r\n" + 
						"El presente aviso de privacidad puede sufrir modificaciones, cambios o actualizaciones derivadas de nuevos requerimientos legales; de nuestras propias necesidades por los productos o servicios que ofrecemos; de nuestras pr�cticas de privacidad; de cambios en nuestro modelo de negocio, o por otras causas.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Cualquier cambio a este Aviso de Privacidad se har� del conocimiento de los clientes de Softseti que marquen al 01 81 1935 9838 o manden correo electr�nico a la siguiente direcci�n: soporte@softseti.net am�n de publicarlo en nuestro portal de internet.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Este aviso de privacidad, describe la Pol�tica de Privacidad del Softseti, el cual constituye un acuerdo v�lido entre el Usuario y Softseti; si el Usuario utiliza los servicios de Softseti significa que ha le�do, entendido, aceptado y consecuentemente acordado con el Softseti los t�rminos del aviso de privacidad antes expuestos. En caso de no estar de acuerdo con ellos, el Usuario NO deber� proporcionar ninguna informaci�n personal, ni utilizar este servicio o cualquier informaci�n relacionada con el sitio.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Aviso importante: �ltima actualizaci�n: Agosto de 2014.";
				
				
				
				TextArea areaTexto = new TextArea();
					areaTexto.setValue(texto);
					areaTexto.setWidth("100%");
					areaTexto.setHeight("100%");
					areaTexto.setReadOnly(true);
					
				final OptionGroup opciones = new OptionGroup();
					opciones.addItem("Acepto");
					opciones.addItem("No acepto");
					
				Button aceptar = new Button("Enviar");
					
				aceptar.addListener(new Button.ClickListener() {
				    /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
				    	
				    	if(opciones.getValue() == null || opciones.getValue().toString().equals("No acepto")){
				    		Notification.show("Debes aceptar para poder continuar", Type.WARNING_MESSAGE);
				    	}else{
				    		
				    		pantalla.removeAllComponents();
				    		
				    		Principal perso = new Principal();
				    		pantalla.addComponent(perso.personalizar(correo));
				    		
				    		Mysql sql = new Mysql();
				    		
				    		try{
				    		
				        		String respuesta = sql.insertarSimple("insert into accesos values ('"+correo+"',now(),'SI','inventario',null)");
				    				
				        		if(!respuesta.equals("OK")){
				        			throw new Exception(respuesta);
				        		}
				        		
				        		respuesta = sql.insertarSimple("update tupro_main.usuarios_datos set tyc = 'SI' where correo = '"+Encriptar.Encriptar(correo)+"'");
			    				
				        		if(!respuesta.equals("OK")){
				        			throw new Exception(respuesta);
				        		}
				        		
				        		ventana.close();
				    		
				    		}catch(Exception e){
				    			e.printStackTrace();
				    		}finally{
				    			sql.cerrar();
				    			sql = null;
				    		}
				    		
				    	}
				    	
				    }
				});
				
			layoutTexto.addComponent(areaTexto);
			layoutAcepta.addComponent(opciones);
				layoutAcepta.setComponentAlignment(opciones, Alignment.MIDDLE_CENTER);
			layoutAcepta.addComponent(aceptar);
				layoutAcepta.setComponentAlignment(aceptar, Alignment.MIDDLE_CENTER);
					
			principal.addComponent(layoutTexto);
				principal.setComponentAlignment(layoutTexto, Alignment.MIDDLE_CENTER);
				principal.setExpandRatio(layoutTexto, 3f);
				
			principal.addComponent(layoutAcepta);
				principal.setComponentAlignment(layoutAcepta, Alignment.MIDDLE_CENTER);
				principal.setExpandRatio(layoutAcepta, 1f);
				
			ventana.setContent(principal);
		
			UI.getCurrent().addWindow(ventana);
			
			return acepta;
			
		}catch(Exception e){
			e.printStackTrace();
			return acepta;
		}finally{
			sql.cerrar();
			sql = null;
		}
		
		
	}

}

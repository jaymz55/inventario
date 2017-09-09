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
	
	//Métodos
	@SuppressWarnings("deprecation")
	public boolean cargarVentana(final String correo, final VerticalLayout pantalla){
		
		Mysql sql = new Mysql();
		
		try{
		
			final Window ventana = new Window("Términos y Condiciones");
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
				String texto = "CONTRATO DE PRESTACION DE SERVICIOS QUE CELEBRAN POR UNA PARTE “USTED” QUE EN LO SUCESIVO SE LE DENOMINARA “EL USUARIO” Y POR LA OTRA PARTE “TUPROGRAMA.MX” QUE EN LOS SUCESIVO SE LE DENOMINARA “LA EMPRESA” LAS CUALES SE SUJETAN AL TENOR DE LAS SIGUIENTES DECLARACIONES Y CLAUSULAS.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"DECLARACIONES\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Declara “LA EMPRESA”\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"a) Que es una persona moral debidamente constituida conforme a la legislación mexicana.\r\n" + 
						"b) Que tiene la capacidad legal, tecnológica y humana para celebrar el presente contrato, facultades que no han sido restringidas o modificadas en forma alguna.\r\n" + 
						"c) Se encuentra inscrita en el Registro Federal de Contribuyentes bajo el número XAXX010101000.\r\n" + 
						"\r\n" + 
						"Declara “EL USUARIO”\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"a) Que los datos proporcionados en la orden de compra son fidedignos, por lo que manifiesta que la información que proporciona es real y comprobable, relevando a “LA EMPRESA” de cualquier responsabilidad por la falsedad con la que se conduzca, por la falsificación de documentos o suplantación de personas, nombres, direcciones o datos de identificación que utilice para contratar los servicios que presta “LA EMPRESA”.\r\n" + 
						"b) Que se encuentra en pleno uso de sus facultades físicas, mentales y legales para poder celebrar este contrato.\r\n" + 
						"c) En el caso de ser usuario individual, “EL USUARIO” garantiza que al menos tiene 18 años de edad y en el caso de ser usuario de una compañía, “EL USUARIO” garantiza que el servicio no será usado por una persona menor a los 18 años de edad.\r\n" + 
						"d) Que es el único y exclusivo responsable por los datos, marcas, logos, patentes o símbolos y colores de identificación que proporciona.\r\n" + 
						"\r\n" + 
						"En virtud de las declaraciones anteriores, las Partes convienen sujetarse a lo dispuesto en las siguientes cláusulas.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"CLAUSULAS\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"PRIMERA. Objeto\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Por el presente contrato “EL USUARIO” contrata el servicio de acceso a la aplicación de punto de venta el cual se encuentra en un servidor compartido provisto por “LA EMPRESA” para su uso, así como también la prestación del servicio de timbrado, asignación de folio e incorporación del sello digital a los Comprobantes Fiscales Digitales (CFDi).\r\n" + 
						"\r\n" + 
						"\r\n" + 
						//"“EL USUARIO” deberá de realizar las configuraciones necesarias para la correcta utilización del servicio de timbrado, asignación de folio e incorporación del sello digital a los Comprobantes Fiscales Digitales (CFDi).\r\n" + 
						//"\r\n" + 
						//"\r\n" + 
						"SEGUNDA. Restricciones del contrato\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” no podrá consumir de manera excesiva los recursos del sistema, incluyendo pero no limitado, a ciclos del procesador, memoria y transferencia, en caso de violentar este punto “LA EMPRESA” puede restringir , limitar o suspender el servicio contratado por el uso excesivo de recursos sin ninguna responsabilidad para esta última.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” deberá mantener de forma segura cualquier identificación, contraseña y otro tipo de información confidencial relacionado con su cuenta y deberá notificar inmediatamente a “LA EMPRESA” del conocimiento o sospecha del uso no autorizado de su cuenta o brecha de seguridad, incluyendo perdida, robo o uso no autorizado de su contraseña o cualquier otro tipo de información de seguridad.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” no podrá realizar alguna acción que pudiera dañar nuestra reputación de negocios, o la de alguna compañía o negocio relacionado con “LA EMPRESA”, ello incluye comentarios denigrantes o de desprestigio que se efectúen en páginas, blogs, posts o cualquier otro medio en los que “EL USUARIO” pueda verter estos comentarios en contra de “LA EMPRESA”, sus directivos, empleados o personal, por lo que nos reservaremos el derecho de remover la cuenta de “EL USUARIO” así como si realizar comentarios que denoten los servicios, productos, red, empresas afiliadas, socios estratégicos, empleados, directores o personal de “LA EMPRESA”.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” que realice comentarios de desprestigio o que inicien campañas denigrantes de los servicios o productos que ofrece “LA EMPRESA”, serán responsables de las determinaciones que emitan las autoridades competentes en materia civil y/o penal así como aceptar el pago de daños y perjuicios que llegare ocasionar a “LA EMPRESA”.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"TERCERA. Disponibilidad del servicio\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“LA EMPRESA” hará o aplicará el mayor esfuerzo razonable para hacer que el servidor y el servicio esté disponible para “EL USUARIO” todo el tiempo, sin embargo ante cualquier eventualidad, “LA EMPRESA” no será responsable por interrupciones del servicio o por el tiempo que el servidor este fuera de línea.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“LA EMPRESA” tiene el derecho de suspender el servicio en cualquier momento de serlo necesario para garantizar la preservación del servidor o por cualquier razón que así lo creyere conveniente tratando siempre de dar aviso a “EL USUARIO”, de no ser posible el “EL USUARIO” se retracta de cualquier acción legal en contra de “LA EMPRESA”, exonerándola de cualquier responsabilidad por ello.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” libera a “LA EMPRESA” de cualquier responsabilidad por la pérdida de información o datos almacenados en el servidor de “LA EMPRESA”, sin embargo “LA EMPRESA” pondrá especial cuidado en la conservación e integridad de la información de “EL USUARIO”, y si la información se llegare a perder, ello no será responsabilidad de “LA EMPRESA”. Es responsabilidad de “EL USUARIO” mantener sus propios respaldos de su información.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"CUARTA. Pagos\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Todo cargo pagadero que ”EL USUARIO” deba realizar por los servicios será de acuerdo con la escala de cargos y las tarifas publicadas por nosotros en nuestro sitio web y será debido y pagadero por adelantado a la disposición de nuestros servicios. Nos reservamos el derecho de cambiar los precios en cualquier momento y sin previo aviso, sin embargo todos los precios quedan garantizados a “EL USUARIO” por el periodo de tiempo que haya realizado su prepago.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El pago de los servicios se realiza por el tiempo acordado en la orden de compra.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Todos los pagos deberán ser en pesos mexicanos o su equivalente en moneda extranjera.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si el pago de cualquier suma que “EL USUARIO” deba cubrir no se realiza antes de la fecha límite, nosotros tendremos el total derecho de suspender los servicios contratados.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"QUINTA. Terminación y autorenovación del contrato\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si “EL USUARIO” incumple con el pago de cualquier renovación que tenga con “LA EMPRESA”, esta última podrá suspender el servicio y/o terminar este acuerdo de voluntades de manera inmediata sin previo aviso a “EL USUARIO”, ello sin ninguna responsabilidad para “LA EMPRESA”.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si “EL USUARIO” falla en el cumplimiento de cualquiera de los términos, condiciones o restricciones de este acuerdo, no importando el orden o exclusión, “LA EMPRESA” podrá suspender el servicio y/o terminar este acuerdo de manera inmediata sin que medie aviso previo.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Cuando “LA EMPRESA” reciba una solicitud de terminación o rescisión de la cuenta contratada o de algunos de los servicios, a la misma se le dará tramite y será aceptada siempre y cuando “LA EMPRESA” cuente con pruebas bastantes y suficientes que se trata de una solicitud realizada por “EL USUARIO”, quien será el único autorizado para realizar la cancelación o la rescisión de la cuenta. Tal información requerida es de manera enunciativa mas no limitativa: correo electrónico, usuario, contraseña y toda aquella información que “LA EMPRESA” considere pertinente para verificar su identidad.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” que desee cancelar algún servicio deberá enviar su solicitud por medio de correo electrónico a “LA EMPRESA”, proporcionando datos de identificación, número de cuenta, identificación de usuario, el servicio que pretenda cancelar y una exposición de motivos por los cuales pretenda cancelar el servicio.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"SEXTA. Ley aplicable y jurisdicción\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de disputa entre “EL USUARIO” y “LA EMPRESA”, las partes están conscientes y de acuerdo en someterse a los Tribunales y las leyes sustantivas y adjetivas del Estado de Nuevo León, México, para dirimir sus diferencias. Por lo que renuncian a cualquier fuero y legislación que les pudiera ser aplicable por razón de su domicilio presente o futuro o bien por cualquier otra causa.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Los títulos o definiciones que se incluyen en este acuerdo son para conveniencia solamente y no afectarán la construcción o la interpretación de este acuerdo.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El usuario al momento de registrarse como nuevo usuario en el módulo \"Contratar\" y levantar su orden compra, está de acuerdo a que estos actos se consideren como su consentimiento expreso, aceptando y conociendo los significados y alcances de los términos y condiciones aquí plasmados, así como la política por spam, validando su consentimiento con la firma electrónica.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de incurrir en abusos y/o actividades ilegales que requieran procedimientos legales, estos deberán ser realizados en primera instancia, sometiendo expresamente jurisdicción a los tribunales y autoridades competentes en el Estado de Nuevo León, México.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"“EL USUARIO” será el único responsable de las actividades que desarrolle al amparo de nuestros servicios o productos, relevando a “LA EMPRESA” de cualquier responsabilidad por ello.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de que “LA EMPRESA” se vea involucrada en procedimientos judiciales o administrativos por las conductas o actividades que despliegue “EL USUARIO”, este último se hará responsable de las costas y gastos que ello origine.";
				
				
				String AvisoPrivacidad = "Responsable de la protección de tus datos personales\r\n" + 
						"\r\n" + 
						"PROVEEDOR DE SERVICIOS TI, S.A. DE C.V., y/o cualquiera de sus subsidiarias y/o afiliadas (Softseti), con domicilio en Filósofos 221 203, Colonia Tecnológico, Monterrey, Nuevo León, C.P. 64700, México, y portal de internet http://softseti.net, en cumplimiento a lo establecido por la Ley Federal de Protección de Datos personales en Posesión de los Particulares (\"Ley\") y con la finalidad de garantizar la privacidad y el derecho a la autodeterminación informativa de sus clientes y usuarios, hace de su conocimiento la política de privacidad y manejo de datos personales.\r\n" + 
						"\r\n" + 
						"El uso o navegación por parte de cualquier persona del sitio antes mencionado le concede la calidad de Usuario y/o Cliente.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Recolección de datos.\r\n" + 
						"\r\n" + 
						"Personales. En la prestación de servicios, Softseti requerirá a sus clientes sólo de datos personales, ya sea de identificación y/o financieros y/o profesionales proporcionados por usted de manera directa o por cualquier medio de contacto y/o foro público de conexión en línea relacionados con los servicios que presta Softseti, destacando los siguientes:\r\n" + 
						"\r\n" + 
						"Nombre\r\n" + 
						"Razón social\r\n" + 
						"Registro Federal de Contribuyentes(RFC)\r\n" + 
						"Domicilio\r\n" + 
						"Telefono particular\r\n" + 
						"Correo electrónico\r\n" + 
						"Datos de identificación\r\n" + 
						"Con los fines señalados en este aviso de privacidad, podemos recabar sus datos personales de distintas formas: durante el proceso de registro, inscripción a alguna promoción o programa o cuando nos lo proporcione directamente.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Es decir toda información personal que el usuario haya proporcionado a Softseti de manera directa por medio de este sitio, aquella información personal proporcionada por el usuario al momento de utilizar nuestros servicios en línea o cuando Softseti obtiene la información personal del usuario lícitamente por cualquier otro medio o fuentes permitidas por la ley, serán utilizados con las siguientes finalidades: Proveer servicios y productos requeridos en el sitio, proveer y garantizar las operaciones de generación y certificación de Comprobantes Fiscales Digitales por Internet (CFDI), informar sobre nuevos productos o servicios que estén relacionados con el contratado o adquirido por el usuario, dar cumplimiento con obligaciones contraídas con el usuario, realizar estudios internos sobre hábitos de consumo, promover los productos y servicios que Softseti ofrece, informarle sobre cambios en los mismos, para fines publicitarios, promocionales, telemarketing, operaciones, administración del sitio web de Softseti, administración de los servicios de Softseti, desarrollo de nuevos productos y servicios, encuestas de calidad del servicio o de producto contratados por el usuario y satisfacción del usuario, análisis de uso de productos, servicios y sitio web, para el envío de avisos acerca de productos y servicios operados por Softseti y/o por sus afiliadas, subsidiarias y/o por sus socios de negocio; cuando la ley o alguna autoridad lo requiera, para solicitarle actualización de sus datos y documentos de identificación, y en general para hacer cumplir nuestros términos, condiciones y la operación, funcionamiento y administración de nuestros negocios.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El concepto de \"datos personales\" en este aviso de privacidad se refiere a toda aquella información de carácter personal que pueda ser usada para identificación del Usuario.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Softseti no recaba ni trata datos personales sensibles.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Uso de cookies y scripts\r\n" + 
						"\r\n" + 
						"Los datos personales que recabamos cuando visitas nuestro sitio de Internet o utilizas nuestros servicios en línea son la Dirección IP y Cookies.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Softseti podrá recabar datos personales por el uso de cookies. La página de nuestro sitio Web utiliza cookies, los cuales son pequeños ficheros de datos que se descargan automáticamente y almacenados en la computadora del Usuario, las cuales graban sus datos personales cuando se conecta al sitio, entre ellos sus preferencias para la visualización de las páginas de este servidor, modificándose al abandonar el sitio y las cuales permiten obtener la información siguiente:\r\n" + 
						"\r\n" + 
						"Páginas de internet que visita el usuario.\r\n" + 
						"La fecha y hora de la última vez que el usuario visitó nuestro sitio Web.\r\n" + 
						"El diseño de contenidos o preferencias que el usuario escogió en su primera visita a nuestro sitio web.\r\n" + 
						"Elementos de seguridad que intervienen en el control de acceso a las áreas restringidas.\r\n" + 
						"Reconocer a los usuarios.\r\n" + 
						"Detectar su ancho de banda.\r\n" + 
						"Medir parámetros de tráfico.\r\n" + 
						"Su tipo de navegador y sistema operativo.\r\n" + 
						"Su dirección IP.\r\n" + 
						"La resolución de su monitor.\r\n" + 
						"Los cookies son anónimos. El acceso a la información por medio de los cookies, permite ofrecer al Usuario un servicio personalizado, ya que almacenan no sólo sus datos personales sino también la frecuencia de utilización del servicio y las secciones de la red visitadas, reflejando así sus hábitos y preferencias.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"El Usuario tiene la opción de impedir la generación de cookies, mediante la selección de la correspondiente opción en la configuración de su navegador de Internet. Sin embargo Softseti no se responsabiliza de que la desactivación de los mismos, ya que impedirían el buen funcionamiento del sitio de Internet.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Los cookies son archivos de texto descargados automáticamente y almacenados en el disco duro del equipo de cómputo del usuario al navegar en una página de Internet específica, que permiten recordar al servidor de Internet algunos datos sobre este usuario, entre ellos, sus preferencias para la visualización de las páginas en ese servidor, nombre y contraseña.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Así mismo las scripts son piezas de código que nos permiten obtener información como la dirección IP del usuario, duración del tiempo de interacción en una página y el tipo de navegador utilizado, entre otros.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Estas cookies y otras tecnologías pueden ser deshabilitadas. Para conocer cómo hacerlo, consulte la documentación de su navegador.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Finalidad del tratamiento de datos.\r\n" + 
						"\r\n" + 
						"Los datos personales que el cliente nos proporcione con fines comerciales para contactarlo y/o enviarle información respecto de los servicios contratados, así como con fines estadísticos sujetos a un proceso de disociación.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Los datos personales también son utilizados para realizar estudios internos sobre los datos demográficos, intereses y comportamiento de los usuarios; con la finalidad de proporcionarles productos, servicios, contenidos y publicidad acordes a sus necesidades, así como proporcionar notificaciones e información de manera confidencial sobre su servicio y contactar a los usuarios cuando sea necesario, por teléfono o correo electrónico en caso de que se requieran datos adicionales para completar alguna transacción.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Transferencia de datos.\r\n" + 
						"\r\n" + 
						"Softseti podrá tratar los datos personales o los podrá poner a disposición de terceros dentro o fuera del país, en este sentido, su información puede ser compartida a otros tercero a consecuencia de una relación contractual o bien en aquellos casos en que la divulgación sea necesaria para la eficaz operación de los servicios proporcionados.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Si usted no manifiesta oposición alguna por escrito para que sus datos sean tratados y/o transferidos a terceros, se entenderá que ha otorgado su consentimiento para ello una vez que haya otorgado la aceptación electrónica y/o al ingresar sus datos dentro del contenido y funciones del sitio.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Softseti podrá transmitir lo datos que recabe de sus clientes a cualquiera de las subsidiarias y/o afiliadas y/o socios de negocios y/o terceros, quienes quedarán obligadas a resguardar y utilizar la información en términos de este Aviso de Privacidad y por cualquiera de las razones previstas en la “Ley” Softseti se reserva el derecho de transmitir los datos de sus clientes.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"En caso de vender o traspasar la totalidad o parte de su negocio y/o activos, Softseti le comunicará la obligación al siguiente propietario para que utilice los datos de los clientes con apego a esté Aviso de Privacidad, así mismo le informará al titular que pretende transferir los datos a terceros. Si el titular no desea que sus datos sean transferidos al tercero deberá manifestarlo al nuevo propietario.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Medios para ejercer los derechos de acceso, rectificación, cancelación u oposición.\r\n" + 
						"\r\n" + 
						"Usted tiene derecho a conocer qué datos personales que tenemos suyos, para qué los utilizamos y las condiciones del uso que les damos (Acceso). Asimismo, es su derecho solicitar la corrección de su información personal en caso de que esté desactualizada, sea inexacta o incompleta (Rectificación); que la eliminemos de nuestros registros o bases de datos cuando considere que la misma no está siendo utilizada adecuadamente (Cancelación); así como oponerse al uso de sus datos personales para fines específicos (Oposición). Estos derechos se conocen como derechos ARCO.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Todo cliente de Softseti interesado en ejecutar los derechos ARCO previstos en la “Ley”, podrá hacerlo poniéndose en contacto con Elías Córdova y/o Eduardo Garza y/o Enrique Gorian, responsables de nuestro Departamento de Protección de Datos Personales, en el domicilio ubicado en Filósofos 221 203, Col. Tecnológico, Monterrey, Nuevo León, CP 64700, México, o bien, se comunique al teléfono 01 81 1935 9838 o vía correo electrónico soporte@softseti.net, el cual solicitamos confirme vía telefónica para garantizar su correcta recepción.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Con relación al procedimiento y requisitos para el ejercicio de sus derechos ARCO, le informamos lo siguiente:\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"a) ¿A través de qué medios pueden acreditar su identidad el titular y, en su caso, su representante, así como la personalidad este último? Con el original y copia de identificación oficial vigente; en el caso de personas morales, se debe agregar original y copia del instrumento notarial en el que consten sus facultades.\r\n" + 
						"b) ¿Qué información y/o documentación deberá contener la solicitud? Los pormenores respecto de los derechos ARCO, y la precisión o intención que busca el titular, narrando con amplitud los hechos en que basa su pedido.\r\n" + 
						"c) ¿En cuántos días le daremos respuesta a su solicitud? En diez días hábiles.\r\n" + 
						"d) ¿Por qué medio le comunicaremos la respuesta a su solicitud? Por correo electrónico.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Usted puede revocar el consentimiento que, en su caso, nos haya otorgado para el tratamiento de sus datos personales. Sin embargo, es importante que tenga en cuenta que no en todos los casos podremos atender su solicitud o concluir el uso de forma inmediata, ya que es posible que por alguna obligación legal requiramos seguir tratando sus datos personales. Asimismo, usted deberá considerar que para ciertos fines, la revocación de su consentimiento implicará que no le podamos seguir prestando el servicio que nos solicitó, o la conclusión de su relación con nosotros.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Cambios al Aviso de Privacidad\r\n" + 
						"\r\n" + 
						"El presente aviso de privacidad puede sufrir modificaciones, cambios o actualizaciones derivadas de nuevos requerimientos legales; de nuestras propias necesidades por los productos o servicios que ofrecemos; de nuestras prácticas de privacidad; de cambios en nuestro modelo de negocio, o por otras causas.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Cualquier cambio a este Aviso de Privacidad se hará del conocimiento de los clientes de Softseti que marquen al 01 81 1935 9838 o manden correo electrónico a la siguiente dirección: soporte@softseti.net amén de publicarlo en nuestro portal de internet.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Este aviso de privacidad, describe la Política de Privacidad del Softseti, el cual constituye un acuerdo válido entre el Usuario y Softseti; si el Usuario utiliza los servicios de Softseti significa que ha leído, entendido, aceptado y consecuentemente acordado con el Softseti los términos del aviso de privacidad antes expuestos. En caso de no estar de acuerdo con ellos, el Usuario NO deberá proporcionar ninguna información personal, ni utilizar este servicio o cualquier información relacionada con el sitio.\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"Aviso importante: Última actualización: Agosto de 2014.";
				
				
				
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

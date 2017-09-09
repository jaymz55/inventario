package sql.DTO;

public class UsuarioDTO {

	private String custid;
	private String password;
	private String nombre;
	private String correo;
	private String privilegios;
	private String custidsRelacionados;
	private String tipo; //Principal o secundario
	private String correoSolicitudes;
	private String correoSolicitudesCopia;
	private double deuda;
	private boolean deudaVencida = false;
	
	private boolean autenticado = false;
	private boolean comodin = false;
	
	private String padre;
	
	//Getters y Setters
	
		public boolean getComodin() {
			return comodin;
		}
		public void setComodin(boolean comodin) {
			this.comodin = comodin;
		}
		public double getDeuda() {
			return deuda;
		}
		public void setDeuda(double deuda) {
			this.deuda = deuda;
		}
		public boolean getDeudaVencida() {
			return deudaVencida;
		}
		public void setDeudaVencida(boolean deudaVencida) {
			this.deudaVencida = deudaVencida;
		}
		public boolean getAutenticado() {
			return autenticado;
		}
		public void setAutenticado(boolean autenticado) {
			this.autenticado = autenticado;
		}
		public String getCorreoSolicitudesCopia() {
			return correoSolicitudesCopia;
		}
		public void setCorreoSolicitudesCopia(String correoSolicitudesCopia) {
			this.correoSolicitudesCopia = correoSolicitudesCopia;
		}
		public String getCorreoSolicitudes() {
			return correoSolicitudes;
		}
		public void setCorreoSolicitudes(String correoSolicitudes) {
			this.correoSolicitudes = correoSolicitudes;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getCorreo() {
			return correo;
		}
		public void setCorreo(String correo) {
			this.correo = correo;
		}
		public String getPrivilegios() {
			return privilegios;
		}
		public void setPrivilegios(String privilegios) {
			this.privilegios = privilegios;
		}
		public String getCustidsRelacionados() {
			return custidsRelacionados;
		}
		public void setCustidsRelacionados(String custidsRelacionados) {
			this.custidsRelacionados = custidsRelacionados;
		}
		public String getPadre() {
			return padre;
		}
		public void setPadre(String padre) {
			this.padre = padre;
		}
	
	
}

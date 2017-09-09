package sql.DTO;

public class ClienteDTO extends AbstractDTO{

	//Variables
		private String idCliente;
		private String custid;
		private String nombre;
		private String telefono;
		private String correo;
		private String direccion;
		private String observaciones;
		private String vendedorAsignado;
		private String idVendedor;
		private String activo;
		
		
	//Getters
		public String getIdCliente() {
			return idCliente;
		}
		public void setIdCliente(String idCliente) {
			this.idCliente = idCliente;
		}
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getTelefono() {
			return telefono;
		}
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		public String getCorreo() {
			return correo;
		}
		public void setCorreo(String correo) {
			this.correo = correo;
		}
		public String getDireccion() {
			return direccion;
		}
		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}
		public String getObservaciones() {
			return observaciones;
		}
		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}
		public String getVendedorAsignado() {
			return vendedorAsignado;
		}
		public void setVendedorAsignado(String vendedorAsignado) {
			this.vendedorAsignado = vendedorAsignado;
		}
		public String getIdVendedor() {
			return idVendedor;
		}
		public void setIdVendedor(String idVendedor) {
			this.idVendedor = idVendedor;
		}
		public String getActivo() {
			return activo;
		}
		public void setActivo(String activo) {
			this.activo = activo;
		}
		
	
	
	//Const
	
	
	
	//Metodos
	
}

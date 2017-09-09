package sql.DTO;

public class VendedorDTO {

	//Variables
		private String idVendedor;
		private String custid;
		private String nombre;
		private String observaciones;
		
	//Getters...	
		public String getIdVendedor() {
			return idVendedor;
		}
		public void setIdVendedor(String idVendedor) {
			this.idVendedor = idVendedor;
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
		public String getObservaciones() {
			return observaciones;
		}
		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

}

package sql.DTO;

import java.util.ArrayList;
import java.util.List;

public class SolicitudDTO {

	//Variables
		private String custid;
		private List<String> productos = new ArrayList<String>();
		private List<String> cantidades = new ArrayList<String>();;
		private List<String> precios = new ArrayList<String>();;
		private String fecha;
		private String descuento;
		private String comentario;
		
	
	//Getters y Setters	
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public List<String> getProductos() {
			return productos;
		}
		public void setProductos(List<String> productos) {
			this.productos = productos;
		}
		public List<String> getCantidades() {
			return cantidades;
		}
		public void setCantidades(List<String> cantidades) {
			this.cantidades = cantidades;
		}
		public List<String> getPrecios() {
			return precios;
		}
		public void setPrecios(List<String> precios) {
			this.precios = precios;
		}
		public String getFecha() {
			return fecha;
		}
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		public String getDescuento() {
			return descuento;
		}
		public void setDescuento(String descuento) {
			this.descuento = descuento;
		}
		public String getComentario() {
			return comentario;
		}
		public void setComentario(String comentario) {
			this.comentario = comentario;
		}
		
		
	
}

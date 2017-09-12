package sql.DTO;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductoDTO {

	//Variables
		private String idProducto;
		private String custid;
		private String nombre;
		private String precio;
		private String iva;
		private String total;
		private String descripcion;
		private String grava_iva;
		private String minimo;
		private String maximo;
		private String activo;
		private JSONObject json;

		private int existencia;
		
	//Getters y Setters
		public String getIdProducto() {
			return idProducto;
		}
		public void setIdProducto(String idProducto) {
			this.idProducto = idProducto;
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
		public String getPrecio() {
			return precio;
		}
		public void setPrecio(String precio) {
			this.precio = precio;
		}
		public String getIva() {
			return iva;
		}
		public void setIva(String iva) {
			this.iva = iva;
		}
		public String getTotal() {
			return total;
		}
		public void setTotal(String total) {
			this.total = total;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getGrava_iva() {
			return grava_iva;
		}
		public void setGrava_iva(String grava_iva) {
			this.grava_iva = grava_iva;
		}
		public String getMinimo() {
			return minimo;
		}
		public void setMinimo(String minimo) {
			this.minimo = minimo;
		}
		public String getMaximo() {
			return maximo;
		}
		public void setMaximo(String maximo) {
			this.maximo = maximo;
		}
		public String getActivo() {
			return activo;
		}
		public void setActivo(String activo) {
			this.activo = activo;
		}
		public int getExistencia() {
			return existencia;
		}
		public void setExistencia(int existencia) {
			this.existencia = existencia;
		}
		//Metodos
		public JSONObject getJson() throws JSONException{
			
			json = new JSONObject();
			
				json.put("id", getIdProducto());
				json.put("custid", getCustid());
				json.put("nombre", getNombre());
				json.put("precio", getPrecio());
				json.put("iva", getIva());
				json.put("total", getTotal());
				json.put("descripcion", getDescripcion());
				json.put("grava_iva", getGrava_iva());
				json.put("minimo", getMinimo());
				json.put("maximo", getMaximo());
				json.put("activo", getActivo());
			
			return json;
			
		}
		
}

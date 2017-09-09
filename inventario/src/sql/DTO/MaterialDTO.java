package sql.DTO;

import org.json.JSONException;
import org.json.JSONObject;

import funciones.Funcion;

public class MaterialDTO {

	//Variables
		private String idMaterial;
		private String custid;
		private String categoria;
		private String nombre;
		private String sku;
		private String minimo = "";
		private String maximo = "";
		private String proveedor;
		private String unidadMedida;
		private String unidadMedidaNombre;
		private String activo;
		private JSONObject json;
		private String barCode;
		//private JSONObject consolidadoUnico;

	//Variables para registro de entrada
		private String cantidad;
		private double costo;
		private double iva;
		private double total;
		private String fechaIngreso;
		private String fechaCaducidad;
		private String idProveedor;
		private String nombreProveedor;
		private String contieneIva;
		private String movimiento;
		
	//Variables de consolidado
		private String existencia;
		private String diferencial;
		
	//Variables de inventario
		private String idEntradaInventario;
		
	//Variables de merma
		private String fechaMerma;
		
	//Constructores
		/*public MaterialDTO(){};
		
		public MaterialDTO(String idMaterial){
			
		}*/
		
	//Getters y Setters
		public String getIdMaterial() {
			return idMaterial;
		}
		public void setIdMaterial(String idMaterial) {
			this.idMaterial = idMaterial;
		}
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public String getCategoria() {
			return categoria;
		}
		public void setCategoria(String categoria) {
			this.categoria = categoria;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}
		public String getMinimo() {
			return Funcion.decimales(minimo);
		}
		public void setMinimo(String minimo) {
			this.minimo = minimo;
		}
		public String getMaximo() {
			return Funcion.decimales(maximo);
		}
		public void setMaximo(String maximo) {
			this.maximo = maximo;
		}
		public String getProveedor() {
			return proveedor;
		}
		public void setProveedor(String proveedor) {
			this.proveedor = proveedor;
		}
		public String getUnidadMedida() {
			return unidadMedida;
		}
		public void setUnidadMedida(String unidadMedida) {
			this.unidadMedida = unidadMedida;
		}
		public String getUnidadMedidaNombre() {
			return unidadMedidaNombre;
		}
		public void setUnidadMedidaNombre(String unidadMedidaNombre) {
			this.unidadMedidaNombre = unidadMedidaNombre;
		}
		public String getActivo() {
			return activo;
		}
		public void setActivo(String activo) {
			this.activo = activo;
		}
	
	//Segundos Getters y Setters
		
		public String getCantidad() {
			return cantidad;
		}
		public void setCantidad(String cantidad) {
			this.cantidad = cantidad;
		}
		public double getCosto() {
			return costo;
		}
		public void setCosto(double costo) {
			this.costo = costo;
		}
		public double getIva() {
			return iva;
		}
		public void setIva(double iva) {
			this.iva = iva;
		}
		public double getTotal() {
			return total;
		}
		
		
	//Al agregar el costo total y considerando si hay IVA o no, se calcula solo el costo e IVA
		public void setTotal(double total) {
			this.total = total;
			
			if(getContieneIva().equals("SI")){
				setCosto(total/1.16);
				setIva((total/1.16)*0.16);
			}else{
				setCosto(total);
				setIva(0.0);
			}
			
		}
		
		
		public String getFechaIngreso() {
			return fechaIngreso;
		}
		public void setFechaIngreso(String fechaIngreso) {
			this.fechaIngreso = fechaIngreso;
		}
		public String getFechaCaducidad() {
			return fechaCaducidad;
		}
		public void setFechaCaducidad(String fechaCaducidad) {
			this.fechaCaducidad = fechaCaducidad;
		}
		public String getIdProveedor() {
			return idProveedor;
		}
		
		public void setIdProveedor(String idProveedor) {
			this.idProveedor = idProveedor;
		}
		public String getNombreProveedor() {
			return nombreProveedor;
		}
		public void setNombreProveedor(String nombreProveedor) {
			this.nombreProveedor = nombreProveedor;
		}
		public String getContieneIva() {
			return contieneIva;
		}
		public void setContieneIva(String contieneIva) {
			this.contieneIva = contieneIva;
		}
		public String getMovimiento() {
			return movimiento;
		}
		public void setMovimiento(String movimiento) {
			this.movimiento = movimiento;
		}
		public String getExistencia() {
			return existencia;
		}
		public void setExistencia(String existencia) {
			this.existencia = existencia;
		}
		public String getDiferencial() {
			return diferencial;
		}
		public void setDiferencial(String diferencial) {
			this.diferencial = diferencial;
		}
		public String getIdEntradaInventario() {
			return idEntradaInventario;
		}
		public void setIdEntradaInventario(String idEntradaInventario) {
			this.idEntradaInventario = idEntradaInventario;
		}
		public String getFechaMerma() {
			return fechaMerma;
		}
		public void setFechaMerma(String fechaMerma) {
			this.fechaMerma = fechaMerma;
		}
		
	//Metodos

		public void setJson(JSONObject json) {
			this.json = json;
		}

		public JSONObject getJson() throws JSONException{
			
			json = new JSONObject();
			
				json.put("idMaterial", getIdMaterial());
				json.put("custid", getCustid());
				json.put("categoria", getCategoria());
				json.put("nombre", getNombre());
				json.put("sku", getSku());
				json.put("minimo", getMinimo());
				json.put("maximo", getMaximo());
				json.put("proveedor", getProveedor());
				json.put("unidadMedida",getUnidadMedida());
				json.put("activo", getActivo());
				
				json.put("existencia", getExistencia());
				json.put("diferencial",getDiferencial());
			
			return json;
			
		}
		public String getBarCode() {
			return barCode;
		}
		public void setBarCode(String barCode) {
			this.barCode = barCode;
		}
		
		//Json para consolidado unico
			/*public void setConsolidadoUnico(JSONObject json) {
				this.consolidadoUnico = json;
			}
	
			public JSONObject getConsolidadoUnico() throws JSONException{
				
				json = new JSONObject();
				
					json.put("idMaterial", getIdMaterial());
					json.put("custid", getCustid());
					json.put("categoria", getCategoria());
					json.put("nombre", getNombre());
					json.put("sku", getSku());
					json.put("minimo", getMinimo());
					json.put("maximo", getMaximo());
					json.put("existencia", getExistencia());
					json.put("diferencial",getDiferencial());
				
				return json;
				
			}*/
}

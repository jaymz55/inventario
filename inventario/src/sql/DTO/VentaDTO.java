package sql.DTO;

public class VentaDTO {

	//Variables
		private String idVenta;
		private String custid;
		private String idProducto;
		private String productoNombre;
		private String idCliente;
		private String clienteNombre;
		private double precio;
		private double iva;
		private double total;
		private double descuento;
		private String descuentoDescripcion;
		private String fecha;
		private int cantidad;
		private String vendedor;
		private String gravaIva;
		private String comentarios;
		private String pagada;
		private String afectaAlmacen;
		private String movimiento;
		
		
	//Getters...
		public String getIdVenta() {
			return idVenta;
		}
		public void setIdVenta(String idVenta) {
			this.idVenta = idVenta;
		}
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public String getIdProducto() {
			return idProducto;
		}
		public void setIdProducto(String idProducto) {
			this.idProducto = idProducto;
		}
		public String getProductoNombre() {
			return productoNombre;
		}
		public void setProductoNombre(String productoNombre) {
			this.productoNombre = productoNombre;
		}
		public String getIdCliente() {
			return idCliente;
		}
		public void setIdCliente(String idCliente) {
			this.idCliente = idCliente;
		}
		public String getClienteNombre() {
			return clienteNombre;
		}
		public void setClienteNombre(String clienteNombre) {
			this.clienteNombre = clienteNombre;
		}
		public double getPrecio() {
			return precio;
		}
		public void setPrecio(double precio) {
			this.precio = precio;
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
		public void setTotal(double total) {
			this.total = total;
		}
		public double getDescuento() {
			return descuento;
		}
		public void setDescuento(double descuento) {
			this.descuento = descuento;
		}
		public String getDescuentoDescripcion() {
			return descuentoDescripcion;
		}
		public void setDescuentoDescripcion(String descuentoDescripcion) {
			this.descuentoDescripcion = descuentoDescripcion;
		}
		public String getFecha() {
			return fecha;
		}
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		public int getCantidad() {
			return cantidad;
		}
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
		public String getVendedor() {
			return vendedor;
		}
		public void setVendedor(String vendedor) {
			this.vendedor = vendedor;
		}
		public String getGravaIva() {
			return gravaIva;
		}
		public void setGravaIva(String gravaIva) {
			this.gravaIva = gravaIva;
		}
		public String getComentarios() {
			return comentarios;
		}
		public void setComentarios(String comentarios) {
			this.comentarios = comentarios;
		}
		public String getPagada() {
			return pagada;
		}
		public void setPagada(String pagada) {
			this.pagada = pagada;
		}
		public String getAfectaAlmacen() {
			return afectaAlmacen;
		}
		public void setAfectaAlmacen(String afectaAlmacen) {
			this.afectaAlmacen = afectaAlmacen;
		}
		public String getMovimiento() {
			return movimiento;
		}
		public void setMovimiento(String movimiento) {
			this.movimiento = movimiento;
		}
		
	//Para variables especiales
		public double getSubtotalAjustado(){
				if(getDescuento() > 0){
					return ((getPrecio()-(getDescuento()/1.16)));
				}else{
					return (getPrecio());
				}
		}
		
		public double getDescuentoAjustado(){
				if(getDescuento() > 0){
					return ((getDescuento())/1.16);
				}else{
					return 0;
				}
		}
		
		public double getIvaAjustado(){
				if(getDescuento() > 0){
					return (getIva()-((getDescuento()/1.16)*.16));
				}else{
					return (getIva());
				}
		}

		public double getTotalAjustado(){
			if(getDescuento() > 0){
				return (getTotal()-getDescuento());
			}else{
				return (getTotal());
			}
	}
		
	//Const
		public VentaDTO(){
			setPagada("NO");
		};
	
	//Metodos
	
}

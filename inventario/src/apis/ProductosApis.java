package apis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import facade.Facade;
import apis.productos.Producto;

@Path("/productos")
public class ProductosApis {

	Facade facade = new Facade();
	
	@GET
	@Path("/custid/{custid}")
	@Produces("application/json;charset=utf-8")
	public String consultaPorCustid(@PathParam("custid")String custid) throws Exception{
		
		Producto prod = new Producto(custid);
		return prod.productosPorCustid().toString();
		
	}

	@GET
	@Path("/barras/{custid}/{codigoBarras}")
	@Produces("application/json;charset=utf-8")
	public String obtenerCodigoBarras(@PathParam("custid")String custid, @PathParam("codigoBarras")String codigoBarras) throws Exception{
		return facade.obtenerIdBarras(custid, codigoBarras);
	}
	
	@GET
	@Path("/barras/{custid}/{codigoBarras}/{idProducto}")
	@Produces("application/json;charset=utf-8")
	public String registrarCodigoBarras(@PathParam("custid")String custid, @PathParam("codigoBarras")String codigoBarras, @PathParam("idProducto")String idProducto) throws Exception{
		return facade.registrarCodigoProducto(custid, codigoBarras, idProducto);
	}
}

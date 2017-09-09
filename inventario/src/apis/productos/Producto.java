package apis.productos;

import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import facade.Facade;
import sql.DTO.ProductoDTO;

public class Producto {

	private String custid;
	private JSONObject jsonProducto;
	private JSONArray arrayProductos;
	Collection<ProductoDTO> coleccionProductos;
	
	private Facade facade = new Facade();
	
	//Constructores
	
		public Producto(String custid){
			this.custid = custid;
		}
		
	//Métodos
		public JSONArray productosPorCustid() throws JSONException{
			
			try{
				arrayProductos = new JSONArray();
				Collection<ProductoDTO> coleccion = facade.obtenerProductos(custid);
				
				for (ProductoDTO producto : coleccion) {
					arrayProductos.put(producto.getJson());
				}
				
				return arrayProductos;
				
			}catch(Exception e){
				e.printStackTrace();
				jsonProducto.put("respuesta", "error");
				arrayProductos.put(jsonProducto);
				return arrayProductos;
			}
			
		}
		
		
		public JSONObject codigoBarras(){
			
			
			
			try{
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				
			}
			
			return new JSONObject();
			
		}
	
}

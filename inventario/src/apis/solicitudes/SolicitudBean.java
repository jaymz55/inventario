package apis.solicitudes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import conexiones.BeanConexion;
import conexiones.BeanConsulta;
import conexiones.Mysql;

public class SolicitudBean {

	//Variables
		private String custid;
		private List<String> productos = new ArrayList<String>();
		private List<String> cantidades = new ArrayList<String>();
		private List<String> precios = new ArrayList<String>();
		private String fecha;
		private String descuento;
		private String comentario;
		
	//Constructores
		public SolicitudBean(String json) throws JSONException, UnsupportedEncodingException{
			
			System.out.println("entrada json: "+json);
			
			JSONObject bean = new JSONObject(json);
			if(bean.has("custid"))
				this.custid = bean.getString("custid");
			if(bean.has("fecha"))
				this.fecha = bean.getString("fecha");
			if(bean.has("descuento"))
				this.descuento = bean.getString("descuento");
			if(bean.has("comentario"))
				this.comentario = URLDecoder.decode(bean.getString("comentario"), "UTF-8");
			
			if(bean.has("productos")){
				
				JSONArray productosArray = new JSONArray(bean.get("productos").toString());
				JSONObject producto = new JSONObject();
				
					for (int i = 0; i < productosArray.length(); i++) {
						producto = productosArray.getJSONObject(i);
						productos.add(producto.getString("id").toString());
						cantidades.add(producto.getString("cantidad").toString());
						precios.add(producto.getString("precio").toString());
					}
				
				productosArray = null;
				producto = null;
				
			}
			
		}
		
		public String getCustid(){
			return custid;
		}
		
		public SolicitudBean(String custid, List<String> productos, List<String> cantidades, List<String> precios, String fecha, String descuento, String comentario){
			
			this.custid = custid;
			this.productos = productos;
			this.cantidades = cantidades;
			this.precios = precios;
			this.fecha = fecha;
			this.descuento = descuento;
			this.comentario = comentario;
			
		}
		
		public SolicitudBean(String custid, List<String> productos, List<String> cantidades, List<String> precios, String fecha){
			
			this.custid = custid;
			this.productos = productos;
			this.cantidades = cantidades;
			this.precios = precios;
			this.fecha = fecha;
			
		}
		
	//Métodos externos
		
		//Registrar
			public String registrar(){
				
				Mysql sql = new Mysql();
				BeanConsulta bean;
				String respuesta = "NO";
				String comentarioAjustado;
				String descuentoAjustado;
				String idSolicitud;
				
				try{
					
				//Ajusto variables
					if(comentario != null)
						comentarioAjustado = "'"+comentario+"'";
					else
						comentarioAjustado = "null";
					
					if(descuento != null)
						descuentoAjustado = descuento;
					else
						descuentoAjustado = "0";
					
				//Abro transacción
					sql.transaccionAbrir();
					
				//Registro solicitud
					respuesta = sql.insertarSimple("insert into tupro_inventario.solicitudes values ("
							+ "null, '"+custid+"','"+fecha+"',"+comentarioAjustado+",null,'NO','NO',"
									+ "'NO','SI', "+descuentoAjustado+")");
					
				//Obtengo id de la inserción
		 		    bean = sql.consultaSimple("SELECT LAST_INSERT_ID()");
		 		    
					if(!respuesta.equals("OK")){
						throw new Exception(respuesta);
					}
					
					idSolicitud = bean.getDato();
					
				//Registro desglose
					for (int i = 0; i < productos.size(); i++) {
						
						respuesta = sql.insertarSimple("insert into tupro_inventario.solicitudes_desglose values (null, '"+custid+"','"+idSolicitud+"','"+productos.get(i)+"','"+cantidades.get(i)+"',"+precios.get(i)+")");
		    			
						if(!respuesta.equals("OK")){
		    				throw new Exception(respuesta);
		    			}
						
					}
					
				//Cierra transacción
					
					if(respuesta.equals("OK")){
						
				    	sql.transaccionCommit();
				    	sql.transaccionCerrar();
				    	return "OK";
						
					}else{
						
						//Rollback
		    			sql.transaccionRollBack();
		    			sql.transaccionCerrar();
		    			return "ERROR";
						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					
				//Rollback
	    			sql.transaccionRollBack();
	    			sql.transaccionCerrar();
	    			return "ERROR";
					
				}finally{
					sql.cerrar();
					sql = null;
					bean = null;
					respuesta = null;
					comentarioAjustado = null;
					descuentoAjustado = null;
					idSolicitud = null;
				}
				
			}


		//Consultar
			public JSONObject consultar(String idSolicitud) throws JSONException{
				
				Mysql sql = new Mysql();
				JSONObject respuesta = new JSONObject();
				BeanConexion beanCon;
				ResultSet rs;
				
				try{
					
					beanCon = sql.conexionSimple("select * from tupro_inventario.solicitudes where id = "+idSolicitud);
					
					if(!beanCon.getRespuesta().equals("OK")){
						throw new Exception(beanCon.getRespuesta());
					}
					
					rs = beanCon.getRs();
					
					while(rs.next()){
						
						respuesta.put("id", rs.getString("id"));
						respuesta.put("custid", rs.getString("custid"));
						respuesta.put("fecha", rs.getString("fecha"));
						respuesta.put("comentarios", rs.getString("comentarios"));
						respuesta.put("destinatario", rs.getString("destinatario"));
						respuesta.put("vista", rs.getString("vista"));
						respuesta.put("modificada", rs.getString("modificada"));
						respuesta.put("completada", rs.getString("completada"));
						respuesta.put("activo", rs.getString("activo"));
						respuesta.put("descuento", rs.getDouble("descuento"));
						
					}
					
					return respuesta;
					
				}catch(Exception e){
					e.printStackTrace();
					
					respuesta.put("respuesta", "error");
					return respuesta;
					
				}finally{
					sql.cerrar();
					sql = null;
					beanCon =  null;
					rs = null;
					respuesta = null;
				}
				
			}
			
			public JSONArray consultarDesglose(String idSolicitud) throws JSONException{
				
				Mysql sql = new Mysql();
				JSONArray respuesta = new JSONArray();
				JSONObject parcial = new JSONObject();
				BeanConexion beanCon;
				ResultSet rs;
				
				try{
					
					beanCon = sql.conexionSimple("select * from tupro_inventario.solicitudes_desglose where id_solicitud = "+idSolicitud);
					
					if(!beanCon.getRespuesta().equals("OK")){
						throw new Exception(beanCon.getRespuesta());
					}
					
					rs = beanCon.getRs();
					
					while(rs.next()){
						
						parcial.put("id", rs.getString("id"));
						parcial.put("custid", rs.getString("custid"));
						parcial.put("id_solicitud", rs.getString("id_solicitud"));
						parcial.put("id_producto", rs.getString("id_prod"));
						parcial.put("cantidad", rs.getString("cantidad"));
						parcial.put("precio", rs.getString("precio"));
						
						respuesta.put(parcial);
						
						parcial = new JSONObject();
						
					}
					
					
					
					return respuesta;
					
				}catch(Exception e){
					e.printStackTrace();
					parcial.put("respuesta", "error");
					respuesta.put(parcial);
					return respuesta;
					
				}finally{
					sql.cerrar();
					sql = null;
					beanCon =  null;
					rs = null;
					respuesta = null;
					parcial = null;
				}
				
			}			
			
			
		//Métodos internos
			

}

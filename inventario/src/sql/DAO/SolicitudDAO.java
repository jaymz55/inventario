package sql.DAO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.SolicitudDTO;

public class SolicitudDAO {

	//Variables
		private String custid;
		private SolicitudDTO solicitud = new SolicitudDTO();
		private JSONObject json;
		
	
	//Constructores
		public SolicitudDAO(String json) throws JSONException, UnsupportedEncodingException{
			this.json = new JSONObject(json);
			deJsonAJava();
		}
	
		public SolicitudDAO(String custid, String json) throws JSONException{
			//this.custid = custid;
			this.json = new JSONObject(json);
		}
	
	//Metodos
		public boolean registrarSolicitud() throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			Statement statement = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				//Abro transaccion
				con.setAutoCommit(false);
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.solicitudes values (null, "
				+ "'"+custid+"','"+solicitud.getFecha()+"',"+solicitud.getComentario()+",null,'NO','NO',"
				+ "'NO','SI', "+solicitud.getDescuento()+")");

				pstm = con.prepareStatement(buffer.toString());
	        	respuesta = pstm.executeUpdate();
				
	        	if(respuesta == 1){
	        	
	        		statement = con.createStatement();
		        	rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
		        	
		        	String idSolicitud = null;
		        	
		        	while(rs.next()){
		        		idSolicitud = rs.getString(1);
		        	}
		        	
		        	for (int i = 0; i < solicitud.getProductos().size(); i++) {
		        		
		        		pstm = con.prepareStatement("insert into tupro_inventario.solicitudes_desglose values (null, '"+custid+"','"+idSolicitud+"','"+solicitud.getProductos().get(i)+"','"+solicitud.getCantidades().get(i)+"',"+solicitud.getPrecios().get(i)+")");
		        		respuesta = pstm.executeUpdate();
	
		        	}
	        	
	        	}
				
	        	if(respuesta == 1){
	        		con.commit();
	        		return true;
	        	}else{
	        		con.rollback();
	        		return false;
	        	}
	        	
			}catch(Exception e){
				con.rollback();
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally{
				
				con.setAutoCommit(true);
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();

				if(con != null){
					//Regreso conexion
					ConnectionPool.getPool().releaseConnection(con);
				}

				con = null;
				pstm = null;
				statement = null;
				rs = null;
				
				buffer = null;
				
			}

		}
		
		
		public SolicitudDTO deJsonAJava() throws JSONException, UnsupportedEncodingException{
			
			if(json.has("custid"))
				solicitud.setCustid(json.getString("custid"));
				this.custid = json.getString("custid");
			if(json.has("fecha"))
				solicitud.setFecha(json.getString("fecha"));
			if(json.has("descuento"))
				solicitud.setDescuento(json.getString("descuento"));
			else
				solicitud.setDescuento("0");
			
			if(json.has("comentario"))
				solicitud.setComentario("'"+URLDecoder.decode(json.getString("comentario")+"'", "UTF-8"));
			else
				solicitud.setComentario("null");
			
			if(json.has("productos")){
				
				JSONArray productosArray = new JSONArray(json.get("productos").toString());
				JSONObject producto = new JSONObject();
				
					for (int i = 0; i < productosArray.length(); i++) {
						producto = productosArray.getJSONObject(i);
						solicitud.getProductos().add(producto.getString("id").toString());
						solicitud.getCantidades().add(producto.getString("cantidad").toString());
						solicitud.getPrecios().add(producto.getString("precio").toString());
					}
				
				productosArray = null;
				producto = null;
				
			}
			return solicitud;
			
		}
		
	
}

package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;

import sql.ConnectionPool;
import sql.SqlConf;

public class CodigoBarrasDAO {

	//variables
		String custid;
		String codigoBarras;
		String idProducto;
		
	//Constructores
		public CodigoBarrasDAO(String custid, String codigoBarras){
			this.custid = custid;
			this.codigoBarras = codigoBarras;
		}
		
		public CodigoBarrasDAO(String custid, String codigoBarras, String idProducto){
			this.custid = custid;
			this.codigoBarras = codigoBarras;
			this.idProducto = idProducto;
		}
		
		
	//Metodos
	public String obtenerIdBarras() throws SQLException{
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		JSONObject respuesta = null;
		
		try{
			
			con = ConnectionPool.getPool().getConnection();
    		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.productos_barras where custid = "+custid+" and barcode = "+codigoBarras);
        	rs = pstm.executeQuery();
			
        	respuesta = new JSONObject();
        	respuesta.put("codigo", "null");
        	
        	while(rs.next()){
        		respuesta.put("codigo", rs.getString("id_producto"));
        	}
        	
        	return respuesta.toString();
        	
		}catch(Exception e){
			e.printStackTrace();
			return "{\"codigo\":\"ERROR\"}";
		}finally{
			
			if(rs != null)rs.close();
			if(pstm != null)pstm.close();

			if(con != null){
				//Regreso conexion
				ConnectionPool.getPool().releaseConnection(con);
			}

			con = null;
			pstm = null;
			rs = null;
			
		}
		
	}

	public String registrarProducto() throws SQLException{
		
		Connection con = null;
		Statement pstm = null;
		ResultSet rs = null;
		JSONObject respuesta = null;
		
		try{
			
			con = ConnectionPool.getPool().getConnection();
			
			//Abro la transaccion
				con.setAutoCommit(false);
			
			//Borro el dato
				pstm = con.createStatement();
				pstm.executeUpdate("delete from "+SqlConf.obtenerBase()+"inventario.productos_barras where custid = "+custid+" and barcode = "+idProducto);
				
			//Inserto los nuevos
	        	pstm.executeUpdate("insert into "+SqlConf.obtenerBase()+"inventario.productos_barras "
	    				+ "values (null, "+custid+", '"+codigoBarras+"', "+idProducto+")");
				
	        	
	        	respuesta = new JSONObject();
	        	respuesta.put("respuesta", "OK");
	        	
	        	con.commit();
	        	
	        	return respuesta.toString();
        	
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
			return "{\"respuesta\":\"ERROR\"}";
		}finally{
			
			if(rs != null)rs.close();
			if(pstm != null)pstm.close();

			if(con != null){
				//Regreso conexion
				ConnectionPool.getPool().releaseConnection(con);
			}

			con = null;
			pstm = null;
			rs = null;
			
		}
		
	}

}

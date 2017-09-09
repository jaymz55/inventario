package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import notificaciones.NotificacionError;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.UnidadMedidaDTO;

//@SuppressWarnings("unused")
public class UnidadMedidaDAO {

	//Variables
		//private String custid;
		private UnidadMedidaDTO unidadMedida;

		
	//Constructores

		
	//Metodos
		
		public UnidadMedidaDTO buscarUnidadMedida(String idUnidadMedida) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = "+idUnidadMedida);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		unidadMedida = new UnidadMedidaDTO();
	        		unidadMedida.setIdUnidadMedida(rs.getString("id"));
	        		unidadMedida.setNombre(rs.getString("nombre"));
	        	}
	        	
	        	return unidadMedida;
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				throw new RuntimeException(e);
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
		
		public Collection<UnidadMedidaDTO> buscarUnidadesMedidas() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<UnidadMedidaDTO> lista = new Vector<UnidadMedidaDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		//pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.material where custid = "+getCustid()+" and activo = 'SI' order by nombre");
        		pstm = con.prepareStatement("SELECT * FROM "+SqlConf.obtenerBase()+"inventario.unidad_medida order by id");

        		rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		unidadMedida = new UnidadMedidaDTO();
	        		unidadMedida.setIdUnidadMedida(rs.getString("id"));
	        		unidadMedida.setNombre(rs.getString("nombre"));
	        		
	        		lista.add(unidadMedida);
	        	}
	        	
	        	return lista;
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				throw new RuntimeException(e);
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

		public boolean registrarUnidadMedida(UnidadMedidaDTO unidadMedida) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.unidad_medida values (null, ");
				buffer.append(unidadMedida.getNombre()+")");
				
				pstm = con.prepareStatement(buffer.toString());
	        	respuesta = pstm.executeUpdate();
				
	        	if(respuesta == 1){
	        		return true;
	        	}else{
	        		return false;
	        	}
	        	
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				throw new RuntimeException(e);
			}finally{
				
				if(pstm != null)pstm.close();

				if(con != null){
					//Regreso conexion
					ConnectionPool.getPool().releaseConnection(con);
				}

				con = null;
				pstm = null;
				
				buffer = null;
				
			}

		}

		public boolean eliminarUnidadMedida(String idUnidadMedida) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				pstm = con.prepareStatement("delete from "+SqlConf.obtenerBase()+"inventario.unidad_medida where id = "+idUnidadMedida);
	        	respuesta = pstm.executeUpdate();
				
	        	if(respuesta == 1){
	        		return true;
	        	}else{
	        		return false;
	        	}
	        	
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				throw new RuntimeException(e);
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

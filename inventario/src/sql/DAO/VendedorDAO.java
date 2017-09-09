package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.vaadin.ui.UI;

import notificaciones.NotificacionError;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import sql.DTO.VendedorDTO;

public class VendedorDAO {

	//Variables
		private String custid;
		private VendedorDTO vendedor;
		UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
	
		
		
	//Getters...
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public VendedorDTO getVendedor() {
			return vendedor;
		}
		public void setVendedor(VendedorDTO vendedor) {
			this.vendedor = vendedor;
		}


	//Constructores
		public VendedorDAO(String custid){
			setCustid(custid);
		}
	
		
	//Metodos
		public Collection<VendedorDTO> buscarVendedores(String custid) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<VendedorDTO> lista = new Vector<VendedorDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.vendedores where "
        				+ "custid in ("+usuario.getCustidsRelacionados()+")");
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		vendedor = new VendedorDTO();
	        		vendedor.setIdVendedor(rs.getString("id"));
	        		vendedor.setCustid(rs.getString("custid"));
	        		vendedor.setNombre(rs.getString("nombre"));
	        		vendedor.setObservaciones(rs.getString("observaciones"));
	        		
	        		lista.add(vendedor);
	        	}
	        	
	        	return lista;
				
			}catch(Exception e){
				e.printStackTrace();
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
				
				lista = null;
				
			}

		}
		
		public boolean registrarVendedor(VendedorDTO vendedor) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.vendedores values (null, ");
				buffer.append(vendedor.getCustid()+", ");
				buffer.append((vendedor.getNombre())!= null?"'"+vendedor.getNombre()+"', ":"null, ");
				buffer.append((vendedor.getObservaciones())!= null?"'"+vendedor.getObservaciones()+"')":"null)");
				
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
				
				if(rs != null)rs.close();
				if(pstm != null)pstm.close();

				if(con != null){
					//Regreso conexion
					ConnectionPool.getPool().releaseConnection(con);
				}

				con = null;
				pstm = null;
				rs = null;
				
				buffer = null;
				
			}

		}

		public boolean eliminarVendedor(String idVendedor) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				pstm = con.prepareStatement("delete from "+SqlConf.obtenerBase()+"inventario.vendedores where id = "+idVendedor);
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

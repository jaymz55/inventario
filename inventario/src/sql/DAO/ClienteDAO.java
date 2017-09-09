package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.vaadin.ui.UI;

import funciones.Encriptar;
import notificaciones.NotificacionError;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.ClienteDTO;
import sql.DTO.UsuarioDTO;

public class ClienteDAO{

	//Variables
		private String custid;
		private ClienteDTO cliente;
		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
	
	//Getters y Setters
		public String getCustid() {
			return custid;
		}

		public void setCustid(String custid) {
			this.custid = custid;
		}
	
	//Constructores
		public ClienteDAO(String custid){
			setCustid(custid);
		}
	
	//Metodos
		
		public ClienteDTO buscarCliente(String idCliente) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			cliente = new ClienteDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select *, (select nombre from "+SqlConf.obtenerBase()+"inventario.vendedores where id = a.vendedor_asignado) as vendedor "
        										+ "from "+SqlConf.obtenerBase()+"inventario.clientes a "
        										+ "where custid in ("+usuario.getCustidsRelacionados()+") "
        												+ "and id = "+idCliente);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		cliente = new ClienteDTO();
	        		cliente.setIdCliente(rs.getString("id"));
	        		cliente.setCustid(rs.getString("custid"));
	        		cliente.setNombre(Encriptar.Desencriptar(rs.getString("nombre")));
	        		cliente.setTelefono(Encriptar.Desencriptar(rs.getString("telefono")));
	        		cliente.setCorreo(Encriptar.Desencriptar(rs.getString("correo")));
	        		cliente.setDireccion(Encriptar.Desencriptar(rs.getString("direccion")));
	        		cliente.setObservaciones(rs.getString("observaciones"));
	        		cliente.setVendedorAsignado(rs.getString("vendedor"));
	        		cliente.setIdVendedor(rs.getString("vendedor_asignado"));
	        		cliente.setActivo(rs.getString("activo"));
	        	}
	        	
	        	return cliente;
				
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
				
				cliente = null;
				
			}
			
		}
		
		public Collection<ClienteDTO> buscarClientes() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<ClienteDTO> lista = new Vector<ClienteDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * , (select nombre from "+SqlConf.obtenerBase()+"inventario.vendedores where id = a.vendedor_asignado) as vendedor "
        										+ "from "+SqlConf.obtenerBase()+"inventario.clientes a "
        												+ "where custid in ("+usuario.getCustidsRelacionados()+") "
        														+ "and activo = 'SI' order by nombre");
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		cliente = new ClienteDTO();
	        		cliente.setIdCliente(rs.getString("id"));
	        		cliente.setCustid(rs.getString("custid"));
	        		cliente.setNombre(Encriptar.Desencriptar(rs.getString("nombre")));
	        		cliente.setTelefono(Encriptar.Desencriptar(rs.getString("telefono")));
	        		cliente.setCorreo(Encriptar.Desencriptar(rs.getString("correo")));
	        		cliente.setDireccion(Encriptar.Desencriptar(rs.getString("direccion")));
	        		cliente.setObservaciones(rs.getString("observaciones"));
	        		cliente.setVendedorAsignado(rs.getString("vendedor"));
	        		cliente.setIdVendedor(rs.getString("vendedor_asignado"));
	        		cliente.setActivo(rs.getString("activo"));
	        		
	        		lista.add(cliente);
	        	}
				
	        	//Ordeno la lista
	        		Coleccion.sortClientes("nombre", lista);
	        	
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

		public boolean registrarCliente(ClienteDTO cliente) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.clientes values (null, ");
				buffer.append(cliente.getCustid()+", ");
				buffer.append((cliente.getNombre())!= null?"'"+Encriptar.Encriptar(cliente.getNombre())+"', ":"null, ");
				buffer.append((cliente.getTelefono())!= null?"'"+Encriptar.Encriptar(cliente.getTelefono())+"', ":"null, ");
				buffer.append((cliente.getCorreo())!= null?"'"+Encriptar.Encriptar(cliente.getCorreo())+"', ":"null, ");
				buffer.append((cliente.getDireccion())!= null?"'"+Encriptar.Encriptar(cliente.getDireccion())+"', ":"null, ");
				buffer.append((cliente.getObservaciones())!= null?"'"+cliente.getObservaciones()+"', ":"null, ");
				buffer.append((cliente.getVendedorAsignado())!= null?"'"+cliente.getVendedorAsignado()+"', ":"null, ");
				buffer.append("'SI')");
				
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

		public boolean actualizarCliente(ClienteDTO cliente) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("update "+SqlConf.obtenerBase()+"inventario.clientes set ");
				buffer.append("nombre = ");
				buffer.append((cliente.getNombre())!= null?"'"+Encriptar.Encriptar(cliente.getNombre())+"', ":"null, ");
				buffer.append("telefono = ");
				buffer.append((cliente.getTelefono())!= null?"'"+Encriptar.Encriptar(cliente.getTelefono())+"', ":"null, ");
				buffer.append("correo = ");
				buffer.append((cliente.getCorreo())!= null?"'"+Encriptar.Encriptar(cliente.getCorreo())+"', ":"null, ");
				buffer.append("direccion = ");
				buffer.append((cliente.getDireccion())!= null?"'"+Encriptar.Encriptar(cliente.getDireccion())+"', ":"null, ");
				buffer.append("observaciones = ");
				buffer.append((cliente.getObservaciones())!= null?"'"+cliente.getObservaciones()+"', ":"null, ");
				buffer.append("vendedor_asignado = ");
				buffer.append((cliente.getIdVendedor() == null || cliente.getIdVendedor().equals(""))?"null ":cliente.getIdVendedor()+" ");
				buffer.append("where id = "+cliente.getIdCliente());
				
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

		public boolean eliminarCliente(String idCliente) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				pstm = con.prepareStatement("update "+SqlConf.obtenerBase()+"inventario.clientes set activo = 'NO' where id = "+idCliente);
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

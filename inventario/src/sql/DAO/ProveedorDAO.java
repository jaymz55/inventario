package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import com.vaadin.ui.UI;

import funciones.Encriptar;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.ProveedorDTO;
import sql.DTO.UsuarioDTO;

public class ProveedorDAO{

	//Variables
		private String custid;
		private ProveedorDTO proveedor;
		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
	
	//Getters y Setters
		public String getCustid() {
			return custid;
		}

		public void setCustid(String custid) {
			this.custid = custid;
		}
	
	//Constructores
		public ProveedorDAO(String custid){
			setCustid(custid);
		}
	
	//Metodos
		
		public ProveedorDTO buscarProveedor(String idProveedor) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			proveedor = new ProveedorDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+usuario.getCustidsRelacionados()+") and id = "+idProveedor);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		proveedor = new ProveedorDTO();
	        		proveedor.setNombre(Encriptar.Desencriptar(rs.getString("nombre")));
	        		proveedor.setProducto(rs.getString("producto"));
	        		proveedor.setContacto(Encriptar.Desencriptar(rs.getString("contacto")));
	        		proveedor.setTelefono(Encriptar.Desencriptar(rs.getString("telefono")));
	        		proveedor.setCorreo(Encriptar.Desencriptar(rs.getString("correo")));
	        		proveedor.setPagina(rs.getString("pagina"));
	        		proveedor.setDireccion(Encriptar.Desencriptar(rs.getString("direccion")));
	        		proveedor.setObservaciones(rs.getString("observaciones"));
	        		proveedor.setActivo(rs.getString("activo"));
	        	}
	        	
	        	return proveedor;
				
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
				
			}
			
		}
		
		public Collection<ProveedorDTO> buscarProveedores() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<ProveedorDTO> lista = new Vector<ProveedorDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.proveedores where custid in ("+usuario.getCustidsRelacionados()+") and activo = 'SI' order by nombre");
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		proveedor = new ProveedorDTO();
	        		proveedor.setIdProveedor(rs.getString("id"));
	        		proveedor.setNombre(Encriptar.Desencriptar(rs.getString("nombre")));
	        		proveedor.setProducto(rs.getString("producto"));
	        		proveedor.setContacto(Encriptar.Desencriptar(rs.getString("contacto")));
	        		proveedor.setTelefono(Encriptar.Desencriptar(rs.getString("telefono")));
	        		proveedor.setCorreo(Encriptar.Desencriptar(rs.getString("correo")));
	        		proveedor.setPagina(rs.getString("pagina"));
	        		proveedor.setDireccion(Encriptar.Desencriptar(rs.getString("direccion")));
	        		proveedor.setObservaciones(rs.getString("observaciones"));
	        		proveedor.setActivo(rs.getString("activo"));
	        		
	        		lista.add(proveedor);
	        	}
				
	        	//Ordeno la lista
	        		Coleccion.sort("nombre", lista);
	        	
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
				
			}

		}

		public boolean registrarProveedor(ProveedorDTO proveedor) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.proveedores values (null, ");
				buffer.append(proveedor.getCustid()+", ");
				buffer.append((proveedor.getNombre())!= null?"'"+Encriptar.Encriptar(proveedor.getNombre())+"', ":"null, ");
				buffer.append((proveedor.getProducto())!= null?"'"+proveedor.getProducto()+"', ":"null, ");
				buffer.append((proveedor.getContacto())!= null?"'"+Encriptar.Encriptar(proveedor.getContacto())+"', ":"null, ");
				buffer.append((proveedor.getTelefono())!= null?"'"+Encriptar.Encriptar(proveedor.getTelefono())+"', ":"null, ");
				buffer.append((proveedor.getCorreo())!= null?"'"+Encriptar.Encriptar(proveedor.getCorreo())+"', ":"null, ");
				buffer.append((proveedor.getPagina())!= null?"'"+proveedor.getPagina()+"', ":"null, ");
				buffer.append((proveedor.getDireccion())!= null?"'"+Encriptar.Encriptar(proveedor.getDireccion())+"', ":"null, ");
				buffer.append((proveedor.getObservaciones())!= null?"'"+proveedor.getObservaciones()+"', ":"null, ");
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

		public boolean actualizarProveedor(ProveedorDTO proveedor) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("update "+SqlConf.obtenerBase()+"inventario.proveedores set ");
				buffer.append("nombre = ");
				buffer.append((proveedor.getNombre())!= null?"'"+Encriptar.Encriptar(proveedor.getNombre())+"', ":"null, ");
				buffer.append("producto = ");
				buffer.append((proveedor.getProducto())!= null?"'"+proveedor.getProducto()+"', ":"null, ");
				buffer.append("contacto = ");
				buffer.append((proveedor.getContacto())!= null?"'"+Encriptar.Encriptar(proveedor.getContacto())+"', ":"null, ");
				buffer.append("telefono = ");
				buffer.append((proveedor.getTelefono())!= null?"'"+Encriptar.Encriptar(proveedor.getTelefono())+"', ":"null, ");
				buffer.append("correo = ");
				buffer.append((proveedor.getCorreo())!= null?"'"+Encriptar.Encriptar(proveedor.getCorreo())+"', ":"null, ");
				buffer.append("pagina = ");
				buffer.append((proveedor.getPagina())!= null?"'"+proveedor.getPagina()+"', ":"null, ");
				buffer.append("direccion = ");
				buffer.append((proveedor.getDireccion())!= null?"'"+Encriptar.Encriptar(proveedor.getDireccion())+"', ":"null, ");
				buffer.append("observaciones = ");
				buffer.append((proveedor.getObservaciones())!= null?"'"+proveedor.getObservaciones()+"' ":"null ");
				buffer.append("where id = "+proveedor.getIdProveedor());
				
				pstm = con.prepareStatement(buffer.toString());
	        	respuesta = pstm.executeUpdate();
				
	        	if(respuesta == 1){
	        		return true;
	        	}else{
	        		return false;
	        	}
	        	
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
				
				buffer = null;
				
			}

		}

		public boolean eliminarProveedor(String idProveedor) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				pstm = con.prepareStatement("update "+SqlConf.obtenerBase()+"inventario.proveedores set activo = 'NO' where id = "+idProveedor);
	        	respuesta = pstm.executeUpdate();
				
	        	if(respuesta == 1){
	        		return true;
	        	}else{
	        		return false;
	        	}
	        	
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
				
			}

		}


		
}

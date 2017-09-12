package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.ProductoDTO;

public class ProductoDAO {

	//Variables
		private String custid;
		private ProductoDTO producto;
	
	//Getters y Setters
		public String getCustid() {
			return custid;
		}

		public void setCustid(String custid) {
			this.custid = custid;
		}
	
	//Constructores
		public ProductoDAO(String custid){
			setCustid(custid);
		}
	
	//Metodos
		
		public ProductoDTO buscarProducto(String idProducto) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			ProductoDTO producto = new ProductoDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.productos where custid = "+getCustid()+" and id = "+idProducto);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		producto = new ProductoDTO();
	        		producto.setNombre(rs.getString("nombre"));
	        		producto.setPrecio(rs.getString("precio"));
	        		producto.setIva(rs.getString("iva"));
	        		producto.setTotal(rs.getString("total"));
	        		producto.setDescripcion(rs.getString("descripcion"));
	        		producto.setGrava_iva(rs.getString("grava_iva"));
	        		producto.setMinimo(rs.getString("minimo"));
	        		producto.setMaximo(rs.getString("maximo"));
	        		producto.setActivo(rs.getString("activo"));
	        	}
	        	
	        	return producto;
				
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
		
		public Collection<ProductoDTO> buscarProductos() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			//Vector<ProductoDTO> lista = new Vector<ProductoDTO>();
			List<ProductoDTO> lista = new ArrayList<>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.productos where custid = "+getCustid()+" and activo = 'SI' order by nombre");
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		producto = new ProductoDTO();
	        		producto.setIdProducto(rs.getString("id"));
	        		producto.setNombre(rs.getString("nombre"));
	        		producto.setPrecio(rs.getString("precio"));
	        		producto.setIva(rs.getString("iva"));
	        		producto.setTotal(rs.getString("total"));
	        		producto.setDescripcion(rs.getString("descripcion"));
	        		producto.setGrava_iva(rs.getString("grava_iva"));
	        		producto.setMinimo(rs.getString("minimo"));
	        		producto.setMaximo(rs.getString("maximo"));
	        		producto.setActivo(rs.getString("activo"));
	        		
	        		lista.add(producto);
	        	}
				
	        	//Ordeno la lista
	        		Coleccion.sortProducto("nombre", lista);
	        	
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

		public boolean registrarProducto(ProductoDTO producto) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuffer buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuffer();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.productos values (null, ");
				buffer.append(producto.getCustid()+", ");
				buffer.append((producto.getNombre())!= null?"'"+producto.getNombre()+"', ":"null, ");
				buffer.append((producto.getPrecio())!= null?"'"+producto.getPrecio()+"', ":"null, ");
				buffer.append((producto.getIva())!= null?"'"+producto.getIva()+"', ":"null, ");
				buffer.append((producto.getTotal())!= null?"'"+producto.getTotal()+"', ":"null, ");
				buffer.append((producto.getDescripcion())!= null?"'"+producto.getDescripcion()+"', ":"null, ");
				buffer.append((producto.getGrava_iva())!= null?"'"+producto.getGrava_iva()+"', ":"null, ");
				buffer.append((producto.getMinimo())!= null?"'"+producto.getMinimo()+"', ":"null, ");
				buffer.append((producto.getMaximo())!= null?"'"+producto.getMaximo()+"', ":"null, ");
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

		public double existenciaProducto(String custid, String idProducto) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			UsuarioDAO dao = new UsuarioDAO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select sum(cantidad) as cantidad from "+SqlConf.obtenerBase()+"inventario.almacen where custid in ("
						+ dao.obtenerCustidsRelacionados(custid)
						+ ") and id_producto = "
						+ idProducto
						+ " and activo = 'SI'");
	        	rs = pstm.executeQuery();
	        	
	        	rs.next();
	        	
	        	return rs.getDouble("cantidad");
				
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
				
				//Variables locales
					dao = null;
				
			}
			
		}
		
		public double obtenerPrecioProducto(String custid, String idProducto) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			UsuarioDAO dao = new UsuarioDAO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select total from "+SqlConf.obtenerBase()+"inventario.productos where custid in ("
						+ dao.obtenerCustidsRelacionados(custid)
						+ ") and id = "
						+ idProducto
						+ " and activo = 'SI'");
	        	rs = pstm.executeQuery();
	        	
	        	rs.next();
	        	
	        	return rs.getDouble("total");
				
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
				
				//Variables locales
					dao = null;
				
			}
			
		}
		
		public String obtenerGravaIva(String custid, String idProducto) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			UsuarioDAO dao = new UsuarioDAO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where custid in ("
						+ dao.obtenerCustidsRelacionados(custid)
						+ ") and id = "
						+ idProducto
						+ " and activo = 'SI'");
	        	rs = pstm.executeQuery();
	        	
	        	rs.next();
	        	
	        	return rs.getString("grava_iva");
				
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
				
				//Variables locales
					dao = null;
				
			}
			
		}
		
		
}

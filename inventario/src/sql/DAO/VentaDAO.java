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
import sql.DTO.VentaDTO;

public class VentaDAO {

	//Variables
		private String custid;
		private VentaDTO venta;
		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
	
		
		
	//Getters...
		public String getCustid() {
			return custid;
		}
		public void setCustid(String custid) {
			this.custid = custid;
		}
		public VentaDTO getVenta() {
			return venta;
		}
		public void setVenta(VentaDTO venta) {
			this.venta = venta;
		}


	//Constructores
		public VentaDAO(String custid){
			setCustid(custid);
		}
	
		
	//Metodos
		public Collection<VentaDTO> buscarVentas(String fechaInicial, String fechaFinal) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<VentaDTO> lista = new Vector<VentaDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select *, " +
        										"(select nombre from "+SqlConf.obtenerBase()+"inventario.productos where custid in ("+usuario.getCustidsRelacionados()+") and id = a.id_producto) as producto_nombre, "+
        										"(select nombre from "+SqlConf.obtenerBase()+"inventario.clientes where custid in ("+usuario.getCustidsRelacionados()+") and id = a.id_cliente) as cliente_nombre "+
        									"from "+SqlConf.obtenerBase()+"inventario.ventas a "+
        									"where custid in ("+usuario.getCustidsRelacionados()+") and fecha between '"+fechaInicial+"' and '"+fechaFinal+"' " +
        									"order by fecha");
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		venta = new VentaDTO();
	        		venta.setIdVenta(rs.getString("id"));
	        		venta.setCustid(rs.getString("custid"));
	        		venta.setIdProducto(rs.getString("id_producto"));
	        		venta.setProductoNombre(rs.getString("producto_nombre"));
	        		venta.setIdCliente(rs.getString("id_cliente"));
	        		venta.setClienteNombre(rs.getString("cliente_nombre"));
	        		venta.setPrecio(rs.getDouble("precio"));
	        		venta.setIva(rs.getDouble("iva"));
	        		venta.setTotal(rs.getDouble("total"));
	        		venta.setDescuento(rs.getDouble("descuento"));
	        		venta.setDescuentoDescripcion(rs.getString("descuento_desc"));
	        		venta.setFecha(rs.getString("fecha"));
	        		venta.setCantidad(rs.getInt("cantidad"));
	        		venta.setVendedor(rs.getString("vendedor"));
	        		venta.setVendedor(rs.getString("vendedor"));
	        		venta.setGravaIva(rs.getString("grava_iva"));
	        		venta.setComentarios(rs.getString("comentarios"));
	        		venta.setPagada(rs.getString("pagada"));
	        		venta.setAfectaAlmacen(rs.getString("afecta_almacen"));
	        		venta.setMovimiento(rs.getString("movimiento"));
	        		
	        		lista.add(venta);
	        	}
				
	        	//Ordeno la lista
	        		//Coleccion.sort("nombre", lista);
	        	
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
		
		public boolean registrarVenta(VentaDTO venta){
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder builder;
			
			try{
				
				//Esta conexion debe ser transaccion
				con = ConnectionPool.getPool().getConnection();
				con.setAutoCommit(false);
				
				builder = new StringBuilder();
				
				builder.append("insert into "+SqlConf.obtenerBase()+"inventario.ventas values (null, ");
				builder.append(venta.getCustid()+", ");
				builder.append((venta.getIdProducto())!= null?venta.getIdProducto()+", ":"null, ");
				builder.append((venta.getIdCliente())!= null?venta.getIdCliente()+", ":"null, ");
				builder.append(venta.getPrecio()+", ");
				builder.append(venta.getIva()+", ");
				builder.append(venta.getTotal()+", ");
				builder.append(venta.getDescuento()+", ");
				builder.append((venta.getDescuentoDescripcion())!= null?"'"+venta.getDescuentoDescripcion()+"', ":"null, ");
				builder.append("'"+venta.getFecha()+"', ");
				builder.append(venta.getCantidad()+", ");
				builder.append((venta.getVendedor())!= null?"'"+venta.getVendedor()+"', ":"null, ");
				builder.append("'"+venta.getGravaIva()+"', ");
				builder.append((venta.getComentarios())!= null?"'"+venta.getComentarios()+"', ":"null, ");
				builder.append("'"+venta.getPagada()+"', ");
				builder.append("'"+venta.getAfectaAlmacen()+"', ");
				builder.append("'"+venta.getMovimiento()+"')");
				
				pstm = con.prepareStatement(builder.toString());
	        	respuesta = pstm.executeUpdate();
				
	        	if(respuesta == 1){
	        		
					//Reviso si afecta a almacen
					if(venta.getAfectaAlmacen().equals("SI")){
						
						//Llamo a afectar almacen ******FALTA
						
					}
					
	        	}
	        	
	        //Cierro commit
	        	con.commit();
				return true;
			}catch(Exception e){
			//Deshago conexion
				try {
					con.rollback();
				} catch (SQLException e1) {
					NotificacionError.mostrar(e1.toString(), 5000);
					e1.printStackTrace();
				}
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
				return false;
			}finally{
				
				try {
					if(rs != null)rs.close();
					if(pstm != null)pstm.close();
				} catch (SQLException e) {
					e.printStackTrace();
					NotificacionError.mostrar(e.toString(), 5000);
				}
				
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

package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Vector;

import funciones.Encriptar;
import funciones.Funcion;
import notificaciones.NotificacionError;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.MaterialDTO;
import sql.DTO.UsuarioDTO;

public class MaterialDAO {

	//Variables
		private String custid;
		@SuppressWarnings("unused")
		private String codigoBarras;
		private MaterialDTO material;

		
	//Constructores
		public MaterialDAO(String custid){
			
			this.custid = custid;
			/*try {
				
				this.usuario = facade.obtenerUsuarioApi(custid);
			
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
		}

		public MaterialDAO(String custid, String codigoBarras){
			this.custid = custid;
			this.codigoBarras = codigoBarras;
		}
		
	//Metodos
		
		public MaterialDTO buscarMaterial(String idMaterial) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			material = new MaterialDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select *, (select barcode from "+SqlConf.obtenerBase()+"inventario.material_barras where id_material = a.id) as codigo from "+SqlConf.obtenerBase()+"inventario.material a where a.custid in ("+custid+") and a.id = "+idMaterial);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdMaterial(rs.getString("id"));
	        		material.setCategoria(rs.getString("categoria"));
	        		material.setNombre(rs.getString("nombre"));
	        		material.setSku(rs.getString("sku"));
	        		material.setMinimo(rs.getString("minimo"));
	        		material.setMaximo(rs.getString("maximo"));
	        		material.setProveedor(rs.getString("proveedor"));
	        		material.setUnidadMedida(rs.getString("unidad_medida"));
	        		material.setActivo(rs.getString("activo"));
	        		material.setBarCode(rs.getString("codigo"));
	        	}
	        	
	        	return material;
				
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
		
		public Collection<MaterialDTO> buscarMateriales() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<MaterialDTO> lista = new Vector<MaterialDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		//pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.material where custid = "+getCustid()+" and activo = 'SI' order by nombre");
        		pstm = con.prepareStatement("SELECT id, categoria, nombre, sku, (select nombre from "+SqlConf.obtenerBase()+"inventario.proveedores where id = a.proveedor) as proveedor, minimo, maximo, unidad_medida FROM "+SqlConf.obtenerBase()+"inventario.material a FORCE INDEX (custid) where a.custid in ("+custid+") and activo = 'SI' group by a.categoria, a.nombre order by a.categoria, a.nombre");
	        	
        		
        		
        		rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdMaterial(rs.getString("id"));
	        		material.setCategoria(rs.getString("categoria"));
	        		material.setNombre(rs.getString("nombre"));
	        		material.setSku(rs.getString("sku"));
	        		material.setMinimo(Funcion.decimales(rs.getString("minimo")));
	        		material.setMaximo(Funcion.decimales(rs.getString("maximo")));
	        		material.setProveedor(Encriptar.Desencriptar(rs.getString("proveedor")));
	        		material.setUnidadMedida(rs.getString("unidad_medida"));
	        		//material.setActivo(rs.getString("activo"));
	        		
	        		lista.add(material);
	        	}
				
	        	//Ordeno la lista
	        		//Coleccion.sortMaterial("categoria", lista);
	        	
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

		public boolean registrarMaterial(MaterialDTO material) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.material values (null, ");
				buffer.append(material.getCustid()+", ");
				buffer.append((material.getCategoria())!= null?"'"+material.getCategoria()+"', ":"null, ");
				buffer.append((material.getNombre())!= null?"'"+material.getNombre()+"', ":"null, ");
				buffer.append((material.getSku())!= null?"'"+material.getSku()+"', ":"null, ");
				buffer.append((!material.getMinimo().equals(""))?material.getMinimo()+", ":"null, ");
				buffer.append((!material.getMaximo().equals(""))?material.getMaximo()+", ":"null, ");
				buffer.append((material.getProveedor())!= null?material.getProveedor()+", ":"null, ");
				buffer.append((material.getUnidadMedida())!= null?"'"+material.getUnidadMedida()+"', ":"null, ");
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

		public boolean actualizarMaterial(MaterialDTO material) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("update "+SqlConf.obtenerBase()+"inventario.material set ");
				buffer.append("categoria = ");
				buffer.append((material.getCategoria())!= null?"'"+material.getCategoria()+"', ":"null, ");
				buffer.append("nombre = ");
				buffer.append((material.getNombre())!= null?"'"+material.getNombre()+"', ":"null, ");
				buffer.append("sku = ");
				buffer.append((material.getSku())!= null?"'"+material.getSku()+"', ":"null, ");
				buffer.append("minimo = ");
				buffer.append((material.getMinimo())!= null?"'"+material.getMinimo()+"', ":"null, ");
				buffer.append("maximo = ");
				buffer.append((material.getMaximo())!= null?"'"+material.getMaximo()+"', ":"null, ");
				buffer.append("proveedor = ");
				buffer.append((material.getProveedor())!= null?"'"+material.getProveedor()+"', ":"null, ");
				buffer.append("unidad_medida = ");
				buffer.append((material.getUnidadMedida())!= null?"'"+material.getUnidadMedida()+"' ":"null ");
				buffer.append("where id = "+material.getIdMaterial());
				
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
		
		public boolean eliminarMaterial(String idMaterial) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				pstm = con.prepareStatement("update "+SqlConf.obtenerBase()+"inventario.material set activo = 'NO' where id = "+idMaterial);
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
		
		public MaterialDTO buscarBarCode(String barCode) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			material = new MaterialDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT id FROM material where id = (select id_material from material_barras where custid in ("+custid+") and barcode = '"+barCode+"')");
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = buscarMaterial(rs.getString("id"));
	        	}
	        	
	        	return material;
				
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

		public boolean registrarCodigo(String idMaterial, String barCode){
			
			//Variables
			
			Connection con = null;
			Statement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				con.setAutoCommit(false);
				
				pstm = con.createStatement();
				
					pstm.executeUpdate("delete from "+SqlConf.obtenerBase()+"inventario.material_barras where custid = "+custid+" and id_material = "+idMaterial);
					pstm.executeUpdate("delete from "+SqlConf.obtenerBase()+"inventario.material_barras where custid = "+custid+" and barcode = "+barCode);
					pstm.executeUpdate("insert into "+SqlConf.obtenerBase()+"inventario.material_barras values (null, "+custid+", '"+barCode+"', "+idMaterial+")");

				con.commit();
				con.setAutoCommit(true);
				return true;
	        	
			}catch(Exception e){
				e.printStackTrace();
				try {
					con.rollback();
					con.setAutoCommit(true);
					return false;
				} catch (SQLException e1) {
					e1.printStackTrace();
					return false;
				}
				
			}finally{
				
				try{
					if(rs != null)rs.close();
					if(pstm != null)pstm.close();
				}catch(Exception e){
					e.printStackTrace();
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

		public boolean registrarEntradaMaterial(MaterialDTO material) throws SQLException{
			
			//Variables
				Connection con = null;
				PreparedStatement pstm = null;
				int respuesta;
				
				try{
					
					//Lleno Proveedor
					material = obtenerProveedorDeMaterial(material);
					
					con = ConnectionPool.getPool().getConnection();
					
					pstm = con.prepareStatement("insert into "+SqlConf.obtenerBase()+"inventario.inventario values (null, "+
									material.getCustid() + ", "+material.getCantidad()+", "+material.getCosto()+", "+material.getIva()+", " +
									material.getTotal()+", null, '"+material.getFechaIngreso()+"',"+(material.getFechaCaducidad() != null?"'"+material.getFechaCaducidad()+"',":"null,")+
									material.getIdMaterial()+", "+(material.getIdProveedor() != null?material.getIdProveedor()+",":"null,")+(material.getNombreProveedor() != null?"'"+material.getNombreProveedor()+"',":"null,")+"'"+
									material.getContieneIva()+"','ENTRADA',null)");
		        	
					respuesta = pstm.executeUpdate();
		        	
		        	if(respuesta == 1){
		        		return true;
		        	}else{
		        		return false;
		        	}
					
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(pstm != null)pstm.close();

					if(con != null){
						//Regreso conexion
						ConnectionPool.getPool().releaseConnection(con);
					}

					con = null;
					pstm = null;
					
				}
			
			
			return false;
		}

		public boolean actualizarEntradaMaterial(String custid, String idMaterial, String fechaIngreso, 
				String fechaCaducidad, String cantidad, String costoTotal, String contieneIva, String idInventario) throws SQLException{
			
			//Variables
				Connection con = null;
				PreparedStatement pstm = null;
				int respuesta;
				StringBuilder buffer;
				
				try{
					
					con = ConnectionPool.getPool().getConnection();
					buffer = new StringBuilder();
					
					//Lleno MaterialDTO 
					material = new MaterialDTO();
					material.setIdEntradaInventario(idInventario);
					material.setCustid(custid);
					material.setIdMaterial(idMaterial);
					material.setFechaIngreso(fechaIngreso);
					material.setFechaCaducidad(fechaCaducidad);
					material.setContieneIva(contieneIva);
					material.setCantidad(cantidad);
					material.setTotal(Double.parseDouble(costoTotal));
					
					
					con = ConnectionPool.getPool().getConnection();

					buffer.append("update "+SqlConf.obtenerBase()+"inventario.inventario set ");
					buffer.append("unidades = ");
					buffer.append((material.getCantidad())!= null?material.getCantidad()+", ":"null, ");
					buffer.append("costo = ");
					buffer.append(material.getCosto()+", ");
					buffer.append("iva = ");
					buffer.append(material.getIva()+", ");
					buffer.append("total = ");
					buffer.append(material.getTotal()+", ");
					buffer.append("fecha = ");
					buffer.append("'"+material.getFechaIngreso()+"', ");
					buffer.append("caducidad = ");
					buffer.append((material.getFechaCaducidad())!= null?"'"+material.getFechaCaducidad()+"', ":"null, ");
					buffer.append("contiene_iva = ");
					buffer.append((material.getContieneIva())!= null?"'"+material.getContieneIva()+"' ":"null ");
					buffer.append("where id = "+material.getIdEntradaInventario());
					
					pstm = con.prepareStatement(buffer.toString());
		        	respuesta = pstm.executeUpdate();
		        	
		        	if(respuesta == 1){
		        		return true;
		        	}else{
		        		return false;
		        	}
					
				}catch(Exception e){
					e.printStackTrace();
					NotificacionError.mostrar(e.toString(), 5000);
				}finally{
					if(pstm != null)pstm.close();

					if(con != null){
						//Regreso conexion
						ConnectionPool.getPool().releaseConnection(con);
					}

					con = null;
					pstm = null;
					
				}
			
			
			return false;
		}

		public MaterialDTO obtenerEntradaMaterial(String idEntradaInventario) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			material = new MaterialDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.inventario a "
        				+ "where a.custid in ("+custid+") and a.id = "+idEntradaInventario);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdEntradaInventario(rs.getString("id"));
	        		material.setCantidad(Funcion.decimales(rs.getString("unidades")));
	        		//material.setCosto(rs.getDouble("costo"));
	        		//material.setIva(rs.getDouble("iva"));
	        		material.setContieneIva(rs.getString("contiene_iva"));
	        		material.setTotal(rs.getDouble("total"));
	        		material.setFechaIngreso(rs.getString("fecha"));
	        		material.setFechaCaducidad(rs.getString("caducidad"));
	        		material.setIdMaterial(rs.getString("id_material"));
	        		material.setMovimiento(rs.getString("movimiento"));
	        	}
	        	
	        	return material;
				
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
		
		public MaterialDTO obtenerProveedorDeMaterial(MaterialDTO material) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT proveedor, (select nombre from proveedores where id = a.proveedor) as nombre\r\n" + 
        				"FROM material a\r\n" + 
        				"where custid in ("+custid+")\r\n" + 
        				"and id = "+material.getIdMaterial());
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material.setIdProveedor(rs.getString("proveedor"));
	        		material.setNombreProveedor(rs.getString("nombre"));
	        	}
	        	
	        	return material;
				
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
		
		
	//Metodos de inventario
		public Collection<MaterialDTO> buscarMaterialesPorFecha(String fechaInicial, String fechaFinal) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<MaterialDTO> lista = new Vector<MaterialDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT\r\n" + 
    					"  id,\r\n" + 
    					"  ifnull((select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material),'-') as categoria,\r\n" + 
    					"  (select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre,\r\n" + 
    					"  ifnull((select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material),'-') as sku,\r\n" + 
    					"  proveedor,\r\n" + 
    					"  unidades,\r\n" + 
    					//"  round(costo * unidades,2) as costo,\r\n" + 
    					//"  round(iva * unidades,2) as iva,\r\n" + 
    					"  ifnull(round(total * unidades,2),0) as total,\r\n" + 
    					"  movimiento,\r\n" + 
    					"  fecha,\r\n" + 
    					"  ifnull(contiene_iva, 'NO') as contiene_iva,\r\n" + 
    					"  caducidad\r\n" + 
    					"FROM "+SqlConf.obtenerBase()+"inventario.inventario a FORCE INDEX (custid)\r\n" + 
    					"where a.custid in ("+custid+")\r\n" + 
    					"  and fecha between '"+fechaInicial+"' and '"+fechaFinal+"'\r\n" + 
    					"order by a.fecha, a.id");
	        	
        		rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdMaterial(rs.getString("id"));
	        		material.setCategoria(rs.getString("categoria"));
	        		material.setNombre(rs.getString("nombre"));
	        		material.setSku(rs.getString("sku"));
	        		material.setProveedor(Encriptar.Desencriptar(rs.getString("proveedor")));
	        		material.setCantidad(Funcion.decimales(rs.getString("unidades")));
	        		material.setContieneIva(rs.getString("contiene_iva"));
	        		//material.setCosto(Double.parseDouble(rs.getString("costo")));
	        		//material.setIva(Double.parseDouble(rs.getString("iva")));
	        		material.setTotal(rs.getDouble("total"));
	        		material.setMovimiento(rs.getString("movimiento"));
	        		material.setFechaIngreso(rs.getString("fecha"));
	        		material.setFechaCaducidad(rs.getString("caducidad"));
	        		
	        		lista.add(material);
	        	}
				
	        	//Ordeno la lista
	        		//Coleccion.sortMaterial("categoria", lista);
	        	
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

		public Collection<MaterialDTO> buscarMaterialesConsolidado() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<MaterialDTO> lista = new Vector<MaterialDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT id, "
        				+ "(select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as categoria, "
    					+ "(select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre, "
    					+ "(select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as sku, "
    					+ "(select ifnull(minimo,0) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as minimo, "
    					+ "(select ifnull(maximo,0) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as maximo, "
    					+ "sum(unidades) as existencia, "
    					+ "(sum(unidades)-(select minimo)) as diferencial "
    					+ "FROM "+SqlConf.obtenerBase()+"inventario.inventario a FORCE INDEX (custid) "
    					+ "where a.custid in ("+custid+") group by id_material order by diferencial");
	        	
        		rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdMaterial(rs.getString("id"));
	        		material.setCategoria(rs.getString("categoria"));
	        		material.setNombre(rs.getString("nombre"));
	        		material.setSku(rs.getString("sku"));
	        		
	        		material.setMinimo(rs.getString("minimo"));
	        		material.setMaximo(rs.getString("maximo"));
	        		material.setExistencia(rs.getString("existencia"));
	        		material.setDiferencial(rs.getString("diferencial"));
	        		
	        		lista.add(material);
	        	}
				
	        	//Ordeno la lista
	        		//Coleccion.sortMaterial("categoria", lista);
	        	
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
		
		public Collection<MaterialDTO> buscarMaterialesConsolidadoEspecifico(String idMaterial) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<MaterialDTO> lista = new Vector<MaterialDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT id, "
        				+ "(select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as categoria, "
    					+ "(select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre, "
    					+ "(select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as sku, "
    					+ "(select ifnull(minimo,0) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as minimo, "
    					+ "(select ifnull(maximo,0) from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as maximo, "
    					+ "sum(unidades) as existencia, "
    					+ "(sum(unidades)-(select minimo)) as diferencial "
    					+ "FROM "+SqlConf.obtenerBase()+"inventario.inventario a FORCE INDEX (custid) where a.custid in ("+custid+") and id_material = "+idMaterial);
	        	
        		rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdMaterial(rs.getString("id"));
	        		material.setCategoria(rs.getString("categoria"));
	        		material.setNombre(rs.getString("nombre"));
	        		material.setSku(rs.getString("sku"));
	        		
	        		material.setMinimo(rs.getString("minimo"));
	        		material.setMaximo(rs.getString("maximo"));
	        		material.setExistencia(rs.getString("existencia"));
	        		material.setDiferencial(rs.getString("diferencial"));
	        		
	        		lista.add(material);
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

		public Collection<MaterialDTO> buscarUltimosMovimientosEspecifico(String idMaterial) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			Vector<MaterialDTO> lista = new Vector<MaterialDTO>();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT *, "
        				+ "(select categoria from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as categoria, "
    					+ "(select nombre from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as nombre, "
    					+ "(select sku from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+custid+") and id = a.id_material) as sku "
    					+ "FROM "+SqlConf.obtenerBase()+"inventario.inventario a FORCE INDEX (custid) where a.custid in ("+custid+") and id_material = "+idMaterial);
	        	
        		rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdMaterial(rs.getString("id"));
	        		material.setCategoria(rs.getString("categoria"));
	        		material.setNombre(rs.getString("nombre"));
	        		material.setSku(rs.getString("sku"));
	        		
	        		material.setMinimo(rs.getString("minimo"));
	        		material.setMaximo(rs.getString("maximo"));
	        		material.setExistencia(rs.getString("existencia"));
	        		material.setDiferencial(rs.getString("diferencial"));
	        		
	        		lista.add(material);
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
		
		
	//Metodos de merma
		public boolean registrarMerma(MaterialDTO material) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("insert into "+SqlConf.obtenerBase()+"inventario.inventario values (null, ");
				buffer.append(material.getCustid()+", ");
				buffer.append(material.getCantidad()+", ");
				buffer.append("null, ");
				buffer.append("null, ");
				buffer.append("null, ");
				buffer.append("null, ");
				buffer.append("'"+material.getFechaMerma()+"', ");
				buffer.append("null, ");
				buffer.append(material.getIdMaterial()+", ");
				buffer.append("null, ");
				buffer.append("null, ");
				buffer.append("null, ");
				buffer.append("'"+material.getMovimiento()+"', ");
				buffer.append("null)");
				
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

		public boolean actualizarMerma(MaterialDTO material) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			StringBuilder buffer;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				buffer = new StringBuilder();
				
				buffer.append("update "+SqlConf.obtenerBase()+"inventario.inventario set ");
				buffer.append("id_material = ");
				buffer.append(material.getIdMaterial()+", ");
				buffer.append("unidades = ");
				buffer.append(material.getCantidad()+", ");
				buffer.append("fecha = ");
				buffer.append("'"+material.getFechaMerma()+"' ");
				buffer.append("where id = "+material.getIdEntradaInventario());
				
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
		
		public boolean eliminarMerma(String idMerma) throws SQLException{
			
			//Variables
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				pstm = con.prepareStatement("delete from "+SqlConf.obtenerBase()+"inventario.inventario where id = "+idMerma);
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
		
		public MaterialDTO obtenerMerma(String idMermaInventario) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			material = new MaterialDTO();
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select * from "+SqlConf.obtenerBase()+"inventario.inventario a "
        				+ "where a.custid in ("+custid+") and a.id = "+idMermaInventario);
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material = new MaterialDTO();
	        		material.setIdEntradaInventario(rs.getString("id"));
	        		material.setCantidad(Funcion.decimales(rs.getString("unidades")));
	        		material.setIdMaterial(rs.getString("id_material"));
	        		material.setFechaMerma(rs.getString("fecha"));
	        	}
	        	
	        	return material;
				
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
		
	//Metodos para costo
		public MaterialDTO obtenerCostoMaterial(UsuarioDTO usuario, MaterialDTO material) throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT\r\n" + 
        				"total,\r\n" + 
        				"contiene_iva,\r\n" + 
        				"(select nombre from unidad_medida where id = (select unidad_medida from "+SqlConf.obtenerBase()+"inventario.material where custid in ("+usuario.getCustidsRelacionados()+") and id = a.id_material)) as unidadMedida " + 
        				"FROM "+SqlConf.obtenerBase()+"inventario.inventario a\r\n" + 
        				"where a.custid in ("+custid+") and a.id_material = "+material.getIdMaterial()+"\r\n" + 
        				"and a.movimiento = 'ENTRADA'\r\n" + 
        				"order by fecha desc\r\n" + 
        				"limit 1");
        		
	        	rs = pstm.executeQuery();
	        	
	        	while(rs.next()){
	        		material.setContieneIva(rs.getString("contiene_iva"));
	        		material.setTotal(Double.parseDouble(rs.getString("total")));
	        		material.setUnidadMedidaNombre(rs.getString("unidadMedida"));
	        	}
	        	
	        	return material;
				
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

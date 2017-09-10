package facade;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import javax.mail.MessagingException;

import org.json.JSONException;

import com.vaadin.ui.UI;

import correos.nuevo.Correo;
import notificaciones.NotificacionError;
import sql.DAO.ClienteDAO;
import sql.DAO.CodigoBarrasDAO;
import sql.DAO.MaterialDAO;
import sql.DAO.ProductoDAO;
import sql.DAO.ProveedorDAO;
import sql.DAO.SolicitudDAO;
import sql.DAO.UnidadMedidaDAO;
import sql.DAO.UsuarioDAO;
import sql.DAO.VendedorDAO;
import sql.DAO.VentaDAO;
import sql.DTO.ClienteDTO;
import sql.DTO.MaterialDTO;
import sql.DTO.ProductoDTO;
import sql.DTO.ProveedorDTO;
import sql.DTO.UnidadMedidaDTO;
import sql.DTO.UsuarioDTO;
import sql.DTO.VendedorDTO;
import sql.DTO.VentaDTO;

public class Facade {

	//Metodos de acceso a usuarios
		public UsuarioDTO obtenerUsuarioSesion(String correo) throws SQLException{
			UsuarioDAO usuario = new UsuarioDAO(correo);
			return usuario.getUsuario();
		}
	
		public UsuarioDTO verificarUsuario(String correo, String password) throws SQLException{
			UsuarioDAO usuario = new UsuarioDAO(correo, password);
			return usuario.getUsuario();
		}
		
		//Para APIS
			public String obtenerCustidsRelacionados(String custid) throws SQLException{
			UsuarioDAO usuario = new UsuarioDAO();
			return usuario.obtenerCustidsRelacionados(custid);
		}
		
	
	//Metodos de proveedores
		public boolean registrarProveedor(ProveedorDTO proveedor) throws SQLException{
			
			UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			ProveedorDAO dao = new ProveedorDAO(usuario.getCustid());
			
			if(dao.registrarProveedor(proveedor)){
				return true;
			}else{
				return false;
			}
			
		}
		
		public boolean actualizarProveedor(ProveedorDTO proveedor) throws SQLException{
			
			UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			ProveedorDAO dao = new ProveedorDAO(usuario.getCustid());
			
			if(dao.actualizarProveedor(proveedor)){
				return true;
			}else{
				return false;
			}
			
		}
		
		public Collection<ProveedorDTO> obtenerProveedores(String custid) throws SQLException{
			
			ProveedorDAO dao = new ProveedorDAO(custid);
			return dao.buscarProveedores();
			
		}
		
		public ProveedorDTO obtenerProveedor(String custid, String idProveedor) throws SQLException{
			
			ProveedorDAO dao = new ProveedorDAO(custid);
			return dao.buscarProveedor(idProveedor);
			
		}
		
		public boolean eliminarProveedor(String custid, String idProveedor) throws SQLException{
			
			ProveedorDAO dao = new ProveedorDAO(custid);
			return dao.eliminarProveedor(idProveedor);
			
		}
	
		//Metodos Hibernate
			public boolean registrarProveedorHibernate(ProveedorDTO proveedor) throws SQLException{
				
				UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
				ProveedorDAO dao = new ProveedorDAO(usuario.getCustid());
				
				if(dao.registrarProveedor(proveedor)){
					return true;
				}else{
					return false;
				}
				
			}
		
	//Metodo de clientes	
		public ClienteDTO obtenerCliente(String custid, String idCliente) throws SQLException{
			
			ClienteDAO dao = new ClienteDAO(custid);
			return dao.buscarCliente(idCliente);
			
		}

		public Collection<ClienteDTO> obtenerClientes(String custid) throws SQLException{
			
			ClienteDAO dao = new ClienteDAO(custid);
			return dao.buscarClientes();
			
		}
		
		public boolean registrarCliente(ClienteDTO cliente) throws SQLException{
			
			UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			ClienteDAO dao = new ClienteDAO(usuario.getCustid());
			
			if(dao.registrarCliente(cliente)){
				return true;
			}else{
				return false;
			}
			
		}
		
		public boolean actualizarCliente(ClienteDTO cliente) throws SQLException{
			
			UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
			ClienteDAO dao = new ClienteDAO(usuario.getCustid());
			
			if(dao.actualizarCliente(cliente)){
				return true;
			}else{
				return false;
			}
			
		}
		
		public boolean eliminarCliente(String custid, String idCliente) throws SQLException{
			
			ClienteDAO dao = new ClienteDAO(custid);
			return dao.eliminarCliente(idCliente);
			
		}
	
		
	//Metodos de material
		public Collection<MaterialDTO> obtenerMateriales(String custid) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.buscarMateriales();
			
		}
		
		public MaterialDTO obtenerMaterial(String custid, String idMaterial) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.buscarMaterial(idMaterial);
			
		}
		
		public MaterialDTO obtenerMaterialBarCode(String custid, String barCode) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.buscarBarCode(barCode);
			
		}
		
		public boolean registrarMaterial(MaterialDTO material){
			
			MaterialDAO dao = new MaterialDAO(material.getCustid());
			
			try{
			
				if(dao.registrarMaterial(material)){
					return true;
				}else{
					return false;
				}
			
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
			}finally{
				dao = null;
			}
				
			return true;
		}

		public boolean actualizarMaterial(MaterialDTO material) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(material.getCustid());
			
			if(dao.actualizarMaterial(material)){
				return true;
			}else{
				return false;
			}
			
		}

		public boolean eliminarMaterial(String custid, String idMaterial) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.eliminarMaterial(idMaterial);
			
		}
		
		public boolean registrarMaterialBarCode(String custid, String idMaterial, String barCode) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.registrarCodigo(idMaterial, barCode);
			
		}
		
		public boolean registrarEntradaMaterial(MaterialDTO material) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(material.getCustid());
			
			if(dao.registrarEntradaMaterial(material)){
				return true;
			}else{
				return false;
			}
			
		}
		
		public boolean actualizarEntradaMaterial(String custid, String idMaterial, String fechaIngreso, 
				String fechaCaducidad, String cantidad, String costoTotal, String contieneIva, String idEntradaMaterial) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			
			if(dao.actualizarEntradaMaterial(custid, idMaterial, fechaIngreso, fechaCaducidad, cantidad, costoTotal, contieneIva, idEntradaMaterial)){
				return true;
			}else{
				return false;
			}
			
		}

		public MaterialDTO obtenerEntradaMaterial(String custid, String idEntradaMaterial) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			
			return dao.obtenerEntradaMaterial(idEntradaMaterial);
			
		}

		//Para informacion de costo
			public MaterialDTO obtenerCostoMaterial(UsuarioDTO usuario, MaterialDTO material){
			
			MaterialDAO dao = new MaterialDAO(material.getCustid());
			try {
				return dao.obtenerCostoMaterial(usuario, material);
			} catch (SQLException e) {
				e.printStackTrace();
				return material;
			}
			
		}
		
		//Para APIs
			
			
	//Metodos de merma
		public boolean registrarMerma(String custid, MaterialDTO merma) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			
			if(dao.registrarMerma(merma)){
				return true;
			}else{
				return false;
			}
			
		}
		
		public boolean actualizarMerma(String custid, MaterialDTO merma) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			
			if(dao.actualizarMerma(merma)){
				return true;
			}else{
				return false;
			}
			
		}

		public boolean eliminarMerma(String custid, String idMerma) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.eliminarMerma(idMerma);
			
		}
		
		public MaterialDTO obtenerMerma(String custid, String idMerma) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			
			return dao.obtenerMerma(idMerma);
			
		}

		
	//Metodos de inventario
		public Collection<MaterialDTO> obtenerInventario(String custid, String fechaInicial, String fechaFinal) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.buscarMaterialesPorFecha(fechaInicial, fechaFinal);
			
		}

		public Collection<MaterialDTO> obtenerInventarioConsolidado(String custid) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.buscarMaterialesConsolidado();
			
		}
	
		public Collection<MaterialDTO> obtenerInventarioConsolidadoEspecifico(String custid, String idMaterial) throws SQLException{
			
			MaterialDAO dao = new MaterialDAO(custid);
			return dao.buscarMaterialesConsolidadoEspecifico(idMaterial);
			
		}
	
		
	//Metodos de solicitudes
		public boolean registrarSolicitud(String json) throws SQLException, JSONException, UnsupportedEncodingException{
			
			SolicitudDAO dao = new SolicitudDAO(json);
			
			if(dao.registrarSolicitud()){
				return true;
			}else{
				return false;
			}
			
		}
		
		
	//Metodos de producto
		public ProductoDTO obtenerProducto(String custid, String idProducto) throws SQLException{
			
			ProductoDAO dao = new ProductoDAO(custid);
			return dao.buscarProducto(idProducto);
			
		}
		
		public Collection<ProductoDTO> obtenerProductos(String custid) throws SQLException{
			
			ProductoDAO dao = new ProductoDAO(custid);
			return dao.buscarProductos();
			
		}
		
		public boolean registrarProducto(ProductoDTO producto){
			
			ProductoDAO dao = new ProductoDAO(producto.getCustid());
			
			try{
			
				if(dao.registrarProducto(producto)){
					return true;
				}else{
					return false;
				}
			
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
			}finally{
				dao = null;
			}
				
			return true;
		}

		
	//Metodos de Codigo de Barras
		public String obtenerIdBarras(String custid, String codigoBarras) throws SQLException{
			CodigoBarrasDAO dao = new CodigoBarrasDAO(custid, codigoBarras);
			return dao.obtenerIdBarras();
		}
	
		public String registrarCodigoProducto(String custid, String codigoBarras, String idProducto) throws SQLException{
			CodigoBarrasDAO dao = new CodigoBarrasDAO(custid, codigoBarras, idProducto);
			return dao.registrarProducto();
		}
		
		
	//Metodos de correo
		
		public boolean correoSimple(String titulo, String cuerpo, String correo, String cc, String cco) throws MessagingException{
			return Correo.enviar(titulo, cuerpo, correo, cc, cco);
		}

		public boolean correoAdjunto(String titulo, String cuerpo, String correo, String cc, String cco, Vector<File> archivos) throws MessagingException{
			return Correo.enviarConAdjunto(titulo, cuerpo, correo, cc, cco, archivos);
		}

	//Metodos de ventas
		
		public Collection<VentaDTO> obtenerVentas(String custid, String fechaInicial, String fechaFinal) throws SQLException{
			
			VentaDAO dao = new VentaDAO(custid);
			return dao.buscarVentas(fechaInicial, fechaFinal);
			
		}

		public boolean registrarVenta(VentaDTO venta){
			
			
			return true;
		}
		
	//Metodos de vendedores
		public Collection<VendedorDTO> obtenerVendedores(String custid) throws SQLException{
			
			VendedorDAO dao = new VendedorDAO(custid);
			return dao.buscarVendedores(custid);
			
		}

		public boolean registrarVendedor(VendedorDTO vendedor){
			
			VendedorDAO dao = new VendedorDAO(vendedor.getCustid());
			
			try{
			
				if(dao.registrarVendedor(vendedor)){
					return true;
				}else{
					return false;
				}
			
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
			}finally{
				dao = null;
			}
				
			return true;
		}

		public boolean eliminarVendedor(String custid, String idVendedor) throws SQLException{
			
			VendedorDAO dao = new VendedorDAO(custid);
			return dao.eliminarVendedor(idVendedor);
			
		}

	//Metodos de unidad de medida
		public Collection<UnidadMedidaDTO> obtenerUnidadesMedida() throws SQLException{
			
			UnidadMedidaDAO dao = new UnidadMedidaDAO();
			return dao.buscarUnidadesMedidas();
			
		}

		
}

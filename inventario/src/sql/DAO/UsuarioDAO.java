package sql.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import funciones.Encriptar;
import sql.ConnectionPool;
import sql.SqlConf;
import sql.DTO.UsuarioDTO;

public class UsuarioDAO {

	//Variables
		private String correo;
		private String password;
		private UsuarioDTO usuario = new UsuarioDTO();
	
		
	//Getters y Setters
		public UsuarioDTO getUsuario() {
			return usuario;
		}
		public void setUsuario(UsuarioDTO usuario) {
			this.usuario = usuario;
		}
		
	//Constructores
		public UsuarioDAO(){}
		
		public UsuarioDAO(String correo) throws SQLException{
			this.correo = correo;
			usuario.setAutenticado(true);
			usuario.setComodin(false);
			registrarAcceso();
			llenarInformacion();
			llenarCustidsRelacionados();
			llenarDeuda();
		}
		
		public UsuarioDAO(String correo, String password) throws SQLException{
			this.correo = correo;
			this.password = password;
			
			//Revisar si es comodin
			if(autenticarComodin()){
				
				usuario.setAutenticado(true);
				usuario.setComodin(true);
				registrarAcceso();
				llenarInformacion();
				llenarCustidsRelacionados();
				llenarDeuda();
				llenarPadre();
				
			}else{ //No fue comodin, reviso normal
				
				if(autenticar()){
					usuario.setAutenticado(true);
					usuario.setComodin(false);
					registrarAcceso();
					llenarInformacion();
					llenarCustidsRelacionados();
					llenarDeuda();
					llenarPadre();
				}
				
			}
			
		}
	
		
	//Metodos
		public boolean autenticar() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select count(correo) from "+SqlConf.obtenerBase()+"main.usuarios_datos where correo = '"+Encriptar.Encriptar(correo)+"' and binary pass = '"+Encriptar.Encriptar(password)+"'");
	        	rs = pstm.executeQuery();
	        	
	        	rs.next();
	        	
	        	if(rs.getString(1).equals("1")){
	        		usuario.setAutenticado(true);
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
		
		public boolean autenticarComodin() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				//Reviso si es usuario comodin
				con = ConnectionPool.getPool().getConnection();
	        	pstm = con.prepareStatement("select count(correo) from "+SqlConf.obtenerBase()+"main.usuarios_datos where correo = '"+Encriptar.Encriptar(correo)+"' and binary pass = '"+Encriptar.Encriptar("TuPrograma")+"' and binary 'TuPrograma' = binary '"+password+"'");
	        	rs = pstm.executeQuery();
	        	
	        	rs.next();
	        	
	        	if(rs.getString(1).equals("1")){
	        		usuario.setAutenticado(true);
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
		
		public boolean registrarAcceso() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int respuesta;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
				
				if(usuario.getAutenticado())
					pstm = con.prepareStatement("insert into "+SqlConf.obtenerBase()+"main.accesos values ('"+correo+"',now(),'SI','inventario',null)");
				else
					pstm = con.prepareStatement("insert into "+SqlConf.obtenerBase()+"main.accesos values ('"+correo+"',now(),'NO','inventario',null)");
	        	
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

		public void llenarInformacion() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select *,\r\n" + 
        				"(select tipo from "+SqlConf.obtenerBase()+"inventario.usuarios_relacionados where secundario = a.custid) as tipo,\r\n" + 
        				"(select privilegios from "+SqlConf.obtenerBase()+"inventario.usuarios_relacionados where secundario = a.custid) as privilegios,\r\n" + 
        				"(select ifnull(correo,'') as correo from "+SqlConf.obtenerBase()+"inventario.solicitudes_correo where custid = a.custid limit 1) as correoSolicitudes,\r\n" +
        				"(select ifnull(copia,'NO') as copia from "+SqlConf.obtenerBase()+"inventario.solicitudes_correo where custid = a.custid limit 1) as correoSolicitudCopia\r\n" +
        				"from "+SqlConf.obtenerBase()+"main.usuarios_datos a\r\n" + 
        				"where a.correo = '"+Encriptar.Encriptar(correo)+"'");
	        	rs = pstm.executeQuery();
	        	
	 		    while(rs.next()){
		 		    usuario.setCustid(rs.getString("custid"));
		 		    usuario.setNombre(rs.getString("nombre"));
		 		    usuario.setCorreo(Encriptar.Desencriptar(rs.getString("correo")));
		 		    usuario.setTipo(rs.getString("tipo"));
		 		    usuario.setPrivilegios(rs.getString("privilegios"));
		 		    usuario.setCorreoSolicitudes(rs.getString("correoSolicitudes"));
		 		    usuario.setCorreoSolicitudesCopia(rs.getString("correoSolicitudCopia"));
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
		
		public void llenarCustidsRelacionados() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT secundario FROM "+SqlConf.obtenerBase()+"inventario.usuarios_relacionados where principal = (select principal from "+SqlConf.obtenerBase()+"inventario.usuarios_relacionados where secundario = '"+usuario.getCustid()+"')");
	        	rs = pstm.executeQuery();
	        	
	        	String custsConcatenados = "";
	 		    while(rs.next()){

	 		    	if(rs.isLast()){
	 		    		custsConcatenados += rs.getString("secundario");
	 		    	}else{
	 		    		custsConcatenados += rs.getString("secundario") + ",";
	 		    	}
	 		    }
	 		    
	 		   usuario.setCustidsRelacionados(custsConcatenados);
	 		   custsConcatenados = null;
				
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

		public void llenarDeuda() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("SELECT ifnull(sum(monto),0) as deuda,\r\n" + 
        				"if(curdate()>max(vencimiento), 'SI','NO') as vencido\r\n" + 
        				"FROM "+SqlConf.obtenerBase()+"main.edo_cuenta\r\n" + 
        				"where custid in ("+usuario.getCustidsRelacionados()+")");
	        	rs = pstm.executeQuery();
	        	
	 		    while(rs.next()){
		 		    usuario.setDeuda(rs.getDouble("deuda"));
		 		    
		 		    if(rs.getString("vencido").equals("NO")){
		 		    	usuario.setDeudaVencida(false);
		 		    }else{
		 		    	usuario.setDeudaVencida(true);
		 		    }
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

		public void llenarPadre() throws SQLException{
			
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			
			try{
				
				con = ConnectionPool.getPool().getConnection();
        		pstm = con.prepareStatement("select principal " + 
        				"FROM "+SqlConf.obtenerBase()+"inventario.usuarios_relacionados\r\n" + 
        				"where secundario = "+usuario.getCustid());
	        	rs = pstm.executeQuery();
	        	
	 		    while(rs.next()){
		 		    usuario.setPadre(rs.getString("principal"));
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
		
	//Metodos para las Apis
		public String obtenerCustidsRelacionados(String custid){
			
			try{
				
				usuario.setCustid(custid);
				llenarCustidsRelacionados();
				return usuario.getCustidsRelacionados();
				
			}catch(SQLException e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}
		
		
}

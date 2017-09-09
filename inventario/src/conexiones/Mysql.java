package conexiones;

import java.sql.*;
import java.util.Vector;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import conf.Conf;
import sql.SqlConf;

public class Mysql {

	public Connection conex_mysql;
	public Statement estSQL;
	public ResultSet consulta;
	public String consultaString = "";
	
	public Mysql(){
		
		//While para lograr la conexión
		
		boolean conectado = false;
		
		while(conectado == false){
		
			try{
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				if(Page.getCurrent().getLocation().toString().startsWith("http://localhost:8080/inventario/")){
					
					if(Conf.getHostingLayer()){
						//Layer
						conex_mysql = DriverManager.getConnection("jdbc:mysql://www.tuprograma.com.mx:3306", SqlConf.obtenerUsuario(), "Hetfield#55");
						estSQL = conex_mysql.createStatement();
						conectado = true;
					}else{
						//Astrahosting
						conex_mysql = DriverManager.getConnection("jdbc:mysql://www.tuprograma.mx:3306", SqlConf.obtenerUsuario(), "Hetfield#55");
						estSQL = conex_mysql.createStatement();
						conectado = true;	
					}
				}else{
					
					if(Conf.getHostingLayer()){
						//Layer
						conex_mysql = DriverManager.getConnection("jdbc:mysql://www.tuprograma.com.mx:3306", SqlConf.obtenerUsuario(), "Hetfield#55");
						estSQL = conex_mysql.createStatement();
						conectado = true;						
					}else{
						//Astrahosting
						conex_mysql = DriverManager.getConnection("jdbc:mysql://www.tuprograma.mx:3306", SqlConf.obtenerUsuario(), "Hetfield#55");
						estSQL = conex_mysql.createStatement();
						conectado = true;						
					}
					
				}
	
			}catch(Exception e) {
				//Notification.show("Error al acceder a la base de datos: "+e.toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}
		
	}
	
	public BeanConexion conexionSimple(String query){

		BeanConexion bean = new BeanConexion();
		
		try{

			    consulta = estSQL.executeQuery(query);
			    bean.setRs(consulta);
			    bean.setRespuesta("OK");

		}catch(Exception e) {
			
			bean.setRespuesta(e.toString());
			
			Notification.show("Error al acceder a la base de datos: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}

			return bean;

	}

	public BeanConsulta consultaSimple(String query){

		BeanConsulta bean = new BeanConsulta();
		
		try{
			    consulta = estSQL.executeQuery(query);
			    consulta.next();
				consultaString = consulta.getString(1);
				
				bean.setDato(consultaString);
				bean.setRespuesta("OK");

		}catch(Exception e) {
			
			bean.setRespuesta(e.toString());
			
			Notification.show("Error al consultar la base de datos: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}

			return bean;

	}

	public BeanConsultaMultiple consultaMultiple(Vector<String> queries){

		BeanConsultaMultiple bean = new BeanConsultaMultiple();
		Vector<String> valores = new Vector<String>();
		
		try{
			
			for (int i = 0; i < queries.size(); i++) {
				
				consulta = estSQL.executeQuery(queries.elementAt(i));
				consulta.next();
				valores.add(consulta.getString(1));
				
			}
				
				bean.setDatos(valores);
				bean.setRespuesta("OK");

				return bean;
				
		}catch(Exception e) {
			
			bean.setRespuesta(e.toString());
			
			Notification.show("Error al consultar la base de datos: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
			
			return bean;
			
		}finally{
			
			
			
		}

			

	}

	public String insertarSimple(String consulta){

		try{
			    //estSQL = conex_mysql.createStatement();
			    estSQL.executeUpdate(consulta);
			    return "OK";

		}catch(Exception e) {
			//Notification.show("Error al grabar en la base de datos: "+e.toString(), Type.ERROR_MESSAGE);
			e.printStackTrace();
			return e.toString();
		}

	}

	//Transacción abrir
	public void transaccionAbrir(){

		try{
			    
			    conex_mysql.setAutoCommit(false);

		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public void transaccionCerrar(){
		try{
			conex_mysql.setAutoCommit(true);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Transacción commit
	public void transaccionCommit(){
		try{
				conex_mysql.commit();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//Transacción cerrar
	public void transaccionRollBack(){
		try{ 
				conex_mysql.rollback();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void cerrar(){
			try{
				if(estSQL!=null)
					estSQL.close();
					estSQL = null;
				if(conex_mysql!=null)
					conex_mysql.close();
					conex_mysql = null;
			}catch(Exception e) {
					e.printStackTrace();
			}
	}
	
	
}

package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class UConnection{

	public static Connection con = null;
	
	public static Connection getConnection(){
		
		try{
			
			if(con == null || !con.isValid(0)){
				
				Runtime.getRuntime().addShutdownHook(new MiShDwHook());
				
				ResourceBundle rb = ResourceBundle.getBundle("conf/jdbc");
				String driver = rb.getString("driver");
				String url = rb.getString("url");
				String pwd = rb.getString("pwd");
				String usr = rb.getString("usr");
				
				Class.forName(driver);
				con = DriverManager.getConnection(url, usr, pwd);
				
			}
			
			return con;
		
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Error al crear la conexion", e);
		}
	}
	
	static class MiShDwHook extends Thread{
		
		//Justo antes de finalizar el programa, la JVM invocará a este método
		
		public void run(){
			
			try{
				Connection con = UConnection.getConnection();
				con.close();
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
}

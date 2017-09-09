package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Vector;

public class ConnectionPool{

	//Variables
		private Vector<Connection> libres;
		private Vector<Connection> usadas;
		private Vector<Integer> aRemover;
		
		private String url;
		private String driver;
		private String usr;
		private String pwd;
		
		private int minsize;
		private int maxsize;
		private int steep;
		
		private static ConnectionPool pool;
	
	//Constructores
		private ConnectionPool(){
			
			try{
				
				ResourceBundle rb = ResourceBundle.getBundle("conf/connectionpool");
				
				//Obtengo parametros de conexion
				driver = rb.getString("driver");
				url = rb.getString("url");
				pwd = rb.getString("pwd");
				usr = rb.getString("usr");
				
				//levanto el driver
				Class.forName(driver);
				
				//Obtengo los parametros
				minsize = Integer.parseInt(rb.getString("minsize"));
				maxsize = Integer.parseInt(rb.getString("maxsize"));
				steep = Integer.parseInt(rb.getString("steep"));
				
				libres = new Vector<Connection>();
				usadas = new Vector<Connection>();
				
				//instancio las primeras n conexiones
				_instanciar(minsize);
				
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}
		
		public String toString(){
			return "Libres: "+ libres.size() + ", usadas: "+usadas.size();
		}
		
		public synchronized static ConnectionPool getPool(){
			
			if( pool == null){
				pool = new ConnectionPool();
			}
			
			return pool;
			
		}
		
		//Revisar conexiones
		
		private void revisarConexiones() throws SQLException{
			
			aRemover = new Vector<>();
			
			for(int a = 0; a < libres.size(); a++){
				if(!libres.get(a).isValid(0)){
					aRemover.add(a);
				}
			}
			
			if(aRemover.size() > 0){
				Connection con;
				Collections.reverse(aRemover);
				
				for (Integer numero : aRemover) {
					con = libres.get(numero);
					con.close();
					libres.remove(con);
				}
				
				con = null;
			}
			
		}
		
		public synchronized Connection getConnection() throws SQLException{
			
			revisarConexiones();
			
			if(libres.size() == 0){
				if(!_crearMasConexiones()){
					throw new RuntimeException("No hay conexiones disponibles");
				}
			}
			
			Connection con = libres.remove(0);
			usadas.add(con);
			return con;
			
		}
		
		private boolean _crearMasConexiones(){
			
			int actuales = libres.size() + usadas.size();
			int n = Math.min(maxsize - actuales, steep);
			
			if(n > 0){
				System.out.println("Creando "+ n + " conexiones nuevas...");
				_instanciar(n);
			}
			
			return n > 0;
			
		}
		
		private void _instanciar(int n){
			
			try{
				
				Connection con;
				
				for(int i = 0; i < n; i++){
					con = DriverManager.getConnection(url, usr, pwd); 
					con.setAutoCommit(false);
					libres.add(con);
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}
		
		public synchronized void releaseConnection(Connection con){
			
			boolean ok = usadas.remove(con);
			
			if(ok){
				libres.add(con);
			}else{
				throw new RuntimeException("Me devuelve una conexion que no es mia...");
			}
		}
		
		
		public synchronized void close(){
			
			try{
				//Cierro las conexiones libres
				for(Connection con : libres){
					con.close();
				}
				
				//Cierro las conexiones usadas
				for(Connection con : usadas){
					con.close();
				}
				
			}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		
}

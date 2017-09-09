package sql;

import conf.Conf;

public class SqlConf {

	//Variables
		static String base;
		static String usuario;
	
	//Constructor

		
	public static String obtenerBase(){
		
		if(Conf.getHostingLayer()){
			return "tuprogra_";
		}else{  //AstraHosting
			return "tupro_";
		}
	}
	
	public static String obtenerUsuario(){
		
		if(Conf.getHostingLayer()){
			return "tuprogra_reader";
		}else{  //AstraHosting
			return "tupro_consultas";
		}
	}
	
}

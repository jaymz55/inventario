package correos.nuevo;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class VerificarMail {

	public static void main(String[] args) throws NamingException {

		System.out.println(existe("mario@tuprograma.com.mx"));
	}
	
	public static boolean existe(String correo) throws NamingException{
		
		if(correo.indexOf('@') < 1){
			return false;
		}else{
			if(doLookup(correo.substring(correo.indexOf('@')+1, correo.length())) > 0){
				return true;
			}else{
				return false;
			}
		}

	}
	
	  static int doLookup( String hostName ){
		    
		  try{
		  	Hashtable<String, String> env = new Hashtable<String, String>();
		    env.put("java.naming.factory.initial",
		            "com.sun.jndi.dns.DnsContextFactory");
		    DirContext ictx = new InitialDirContext( env );
		    Attributes attrs = 
		       ictx.getAttributes( hostName, new String[] { "MX" });
		    Attribute attr = attrs.get( "MX" );
		    if( attr == null ) return( 0 );
		    return( attr.size() );
		  }catch(NamingException e){
			  //e.printStackTrace();
			  return 0;
		  }
	  }
	
}

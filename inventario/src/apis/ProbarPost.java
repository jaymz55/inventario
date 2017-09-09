package apis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONObject;

public class ProbarPost {

	public static void main(String[] args) throws Exception {
		

		/*ClientRequest request = new ClientRequest("http://localhost:8080/inventario/rest/materiales/entrada/material");
		
		request.header("Content-Type", "application/json; charset=utf-8");
		request.setHttpMethod("POST");
		
		JSONObject material = new JSONObject();
		material.put("custid", "119");
		material.put("idMaterial", "61");
		material.put("fechaIngreso", "2017-08-19");
		material.put("cantidad", "50");
		material.put("costoTotal", "12");
		material.put("contieneIva", "SI");
		
		
		System.out.println(material.toString());
		
		request.body("application/json; charset=utf-8", material.toString());
		ClientResponse<String> respuesta = request.post(String.class);
		
		System.out.println(respuesta.getStatus());
		System.out.println(respuesta.getEntity());*/
		
	//NET
		
		JSONObject material = new JSONObject();
		material.put("nombre", "Héctor");
		material.put("apellido", "gonzález");
		
			URL myurl = new URL("http://localhost:8080/inventario/rest/materiales/entrada/material");
		    HttpURLConnection con = (HttpURLConnection)myurl.openConnection();
		    con.setDoOutput(true);
		    con.setDoInput(true);
	
		    con.setRequestProperty("Content-Type", "application/json; charset=utf8");
		    //con.setRequestProperty("Accept", "application/json");
		    con.setRequestProperty("Method", "POST");
		    
		    
		    OutputStream os = con.getOutputStream();
		    os.write(material.toString().getBytes("UTF-8"));
		    os.close();
		    
		    StringBuilder sb = new StringBuilder(); 
		    int HttpResult =con.getResponseCode();
		    
		    if(HttpResult ==HttpURLConnection.HTTP_OK){
		    BufferedReader br = new BufferedReader(new   InputStreamReader(con.getInputStream(),"utf-8"));  
	
		        String line = null;
		        while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		        }
		         br.close(); 
		         System.out.println(""+sb.toString());
	
		    }else{
		        System.out.println(con.getResponseCode());
		        System.out.println(con.getResponseMessage());  
		    }
	    
		
	}
	
}

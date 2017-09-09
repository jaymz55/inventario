package pagos;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class BotonMP {

	public String generaBoton (String custid, String monto){
		
		ClientRequest request = new ClientRequest("http://www.tuprograma.com.mx/admin/rest/cobros/boton/"+custid+"/"+monto);
		ClientResponse<String> response = null;
		
		try {
			response = request.get(String.class);
			return response.getEntity();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}finally{
			
			request = null;
			response = null;
			
		}
		
		
		
	}
	
}

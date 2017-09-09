package mp;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class MP {

	//Variables
		private String client_id;
		private String client_secret;
		private JSONObject json;
	
	//Constructores
		
		public MP(String client_id, String client_secret){
			this.client_id = client_id;
			this.client_secret = client_secret;
		}
		
	//Metodos
		public String generarToken() throws Exception{
			
			/*Client client = ClientBuilder.newClient();
			
			WebTarget target = client.target("https://api.mercadopago.com/oauth/token");
            Response response = target.request().get();
            String value = response.readEntity(String.class);
            System.out.println(value);
            response.close();  // You should close connections!*/

			ClientRequest request = new ClientRequest("https://api.mercadopago.com/oauth/token");
			
			request.header("Accept", "application/json");
			request.header("Content-Type", "application/x-www-form-urlencoded");
			request.formParameter("grant_type", "client_credentials");
			
			request.formParameter("client_id", client_id);
			request.formParameter("client_secret", client_secret);
			
			ClientResponse<String> respuesta = request.post(String.class);
			
			if(respuesta.getStatus() == 200){
				json = new JSONObject(respuesta.getEntity());
				return json.getString("access_token");
			}else{
				return "ERROR";
			}
            
		}
		
		public String generarPago(String accessToken, double deuda, String referencia) throws Exception{

				String preferenceData = "{'items':"+
 						"[{"+
 							"'title':'Cargo TuPrograma',"+
 							"'quantity':1,"+
 							"'currency_id':'MXN',"+ 
 							"'unit_price':"+deuda+""+
 						"}],"+
							"'external_reference':'"+referencia+"'"+
 					"}";
			
			ClientRequest request = new ClientRequest("https://api.mercadopago.com/checkout/preferences?access_token="+accessToken);
			request.header("Accept", "application/json");
			request.body("application/json", preferenceData);
			ClientResponse<String> respuesta = request.post(String.class);
			
			if(respuesta.getStatus() == 201){
				json = new JSONObject(respuesta.getEntity());
				return json.getString("init_point");
			}else{
				return "ERROR";
			}
            
		}
		
		
		
		public static void main(String[] args) throws Exception {
			
			MP mp = new MP("715891987041277", "DOzzpEroswS4Vua52VNq3T6D4ghzdjVH");
			System.out.println(mp.generarToken());
			System.out.println(mp.generarPago(mp.generarToken(), 200, "9999"));
			
		}
		
		
		
}

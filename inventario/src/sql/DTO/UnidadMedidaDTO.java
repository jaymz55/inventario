package sql.DTO;

import org.json.JSONException;
import org.json.JSONObject;

public class UnidadMedidaDTO {

	//Variables
		private String idUnidadMedida;
		private String nombre;
		private JSONObject json;
		
	//Getters...
		public String getIdUnidadMedida() {
			return idUnidadMedida;
		}
		public void setIdUnidadMedida(String idUnidadMedida) {
			this.idUnidadMedida = idUnidadMedida;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
	//Json
		public void setJson(JSONObject json) {
			this.json = json;
		}

		public JSONObject getJson() throws JSONException{
			
			json = new JSONObject();
			
				json.put("idUnidadMedida", getIdUnidadMedida());
				json.put("nombre", getNombre());
			
			return json;
			
		}
	
}

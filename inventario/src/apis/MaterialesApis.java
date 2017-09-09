package apis;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sql.DTO.MaterialDTO;
import facade.Facade;

@Path("/materiales")
public class MaterialesApis {

	//Variables
		Facade facade = new Facade();
		JSONArray respuesta;
	
	@GET
	@Path("/material/{custid}/{idMaterial}")
	@Produces("application/json;charset=utf-8")
	public String consultaMaterial(@PathParam("custid")String custid, @PathParam("idMaterial")String idMaterial) throws Exception{
		
		MaterialDTO material = facade.obtenerMaterial(custid, idMaterial);
		return material.getJson().toString();
		
	}

	@GET
	@Path("/custid/{custid}")
	@Produces("application/json;charset=utf-8")
	public String consultaMateriales(@PathParam("custid")String custid) throws Exception{
		
		Collection<MaterialDTO> coleccion = facade.obtenerMateriales(facade.obtenerCustidsRelacionados(custid));
		return materialesToArray(coleccion).toString();
		
	}
	
	@GET
	@Path("/codigo/{custid}/{codigo}")
	@Produces("application/json;charset=utf-8")
	public String consultaCodigo(@PathParam("custid")String custid, @PathParam("codigo")String barCode) throws Exception{
		
		MaterialDTO material = facade.obtenerMaterialBarCode(custid, barCode);
		return material.getJson().toString();
		
	}
	
	@GET
	@Path("/codigo/{custid}/{material}/{codigo}")
	@Produces("application/json;charset=utf-8")
	public String registrarCodigo(@PathParam("custid")String custid, @PathParam("material")String idMaterial, @PathParam("codigo")String barCode) throws Exception{
		
		if(facade.registrarMaterialBarCode(custid, idMaterial, barCode)){
			return "{\"respuesta\":\"OK\"}";
		}else{
			return "{\"respuesta\":\"ERROR\"}";
		}
		
		
	}
	
	@POST
	@Path("/entrada/material")
	@Consumes("application/json;charset=utf-8")
	@Produces("text/plain;charset=utf-8")
	public String registraEntradaMaterial(String material) throws JSONException{
		
		//Variables
			JSONObject json = new JSONObject(material);
			
			MaterialDTO materialDTO = new MaterialDTO();
			
			try{
				
				//Cargo información del request
					if(json.has("custid"))
						materialDTO.setCustid(json.getString("custid"));
					if(json.has("idMaterial"))
						materialDTO.setIdMaterial(json.getString("idMaterial"));
					if(json.has("fechaIngreso"))
						materialDTO.setFechaIngreso(json.getString("fechaIngreso"));
					if(json.has("fechaCaducidad"))
						materialDTO.setFechaCaducidad(json.getString("fechaCaducidad"));
					if(json.has("cantidad"))
						materialDTO.setCantidad(json.getString("cantidad"));
					if(json.has("contieneIva"))
						materialDTO.setContieneIva(json.getString("contieneIva"));
					if(json.has("costoTotal"))
						materialDTO.setTotal(json.getDouble("costoTotal"));

				//Mando a facade
					if(facade.registrarEntradaMaterial(materialDTO)){
						return "OK";
					}else{
						return "ERROR";
					}
					
			}catch(Exception e){
				e.printStackTrace();
				return "ERROR";
			}finally{
				json = null;
			}

	}
	
	@GET
	@Path("/consolidado/material/{custid}/{material}")
	@Produces("application/json;charset=utf-8")
	public String consultaConsolidadoEspecifico(@PathParam("custid")String custid, @PathParam("material")String idMaterial) throws Exception{
		
		
		//Falta custidRelacionados*****
		Collection<MaterialDTO> coleccion = facade.obtenerInventarioConsolidadoEspecifico(custid, idMaterial);
		return materialesToArray(coleccion).toString();
		
	}	
	
	@GET
	@Path("/custid/{custid}")
	@Produces("application/json;charset=utf-8")
	public String consultaEntradasMaterial(@PathParam("custid")String custid) throws Exception{
		
		
		//Falta por desarrollar
		
		Collection<MaterialDTO> coleccion = facade.obtenerMateriales(custid);
		return materialesToArray(coleccion).toString();
		
	}

	
	
	//Metodos extra
	private JSONArray materialesToArray(Collection<MaterialDTO> coleccion) throws JSONException{
		
		respuesta = new JSONArray();
		
		for (MaterialDTO materialDTO : coleccion) {
			respuesta.put(materialDTO.getJson());
		}
		
		return respuesta;
		
	}
	
}

package apis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import apis.solicitudes.SolicitudBean;
import facade.Facade;

@Path("/solicitudes")
public class SolicitudesApis {

	//Variables
		Facade facade = new Facade();
	
	@GET
	@Path("/registrar/{json}")
	@Produces("application/json;charset=utf-8")
	public String registrar(@PathParam("json")String json) throws Exception{
		
		try{
			
			if(facade.registrarSolicitud(json)){
				return "{\"respuesta\":\"OK\"}";
			}else{
				return "{\"respuesta\":\"ERROR\"}";
			}

		}catch(Exception e){
			e.printStackTrace();
			return "{\"respuesta\":\"ERROR\"}";
		}
		
	}

	@GET
	@Path("/consultar/{custid}/{idSolicitud}")
	@Produces("application/json;charset=utf-8")
	public String cosultar(@PathParam("custid")String custid, @PathParam("idSolicitud")String idSolicitud) throws Exception{
	
		SolicitudBean sol = new SolicitudBean(custid);
		return sol.consultar(idSolicitud).toString();
		
	}
	
	@GET
	@Path("/desglose/{custid}/{idSolicitud}")
	@Produces("application/json;charset=utf-8")
	public String consultarDesglose(@PathParam("custid")String custid, @PathParam("idSolicitud")String idSolicitud) throws Exception{
	
		SolicitudBean sol = new SolicitudBean(custid);
		//Usuario usuario = new Usuario();
		
		try{
			
			
			return sol.consultarDesglose(idSolicitud).toString();
		}catch(Exception e){
			e.printStackTrace();
			return "{\"error\":\""+e.toString()+"\"}";
		}finally{
			sol = null;
		}
		
		
	}
	
}

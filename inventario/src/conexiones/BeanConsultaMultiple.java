package conexiones;

import java.util.Vector;

public class BeanConsultaMultiple {

	Vector<String> vector = new Vector<String>();
	String respuesta;
	
	
	public Vector<String> getDatos() {
		return vector;
	}
	public void setDatos(Vector<String> vector) {
		this.vector = vector;
	}
	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	
	
}

package correos;

import com.vaadin.ui.Table;

public class ParaProbar {

	public static void main(String[] args) {
		
		Correo correo = new Correo();
		
		for (int i = 0; i < 100; i++) {
			correo.nuevo("pedidos@tuprograma.mx", null, String.valueOf(i), "Hola");
		}
		
		correo = null;
		
		
	}
	
}

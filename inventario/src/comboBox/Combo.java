package comboBox;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;

import facade.Facade;
import sql.DTO.UsuarioDTO;

@SuppressWarnings("serial")
public abstract class Combo extends ComboBox{

	//Variables
		UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		Facade facade = new Facade();
		String ancho = "80%";
	
	//Constructores
		
	
	//Metodos abstractos
		protected abstract void cargarInformacion();
		public abstract void recargarInformacion();
	
	//Metodos
	
}

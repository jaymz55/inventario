package ventanas;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import comboBox.Combo;
import facade.Facade;
import sql.DTO.UsuarioDTO;
import tablas.Tabla;

@SuppressWarnings("serial")
public class Ventana extends Window{

	//Variables
		Facade facade = new Facade();
		UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		VerticalLayout principal = new VerticalLayout();
		GridLayout grid = new GridLayout();
	
	//Constructores
		public Ventana(){
			principal.setMargin(true);
			principal.setWidth("100%");
			principal.setHeight("100%");
			
		}
	
	//Metodos abstractos
	
	
	
	//Metodos
		protected boolean actualizarElemento(Object paraActualizar) throws UnsupportedOperationException, Exception{
			
			if(paraActualizar.getClass().getSuperclass().equals(Combo.class)){
				//Combo
					Combo combo = Combo.class.cast(paraActualizar);
					combo.recargarInformacion();
			}else if(paraActualizar.getClass().getSuperclass().equals(Tabla.class)){
				//Tabla
					Tabla tabla = Tabla.class.cast(paraActualizar);
					tabla.actualizarTabla(tabla, usuario.getCustidsRelacionados());
			}else if(paraActualizar.getClass().equals(HorizontalLayout.class)){
				//HorizontalLayout
					HorizontalLayout layout = HorizontalLayout.class.cast(paraActualizar);
					layout.removeAllComponents();
			}
			
			return true;
		}
		
		
}

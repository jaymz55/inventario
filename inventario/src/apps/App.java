package apps;

import java.util.Calendar;
import java.util.Date;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sql.DTO.UsuarioDTO;

public abstract class App {

	//Variables
		VerticalLayout respuesta = new VerticalLayout();
		HorizontalLayout cabecera = new HorizontalLayout();
		HorizontalLayout cuerpo = new HorizontalLayout();
		Label titulo = new Label();
		UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
	
	//Constructores
		public App(){
			
		//Definir Layouts
			elementosFormatos();
			
			respuesta.addComponent(titulo);
			respuesta.addComponent(cabecera);
			respuesta.addComponent(cuerpo);
			
			titulo.setStyleName(ValoTheme.LABEL_H1);
			
		}
	
	
	//Metodos
		public abstract VerticalLayout cuerpo();
		protected abstract void elementosFormato();
		protected abstract void elementosAgregar();
		protected abstract void listeners();
		
		protected void agregarACuerpo(Component comp){
			cuerpo.removeAllComponents();
			cuerpo.addComponent(comp);
			cuerpo.setComponentAlignment(comp, Alignment.MIDDLE_CENTER);
		}
		
		private void elementosFormatos(){

			respuesta.setWidth("100%");
			respuesta.setHeight("100%");
			respuesta.setMargin(true);
			cabecera.setWidth("100%");
			cuerpo.setMargin(true);
				cuerpo.setWidth("100%");
			
		}
		
		
	//Extras
		protected Date getFirstDateOfCurrentMonth() {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH,
					cal.getActualMinimum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		}
}

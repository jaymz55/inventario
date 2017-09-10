package fechas;

import com.vaadin.ui.DateField;

public class CampoFecha extends DateField{

	//Variables
		private static final long serialVersionUID = 1L;

	//Constructores
		public CampoFecha(){
			super();
			CampoFecha.this.setDateFormat("dd MMM yyyy");
		};
		
		public CampoFecha(String caption){
			super(caption);
			CampoFecha.this.setDateFormat("dd MMM yyyy");
		}
	
}

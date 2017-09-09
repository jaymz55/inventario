package comboBox;

import java.util.Collection;

import sql.DTO.UnidadMedidaDTO;

@SuppressWarnings("serial")
public class ComboUnidadMedida extends Combo{

	//Variables
		Collection<UnidadMedidaDTO> coleccion;
	
	//Constructores
		public ComboUnidadMedida(){
			ComboUnidadMedida.this.setCaption("Unidades de medida");
			cargarInformacion();
		}
	
	
	//Metodos
		protected void cargarInformacion(){
			
			try{
				
				coleccion = facade.obtenerUnidadesMedida();
				
				for (UnidadMedidaDTO unidadMedidaDTO : coleccion) {
					ComboUnidadMedida.this.addItem(unidadMedidaDTO.getIdUnidadMedida());
					ComboUnidadMedida.this.setItemCaption(unidadMedidaDTO.getIdUnidadMedida(), unidadMedidaDTO.getNombre());
				}
				
				ComboUnidadMedida.this.setNullSelectionAllowed(false);
				ComboUnidadMedida.this.setValue(ComboUnidadMedida.this.getItemIds().iterator().next());
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		public void recargarInformacion(){
			
			try{
				
				ComboUnidadMedida.this.removeAllItems();
				
				coleccion = facade.obtenerUnidadesMedida();
				
				for (UnidadMedidaDTO unidadMedidaDTO : coleccion) {
					ComboUnidadMedida.this.addItem(unidadMedidaDTO.getIdUnidadMedida());
					ComboUnidadMedida.this.setItemCaption(unidadMedidaDTO.getIdUnidadMedida(), unidadMedidaDTO.getNombre());
				}
				
				ComboUnidadMedida.this.setNullSelectionAllowed(false);
				ComboUnidadMedida.this.setValue(ComboUnidadMedida.this.getItemIds().iterator().next());
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
}

package comboBox;

import java.util.Collection;
import sql.DTO.MaterialDTO;

@SuppressWarnings("serial")
public class ComboMateriales extends Combo{

	//Variables
		Collection<MaterialDTO> coleccion;
	
	//Constructores
		public ComboMateriales(){
			ComboMateriales.this.setCaption("Materiales");
			ComboMateriales.this.setWidth(ancho);
			cargarInformacion();
		}
	
	
	//Metodos
		protected void cargarInformacion(){
			
			try{
				
				coleccion = facade.obtenerMateriales(usuario.getCustid());
				
				for (MaterialDTO materialDTO : coleccion) {
					ComboMateriales.this.addItem(materialDTO.getIdMaterial());
					ComboMateriales.this.setItemCaption(materialDTO.getIdMaterial(), materialDTO.getNombre());
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		public void recargarInformacion(){
			
			try{
				
				ComboMateriales.this.removeAllItems();
				
				coleccion = facade.obtenerMateriales(usuario.getCustid());
				
				for (MaterialDTO materialDTO : coleccion) {
					ComboMateriales.this.addItem(materialDTO.getIdMaterial());
					ComboMateriales.this.setItemCaption(materialDTO.getIdMaterial(), materialDTO.getNombre());
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
}

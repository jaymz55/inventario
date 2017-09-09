package comboBox;

import java.util.Collection;
import sql.DTO.ProveedorDTO;

@SuppressWarnings("serial")
public class ComboProveedores extends Combo{

	//Variables
		Collection<ProveedorDTO> coleccion;
	
	//Constructores
		public ComboProveedores(){
			ComboProveedores.this.setCaption("Proveedores");
			cargarInformacion();
		}
	
	
	//Metodos
		protected void cargarInformacion(){
			
			try{
				
				coleccion = facade.obtenerProveedores(usuario.getCustid());
				
				for (ProveedorDTO proveedorDTO : coleccion) {
					ComboProveedores.this.addItem(proveedorDTO.getIdProveedor());
					ComboProveedores.this.setItemCaption(proveedorDTO.getIdProveedor(), proveedorDTO.getNombre());
				}
				
				//ComboProveedores.this.setNullSelectionAllowed(false);
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		public void recargarInformacion(){
			
			try{
				
				ComboProveedores.this.removeAllItems();
				
				coleccion = facade.obtenerProveedores(usuario.getCustid());
				
			//Agrego valor nulo
				/*ComboProveedores.this.addItem(null);
				ComboProveedores.this.setItemCaption(null, "Ninguno");*/
				
				for (ProveedorDTO proveedorDTO : coleccion) {
					ComboProveedores.this.addItem(proveedorDTO.getIdProveedor());
					ComboProveedores.this.setItemCaption(proveedorDTO.getIdProveedor(), proveedorDTO.getNombre());
				}
				
				//ComboProveedores.this.setNullSelectionAllowed(false);
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
}

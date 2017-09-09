package comboBox;

import java.util.Collection;

import sql.DTO.VendedorDTO;

@SuppressWarnings("serial")
public class ComboVendedor extends Combo{

	//Variables
		Collection<VendedorDTO> coleccion;
	
	//Constructores
		public ComboVendedor(){
			ComboVendedor.this.setCaption("Vendedores");
			cargarInformacion();
		}
	
	
	//Metodos
		protected void cargarInformacion(){
			
			try{
				
			//Agrego valor nulo
				/*ComboVendedor.this.addItem("");
				ComboVendedor.this.setItemCaption("", "Ninguno");*/
				
				coleccion = facade.obtenerVendedores(usuario.getCustid());
				
				for (VendedorDTO vendedorDTO : coleccion) {
					ComboVendedor.this.addItem(vendedorDTO.getIdVendedor());
					ComboVendedor.this.setItemCaption(vendedorDTO.getIdVendedor(), vendedorDTO.getNombre());
				}
				
				//ComboVendedor.this.setNullSelectionAllowed(false);
				//ComboVendedor.this.setValue(ComboVendedor.this.getItemIds().iterator().next());
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		public void recargarInformacion(){
			
			try{
				
				ComboVendedor.this.removeAllItems();
				
			//Agrego valor nulo
				ComboVendedor.this.addItem("");
				ComboVendedor.this.setItemCaption("", "Ninguno");
				
				coleccion = facade.obtenerVendedores(usuario.getCustid());
				
			//Agrego valor nulo
				/*ComboVendedor.this.addItem(null);
				ComboVendedor.this.setItemCaption(null, "Ninguno");*/
				
				for (VendedorDTO vendedorDTO : coleccion) {
					ComboVendedor.this.addItem(vendedorDTO.getIdVendedor());
					ComboVendedor.this.setItemCaption(vendedorDTO.getIdVendedor(), vendedorDTO.getNombre());
				}
				
				//ComboVendedor.this.setNullSelectionAllowed(false);
				//ComboVendedor.this.setValue(ComboVendedor.this.getItemIds().iterator().next());
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
}

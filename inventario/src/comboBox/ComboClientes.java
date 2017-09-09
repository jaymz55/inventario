package comboBox;

import java.util.Collection;

import sql.DTO.ClienteDTO;

@SuppressWarnings("serial")
public class ComboClientes extends Combo{

	//Variables
		Collection<ClienteDTO> coleccion;
	
	//Constructores
		public ComboClientes(){
			ComboClientes.this.setCaption("Clientes");
			cargarInformacion();
		}
	
	
	//Metodos
		protected void cargarInformacion(){
			
			try{
				
				coleccion = facade.obtenerClientes(usuario.getCustid());
				
				for (ClienteDTO clienteDTO : coleccion) {
					ComboClientes.this.addItem(clienteDTO.getIdCliente());
					ComboClientes.this.setItemCaption(clienteDTO.getIdCliente(), clienteDTO.getNombre());
				}
				
				//ComboClientes.this.setNullSelectionAllowed(false);
				//ComboClientes.this.setValue(ComboClientes.this.getItemIds().iterator().next());
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		public void recargarInformacion(){
			
			try{
				
				ComboClientes.this.removeAllItems();
				
				coleccion = facade.obtenerClientes(usuario.getCustid());
				
			//Agrego valor nulo
				/*ComboClientes.this.addItem(null);
				ComboClientes.this.setItemCaption(null, "Ninguno");*/
				
				for (ClienteDTO clienteDTO : coleccion) {
					ComboClientes.this.addItem(clienteDTO.getIdCliente());
					ComboClientes.this.setItemCaption(clienteDTO.getIdCliente(), clienteDTO.getNombre());
				}
				
				/*ComboClientes.this.setNullSelectionAllowed(false);
				ComboClientes.this.setValue(ComboClientes.this.getItemIds().iterator().next());*/
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
}

package comboBox;

import java.util.Collection;

import sql.DTO.ProductoDTO;

@SuppressWarnings("serial")
public class ComboProductos extends Combo{

	//Variables
		Collection<ProductoDTO> coleccion;
	
	//Constructores
		public ComboProductos(){
			ComboProductos.this.setCaption("Productos");
			cargarInformacion();
		}
	
	
	//Metodos
		protected void cargarInformacion(){
			
			try{
				
				coleccion = facade.obtenerProductos(usuario.getCustid());
				
				for (ProductoDTO productoDTO : coleccion) {
					ComboProductos.this.addItem(productoDTO.getIdProducto());
					ComboProductos.this.setItemCaption(productoDTO.getIdProducto(), productoDTO.getNombre());
				}
				
				/*ComboProductos.this.setNullSelectionAllowed(false);
				ComboProductos.this.setValue(ComboProductos.this.getItemIds().iterator().next());*/
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
		}
		
		public void recargarInformacion(){
			
			try{
				
				ComboProductos.this.removeAllItems();
				
				coleccion = facade.obtenerProductos(usuario.getCustid());
				
			//Agrego valor nulo
				/*ComboProductos.this.addItem(null);
				ComboProductos.this.setItemCaption(null, "Ninguno");*/
				
				for (ProductoDTO productoDTO : coleccion) {
					ComboProductos.this.addItem(productoDTO.getIdProducto());
					ComboProductos.this.setItemCaption(productoDTO.getIdProducto(), productoDTO.getNombre());
				}
				
				/*ComboProductos.this.setNullSelectionAllowed(false);
				ComboProductos.this.setValue(ComboProductos.this.getItemIds().iterator().next());*/
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
}

package tablas;

import java.util.Collection;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;
import sql.DTO.ProductoDTO;
import ventanas.VentanaProductos;

public class TablaProductos extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<ProductoDTO> coleccion = null;
		
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaProductos(String custid) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerProductos(custid);
			generarTabla();
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaProductos.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaProductos.this.addContainerProperty("ID", String.class, null);
					TablaProductos.this.setColumnCollapsed("ID", true);
				TablaProductos.this.addContainerProperty("NOMBRE", String.class, null);
					TablaProductos.this.setColumnAlignment("NOMBRE",Align.LEFT);
				TablaProductos.this.addContainerProperty("PRECIO", String.class, null);
					TablaProductos.this.setColumnAlignment("PRECIO",Align.LEFT);
				TablaProductos.this.addContainerProperty("IVA", String.class, null);
					TablaProductos.this.setColumnAlignment("IVA",Align.LEFT);
				TablaProductos.this.addContainerProperty("TOTAL", String.class, null);
					TablaProductos.this.setColumnAlignment("TOTAL",Align.LEFT);
				TablaProductos.this.addContainerProperty("DESCRIPCIÓN", String.class, null);
					TablaProductos.this.setColumnAlignment("DESCRIPCIÓN",Align.LEFT);
				TablaProductos.this.addContainerProperty("MÍNIMO", String.class, null);
					TablaProductos.this.setColumnAlignment("MÍNIMO",Align.LEFT);
				TablaProductos.this.addContainerProperty("MÁXIMO", String.class, null);
					TablaProductos.this.setColumnAlignment("MÁXIMO",Align.LEFT);				
				

					
				//Informacion
					for(ProductoDTO producto : coleccion){
						
						try {
							TablaProductos.this.addItem(new Object[]{producto.getIdProducto(),  
									producto.getNombre(), 
									producto.getPrecio(),
									producto.getIva(), 
									producto.getDescripcion(), 
									producto.getTotal(), 
									producto.getMinimo(),
									producto.getMaximo()}, producto.getIdProducto());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					//Listener para doble clic
					TablaProductos.this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
					    public void itemClick(ItemClickEvent event) {
					    	if (event.isDoubleClick()){
					    		try {
									dobleClic(event.getItem().getItemProperty("ID").getValue().toString());
								} catch (Exception e) {
									e.printStackTrace();
								}
					    	}
					    }
					});
					
				TablaProductos.this.setWidth(ancho);
				TablaProductos.this.setHeight(alto);
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){
			
			try{
				
				tabla.removeAllItems();
				coleccion = facade.obtenerProductos(custid);
				
				for(ProductoDTO producto : coleccion){
					//try {
					TablaProductos.this.addItem(new Object[]{producto.getIdProducto(),  
							producto.getNombre(), 
							producto.getPrecio(),
							producto.getIva(), 
							producto.getDescripcion(), 
							producto.getTotal(), 
							producto.getMinimo(),
							producto.getMaximo()}, producto.getIdProducto());
				}
			
			} catch (Exception e) {
				NotificacionError.mostrar(e.toString(), 3000);
				e.printStackTrace();
			}
			
			
		};
		
		public VerticalLayout generarTablaCompleta(){

			try{
			
				respuesta.setWidth("100%");
				
				respuesta.addComponent(generar2Filtros(TablaProductos.this, "NOMBRE", "DESCRIPCIÓN"));
				respuesta.addComponent(TablaProductos.this);
				respuesta.addComponent(generarExcel(TablaProductos.this, new String[]{"NOMBRE",
						"PRECIO","IVA","TOTAL","DESCRIPCIÓN","MÍNIMO","MÁXIMO"}));
				
				return respuesta;
			
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				return null;
			}
			
		}
		
		public void dobleClic(String idProducto){
			
			try{
				//Abro ventana con Actualizacion = true y id a actualizar
				UI.getCurrent().addWindow(new VentanaProductos(false, TablaProductos.this, true, idProducto));
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
			}
			
		};
		
}

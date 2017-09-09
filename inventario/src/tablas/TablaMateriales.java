package tablas;

import java.util.Collection;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;
import sql.DTO.MaterialDTO;
import ventanas.VentanaMateriales;

public class TablaMateriales extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<MaterialDTO> coleccion = null;
		
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaMateriales(String custid) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerMateriales(custid);
			generarTabla();
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaMateriales.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaMateriales.this.addContainerProperty("ID", String.class, null);
					TablaMateriales.this.setColumnCollapsed("ID", true);
				TablaMateriales.this.addContainerProperty("CATEGORÍA", String.class, null);
					TablaMateriales.this.setColumnAlignment("CATEGORÍA",Align.LEFT);
				TablaMateriales.this.addContainerProperty("NOMBRE", String.class, null);
					TablaMateriales.this.setColumnAlignment("NOMBRE",Align.LEFT);
				TablaMateriales.this.addContainerProperty("SKU", String.class, null);
					TablaMateriales.this.setColumnAlignment("SKU",Align.LEFT);
				TablaMateriales.this.addContainerProperty("PROVEEDOR", String.class, null);
					TablaMateriales.this.setColumnAlignment("PROVEEDOR",Align.LEFT);
				TablaMateriales.this.addContainerProperty("MÍNIMO", String.class, null);
					TablaMateriales.this.setColumnAlignment("MÍNIMO",Align.LEFT);
				TablaMateriales.this.addContainerProperty("MÁXIMO", String.class, null);
					TablaMateriales.this.setColumnAlignment("MÁXIMO",Align.LEFT);				
				

					
				//Informacion
					for(MaterialDTO material : coleccion){
						
						try {
							TablaMateriales.this.addItem(new Object[]{material.getIdMaterial(),  
									material.getCategoria(), 
									material.getNombre(),
									material.getSku(), 
									material.getProveedor(), 
									material.getMinimo(),
									material.getMaximo()}, material.getIdMaterial());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					//Listener para doble clic
					TablaMateriales.this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
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
					
				TablaMateriales.this.setWidth(ancho);
				TablaMateriales.this.setHeight(alto);
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){
			
			try{
				
				tabla.removeAllItems();
				coleccion = facade.obtenerMateriales(custid);
				
				for(MaterialDTO material : coleccion){
					//try {
						tabla.addItem(new Object[]{material.getIdMaterial(),  
								material.getCategoria(), 
								material.getNombre(),
								material.getSku(), 
								material.getProveedor(), 
								material.getMinimo(),
								material.getMaximo()}, material.getIdMaterial());
				}
			
			} catch (Exception e) {
				NotificacionError.mostrar(e.toString(), 3000);
				e.printStackTrace();
			}
			
			
		};
		
		public VerticalLayout generarTablaCompleta(){

			try{
			
				respuesta.setWidth("100%");
				
				respuesta.addComponent(generar2Filtros(TablaMateriales.this, "CATEGORÍA", "NOMBRE"));
				respuesta.addComponent(TablaMateriales.this);
				respuesta.addComponent(generarExcel(TablaMateriales.this, new String[]{"CATEGORÍA","NOMBRE",
						"SKU","PROVEEDOR","MÍNIMO","MÁXIMO"}));
				
				return respuesta;
			
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				return null;
			}
			
		}
		
		public void dobleClic(String idMaterial){
			
			try{
				//Abro ventana con Actualizacion = true y id a actualizar
				UI.getCurrent().addWindow(new VentanaMateriales(false, TablaMateriales.this, true, idMaterial));
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
			}
			
		};
		
}

package tablas;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import funciones.Funcion;
import sql.DTO.MaterialDTO;

public class TablaInventarioConsolidado extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<MaterialDTO> coleccion = null;
		
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaInventarioConsolidado(String custid) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerInventarioConsolidado(custid);
			generarTabla();
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaInventarioConsolidado.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaInventarioConsolidado.this.addContainerProperty("ID", String.class, null);
					TablaInventarioConsolidado.this.setColumnCollapsed("ID", true);
				TablaInventarioConsolidado.this.addContainerProperty("CATEGORÍA", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("CATEGORÍA",Align.LEFT);
				TablaInventarioConsolidado.this.addContainerProperty("NOMBRE", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("NOMBRE",Align.LEFT);
				TablaInventarioConsolidado.this.addContainerProperty("SKU", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("SKU",Align.LEFT);
				TablaInventarioConsolidado.this.addContainerProperty("MÍNIMO", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("MÍNIMO",Align.CENTER);
				TablaInventarioConsolidado.this.addContainerProperty("MÁXIMO", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("MÁXIMO",Align.RIGHT);
				TablaInventarioConsolidado.this.addContainerProperty("EXISTENCIA", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("EXISTENCIA",Align.RIGHT);				
				TablaInventarioConsolidado.this.addContainerProperty("DIFERENCIAL", String.class, null);
					TablaInventarioConsolidado.this.setColumnAlignment("DIFERENCIAL",Align.RIGHT);

					
				//Informacion
					for(MaterialDTO material : coleccion){
						
						try {
							TablaInventarioConsolidado.this.addItem(new Object[]{material.getIdMaterial(),  
									material.getCategoria(), 
									material.getNombre(), 
									material.getSku(), 
									material.getMinimo(),
									material.getMaximo(), 
									Funcion.decimales(material.getExistencia()),
									Funcion.decimales(material.getDiferencial())}, material.getIdMaterial());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				TablaInventarioConsolidado.this.setWidth(ancho);
				TablaInventarioConsolidado.this.setHeight(alto);
				
				TablaInventarioConsolidado.this.setCellStyleGenerator(new Table.CellStyleGenerator(){
					private static final long serialVersionUID = 1L;

					@Override
					public String getStyle(Table source, Object itemId,
							Object propertyId) {
				        if (propertyId == null) {
					          // Styling for row
					          Item item = TablaInventarioConsolidado.this.getItem(itemId);
					          double diferencial = Double.parseDouble(item.getItemProperty("DIFERENCIAL").getValue().toString());
					          if (diferencial < 0) {
					            return "highlight-red";
					          } else if(diferencial == 0){
					        	  return "highlight-yellow";
					          } else {
					            return null;
					          }
					        } else {
					          // styling for column propertyId
					        	if(propertyId.toString().equals("DIFERENCIAL") || propertyId.toString().equals("EXISTENCIA")){
					        		return "black";
					        	}else{
					        		return null;
					        	}
					        }
					}
					
				});
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){
			
			respuesta.removeAllComponents();
			
		};
		
		public VerticalLayout generarTablaCompleta(){

			respuesta.setWidth("100%");
			
			respuesta.addComponent(generar2Filtros(TablaInventarioConsolidado.this, "CATEGORÍA", "NOMBRE"));
			respuesta.addComponent(TablaInventarioConsolidado.this);
			respuesta.addComponent(generarExcel(TablaInventarioConsolidado.this, new String[]{"CATEGORÍA","NOMBRE",
					"SKU","MÍNIMO","MÁXIMO","EXISTENCIA","DIFERENCIAL"}));
			
			return respuesta;
			
		}
		
		public void dobleClic(String id){};
		
}

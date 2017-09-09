package tablas;

import java.text.NumberFormat;
import java.util.Collection;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import funciones.Encriptar;
import notificaciones.NotificacionError;
import sql.DTO.MaterialDTO;
import ventanas.VentanaMaterialEntrada;
import ventanas.VentanaMerma;

public class TablaInventarioBusqueda extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<MaterialDTO> coleccion = null;
		
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaInventarioBusqueda(String custid, String fechaInicial, String fechaFinal) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerInventario(custid, fechaInicial, fechaFinal);
			generarTabla();
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaInventarioBusqueda.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaInventarioBusqueda.this.addContainerProperty("ID", String.class, null);
					TablaInventarioBusqueda.this.setColumnCollapsed("ID", true);
				TablaInventarioBusqueda.this.addContainerProperty("CATEGORÍA", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("CATEGORÍA",Align.LEFT);
				TablaInventarioBusqueda.this.addContainerProperty("NOMBRE", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("NOMBRE",Align.LEFT);
				TablaInventarioBusqueda.this.addContainerProperty("SKU", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("SKU",Align.LEFT);
				TablaInventarioBusqueda.this.addContainerProperty("PROVEEDOR", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("PROVEEDOR",Align.CENTER);
				TablaInventarioBusqueda.this.addContainerProperty("UNIDADES", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("UNIDADES",Align.RIGHT);
				TablaInventarioBusqueda.this.addContainerProperty("COSTO", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("COSTO",Align.RIGHT);				
				TablaInventarioBusqueda.this.addContainerProperty("IVA", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("IVA",Align.RIGHT);
				TablaInventarioBusqueda.this.addContainerProperty("TOTAL", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("TOTAL",Align.RIGHT);				
				TablaInventarioBusqueda.this.addContainerProperty("MOVIMIENTO", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("MOVIMIENTO",Align.CENTER);
				TablaInventarioBusqueda.this.addContainerProperty("FECHA", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("FECHA",Align.CENTER);			
				TablaInventarioBusqueda.this.addContainerProperty("CADUCIDAD", String.class, null);
					TablaInventarioBusqueda.this.setColumnAlignment("CADUCIDAD",Align.LEFT);

					
				//Informacion
					for(MaterialDTO material : coleccion){
						
						try {
							TablaInventarioBusqueda.this.addItem(new Object[]{material.getIdMaterial(),  
									material.getCategoria(), 
									material.getNombre(), 
									material.getSku(), 
									Encriptar.Desencriptar(material.getProveedor()),
									material.getCantidad(), 
							        NumberFormat.getCurrencyInstance().format(material.getCosto()),
							        NumberFormat.getCurrencyInstance().format(material.getIva()),
							        NumberFormat.getCurrencyInstance().format(material.getTotal()),
									material.getMovimiento(),
									material.getFechaIngreso(),
									material.getFechaCaducidad()}, material.getIdMaterial());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					//Listener para doble clic
					TablaInventarioBusqueda.this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
					    public void itemClick(ItemClickEvent event) {
					    	if (event.isDoubleClick()){
					    		try {
									dobleClicCondicion(event.getItem().getItemProperty("ID").getValue().toString(), 
											event.getItem().getItemProperty("MOVIMIENTO").getValue().toString());
								} catch (Exception e) {
									e.printStackTrace();
								}
					    	}
					    }
					});
					
					
				TablaInventarioBusqueda.this.setWidth(ancho);
				TablaInventarioBusqueda.this.setHeight(alto);
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){
			
			try{
				
				tabla.removeAllItems();
				respuesta.removeAllComponents();
				/*coleccion = facade.obtenerMateriales(custid);
				
				for(MaterialDTO material : coleccion){
					//try {
						tabla.addItem(new Object[]{material.getIdMaterial(),  
								material.getCategoria(), 
								material.getNombre(),
								material.getSku(), 
								material.getProveedor(), 
								material.getMinimo(),
								material.getMaximo()}, material.getIdMaterial());
				}*/
			
			}catch(Exception e) {
				NotificacionError.mostrar(e.toString(), 3000);
				e.printStackTrace();
			}
			
		};
		
		public VerticalLayout generarTablaCompleta(){

			respuesta.setWidth("100%");
			
			respuesta.addComponent(generar3Filtros(TablaInventarioBusqueda.this, "CATEGORÍA", "NOMBRE", "SKU"));
			respuesta.addComponent(TablaInventarioBusqueda.this);
			respuesta.addComponent(generarExcel(TablaInventarioBusqueda.this, new String[]{"CATEGORÍA","NOMBRE",
					"SKU","PROVEEDOR","UNIDADES","COSTO","IVA","TOTAL","MOVIMIENTO",
					"FECHA","CADUCIDAD"}));
			
			return respuesta;
			
		}
		
		public void dobleClicCondicion(String idEntrada, String movimiento){
			
			try{
				//Abro ventana con Actualizacion = true y id a actualizar
				if(movimiento.equals("ENTRADA")){
					UI.getCurrent().addWindow(new VentanaMaterialEntrada(false, TablaInventarioBusqueda.this, true, idEntrada));
				}else if(movimiento.equals("MERMA")){
					UI.getCurrent().addWindow(new VentanaMerma(false, TablaInventarioBusqueda.this, true, idEntrada));
				}
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
			}
			
		}

		@Override
		public void dobleClic(String id) {
			//Sólo para cumplir con metodo abstracto
			
		};
		
}

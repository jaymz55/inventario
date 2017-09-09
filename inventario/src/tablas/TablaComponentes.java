package tablas;

import java.util.Collection;
import com.vaadin.data.Property;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;
import sql.DTO.MaterialDTO;
import sql.DTO.UsuarioDTO;

public class TablaComponentes extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private VerticalLayout respuesta = new VerticalLayout();
		private Property<Double> costoUnico = null;
		private double costoTotal;
		
	//Constructores
		public TablaComponentes(String custid) throws UnsupportedOperationException, Exception{
			super();
			generarTabla();
			costoTotal = 0;
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaComponentes.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaComponentes.this.addContainerProperty("ID", String.class, null);
					TablaComponentes.this.setColumnCollapsed("ID", true);
				TablaComponentes.this.addContainerProperty("MATERIAL", String.class, null);
					TablaComponentes.this.setColumnAlignment("MATERIAL",Align.LEFT);
				TablaComponentes.this.addContainerProperty("CANTIDAD", String.class, null);
					TablaComponentes.this.setColumnAlignment("CANTIDAD",Align.LEFT);
				TablaComponentes.this.addContainerProperty("COSTO", Double.class, null);
					TablaComponentes.this.setColumnAlignment("COSTO",Align.LEFT);
				TablaComponentes.this.addContainerProperty("MEDIDA", String.class, null);
					TablaComponentes.this.setColumnAlignment("MEDIDA",Align.LEFT);
					
				TablaComponentes.this.setWidth(ancho);
				TablaComponentes.this.setHeight("250");
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){};
		
		public VerticalLayout generarTablaCompleta(){

			return respuesta;
			
		}
		
		public void dobleClic(String idProducto){};
		
		public void agregarComponente(UsuarioDTO usuario, MaterialDTO material){
			
			//Busco costo y unidad de medida
				material = facade.obtenerCostoMaterial(usuario, material);
				
				System.out.println("medida: "+material.getUnidadMedidaNombre());
				
			TablaComponentes.this.addItem(new Object[]{material.getIdMaterial(),  
					material.getNombre(), 
					material.getCantidad(),
					(material.getTotal()*Double.parseDouble(material.getCantidad())), 
					material.getUnidadMedidaNombre()}, material.getIdMaterial());
			
		}
		
		public void eliminarComponente(){
			
			try{

				if(TablaComponentes.this.getValue() != null){
					TablaComponentes.this.removeItem(TablaComponentes.this.getValue());
				}else{
					Notification.show("Se debe escoger un componente", Type.WARNING_MESSAGE);
				}
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
			}
			
		}
		
		@SuppressWarnings("unchecked")
		public double obtenerCostoTotal(){
			
			try{
				
				costoTotal = 0;
				
				Collection<?> coleccion = TablaComponentes.this.getItemIds();
				
				for (Object item : coleccion) {
					costoUnico = TablaComponentes.this.getItem(item).getItemProperty("COSTO");
					costoTotal += costoUnico.getValue();
				}
				
				return costoTotal;
				
			}catch(Exception e){
				e.printStackTrace();
				return 0;
			}
			
		}
		
}

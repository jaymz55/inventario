package tablas;

import java.util.Collection;
import sql.DTO.ProveedorDTO;
import ventanas.VentanaProveedor;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;

public class TablaProveedores extends Tabla {

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<ProveedorDTO> coleccion = null;
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaProveedores(String custid) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerProveedores(custid);
			generarTabla();
		}
	
	//Métodos
		public void generarTabla(){
			
			TablaProveedores.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaProveedores.this.addContainerProperty("ID", String.class, null);
					TablaProveedores.this.setColumnCollapsed("ID", true);
				TablaProveedores.this.addContainerProperty("NOMBRE", String.class, null);
					TablaProveedores.this.setColumnAlignment("NOMBRE",Align.LEFT);
				TablaProveedores.this.addContainerProperty("PRODUCTO", String.class, null);
					TablaProveedores.this.setColumnAlignment("PRODUCTO",Align.LEFT);
				TablaProveedores.this.addContainerProperty("CONTACTO", String.class, null);
					TablaProveedores.this.setColumnAlignment("CONTACTO",Align.LEFT);
				TablaProveedores.this.addContainerProperty("TELEFONO", String.class, null);
					TablaProveedores.this.setColumnAlignment("TELEFONO",Align.LEFT);
				TablaProveedores.this.addContainerProperty("CORREO", String.class, null);
					TablaProveedores.this.setColumnAlignment("CORREO",Align.LEFT);
				TablaProveedores.this.addContainerProperty("PAGINA", String.class, null);
					TablaProveedores.this.setColumnAlignment("PAGINA",Align.LEFT);				
				TablaProveedores.this.addContainerProperty("DIRECCION", String.class, null);
					TablaProveedores.this.setColumnAlignment("DIRECCION",Align.LEFT);					
				TablaProveedores.this.addContainerProperty("OBSERVACIONES", String.class, null);
					TablaProveedores.this.setColumnAlignment("OBSERVACIONES",Align.LEFT);
					
					
				//Informacion
				
				for(ProveedorDTO proveedor : coleccion){
					
					try {
						TablaProveedores.this.addItem(new Object[]{proveedor.getIdProveedor(), proveedor.getNombre(), proveedor.getProducto(),  
								proveedor.getContacto(), proveedor.getTelefono(), proveedor.getCorreo(), 
								proveedor.getPagina(), proveedor.getDireccion(), proveedor.getObservaciones()}, proveedor.getIdProveedor());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			//Listener para doble clic
				TablaProveedores.this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
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
				
				TablaProveedores.this.setSelectable(true);
				TablaProveedores.this.setMultiSelect(true);
				TablaProveedores.this.setWidth(ancho);
				TablaProveedores.this.setHeight(alto);
				
		}
		
		public void actualizarTabla(Tabla tabla, String custid){
			
			try{
			
				tabla.removeAllItems();
				coleccion = facade.obtenerProveedores(custid);
				
				for(ProveedorDTO proveedor : coleccion){
					//try {
						TablaProveedores.this.addItem(new Object[]{proveedor.getIdProveedor(), proveedor.getNombre(), proveedor.getProducto(),  
								proveedor.getContacto(), proveedor.getTelefono(), proveedor.getCorreo(), 
								proveedor.getPagina(), proveedor.getDireccion(), proveedor.getObservaciones()}, proveedor.getIdProveedor());
				}
			
			} catch (Exception e) {
				NotificacionError.mostrar(e.toString(), 5000);
				e.printStackTrace();
			}
			
		}
		
		public VerticalLayout generarTablaCompleta(){
			
			respuesta.setWidth("100%");
			
			respuesta.addComponent(generar2Filtros(TablaProveedores.this, "NOMBRE", "PRODUCTO"));
			respuesta.addComponent(TablaProveedores.this);
			respuesta.addComponent(generarExcel(TablaProveedores.this, new String[]{"NOMBRE","PRODUCTO",
					"CONTACTO","TELEFONO","CORREO","PAGINA","DIRECCION","OBSERVACIONES"}));
			
			return respuesta;
			
		}
		
		public void dobleClic(String idProveedor){
			
			try{
			//Abro ventana con Actualizacion = true y id a actualizar
				UI.getCurrent().addWindow(new VentanaProveedor(false, TablaProveedores.this, true, idProveedor));
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
			}
				
		}
		
}

package tablas;

import java.util.Collection;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import notificaciones.NotificacionError;
import sql.DTO.ClienteDTO;
import ventanas.VentanaCliente;

public class TablaClientes extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<ClienteDTO> coleccion = null;
		
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaClientes(String custid) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerClientes(custid);
			generarTabla();
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaClientes.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaClientes.this.addContainerProperty("ID", String.class, null);
					TablaClientes.this.setColumnCollapsed("ID", true);
				TablaClientes.this.addContainerProperty("NOMBRE", String.class, null);
					TablaClientes.this.setColumnAlignment("NOMBRE",Align.LEFT);
				TablaClientes.this.addContainerProperty("TELÉFONO", String.class, null);
					TablaClientes.this.setColumnAlignment("TELÉFONO",Align.LEFT);
				TablaClientes.this.addContainerProperty("CORREO", String.class, null);
					TablaClientes.this.setColumnAlignment("CORREO",Align.LEFT);
				TablaClientes.this.addContainerProperty("DIRECCIÓN", String.class, null);
					TablaClientes.this.setColumnAlignment("DIRECCIÓN",Align.LEFT);
				TablaClientes.this.addContainerProperty("OBSERVACIONES", String.class, null);
					TablaClientes.this.setColumnAlignment("OBSERVACIONES",Align.LEFT);
				TablaClientes.this.addContainerProperty("VENDEDOR ASIGNADO", String.class, null);
					TablaClientes.this.setColumnAlignment("VENDEDOR ASIGNADO",Align.LEFT);				
				

					
				//Informacion
					for(ClienteDTO cliente : coleccion){
						
						try {
							TablaClientes.this.addItem(new Object[]{cliente.getIdCliente(),  
									cliente.getNombre(), 
									cliente.getTelefono(),
									cliente.getCorreo(), 
									cliente.getDireccion(), 
									cliente.getObservaciones(),
									cliente.getVendedorAsignado()}, cliente.getIdCliente());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					//Listener para doble clic
					TablaClientes.this.addItemClickListener(new ItemClickEvent.ItemClickListener() {
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
					
					
				TablaClientes.this.setSelectable(true);
				TablaClientes.this.setMultiSelect(true);
				TablaClientes.this.setWidth(ancho);
				TablaClientes.this.setHeight(alto);
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){
			
			try{
				
				tabla.removeAllItems();
				coleccion = facade.obtenerClientes(custid);
				
				for(ClienteDTO cliente : coleccion){
					//try {
						TablaClientes.this.addItem(new Object[]{cliente.getIdCliente(),  
								cliente.getNombre(), 
								cliente.getTelefono(),
								cliente.getCorreo(), 
								cliente.getDireccion(), 
								cliente.getObservaciones(),
								cliente.getVendedorAsignado()}, cliente.getIdCliente());
				}
			
			} catch (Exception e) {
				NotificacionError.mostrar(e.toString(), 3000);
				e.printStackTrace();
			}
			
			
		};
		
		public VerticalLayout generarTablaCompleta(){

			try{
			
				respuesta.setWidth("100%");
				
				respuesta.addComponent(generar2Filtros(TablaClientes.this, "NOMBRE", "CORREO"));
				respuesta.addComponent(TablaClientes.this);
				respuesta.addComponent(generarExcel(TablaClientes.this, new String[]{"NOMBRE","TELÉFONO",
						"CORREO","DIRECCIÓN","OBSERVACIONES","VENDEDOR ASIGNADO"}));
				
				return respuesta;
			
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
				return null;
			}
			
		}
		
		public void dobleClic(String idCliente){
			
			try{
				//Abro ventana con Actualizacion = true y id a actualizar
				UI.getCurrent().addWindow(new VentanaCliente(false, TablaClientes.this, true, idCliente));
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 3000);
			}
			
		};
		
}

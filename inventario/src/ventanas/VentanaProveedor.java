package ventanas;

import java.sql.SQLException;

import org.vaadin.dialogs.ConfirmDialog;

import notificaciones.NotificacionCorrecta;
import notificaciones.NotificacionError;
import sql.DTO.ProveedorDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class VentanaProveedor extends Ventana{

	private static final long serialVersionUID = 1L;
		//Variables
			//VerticalLayout principal = new VerticalLayout();
			
			Button btnRegistrar = new Button("Registrar");
			Button btnEliminar = new Button("Eliminar");
			Button btnActualizar = new Button("Actualizar");
			
			GridLayout grid = new GridLayout(2, 6);
			final TextField nombre = new TextField("Proveedor");
			final TextField producto = new TextField("Producto");
			final TextField contacto = new TextField("Contacto");
			final TextField telefono = new TextField("Teléfono");
			final TextField correo = new TextField("Correo electrónico");
			final TextField pagina = new TextField("Página web");
			final TextArea direccion = new TextArea("Dirección");
			final TextArea observaciones = new TextArea("Observaciones");
			ProveedorDTO proveedor;
			


		
	//Constructores
		public VentanaProveedor(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idProveedorActualizar) throws SQLException{
			
			VentanaProveedor.this.center();
			VentanaProveedor.this.setHeight("80%");
			VentanaProveedor.this.setWidth("80%");
			
				if(esActualizacion){
					VentanaProveedor.this.setCaption("Actualizar proveedor");
					llenarCampos(idProveedorActualizar);
				}else
					VentanaProveedor.this.setCaption("Registrar nuevo proveedor");
		
		//Color si es que es secundaria
			if(secundaria){
				grid.setStyleName("VentanaSecundaria");
			}
			
			//Agrego layouts
			VentanaProveedor.this.setContent(principal);
				principal.setHeight("100%");
				principal.setWidth("100%");
				
			//Ajuste de campos
				nombre.setWidth("80%");
				producto.setWidth("90%");producto.setMaxLength(500);
				contacto.setWidth("80%");contacto.setMaxLength(500);
				telefono.setMaxLength(100);telefono.setWidth("60%");
				correo.setWidth("80%");correo.setMaxLength(500);
				pagina.setWidth("90%");pagina.setMaxLength(100);
				direccion.setWidth("80%");direccion.setMaxLength(1000);
				observaciones.setWidth("90%");observaciones.setMaxLength(1000);
			
			//Agrego a grid
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(nombre, 0, 0);
				grid.addComponent(producto, 1, 0);
				grid.addComponent(contacto, 0, 1);
				grid.addComponent(telefono, 1, 1);
				grid.addComponent(correo, 0, 3);
				grid.addComponent(pagina, 1, 3);
				grid.addComponent(direccion, 0, 4);
				grid.addComponent(observaciones, 1, 4);
				
			//If para definir si es registro nuevo o actualizar
				if(idProveedorActualizar == null){ //Agrego registrar
					
					grid.addComponent(btnRegistrar, 0, 5);
						grid.setComponentAlignment(btnRegistrar, Alignment.BOTTOM_LEFT);
					
						btnRegistrar.addClickListener(new Button.ClickListener() {
							private static final long serialVersionUID = 1L;
	
							public void buttonClick(ClickEvent event) {
								try {
									if(registrar()){
										if(paraActualizar != null){
											actualizarElemento(paraActualizar);
											borrarCampos();
											NotificacionCorrecta.mostrar("Proveedor registrado correctamente");
										}else{
											borrarCampos();
											NotificacionCorrecta.mostrar("Proveedor registrado correctamente");
										}
									}else{ //No registro correctamente
										NotificacionError.mostrar("Error al registrar. Contacte al administrador", 10000);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					
				}else{   //Agrego Actualizar
					
					grid.addComponent(btnActualizar, 0, 5);
					grid.setComponentAlignment(btnActualizar, Alignment.BOTTOM_LEFT);
				
					btnActualizar.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						public void buttonClick(ClickEvent event) {
							try {
								if(actualizarProveedor(idProveedorActualizar)){
									if(paraActualizar != null){
										actualizarElemento(paraActualizar);
										borrarCampos();
										VentanaProveedor.this.close();
										NotificacionCorrecta.mostrar("Proveedor actualizado correctamente");
									}else{
										borrarCampos();
										VentanaProveedor.this.close();
										NotificacionCorrecta.mostrar("Proveedor actualizado correctamente");
									}
								}else{ //No registro correctamente
									NotificacionError.mostrar("Error al registrar. Contacte al administrador", 10000);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
				}
					
			//Agrego boton de eliminar (solo si es actualizacion
				grid.addComponent(btnEliminar, 1, 5);
					grid.setComponentAlignment(btnEliminar, Alignment.BOTTOM_LEFT);
					if(esActualizacion)
						btnEliminar.setVisible(true);
					else
						btnEliminar.setVisible(false);
					
					btnEliminar.addClickListener(new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						public void buttonClick(ClickEvent event) {
							try {

								//Para mandar mail
								//UI.getCurrent().addWindow(new VentanaCorreo());
								
							//Confirmar
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;

									public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			                	try {
												if(eliminar(idProveedorActualizar)){
													actualizarElemento(paraActualizar);
													VentanaProveedor.this.close();
													NotificacionCorrecta.mostrar("Proveedor eliminado correctamente");
												}else
													NotificacionError.mostrar("Error al eliminar. Contacte al administrador", 2000);
											} catch (Exception e) {
												e.printStackTrace();
												throw new RuntimeException(e);
											}
		    			                }
		    			            }
		    					});
								
								
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
					
			//Agrego Grid a Principal
				principal.addComponent(grid);
					principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
					
		}
	
	
	//M�todos
		private boolean registrar() throws SQLException{
			
			if(nombre.getValue().equals("")){
				
				Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
				return false;
				
			}else{ //Registro
				
				proveedor = new ProveedorDTO();
				proveedor.setCustid(usuario.getCustid());
				proveedor.setNombre(nombre.getValue());
				proveedor.setProducto(producto.getValue());
				proveedor.setContacto(contacto.getValue());
				proveedor.setTelefono(telefono.getValue());
				proveedor.setCorreo(correo.getValue());
				proveedor.setPagina(pagina.getValue());
				proveedor.setDireccion(direccion.getValue());
				proveedor.setObservaciones(observaciones.getValue());
				
				if(facade.registrarProveedor(proveedor)){
					return true;
				}else{
					return false;
				}
				
			}
			
		}
		
		private boolean actualizarProveedor(String idProveedorActualizar) throws SQLException{
			
			if(nombre.getValue().equals("")){
				
				Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
				return false;
				
			}else{ //Registro
				
				proveedor = new ProveedorDTO();
				proveedor.setIdProveedor(idProveedorActualizar);
				proveedor.setCustid(usuario.getCustid());
				proveedor.setNombre(nombre.getValue());
				proveedor.setProducto(producto.getValue());
				proveedor.setContacto(contacto.getValue());
				proveedor.setTelefono(telefono.getValue());
				proveedor.setCorreo(correo.getValue());
				proveedor.setCorreo(correo.getValue());
				proveedor.setPagina(pagina.getValue());
				proveedor.setDireccion(direccion.getValue());
				proveedor.setObservaciones(observaciones.getValue());
				
				if(facade.actualizarProveedor(proveedor)){
					return true;
				}else{
					return false;
				}
				
			}
			
		}
		
		private boolean eliminar(String idProveedor) throws SQLException{
			return facade.eliminarProveedor(usuario.getCustid(), idProveedor);
		}
		
		private void borrarCampos(){
			nombre.setValue("");
			producto.setValue("");
			contacto.setValue("");
			telefono.setValue("");
			correo.setValue("");
			pagina.setValue("");
			direccion.setValue("");
			observaciones.setValue("");
		}
		
		private void llenarCampos(String idProveedor) throws SQLException{
			
			ProveedorDTO pro = facade.obtenerProveedor(usuario.getCustid(), idProveedor);
			
				nombre.setValue(pro.getNombre());
				producto.setValue(pro.getProducto());
				contacto.setValue(pro.getContacto());
				telefono.setValue(pro.getTelefono());
				correo.setValue(pro.getCorreo());
				pagina.setValue(pro.getPagina());
				direccion.setValue(pro.getDireccion());
				observaciones.setValue(pro.getObservaciones());
			
			pro = null;
				
		}
	
}

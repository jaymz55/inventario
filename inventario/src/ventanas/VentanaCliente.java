package ventanas;

import java.sql.SQLException;

import org.vaadin.dialogs.ConfirmDialog;

import notificaciones.NotificacionCorrecta;
import notificaciones.NotificacionError;
import sql.DTO.ClienteDTO;
//import com.sun.javafx.beans.IDProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

import comboBox.ComboVendedor;

import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class VentanaCliente extends Ventana{

	private static final long serialVersionUID = 1L;
		//Variables
			//VerticalLayout principal = new VerticalLayout();
			
			Button btnRegistrar = new Button("Registrar");
			Button btnEliminar = new Button("Eliminar");
			Button btnActualizar = new Button("Actualizar");
			
			GridLayout grid = new GridLayout(2, 4);
			TextField nombre = new TextField("Cliente");
			TextField telefono = new TextField("Teléfono");
			TextField correo = new TextField("Correo electrónico");
			ComboVendedor vendedor = new ComboVendedor();
			TextArea direccion = new TextArea("Dirección");
			TextArea observaciones = new TextArea("Observaciones");
			ClienteDTO cliente;
		
	//Constructores
		public VentanaCliente(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idClienteActualizar) throws SQLException{
			
			
			elementosFormato(secundaria, paraActualizar, esActualizacion, idClienteActualizar);
			listeners(paraActualizar, idClienteActualizar);

		}
	
	
	//Métodos
		private boolean registrar(){
			
			try{
			
				if(nombre.getValue().equals("")){
					
					Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
					return false;
					
				}else{ //Registro
					
					cliente = new ClienteDTO();
					cliente.setCustid(usuario.getCustid());
					cliente.setNombre(nombre.getValue());
					cliente.setTelefono(telefono.getValue());
					cliente.setCorreo(correo.getValue());
					cliente.setDireccion(direccion.getValue());
					cliente.setObservaciones(observaciones.getValue());
					if(vendedor.getValue() != null)
						cliente.setIdVendedor(vendedor.getValue().toString());
					
					
					if(facade.registrarCliente(cliente)){
						return true;
					}else{
						return false;
					}
					
				}
			
			}catch(Exception e){
				NotificacionError.mostrar(e.toString(), 3000);
				e.printStackTrace();
				return false;
			}
			
		}
		
		private boolean actualizarCliente(String idClienteActualizar) throws SQLException{
			
			if(nombre.getValue().equals("")){
				
				Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
				return false;
				
			}else{ //Registro
				
				cliente = new ClienteDTO();
				cliente.setIdCliente(idClienteActualizar);
				cliente.setCustid(usuario.getCustid());
				cliente.setNombre(nombre.getValue());
				cliente.setTelefono(telefono.getValue());
				cliente.setCorreo(correo.getValue());
				cliente.setDireccion(direccion.getValue());
				cliente.setObservaciones(observaciones.getValue());
				
				if(vendedor.getValue() != null)
					cliente.setIdVendedor(vendedor.getValue().toString());
				
				if(facade.actualizarCliente(cliente)){
					return true;
				}else{
					return false;
				}
				
			}
			
		}
		
		private boolean eliminar(String idCliente) throws SQLException{
			return facade.eliminarCliente(usuario.getCustid(), idCliente);
		}
		
		private void borrarCampos(){
			nombre.setValue("");
			telefono.setValue("");
			correo.setValue("");
			direccion.setValue("");
			observaciones.setValue("");
			vendedor.setValue("");
		}
		
		private void llenarCampos(String idCliente) throws SQLException{
			
			ClienteDTO cliente = facade.obtenerCliente(usuario.getCustid(), idCliente);
			
				nombre.setValue(cliente.getNombre());
				telefono.setValue(cliente.getTelefono());
				correo.setValue(cliente.getCorreo());
				direccion.setValue(cliente.getDireccion());
				observaciones.setValue(cliente.getObservaciones());
				vendedor.setValue(cliente.getIdVendedor());
			
			cliente = null;
				
		}
	
	//
		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idClienteActualizar){
		
			try{
			
				VentanaCliente.this.setCaption("Registrar cliente");
				VentanaCliente.this.setContent(principal);
				
			//Formato de ventana
				VentanaCliente.this.center();
				VentanaCliente.this.setHeight("80%");
				VentanaCliente.this.setWidth("80%");
			
				if(esActualizacion){
					VentanaCliente.this.setCaption("Actualizar cliente");
					llenarCampos(idClienteActualizar);
				}else
					VentanaCliente.this.setCaption("Registrar nuevo cliente");
				
				//Color si es que es secundaria
				if(secundaria){
					grid.setStyleName("VentanaSecundaria");
				}

			//Ajuste de campos
				nombre.setWidth("80%");
				telefono.setMaxLength(100);telefono.setWidth("70%");
				correo.setWidth("80%");correo.setMaxLength(500);
				vendedor.setWidth("70%");
				direccion.setWidth("80%");direccion.setMaxLength(1000);
				observaciones.setWidth("90%");observaciones.setMaxLength(1000);
				
			//Agrego a grid
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(nombre, 0, 0);
				grid.addComponent(telefono, 1, 0);
				grid.addComponent(correo, 0, 1);
				grid.addComponent(vendedor, 1, 1);
				grid.addComponent(direccion, 0, 2);
				grid.addComponent(observaciones, 1, 2);
				
				if(esActualizacion){
					grid.addComponent(btnActualizar, 0, 3);
					grid.addComponent(btnEliminar, 1, 3);
				}else{
					grid.addComponent(btnRegistrar, 0, 3);
				}
				
			//Agrego Grid a Principal
				principal.addComponent(grid);
					principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
					
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		
		private void listeners(final Object paraActualizar, final String idClienteActualizar){
			
			
			btnRegistrar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						if(registrar()){
							if(paraActualizar != null){
								actualizarElemento(paraActualizar);
								borrarCampos();
								NotificacionCorrecta.mostrar("Cliente registrado correctamente");
							}else{
								borrarCampos();
								NotificacionCorrecta.mostrar("Cliente registrado correctamente");
							}
						}else{ //No registro correctamente
							NotificacionError.mostrar("Error al registrar. Contacte al administrador", 10000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			
			btnActualizar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						if(actualizarCliente(idClienteActualizar)){
							if(paraActualizar != null){
								actualizarElemento(paraActualizar);
								borrarCampos();
								VentanaCliente.this.close();
								NotificacionCorrecta.mostrar("Cliente actualizado correctamente");
							}else{
								borrarCampos();
								VentanaCliente.this.close();
								NotificacionCorrecta.mostrar("Cliente actualizado correctamente");
							}
						}else{ //No registro correctamente
							NotificacionError.mostrar("Error al registrar. Contacte al administrador", 10000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			
			btnEliminar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						
					//Confirmar
    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
    							"SI", "NO", new ConfirmDialog.Listener() {
									private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
    			                if (dialog.isConfirmed()) {
    			                	try {
										if(eliminar(idClienteActualizar)){
											actualizarElemento(paraActualizar);
											VentanaCliente.this.close();
											NotificacionCorrecta.mostrar("Cliente eliminado correctamente");
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
			
		}
		
}

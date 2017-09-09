package ventanas;

import java.sql.SQLException;

import org.vaadin.dialogs.ConfirmDialog;

import notificaciones.NotificacionCorrecta;
import notificaciones.NotificacionError;
import sql.DTO.VendedorDTO;

//import com.sun.javafx.beans.IDProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

import comboBox.ComboVendedor;

import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class VentanaVendedor extends Ventana{

	private static final long serialVersionUID = 1L;
		//Variables
			//VerticalLayout principal = new VerticalLayout();
			
			Button btnRegistrar = new Button("Registrar");
			Button btnEliminar = new Button("Eliminar");
			
			TabSheet tab = new TabSheet();
			//TabSheet tabEliminar = new TabSheet();
			
			GridLayout gridRegistrar = new GridLayout(1, 3);
			GridLayout gridEliminar = new GridLayout(1, 2);
			
			TextField nombre = new TextField("Nombre");
			TextArea observaciones = new TextArea("Observaciones");
			
			ComboVendedor comboVendedor = new ComboVendedor();
			
			VendedorDTO vendedor;
		
	//Constructores
		public VentanaVendedor(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idVendedorActualizar) throws SQLException{
			
			elementosFormato(secundaria, paraActualizar, esActualizacion, idVendedorActualizar);
			listeners(comboVendedor, idVendedorActualizar);

		}
	
	
	//Métodos
		private boolean registrar(){
			
			try{
			
				if(nombre.getValue().equals("")){
					
					Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
					return false;
					
				}else{ //Registro
					
					vendedor = new VendedorDTO();
					vendedor.setCustid(usuario.getCustid());
					vendedor.setNombre(nombre.getValue());
					vendedor.setObservaciones(observaciones.getValue());
					
					
					if(facade.registrarVendedor(vendedor)){
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
		
		private boolean eliminar(String idVendedor) throws SQLException{
			return facade.eliminarVendedor(usuario.getCustid(), idVendedor);
		}
		
		private void borrarCampos(){
			nombre.setValue("");
			observaciones.setValue("");
		}

	
	//
		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idClienteActualizar){
		
			try{
			
				VentanaVendedor.this.setCaption("Control de vendedores");
				VentanaVendedor.this.setContent(tab);
				
			//Formato de ventana
				VentanaVendedor.this.center();
				VentanaVendedor.this.setHeight("60%");
				VentanaVendedor.this.setWidth("40%");
			
			//Formato de Tabs
				tab.setSizeFull();
				tab.addTab(gridRegistrar,"Registrar vendedor");
				tab.addTab(gridEliminar,"Eliminar vendedor");
					
			//Color si es que es secundaria
				if(secundaria){
					tab.setStyleName("VentanaSecundaria");
				}

			//Ajuste de campos
				nombre.setWidth("100%"); nombre.setMaxLength(500);
				comboVendedor.setWidth("100%");
				observaciones.setWidth("100%");observaciones.setMaxLength(1000);
				
			//Agrego a grid
				gridRegistrar.setMargin(true);
				gridRegistrar.setWidth("100%");
				gridRegistrar.setHeight("100%");
					gridRegistrar.addComponent(nombre, 0, 0);
					gridRegistrar.addComponent(observaciones, 0, 1);
					gridRegistrar.addComponent(btnRegistrar, 0, 2);

				gridEliminar.setMargin(true);
				gridEliminar.setWidth("100%");
				gridEliminar.setHeight("50%");
					gridEliminar.addComponent(comboVendedor, 0, 0);
					gridEliminar.addComponent(btnEliminar, 0, 1);

				
			//Agrego Grid a Principal
				principal.addComponent(grid);
					principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
					
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		
		private void listeners(final Object paraActualizar, final String idVendedorActualizar){
			
			
			btnRegistrar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					
					if(nombre.getValue().equals("")){
						Notification.show("Se deben ingresar los datos completos", Type.WARNING_MESSAGE);
					}else{
						try {
							
							if(registrar()){
								if(paraActualizar != null){
									actualizarElemento(paraActualizar);
									borrarCampos();
									NotificacionCorrecta.mostrar("Vendedor registrado correctamente");
								}else{
									borrarCampos();
									NotificacionCorrecta.mostrar("Vendedor registrado correctamente");
								}
							}else{ //No registro correctamente
								NotificacionError.mostrar("Error al registrar. Contacte al administrador", 10000);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					}
				}
			});		
			
			btnEliminar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						
					//Confirmo que haya valor correcto
						if(comboVendedor.getValue() == null || comboVendedor.getItemCaption(comboVendedor.getValue()).equals("Ninguno")){
							Notification.show("El vendedor no se puede eliminar", Type.WARNING_MESSAGE);
						}else{
						
							//Confirmar
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;
		
									public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			                	try {
												if(eliminar(comboVendedor.getValue().toString())){
													actualizarElemento(paraActualizar);
													//VentanaVendedor.this.close();
													NotificacionCorrecta.mostrar("Vendedor eliminado correctamente");
												}else
													NotificacionError.mostrar("Contacte al administrador", 2000);
											} catch (Exception e) {
												e.printStackTrace();
												throw new RuntimeException(e);
											}
		    			                }
		    			            }
		    					});
						
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
		}
		
}

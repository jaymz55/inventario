package ventanas;

import java.sql.SQLException;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.ui.NumberField;

import notificaciones.NotificacionCorrecta;
import notificaciones.NotificacionError;
import sql.DTO.MaterialDTO;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import autocomplete.AutoCompleteCategorias;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

import comboBox.ComboProveedores;
import comboBox.ComboUnidadMedida;
import com.vaadin.ui.TextField;

public class VentanaMateriales extends Ventana{

	private static final long serialVersionUID = 1L;
		//Variables
			
			Button btnRegistrar = new Button("Registrar");
			Button btnEliminar = new Button("Eliminar");
			Button btnActualizar = new Button("Actualizar");
			Button btnAgregarProveedor = new Button("Agregar proveedor");

			GridLayout grid = new GridLayout(2, 5);
			TextField nombre = new TextField("Cliente");
			TextField sku = new TextField("SKU");
			ComboProveedores proveedores = new ComboProveedores();
			AutoCompleteCategorias categoria = new AutoCompleteCategorias(usuario.getCustid(), "Categorías");
			NumberField minimo = new NumberField("Cantidad mínima");
			NumberField maximo = new NumberField("Cantidad máxima");
			
			ComboUnidadMedida comboUnidadMedida = new ComboUnidadMedida();

			MaterialDTO material;
		
	//Constructores
		public VentanaMateriales(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idMaterialActualizar) throws SQLException{
			
			
			elementosFormato(secundaria, paraActualizar, esActualizacion, idMaterialActualizar);
			listeners(paraActualizar, idMaterialActualizar);

		}
	
	
	//Métodos
		private boolean registrar(){
			
			try{
			
				if(nombre.getValue().equals("")){
					
					Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
					return false;
					
				}else{ //Registro
					
					material = new MaterialDTO();
					material.setCustid(usuario.getCustid());
					material.setCategoria(categoria.getValue());
					material.setNombre(nombre.getValue());
					material.setSku(sku.getValue());
					material.setMinimo(minimo.getValue());
					material.setMaximo(maximo.getValue());
					material.setProveedor(proveedores.getValue().toString());
					material.setUnidadMedida(comboUnidadMedida.getValue().toString());
					material.setActivo("SI");
					
					if(facade.registrarMaterial(material)){
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
		
		private boolean actualizarMaterial(String idMaterialActualizar) throws SQLException{
			
			if(nombre.getValue().equals("")){
				
				Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
				return false;
				
			}else{ //Registro
				
				material = new MaterialDTO();
				material.setIdMaterial(idMaterialActualizar);
				material.setCustid(usuario.getCustid());
				material.setCategoria(categoria.getValue());
				material.setNombre(nombre.getValue());
				material.setSku(sku.getValue());
				material.setMinimo(minimo.getValue());
				material.setMaximo(maximo.getValue());
				

				if(proveedores.getValue() != null)
					material.setProveedor(proveedores.getValue().toString());
				
				material.setUnidadMedida(comboUnidadMedida.getValue().toString());

				if(facade.actualizarMaterial(material)){
					return true;
				}else{
					return false;
				}
				
			}
			
		}
		
		private boolean eliminar(String idMaterial) throws SQLException{
			return facade.eliminarMaterial(usuario.getCustid(), idMaterial);
		}
		
		private void borrarCampos(){
			nombre.setValue("");
			sku.setValue("");
			proveedores.setValue(null);
			categoria.setValue("");
			minimo.setValue("");
			maximo.setValue("");
		}
		
		private void llenarCampos(String idMaterial) throws SQLException{
			
			MaterialDTO material = facade.obtenerMaterial(usuario.getCustid(), idMaterial);
			
				nombre.setValue(material.getNombre());
				sku.setValue(material.getSku());
				proveedores.setValue(material.getProveedor());
				categoria.setValue(material.getCategoria());
				minimo.setValue(material.getMinimo());
				maximo.setValue(material.getMaximo());
				comboUnidadMedida.setValue(material.getUnidadMedida());
			
			material = null;
				
		}
	
	//
		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idMaterialActualizar){
		
			try{
			
				VentanaMateriales.this.setCaption("Registrar material");
				VentanaMateriales.this.setContent(principal);
				
			//Formato de ventana
				VentanaMateriales.this.center();
				VentanaMateriales.this.setHeight("70%");
				VentanaMateriales.this.setWidth("80%");
			
				if(esActualizacion){
					VentanaMateriales.this.setCaption("Actualizar material");
					llenarCampos(idMaterialActualizar);
				}else
					VentanaMateriales.this.setCaption("Registrar nuevo material");
				
				//Color si es que es secundaria
				if(secundaria){
					grid.setStyleName("VentanaSecundaria");
				}

			//Ajuste de campos
				nombre.setWidth("80%");nombre.setMaxLength(200);
				sku.setMaxLength(200);sku.setWidth("80%");
				proveedores.setWidth("80%");
				minimo.setDecimalPrecision(3);
				maximo.setDecimalPrecision(3);

			//Ajuste de boton secundario
				btnAgregarProveedor.setStyleName(ValoTheme.BUTTON_LINK);
				
			//Agrego a grid
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(nombre, 0, 0);
				grid.addComponent(sku, 1, 0);
				grid.addComponent(proveedores, 0, 1);
				grid.addComponent(categoria, 1, 1);
				grid.addComponent(minimo, 0, 2);
				grid.addComponent(btnAgregarProveedor, 1, 2);
					grid.setComponentAlignment(btnAgregarProveedor, Alignment.TOP_LEFT);
				grid.addComponent(maximo, 0, 3);
				grid.addComponent(comboUnidadMedida, 1, 3);
				
				if(esActualizacion){
					grid.addComponent(btnActualizar, 0, 4);
					grid.addComponent(btnEliminar, 1, 4);
				}else{
					grid.addComponent(btnRegistrar, 0, 4);
				}
				
			//Agrego Grid a Principal
				principal.addComponent(grid);
					principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
					
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		
		private void listeners(final Object paraActualizar, final String idMaterialActualizar){
			
			
			btnRegistrar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						if(registrar()){
							if(paraActualizar != null){
								actualizarElemento(paraActualizar);
								borrarCampos();
								NotificacionCorrecta.mostrar("Material registrado correctamente");
							}else{
								borrarCampos();
								NotificacionCorrecta.mostrar("Material registrado correctamente");
							}
						}else{ //No registro correctamente
							NotificacionError.mostrar("Contacte al administrador", 10000);
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
						if(actualizarMaterial(idMaterialActualizar)){
							if(paraActualizar != null){
								actualizarElemento(paraActualizar);
								borrarCampos();
								VentanaMateriales.this.close();
								NotificacionCorrecta.mostrar("Material actualizado correctamente");
							}else{
								borrarCampos();
								VentanaMateriales.this.close();
								NotificacionCorrecta.mostrar("Material actualizado correctamente");
							}
						}else{ //No registro correctamente
							NotificacionError.mostrar("Contacte al administrador", 10000);
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
										if(eliminar(idMaterialActualizar)){
											actualizarElemento(paraActualizar);
											VentanaMateriales.this.close();
											NotificacionCorrecta.mostrar("Material eliminado correctamente");
										}else
											NotificacionError.mostrar("Contacte al administrador", 2000);
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
			
			btnAgregarProveedor.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						
						UI.getCurrent().addWindow(new VentanaProveedor(true, proveedores, false, null));
						
					} catch (Exception e) {
						e.printStackTrace();
						NotificacionError.mostrar("Contacte al administrador", 3000);
					}
				}
			});
			
			
		}
		
}

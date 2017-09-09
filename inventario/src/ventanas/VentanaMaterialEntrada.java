package ventanas;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification.Type;

import comboBox.ComboMateriales;
import funciones.Funcion;

public class VentanaMaterialEntrada extends Ventana{

	private static final long serialVersionUID = 1L;
		//Variables
			//VerticalLayout principal = new VerticalLayout();
			
			Button btnRegistrar = new Button("Registrar");
			Button btnEliminar = new Button("Eliminar");
			Button btnActualizar = new Button("Actualizar");
			
			GridLayout grid = new GridLayout(2, 4);
			ComboMateriales nombre = new ComboMateriales();
			NumberField cantidad = new NumberField("Cantidad");
			NumberField costo = new NumberField("Costo por unidad");
			DateField fechaIngreso = new DateField("Fecha de ingreso");
			DateField fechaCaducidad = new DateField("Fecha de caducidad");
			CheckBox contieneIva = new CheckBox("¿Contiene IVA?");
			MaterialDTO material;
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
	//Constructores
		public VentanaMaterialEntrada(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idEntradaActualizar) throws SQLException{
			
			
			elementosFormato(secundaria, paraActualizar, esActualizacion, idEntradaActualizar);
			listeners(paraActualizar, idEntradaActualizar);

		}
	
	
	//Métodos
		private boolean registrar(){
			
			try{
			
				if(nombre.getValue() == null || cantidad.getValue().equals("")
						|| costo.getValue().equals("") || fechaIngreso.getValue() == null){
					
					Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
					return false;
					
				}else{ //Registro
					
					material = new MaterialDTO();
					material.setIdMaterial(nombre.getValue().toString());
					material.setCustid(usuario.getCustid());
					material.setCantidad(cantidad.getValue());
					material.setCosto(costo.getDoubleValueDoNotThrow());
					material.setFechaIngreso(Funcion.fechaFormato(fechaIngreso.getValue(), "yyyy-MM-dd"));
					if(fechaCaducidad.getValue() != null)
						material.setFechaIngreso(Funcion.fechaFormato(fechaCaducidad.getValue(), "yyyy-MM-dd"));

					if(contieneIva.getValue())
						material.setContieneIva("SI");
					else
						material.setContieneIva("NO");
					
					if(facade.registrarEntradaMaterial(material)){
						return true;
					}else{
						return false;
					}
					
				}
			
			}catch(Exception e){
				NotificacionError.mostrar(e.toString(), 5000);
				e.printStackTrace();
				return false;
			}
			
		}
		
		private boolean actualizarMaterial(String idMaterialActualizar) throws SQLException{
				
			try{
			
				material = new MaterialDTO();
				material.setIdEntradaInventario(idMaterialActualizar);
				material.setCustid(usuario.getCustid());
				material.setCantidad(cantidad.getValue());
				material.setCosto(costo.getDoubleValueDoNotThrow());
				material.setFechaIngreso(Funcion.fechaFormato(fechaIngreso.getValue(), "yyyy-MM-dd"));
				if(fechaCaducidad.getValue() != null)
					material.setFechaIngreso(Funcion.fechaFormato(fechaCaducidad.getValue(), "yyyy-MM-dd"));

				if(contieneIva.getValue())
					material.setContieneIva("SI");
				else
					material.setContieneIva("NO");
				
				if(facade.actualizarEntradaMaterial(material.getCustid(), material.getIdMaterial(), 
						material.getFechaIngreso(), material.getFechaCaducidad(), material.getCantidad(), 
						Double.toString(material.getCosto()), material.getContieneIva(), material.getIdEntradaInventario())){
					return true;
				}else{
					return false;
				}
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
				return false;
			}
			
		}
		
		private boolean eliminar(String idCliente) throws SQLException{
			return facade.eliminarCliente(usuario.getCustid(), idCliente);
		}
		
		private void borrarCampos(){
			nombre.setValue(null);
			cantidad.setValue("");
			costo.setValue("");
			fechaCaducidad.setValue(null);
		}
		
		private void llenarCampos(String idEntradaInventario) throws SQLException{
			
			MaterialDTO material = facade.obtenerEntradaMaterial(usuario.getCustid(), idEntradaInventario);

				try {
					nombre.setValue(material.getIdMaterial());
					cantidad.setValue(material.getCantidad());
					costo.setValue(material.getTotal());
					fechaIngreso.setValue(dateFormat.parse(material.getFechaIngreso()));
					
					if(material.getFechaCaducidad() != null)
						fechaCaducidad.setValue(dateFormat.parse(material.getFechaCaducidad()));
					
					if(material.getContieneIva().equals("SI"))
						contieneIva.setValue(true);
					else
						contieneIva.setValue(false);
					
				} catch (Exception e) {
					e.printStackTrace();
					NotificacionError.mostrar(e.toString(), 5000);
				}
			
			material = null;
				
		}
	
	//
		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idEntradaActualizar){
		
			try{
			
				VentanaMaterialEntrada.this.setCaption("Registrar cliente");
				VentanaMaterialEntrada.this.setContent(principal);
				
			//Formato de ventana
				VentanaMaterialEntrada.this.center();
				VentanaMaterialEntrada.this.setHeight("60%");
				VentanaMaterialEntrada.this.setWidth("50%");

				if(esActualizacion){
					VentanaMaterialEntrada.this.setCaption("Actualizar entrada");
					llenarCampos(idEntradaActualizar);
				}else
					VentanaMaterialEntrada.this.setCaption("Registrar nueva entrada");
				
				//Color si es que es secundaria
				if(secundaria){
					grid.setStyleName("VentanaSecundaria");
				}

			//Ajuste de campos
				//correo.setWidth("80%");correo.setMaxLength(500);
				contieneIva.setValue(true);
				fechaIngreso.setValue(new Date());
				
				
			//Agrego a grid
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(nombre, 0, 0);
				grid.addComponent(costo, 1, 0);
					grid.setComponentAlignment(costo, Alignment.TOP_CENTER);
				grid.addComponent(cantidad, 0, 1);
				grid.addComponent(contieneIva, 1, 1);
					grid.setComponentAlignment(contieneIva, Alignment.MIDDLE_CENTER);
				grid.addComponent(fechaIngreso, 0, 2);
				grid.addComponent(fechaCaducidad, 1, 2);
					grid.setComponentAlignment(fechaCaducidad, Alignment.TOP_CENTER);
				
				if(esActualizacion){
					grid.addComponent(btnActualizar, 0, 3);
					grid.addComponent(btnEliminar, 1, 3);
						grid.setComponentAlignment(btnEliminar, Alignment.TOP_CENTER);
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
		
		private void listeners(final Object paraActualizar, final String idEntradaActualizar){
			
			
			btnRegistrar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						
						if(nombre.getValue() == null || cantidad.getValue().equals("")
								|| costo.getValue().equals("") || fechaIngreso.getValue() == null){
							
							Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
							
						}else{ //Registro
						
							if(registrar()){
								if(paraActualizar != null){
									actualizarElemento(paraActualizar);
									borrarCampos();
									NotificacionCorrecta.mostrar("Entrada registrada correctamente");
								}else{
									borrarCampos();
									NotificacionCorrecta.mostrar("Entrada registrada correctamente");
								}
							}else{ //No registro correctamente
								NotificacionError.mostrar("Contacte al administrador", 5000);
							}
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
						if(actualizarMaterial(idEntradaActualizar)){
							if(paraActualizar != null){
								actualizarElemento(paraActualizar);
								borrarCampos();
								VentanaMaterialEntrada.this.close();
								NotificacionCorrecta.mostrar("Cliente actualizado correctamente");
							}else{
								borrarCampos();
								VentanaMaterialEntrada.this.close();
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
										if(eliminar(idEntradaActualizar)){
											actualizarElemento(paraActualizar);
											VentanaMaterialEntrada.this.close();
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

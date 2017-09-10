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
import com.vaadin.ui.Notification.Type;

import comboBox.ComboMateriales;
import fechas.CampoFecha;
import funciones.Funcion;

public class VentanaMerma extends Ventana{

	private static final long serialVersionUID = 1L;
		//Variables
			//VerticalLayout principal = new VerticalLayout();
			
			Button btnRegistrar = new Button("Registrar");
			Button btnEliminar = new Button("Eliminar");
			Button btnActualizar = new Button("Actualizar");
			
			GridLayout grid = new GridLayout(2, 3);
			ComboMateriales nombre = new ComboMateriales();
			NumberField cantidad = new NumberField("Cantidad");
			CampoFecha fechaMerma = new CampoFecha("Fecha");
			MaterialDTO material;
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
	//Constructores
		public VentanaMerma(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idMermaActualizar) throws SQLException{
			
			elementosFormato(secundaria, paraActualizar, esActualizacion, idMermaActualizar);
			listeners(paraActualizar, idMermaActualizar);

		}
	
	//M�todos
		private boolean registrar(){
			
			try{
			
					material = new MaterialDTO();
					material.setIdMaterial(nombre.getValue().toString());
					material.setCustid(usuario.getCustid());
					material.setIdMaterial(nombre.getValue().toString());
					material.setFechaMerma(Funcion.fechaFormato(fechaMerma.getValue(), "yyyy-MM-dd"));
					material.setCantidad(String.valueOf(Double.parseDouble(cantidad.getValue())*-1));
					material.setMovimiento("MERMA");
					
					if(facade.registrarMerma(material.getCustid(), material)){
						return true;
					}else{
						return false;
					}
			
			
			}catch(Exception e){
				NotificacionError.mostrar(e.toString(), 5000);
				e.printStackTrace();
				return false;
			}
			
		}
		
		private boolean actualizarMerma(String idMermaActualizar) throws SQLException{
				
			try{
			
				material = new MaterialDTO();
				material.setIdMaterial(nombre.getValue().toString());
				material.setCustid(usuario.getCustid());
				material.setIdMaterial(nombre.getValue().toString());
				material.setFechaMerma(Funcion.fechaFormato(fechaMerma.getValue(), "yyyy-MM-dd"));
				material.setCantidad(String.valueOf(Double.parseDouble(cantidad.getValue())*-1));
				material.setIdEntradaInventario(idMermaActualizar);
				
				if(facade.actualizarMerma(material.getCustid(), material)){
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
		
		private boolean eliminar(String idMerma) throws SQLException{
			return facade.eliminarMerma(usuario.getCustid(), idMerma);
		}
		
		private void borrarCampos(){
			nombre.setValue(null);
			cantidad.setValue("");
			fechaMerma.setValue(null);
		}
		
		private void llenarCampos(String idMermaInventario) throws SQLException{
			
			MaterialDTO material = facade.obtenerMerma(usuario.getCustid(), idMermaInventario);

				try {

					nombre.setValue(material.getIdMaterial());
					cantidad.setValue(String.valueOf(Double.parseDouble(material.getCantidad())*-1));
					fechaMerma.setValue(dateFormat.parse(material.getFechaMerma()));
					
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
			
				VentanaMerma.this.setCaption("Registrar merma");
				VentanaMerma.this.setContent(principal);
				
			//Formato de ventana
				VentanaMerma.this.center();
				VentanaMerma.this.setHeight("50%");
				VentanaMerma.this.setWidth("40%");

				if(esActualizacion){
					VentanaMerma.this.setCaption("Actualizar merma");
					llenarCampos(idEntradaActualizar);
				}else
					VentanaMerma.this.setCaption("Registrar nueva merma");
				
				//Color si es que es secundaria
				if(secundaria){
					grid.setStyleName("VentanaSecundaria");
				}

			//Ajuste de campos
				//correo.setWidth("80%");correo.setMaxLength(500);
				fechaMerma.setValue(new Date());
				//fechaMerma.setDateFormat("dd MMM yyyy");
				
				
			//Agrego a grid
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(nombre, 0, 0);
				grid.addComponent(cantidad, 1, 0);
					grid.setComponentAlignment(cantidad, Alignment.TOP_CENTER);
				grid.addComponent(fechaMerma, 1, 1);
					grid.setComponentAlignment(fechaMerma, Alignment.MIDDLE_CENTER);
				
				if(esActualizacion){
					grid.addComponent(btnActualizar, 0, 2);
					grid.addComponent(btnEliminar, 1, 2);
						grid.setComponentAlignment(btnActualizar, Alignment.MIDDLE_CENTER);
						grid.setComponentAlignment(btnEliminar, Alignment.MIDDLE_CENTER);
				}else{
					grid.addComponent(btnRegistrar, 0, 2);
						grid.setComponentAlignment(btnRegistrar, Alignment.TOP_CENTER);
				}
				
				
			//Agrego Grid a Principal
				principal.addComponent(grid);
					principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
					
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		
		private void listeners(final Object paraActualizar, final String idMermaActualizar){
			
			
			btnRegistrar.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						
						if(nombre.getValue() == null || cantidad.getValue().equals("")
								|| fechaMerma.getValue() == null){
							
							Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
							
						}else{ //Registro
						
							if(registrar()){
								if(paraActualizar != null){
									actualizarElemento(paraActualizar);
									borrarCampos();
									NotificacionCorrecta.mostrar("Merma registrada correctamente");
								}else{
									borrarCampos();
									NotificacionCorrecta.mostrar("Merma registrada correctamente");
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
						if(actualizarMerma(idMermaActualizar)){
							if(paraActualizar != null){
								actualizarElemento(paraActualizar);
								borrarCampos();
								VentanaMerma.this.close();
								NotificacionCorrecta.mostrar("Merma actualizada correctamente");
							}else{
								borrarCampos();
								VentanaMerma.this.close();
								NotificacionCorrecta.mostrar("Merma actualizada correctamente");
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
    					ConfirmDialog.show(UI.getCurrent(), "Confirmaci�n", "�Est�s seguro de querer eliminarlo?",
    							"SI", "NO", new ConfirmDialog.Listener() {
									private static final long serialVersionUID = 1L;

							public void onClose(ConfirmDialog dialog) {
    			                if (dialog.isConfirmed()) {
    			                	try {
										if(eliminar(idMermaActualizar)){
											actualizarElemento(paraActualizar);
											VentanaMerma.this.close();
											NotificacionCorrecta.mostrar("Merma eliminada correctamente");
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
			
		}
		
}

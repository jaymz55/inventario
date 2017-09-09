package ventanas;

import java.sql.SQLException;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.ui.NumberField;

import notificaciones.NotificacionCorrecta;
import notificaciones.NotificacionError;
import sql.DTO.MaterialDTO;
import sql.DTO.ProductoDTO;
import tablas.TablaComponentes;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification.Type;

import comboBox.ComboMateriales;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class VentanaProductos extends Ventana{

	private static final long serialVersionUID = 1L;
		
		//Variables
			//Botones
			Button btnRegistrar = new Button("Registrar");
			Button btnAgregarElemento = new Button("Agregar");
			Button btnEliminarElemento = new Button("Eliminar");
			Button btnAgregarMaterial = new Button("Alta de material");
			
			Button btnActualizarProducto = new Button("Actualizar");
			Button btnEliminarProducto = new Button("Eliminar");
			
			TabSheet tab = new TabSheet();
			
			GridLayout gridInformacion = new GridLayout(2, 5);
			GridLayout gridComponentes = new GridLayout(2, 6);
			
			TextField nombre = new TextField("Nombre de producto");
			NumberField precio = new NumberField("Precio");
			CheckBox gravaIva = new CheckBox("¿Grava IVA?");
			TextArea descripcion = new TextArea("Descripción");
			NumberField minimo = new NumberField("Mínimo");
			NumberField maximo = new NumberField("Máximo");

			ComboMateriales comboMateriales = new ComboMateriales();
			NumberField cantidad = new NumberField("Cantidad");
			TablaComponentes tablaComponentes;
			Label lblCostoTotal = new Label("Costo total: $");
			
			MaterialDTO material;
			ProductoDTO producto;
		
	//Constructores
		public VentanaProductos(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idProductoActualizar) throws SQLException{
			
			elementosFormato(secundaria, paraActualizar, esActualizacion, idProductoActualizar);
			listeners(comboMateriales, idProductoActualizar);

		}
	
	
	//Métodos
		private boolean registrar(){
			
			try{
			
				if(nombre.getValue().equals("")){
					
					Notification.show("Se deben de ingresar los datos completos", Type.WARNING_MESSAGE);
					return false;
					
				}else{ //Registro
					
					producto = new ProductoDTO();
					producto.setCustid(usuario.getCustid());
					producto.setNombre(nombre.getValue());
					producto.setDescripcion(descripcion.getValue());
					
					
					if(facade.registrarProducto(producto)){
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
			descripcion.setValue("");
		}

	
	//
		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idClienteActualizar){
		
			try{
			
				VentanaProductos.this.setCaption("Registro de productos");
				VentanaProductos.this.setContent(tab);
				
			//Formato de ventana
				VentanaProductos.this.center();
				VentanaProductos.this.setHeight("90%");
				VentanaProductos.this.setWidth("70%");
			
			//Formato de Tabs
				tab.setSizeFull();
				tab.addTab(gridInformacion,"Información");
				tab.addTab(gridComponentes,"Componentes");
					
			//Color si es que es secundaria
				if(secundaria){
					tab.setStyleName("VentanaSecundaria");
				}

			//Ajuste de campos
				nombre.setWidth("100%"); nombre.setMaxLength(500);
				gravaIva.setValue(true);
				descripcion.setWidth("100%");descripcion.setMaxLength(500);
				tablaComponentes = new TablaComponentes(usuario.getCustid());
				btnAgregarMaterial.setStyleName(ValoTheme.BUTTON_LINK);
				
			//Agrego a grid
				gridInformacion.setMargin(true);
				gridInformacion.setWidth("100%");
				gridInformacion.setHeight("100%");
					gridInformacion.addComponent(nombre, 0, 0, 1, 0);
					gridInformacion.addComponent(precio, 0, 1);
					gridInformacion.addComponent(gravaIva, 1, 1);
					gridInformacion.addComponent(descripcion, 0, 2, 1, 2);
					gridInformacion.addComponent(minimo, 0, 3);
					gridInformacion.addComponent(maximo, 1, 3);
					gridInformacion.addComponent(btnRegistrar, 0, 4);

				gridComponentes.setMargin(true);
				gridComponentes.setWidth("100%");
				gridComponentes.setHeight("100%");
					gridComponentes.addComponent(comboMateriales, 0, 0, 1, 0);
					gridComponentes.addComponent(btnAgregarMaterial, 1, 1);
						gridComponentes.setComponentAlignment(btnAgregarMaterial, Alignment.MIDDLE_CENTER);
					gridComponentes.addComponent(cantidad, 0, 2, 1, 2);
					gridComponentes.addComponent(tablaComponentes, 0, 3, 1, 3);
					gridComponentes.addComponent(lblCostoTotal, 0, 4);
					gridComponentes.addComponent(btnAgregarElemento, 0, 5);
					gridComponentes.addComponent(btnEliminarElemento, 1, 5);

				
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
			
			btnEliminarProducto.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					try {
						
					//Confirmo que haya valor correcto
						if(comboMateriales.getValue() == null || comboMateriales.getItemCaption(comboMateriales.getValue()).equals("Ninguno")){
							Notification.show("El vendedor no se puede eliminar", Type.WARNING_MESSAGE);
						}else{
						
							//Confirmar
		    					ConfirmDialog.show(UI.getCurrent(), "Confirmación", "¿Estás seguro de querer eliminarlo?",
		    							"SI", "NO", new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;
		
									public void onClose(ConfirmDialog dialog) {
		    			                if (dialog.isConfirmed()) {
		    			                	try {
												if(eliminar(comboMateriales.getValue().toString())){
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
			
		//Botones de tabla
			btnAgregarElemento.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {
					
					if(revisarParaAgregar()){
						try {

							material = new MaterialDTO();
							material = facade.obtenerMaterial(usuario.getCustid(), comboMateriales.getValue().toString());
							
							//Agrego el valor de Cantidad
								material.setCantidad(cantidad.getValue());
							
							//Mando a agregar a la tabla
							tablaComponentes.agregarComponente(usuario, material);
							
							lblCostoTotal.setValue("Costo total: $"+tablaComponentes.obtenerCostoTotal());
						
						} catch (SQLException e) {
							e.printStackTrace();
							NotificacionError.mostrar(e.toString(), 5000);
						}

						
					}
					
				}
			});
			
		}
		
		//Metodos
		private boolean revisarParaAgregar(){
			
			try{
				
				if(comboMateriales.getValue() == null || cantidad.getValue().equals("")){
					Notification.show("Se deben agregar todos los valores", Type.WARNING_MESSAGE);
					return false;
				}else{
					return true;
				}
				
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
			
		}
		
		
}

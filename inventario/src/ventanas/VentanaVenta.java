package ventanas;

import java.sql.SQLException;

import org.vaadin.ui.NumberField;

import sql.DTO.VentaDTO;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import comboBox.ComboClientes;
import comboBox.ComboProductos;
import comboBox.ComboVendedor;
import notificaciones.NotificacionError;

@SuppressWarnings("serial")
public class VentanaVenta extends Ventana{

		//Variables
			//Layouts
				HorizontalLayout layoutDescuentos = new HorizontalLayout();
				VerticalLayout layoutClientes = new VerticalLayout();
				VerticalLayout popupContent = new VerticalLayout();
	
			//Botones
				Button btnRegistrar = new Button("Registrar");
				Button btnCliente = new Button("Alta de cliente");
			
			//Campos
				ComboProductos productos = new ComboProductos();
				NumberField cantidad = new NumberField("Cantidad");
				DateField fechaVenta = new DateField("Fecha de venta");
				ComboClientes clientes = new ComboClientes();
				
				ComboVendedor vendedores = new ComboVendedor();
				NumberField precio = new NumberField("Precio");
				NumberField descuento = new NumberField("Descuento");
				TextArea descComentario = new TextArea("Comentario");
				PopupView popup = new PopupView("Comentario del descuento",popupContent);
				NumberField existencia = new NumberField("Existencia");
				TextArea comentarios = new TextArea("Comentarios");
				
			//Otros
				VentaDTO venta;

	//Constructores
		public VentanaVenta(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idVentaActualizar) throws SQLException{
			
				elementosFormato(secundaria, paraActualizar, esActualizacion, idVentaActualizar);
				listeners();
					
		}
	
	
	//Métodos
		private boolean registrar(){
			
			try{
			
				
				
				return true;
				
			}catch(Exception e){
				e.printStackTrace();
				NotificacionError.mostrar(e.toString(), 5000);
				return false;
			}
			
			
		}

		
		/*private boolean actualizarElemento(Object paraActualizar) throws UnsupportedOperationException, Exception{
			
			if(paraActualizar.getClass().equals(ComboBox.class)){
				System.out.println("Es un combo");
			}else if(paraActualizar.getClass().equals(TablaProveedores.class)){
				//Actualizo la tabla
					TablaProveedores tabla = TablaProveedores.class.cast(paraActualizar);
					tabla.actualizarTabla(tabla, usuario.getCustid());
			}
			
			return true;
		}*/
		
		private boolean eliminar(String idProveedor) throws SQLException{
			return facade.eliminarProveedor(usuario.getCustid(), idProveedor);
		}
		
		private void borrarCampos(){
			productos.setValue("");
			cantidad.setValue("");
			clientes.setValue("");
			vendedores.setValue("");
			precio.setValue("");
			descuento.setValue("");
			descComentario.setValue("");
			existencia.setValue("");
			comentarios.setValue("");
		}
		

		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idVentaActualizar){
			
			try{
				//Content
					VentanaVenta.this.setCaption("Registrar salida / venta");
					VentanaVenta.this.setContent(principal);
				//Posicion
					VentanaVenta.this.center();
					VentanaVenta.this.setHeight("70%");
					VentanaVenta.this.setWidth("80%");
				//Layouts					
					layoutDescuentos.setWidth("100%");
					layoutDescuentos.setHeight("100%");
					
					popupContent.setMargin(true);

						layoutDescuentos.addComponent(precio);
						layoutDescuentos.addComponent(descuento);
						layoutDescuentos.addComponent(popup);
							layoutDescuentos.setComponentAlignment(popup, Alignment.MIDDLE_CENTER);

					layoutClientes.setWidth("100%");
					layoutClientes.setHeight("100%");
						layoutClientes.addComponent(clientes);
						layoutClientes.addComponent(btnCliente);
							layoutClientes.setComponentAlignment(btnCliente, Alignment.MIDDLE_CENTER);
						
					
				//Grid
					grid = new GridLayout(2, 5);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.setMargin(true);
						
					grid.addComponent(productos,0, 0);
					grid.addComponent(vendedores, 1, 0);
					grid.addComponent(cantidad, 0, 1);
					grid.addComponent(layoutDescuentos, 1, 1);
					grid.addComponent(fechaVenta, 0, 2);
					grid.addComponent(existencia, 1, 2);
					grid.addComponent(layoutClientes, 0, 3);
					grid.addComponent(comentarios, 1, 3);
					grid.addComponent(btnRegistrar, 0, 4);
					
					//grid.setStyleName(ValoTheme.LAYOUT_WELL);

				
				//Agrego a principal
					principal.addComponent(grid);
						principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
			
				//Color si es que es secundaria
					if(secundaria){
						grid.setStyleName("VentanaSecundaria");
					}
					
				//Boton alta cliente como link
					btnCliente.setStyleName(ValoTheme.BUTTON_LINK);
					
					productos.setWidth("70%");
					vendedores.setWidth("70%");
					clientes.setWidth("70%");
					comentarios.setWidth("70%");
					
					
				//Ajustes de campo Precio y descuento
					precio.setNullRepresentation("");
					precio.setDecimalPrecision(2);
					precio.setErrorText("Número no válido");
					precio.setInvalidAllowed(false);
					precio.setNegativeAllowed(false);
					precio.setWidth("50%");
					
					descuento.setNullRepresentation("");
					descuento.setDecimalPrecision(2);
					descuento.setErrorText("Número no válido");
					descuento.setInvalidAllowed(false);
					descuento.setNegativeAllowed(false);
					descuento.setWidth("50%");
					
				//Ajustes descuento comentario
					descComentario.setRows(5);
					descComentario.setColumns(30);
					descComentario.setMaxLength(500);
					popupContent.addComponent(descComentario);
					popupContent.setComponentAlignment(descComentario,
							Alignment.MIDDLE_CENTER);
					popup.setHideOnMouseOut(false);
					
				//Ajuste de existencia
					existencia.setEnabled(false);
					
				
			}catch(Exception e){
				NotificacionError.mostrar(e.toString(), 3000);
				e.printStackTrace();
			}
				
		}
		
		private void listeners(){
			
			//Boton registrar
				btnRegistrar.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							registrar();
						} catch (Exception e) {
							NotificacionError.mostrar(e.toString(), 3000);
							e.printStackTrace();
						}
					}
				});
				
				btnCliente.addClickListener(new Button.ClickListener() {
					private static final long serialVersionUID = 1L;
	
					public void buttonClick(ClickEvent event) {
						try {
							UI.getCurrent().addWindow(new VentanaCliente(true, clientes, false, null));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			
		}
		
}

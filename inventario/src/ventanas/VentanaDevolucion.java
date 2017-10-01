package ventanas;

import java.sql.SQLException;

import org.vaadin.ui.NumberField;

import sql.DTO.UsuarioDTO;
import sql.DTO.VentaDTO;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import comboBox.ComboClientes;
import comboBox.ComboProductos;
import facade.Facade;
import fechas.CampoFecha;
import funciones.Funcion;
import notificaciones.NotificacionCorrecta;
import notificaciones.NotificacionError;

@SuppressWarnings("serial")
public class VentanaDevolucion extends Ventana{

		//Variables
			//Layouts
	
			//Botones
				Button btnRegistrar = new Button("Registrar devolución");
			
			//Campos
				ComboProductos productos = new ComboProductos();
				NumberField cantidad = new NumberField("Cantidad");
				CampoFecha fechaDevolucion = new CampoFecha("Fecha");
				ComboClientes clientes = new ComboClientes();
				
				CheckBox chkRegresaAlmacen = new CheckBox("Regresa a almacén");
				NumberField descuento = new NumberField("Descuento");
				TextArea comentarios = new TextArea("Comentarios");
				
			//Otros
				VentaDTO venta;
				Facade facade = new Facade();
				final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
				
	//Constructores
		public VentanaDevolucion(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idVentaActualizar) throws SQLException{
			
				elementosFormato(secundaria, paraActualizar, esActualizacion, idVentaActualizar);
				listeners();
					
		}
	
	
	//Metodos
		private boolean registrar(){
			
			try{
			
				//Lleno la informacion
					venta = new VentaDTO();
					venta.setCustid(usuario.getCustid());
					venta.setIdProducto(productos.getValue().toString());
					venta.setCantidad(Integer.parseInt(cantidad.getValue()));
					venta.setFecha(Funcion.fechaFormato(fechaDevolucion.getValue(), "yyyy-MM-dd"));
					venta.setIdCliente(clientes.getValue().toString());
					//Primero descuento
						venta.setDescuento(descuento.getDoubleValueDoNotThrow());
						venta.setTotal(facade.obtenerPrecioProducto(usuario.getCustid(), productos.getValue().toString())*-1);
						
					venta.setGravaIva(facade.obtenerGravaIva(usuario.getCustid(), productos.getValue().toString()));
					venta.setComentarios(comentarios.getValue());
					venta.setPagada("NO");
					venta.setAfectaAlmacen("SI");
					venta.setMovimiento("DEVOLUCIÓN");
					
					//Registro
						return facade.registrarVenta(venta);
				
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
		
		/*private boolean eliminar(String idVenta) throws SQLException{
			return facade.eliminarProveedor(usuario.getCustid(), idVenta);
		}*/
		
		private void borrarCampos(){
			productos.setValue(null);
			cantidad.setValue("");
			clientes.setValue(null);
			descuento.setValue("");
			chkRegresaAlmacen.setValue(false);
			comentarios.setValue("");
		}
		
		private void elementosFormato(boolean secundaria, final Object paraActualizar, boolean esActualizacion, 
				final String idVentaActualizar){
			
			try{
				//Content
					VentanaDevolucion.this.setCaption("Registrar devolución");
					VentanaDevolucion.this.setContent(principal);
				//Posicion
					VentanaDevolucion.this.center();
					VentanaDevolucion.this.setHeight("50%");
					VentanaDevolucion.this.setWidth("70%");
				//Layouts					
						
					
				//Grid
					grid = new GridLayout(4, 3);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.setMargin(true);
						
					grid.addComponent(productos,0, 0);
						grid.setComponentAlignment(productos, Alignment.TOP_CENTER);
					grid.addComponent(cantidad, 1, 0);
						grid.setComponentAlignment(cantidad, Alignment.TOP_CENTER);
					grid.addComponent(descuento, 2, 0);
						grid.setComponentAlignment(descuento, Alignment.TOP_CENTER);
					grid.addComponent(fechaDevolucion, 3, 0);
						grid.setComponentAlignment(fechaDevolucion, Alignment.TOP_CENTER);
					grid.addComponent(clientes, 0, 1);
						grid.setComponentAlignment(clientes, Alignment.TOP_CENTER);
					grid.addComponent(chkRegresaAlmacen, 1, 1);
						grid.setComponentAlignment(chkRegresaAlmacen, Alignment.MIDDLE_CENTER);
					grid.addComponent(comentarios, 2, 1, 3, 1);
						grid.setComponentAlignment(comentarios, Alignment.MIDDLE_CENTER);
					
					grid.addComponent(btnRegistrar, 0, 2);
						grid.setComponentAlignment(btnRegistrar, Alignment.TOP_CENTER);
					
					//grid.setStyleName(ValoTheme.LAYOUT_WELL);

				
				//Agrego a principal
					principal.addComponent(grid);
						principal.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
			
				//Color si es que es secundaria
					if(secundaria){
						grid.setStyleName("VentanaSecundaria");
					}
					
				//Boton alta cliente como link
					
					productos.setWidth("70%");
					clientes.setWidth("70%");
					comentarios.setWidth("70%");
					
				
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
							if(registrar()){
								NotificacionCorrecta.mostrar("Devolución registrada correctamente");
								borrarCampos();
							}else{
								//NotificacionError.mostrar("Contacte al administrador", 5000);
							}
						} catch (Exception e) {
							NotificacionError.mostrar(e.toString(), 3000);
							e.printStackTrace();
						}
					}
				});
				
		}
		
}

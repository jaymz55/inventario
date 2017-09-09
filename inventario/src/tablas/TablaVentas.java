package tablas;

import java.text.NumberFormat;
import java.util.Collection;

import com.vaadin.ui.VerticalLayout;

import funciones.Encriptar;
import sql.DTO.VentaDTO;

public class TablaVentas extends Tabla{

	//Variables
		private static final long serialVersionUID = 1L;
		private Collection<VentaDTO> coleccion = null;
		
		private VerticalLayout respuesta = new VerticalLayout();
	
	//Constructores
		public TablaVentas(String custid, String fechaInicial, String fechaFinal) throws UnsupportedOperationException, Exception{
			super();
			coleccion = facade.obtenerVentas(custid, fechaInicial, fechaFinal);
			generarTabla();
		}
		
	//Métodos
		public void generarTabla(){
			
			TablaVentas.this.setColumnCollapsingAllowed(true);
			
			//Encabezados
				TablaVentas.this.addContainerProperty("ID", String.class, null);
					TablaVentas.this.setColumnCollapsed("ID", true);
				TablaVentas.this.addContainerProperty("PRODUCTO", String.class, null);
					TablaVentas.this.setColumnAlignment("PRODUCTO",Align.LEFT);
				TablaVentas.this.addContainerProperty("CLIENTE", String.class, null);
					TablaVentas.this.setColumnAlignment("CLIENTE",Align.LEFT);
				TablaVentas.this.addContainerProperty("FECHA", String.class, null);
					TablaVentas.this.setColumnAlignment("FECHA",Align.LEFT);
				TablaVentas.this.addContainerProperty("CANTIDAD", Integer.class, null);
					TablaVentas.this.setColumnAlignment("CANTIDAD",Align.CENTER);
				TablaVentas.this.addContainerProperty("SUBTOTAL", String.class, null);
					TablaVentas.this.setColumnAlignment("SUBTOTAL",Align.RIGHT);
				TablaVentas.this.addContainerProperty("DESCUENTO", String.class, null);
					TablaVentas.this.setColumnAlignment("DESCUENTO",Align.RIGHT);				
				TablaVentas.this.addContainerProperty("IMPUESTO", String.class, null);
					TablaVentas.this.setColumnAlignment("IMPUESTO",Align.RIGHT);
				TablaVentas.this.addContainerProperty("TOTAL", String.class, null);
					TablaVentas.this.setColumnAlignment("TOTAL",Align.RIGHT);				
				TablaVentas.this.addContainerProperty("TOTAL SIN DESCUENTO", String.class, null);
					TablaVentas.this.setColumnAlignment("TOTAL SIN DESCUENTO",Align.CENTER);
				TablaVentas.this.addContainerProperty("DESCUENTO TOTAL OTORGADO", String.class, null);
					TablaVentas.this.setColumnAlignment("DESCUENTO TOTAL OTORGADO",Align.CENTER);			
				TablaVentas.this.addContainerProperty("DESCUENTO_DESC", String.class, null);
					TablaVentas.this.setColumnAlignment("DESCUENTO_DESC",Align.LEFT);
				TablaVentas.this.addContainerProperty("VENDEDOR", String.class, null);
					TablaVentas.this.setColumnAlignment("VENDEDOR",Align.LEFT);
				TablaVentas.this.addContainerProperty("COMENTARIOS", String.class, null);
					TablaVentas.this.setColumnAlignment("COMENTARIOS",Align.LEFT);

					
				//Informacion
					for(VentaDTO venta : coleccion){
						
						try {
							TablaVentas.this.addItem(new Object[]{venta.getIdVenta(),  
									venta.getProductoNombre(), 
									Encriptar.Desencriptar(venta.getClienteNombre()),
									venta.getFecha(), 
									venta.getCantidad(), 
							        NumberFormat.getCurrencyInstance().format(venta.getSubtotalAjustado()),
							        NumberFormat.getCurrencyInstance().format(venta.getDescuentoAjustado()),
							        NumberFormat.getCurrencyInstance().format(venta.getIvaAjustado()),
							        NumberFormat.getCurrencyInstance().format(venta.getTotalAjustado()),
									"("+NumberFormat.getCurrencyInstance().format(venta.getTotal())+")",
									"("+NumberFormat.getCurrencyInstance().format(venta.getDescuento())+")",
									venta.getDescuentoDescripcion(),
									venta.getVendedor(),
									venta.getComentarios()}, venta.getIdVenta());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
				TablaVentas.this.setWidth(ancho);
				TablaVentas.this.setHeight(alto);
			
		}
		
		public void actualizarTabla(Tabla tabla, String custid){};
		
		public VerticalLayout generarTablaCompleta(){

			respuesta.setWidth("100%");
			
			respuesta.addComponent(generar2Filtros(TablaVentas.this, "PRODUCTO", "CLIENTE"));
			respuesta.addComponent(TablaVentas.this);
			respuesta.addComponent(generarExcel(TablaVentas.this, new String[]{"PRODUCTO","CLIENTE",
					"FECHA","CANTIDAD","SUBTOTAL","DESCUENTO","IMPUESTO","TOTAL","TOTAL SIN DESCUENTO",
					"DESCUENTO TOTAL OTORGADO","DESCUENTO_DESC","VENDEDOR","COMENTARIOS"}));
			
			return respuesta;
			
		}
		
		public void dobleClic(String id){};
		
}

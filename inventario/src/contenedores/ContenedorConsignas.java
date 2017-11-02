package contenedores;

import java.io.Serializable;
import java.util.ArrayList;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import comboBox.ComboProductos;

public class ContenedorConsignas extends Contenedor{

	//Variables
		private static final long serialVersionUID = 1L;
		BeanItemContainer<BeanConsigna> contenedor;
		ArrayList<BeanConsigna> coleccion;
	

	//Const
	/*
	 * Llamo al constructor del padre y mando además el tipo de combo que necesito
	 */
	public ContenedorConsignas(String titulo) {
		super(titulo, new ComboProductos());
		cargarInformacion();
	}

	@Override
	protected void cargarInformacion() {
		
		contenedor = new BeanItemContainer<BeanConsigna>(BeanConsigna.class);
		
		coleccion = new ArrayList<BeanConsigna>();
		//coleccion.add(new BeanConsigna("Uno", 3.1416));
		
		contenedor.addAll(coleccion);
		
		cargarTabla(null, contenedor);
		
		//actualizarTabla(coleccion);
		//titulosTabla(new String[]{"CANTIDAD","PRODUCTO"});
	}

	@Override
	public void recargarInformacion() {
		
	}

	//Listeners
	protected void listeners() {
		
		btnAgregar.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;
			
			public void buttonClick(ClickEvent event) {
			
		    	try {
					
		    		
		    		
				} catch (IllegalArgumentException
						| NullPointerException e) {
					e.printStackTrace();
				}
		    	
		    }
		});
		
	}
	
	
	//Clases internas
	/*
	 * Extiende a la clase Bean de Padre
	 */
		public class BeanConsigna extends Bean implements Serializable {
	
			private static final long serialVersionUID = 1L;
			String producto;
		    double cantidad;
		    
		    public BeanConsigna(String PRODUCTO, double cantidad) {
		        this.producto   = PRODUCTO;
		        this.cantidad = cantidad;
		    }
		    
		    public String getProducto() {
		        return producto;
		    }
		    
		    public void setProducto(String producto) {
		        this.producto = producto;
		    }
		    
		    public double getCantidad() {
		        return cantidad;
		    }
		    
		    public void setCantidad(double cantidad) {
		        this.cantidad = cantidad;
		    }
		}
	
}

package contenedores;

import org.vaadin.ui.NumberField;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import comboBox.Combo;

public abstract class Contenedor extends HorizontalLayout{
	
	//Variables
		private static final long serialVersionUID = 1L;
		
		Table tabla;
		
		//Layouts
			VerticalLayout layIzquierdo;
			VerticalLayout layDerecho;
			
			GridLayout grid = new GridLayout(1,4);
			
		//Botones
			Combo combo;
			Button btnAgregar;
			Button btnEliminar;
			Button btnRegistrar;
			
			NumberField cantidad;
		
	//Const
		public Contenedor(String titulo, Combo combo){
			formatoElementos(titulo, combo);
		}
	
	//Metodos abstractos
		protected abstract void cargarInformacion();
		public abstract void recargarInformacion();
	
	//Metodos
		/*
		 * titulo de la tabla *Puede ser null
		 */
		protected void formatoElementos(String titulo, Combo combo){
			
			//Layouts
				layIzquierdo = new VerticalLayout();
				layDerecho = new VerticalLayout();
				
				Contenedor.this.addComponent(layIzquierdo);
				Contenedor.this.addComponent(layDerecho);
				Contenedor.this.setExpandRatio(layIzquierdo, 3);
				Contenedor.this.setExpandRatio(layDerecho, 1);
				
				
				Contenedor.this.setWidth("100%");
				Contenedor.this.setHeight("100%");
					
			//Botones
				this.combo = combo;
				btnAgregar = new Button("Agregar");
				btnEliminar = new Button("Eliminar");
				
				cantidad = new NumberField("Cantidad");
					
				grid.addComponent(combo, 0, 0);
				grid.addComponent(cantidad, 0, 1);
				grid.addComponent(btnAgregar, 0, 2);
				grid.addComponent(btnEliminar, 0, 3);
				
				grid.setHeight("100%");
				grid.setWidth("100%");
				
				layDerecho.addComponent(grid);
				layDerecho.setHeight("250");
				
		}
		
		protected void titulosTabla(String[] titulos){
			tabla.setColumnHeaders(titulos);
		};
		
		/*protected void actualizarTabla(ArrayList<Bean> coleccion){
			contenedor.addAll(coleccion);
		}*/
		
		protected void agregarItem(Bean bean){
			//coleccion.add(bean);
		}
		
		protected void cargarTabla(String titulo, @SuppressWarnings("rawtypes") BeanItemContainer contenedor){
			tabla = new Table(titulo, contenedor);
			tabla.setWidth("90%");
			tabla.setHeight("90%");
			
			layIzquierdo.addComponent(tabla);
		}
		
	//Clases internas
		public abstract class Bean{}
}

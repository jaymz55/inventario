package sql.DAO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import funciones.Funcion;
import sql.DTO.ClienteDTO;
import sql.DTO.MaterialDTO;
import sql.DTO.ProductoDTO;
import sql.DTO.ProveedorDTO;

public class Coleccion {

	public static void sort(final String field, List<ProveedorDTO> itemLocationList) {
	    Collections.sort(itemLocationList, new Comparator<ProveedorDTO>() {
	        @Override
	        public int compare(ProveedorDTO o1, ProveedorDTO o2) {
	            if(field.equals("nombre")) {
	                return Funcion.primeraLetraMayuscula(o1.getNombre()).compareTo(Funcion.primeraLetraMayuscula(o2.getNombre()));
	            }else{ //Este no se usa, solo es para completar el return
	            	return o1.getNombre().compareTo(o2.getNombre());
	            }
	        }           
	    });
	}
	
	
	public static void sortClientes(final String field, List<ClienteDTO> itemLocationList) {
	    Collections.sort(itemLocationList, new Comparator<ClienteDTO>() {
	        @Override
	        public int compare(ClienteDTO o1, ClienteDTO o2) {
	            if(field.equals("nombre")) {
	                return Funcion.primeraLetraMayuscula(o1.getNombre()).compareTo(Funcion.primeraLetraMayuscula(o2.getNombre()));
	            }else{ //Este no se usa, solo es para completar el return
	            	return o1.getNombre().compareTo(o2.getNombre());
	            }
	        }           
	    });
	}
	
	public static void sortProducto(final String field, List<ProductoDTO> itemLocationList) {
	    Collections.sort(itemLocationList, new Comparator<ProductoDTO>() {
	        @Override
	        public int compare(ProductoDTO o1, ProductoDTO o2) {
	            if(field.equals("nombre")) {
	                return o1.getNombre().compareTo(o2.getNombre());
	            }else{ //Este no se usa, solo es para completar el return
	            	return o1.getNombre().compareTo(o2.getNombre());
	            }
	        }           
	    });
	}
	
	public static void sortMaterial(final String field, List<MaterialDTO> itemLocationList) {
	    Collections.sort(itemLocationList, new Comparator<MaterialDTO>() {
	        @Override
	        public int compare(MaterialDTO o1, MaterialDTO o2) {
	            if(field.equals("nombre")) {
	                return o1.getNombre().compareTo(o2.getNombre());
	            }else{ //Este no se usa, solo es para completar el return
	            	return o1.getNombre().compareTo(o2.getNombre());
	            }
	        }           
	    });
	}
	
}

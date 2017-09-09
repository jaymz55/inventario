package funciones;

import java.text.SimpleDateFormat;

public class Funcion {
	
	public static String fechaFormato(Object fecha, String formato){
		final SimpleDateFormat dateFormat = new SimpleDateFormat(formato);
		String resultado = dateFormat.format(fecha);
		return resultado;
	}
	
	public static String quitarComillas(String palabra){
		
		return palabra.replaceAll("'", "");
		
	}
	
	public static String decimales(String monto){
		
		if(monto != null){
			if(monto.matches(".*000")){
				return monto.substring(0, monto.length()-4);
			}else{
				return monto;
			}
		}else{
			return "";
		}
		
	}
	
	public static String primeraLetraMayuscula(String original) {
	    if (original == null || original.length() == 0) {
	        return original;
	    }
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
}

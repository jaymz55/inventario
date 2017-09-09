package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import facade.Facade;
import notificaciones.NotificacionError;
import sql.DTO.MaterialDTO;

public class AutoCompleteCategorias extends AutoComplete{

	//Variables
		private static final long serialVersionUID = 1L;
		Collection<MaterialDTO> listaDTO;
		List<String> listaString;
		Facade facade = new Facade();
		private String custid;
	
	
	//Constructores
		public AutoCompleteCategorias(String custid, String caption){
			super(caption);
			this.custid = custid;
			setSuggestionProvider();
		}
	
	//Metodos
	
	@Override
	protected void maximoCaracteres() {
		AutoCompleteCategorias.this.setMaxLength(200);
	}

	@Override
	protected void setSuggestionProvider() {
		
		try{
		
			listaDTO = facade.obtenerMateriales(custid);
			listaString = new ArrayList<String>();
			
			for (MaterialDTO materialDTO : listaDTO) {
				listaString.add(materialDTO.getCategoria());
			}
			
			AutoCompleteCategorias.this.setSuggestionProvider(obtenerLista(listaString));
			
		}catch(Exception e){
			e.printStackTrace();
			NotificacionError.mostrar(e.toString(), 5000);
		}finally{
			listaDTO = null;
			listaString = null;
			facade = null;
			custid = null;
		}
		
	}

}

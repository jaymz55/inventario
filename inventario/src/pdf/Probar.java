package pdf;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

import conexiones.BeanConexion;
import conexiones.MysqlLocal;

public class Probar {

	public static void main(String[] args) {
		
		//Nuevo nuevo = new Nuevo();
		
		MysqlLocal sql = new MysqlLocal();
		BeanConexion beanCon;
		ResultSet rs;
		
		try{
			
			beanCon = sql.conexionSimple("SELECT '1' as cantidad, nombre, total FROM tupro_inventario.productos where custid = 10007 limit 5");
			
			if(!beanCon.getRespuesta().equals("OK")){
				throw new Exception(beanCon.getRespuesta());
			}
			
			rs = beanCon.getRs();
			
			final Table tabla = new Table();
			
			tabla.addContainerProperty("cantidad", String.class, null);
			tabla.addContainerProperty("producto", String.class, null);
			tabla.addContainerProperty("precio", String.class, null);
			
			int id = 0;
			
			while(rs.next()){

				tabla.addItem(new Object[]{rs.getString("cantidad"), rs.getString("nombre"), rs.getString("total")}, id);
				id++;
				
			}
			
			//Recorro los datos
			
			Collection a = tabla.getItemIds();
			
			for (Iterator iterator = a.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				
				System.out.println(tabla.getItem(object).getItemProperty("precio").getValue());
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sql.cerrar();
			sql = null;
			beanCon = null;
			rs = null;
		}
		
	}
	
}

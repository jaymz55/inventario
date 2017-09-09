package excel;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.vaadin.haijian.ExcelExporter;

import com.vaadin.ui.Table;

public class BotonExcel extends ExcelExporter{

	//Variables
		Date fecha;
		SimpleDateFormat format;
	
	//Constructores
		public BotonExcel(Table tabla, Object[] columnasAExportar){
			
			super(tabla);
			
			fecha = new Date();
			format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			
			BotonExcel.this.setDownloadFileName(format.format(fecha));
			BotonExcel.this.setCaption("Excel");
			BotonExcel.this.setVisibleColumns(columnasAExportar);
			
		}
	
	//Métodos
	
}

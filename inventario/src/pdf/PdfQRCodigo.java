package pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;

import com.google.zxing.WriterException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import codes.QRCode;
import conexiones.BeanConsulta;
import conexiones.Mysql;
import sql.SqlConf;
import sql.DTO.UsuarioDTO;

public class PdfQRCodigo {
	
	//http://www.vogella.com/tutorials/JavaPDF/article.html
	
    //private static String FILE = "C:\\Users\\Héctor\\Desktop\\Cotizacion.pdf";
	
	//private static String FILE = url.toString();
	
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12,
            Font.BOLD);
    private static Font small = new Font(Font.FontFamily.HELVETICA, 8,
            Font.NORMAL);    
	
	//public static void main(String[] args) {
		public void generarPdf(Table tabla, double descuento, String notaGrande, String notaChica){
		//Genero y abro el documento
		try{
			
			//Genero archivo temporal
			File temp = File.createTempFile("codigoQR", ".pdf");
			
			
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(temp));
			//PdfWriter.getInstance(document, new FileOutputStream(url.toString()));
			document.open();
		
			agregarTitulo(document, notaGrande, notaChica);
			agregarTabla(document, tabla, descuento);
			abrirArchivo(temp);
			
			
			document.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Genero el título y subtítulo

		
	}
	
	private static void agregarTitulo(Document document, String notaGrande, String notaChica) throws DocumentException, MalformedURLException, IOException {
		
		UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		
		try{
		
	        Paragraph preface = new Paragraph();
	
	        System.out.println("Padre: "+usuario.getPadre());
	        
	        URL url = new URL("http://www.tuprograma.mx/imagenes/"+usuario.getPadre()+".png");
	        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	        
	        if(huc.getResponseCode() == 200) { 
	            
	            Image img = Image.getInstance("http://www.tuprograma.mx/imagenes/"+usuario.getPadre()+".png");
	            
	            //img.scaleAbsolute(90, 70);
	            img.scaleAbsolute((float)((70/img.getScaledHeight())*img.getScaledWidth() ), 70);
	            img.setAlignment(2);
	            
	            preface.add(img);
	        	
	        }
	        
	        url = null;
	        huc = null;
	        
	        
	        addEmptyLine(preface, 1);
	        
	       //Con info guardada en SQL
	        //preface.add(new Paragraph(obtenerTexto(usuario.getPadre(), "titulo_grande"), catFont));
	        preface.add(new Paragraph("Cúrcuma de 400 gramos.", small));
	        
	       //Agregamos línea punteada
	        /*DottedLineSeparator dottedline = new DottedLineSeparator();
	        dottedline.setOffset(-2);
	        dottedline.setGap(2f);
	        preface.add(dottedline);*/
	        
	       //Agregamos línea corrida
	        LineSeparator line = new LineSeparator();
	        preface.add(line);
	        
	       //Agregamos notas
	        
	        addEmptyLine(preface, 1);
	        preface.add(new Paragraph(notaGrande, smallBold));
	        preface.add(new Paragraph(notaChica, small));
	        
	        
	        addEmptyLine(preface, 2);
	        document.add(preface);
        
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			usuario = null;	
		}
		
        
        
	}
	
	private static void agregarTabla(Document document, Table tabla, double descuento) throws DocumentException {
		
		//Variables

        
        //document.add(table);
		
	}
	

	private static void abrirArchivo(final File temp) throws IOException{
		
		//Revisar si es IE
		if(Page.getCurrent().getWebBrowser().isIE()){
			
			Notification.show("Debes utilizar otro navegador para abrir PDF", Type.WARNING_MESSAGE);
			
		}else{
		
			//Genero QR
			File tempQR = File.createTempFile("codigo", ".png");
			
			QRCode qr = new QRCode();
			try {
				tempQR = qr.createQRImage(tempQR, "119-5555", 345, "png");
			} catch (WriterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			FileResource resource = new FileResource(tempQR);
			
			//FileResource resource = new FileResource(temp);
			
			Window window = new Window();
			window.setWidth("90%");
			window.setHeight("90%");
			//BrowserFrame e = new BrowserFrame("PDF File", new ExternalResource("http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf"));
			BrowserFrame e = new BrowserFrame("PDF File", resource);
			e.setWidth("100%");
			e.setHeight("100%");
			window.setContent(e);
			window.center();
			window.setModal(true);
			UI.getCurrent().addWindow(window);
			
		}
	}
	
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    
    private static String obtenerTexto(String custid, String campo){
    	
    	Mysql sql = new Mysql();
    	BeanConsulta bean = new BeanConsulta();
    	//String respuesta;
    	
    	try{
    		
    		// agrego count(custid) para que de devuelva campo vacío en caso de no existir info
    		bean = sql.consultaSimple("select "+campo+", count(custid) from "+SqlConf.obtenerBase()+"inventario.solicitudes_pdf where custid = "+custid);
    		
    		if(!bean.getRespuesta().equals("OK")){
    			throw new Exception(bean.getRespuesta());
    		}
    		
    		return bean.getDato();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    		return e.toString();
    	}finally{
    		sql.cerrar();
    		sql = null;
    		bean = null;
    	}
    	
    }
	
}

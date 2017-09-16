package pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import codes.QRCode;

public class PdfQRCodigo {
	
	//Variables
	
		File temp;
		Paragraph preface = new Paragraph();
	    Font small = new Font(Font.FontFamily.HELVETICA, 17,Font.NORMAL);

	//Metodos
	
		public void generarPdf(String leyenda, String codigo){

		try{
			
			//Genero archivo temporal
			temp = File.createTempFile("codigoQR", ".pdf");
			
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(temp));
			document.open();

			//Reinicio preface para que no acumule los codigos
			//preface = new Paragraph();
			
			agregarLeyenda(document, leyenda);
			agregarCodigo(document, codigo);
			abrirArchivo(temp, codigo);
			
			document.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}

		
	}

		private void agregarLeyenda(Document document, String leyenda) throws DocumentException, MalformedURLException, IOException {
			
			try{

		        //addEmptyLine(preface, 1);
		        preface.add(new Paragraph(leyenda, small));
	        
			}catch(Exception e){
				e.printStackTrace();
			}
	        
		}
		
		private void agregarCodigo(Document document, String codigo) throws DocumentException, MalformedURLException, IOException {
			
			try{

		      //Genero QR
				File tempQR = File.createTempFile("codigo", ".png");
				
				QRCode qr = new QRCode();
				try {
					tempQR = qr.createQRImage(tempQR, "TuInventario/"+codigo, 170, "png");
				} catch (WriterException e1) {
					e1.printStackTrace();
				}
		        
				Image img = Image.getInstance(tempQR.getAbsolutePath());
				
		        preface.add(img);		        
		        
		        addEmptyLine(preface, 2);
		        document.add(preface);
	        
			}catch(Exception e){
				e.printStackTrace();
			}
	        
		}
		
	private void abrirArchivo(final File temp, String codigo) throws IOException{
		
		//Revisar si es IE
		if(Page.getCurrent().getWebBrowser().isIE()){
			
			Notification.show("Debes utilizar otro navegador para abrir PDF", Type.WARNING_MESSAGE);
			
		}else{
			
			try{
				
				FileResource resource = new FileResource(temp);
				
				Window window = new Window();
				window.setWidth("90%");
				window.setHeight("90%");
				BrowserFrame e = new BrowserFrame("PDF File", resource);
				e.setWidth("100%");
				e.setHeight("100%");
				window.setContent(e);
				window.center();
				window.setModal(true);
				UI.getCurrent().addWindow(window);
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    
    public void reiniciarPdf(){
    	
    	preface = new Paragraph();
    	
    }
	
}

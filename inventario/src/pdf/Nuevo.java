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

public class Nuevo {
	
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
			File temp = File.createTempFile("Cotiza", ".pdf");
			
			
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(temp));
			//PdfWriter.getInstance(document, new FileOutputStream(url.toString()));
			document.open();
		
			agregarTitulo(document, notaGrande, notaChica);
			agregarTabla(document, tabla, descuento);
			agregarNotas(document);
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
	        preface.add(new Paragraph(obtenerTexto(usuario.getPadre(), "titulo_grande"), catFont));
	        preface.add(new Paragraph(obtenerTexto(usuario.getPadre(), "titulo_chico"), small));
	        
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
		//double total;
		
		//Especificando el número de columnas
        PdfPTable table = new PdfPTable(3);

        float[] columnWidths = new float[]{10f, 30f, 10f};
        table.setWidths(columnWidths);
        
        PdfPCell c1 = new PdfPCell(new Phrase("Cantidad"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Producto"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Precio"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        
    //Para moneda
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
        
        //Empieza a desglosar la tabla
        
        //Variables
        double total = 0;
        
        PdfPCell celdaCantidad;
        PdfPCell celdaProducto;
        PdfPCell celdaPrecio;
        
		Collection a = tabla.getItemIds();
		
		for (Iterator iterator = a.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			
			celdaCantidad = new PdfPCell(new Phrase(tabla.getItem(object).getItemProperty("CANTIDAD").getValue().toString()));
				celdaCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdaProducto = new PdfPCell(new Phrase(tabla.getItem(object).getItemProperty("PRODUCTO").getValue().toString()));
			
			celdaPrecio = new PdfPCell(new Phrase(formatter.format(tabla.getItem(object).getItemProperty("PRECIO").getValue())));
				celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
			
			table.addCell(celdaCantidad);
			table.addCell(celdaProducto);
			table.addCell(celdaPrecio);
			
			total = total + Double.parseDouble(tabla.getItem(object).getItemProperty("PRECIO").getValue().toString());
			
			//System.out.println(tabla.getItem(object).getItemProperty("precio").getValue());
		}
        
        /*PdfPCell celdaCantidad = new PdfPCell(new Phrase("1.0"));
        celdaCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(celdaCantidad);
        table.addCell("Cúrcuma");
        
        PdfPCell precio = new PdfPCell(new Phrase("145.00"));
        precio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(precio);
        
        table.addCell(celdaCantidad);
        table.addCell("Cepillo de dientes y de cuidado personal especial para los pequeños hijos de Dios");

        precio = new PdfPCell(new Phrase("250.00"));
        precio.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(precio);*/

		
    //Agrego totales
        PdfPCell ultimo = new PdfPCell(new Phrase(""));
        ultimo.setBorder(0);
        
        table.addCell(ultimo);
        
        ultimo = new PdfPCell(new Phrase("Precio"));
        ultimo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ultimo.setBorder(0);
        
        table.addCell(ultimo);
        
        ultimo = new PdfPCell(new Phrase(formatter.format(total)));
        ultimo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ultimo.setBorder(0);
        
        table.addCell(ultimo);

    //Agrego descuento
        PdfPCell celdaDescuento = new PdfPCell(new Phrase(""));
        celdaDescuento.setBorder(0);
        
        table.addCell(celdaDescuento);
        
        celdaDescuento = new PdfPCell(new Phrase("- Descuento"));
        celdaDescuento.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaDescuento.setBorder(0);
        
        table.addCell(celdaDescuento);
        
        celdaDescuento = new PdfPCell(new Phrase(formatter.format(descuento)));
        celdaDescuento.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaDescuento.setBorder(0);
        
        table.addCell(celdaDescuento);
        
    //Agrego total
        PdfPCell celdaTotal = new PdfPCell(new Phrase(""));
        celdaTotal.setBorder(0);
        
        table.addCell(celdaTotal);
        
        celdaTotal = new PdfPCell(new Phrase("Total"));
        celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotal.setBorder(0);
        
        table.addCell(celdaTotal);
        
        celdaTotal = new PdfPCell(new Phrase(formatter.format(total-descuento)));
        celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotal.setBorder(0);
        
        table.addCell(celdaTotal);
        
        document.add(table);
		
	}
	
	private static void agregarNotas(Document document) throws DocumentException, MalformedURLException, IOException {
		
		UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
		
        Paragraph preface = new Paragraph();

        addEmptyLine(preface, 2);
        
        //DesdeSQL
        preface.add(new Paragraph(obtenerTexto(usuario.getPadre(), "notas_grande"), smallBold));
        preface.add(new Paragraph(obtenerTexto(usuario.getPadre(), "notas_chico"), small));
        addEmptyLine(preface, 2);
        preface.add(new Paragraph(obtenerTexto(usuario.getPadre(), "pie"), smallBold));
        
        /*preface.add(new Paragraph("Condiciones:", smallBold));
        preface.add(new Paragraph(
                "(A) El envío es gratuito dentro de la Ciudad de México en pedidos superiores a $3,000. En caso contrario en Área Metropolitana habrá un cargo de $180 (más IVA)",
                small));
        preface.add(new Paragraph(
                "(B)  Hay recolección gratuita en la Colonia Escandon, Ciudad de México",
                small));
        preface.add(new Paragraph(
                "(C)  Una vez confirmado el pedido,  se enviará el  comprobante de pago a info@spanda-ayurveda.com.mx",
                small));
        preface.add(new Paragraph(
                "(D)  Hay recolección gratuita en la Colonia Escandon, Ciudad de México",
                small));
        preface.add(new Paragraph(
                "(E)  Una vez confirmado el pedido,  se enviará el  comprobante de pago a info@spanda-ayurveda.com.mx",
                small));
        preface.add(new Paragraph(
                "(F) Una vez confirmado el pago el tiempo de entrega y/o envío es de 3-5 días hábiles dependiendo del volumen de compra y método de envío",
                small));
        preface.add(new Paragraph(
                "(G) Una vez entregada la mercancía no se aceptan devoluciones",
                small));
        preface.add(new Paragraph(
                "(H) Los precios y condiciones están sujetos a cambio sin previo aviso",
                small));
        preface.add(new Paragraph(
                "* Productos sujetos para distribución a consideración de Spanda Ayurveda. Sujetos a disponibilidad",
                small));
        addEmptyLine(preface, 2);
        
        preface.add(new Paragraph(
                "CUENTA SPANDA AYURVEDA / SANTANDER",
                smallBold));

        preface.add(new Paragraph(
                "LUZ MARIA DIAZ CONDE",
                smallBold));
        
        preface.add(new Paragraph(
                "CTA: 60-55247732-2",
                smallBold));

        preface.add(new Paragraph(
                "CLABE: 014457605524773226",
                smallBold));*/
        
        addEmptyLine(preface, 1);
        
        Paragraph firma = new Paragraph(obtenerTexto(usuario.getPadre(), "firma"),smallBold);
        
        firma.setAlignment(2);
        
        preface.add(firma);
        
        //Barcode

        
        document.add(preface);
		
	}
	
	private static void abrirArchivo(final File temp) throws IOException{
		
		/*Window window = new Window();
		//((VerticalLayout) window.getContent()).setSizeFull();
		window.setResizable(true);
		window.setCaption("Cotización");
		window.setWidth("800");
		window.setHeight("600");
		window.center();
		StreamSource s = new StreamResource.StreamSource() {

		@Override
		public InputStream getStream() {
		try {
		//File f = new File("C:/themes/repy.pdf");
		FileInputStream fis = new FileInputStream(temp);
		return fis;
		} catch (Exception e) {
		e.printStackTrace();
		return null;
		}
		}
		};

		StreamResource r = new StreamResource(s, "Cotiza.pdf"); //, mainLayout.getApplication()
		Embedded e = new Embedded();
		e.setSizeFull();
		e.setType(Embedded.TYPE_BROWSER);
		r.setMIMEType("application/pdf");

		e.setSource(r);
		
		window.setContent(e);
		UI.getCurrent().addWindow(window);*/
		
		//Revisar si es IE
		if(Page.getCurrent().getWebBrowser().isIE()){
			
			Notification.show("Debes utilizar otro navegador para abrir PDF", Type.WARNING_MESSAGE);
			
		}else{
		
			//Genero QR
			/*File tempQR = File.createTempFile("codigo", ".png");
			
			QRCode qr = new QRCode();
			try {
				tempQR = qr.createQRImage(tempQR, "119-5555", 345, "png");
			} catch (WriterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			FileResource resource = new FileResource(tempQR);*/
			
			FileResource resource = new FileResource(temp);
			
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

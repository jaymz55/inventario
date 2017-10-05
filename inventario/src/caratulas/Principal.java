package caratulas;

import java.io.File;

import apps.AppClientes;
import apps.AppGraficas;
import apps.AppInventario;
import apps.AppMateriales;
import apps.AppProductos;
//import apps.AppMaterial;
import apps.AppProveedores;
import apps.AppQRCodes;
import apps.AppVentas;
import conf.Conf;

import com.example.opciones.Almacen;
import com.example.opciones.Clientes;
import com.example.opciones.Configuracion;
import com.example.opciones.EdoDeCuenta;
import com.example.opciones.EntradasSalidasInventario;
import com.example.opciones.Graficas;
import com.example.opciones.GraficasProductos;
import com.example.opciones.Material;
import com.example.opciones.Productos;
import com.example.opciones.Proveedores;
import com.example.opciones.Solicitudes;
import com.example.opciones.Ventas;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import cookies.Cookies;
import pagos.BotonMP;
import sql.DTO.UsuarioDTO;

@SuppressWarnings("serial")
public class Principal {

	
	public VerticalLayout personalizar(String correo){
		
		//Variables
		
			final VerticalLayout respuesta = new VerticalLayout();
				respuesta.setHeight("100%");
				respuesta.setWidth("100%");
		
			final HorizontalLayout cabecera = new HorizontalLayout();
				cabecera.setHeight("10%");
				cabecera.setWidth("100%");
				
				
			final HorizontalLayout cuerpo = new HorizontalLayout();
				cuerpo.setHeight("90%");
				cuerpo.setWidth("100%");
			
			boolean habilitado = true;
				
		 	//Traigo la clase usuario
				final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();
				
				if(usuario.getDeuda() > 0 && !usuario.getDeudaVencida()){
				
					Notification.show("Tienes una nueva factura generada. Consúltala en la sección Estado de cuenta", Type.TRAY_NOTIFICATION);
					habilitado = true;
					
				}else if(usuario.getDeuda() > 0 && usuario.getDeudaVencida()){
					
					habilitado = false;
					
					final Window edo = new Window("Estado de cuenta");
						edo.center();
						edo.setHeight("20%");
						edo.setWidth("40%");
					
					HorizontalLayout pagoLayOut = new HorizontalLayout();
						pagoLayOut.setHeight("100%");
						pagoLayOut.setWidth("100%");
						pagoLayOut.setMargin(true);
						
						Label etiqueta = new Label("Tu cuenta tiene un saldo de $"+usuario.getDeuda());
							etiqueta.setStyleName("LabelDeuda");
							etiqueta.setSizeUndefined();
							
						pagoLayOut.addComponent(etiqueta);
							pagoLayOut.setComponentAlignment(etiqueta, Alignment.MIDDLE_CENTER);
						pagoLayOut.setExpandRatio(etiqueta, 3);
						
						Button botonPago = new Button("Pagar");
						botonPago.setStyleName(ValoTheme.BUTTON_PRIMARY);
						
						botonPago.addClickListener(new Button.ClickListener() {
							public void buttonClick(ClickEvent event) {
								
								BotonMP boton = new BotonMP();
								UI.getCurrent().getPage().open(boton.generaBoton(usuario.getCustid(), String.valueOf(usuario.getDeuda())), "_blank");
								boton = null;
								
							}
						});

					pagoLayOut.addComponent(botonPago);
						pagoLayOut.setComponentAlignment(botonPago, Alignment.MIDDLE_CENTER);
						pagoLayOut.setExpandRatio(botonPago, 1);
						
					edo.setContent(pagoLayOut);
					
					UI.getCurrent().addWindow(edo);
					
				}
			
				
				final Proveedores prov = new Proveedores();
				final EntradasSalidasInventario ent = new EntradasSalidasInventario();
				final Productos product = new Productos();
				final Ventas ventas = new Ventas();
				final Almacen almacen = new Almacen();
				final Material material = new Material();
				final Clientes client = new Clientes();
				final EdoDeCuenta cuenta = new EdoDeCuenta();
				final Solicitudes solicitud = new Solicitudes();
				final Configuracion conf = new Configuracion();
				final Graficas graf = new Graficas();
				final GraficasProductos grafProd = new GraficasProductos();
				final AppQRCodes qrCodes = new AppQRCodes();
				final AppVentas appVentas = new AppVentas();
				final AppClientes appClientes = new AppClientes();
				final AppMateriales appMateriales = new AppMateriales();
				final AppProveedores appProveedores = new AppProveedores();
				final AppInventario appInventario = new AppInventario();
				final AppGraficas appTablero = new AppGraficas();
				
				
			MenuBar barmenu = new MenuBar();
				barmenu.setEnabled(habilitado);
				//Para cambiar colores - https://vaadin.com/forum#!/thread/723723
			
			cabecera.addComponent(barmenu);
			
			FileResource imagenTupro = new FileResource(new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + "/WEB-INF/Imagenes/Tuprograma_abre_chico.gif"));
			Image image = new Image(null, imagenTupro);
			cabecera.addComponent(image);
			cabecera.setComponentAlignment(image, Alignment.TOP_RIGHT);
			
			MenuBar.Command mycommand = new MenuBar.Command() {
				private static final long serialVersionUID = 1L;

				public void menuSelected(MenuItem selectedItem) {
			        
			    	//cerrar las ventanas abiertas
			    	
			    	for ( Component c : UI.getCurrent() ) {

			    		if (c.getClass().equals(Window.class)) {
			    	      Window a = (Window) c;
			    	      a.close();
			    	    }
			    	  }
			    	
			    	String opcion = selectedItem.getText();
			    	cuerpo.removeAllComponents();
			    	if(opcion.equals("Proveedores")){
			    		if(Conf.getNuevoMundo())
			    			cuerpo.addComponent(appProveedores.cuerpo());
			    		else
			    			cuerpo.addComponent(prov.cuerpo());
			    	}else if(opcion.equals("Material")){ 
			    		if(Conf.getNuevoMundo())
			    			cuerpo.addComponent(appMateriales.cuerpo());
			    		else
			    			cuerpo.addComponent(material.cuerpo());
			    	}else if(opcion.equals("Inventario")){ 
			    		if(Conf.getNuevoMundo())
			    			cuerpo.addComponent(appInventario.cuerpo());
			    		else
			    			cuerpo.addComponent(ent.cuerpo());
			    	}else if(opcion.equals("QR de material")){ 
			    		cuerpo.addComponent(qrCodes.cuerpo());
			    	}else if(opcion.equals("Solicitudes")){ 
			    		cuerpo.addComponent(solicitud.cuerpo());
			    	}else if(opcion.equals("Catálogo de productos")){ 
			    		/*if(Conf.getNuevoMundo())
			    			cuerpo.addComponent(appProductos.cuerpo());
			    		else*/
			    			cuerpo.addComponent(product.cuerpo());
			    	}else if(opcion.equals("Almacén")){ 
			    		cuerpo.addComponent(almacen.cuerpo());
			    	}else if(opcion.equals("Ventas / Devoluciones")){ 
			    		if(Conf.getNuevoMundo())
			    			cuerpo.addComponent(appVentas.cuerpo());
			    		else
			    			cuerpo.addComponent(ventas.cuerpo());
			    	}else if(opcion.equals("Generales")){ 
			    		cuerpo.addComponent(graf.cuerpo());
			    	}else if(opcion.equals("Por producto")){ 
			    		cuerpo.addComponent(grafProd.cuerpo());
			    	}else if(opcion.equals("Tablero")){ 
			    		cuerpo.addComponent(appTablero.cuerpo());
			    	}else if(opcion.equals("Clientes")){ 
			    		if(Conf.getNuevoMundo())
			    			cuerpo.addComponent(appClientes.cuerpo());
			    		else
			    			cuerpo.addComponent(client.cuerpo());
			    	}else if(opcion.equals("Buscar pagos")){ 
			    		//cuerpo.addComponent(buscarPagos.cuerpo());
			    	}else if(opcion.equals("Estado de cuenta")){ 
			    		cuerpo.addComponent(cuenta.cuerpo());
			    	}else if(opcion.equals("Configuración")){ 
			    		cuerpo.addComponent(conf.cuerpo());
			    	}else if(opcion.equals("Cerrar sesión")){
			    		
			    		//Borro sesión guardada
			    		Cookies cookie = new Cookies();
			    		cookie.deleteCookie("usuario");
			    		
			    		UI.getCurrent().close();
			    		Page.getCurrent().reload();
			    	}	    	
			    	
			    }  
			};
			        
			
			MenuBar.MenuItem proveedores =
				    barmenu.addItem("Proveedores", null, null);
				if(usuario.getPrivilegios().equals("1") || usuario.getPrivilegios().equals("2"))
					proveedores.addItem("Proveedores", null, mycommand);
			
			MenuBar.MenuItem inventario =
			    barmenu.addItem("Materia prima", null, null);
			
				if(usuario.getPrivilegios().equals("1") || usuario.getPrivilegios().equals("2")){
					inventario.addItem("Material", null, mycommand);
					inventario.addItem("Inventario", null, mycommand);
					inventario.addItem("QR de material", mycommand);
				}
				
			MenuBar.MenuItem solicitudes =
					barmenu.addItem("Solicitudes", null, mycommand);
				
			MenuBar.MenuItem productos =
				barmenu.addItem("Producto terminado", null, null);
			
				if(usuario.getPrivilegios().equals("1") || usuario.getPrivilegios().equals("3")){
					productos.addItem("Catálogo de productos", null, mycommand);
					productos.addItem("Almacén", null, mycommand);
					productos.addItem("Ventas / Devoluciones", null, mycommand);
				}
				
			MenuBar.MenuItem clientes =
				barmenu.addItem("Clientes", null, null);
			
			if(usuario.getPrivilegios().equals("1") || usuario.getPrivilegios().equals("3"))
				clientes.addItem("Clientes", null, mycommand);
			
			if(usuario.getCorreo().equals("baul.lukaro@hotmail.com")){
			
			MenuBar.MenuItem pagos =
					barmenu.addItem("MP", null, null);
			
				pagos.addItem("Buscar pagos", mycommand);
				
			}
			
			MenuBar.MenuItem reportes =
					barmenu.addItem("Gráficas", null, null);
			
				reportes.addItem("Generales", null, mycommand);
				reportes.addItem("Por producto", null, mycommand);
				reportes.addItem("Tablero", null, mycommand);
			
			MenuBar.MenuItem edoCuenta =
					barmenu.addItem("Estado de cuenta", null, mycommand);
			
			//if(usuario.getTipo().equals("PRINCIPAL")){
			/*if(usuario.getCorreo().equals("hectorb2002@hotmail.com")){
				MenuBar.MenuItem config =
						barmenu.addItem("Configuración", null, mycommand);
			}*/
			
			// Cerrar sesi�n
			MenuBar.MenuItem cerrar =
					barmenu.addItem("Cerrar sesión", null, mycommand);
			
			respuesta.addComponent(cabecera);
			respuesta.addComponent(cuerpo);
			
		return respuesta;
		
	}
}

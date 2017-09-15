package com.example.opciones;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.haijian.ExcelExporter;
import org.vaadin.ui.NumberField;

import sql.SqlConf;
import sql.DTO.UsuarioDTO;
import tablas.CrearTablas;

import com.example.inventario.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

import conexiones.BeanConexion;
import conexiones.BeanConsulta;
import conexiones.BeanConsultaMultiple;
import conexiones.Mysql;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import funciones.Encriptar;
import funciones.Funcion;

public class Ventas {

	@SuppressWarnings({ "deprecation", "serial" })
	public VerticalLayout cuerpo() {

		VerticalLayout respuesta = new VerticalLayout();
		respuesta.setWidth("100%");
		respuesta.setMargin(true);

		final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();

		final HorizontalLayout titulo = new HorizontalLayout();
		titulo.setHeight("70");
		titulo.setWidth("90%");

		Label tituloLabel = new Label("Registro de ventas");
		tituloLabel.setStyleName(ValoTheme.LABEL_H1);

		titulo.addComponent(tituloLabel);

		respuesta.addComponent(titulo);

		final HorizontalLayout cabecera = new HorizontalLayout();
		cabecera.setHeight("100%");
		cabecera.setWidth("100%");

		final VerticalLayout cuerpo = new VerticalLayout();
		cuerpo.setHeight("100%");
		cuerpo.setWidth("100%");

		final VerticalLayout tablas = new VerticalLayout();
		tablas.setHeight("100%");
		tablas.setWidth("100%");

		final DateField fechaUno = new DateField();
		fechaUno.setDateFormat("dd MMMM yyyy");
		fechaUno.setValue(getFirstDateOfCurrentMonth());
		fechaUno.setStyleName("ajustado");

		final DateField fechaDos = new DateField();
		fechaDos.setDateFormat("dd MMMM yyyy");
		fechaDos.setValue(new Date());
		fechaDos.setStyleName("ajustado");

		final Button buscar = new Button("Buscar");
		buscar.setStyleName("boton_simple");

		buscar.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				cuerpo.removeAllComponents();
				cuerpo.addComponent(generarTabla(
						tablas,
						usuario.getCustidsRelacionados(),
						Funcion.fechaFormato(fechaUno.getValue(), "yyyy-MM-dd"),
						Funcion.fechaFormato(fechaDos.getValue(), "yyyy-MM-dd")));
				cuerpo.setComponentAlignment(cuerpo.getComponent(0),
						Alignment.MIDDLE_CENTER);

			}
		});

		final Button insertar = new Button("Venta");
		insertar.setStyleName("boton_simple");
		insertar.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				final Window ventanaRegistrar = new Window(
						"Registrar salida / venta");
				ventanaRegistrar.center();
				ventanaRegistrar.setHeight("70%");
				ventanaRegistrar.setWidth("85%");

				final ComboBox producto = llenarComboBox(new ComboBox(),
						usuario.getCustidsRelacionados());
				producto.setWidth("80%");

				final ComboBox cliente = llenarComboBoxCliente(new ComboBox(),
						usuario.getCustidsRelacionados());
				cliente.setWidth("80%");

				final NumberField cantidad = new NumberField("Cantidad");
				cantidad.setNullRepresentation("");
				cantidad.setDecimalPrecision(2);
				cantidad.setErrorText("N�mero no v�lido");
				cantidad.setInvalidAllowed(false);
				cantidad.setNegativeAllowed(false);
				cantidad.setImmediate(true);

				final HorizontalLayout montos = new HorizontalLayout();
				montos.setWidth("80%");

				final NumberField precio = new NumberField("Precio");
				precio.setNullRepresentation("");
				precio.setDecimalPrecision(2);
				precio.setErrorText("N�mero no v�lido");
				precio.setInvalidAllowed(false);
				precio.setNegativeAllowed(false);
				precio.setWidth("50%");

				final NumberField descuento = new NumberField("Descuento");
				descuento.setNullRepresentation("");
				descuento.setDecimalPrecision(2);
				descuento.setErrorText("N�mero no v�lido");
				descuento.setInvalidAllowed(false);
				descuento.setNegativeAllowed(false);
				descuento.setWidth("50%");

				final TextArea comentarios = new TextArea("Comentarios");
				comentarios.setWidth("80%");
				comentarios.setMaxLength(500);

				// Pop para comentarios de descuento
				final VerticalLayout popupContent = new VerticalLayout();
				// popupContent.setWidth("400");
				popupContent.setMargin(true);

				final TextArea descComentario = new TextArea("Comentario");
				descComentario.setRows(5);
				descComentario.setColumns(30);
				descComentario.setMaxLength(500);
				popupContent.addComponent(descComentario);
				popupContent.setComponentAlignment(descComentario,
						Alignment.MIDDLE_CENTER);

				PopupView popup = new PopupView("Comentario del descuento",
						popupContent);
				popup.setHideOnMouseOut(false);

				montos.addComponent(precio);
				montos.setComponentAlignment(precio, Alignment.MIDDLE_LEFT);
				montos.addComponent(descuento);
				montos.setComponentAlignment(descuento, Alignment.MIDDLE_CENTER);
				montos.addComponent(popup);
				montos.setComponentAlignment(popup, Alignment.BOTTOM_RIGHT);

				cantidad.addTextChangeListener(new TextChangeListener() {
					public void textChange(TextChangeEvent event) {/*
																	 * addValueChangeListener
																	 * (new
																	 * Property.
																	 * ValueChangeListener
																	 * () {
																	 * public
																	 * void
																	 * valueChange
																	 * (
																	 * ValueChangeEvent
																	 * event) {
																	 */

						if (producto.getValue() != null
								&& !event.getText().equals("")) {

							Mysql sql = new Mysql();

							try {

								BeanConsulta bean = sql
										.consultaSimple("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "
												+ producto.getValue()
														.toString());

								if (!bean.getRespuesta().equals("OK")) {
									throw new Exception(bean.getRespuesta());
								}

								double precioProducto = Double.parseDouble(bean
										.getDato());

								// double precioProducto =
								// Double.parseDouble(sql.consultaSimple("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "+producto.getValue().toString()));
								precio.setValue(precioProducto
										* Double.parseDouble(event.getText()));

							} catch (Exception e) {
								Notification.show("Error en la aplicaci�n: "
										+ e.toString(), Type.ERROR_MESSAGE);
								e.printStackTrace();
							} finally {
								sql.cerrar();
							}

							// }

						} else {
							precio.setValue("");
						}
					}
				});

				final DateField fechaVenta = new DateField("Fecha de venta");
				fechaVenta.setDateFormat("dd MMMM yyyy");
				// fechaVenta.setStyleName(ValoTheme.DATEFIELD_SMALL);

				final ComboBox vendedor = llenarComboBoxVendedor(
						new ComboBox(), usuario.getCustidsRelacionados());
				vendedor.setWidth("60%");

				final NumberField existencia = new NumberField("Existencia");
				existencia.setEnabled(false);

				// Listener para registrar existencia cuando se escoge producto
				producto.addListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = -5188369735622627751L;

					public void valueChange(ValueChangeEvent event) {

						Mysql sql = new Mysql();

						try {

							BeanConexion beanCon = sql
									.conexionSimple("select sum(cantidad) as cantidad from "+SqlConf.obtenerBase()+"inventario.almacen where custid in ("
											+ usuario.getCustidsRelacionados()
											+ ") and id_producto = "
											+ producto.getValue()
											+ " and activo = 'SI'");

							if (!beanCon.getRespuesta().equals("OK")) {
								throw new Exception(beanCon.getRespuesta());
							}

							ResultSet rs = beanCon.getRs();

							while (rs.next()) {

								existencia.setValue(rs.getDouble("cantidad"));

							}

						} catch (Exception e) {
							Notification.show(
									"Error en la aplicaci�n: " + e.toString(),
									Type.ERROR_MESSAGE);
							e.printStackTrace();
						} finally {
							sql.cerrar();
						}

					}
				});

				// Listener para agregar vendedor asignado al momento de cambiar
				// de cliente
				cliente.addListener(new Property.ValueChangeListener() {
					private static final long serialVersionUID = -5188369735622627751L;

					public void valueChange(ValueChangeEvent event) {

						if (cliente.getValue() != null) {

							Mysql sql = new Mysql();

							try {

								BeanConexion beanCon = sql
										.conexionSimple("select vendedor_asignado from "+SqlConf.obtenerBase()+"inventario.clientes where id = "
												+ cliente.getValue().toString());

								if (!beanCon.getRespuesta().equals("OK")) {
									throw new Exception(beanCon.getRespuesta());
								}

								ResultSet rs = beanCon.getRs();

								while (rs.next()) {

									vendedor.setValue(rs
											.getObject("vendedor_asignado"));

								}

							} catch (Exception e) {
								Notification.show("Error en la aplicaci�n: "
										+ e.toString(), Type.ERROR_MESSAGE);
								e.printStackTrace();
							} finally {
								sql.cerrar();
							}

						}
					}
				});

				Button registrar = new Button("Registrar");
				registrar.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {

						if (producto.getValue() != null
								&& !cantidad.getValue().equals("")
								&& !precio.getValue().equals("")
								&& fechaVenta.getValue() != null) {

							Mysql sql = new Mysql();
							Mysql sql2 = new Mysql();
							String respuesta = "NO";

							try {

								// Abro transacci�n
								sql.transaccionAbrir();

								String clienteAjuste = "";
								String vendedorAjuste = "";
								String descuentoAjuste = "";
								String descuentoDescAjuste = "";
								String comentariosAjuste = "";

								if (cliente.getValue() == null) {
									clienteAjuste = "null";
								} else {
									clienteAjuste = cliente.getValue()
											.toString();
								}

								if (vendedor.getValue() == null) {
									vendedorAjuste = "null";
								} else {
									vendedorAjuste = "'"
											+ vendedor.getItemCaption(vendedor
													.getValue()) + "'";
								}

								if (descuento.getValue().equals("")) {
									descuentoAjuste = "0.0";
									descuentoDescAjuste = "null";
								} else {
									descuentoAjuste = descuento.getValue()
											.toString();
									descuentoDescAjuste = "'"
											+ descComentario.getValue() + "'";
								}

								if (comentarios.getValue().equals("")) {
									comentariosAjuste = "null";
								} else {
									comentariosAjuste = "'"
											+ Funcion.quitarComillas(comentarios.getValue()) + "'";
								}

								Vector<String> vector = new Vector<>();
								vector.add("select precio from "+SqlConf.obtenerBase()+"inventario.productos where id = "
										+ producto.getValue().toString());
								vector.add("select iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "
										+ producto.getValue().toString());
								vector.add("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "
										+ producto.getValue().toString());

								BeanConsultaMultiple bean = sql
										.consultaMultiple(vector);

								if (!bean.getRespuesta().equals("OK")) {
									throw new Exception(bean.getRespuesta());
								}

								Vector<String> respuestas = bean.getDatos();
								String precioString = respuestas.elementAt(0);
								String ivaString = respuestas.elementAt(1);
								String totalString = respuestas.elementAt(2);

								// Grava iva
								BeanConsulta beanCon = sql
										.consultaSimple("select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "
												+ producto.getValue()
														.toString());

								if (!beanCon.getRespuesta().equals("OK")) {
									throw new Exception(bean.getRespuesta());
								}

								respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.ventas values ("
										+ "null, '"
										+ usuario.getCustid()
										+ "','"
										+ producto.getValue().toString()
										+ "',"
										+ clienteAjuste
										+ ","
										+ precioString
										+ ","
										+ ivaString
										+ ","
										+ totalString
										+ ","
										+ descuentoAjuste
										+ ","
										+ descuentoDescAjuste
										+ ",'"
										+ Funcion.fechaFormato(
												fechaVenta.getValue(),
												"yyyy-MM-dd")
										+ "',"
										+ cantidad.getValue()
										+ ","
										+ vendedorAjuste
										+ ",'"
										+ beanCon.getDato()
										+ "',"
										+ comentariosAjuste
										+ ",'NO', 'SI', 'VENTA')");

								if (!respuesta.equals("OK")) {
									throw new Exception(respuesta);
								}

								// Registro salida de producto de almac�n s�lo
								// si registra bien la venta

								// Obtengo id de venta
								String idVenta = "";
								BeanConsulta beanSimple = sql
										.consultaSimple("SELECT LAST_INSERT_ID()");

								if (!beanSimple.getRespuesta().equals("OK")) {
									throw new Exception(bean.getRespuesta());
								}

								idVenta = beanSimple.getDato();

								respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.almacen values ("
										+ "null, "
										+ usuario.getCustid()
										+ ", "
										+ producto.getValue().toString()
										+ ",'"
										+ Funcion.fechaFormato(
												fechaVenta.getValue(),
												"yyyy-MM-dd")
										+ "', null, "
										+ (Double.parseDouble(cantidad
												.getValue()) * -1)
										+ ",'VENTA', null, null, 'SI', "
										+ idVenta + ")");

								if (!respuesta.equals("OK")) {
									throw new Exception(respuesta);
								}

								if (respuesta.equals("OK")) {

									// Cierro transacci�n
									sql.transaccionCommit();
									sql.transaccionCerrar();

									producto.setValue(null);
									cliente.setValue(null);
									precio.setValue("");
									cantidad.setValue("");
									vendedor.setValue(null);
									descuento.setValue("");
									descComentario.setValue("");
									existencia.setValue("");
									comentarios.setValue("");

									Notification n = new Notification(
											"Registro correcto de venta",
											Type.TRAY_NOTIFICATION);
									n.setDelayMsec(2000);
									n.setPosition(Notification.POSITION_CENTERED); // POSITION_TOP_RIGHT
									n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
									n.show(UI.getCurrent().getPage());

								}

							} catch (Exception e) {

								// Cierro transacci�n
								sql.transaccionRollBack();
								sql.transaccionCerrar();

								e.printStackTrace();
								Notification.show(
										"Error en sistema: " + e.toString(),
										Type.ERROR_MESSAGE);
							} finally {
								sql.cerrar();
								sql2.cerrar();
							}

							cuerpo.removeAllComponents();
							cuerpo.addComponent(generarTabla(tablas, usuario
									.getCustidsRelacionados(), Funcion
									.fechaFormato(fechaUno.getValue(),
											"yyyy-MM-dd"), Funcion
									.fechaFormato(fechaDos.getValue(),
											"yyyy-MM-dd")));

						} else {
							Notification.show(
									"Se deben de ingresar todos los datos",
									Type.WARNING_MESSAGE);
						}

					}
				});

				// Bot�n de alta de cliente
				Button clienteAlta = new Button("Alta de cliente");
				clienteAlta.setStyleName(ValoTheme.BUTTON_LINK);

				clienteAlta.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {

						final Window ventanaRegistrar = new Window(
								"Registrar cliente");
						ventanaRegistrar.center();
						ventanaRegistrar.setHeight("70%");
						ventanaRegistrar.setWidth("85%");
						ventanaRegistrar.setStyleName("VentanaSecundaria");

						final TextField nombre = new TextField("Cliente");
						nombre.setWidth("80%");
						nombre.setMaxLength(500);

						final TextField telefono = new TextField("Teléfono");
						telefono.setWidth("80%");
						telefono.setMaxLength(100);
						final TextField correo = new TextField(
								"Correo electrónico");
						correo.setWidth("80%");
						correo.setMaxLength(500);
						final ComboBox vendedorAsignado = llenarComboBoxVendedoresAsignados(
								new ComboBox(),
								usuario.getCustidsRelacionados());
						vendedorAsignado.setWidth("60%");
						final TextArea direccion = new TextArea("Dirección");
						direccion.setWidth("80%");
						direccion.setMaxLength(1000);
						final TextArea observaciones = new TextArea(
								"Observaciones");
						observaciones.setWidth("80%");
						observaciones.setMaxLength(1000);

						Button registrar = new Button("Registrar");
						registrar.addListener(new Button.ClickListener() {
							public void buttonClick(ClickEvent event) {

								if (nombre.getValue().equals("")) {
									Notification.show(
											"Se debe de ingresar un nombre",
											Type.WARNING_MESSAGE);
								} else {

									Mysql sql = new Mysql();
									try {

										String vendedorAsignadoAjustado = "null";
										if (vendedorAsignado.getValue() != null)
											vendedorAsignadoAjustado = vendedorAsignado
													.getValue().toString();

										String respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.clientes values ("
												+ "null, '"
												+ usuario.getCustid()
												+ "','"
												+ Encriptar.Encriptar(nombre
														.getValue())
												+ "','"
												+ Encriptar.Encriptar(telefono
														.getValue())
												+ "',"
												+ "'"
												+ Encriptar.Encriptar(correo
														.getValue())
												+ "','"
												+ Encriptar.Encriptar(direccion
														.getValue())
												+ "',"
												+ "'"
												+ Funcion
														.quitarComillas(observaciones
																.getValue())
												+ "',"
												+ vendedorAsignadoAjustado
												+ ",'SI')");

										if (!respuesta.equals("OK")) {
											throw new Exception(respuesta);
										}

										nombre.setValue("");
										telefono.setValue("");
										correo.setValue("");
										vendedorAsignado.setValue(null);
										direccion.setValue("");
										observaciones.setValue("");

										Notification n = new Notification(
												"Registro correcto de cliente",
												Type.TRAY_NOTIFICATION);
										n.setDelayMsec(2000);
										n.setPosition(Notification.POSITION_CENTERED); // POSITION_TOP_RIGHT
										n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
										n.show(UI.getCurrent().getPage());

										// Actualizo Combo de clientes
										cliente.removeAllItems();
										BeanConexion beanCon = sql
												.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.clientes where custid = '"
														+ usuario.getCustid()
														+ "' and activo = 'SI'");

										if (!beanCon.getRespuesta()
												.equals("OK")) {
											throw new Exception(beanCon
													.getRespuesta());
										}

										ResultSet rs = beanCon.getRs();

										while (rs.next()) {
											cliente.addItem(rs.getInt("id"));
											cliente.setItemCaption(
													rs.getInt("id"),
													Encriptar.Desencriptar(rs
															.getString("nombre")));
										}

									} catch (Exception e) {
										Notification.show(
												"Error al registrar: "
														+ e.toString(),
												Type.ERROR_MESSAGE);
										e.printStackTrace();
									} finally {
										sql.cerrar();
									}

								}
							}
						});

						GridLayout grid = new GridLayout(2, 4);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.addComponent(nombre, 0, 0);
						grid.addComponent(telefono, 1, 0);
						grid.addComponent(correo, 0, 1);
						grid.addComponent(vendedorAsignado, 1, 1);
						grid.addComponent(direccion, 0, 2);
						grid.addComponent(observaciones, 1, 2);
						grid.addComponent(registrar, 0, 3);
						grid.setComponentAlignment(registrar,
								Alignment.BOTTOM_LEFT);

						ventanaRegistrar.setContent(grid);

						UI.getCurrent().addWindow(ventanaRegistrar);

					}
				});

				GridLayout grid = new GridLayout(2, 7);
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(producto, 0, 0);
				grid.addComponent(vendedor, 1, 0);
				grid.addComponent(cantidad, 0, 1);
				grid.addComponent(montos, 1, 1);
				grid.addComponent(fechaVenta, 0, 2);
				grid.addComponent(existencia, 1, 2);
				grid.addComponent(cliente, 0, 3);
				grid.addComponent(comentarios, 1, 3, 1, 5);
				// grid.setComponentAlignment(clienteAlta,
				// Alignment.MIDDLE_LEFT);
				grid.addComponent(clienteAlta, 0, 4);
				grid.setComponentAlignment(clienteAlta, Alignment.MIDDLE_CENTER);
				grid.addComponent(registrar, 0, 5);
				grid.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);

				ventanaRegistrar.setContent(grid);

				UI.getCurrent().addWindow(ventanaRegistrar);

			}
		});

		// Bot�n de registro de devoluciones

		final Button devolucion = new Button("Devolución");
		devolucion.setStyleName("boton_simple");
		devolucion.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				final Window ventanaRegistrar = new Window("Devolución");
				ventanaRegistrar.center();
				ventanaRegistrar.setHeight("50%");
				ventanaRegistrar.setWidth("70%");

				final ComboBox producto = llenarComboBox(new ComboBox(),
						usuario.getCustidsRelacionados());
				producto.setWidth("100%");

				final NumberField cantidad = new NumberField("Cantidad");

				final ComboBox cliente = llenarComboBoxCliente(new ComboBox(),
						usuario.getCustidsRelacionados());
				cliente.setWidth("100%");

				final NumberField descuento = new NumberField("Descuento");
				descuento.setNullRepresentation("");
				descuento.setDecimalPrecision(2);
				descuento.setErrorText("Número no válido");
				descuento.setInvalidAllowed(false);
				descuento.setNegativeAllowed(false);
				descuento.setWidth("50%");

				final DateField fechaDevolucion = new DateField("Fecha");
				TimeZone timeZone1 = TimeZone
						.getTimeZone("America/Mexico_City");
				fechaDevolucion.setTimeZone(timeZone1);
				fechaDevolucion.setDateFormat("dd MMMM yyyy");

				final CheckBox regresa = new CheckBox("Regresa a almacén");
				regresa.setValue(false);

				final TextArea comentarios = new TextArea("Comentarios");
				comentarios.setWidth("80%");
				comentarios.setMaxLength(500);

				Button registrar = new Button("Registrar devolución");
				registrar.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {

						if (producto.getValue() != null
								&& !cantidad.getValue().equals("")
								&& fechaDevolucion.getValue() != null) {

							ConfirmDialog.show(
									UI.getCurrent(),
									"Confirmación",
									"¿Estás seguro de querer registrar la devolución?",
									"SI", "NO", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (dialog.isConfirmed()) {

												Mysql sql = new Mysql();

												try {

													// Empieza transacci�n
													sql.transaccionAbrir();

													String clienteAjustado = "null";
													Double descuentoAjuste;
													String comentarioAjuste = "null";
													String regresaAlmacen;

													if (cliente.getValue() != null) {
														clienteAjustado = cliente
																.getValue()
																.toString();
													}

													if (!comentarios.getValue()
															.equals("")) {
														comentarioAjuste = "'"
																+ comentarios
																		.getValue()
																+ "'";
													}

													if (descuento.getValue()
															.equals("")) {
														descuentoAjuste = 0.0;
													} else {
														descuentoAjuste = Double
																.parseDouble(descuento
																		.getValue()
																		.toString())
																* -1;
													}

													if (regresa.getValue() == true) {
														regresaAlmacen = "SI";
													} else {
														regresaAlmacen = "NO";
													}

													Vector<String> vector = new Vector<>();
													vector.add("select precio from "+SqlConf.obtenerBase()+"inventario.productos where id = "
															+ producto
																	.getValue()
																	.toString());
													vector.add("select iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "
															+ producto
																	.getValue()
																	.toString());
													vector.add("select total from "+SqlConf.obtenerBase()+"inventario.productos where id = "
															+ producto
																	.getValue()
																	.toString());

													BeanConsultaMultiple bean = sql
															.consultaMultiple(vector);

													if (!bean.getRespuesta()
															.equals("OK")) {
														throw new Exception(
																bean.getRespuesta());
													}

													Vector<String> respuestas = bean
															.getDatos();
													Double precioString = Double
															.parseDouble(respuestas
																	.elementAt(0));
													Double ivaString = Double
															.parseDouble(respuestas
																	.elementAt(1));
													Double totalString = Double
															.parseDouble(respuestas
																	.elementAt(2));

													// Grava iva
													BeanConsulta beanCon = sql
															.consultaSimple("select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = "
																	+ producto
																			.getValue()
																			.toString());

													if (!beanCon.getRespuesta()
															.equals("OK")) {
														throw new Exception(
																bean.getRespuesta());
													}

													String respuesta = sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.ventas values ("
															+ "null, '"
															+ usuario
																	.getCustid()
															+ "','"
															+ producto
																	.getValue()
																	.toString()
															+ "',"
															+ clienteAjustado
															+ ","
															+ (precioString * -1)
															+ ","
															+ (ivaString * -1)
															+ ","
															+ (totalString * -1)
															+ ", "
															+ descuentoAjuste
															+ ", null,'"
															+ Funcion
																	.fechaFormato(
																			fechaDevolucion
																					.getValue(),
																			"yyyy-MM-dd")
															+ "',"
															+ cantidad
																	.getValue()
															+ ", null,'"
															+ beanCon.getDato()
															+ "', "
															+ comentarioAjuste
															+ ",'SI', '"
															+ regresaAlmacen
															+ "','DEVOLUCIÓN')");

													if (!respuesta.equals("OK")) {
														throw new Exception(
																respuesta);
													}

													// Registro entrada de
													// producto de almac�n s�lo
													// si se escoge la opci�n
													// del Check

													if (regresa.getValue() == true) {

														String idVenta = "";
														BeanConsulta beanSimple = sql
																.consultaSimple("SELECT LAST_INSERT_ID()");

														if (!beanSimple
																.getRespuesta()
																.equals("OK")) {
															throw new Exception(
																	bean.getRespuesta());
														}

														idVenta = beanSimple
																.getDato();

														respuesta = sql
																.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.almacen values ("
																		+ "null, "
																		+ usuario
																				.getCustid()
																		+ ", "
																		+ producto
																				.getValue()
																				.toString()
																		+ ",'"
																		+ Funcion
																				.fechaFormato(
																						fechaDevolucion
																								.getValue(),
																						"yyyy-MM-dd")
																		+ "', null, "
																		+ Double.parseDouble(cantidad
																				.getValue())
																		+ ",'DEVOLUCI�N', null, null, 'SI', "
																		+ idVenta
																		+ ")");

														if (!respuesta
																.equals("OK")) {
															throw new Exception(
																	respuesta);
														}

													}

													sql.transaccionCommit();
													sql.transaccionCerrar();

													producto.setValue(null);
													cantidad.setValue("");
													descuento.setValue("");
													cliente.setValue(null);
													regresa.setValue(false);
													comentarios.setValue("");

													Notification n = new Notification(
															"Registro de devoluci�n correcto",
															Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); // POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent()
															.getPage());

													cuerpo.removeAllComponents();
													cuerpo.addComponent(generarTabla(
															tablas,
															usuario.getCustidsRelacionados(),
															Funcion.fechaFormato(
																	fechaUno.getValue(),
																	"yyyy-MM-dd"),
															Funcion.fechaFormato(
																	fechaDos.getValue(),
																	"yyyy-MM-dd")));
													cuerpo.setComponentAlignment(
															cuerpo.getComponent(0),
															Alignment.MIDDLE_CENTER);

												} catch (Exception e) {

													sql.transaccionRollBack();
													sql.transaccionCerrar();

													e.printStackTrace();
													Notification.show(
															"Error en sistema: "
																	+ e.toString(),
															Type.ERROR_MESSAGE);
												} finally {
													sql.cerrar();
												}

											}
										}

									});

						} else {
							Notification.show(
									"Se deben de ingresar todos los datos",
									Type.WARNING_MESSAGE);
						}

					}
				});

				GridLayout grid = new GridLayout(4, 3);
				grid.setMargin(true);
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.addComponent(producto, 0, 0);
				grid.setComponentAlignment(producto, Alignment.TOP_LEFT);
				grid.addComponent(cantidad, 1, 0);
				grid.setComponentAlignment(cantidad, Alignment.TOP_CENTER);
				grid.addComponent(descuento, 2, 0);
				grid.setComponentAlignment(descuento, Alignment.TOP_CENTER);
				grid.addComponent(fechaDevolucion, 3, 0);
				grid.setComponentAlignment(fechaDevolucion,
						Alignment.TOP_CENTER);
				grid.addComponent(cliente, 0, 1);
				grid.setComponentAlignment(cliente, Alignment.TOP_LEFT);

				// Horizontal para el espacio en regresa
				HorizontalLayout regresaLay = new HorizontalLayout();
				regresaLay.setMargin(true);
				regresaLay.setWidth("100%");
				regresaLay.addComponent(regresa);
				regresaLay.setComponentAlignment(regresa, Alignment.TOP_CENTER);

				grid.addComponent(regresaLay, 1, 1);
				// grid.setComponentAlignment(regresa, Alignment.TOP_CENTER);
				grid.addComponent(comentarios, 2, 1, 3, 1);
				grid.setComponentAlignment(comentarios, Alignment.TOP_CENTER);
				grid.addComponent(registrar, 0, 2);
				grid.setComponentAlignment(registrar, Alignment.MIDDLE_LEFT);

				ventanaRegistrar.setContent(grid);

				UI.getCurrent().addWindow(ventanaRegistrar);

			}
		});

		// Bot�n de alta de vendedor

		final Button vendedorAlta = new Button("Vendedores");
		vendedorAlta.setStyleName("boton_simple");
		// vendedorAlta.setStyleName(ValoTheme.BUTTON_LINK);

		// Empieza ventana de vendedores
		vendedorAlta.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				final Window ventanaRegistrar = new Window();
				ventanaRegistrar.center();
				ventanaRegistrar.setHeight("60%");
				ventanaRegistrar.setWidth("55%");

				final ComboBox vendedor = llenarComboBoxVendedor(
						new ComboBox(), usuario.getCustidsRelacionados());
				vendedor.setWidth("100%");

				final TextField nombre = new TextField("Nombre");
				nombre.setWidth("100%");

				final TextArea observaciones = new TextArea("Observaciones");
				observaciones.setWidth("100%");
				observaciones.setMaxLength(1000);

				Button registrar = new Button("Registrar vendedor");
				registrar.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {

						if (nombre.getValue().equals("")) {
							Notification.show("Se debe de ingresar un nombre",
									Type.WARNING_MESSAGE);
						} else {

							Mysql sql = new Mysql();
							try {

								sql.insertarSimple("insert into "+SqlConf.obtenerBase()+"inventario.vendedores values ("
										+ "null, '"
										+ usuario.getCustid()
										+ "','"
										+ Funcion.quitarComillas(nombre
												.getValue())
										+ "',"
										+ "'"
										+ Funcion.quitarComillas(observaciones
												.getValue()) + "')");

								nombre.setValue("");
								observaciones.setValue("");

								// Actualizar vendedor
								vendedor.removeAllItems();

								BeanConexion beanCon = sql
										.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid = '"
												+ usuario.getCustid() + "'");

								if (!beanCon.getRespuesta().equals("OK")) {
									throw new Exception(beanCon.getRespuesta());
								}

								ResultSet rs = beanCon.getRs();

								while (rs.next()) {
									vendedor.addItem(rs.getInt("id"));
									vendedor.setItemCaption(rs.getInt("id"),
											rs.getString("nombre"));
								}

								Notification n = new Notification(
										"Registro correcto de vendedor",
										Type.TRAY_NOTIFICATION);
								n.setDelayMsec(2000);
								n.setPosition(Notification.POSITION_CENTERED); // POSITION_TOP_RIGHT
								n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
								n.show(UI.getCurrent().getPage());

							} catch (Exception e) {
								Notification.show(
										"Error al registrar: " + e.toString(),
										Type.ERROR_MESSAGE);
								e.printStackTrace();
							} finally {
								sql.cerrar();
							}

							// dos.removeAllComponents();
							// dos.addComponent(generarTabla(tablas,
							// usuario.getCustid()));

						}
					}
				});

				Button eliminar = new Button("Eliminar vendedor");
				eliminar.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {

						if (vendedor.getValue() == null) {
							Notification.show("Se debe de ingresar un nombre",
									Type.WARNING_MESSAGE);
						} else {

							ConfirmDialog.show(UI.getCurrent(), "Confirmación",
									"¿Estás seguro de querer eliminarlo?",
									"SI", "NO", new ConfirmDialog.Listener() {

										public void onClose(ConfirmDialog dialog) {
											if (dialog.isConfirmed()) {

												Mysql sql = new Mysql();
												try {

													sql.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.vendedores where id = "
															+ vendedor
																	.getValue());

													// Actualizo vendedor
													vendedor.removeAllItems();

													BeanConexion beanCon = sql.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid = '"
															+ usuario
																	.getCustid()
															+ "'");

													if (!beanCon.getRespuesta()
															.equals("OK")) {
														throw new Exception(
																beanCon.getRespuesta());
													}

													ResultSet rs = beanCon
															.getRs();

													while (rs.next()) {
														vendedor.addItem(rs
																.getInt("id"));
														vendedor.setItemCaption(
																rs.getInt("id"),
																rs.getString("nombre"));
													}

													Notification n = new Notification(
															"Registro de vendedor eliminado correctamente",
															Type.TRAY_NOTIFICATION);
													n.setDelayMsec(2000);
													n.setPosition(Notification.POSITION_CENTERED); // POSITION_TOP_RIGHT
													n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
													n.show(UI.getCurrent()
															.getPage());

												} catch (Exception e) {
													Notification.show(
															"Error al eliminar: "
																	+ e.toString(),
															Type.ERROR_MESSAGE);
													e.printStackTrace();
												} finally {
													sql.cerrar();
												}

											}
										}

									});

						}
					}
				});

				//genero LayOuts para info
				
				HorizontalLayout vendedoresGeneral = new HorizontalLayout();
					vendedoresGeneral.setMargin(true);
					vendedoresGeneral.setHeight("100%");
					vendedoresGeneral.setWidth("100%");
					
				VerticalLayout generarVendedores = new VerticalLayout();
					generarVendedores.setMargin(true);
					generarVendedores.setHeight("80%");
					generarVendedores.setWidth("80%");
					generarVendedores.setStyleName(ValoTheme.LAYOUT_WELL);
					generarVendedores.setCaption("Registrar vendedor");
					
					generarVendedores.addComponent(nombre);
						generarVendedores.setComponentAlignment(nombre, Alignment.MIDDLE_LEFT);
					generarVendedores.addComponent(observaciones);
						generarVendedores.setComponentAlignment(observaciones, Alignment.MIDDLE_LEFT);
					generarVendedores.addComponent(registrar);
						generarVendedores.setComponentAlignment(registrar, Alignment.BOTTOM_LEFT);
					
				VerticalLayout eliminarVendedores = new VerticalLayout();
					eliminarVendedores.setMargin(true);
					eliminarVendedores.setStyleName(ValoTheme.LAYOUT_WELL);
					eliminarVendedores.setHeight("80%");
					eliminarVendedores.setWidth("80%");
					eliminarVendedores.setCaption("Eliminar vendedor");
					
					eliminarVendedores.addComponent(vendedor);
						eliminarVendedores.setComponentAlignment(vendedor, Alignment.MIDDLE_CENTER);
						eliminarVendedores.setExpandRatio(vendedor, 1);
					eliminarVendedores.addComponent(eliminar);
						eliminarVendedores.setComponentAlignment(eliminar, Alignment.TOP_RIGHT);
						eliminarVendedores.setExpandRatio(eliminar, 2);
						
				
						vendedoresGeneral.addComponent(generarVendedores);
							vendedoresGeneral.setComponentAlignment(generarVendedores, Alignment.MIDDLE_CENTER);
						vendedoresGeneral.addComponent(eliminarVendedores);
							vendedoresGeneral.setComponentAlignment(eliminarVendedores, Alignment.MIDDLE_CENTER);

				ventanaRegistrar.setContent(vendedoresGeneral);

				UI.getCurrent().addWindow(ventanaRegistrar);

			}
		});

		HorizontalLayout insertarLay = new HorizontalLayout();
		insertarLay.setCaption("Insertar");
		insertarLay.setMargin(true);
		insertarLay.setStyleName(ValoTheme.LAYOUT_WELL);
		insertarLay.setWidth("100%");
		insertarLay.setHeight("100");

		HorizontalLayout reporte = new HorizontalLayout();
		reporte.setCaption("Reportes");
		reporte.setMargin(true);
		reporte.setStyleName(ValoTheme.LAYOUT_WELL);
		reporte.setWidth("100%");
		reporte.setHeight("100");

		reporte.addComponent(fechaUno);
		reporte.setComponentAlignment(fechaUno, Alignment.BOTTOM_CENTER);
		reporte.addComponent(fechaDos);
		reporte.setComponentAlignment(fechaDos, Alignment.BOTTOM_CENTER);
		reporte.addComponent(buscar);
		reporte.setComponentAlignment(buscar, Alignment.BOTTOM_CENTER);

		HorizontalLayout registros = new HorizontalLayout();
		registros.setWidth("85%");
		registros.setHeight("100");
		registros.setCaption("Registros");
		registros.setMargin(true);
		registros.setStyleName(ValoTheme.LAYOUT_WELL);
		registros.addComponent(insertar);
		registros.setComponentAlignment(insertar, Alignment.MIDDLE_LEFT);
		registros.addComponent(devolucion);
		registros.setComponentAlignment(devolucion, Alignment.MIDDLE_CENTER);
		registros.addComponent(vendedorAlta);
		registros.setComponentAlignment(vendedorAlta, Alignment.MIDDLE_RIGHT);

		cabecera.addComponent(registros);
		cabecera.addComponent(reporte);

		respuesta.addComponent(cabecera);
		// respuesta.setComponentAlignment(cabecera, Alignment.BOTTOM_RIGHT);
		cuerpo.addComponent(tablas);
		respuesta.addComponent(cuerpo);

		return respuesta;

	}

	// Empiezan m�todos externos

	private VerticalLayout generarTabla(VerticalLayout tablas, String custid,
			String fechaInicial, String fechaFinal) {

		Mysql sql = new Mysql();

		try {

			tablas.removeAllComponents();

			BeanConexion beanCon = sql
					.conexionSimple("select id,\r\n"
							+ "  (select nombre from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) as producto,\r\n"
							+ "  (select nombre from "+SqlConf.obtenerBase()+"inventario.clientes where id = a.id_cliente) as cliente,\r\n"
							+ "  fecha,\r\n"
							+ "  if(precio < 0, cantidad * -1, cantidad) as cantidad	,\r\n"
							+ "  (precio*cantidad) as subtotal ,\r\n"
							+ "\r\n"
							+ "  if((select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) = 'SI',\r\n"
							+ "  round(((descuento - (descuento)*.16)),2), round(descuento,2)) as descuentos,\r\n"
							+ "\r\n"
							+ "  if((select grava_iva from "+SqlConf.obtenerBase()+"inventario.productos where id = a.id_producto) = 'SI',\r\n"
							+ "  round(((precio*cantidad)-((descuento - (descuento)*.16)))*.16,2), (iva*cantidad)) as impuesto,\r\n"
							+ "\r\n"
							+ "  ((select subtotal) - (select descuentos)) + (select impuesto) as total,\r\n"
							+ "\r\n"
							+ "  concat('(',total*cantidad,')') as 'Total sin descuento',\r\n"
							+ "  concat('(',descuento,')') as 'Descuento total otorgado',\r\n"
							+ "\r\n"
							+ "  descuento_desc, \r\n"
							+ " vendedor, comentarios "
							+ "from "+SqlConf.obtenerBase()+"inventario.ventas a FORCE INDEX (custid)\r\n"
							+ "where custid in (" + custid + ")\r\n"
							+ "  and fecha between '" + fechaInicial
							+ "' and '" + fechaFinal + "'");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			tablas.removeAllComponents();
			// Genero el array con las columnas a exportar en Excel (no
			// incluyendo ID)
			Object[] columnasExportar = { "PRODUCTO", "CLIENTE", "FECHA",
					"CANTIDAD", "SUBTOTAL", "DESCUENTOS", "IMPUESTO", "TOTAL",
					"TOTAL SIN DESCUENTO", "DESCUENTO TOTAL OTORGADO",
					"DESCUENTO_DESC", "VENDEDOR", "COMENTARIOS" };
			tablas.addComponent(crearCon2FiltrosVentas(tablas, rs, "PRODUCTO",
					"CLIENTE", columnasExportar, fechaInicial, fechaFinal));
			tablas.setComponentAlignment(tablas.getComponent(0),
					Alignment.TOP_CENTER);

		} catch (Exception e) {
			Notification.show("Error en la aplicaci�n: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		return tablas;

	}

	@SuppressWarnings("deprecation")
	public VerticalLayout crearCon2FiltrosVentas(final VerticalLayout tablas,
			ResultSet rs, final String tituloColumnaFiltrar,
			final String tituloColumnaFiltrar2,
			final Object[] columnasExportar, final String fechaInicial,
			final String fechaFinal) throws UnsupportedOperationException,
			Exception {

		VerticalLayout respuesta = new VerticalLayout();
		respuesta.setWidth("95%");

		HorizontalLayout interior = new HorizontalLayout();
		interior.setMargin(true);
		interior.setWidth("30%");
		// Text field for inputting a filter
		final TextField filtro = new TextField();	
			filtro.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar.toLowerCase()));
			filtro.setStyleName("boton_simple");
		final TextField filtro2 = new TextField();
			filtro2.setInputPrompt(Funcion.primeraLetraMayuscula(tituloColumnaFiltrar2.toLowerCase()));
			filtro2.setStyleName("boton_simple");
		interior.addComponent(filtro);
		interior.setComponentAlignment(filtro, Alignment.MIDDLE_LEFT);
		interior.addComponent(filtro2);
		interior.setComponentAlignment(filtro2, Alignment.MIDDLE_CENTER);

		respuesta.addComponent(interior);

		final Table tabla = new Table();
		tabla.setLocale(Locale.US);
		respuesta.addComponent(tabla);

		ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();

		for (int i = 1; i < count + 1; i++) {
			String name = meta.getColumnName(i);

			if (name.toUpperCase().equals("SUBTOTAL")
					|| name.toUpperCase().equals("CANTIDAD")
					|| name.toUpperCase().equals("DESCUENTOS")
					|| name.toUpperCase().equals("IMPUESTO")
					|| name.toUpperCase().equals("TOTAL")) {
				tabla.addContainerProperty(name.toUpperCase(), Double.class,
						null);
				tabla.setColumnAlignment(name.toUpperCase(), Table.ALIGN_RIGHT);
			} else if (name.toUpperCase().equals("FECHA")
					|| name.toUpperCase().equals("TOTAL SIN DESCUENTO")
					|| name.toUpperCase().equals("DESCUENTO TOTAL OTORGADO")) {
				tabla.addContainerProperty(name.toUpperCase(), String.class,
						null);
				tabla.setColumnAlignment(name.toUpperCase(), Table.ALIGN_RIGHT);
			} else if (name.toUpperCase().equals("ID")) {
				tabla.addContainerProperty(name.toUpperCase(), String.class,
						null);
				tabla.setColumnCollapsingAllowed(true);
				tabla.setColumnCollapsed(name.toUpperCase(), true);
			} else {
				tabla.addContainerProperty(name.toUpperCase(), String.class,
						null);
				tabla.setColumnAlignment(name.toUpperCase(), Table.ALIGN_LEFT);

			}
		}

		int id = 0;
		while (rs.next()) {

			if (count == 2) {
				tabla.addItem(
						new Object[] { rs.getObject(1), rs.getObject(2) }, id);
			} else if (count == 3) {
				tabla.addItem(new Object[] { rs.getString(1), rs.getString(2),
						rs.getString(3) }, id);
			} else if (count == 4) {
				tabla.addItem(new Object[] { rs.getObject(1), rs.getObject(2),
						rs.getObject(3), rs.getObject(4) }, id);
			} else if (count == 5) {
				tabla.addItem(new Object[] { rs.getString(1), rs.getString(2),
						rs.getString(3), rs.getDouble(4), rs.getString(5) }, id);
			} else if (count == 6) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getDouble(4),
								rs.getDouble(5), rs.getString(6) }, id);
			} else if (count == 7) {
				tabla.addItem(
						new Object[] { rs.getObject(1), rs.getObject(2),
								rs.getObject(3), rs.getObject(4),
								rs.getObject(5), rs.getObject(6),
								rs.getObject(7) }, id);
			} else if (count == 8) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								Encriptar.Desencriptar(rs.getString(3)),
								rs.getDouble(4), rs.getString(5),
								rs.getDouble(6), rs.getDouble(7),
								rs.getDouble(8) }, id);
			} else if (count == 9) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								Encriptar.Desencriptar(rs.getString(3)),
								rs.getDouble(4), rs.getString(5),
								rs.getDouble(6), rs.getDouble(7),
								rs.getDouble(8), rs.getString(9) }, id);
			} else if (count == 10) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								Encriptar.Desencriptar(rs.getString(3)),
								rs.getDouble(4), rs.getString(5),
								rs.getDouble(6), rs.getDouble(7),
								rs.getDouble(8), rs.getDouble(9),
								rs.getString(10) }, id);
			} else if (count == 11) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11) }, id);
			} else if (count == 12) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								Encriptar.Desencriptar(rs.getString(3)),
								rs.getString(4), rs.getDouble(5),
								rs.getDouble(6), rs.getDouble(7),
								rs.getDouble(8), rs.getDouble(9),
								rs.getString(10), rs.getString(11),
								rs.getString(12) }, id);
			} else if (count == 13) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13) }, id);
			} else if (count == 14) { // Usa �ste
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								Encriptar.Desencriptar(rs.getString(3)),
								rs.getString(4), rs.getDouble(5),
								rs.getDouble(6), rs.getDouble(7),
								rs.getDouble(8), rs.getDouble(9),
								rs.getString(10), rs.getString(11),
								rs.getString(12), rs.getString(13),
								rs.getString(14) }, id);
			} else if (count == 15) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14),
								rs.getString(15) }, id);
			} else if (count == 16) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14),
								rs.getString(15), rs.getString(16) }, id);
			} else if (count == 17) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14),
								rs.getString(15), rs.getString(16),
								rs.getString(17) }, id);
			} else if (count == 18) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14),
								rs.getString(15), rs.getString(16),
								rs.getString(17), rs.getString(18) }, id);
			} else if (count == 19) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14),
								rs.getString(15), rs.getString(16),
								rs.getString(17), rs.getString(18),
								rs.getString(19) }, id);
			} else if (count == 20) {
				tabla.addItem(
						new Object[] { rs.getString(1), rs.getString(2),
								rs.getString(3), rs.getString(4),
								rs.getString(5), rs.getString(6),
								rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10),
								rs.getString(11), rs.getString(12),
								rs.getString(13), rs.getString(14),
								rs.getString(15), rs.getString(16),
								rs.getString(17), rs.getString(18),
								rs.getString(19), rs.getString(20) }, id);
			}
			id++;
		}
		tabla.setSelectable(true);
		tabla.setMultiSelect(true);
		tabla.setHeight("300");
		tabla.setWidth("95%");

		ExcelExporter excelExporter = new ExcelExporter(tabla);
		Date fecha = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		excelExporter.setDownloadFileName(format.format(fecha));
		excelExporter.setCaption("Excel");
		excelExporter.setVisibleColumns(columnasExportar);

		respuesta.addComponent(excelExporter);

		// Filter table according to typed input
		filtro.addListener(new TextChangeListener() {
			SimpleStringFilter filter = null;

			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) tabla.getContainerDataSource();

				// Remove old filter
				if (filter != null)
					f.removeContainerFilter(filter);

				// Set new filter for the "Name" column
				filter = new SimpleStringFilter(tituloColumnaFiltrar, event
						.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});

		filtro2.addListener(new TextChangeListener() {
			SimpleStringFilter filter = null;

			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable) tabla.getContainerDataSource();

				// Remove old filter
				if (filter != null)
					f.removeContainerFilter(filter);

				// Set new filter for the "Name" column
				filter = new SimpleStringFilter(tituloColumnaFiltrar2, event
						.getText(), true, false);
				f.addContainerFilter(filter);
			}
		});

		// Doble clic
		tabla.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {

					Property<String> itemProperty = event.getItem()
							.getItemProperty("ID");
					final String id = itemProperty.getValue();

					final UsuarioDTO usuario = (UsuarioDTO) UI.getCurrent().getData();

					final Window ventanaActualizar = new Window("Eliminar");
					ventanaActualizar.center();
					ventanaActualizar.setHeight("20%");
					ventanaActualizar.setWidth("20%");

					final Mysql sql = new Mysql();

					try {

						Button eliminar = new Button("Eliminar");
						eliminar.addListener(new Button.ClickListener() {
							public void buttonClick(ClickEvent event) {

								ConfirmDialog.show(
										UI.getCurrent(),
										"Confirmación",
										"¿Estás seguro de querer eliminar esta venta?",
										"SI", "NO",
										new ConfirmDialog.Listener() {

											public void onClose(
													ConfirmDialog dialog) {
												if (dialog.isConfirmed()) {

													Mysql sql = new Mysql();
													String respuesta = "NO";

													try {

														sql.transaccionAbrir();

														// Saco si reflej� en
														// almacen o no
														String afectaAlmacen = "";
														BeanConsulta bean = sql
																.consultaSimple("select afecta_almacen from "+SqlConf.obtenerBase()+"inventario.ventas where id = "
																		+ id);

														if (!bean
																.getRespuesta()
																.equals("OK")) {
															throw new Exception(
																	bean.getRespuesta());
														}

														afectaAlmacen = bean
																.getDato();

														if (afectaAlmacen
																.equals("SI")) {

															// Borro de almacen
															respuesta = sql
																	.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.almacen where id_venta = "
																			+ id);

															if (!respuesta
																	.equals("OK")) {
																throw new Exception(
																		respuesta);
															}

														}

														// Borro de ventas
														respuesta = sql
																.insertarSimple("delete from "+SqlConf.obtenerBase()+"inventario.ventas where id = "
																		+ id);

														if (!respuesta
																.equals("OK")) {
															throw new Exception(
																	respuesta);
														} else {

															sql.transaccionCommit();
															sql.transaccionCerrar();

															generarTabla(
																	tablas,
																	usuario.getCustidsRelacionados(),
																	fechaInicial,
																	fechaFinal);

															Notification n = new Notification(
																	"Movimiento eliminado correctamente",
																	Type.TRAY_NOTIFICATION);
															n.setDelayMsec(2000);
															n.setPosition(Notification.POSITION_CENTERED); // POSITION_TOP_RIGHT
															n.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
															n.show(UI
																	.getCurrent()
																	.getPage());

															ventanaActualizar
																	.close();

														}

													} catch (Exception e) {

														sql.transaccionRollBack();
														sql.transaccionCerrar();

														Notification.show(
																"Error en la aplicaci�n: "
																		+ e.toString(),
																Type.ERROR_MESSAGE);
														e.printStackTrace();
													} finally {
														sql.cerrar();
													}

												}
											}

										});

							}
						});

						GridLayout grid = new GridLayout(1, 1);
						grid.setMargin(true);
						grid.setWidth("100%");
						grid.setHeight("100%");
						grid.addComponent(eliminar, 0, 0);
						grid.setComponentAlignment(eliminar,
								Alignment.MIDDLE_CENTER);

						ventanaActualizar.setContent(grid);

						UI.getCurrent().addWindow(ventanaActualizar);

					} catch (Exception e) {
						Notification.show(
								"Error en la aplicaci�n: " + e.toString(),
								Type.ERROR_MESSAGE);
						e.printStackTrace();
					} finally {
						sql.cerrar();
					}

				}
			}
		});

		return respuesta;
	}

	// Empiezan m�todos externos

	private AutocompleteSuggestionProvider listaCategorias(String custid) {

		List<String> nombres = new ArrayList<String>();
		Mysql sql = new Mysql();

		try {

			BeanConexion beanCon = sql
					.conexionSimple("select categoria from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"
							+ custid + "' group by categoria");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			while (rs.next()) {
				nombres.add(rs.getString("categoria"));
			}

		} catch (Exception e) {
			Notification.show("Error en la aplicaci�n: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(
				nombres, MatchMode.CONTAINS, true, Locale.US);
		return suggestionProvider;

	}

	private AutocompleteSuggestionProvider listaNombres(String custid) {

		List<String> nombres = new ArrayList<String>();
		Mysql sql = new Mysql();

		try {

			BeanConexion beanCon = sql
					.conexionSimple("select nombre from "+SqlConf.obtenerBase()+"inventario.inventario where custid = '"
							+ custid + "' group by nombre");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			while (rs.next()) {
				nombres.add(rs.getString("nombre"));
			}

		} catch (Exception e) {
			Notification.show("Error en la aplicación: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(
				nombres, MatchMode.CONTAINS, true, Locale.US);
		return suggestionProvider;

	}

	private ComboBox llenarComboBox(ComboBox combo, String custid) {

		Mysql sql = new Mysql();

		try {

			combo.setCaption("Productos");

			BeanConexion beanCon = sql
					.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.productos where custid in ("
							+ custid + ")");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			while (rs.next()) {
				combo.addItem(rs.getInt("id"));
				combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
			}

		} catch (Exception e) {
			Notification.show("Error en la aplicación: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		combo.setNullSelectionAllowed(false);

		return combo;

	}

	private ComboBox llenarComboBoxCliente(ComboBox combo, String custid) {

		Mysql sql = new Mysql();

		try {

			combo.setCaption("Clientes");

			BeanConexion beanCon = sql
					.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.clientes where custid in ("
							+ custid + ") and activo = 'SI'");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			while (rs.next()) {
				combo.addItem(rs.getInt("id"));
				combo.setItemCaption(rs.getInt("id"),
						Encriptar.Desencriptar(rs.getString("nombre")));
			}

		} catch (Exception e) {
			Notification.show("Error en la aplicación: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		combo.setNullSelectionAllowed(false);

		return combo;

	}

	private ComboBox llenarComboBoxVendedor(ComboBox combo, String custid) {

		Mysql sql = new Mysql();

		try {

			combo.setCaption("Vendedor");

			BeanConexion beanCon = sql
					.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid in ("
							+ custid + ")");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			while (rs.next()) {
				combo.addItem(rs.getInt("id"));
				combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
			}

		} catch (Exception e) {
			Notification.show("Error en la aplicación: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		combo.setNullSelectionAllowed(false);

		return combo;

	}

	private ComboBox llenarComboBoxVendedoresAsignados(ComboBox combo,
			String custid) {

		Mysql sql = new Mysql();

		try {

			combo.setCaption("Vendedor asignado");

			BeanConexion beanCon = sql
					.conexionSimple("SELECT id, nombre FROM "+SqlConf.obtenerBase()+"inventario.vendedores where custid in ("
							+ custid + ") order by nombre");

			if (!beanCon.getRespuesta().equals("OK")) {
				throw new Exception(beanCon.getRespuesta());
			}

			ResultSet rs = beanCon.getRs();

			combo.addItem(0);
			combo.setItemCaption(0, "Ninguno");

			while (rs.next()) {
				combo.addItem(rs.getInt("id"));
				combo.setItemCaption(rs.getInt("id"), rs.getString("nombre"));
			}

			combo.setNullSelectionAllowed(true);

		} catch (Exception e) {
			Notification.show("Error en la aplicación: " + e.toString(),
					Type.ERROR_MESSAGE);
			e.printStackTrace();
		} finally {
			sql.cerrar();
		}

		combo.setNullSelectionAllowed(false);

		return combo;

	}

	private Date getFirstDateOfCurrentMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,
				cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

}

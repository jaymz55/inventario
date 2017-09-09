package notificaciones;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class NotificacionError extends Notification{

	private static final long serialVersionUID = 1L;

	public NotificacionError(String caption, int tiempo) {
		super(caption);
		NotificacionError.this.setDelayMsec(tiempo);
		NotificacionError.this.setPosition(Position.MIDDLE_CENTER);
		NotificacionError.this.setStyleName(ValoTheme.NOTIFICATION_ERROR);
		NotificacionError.this.show(UI.getCurrent().getPage());
	}
	
	public static NotificacionError mostrar(String texto, int tiempo){
		
		return new NotificacionError("Error en sistema: "+texto, tiempo);
		
	}
	
}

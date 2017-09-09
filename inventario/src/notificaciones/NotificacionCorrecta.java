package notificaciones;

import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class NotificacionCorrecta extends Notification{

	private static final long serialVersionUID = 1L;

	public NotificacionCorrecta(String caption) {
		super(caption);
		NotificacionCorrecta.this.setDelayMsec(2000);
		NotificacionCorrecta.this.setPosition(Notification.POSITION_CENTERED); //POSITION_TOP_RIGHT
		NotificacionCorrecta.this.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
		NotificacionCorrecta.this.show(UI.getCurrent().getPage());
	}
	
	public static NotificacionCorrecta mostrar(String texto){
		
		return new NotificacionCorrecta(texto);
		
	}

}

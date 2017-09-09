package cookies;

import javax.servlet.http.Cookie;

import com.vaadin.server.VaadinService;

public class Cookies {

	//Declaro las variables
	String ubicacion = VaadinService.getCurrent().getBaseDirectory().getPath().substring(VaadinService.getCurrent().getBaseDirectory().getPath().length() - 4);
	/*Esto lo declaro para ver si estoy en la lap o en la página, y con 
	* base en eso, defino qué tipo de cookies voy a usar
	**/
	
	String online = "NO";
	
	public Cookies(){
	
		if(ubicacion.equals("ROOT")){
			online = "SI";
		}
	
	}
	
	public Cookie getCookieByName(String name) { 
		  // Fetch all cookies from the request 
		  Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

		  // Iterate to find cookie by its name 
		  for (Cookie cookie : cookies) { 
		    if (name.equals(cookie.getName())) { 
		      return cookie; 
		    } 
		  }

		  return null; 
	}
	
	public void createCookie(final String name, final String value, final int maxAge) {

		// Create a new cookie
		Cookie myCookie = new Cookie(name, value);
		myCookie.setMaxAge(maxAge);
		
		//Si la app va a correr como ROOT, debo usar éste:
		
		if(online.equals("SI"))
			myCookie.setPath("/");
		else
			myCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
		
		VaadinService.getCurrentResponse().addCookie(myCookie);
		
		//return myCookie;
		
	}
	
	public void deleteCookie(String name){
		Cookie cookie = getCookieByName(name);

	    if (cookie != null) {
	        cookie.setValue(null);
	        cookie.setMaxAge(0);
	        
	        if(online.equals("SI"))
	        	cookie.setPath("/");
	        else
	        	cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
	        
	        VaadinService.getCurrentResponse().addCookie(cookie);
	    }
	}
	
	
}

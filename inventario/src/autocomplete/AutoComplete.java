package autocomplete;

import java.util.List;
import java.util.Locale;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

public abstract class AutoComplete extends AutocompleteTextField{

	//Variables
		private static final long serialVersionUID = 1L;
	
		//Constructor
		public AutoComplete(String caption){
			AutoComplete.this.setWidth("80%");
			AutoComplete.this.setCache(true); // Client side should cache suggestions
			AutoComplete.this.setDelay(50); // Delay before sending a query to the server
			AutoComplete.this.setItemAsHtml(false); // Suggestions contain html formating. If true, make sure that the html is save to use!
			AutoComplete.this.setMinChars(1); // The required value length to trigger a query
			AutoComplete.this.setScrollBehavior(ScrollBehavior.REFRESH); // The method that should be used to compensate scrolling of the page
			AutoComplete.this.setSuggestionLimit(0); // The max amount of suggestions send to the client. If the limit is >= 0 no limit is applied
			AutoComplete.this.setCaption(caption);

		}
	
	//Metodos abstractos
		protected abstract void maximoCaracteres();
		protected abstract void setSuggestionProvider();
	
	//Metodos
		protected AutocompleteSuggestionProvider obtenerLista(List<String> nombres){
			
			AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(nombres, MatchMode.CONTAINS, true, Locale.US);
			return suggestionProvider;
			
		}
}

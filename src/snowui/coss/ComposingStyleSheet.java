package snowui.coss;

import java.util.HashMap;

import disaethia.io.nbt.NBTCompound;
import disaethia.io.nbt.NBTList;

public class ComposingStyleSheet {
	
	// -- ++  IO  ++ -- //
	
	public static ComposingStyleSheet from(NBTCompound nbt) {
		ComposingStyleSheet sheet = new ComposingStyleSheet();
		setFromNBT(sheet, nbt);
		return sheet;
	}
	
	private static void setFromNBT(ComposingStyleSheet sheet, NBTCompound nbt) {
		
		for (int i = 0; i < nbt.length(); i++) {
			NBTCompound c_info = nbt.get(i).getCompound();
			String 		type   = nbt.get(i).getName();
			
			if (c_info.get("contains") != null) {
				NBTList contains = c_info.get("contains").getList();
				for (int j = 0; j < contains.length.get(); j++) {
					sheet.addContains(type, contains.getString(j).get());
				}
			}
			
			if (c_info.get("properties") != null) {
				NBTCompound properties = c_info.get("properties").getCompound();
				for (int j = 0; j < properties.length(); j++) {
					sheet.setProperty(type, properties.get(j).getName(),  properties.get(j).getString().get());
				}
			}
			
			if (c_info.get("predicates") != null) {
				NBTCompound predicates = c_info.get("predicates").getCompound();
				for (int j = 0; j < predicates.length(); j++) {
					sheet.setPredicate(type, predicates.get(j).getName(),  predicates.get(j).getString().get());
				}
			}
		}
	}
	
	// -- ++  Default Style  ++ -- //
	
	@SuppressWarnings("unused")
	private static void HELPERSetUniformMargins(ComposingStyleSheet sheet, String type, int margin) {
		HELPERSetUniformMargins(sheet, type, Integer.toString(margin));
	}
	
	private static void HELPERSetUniformMargins(ComposingStyleSheet sheet, String type, String margin) {
		sheet.setProperty(type, "left_margin", 		margin);
		sheet.setProperty(type, "right_margin", 	margin);
		sheet.setProperty(type, "top_margin", 		margin);
		sheet.setProperty(type, "bottom_margin",	margin);
	}
	
	private static void HELPERSetUniformSizeLimit(ComposingStyleSheet sheet, String type, String limit) {
		sheet.setProperty(type, "max_width", 	limit); // flex, container, or [number]px (EX: 10px)
		sheet.setProperty(type, "min_width", 	limit); // flex, container, or [number]px (EX: 10px)
		sheet.setProperty(type, "max_height", 	limit); // flex, container, or [number]px (EX: 10px)
		sheet.setProperty(type, "min_height", 	limit); // flex, container, or [number]px (EX: 10px)
	}
	
	public static void setDefaults(ComposingStyleSheet sheet) {

		// -- Default -- //
		
		sheet.setProperty("default", "base_color", 				"WHITE");
		sheet.setProperty("default", "outline_color", 			"BLACK");
		sheet.setProperty("default", "preview_color", 			"css_rgba(187, 161, 255, 0.5)");
		sheet.setProperty("default", "background_color", 		"css_rgba(0,0,0,0)");
		sheet.setProperty("default", "background_margin", 		"4");
		sheet.setProperty("default", "horizontal_alignment",	"left"); 	// Left, right, middle
		sheet.setProperty("default", "vertical_alignment", 		"top");	 	// Top, bottom, middle
		sheet.setProperty("default", "outline_size", 			"0");
		sheet.setProperty("default", "size", 					"18"); 		// Symbol size, scroll bar size, etc
		sheet.setProperty("default", "outline_margin", 			"3");
		sheet.setProperty("default", "list_spacing", 			"0");
		sheet.setProperty("default", "list_indent", 			"0");
		
		HELPERSetUniformMargins		(sheet, "default", "2");				// Sets [left/top/right/bottom]_margin
		HELPERSetUniformSizeLimit	(sheet, "default", "flex");				// sets [{min/max}/{width/height}]
	
		// -- Built-in types -- //
		
		sheet.setProperty("snowui-accent_bg", 	  		"background_color", 		"DESBLUE");
		sheet.setProperty("snowui-accent_bg", 			"background_margin", 		"2");
		sheet.setProperty("snowui-accent", 	  			"base_color", 				"DESBLUE");
		sheet.setProperty("snowui-hover_accent", 		"base_color", 				"DARKDESBLUE");
		sheet.setProperty("snowui-down_accentown", 		"base_color", 				"DARKDESBLUE");
		sheet.setProperty("snowui-dark", 	  			"base_color", 				"BASIC_PANEL");
		sheet.setProperty("snowui-hover_dark", 			"base_color", 				"BLACK");
		sheet.setProperty("snowui-down_dark", 			"base_color", 				"BLACK");
		sheet.setProperty("snowui-down", 	  			"background_color", 		"BASIC_PANEL_DOWN");
		sheet.setProperty("snowui-hover", 	  			"background_color", 		"BASIC_PANEL_HOVER");
		sheet.setProperty("snowui-selected", 			"outline_size", 			"2");
		sheet.setProperty("snowui-selected", 			"outline_color", 			"DESYELLOW");
		sheet.setProperty("snowui-selected_c", 			"outline_margin", 			"0");
		sheet.setProperty("snowui-w_contained", 		"min_width", 				"container");
		sheet.setProperty("snowui-w_contained", 		"max_width", 				"container");
		sheet.setProperty("snowui-h_contained", 		"min_height", 				"container");
		sheet.setProperty("snowui-h_contained", 		"max_height", 				"container");
		sheet.setProperty("snowui-centered",  			"horizontal_alignment",		"middle");
		sheet.setProperty("snowui-centered",  			"vertical_alignment", 		"middle");
		sheet.setProperty("snowui-centered",  			"horizontal_alignment",		"middle");
		sheet.setProperty("snowui-centered",  			"vertical_alignment", 		"middle");
		sheet.setProperty("snowui-panel",  				"outline_color", 			"TRANSPARENT_WHITE");	
		sheet.setProperty("snowui-panel",  				"outline_size", 			"1");	
		sheet.setProperty("snowui-panel",  				"outline_margin", 			"5");	
		sheet.setProperty("snowui-panel",  				"background_color", 		"BLACK");
		sheet.setProperty("snowui-small", 				"size", 					"8");
		sheet.setProperty("snowui-medium", 				"size", 					"12");
		sheet.setProperty("snowui-black", 				"base_color", 				"BLACK");
		sheet.setProperty("snowui-transparent", 		"base_color", 				"TRANSPARENT_WHITE");
		
		HELPERSetUniformMargins(sheet, 					"snowui-panel", 			"8");
		HELPERSetUniformMargins(sheet, 					"snowui-margin_medium", 	"4");
		HELPERSetUniformMargins(sheet, 					"snowui-margin_large", 		"10");
		
		sheet.addContains("snowui-panel_hoverable", 								"snowui-panel");
		sheet.addContains("snowui-contained", 										"snowui-w_contained");
		sheet.addContains("snowui-contained", 										"snowui-h_contained");
		sheet.addContains("snowui-selected_c", 										"snowui-selected");

		sheet.setPredicate("default", 					"SELECTED=true", 			"snowui-selected");
		sheet.setPredicate("snowui-selectable_c", 		"SELECTED=true", 			"snowui-selected_c");
		sheet.setPredicate("snowui-hoverable", 			"HOVERED=true", 			"snowui-hover");
		sheet.setPredicate("snowui-hoverable", 			"HOVERED=true, DOWN=true", 	"snowui-down");
		sheet.setPredicate("snowui-accent", 			"HOVERED=true", 			"snowui-hover_accent");
		sheet.setPredicate("snowui-accent", 			"HOVERED=true, DOWN=true", 	"snowui-down_accent");
		sheet.setPredicate("snowui-panel_hoverable", 	"HOVERED=true", 			"snowui-accent_bg");
		
		// -- Built-in Elements -- //
		
		// In order to allow for stylesheets to override properties in types
		// indirectly, by containing types that override those properties,
		// no default element style should have properties directly set in it.
		
		sheet.addContains("text", 										"snowui-hoverable");
		sheet.addContains("icon", 										"snowui-hoverable");
		sheet.addContains("slider", 									"snowui-small");
		sheet.addContains("slider_handle", 								"snowui-accent");
		sheet.addContains("scrollbar", 									"snowui-dark");
		sheet.addContains("scroll_handle", 								"snowui-accent");
		sheet.addContains("scroll_handle", 								"snowui-small");
		sheet.addContains("scroll_handle", 								"snowui-margin_medium");
		sheet.addContains("title_bar", 									"snowui-dark");
		sheet.addContains("textbox-text", 								"snowui-black");
		sheet.addContains("textbox", 									"snowui-margin_large");
		sheet.addContains("textbox-text", 								"snowui-margin_medium");
		
		sheet.addContains("context_menu", 								"snowui-panel");
		sheet.addContains("input_popup", 								"snowui-panel");
		sheet.addContains("confirm_popup", 								"snowui-panel");
		sheet.addContains("confirm_popup_options", 						"snowui-centered");
		sheet.addContains("context_menu_option", 						"snowui-w_contained");
		sheet.addContains("context_menu_option", 						"snowui-hoverable");
		sheet.addContains("context_menu_option_right_text", 			"snowui-transparent");
		sheet.addContains("context_menu_option_left_icon", 				"snowui-centered");
		sheet.addContains("context_menu_option_left_icon", 				"snowui-medium");
		sheet.addContains("context_menu_option_right_icon", 			"snowui-medium");

		sheet.addContains("tabgroup", 									"snowui-selectable_c");
		sheet.addContains("minitab", 									"snowui-selectable_c");

		sheet.addContains("text_button", 								"snowui-panel_hoverable");

	}
	
	// -- ++  ...  ++ -- //
	
	HashMap<String, COSSType> sheet = new HashMap<>();
		
	public ComposingStyleSheet() {
		setDefaults(this);
	}
	
	public ComposingStyleSheet(NBTCompound nbt) {
		this();
		setFromNBT(this, nbt);
	}
	
	private COSSType getType(String type) {
		COSSType type_info = sheet.get(type);
		if (type_info == null) {
			sheet.put(type, new COSSType());
			type_info = sheet.get(type);
		}
		return type_info;
	}

	public boolean hasProperty(String type, String property) {
		return getProperty(type, property, null) != null;
	}

	public void setProperty(String type, String property, String value) {		
		if (value == null) getType(type).properties().remove(property);
		else getType(type).properties().put(property, COSSProperty.from(value));
	}
	
	public void setPredicate(String type, String predicates, String target_type) {
		setPredicate(type, COSSPredicate.from(predicates), target_type);
	}
	
	public void setPredicate(String type, COSSPredicate predicates, String target_type) {
		getType(type).predicates().put(target_type, predicates);
	}
	
	public void addContains(String type, String contained_type) {
		getType(type).contains().add(contained_type);
	}
	
	/**
	 * Returns the value associated with a given property and type.<br>
	 * If no such value exists, returns the value associated with the given property and the type "default".<br>
	 * If no such value exists, returns null.
	 * @param type
	 * @param property
	 * @return
	 */
	public COSSProperty getProperty(String type, String property, COSSPredicate predicate) {
			   COSSProperty result = getPropertyNoDefault(type, property, predicate);
		if (result == null) result = getPropertyNoDefault("default", property, predicate);
		if (result == null) result = getPropertyNoDefault("default", property, null);
					 return result;
	}
	
	/** Same as getProperty(), but returns 'null' rather than defaulting. 
	 * Useful for searching through contained styles. */
	private COSSProperty getPropertyNoDefault(String type, String property, COSSPredicate predicate) {
		
		// Type doesn't exist, so check for fallbacks ('.') and if none are found, fallback to 'default'.
		if (sheet.get(type) == null) {
			int fallback_cutoff = type.lastIndexOf('.');
			if (fallback_cutoff == -1) return null;
			if (fallback_cutoff >= 0) return getPropertyNoDefault(type.substring(0, fallback_cutoff), property, predicate);
		}

		// Check for predicates
		String target = sheet.get(type).getPredicateTargetType(predicate);
		if (target != null) {
			COSSProperty result_from_predicate = getPropertyNoDefault(target, property, predicate);
			if (result_from_predicate != null) return result_from_predicate;
		}
			
		HashMap<String, COSSProperty> properties_of_this_type = sheet.get(type).properties();

		// If the property is null, check the contained types for the same property,
		if (properties_of_this_type.get(property) == null) {
			COSSProperty result = null;
			COSSType type_info = getType(type);
			for (String contained_type : type_info.contains()) {
				if (contained_type.equals(type)) continue;
				
				result = getPropertyNoDefault(contained_type, property, predicate);
				if (result != null) return result;
			}
		}
	
		return properties_of_this_type.get(property);
	}

}

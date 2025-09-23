package snowui.coss;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector4f;

import disaethia.io.nbt.NBTCompound;
import disaethia.io.nbt.NBTList;
import snowui.utility.Utility;
import snowui.coss.StyleEnums.*;
import snowui.coss.datatypes.StyleProperty;
import snowui.coss.datatypes.StyleType;

import static snowui.coss.StyleEnums.StylePropertyType.*;

public class Stylesheet {
	
	private static Stylesheet fromNBT(NBTCompound nbt) {
		Stylesheet info = new Stylesheet();
		
		for (int i = 0; i < nbt.length(); i++) {
			NBTCompound c_info = nbt.get(i).getCompound();
			String 		type   = nbt.get(i).getName();
			
			if (c_info.get("contains") != null) {
				NBTList contains = c_info.get("contains").getList();
				for (int j = 0; j < contains.length.get(); j++) {
					info.addContains(type, contains.getString(j).get());
				}
			}
			
			if (c_info.get("properties") != null) {
				NBTCompound properties = c_info.get("properties").getCompound();
				for (int j = 0; j < properties.length(); j++) {
					info.setProperty(type, properties.get(j).getName(),  properties.get(j).getString().get());
				}
			}
		}
		
		return info;
	}

	/* -- == ACTUAL CODE == -- */
	

	HashMap<String, Vector4f> cached_string_encoded_colors = new HashMap<String, Vector4f>();
	
	public Stylesheet() {
		
		// Setup default style
		setProperty("default", "base_color", 			"#292f45");
		setProperty("default", "outline_color", 		"BLACK");
		setProperty("default", "text_color", 			"#FFFFFF");		
		setProperty("default", "transparent_highlight",	"TRANSPARENT_WHITE");

		setProperty("default", "left_margin", 			"1");
		setProperty("default", "right_margin", 			"1");
		setProperty("default", "top_margin", 			"1");
		setProperty("default", "bottom_margin", 		"1");
		
		setProperty("default", "max_width", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		setProperty("default", "min_width", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		setProperty("default", "max_height", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		setProperty("default", "min_height", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		
		setProperty("default", "horizontal_alignment", 	"left"); 		// Left, right, middle
		setProperty("default", "vertical_alignment", 	"top");			// Top, bottom, middle

		setProperty("default", "text_size", 			"8");
		setProperty("default", "outline_size", 			"1");
		setProperty("default", "scrollbar_size", 		"4");				
		setProperty("default", "symbol_size", 			"8");				
		setProperty("default", "symbol_x_margin", 		"4");

		setProperty("default", "list_spacing", 			"1");
		setProperty("default", "horizontal_indent", 	"4");
		setProperty("default", "preview_color", 		"css_rgba(255, 255, 255, 0.5)");
		 
		setProperty("default", "tab_bar_height", 		"16");	
		
		// Styles for individual elements
		
		// TODO: Make Scrollbar independently stylable, and make it use the regular base, outline, hover, etc colors.
		// 		 Default style can still be set with 'scrollbar' rather than 'default'.
		setProperty("default", 					"scroll_bar_color", 				"WHITE");
		setProperty("default", 					"scroll_handle_color", 				"BLACK");
		setProperty("default", 					"scroll_handle_highlight_color", 	"#353a42");
		setProperty("default", 					"scroll_handle_drag_color", 		"#1a3054");
		
		// Buttons
		setProperty("button.hover", 			"base_color", 						"#434c73");
		setProperty("button.hover.press", 		"base_color", 						"#0a070f");
		setProperty("button.hover.release", 	"base_color", 						"#31304a");
		setProperty("button.hover.down", 		"base_color", 						"#1c202e");
		setProperty("button.select", 			"base_color", 						"#ffc72b");
		setProperty("button.select.hover", 		"base_color", 						"#d9bc6a");
		setProperty("button.select.hover.down", "base_color", 						"#8f6d38");
		
		// Alerts
		setProperty("alert", 					"base_color", 						"#ff546e");
		setProperty("alert", 					"outline_color", 					"#ffa6b3");
		setProperty("alert", 					"horizontal_alignment", 			"middle");		
		setProperty("alert",					"vertical_alignment", 				"middle");	
		setProperty("alert_content", 			"outline_size", 					"0");
		addContains("alert_content", 			"alert");
		setProperty("alert_close", 				"left_margin", 						"2");
		addContains("alert_close", 				"alert");
		setProperty("alert_close.hover", 		"base_color", 						"#ff8597");
		addContains("alert_close.hover", 		"alert_close");
		setProperty("alert_close.hover.down", 	"base_color", 						"#e83a54");
		addContains("alert_close.hover.down", 	"alert_close");

		// Tabs (aka buttons lol)
		
		setProperty("tabbed_area", 				"base_color", 						"#1f1f30");				
		setProperty("tab", 		 				"min_height", 						"container");
		setProperty("tab", 		 				"max_height", 						"container");
		setProperty("tab_content", 		 		"vertical_alignment", 				"middle");
		setProperty("tab_content", 				"vertical_alignment", 				"middle");
		
		addContains("tab.select", 				"tab"); // For the alignment

		addContains("tab.hover", 				"button.hover");
		addContains("tab.hover.press", 			"button.hover.press");
		addContains("tab.hover.release", 		"button.hover.release");
		addContains("tab.hover.down", 			"button.hover.down");
		addContains("tab.select", 				"button.select");
		addContains("tab.select.hover", 		"button.select.hover");
		addContains("tab.select.hover.down", 	"button.select.hover.down");

		// Possibly a misuse of something called 'style'... consider it later.
		setProperty("default", "split_point_resize_radius", "3");
		setProperty("default", "minimum_drag_distance", "2");

	}
		
	// Style information
	

	
	HashMap<String, StyleType> style = new HashMap<String, StyleType>();
	
	// Caches
	public String hash(String type, String property) { return type+"~"+property; }
	HashMap<String, Integer> ints = new HashMap<String, Integer>();
	
	/**
	 * Returns the value associated with a given property and type.<br>
	 * If no such value exists, returns the value associated with the given property and the type "default".<br>
	 * If no such value exists, returns null.
	 * @param type
	 * @param property
	 * @return
	 */
	public StyleProperty getProperty(String type, String property) {
				   StyleProperty result = getPropertyNoDefault(type, property);
		if (result == null) result = getPropertyNoDefault("default", property);
					 return result;
	}
	
	/** Same as getProperty(), but returns 'null' rather than defaulting. 
	 * Useful for searching through contained styles. */
	private StyleProperty getPropertyNoDefault(String type, String property) {

		// Type doesn't exist, so check for fallbacks ('<') and if none are found, fallback to 'default'.
		if (style.get(type) == null) {
			int fallback_cutoff = type.lastIndexOf('.');
			if (fallback_cutoff == -1) return null;
			if (fallback_cutoff >= 0) return getPropertyNoDefault(type.substring(0, fallback_cutoff), property);
		}
		
		HashMap<String, StyleProperty> properties_of_this_type = style.get(type).properties();

		// If the property is null, check the contained types for the same property,
		if (properties_of_this_type.get(property) == null) {
			StyleProperty result = null;
			StyleType type_info = typeExists(type);
			for (String contained_type : type_info.contains()) {
				if (contained_type.equals(type)) continue;
				
				result = getPropertyNoDefault(contained_type, property);
				if (result != null) return result;
			}
		}
	
		return properties_of_this_type.get(property);
	}
	
	public boolean hasProperty(String type, String property) {
		return (getProperty(type, property) != null);
	}
	
	public void setProperty(String type, String property, String value) {
		StyleProperty setvalue = null;
		
		if (value.equals("true"))  { setvalue = new StyleProperty(BOOLEAN,  true); }
		if (value.equals("false")) { setvalue = new StyleProperty(BOOLEAN, false); }
		
		if (value.endsWith("px")) {
			String substring = value.substring(0, value.length()-2);
			setvalue = new StyleProperty(PIXELS, Integer.valueOf(substring));
		}
		
		if (value.contains("#")) {				// Hex colors
			if (!cached_string_encoded_colors.containsKey(value)) {
				cached_string_encoded_colors.put(value, Utility.fromHex(value));
			} 
			setvalue = new StyleProperty(COLOR, cached_string_encoded_colors.get(value));
		}
		
		if (value.startsWith("css_rgba")) {		// RGBA colors ( ex: css_rgba(82, 52, 145, 0.34))
			if (!cached_string_encoded_colors.containsKey(value)) {
				cached_string_encoded_colors.put(value, Utility.fromRGBADecimal(value));
			}
			setvalue = new StyleProperty(COLOR, cached_string_encoded_colors.get(value));
		}
		
		for (Color c : Color.values()) { 		// Constant colors
			if (c.name().equals(value.toUpperCase())) setvalue = new StyleProperty(COLOR, c.val());
		}
		
		for (Constant c : Constant.values()) { 	// Constant values
			if (c.name().equals(value.toUpperCase())) setvalue = new StyleProperty(CONSTANT, c);
		}
		
		// Default to integer/pixels
		if (setvalue == null) {
			setvalue = new StyleProperty(PIXELS, Integer.valueOf(value));
		}
		
		HashMap<String, StyleProperty> properties_of_this_type = typeExists(type).properties();
		properties_of_this_type.put(property, setvalue);
	}
	

	private StyleType typeExists(String type) {
		StyleType type_info = style.get(type);
		// Make sure this type actually exists, if it doesn't, make it exist.
		if (type_info == null) {
			ArrayList<String> contains = new ArrayList<String>();
			style.put(type, new StyleType(contains, new HashMap<String, StyleProperty>()));
			type_info = style.get(type);
		}
		return type_info;
	}
	
	public void addContains(String type, String contained_type) {
		StyleType info = typeExists(type);
		info.contains().add(contained_type);
	}

}

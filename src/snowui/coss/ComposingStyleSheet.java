package snowui.coss;

import java.util.HashMap;

import disaethia.io.nbt.NBTCompound;
import disaethia.io.nbt.NBTList;
import frost3d.utility.Log;

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
	
	public static void setDefaults(ComposingStyleSheet sheet) {

		sheet.setProperty("default", "base_color", 				"#686e82");
		sheet.setProperty("default", "outline_color", 			"BLACK");
		sheet.setProperty("default", "preview_color", 			"css_rgba(187, 161, 255, 0.5)");


		sheet.setProperty("default", "left_margin", 			"1");
		sheet.setProperty("default", "right_margin", 			"1");
		sheet.setProperty("default", "top_margin", 				"1");
		sheet.setProperty("default", "bottom_margin", 			"1");
		
		sheet.setProperty("default", "max_width", 				"flex"); // flex, container, or [number]px (EX: 10px)
		sheet.setProperty("default", "min_width", 				"flex"); // flex, container, or [number]px (EX: 10px)
		sheet.setProperty("default", "max_height", 				"flex"); // flex, container, or [number]px (EX: 10px)
		sheet.setProperty("default", "min_height", 				"flex"); // flex, container, or [number]px (EX: 10px)
		
		sheet.setProperty("default", "horizontal_alignment",	"left"); // Left, right, middle
		sheet.setProperty("default", "vertical_alignment", 		"top");	 // Top, bottom, middle

		sheet.setProperty("default", "font_size", 				"8");
		sheet.setProperty("default", "outline_size", 			"0");
		sheet.setProperty("default", "outline_margin", 			"3");
		sheet.setProperty("default", "symbol_size", 			"8");
		
		sheet.setProperty("default", "list_spacing", 			"0");
		sheet.setProperty("default", "list_indent", 			"0");
		
		// Symbol size, scroll bar size, etc
		sheet.setProperty("default", "size", 					"16");


		// -- Predicates -- //
		
		sheet.setPredicate("default", "HOVERED=true", 				"hover");
		sheet.setPredicate("default", "HOVERED=true, DOWN=true", 	"down");
		
		sheet.setProperty("hover", 	  "base_color", 				"BLACK");
		sheet.setProperty("down", 	  "base_color", 				"#3c0ffa");
		
		sheet.setProperty("hover", 	  "outline_size", 				"2");
		sheet.setProperty("hover", 	  "outline_color", 				"BLUE");

		
		// -- Default Elements -- //
		
		sheet.setProperty("text", 	  		"base_color", 			"BLACK");	
		sheet.setProperty("text", 	  		"size", 				"18");	

		sheet.setProperty("scrollbar", 		"base_color", 			"#1b1f24");
		sheet.setProperty("scroll_handle", 	"base_color", 			"#6097f0");
		
		sheet.setProperty("slider_handle", 	"base_color", 			"#6097f0");
		sheet.setProperty("slider_handle", 	"size", 				"18");
		sheet.setProperty("slider", 	"size", 					"8");
		
		
		sheet.setProperty("textbox-style", 	  		"base_color", 			"AQUA");	
		sheet.setProperty("textbox-style", 	  		"min_width", 			"container");	
		sheet.setProperty("textbox-style", 	  		"max_width", 			"container");	
		sheet.setProperty("textbox-style", 	  		"min_height", 			"container");	
		sheet.setProperty("textbox-style", 	  		"max_height", 			"container");	

		sheet.setProperty("textbox-style", 			"left_margin", 			"8");
		sheet.setProperty("textbox-style", 			"right_margin", 		"8");
		sheet.setProperty("textbox-style", 			"top_margin", 			"8");
		sheet.setProperty("textbox-style", 			"bottom_margin", 		"8");
		
		sheet.setPredicate("textbox", 			"SELECTED=true", 		"textbox_selected");
		sheet.addContains("textbox", 	"textbox-style");

		
		sheet.setProperty("textbox_selected", 	"outline_color", 		"RED");	
		sheet.setProperty("textbox_selected", 	"base_color", 			"DESYELLOW");	
		sheet.setProperty("textbox_selected", 	"outline_size", 		"3");	
		sheet.setProperty("textbox_selected", 	"outline_margin", 		"6");	
		sheet.addContains("textbox_selected", 	"textbox-style");


//		sheet.setProperty("scroll_area", "horizontal_alignment",	"right"); // Left, right, middle
//		sheet.setProperty("scroll_area", "vertical_alignment",	"bottom"); // Left, right, middle

		// TODO
//		sheet.setProperty("scrollbar", "left_margin", 			"3");
//		sheet.setProperty("scrollbar", "right_margin", 			"3");
//		sheet.setProperty("scrollbar", "top_margin", 			"3");
//		sheet.setProperty("scrollbar", "bottom_margin", 		"3");
		
		sheet.setProperty("scroll_handle", "left_margin", 			"5");
		sheet.setProperty("scroll_handle", "right_margin", 			"5");
		sheet.setProperty("scroll_handle", "top_margin", 			"5");
		sheet.setProperty("scroll_handle", "bottom_margin", 		"5");
		
		sheet.setProperty("scrollbar", "size", 		"20");
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
		getType(type).properties().put(property, COSSProperty.from(value));
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
		if (target != null) return getPropertyNoDefault(target, property, predicate);
			
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

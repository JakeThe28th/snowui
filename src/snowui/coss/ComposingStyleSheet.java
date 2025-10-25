package snowui.coss;

import java.util.HashMap;

import snowui.coss.enums.*;
import static snowui.coss.enums.Color.*;
import static snowui.coss.enums.Constant.*;
import static snowui.coss.enums.PredicateKey.*;
import static snowui.coss.enums.StylePropertyType.*;

import disaethia.io.nbt.NBTCompound;
import disaethia.io.nbt.NBTList;

public class ComposingStyleSheet {
	
	// -- ++  IO  ++ -- //
	
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

		// sheet.setup default style
		sheet.setProperty("default", "base_color", 			"#292f45");
		sheet.setProperty("default", "outline_color", 		"BLACK");

		sheet.setProperty("default", "left_margin", 		"1");
		sheet.setProperty("default", "right_margin", 		"1");
		sheet.setProperty("default", "top_margin", 			"1");
		sheet.setProperty("default", "bottom_margin", 		"1");
		
		sheet.setProperty("default", "max_width", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		sheet.setProperty("default", "min_width", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		sheet.setProperty("default", "max_height", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		sheet.setProperty("default", "min_height", 			"flex"); 		// flex, container, or [number]px (EX: 10px)
		
		sheet.setProperty("default", "horizontal_alignment",	"left"); 		// Left, right, middle
		sheet.setProperty("default", "vertical_alignment", 		"top");			// Top, bottom, middle

		sheet.setProperty("default", "font_size", 				"8");
		sheet.setProperty("default", "outline_size", 			"1");
		sheet.setProperty("default", "symbol_size", 			"8");
		
		// -- Default Elements -- //
		
		sheet.setProperty("text", "color", 					"#FFFFFF");	
		
		addpredicate hover blah blah
		
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
		return getProperty(type, property) != null;
	}

	public void setProperty(String type, String property, String value) {		
		getType(type).properties().put(property, COSSProperty.from(value));
	}
	
	public void setPredicate(String type, String predicate, String value) {
		setPredicate(type, PredicateKey.valueOf(predicate), value.equals("true"));
	}
	
	public void setPredicate(String type, PredicateKey predicate, boolean value) {
		getType(type).predicates().set(predicate, value);
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
	public COSSProperty getProperty(String type, String property) {
				   COSSProperty result = getPropertyNoDefault(type, property);
		if (result == null) result = getPropertyNoDefault("default", property);
					 return result;
	}
	
	/** Same as getProperty(), but returns 'null' rather than defaulting. 
	 * Useful for searching through contained styles. */
	private COSSProperty getPropertyNoDefault(String type, String property) {

		TODO: check predicates
		
		// Type doesn't exist, so check for fallbacks ('<') and if none are found, fallback to 'default'.
		if (sheet.get(type) == null) {
			int fallback_cutoff = type.lastIndexOf('.');
			if (fallback_cutoff == -1) return null;
			if (fallback_cutoff >= 0) return getPropertyNoDefault(type.substring(0, fallback_cutoff), property);
		}
		
		HashMap<String, COSSProperty> properties_of_this_type = sheet.get(type).properties();

		// If the property is null, check the contained types for the same property,
		if (properties_of_this_type.get(property) == null) {
			COSSProperty result = null;
			COSSType type_info = getType(type);
			for (String contained_type : type_info.contains()) {
				if (contained_type.equals(type)) continue;
				
				result = getPropertyNoDefault(contained_type, property);
				if (result != null) return result;
			}
		}
	
		return properties_of_this_type.get(property);
	}

}

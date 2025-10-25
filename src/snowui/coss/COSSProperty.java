package snowui.coss;

import org.joml.Vector4f;

import frost3d.utility.Utility;
import snowui.coss.enums.*;
import static snowui.coss.enums.Color.*;
import static snowui.coss.enums.Constant.*;
import static snowui.coss.enums.PredicateKey.*;
import static snowui.coss.enums.StylePropertyType.*;

import java.util.HashMap;

public record COSSProperty (
	  StylePropertyType type,
	  Object value
	  ) {
	
		public int 		integer()  { return pixels(); }
		public int  	pixels()   { if (type == PIXELS) 	return (int) 	  value; else throw new PropertyException(type); }
		public Vector4f color()    { if (type == COLOR) 	return (Vector4f) value; else throw new PropertyException(type); }
		public Constant constant() { if (type == CONSTANT)	return (Constant) value; else throw new PropertyException(type); }
		public boolean  bool() 	   { if (type == BOOLEAN) 	return (Boolean)  value; else throw new PropertyException(type); }
		
		public static class PropertyException extends IllegalArgumentException {
			private static final long serialVersionUID = 7895350813338876870L;
			public PropertyException(StylePropertyType t) { super("Property is of type '" + t.name() + "', yet accessed with an other."); }
		}
		
		/*== Deserialization ==*/
		
		static HashMap<String, Vector4f> cached_string_encoded_colors = new HashMap<String, Vector4f>();
		public static COSSProperty from(String value) {
			if (value.equals("true"))  { return new COSSProperty(BOOLEAN,  true); }
			if (value.equals("false")) { return new COSSProperty(BOOLEAN, false); }
			
			if (value.endsWith("px")) {
				String substring = value.substring(0, value.length()-2);
				return new COSSProperty(PIXELS, Integer.valueOf(substring));
			}
			
			if (value.contains("#")) {				// Hex colors
				if (!cached_string_encoded_colors.containsKey(value)) {
					cached_string_encoded_colors.put(value, Utility.fromHex(value));
				} 
				return new COSSProperty(COLOR, cached_string_encoded_colors.get(value));
			}
			
			if (value.startsWith("css_rgba")) {		// RGBA colors ( ex: css_rgba(82, 52, 145, 0.34))
				if (!cached_string_encoded_colors.containsKey(value)) {
					cached_string_encoded_colors.put(value, Utility.fromRGBADecimal(value));
				}
				return new COSSProperty(COLOR, cached_string_encoded_colors.get(value));
			}
			
			for (Color c : Color.values()) { 		// Constant colors
				if (c.name().equals(value.toUpperCase())) return new COSSProperty(COLOR, c.val());
			}
			
			for (Constant c : Constant.values()) { 	// Constant values
				if (c.name().equals(value.toUpperCase())) return new COSSProperty(CONSTANT, c);
			}
			
			// Default to integer/pixels
			return new COSSProperty(PIXELS, Integer.valueOf(value));
	}
		
}	
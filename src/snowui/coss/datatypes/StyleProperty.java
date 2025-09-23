package snowui.coss.datatypes;

import org.joml.Vector4f;

import snowui.coss.StyleEnums.*;

import static snowui.coss.StyleEnums.StylePropertyType.*;

public record StyleProperty (
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
		}
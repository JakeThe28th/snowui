package snowui.coss;

import org.joml.Vector4f;

public class StyleEnums {
	
	public static enum StylePropertyType { COLOR, PIXELS, CONSTANT, BOOLEAN, }
	public static enum Constant   		 { TOP, BOTTOM, LEFT, RIGHT, MIDDLE, FLEX, CONTAINER }

	public static enum Color {
		WHITE(new Vector4f(1,1,1,1)),
		BLACK(new Vector4f(0,0,0,1)),
		
		RED(new Vector4f(1,0,0,1)),
		GREEN(new Vector4f(0,1,0,1)),
		BLUE(new Vector4f(0,0,1,1)),
		
		DESYELLOW(new Vector4f(1f,1f,.5f,1)),
		
		TRANSPARENT_WHITE(new Vector4f(1,1,1,.5f)),
		TRANSPARENT_BLACK(new Vector4f(0,0,0,.75f));

		
		Vector4f color = null;
		Color(Vector4f color) { this.color = color; }
		public Vector4f val() { return this.color; }
	}
	

}

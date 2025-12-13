package snowui.coss.enums;

import org.joml.Vector4f;

import frost3d.utility.Utility;

public enum Color {
	WHITE(new Vector4f(1,1,1,1)),
	BLACK(new Vector4f(0,0,0,1)),
	
	RED(new Vector4f(1,0,0,1)),
	GREEN(new Vector4f(0,1,0,1)),
	BLUE(new Vector4f(0,0,1,1)),
	
	DESBLUE(new Vector4f(0.5f,0.5f,1,1)),
	DESYELLOW(new Vector4f(1f,1f,.5f,1)),
	
	TRANSPARENT_WHITE(new Vector4f(1,1,1,.5f)),
	WHITE25(new Vector4f(1,1,1,.25f)),
	WHITE75(new Vector4f(1,1,1,.75f)),
	TRANSPARENT_BLACK(new Vector4f(0,0,0,.5f)),
	BLACK25(new Vector4f(0,0,0,.25f)),
	BLACK75(new Vector4f(0,0,0,.75f)),
	
	YELLOW(new Vector4f(1, 1, 0, 1)),
	GOLD(new Vector4f(1, 0.66f, 0, 1)),
	AQUA(new Vector4f(0, 1, 0.75f, 1)),
	TRANSPARENT_AQUA(new Vector4f(0, 1, 0.75f, 0.25f)),
	TRANSPARENT_RED(new Vector4f(1,0,0,0.25f)),
	
	BASIC_PANEL			(Utility.fromHex("#414141")),
	BASIC_PANEL_HOVER	(Utility.fromHex("#4A4A4A")),
	BASIC_PANEL_DOWN	(Utility.fromHex("#959595")),
	BASIC_PANEL_DARK	(Utility.fromHex("#2C2C2C")),
	BASIC_CLEAR			(Utility.fromHex("#161616"))

	;
	
	Vector4f color = null;
	Color(Vector4f color) { this.color = color; }
	public Vector4f val() { return this.color; }
}

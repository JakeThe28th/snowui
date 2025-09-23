package snowui.coss.datatypes;

import snowui.coss.Stylesheet;

/** Like StyleType but it's used for elements TODO: reorganize */
public class StylePropertyCollection {

	/** Name of the Type to use for style queries in this class. */
	String 		  	identifier;
	public void   	identifier(String id)	{   this.identifier = id; selector_and_id = identifier(); }
	public String 	identifier()			{ return identifier; }
	
	public String selector_and_id = "";
	public void   	clear_selector()		{ 		 selector_and_id = identifier();     }
	public void   	selector(String sel)	{ 		 selector_and_id = identifier()+sel; }
	
	// Target size //
	int target_width;
	public void target_width(int val)		{ 	     target_width = val;  }
	public int 	target_width()				{ return target_width; 		  }	
	
	int target_height;
	public void target_height(int val)		{ 		 target_height = val; }
	public int 	target_height()				{ return target_height; 	  }
	
	public boolean 	cached = false;
	// Cached properties //
	public StyleProperty left_margin; 	
	public StyleProperty top_margin; 	
	public StyleProperty right_margin; 	
	public StyleProperty bottom_margin;
	public StyleProperty min_height; 	
	public StyleProperty max_height; 	
	public StyleProperty min_width; 	
	public StyleProperty max_width;
	public StyleProperty halign; 	
	public StyleProperty valign;
	public StyleProperty outline_color;
	public StyleProperty base_color; 	
	public StyleProperty text_color;
	public StyleProperty outline_size;
	public StyleProperty symbol_size;
	public StyleProperty symbol_x_margin;
	public StyleProperty list_spacing;
	public StyleProperty horizontal_indent;
	public StyleProperty preview_color;
	public void updateProperties(Stylesheet s) {
		left_margin 			= s.getProperty(selector_and_id, "left_margin");
		top_margin 				= s.getProperty(selector_and_id, "top_margin");
		right_margin 			= s.getProperty(selector_and_id, "right_margin");
		bottom_margin 			= s.getProperty(selector_and_id, "bottom_margin");
		min_height 				= s.getProperty(selector_and_id, "min_height");
		max_height 				= s.getProperty(selector_and_id, "max_height");
		min_width 				= s.getProperty(selector_and_id, "min_width");
		max_width 				= s.getProperty(selector_and_id, "max_width");
		halign 					= s.getProperty(selector_and_id, "horizontal_alignment");
		valign 					= s.getProperty(selector_and_id, "vertical_alignment");
		outline_color 			= s.getProperty(selector_and_id, "outline_color");
		base_color 				= s.getProperty(selector_and_id, "base_color");
		text_color 				= s.getProperty(selector_and_id, "text_color");
		outline_size 			= s.getProperty(selector_and_id, "outline_size");
		symbol_size 			= s.getProperty(selector_and_id, "symbol_size");
		symbol_x_margin			= s.getProperty(selector_and_id, "symbol_x_margin");
		list_spacing 			= s.getProperty(selector_and_id, "list_spacing");
		horizontal_indent 		= s.getProperty(selector_and_id, "horizontal_indent");
		preview_color 			= s.getProperty(selector_and_id, "preview_color");
		cached = true;
	}

	
	/** The true width of the element, including all margins. */
	public int width(GUIInstance i) { 
		int min_intrinsic_width = min_width.type() == PIXELS ? min_width.pixels() : target_width;
		int max_intrinsic_width = max_width.type() == PIXELS ? max_width.pixels() : target_width;
		return padw(clamp(target_width, min_intrinsic_width, max_intrinsic_width)); 
		}
	
	/** The true height of the element, including all margins. */
	public int height(GUIInstance i) { 
		int min_intrinsic_height = min_height.type() == PIXELS ? min_height.pixels() : target_height;
		int max_intrinsic_height = max_height.type() == PIXELS ? max_height.pixels() : target_height;
		return padh(clamp(target_height, min_intrinsic_height, max_intrinsic_height)); 
	}
	
	public int padw(int w)   { return left_margin.integer() + w + right_margin.integer(); }
	public int padh(int h)   { return top_margin. integer() + h + bottom_margin.integer(); }
	public int unpadw(int w) { return w - (left_margin.integer() + right_margin.integer()); }
	public int unpadh(int h) { return h - (top_margin. integer() + bottom_margin.integer()); }
	
	public int clamp(int val, int min, int max) {
		if (val < min) val = min;
		if (val > max) val = max;
		return val;
	}
}

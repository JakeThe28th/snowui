package snowui.coss;

public class CachedProperties {
	
	public int padw(int w)   { return left_margin.integer() + w + right_margin.integer(); }
	public int padh(int h)   { return top_margin. integer() + h + bottom_margin.integer(); }
	public int unpadw(int w) { return w - (left_margin.integer() + right_margin.integer()); }
	public int unpadh(int h) { return h - (top_margin. integer() + bottom_margin.integer()); }
	
	public COSSProperty left_margin; 	
	public COSSProperty top_margin; 	
	public COSSProperty right_margin; 	
	public COSSProperty bottom_margin;
	public COSSProperty min_height; 	
	public COSSProperty max_height; 	
	public COSSProperty min_width; 	
	public COSSProperty max_width;
	public COSSProperty halign; 	
	public COSSProperty valign;
	public COSSProperty outline_color;
	public COSSProperty base_color; 	
	public COSSProperty text_color;
	public COSSProperty outline_size;
	public COSSProperty symbol_size;
	public COSSProperty symbol_x_margin;
	public COSSProperty list_spacing;
	public COSSProperty horizontal_indent;
	public COSSProperty preview_color;
	public CachedProperties(ComposingStyleSheet s, String selector_and_id) {
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
		outline_size 			= s.getProperty(selector_and_id, "outline_size");
		symbol_size 			= s.getProperty(selector_and_id, "symbol_size");
		symbol_x_margin			= s.getProperty(selector_and_id, "symbol_x_margin");
		list_spacing 			= s.getProperty(selector_and_id, "list_spacing");
		horizontal_indent 		= s.getProperty(selector_and_id, "horizontal_indent");
		preview_color 			= s.getProperty(selector_and_id, "preview_color");
	}

}

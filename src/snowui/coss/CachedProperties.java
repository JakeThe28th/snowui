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
	public COSSProperty list_indent;
	public COSSProperty preview_color;
	public CachedProperties(ComposingStyleSheet s, String type, COSSPredicate predicate) {
		left_margin 			= s.getProperty(type, "left_margin", predicate);
		top_margin 				= s.getProperty(type, "top_margin", predicate);
		right_margin 			= s.getProperty(type, "right_margin", predicate);
		bottom_margin 			= s.getProperty(type, "bottom_margin", predicate);
		min_height 				= s.getProperty(type, "min_height", predicate);
		max_height 				= s.getProperty(type, "max_height", predicate);
		min_width 				= s.getProperty(type, "min_width", predicate);
		max_width 				= s.getProperty(type, "max_width", predicate);
		halign 					= s.getProperty(type, "horizontal_alignment", predicate);
		valign 					= s.getProperty(type, "vertical_alignment", predicate);
		outline_color 			= s.getProperty(type, "outline_color", predicate);
		base_color 				= s.getProperty(type, "base_color", predicate);
		outline_size 			= s.getProperty(type, "outline_size", predicate);
		list_spacing 			= s.getProperty(type, "list_spacing", predicate);
		list_indent 			= s.getProperty(type, "list_indent", predicate);

	}

}

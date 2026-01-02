package snowui.coss;

public record CachedProperties(
		COSSProperty left_margin, 	
		COSSProperty top_margin, 	
		COSSProperty right_margin, 	
		COSSProperty bottom_margin,
		COSSProperty min_height, 	
		COSSProperty max_height, 	
		COSSProperty min_width, 	
		COSSProperty max_width,
		COSSProperty halign, 	
		COSSProperty valign,
		COSSProperty outline_color,
		COSSProperty base_color, 
		COSSProperty background_color, 	
		COSSProperty preview_color, 	
		COSSProperty outline_size,
		COSSProperty outline_margin,
		COSSProperty background_margin,
		COSSProperty list_spacing,
		COSSProperty list_indent,
		COSSProperty size
		) {
	
	public int padw(int w)   { return left_margin.integer() + w + right_margin.integer(); }
	public int padh(int h)   { return top_margin. integer() + h + bottom_margin.integer(); }
	public int unpadw(int w) { return w - (left_margin.integer() + right_margin.integer()); }
	public int unpadh(int h) { return h - (top_margin. integer() + bottom_margin.integer()); }

	public CachedProperties(ComposingStyleSheet s, String type, COSSPredicate predicate) {
		this(
		s.getProperty(type, "left_margin", predicate),
		s.getProperty(type, "top_margin", predicate),
		s.getProperty(type, "right_margin", predicate),
		s.getProperty(type, "bottom_margin", predicate),
		s.getProperty(type, "min_height", predicate),
		s.getProperty(type, "max_height", predicate),
		s.getProperty(type, "min_width", predicate),
		s.getProperty(type, "max_width", predicate),
		s.getProperty(type, "horizontal_alignment", predicate),
		s.getProperty(type, "vertical_alignment", predicate),
		s.getProperty(type, "outline_color", predicate),
		s.getProperty(type, "base_color", predicate),
		s.getProperty(type, "background_color", predicate),
		s.getProperty(type, "preview_color", predicate),
		s.getProperty(type, "outline_size", predicate),
		s.getProperty(type, "outline_margin", predicate),
		s.getProperty(type, "background_margin", predicate),
		s.getProperty(type, "list_spacing", predicate),
		s.getProperty(type, "list_indent", predicate),
		s.getProperty(type, "size", predicate)
		);

	}

}

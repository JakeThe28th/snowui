package snowui.frost3d;

import frost3d.interfaces.F3DCanvas;
import frost3d.utility.Rectangle;

public class SM_RoundCorners implements ShaderModule {
	
	protected SM_RoundCorners() { }
	
	String fragment_code = 	
		"""
		// scale the texture coordinates to match the rectangle size
		vec2 scaled_texc = f_texcoord * vec2(rect_width, rect_height);

		float space_height = rect_height;
		float space_width  = rect_width;

		if (scaled_texc.x < corner_size_pixels && scaled_texc.y < corner_size_pixels) {
			if (distance(vec2(corner_size_pixels,corner_size_pixels), scaled_texc) > corner_size_pixels) FragColor.a = 0;
		}
		if (scaled_texc.x < corner_size_pixels && scaled_texc.y > space_height-corner_size_pixels) {
			if (distance(vec2(corner_size_pixels,space_height-corner_size_pixels), scaled_texc) > corner_size_pixels) FragColor.a = 0;
		}
		if (scaled_texc.x > space_width-corner_size_pixels && scaled_texc.y < corner_size_pixels) {
			if (distance(vec2(space_width-corner_size_pixels,corner_size_pixels), scaled_texc) > corner_size_pixels) FragColor.a = 0;
		}
		if (scaled_texc.x > space_width-corner_size_pixels && space_height-scaled_texc.y < corner_size_pixels) {
			if (distance(vec2(space_width-corner_size_pixels,space_height-corner_size_pixels), scaled_texc) > corner_size_pixels) FragColor.a = 0;
		}
		""";
	
	String[] fragment_uniforms = 
		{
			"uniform int corner_size_pixels = 100;",
			"uniform int rect_width 		= 100;",
			"uniform int rect_height 		= 100;"
		};

	@Override public String[] fragment_uniforms	() { return fragment_uniforms	; }
	@Override public String   fragment_code		() { return fragment_code		; }
	
	@Override public String[] vertex_uniforms	() { return new String[0]		; }
	@Override public String   vertex_code		() { return null				; }
	
	public void corner_size_pixels(F3DCanvas canvas, int size) {
		canvas.uniform("corner_size_pixels", size);
	}
	
	public void current_rectangle(F3DCanvas canvas, Rectangle rectangle) {
		canvas.uniform("rect_width", rectangle.width());
		canvas.uniform("rect_height", rectangle.height());
	}
	
}

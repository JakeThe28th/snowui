package snowui.frost3d;

import frost3d.interfaces.F3DCanvas;

public class SM_FadeEdges implements ShaderModule {
	
	protected SM_FadeEdges() { }
	
	String fragment_code = 	
		"""
		float t = 0;
		float blend_alpha = 1;
		if (gl_FragCoord.x < right_opaque) {
			// Fading (for the left of scrolling song titles)
			t = (gl_FragCoord.x - left_transparent) / (left_opaque - left_transparent);
			blend_alpha = clamp(t, 0, 1);
			if (blend_alpha < 0.001) discard;
		} else {
			// Fading (for the right of scrolling song titles)
			t = (gl_FragCoord.x - right_transparent) / (right_opaque - right_transparent);
			blend_alpha = clamp(t, 0, 1);
			if (blend_alpha < 0.001) discard;
		}

		FragColor.a = FragColor.a * blend_alpha;
		""";
	
	String[] fragment_uniforms = 
		{
			"uniform int left_transparent 	= 10;",
			"uniform int left_opaque 		= 10;",
			"uniform int right_opaque 		= 90;",
			"uniform int right_transparent 	= 90;"
		};

	@Override public String[] fragment_uniforms	() { return fragment_uniforms	; }
	@Override public String   fragment_code		() { return fragment_code		; }
	
	@Override public String[] vertex_uniforms	() { return new String[0]		; }
	@Override public String   vertex_code		() { return null				; }
	
	public void left_edge(F3DCanvas canvas, int left_transparent, int left_opaque) {
		canvas.uniform("left_transparent", 	left_transparent);
		canvas.uniform("left_opaque",	 	left_opaque);
	}
	
	public void right_edge(F3DCanvas canvas, int right_opaque, int right_transparent) {
		canvas.uniform("right_opaque", 		right_opaque);
		canvas.uniform("right_transparent", right_transparent);
	}
	
}

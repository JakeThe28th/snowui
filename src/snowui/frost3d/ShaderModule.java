package snowui.frost3d;

interface ShaderModule {
	
	String[] fragment_uniforms	();
	String   fragment_code		();
	
	default boolean has_fragment_code() {
		return fragment_code() != null;
	}
	
	String[] vertex_uniforms	();
	String   vertex_code		();
	
	default boolean has_vertex_code() {
		return vertex_code() != null;
	}
	
}

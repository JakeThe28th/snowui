package snowui.frost3d;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import frost3d.GLShaderProgram;

public class ModularGUIShader {
	
	String base_vertex = 
		"""
			#version 330 core
			layout (location = 0) in vec3 v_position;
			layout (location = 1) in vec2 v_texcoord;

			uniform mat4 world_transform;	
			uniform mat4 transform;											
			out vec2 f_texcoord;

			void main() {
				f_texcoord = v_texcoord;
			    gl_Position = world_transform * transform * vec4(v_position, 1.0);
			}
		""";
			
	String base_fragment = """
			#version 330 core
			out vec4 FragColor;

			uniform sampler2D texture_image;
			in vec2 f_texcoord;
			uniform vec4 mix_color;

			[[INJECT_UNIFORMS]]

			void main() {
				FragColor = texture(texture_image, f_texcoord) * mix_color;
				[[INJECT_CODE]]
				if (FragColor.a < 0.001) discard;
			} 
		""";
	
	GLShaderProgram program = null;
	
	static record ShaderInject(String content, String... uniform_declarations) {}	
	
	HashMap<String, ShaderInject> injects = new HashMap<>();
	HashMap<String, Boolean> injects_on = new HashMap<>();

	public void register(String name, ShaderInject inject) { injects.put(name, inject); }
	//TODO: modular shader
	{
		register("rounded_corners", 
			new ShaderInject(
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
				""",
				"uniform int corner_size_pixels = 100;",
				"uniform int rect_width 		= 100;",
				"uniform int rect_height 		= 100;"
			));
	}

	public GLShaderProgram program() {
		if (program == null) {

			String fragment_inject_uniforms = "";
			String fragment_inject_code 	= "";
			
			for (String inject : injects.keySet()) {

				for (String uniform : injects.get(inject).uniform_declarations) {
					fragment_inject_uniforms += uniform + "\n";
				}
				
				fragment_inject_code += injects.get(inject).content + "\n";
			}
			
			String vertex_inject_uniforms 	= "";
			String vertex_inject_code 		= "";

			String fragment = base_fragment;
			fragment = fragment.replaceAll( Pattern.quote("[[INJECT_UNIFORMS]]") , Matcher.quoteReplacement(fragment_inject_uniforms	));
			fragment = fragment.replaceAll( Pattern.quote("[[INJECT_CODE]]")	 , Matcher.quoteReplacement(fragment_inject_code		));
			
			String vertex = base_vertex;
			vertex   = vertex  .replaceAll( Pattern.quote("[[INJECT_UNIFORMS]]") , Matcher.quoteReplacement(vertex_inject_uniforms	));
			vertex   = vertex  .replaceAll( Pattern.quote("[[INJECT_CODE]]")	 , Matcher.quoteReplacement(vertex_inject_code		));
			
			program = new GLShaderProgram(vertex, fragment); 
		}
		return program;
	}

}

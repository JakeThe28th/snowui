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
				float corner_size_float = float(corner_size_pixels) / 100;

				float ratio = rect_width / float(rect_height);
				vec2 space = vec2(f_texcoord.x * ratio, f_texcoord.y);

				if (space.x < corner_size_float && space.y < corner_size_float) {
					if (distance(vec2(corner_size_float,corner_size_float), space) > corner_size_float) FragColor.a = 0;
				}
				if (space.x < corner_size_float && space.y > 1-corner_size_float) {
					if (distance(vec2(corner_size_float,1-corner_size_float), space) > corner_size_float) FragColor.a = 0;
				}
				if (space.x > 1-corner_size_float && space.y < corner_size_float) {
					if (distance(vec2(1-corner_size_float,corner_size_float), space) > corner_size_float) FragColor.a = 0;
				}
				if (space.x > 1-corner_size_float && 1-space.y < corner_size_float) {
					if (distance(vec2(1-corner_size_float,1-corner_size_float), space) > corner_size_float) FragColor.a = 0;
				}
				""",
				"uniform int corner_size_pixels = 10;",
				"uniform int rect_width = 100;",
				"uniform int rect_height = 100;"
			));
	}
//	
//	unrelated but hmm
//		ok so, level editor
//			layer -- unique objects with their own serialization and editors
//		NPC / Object / Entity / Mob...
//			Defined separately, tand hthen referenced in level, not defined in level?
//			so like, each NPC is like a '.npc' file..?
//					
//		special layer (like WorldDefinition or something) defines how input affects movement 
//		and how that's projected to the screen?
//		
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

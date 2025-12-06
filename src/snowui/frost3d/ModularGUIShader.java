package snowui.frost3d;

import java.util.ArrayList;
import java.util.HashMap;

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
	TODO: modular shader
	{
		register("rounded_corners", 
			new ShaderInject(
				"""
				if (f_texcoord.x < corner_size && f_texcoord.y < corner_size) {
					if (distance(vec2(corner_size,corner_size), f_texcoord) > corner_size) FragColor.a = 0;
				}
				if (f_texcoord.x < corner_size && f_texcoord.y > 1-corner_size) {
					if (distance(vec2(corner_size,1-corner_size), f_texcoord) > corner_size) FragColor.a = 0;
				}
				if (f_texcoord.x > 1-corner_size && f_texcoord.y < corner_size) {
					if (distance(vec2(1-corner_size,corner_size), f_texcoord) > corner_size) FragColor.a = 0;
				}
				if (f_texcoord.x > 1-corner_size && 1-f_texcoord.y < corner_size) {
					if (distance(vec2(1-corner_size,1-corner_size), f_texcoord) > corner_size) FragColor.a = 0;
				}
				""",
				"uniform float corner_size = .25;"
			));
	}
	
	unrelated but hmm
		ok so, level editor
			layer -- unique objects with their own serialization and editors
		NPC / Object / Entity / Mob...
			Defined separately, tand hthen referenced in level, not defined in level?
			so like, each NPC is like a '.npc' file..?
					
		special layer (like WorldDefinition or something) defines how input affects movement 
		and how that's projected to the screen?
		
	public GLShaderProgram program() {
		if (program == null) {
			String uniform_injects = "";
			String code_injects = "";
			for (String inject : injects.keySet()) {
				uniform_injects += "//" + inject;
			}
		}
		return program;
	}

}

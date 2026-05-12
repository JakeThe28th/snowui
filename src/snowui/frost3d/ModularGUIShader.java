package snowui.frost3d;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import frost3d.GLShaderProgram;
import frost3d.interfaces.F3DCanvas;
import frost3d.utility.Log;

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

			[[UNIFORMS]]

			void main() {
				FragColor = texture(texture_image, f_texcoord) * mix_color;
				[[CODE]]
				if (FragColor.a < 0.001) discard;
			} 
		""";
	
	GLShaderProgram program = null;
		
	HashMap<Class<? extends ShaderModule>, ShaderModule> modules = new HashMap<>();
	HashMap<Class<? extends ShaderModule>, Boolean	   > enabled = new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T module(Class<? extends T> clazz) {
		return (T) modules.get(clazz);
	}
	
	/** Whether or not to use the given module for the next-queued item.<br>
	 *  NOTE: The module must be 'enable'd for this to do anything. */
	public void use(F3DCanvas canvas, Class<?> key, boolean val) {
		String condition = "use_" + key.getSimpleName();
		canvas.uniform(condition, val ? 1 : 0);
	}
	
	/** Enables the module, and recompiles the shader. */
	public void enable(Class<? extends ShaderModule> key) {
		enabled.put(key, true);
		program = null;
	}
	
	/** Disables the module, and recompiles the shader. */
	public void disable(Class<? extends ShaderModule> key) {
		enabled.put(key, false);
		program = null;
	}

	public void register(ShaderModule module) {
		modules.put(module.getClass(), module); 
		enable(module.getClass());
	}
	
	{
		register(new SM_RoundCorners());
		register(new SM_FadeEdges());
	}

	public GLShaderProgram program() {
		if (program == null) {

			String fragment_uniforms = "";
			String fragment_code 	 = "";
			
			for (Class<?> key : modules.keySet()) {
				if (enabled.get(key) != true) continue;
				ShaderModule m = modules.get(key);
				String condition = "use_" + key.getSimpleName();
				for (String uniform : m.fragment_uniforms()) fragment_uniforms += uniform 		    + "\n";				
				if  (				  m.has_fragment_code()) fragment_code     += "if ("+condition+" == 1) {\n" + m.fragment_code() + "\n}\n";
															 fragment_uniforms += "uniform int " + condition + " = 0;";
			}
			
			String vertex_uniforms 	= "";
			String vertex_code 		= "";

			for (Class<?> key : modules.keySet()) {
				if (enabled.get(key) != true) continue;
				ShaderModule m = modules.get(key);
				String condition = "use_" + key.getSimpleName();
				for (String uniform : m.vertex_uniforms()) vertex_uniforms += uniform 	      + "\n";				
				if  (				  m.has_vertex_code()) vertex_code     += "if ("+condition+" == 1) {\n" + m.vertex_code() + "\n}\n";
														   vertex_uniforms += "uniform int " + condition + " = 0;";
			}
			
			String fragment = base_fragment;
			fragment = fragment.replaceAll( Pattern.quote("[[UNIFORMS]]") , Matcher.quoteReplacement(fragment_uniforms	));
			fragment = fragment.replaceAll( Pattern.quote("[[CODE]]")	 , Matcher.quoteReplacement(fragment_code		));
			
			String vertex = base_vertex;
			vertex   = vertex  .replaceAll( Pattern.quote("[[UNIFORMS]]") , Matcher.quoteReplacement(vertex_uniforms	));
			vertex   = vertex  .replaceAll( Pattern.quote("[[CODE]]")	 , Matcher.quoteReplacement(vertex_code		));
			
//			try {
//				Files.writeString(Paths.get("vert.txt"), vertex);
//				Files.writeString(Paths.get("fragment.txt"), fragment);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			program = new GLShaderProgram(vertex, fragment); 
		}
		return program;
	}

}

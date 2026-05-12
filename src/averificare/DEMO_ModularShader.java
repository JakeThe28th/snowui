package averificare;

import java.io.FileNotFoundException;
import java.io.IOException;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleTexture;
import frost3d.implementations.SimpleWindow;
import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUISlider;
import snowui.frost3d.ModularGUIShader;
import snowui.frost3d.SM_RoundCorners;
import frost3d.GLShaderProgram;

public class DEMO_ModularShader {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(500, 400, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
				ModularGUIShader shader = new ModularGUIShader();

		GUISlider slider = new GUISlider() {
			@Override
			public void onChange(float newv) {
				shader.module(SM_RoundCorners.class).corner_size_pixels(gui.canvas(), (int) (newv * 100));
			}
		};
		
		
		SimpleTexture texture = new SimpleTexture("khronos.png");
		
		GUIElement root_element = new GUIElement() {
			{ registerSubElement(slider); }
			Rectangle demo_rect;
			@Override public void recalculateSize(GUIInstance gui) { }
			@Override public void updateDrawInfo(GUIInstance gui) {
				demo_rect = limit_rectangle().internal(0.3f, 0.3f, 0.7f, 0.7f);
				slider.limit_rectangle(limit_rectangle().internal(0.1f, 0.8f, 0.9f, 1f));
			}
			@Override
			public void draw(GUIInstance gui, int depth) {
				gui.canvas().color(Color.WHITE.val());
				shader.use(gui.canvas(), SM_RoundCorners.class, true);
				shader.module(SM_RoundCorners.class).current_rectangle(gui.canvas(), demo_rect);
				gui.canvas().rect(demo_rect, depth, texture);
				shader.use(gui.canvas(), SM_RoundCorners.class, false);
			}
		};

		gui.root(root_element);
		gui.clear_color(Color.WHITE.val());		
		
		GLShaderProgram last_program = shader.program();
		gui.shader(shader.program());
		
		shader.enable(SM_RoundCorners.class);

		
		while (!window.should_close()) {
			if (last_program != shader.program()) {
				gui.shader(shader.program());
				last_program = shader.program();
			}
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();
		GLState.endGLFW();
	}
	
}

package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUISlider;
import snowui.frost3d.ModularGUIShader;

public class DEMO_ModularShader {

	public static void main(String[] args) {
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(500, 400, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUISlider slider = new GUISlider() {
			@Override
			public void onChange(float newv) {
				gui.canvas().uniform("corner_size_pixels", (int) (newv*100));
			}
		};
		
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
				gui.canvas().color(Color.BLACK.val());
				gui.canvas().uniform("rect_width", demo_rect.width());
				gui.canvas().uniform("rect_height", demo_rect.height());
				gui.canvas().rect(demo_rect, depth);
			}
		};

		gui.root(root_element);
		
		ModularGUIShader shader = new ModularGUIShader();
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.canvas().push_shader(shader.program());
			gui.render();
			window.tick();
			gui.canvas().pop_shader();
		}
		
		window.end();
		GLState.endGLFW();
	}
	
}

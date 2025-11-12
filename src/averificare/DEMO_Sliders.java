package averificare;

import org.lwjgl.glfw.GLFW;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUISlider;

public class DEMO_Sliders {

	public static void main(String[] args) {
		
		GLState.initializeGLFW();
		
		// Create window ...
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		//window.setVsync(false);
		boolean vsync = false;
		
		// Create core shaders (needs to be done after window cuz context)
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUISlider slider		 	= new GUISlider();
		
		gui.root(slider);
		
		while (!window.should_close()) {
			if (window.input().keyPressed(GLFW.GLFW_KEY_LEFT_CONTROL) ) { 
				vsync = !vsync; window.setVsync(vsync);
			}
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}
	
}

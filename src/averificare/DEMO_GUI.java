package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUIText;

public class DEMO_GUI {

	public static void main(String[] args) {
		
		GLState.initializeGLFW();
		
		// Create window ...
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		//window.setVsync(false);
		
		// Create core shaders (needs to be done after window cuz context)
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		gui.root(new GUIText("Hello, "));
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}
	
}

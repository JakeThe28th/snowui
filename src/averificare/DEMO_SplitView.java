package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.extended.GUIDockable;
import snowui.elements.extended.GUISplit;

public class DEMO_SplitView {

	public static void main(String[] args) {
		
		// tabs, dockers, text editors, right click menu

		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(2*300, 2*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUISplit split = new GUISplit();
		
		split.first(new GUIDockable("A"));
		split.second(new GUIDockable("b"));
		
		gui.style().setProperty("dockable", "min_width", "container");
		gui.style().setProperty("dockable", "max_width", "container");
		gui.style().setProperty("dockable", "min_height", "container");
		gui.style().setProperty("dockable", "max_height", "container");

		gui.root(split);
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		window.end();
		GLState.endGLFW();	
	}
}

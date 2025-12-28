package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.extended.GUIDockable;

public class DEMO_DockWindow2 {

	public static void main(String[] args) {
		
		// tabs, dockers, text editors, right click menu

		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(2*300, 2*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUIDockable docker = new GUIDockable();
		
		docker.titlebar().title("Hello! I'm, Lie!!!");
		docker.draggable(true);
		
		gui.root(docker);
		
		gui.style().setProperty("default", "horizontal_alignment", "middle");
		gui.style().setProperty("default", "vertical_alignment", "middle");
		
		
		docker.root().identifier("big");
		
		gui.style().setProperty("big", "base_color", "#0000FF");

		gui.style().setProperty("big", "left_margin", "40");
		gui.style().setProperty("big", "right_margin", "40");
		gui.style().setProperty("big", "top_margin", "40");
		gui.style().setProperty("big", "bottom_margin", "40");
		
		gui.style().setProperty("dockable", "base_color", "#AAAAAA");

		gui.style().setProperty("title_bar", "outline_size", "2");
		gui.style().setProperty("title_bar", "outline_margin", "1");

		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		window.end();
		GLState.endGLFW();	
	}
}

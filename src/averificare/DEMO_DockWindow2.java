package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import frost3d.utility.Log;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.docking.GUIDockable;
import snowui.elements.docking.GUISplit;

public class DEMO_DockWindow2 {

	public static void main(String[] args) {
		
		// tabs, dockers, text editors, right click menu

		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(2*300, 2*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUISplit main_split = new GUISplit();
				
		GUIDockable docker_a = new GUIDockable();
		docker_a.titlebar().title("Hello! I'm, Lie!!! 1");
		docker_a.draggable(true);
		setStyle(docker_a, gui);
		
		GUIDockable docker_b = new GUIDockable();
		docker_b.titlebar().title("Hello! I'm, Lie!!! 2");
		docker_b.draggable(true);
		setStyle(docker_b, gui);
		
		GUIDockable docker_c = new GUIDockable();
		docker_c.titlebar().title("Hello! I'm, Lie!!! 3");
		docker_c.draggable(true);
		setStyle(docker_c, gui);
		
		GUISplit split_b = new GUISplit(docker_b, docker_c);
		split_b.verticalify();

		main_split.first(docker_a);
		main_split.second(split_b);

		gui.root(main_split);

		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		window.end();
		GLState.endGLFW();	
	}

	private static void setStyle(GUIDockable docker, GUIInstance gui) {
		docker.root().identifier("big");
		
		gui.style().setProperty("big", "horizontal_alignment", "middle");
		gui.style().setProperty("big", "vertical_alignment", "middle");
		
		gui.style().setProperty("big", "base_color", "#0000FF");

		gui.style().setProperty("big", "left_margin", "40");
		gui.style().setProperty("big", "right_margin", "40");
		gui.style().setProperty("big", "top_margin", "40");
		gui.style().setProperty("big", "bottom_margin", "40");
		
		gui.style().setProperty("dockable", "base_color", "#AAAAAA");

		gui.style().setProperty("title_bar", "outline_size", "2");
		gui.style().setProperty("title_bar", "outline_margin", "1");
		gui.style().addContains("title_bar", "contained");

		docker.identifier("contained");
		gui.style().setProperty("contained", "min_width", "container");
		gui.style().setProperty("contained", "max_width", "container");
		gui.style().setProperty("contained", "min_height", "container");
		gui.style().setProperty("contained", "max_height", "container");
		
	}
}

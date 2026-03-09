package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import frost3d.utility.Log;
import snowui.GUIInstance;
import snowui.elements.docking.GUIDockable;
import snowui.elements.docking.GUIDockableTabList;
import snowui.elements.docking.GUISplit;

public class DEMO_DockWindow3_TabsVersion {

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
		
		GUIDockableTabList docker_tabs = new GUIDockableTabList();
				
				for (String c : new String[] {"A", "B", "C", "D", "E", "F", "G" }) {
					GUIDockable docker_iter = new GUIDockable();
					docker_iter.titlebar().title("Hello! A docker "+c+" in Tab!");
					docker_iter.draggable(true);
					setStyle(docker_iter, gui);
					docker_tabs.addDocker(docker_iter);
				}
				
				// TODO: Test Without
				docker_tabs.identifier("contained");

		main_split.first(docker_a);
		main_split.second(docker_tabs);

		gui.root(main_split);
		
		int 	i = 0;
		boolean m = false;
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
			
			// i++;
			if (i > 0	  ) docker_tabs.alignTabBarLeft();
			if (i > 60 * 1) docker_tabs.alignTabBarTop();
			if (i > 60 * 2) docker_tabs.alignTabBarRight();
			if (i > 60 * 3) docker_tabs.alignTabBarBottom();
			if (i > 60 * 4) i = 0;
			
			if (i == 1) {
				m = !m;
				if (m) {
					docker_tabs.setDisplayModeToFull();
				} else {
					docker_tabs.setDisplayModeToIconOnly();
				}
			}
			
			docker_tabs.force_update_all();
			

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

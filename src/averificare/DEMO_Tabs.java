package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIScrollable;
import snowui.elements.base.GUISlider;
import snowui.elements.base.GUITabList;
import snowui.elements.base.GUIText;

public class DEMO_Tabs {

	public static void main(String[] args) {
		
		// tabs, dockers, text editors, right click menu

		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUIList list = new GUIList();
			for (int i = 0; i < 50; i++) {
				list.add(new GUIText("Entry " + i));
			}
		
		GUITabList tabs = new GUITabList();
			tabs.add("Tab 0", new GUIText("Hello!"));
			tabs.add("Tab 1", new GUISlider());
			tabs.add("Tab 2", new GUIScrollable(list));

		gui.root(tabs);
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		window.end();
		GLState.endGLFW();	
	}
}

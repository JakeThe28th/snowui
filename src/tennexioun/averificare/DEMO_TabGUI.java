package tennexioun.averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.base.GUIText;
import snowui.elements.extended.GUISplit;
import tennexioun.DATANavigationBar;

public class DEMO_TabGUI {
	
	public static void main(String[] args) {
		
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, DEMO_TabGUI.class.getCanonicalName());
		BuiltinShaders.init();
		
		GUIInstance 		gui 	= new GUIInstance(window, window.input());
		DATANavigationBar 	tabs 	= new DATANavigationBar();
		GUIText				debug 	= new GUIText("Emptiness") {
			@Override
			public void draw(GUIInstance gui, int depth) {
				gui.canvas().color(Color.DESBLUE.val());
				gui.canvas().rect(limit_rectangle(), depth);
				super.draw(gui, depth + 2);
			}
		};
		
		gui.root(new GUISplit(debug, tabs.gui()));
		
		tabs.addGroup();
		tabs.addGroup();
		tabs.addGroup();
		
		tabs.current_group().insertTab("Test URI Add! 1");
		tabs.current_group().insertTab("Test URI Add! 2");
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}

}

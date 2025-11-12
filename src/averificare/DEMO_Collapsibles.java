package averificare;

import org.lwjgl.glfw.GLFW;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.enums.IconType;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUICollapsible;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;

public class DEMO_Collapsibles {

	public static void main(String[] args) {
		
		GLState.initializeGLFW();
		
		// Create window ...
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		//window.setVsync(false);
		boolean vsync = false;
		
		// Create core shaders (needs to be done after window cuz context)
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUIList list = new GUIList();
		
		for (int i = 0; i < 10; i++) {
			GUICollapsible collapse	= new GUICollapsible()
					.root(new GUIText("Roots"))
					.hidden(new GUIText("snoozie"));
			list.add(collapse);
		}
		
		GUICollapsible collapse0	= new GUICollapsible()
				.root(new GUIText("Roots"))
				.hidden(new GUIIcon(IconType.GENERIC_HOME));
		
		GUICollapsible collapse1	= new GUICollapsible()
				.root(new GUIText("Roots"))
				.hidden(new GUIIcon(IconType.CONTROL_PIN));
		
		((GUICollapsible) list.get(5)).hidden(new GUIList(collapse0, collapse1));
		
		gui.root(list);
		
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

package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;

public class DEMO_GUI {

	public static void main(String[] args) {
		
		// note: list updates cuz its style containd the default style base color whch changes on hover and
		// click even tho its invisible
		
		GLState.initializeGLFW();
		
		// Create window ...
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		//window.setVsync(false);
		
		// Create core shaders (needs to be done after window cuz context)
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUIList list = new GUIList();
		for (int i = 0; i < 5; i++) {
			list.add(new GUIText("Hello, " + i));
		}
		
		GUIList sub_list_1 = new GUIList();
		for (int i = 0; i < 3; i++) {
			sub_list_1.add(new GUIText("mine crafting, " + i));
		}
		list.add(sub_list_1);
		sub_list_1.identifier("demo_list");
		
		GUIList sub_list_2 = new GUIList();
		sub_list_2.horizontalify();
		for (int i = 0; i < 30; i++) {
			sub_list_2.add(new GUIText("flint & stel, " + i));
		}
		list.add(sub_list_2);
		sub_list_2.wrap(true);
		sub_list_2.identifier("demo_horizontal_list");
		
		for (int i = 0; i < 5; i++) {
			list.add(new GUIText("Hello, " + i));
			if (i==2) list.get(i).identifier("none");
		}
		
		list.identifier("demo_list");
		gui.style().setProperty("demo_list", "list_indent", "16");
		
		gui.style().setProperty("demo_horizontal_list", "list_spacing", "36");
		
		
		// Wrapping testing
//		gui.style().setProperty("demo_list", "min_width", "container");
//		gui.style().setProperty("demo_list", "max_width", "container");

		// Centering testing
		gui.style().setProperty("demo_list", "horizontal_alignment", "middle");
		
		
		gui.root(list);
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}
	
}

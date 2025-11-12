package averificare;

import org.lwjgl.glfw.GLFW;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.enums.IconType;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUIClickableRectangle;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIScrollable;
import snowui.elements.base.GUIText;

public class DEMO_Scrollables {

	public static void main(String[] args) {
		
		GLState.initializeGLFW();
		
		// Create window ...
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		//window.setVsync(false);
		boolean vsync = false;
		
		// Create core shaders (needs to be done after window cuz context)
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUIList list		 	= new GUIList();
		GUIList sub_list_1 		= new GUIList();
		GUIList sub_list_2 		= new GUIList().horizontalify();

		for (int i = 0; i < 5;  i++) 	{ list		.add(new GUIText("Hello, " 			+ i)); }
		for (int i = 0; i < 3;  i++) 	{ sub_list_1.add(new GUIText("mine crafting, " 	+ i)); }
		for (int i = 0; i < 30; i++) 	{ sub_list_2.add(new GUIText("flint & stel, " 	+ i)); }

		list		.identifier("demo_list");
		sub_list_1	.identifier("demo_list");
		sub_list_2	.identifier("demo_horizontal_list");

		list.add(sub_list_1);		
		list.add(sub_list_2);
		
		list.add(new GUIClickableRectangle());
		
		for (int i = 0; i < 60; i++) 	{ list.add(new GUIText("Hello, " + i)); }
		
		list.get(2).identifier("none");
		
		gui.style().setProperty("demo_list", 			"list_indent", 		"16");
		gui.style().setProperty("demo_horizontal_list", "list_spacing", 	"36");
		gui.style().setProperty("demo_list", 			"min_width", 		"container");
		gui.style().setProperty("demo_list", 			"max_width", 		"container");
		
		sub_list_2.set(new GUIIcon(IconType.GENERIC_HOME), 3);
		
		gui.root(new GUIScrollable(list));
		
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

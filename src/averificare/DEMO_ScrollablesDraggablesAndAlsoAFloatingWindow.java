package averificare;

import java.util.ArrayList;

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
import snowui.elements.floating.GUIContextMenu;
import snowui.elements.floating.GUIContextMenuOption;

public class DEMO_ScrollablesDraggablesAndAlsoAFloatingWindow {

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

		for (int i = 0; i < 5;  i++) 	{ list		.add(new GUIText("Hello, " 			+ i).draggable(true)); }
		for (int i = 0; i < 3;  i++) 	{ sub_list_1.add(new GUIText("mine crafting, " 	+ i).draggable(true)); }
		for (int i = 0; i < 30; i++) 	{ sub_list_2.add(new GUIText("flint & stel, " 	+ i).draggable(true)); }

		list		.identifier("demo_list");
		sub_list_1	.identifier("demo_list");
		sub_list_2	.identifier("demo_horizontal_list");

		list.add(sub_list_1);		
		list.add(sub_list_2);
		
		list.add(new GUIClickableRectangle());
		
		for (int i = 0; i < 100; i++) 	{ list.add(new GUIText("Hello, " + i).draggable(true)); }
		
		list.get(2).identifier("none");
		
		gui.style().setProperty("demo_list", 			"list_indent", 		"16");
		gui.style().setProperty("demo_horizontal_list", "list_spacing", 	"36");
		gui.style().setProperty("demo_list", 			"min_width", 		"container");
		gui.style().setProperty("demo_list", 			"max_width", 		"container");

		gui.style().setProperty("demo_list", "list_spacing", "5");

		
		sub_list_2.set(new GUIIcon(IconType.GENERIC_HOME).draggable(true), 3);
		
		gui.root(new GUIScrollable(list));
		
//		TODO: Figure out how to segregate this code to be less horrible
//			- Separate Options api thing from the actual elements
//			- Maybe put it in its' own package outside of 'elements'???
//			- some kind of extension system????????
//		
//		...Also, figure out how to handle rollover vs clicking on expandable things...
					
		
		ArrayList<GUIContextMenuOption> options3 = new ArrayList<GUIContextMenuOption>();
			options3.add(new GUIContextMenuOption(null, "W!", null));
			options3.add(new GUIContextMenuOption(IconType.CONTROL_SKIP_PREVIOUS, "te", "C+418"));
			
		ArrayList<GUIContextMenuOption> options2 = new ArrayList<GUIContextMenuOption>();
			options2.add(new GUIContextMenuOption(null, "Wow! So Element!", null));
			options2.add(new GUIContextMenuOption(options3, "The darkness keeps flowing"));
			options2.add(new GUIContextMenuOption(IconType.FAVORITE_DINOSAUR, "Wow! So Temperate", "Ctrl+7"));
		
		ArrayList<GUIContextMenuOption> options = new ArrayList<GUIContextMenuOption>();
			options.add(new GUIContextMenuOption(IconType.CONTROL_PAUSE, "Test!", "...r"));
			options.add(new GUIContextMenuOption(IconType.GENERIC_HOME, "Test2! Hello, This is Text", "No r"));
			options.add(new GUIContextMenuOption(null, "Te! He Text", null));
			options.add(new GUIContextMenuOption(IconType.CONTROL_PIN, "xt", "No r"));
			options.add(new GUIContextMenuOption(options2, "Shadows Cut Further"));
			
		gui.add_window(new GUIContextMenu(options));
		
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

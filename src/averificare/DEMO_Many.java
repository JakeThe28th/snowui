package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;
import snowui.elements.base.GUITextBox;

public class DEMO_Many {

	public static void main(String[] args) {
		
		// tabs, URLs, dockers, text editors, right click menu
		
		/*
		 * 1. Let's create right click menus!!
		 * 2. Text Editors
		 * 3. Tabs
		 * 4. Dockers
		 */

		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUIList list = new GUIList();
		for (int i = 0; i < 5; i++) {
			list.add(new GUIText("Hello, " + i));
		}
		
		GUITextBox textbox = new GUITextBox(" (There's an anime review.)\r\n"
				+ "* (Read it?)\r\n"
				+ "* Teen'z Corner\r\n"
				+ "  Monthly Toon Review:\r\n"
				+ "  Mew Mew Kissy Cutie 2\r\n"
				+ "* This reviewer had Mew Mew 2 as her first exposure to the series...\r\n"
				+ "* And let her tell you, it makes Mew Mew 1 look like a dumpster with sparkly cat ears!\r\n"
				+ "* With a darker storyline and more mature themes...\r\n"
				+ "* The second one treats the viewer like a real adult...\r\n"
				+ "* Instead of like an animal that will die if it goes ten seconds without seeing a beach ball.\r\n"
				+ "* Not to mention, Mew Mew's character in the first one...\r\n"
				+ "* Is more stale than the ramen I eat at home by myself with the lights off.\r\n"
				+ "* Teens and older should check out this dark masterpiece!\r\n"
				+ "* Signed,\r\n"
				+ "  The Anonymous Yellow Lizard\r\n"
				+ "* (You decide not to read it.)");
		
		gui.root(textbox);
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		window.end();
		GLState.endGLFW();	
	}
}

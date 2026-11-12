package averificare;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIScrollable;
import snowui.elements.base.GUISlider;
import snowui.elements.base.GUIText;
import snowui.elements.base.GUITextBox;
import snowui.elements.docking.GUIDockable;
import snowui.elements.docking.GUISplit;
import snowui.neotext.GUINeoTextBox;

public class DEMO_GUI_NeoTextRenderingEditing_Docker {

	public static void main(String[] args) {

		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		GUINeoTextBox textbox = new GUINeoTextBox(" (There's an anime review.)\r\n"
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
		
		GUINeoTextBox textbox2 = new GUINeoTextBox(" (There's an anime review.)\r\n"
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
		
		textbox2.text.text_editor.document.wrap_config.FORCE_WRAP_ON_EDGE = false;
		textbox2.text.text_editor.document.wrap_config.WRAP_ON_BREAKPOINTS = false;
		textbox2.text.text_editor.document.wrap_config.SKIP_SPACE_IF_FIRST_CHAR = false;

		//gui.root(textbox);
		
		gui.style().setProperty("textbox-text", 			"left_margin", 			"8");
		gui.style().setProperty("textbox-text", 			"right_margin", 		"8");
		gui.style().setProperty("textbox-text", 			"top_margin", 			"8");
		gui.style().setProperty("textbox-text", 			"bottom_margin", 		"8");
		
		gui.style().setProperty("textbox-text", 			"outline_size", 		"0");

		GUIDockable docker = new GUIDockable();
		GUISplit split = new GUISplit();
		GUISlider slider = new GUISlider();
		GUIText text = new GUIText("Yolooooo");
		GUIList list = new GUIList();
			list.add(slider);
			list.add(textbox);
			list.add(text);
			split.first(docker);
			GUIDockable d2 = new GUIDockable();
			d2.root(new GUIScrollable(textbox2));
			docker.root(list);
			split.second(d2);
			gui.root(split);
			
		gui.style().setProperty("dockable", 			"base_color", 		"RED");
		list.identifier("snowui-w_contained");
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		window.end();
		GLState.endGLFW();	

	}

}

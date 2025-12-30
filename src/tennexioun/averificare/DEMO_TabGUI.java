package tennexioun.averificare;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.coss.ComposingStyleSheet;
import snowui.coss.enums.Color;
import snowui.elements.base.GUIText;
import snowui.elements.docking.GUISplit;
import tennexioun.TXDATANavigationBar;
import tennexioun.TXINNavigationBar;
import tennexioun.TXNavigationBarSerializer;

public class DEMO_TabGUI {

	private static void HELPERSetUniformMargins(ComposingStyleSheet sheet, String type, String margin) {
		sheet.setProperty(type, "left_margin", 		margin);
		sheet.setProperty(type, "right_margin", 	margin);
		sheet.setProperty(type, "top_margin", 		margin);
		sheet.setProperty(type, "bottom_margin",	margin);
	}
	
	public static void main(String[] args) throws IOException {
		
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, DEMO_TabGUI.class.getCanonicalName());
		BuiltinShaders.init();
		
		GUIInstance 		gui 	= new GUIInstance(window, window.input());
		GUIText				debug 	= new GUIText("Emptiness") {
			@Override
			public void draw(GUIInstance gui, int depth) {
				gui.canvas().color(Color.DESBLUE.val());
				gui.canvas().rect(limit_rectangle(), depth);
				super.draw(gui, depth + 2);
			}
		};
		
		TXDATANavigationBar tabs;
		
		if (new File("tabtest.nbt").exists()) {
			tabs = TXNavigationBarSerializer.load(Paths.get("tabtest.nbt"));
		} else {
			tabs = makeDefaultTabs();
		}
		
		gui.root(new GUISplit(debug, tabs.gui()));

		TXINNavigationBar inputhandler = tabs.inputhandler(window.input(), gui);

		gui.style().setProperty("text", "base_color", "#FFFFFF");
		gui.style().setProperty("selected", "outline_margin", "0");
		gui.style().setProperty("tab_group_title", "base_color", "#FFFFFF");
		gui.style().setProperty("tab_group_title", "size", "18");
		HELPERSetUniformMargins(gui.style(), "tab_group_title", "3");
		HELPERSetUniformMargins(gui.style(), "minitab", "1");
		
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			inputhandler.tick();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
		TXNavigationBarSerializer.save(tabs, Paths.get("tabtest.nbt"));

	}

	private static TXDATANavigationBar makeDefaultTabs() {
		TXDATANavigationBar tabs = new TXDATANavigationBar();
		tabs.addGroup();
		tabs.addGroup();
		tabs.addGroup();
		tabs.addGroup();
		tabs.current_group().insertTab("Test URI Add! 1");
		tabs.current_group().insertTab("Test URI Add! 2");
		tabs.group(2).insertTab("By the way everyone, the");
		tabs.group(2).insertTab("reason you can see me right now");
		tabs.group(2).insertTab("is because i'm projecting myself with");
		tabs.group(2).insertTab("special technolo");
		for (int i = 0; i < 500; i++) tabs.group(3).insertTab("fruit never expires");
		return tabs;
	}

}

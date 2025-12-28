package tennexioun;

import org.lwjgl.glfw.GLFW;

import frost3d.Input;
import snowui.GUIInstance;
import snowui.elements.extended.GUITextBox;

public class TXINNavigationBar {

	Input input;
	TXDATANavigationBar navigation_bar;
	GUIInstance gui;
	
	public static interface InsertTabCallback { public void run(TXDATANavigationBar navigation); }
	
	public String 				new_tab_uri = "snowui://blank";
	public InsertTabCallback 	insert_tab_cb = null;
	
	TXINNavigationBar(TXDATANavigationBar navigation_bar, Input input, GUIInstance gui) {
		this.input = input;
		this.navigation_bar = navigation_bar;
		this.gui = gui;
	}
	
	public void tick() {
		if (GUITextBox.selected != null) return;
		// Tab bar controls
		if (input.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			// Tab group navigation
			if (input.keyPressed(GLFW.GLFW_KEY_LEFT) || input.keyPressed(GLFW.GLFW_KEY_A)) {
				navigation_bar.previous_group();
			}
			if (input.keyPressed(GLFW.GLFW_KEY_RIGHT) || input.keyPressed(GLFW.GLFW_KEY_D)) {
				navigation_bar.next_group();
			}
			// Tab group add/edit
			if (input.keyPressed(GLFW.GLFW_KEY_Q)) navigation_bar.addGroup();
			if (input.keyPressed(GLFW.GLFW_KEY_E)) navigation_bar.gui().edit_group_name(gui);
		} else {
			// Tab navigation
			if (input.keyPressed(GLFW.GLFW_KEY_LEFT) || input.keyPressed(GLFW.GLFW_KEY_A)) {
				navigation_bar.current_group().previous_tab();
			}
			if (input.keyPressed(GLFW.GLFW_KEY_RIGHT) || input.keyPressed(GLFW.GLFW_KEY_D)) {
				navigation_bar.current_group().next_tab();
			}
			// Tab add/delete/restore
			if (input.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
				if (input.keyPressed(GLFW.GLFW_KEY_Q)) if (insert_tab_cb != null) insert_tab_cb.run(navigation_bar);
				if (input.keyPressed(GLFW.GLFW_KEY_W)) navigation_bar.current_group().deleteTab();
				if (input.keyPressed(GLFW.GLFW_KEY_Z)) navigation_bar.current_group().restoreTab();
				if (input.keyPressed(GLFW.GLFW_KEY_N)) navigation_bar.current_group().insertTab(new_tab_uri);
			}
		}
	}

}

package snowui;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

import frost3d.Input;
import frost3d.enums.CursorType;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.implementations.VectorIconRenderer;
import frost3d.interfaces.F3DExtWindow;
import frost3d.interfaces.F3DIconRenderer;
import frost3d.interfaces.F3DWindow;
import frost3d.utility.Rectangle;
import snowui.coss.ComposingStyleSheet;
import snowui.elements.abstracts.GUIElement;
import snowui.support.DragAndDropSupport;
import snowui.utility.GUIDebugger;
import snowui.utility.FPSCounter;
import snowui.utility.GUIUtility;

public class GUIInstance {
		
	static final boolean SHOW_FPS = true;
	public static final boolean DEBUG = true;
	
	public FPSCounter 			fps 		= new FPSCounter();
	public DragAndDropSupport 	drag_and_drop_support = new DragAndDropSupport(this);
	
	public static final int INPUT_REPEAT_TIME = 500;

	public int PREVIEW_DEPTH() {
		return 900;
	}
	
	public void size(int width, int height) {
		if (canvas == null || canvas.width() != width || canvas.height() != height) {
			canvas = new SimpleCanvas();
			canvas.framebuffer(null);
			canvas.size(width, height);
			canvas.textrenderer(text);
			canvas.iconrenderer(icons);
			//canvas.clear_color(0, 0, 0, 1);
			if (root != null) root.scissor_rectangle_recursive(canvas.size());
		}
	}
	
	F3DIconRenderer				icons;
	SimpleTextRenderer 			text;
	SimpleCanvas 				canvas;
	
	public void font_size(int new_size) {
		textrenderer().font_size(new_size);
		iconrenderer().size(new_size);
	}
	
	public F3DIconRenderer 		iconrenderer() { return icons; }
	public SimpleTextRenderer 	textrenderer() { return text; }
	public SimpleCanvas 		canvas() { return canvas; }
	
	public void push_scissor(Rectangle rectangle) {
		canvas.push_scissor(rectangle);
	}
	
	public void pop_scissor() {
		canvas.pop_scissor();
	}

	// TODO: Add tab navigation support (maybe by hiding and moving the mouse around?)

	Input input;
	public Vector2i mouspos	() 	{ return new Vector2i(input.mouseX(), input.mouseY()); }
	public int 		mx		() 	{ return input.mouseX(); }
	public int 		my		() 	{ return input.mouseY(); }
	public boolean  hasInput() 	{ return input.hasInputThisFrame(); }
	
	public boolean 	primary_click_pressed () { return input.mouseButtonPressed  (GLFW_MOUSE_BUTTON_LEFT); }
	public boolean 	primary_click_down	  () { return input.mouseButtonDown	    (GLFW_MOUSE_BUTTON_LEFT); }
	public boolean 	primary_click_released() { return input.mouseButtonReleased (GLFW_MOUSE_BUTTON_LEFT); }

	public static final float SCROLL_SCALE = 25;
	
	public Vector2f scroll() { 
		if (input.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			return new Vector2f((float) input.scrollY() * SCROLL_SCALE, (float) input.scrollX() * SCROLL_SCALE); 
		} else {
			return new Vector2f((float) input.scrollX() * SCROLL_SCALE, (float) input.scrollY() * SCROLL_SCALE); 
		}
	}
	
	public Input rawinput() { return input; }
	
	CursorType current_cursor = CursorType.ARROW_CURSOR;
	public void cursor(CursorType new_cursor) {
		this.current_cursor = new_cursor;
	}
	
	public GUIInstance(F3DWindow window, Input input) {
		this.input = input;
		this.text = new SimpleTextRenderer();
		text.anti_aliasing_enabled(true);
		this.icons = new VectorIconRenderer();
	}
	
	// -- ++  ...  ++ -- //
	
	GUIElement root;
	public void root(GUIElement r) { root = r; }
	public GUIElement root() { return root; }

	ArrayList<GUIElement> windows; // TODO
	
	ComposingStyleSheet style = new ComposingStyleSheet();
	public ComposingStyleSheet style() { return style; }
	public void style(ComposingStyleSheet sheet) {
		this.style = sheet;
	}
	
	public void render() {
		cursor(CursorType.ARROW_CURSOR);
		
		GUIElement.tick(this, root, canvas().size(), 0);	
		drag_and_drop_support.tick();
		
		if (SHOW_FPS) fps.drawFPS(canvas);
		if (DEBUG) GUIDebugger.drawTree(this, root, input);
		
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		canvas.draw_frame();
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(8, "Draw (canvas)");
		
		if (input.cursor() != current_cursor) input.cursor(current_cursor);
	}
	
	public GUIElement current_hovered_element() {
		// TODO: work with windows/menus etc
		return GUIUtility.getHoveredElement(root);
	}

	GUIElement last_pressed_element;
	
	public void last_pressed_element(GUIElement element) {
		last_pressed_element = element;
	}
	
	public GUIElement last_pressed_element() {
		return last_pressed_element;
	}

	public GUIElement current_window_root() {
		// TODO Auto-generated method stub
		return root;
	}

}

package snowui;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

import frost3d.Input;
import frost3d.enums.CursorType;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.implementations.VectorIconRenderer;
import frost3d.interfaces.F3DIconRenderer;
import frost3d.interfaces.F3DWindow;
import snowui.coss.COSSProperty;
import snowui.coss.ComposingStyleSheet;
import snowui.coss.enums.Color;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.FloatingElement;
import snowui.elements.meta.GUIRootContainer;
import snowui.support.DragAndDropSupport;
import snowui.utility.GUIDebugger;
import snowui.utility.FPSCounter;
import snowui.utility.GUIUtility;

public class GUIInstance {
		
	public static final boolean SHOW_FPS 				= true;
	public static final boolean DEBUG 					= true;
	
	public FPSCounter 			fps 					= new FPSCounter();
	public DragAndDropSupport 	drag_and_drop_support 	= new DragAndDropSupport(this);
	
	public static final int 	INPUT_REPEAT_TIME 		= 500;
	public 				int 	PREVIEW_DEPTH()  { return 800; }
	
	// ............................ -- Canvas Stuff -- ............................ //
	
	public void size(int width, int height) {
		if (canvas == null || canvas.width() != width || canvas.height() != height) {
			canvas = new SimpleCanvas();
			canvas.framebuffer(null);
			canvas.size(width, height);
			canvas.textrenderer(text);
			canvas.iconrenderer(icons);
			canvas.clear_color(clear_color);
			root_container.scissor_rectangle_recursive(canvas.size());
		}
	}
	
	F3DIconRenderer				icons;
	SimpleTextRenderer 			text;
	SimpleCanvas 				canvas;
	Vector4f 					clear_color;

	public F3DIconRenderer 		iconrenderer() { return icons			; }
	public SimpleTextRenderer 	textrenderer() { return text			; }
	public SimpleCanvas 		canvas() 	   { return canvas			; }
	public Vector4f 			clear_color()  { return clear_color		; }
	
	public void clear_color(Vector4f new_color) { 
		clear_color = new_color;
		if (canvas != null) canvas.clear_color(clear_color);
	}
	
	public void iconrenderer(F3DIconRenderer new_icons) {
		icons = new_icons;
		if (canvas != null) canvas.iconrenderer(new_icons);
	}
	
	public void font_size(int new_size) {
		textrenderer().font_size(new_size);
		iconrenderer().size(new_size);
	}
	
	// ............................ -- Input -- ............................ //

	// TODO: Add tab navigation support (maybe by hiding and moving the mouse around?)

	Input input;
	public Input 	rawinput()  { return input; }

	public Vector2i mousepos() 	{ return new Vector2i(input.mouseX(), input.mouseY()); }
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
	
	       CursorType   cursor;
	public CursorType 	cursor() 						{ return this.cursor; }
	public void 		cursor(CursorType new_cursor) 	{ this.cursor = new_cursor; }
	
	// ............................ -- Stylesheet -- ............................ //
	
	       ComposingStyleSheet style;
	public ComposingStyleSheet style() { return style; }
	
	public void style(ComposingStyleSheet sheet) {
		this.style = sheet;
		force_update_all();
		COSSProperty style_clear_color = sheet.getProperty("gui_clear_color", "base_color", null);
		if (style_clear_color != null) this.clear_color(style_clear_color .color());
	}
	
	// ............................ -- Elements -- ............................ //
	
	GUIRootContainer 			root_container = new GUIRootContainer();
	public void 				root(GUIElement r) 	{ 		 root_container.root(r); }
	public GUIElement 			root() 				{ return root_container.root() ; }
	
	ArrayList<FloatingElement> 	windows = new ArrayList<FloatingElement>();
	public void 				add_window(FloatingElement element) 	{ windows.add(element); }
	public void 				remove_window(FloatingElement element) 	{ windows.remove(element); }
	
	public void force_update_all() {
		root_container.force_update_all();
		for (FloatingElement w : windows) w.as_element().force_update_all();
	}
	
	public GUIElement current_window_root() {
		for (int index = windows.size()-1; index >= 0; index--) {
			GUIElement element = windows.get(index).as_element();
			if (element.limit_rectangle() != null && element.limit_rectangle().contains(mousepos())) {
				return windows.get(index).as_element();
			}
		}
		return root_container;
	}
	
	public ArrayList<GUIElement> current_hovered_element_tree() {
		return GUIUtility.getHoveredElementTree(current_window_root());
	}

	       GUIElement last_pressed_element;
	public GUIElement last_pressed_element() 				{ return last_pressed_element; }
	public void last_pressed_element(GUIElement element) 	{ last_pressed_element = element; }
	
	// ............................ -- The Rest -- ............................ //
	
	public GUIInstance(F3DWindow window, Input input) {
		this.input 			= input;
		this.text 			= new SimpleTextRenderer();
		this.icons 			= new VectorIconRenderer();
		this.style 			= new ComposingStyleSheet();
		this.clear_color 	= Color.BASIC_CLEAR.val();
		text.anti_aliasing_enabled(true);
	}
	
	@SuppressWarnings("unchecked")
	public void render() {
		cursor(CursorType.ARROW_CURSOR);
		
		GUIElement.tick(this, root_container, canvas().size(), 0, (root_container == this.current_window_root()));	
		for (FloatingElement w : ((ArrayList<FloatingElement>) windows.clone())) {
			GUIElement.tick(this, w.as_element(), w.position(), 0, (w == this.current_window_root()));
		}
		drag_and_drop_support.tick();
		
		if (SHOW_FPS) fps.drawFPS(canvas);
		if (DEBUG) GUIDebugger.drawTree(this, current_window_root(), input);
		
		// Dunno why, but for some reason, dragging a GUISplit
		// causes weird visual glitches to appear until input
		// is released, *unless* something else is drawn after
		// drawing all elements (and that something can be
		// the debugger or FPS counter as well).
		//
		// This will likely have many ramifications and is a
		// very important bug that I do not feel like
		// dealing with right now
		canvas.rect(0, 0, 0, 0, 0);
		
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		canvas.draw_frame();
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(8, "Draw (canvas)");
		
		if (input.cursor() != cursor) input.cursor(cursor);
	}
	
}

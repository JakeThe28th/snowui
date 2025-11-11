package snowui;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;

import frost3d.Input;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.interfaces.F3DWindow;
import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.coss.ComposingStyleSheet;
import snowui.elements.GUIElement;
import snowui.utility.DebugElementTree;
import snowui.utility.FPSCounter;

public class GUIInstance {
		
	static final boolean SHOW_FPS = true;
	static final boolean DEBUG = true;
	FPSCounter fps = new FPSCounter();
	
	public static final int INPUT_REPEAT_TIME = 500;

	
	// only works if DEBUG is true
	// i'm like 99% sure
	boolean show_debug = true;
	
	public void size(int width, int height) {
		if (canvas == null || canvas.width() != width || canvas.height() != height) {
			canvas = new SimpleCanvas();
			canvas.framebuffer(null);
			canvas.size(width, height);
			canvas.textrenderer(text);
			//canvas.clear_color(0, 0, 0, 1);
		}
	}
	
	F3DWindow 					window;
	SimpleTextRenderer 			text;
	SimpleCanvas 				canvas;
	
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
	
	public GUIInstance(F3DWindow window, Input input) {
		this.input = input;
		this.text = new SimpleTextRenderer();
		text.anti_aliasing_enabled(true);
	}
	
	// -- ++  ...  ++ -- //
	
	GUIElement root;
	public void root(GUIElement r) { root = r; }
	
	ArrayList<GUIElement> windows; // TODO
	
	ComposingStyleSheet style = new ComposingStyleSheet();
	public ComposingStyleSheet style() { return style; }
	
	public void render() {
		
		if (input.keyPressed(GLFW_KEY_LEFT_ALT)) {
			show_debug = !show_debug;
		}
		
		GUIElement.tick(this, root);
		if (SHOW_FPS) fps.drawFPS(canvas);
		if (DEBUG) if (show_debug) DebugElementTree.drawTree(canvas, root);
		canvas.draw_frame();
	}
	
}

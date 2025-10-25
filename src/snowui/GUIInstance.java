package snowui;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.joml.Vector2i;
import org.joml.Vector4f;

import frost3d.Input;
import frost3d.Shaders;
import frost3d.conveniences.Icons;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.interfaces.F3DCanvas;
import frost3d.interfaces.F3DWindow;
import frost3d.utility.Rectangle;
import snowui.coss.ComposingStyleSheet;
import snowui.elements.GUIElement;

public class GUIInstance {
	
	// -- ++  ...  ++ -- //
	
	static final boolean SHOW_FPS = false;
	// TODO move to color
	public static final Vector4f TRANSPARENT_WHITE 	= new Vector4f(1, 1, 1, 0.25f);
	public static final Vector4f TRANSLUCENT_WHITE 	= new Vector4f(1, 1, 1, 0.5f);
	public static final Vector4f TRANSPARENT_RED 	= new Vector4f(1, 0, 0, 0.25f);
	public static final Vector4f TRANSPARENT_AQUA 	= new Vector4f(0, 1, 0.75f, 0.25f);
	public static final Vector4f BLACK 				= new Vector4f(0,0,0,1);
	public static final Vector4f WHITE 				= new Vector4f(1,1,1,1);
	public static final Vector4f TRANSLUCENT_BLACK 	= new Vector4f(0, 0, 0, 0.5f);
	public static final Vector4f RED 				= new Vector4f(1, 0, 0, 1);
	public static final Vector4f YELLOW 			= new Vector4f(1, 1, 0, 1);
	public static final Vector4f GOLD 				= new Vector4f(1, 0.66f, 0, 1);

	public static final Vector4f BLACK75 			= new Vector4f(0,0,0,0.75f);
	
	// -- ++  ...  ++ -- //
	
	int width = -1;
	int height = -1;
	Rectangle gui_bounds = new Rectangle(0, 0, width, height);
	
	public void size(int width, int height) {
		if (this.width != width || this.height != height) {
			this.width = width;
			this.height = height;
			
			canvas = new SimpleCanvas();
			
			canvas.framebuffer(null);
			canvas.size(width, height);
			
			text = new SimpleTextRenderer();
			text.anti_aliasing_enabled(true);
			canvas.textrenderer(text);
			
			gui_bounds = new Rectangle(0, 0, width, height);
			
			//canvas.clear_color(0, 0, 0, 1);
		}
	}
	
	public Vector2i sizeof(String str) {
		return text.size(str);
	}
	

	public Rectangle size() {
		return gui_bounds;
	}
	
	SimpleTextRenderer text;
	SimpleCanvas canvas;
	F3DWindow window;
	Input input;
	
	public SimpleCanvas canvas() { return canvas; }

	 
	public GUIInstance(F3DWindow window, Input input) {
		
		this.input = input;
		
		Shaders.bind("gui");
		
	}
	
	// -- ++  ...  ++ -- //
	
	GUIElement root;
	
	public void root(GUIElement r) {
		root = r;
	}
	
	ComposingStyleSheet style = new ComposingStyleSheet();
	public ComposingStyleSheet style() { return style; }
	
	public void render() {		
		
		int xx = input.mouseX();
		
		// Rendering Tests //
//		canvas.color(new Vector4f(1,1,1,1));
//		canvas.text(xx, 10, 0, "Text A");
//		
//		canvas.color(new Vector4f(1,1,0,1));
//		canvas.rect(20, 20, 50, 50, 1);
//		
//		canvas.color(new Vector4f(1,0,0,1));
//		canvas.text(10, 40, 2, "Text B (red)");
//		
//		canvas.color(new Vector4f(1,0,1,1));
//		canvas.rect(50, 25, 60, 60, 3);
		
		//canvas.color(new Vector4f(1,1,1,1));
		//canvas.rect(70, 70, 170, 170, 4, khronos);
		
		GUIElement.tick(this, root);
		
		canvas.color(new Vector4f(1,1,0.5f,1));
		Icons.icon(canvas, xx, 60, 5, "home", 30);
		
		drawFPS();
	
		canvas.draw_frame();

	}
	
	// -- ++  ...  ++ -- //
	
	public long 	frame_start_time 		= 0;
	int[] 			frametimes 				= new int[60];
	int 			frametime_index 		= 0;
	
	public void drawFPS() {
		if (SHOW_FPS) {

			NumberFormat f = DecimalFormat.getInstance();
			f.setMinimumIntegerDigits(3);
			f.setMinimumFractionDigits(3);
			
			int cumulative_frame_time = 0;
			for (int t : frametimes) cumulative_frame_time += t;
			
			drawStrings(canvas, 5, 5, 1000, 
				"MSPT=" + f.format((cumulative_frame_time / (float) frametimes.length)),
				"FPS=" + f.format(1000f/(cumulative_frame_time / (float) frametimes.length))
				);
			
			int frame_time = (int) (System.currentTimeMillis()-frame_start_time);
			frametimes[frametime_index] = frame_time;
			frametime_index ++;
			if (frametime_index >= frametimes.length) frametime_index = 0;
			
			//
			
			frame_start_time = System.currentTimeMillis();
			
		}
	}
	
	void drawStrings(F3DCanvas canvas, int x, int y, int depth, String... strings) {
		int xx = x;
		int yy = y;
		int m  = 5;
		for (String str : strings) {
			Vector2i size = text.size(str);
			canvas.color(BLACK75);
			canvas.rect(xx, yy, xx+size.x+(m*2), yy+size.y+(m*2), depth);
			canvas.color(WHITE);
			canvas.text(xx+m, yy+m, depth+1, str);
			yy += size.y + m*3;
		}
	}


	public boolean hasInput() {
		// TODO Auto-generated method stub
		return false;
	}

	
	//todo maybe the frametime of the gui alone is a useful metric but the round trip is prolly good enough for now

}

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
import frost3d.interfaces.F3DWindow;

public class GUIInstance {
	
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
	
	int width = -1;
	int height = -1;
	
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
			
			//canvas.clear_color(0, 0, 0, 1);
		}
	}
	
	SimpleTextRenderer text;
	SimpleCanvas canvas;
	F3DWindow window;
	Input input;
	 
	public GUIInstance(F3DWindow window, Input input) {
		
		this.input = input;
		
		
		Shaders.bind("gui");
		
	}
	
	static final boolean SHOW_FPS = true;
	
	public long frame_start_time = 0;
	int cumulative_frame_time = 0;
	int frames_counted = 0;
	
	public void render() {
		
		//if (SHOW_FPS) frame_start_time = System.currentTimeMillis();
		
		
		int xx = input.mouseX();
		
		// Rendering Tests //
			canvas.color(new Vector4f(1,1,1,1));
			canvas.text(xx, 10, 0, "Text A");
			
			canvas.color(new Vector4f(1,1,0,1));
			canvas.rect(20, 20, 50, 50, 1);
			
			canvas.color(new Vector4f(1,0,0,1));
			canvas.text(10, 40, 2, "Text B (red)");
			
			canvas.color(new Vector4f(1,0,1,1));
			canvas.rect(50, 25, 60, 60, 3);
			
			//canvas.color(new Vector4f(1,1,1,1));
			//canvas.rect(70, 70, 170, 170, 4, khronos);
			
			canvas.color(new Vector4f(1,1,0.5f,1));
			Icons.icon(canvas, xx, 60, 5, "home", 30);
			
		if (SHOW_FPS) {
			NumberFormat f = DecimalFormat.getInstance();
			f.setMinimumIntegerDigits(3);
			f.setMinimumFractionDigits(3);
			
			String mspt_text = "MSPT=" + f.format((cumulative_frame_time / (float) frames_counted));
			Vector2i size1 = text.size(mspt_text);
			canvas.color(BLACK75);
			canvas.rect(5, 5, size1.x+10, size1.y+10, 1000);
			canvas.color(WHITE);
			canvas.text(10, 10, 1001, mspt_text);
			
			int offset = size1.y + 20;
			String fps_text = "FPS=" + f.format(1000f/(cumulative_frame_time / (float) frames_counted));
			Vector2i size2 = text.size(fps_text);
			canvas.color(BLACK75);
			canvas.rect(5, 5+offset, size2.x+10, size2.y+10+offset, 1000);
			canvas.color(WHITE);
			canvas.text(10, 10+offset, 1001, fps_text);
		}
			
			canvas.draw_frame();
			
		if (SHOW_FPS) {
			int frame_time = (int) (System.currentTimeMillis()-frame_start_time);
			cumulative_frame_time += frame_time;
			frames_counted++;
			
			if (frames_counted > 120) {
				frames_counted = 1;
				cumulative_frame_time = frame_time;
			}
		}
			
	}

}

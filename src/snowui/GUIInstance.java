package snowui;

import org.joml.Vector4f;

import frost3d.Shaders;
import frost3d.conveniences.Icons;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.interfaces.F3DCanvas;
import frost3d.interfaces.F3DWindow;

public class GUIInstance {
	
	F3DCanvas canvas;
	F3DWindow window;
	
	public GUIInstance(F3DWindow window) {
		
		canvas = new SimpleCanvas();
		
		canvas.framebuffer(window.framebuffer());
		canvas.width(window.width());
		canvas.height(window.height());
			
		SimpleTextRenderer text = new SimpleTextRenderer();
		text.anti_aliasing_enabled(true);
		canvas.textrenderer(text);
		
		Shaders.bind("gui");
		
	}

	public void render() {
		xx = window.input().mouseX();
		
		// Rendering Tests //
			canvas.color(new Vector4f(1,1,1,1));
			canvas.text(xx, 10, 0, "Text A");
			
			canvas.color(new Vector4f(1,1,0,1));
			canvas.rect(20, 20, 50, 50, 1);
			
			canvas.color(new Vector4f(1,0,0,1));
			canvas.text(10, 40, 2, "Text B (red)");
			
			canvas.color(new Vector4f(1,0,1,1));
			canvas.rect(50, 25, 60, 60, 3);
			
			canvas.color(new Vector4f(1,1,1,1));
			canvas.rect(70, 70, 170, 170, 4, khronos);
			
			canvas.color(new Vector4f(1,1,0.5f,1));
			Icons.icon(canvas, xx, 60, 5, "home", 30);
			
			canvas.draw_frame();
	}

}

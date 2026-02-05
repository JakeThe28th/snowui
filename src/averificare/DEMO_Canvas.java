package averificare;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.joml.Vector2i;

import frost3d.Framebuffer;
import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTexture;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.frost3d.GUIF3DCanvas;

public class DEMO_Canvas {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());
		
		SimpleCanvas canvas = new SimpleCanvas();
					 canvas.adopt(new Framebuffer(360, 200));
					 canvas.color(Color.WHITE.val());
		GUIF3DCanvas canvas_gui = new GUIF3DCanvas(canvas);

		gui.root(canvas_gui);
		
		SimpleTexture texture = new SimpleTexture("khronos.png");

		gui.style().setProperty("f3d_canvas", "background_color", "#33593d");
		
		while (!window.should_close()) {
			
			Vector2i m = canvas_gui.internal_mouse(gui);
			if (m != null) {
				int s = 100;
				canvas.rect(m.x-s, m.y-s, m.x+s, m.y+s, 0, texture);
				canvas.draw_frame();
			}
			
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}
	
}

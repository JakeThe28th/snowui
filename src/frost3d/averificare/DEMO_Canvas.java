package frost3d.averificare;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import frost3d.Shaders;
import frost3d.conveniences.Icons;
import frost3d.interfaces.*;
import frost3d.utility.Log;
import frost3d.implementations.*;

public class DEMO_Canvas {

	public static void main(String[] args) {
		canvas_test();
	}
	
	private static void canvas_test() {

		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		GLFWErrorCallback.createPrint(System.err).set();
		if ( !glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");
						
		SimpleCanvas canvas = new SimpleCanvas();
		
			// Create window ...
			SimpleWindow window = new SimpleWindow(300, 600, "Canvas Rendering Tests") {
				@Override public void onWindowResize() {
					canvas.world_transform(new Matrix4f().ortho(0, width(), height(), 0, -1024f, 1024f));
				}
			};
			
			// Create core shaders (needs to be done after window cuz context)
			Shaders.init();
			Shaders.bind("gui");
			
			// The canvas will render directly to the window.
			// So, after calling draw_frame(), all that's
			// necessary is glfwSwapBuffers().
			canvas.framebuffer(window.framebuffer());
			canvas.width(window.width);
			canvas.height(window.height);
			
			// Set the text renderer ...
			SimpleTextRenderer text = new SimpleTextRenderer();
			text.anti_aliasing_enabled(true);
			canvas.textrenderer(text);
			
			int xx = 0;
			
		GLTexture khronos = null;
		try { khronos = new SimpleTexture("khronos.png"); } catch (IOException _) { Log.send("Failed to load texture."); }
			
		while (!window.should_close()) {
			
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

			window.tick();
		}
		
		window.end();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}

}

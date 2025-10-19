package averificare;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.*;
import frost3d.Shaders;
import frost3d.implementations.SimpleWindow;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleMesh;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.opengl.GL11.*;

public class DEMO_GUI0 {
	
	public static void main(String[] args) {
		canvas_test();
		//gui_test();
	}
	
	private static void canvas_test() {

		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		GLFWErrorCallback.createPrint(System.err).set();
		if ( !glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");
						
		SimpleCanvas canvas = new SimpleCanvas();
		
			// Create core shaders
			Shaders.init();
			Shaders.bind("gui");
		
			// Create window ...
			SimpleWindow window = new SimpleWindow(300, 600, "Canvas Rendering Tests") {
				@Override public void onWindowResize() {
					canvas.world_transform(new Matrix4f().ortho(0, width(), height(), 0, -1024f, 1024f));
				}
			};
			
			// The canvas will render directly to the window.
			// So, after calling draw_frame(), all that's
			// necessary is glfwSwapBuffers().
			canvas.framebuffer(window.framebuffer());
			canvas.width(window.width);
			canvas.height(window.height);
			
			//canvas.textrenderer( /* TODO */);
			
			// TODO
			
			
			//glViewport(0,0,window.width, window.height);
			
		while (!window.should_close()) {
			
			// Rendering Tests //
				//canvas.color(new Vector4f(1,1,1,1));
				//canvas.text(10, 10, 0, "Text A");
				
				canvas.color(new Vector4f(1,1,0,1));
				canvas.rect(20, 20, 50, 50, 1);
				
				//canvas.color(new Vector4f(1,0,0,1));
				//canvas.text(10, 10, 2, "Text B (red)");
				
				canvas.color(new Vector4f(1,0,1,1));
				canvas.rect(50, 25, 60, 60, 3);
				
				canvas.draw_frame();

			window.tick();
		}
		
		window.end();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
	}

}

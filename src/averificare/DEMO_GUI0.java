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
	
	public static SimpleWindow current_window;

	private static void canvas_test() {

		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		GLFWErrorCallback.createPrint(System.err).set();
		if ( !glfwInit() ) throw new IllegalStateException("Unable to initialize GLFW");
		
		SimpleWindow window = new SimpleWindow(300, 600, "Canvas Rendering Tests");
		current_window = window;
		
		// TODO: Probably take this 'canvas' thing and abstract it to be used outside of the GUI
		
		SimpleCanvas canvas = new SimpleCanvas();
			// The canvas will render directly to the window.
			// So, after calling draw_frame(), all that's
			// necessary is glfwSwapBuffers().
			canvas.framebuffer(window.framebuffer());
			canvas.width(window.width);
			canvas.height(window.height);
			
			//canvas.textrenderer( /* TODO */);
			
			// TODO
			Shaders.init();; // TODO: re-private renderqueue
			Shaders.bind("gui");
			canvas.renderqueue.world_transform(new Matrix4f().ortho(0, window.width, window.height, 0, -1024f, 1024f));
			
			//glViewport(0,0,window.width, window.height);
			
			// testing
//			SimpleMesh m = new SimpleMesh(
//					new float[] {
//							1,  1, 0.0f,  // top right
//							1,  0, 0.0f,  // bottom right
//						    0,  0, 0.0f,  // bottom left
//						    0,  1, 0.0f   // top left 
//							},
//							new float[] {
//							1,1,
//							1,0,
//							0,0,
//							0,1},
//							new int[] {
//							    0, 1, 3,   // first triangles
//							    1, 2, 3    // second triangle
//							});
			
		while (!window.should_close()) {
//			
//			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//
//			m.bind();
//			
//			GL40.glDrawElements(GL_TRIANGLES, m.index_count(), GL_UNSIGNED_INT, 0);

			
			
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

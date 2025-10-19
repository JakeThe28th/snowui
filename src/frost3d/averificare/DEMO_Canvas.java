package frost3d.averificare;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

import java.io.IOException;
import java.text.ParseException;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import frost3d.Framebuffer;
import frost3d.GLState;
import frost3d.Shaders;
import frost3d.conveniences.Icons;
import frost3d.enums.Alignment;
import frost3d.enums.FillMode;
import frost3d.interfaces.*;
import frost3d.utility.Log;
import frost3d.implementations.*;

public class DEMO_Canvas {

	public static void main(String[] args) {
		canvas_test();
	}
	
	private static void canvas_test() {
	
		GLState.initializeGLFW();
		
		SimplePositionedCanvas canvas = new SimplePositionedCanvas();
		
			canvas.size(80, 120);
		
			// Create window ...
			SimpleWindow window = new SimpleWindow(300, 600, "Canvas Rendering Tests") {
				@Override public void onWindowResize() {
					//canvas.size(width()/2, height()/2);
				}
			};
			
			// Create core shaders (needs to be done after window cuz context)
			Shaders.init();
			Shaders.bind("gui");
			
			// The canvas will render directly to the window.
			// So, after calling draw_frame(), all that's
			// necessary is glfwSwapBuffers().
			canvas.framebuffer(null);
			Framebuffer frame = new Framebuffer(80,120);
			canvas.framebuffer(frame);
			
			// Set the text renderer ...
			SimpleTextRenderer text = new SimpleTextRenderer();
			text.anti_aliasing_enabled(true);
			canvas.textrenderer(text);
			
			canvas.halign(Alignment.MIDDLE);
			canvas.valign(Alignment.MIDDLE);
			canvas.fillmode(FillMode.CONTAIN);
			
			int xx = 0;
			
		GLTexture khronos = null;
		try { khronos = new SimpleTexture("khronos.png"); } catch (IOException _) { Log.send("Failed to load texture."); }
			
//		Framebuffer pixeltest = new Framebuffer(128,256);
		
		while (!window.should_close()) {
			
			//xx = window.input().mouseX();
			canvas.outrectangle(window.width, window.height);
			xx = canvas.internalpoint(new Vector2i(window.input().mouseX(),0)).x;
			Shaders.bind("gui");
			
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
				
				window.bind();
				GLState.clearColor(1, 0, 0, 1);
				GLState.clear();
				Shaders.bind("screen");				
				canvas.mesh().bind();
				GL40.glActiveTexture(GL40.GL_TEXTURE0);									// Activate texture0
				GL40.glBindTexture(GL40.GL_TEXTURE_2D,  canvas.framebuffer().color_texture.gltexture());	// Bind the texture t to texture0
				
				GL40.glDrawElements(GL_TRIANGLES, canvas.mesh().index_count(), GL_UNSIGNED_INT, 0);


			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}

}

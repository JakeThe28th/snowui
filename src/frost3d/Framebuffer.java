package frost3d;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

import org.joml.Vector2d;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import frost3d.enums.Alignment;
import frost3d.enums.FillMode;
import frost3d.implementations.SimpleMesh;
import frost3d.implementations.SimpleTexture;
import frost3d.interfaces.F3DWindow;
import frost3d.interfaces.GLTexture;
import frost3d.utility.Log;

public class Framebuffer {
	
	int fbo = 0;
	GLTexture color_texture = null;
	GLTexture depth_texture = null;
	
	int width;
	int height;
	
	float z = 0.0f;
	
	HashMap<String, SimpleMesh> mesh_queue = new HashMap<String, SimpleMesh>();
	
	public Framebuffer(int width, int height) { 
		this.width =  width;
		this.height = height;
		
		this.fbo = glGenFramebuffers();
		bind();
			
		// Make an empty RGBA texture to render to
		this.color_texture = new SimpleTexture(); // The texture is bound upon creation, no need to call bind().
			// Empty texture data. 
			SimpleTexture.texImage2D(GL_TEXTURE_2D, width, height, GL_RGBA, GL_UNSIGNED_BYTE, 0);
			// Attach the texture to the Framebuffer
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, color_texture.gltexture(), 0);
			
		// Make a depth buffer
		this.depth_texture = new SimpleTexture(); // The texture is bound upon creation, no need to call bind().
			// Empty texture data. Why float?
			SimpleTexture.texImage2D(GL_TEXTURE_2D, width, height, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
			// Attach the depth buffer to the framebuffer
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depth_texture.gltexture(), 0);
			
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE)
			Log.send("Framebuffer completed!");
			else Log.send("Failure.");
		
	}
	
	
	public GLTexture getTexture() { return color_texture; }
	
	
	public void bind() { glBindFramebuffer(GL_FRAMEBUFFER, fbo);
	 GL30.glViewport(0, 0, this.width(), this.height()); }
	
	public void free() { glDeleteFramebuffers(fbo);
		 color_texture.free();
		 depth_texture.free(); }
	
	public int width() { return width; }
	public int height() { return height; }
	
	/* -- Normalized Drawing & Input -- */
	
	/**
	 * Returns the normalized coordinates of a rectangle that will fit<br>
	 * inside of another rectangle based on the fill mode and alignment.<br><br>
	 *  
	 * For example, putting in the size of a window as the outer size, <br>
	 * the size of a framebuffer as the inner size, and setting the alignment<br>
	 * to 'middle' and the fill mode to 'contain' will return normalized coordinates<br>
	 * of a quad with the aspect ratio of the framebuffer letterboxed within a<br>
	 * rectangle with the aspect ratio of the window, aligned to the center when possible.
	 * 
	 * <br>
	 * 
	 * @param outer_width
	 * @param outer_height
	 * @param inner_width
	 * @param inner_height
	 * @param horizontal_align
	 * @param vertical_align
	 * @param fill_mode
	 * @return
	 */
	public Vector4f getFittedRectangle(int outer_width, int outer_height, int inner_width, int inner_height, Alignment horizontal_align, Alignment vertical_align, FillMode fill_mode) {
		
		if (fill_mode == FillMode.CONTAIN) {
			
			float temp_width = 0;
			float temp_height = 0;
			
			// y * (x / y) = x,    this multiply(y) = x
			float window_width_ratio = outer_width / (float) outer_height; 
			float frame_width_ratio = inner_width / (float) inner_height; 		
			// If this is less than 0, y is greater than x, 
			// and the amount less than x is how much greater it is.
			// Same for vice versa.
			
			// Perfect ratio
			if (frame_width_ratio == window_width_ratio) {
				temp_width = 1;
				temp_height = 1;
			}
	
			if  (frame_width_ratio > window_width_ratio)  {
			   // Same as the next case, but match width to window width
			   // and use a height proportion instead of a width one
				temp_width = 1;
				temp_height = window_width_ratio / frame_width_ratio;
	
			} else if (frame_width_ratio < window_width_ratio) {
				// If the framebuffer is taller than it is wide,
				// match its display height to the window's height,
				// and make its display width proportional to it's own width.
				// The width is also multiplied to be proportional to the window width,
				// because the coordinates don't have any concept of aspect ratio of the window.
				temp_height = 1;
				temp_width = frame_width_ratio * (1 / window_width_ratio);
			} else if ( inner_height == inner_width ) { // perfect square
	
				// Use the original code based entirely on window size
				if ( outer_width > outer_height) {
					temp_height = 1;
					temp_width = outer_height/((float)outer_width);
				} else if ( outer_height > outer_width ) {
					temp_width = 1;
					temp_height = outer_width/((float)outer_height);
				} else if ( outer_height == outer_width ) { // perfect square
					temp_width = 1;
					temp_height = 1;
				}
				
			}
			
			// Calculate horizontal alignment
				// Default to middle. Passing in 'top' or 'bottom' can be safely ignored this way.
				float x_offset = ((1.0f-temp_width)/2.0f); 
				
				if (horizontal_align == Alignment.LEFT  ) x_offset = 0;
				if (horizontal_align == Alignment.RIGHT ) x_offset = ((1.0f-temp_width));
			
			// Calculate vertical alignment
				// Default to middle. Passing in 'left' or 'right' can be safely ignored this way.
				float y_offset = ((1.0f-temp_height)/2.0f);
				
				if (vertical_align == Alignment.TOP     ) y_offset = ((1.0f-temp_height));
				if (vertical_align == Alignment.BOTTOM  ) y_offset = 0;
			
			// Calculate actual coordinates
			float left = (x_offset);
			float right = (x_offset+temp_width);
			
			float bottom = (y_offset);
			float top = (y_offset+temp_height);
			
			// Convert from (0 to 1) to (-1 to 1)
			left = (left * 2) - 1;
			right = (right * 2) - 1;
			bottom = (bottom * 2) - 1;
			top = (top * 2) - 1;
			
			return new Vector4f(left, top, right, bottom);
			
		}
		
		throw new Error(this.getClass().getName() + "Fill Mode not implemented yet: " + fill_mode.toString());
	}
	
	/**
	 * Returns a mesh that is a quad with the specified coordinates.
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	private SimpleMesh mesh(float left, float top, float right, float bottom, float z) {

		// Remind myself to look into this if this ever somehow comes up and becomes a memory leak or something.
		if (mesh_queue.size() > 255) {
			Log.send(this.getClass().getName() + ": Mesh cache is weirdly big. " + mesh_queue.size() + " cached meshes. Clearing queue.");
			for (String key : mesh_queue.keySet()) {
				mesh_queue.get(key).free();
			}
			mesh_queue.clear();
		}
		
		// Check queue for meshes matching these coordinates
		String name = left+":"+top+":"+right+":"+bottom+":"+z;
		SimpleMesh existing = mesh_queue.get(name);
			if (existing != null) return existing;

		// Make mesh
		SimpleMesh mesh = new SimpleMesh(new float[] {
			     right,  top, z,  // top right
			     right, bottom, z,  // bottom right
			     left, bottom, z,  // bottom left
			     left,  top, z   // top left 
				},
				new float[] {
			     1f,  1f,  // top right
			     1f,  0f,  // bottom right
			     0f,  0f,  // bottom left
			     0f,  1f,  // top left 
				},
				new int[] {
				    0, 1, 3,   // first triangle
				    1, 2, 3    // second triangle
				});
		
		mesh_queue.put(name, mesh);
		
		return mesh;
	}


	/* - > -- Normalized Drawing: W/ Alignment&Fill mode -- < - */
	
	/**
	 * Draw a Framebuffer in 2D by specifying alignment and fill mode.<br>
	 * This method operates with normalized coordinates from -1 to 1, <br>
	 * and so will ignore the currently set world matrix.<br><br>
	 * 
	 * This method bypasses the render queue.
	  * @param width The width of the viewport to draw to (for the aspect ratio)
	 * @param height The height of the viewport to draw to (for the aspect ratio)
	 * @param horizontal_align
	 * @param vertical_align
	 * @param fill_mode
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public void drawDirect(int width, int height, Alignment horizontal_align, Alignment vertical_align, FillMode fill_mode) throws IOException, ParseException {
		drawDirect(width, height, horizontal_align, vertical_align, fill_mode, 0);
	}
	
	/**
	 * Draw a Framebuffer in 2D by specifying alignment and fill mode.<br>
	 * This method operates with normalized coordinates from -1 to 1, <br>
	 * and so will ignore the currently set world matrix.<br><br>
	 * 
	 * This method bypasses the render queue.
	  * @param width The width of the viewport to draw to (for the aspect ratio)
	 * @param height The height of the viewport to draw to (for the aspect ratio)
	 * @param horizontal_align
	 * @param vertical_align
	 * @param fill_mode
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public void drawDirect(int width, int height, Alignment horizontal_align, Alignment vertical_align, FillMode fill_mode, float z) throws IOException, ParseException {
		drawDirect(width, height, horizontal_align, vertical_align, fill_mode, "engine:assets/shaders/screen.snbt", z);
	}
	
	/**
	 * Draw a Framebuffer in 2D by specifying alignment and fill mode.<br>
	 * This method operates with normalized coordinates from -1 to 1, <br>
	 * and so will ignore the currently set world matrix.<br><br>
	 * 
	 * This method bypasses the render queue.
	 * 
	 * @param width The width of the viewport to draw to (for the aspect ratio)
	 * @param height The height of the viewport to draw to (for the aspect ratio)
	 * @param horizontal_align
	 * @param vertical_align
	 * @param fill_mode
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public void drawDirect(int width, int height, Alignment horizontal_align, Alignment vertical_align, FillMode fill_mode, String shader, float z) throws IOException, ParseException {
		Vector4f normalized_rect = getFittedRectangle(width, height, this.width, this.height, horizontal_align, vertical_align, fill_mode);
		drawDirect(normalized_rect.x, normalized_rect.y, normalized_rect.z, normalized_rect.w, shader, z);
	}

	/* - > -- Normalized Drawing: W/ Direct coordinates -- < - */

	/**
	 * Draw a Framebuffer in 2D by specifying coordinates.<br>
	 * This method operates with normalized coordinates from -1 to 1, <br>
	 * and so will ignore the currently set world matrix.<br><br>
	 * 
	 * This method bypasses the render queue.
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @throws IOException
	 * @throws ParseException
	 */
	public void drawDirect(float left, float top, float right, float bottom, float z) throws IOException, ParseException {
		drawDirect(left, top, right, bottom, "engine:assets/shaders/screen.snbt", z);
	}
	
	/**
	 * Draw a Framebuffer in 2D by specifying coordinates.<br>
	 * This method operates with normalized coordinates from -1 to 1, <br>
	 * and so will ignore the currently set world matrix.<br><br>
	 * 
	 * This method bypasses the render queue.
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param shader
	 * @throws IOException
	 * @throws ParseException
	 */
	public void drawDirect(float left, float top, float right, float bottom, String shader, float z) throws IOException, ParseException {
		Shaders.bind(shader);
		
		glActiveTexture(GL_TEXTURE0);									// Activate texture0
		glBindTexture(GL_TEXTURE_2D,  this.color_texture.gltexture());	// Bind the texture t to texture0
		
		SimpleMesh mesh = mesh(left, top, right, bottom, z);
		
		glBindVertexArray(mesh.vao());
		glDrawElements(GL11.GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}
	
	
	
	/* -- Normalized Input -- */
	
	/**
	 * Friendlier name for getUnitMouseCoordinates.<br><br>
	 * 
	 * Convert window mouse coordinates to relative coordinates,<br>
	 * expressed in units matching the Camera passed in,<br>
	 * and relative to a hypothetical Framebuffer drawn with the given alignment settings.
	 * @param window
	 * @param horizontal_align
	 * @param vertical_align
	 * @param fill_mode
	 * @param units
	 * @return
	 */
	public Vector2d getMouse(F3DWindow window, Vector2d mouse_coordinates,  Alignment horizontal_align, Alignment vertical_align, FillMode fill_mode, Camera units) {
		return getUnitMouseCoordinates(window, mouse_coordinates, horizontal_align, vertical_align, fill_mode, units);
	}
	
	/**
	 * Convert window mouse coordinates to relative coordinates,<br>
	 * expressed in units matching the Camera passed in,<br>
	 * and relative to a hypothetical Framebuffer drawn with the given alignment settings.
	 * @param window
	 * @param horizontal_align
	 * @param vertical_align
	 * @param fill_mode
	 * @param units
	 * @return
	 */
	public Vector2d getUnitMouseCoordinates(F3DWindow window, Vector2d mouse_coordinates, Alignment horizontal_align, Alignment vertical_align, FillMode fill_mode, Camera units) {
		// Convert mouse position to Vector2d
		Vector2d original_normalized_coordinates = new Vector2d(mouse_coordinates.x()/window.width(), mouse_coordinates.y()/window.height());
		
		// Call the non- alignment/fill mode variation of this method
		Vector4f normalized_rect = getFittedRectangle(window.width(), window.height(), this.width, this.height, horizontal_align, vertical_align, fill_mode);
		return getUnitMouseCoordinates(normalized_rect.x, normalized_rect.y, normalized_rect.z, normalized_rect.w, original_normalized_coordinates, units);
	}
	
	private Vector2d getUnitMouseCoordinates(float left, float top, float right, float bottom, Vector2d original_normalized_coordinates, Camera units) {
		Vector2d new_normalized_coordinates = getNormalizedInternalMouseCoordinates(left, top, right, bottom, original_normalized_coordinates);
		new_normalized_coordinates.mul(new Vector2d(units.width, units.height));
		new_normalized_coordinates.sub(new Vector2d(units.horizontal_offset, units.vertical_offset));
		return new_normalized_coordinates;
	}
	
	/**
	 * NOTE: The input and output are 0 to 1.
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param original_normalized_coordinates
	 * @return
	 */
	private Vector2d getNormalizedInternalMouseCoordinates(float left, float top, float right, float bottom, Vector2d original_normalized_coordinates) {
		
		// Coordinates are provided scaled from 0 to 1.
		double mx = original_normalized_coordinates.x();
		double my = original_normalized_coordinates.y();
	
		// X axis
		left  = (left  + 1) / 2; 	// scale from range -1 to 1 to range 0 to 1
		right = (right + 1) / 2;	// scale from range -1 to 1 to range 0 to 1
		
		mx 	  = mx    - left; 		// Subtract it so that when mx is at (left), it's at 0
		right = right - left; 		// Subtract so that 'right' is consistent with that ^
		
		mx 	 /= right; 				// Divide so that when mx is at (right), it's at 1. (right/right = 1)
		
		// mx is now in a range from 0 to 1 in relation to 'left' and 'right'.
		
		// Same thing, but for the Y axis
		top    = (top    + 1) / 2;
		bottom = (bottom + 1) / 2;
		
		my 	  -= bottom; 
		top   -= bottom;
		
		my 	  /= top;

		return new Vector2d(mx, my);
	}

}

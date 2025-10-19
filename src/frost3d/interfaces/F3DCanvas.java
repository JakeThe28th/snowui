package frost3d.interfaces;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import frost3d.utility.Rectangle;

public interface F3DCanvas {
	
	public void textrenderer(F3DTextRenderer v);

	public void framebuffer	(int v);
	public void width		(int v);
	public void height		(int v);
	
	// -- ++ STATE ++ -- //
	
	/** Saves the current scissor and then sets the new scissor to this. */
	public void push_scissor(Rectangle box);
	
	/** Saves the current scissor and then sets the new scissor to this. */
	public void push_scissor(int left, int top, int right, int bottom);

	/** Reverts to the previous scissor box. */
	public void pop_scissor();
	
	/** Sets the current color to this */
	public void color(Vector4f color);
	
	// -- ++ DRAWING ++ -- //
	
	public void rect(int left, int top, int right, int bottom, int depth);
	
	public void rect(int left, int top, int right, int bottom, int depth, GLTexture texture);
	
	public void text(int x, int y, int depth, String text);
	
	// -- ++  ...  ++ -- //

	/** Finalizes all of the provided draw commands,
	 *  and renders them to the framebuffer. */
	public void draw_frame();

	public void queue(GLMesh mesh, Matrix4f transform, GLTexture texture);

}

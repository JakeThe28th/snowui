package frost3d.implementations;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.util.Stack;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import averificare.DEMO_GUI0;
import frost3d.RenderQueue;
import frost3d.Shapes;
import frost3d.interfaces.F3DCanvas;
import frost3d.interfaces.F3DTextRenderer;
import frost3d.interfaces.GLMesh;
import frost3d.interfaces.GLTexture;
import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.api.GUITextRenderer;

public class SimpleCanvas implements F3DCanvas {
	//turn this into generic canvas and make gui use ICanvas an interface
	
		// -- ++ (  effectively final state,  ) ++ -- //

		private RenderQueue renderqueue = new RenderQueue();
		
		// -- ++ ( infrequently changed state ) ++ -- //
		
		F3DTextRenderer textrenderer;
		
		int framebuffer 	= -1;
		int width 			= -1;
		int height 			= -1;

		private Rectangle current_scissor;
		
		public void textrenderer(F3DTextRenderer v) { this.textrenderer = v; }

		public void framebuffer	(int v) { this.framebuffer 	= v; }
		public void width		(int v) { this.width 		= v; }
		public void height		(int v) { this.height 		= v; }
		
		// -- ++ (  frequently changed state  ) ++ -- //

		static Stack<Rectangle> scissor_stack = new Stack<Rectangle>();
		
		/** Saves the current scissor and then sets the new scissor to this. */
		public void push_scissor(Rectangle box) {
			scissor_stack.push(current_scissor);
			current_scissor = box;
		}
		
		/** Reverts to the previous scissor box. */
		public void pop_scissor() {
			current_scissor = scissor_stack.pop();
		}
		
		public Rectangle scissor() {
			if (current_scissor == null) return new Rectangle(0, 0, width, height);
			return current_scissor;
		}
		
		public void push_scissor(int left, int top, int right, int bottom) {
			// TODO
		}
		
		Vector4f color = new Vector4f();
		
		public void color(Vector4f color) {
			// TODO
			this.color = color;
		}
		

		public void world_transform(Matrix4f mat) {
			this.renderqueue.world_transform(mat);
		}
		
		// -- **  ** -- //

		public void draw_frame() {
			
			// clear the framebuffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			renderqueue.render();
						
		}
		
		// -- ** Drawing API ** -- //

		public void rect(int left, int top, int right, int bottom, int depth) {
			// TODO
			Shapes.rect(this, left, top, right, bottom, depth);
		}
		
		public void text(int x, int y, int depth, String text) {
			// TODO
			textrenderer.text(this, x, y, depth, text);
		}
		

		@Override
		public void queue(GLMesh mesh, Matrix4f transform, GLTexture texture) {
			// TODO Auto-generated method stub
			// default
			renderqueue.mix_color(color);
			
			// specific
			renderqueue.mesh(mesh);
			renderqueue.transform(transform);
			renderqueue.texture(texture);
			renderqueue.queue();
		}

}

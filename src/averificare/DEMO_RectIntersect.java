package averificare;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.implementations.SimpleWindow;
import frost3d.utility.Rectangle;
import snowui.coss.enums.Color;

public class DEMO_RectIntersect {
	
	public static void main(String[] args) {	
		
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, "GUI Test");
		BuiltinShaders.init();
		
		SimpleCanvas canvas = new SimpleCanvas();
			canvas.size(window.width, window.height);
			canvas.textrenderer(new SimpleTextRenderer());
			
		Rectangle recta = new Rectangle(20, 20, 120, 70);
		Rectangle rectb = new Rectangle(90, 90, 300, 370);

		boolean recta_drag = false;
		boolean rectb_drag = false;
		
		int click_x = 0;
		int click_y = 0;
		
		while (!window.should_close()) {
		
		
					
			
			if (window.input().mouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				
				click_x = window.input().mouseX();
				click_y = window.input().mouseY();

				if (recta.contains(new Vector2i(window.input().mouseX(), window.input().mouseY()))) {
					recta_drag = true;
				}
				if (rectb.contains(new Vector2i(window.input().mouseX(), window.input().mouseY()))) {
					recta_drag = false;
					rectb_drag = true;
				}
				
			}
			
			int offset_x = window.input().mouseX() - click_x;
			int offset_y = window.input().mouseY() - click_y;
			
			if (!window.input().mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				if (recta_drag) recta = recta.offset(offset_x, offset_y);
				if (rectb_drag) rectb = rectb.offset(offset_x, offset_y);
				recta_drag = false;
				rectb_drag = false;
			}

			Rectangle rectaQ = recta;
			Rectangle rectbQ = rectb;
			if (recta_drag) rectaQ = recta.offset(offset_x, offset_y);
			if (rectb_drag) rectbQ = rectb.offset(offset_x, offset_y);

			canvas.color(Color.RED.val());
			canvas.rect(rectaQ, 1);
			
			canvas.color(Color.BLUE.val());
			canvas.rect(rectbQ, 1);
			
			if (rectaQ.intersects(rectbQ)) {
				canvas.text(10, 10, 100, "TRUE!!");
			}
			
			canvas.draw_frame();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}
	
}

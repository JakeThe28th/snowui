package averificare;

import org.joml.Matrix4f;
import frost3d.GLState;
import frost3d.Shaders;
import frost3d.implementations.SimpleCanvas;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;

public class DEMO_GUI {


	public static void main(String[] args) {

		GLState.initializeGLFW();
		
		// Create window ...
		SimpleWindow window = new SimpleWindow(300, 600, "GUI Test") {
			@Override public void onWindowResize() {
				canvas.world_transform(new Matrix4f().ortho(0, width(), height(), 0, -1024f, 1024f));
			}
		};
		
		// Create core shaders (needs to be done after window cuz context)
		Shaders.init();
		
		GUIInstance gui = new GUIInstance(window);
		
		while (!window.should_close()) {
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
		
	}
}

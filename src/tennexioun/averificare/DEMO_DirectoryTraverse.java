package tennexioun.averificare;

import java.io.File;
import java.io.IOException;
import frost3d.GLState;
import frost3d.data.BuiltinShaders;
import frost3d.implementations.SimpleWindow;
import snowui.GUIInstance;
import tennexioun.TXGUIDirectory;
import tennexioun.TXGUIFileDirectory;

public class DEMO_DirectoryTraverse {

	public static void main(String[] args) throws IOException {
		
		GLState.initializeGLFW();
		SimpleWindow window = new SimpleWindow(4*300, 3*300, DEMO_DirectoryTraverse.class.getCanonicalName());
		BuiltinShaders.init();
		
		GUIInstance gui = new GUIInstance(window, window.input());

		TXGUIDirectory directory = new TXGUIFileDirectory(new File("C:\\Users\\sumi\\Desktop\\homestuck"));
		
		gui.root(directory);
		
		// gui.style().setProperty("default", "list_spacing", "10");
		
		while (!window.should_close()) {
			gui.size(window.width, window.height);
			gui.render();
			window.tick();
		}
		
		window.end();

		GLState.endGLFW();
	}

}

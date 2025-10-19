package frost3d.implementations;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import frost3d.Input;
import frost3d.interfaces.F3DWindow;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class SimpleWindow implements F3DWindow {
	
	Input input;
	public Input input() { return input; }
	
	private long window;
	public int width;
	public int height;
	
	public int width() { return width; }
	public int height() { return height; }

	public boolean should_close() {
		return glfwWindowShouldClose(window);
	}
	
	public void tick() {
		glfwSwapBuffers(window); // swap the color buffers

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}

	public SimpleWindow(int w, int h, String title) {
		
		width = w;
		height = h;
		
		onWindowResize();
		
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(w, h, title, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
//		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
//				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//		});
		
		glfwSetWindowSizeCallback(window, (_, width, height) -> {
			this.width = width;
			this.height = height;
			viewport();
			onWindowResize();
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();
		
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		
		// Set the clear color
		glClearColor(0.20f, 0.0f, 0.0f, 0.0f);

		input = new Input(identifier());
	}
	
	public void end() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
	}

	public int framebuffer() {
		// TODO Auto-generated method stub <-- actually the default is what i wanted :)
		return 0;
	}
	
	public void viewport() {
		glViewport(0,0, width, height);
	}
	
	@Override public long identifier() { return window; }
	@Override public void bind() {
		// TODO Auto-generated method stub
	}
	
	// ... //

	public void onWindowResize() { }

}

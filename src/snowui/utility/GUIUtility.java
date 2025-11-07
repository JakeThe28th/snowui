package snowui.utility;

import java.util.ArrayList;

import snowui.elements.GUIElement;

public class GUIUtility {

	public static int combined_height(ArrayList<GUIElement> elements) {
		int height = 0;
		for (GUIElement e : elements) height += e.height();
		return height;
	}
	
	public static int combined_width(ArrayList<GUIElement> elements) {
		int width = 0;
		for (GUIElement e : elements) width += e.width();
		return width;
	}
	
	public static int max_height(ArrayList<GUIElement> elements) {
		int height = 0;
		for (GUIElement e : elements) if (e.height() > height) height = e.height();
		return height;
	}

	public static int max_width(ArrayList<GUIElement> elements) {
		int width = 0;
		for (GUIElement e : elements) if (e.width() > width) width = e.width();
		return width;
	}

}

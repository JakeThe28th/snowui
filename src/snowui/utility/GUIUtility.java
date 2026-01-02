package snowui.utility;

import java.util.ArrayList;

import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;

public class GUIUtility {
	
	public static GUIElement getHoveredElement(GUIElement e) {
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.BOUNDED)) return getHoveredElement(sub);
		}
		return e;
	}
	
	public static ArrayList<GUIElement> getHoveredElementTree(GUIElement e) {
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.BOUNDED)) {
				ArrayList<GUIElement> list = getHoveredElementTree(sub);
				list.add(e);
				return list;
			}
		}
		ArrayList<GUIElement> list = new ArrayList<GUIElement>();
		list.add(e);
		return list;
	}


	// -- ++  ...  ++ -- //

	public static int combined_height(ArrayList<? extends GUIElement> elements) {
		int height = 0;
		for (GUIElement e : elements) height += e.height();
		return height;
	}
	
	public static int combined_width(ArrayList<? extends GUIElement> elements) {
		int width = 0;
		for (GUIElement e : elements) width += e.width();
		return width;
	}
	
	public static int max_height(ArrayList<? extends GUIElement> elements) {
		int height = 0;
		for (GUIElement e : elements) if (e.height() > height) height = e.height();
		return height;
	}

	public static int max_width(ArrayList<? extends GUIElement> elements) {
		int width = 0;
		for (GUIElement e : elements) if (e.width() > width) width = e.width();
		return width;
	}

	public static int wrapped_height(ArrayList<? extends GUIElement> elements, int spacing, int width) {
		int result_height = 0;
		
		int xx = 0;
		int max_height = 0;

		for (GUIElement element : elements) {
			if ((xx + element.width()) > width) {
				xx = 0;
				result_height += max_height;
				max_height = 0;
			}
			if (element.height() > max_height) max_height = element.height();
			xx += element.width();
			xx += spacing;
		}
		result_height += max_height;
		
		return result_height;
	}
	

	public static int wrapped_width(ArrayList<? extends GUIElement> elements, int spacing, int height) {
		// TODO
		return 37;
	}

	// -- -- -- //

	public static ArrayList<GUIElement> array(GUIElement... elements) {
		ArrayList<GUIElement> arr = new ArrayList<GUIElement>();
		for (GUIElement e : elements) arr.add(e);
		return arr;
	}
	
}

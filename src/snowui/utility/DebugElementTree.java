package snowui.utility;

import java.util.ArrayList;

import frost3d.interfaces.F3DCanvas;
import frost3d.utility.Log;
import snowui.coss.enums.PredicateKey;
import snowui.elements.GUIElement;

public class DebugElementTree {
	
	public static void drawTree(F3DCanvas canvas, GUIElement e) {
		
		// Hover tree
		
		ArrayList<String> elements = new ArrayList<String>();
		add(elements, e);
		
		DrawUtility.drawStrings(canvas, canvas.width()-5, canvas.height()-5, 1000, elements);
		
		// State
		
		ArrayList<String> state = new ArrayList<String>();
		GUIElement hovered = getHoveredElement(e);
		for (PredicateKey key : hovered.state().keySet()) {
			state.add(key.toString() + " = " + hovered.state().get(key));
		}
		DrawUtility.drawStrings(canvas, 5, canvas.height()-5, 1000, state);
		
	}

	private static void add(ArrayList<String> elements, GUIElement e) {
		elements.add(e.getClass().getSimpleName() + ": " + e.listStyles());
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.HOVERED)) add(elements, sub);
		}
	}
	
	private static GUIElement getHoveredElement(GUIElement e) {
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.HOVERED)) return getHoveredElement(sub);
		}
		return e;
	}


}

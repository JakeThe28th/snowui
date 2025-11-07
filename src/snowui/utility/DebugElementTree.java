package snowui.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.joml.Vector4f;

import frost3d.interfaces.F3DCanvas;
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
		
		// Update time
		
		NumberFormat f = DecimalFormat.getInstance();
		f.setMinimumIntegerDigits(3);
		f.setMinimumFractionDigits(3);
		state.add("§`Last updated: " + f.format(hovered.last_update_elapsed_time()/1000f) + " seconds ago");
		state.add("§`Last draw update: " + f.format(hovered.last_draw_update_elapsed_time()/1000f) + " seconds ago");
		state.add("§`Last state update: " + f.format(hovered.last_state_update_elapsed_time()/1000f) + " seconds ago");

		// Draw that ^^^ debug info
		
		DrawUtility.drawStrings(canvas, 5, canvas.height()-5, 1000, state);
		
		// Draw a red flashing rectangle over the currently hovered element 
		
		if (hovered.state().get(PredicateKey.HOVERED)) {
			canvas.color(new Vector4f(1, 0, 0, flash_opacity()));
			canvas.rect(hovered.hover_rectangle(), 1000);
		}
		
	}

	private static float flash_opacity() {
		return (float) (Math.cos((System.currentTimeMillis() % 100000) / 200f) + 1) / 3;
	}

	private static void add(ArrayList<String> elements, GUIElement e) {
		elements.add(e.getClass().getSimpleName() + ": " + e.listStyles());
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.BOUNDED)) add(elements, sub);
		}
	}
	
	private static GUIElement getHoveredElement(GUIElement e) {
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.BOUNDED)) return getHoveredElement(sub);
		}
		return e;
	}


}

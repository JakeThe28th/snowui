package snowui.elements.interfaces;

import snowui.GUIInstance;
import snowui.elements.GUIElement;

/** An element which a Draggable element may be placed into. */
public interface Droppable {
	
	public boolean	canDropHere			  (GUIInstance gui, GUIElement element);
	public void 	drop				  (GUIInstance gui, GUIElement element);
	public void 	dropPreview			  (GUIInstance gui, GUIElement element);
	public void 	DEBUG_draw_drop_areas (GUIInstance gui, GUIElement element, int depth);

}

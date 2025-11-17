package snowui.support;

import org.joml.Vector2i;

import snowui.GUIInstance;
import snowui.elements.GUIElement;
import snowui.elements.interfaces.ElementReceiver;

public class DragAndDropSupport {
	
	public static final int DRAG_THRESHOLD = 5;
	
	private GUIInstance g;
	public DragAndDropSupport(GUIInstance gui) { this.g = gui; }

	public GUIElement 	drag_target;
	public Vector2i 	drag_start;
	public void 		drag_target(GUIElement element) { drag_target = element; }
	public void 		set_drag_start() 				{ drag_start  = new Vector2i(g.mx(), g.my()); }
	
	public GUIElement	held = null;
	private GUIElement held_clone;
	
	public boolean should_drag() {
		if (drag_start == null) return false;
		return (
				Math.absExact(drag_start.x - g.mx()) > DRAG_THRESHOLD || 
				Math.absExact(drag_start.y - g.my()) > DRAG_THRESHOLD
			   );
	}
	
	public void tick() {
		
		if (g.primary_click_released()) { drag_target(null); }
		
		// Grab
		if (held == null && drag_target != null && drag_target.draggable() && should_drag()) {
			held = drag_target;
			held_clone = (GUIElement) held.clone();
		}
		
		// Set down
		if (held == null) return;
		
		GUIElement.tickFloating(g, held_clone, g.mx(), g.my(), 1000);
		
		if (g.current_hovered_element() instanceof ElementReceiver) {
			ElementReceiver target =  ((ElementReceiver) g.current_hovered_element());
			if (target.canDropHere(g, held)) {
				target.dropPreview(g, held);
				if (g.primary_click_released()) {
					if (held.parent() instanceof ElementReceiver) {
						((ElementReceiver) held.parent()).remove(held);
					}
					target.drop(g, held);
					held = null;
				}
			} else {
				if (g.primary_click_released()) { 
					held = null;
					held_clone = null;
				}
			}
		} else {
			if (g.primary_click_released()) { 
				held = null;
				held_clone = null;
			}
		}
		
	}

}

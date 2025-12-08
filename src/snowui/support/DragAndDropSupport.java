package snowui.support;

import org.joml.Vector2i;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
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
	
	public boolean should_drag() {
		if (drag_start == null) return false;
		return (
				Math.absExact(drag_start.x - g.mx()) > DRAG_THRESHOLD || 
				Math.absExact(drag_start.y - g.my()) > DRAG_THRESHOLD
			   );
	}
	
	public void tick() {
		
		if (g.primary_click_pressed()) { 
			set_drag_start(); 
			drag_target(find_drag_target(g.current_window_root())); 
		}
		if (g.primary_click_released()) { drag_target(null); }
		
		// Grab
		if (held == null && drag_target != null && drag_target.draggable() && should_drag()) {
			held = drag_target;
		}
		
		// Set down
		if (held == null) return;
		
		// (...probably not ideal, but making a copy isn't practical,
		//     and this doesn't seem to affect performance *too* much, so...)
		Rectangle limit = held.limit_rectangle();
		GUIElement.tickFloating(g, held, g.mx(), g.my(), 600);
		held.limit_rectangle(limit);
		
		if (g.current_hovered_element() instanceof ElementReceiver) {
			ElementReceiver target =  ((ElementReceiver) g.current_hovered_element());
			if (target.canDropHere(g, held) && held.can_drop(g)) {
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
				}
			}
		} else {
			if (g.primary_click_released()) { 
				held = null;
			}
		}
		
	}
	
	public GUIElement find_drag_target(GUIElement e) {
		GUIElement found = null;
		if (e.get(PredicateKey.BOUNDED) && e.is_on_screen()) {
			if (e.draggable() && e.drag_rectangle().contains(g.mouspos())) {
				found = e;
			}	
			for (GUIElement sub_e : e.sub_elements()) {
				GUIElement se = find_drag_target(sub_e);
				if (se != null) found = se;
			}
		}
		return found;
	}

}

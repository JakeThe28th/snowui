package snowui.elements.docking;

import frost3d.enums.CursorType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.SubElementReplaceable;
import snowui.elements.interfaces.components.SubElementReplaceQueue;

public class GUISplit extends GUIElement implements SubElementReplaceable {
	
	{ identifier("split_view"); }

	public GUISplit(GUIElement first, GUIElement second) {
		first(first);
		second(second);
	}

	public GUISplit() {

	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		unpadded_height = 5;
		unpadded_width = 5;
		
		if (first != null) {
			unpadded_height = first.height();
			unpadded_width = first.width();
		}
		
		if (second != null) {
			if (unpadded_height < second.height()) unpadded_height = second.height();
			if (unpadded_width < second.width()) unpadded_width = second.width();
		}
	}
	
	GUIElement first;
	GUIElement second;
	
	float split = 0.5f;
	boolean vertical = false;
	
	public GUISplit verticalify() {
		if (!vertical) {
			this.should_update(true);
		}
		vertical = true;
		return this;
	}
	
	public GUISplit horizontalify() {
		if (vertical) {
			this.should_update(true);
		}
		vertical = false;
		return this;
	}
	
	public GUISplit first(GUIElement e) {
		if (first != null) this.removeSubElement(first);
		first = e;
		if (e == null) return this;
		this.registerSubElement(e);
		return this;
	}
	
	public GUIElement first() { return first; }

	public GUISplit second(GUIElement e) {
		if (second != null) this.removeSubElement(second);
		second = e;
		if (e == null) return this;
		this.registerSubElement(e);
		return this;
	}
	
	public GUIElement second() { return second; }
	
	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = limit_rectangle();
		this.hover_rectangle(b);
		
		if ((first == null || second == null) && (first != null || second != null)) { 
			if (first  != null) first.limit_rectangle(b);
			if (second != null) second.limit_rectangle(b);
		} else {
			if (vertical) {
				if (first  != null) first.limit_rectangle(b.internal(0, 0, 1, split));
				if (second != null) second.limit_rectangle(b.internal(0, split, 1, 1));
			} else {
				if (first  != null) first.limit_rectangle(b.internal(0, 0, split, 1));
				if (second != null) second.limit_rectangle(b.internal(split, 0, 1, 1));
			}
		}

		if (first  != null) first.scissor_rectangle_recursive(first.limit_rectangle());
		if (second != null) second.scissor_rectangle_recursive(second.limit_rectangle());
	}
	
	@Override
	public void onScissorChange() {
		if (first  != null) first.scissor_rectangle_recursive(first.limit_rectangle());
		if (second != null) second.scissor_rectangle_recursive(second.limit_rectangle());
	}

	@Override public void draw(GUIInstance gui, int depth) { }

	boolean can_drag = false;
	boolean dragging = false;
	
	@Override
	public boolean preUpdateState(GUIInstance gui) {
		Rectangle b = limit_rectangle();
		if (b == null) return false;
		
		int split_radius = 10;
		Rectangle split_rectangle;
		
		if (vertical) {
			int split_y = (int) (b.top() + (b.height() * split));
			split_rectangle = new Rectangle(b.left(), split_y - split_radius, b.right(), split_y + split_radius);
		} else {
			int split_x = (int) (b.left() + (b.width() * split));
			split_rectangle = new Rectangle(split_x - split_radius, b.top(), split_x + split_radius, b.bottom());
		}
		
		if (split_rectangle .contains(gui.mousepos())) {
			can_drag = true;
		} else {
			can_drag = false;
		}
		
		return can_drag || dragging;
	}
	
	@Override
	public void replace(GUIElement original, GUIElement replacement) {
		if (first == original) first(replacement);
		if (second == original) second(replacement);
		this.should_update(true);
	}

	SubElementReplaceQueue replace_queue = new SubElementReplaceQueue();
	@Override public SubElementReplaceQueue queue() { return replace_queue; }
	
	@Override
	public void tickAnimation(GUIInstance gui) {
		if (		   !gui.primary_click_down()) 		dragging = false;
		if (can_drag && gui.primary_click_pressed()) 	dragging = true;
		if (can_drag || dragging) if ( vertical) 		setcursor(gui, CursorType.RESIZE_NS_CURSOR);
		if (can_drag || dragging) if (!vertical) 		setcursor(gui, CursorType.RESIZE_EW_CURSOR);
		if (			dragging) 						set_amount_to_mouse(gui);
		collapse();
		replace_queue.dequeue(this);
	}
	
	private void setcursor(GUIInstance gui, CursorType cursor) {
		if (gui.cursor() != CursorType.ARROW_CURSOR) {
			gui.cursor(CursorType.RESIZE_NWSE_CURSOR);
		} else {
			gui.cursor(cursor);
		}
	}

	void set_amount_to_mouse(GUIInstance gui) {
		if (vertical) {
			split = (gui.my()-limit_rectangle().top()) / (float) limit_rectangle().height();
		} else {
			split = (gui.mx()-limit_rectangle().left()) / (float) limit_rectangle().width();
		}
		this.should_update(true);
	}

	public float split() {
		return split;
	}
	
	public void split(float nsplit) {
		split = nsplit;
		this.should_update(true);
	}
	
	// -= : Collapsing : =- //
	
	void collapse() {
		// When one side is null, GUISplit[s] display as the non-null side. 
		// If this side is another GUISplit, it's effectively the same as
		// if the super element was the sub-element. So, the sub-element's
		// contents are collapsed into the super-element this way.
		     if (first  == null && second instanceof GUISplit) become((GUISplit) second);
		else if (second == null && first  instanceof GUISplit) become((GUISplit) first);
		// Additionally, if both are null, nothing can be rendered, so
	 	// it's similar to as if the GUISplit itself were null.
		     if (first == null && second == null) {
		    	 if (parent() instanceof SubElementReplaceable) {
		    		 ((SubElementReplaceable) parent()).queue().queue_replace(this, null);
		    	 }
		     }
		// This prevents infinite GUISplit[s] from being created and maintained
	    // after dragging GUIDockable[s] onto each-other.
	}
	
	void become(GUISplit other) {
		first(other.first);
		second(other.second);
		this.vertical = other.vertical;
		this.split = other.split;
		this.should_update(true);
	}
	
}

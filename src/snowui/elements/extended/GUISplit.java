package snowui.elements.extended;

import frost3d.enums.CursorType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.SubElementReplaceable;
import snowui.elements.interfaces.components.SubElementReplaceQueue;

public class GUISplit extends GUIElement implements SubElementReplaceable {

	public GUISplit(GUIElement first, GUIElement second) {
		first(first);
		second(second);
	}

	public GUISplit() {

	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 37;
		this.unpadded_width = 37;
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
		
		if (vertical) {
			first.limit_rectangle(b.internal(0, 0, 1, split));
			second.limit_rectangle(b.internal(0, split, 1, 1));
		} else {
			first.limit_rectangle(b.internal(0, 0, split, 1));
			second.limit_rectangle(b.internal(split, 0, 1, 1));
		}
		
		first.scissor_rectangle_recursive(first.limit_rectangle());
		second.scissor_rectangle_recursive(second.limit_rectangle());
	}

	@Override public void draw(GUIInstance gui, int depth) { }

	boolean can_drag = false;
	boolean dragging = false;
	
	@Override
	public boolean preUpdateState(GUIInstance gui) {
		Rectangle b = limit_rectangle();
		
		int split_radius = 10;
		Rectangle split_rectangle;
		
		if (vertical) {
			int split_y = (int) (b.top() + (b.height() * split));
			split_rectangle = new Rectangle(b.left(), split_y - split_radius, b.right(), split_y + split_radius);
		} else {
			int split_x = (int) (b.left() + (b.width() * split));
			split_rectangle = new Rectangle(split_x - split_radius, b.top(), split_x + split_radius, b.bottom());
		}
		
		if (split_rectangle .contains(gui.mouspos())) {
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
	
}

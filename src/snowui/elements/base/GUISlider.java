package snowui.elements.base;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;

//TODO: Vertical version
public class GUISlider extends GUIElement {

	class GUISliderHandle extends GUIElement {

		{ identifier("slider_handle"); }
		
		int x, y, size;

		@Override public void recalculateSize(GUIInstance gui) { unpadded_height = 53; unpadded_width = 59; }
		@Override public void updateDrawInfo (GUIInstance gui) { update_handle_position(); }
		
		public void update_handle_position() {
			size = style().size().pixels();
			x = (int) (bar.left() + (bar.width() * amount));
			y = bar.center().y;
			int ss = size/2;
			this.hover_rectangle(new Rectangle(x-ss, y-ss, x+ss, y+ss));
		}

		@Override
		public void draw(GUIInstance gui, int depth) {
			gui.canvas().color(style().base_color().color());
			gui.canvas().rect(bar.left(), bar.top(), x, bar.bottom(), depth);
			gui.canvas().color(style().base_color().color());
			gui.canvas().dot(x, y, depth, size/2);
		}
		
		@Override public void onPress(GUIInstance gui) { dragging = true; }
		
	}
	
	{ identifier("slider"); }

	// N/A
	@Override public void recalculateSize(GUIInstance gui) { unpadded_height = 53; unpadded_width = 59; }
	
	float amount = 0.5f;
	
	GUISliderHandle handle = new GUISliderHandle();
	
	{ this.registerSubElement(handle); }

	Rectangle bar;
	
	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = padded_limit_rectangle();
		
		int bar_height 		= 		 style().size().pixels();
		int handle_height 	= handle.style().size().pixels();

		int yy = b.center().y;
		
		bar = 			 new Rectangle(b.left(), yy-(bar_height   /2), b.right(), yy+(bar_height   /2))  ;
		hover_rectangle( new Rectangle(b.left(), yy-(handle_height/2), b.right(), yy+(handle_height/2)) );

		handle.limit_rectangle(b);
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(bar, depth);
	}
	
	@Override public void onPress(GUIInstance gui) { dragging = true; }
	
	boolean dragging = false;
	
	void set_amount_to_mouse(GUIInstance gui) {
		amount = (gui.mx()-bar.left()) / (float) bar.width();
		onChange(amount);
		handle.update_handle_position();
	}
	
	@Override
	public void tickAnimation(GUIInstance gui) {
		if (!gui.primary_click_down()) dragging = false;
		if (dragging) {
			set_amount_to_mouse(gui);
		}
	}
	
	public void onChange(float new_amount) {}
	
}

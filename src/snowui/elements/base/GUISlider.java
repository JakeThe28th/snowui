package snowui.elements.base;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;

//TODO: Vertical version
public class GUISlider extends GUIElement {

	public class GUISliderHandle extends GUIElement {

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
			gui.canvas().dot(x, y, depth, size/2);
		}
		
		@Override public void onPress(GUIInstance gui) { dragging = true; }
		
	}
	
	{ identifier("slider"); }

	public GUISlider() { }
	
	public GUISlider(float percent) {
		this.amount = percent;
	}

	// N/A
	@Override public void recalculateSize(GUIInstance gui) { unpadded_height = style().size().pixels(); unpadded_width = 3; }
	
	protected float amount = 0.5f;
	
	protected boolean display_amount_on_hover = false;
	
	protected GUISliderHandle handle = new GUISliderHandle();
	
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
		
		if (display_amount_on_hover && (dragging || get(PredicateKey.BOUNDED))) {
			gui.canvas().color(Color.BLACK75.val());
			int cx = bar.center().x;
			int cy = bar.center().y;
			String text = amountFormatted();
			gui.textrenderer().font_size(18);
			int width = gui.textrenderer().size(text).x;
			int height = gui.textrenderer().size(text).y;
			int m = 5;
			Rectangle display_rect = new Rectangle(cx - ((width+m+m)/2), cy - (height+m+m), cx + ((width+m+m)/2), cy);
			gui.canvas().push_scissor(display_rect);
			gui.canvas().rect(display_rect, depth+10);
			gui.canvas().color(Color.WHITE.val());
			gui.canvas().text(display_rect.left()+m, display_rect.top()+m, depth+20, text);
			gui.canvas().pop_scissor();
		}
	}
	
	protected String amountFormatted() {
		return String.format("%.2f", amount);
	}
	
	@Override public void onPress(GUIInstance gui) { dragging = true; }
	
	boolean dragging = false;
	
	void set_amount_to_mouse(GUIInstance gui) {
		amount = (gui.mx()-bar.left()) / (float) bar.width();
		if (amount < 0) amount = 0;
		if (amount > 1) amount = 1;
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

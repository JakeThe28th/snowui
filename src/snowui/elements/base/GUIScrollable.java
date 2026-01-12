package snowui.elements.base;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Constant;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;

public class GUIScrollable extends GUIElement {
	
	private GUIScrollable scroll_this() { return this; }
	
	{ identifier("scroll_area"); }
	
	private class GUIScrollBar extends GUIElement {
		
		GUIClickableRectangle scroll_handle = new GUIClickableRectangle() {
			{ identifier("scroll_handle"); }
			@Override public void onPress(GUIInstance gui) {
				mouse_x_offset = scroll_handle.padded_limit_rectangle().left() - gui.mx();
				mouse_y_offset = scroll_handle.padded_limit_rectangle().top() - gui.my();

				scrolling = true;
			}
			@Override
			public void recalculateSize(GUIInstance gui) {
				this.unpadded_height = style().size().pixels();
				this.unpadded_width = style().size().pixels();
			}
		};
		
		{
			registerSubElement(scroll_handle);
			identifier("scrollbar");
		}
		
		boolean vertical = true;
		float scroll_amount;
		float content_size;
		float screen_size;

		public void scroll_amount(float amt) { if (scroll_amount == amt) return; scroll_amount = amt; update(); onScroll(); }
		public void content_size (int size) { content_size = size; update(); }	
		public void screen_size  (int size) { screen_size = size; update(); }
		
		private void update() {
			should_update(true);
			scroll_this().should_update(true);
		}
		
		public GUIScrollBar horizontalify() {
			vertical = false;
			return this;
		}
		
		@Override
		public void recalculateSize(GUIInstance gui) {
			this.unpadded_height = scroll_handle.height();
			this.unpadded_width = scroll_handle.width();
		}

		@Override
		public void updateDrawInfo(GUIInstance gui) {
			Rectangle b = padded_limit_rectangle();
			this.hover_rectangle(b);
			
			float screen_size_normmalized = screen_size / content_size;
			float scroll_amount_normmalized = scroll_amount / content_size;

			Rectangle limit;
			
			if (vertical) {
				limit = new Rectangle(0, 0, b.width(), (int) (screen_size_normmalized * b.height()));
				limit = limit.offset(0, (int) (scroll_amount_normmalized * b.height()));
			} else {
				limit = new Rectangle(0, 0, (int) (screen_size_normmalized * b.width()), b.height());
				limit = limit.offset((int) (scroll_amount_normmalized * b.width()), 0);
			}
			
			limit = limit.offset(b.left(), b.top());
			scroll_handle.limit_rectangle(limit);
		}

		@Override
		public void draw(GUIInstance gui, int depth) {
			gui.canvas().color(style().base_color().color());
			gui.canvas().rect(padded_limit_rectangle(), depth);
		}
		
		boolean scrolling = false;
		int mouse_x_offset = 0;
		int mouse_y_offset = 0;
		
		public void tickAnimation(GUIInstance gui) {
			if (!gui.primary_click_down()) scrolling = false;
			if (scrolling) {
				
				scroll_this().should_update(true);
				
				float mouse_amount_normalized = 0;
				
				if (vertical) {
					int my = gui.my()+mouse_y_offset;
					mouse_amount_normalized = padded_limit_rectangle().normalized(0, my).y;
				} else {
					int mx = gui.mx()+mouse_x_offset;
					mouse_amount_normalized = padded_limit_rectangle().normalized(mx, 0).x;
				}
				
				scroll_amount(mouse_amount_normalized * content_size);
				
			}
			if (scroll_this().get(PredicateKey.BOUNDED)) {
				if (vertical) scroll_amount(scroll_amount - gui.scroll().y);
				if (!vertical) scroll_amount(scroll_amount - gui.scroll().x);
			}
			
			limit_scroll_amount();
		}
		
		private void limit_scroll_amount() {
			if (scroll_amount < 0) scroll_amount(0);
			if (scroll_amount > content_size - screen_size) scroll_amount(content_size - screen_size);
		}
		
		@Override public void onPress(GUIInstance gui) {
			if (scroll_handle.limit_rectangle().contains(gui.mousepos())) {
				scroll_handle.onPress(gui);
			} else {
				scroll_towards_mouse(gui);
			}
		}
		
		@Override public void onHold(GUIInstance gui) {
			if (scroll_handle.limit_rectangle().contains(gui.mousepos())) {
				// N/A
			} else if (time_since_pressed() > GUIInstance.INPUT_REPEAT_TIME) {
				should_update(true);
				scroll_towards_mouse(gui);
			}
		}

		private void scroll_towards_mouse(GUIInstance gui) {
			
			Rectangle b = padded_limit_rectangle();
			
			float screen_size_normmalized = screen_size / content_size;
			float scroll_amount_normmalized = scroll_amount / content_size;
			
			float mouse_normalized = 0;
			if (vertical) mouse_normalized = (gui.my() - b.top()) / (float) b.height();
			if (!vertical) mouse_normalized = (gui.mx() - b.left()) / (float) b.width();
			
			if (mouse_normalized > scroll_amount_normmalized + screen_size_normmalized) {
				page_down();
			} else if (mouse_normalized < scroll_amount_normmalized) {
				page_up();
			}
			
			limit_scroll_amount();
		}
		
		public void page_up() 	{ scroll_amount(scroll_amount - screen_size);  }
		public void page_down() { scroll_amount(scroll_amount + screen_size);  }
		public int scroll_amount_pixels() { return (int) scroll_amount; }
		
		public void onScroll() { }

	}
	
	static final int DEFAULT_SCROLL_BAR_WIDTH = 16;
	static final int DEFAULT_SCROLL_BAR_HEIGHT = 16;

	GUIElement root;
	
	GUIScrollBar horizontal_scrollbar = new GUIScrollBar() {
		@Override public void onScroll() {scroll_this().should_update(true); }
	}.horizontalify() ;
	
	GUIScrollBar vertical_scrollbar = new GUIScrollBar() {
		@Override public void onScroll() {scroll_this().should_update(true); }
	};
	
	
	{
		registerSubElement(horizontal_scrollbar);
		registerSubElement(vertical_scrollbar);
	}

	// alignment left = scroll on the left, right = scroll on the right, etc
	// vertical alignment for horizontal scroll, halign for v scroll 
	
	// scroll bar size is the min/max width fields (but they ignore flex and container)
	// flex uses a default
	
	public GUIScrollable(GUIElement root) {
		root(root);
		// vvv whether or not to show these is calculated in updateDrawInfo
		// so, hide them on the first frame (since it doesn't get set in time
		// to avoid causing errors)
		// Probably not the best solution but I'll fix it later
		vertical_scrollbar.set(PredicateKey.HIDDEN, true);
		horizontal_scrollbar.set(PredicateKey.HIDDEN, true);
	}

	public void root(GUIElement root) {
		if (this.root != null) this.removeSubElement(this.root);
		this.root = root;
		this.registerSubElement(root);
		
		// TODO: make scrollbars always layer above root
		// by making them last in the sub_e
		removeSubElement(horizontal_scrollbar);
		removeSubElement(vertical_scrollbar);
		registerSubElement(horizontal_scrollbar);
		registerSubElement(vertical_scrollbar);
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 17;	// n/a, so...
		this.unpadded_width = 17;	// n/a, so...
	}
	
	@Override
	public void updateDrawInfo(GUIInstance gui) {
		
		Rectangle box = limit_rectangle();
		this.hover_rectangle(box);
		
		int content_width 	= root.width();
		int content_height 	= root.height();
		
		int screen_width 	= limit_rectangle().width();
		int screen_height 	= limit_rectangle().height();
		
		int scrollbar_width = vertical_scrollbar.width();
		int scrollbar_height = horizontal_scrollbar.height();
		boolean scrolling_y = false;
		boolean scrolling_x = false;

		// Whether or not we should be scrolling affects the scrollbar size, so check twice
		for (int rep = 0; rep < 2; rep ++) {
			scrolling_y = content_height 	> screen_height;
			scrolling_x = content_width 	> screen_width;
			if (scrolling_y) screen_width 	-= scrollbar_width;
			if (scrolling_x) screen_height 	-= scrollbar_height;
		}
		vertical_scrollbar.set(PredicateKey.HIDDEN, !scrolling_y);
		horizontal_scrollbar.set(PredicateKey.HIDDEN, !scrolling_x);

		// Vertical scrollbar
		Rectangle v_scrollbar = null;
		if (scrolling_y) {
			if (style().halign().constant() == Constant.RIGHT) {
				v_scrollbar = box.internal_int(box.width()-scrollbar_width, 0, box.width(), box.height());
			} else {
				v_scrollbar = box.internal_int(0, 0, scrollbar_width, box.height());
			}
			if (scrolling_x) {
				if (style().valign().constant() == Constant.BOTTOM) {
					v_scrollbar = v_scrollbar.internal_int(0, 0, v_scrollbar.width(), v_scrollbar.height()-scrollbar_height);
				} else {
					v_scrollbar = v_scrollbar.internal_int(0, scrollbar_height, v_scrollbar.width(), v_scrollbar.height());
				}
			}
			//vertical_scrollbar.scroll_amount(scroll_y);
			vertical_scrollbar.content_size(content_height);
			vertical_scrollbar.screen_size(screen_height);
			vertical_scrollbar.limit_rectangle(v_scrollbar);
		}

		
		// Horizontal scrollbar
		Rectangle h_scrollbar = null;
		if (scrolling_x) {
			if (style().valign().constant() == Constant.BOTTOM) {
				h_scrollbar = box.internal_int(0, box.height()-scrollbar_height, box.width(), box.height());
			} else {
				h_scrollbar = box.internal_int(0, 0, box.width(), scrollbar_height);
			}
			if (scrolling_y) {
				if (style().halign().constant() == Constant.RIGHT) {
					h_scrollbar = h_scrollbar.internal_int(0, 0, h_scrollbar.width()-scrollbar_width, h_scrollbar.height());
				} else {
					h_scrollbar = h_scrollbar.internal_int(scrollbar_width, 0, h_scrollbar.width(), h_scrollbar.height());
				}
			}
			//horizontal_scrollbar.scroll_amount(scroll_x);
			horizontal_scrollbar.content_size(content_width);
			horizontal_scrollbar.screen_size(screen_width);
			horizontal_scrollbar.limit_rectangle(h_scrollbar);	
		}

		Rectangle area_rectangle = limit_rectangle().internal_int(0, 0, limit_rectangle().width(), limit_rectangle().height());
		if (scrolling_y) {
			if (style().halign().constant() == Constant.RIGHT) {
				area_rectangle = area_rectangle.internal_int(0, 0, area_rectangle.width()-scrollbar_width, area_rectangle.height());
			} else {
				area_rectangle = area_rectangle.internal_int(scrollbar_width, 0, area_rectangle.width(), area_rectangle.height());
			}
		}
		if (scrolling_x) {
			if (style().valign().constant() == Constant.BOTTOM) {
				area_rectangle = area_rectangle.internal_int(0, 0, area_rectangle.width(), area_rectangle.height()-scrollbar_height);
			} else {
				area_rectangle = area_rectangle.internal_int(0, scrollbar_height, area_rectangle.width(), area_rectangle.height());
			}
		}
		
		root.scissor_rectangle_recursive(area_rectangle);
		last_area = area_rectangle;
		
		
		Rectangle root_limit = new Rectangle(
				 area_rectangle.left() 	- horizontal_scrollbar	.scroll_amount_pixels(),
				 area_rectangle.top() 	- vertical_scrollbar	.scroll_amount_pixels(),
				(area_rectangle.left() 	- horizontal_scrollbar	.scroll_amount_pixels()) + root.width(),
				(area_rectangle.top() 	- vertical_scrollbar	.scroll_amount_pixels()) + root.height()
		);
		
		if (root.style().min_width().constant() == Constant.CONTAINER) {
			root_limit = new Rectangle(root_limit.left(), root_limit.top(), area_rectangle.right(), root_limit.bottom());
		}
		
		root.limit_rectangle(root_limit);

		
	}
	
	Rectangle last_area = null;
	@Override
	public void onScissorChange() { if (last_area != null) root.scissor_rectangle_recursive(last_area); }

	@Override
	public void draw(GUIInstance gui, int depth) { }
	
	public void tickAnimation(GUIInstance gui) {
		if (horizontal_scrollbar.get(PredicateKey.HIDDEN)) horizontal_scrollbar.scroll_amount(0);
		if (vertical_scrollbar.get(PredicateKey.HIDDEN)) vertical_scrollbar.scroll_amount(0);
	}
	
}

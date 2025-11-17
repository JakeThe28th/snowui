package snowui.elements.base;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.GUIElement;
import snowui.utility.Margin;

// TODO: Vertical version?

public class GUICollapsible extends GUIElement {
	
	public GUICollapsible this_collapsible() { return this; }
	
	GUIIcon collapse_icon = new GUIIcon(IconType.GENERIC_ARROW_RIGHT) {
		@Override public void onSingleClick(GUIInstance gui) {
			collapsed(!collapsed);
			icon(collapsed ? IconType.GENERIC_ARROW_RIGHT : IconType.GENERIC_ARROW_DOWN);
			should_update(true);
		}
	};
	
	GUIElement root;
	GUIElement hidden;
	
	boolean collapsed = true;
	
	{ this.registerSubElement(collapse_icon); }
	
	public GUICollapsible root(GUIElement element) {
		if (root != null) this.removeSubElement(root);
		root = element;
		this.registerSubElement(element);
		return this;
	}
	
	public GUICollapsible hidden(GUIElement element) {
		if (hidden != null) this.removeSubElement(hidden);
		hidden = element;
		should_update(true);
		this.registerSubElement(element);
		collapsed(collapsed);
		return this;
	}
	
	public GUICollapsible collapsed(boolean b) { 
		this.collapsed = b; 
		hidden.set(PredicateKey.HIDDEN, b);
		should_update(true);
		should_recalculate_size(true);
		return this;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		unpadded_width = collapse_icon.width() + root.width();
		if (!collapsed && unpadded_width < collapse_icon.width() + hidden.width()) {
			unpadded_width = collapse_icon.width() + hidden.width();
		}
		unpadded_height = collapse_icon.height();
		if (root.height() > unpadded_height) unpadded_height = root.height(); 

		if (!collapsed) unpadded_height += hidden.height();
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = Margin.calculate(style(), padded_limit_rectangle(), unpadded_width, unpadded_height);
		
		int icon_width = collapse_icon.width();
		int upper_height = collapse_icon.height();
		if (root.height() > upper_height) upper_height = root.height();
		
		collapse_icon.limit_rectangle(new Rectangle(
				b.left(), 
				b.top(), 
				b.left() + icon_width, 
				b.top() + upper_height)
			);
		
		root.limit_rectangle(new Rectangle(
				b.left() + icon_width, 
				b.top(), 
				b.right() , 
				b.top() + upper_height)
			);
		
		hidden.limit_rectangle(new Rectangle(
				b.left() + icon_width, 
				b.top() + upper_height, 
				b.right() , 
				b.bottom())
			);
		
		this.hover_rectangle(b);
		
	}

	@Override public void draw(GUIInstance gui, int depth) { }
	
}

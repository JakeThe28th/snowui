package snowui.elements.extended;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIText;
import snowui.utility.GUIUtility;

public class GUIDockable extends GUIElement {
	
	GUIElement root;
	GUITitleBar titlebar = new GUITitleBar();
	
	{
		titlebar.set(PredicateKey.DISABLED, true);
		identifier("dockable");
		registerSubElement(titlebar);
		root(new GUIText("No content"));
	}
	
	public void root(GUIElement root) {
		if (this.root != null) this.removeSubElement(this.root);
		this.registerSubElement(root);
		this.root = root;
	}
	
	public GUIElement root() {
		return this.root;
	}
	
	public GUITitleBar titlebar() {
		return this.titlebar;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = GUIUtility.combined_height(sub_elements);
		this.unpadded_width = GUIUtility.max_width(sub_elements);
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = aligned_limit_rectangle();
		this.hover_rectangle(b);
		titlebar.limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top() + titlebar.height()));
		root.limit_rectangle(new Rectangle(b.left(), b.top() + titlebar.height(), b.right(), b.bottom()));
	}

	@Override public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(this.titlebar.limit_rectangle(), depth);
		gui.canvas().rect(this.root.limit_rectangle(), depth);
	}
	
	@Override public Rectangle drag_rectangle() {
		return titlebar.hover_rectangle();
	}

}

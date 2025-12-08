package snowui.elements.base;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.utility.GUIUtility;

public class GUIDockable extends GUIElement {
	
	GUIElement root;
	GUIElement titlebar = new TitleBar();
	
	{
		identifier("dockable");
		this.registerSubElement(titlebar);
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
	
	static class TitleBar extends GUIElement {
		GUIIcon icon = new GUIIcon(IconType.GENERIC_SEARCH);
		GUIText title = new GUIText("Dockable Window");
		{
			identifier("title_bar");
			this.registerSubElement(title);
			this.registerSubElement(icon);
		}
		
		@Override
		public void recalculateSize(GUIInstance gui) {
			this.unpadded_height = GUIUtility.max_height(sub_elements);
			this.unpadded_width = GUIUtility.combined_width(sub_elements);
		}
		
		@Override
		public void updateDrawInfo(GUIInstance gui) {
			Rectangle b = aligned_limit_rectangle(); 
			icon.limit_rectangle(new Rectangle(b.left(), b.top(), b.left() + icon.width(), b.bottom()));
			title.limit_rectangle(new Rectangle(b.left() + icon.width(), b.top(), b.right(), b.bottom()));
			this.hover_rectangle(b);
		}
		
		@Override public void draw(GUIInstance gui, int depth) { }
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

}

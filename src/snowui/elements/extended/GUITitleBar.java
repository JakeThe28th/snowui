package snowui.elements.extended;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIText;
import snowui.utility.GUIUtility;

public class GUITitleBar extends GUIElement {
		
	GUIIcon icon = new GUIIcon(IconType.GENERIC_EDIT);
	GUIText title = new GUIText("Dockable Window");
	
	public void title(String name) { title.text(name); }
	
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
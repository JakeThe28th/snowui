package snowui.elements.floating;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.docking.GUITitleBar;
import snowui.elements.interfaces.FloatingElement;

public class FloatingWindow extends GUIElement implements FloatingElement {
	 
	int x = 20;
	int y = 20;
	
	int width = 300;
	int height = 100;
	
	Rectangle position = new Rectangle(x, y, x + width, y + height);
	
	GUITitleBar title = new GUITitleBar();
	
	{
		this.registerSubElement(title);
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width = width;
		this.unpadded_height = height;
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.limit_rectangle();
		title.limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top() + title.height()));
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(this.limit_rectangle(), depth);
	}

	@Override
	public GUIElement as_element() {
		return this;
	}

	@Override
	public Rectangle position() {
		return position;
	}
	 
}

package snowui.elements.base;

import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;

public class GUIClickableRectangle extends GUIElement {
	
	{ identifier("clickable"); }

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 37;
		this.unpadded_width = 37;
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		this.hover_rectangle(padded_limit_rectangle());
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(padded_limit_rectangle(), depth);
	}

}

package snowui.elements.base;

import org.joml.Vector2i;

import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;
import snowui.elements.GUIElement;
import snowui.utility.Margin;

public class GUIText extends GUIElement {
	
	{ identifier("text"); }
	
	String text = null;
	
	public GUIText(String text) {
		this.text = text;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		Vector2i size = gui.canvas().textrenderer().size(text);
		this.unpadded_width = size.x;
		this.unpadded_height = size.y;
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		hover_rectangle(Margin.calculate(style(), this.padded_limit_rectangle(), unpadded_width, unpadded_height));
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.font_size(style().size().pixels());
		gui.canvas().color(style().base_color().color());
		gui.canvas().text(hover_rectangle().left(), hover_rectangle().top(), depth, text);
	}
	
}

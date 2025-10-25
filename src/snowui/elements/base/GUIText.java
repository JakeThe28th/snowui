package snowui.elements.base;

import org.joml.Vector2i;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.GUIElement;

public class GUIText extends GUIElement {
	
	String text = null;
	
	public GUIText(String text) {
		this.text = text;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		Vector2i size = gui.sizeof(text);
		this.unpadded_height = size.x;
		this.unpadded_height = size.y;
	}

	@Override
	public void updateDrawInfo(GUIInstance gui, Rectangle bounds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickAnimation(GUIInstance gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style.base_color.color());
		gui.canvas().text(bounds.left(), bounds.top(), depth, text);
	}
	
}

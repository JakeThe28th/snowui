package snowui.elements.base;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.GUIElement;

public class GUIText extends GUIElement {
	
	String text = "";
	
	public GUIText(String text) {
		this.text = text;
	}

	@Override
	public void updateState(GUIInstance gui) {
		// TODO Auto-generated method stub
		
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
	public void recalculateSize(GUIInstance gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(GUIInstance gui, int depth) {

		//gui.canvas().color(style().base_color.color());
		//gui.canvas().text(bounds.left(), bounds.top(), depth, text);
	}

	

}

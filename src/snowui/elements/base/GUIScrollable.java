package snowui.elements.base;

import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.GUIElement;

public class GUIScrollable extends GUIElement {
	
	GUIElement root;
	
	int scroll_y = 0;
	int scroll_x = 0;
	
	public GUIScrollable(GUIElement root) {
		root(root);
	}

	private void root(GUIElement root) {
		if (this.root != null) this.removeSubElement(this.root);
		this.root = root;
		this.registerSubElement(root);
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 17;	// n/a, so...
		this.unpadded_width = 17;	// n/a, so...
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		// TODO Auto-generated method stub
		TODO
		root.scissor_rectangle_recursive(limit_rectangle());
		root.limit_rectangle(limit_rectangle().offset(scroll_x, scroll_y));
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void tickAnimation(GUIInstance gui) {
		scroll_y++;
		this.should_update = true;
	}

}

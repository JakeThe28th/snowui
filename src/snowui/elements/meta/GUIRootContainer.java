package snowui.elements.meta;

import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.SubElementReplaceable;

public class GUIRootContainer extends GUIElement implements SubElementReplaceable {
	
	GUIElement root;
	
	public void root(GUIElement element) {
		if (root != null) this.removeSubElement(root);
		root = element;
		this.registerSubElement(element);
	}
	
	public GUIElement root() { return root; }

	@Override public void recalculateSize(GUIInstance gui) { }
	@Override public void draw(GUIInstance gui, int depth) { }

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		root.limit_rectangle(this.limit_rectangle());
		this.hover_rectangle(limit_rectangle());
	}
	
	@Override
	public void replace(GUIElement original, GUIElement replacement) {
		if (root == original) root(replacement);
		this.should_update(true);
	}


}

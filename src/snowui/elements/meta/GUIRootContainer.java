package snowui.elements.meta;

import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.SubElementReplaceable;
import snowui.elements.interfaces.components.SubElementReplaceQueue;

public class GUIRootContainer extends GUIElement implements SubElementReplaceable {
	
	GUIElement root;
	
	public void root(GUIElement element) {
		if (root != null) this.removeSubElement(root);
		root = element;
		if (root == null) return;
		this.registerSubElement(element);
	}
	
	public GUIElement root() { return root; }

	@Override public void recalculateSize(GUIInstance gui) { }
	@Override public void draw(GUIInstance gui, int depth) { }

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		if (root != null) root.limit_rectangle(this.limit_rectangle());
		this.hover_rectangle(limit_rectangle());
	}
	
	@Override
	public void replace(GUIElement original, GUIElement replacement) {
		if (root == original) root(replacement);
		this.should_update(true);
	}
	
	@Override
	public void tickAnimation(GUIInstance gui) {
		replace_queue.dequeue(this);
	}
	
	SubElementReplaceQueue replace_queue = new SubElementReplaceQueue();
	@Override public SubElementReplaceQueue queue() { return replace_queue; }

}

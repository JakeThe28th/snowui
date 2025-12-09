package snowui.elements.extended;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.SubElementReplaceable;

public class GUISplit extends GUIElement implements SubElementReplaceable {

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 37;
		this.unpadded_width = 37;
	}
	
	GUIElement first;
	GUIElement second;
	
	float split = 0.5f;
	boolean vertical = false;
	
	public GUISplit first(GUIElement e) {
		if (first != null) this.removeSubElement(first);
		first = e;
		this.registerSubElement(e);
		return this;
	}

	public GUISplit second(GUIElement e) {
		if (second != null) this.removeSubElement(second);
		second = e;
		this.registerSubElement(e);
		return this;
	}
	
	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = limit_rectangle();
		first.limit_rectangle(b.internal(0, 0, split, 1));
		second.limit_rectangle(b.internal(split, 0, 1, 1));
	}

	@Override public void draw(GUIInstance gui, int depth) { }

	@Override
	public void replace(GUIElement original, GUIElement replacement) {
		if (first == original) first(replacement);
		if (second == original) second(replacement);
	}

	@Override
	public void tickAnimation(GUIInstance gui) {
		split = (float) Math.cos((((float) System.nanoTime() % 1000)) / 1f);
	}
}

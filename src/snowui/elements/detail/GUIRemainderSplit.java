package snowui.elements.detail;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.utility.GUIUtility;

/** Draws one element at its' minimum size, and the second element in the remaining space. */
public class GUIRemainderSplit extends GUIElement {
	
	GUIElement first;
	GUIElement second;
	
	boolean vertical = true;
	

	public GUIRemainderSplit horizontalify() {
		this.vertical = false;
		return this;
	}
	
	public GUIRemainderSplit(GUIElement first, GUIElement second) {
		this.first = first;
		this.registerSubElement(first);
		this.second = second;
		this.registerSubElement(second);
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		if (vertical) {
			this.unpadded_height = GUIUtility.combined_height(sub_elements);
			this.unpadded_width = GUIUtility.max_width(sub_elements);
		} else {
			this.unpadded_height = GUIUtility.max_height(sub_elements);
			this.unpadded_width = GUIUtility.combined_width(sub_elements);
		}
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.limit_rectangle();
		this.hover_rectangle(b);
		if (vertical) {
			first .limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top()+first.height()));
			second.limit_rectangle(new Rectangle(b.left(), b.top()+first.height(), b.right(), b.bottom()));
		} else {
			first .limit_rectangle(new Rectangle(b.left(), b.top(), b.left()+first.width(), b.bottom()));
			second.limit_rectangle(new Rectangle(b.left()+first.width(), b.top(), b.right(), b.bottom()));
		}
	}

}

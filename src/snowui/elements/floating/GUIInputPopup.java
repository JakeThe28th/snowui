package snowui.elements.floating;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;
import snowui.elements.base.GUITextBox;
import snowui.elements.interfaces.FloatingElement;

public class GUIInputPopup extends GUIElement implements FloatingElement {
	
	{ identifier("generic_panel"); }
	
	private GUIInputPopup popup() { return this; }
	
	GUIText title;
	GUITextBox input;
	
	Rectangle area = new Rectangle(-1000, -1000, -1000, -1000);
	
	int x = 0;
	int y = 0;
	
	public GUIInputPopup(String title, String initial, GUIInstance gui) {
		this.title = new GUIText(title);
		this.registerSubElement(this.title);
		this.input = new GUITextBox(initial) {
			@Override
			public void onFinishEditing(String o, String result) {
				popup().onFinish(initial);
				gui.remove_window(popup());
			}
		};
		this.input.finish_on_enter(true);
		this.registerSubElement(this.input);
		this.x = gui.canvas().size().width()/2;
		this.y = gui.canvas().size().height()/2;
	}

	@Override public GUIElement as_element() { return this; }
	@Override public Rectangle position() { return area; }

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width = input.width();
		if (title.width() > this.unpadded_width) this.unpadded_width = title.width();
		this.unpadded_height = input.height() + title.height();
		area = new Rectangle(x-(unpadded_width/2),y-(unpadded_height/2),x+(unpadded_width/2),y+(unpadded_height/2));
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.aligned_limit_rectangle();
		this.hover_rectangle(b);
		title.limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top() + title.height()));
		input.limit_rectangle(new Rectangle(b.left(), b.top()+title.height(), b.right(), b.bottom()));
	}
	
	public void onFinish(String string) { }

}

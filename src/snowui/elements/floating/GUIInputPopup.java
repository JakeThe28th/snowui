package snowui.elements.floating;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;
import snowui.elements.base.GUITextBox;
import snowui.elements.interfaces.FloatingElement;

public class GUIInputPopup extends GUIElement implements FloatingElement {
	
	{ identifier("input_popup"); }
	
	private GUIInputPopup popup() { return this; }
	
	GUIText title;
	GUITextBox input;
	GUIList options = new GUIList();
	
	{
		options.add(new GUIText("OK") {
			@Override
			public void onSingleClick(GUIInstance gui) {
				popup().onFinish(input.text().text());
				gui.remove_window(popup());
			}
		}.identifier("text_button"));
		options.add(new GUIText("Cancel"){
			@Override
			public void onSingleClick(GUIInstance gui) {
				popup().onFinish(null);
				gui.remove_window(popup());
			}
		}.identifier("text_button"));
		options.horizontalify();
		options.identifier("confirm_popup_options");
		this.registerSubElement(options);
	}
	
	Rectangle area = new Rectangle(-1000, -1000, -1000, -1000);
	
	int x = 0;
	int y = 0;
	
	// TODO: Maybe merge the popups ? they share a lot of code...
	
	public GUIInputPopup(String title, String initial, GUIInstance gui) {
		this.title = new GUIText(title);
		this.registerSubElement(this.title);
		this.title.identifier("popup_title");
		this.title.set(PredicateKey.DISABLED, true);
		this.input = new GUITextBox(initial) {
			@Override
			public void onFinishEditing(String o, String result) {
				popup().onFinish(result);
				gui.remove_window(popup());
			}
		};
		this.input.finish_on_enter(true);
		this.registerSubElement(this.input);
		center(gui);
	}

	@Override public GUIElement as_element() { return this; }
	@Override public Rectangle position() { return area; }

	@Override
	public void recalculateSize(GUIInstance gui) {
		updateArea(gui);
	}
	
	void updateArea(GUIInstance gui) {
		this.unpadded_width = gui.canvas().width() / 2;
		if (title.style() != null) {
			if (title.width() > this.unpadded_width) this.unpadded_width = title.width();
			this.unpadded_height = input.height() + title.height();
		}
		int hh = unpadded_height/2;
		int re = unpadded_height%2;
		area = new Rectangle(x-(unpadded_width/2),y-hh,x+(unpadded_width/2),y+(hh+re));
	}
	
	void center(GUIInstance gui) {
		this.x = gui.canvas().size().width()/2;
		this.y = gui.canvas().size().height()/2;
	}
	
	@Override
	public void tickAnimation(GUIInstance gui) {
		super.tickAnimation(gui);
		updateArea(gui);
		center(gui);
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.aligned_limit_rectangle();
		this.hover_rectangle(b);
		title	.limit_rectangle(new Rectangle(b.left(), 				  b.top(), 				  b.right(), 					b.top() + title.height()));
		options	.limit_rectangle(new Rectangle(b.right()-options.width(), b.top()+title.height(), b.right(), 					b.bottom()));
		
		Rectangle input_area   = new Rectangle(b.left(), 				  b.top()+title.height(), b.right()-options.width(), 	b.bottom());
		input	.wrap_width     (input_area.width(), gui);
		input	.limit_rectangle(input_area);
	}
	
	public void onFinish(String string) { }

}

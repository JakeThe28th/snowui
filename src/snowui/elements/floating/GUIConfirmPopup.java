package snowui.elements.floating;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;
import snowui.elements.interfaces.FloatingElement;

public class GUIConfirmPopup extends GUIElement implements FloatingElement {
	
	{ identifier("generic_panel"); }
	
	private GUIConfirmPopup popup() { return this; }
	
	GUIText title;
	GUIList options = new GUIList();
	
	{
		options.add(new GUIText("Yes") {
			@Override
			public void onSingleClick(GUIInstance gui) {
				onConfirm();
				gui.remove_window(popup());
			}
		}.identifier("text_button"));
		options.add(new GUIText("No"){
			@Override
			public void onSingleClick(GUIInstance gui) {
				onDeny();
				gui.remove_window(popup());
			}
		}.identifier("text_button"));
		options.horizontalify();
		options.identifier("centered");
		this.registerSubElement(options);
	}
	
	Rectangle area = new Rectangle(-1000, -1000, -1000, -1000);
	
	int x = 0;
	int y = 0;
	
	public GUIConfirmPopup(String title, GUIInstance gui) {
		this.title = new GUIText(title);
		this.registerSubElement(this.title);
		this.x = gui.canvas().size().width()/2;
		this.y = gui.canvas().size().height()/2;
	}

	@Override public GUIElement as_element() { return this; }
	@Override public Rectangle position() { return area; }

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width = options.width();
		if (title.width() > this.unpadded_width) this.unpadded_width = title.width();
		this.unpadded_height = options.height() + title.height();
		area = new Rectangle(x-(unpadded_width/2),y-(unpadded_height/2),x+(unpadded_width/2),y+(unpadded_height/2));
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.aligned_limit_rectangle();
		this.hover_rectangle(b);
		title.limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top() + title.height()));
		options.limit_rectangle(new Rectangle(b.left(), b.top()+title.height(), b.right(), b.bottom()));
	}
	
	public void onConfirm() { }
	public void onDeny() { }

}

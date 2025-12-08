package snowui.elements.base;

import frost3d.enums.IconType;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;

public class GUIIcon extends GUIElement {

	{ identifier("icon"); }
	
	IconType icon = null;
	
	public GUIIcon icon(IconType new_icon) {
		this.icon = new_icon;
		return this;
	}
	
	public GUIIcon(IconType icon) {
		this.icon = icon;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width = style().size().pixels();
		this.unpadded_height = style().size().pixels();
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		hover_rectangle(aligned_limit_rectangle());
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.font_size(style().size().pixels());
		gui.canvas().color(style().base_color().color());
		gui.canvas().icon(hover_rectangle().left(), hover_rectangle().top(), depth, icon);
	}
	
}

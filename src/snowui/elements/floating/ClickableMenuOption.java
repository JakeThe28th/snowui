package snowui.elements.floating;

import frost3d.enums.IconType;
import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIText;
import snowui.utility.GUIUtility;

public class ClickableMenuOption implements MenuOption {
	IconType 	icon;
	String 		left_text;
	String 		right_text;
	public ClickableMenuOption(IconType icon, String left_text, String right_text) {
		this.icon = icon;
		this.left_text = left_text;
		this.right_text = right_text;
	}
	public void onSingleClick() {};
	@Override
	public MenuElement of() {
		return new MenuElement() {
			GUIIcon g_icon;
			GUIText g_left_text;
			GUIText g_right_text;
			boolean init = false;
			{ 
				identifier("basic_menu_option"); 
				if (icon != null) {
					g_icon = new GUIIcon(icon);
					this.registerSubElement(g_icon);
				}
				if (left_text != null) {
					g_left_text = new GUIText(left_text);
					this.registerSubElement(g_left_text);
				}
				if (right_text != null) {
					g_right_text = new GUIText(right_text);
					g_right_text.identifier("basic_menu_option_right");
					this.registerSubElement(g_right_text);
				}
			}
			
			int SPACE = 40;
			int ICON_SPACE = 10;
			
			@Override
			public void recalculateSize(GUIInstance gui) {
				this.unpadded_width = GUIUtility.combined_width(sub_elements) + SPACE + ICON_SPACE;
				this.unpadded_height = GUIUtility.max_height(sub_elements);
				if (!init) {
					left_text_start = 0;
					if (g_icon		!= null) left_text_start = g_icon.width() + ICON_SPACE;
				}
				if (!init) {
					left_text_end = 0;
					if (g_left_text != null) left_text_end 	= left_text_start + g_left_text.width() + SPACE;
				}
				init = true;
			}
			@Override
			public void updateDrawInfo(GUIInstance gui) {
				Rectangle b = this.aligned_limit_rectangle();
				if (g_icon != null) {
					g_icon.limit_rectangle(new Rectangle(b.left(), b.top(), b.left() + left_text_start, b.bottom()));
				}
				if (g_left_text != null) {
					g_left_text.limit_rectangle(new Rectangle(b.left() + left_text_start, b.top(), b.left() + left_text_end, b.bottom()));
				}
				if (g_right_text != null) {
					g_right_text.limit_rectangle(new Rectangle(b.left() + left_text_end, b.top(), b.right(), b.bottom()));
				}
			}
			@Override public void draw(GUIInstance gui, int depth) { }
		};
	}
	
}

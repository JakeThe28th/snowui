package snowui.elements.floating;

import java.util.ArrayList;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIText;
import snowui.utility.GUIUtility;

public class GUIContextMenuOption extends GUIElement {
	
	{ identifier("context_menu_option"); }
	
	GUIIcon g_icon;
	GUIText g_left_text;
	GUIText g_right_text;
	GUIIcon g_expand_icon;
	
	// Intrinsic
	int l_icon_size = 0;
	int l_text_size = 0;
	int r_text_size = 0;
	int r_icon_size = 0;
	
	// Set by parent
	int l_text_x = 0;
	int r_text_x = 0;
	int r_icon_x = 0;
	
	boolean prefer_right_side = true;

	public GUIContextMenuOption(IconType icon, String left_text, String right_text) {
		if (icon != null) {
			g_icon = new GUIIcon(icon);
			g_icon.set(PredicateKey.DISABLED, true);
			this.registerSubElement(g_icon);
		}
		if (left_text != null) {
			g_left_text = new GUIText(left_text);
			g_left_text.set(PredicateKey.DISABLED, true);
			this.registerSubElement(g_left_text);
		}
		if (right_text != null) {
			g_right_text = new GUIText(right_text);
			g_right_text.set(PredicateKey.DISABLED, true);
			g_right_text.identifier("context_menu_option_right");
			this.registerSubElement(g_right_text);
		}
	}
	
	ArrayList<GUIContextMenuOption> sub_options = null;
	GUIContextMenu g_sub_options = null;
	
	public GUIContextMenuOption(ArrayList<GUIContextMenuOption> o, String label) {
		if (label != null) {
			g_left_text = new GUIText(label);
			g_left_text.set(PredicateKey.DISABLED, true);
			this.registerSubElement(g_left_text);
		}
		this.sub_options = o;
		this.g_expand_icon = new GUIIcon(IconType.GENERIC_ARROW_RIGHT);
		g_expand_icon.set(PredicateKey.DISABLED, true);
		this.registerSubElement(g_expand_icon);
	}
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = GUIUtility.max_height	 (sub_elements);
		if (g_icon	   		!= null) l_icon_size = g_icon			.width();
		if (g_left_text		!= null) l_text_size = g_left_text		.width();
		if (g_right_text	!= null) r_text_size = g_right_text		.width();
		if (g_expand_icon	!= null) r_icon_size = g_expand_icon	.width();
		update_width();
	}
	
	public void update_width() { this.unpadded_width  = r_icon_x + r_icon_size; }
	
	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.aligned_limit_rectangle();
		this.hover_rectangle(b);
		if (g_icon != null) {
			g_icon.limit_rectangle(new Rectangle(b.left(), b.top(), b.left() + l_text_x, b.bottom()));
		}
		if (g_left_text != null) {
			g_left_text.limit_rectangle(new Rectangle(b.left() + l_text_x, b.top(), b.left() + r_text_x, b.bottom()));
		}
		if (g_right_text != null) {
			g_right_text.limit_rectangle(new Rectangle(b.left() + r_text_x, b.top(), r_icon_x, b.bottom()));
		}
		if (g_expand_icon != null) {
			g_expand_icon.limit_rectangle(new Rectangle(b.left() + r_icon_x, b.top(), b.right(), b.bottom()));
		}
	}
	
	@Override
	public void onHover(GUIInstance gui) {
		if (this.sub_options != null && this.g_sub_options == null) {
			this.g_sub_options = new GUIContextMenu(this.sub_options);
			gui.add_window(g_sub_options);
			g_sub_options.y = this.limit_rectangle().top();
			if (prefer_right_side) {
				g_sub_options.x = this.limit_rectangle().right();
			} else {
				g_sub_options.x = this.limit_rectangle().left();
			}
			g_sub_options.prefer_right_side = this.prefer_right_side;
		}
	}
	
};

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
	
	int left_text_start 	= 0;
	int left_text_end 		= 0;
	int right_text_end 		= 0;
	int ex_icon_width 		= 0;
	boolean init = false;
	
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
	
	int SPACE = 40;
	int ICON_SPACE = 10;
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = GUIUtility.max_height	 (sub_elements);
		update_alignments(true);
		update_width();
	}
	
	public void update_alignments(boolean allow_lower) {
		if (g_icon	      != null)
			left_text_start  = a(left_text_start, g_icon.width() 	+ ICON_SPACE, 					allow_lower);
		if (g_left_text   != null) 
			left_text_end    = a(left_text_end,   left_text_start   + g_left_text.width() + SPACE, 	allow_lower);
		if (g_right_text  != null)
			right_text_end   = a(right_text_end,  left_text_end 	+ g_right_text.width(), 		allow_lower);
		if (g_expand_icon != null)
			ex_icon_width    = a(ex_icon_width,   g_expand_icon.width(), 							allow_lower);
		init 			 = true;
	}

	public void update_width() {
		this.unpadded_width  = right_text_end + ex_icon_width;
	}
	
	private int a(int old, int replacement, boolean allow_lower) {
		return (allow_lower || replacement > old) ? replacement : old;
	}
	
	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.aligned_limit_rectangle();
		this.hover_rectangle(b);
		if (g_icon != null) {
			g_icon.limit_rectangle(new Rectangle(b.left(), b.top(), b.left() + left_text_start, b.bottom()));
		}
		if (g_left_text != null) {
			g_left_text.limit_rectangle(new Rectangle(b.left() + left_text_start, b.top(), b.left() + left_text_end, b.bottom()));
		}
		if (g_right_text != null) {
			g_right_text.limit_rectangle(new Rectangle(b.left() + left_text_end, b.top(), right_text_end, b.bottom()));
		}
		if (g_expand_icon != null) {
			g_expand_icon.limit_rectangle(new Rectangle(b.left() + right_text_end, b.top(), b.right(), b.bottom()));
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

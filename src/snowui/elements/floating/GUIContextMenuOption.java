package snowui.elements.floating;

import java.util.ArrayList;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIText;
import snowui.elements.interfaces.ContextMenuOption;
import snowui.utility.GUIUtility;

public class GUIContextMenuOption extends GUIElement implements ContextMenuOption {
	
	{ identifier("context_menu_option"); }
	
	GUIIcon g_icon;
	GUIText g_left_text;
	GUIText g_right_text;
	
	int left_text_start;
	int left_text_end;
	@Override public int 	left_text_start() 		{ return left_text_start	   ; }
	@Override public int 	left_text_end() 		{ return left_text_end	       ; }
	@Override public void 	left_text_start(int v) 	{ 		 left_text_start 	= v; }
	@Override public void 	left_text_end(int v) 	{ 		 left_text_end 		= v; }
	
	boolean init = false;

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
	
	ArrayList<ContextMenuOption> sub_options = null;
	
	public GUIContextMenuOption(ArrayList<ContextMenuOption> o, String label) {
		// TODO ...
	}
	
	int SPACE = 40;
	int ICON_SPACE = 10;
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width  = GUIUtility.combined_width (sub_elements) + SPACE + ICON_SPACE;
		this.unpadded_height = GUIUtility.max_height	 (sub_elements);
		if (!init) {
			left_text_start  = 0;
			left_text_end 	 = 0;
			if (g_icon		!= null) left_text_start = g_icon.width() + ICON_SPACE;
			if (g_left_text != null) left_text_end 	= left_text_start + g_left_text.width() + SPACE;
			init 			 = true;
		}
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
			g_right_text.limit_rectangle(new Rectangle(b.left() + left_text_end, b.top(), b.right(), b.bottom()));
		}
	}
	
	@Override public GUIElement as_element() { return this; }
	
};

package snowui.elements.floating;

import java.util.ArrayList;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.interfaces.FloatingElement;

public class GUIContextMenu extends GUIList implements FloatingElement {
	
	boolean prefer_right_side = true;

	{ identifier("context_menu"); }
	
	ArrayList<GUIContextMenuOption> sub_context_elements = new ArrayList<GUIContextMenuOption>();
	
	public GUIContextMenu(ArrayList<GUIContextMenuOption> options) {
		super(); 
		for (GUIContextMenuOption o : options) add(o);
		for (GUIContextMenuOption o : options) sub_context_elements.add(o);
	}
	
	int TEXT_SPACE = 40;
	int ICON_SPACE = 10;
	
	private void fix_menu_offsets() {
		int l_icon_size = 0;
		int l_text_size = 0;
		int r_text_size = 0;
		int r_icon_size = 0;
		for (GUIContextMenuOption e : sub_context_elements) {
			if (l_icon_size < e.l_icon_size) l_icon_size = e.l_icon_size;
			if (l_text_size < e.l_text_size) l_text_size = e.l_text_size;
			if (r_text_size < e.r_text_size) r_text_size = e.r_text_size;
			if (r_icon_size < e.r_icon_size) r_icon_size = e.r_icon_size;
		}
		for (GUIContextMenuOption e : sub_context_elements) {
			e.l_text_x = 0			+ 	l_icon_size;
			e.r_text_x = e.l_text_x	+ 	l_text_size + TEXT_SPACE;
			e.r_icon_x = e.r_text_x	+	r_text_size + ICON_SPACE;
			e.update_width();
		}
	}

	int x = 50;
	int y = 50;
	
	Rectangle area = new Rectangle(-1000,-1000,-1000,-1000);
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		
		fix_menu_offsets();

		super.recalculateSize(gui);

		if (prefer_right_side) {
			area = new Rectangle(x, y, x + width(), y + height());
		} else {
			area = new Rectangle(x - width(), y, x, y + height());
		}
	}

	@Override public GUIElement 	as_element() 						{ return this; }
	@Override public Rectangle 		position() 							{ return area; }

}

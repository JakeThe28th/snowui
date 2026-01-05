package snowui.elements.floating;

import java.util.ArrayList;

import frost3d.utility.Log;
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
	
	private void fix_menu_offsets() {
		int le = 0, ls = 0, re = 0, ic = 0;
		for (GUIContextMenuOption e : sub_context_elements) {
			if (ls < e.left_text_start)  ls = e.left_text_start;
			if (le < e.left_text_end) 	 le = e.left_text_end;
			if (re < e.right_text_end)   re = e.right_text_end;
			if (ic < e.ex_icon_width)	 ic = e.ex_icon_width;
		}
		for (GUIContextMenuOption e : sub_context_elements) {
			e.left_text_start 	= ls;
			e.left_text_end		= le;
			e.right_text_end	= re;
			e.ex_icon_width		= ic;
			e.update_width();
			e.update_alignments(false);
		}
	}

	int x = 50;
	int y = 50;
	
	Rectangle area = new Rectangle(0,0,100,100);
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		
		// Kinda scuffed ...
		for (int i = 0; i < 3; i++) fix_menu_offsets();

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

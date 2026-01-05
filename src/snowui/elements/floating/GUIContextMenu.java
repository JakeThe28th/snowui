package snowui.elements.floating;

import java.util.ArrayList;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.interfaces.FloatingElement;
import snowui.elements.interfaces.ContextMenuOption;

public class GUIContextMenu extends GUIList implements FloatingElement {

	{ identifier("context_menu"); }
	
	public GUIContextMenu(ArrayList<ContextMenuOption> options) {
		super(); for (ContextMenuOption o : options) add(o.as_element());
	}
	
	private void fix_menu_offsets() {
		int le = 0, ls = 0;
		for (GUIElement e : sub_elements()) {
			if (ls < ((ContextMenuOption) e).left_text_start()) ls = ((ContextMenuOption) e).left_text_start();
			if (le < ((ContextMenuOption) e).left_text_end()) 	le = ((ContextMenuOption) e).left_text_end();
		}
		for (GUIElement e : sub_elements()) {
			((ContextMenuOption) e).left_text_start(ls);
			((ContextMenuOption) e).left_text_end(le);
		}
	}

	int x = 50;
	int y = 50;
	
	Rectangle area = new Rectangle(0,0,100,100);
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		super.recalculateSize(gui);
		area = new Rectangle(x, y, x + width(), y + height());
		fix_menu_offsets();
	}

	@Override public GUIElement 	as_element() 						{ return this; }
	@Override public Rectangle 		position() 							{ return area; }

}

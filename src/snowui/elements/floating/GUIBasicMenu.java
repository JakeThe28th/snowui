package snowui.elements.floating;

import java.util.ArrayList;

import frost3d.enums.IconType;
import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;
import snowui.elements.interfaces.FloatingElement;
import snowui.utility.GUIUtility;

public class GUIBasicMenu extends GUIElement implements FloatingElement {

	{ identifier("basic_menu"); }
	
	
	
	
	public static class ExpandingMenuOption implements MenuOption {
		ArrayList<MenuOption> sub_options;
		public ExpandingMenuOption(ArrayList<MenuOption> sub_options) {
			this.sub_options = sub_options;
		}
		@Override
		public MenuElement of() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	// -- -- //
	
	GUIList menu = new GUIList();
	
	{ this.registerSubElement(menu); }
	
	public GUIBasicMenu(ArrayList<MenuOption> options) {
		for (MenuOption o : options) {
			menu.add(o.of());
		}
		fix_menu_offsets();
		
	}
	
	private void fix_menu_offsets() {
		int le = 0;
		int ls = 0;
		for (GUIElement e : menu.sub_elements()) {
			if (ls < ((MenuElement) e).left_text_start) ls = ((MenuElement) e).left_text_start;
			if (le < ((MenuElement) e).left_text_end) 	le = ((MenuElement) e).left_text_end;
		}
		for (GUIElement e : menu.sub_elements()) {
			((MenuElement) e).left_text_start = ls;
			((MenuElement) e).left_text_end = le;
		}
	}

	int x = 50;
	int y = 50;
	
	Rectangle area = new Rectangle(0,0,100,100);
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = menu.height();
		this.unpadded_width = menu.width();
		area = new Rectangle(x, y, x + width(), y + height());
		fix_menu_offsets();
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		menu.limit_rectangle(this.aligned_limit_rectangle());
		this.hover_rectangle(this.aligned_limit_rectangle());
	}

	@Override public void draw(GUIInstance gui, int depth) { }

	@Override
	public GUIElement as_element() { return this; }

	@Override
	public Rectangle position() { return area; }

}

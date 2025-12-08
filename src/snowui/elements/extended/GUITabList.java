package snowui.elements.extended;

import java.util.ArrayList;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIClickableRectangle;
import snowui.elements.base.GUIText;
import snowui.utility.GUIUtility;

public class GUITabList extends GUIElement {
	
	{ identifier("tablist"); }
			
	public static class Tab {
		GUIElement tab_clickable;
		GUIElement tab_hidden;
		GUIElement clickable() { return tab_clickable; }
		GUIElement hidden() { return tab_hidden; }
		public Tab(GUIElement click, GUIElement hide) {
			this.tab_clickable = click;
			this.tab_hidden = hide;
		}
	}
	
	int current_tab = 0;
	
	ArrayList<Tab> tabs = new ArrayList<>();
	ArrayList<GUIElement> clickables = new ArrayList<>();
	
	public void add(String tabname, GUIElement hidden) {
		GUIElement clickable = new GUIElement() {
			public GUIElement this_clickable() { return this; }
			{ identifier("tab"); }
			GUIText name = new GUIText(tabname) { 
				@Override public void onSingleClick(GUIInstance gui) { this_clickable().onSingleClick(gui); }
			};
			GUIClickableRectangle rect = new GUIClickableRectangle() { 
				@Override public void onSingleClick(GUIInstance gui) { this_clickable().onSingleClick(gui); }
			};
			{ this.registerSubElement(rect); this.registerSubElement(name); }
			@Override
			public void recalculateSize(GUIInstance gui) {
				this.unpadded_height = name.height();
				this.unpadded_width = name.width();
			}
			@Override
			public void updateDrawInfo(GUIInstance gui) {
				this.hover_rectangle(padded_limit_rectangle());
				name.limit_rectangle(padded_limit_rectangle());
				rect.limit_rectangle(padded_limit_rectangle());
			}
			@Override
			public void onSingleClick(GUIInstance gui) {
				select_tab(indexOfClickable(this));
			}
			@Override public void draw(GUIInstance gui, int depth) {}
		};
		this.add(clickable, hidden);
	}

	public void add(GUIElement clickable, GUIElement hidden) {
		this.registerSubElement(clickable);
		this.registerSubElement(hidden);
		clickables.add(clickable);
		tabs.add(new Tab(clickable, hidden));
		hidden.set(PredicateKey.HIDDEN, true);
		select_tab(current_tab);
	}
	
	public void remove(int index) {
		Tab remove = tabs.get(index);
		remove(remove);
	}
	
	public void remove(Tab remove) {
		clickables.remove(remove.clickable());
		tabs.remove(remove);
		this.removeSubElement(remove.tab_clickable);
		this.removeSubElement(remove.tab_hidden);
	}
	
	public int indexOfClickable(GUIElement clickable) {
		for (int tab = 0; tab < tabs.size(); tab++) {
			if (tabs.get(tab).clickable().equals(clickable)) return tab;
		}
		return -1;
	}
	
	public void select_tab(int index) {
		this.current_tab = index;
		this.should_update(true);
		tabs.get(current_tab).tab_hidden.set(PredicateKey.HIDDEN, false);
		for (int tab = 0; tab < tabs.size(); tab++) {
			if (tab != current_tab) tabs.get(tab).tab_hidden.set(PredicateKey.HIDDEN, true);
		}
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 37;
		this.unpadded_width = 37;
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		this.hover_rectangle(limit_rectangle());
		
		int tab_count = tabs.size();
		if (tab_count == 0) return;
		
		Rectangle b = limit_rectangle();
		
		int tab_size = b.width() / tab_count;
		int tab_length = GUIUtility.max_height(clickables);
		
		for (int tab = 0; tab < tabs.size(); tab++) {
			tabs.get(tab).tab_clickable.limit_rectangle(new Rectangle(
					b.left() + tab_size * tab,
					b.top(),
					b.left() + tab_size * (tab+1),
					b.top() + tab_length
				));
			
			tabs.get(tab).tab_hidden.limit_rectangle(new Rectangle(
					b.left(),
					b.top() + tab_length,
					b.right(),
					b.bottom()
				));
		}
	}

	@Override public void draw(GUIInstance gui, int depth) { }

}

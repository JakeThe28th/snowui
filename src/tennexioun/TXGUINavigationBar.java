package tennexioun;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUITextBox;
import snowui.utility.GUIUtility;
import tennexioun.TXDATANavigationBar.Tab;
import tennexioun.TXDATANavigationBar.TabGroup;
import tennexioun.base.TXGUINAVTab;
import tennexioun.base.TXGUINAVTabGroup;

/** A GUIElement that mirrors a DATANavigationBar.<br>
 *  Modifications to the DATANavigationBar are reflected here,
 *  and vice-versa.<br><br>
 *  To create, use DATANavigationBar.gui(). */
public class TXGUINavigationBar extends GUIElement {
	
	GUIList 	tab_groups 	= new GUIList().horizontalify().wrap(true);
	GUIList 	tabs 		= new GUIList().horizontalify().wrap(true);
	GUITextBox 	uri			= new GUITextBox("") {
		@Override
		public void onFinishEditing(String old_text, String new_text) {
			linked.setURI(new_text);
			linked.onManualURIChange(old_text, new_text);
		}
	};
	
	{
		this.registerSubElement(tab_groups);
		this.registerSubElement(tabs);
		this.registerSubElement(uri);
		uri.finish_on_enter(true);
	}
	
	TXDATANavigationBar linked;
	
	public void set_groups(TXDATANavigationBar data) {
		current_group = -1;
		tab_groups.clear();
		for (TabGroup group : data.groups) {
			tab_groups.add(new TXGUINAVTabGroup(group, data));
		}
	}
	
	public void set_tabs(TabGroup group) {
		current_tab = -1;
		tabs.clear();
		for (Tab tab : group.tabs) {
			tabs.add(new TXGUINAVTab(tab, group));
		}
	}
	
	int current_group = -1;
	int current_tab = -1;
	
	public void set_current_tab_and_group(int group_index, int tab_index) {
		if (current_group != group_index && tab_groups.length() > 0) {
			if (current_group >= 0) tab_groups.get(current_group).set(PredicateKey.SELECTED, false);
			current_group = group_index;
			tab_groups.get(current_group).set(PredicateKey.SELECTED, true);
		}
		if (current_tab != tab_index && tabs.length() > 0) {
			if (current_tab >= 0) tabs.get(current_tab).set(PredicateKey.SELECTED, false);
			current_tab = tab_index;
			tabs.get(current_tab).set(PredicateKey.SELECTED, true);
		}
	}

	public void set_uri(String current_uri) {
		uri.set_text(current_uri);
	}
	
	
	protected TXGUINavigationBar(TXDATANavigationBar source) {
		linked = source;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = tab_groups.height() + tabs.height() + uri.height();
		this.unpadded_width = GUIUtility.max_width(sub_elements);
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.padded_limit_rectangle();
		this.hover_rectangle(b);
		
		int yy = b.top();
		tab_groups.limit_rectangle(new Rectangle(b.left(), yy, b.right(), yy+tab_groups.height()));
		    yy += tab_groups.height();
		    
		tabs.limit_rectangle(new Rectangle(b.left(), yy, b.right(), yy+tabs.height()));
		    yy += tabs.height();
		    
		uri.limit_rectangle(new Rectangle(b.left(), yy, b.right(), yy+uri.height()));
		    yy += uri.height();
	}

	@Override public void draw(GUIInstance gui, int depth) { }

	public void edit_group_name(GUIInstance gui) {
		current_group().title.select(gui);
	}

	protected TXGUINAVTabGroup current_group() 	{ return group(current_group); }
	protected TXGUINAVTabGroup group(int index) { return ((TXGUINAVTabGroup) tab_groups.get(index)); }
	
	public void set_group_name(int group_index, String name) {
		group(group_index).title.text().force_text(name);
	}

}

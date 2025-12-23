package tennexioun;

import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIList;
import snowui.elements.extended.GUITextBox;
import tennexioun.DATANavigationBar.TabGroup;

/** A GUIElement that mirrors a DATANavigationBar.<br>
 *  Modifications to the DATANavigationBar are reflected here,
 *  and vice-versa.<br><br>
 *  To create, use DATANavigationBar.gui(). */
public class GUINavigationBar extends GUIElement {
	
	GUIList 	tab_groups 	= new GUIList();
	GUIList 	tabs 		= new GUIList();
	GUITextBox 	uri			= new GUITextBox("");
	
	DATANavigationBar linked;
	
	public void set_current_tab_and_group(int group_index, int tab_index) {
		
	}
	
	protected GUINavigationBar(DATANavigationBar source) {
		linked = source;
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		// TODO Auto-generated method stub
		
	}

	public void set_tabs(TabGroup current_group) {
		// TODO Auto-generated method stub
		
	}

	public void set_groups(DATANavigationBar dataNavigationBar) {
		// TODO Auto-generated method stub
		
	}

	public void set_uri(String current_uri) {
		// TODO Auto-generated method stub
		
	}

}

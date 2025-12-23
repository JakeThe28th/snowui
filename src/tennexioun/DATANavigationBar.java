package tennexioun;

import java.util.ArrayList;

/** A representation of a navigation bar, with a (string) URI, tabs, and groups.<br>
 *  This object has a corresponding GUIElement which can be retrieved with the gui() method. */
public class DATANavigationBar {
	
	int tbgc;
	
	public class TabGroup {
		String 		 group_name		= "Unnamed Group #" + (tbgc++);
		ArrayList<Tab> tabs 		= new ArrayList<>();
		int			 tab_index 		= 0;
		public Tab	 tab		(int index)		{ return tabs.get(index); }
		public Tab 	 current_tab() 				{ return tabs.size() > 0 ? tabs.get(tab_index) : null; }
		public void	 current_tab(int index) 	{ tab_index = index; syncGUI_Navigation();  }
		public void	 current_tab(Tab tab) 		{ current_tab(tabs.indexOf(tab)); }
		// ... Navigation ... //
		public void	nexttab() {
			if (tab_index >= tabs.size()-1) return;
			current_tab(tab_index + 1);
		}
		public void	prevtab() {
			if (tab_index <= 0) return;
			current_tab(tab_index - 1);
		}
		// ...  Mutability  ... //
		public void insertTab(String tab_uri) {
			tabs.add(tab_index, new Tab(tab_uri));
			syncGUI_Content_Tabs();
		}
		public void deleteTab(int tab_index) {
			tabs.remove(tab_index);
			syncGUI_Content_Tabs();
		}
		public void deleteTab() {
			deleteTab(tab_index);
		}
	}
	
	public class Tab {
		String tab_uri;
		// TODO: Icons, custom data, etc.
		public Tab(String uri) 	{ tab_uri = uri; }
		public String uri() 			{ return tab_uri; }
	}
	
	// -- =+  GUI  += -- //
	GUINavigationBar gui = new GUINavigationBar(this);
	public GUINavigationBar gui() { return gui; }
	
	private void syncGUI_Navigation() {
		gui.set_current_tab_and_group(group_index, current_group().tab_index);
	}
	
	private void syncGUI_Content_Tabs() {
		gui.set_tabs(current_group());
	}
	
	private void syncGUI_Content_Groups() {
		gui.set_groups(this);
	}
	
	private void syncGUI_Content_URI() {
		gui.set_uri(current_uri);
	}
	
	// -- =+  Groups  += -- //
	ArrayList<TabGroup> groups = new ArrayList<>();
	int group_index = 0;
	
	public TabGroup group		 (int index) 		{ return groups.get(index); }
	public TabGroup current_group() 				{ return groups.size() > 0 ? groups.get(group_index) : null; }
	public void		current_group(int index) 	 	{ group_index = index; }
	public void		current_group(TabGroup group) 	{ group_index = groups.indexOf(group); }
	
	// -- =+  Tabs  += -- //
	public Tab 	  	current_tab() 					{ return current_group().current_tab(); }

	// -- =+  URI  += -- //
	String current_uri = "meta://nothing";
	public String 	current_uri() 				{ return current_uri; 			}
	public void 	current_uri(String new_uri) { 		 current_uri = new_uri; }
	
	// ... Navigation ... //
	public void	nextgroup() {
		if (group_index >= groups.size()-1) return;
		current_group(group_index + 1);
	}
	public void	prevgroup() {
		if (group_index <= 0) return;
		current_group(group_index - 1);
	}

	// ...  Mutability  ... //
	public void setURI(String uri) {
		onURIChange(current_uri, uri);
		current_uri = uri;
		syncGUI_Content_URI();
	}
	
	public void addGroup() {
		groups.add(new TabGroup());
		syncGUI_Content_Groups();
	}

	// -- Callbacks -- //
	
	public void onURIChange(String old_uri, String new_uri) {
		
	}

}

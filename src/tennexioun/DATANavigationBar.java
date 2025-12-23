package tennexioun;

import java.util.ArrayList;

import org.joml.Vector4f;

/** A representation of a navigation bar, with a (string) URI, tabs, and groups.<br>
 *  This object has a corresponding GUIElement which can be retrieved with the gui() method. */
public class DATANavigationBar {
	
	int tbgc;
	
	public class TabGroup {
		public Vector4f color;
		{
			float r = (float) Math.random();
			float g = (float) Math.random();
			float b = (float) Math.random();
			float sum = r + g + b;
			color =  new Vector4f(r/sum, g/sum, b/sum, 1);
		}
		String 		  group_name		= "Unnamed Group #" + (tbgc++);
		ArrayList<Tab> tabs 			= new ArrayList<>();
		int			  tab_index 		= 0;
		public Tab	  tab		 (int index)	{ return tabs.get(index); }
		public Tab 	  current_tab() 			{ return tabs.size() > 0 ? tabs.get(tab_index) : null; }
		public void	  current_tab(int index) 	{ tab_index = index; syncGUI_Navigation(); setURI(current_tab().tab_uri); }
		public void	  current_tab(Tab tab) 		{ current_tab(tabs.indexOf(tab)); }
		public String group_name () 			{ return group_name; }
		public void   group_name (String name) 	{ group_name = name; syncGUI_Groups(); }
		// ... Navigation ... //
		public void	nexttab() {
			if (tab_index >= tabs.size()-1) return;
			current_tab(tab_index + 1);
		}
		public void	prevtab() {
			if (tab_index <= 0) return;
			current_tab(tab_index - 1);
		}
		private void select() {
			syncGUI_Tabs(); 
			if (current_tab() != null) setURI(current_tab().tab_uri);
		}
		// ...  Mutability  ... //
		public void insertTab(String tab_uri) {
			tabs.add(tab_index, new Tab(tab_uri));
			syncGUI_Tabs();
		}
		public void deleteTab(int tab_index) {
			tabs.remove(tab_index);
			syncGUI_Tabs();
		}
		public void deleteTab() {
			deleteTab(tab_index);
		}
	}
	
	public class Tab {
		String tab_uri;
		// TODO: Icons, custom data, etc.
		public Tab(String uri) 	{ tab_uri = uri; }
		public String uri() 	{ return tab_uri; }
	}
	
	// -- =+  GUI  += -- //
	GUINavigationBar gui = new GUINavigationBar(this);
	public GUINavigationBar gui() { return gui; }
	
	private void syncGUI_Navigation() {
		gui.set_current_tab_and_group(group_index, current_group().tab_index);
	}
	
	private void syncGUI_Tabs() {
		gui.set_tabs(current_group());
		syncGUI_Navigation();
	}
	
	private void syncGUI_Groups() {
		gui.set_groups(this);
		syncGUI_Navigation();
	}
	
	private void syncGUI_URI() {
		gui.set_uri(current_uri);
	}
	
	// -- =+  Groups  += -- //
	ArrayList<TabGroup> groups = new ArrayList<>();
	int group_index = 0;
	
	public TabGroup group		 (int index) 		{ return groups.get(index); }
	public TabGroup current_group() 				{ return groups.size() > 0 ? groups.get(group_index) : null; }
	public void		current_group(int index) 	 	{ group_index = index; current_group().select(); }
	public void		current_group(TabGroup group) 	{ current_group(groups.indexOf(group)); }
	
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
		syncGUI_URI();
	}
	
	public void addGroup() {
		groups.add(new TabGroup());
		syncGUI_Groups();
	}

	// -- Callbacks -- //
	
	public void onURIChange(String old_uri, String new_uri) {
		
	}

}

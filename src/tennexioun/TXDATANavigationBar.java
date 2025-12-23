package tennexioun;

import java.util.ArrayList;

import org.joml.Vector4f;

import frost3d.Input;
import snowui.GUIInstance;

/** A representation of a navigation bar, with a (string) URI, tabs, and groups.<br>
 *  This object has a corresponding GUIElement which can be retrieved with the gui() method. */
public class TXDATANavigationBar {
	
	ArrayList<Tab> deleted_tabs = new ArrayList<>();

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
		String 		  name				= "Unnamed Group #" + (tbgc++);
		ArrayList<Tab> tabs 			= new ArrayList<>();
		int			  tab_index 		= 0;
		public Tab	  tab		 (int index)	{ return tabs.get(index); }
		public Tab 	  current_tab() 			{ return tabs.size() > 0 ? tabs.get(tab_index) : null; }
		public void	  current_tab(int index) 	{ tab_index = index; syncGUI_Navigation(); setURI(current_tab().uri); }
		public void	  current_tab(Tab tab) 		{ current_tab(tabs.indexOf(tab)); }
		public String name 		 () 			{ return name; }
		public void   name 		 (String name) 	{ this.name = name; syncGUI_GroupName(this, name); }
		// ... Navigation ... //
		public void	next_tab() {
			if (tab_index >= tabs.size()-1) return;
			current_tab(tab_index + 1);
		}
		public void	previous_tab() {
			if (tab_index <= 0) return;
			current_tab(tab_index - 1);
		}
		private void select() {
			if (current_group() == this) {
				syncGUI_Tabs(); 
				if (current_tab() != null) setURI(current_tab().uri);
			}
		}
		// ...  Mutability  ... //
		public void deleteTab(int tab_index) {
			deleted_tabs.add(tabs.get(tab_index));
			tabs.remove(tab_index);
			syncGUI_Tabs();
			select();
		}
		public void deleteTab() {
			deleteTab(tab_index);
		}
		public void insertTab(Tab tab) {
			tabs.add(tab_index, tab);
			syncGUI_Tabs();
			select();
		}
		public void insertTab(String tab_uri) {
			insertTab(new Tab(tab_uri));
		}
		public void restoreTab() {
			if (deleted_tabs.size() <= 0) return;
			insertTab(deleted_tabs.getLast());
			deleted_tabs.removeLast();
		}
	}
	
	public class Tab {
		String uri;
		// TODO: Icons, custom data, etc.
		public Tab(String uri) 	{ this.uri = uri; }
		public String uri() 	{ return uri; }
	}
	
	// -- =+  Input  += -- //
	public TXINNavigationBar inputhandler(Input input, GUIInstance gui) { return new TXINNavigationBar(this, input, gui); }

	// -- =+  GUI  += -- //
	TXGUINavigationBar gui = new TXGUINavigationBar(this);
	public TXGUINavigationBar gui() { return gui; }
	
	protected void syncAllGUI() {
		syncGUI_Groups();
		syncGUI_Tabs();
		syncGUI_URI();
	}
	
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

	private void syncGUI_GroupName(TabGroup group, String name) {
		gui.set_group_name(groups.indexOf(group), name);
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
	public void	next_group() {
		if (group_index >= groups.size()-1) return;
		current_group(group_index + 1);
	}
	public void	previous_group() {
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

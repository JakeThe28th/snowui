package snowui.elements.docking;

import java.util.ArrayList;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIIcon;
import snowui.elements.base.GUIText;
import snowui.elements.interfaces.SubElementReplaceable;
import snowui.elements.interfaces.components.SubElementReplaceQueue;
import snowui.utility.GUIUtility;

public class GUIDockableTabList extends GUIElement implements SubElementReplaceable {
	
	{ identifier("dockable_tab_list"); }
	
	/* Specific tab list for Dockables *only*.
	 * 
	 * It will probably be less'v a pain in the 
	 * long run to separate these from the more
	 * generalized tabs element.
	 * 
	 * TODO: At some point, I should find a way
	 * to DRY this and the generic TabList into
	 * one thing. Though, at the moment I think
	 * I'm only using tabs for dockers anyways,
	 * so... (excluding the URL bar style tabs)
	 * 
	 *  */
	
	// -- == ----- Visual Settings ----- == -- //
	
	enum 	TabDisplayMode 					   			   { FULL, ICON_ONLY }
	private TabDisplayMode 	display_mode = TabDisplayMode.ICON_ONLY;
	public 	TabDisplayMode 	display_mode				() { return display_mode; 								}
	public 	void 			setDisplayModeToFull		() { 		display_mode = TabDisplayMode.FULL; 		
																	refreshTabDisplayElements(); 				}
	public 	void 			setDisplayModeToIconOnly	() { 		display_mode = TabDisplayMode.ICON_ONLY; 	
																	refreshTabDisplayElements(); 				}
	
	enum	TabAlignment								{ LEFT, TOP, RIGHT, BOTTOM }
	private TabAlignment	tab_bar_alignment = TabAlignment.LEFT;
	public 	TabAlignment 	tab_bar_alignment			() { return tab_bar_alignment; 							}
	public 	void 			alignTabBarLeft				() { tab_bar_alignment = TabAlignment.LEFT;				}
	public 	void 			alignTabBarTop				() { tab_bar_alignment = TabAlignment.TOP;				}
	public 	void 			alignTabBarRight			() { tab_bar_alignment = TabAlignment.RIGHT;			}
	public 	void 			alignTabBarBottom			() { tab_bar_alignment = TabAlignment.BOTTOM;			}

	
	// -- == ----- Data State ----- == -- //
	
	/* Keeps track of the underlying data that
	 * persists between handle refreshes . */
	
	interface TabInfo {
		public IconType icon();
		public String title();
		// The element this tab represents. 
		//   Note that the whole element is rendered;
		//   if you want to, for example, hide the
		//   titlebar to avoid duplicating it you 
		//   need to handle that there.
		public GUIElement element(); 
	}
	
	ArrayList<TabInfo> tabs = new ArrayList<>();
	
	public void addDocker(GUIDockable docker) {
		addDocker(tabs.size(), docker);
	}
	
	public void addDocker(int index, GUIDockable docker) {
		for (TabInfo tab : tabs) if (tab.element() == docker) {
			throw new IllegalStateException("Attempted to add a docker to a tab group it's already in.");
		}
		TabInfo new_tab = new TabInfo() {
			@Override public IconType 		icon		() { return docker.titlebar().icon.icon(); }
			@Override public String 		title		() { return docker.titlebar().title.text(); }
			@Override public GUIDockable 	element		() { return docker; }
		};
		tabs.add(index, new_tab);
		refreshTabDisplayElements();
		setCurrentTab(index);
	}
	
	// -- == ----- Element State ----- == -- //
	
	/* Keeps track of the elements rendered,
	 * since they're replaced a lot . */

	ArrayList<GUIElement> tab_display_elements = new ArrayList<>();
	
	/** The elements which are displayed in the tab bar. */
	private ArrayList<? extends GUIElement> tab_display_elements() {
		return tab_display_elements;
	}
	
	/** Replace the elements used to display tab handles. */
	private void refreshTabDisplayElements() {
		
		// TODO: Maybe this is heavy for just adding elements,
		// but I doubt there'll be *that* many tabs added to this,
		// unlike something like the url navigation bar thingy...
				
		// remove old versions
		for (GUIElement e : tab_display_elements) this.removeSubElement(e);
		tab_display_elements.clear();
		
		// NOTE: Of course, using the Docker's titlebar and icon
		// directly would cause conflicts, so we have to settle for
		// copies.
		
		// add new versions
		for (TabInfo tab : tabs) {
			GUIElement display = null;
			int tab_index = tabs.indexOf(tab);
			switch (display_mode) {
				case FULL: 		display = new GUIText(tab.title()) { @Override public void onSingleClick() { setCurrentTab(tab_index); }} ; break;
				case ICON_ONLY: display = new GUIIcon(tab.icon()) { @Override public void onSingleClick() { setCurrentTab(tab_index); }} ; break;
			}
			display.identifier("dockable_tab");
			tab_display_elements.add(display);
			registerSubElement(display);
		}
		
		if (current_tab_content != null) {
			tab_display_elements.get(current_tab_index).set(PredicateKey.SELECTED, true);
		}
		
	}
	
	/** The currently selected tab's root element. */
	GUIElement current_tab_content = null;
	
	int current_tab_index;
	
	public void setCurrentTab(int i) {
		if (current_tab_content != null) tab_display_elements.get(current_tab_index).set(PredicateKey.SELECTED, false);
		if (current_tab_content != null) removeSubElement(current_tab_content);
		current_tab_content = tabs.get(i).element();
		registerSubElement(current_tab_content);
		
		current_tab_index = i;
		tab_display_elements.get(current_tab_index).set(PredicateKey.SELECTED, true);
	}
	
	// -- == ------- GUI Code ------- == -- //

	@Override
	public void recalculateSize(GUIInstance gui) {
		int tab_content_width = (current_tab_content == null) ? 0 : current_tab_content.width();
		int tab_content_height = (current_tab_content == null) ? 0 : current_tab_content.height();
		switch (tab_bar_alignment) {
			case LEFT:
			case RIGHT:
				this.unpadded_width = GUIUtility.max_width(tab_display_elements()) + tab_content_width;
				this.unpadded_height = current_tab_content.height();
				int tab_combined_height = GUIUtility.combined_height(tab_display_elements());
				if (tab_combined_height > unpadded_height) unpadded_height = tab_combined_height;
				break;
			case TOP:
			case BOTTOM:
				this.unpadded_width = current_tab_content.width();
				this.unpadded_height = GUIUtility.max_height(tab_display_elements()) + tab_content_height;
				int tab_combined_width = GUIUtility.combined_width(tab_display_elements());
				if (tab_combined_width > unpadded_width) unpadded_width = tab_combined_width;
				break;
		}
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = this.aligned_limit_rectangle();
		this.hover_rectangle(b);
		
		// -- Tab Bar -- //

		int tab_bar_width = 0;
		int tab_bar_height = 0;
		
		switch (tab_bar_alignment) {
			case LEFT:
			case RIGHT:
				tab_bar_width = GUIUtility.max_width(tab_display_elements());
				tab_bar_height = b.height();
				break;
			case TOP:
			case BOTTOM:
				tab_bar_width = b.width();
				tab_bar_height = GUIUtility.max_height(tab_display_elements());
				break;
		}
		
		int tab_bar_top = 0;
		int tab_bar_left = 0;
		
		switch (tab_bar_alignment) {
			case TOP:
			case LEFT:
				tab_bar_top = b.top();
				tab_bar_left = b.left();
				break;
			case RIGHT:
				tab_bar_top = b.top();
				tab_bar_left = b.right() - tab_bar_width;
				break;
			case BOTTOM:
				tab_bar_top = b.bottom() - tab_bar_height;
				tab_bar_left = b.left();
				break;
		}

		int yy = 0;
		int xx = 0;
		
		for (GUIElement display_tab : tab_display_elements()) {
			switch (tab_bar_alignment) {
				case LEFT:
				case RIGHT: {
					int hh = display_tab.height();
					var l = new Rectangle(tab_bar_left, tab_bar_top + yy, tab_bar_left + tab_bar_width, tab_bar_top + yy + hh);
					display_tab.limit_rectangle(l);
					yy += hh;
					break; }
				case TOP:
				case BOTTOM: {
					int ww = display_tab.width();
					var l = new Rectangle(tab_bar_left + xx, tab_bar_top, tab_bar_left + xx + ww, tab_bar_top + tab_bar_height);
					display_tab.limit_rectangle(l);
					xx += ww; }
			}
		}
		
		// -- Tab Root Element -- //
		
		Rectangle l = null;
		
		switch (tab_bar_alignment) {
			case LEFT	: l = new Rectangle(b.left() + tab_bar_width, b.top(), b.right(), b.bottom()); 	break;
			case RIGHT	: l = new Rectangle(b.left(), b.top(), b.right() - tab_bar_width, b.bottom()); 	break;
			case TOP	: l = new Rectangle(b.left(), b.top() + tab_bar_height, b.right(), b.bottom()); break;
			case BOTTOM	: l = new Rectangle(b.left(), b.top(), b.right(), b.bottom() - tab_bar_height); break;
		}
		
		current_tab_content.limit_rectangle(l);
		
	}
	
	// -- == ------- Docking ------- == -- //

	/* Most of the work for docking is actually done in
	 * GUIDockable (gee who woulda thunk...), it's responsible for:
	 * 	
	 * 		- Adding a sub-docker to its' parent if 
	 * 		  its' parent is <this> and the docker
	 * 		  was dragged to its' center
	 * 
	 * 		- Replacing itself in its parent with a 
	 *        placeholder when it's dragged but not 
	 *        yet dropped
	 *        
	 * 		- Removing itself from its' parent when
	 * 		  dragged and dropped to somewhere else
	 * 
	 * That means this class doesn't actually have to implement
	 * ElementReceiver...
	 */


	@Override
	public void replace(GUIElement original, GUIElement replacement) {
		
		// NOTE: This method is only meant to facilitate 
		// the replacement with a dummy element a Dockable
		// does while it's being dragged.
		
		if (replacement instanceof GUISplit) {
			throw new IllegalStateException("Unexpected GUISplit in " + this.getClass().getName());
		}
		
		TabInfo og_tab = null;
		for (TabInfo tab : tabs) if (tab.element().equals(original)) og_tab = tab;
		
		if (og_tab != null) {
			int og_index = tabs.indexOf(og_tab);
			tabs.remove(og_tab);
			if (replacement != null) {
				if (replacement instanceof GUIDockable) {
					// Putting the docker back after stopping dragging it
					addDocker(og_index, (GUIDockable) replacement);
				} else {
					// Dummy element
					IconType og_icon = og_tab.icon();
					String og_title = og_tab.title();
					TabInfo new_tab = new TabInfo() {
						@Override public IconType 		icon		() { return og_icon; }
						@Override public String 		title		() { return og_title + "*"; }
						@Override public GUIElement 	element		() { return replacement; }
					};
					tabs.add(og_index, new_tab);
				}
			} else {
					// Removing element after dropping it somewhere else
					og_index = tabs.size() - 1;
			}
			if (current_tab_content != null) removeSubElement(current_tab_content);
			current_tab_content = null;
			refreshTabDisplayElements();
			if (og_index >= 0) {
				setCurrentTab(og_index);
			} else {
				current_tab_content = new GUIText("No Tabs.").identifier("snowui-centered");
				registerSubElement(current_tab_content);
			}
			
		}
		
	}

	SubElementReplaceQueue replace_queue = new SubElementReplaceQueue();
	@Override public SubElementReplaceQueue queue() { return replace_queue; }
	
	@Override
	public void tickAnimation(GUIInstance gui) {
		collapse();
		replace_queue.dequeue(this);
	}
	
	private void collapse() {
		// If there are no tabs, then this might as well just not exist
		if (tabs.size() == 0) {
			if (parent() instanceof SubElementReplaceable) {
				((SubElementReplaceable) parent()).queue().queue_replace(this, null);
			}
		}
		// If there's only one tab, then this should just become that tab
		if (tabs.size() == 1) {
			if (parent() instanceof SubElementReplaceable) {
				((SubElementReplaceable) parent()).queue().queue_replace(this, tabs.get(0).element());
			}
		}
	}
	
}

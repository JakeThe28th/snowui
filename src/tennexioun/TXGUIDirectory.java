package tennexioun;

import frost3d.enums.IconType;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUICollapsible;
import snowui.elements.base.GUIList;
import snowui.elements.base.GUIText;
import snowui.elements.interfaces.ElementReceiver;

public abstract class TXGUIDirectory extends GUICollapsible implements ElementReceiver {
	
	class ItemNameplate extends GUIElement {
		GUIText name;
		public GUIText text() { return name; }
		public ItemNameplate(String name) {
			this.name = new GUIText(name);
			this.registerSubElement(this.name);
			this.name.set(PredicateKey.DISABLED, true);
		}
		@Override public void recalculateSize(GUIInstance gui) {
			this.unpadded_height = name.height();
			this.unpadded_width = name.width();
		}
		@Override public void updateDrawInfo(GUIInstance gui) {
			Rectangle b = aligned_limit_rectangle();
			this.hover_rectangle(b);
			name.limit_rectangle(b);
		}
		@Override public void onSingleClick(GUIInstance gui) { onSingleClickName(gui); }
		@Override public void draw(GUIInstance gui, int depth) { 
			if (style().base_color() != gui.style().getProperty("default", "base_color", null)) {
				gui.canvas().color(style().base_color().color());
				gui.canvas().rect(hover_rectangle().expand(2), depth);
			}
		}
	}
	
	private   boolean 			populated 	 = false;
	private   GUIList 			sub_items 	 = new GUIList();	
	private   boolean 			is_directory = true;
	
	protected TXGUIDirectory 	drag_root;
	protected TXGUIDirectory 	parent;
	
	public TXGUIDirectory(String name) {
		root(new ItemNameplate(name));
		this.hidden(sub_items);
		draggable(true);
	}
	
	@Override 
	public void tickAnimation(GUIInstance gui) {
		if (is_directory && !collapsed && !populated) { populate(); populated = true; }
	}
	
	protected void add(TXGUIDirectory directory) {
		directory	.parent = this;
		sub_items	.add(directory);
		directory.drag_root = drag_root == null ? this : drag_root;
	}
	
	protected void is_directory(boolean is_directory) {
		this.is_directory = is_directory;
		if (!is_directory) {
			this.collapse_icon.icon(IconType.GENERIC_EDIT);
			this.collapse_icon.set(PredicateKey.DISABLED, true);
		}
	}
	
	public boolean is_directory() { return is_directory; }
	
	public 		abstract void 		onSingleClickName(GUIInstance gui);
	protected 	abstract void		populate();
	protected 	abstract void		onParentChange(TXGUIDirectory new_parent);
	
	// -- Dragging :: drop -- //

	@SuppressWarnings("unused")
	// TODO: probably make it so dragging onto a sub-element that is a file
	// just adds to its' parent
	private TXGUIDirectory directory_of(GUIList target) {
		GUIElement drop_parent = ((GUIElement) target).parent();
		if (!(drop_parent instanceof TXGUIDirectory)) return null;
		return (TXGUIDirectory) drop_parent;
	}
	
	@Override
	public boolean can_drop(GUIInstance gui, ElementReceiver target) {
		if (!(target instanceof TXGUIDirectory)) return false;
		TXGUIDirectory drop_parent = (TXGUIDirectory) target;
		return drop_parent == drag_root || drop_parent.drag_root == drag_root; 
	}
	
	@Override
	public void onDrop(GUIInstance gui, ElementReceiver target) {
		if (!(target instanceof TXGUIDirectory)) throw new IllegalStateException();
		TXGUIDirectory drop_parent = (TXGUIDirectory) target;
		if (drop_parent != this.parent) onParentChange(drop_parent);
		parent = drop_parent;
	}
	
	// -- Dragging :: reception -- //

	public boolean canDropHere(GUIInstance gui, GUIElement element) {
		return 	element 						 instanceof TXGUIDirectory 	&& 
				root().drag_rectangle()			.contains(gui.mouspos()) 	&& 
				this							.is_directory;
	}
	
	public void drop(GUIInstance gui, GUIElement element) {
		sub_items.add(element);
	}
	
	public void dropPreview(GUIInstance gui, GUIElement element) {
		gui.canvas().color(style().preview_color().color());
		gui.canvas().rect(root().drag_rectangle(), gui.PREVIEW_DEPTH());
	}
	
	public void DEBUG_draw_drop_areas(GUIInstance gui, GUIElement element, int depth) {
		gui.canvas().color(Color.DESYELLOW.val());
		gui.canvas().rect(root().drag_rectangle(), gui.PREVIEW_DEPTH());
	}
	
	public void remove(GUIElement subelement) { }

}

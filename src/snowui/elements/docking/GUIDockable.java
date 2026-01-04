package snowui.elements.docking;

import org.joml.Vector2i;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIText;
import snowui.elements.interfaces.ElementReceiver;
import snowui.elements.interfaces.SubElementReplaceable;
import snowui.utility.GUIUtility;

public class GUIDockable extends GUIElement implements ElementReceiver {
	
	GUIElement 	root;
	GUITitleBar titlebar = new GUITitleBar();
	
	{
		titlebar.set(PredicateKey.DISABLED, true);
		identifier("dockable");
		registerSubElement(titlebar);
		root(new GUIText("No content"));
	}
	
	public GUIDockable() { }
	public GUIDockable(String title) {
		titlebar.title.text(title);
	}

	public GUIDockable(String title, GUIElement root) {
		this(title);
		root(root);
	}
	
	public void root(GUIElement root) {
		if (this.root != null) this.removeSubElement(this.root);
		this.registerSubElement(root);
		this.root = root;
	}
	
	public GUIElement 	root() 		{ return this.root; }
	public GUITitleBar 	titlebar() 	{ return this.titlebar; }

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = GUIUtility.combined_height(sub_elements);
		this.unpadded_width = GUIUtility.max_width(sub_elements);
	}
	
	@Override
	public int floatwidth() { return 200; }
	
	@Override
	public int floatheight() { return 200; }

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = aligned_limit_rectangle();
		this.hover_rectangle(b);
		titlebar.limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top() + titlebar.height()));
		root.limit_rectangle(new Rectangle(b.left(), b.top() + titlebar.height(), b.right(), b.bottom()));
	}

	@Override public void draw(GUIInstance gui, int depth) {
		// gui.canvas().color(style().base_color().color());
		gui.canvas().color(titlebar.style().base_color().color());
		gui.canvas().rect(this.titlebar.limit_rectangle(), depth);
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(this.root.limit_rectangle(), depth);
	}
	
	@Override public Rectangle drag_rectangle() {
		return titlebar.hover_rectangle();
	}
	
	@Override
	public boolean canDropHere(GUIInstance gui, GUIElement element) {
		return 
			element instanceof GUIDockable && 
			this.aligned_limit_rectangle().contains(gui.mousepos()) &&
     	   !this.aligned_limit_rectangle().internal(edge, edge, 1-edge, 1-edge).contains(gui.mousepos());
	}
	
	SubElementReplaceable original_parent;
	GUIElement temporary_replacement = new GUIText("(Drag docker here to return)");
	
	{
		temporary_replacement.identifier("centered");
	}
	
	@Override
	public void onDragStart(GUIInstance gui) {
		if (parent() instanceof SubElementReplaceable) {
			original_parent = (SubElementReplaceable) parent();
			original_parent.replace(this, temporary_replacement);
		}
	}
	
	@Override
	public void onDrop(GUIInstance gui, ElementReceiver target) {
		if (target == null && original_parent != null) {
			original_parent.replace(temporary_replacement, this);
		} else {
			if (original_parent != null) original_parent.replace(temporary_replacement, null);
		}
		this.force_update_all();
	}

	float edge = 1f/4f;

	@Override
	public void drop(GUIInstance gui, GUIElement element) {
		Rectangle b = this.aligned_limit_rectangle();
		Rectangle left_side 	= b.internal(0, 		0, 		  edge, 	1);
//		Rectangle middle 		= b.internal(	 edge, 	edge, 	1-edge, 	1-edge);
		Rectangle right_side 	= b.internal(1 - edge, 	0, 		1, 			1);
		Rectangle top_side 		= b.internal(edge, 		0, 		1-edge, 	edge);
		Rectangle bottom_side 	= b.internal(edge, 		1-edge, 1-edge, 	1);
		
		// TODO: Tab List Stuff
		
//		GUIDockable insert = (GUIDockable) element;
//		
//		if (middle.contains(gui.mousepos())) 	{ 
//			if (parent() instanceof GUITabList) {
//				((GUITabList) parent()).add(insert.titlebar.title.text(), insert);
//			}
//			if (parent() instanceof GUISplit) {
//				GUITabList tabs = new GUITabList();
//				((SubElementReplaceable) parent()).replace(this, tabs);
//				tabs.add(		titlebar.title.text(), this);
//				tabs.add(insert.titlebar.title.text(), insert);
//			}
//		}
		
		SubElementReplaceable p_replace = null;
		
		// If this is in a GUISplit, it should insert the added element next to itself.
		if (parent() instanceof SubElementReplaceable) p_replace = (SubElementReplaceable) parent();
		
		// If this is in a GUITabList, it should insert the added element next to that tab list.
//		if (parent() instanceof GUITabList) {
//			if (parent().parent() instanceof SubElementReplaceable) {
//				p_replace = (SubElementReplaceable) parent().parent();
//			}
//		}

		Vector2i mp = gui.mousepos();
		if (p_replace != null) {
			if (left_side	.contains(mp)) p_replace.replace(this, new GUISplit(element, this));
			if (right_side	.contains(mp)) p_replace.replace(this, new GUISplit(this, element));
			if (top_side	.contains(mp)) p_replace.replace(this, new GUISplit(element, this).verticalify());
			if (bottom_side	.contains(mp)) p_replace.replace(this, new GUISplit(this, element).verticalify());
		}
	}

	@Override
	public void dropPreview(GUIInstance gui, GUIElement element) {
		Rectangle b = this.aligned_limit_rectangle();
		Rectangle left_side 	= b.internal(0, 		0, 		  edge, 	1);
//		Rectangle middle 		= b.internal(	 edge, 	edge, 	1-edge, 	1-edge);
		Rectangle right_side 	= b.internal(1 - edge, 	0, 		1, 			1);
		Rectangle top_side 		= b.internal(edge, 		0, 		1-edge, 	edge);
		Rectangle bottom_side 	= b.internal(edge, 		1-edge, 1-edge, 	1);
		Rectangle draw = null;
		if (left_side	.contains(gui.mousepos())) 	{ draw = left_side	; }
//		if (middle		.contains(gui.mousepos())) 	{ draw = middle		; }
		if (right_side	.contains(gui.mousepos())) 	{ draw = right_side	; }
		if (top_side	.contains(gui.mousepos())) 	{ draw = top_side	; }
		if (bottom_side	.contains(gui.mousepos())) 	{ draw = bottom_side; }
		if (draw != null) {
			gui.canvas().color(style().preview_color().color());
			gui.canvas().rect(draw, gui.PREVIEW_DEPTH());
		}
	}

	@Override
	public void DEBUG_draw_drop_areas(GUIInstance gui, GUIElement element, int depth) {
		Rectangle r = this.aligned_limit_rectangle();
		if (r.contains(gui.mousepos())) {
			gui.canvas().color(Color.DESBLUE.val());
		} else {
			gui.canvas().color(Color.DESYELLOW.val());
		}
		gui.canvas().rect(r, depth);
	}

	@Override
	public void remove(GUIElement subelement) {
		// TODO Auto-generated method stub
		
	}

}


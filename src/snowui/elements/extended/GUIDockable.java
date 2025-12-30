package snowui.elements.extended;

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
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = aligned_limit_rectangle();
		this.hover_rectangle(b);
		titlebar.limit_rectangle(new Rectangle(b.left(), b.top(), b.right(), b.top() + titlebar.height()));
		root.limit_rectangle(new Rectangle(b.left(), b.top() + titlebar.height(), b.right(), b.bottom()));
	}

	@Override public void draw(GUIInstance gui, int depth) {
		// gui.canvas().color(style().base_color().color());
		gui.canvas().color(Color.AQUA.val());
		gui.canvas().rect(this.titlebar.limit_rectangle(), depth);
		gui.canvas().color(Color.GOLD.val());
		gui.canvas().rect(this.root.limit_rectangle(), depth);
	}
	
	@Override public Rectangle drag_rectangle() {
		return titlebar.hover_rectangle();
	}
	
	@Override
	public boolean canDropHere(GUIInstance gui, GUIElement element) {
		return this.aligned_limit_rectangle().contains(gui.mouspos());
	}
	
	SubElementReplaceable original_parent;
	GUIElement temporary_replacement = new GUIText("DEBUG");;
	
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
	}

	@Override
	public void drop(GUIInstance gui, GUIElement element) {
		Rectangle b = this.aligned_limit_rectangle();
		float edge = 1f/3f;
		Rectangle left_side 	= b.internal(0, 		0, 		  edge, 	1);
		Rectangle middle 		= b.internal(	 edge, 	edge, 	1-edge, 	1-edge);
		Rectangle right_side 	= b.internal(1 - edge, 	0, 		1, 			1);
		Rectangle top_side 		= b.internal(edge, 		0, 		1-edge, 	edge);
		Rectangle bottom_side 	= b.internal(edge, 		1-edge, 1-edge, 	1);
		if (left_side	.contains(gui.mouspos())) 	{ 
			if (parent() instanceof SubElementReplaceable) {
				((SubElementReplaceable) parent()).replace(this, new GUISplit(element, this));
			}
		}
		if (middle		.contains(gui.mouspos())) 	{ 
			
			
			
		}
		if (right_side	.contains(gui.mouspos())) 	{ 
			if (parent() instanceof SubElementReplaceable) {
				((SubElementReplaceable) parent()).replace(this, new GUISplit(this, element));
			}
		}
		if (top_side	.contains(gui.mouspos())) 	{ 
			if (parent() instanceof SubElementReplaceable) {
				((SubElementReplaceable) parent()).replace(this, new GUISplit(element, this).verticalify());
			}
		}
		if (bottom_side	.contains(gui.mouspos())) 	{ 
			if (parent() instanceof SubElementReplaceable) {
				((SubElementReplaceable) parent()).replace(this, new GUISplit(this, element).verticalify());
			}
		}
	}

	@Override
	public void dropPreview(GUIInstance gui, GUIElement element) {
		Rectangle b = this.aligned_limit_rectangle();
		float edge = 1f/3f;
		Rectangle left_side 	= b.internal(0, 		0, 		  edge, 	1);
		Rectangle middle 		= b.internal(	 edge, 	edge, 	1-edge, 	1-edge);
		Rectangle right_side 	= b.internal(1 - edge, 	0, 		1, 			1);
		Rectangle top_side 		= b.internal(edge, 		0, 		1-edge, 	edge);
		Rectangle bottom_side 	= b.internal(edge, 		1-edge, 1-edge, 	1);
		Rectangle draw = null;
		if (left_side	.contains(gui.mouspos())) 	{ draw = left_side	; }
		if (middle		.contains(gui.mouspos())) 	{ draw = middle		; }
		if (right_side	.contains(gui.mouspos())) 	{ draw = right_side	; }
		if (top_side	.contains(gui.mouspos())) 	{ draw = top_side	; }
		if (bottom_side	.contains(gui.mouspos())) 	{ draw = bottom_side; }
		if (draw != null) {
			gui.canvas().color(style().preview_color().color());
			gui.canvas().rect(draw, gui.PREVIEW_DEPTH());
		}
	}

	@Override
	public void DEBUG_draw_drop_areas(GUIInstance gui, GUIElement element, int depth) {
		Rectangle r = this.aligned_limit_rectangle();
		if (r.contains(gui.mouspos())) {
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


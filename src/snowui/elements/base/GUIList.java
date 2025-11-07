package snowui.elements.base;

import java.util.ArrayList;
import java.util.function.Predicate;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.GUIElement;
import snowui.utility.GUIUtility;

public class GUIList extends GUIElement {
	
	{ identifier("list"); }
	
	//TODO: wrapping
	
	boolean vertical = true;
	boolean wrap = false;
	
	ArrayList<GUIElement> elements = new ArrayList<>();
	
	{ sub_elements = elements; }
	
	public GUIList() { }
	
	public GUIList(GUIElement...elements) {
		for (GUIElement e : elements) add(e);
	}
	
	public GUIList verticalify() {
		vertical = true;
		return this;
	}
	
	public GUIList horizontalify() {
		vertical = false;
		return this;
	}
	
	public void add(GUIElement e) {
		this.elements.add(e);
		this.should_recalculate_size = true;
	}
	
	public void add(GUIElement e, int index) {
		this.elements.add(index, e);
		this.should_recalculate_size = true;
	}
	
	public void set(GUIElement e, int index) {
		this.elements.set(index, e);
		this.should_recalculate_size = true;
	}
	
	public void removeIf(Predicate<? super GUIElement> predicate) {
		this.elements.removeIf(predicate);
		this.should_recalculate_size = true;
	}
	
	public int length() 			{ return elements.size();     }
	public GUIElement get(int index) { return elements.get(index); }

	@Override
	public void recalculateSize(GUIInstance gui) {
		if (wrap) {
			// size is n/a for wrapped lists, so...
			this.unpadded_height = 17;
			this.unpadded_width = 17;
		} else {
			if (vertical) {
				this.unpadded_height = GUIUtility.combined_height(elements);
				this.unpadded_width = GUIUtility.max_width(elements) + style().list_indent().pixels();
			} else {
				this.unpadded_height = GUIUtility.max_height(elements) + style().list_indent().pixels();
				this.unpadded_width = GUIUtility.combined_width(elements);
			}
		}
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle bounds = this.limit_rectangle();
		
		int left 	= bounds.left();
		int top 	= bounds.top();
		int right 	= bounds.right();
		int bottom 	= bounds.bottom();
		
		if (vertical) {
			left += style().list_indent().pixels();
		} else {
			top += style().list_indent().pixels();
		}
				
		for (GUIElement element : elements) {
			if (vertical) {
				bottom = top+element.height();
			} else {
				right = left+element.width();
			}
			element.limit_rectangle(new Rectangle(left, top, right, bottom));
			
			if (vertical) {
				top+=element.height();
				top+=style().list_spacing().pixels();
			} else {
				left+=element.width();
				left+=style().list_spacing().pixels();
			}
		}
		
		this.hover_rectangle(this.limit_rectangle());
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		
	}

		

}

/*

 - Collapsible List =
     Collapsible
	   List
	     -Collapsible
		    List
		 -Collapsible
		    List
		...

extends makes the alternation hidden by being
a Collapsible that already has a sub-element 'list'.


List
  - Horizontal & Vertical, ideally sharing the same code somehow
  - Support the ability to use wrapping instead of overflowing (scrolling)
  - Support culling properly


*/
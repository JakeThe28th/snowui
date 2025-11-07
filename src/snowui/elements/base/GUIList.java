package snowui.elements.base;

import java.util.ArrayList;
import java.util.function.Predicate;

import frost3d.utility.Log;
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
		if (!vertical) {
			this.should_recalculate_size = true;
			this.should_update = true;
		}
		vertical = true;
		return this;
	}
	
	public GUIList horizontalify() {
		if (vertical) {
			this.should_recalculate_size = true;
			this.should_update = true;
		}
		vertical = false;
		return this;
	}
	
	public GUIList wrap(boolean b) {
		if (wrap != b) {
			this.should_recalculate_size = true;
			this.should_update = true;
		}
		this.wrap = b;
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
			recalculateWrappedListSize();
		} else {
			if (vertical) {
				this.unpadded_height = GUIUtility.combined_height(elements);
				this.unpadded_width = GUIUtility.max_width(elements) + style().list_indent().pixels();
				this.unpadded_height += style().list_spacing().pixels()*elements.size();
			} else {
				this.unpadded_height = GUIUtility.max_height(elements) + style().list_indent().pixels();
				this.unpadded_width = GUIUtility.combined_width(elements);
				this.unpadded_width += style().list_spacing().pixels()*elements.size();
			}
		}
	}
	

	@Override
	protected void onLimitRectangleChange() { 
		if (wrap) { 
			recalculateWrappedListSize(); 
			should_update = true; // <-- without this, the hover rectangle
								  //     and probably other stuff gets messed up
		} 
	}
	
	/** Kinda scuffed, but the size of the list being wrong during drawing is more scuffed. */
	private void recalculateWrappedListSize() {
		log_element_update();
		if (padded_limit_rectangle() == null) {
			this.unpadded_height = 17;
			this.unpadded_width = 17;
		} else {
			if (vertical) {
				this.unpadded_height = 17;	// n/a for wrapped lists, so...
				this.unpadded_width = GUIUtility.wrapped_width(
						elements, 
						style().list_spacing().pixels(), 
						padded_limit_rectangle().height()-style().list_indent().pixels()
						);
			} else {
				this.unpadded_width = 17;	// n/a for wrapped lists, so...
				this.unpadded_height = GUIUtility.wrapped_height(
						elements, 
						style().list_spacing().pixels(), 
						padded_limit_rectangle().width()-style().list_indent().pixels()
						);
			}
		}
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle bounds = this.padded_limit_rectangle();
		
		int left 	= bounds.left();
		int top 	= bounds.top();
		int right 	= bounds.right();
		int bottom 	= bounds.bottom();
		
		if (vertical) {
			left += style().list_indent().pixels();
		} else {
			top += style().list_indent().pixels();
		}
		
		int max_element_width = 0;
		int max_element_height = 0;
				
		for (GUIElement element : elements) {
			if (wrap) {
				if (vertical) {
					if (top+element.height() > bounds.bottom()) {
						top = bounds.top();
						left += max_element_width;
						max_element_width = 0;
					}
				} else {
					if (left+element.width() > bounds.right()) {
						left = bounds.left();
						top += max_element_height;
						max_element_height = 0;
					}
				}
				if (element.width() > max_element_width) max_element_width = element.width();
				if (element.height() > max_element_height) max_element_height = element.height();
				bottom = top+element.height();
				right = left+element.width();
			} else {
				if (vertical) {
					bottom = top+element.height();
				} else {
					right = left+element.width();
				}
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
		
		this.hover_rectangle(this.padded_limit_rectangle());
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
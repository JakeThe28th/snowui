package snowui.elements.base;

import java.util.ArrayList;
import java.util.function.Predicate;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.ElementReceiver;
import snowui.utility.GUIUtility;
import snowui.utility.Margin;

public class GUIList extends GUIElement implements ElementReceiver {
	
	{ identifier("list"); }
	
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
			this.should_recalculate_size(true);
			this.should_update(true);
		}
		vertical = true;
		return this;
	}
	
	public GUIList horizontalify() {
		if (vertical) {
			this.should_recalculate_size(true);
			this.should_update(true);
		}
		vertical = false;
		return this;
	}
	
	public GUIList wrap(boolean b) {
		if (wrap != b) {
			this.should_recalculate_size(true);
			this.should_update(true);
		}
		this.wrap = b;
		return this;
	}
	
	public void add(GUIElement e) {
		this.elements.add(e);
		onRegisterSubElement(e);
		this.should_recalculate_size(true);
	}
	
	public void add(GUIElement e, int index) {
		if (index == elements.size()) {
			this.elements.add(e);
		} else {
			this.elements.add(index, e);
		}
		onRegisterSubElement(e);
		this.should_recalculate_size(true);
	}
	
	public void set(GUIElement e, int index) {
		onRemoveSubElement(elements.get(index));
		this.elements.set(index, e);
		onRegisterSubElement(e);
		this.should_recalculate_size(true);
	}
	
	public void removeIf(Predicate<? super GUIElement> predicate) {
		this.elements.removeIf(predicate);
		// TODO: call onRemoveSubElement for this eventually
		this.should_recalculate_size(true);
	}
	
	@Override
	public void remove(GUIElement subelement) {
		if (!elements.contains(subelement)) return;
		drop_points.remove(elements.indexOf(subelement));
		onRegisterSubElement(subelement);
		removeSubElement(subelement);
		elements.remove(subelement);
		this.should_recalculate_size(true);
	}	
	
	@SuppressWarnings("unchecked")
	public void clear() {
		for (GUIElement element : (ArrayList<GUIElement>) elements.clone()) {
			remove(element);
		}
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
			should_update(true); // <-- without this, the hover rectangle
								  //     and probably other stuff gets messed up
		} 
	}
	
	/** Kinda scuffed, but the size of the list being wrong during drawing is more scuffed. */
	private void recalculateWrappedListSize() {
		log_element_update();
		if (padded_limit_rectangle() == null) {
			this.unpadded_height = GUIUtility.max_height(sub_elements);
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
	
	ArrayList<Rectangle> drop_points = new ArrayList<Rectangle>();

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		
		drop_points.clear();
		
		Rectangle bounds = this.limit_rectangle();
		if (!wrap) {
			bounds = Margin.calculate(style(), bounds, unpadded_width, unpadded_height);
		} else {
			bounds = this.padded_limit_rectangle();
		}

		this.hover_rectangle(bounds);
		
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
			
			if (!wrap) {
				if (vertical) {
					drop_points.add(new Rectangle(left, bottom, right, top));
				} else {
					drop_points.add(new Rectangle(right, top, left, bottom));
				}
			}
			
		}
		
	}

	@Override public void draw(GUIInstance gui, int depth) { }

	@Override
	public boolean canDropHere(GUIInstance gui, GUIElement element) {
		for (Rectangle r : drop_points) {
			if (r.contains(gui.mousepos())) return true;
		}
		return false;
	}

	@Override
	public void drop(GUIInstance gui, GUIElement element) {
		for (int i = 0; i < drop_points.size(); i++) {
			Rectangle r = drop_points.get(i);
			if (r.contains(gui.mousepos())) {
				add(element, i+1);
			}
		}
	}

	@Override
	public void dropPreview(GUIInstance gui, GUIElement element) {
		for (Rectangle r : drop_points) {
			if (r.contains(gui.mousepos())) {
				gui.canvas().color(style().preview_color().color());
				gui.canvas().rect(r, gui.PREVIEW_DEPTH());
			}
		}
	}

	@Override
	public void DEBUG_draw_drop_areas(GUIInstance gui, GUIElement element, int depth) {		
		for (Rectangle r : drop_points) {
			if (r.contains(gui.mousepos())) {
				gui.canvas().color(Color.DESBLUE.val());
			} else {
				gui.canvas().color(Color.DESYELLOW.val());
			}
			gui.canvas().rect(r, depth);
		}
	}

}
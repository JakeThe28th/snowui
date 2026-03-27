package snowui.elements.extended;

import java.util.ArrayList;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.elements.abstracts.GUIElement;

// TODO: wip

public class GUIMasonryList extends GUIElement {

	int item_min_width = 92;
	int item_max_width = 220;
	int target_column_count = 5;

	protected ArrayList<GUIElement> elements = new ArrayList<>();
	
	{ sub_elements = elements; }
	
	{ identifier("masonry_list"); }

	public void add(GUIElement element) {
		elements.add(element);
		onRegisterSubElement(element);
		this.should_recalculate_size(true);

	}
	
	public void remove(GUIElement subelement) {
		if (!elements.contains(subelement)) return;
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
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		// TODO
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = limit_rectangle();
		hover_rectangle(b);

		int columns = target_column_count;
		int item_width = b.width() / columns;

		while (item_width > item_max_width) {
			columns++;
			item_width = b.width() / columns;
		}
		
		while (item_width < item_min_width && columns > 1) {
			columns--;
			item_width = b.width() / columns;
		}
		
		int xx = b.left();
		int yy = b.top();
		
		int[] heightmap = new int[elements.size()];
		
		int max_y = 0;
		
		int column = 0;
		
		for (int i = 0; i < elements.size(); i++) {

			int ww = item_width;
			int hh = (int) ((elements.get(i).height() / (double) elements.get(i).width()) * ww);
			
			if (i-columns >= 0) {
				yy = heightmap[(i-columns)];
			}

			heightmap[i] = yy + hh;
			if (yy + hh > max_y) max_y = yy + hh;
			
			elements.get(i).limit_rectangle(new Rectangle(xx, yy, xx + ww, yy + hh));
			
			xx += item_width;
			column++;
			if (column >= columns) {
				column = 0;
				xx = b.left();
			}

		}
		
		setWrappedHeight(max_y-b.top());
	}

	private void setWrappedHeight(int hh) {
		if (this.unpadded_height != hh) {
			this.unpadded_height = hh;
			should_recalculate_size(true); // to notify parents
		}
	}

}

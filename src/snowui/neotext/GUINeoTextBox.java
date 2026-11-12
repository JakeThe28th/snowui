package snowui.neotext;

import org.joml.Vector2i;

import frost3d.utility.Rectangle;
import neotext.layout.NTEditor;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIScrollable;

public class GUINeoTextBox extends GUIElement implements GUIScrollable.CanHandleOwnScrolling {
	
	static GUINeoTextBox focused_text_box = null;
	
	{ identifier("textbox"); }
	
	public class EditableText extends GUIElement {

		{ identifier("textbox-text"); }

		public NTEditor text_editor = new NTEditor();

		@Override public void onSingleClick(GUIInstance gui) { select(gui); }
		
		Rectangle area = new Rectangle(0,0,0,0);

		@Override public void recalculateSize(GUIInstance gui) {
			Vector2i size 		 = text_editor.document.cached_rendered_size();
			this.unpadded_height = size.y;
			this.unpadded_width  = size.x;
		}
		
		@Override
		public void updateDrawInfo(GUIInstance gui) {
			area = padded_limit_rectangle();
			hover_rectangle(area);
			// (every limit rectangle change should recalculate the text box's size)
			should_recalculate_size(true);
		}
		
		@Override
		public void tickAnimation(GUIInstance gui) {
			if (is_selected()) text_editor.tick(gui.rawinput(), gui.mousepos().sub(area.left(), area.top()));
		}
		
		@Override
		public void draw(GUIInstance gui, int depth) {
							   text_editor.document.render(area, gui.canvas());
			if (is_selected()) text_editor		   .render(area, gui.canvas());
		}
		
	}
	
	public EditableText text = new EditableText();
	
	public GUINeoTextBox(String string) {
		registerSubElement(text);
		text.text_editor.document.set_from_string(string);
	}
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width  = text.width();
		this.unpadded_height = text.height();
	}
	
	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(hover_rectangle(), depth);
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		hover_rectangle(padded_limit_rectangle());
		text.limit_rectangle(hover_rectangle());
	}

	@Override 
	public void onSingleClick(GUIInstance gui) { select(gui); }
	
	public boolean is_selected() { return (focused_text_box == this); }

	private void select(GUIInstance gui) {
		if (focused_text_box != null) focused_text_box.deselect();
		gui.rawinput().input_string("");
		focused_text_box = this;
		set(PredicateKey.SELECTED, true);
	}
	
	private void deselect() {
		if (!is_selected()) return;
		focused_text_box = null;
		set(PredicateKey.SELECTED, false);
	}
	
	@Override
	public boolean updateState(GUIInstance gui) {
		if (hover_rectangle() != null && gui.primary_click_released() && !hover_rectangle().contains(gui.mousepos())) deselect();
		return false;
	}
	
	public void show_toolbar(boolean v) {};
	public void show_scrollbars(boolean v) {};
	public void allow_search(boolean v) {}

	@Override
	public void set_x_scroll(int pixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set_y_scroll(int pixels) {
		// TODO Auto-generated method stub
		
	};
	
}

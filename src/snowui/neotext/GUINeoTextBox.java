package snowui.neotext;

import org.joml.Vector2i;
import org.joml.Vector4f;

import frost3d.enums.CursorType;
import frost3d.utility.Rectangle;
import neotext.layout.NTDocument;
import neotext.layout.NTEditor;
import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIScrollable;

public class GUINeoTextBox extends GUIElement implements GUIScrollable.CanHandleOwnScrolling {
	
	public static GUINeoTextBox focused_text_box = null;
		
	{ identifier("textbox"); }
	
	public class EditableText extends GUIElement {
		
		public void should_recalculate_size(boolean b){
			super.should_recalculate_size(b);
		}

		{ identifier("textbox-text"); }

		public NTEditor text_editor = new NTEditor() {
			@Override
			public void onTextModified(NTEditor self) {
				GUINeoTextBox.this.onTextModified(self.document.toPlainText());
				should_recalculate_size(true);
			}
			@Override
			public void onEnter(NTEditor self) {
				deselect();
			}
		};

		@Override public void onPress(GUIInstance gui) { 
			if (!is_selected()) {
				select(gui);
				text.text_editor.select_hovered(gui.rawinput(), gui.mousepos().sub(area.left(), area.top()), false, false); 
			}
		}
		
		Rectangle area = new Rectangle(0,0,0,0);

		@Override public void recalculateSize(GUIInstance gui) {
			Vector2i size 		 = text_editor.document.cached_rendered_size();
			this.unpadded_height = size.y;
			this.unpadded_width  = size.x;
		}
		
		@Override
		public void updateDrawInfo(GUIInstance gui) {
			area = aligned_limit_rectangle();
			hover_rectangle(area);
			// (every limit rectangle change should recalculate the text box's size)
			should_recalculate_size(true);
		}
		
		@Override
		public void tickAnimation(GUIInstance gui) {
			// TODO : the check in if (is_selected()) { ... } should prolly catch this
			if (unpadded_width != text_editor.document.cached_rendered_size().x) should_recalculate_size(true);
			// deselect when somewhere else is clicked
			if (hover_rectangle() != null && gui.primary_click_released() && !hover_rectangle().contains(gui.mousepos())) deselect();
			// tick editor
			if (is_selected()) {
				if (!gui.rawinput().input_string().isEmpty()) should_recalculate_size(true);
				text_editor.tick(gui.rawinput(), gui.mousepos().sub(area.left(), area.top()));
			}
			// cursor icon
			if (get(PredicateKey.HOVERED)) gui.cursor(CursorType.IBEAM_CURSOR);
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
	public void tickAnimation(GUIInstance gui) {
		// cursor icon
		if (get(PredicateKey.HOVERED)) gui.cursor(CursorType.IBEAM_CURSOR);
	}
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width  = text.width();
		this.unpadded_height = text.height();
	}
	
	@Override
	public void draw(GUIInstance gui, int depth) {
		if (show_background()) {
			gui.canvas().color(style().base_color().color());
			gui.canvas().rect(hover_rectangle(), depth);
		}
	}

	boolean show_background = true;
	
	public boolean show_background() { return show_background; }
	public void show_background(boolean v) { show_background = v;; }

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		hover_rectangle(aligned_limit_rectangle());
		text.limit_rectangle(hover_rectangle());
	}

	@Override 
	public void onPress(GUIInstance gui) { select(gui); }
	
	public boolean is_selected() { return (focused_text_box == this); }

	public void select(GUIInstance gui) {
		if (focused_text_box != null) focused_text_box.deselect();
		gui.rawinput().input_string("");
		focused_text_box = this;
		set(PredicateKey.SELECTED, true);
	}
	
	private void deselect() {
		if (!is_selected()) return;
		focused_text_box = null;
		set(PredicateKey.SELECTED, false);
		onFinish(text.text_editor.document.toPlainText());
	}
	
	public void show_toolbar(boolean v) {};
	public void show_scrollbars(boolean v) {};
	public void allow_search(boolean v) {}

	@Override
	public void set_x_scroll(int pixels) {
		text.text_editor.document.set_scroll_x(pixels);
	}

	@Override
	public void set_y_scroll(int pixels) {
		text.text_editor.document.set_scroll_y(pixels);
	};
	
	public void set_text(String plaintext, Vector4f color) {
		text.text_editor.document.set_from_string(plaintext);
		document().recolor_all(color);
		onTextModified(plaintext);
	}
	
	public void set_text(String plaintext, boolean silent) {
		text.text_editor.document.set_from_string(plaintext);
		if (!silent) onTextModified(plaintext);
		text.should_recalculate_size(true);
	}

	public NTDocument document() {
		return text.text_editor.document;
	}

	public NTEditor editor() {
		return text.text_editor;
	}
	
	
	public void onTextModified(String plaintext) {
		// callback
	}
	
	public void onFinish(String plaintext) {
		// callback
	}

	public void finish_on_enter(boolean b) {
		text.text_editor.finish_on_enter(b);
	}
	
}

package snowui.elements.extended;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import frost3d.Input;
import frost3d.implementations.SimpleTextRenderer;
import frost3d.utility.Log;
import frost3d.utility.NavigableLimitedStack;
import frost3d.utility.Rectangle;
import frost3d.utility.Utility;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;

public class GUITextBox extends GUIElement {
	
	GUITextBox textbox_this() { return this; }
	
	public static GUITextBox selected = null;
	
	class EditableText extends GUIElement {
		
		static record ContentState(String content, int cursor) {}
		
		int cursor = 20;
		
		int select_start = 0;
		int select_end =  0;

		private int select_end() {
			if (select_end > select_start) return select_end;
			return select_start;
		}

		private int select_start() {
			if (select_end > select_start) return select_start;
			return select_end;
		}
		
		{ identifier("textbox-text"); }
		
		String content;
		
		NavigableLimitedStack<ContentState> content_undo = new NavigableLimitedStack<>();

		public EditableText(String text) {
			content(text);
		}

		@Override
		public void recalculateSize(GUIInstance gui) {
			// N/A because of wrapping...
			gui.font_size(style().size().pixels());
			Vector2i size = gui.canvas().textrenderer().size(content);
			this.unpadded_width = size.x;
			this.unpadded_height = size.y;
			
			// TODO: figure out how to make this DRY compliant 
			// [and also remember to update this whenever the word wrap logic in draw() changes]
			
			int xx = 0, yy = 0, i = 0;
			int line_height = unpadded_height;
			
			int text_width = this.unpadded_width;
			if (this.hover_rectangle() != null) text_width = this.hover_rectangle().width();

			while(i < content.length()) {
				int next_breakable_index = i;
				int next_size = 0;
				while (next_breakable_index < content.length()) {
					if (content.charAt(next_breakable_index) == ' ') break;
					if (content.charAt(next_breakable_index) == '-') break;
					next_size += gui.textrenderer().advance(content, next_breakable_index);
					next_breakable_index ++;
				}
				if (next_size < text_width && xx + next_size > text_width) {
					xx = 0;
					yy += line_height;
				}
				while (i < next_breakable_index + 1 && i < content.length()) {
					int advance = gui.textrenderer().advance(content, i);
					if (xx + advance > text_width || content.charAt(i) == '\n') {
						xx = 0;
						yy += line_height;
					}
					xx += advance;
					i++;
				}
			}
			
			this.unpadded_height += yy;
			
		}

		@Override
		public void updateDrawInfo(GUIInstance gui) {
			this.hover_rectangle(padded_limit_rectangle());
			this.should_recalculate_size(true);
		}
		
		private void setcursor(int new_position, boolean shift) {
			cursor = new_position;
			setinputtime();
			if (!shift) {
				select_start = cursor + 1;
			}
			select_end = cursor + 1;
		}
		
		private void fix_cursor_pos() {
			if (cursor < -1) cursor = -1;
			if (cursor >= content.length()) cursor = content.length()-1;
		}
		
		public void content(String new_content) {
			if (content == null || !content.equals(new_content)) {
				content = new_content;
				content_undo.push(new ContentState(content, cursor));
				fix_cursor_pos();
				setinputtime();
				onTextChange(content);
				this.should_recalculate_size(true);
			}
		}
		
		private boolean delete_selection() {
			if (select_end() - select_start() > 0) {
				int start 	= select_start();
				int end 	= select_end();
				setcursor(start, false);
				content(content.substring(0, start) + content.substring(end));
				return true;
			}
			return false;
		}

		public String selected_string() {
			return content.substring(select_start(), select_end());
		}
		
		@Override
		public void draw(GUIInstance gui, int depth) {
			
			Rectangle b = this.hover_rectangle();
			SimpleTextRenderer text = gui.textrenderer();
			int xx = 0;
			int yy = 0;
			int i = 0;
			int line_height = text.size(content).y;
			gui.canvas().color(style().base_color().color());
			
			gui.font_size(style().size().pixels());
			
			if (is_selected() && -1 == cursor && blink()) {
				text.character(gui.canvas(), b.left(), b.top() + yy, depth, '|');
			}
			
			while(i < content.length()) {
				
				int next_breakable_index = i;
				int next_size = 0;
				while (next_breakable_index < content.length()) {
					if (content.charAt(next_breakable_index) == ' ') break;
					if (content.charAt(next_breakable_index) == '-') break;
					next_size += text.advance(content, next_breakable_index);
					next_breakable_index ++;
				}
				
				// 'next_size' reflects the length of the current word, assuming
				// words are delimited by the above breakable characters.
				
				// If next_size >= b.width, then it's too big to fit and will
				// be split by character in the loop anyways, so do nothing.
				
				if (next_size < b.width() && xx + next_size > b.width()) {
					xx = 0;
					yy += line_height;
				}
				
				while (i < next_breakable_index + 1 && i < content.length()) {
					int advance = text.advance(content, i);
					if (xx + advance > b.width() || content.charAt(i) == '\n') {
						xx = 0;
						yy += line_height;
					}
					if (is_selected()) {
					if (i >= select_start() && i < select_end() || gui.primary_click_down() || gui.primary_click_pressed() || preselect_mouse != null) {
						int offset = 2;	
						Rectangle letter_rect = new Rectangle(
								b.left() + xx, 
								b.top() + yy + offset, 
								b.left() + xx + advance,  
								b.top() + yy + offset+ line_height
							);
						
						if (gui.primary_click_down() && letter_rect.contains(gui.mouspos())) {
							setcursor(i, true);
						}
						
						if (preselect_mouse != null && letter_rect.contains(preselect_mouse)) {
							setcursor(i, false);
							preselect_mouse = null;
						}
							
						if (gui.primary_click_pressed() && letter_rect.contains(gui.mouspos())) {
							if (gui.rawinput().keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
								setcursor(i, true);
							} else {
								select_start = cursor + 1;
							}
						}
						
						if (i >= select_start() && i < select_end()) {
							gui.canvas().color(Color.TRANSPARENT_AQUA.val());
							gui.canvas().rect(letter_rect, depth);
							gui.canvas().color(style().base_color().color());
						}
					}	
					}
					text.character(gui.canvas(), b.left() + xx, b.top() + yy, depth + 2, content.charAt(i));
					if (is_selected() && i == cursor && blink()) {
						text.character(gui.canvas(), b.left() + xx + (advance/2), b.top() + yy, depth, '|');
					}
					xx += advance;
					i++;
				}
				
			}
			
			performTextEditing(gui);
			
		}
		
		private void performTextEditing(GUIInstance gui) {
			try {
				
				if (is_selected()) {
					Input i = gui.rawinput();
					
					boolean shift = i.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
					
					if (i.keyPressed(GLFW.GLFW_KEY_LEFT) || i.keyRepeated(GLFW.GLFW_KEY_LEFT)) {
						setcursor(cursor-1, shift);
					}
					if (i.keyPressed(GLFW.GLFW_KEY_RIGHT) || i.keyRepeated(GLFW.GLFW_KEY_RIGHT)) {
						setcursor(cursor+1, shift);
					}
					
					fix_cursor_pos();
					
					if (i.keyPressed(GLFW.GLFW_KEY_BACKSPACE) || i.keyRepeated(GLFW.GLFW_KEY_BACKSPACE)) {
						boolean had_selection = delete_selection();
						if (!had_selection && cursor > -1) {
							setcursor(cursor-1, false);
							content(content.substring(0, cursor + 1) + content.substring(cursor + 2));
						}
					}
					
					if (i.keyPressed(GLFW.GLFW_KEY_DELETE) || i.keyRepeated(GLFW.GLFW_KEY_DELETE)) {
						if (content.length() > 1) {
							content(content.substring(0, cursor + 1) + content.substring(cursor + 2));
						} else if (content.length() > 0) {
							content("");
						}
					}
					
					if (i.keyPressed(GLFW.GLFW_KEY_ENTER) || i.keyRepeated(GLFW.GLFW_KEY_ENTER)) {
						if (finish_on_enter()) {
							deselect();
						} else {
							i.input_string(i.input_string() + "\n");
						}
					}
					
					if (i.input_string() != "") {
						delete_selection();
						int t_cursor = cursor;
						setcursor(t_cursor + i.input_string().length(), false);
						if (content.length() > 0) {
							content(content.substring(0, t_cursor + 1) + i.input_string() + content.substring(t_cursor + 1));
						} else {
							content(content + i.input_string());
						}
						i.input_string("");
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && i.keyPressed(GLFW.GLFW_KEY_Z) || i.keyRepeated(GLFW.GLFW_KEY_Z)) {
						if (content_undo.index() > 0) {
							ContentState state = content_undo.prev();
							content = state.content;
							cursor = state.cursor;
						}
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && i.keyPressed(GLFW.GLFW_KEY_Y) || i.keyRepeated(GLFW.GLFW_KEY_Y)) {
						if (content_undo.index() < content_undo.size() -1) {
							ContentState state = content_undo.next();
							content = state.content;
							cursor = state.cursor;
						}
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && i.keyPressed(GLFW.GLFW_KEY_C)) {
						Input.setClipboardString(selected_string());
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && i.keyPressed(GLFW.GLFW_KEY_X)) {
						Input.setClipboardString(selected_string());
						delete_selection();
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && i.keyPressed(GLFW.GLFW_KEY_V)) {
						i.input_string(i.input_string() + Input.getClipboardString());
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && i.keyPressed(GLFW.GLFW_KEY_A)) {
						select_start = 0;
						select_end = content.length();
					}
					
//					gui.canvas().color(Color.BLACK.val());
//					gui.canvas().text(10, 10, depth, content_undo.index() + "");
//					gui.canvas().text(10, 40, depth, content_undo.size() + "");
				
					
				}
				
				} catch (Exception e) {
					Log.trace(e);
					String old_content = content;
					Input.setClipboardString(old_content);
					content = "Error while modifying text. Previous contents have been copied to your clipboard.";
					content += "\n\n--- ERROR ---\n\n";
					content += Utility.getStackTrace(e);
					content += "\n\n--- PREVIOUS CONTENT ---\n\n";
					content += old_content;
				}
		}

		long last_input_time = System.currentTimeMillis();

		private boolean blink() {
			if (System.currentTimeMillis() - last_input_time < 500) return true;
			return (System.nanoTime() / 1000000000) % 2 == 0;
		}
		
		private void setinputtime() {
			last_input_time = System.currentTimeMillis();
		}

		@Override 
		public void onSingleClick(GUIInstance gui) {
			select(gui);
		}
		
	}
	
	boolean finish_on_enter = false;
	
	private Vector2i preselect_mouse;
	
	public boolean finish_on_enter() {
		return finish_on_enter;
	}
	
	public void finish_on_enter(boolean b) {
		finish_on_enter = b;
	}
	
	boolean is_selected() {
		return selected == this;
	}
	
	public void select(GUIInstance gui) {
		if (selected == this) return;
		gui.rawinput().input_string("");
		selected = this;
		set(PredicateKey.SELECTED, true);
		text.setcursor(0, false);
		preselect_mouse = gui.mouspos();
	}
	
	@Override 
	public void onSingleClick(GUIInstance gui) {
		select(gui);
	}

	@Override
	public boolean updateState(GUIInstance gui) {
		if (gui.primary_click_released() && !hover_rectangle().contains(gui.mouspos())) {
			deselect();
		}
		return false;
	}
	
	public void deselect() {
		set(PredicateKey.SELECTED, false);
		if (is_selected()) selected = null;
	}

	EditableText text;
	
	{ identifier("textbox"); }
	
	public GUITextBox(String text) {
		this.text = new EditableText(text);
		this.registerSubElement(this.text);
	}
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_width = text.width();
		this.unpadded_height = text.height();
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		hover_rectangle(padded_limit_rectangle());
		text.limit_rectangle(hover_rectangle());
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		gui.canvas().color(style().base_color().color());
		gui.canvas().rect(hover_rectangle(), depth);
	}
	
	public void onTextChange(String new_text) {
		
	}

}

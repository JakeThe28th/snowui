package snowui.elements.base;

import org.joml.Vector2i;
import org.joml.Vector4f;
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

// TODO : ctrl + backspace = delete word
// TODO : ctrl + arrows = move forward and backward word
// TODO : up/down arrow also

// TODO : add scrollbars, (!wrap) = horizontally, (wrap) = vertically

public class GUITextBox extends GUIElement {
	
	GUITextBox textbox_this() { return this; }
	
	public static GUITextBox selected = null;
	
	public class EditableText extends GUIElement {
		
		// --- text editing functionality stuff --- //
		
		/** Handles all text input that doesn't require knowing what character was clicked */
		private void performTextEditing(GUIInstance gui) {
			try {
				if (is_selected()) {
					Input i = gui.rawinput();
					
					boolean shift = i.keyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
					
					if (keyTyped(i, GLFW.GLFW_KEY_LEFT  )) 	setcursor(cursor-1, shift);
					if (keyTyped(i, GLFW.GLFW_KEY_RIGHT )) 	setcursor(cursor+1, shift);
					if (keyTyped(i, GLFW.GLFW_KEY_HOME  )) 	setcursor(0, shift);
					
					// Kinda scuffed implementation of going up and down lines...
					if (brittle_coordinates != null && (keyTyped(i, GLFW.GLFW_KEY_DOWN) || keyTyped(i, GLFW.GLFW_KEY_UP))) { 
						Vector2i[] c = brittle_coordinates;
						
						Vector2i v = c[cursor];
						v.x = c[cursor_for_vertical_navigation].x;

						int target_height = 0;
						
						if (keyTyped(i, GLFW.GLFW_KEY_DOWN)) {
							for (int n = cursor+1; n < c.length; n++) if (c[n].y > v.y) { target_height = c[n].y; break; }
						}
						
						if (keyTyped(i, GLFW.GLFW_KEY_UP)) {
							for (int n = cursor-1; n > 0; n--)        if (c[n].y < v.y) { target_height = c[n].y; break; }
						}
						
						int new_cursor = cursor;
						
						if (keyTyped(i, GLFW.GLFW_KEY_DOWN)) {
							for (int n = cursor+1; n < c.length; n++) 
								if (c[n].y == target_height) { new_cursor = n; if (c[n].x >= v.x) { break; } }
						}
						
						if (keyTyped(i, GLFW.GLFW_KEY_UP)) {
							for (int n = cursor-1; n > 0; n--) 
								if (c[n].y == target_height) { new_cursor = n; if (c[n].x <= v.x) { break; } }
						}
						
						int old = cursor_for_vertical_navigation;
						setcursor(new_cursor, shift); 
						cursor_for_vertical_navigation = old;
					}
					
					fix_cursor_pos();
					
					if (keyTyped(i, GLFW.GLFW_KEY_BACKSPACE)) {
						boolean had_selection = delete_selection();
						if (!had_selection && cursor > -1) {
							setcursor(cursor-1, false);
							content(content.substring(0, cursor + 1) + content.substring(cursor + 2));
						}
					}
					
					if (keyTyped(i, GLFW.GLFW_KEY_DELETE)) {
						if (content.length() > 1) {
							content(content.substring(0, cursor + 1) + content.substring(cursor + 2));
						} else if (content.length() > 0) {
							content("");
						}
					}
					
					if (keyTyped(i, GLFW.GLFW_KEY_ENTER)) {
						if (finish_on_enter()) {
							deselect();
						} else {
							i.input_string(i.input_string() + "\n");
						}
					}
					
					if (keyTyped(i, GLFW.GLFW_KEY_ESCAPE)) {
						deselect();
					}
					
					if (keyTyped(i, GLFW.GLFW_KEY_TAB)) {
						i.input_string(i.input_string() + "\t");
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
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && keyTyped(i, GLFW.GLFW_KEY_Z)) {
						if (content_undo.index() > 0) {
							ContentState state = content_undo.prev();
							content = state.content;
							cursor = state.cursor;
							invalidate_brittle();
							onTextChange(content);
						}
					}
					
					if (i.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL) && keyTyped(i, GLFW.GLFW_KEY_Y)) {
						if (content_undo.index() < content_undo.size() -1) {
							ContentState state = content_undo.next();
							content = state.content;
							cursor = state.cursor;
							invalidate_brittle();
							onTextChange(content);
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

		static record ContentState(String content, int cursor) {}
		NavigableLimitedStack<ContentState> content_undo = new NavigableLimitedStack<>();
		
		int cursor_for_vertical_navigation;
		
		int cursor, select_start, select_end;
		
		String content;
		String preselect_content;

		private int select_end  () { return (select_end > select_start) ? select_end   : select_start; }
		private int select_start() { return (select_end > select_start) ? select_start : select_end;   }
		
		private void setcursor(int new_position, boolean shift) {
			setinputtime();
			cursor = new_position;
			if (!shift) select_start = cursor + 1;
			select_end = cursor + 1;
			cursor_for_vertical_navigation = cursor;
		}
		
		private void fix_cursor_pos() {
			if (cursor < -1) cursor = -1;
			if (cursor >= content.length()) cursor = content.length()-1;
		}
		
		public void content(String new_content) {
			if (content == null || !content.equals(new_content)) {
				content = new_content;
				invalidate_brittle();
				content_undo.push(new ContentState(content, cursor));
				fix_cursor_pos();
				setinputtime();
				onTextChange(content);
				should_recalculate_size(true);
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
		
		boolean keyTyped(Input i, int key) { return i.keyPressed(key) || i.keyRepeated(key); }
		
		// --- ----------  gui stuff  ---------- --- //
		
		// TODO: Refactor/Remove this eventually
		Vector4f[] brittle_colormap;
		Vector2i[] brittle_coordinates;
		
		boolean update_coords = true;
		
		private void invalidate_brittle() {
			brittle_colormap = null;
			if (brittle_coordinates != null) {
				brittle_coordinates = new Vector2i[content.length()];
				update_coords = true;
			}
		}
		
		/** Sets colors. These colors will go away when the text is modified. */
		public void brittle_color(int index, Vector4f color) {
			if (brittle_colormap == null) {
				brittle_colormap = new Vector4f[content.length()];
			}
			brittle_colormap[index] = color;
		}
		
		public Vector2i brittle_coordinate(int index) {
			if (index >= brittle_coordinates.length) return null;
			return brittle_coordinates[index];
		}
		
		/** Draws the text, or returns the height of the text. 
		 *  Also, handles actions that rely on clicking a specific character of text. */
		private Vector2i performTextDrawing(Rectangle b, Rectangle outer, GUIInstance gui, int depth, boolean draw) {
			
			gui.font_size(style().size().pixels());

			SimpleTextRenderer text = gui.textrenderer();
			int line_height = text.size(content).y;
			
			int xx = 0, yy = 0, i = 0;
			
			int max_xx = xx;
			
			int cursor_width = 0;
			
			int tab_width = 4 * text.advance(" ", 0);
						
			if (draw) {
				gui.canvas().color(style().base_color().color());
				// If the cursor is at the start, it won't be drawn in the while loop.
				if (is_selected() && -1 == cursor && blink()) {
					text.character(gui.canvas(), b.left(), b.top() + yy, depth, '|');
				}
				cursor_width = text.size("|").x / 2;
			}
			
			boolean clicking = draw && (gui.primary_click_down() || gui.primary_click_pressed() || preselect_mouse != null);

			Rectangle[] characters = null;
			if (clicking) { characters = new Rectangle[content.length()]; }
						
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
				
				boolean wrapped = false;

				if (wrap) {
					if (next_size < b.width() && xx + next_size > b.width()) {
						xx = 0;
						yy += line_height;
						wrapped = true;
					}
				}
				
				while (i < next_breakable_index + 1 && i < content.length()) {
					int advance = text.advance(content, i);
					if (content.charAt(i) == '\n') {
						advance = 0;
					}
					if (content.charAt(i) == '\t') {
						//advance = tab_width;
						advance = (((xx / tab_width) + 1) * tab_width) - xx;
					}
					if ((wrap && xx + advance > b.width()) || content.charAt(i) == '\n') {
						xx = 0;
						yy += line_height;
						wrapped = true;
					}
					if (wrapped && content.charAt(i) == ' ') {
						advance = 0;
					}
					if (draw) {
						if (is_selected()) {
						if (i >= select_start() && i < select_end() || clicking) {
							int offset = 2;	
							
							int left 	= b.left() + xx;
							int top		= b.top() + yy + offset;
							int right 	= b.left() + xx + advance;
							int bottom 	= b.top() + yy + offset+ line_height;
							
							if (xx == 0) left 	= outer.left();
							if (yy == 0) top 	= outer.top();
							
							if (i == content.length()-1) right 	= outer.right();

							Rectangle letter_rect = new Rectangle(left, top, right, bottom);
							
							if (clicking) characters[i] = letter_rect;
							
							if (wrapped) {
								if (clicking) characters[i-1] =
									new Rectangle(
										characters[i-1]	.left(), 
										characters[i-1]	.top(), 
										outer			.right(), 
										characters[i-1]	.bottom()
									);
							}
							
							if (i >= select_start() && i < select_end()) {
								gui.canvas().color(Color.TRANSPARENT_AQUA.val());
								gui.canvas().rect(letter_rect, depth);
								gui.canvas().color(style().base_color().color());
							}
						}	
						}
						
						// Set color
						if (brittle_colormap != null && brittle_colormap[i] != null) gui.canvas().color(brittle_colormap[i]);
						text.character(gui.canvas(), b.left() + xx, b.top() + yy, depth + 2, content.charAt(i));
						// Reset color
						if (brittle_colormap != null && brittle_colormap[i] != null) gui.canvas().color(style().base_color().color());
						
						if (brittle_coordinates != null && update_coords && draw) {
							brittle_coordinates[i] = new Vector2i(b.left() + xx, b.top() + yy);
						}
						
						if (is_selected() && i == cursor && blink()) {
							text.character(gui.canvas(), (b.left() - cursor_width) + xx + (advance), b.top() + yy, depth, '|');
						}
					}
					wrapped = false;
					xx += advance;
					if (max_xx < xx) max_xx = xx;
					i++;
				}
			}
			
			if (characters != null) for (int c = 0; c < content.length(); c++) {
				
				Rectangle letter_rect = characters[c];
				if (letter_rect == null) continue;
				
				if (gui.primary_click_down() && letter_rect.contains(gui.mousepos())) {
					// DEBUG : display bounding box of clicked character
//					gui.canvas().color(Color.RED.val());
//					gui.canvas().rect(letter_rect, depth);
//					gui.canvas().color(style().base_color().color());
					setcursor(c, true);
				}
				
				if (preselect_mouse != null && letter_rect.contains(preselect_mouse)) {
					setcursor(c, false);
					preselect_mouse = null;
				}
					
				if (gui.primary_click_pressed() && letter_rect.contains(gui.mousepos())) {
					if (gui.rawinput().keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
						setcursor(c, true);
					} else {
						select_start = cursor + 1;
					}
				}
			}
						
			if (draw) update_coords = false;
			
			if (!draw) {
				return new Vector2i(max_xx, yy + line_height);
			} else {
				return null;
			}
		}
	
		{ identifier("textbox-text"); }
		
		@Override
		public int width() {
			if (!wrap) {
				return super.width();
			} else {
				throw new IllegalStateException("Attempted to get the width of a wrapped textbox");
			}
		}
		
		boolean wrap 		= false;
		int		wrap_width 	= 0;
		
		public void wrap_width(int val, GUIInstance gui) {
			val = style().unpadw(val);
			wrap = true;
			if (wrap_width != val) {
				wrap_width = val;
				this.unpadded_width = val;
				this.unpadded_height = performTextDrawing(new Rectangle(0,0,wrap_width,0), null, gui, -1, false).y;
				textbox_this().should_recalculate_size(true);
			}
		}
		
		public EditableText(String text) {
			content(text);
		}
		
		@Override
		public void recalculateSize(GUIInstance gui) {			
			if (wrap) {
				// In case the content updated, the height is be recalculated here so the parent knows if it needs to update.
				this.unpadded_height = performTextDrawing(new Rectangle(0,0,wrap_width,0), null, gui, -1, false).y;
				textbox_this().should_recalculate_size(true);
			} else {
//				gui.font_size(style().size().pixels());
//				Vector2i size = gui.canvas().textrenderer().size(content);
//				this.unpadded_width = size.x;
//				int newline_count = 1;
//				for (int i = 0; i < content.length(); i++) if (content.charAt(i) == '\n') newline_count++;
//				this.unpadded_height = size.y * newline_count;
				Vector2i size = performTextDrawing(null, null, gui, -1, false);
				this.unpadded_height = size.y;
				this.unpadded_width  = size.x;

			}

		}

		@Override
		public void updateDrawInfo(GUIInstance gui) {
			this.hover_rectangle(padded_limit_rectangle());
		}
		
		@Override
		public void draw(GUIInstance gui, int depth) {
			performTextEditing(gui); // With the introduction of brittle properties, 
									 // this needs to run *before* drawing, since otherwise
									 // the brittle coordinate map won't update until
									 // the next loop.
			performTextDrawing(hover_rectangle(), limit_rectangle(), gui, depth, true);
		}
		
		long last_input_time = System.currentTimeMillis();
		private void setinputtime() { last_input_time = System.currentTimeMillis(); }

		private boolean blink() { return (System.currentTimeMillis() - last_input_time < 500) || ((System.nanoTime() / 1000000000) % 2 == 0); }
		

		@Override public void onSingleClick(GUIInstance gui) { select(gui); }

		public void 	force_text(String string) 	{ content(string); }
		public String	text() 						{ return content; }
		
	}
	
	// --== -------------- Outer text box -------------- ==-- //
	
	public GUITextBox(String text) {
		this.text = new EditableText(text);
		this.registerSubElement(this.text);
	}
	
	EditableText text;
	public EditableText text() { return text; }

	{ identifier("textbox"); }
	
	boolean finish_on_enter = false;
	boolean hide_background	= false;
	
	private Vector2i preselect_mouse;
	
	public boolean 	finish_on_enter() 			{ return finish_on_enter		; }
	public void 	finish_on_enter(boolean b) 	{ 		 finish_on_enter = b	; }
	public void 	hide_background(boolean b) 	{ 		 hide_background = b	; }
		   boolean	is_selected() 				{ return selected == this		; }
	
	public void select(GUIInstance gui) {
		if (selected == this) return;
		selected = this;
		text.preselect_content = text.content;
		gui.rawinput().input_string("");
		set(PredicateKey.SELECTED, true);
		text.setcursor(0, false);
		preselect_mouse = gui.mousepos();
	}
	
	public void deselect() {
		set(PredicateKey.SELECTED, false);
		if (!is_selected()) return;
		selected = null;
		onFinishEditing(text.preselect_content, text.content);
	}

	@Override 
	public void onSingleClick(GUIInstance gui) { select(gui); }

	@Override
	public boolean updateState(GUIInstance gui) {
		if (hover_rectangle() != null && gui.primary_click_released() && !hover_rectangle().contains(gui.mousepos())) deselect();
		return false;
	}
	
	@Override
	public void recalculateSize(GUIInstance gui) {
		if (text.wrap) {
			// N/A
		} else {
			this.unpadded_width = text.width();
		}
		this.unpadded_height = text.height();
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		hover_rectangle(padded_limit_rectangle());
		text.limit_rectangle(hover_rectangle());
	}

	@Override
	public void draw(GUIInstance gui, int depth) {
		if (!hide_background) {
			gui.canvas().color(style().base_color().color());
			gui.canvas().rect(hover_rectangle(), depth);
		}
	}

	public void set_text(String string) {
		text.force_text(string);
	}

	public void wrap_width(int width, GUIInstance gui) {
		text.wrap_width(style().unpadw(width), gui);
	}
	
	// --== -------------- Callbacks -------------- ==-- //

	public void onTextChange(String new_text) {
		
	}
	
	/** NOTE: Editing the sub_elements array of a parent during this callback will
	 *  cause {ConcurrentModificationException}s. TODO: maybe fix that lol */
	public void onFinishEditing(String old_text, String new_text) {
		
	}
	
	public void save_character_coordinates(boolean b) {
		if (b) {
			text.brittle_coordinates = new Vector2i[text.content.length()];
		} else {
			text.brittle_coordinates = null;
		}
	}

}

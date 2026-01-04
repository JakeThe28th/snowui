package snowui.utility;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import frost3d.Input;
import frost3d.implementations.SimpleCanvas;
import frost3d.interfaces.F3DCanvas;
import frost3d.utility.Utility;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.docking.GUISplit;
import snowui.elements.interfaces.ElementReceiver;
import snowui.support.DragAndDropSupport;

public class GUIDebugger {
	
	public static interface IDCDataConverter {
		public String dataToString(int d);
	}
	
	public static class IntegerDataChart {
		
		int 		size;
		int 		channels;
		int[][] 	data;
		int 		index = 0;;
		Vector4f[] 	colors;
		String[] 	channel_names;
		
		int 		self_channel_count = 1;
		
		
		boolean show_self_channels = true;
		public boolean show_self_channels() { return show_self_channels; }
		public void show_self_channels(boolean v) { show_self_channels = v; }
		
		int			y_axis_interval = 100;
		
		IDCDataConverter data_converter = new IDCDataConverter() {
			public String dataToString(int d) {
				return "Data: " + d;
			}
		};
		
		public IntegerDataChart(int size, int channels) {
			channels += self_channel_count;
			this.size = size;
			this.channels = channels;
			this.data = new int[size][channels];
			this.colors = new Vector4f[channels];
			
			randomcolors();
			
			this.channel_names = new String[channels];
			for (int i = 0; i < channels; i++) {
				this.channel_names[i] = "Channel["+(i-self_channel_count)+"]";
			}
			if (self_channel_count > 0) this.channel_names[0] = "Other";

		}
		
		public void randomcolors() {
			for (int i = 0; i < channels; i++) {
				float r = i / ((float) channels);
				float g = (float) Math.random();
				float b = (float) Math.random();
				float sum = r + g + b;
				this.colors[i] = new Vector4f(r/sum, g/sum, b/sum, 1);
			}
		}
		
		long last_advance_nanotime = 0;

		public void advance() { 
			if (self_channel_count > 0) {
				int other = (int) (System.nanoTime()-this.last_advance_nanotime);
				this.last_advance_nanotime = System.nanoTime();
				for (int i = 1; i < channels; i++) {
					other -= data[index][i];
				}
				if (other < 0) other = 0;
				add(other, -1);
			}
			index++; 
			if (index >= size) index = 0; 
		}
		
		public void add(int n) { add(n, self_channel_count); }
		public void add(int n, int channel) {
			data[index][channel+self_channel_count] = n;
		}
		
		public void draw(F3DCanvas canvas, int x, int y, int depth, float scale_y, int advance) {
			
			// Color labels
			int lineheight = canvas.textrenderer().size(channel_names[0]).y;
			int m = 5;
			int sep = 2;
			int lines = 4;
			int namex = x;
			int namey = y -= ( (lineheight*lines) + ((m*2)*lines) + (sep*lines-1));
			for (int channel = channels-1; channel >= 0; channel--) {
				if ((self_channel_count < 1 || !show_self_channels) && channel == 0) continue;
				String name = channel_names[channel];
				int width = canvas.textrenderer().size(name).x;
				int height = canvas.textrenderer().size(name).y;				
				int color_square_size = height;
				canvas.color(Color.BLACK75.val());
				canvas.rect(namex, namey, namex+width+color_square_size+(m*3), namey+height+(m*2), depth + channels+2);
				canvas.color(colors[channel]);
				canvas.rect(namex+m, namey+m, namex+m+color_square_size, namey+m+color_square_size, depth + channels+2);
				canvas.color(Color.WHITE.val());
				canvas.text(namex+m+color_square_size+m, namey+m, depth + channels+3, name);
				namex += width + (m*3) + sep + color_square_size;
				if (namex - x > canvas.width()/3) {
					namex = x;
					namey += lineheight + (m*2) + sep;
				}
			}
			y-=m;
			x+=m;
			
			// Graph
			for (int channel = channels-1; channel >= 0; channel--) {
				if ((self_channel_count < 1 || !show_self_channels) && channel == 0) continue;
				canvas.color(colors[channel]);
				for (int xx = 0; xx < size; xx++) {
					int yy = y;
					
					int prev_data = 0;
					for (int i = 0; i < channel; i++) {
						if ((self_channel_count < 1 || !show_self_channels) && i == 0) continue;
						prev_data += data[wrap(xx + index)][i] * scale_y;
					}
					int this_data = (int) (data[wrap(xx + index)][channel] * scale_y);
					canvas.rect(x + xx*advance, yy-(prev_data+this_data), (x + xx*advance) + advance, yy, depth + channel);
				}
			}
			
			// Axis labels
			int yy = 0;
			for (int mul = 1; mul < 10; mul++) {
				yy += (y_axis_interval * mul) / scale_y;
				
				int y_val = (int) (y - (yy*scale_y));
				
				canvas.color(Color.BLACK.val());
				canvas.rect(x, y_val, x+(size*advance), y_val + 2, depth + channels + 10);
				
				String str = data_converter.dataToString(yy);
				int ww = canvas.textrenderer().size(str).x;
				int hh = canvas.textrenderer().size(str).y;

				canvas.color(Color.BLACK75.val());
				canvas.rect(x, y_val, x + ww + (m*2), y_val + hh + (m*2), depth + channels + 10);
				
				canvas.color(Color.WHITE.val());
				canvas.text(x+m, y_val+m, depth + channels + 10, str);
			}
			
		}

		private int wrap(int index) {
			if (index >= size) index = index - size;
			return index;
		}

		public static IntegerDataChart resize(IntegerDataChart chart, int new_size) {
			IntegerDataChart new_chart = new IntegerDataChart(new_size, chart.channels - chart.self_channel_count);
			new_chart.colors = chart.colors;
			new_chart.data_converter = chart.data_converter;
			new_chart.self_channel_count = chart.self_channel_count;
			return new_chart;
		}

		public void name(int channel, String name) {
			channel_names[channel+self_channel_count] = name;
		}
		
		long profile_start = 0;
		
		public void endprofile(int channel, String name) {
			add((int) (System.nanoTime()-profile_start), channel);
			name(channel, name);
		}

		public void startprofile() {
			profile_start = System.nanoTime();
		}

				
	}
	
	static enum DebugState {
		FRAMETIME_CHART,
		UPDATE_TIMES,
		DRAG_AND_DROP,
		ELEMENT_TREE
	}
	
	static boolean show_hover_overlay = true;
	static DebugState current_debug_state = DebugState.FRAMETIME_CHART;
	
	public static IntegerDataChart framechart = new IntegerDataChart(60*4, 10);
	static int framechart_size = 60*2;
	static int framechart_advance = 3;
	static float framechart_y_scale = 1f/60000f;
	static {
		framechart.data_converter = new IDCDataConverter() {
			public String dataToString(int d) {
				double ms = ((double) d/1E6);
				NumberFormat f = DecimalFormat.getInstance();
				f.setMinimumFractionDigits(2);
				f.setMaximumFractionDigits(2);
				return ms + "ms (" + f.format(1000/ms) + " fps)";
			}
		};
	}
	
	static boolean element_tree_fade_after_interact = false;
	
	public static boolean show_debug = false;
	static boolean used_modifier = false;
	
	static int font_size = 18;
	
	public static void drawTree(GUIInstance gui, GUIElement e, Input input) {
				
		if (input.keyReleased(GLFW_KEY_LEFT_ALT) && !used_modifier) {
			show_debug = !show_debug;
		}
		
		if (!input.keyDown(GLFW.GLFW_KEY_LEFT_ALT)) {
			used_modifier = false;
		}
		
		if (!show_debug) return;
		
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		
		if (input.keyDown(GLFW.GLFW_KEY_LEFT_ALT)) {
			if (input.keyPressed(GLFW.GLFW_KEY_GRAVE_ACCENT)) {
				gui.fps.show_fps = !gui.fps.show_fps;
				used_modifier = true;
			}
			if (input.keyPressed(GLFW.GLFW_KEY_1)) {
				current_debug_state = DebugState.values()[0];
				used_modifier = true;
			}
			if (input.keyPressed(GLFW.GLFW_KEY_2)) {
				current_debug_state = DebugState.values()[1];
				used_modifier = true;
			}
			if (input.keyPressed(GLFW.GLFW_KEY_3)) {
				current_debug_state = DebugState.values()[2];
				used_modifier = true;
			}
			if (input.keyPressed(GLFW.GLFW_KEY_4)) {
				current_debug_state = DebugState.values()[3];
				used_modifier = true;
			}
			if (current_debug_state == DebugState.FRAMETIME_CHART) {
				if (input.keyDown(GLFW.GLFW_KEY_EQUAL)) {
					framechart_y_scale *= 1.01;
					if (framechart_y_scale < 1f/1000000f) framechart_y_scale = 1f/100f;
					used_modifier = true;
				}
				if (input.keyDown(GLFW.GLFW_KEY_MINUS)) {
					framechart_y_scale /= 1.01;
					if (framechart_y_scale < 1f/1000000f) framechart_y_scale = 1f/100f;
					used_modifier = true;
				}
				if (input.keyPressed(GLFW.GLFW_KEY_0)) {
					framechart_advance++;
					if (framechart_advance > 4) framechart_advance = 1;
					used_modifier = true;
				}
				if (input.keyPressed(GLFW.GLFW_KEY_9)) {
					int old_framechart_size = framechart_size;
					framechart_size += 30;
					if (framechart_size >= (gui.canvas().width() / framechart_advance)) {
						if (old_framechart_size < (gui.canvas().width() / framechart_advance) - 5) {
							framechart_size = (gui.canvas().width() / framechart_advance) - 5;
						} else {
							framechart_size = 30;
						}
					}
					framechart = IntegerDataChart.resize(framechart, framechart_size);
					used_modifier = true;
				}
				if (input.keyPressed(GLFW.GLFW_KEY_8)) {
					if (framechart.y_axis_interval != 16) {
						framechart.y_axis_interval = 16;
					} else {
						framechart.y_axis_interval = 100;
					}
					
					used_modifier = true;
				}
				if (input.keyPressed(GLFW.GLFW_KEY_7)) {
					framechart.randomcolors();
					used_modifier = true;
				}
				if (input.keyPressed(GLFW.GLFW_KEY_6)) {
					framechart.show_self_channels(!framechart.show_self_channels());
					used_modifier = true;
				}
			}
			if (current_debug_state == DebugState.ELEMENT_TREE) {
				if (input.keyPressed(GLFW.GLFW_KEY_0)) {
					element_tree_fade_after_interact = !element_tree_fade_after_interact;
					used_modifier = true;
				}
			}
		}
		
		F3DCanvas canvas = gui.canvas();
		
		canvas.textrenderer().font_size(font_size);

		// State
		
		GUIElement hovered = GUIUtility.getHoveredElement(e);
		
		// Draw a red flashing rectangle over the currently hovered element 
		if (show_hover_overlay && input.keyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
		if (hovered.state().get(PredicateKey.HOVERED)) {
			canvas.color(new Vector4f(1, 0, 0, flash_opacity()));
			canvas.rect(hovered.hover_rectangle(), 1000);
		} else if (hovered.state().get(PredicateKey.BOUNDED)) {
			canvas.color(new Vector4f(1, 0, 1, flash_opacity()));
			canvas.rect(hovered.hover_rectangle(), 1000);
		}
		
		if (current_debug_state == DebugState.DRAG_AND_DROP) {
			GUIElement drag_target = gui.drag_and_drop_support.find_drag_target(gui.current_window_root());
			if (hovered instanceof ElementReceiver) {
				((ElementReceiver) hovered).DEBUG_draw_drop_areas(gui, null, 1000);
			}
			if (drag_target != null && drag_target.draggable()) {
				if (drag_target.parent() instanceof ElementReceiver) {
					((ElementReceiver) drag_target.parent()).DEBUG_draw_drop_areas(gui, null, 1000);
				}	
				canvas.color(Color.TRANSPARENT_AQUA.val());
				canvas.rect(drag_target.drag_rectangle(), 1000);
				
			}
			if (gui.drag_and_drop_support.drag_start != null) {
				canvas.color(Color.RED.val());
				canvas.rect(
						gui.drag_and_drop_support.drag_start.x - DragAndDropSupport.DRAG_THRESHOLD, 
						gui.drag_and_drop_support.drag_start.y - DragAndDropSupport.DRAG_THRESHOLD, 
						gui.drag_and_drop_support.drag_start.x + DragAndDropSupport.DRAG_THRESHOLD, 
						gui.drag_and_drop_support.drag_start.y + DragAndDropSupport.DRAG_THRESHOLD, 
						1000
					);
			}
			ArrayList<String> dragndrop = new ArrayList<String>();
				dragndrop.add("hovered" + hovered);
				dragndrop.add("parent: " + hovered.parent());
				dragndrop.add("held: " + gui.drag_and_drop_support.held);
				dragndrop.add("target: " + gui.drag_and_drop_support.drag_target);

			DrawUtility.drawStrings(canvas, 5, canvas.height()-5, 1000, dragndrop);
		}
		
		if (current_debug_state == DebugState.FRAMETIME_CHART) {
			framechart.draw(canvas, 5, canvas.height()-5, 1000, framechart_y_scale, framechart_advance);
			framechart.advance();
		}

		if (current_debug_state == DebugState.UPDATE_TIMES) {
			// State
			ArrayList<String> state = new ArrayList<String>();
			for (PredicateKey key : hovered.state().keySet()) {
				state.add(key.toString() + " = " + hovered.state().get(key));
			}
			// Update times
			NumberFormat f = DecimalFormat.getInstance();
			f.setMinimumIntegerDigits(3);
			f.setMinimumFractionDigits(3);
			state.add("§`Last updated: " + f.format(hovered.last_update_elapsed_time()/1000f) + " seconds ago");
			state.add("§`Last draw update: " + f.format(hovered.last_draw_update_elapsed_time()/1000f) + " seconds ago");
			state.add("§`Last state update: " + f.format(hovered.last_state_update_elapsed_time()/1000f) + " seconds ago");
			state.add("§`Last special update: " + f.format(hovered.last_element_update_elapsed_time()/1000f) + " seconds ago");
			// Misc
			state.add("§>Is on screen: " + e.is_on_screen());
			state.add("Render Queue Items: " + ((SimpleCanvas) canvas).queue_size());
			DrawUtility.drawStrings(canvas, 5, canvas.height()-5, 1000, state);
		}
		
		if (current_debug_state == DebugState.ELEMENT_TREE) {
			canvas.textrenderer().font_size(15);
			DrawUtility.drawStrings(canvas, 5, canvas.height()-5, 1000, getElementTreeText(gui.current_window_root(), "", false), 1);
			canvas.textrenderer().font_size(font_size);
		}
		
		// Hover tree
		ArrayList<String> elements = new ArrayList<String>();
		add(elements, e);
		DrawUtility.drawStrings(canvas, canvas.width()-5, canvas.height()-5, 1000, elements);
		
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(9, "Draw (Debugger)");
		
	}
	
	private static ArrayList<String> getElementTreeText(GUIElement root, String pre, boolean hidden) {
		ArrayList<String> result = new ArrayList<String>();
		if (root == null) {
			result.add("§c" + pre + "snowui.??? NULL");
			return result;
		}
		
		hidden = hidden || root.get(PredicateKey.HIDDEN);
		
		String color = "§h";
		if (root.get(PredicateKey.BOUNDED)) color = "";
		if (root instanceof GUISplit) color = "§c";
		if (root.get(PredicateKey.HOVERED)) color = "§b";
		if (root.get(PredicateKey.DISABLED)) color = "§7";
		if (root.get(PredicateKey.DISABLED) && root.get(PredicateKey.BOUNDED)) color = "§9";
		if (hidden) color = "§8";
		
		if (element_tree_fade_after_interact) {
			float elapsed = (float) (root.last_update_elapsed_time());
			color = "§[opacity=" + Utility.clampf(((1000f-elapsed) / 1000f), 0, 1) + "]" + color;
		}
		
		result.add(color + space(root.listStyles(), 30) + pre + root.getClass().getName());
		boolean special = false;
		
		if (root instanceof GUISplit) {
			ArrayList<String> array;
			array = getElementTreeText(((GUISplit) root).first(), pre + "   ", hidden);
			array.set(0, array.get(0).replaceAll(Pattern.quote("snowui"), "FIRST: snowui"));
			result.addAll(array);
			array = getElementTreeText(((GUISplit) root).second(), pre + "   ", hidden);
			array.set(0, array.get(0).replaceAll(Pattern.quote("snowui"), "SECOND: snowui"));
			result.addAll(array);
			special = true;
		}
		
		if (!special) 
		for (GUIElement e : root.sub_elements()) {
			result.addAll(getElementTreeText(e, pre + "   ", hidden));
		}
		return result;
	}

	private static String space(String str, int len) {
		String spc = "";
		for (int i = 0; i < len - str.length(); i++) spc += " ";
		return str + spc;
	}

	private static float flash_opacity() {
		return (float) (Math.cos((System.currentTimeMillis() % 100000) / 200f) + 1) / 3;
	}

	private static void add(ArrayList<String> elements, GUIElement e) {
		elements.add(e.getClass().getSimpleName() + ": " + e.listStyles());
		for (GUIElement sub : e.sub_elements()) {
			if (sub.state().get(PredicateKey.BOUNDED)) add(elements, sub);
		}
	}
	
	public static void endprofile(int channel, String name) {
		framechart.endprofile(channel, name);
	}

	public static void startprofile() {
		framechart.startprofile();
	}


}

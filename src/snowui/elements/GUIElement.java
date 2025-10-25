package snowui.elements;

import java.util.EnumMap;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.CachedProperties;

public abstract class GUIElement {

	private static boolean should_update = false;
	String identifier = "none";
	String selector = "";
	String selector_and_id = identifier;
	
	public static void tick(GUIInstance gui, GUIElement e) {
		if (gui.hasInput()) 	e.updateState(gui);
		if (e.style == null) 	e.cacheStyle(gui);
		if (should_update) 	  { e.updateDrawInfo(gui, gui.size()); should_update = false; }
		e.tickAnimation(gui);
		e.draw(gui, 0);
	}
	
	CachedProperties style;
	public CachedProperties style() { return style; }
	public void cacheStyle(GUIInstance gui) {
		style = new CachedProperties(gui.style(), selector_and_id);
	}
	
	public abstract void updateState(GUIInstance gui);

	public abstract void updateDrawInfo(GUIInstance gui, Rectangle bounds);
	
	public abstract void tickAnimation(GUIInstance gui);

	public abstract void recalculateSize(GUIInstance gui);
	
	
	
	public abstract void draw(GUIInstance gui, int depth);
	
}
package snowui.elements;

import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.COSSPredicate;
import snowui.coss.CachedProperties;
import snowui.coss.enums.PredicateKey;

public abstract class GUIElement {
	
	COSSPredicate 		state 		= new COSSPredicate();
	CachedProperties 	style 		= null;
	
	String 				identifier 	= "none";

	public void cacheStyle(GUIInstance gui) {
		style = new CachedProperties(gui.style(), identifier, state);
	}
	
	protected static boolean should_update = false;
	
	protected int unpadded_width = 0;
	protected int unpadded_height = 0;
	
	public int width() { return style.padw(unpadded_width); }
	public int height() { return style.padh(unpadded_height); }

	public static void tick(GUIInstance gui, GUIElement e) {
		if (gui.hasInput()) 	e.updateState(gui);
		if (e.style == null) 	e.cacheStyle(gui);
		if (should_update) 	  { e.updateDrawInfo(gui, gui.size()); should_update = false; }
		e.tickAnimation(gui);
		e.draw(gui, 0);
	}
	
	public abstract void recalculateSize(GUIInstance gui);
	public abstract void updateDrawInfo(GUIInstance gui, Rectangle bounds);
	public abstract void tickAnimation(GUIInstance gui);
	public abstract void draw(GUIInstance gui, int depth);
	
	protected Rectangle hover_rectangle;
	
	public boolean updateState(GUIInstance gui) { 
		hmm how do i handle updating subelements here...
		
		if (hover_rectangle == null) return false;
		
		return false;
		
	}
	
	public void set(PredicateKey key, boolean value) {
		state.set(key, value);
		this.style = null;
	}
	
}
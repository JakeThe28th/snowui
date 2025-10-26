package snowui.elements;

import java.util.ArrayList;

import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.COSSPredicate;
import snowui.coss.CachedProperties;
import snowui.coss.enums.Color;
import snowui.coss.enums.PredicateKey;

public abstract class GUIElement {
	
	ArrayList<GUIElement> sub_elements = new ArrayList<>();
	
	protected void registerSubElement(GUIElement e) {
		sub_elements.add(e);
	}
	
	protected void removeSubElement(GUIElement e) {
		sub_elements.remove(e);
	}

	private COSSPredicate 	next_state 		= new COSSPredicate();
	
	COSSPredicate 			state 			= new COSSPredicate();
	CachedProperties 		style 			= null;
	
	public COSSPredicate 	state() 		{ return state; }
	public CachedProperties style() 		{ return style; }
	
	String 					identifier 		= "none";
	
	protected boolean should_update = true;
	protected boolean should_recalculate_size = true;
	
	protected Rectangle hover_rectangle;

	protected int unpadded_width = 0;
	protected int unpadded_height = 0;
	
	public int width() { return style.padw(unpadded_width); }
	public int height() { return style.padh(unpadded_height); }

	public static void tick(GUIInstance gui, GUIElement e) {
		e.triggerUpdateState(gui);
		e.triggerDequeueState();
		e.triggerCacheStyle(gui);
		e.triggerRecalculateSize(gui);
		e.triggerUpdateDrawInfo(gui, gui.canvas().size());
		e.triggerTickAnimation(gui);
		e.triggerDraw(gui, 0);
	}
	
	public abstract void recalculateSize(GUIInstance gui);
	public 			void updateState(GUIInstance gui) {};
	/** Opportunity to set up drawing information. <br>
	 *  Notably, this method is responsible for updating
	 *  sub-elements drawing information itself. */
	public abstract void updateDrawInfo(GUIInstance gui, Rectangle bounds);
	public 		 	void tickAnimation(GUIInstance gui) {};
	public abstract void draw(GUIInstance gui, int depth);
	
	public void set(PredicateKey key, boolean value) {
		next_state.set(key, value);
	}
	
	private void dequeueState() {
		if (!next_state.equals(state)) {
			should_recalculate_size = true;
			this.style = null;
		}
		state = next_state;
		next_state = new COSSPredicate(state);
	}
	
	public void cacheStyle(GUIInstance gui) {
		style = new CachedProperties(gui.style(), identifier, state);
	}
	
	// -- == ... == -- //
	
	/* -- Callbacks --*/
	public void onMousePress() 		 { }
	public void onMouseDown() 		 { }
	public void onMouseRelease() 	 { }
	public void onMouseHover() 		 { }
	public void onMouseDoubleClick() { }
	
	/* It should be possible to update an aspect
	 * of an element without forcing all sub-elements
	 * to unnecessarily update. This code provides
	 * the logic needed for that... vvv */

	private boolean triggerUpdateState(GUIInstance gui) {
		if (gui.hasInput()) {
						
			set(PredicateKey.BOUNDED, false);
			set(PredicateKey.HOVERED, false);
			set(PredicateKey.PRESSED, false);
			set(PredicateKey.RELEASED, false);
			set(PredicateKey.DOWN, false);
			
			boolean overridden = false;
			for (GUIElement e : sub_elements) { overridden = overridden || e.triggerUpdateState(gui); }

			if (hover_rectangle != null && hover_rectangle.contains(gui.mouspos())) {
				
				gui.canvas().color(Color.DESBLUE.val());
				gui.canvas().rect(hover_rectangle, 10);
				
				set(PredicateKey.BOUNDED, true);
				
				if (overridden == false) {
				
					set(PredicateKey.HOVERED, true);
					
					onMouseHover();
					
					return true;

				}
				
			}
			
			updateState(gui);

			return overridden;
		}
		return false;
	}
	
	private void triggerCacheStyle(GUIInstance gui) {
		for (GUIElement e : sub_elements) { e.triggerCacheStyle(gui); }
		if (style == null) {
			cacheStyle(gui);
			should_update = true;
		}
	}
	
	private void triggerUpdateDrawInfo(GUIInstance gui, Rectangle bounds) {
		if (should_update) {
			updateDrawInfo(gui, bounds);
			should_update = false; 
		} else {
			/** Calling updateDrawInfo() itself updates all sub-elements,
			 *  so only recurse if we're not calling it. */
			for (GUIElement e : sub_elements) { e.triggerUpdateDrawInfo(gui, bounds); }
		}
	}

	private void triggerTickAnimation(GUIInstance gui) {
		for (GUIElement e : sub_elements) { e.triggerTickAnimation(gui); }
		tickAnimation(gui);
	}
	
	private void triggerDraw(GUIInstance gui, int depth) {
		for (GUIElement e : sub_elements) { e.triggerDraw(gui, depth); }
		draw(gui, depth);
	}
	
	private void triggerRecalculateSize(GUIInstance gui) {
		for (GUIElement e : sub_elements) { e.triggerRecalculateSize(gui); }
		if (should_recalculate_size) {
			recalculateSize(gui);
			should_update = true;
			should_recalculate_size = false;
		}
	}
	
	private void triggerDequeueState() {
		for (GUIElement e : sub_elements) { e.triggerDequeueState(); }
		dequeueState();
	}
	
}
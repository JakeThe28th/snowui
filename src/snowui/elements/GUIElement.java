package snowui.elements;

import java.util.ArrayList;

import frost3d.utility.Log;
import frost3d.utility.Rectangle;
import snowui.GUIInstance;
import snowui.coss.COSSPredicate;
import snowui.coss.CachedProperties;
import snowui.coss.enums.PredicateKey;

public abstract class GUIElement {
	
	public static final int ELEMENT_ADD_DEPTH = 16;

	protected ArrayList<GUIElement> sub_elements = new ArrayList<>();
	
	public ArrayList<GUIElement> sub_elements() {
		return sub_elements;
	}
	
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
	
	public void identifier(String i) 		{ identifier = i; }

	public String listStyles() {
		return identifier;
	}
	
	public boolean is_on_screen() {
		if (scissor_rectangle() != null && limit_rectangle() != null) {
			return scissor_rectangle().intersects(limit_rectangle());
		} else {
			return true;
		}
	}
	
	protected boolean should_update = true;
	protected boolean should_recalculate_size = true;
	protected boolean should_cache_style = true;
	
	private   Rectangle hover_rectangle;
	public 	  Rectangle hover_rectangle() 						{ return hover_rectangle; 		}
	public 	  void 	 	hover_rectangle(Rectangle rectangle) 	{ 		 hover_rectangle = rectangle; 	}
		
	/** Defines the bounding box that updateDrawInfo() stays within */
	private   Rectangle limit_rectangle;
	private   Rectangle padded_limit_rectangle;

	/** Defines the bounding box that updateDrawInfo() stays within */
	public 	  Rectangle limit_rectangle() 						{ return limit_rectangle; 		}
	public 	  Rectangle padded_limit_rectangle() 				{ return padded_limit_rectangle; 		}
	/** Defines the bounding box that updateDrawInfo() stays within */
	public 	  void 	 	limit_rectangle(Rectangle rectangle) 	{   
		boolean limit_changed = false;
		boolean was_on_screen = false; // <-- value doesn't matter since 
									   // it's always overridden before it's used
		if (!rectangle.equals(limit_rectangle)) { 
			should_update = true;
			limit_changed = true;
			was_on_screen = is_on_screen();
		}
		limit_rectangle = rectangle; 
		if (style() != null) set_padded_limit_rectangle();
		if (limit_changed) onLimitRectangleChange();
		if (limit_changed && was_on_screen && !is_on_screen()) {
			// triggerUpdateDrawInfo doesn't update
			// if !is_on_screen(), so set all sub-elements
			// to be also not on-screen by forcing their
			// limit rectangles equal to this element's.
			
			// (but it'd be wasteful to do this even 
			// when the on/off screen status isn't
			// changing, so [was_on_screen] is checked)
			limit_rectangle_recursive(limit_rectangle());
		}
	}
	
	public 	  void 	 	limit_rectangle_recursive(Rectangle rectangle) { 		 
		limit_rectangle = rectangle; 	
		for (GUIElement e : sub_elements) {
			e.limit_rectangle_recursive(rectangle);
		}
	}
	

	private void set_padded_limit_rectangle() {
		padded_limit_rectangle = new Rectangle(
				limit_rectangle.left() + style().left_margin().pixels(),
				limit_rectangle.top() + style().top_margin().pixels(),
				limit_rectangle.right() - style().right_margin().pixels(),
				limit_rectangle.bottom() - style().bottom_margin().pixels()
				);
	}
	
	private   Rectangle scissor_rectangle;
	public 	  Rectangle scissor_rectangle() 					{ return scissor_rectangle; 		}
	public 	  void 	 	scissor_rectangle_recursive(Rectangle rectangle) { 		 
		scissor_rectangle = rectangle; 	
		for (GUIElement e : sub_elements) {
			e.scissor_rectangle_recursive(rectangle);
		}
	}
	
	protected int unpadded_width = 0;
	protected int unpadded_height = 0;
	
	public int width() { return style.padw(unpadded_width); }
	public int height() { return style.padh(unpadded_height); }

	public static void tick(GUIInstance gui, GUIElement e) {
		e.limit_rectangle(gui.canvas().size());	// (Since this is only run on the root element)
		e.triggerUpdateState(gui);
		e.triggerTickAnimation(gui);
		e.triggerDequeueState();
		e.triggerCacheStyle(gui);
		e.triggerRecalculateSize(gui);
		e.triggerUpdateDrawInfo(gui);
		e.triggerDraw(gui, 0);
	}
	
	public abstract void recalculateSize(GUIInstance gui);
	public 		 boolean updateState(GUIInstance gui) { return false; };
	/** Opportunity to set up drawing information. <br>
	 *  Notably, this method is responsible for updating
	 *  sub-elements drawing information itself. */
	public abstract void updateDrawInfo(GUIInstance gui);
	public 		 	void tickAnimation(GUIInstance gui) {};
	public abstract void draw(GUIInstance gui, int depth);
	
	public void set(PredicateKey key, boolean value) {
		next_state.set(key, value);
	}
	
	public boolean get(PredicateKey key) {
		return state.get(key);
	}
	
	private void dequeueState() {
		if (!next_state.equals(state)) {
			log_state_update();
			should_cache_style = true;
			state = next_state;
			next_state = new COSSPredicate(state);
		}
		
	}
	
	public boolean cacheStyle(GUIInstance gui) {
		should_cache_style = false;
		CachedProperties old_style = style;
		style = new CachedProperties(gui.style(), identifier, state);
		return !style.equals(old_style);
	}
	
	// -- == ... == -- //
	
	/* -- Callbacks -- */
	public void onClick			() 	{ }
	public void onHold			()  { }
	public void onRelease		()  { }
	public void onHover			()  { }
	public void onDoubleClick	()  { }
	
	public void onClick			(GUIInstance gui) 	{ }
	public void onHold			(GUIInstance gui)  { }
	public void onRelease		(GUIInstance gui)  { }
	
	/* -- Internal callbacks -- */
	protected void onLimitRectangleChange() { }
	
	/* It should be possible to update an aspect
	 * of an element without forcing all sub-elements
	 * to unnecessarily update. This code provides
	 * the logic needed for that... vvv */
	
	private long last_update_time = 0;
	public 	void log_update() 					 { last_update_time = System.currentTimeMillis(); }
	public 	long last_update_elapsed_time() 	 { return System.currentTimeMillis() - last_update_time; }
	
	private long last_draw_update_time = 0;
	public 	void log_draw_update() 				 { last_draw_update_time = System.currentTimeMillis(); }	
	public 	long last_draw_update_elapsed_time() { return System.currentTimeMillis() - last_draw_update_time; }

	private long last_state_update_time = 0;
	public 	void log_state_update() 			 { last_state_update_time = System.currentTimeMillis(); }	
	public 	long last_state_update_elapsed_time() { return System.currentTimeMillis() - last_state_update_time; }
	
	private long last_element_update_time = 0;
	public 	void log_element_update() 				{ last_element_update_time = System.currentTimeMillis(); }	
	public 	long last_element_update_elapsed_time() { return System.currentTimeMillis() - last_element_update_time; }

	private final boolean triggerUpdateState(GUIInstance gui) {
		if (gui.hasInput() && !get(PredicateKey.DISABLED)) {	
			// Reset all of the properties that this method touches
			set(PredicateKey.BOUNDED, 	false);
			set(PredicateKey.HOVERED, 	false);
			set(PredicateKey.PRESSED, 	false);
			set(PredicateKey.RELEASED, 	false);
			set(PredicateKey.DOWN, 		false);
			
			// Checking this later lets us not trigger events if a sub-element is hovered
			boolean overridden = false;
			for (GUIElement e : sub_elements) { overridden = overridden || e.triggerUpdateState(gui); }

			Rectangle _hover_rectangle = hover_rectangle;
			if (_hover_rectangle != null && scissor_rectangle() != null) { 
				_hover_rectangle = _hover_rectangle.constrain_to(scissor_rectangle());
			}
			
			if (_hover_rectangle != null && _hover_rectangle.contains(gui.mouspos())) {
				// (PredicateKey.BOUNDED should be set regardless of if a sub-element is hovered, though)
				set(PredicateKey.BOUNDED, true);
				if (overridden == false) {
					set(PredicateKey.HOVERED, true);
					onHover();
					if (gui.primary_click_pressed()) 	{ set(PredicateKey.PRESSED,  true); onClick   (); onClick(gui)	; }
					if (gui.primary_click_down()) 		{ set(PredicateKey.DOWN,     true); onHold    (); onHold(gui)	; }
					if (gui.primary_click_released()) 	{ set(PredicateKey.RELEASED, true); onRelease (); onRelease(gui); }
				}
			}
			
			// Element-specific state code can check PredicateKey.HOVERED.
			// In some cases, like dragging a slider, it might want to
			// keep overriding super-elements even when not hovered, so
			// we can't just put it in the if statement up there.
			overridden = overridden || updateState(gui);
			return overridden;
		}
		return false;
	}
	
	private final void triggerCacheStyle(GUIInstance gui) {
		for (GUIElement e : sub_elements) { e.triggerCacheStyle(gui); }
		if (should_cache_style) {
			if (cacheStyle(gui)) {
				log_update();
				should_update = true;
				should_recalculate_size = true;
				if (limit_rectangle != null) set_padded_limit_rectangle();
			}
		}
	}

	private final void triggerUpdateDrawInfo(GUIInstance gui) {
		if (should_update && is_on_screen()) {
			log_draw_update();
			updateDrawInfo(gui);
			should_update = false; 
		}
		for (GUIElement e : sub_elements) { e.triggerUpdateDrawInfo(gui); }
	}

	private final void triggerTickAnimation(GUIInstance gui) {
		for (GUIElement e : sub_elements) { e.triggerTickAnimation(gui); }
		tickAnimation(gui);
	}
	
	private final void triggerDraw(GUIInstance gui, int depth) {
		if (is_on_screen()) draw(gui, depth);
		for (GUIElement e : sub_elements) { e.triggerDraw(gui, depth + ELEMENT_ADD_DEPTH); }
	}
	
	private final void triggerRecalculateSize(GUIInstance gui) {
		for (GUIElement e : sub_elements) { e.triggerRecalculateSize(gui); }
		if (should_recalculate_size) {
			log_update();
			recalculateSize(gui);
			should_update = true;
			should_recalculate_size = false;
		}
	}
	
	private final void triggerDequeueState() {
		for (GUIElement e : sub_elements) { e.triggerDequeueState(); }
		dequeueState();
	}

}
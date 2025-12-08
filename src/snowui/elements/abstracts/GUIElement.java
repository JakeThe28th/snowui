package snowui.elements.abstracts;

import java.util.ArrayList;

import frost3d.utility.Rectangle;
import frost3d.utility.Utility;
import snowui.GUIInstance;
import snowui.coss.COSSPredicate;
import snowui.coss.CachedProperties;
import snowui.coss.enums.PredicateKey;
import snowui.utility.GUIDebugger;
import snowui.utility.Margin;

public abstract class GUIElement {
	
	public static final int ELEMENT_ADD_DEPTH = 16;
	
	GUIElement parent = null;
	
	public GUIElement 	parent() 				{ return parent; }
	public void 		parent(GUIElement e) 	{ parent = e; }

	protected ArrayList<GUIElement> sub_elements = new ArrayList<>();
	
	public ArrayList<GUIElement> sub_elements() {
		return sub_elements;
	}
	
	protected void registerSubElement(GUIElement e) {
		sub_elements.add(e);
		onRegisterSubElement(e);
	}
	
	protected void onRegisterSubElement(GUIElement e) {
		e.parent(this);
	}
	
	protected void onRemoveSubElement(GUIElement e) {
		if (e.parent == this) e.parent(null);
	}
	
	protected void removeSubElement(GUIElement e) {
		sub_elements.remove(e);
		onRemoveSubElement(e);
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
		if (get(PredicateKey.HIDDEN)) return false;
		return is_within_scissor();
	}
	
	public boolean is_within_scissor() {
		if (scissor_rectangle() != null && limit_rectangle() != null) {
			return scissor_rectangle().intersects(limit_rectangle());
		} else {
			return true;
		}
	}
	
	public void force_update_all() {
		should_update(true);
		should_recalculate_size(true);
		should_cache_style(true);
		for (GUIElement e : sub_elements()) {
			e.force_update_all();
		}
	}
	
	private boolean should_update 			= true;
	private boolean should_recalculate_size = true;
	private boolean should_cache_style 		= true;
	
	protected void should_update			(boolean v) { should_update 			= v; }
	protected void should_recalculate_size	(boolean v) { 
		should_recalculate_size 	= v; 
		if (parent() != null) parent().should_recalculate_size(v); 
	}
	protected void should_cache_style		(boolean v) { should_cache_style 		= v; }

	private   Rectangle hover_rectangle;
	public 	  Rectangle hover_rectangle() 						{ return hover_rectangle; 		}
	public 	  void 	 	hover_rectangle(Rectangle rectangle) 	{ 		 hover_rectangle = rectangle; 	}
		
	/** Defines the bounding box that updateDrawInfo() stays within */
	private   Rectangle limit_rectangle;
	private   Rectangle padded_limit_rectangle;
	private   Rectangle aligned_limit_rectangle;

	/** Defines the bounding box that updateDrawInfo() stays within */
	public 	  Rectangle limit_rectangle() 						{ return limit_rectangle; 			}
	public 	  Rectangle padded_limit_rectangle() 				{ return padded_limit_rectangle; 	}
	public 	  Rectangle aligned_limit_rectangle() 				{ return aligned_limit_rectangle; 	}
	/** Defines the bounding box that updateDrawInfo() stays within */
	public 	  void 	 	limit_rectangle(Rectangle rectangle) 	{  
		boolean limit_changed = false;
		boolean was_on_screen = false; // <-- value doesn't matter since 
									   // it's always overridden before it's used
		Utility._assert(rectangle != null, "limit_rectangle attempted to be set 'null'");
		if (!rectangle.equals(limit_rectangle)) { 
			should_update(true);
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
		set_aligned_limit_rectangle();
	}
	
	public void set_aligned_limit_rectangle() {
		aligned_limit_rectangle = Margin.calculate(
				style(), 
				padded_limit_rectangle(), 
				unpadded_width, 
				unpadded_height
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

	public static void tick(GUIInstance gui, GUIElement e, Rectangle limit, int depth) {
		e.limit_rectangle(limit);	// (Since this is only run on the root element)
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerUpdateState(gui);
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(0, "Update state");
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerTickAnimation(gui);
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(1, "Tick animation");
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerDequeueState();
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(2, "Dequeue state");
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerCacheStyle(gui);
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(3, "Cache style");
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerRecalculateSize(gui);
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(5, "Recalculate size");
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerUpdateDrawInfo(gui);
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(6, "Update drawing info");
		if (GUIInstance.DEBUG) GUIDebugger.startprofile();
		e.triggerDraw(gui, depth);
		if (GUIInstance.DEBUG) GUIDebugger.endprofile(7, "Draw (element)");
	}
	
	public static void tickFloating(GUIInstance gui, GUIElement e, int x, int y, int depth) {
		e.triggerRecalculateSize(gui);
		e.triggerCacheStyle(gui);
		tick(gui, e, new Rectangle(x, y, x + e.width(), y + e.height()), depth);
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
	
	private void triggerOnSingleClick(GUIInstance gui) {
		onSingleClick(); 
		onSingleClick(gui);
	}
	
	private void triggerOnPress(GUIInstance gui) {
		gui.last_pressed_element(this);
		last_press_time = System.currentTimeMillis();
		onPress(); 
		onPress(gui);
	}

	private void triggerOnHold(GUIInstance gui) {
		onHold(); 
		onHold(gui);
	}
	
	private void triggerOnRelease(GUIInstance gui) {
		onRelease(); 
		onRelease(gui);
		
		if (gui.last_pressed_element() == this) {
			triggerOnSingleClick(gui);
		}
		
	}
	
	private void triggerOnHover(GUIInstance gui) {
		onHover(); 
		onHover(gui);
	}
	
	long last_press_time = 0;

	public int time_since_pressed() {
		return (int) (System.currentTimeMillis() - last_press_time);
	}
	
	/* -- Callbacks -- */
	public void onPress			() 	{ }
	public void onHold			()  { }
	public void onRelease		()  { }
	public void onHover			()  { }
	public void onSingleClick	()  { }
	public void onDoubleClick	()  { }
	
	public void onPress			(GUIInstance gui)  { }
	public void onHold			(GUIInstance gui)  { }
	public void onRelease		(GUIInstance gui)  { }
	public void onHover			(GUIInstance gui)  { }
	public void onSingleClick	(GUIInstance gui)  { }
	public void onDoubleClick	(GUIInstance gui)  { }
	
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

			if (!is_on_screen()) return false;
			
			@SuppressWarnings("unchecked") // Some events might want to modify the sub-elements list,
										   // So, a copy is made.
			ArrayList<GUIElement> sub_elements = (ArrayList<GUIElement>) this.sub_elements.clone();
			
			// Checking this later lets us not trigger events if a sub-element is hovered
			boolean overridden = false;
			for (GUIElement e : sub_elements) { overridden = overridden | e.triggerUpdateState(gui); }

			Rectangle _hover_rectangle = hover_rectangle;
			if (_hover_rectangle != null && scissor_rectangle() != null) { 
				_hover_rectangle = _hover_rectangle.constrain_to(scissor_rectangle());
			}
			
			if (_hover_rectangle != null && _hover_rectangle.contains(gui.mouspos())) {
				// (PredicateKey.BOUNDED should be set regardless of if a sub-element is hovered, though)
				set(PredicateKey.BOUNDED, true);
				if (overridden == false) {
					overridden = true;
					set(PredicateKey.HOVERED, true);
					triggerOnHover(gui);
					if (gui.primary_click_pressed()) 	{ set(PredicateKey.PRESSED,  true); triggerOnPress(gui); }
					if (gui.primary_click_down()) 		{ set(PredicateKey.DOWN,     true); triggerOnHold(gui); }
					if (gui.primary_click_released()) 	{ set(PredicateKey.RELEASED, true); triggerOnRelease(gui);; }
				}
			}
			
			// Element-specific state code can check PredicateKey.HOVERED.
			// In some cases, like dragging a slider, it might want to
			// keep overriding super-elements even when not hovered, so
			// we can't just put it in the if statement up there.
			overridden = overridden || updateState(gui);
			return overridden;
		} else {
			if (!is_on_screen()) return false;
			boolean overridden = false;
			for (GUIElement e : sub_elements) { overridden = overridden || e.triggerUpdateState(gui); }
			if (!overridden && get(PredicateKey.DOWN)) { triggerOnHold(gui); }
			return overridden;
		}
	//	return false;
	}
	
	private final void triggerCacheStyle(GUIInstance gui) {
		if (should_cache_style) {
			if (cacheStyle(gui)) {
				log_update();
				should_update(true);
				should_recalculate_size(true);
				if (limit_rectangle != null) set_padded_limit_rectangle();
			}
		}
		if (is_on_screen()) {
			for (GUIElement e : sub_elements) { e.triggerCacheStyle(gui); }
		}
	}

	private final void triggerUpdateDrawInfo(GUIInstance gui) {
		if (should_update && is_on_screen()) {
			log_draw_update();
			updateDrawInfo(gui);
			should_update = false; 
		}
		if (is_on_screen()) {
			for (GUIElement e : sub_elements) { e.triggerUpdateDrawInfo(gui); }
		}
	}

	private final void triggerTickAnimation(GUIInstance gui) {
		if (is_on_screen()) {
			for (GUIElement e : sub_elements) { e.triggerTickAnimation(gui); }
			tickAnimation(gui);
		}
	}
	
	private final void triggerDraw(GUIInstance gui, int depth) {
		if (is_on_screen()) {
			if (scissor_rectangle() != null) gui.push_scissor(scissor_rectangle());
			if (style().outline_size().integer() != 0) {
				drawOutline(gui, depth+2);
			}
			draw(gui, depth);
			if (scissor_rectangle() != null) gui.pop_scissor();
			for (GUIElement e : sub_elements) { e.triggerDraw(gui, depth + ELEMENT_ADD_DEPTH); }
		}
	}
	
	private final void triggerRecalculateSize(GUIInstance gui) {
		if (is_on_screen()) {
			for (GUIElement e : sub_elements) { e.triggerRecalculateSize(gui); }
			if (should_recalculate_size) {
				log_update();
				recalculateSize(gui);
				should_update(true);
				should_recalculate_size = false;
				if (this.aligned_limit_rectangle != null) {
					set_aligned_limit_rectangle();
				}
			}
		}
	}
	
	private final void triggerDequeueState() {
		if (is_within_scissor() && (!get(PredicateKey.HIDDEN) || !next_state.get(PredicateKey.HIDDEN))) {
			for (GUIElement e : sub_elements) { e.triggerDequeueState(); }
			dequeueState();
		}
	}
	
	// -- == ... == -- //

	protected void drawOutline(GUIInstance gui, int depth) {
		if (hover_rectangle() == null) return;
		Rectangle b = hover_rectangle().expand(style().outline_margin().pixels());
		gui.canvas().color(style().outline_color().color());
		int o = style().outline_size().pixels();
		gui.canvas().rect(b.left(), 	b.top(), 		b.right(), 	b.top()+o, 	depth);
		gui.canvas().rect(b.left(), 	b.bottom()-o, 	b.right(), 	b.bottom(), depth);
		gui.canvas().rect(b.left(), 	b.top(), 		b.left()+o, b.bottom(), depth);
		gui.canvas().rect(b.right()-o, 	b.top(), 		b.right(), 	b.bottom(), depth);
	}
	
	// -- == Dragging == -- //
	
		   boolean 		draggable = false;
	public boolean 		draggable() 				{ return draggable; }
	public GUIElement 	draggable(boolean b) 		{ draggable = b; return this; }
	
	public Rectangle	drag_rectangle()			{ return hover_rectangle(); }
	public boolean 		can_drop(GUIInstance gui) 	{ return true; }
	
}
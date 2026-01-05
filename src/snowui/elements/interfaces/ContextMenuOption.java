package snowui.elements.interfaces;

import snowui.elements.abstracts.GUIElement;

public interface ContextMenuOption {
	GUIElement 		as_element			();
	public int 		left_text_start		();
	public int 		left_text_end		();
	public void 	left_text_start		(int v);
	public void 	left_text_end		(int v);
}
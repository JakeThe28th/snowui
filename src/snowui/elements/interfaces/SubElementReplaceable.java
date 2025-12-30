package snowui.elements.interfaces;

import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.components.SubElementReplaceQueue;

public interface SubElementReplaceable {
	public void replace(GUIElement original, GUIElement replacement);
	public SubElementReplaceQueue queue();
}

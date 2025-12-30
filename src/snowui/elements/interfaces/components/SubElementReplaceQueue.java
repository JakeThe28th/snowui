package snowui.elements.interfaces.components;

import java.util.ArrayList;

import snowui.elements.abstracts.GUIElement;
import snowui.elements.interfaces.SubElementReplaceable;

public class SubElementReplaceQueue {

	static record ReplaceOperation(GUIElement original, GUIElement replacement) {}
	
	ArrayList<ReplaceOperation> queue = new ArrayList<>();
	
	public void queue_replace(GUIElement original, GUIElement replacement) {
		queue.add(new ReplaceOperation(original, replacement));
	}
	
	public void dequeue(SubElementReplaceable target) {
		for (ReplaceOperation op : queue) target.replace(op.original, op.replacement);
		queue.clear();
	}
	
}

package snowui.elements.interfaces;

public class Draggable {
	
	public boolean willSetDown(Droppable target);
	
}

should the element be draggable or should the
container a;lloow dragging of its sub-elements 

|| draggalbe onPickup remove from parent if list

https://en.wikipedia.org/wiki/Binary_space_partitioning - site:todo.c see if splitting lists into partition elements (for bounding box checking) is actually faster or not
	
ok how about:
	every element has the capacity to be dragged by default
	it's disabled on initialized but you can ' call "draggable(boolean v)" to
	set it.
	
	if it's enabled, if the mouse started presing the element and then moves, 
	after a threshold the element is picked up by the mouse (calling triggerDragStart()).
	then, it stays with the mouse until it's released
	(and release doesn't trigger onRelease or onClick, but triggerDragEnd()
			
	triggerDragEnd does the following
		if the mouse is hovering over a droppable area,
			calls willSetDown to see if the element will drop there,
			calls willAccept on the drop target to see if it will recieve it
			and if yes, drop it
		if none of those are true, then the element is returned to its' original position,
		
		set during triggerDragStart
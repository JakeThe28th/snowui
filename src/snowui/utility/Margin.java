package snowui.utility;

import snowui.coss.CachedProperties;
import static snowui.coss.enums.Constant.*;
import static snowui.coss.enums.StylePropertyType.*;

import frost3d.utility.Rectangle;
import frost3d.utility.Utility;

public class Margin {
		
	public static Rectangle calculate(CachedProperties info, Rectangle bounds, int target_width, int target_height) {

		int left_limit 		= bounds.left();
		int top_limit 		= bounds.top();
		int right_limit 	= bounds.right();
		int bottom_limit 	= bounds.bottom();
		
		// Get the width/height of the internal part of the element.
		
			// --** = Width = **-- //

			int minimum_width 	= target_width; // Flex by default
			     if (info.min_width.type()     == PIXELS) 	{  minimum_width = info.min_width.pixels(); }
			else if (info.min_width.constant() == CONTAINER) {  minimum_width = info.unpadw(right_limit-left_limit); }
	
			int maximum_width 	= target_width; // Flex by default
			     if (info.max_width.type()     == PIXELS) 	{  maximum_width = info.max_width.pixels(); }
			else if (info.max_width.constant() == CONTAINER) {  maximum_width = info.unpadw(right_limit-left_limit); }
	
			int internal_width  = Utility.clamp(target_width, minimum_width, maximum_width);
	
			 // --** = Height (copied from Width) = **-- //
		    
			int minimum_height 	= target_height; // Flex by default
			     if (info.min_height.type()     == PIXELS) 	{  minimum_height = info.min_height.pixels(); }
			else if (info.min_height.constant() == CONTAINER){  minimum_height = info.unpadh(bottom_limit-top_limit); }
		
			int maximum_height 	= target_height; // Flex by default
			     if (info.max_height.type()     == PIXELS) 	{  maximum_height = info.max_height.pixels(); }
			else if (info.max_height.constant() == CONTAINER){  maximum_height = info.unpadh(bottom_limit-top_limit); }
		
			int internal_height  = Utility.clamp(target_height, minimum_height, maximum_height);

		/* Horizontal Alignment; Ignoring margins, 
		 * If left aligned,  
		 * 		'left'  should be 0 and 
		 * 		'right' should be 'internal_width'.
		 * If middle aligned,
		 * 		'left'  should be ('available_bounding_width'/2) - (internal_width/2)
		 * 		'right' should be ('available_bounding_width'/2) + (internal_width/2)
		 * If right aligned, 
		 * 		'left'  should be 'available_bounding_width'-internal_width, and 
		 * 		'right' should be 'available_bounding_width'. */
			int available_bounding_width = info.unpadw(right_limit-left_limit);
			
			// Assume left-aligned by default.
			int x_offset  = 0;
			if (info.halign.constant() == MIDDLE) { x_offset  = (available_bounding_width/2)-(internal_width/2); }
			if (info.halign.constant() == RIGHT)  { x_offset  = available_bounding_width-internal_width; }
			
			// Adding back in the margins.
			int left  = left_limit + info.left_margin.integer() + x_offset;
			int right = 			 left  + internal_width;

		/* Vertical Alignment; What We Just Did But top is Top and bottom is Bottom. */
			int available_bounding_height = info.unpadh(bottom_limit-top_limit);
			
			// Assume top-aligned by default.
			int y_offset  = 0;
			if (info.valign.constant() == MIDDLE) { y_offset  = (available_bounding_height/2)-(internal_height/2); }
			if (info.valign.constant() == BOTTOM) { y_offset  = available_bounding_height-internal_height; }
			
			// Adding back in the margins.
			int top   = top_limit  + info.top_margin.integer() + y_offset;
			int bottom = 			 top + internal_height;
			
			// The bounding box of the (properly aligned and padded) internal part of the element.
			return new Rectangle(left, top, right, bottom);
	}

}

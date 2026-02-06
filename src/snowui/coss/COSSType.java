package snowui.coss;

import java.util.ArrayList;
import java.util.HashMap;

/* Feels kind of weird to put this in it's own file,
 * but COSSProperty is too big to be in ComposingSt-
 * -yleSheet after moving the deserialization code 
 * into it, and I hate inconsistency more. */

public record COSSType(
		ArrayList<String> contains, 
		HashMap<String, COSSProperty> properties,
		HashMap<String, COSSPredicate> predicates // in theory, these should 
													 // be swapped but using hashmaps
		 											 // with custom types as keys is pain
		) {
	public COSSType() { 
		this(
			new ArrayList<String>(), 
			new HashMap<String, COSSProperty>(), 
			new HashMap<String, COSSPredicate>()
			); 
	}

	/** Returns the type name corresponding to the predicate with the most matching values. */
	public String getPredicateTargetType(COSSPredicate predicate) {
		int max_match = 0;
		String target = null;
		for (String current_key : predicates().keySet()) {
			COSSPredicate current = predicates().get(current_key);
			if (current.match_count(predicate) > max_match && current.matches(predicate)) {
				target = current_key;
				max_match = current.match_count(predicate);
			}
		}
		return target;
	}
	
}
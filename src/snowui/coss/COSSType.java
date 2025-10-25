package snowui.coss;

import java.util.ArrayList;
import java.util.HashMap;

/* Feels kind of weird to put this in it's own file,
 * but COSSProperty is too big to be in ComposingSt-
 * -yleSheet after moving the deserialization code 
 * into it, and I hate inconsistency more.* */

public record COSSType(
		ArrayList<String> contains, 
		HashMap<String, COSSProperty> properties,
		COSSPredicateSet predicates // <-- *shut up
		) {
	public COSSType() { this(new ArrayList<String>(), new HashMap<String, COSSProperty>(), new COSSPredicateSet()); }
}
package snowui.coss;

import java.util.EnumMap;

import snowui.coss.enums.PredicateKey;

public class COSSPredicateSet {

	EnumMap<PredicateKey, Boolean> state = new EnumMap<>(PredicateKey.class);
	
	public void set(PredicateKey key, boolean value) {
		state.put(key, value);
	}
	
	/** Two predicates match if all the values they have are the same,
	 *  all values they don't share may differ. */
	public boolean matches(COSSPredicateSet me, COSSPredicateSet other) {
		for (PredicateKey p : other.state.keySet()) {
			if (me.state.get(p) == null) continue;
			if (other.state.get(p) != me.state.get(p)) return false;
		}
		return true;
	}
	
}

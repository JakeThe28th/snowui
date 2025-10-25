package snowui.coss;

import java.util.EnumMap;

import snowui.coss.enums.PredicateKey;

public class COSSPredicate {

	EnumMap<PredicateKey, Boolean> state = new EnumMap<>(PredicateKey.class);
	
	public void set(PredicateKey key, boolean value) {
		state.put(key, value);
	}
	
	/** Two predicates match if all the values they have are the same,
	 *  all values they don't share may differ. */
	public boolean matches(COSSPredicate me, COSSPredicate other) {
		for (PredicateKey p : other.state.keySet()) {
			if (me.state.get(p) == null) continue;
			if (other.state.get(p) != me.state.get(p)) return false;
		}
		return true;
	}

	/** EX: "HOVER=true, DOWN=true" */
	public static COSSPredicate from(String predicate) {
		COSSPredicate set = new COSSPredicate();;
		
		for (String pair : predicate.split(",")) {
			String key = pair.split("=")[0];
			String value = pair.split("=")[1];

			PredicateKey pkey = PredicateKey.valueOf(key.trim());
			boolean pvalue = value.trim().toLowerCase().equals("true");
			
			set.set(pkey, pvalue);
		}
		
		return set;
	}
	
}

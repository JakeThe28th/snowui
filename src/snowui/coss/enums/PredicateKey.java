package snowui.coss.enums;

public enum PredicateKey {
	HOVERED, // ONLY the most deeply nested hovered element (exclusive hover)
	PRESSED,
	RELEASED,
	DOWN,
	SELECTED,
	CLOSED,
	LADEN, // Has sub-elements
	DISABLED , // Doesn't have input
	BOUNDED, // Inclusive hover (old hover)
}
package snowui.coss.datatypes;

import java.util.ArrayList;
import java.util.HashMap;

public record StyleType(ArrayList<String> contains, HashMap<String, StyleProperty> properties) {}

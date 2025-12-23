package tennexioun.base;

import frost3d.utility.Utility;
import snowui.GUIInstance;
import tennexioun.DATANavigationBar.Tab;
import tennexioun.DATANavigationBar.TabGroup;

/** The visual element of a single tab. */
public class GUINAVTab extends GUI_NamedUnit {
	{ identifier("minitab"); }
	Tab tab;
	TabGroup group;
	public GUINAVTab(Tab tab, TabGroup group) {
		super(null);
		super.color = Utility.fromHex("#444444");
		this.tab = tab;
		this.group = group;
	}
	@Override public void onSingleClick() { group.current_tab(tab);; }
	@Override public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = 20;
		this.unpadded_width = 20;
	}
}

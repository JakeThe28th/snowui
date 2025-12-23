package tennexioun.base;

import frost3d.utility.Utility;
import snowui.GUIInstance;
import tennexioun.TXDATANavigationBar.Tab;
import tennexioun.TXDATANavigationBar.TabGroup;

/** The visual element of a single tab. */
public class TXGUINAVTab extends TXGUI_NamedUnit {
	{ identifier("minitab"); }
	Tab tab;
	TabGroup group;
	public TXGUINAVTab(Tab tab, TabGroup group) {
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

package tennexioun.base;

import snowui.GUIInstance;
import snowui.coss.enums.PredicateKey;
import tennexioun.DATANavigationBar;
import tennexioun.DATANavigationBar.TabGroup;

/** The visual element of a tab group. */
public class GUINAVTabGroup extends GUI_NamedUnit {
	TabGroup group;
	DATANavigationBar nav;
	public GUINAVTabGroup(TabGroup g, DATANavigationBar n) {
		super(g.group_name());
		super.color = g.color;
		group = g;
		nav = n;
		super.title.identifier("tab_group_title");
		super.title.text().identifier("tab_group_title");
		super.title.set(PredicateKey.DISABLED, true);
		super.title.text().set(PredicateKey.DISABLED, true);
		title.finish_on_enter(true);
		// select_group(this);
	}
	@Override public void onSingleClick() { nav.current_group(group); }
	@Override public void recalculateSize(GUIInstance gui) {
		if (super.title == null) {
			this.unpadded_height = 25;
			this.unpadded_width = 40;
		} else {
			this.unpadded_height = title.height();
			this.unpadded_width = title.width();
		}
	}
	@Override
	public void onFinishEditingTitle(String old_text, String new_text) {
		group.group_name(new_text);
	}
}
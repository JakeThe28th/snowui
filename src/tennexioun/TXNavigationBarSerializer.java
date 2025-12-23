package tennexioun;

import java.io.IOException;
import java.nio.file.Path;

import disaethia.io.nbt.NBTCompound;
import disaethia.io.nbt.NBTList;
import disaethia.io.nbt.NBTNamedTag;
import frost3d.utility.Log;
import frost3d.utility.Utility;
import tennexioun.TXDATANavigationBar.Tab;
import tennexioun.TXDATANavigationBar.TabGroup;

public class TXNavigationBarSerializer {

	public static NBTCompound serialize(TXDATANavigationBar bar) {
		NBTCompound ser_tabstate = new NBTCompound();
		NBTList ser_grouplist = new NBTList((byte)10);
		for (TabGroup group : bar.groups) {
			NBTCompound ser_group = new NBTCompound();
			NBTList ser_tablist = new NBTList((byte)10);
			for (Tab tab : group.tabs) {
				NBTCompound ser_tab = new NBTCompound();
				ser_tab.put("uri", tab.uri);
				ser_tablist.add(ser_tab);
				
				// TODO: serialize custom data, also.
				
			}
			ser_group.put("tabs", ser_tablist);
			ser_group.put("tab_index", group.tab_index);
			ser_group.put("name", group.name);
			ser_group.put("color", Utility.toHex(group.color));
			ser_grouplist.add(ser_group);
		}
		ser_tabstate.put("groups", ser_grouplist);
		ser_tabstate.put("group_index", bar.group_index);
		ser_tabstate.put("group_count", bar.tbgc);
		ser_tabstate.put("current_uri", bar.current_uri);
		return ser_tabstate;
	}
	
	public static TXDATANavigationBar deserialize(NBTCompound serialized) {
		TXDATANavigationBar bar = new TXDATANavigationBar();
		
		NBTNamedTag value;
		
		value = serialized.get("group_index");
		bar.group_index = value != null ? value.getInt().get() : 0;
		
		value = serialized.get("group_count");
		bar.tbgc = value != null ? value.getInt().get() : 0;
		
		value = serialized.get("current_uri");
		if (value != null) bar.setURI(value.getString().get());
		
		NBTList ser_groups = serialized.get("groups").getList();
		for (int i = 0; i < ser_groups.length.get(); i++) {
			NBTCompound ser_group = ser_groups.getCompound(i); 
			TabGroup des_group = bar.new TabGroup();
			des_group.tabs.clear();
			
			value = ser_group.get("tab_index");
			des_group.tab_index = value != null ? value.getInt().get() : 0;
			
			value = ser_group.get("name");
			des_group.name = value != null ? value.getString().get() : "???";

			value = ser_group.get("color");
			if (value != null) des_group.color = Utility.fromHex(value.getString().get());
			
			NBTList ser_tablist = ser_group.get("tabs").getList();
			for (int j = 0; j < ser_tablist.length.get(); j++) {
				NBTCompound ser_tab = ser_tablist.getCompound(j);
				
				String uri = ser_tab.get("uri").getString().get();
				Tab des_tab = bar.new Tab(uri);
				des_group.tabs.add(des_tab);
				
				// TODO: deserialize custom data, also.
			}
			bar.groups.add(des_group);
		}
		bar.syncAllGUI();
		return bar;
	}

	public static void save(TXDATANavigationBar bar, Path path) throws IOException {	
		if (path.getParent() != null) path.getParent().toFile().mkdirs();
		path.toFile().delete();
		new NBTNamedTag("tabstate", serialize(bar)).save(path.toString());
	}
	
	public static TXDATANavigationBar load(Path path) throws IOException {
		NBTCompound ser_tabstate = NBTNamedTag.read(path.toString()).getCompound();
		return deserialize(ser_tabstate);
	}
	
}

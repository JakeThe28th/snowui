package tennexioun;

import java.io.File;
import frost3d.utility.Log;
import snowui.GUIInstance;

public class TXGUIFileDirectory extends TXGUIDirectory {
	
	File path;
	
	public TXGUIFileDirectory(File dir) {
		super(dir.getName());
		
		path = dir;
		super.is_directory(dir.isDirectory());
	}

	@Override
	protected void populate() {
		File dir = path;
		for (File f : dir.listFiles()) {
			if (!f.isDirectory()) continue;
			add(new TXGUIFileDirectory(f));
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) continue;
			add(new TXGUIFileDirectory(f));
		}
	}

	@Override
	protected void onParentChange(TXGUIDirectory new_parent) {
		Log.send("Parent changed to " + ((TXGUIFileDirectory) new_parent).path);
	}
	
	@Override
	public void onSingleClickName(GUIInstance gui) {
		Log.send("Clicked " + path);
	}

}

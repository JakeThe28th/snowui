package snowui.utility;

import frost3d.utility.Log;
import frost3d.utility.Utility;

public class AnimationTimer {
	
	long start;
	long length;
	
	int state = 0;
	
	public void setStartTimeMS(long time) {
		start = time;
	}
	
	public void setLengthMS(long time) {
		length = time;
	}
	
	public int getElapsedMS() {
		return (int) (System.currentTimeMillis() - start);
	}
	
	public float get() {
		return ((float) getElapsedMS()) / ((float) length);
	}
	
	public float sget(int iter) {
		float result = get();
		for (int i = 0; i < iter; i++) {
			result = (float) Utility.lerp(result, 1, get());
		}
		return result;
	}

	public void start() {
		setStartTimeMS(System.currentTimeMillis());
		state = 1;
	}	
	
	public boolean just_finished() {
		if (state == 1 && get() >= 1) {
			state = 2;
			return true;
		}
		return false;
	}

}

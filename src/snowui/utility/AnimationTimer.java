package snowui.utility;

public class AnimationTimer {
	
	long start;
	long length;
	
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

}

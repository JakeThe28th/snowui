package snowui.utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import frost3d.interfaces.F3DCanvas;

public class FPSCounter {

	public long 	frame_start_time 		= 0;
	int[] 			frametimes 				= new int[60];
	int 			frametime_index 		= 0;
	
	public void drawFPS(F3DCanvas canvas) {
			NumberFormat f = DecimalFormat.getInstance();
			f.setMinimumIntegerDigits(3);
			f.setMinimumFractionDigits(3);
			
			int cumulative_frame_time = 0;
			for (int t : frametimes) cumulative_frame_time += t;
			
			DrawUtility.drawStrings(canvas, canvas.width()-5, 5, 1000, 
				"MSPT=" + f.format((cumulative_frame_time / (float) frametimes.length)),
				"FPS=" + f.format(1000f/(cumulative_frame_time / (float) frametimes.length))
				);
			
			int frame_time = (int) (System.currentTimeMillis()-frame_start_time);
			frametimes[frametime_index] = frame_time;
			frametime_index ++;
			if (frametime_index >= frametimes.length) frametime_index = 0;
			
			//
			
			frame_start_time = System.currentTimeMillis();
	}
	
}

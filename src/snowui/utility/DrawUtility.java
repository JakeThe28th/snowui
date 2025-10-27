package snowui.utility;

import org.joml.Vector2i;

import frost3d.interfaces.F3DCanvas;
import snowui.coss.enums.Color;

public class DrawUtility {
	
	public static void drawStrings(F3DCanvas canvas, int x, int y, int depth, String... strings) {
		
		int xx = x;
		int yy = y;
		int m  = 5;
		
		int ww = 0;
		
		for (String str : strings) {
			int ts = canvas.textrenderer().size(str).x;
			ww = (ww < ts) ? ts : ww;
		}
		
		ww += m*2;
		
		if (x + ww > canvas.width()) xx = x - ww;

		for (String str : strings) {
			Vector2i size = canvas.textrenderer().size(str);
			canvas.color(Color.BLACK25.val());
			canvas.rect(xx, yy, xx+size.x+(m*2), yy+size.y+(m*2), depth);
			canvas.color(Color.WHITE.val());
			canvas.text(xx+m, yy+m, depth+1, str);
			yy += size.y + m*3;
		}
	}


}

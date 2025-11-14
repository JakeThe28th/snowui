package snowui.utility;

import java.util.Arrays;

import org.joml.Vector2i;

import frost3d.interfaces.F3DCanvas;
import snowui.coss.enums.Color;

public class DrawUtility {
	
	public static void drawStrings(F3DCanvas canvas, int x, int y, int depth, String... strings) {
		drawStringsI(canvas, x, y, depth, Arrays.asList(strings));
	}
	
	public static void drawStrings(F3DCanvas canvas, int x, int y, int depth, Iterable<String> strings) {
		drawStringsI(canvas, x, y, depth, strings);
	}

	public static void drawStringsI(F3DCanvas canvas, int x, int y, int depth, Iterable<String> strings) {
		
		int xx = x;
		int yy = y;
		int m  = 5;
		
		int ww = 0;
		int hh = 0;
		
		for (String str : strings) {
			if (str.startsWith("§")) str = str.substring(2);
			int ts = canvas.textrenderer().size(str).x;
			ww = (ww < ts) ? ts : ww;
			hh += canvas.textrenderer().size(str).y + m*3;
		}
		
		ww += m*2;
		hh -= m;
		
		if (x + ww > canvas.width()) xx = x - ww;
		if (y + hh > canvas.height()) yy = y - hh;

		for (String str : strings) {
			Vector2i size = canvas.textrenderer().size(str);
			canvas.color(Color.TRANSPARENT_BLACK.val());
			canvas.rect(xx, yy, xx+size.x+(m*2), yy+size.y+(m*2), depth);
			canvas.color(Color.WHITE.val());
			if (str.startsWith("§`")) canvas.color(Color.DESYELLOW.val());
			if (str.startsWith("§")) str = str.substring(2);
			canvas.text(xx+m, yy+m, depth+1, str);
			yy += size.y + m*3;
		}
	}


}

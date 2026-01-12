package snowui.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.joml.Vector2i;
import org.joml.Vector4f;

import frost3d.interfaces.F3DCanvas;
import frost3d.utility.Utility;
import snowui.coss.enums.Color;

public class DrawUtility {
	
	public static void drawStrings(F3DCanvas canvas, int x, int y, int depth, String... strings) {
		drawStringsI(canvas, x, y, depth, Arrays.asList(strings), 5);
	}
	
	public static void drawStrings(F3DCanvas canvas, int x, int y, int depth, Iterable<String> strings) {
		drawStringsI(canvas, x, y, depth, strings, 5);
	}
	
	public static void drawStrings(F3DCanvas canvas, int x, int y, int depth, Iterable<String> strings, int m) {
		drawStringsI(canvas, x, y, depth, strings, m);
	}

	public static void drawStringsI(F3DCanvas canvas, int x, int y, int depth, Iterable<String> strings, int m) {
		
		int xx = x;
		int yy = y;
		
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
			// Formatting stuff
			float opacity = 1;
			for (String[] fmt : readfmt(str)) {
				if (fmt[0].equals("opacity")) opacity = Float.parseFloat(fmt[1]);
				str = str.substring(fmt[0].length()+fmt[1].length()+4);
			}
			// Color codes
			if (str.startsWith("§`")) canvas.color(Color.DESYELLOW.val());
			if (str.startsWith("§>")) canvas.color(Color.DESBLUE.val());
			if (str.startsWith("§c")) canvas.color(fade(Utility.fromHex("#FF5555"), opacity));
			if (str.startsWith("§b")) canvas.color(fade(Utility.fromHex("#55FFFF"), opacity));
			if (str.startsWith("§7")) canvas.color(fade(Utility.fromHex("#AAAAAA"), opacity));
			if (str.startsWith("§9")) canvas.color(fade(Utility.fromHex("#5555FF"), opacity));
			if (str.startsWith("§h")) canvas.color(fade(Utility.fromHex("#E3D4D1"), opacity));
			if (str.startsWith("§f")) canvas.color(fade(Utility.fromHex("#FFFFFF"), opacity));
			if (str.startsWith("§8")) canvas.color(fade(Utility.fromHex("#555555"), opacity));
			if (str.startsWith("§")) str = str.substring(2);
			canvas.text(xx+m, yy+m, depth+1, str);
			yy += size.y + m*3;
		}
	}
	
	// read formatting information, like "§[bold=true]§[opacity=0.1]"
	private static String[][] readfmt(String source) {
		ArrayList<ArrayList<String>> array = new ArrayList<>();
		while (source.startsWith("§[")) {
			String sub = ""; 
			int index = 2;
			while (index < source.length() && source.charAt(index) != ']') { sub += source.charAt(index); index++; }
			String[] format_code = sub.split(Pattern.quote("="));
			source = source.substring(sub.length()+3);
			
			ArrayList<String> list = new ArrayList<String>();
				list.add(format_code[0]);
				list.add(format_code[1]);
			array.add(list);
		}
		
		String[][] result = new String[array.size()][2];
		for (int i = 0; i < array.size(); i++) {
			result[i][0] = array.get(i).get(0);
			result[i][1] = array.get(i).get(1);
		}
		
		return result;
	}

	private static Vector4f fade(Vector4f color, float amt) {
		if (amt == 1) return color;
		return new Vector4f(color.x, color.y, color.z, color.w * amt);
	}

	public static Vector2i drawStringsCentered(int xoff, int yoff, int margin, int line_sep, F3DCanvas canvas, String mode_info) {
		canvas.color(Color.BLACK.val());
		int width = 0;
		int height = 0;
		for (String line : mode_info.split("\n")) {
			Vector2i size = canvas.textrenderer().size(line.trim());
			if (size.x > width) width = size.x;
			height += size.y + line_sep;
		}
		Vector2i size = new Vector2i(width, height).div(2);
		int xx = canvas.width() / 2;
		int yy = canvas.height() / 2;
		xx += xoff;
		yy += yoff;
		canvas.rect(xx-(size.x+margin), yy-(size.y+margin), xx+size.x+margin, yy+size.y+margin, 0);
		
		canvas.color(Color.WHITE.val());
		
		int offset_yy = 0;
		String[] lines = mode_info.split("\n");
		for (String line : lines) {
			canvas.text(xx-size.x, (yy-size.y)+offset_yy, 0, line.trim());
			offset_yy += (size.y*2) / lines.length;
		}	
		return size.mul(2);
	}


}

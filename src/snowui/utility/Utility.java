package snowui.utility;

import org.joml.Vector4f;

public class Utility {
	
	public static Vector4f fromHex(String hex) {
		return new Vector4f(  
				Integer.valueOf( hex.substring( 1, 3 ), 16 ) / 255f,
				Integer.valueOf( hex.substring( 3, 5 ), 16 ) / 255f,
				Integer.valueOf( hex.substring( 5, 7 ), 16 ) / 255f,
				1);
	}
	
	// css_rgba(82, 52, 145, 0.34)
	public static Vector4f fromRGBADecimal(String rgba) {
		rgba = rgba.substring(9, rgba.length()-1);
		String[] channels = rgba.split(",");
		return new Vector4f(
				Float.parseFloat(channels[0])/255,
				Float.parseFloat(channels[1])/255,
				Float.parseFloat(channels[2])/255,
				Float.parseFloat(channels[3])		// why
				);
	}

}

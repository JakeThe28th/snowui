package frost3d.implementations;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import frost3d.enums.Alignment;
import frost3d.enums.FillMode;
import frost3d.interfaces.F3DPositionedCanvas;
import frost3d.interfaces.GLMesh;
import frost3d.utility.FloatRectangle;
import frost3d.utility.Utility;

public class SimplePositionedCanvas extends SimpleCanvas implements F3DPositionedCanvas {

	Alignment halign;
	@Override public void halign(Alignment a) { halign = a; }
	
	Alignment valign;
	@Override public void valign(Alignment a) { valign = a; }
	
	FillMode fillmode;
	@Override public void fillmode(FillMode f) { fillmode = f; }
	
	int outer_width;
	int outer_height;
	@Override
	public void outrectangle(int outer_width, int outer_height) {
		this.outer_width = outer_width;
		this.outer_height = outer_height;
	}
	
	@Override
	public Vector2i internalpoint(Vector2i input) {
		FloatRectangle rect = Utility.getFittedRectangle(outer_width, outer_height, width, height, halign, valign, fillmode);
		Vector2d vec = Utility.getNormalizedInternalCoordinates(rect, new Vector2d(input.x / (float) outer_width, input.y / (float) outer_height));
		return new Vector2i((int) (vec.x * width), (int) (vec.y * height));
	}

	@Override
	public GLMesh mesh() {
		return Utility.mesh(Utility.getFittedRectangle(outer_width, outer_height, width, height, halign, valign, fillmode), 0);
	}

}

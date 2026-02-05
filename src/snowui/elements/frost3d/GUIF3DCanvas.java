package snowui.elements.frost3d;

import org.joml.Vector2f;
import org.joml.Vector2i;

import frost3d.enums.Alignment;
import frost3d.enums.FillMode;
import frost3d.interfaces.F3DCanvas;
import frost3d.utility.FloatRectangle;
import frost3d.utility.Rectangle;
import frost3d.utility.Utility;
import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.abstracts.GUIElement;

public class GUIF3DCanvas extends GUIElement {
	
	// TODO: Version of this, but just for letterboxing images instead of Canvases //
	
	{ identifier("f3d_canvas"); }
	
	Alignment halign 			= Alignment.MIDDLE;
	Alignment valign 			= Alignment.MIDDLE;
	FillMode  fillmode			= FillMode.CONTAIN;
	boolean	  flip				= false;

	public void halign		(Alignment neo) { this.halign  	= neo; }
	public void valign		(Alignment neo) { this.valign  	= neo; }
	public void fillmode	(FillMode  neo) { this.fillmode = neo; }
	public void flip		(boolean   neo)	{ this.flip 	= neo; }
	
	F3DCanvas canvas			= null;
	Rectangle draw_rectangle	= null;
	
	public GUIF3DCanvas(F3DCanvas canvas) {
		this.canvas = canvas;
	}
	
	public Vector2i internal_mouse(GUIInstance gui) {
		if (draw_rectangle != null) {
			Vector2f normalized = draw_rectangle.normalized(gui.mx(), gui.my());
				     normalized.y = 1-normalized.y;
			return new Vector2i((int) (normalized.x * canvas.width()), (int) (normalized.y * canvas.height()));
		} else {
			return null;
		}
	}

	@Override
	public void recalculateSize(GUIInstance gui) {
		this.unpadded_height = canvas.height();
		this.unpadded_width = canvas.width();
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		Rectangle b = limit_rectangle();
		this.hover_rectangle(b);
		if (canvas.framebuffer() != null) {
			FloatRectangle normalized;
			normalized 		= Utility.getFittedRectangle( b.width(), b.height(), 
														  canvas.width(), canvas.height(), 
														  halign, valign, fillmode );
			normalized 		= normalized.multiply(1, flip ? -1 : 1);
			normalized 		= normalized.normalizeFrom_NegativeOneToOne_TO_ZeroToOne();
			normalized 		= normalized.multiply(b.width(), b.height());
			draw_rectangle 	= normalized.toIntegerRectangle().offset(b.left(), b.top());
		}
	}
	
	public void draw(GUIInstance gui, int depth) {
		if (draw_rectangle != null) {
			gui.canvas().color(style().base_color().color());
			gui.canvas().rect(draw_rectangle, depth, canvas.framebuffer().texture());
		} else {
			gui.canvas().color(Color.RED.val());
			int tx = limit_rectangle().center().x - gui.canvas().textrenderer().size("Failed to render Canvas").x/2;
			int ty = limit_rectangle().center().y - gui.canvas().textrenderer().size("Failed to render Canvas").y/2;
			gui.canvas().text(tx, ty, depth, "Failed to render Canvas");
		}
	}

}

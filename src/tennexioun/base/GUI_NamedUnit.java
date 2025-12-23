package tennexioun.base;

import org.joml.Vector4f;

import snowui.GUIInstance;
import snowui.coss.enums.Color;
import snowui.elements.abstracts.GUIElement;
import snowui.elements.base.GUIClickableRectangle;
import snowui.elements.base.GUIIcon;
import snowui.elements.extended.GUITextBox;

public abstract class GUI_NamedUnit extends GUIElement {

	GUI_NamedUnit element_this() { return this; }
	
	public GUIClickableRectangle 	bg;
	public GUITextBox 				title;
	public GUIIcon 					icon;
		
	public Vector4f					color;
		
	public GUI_NamedUnit(String title) {
		color = Color.WHITE.val();
		bg = new GUIClickableRectangle() {
			@Override
			public void draw(GUIInstance gui, int depth) {
				gui.canvas().color(color);
				gui.canvas().rect(padded_limit_rectangle(), depth);
			}
			@Override public void onSingleClick() { element_this().onSingleClick(); }
		};						
		this.registerSubElement(bg);
		
		if (title != null) {
			this.title = new GUITextBox(title) {
				@Override
				public void onFinishEditing(String old_text, String new_text) {
					onFinishEditingTitle(old_text, new_text);
				}
			};
			this.title.hide_background(true);
			this.registerSubElement(this.title);
		}
	}
	
	public void onFinishEditingTitle(String old_text, String new_text) {
		
	}

	@Override
	public void updateDrawInfo(GUIInstance gui) {
		bg.limit_rectangle(this.aligned_limit_rectangle());
		if (title != null) title.limit_rectangle(this.aligned_limit_rectangle());
		this.hover_rectangle(bg.limit_rectangle());
	}

	@Override public void draw(GUIInstance gui, int depth) { }

}


package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

import com.pluvicsoftware.javaVN.Constants;

public class DBLabel extends DBElement {
	
	/* ============ *
	 * CONSTRUCTORS *
	 * ============ */
	public DBLabel (int x, int y, String text) {
		super(x, y, 0, 0);
		System.out.println("Creating Label...");
		
		setTextAndFont(text, font);
	}
	
	public DBLabel (int x, int y, String text, Font font) {
		super(x, y, 0, 0);
		System.out.println("Creating Label...");
		
		setTextAndFont(text, font);
	}
	
	public DBLabel (int x, int y, String text, Font font, Color color) {
		super(x, y, 0, 0);
		System.out.println("Creating Label...");
		
		setTextAndFont(text, font);
		this.color = color;
	}
	
	public DBLabel (int x, int y, String text, Color color) {
		super(x, y, 0, 0);
		System.out.println("Creating Label...");
		
		setTextAndFont(text, font);
		this.color = color;
	}
	/* ================ *
	 * END CONSTRUCTORS *
	 * ================ */
	
	
	//Draw function
	public void draw(Graphics2D g2d) {
		//System.out.println("Drawing label...");
		g2d.setColor(color);
		g2d.setFont(font);
		g2d.drawString(text, xpos, ypos);
		getSizeFromContext(g2d.getFontMetrics());
		//if (width == 0 && height == 0) getSizeFromContext(g2d.getFontMetrics());
	}
	
	public boolean contains(int x, int y) {
		if(x >= xpos && x <= xpos + width && y <= ypos && y >= ypos - height) return true;
		return false;
	}
	
	private void getSizeFromContext(FontMetrics m) {
		//System.out.println("Getting size...");
		width = m.stringWidth(text);
		height = m.getAscent() - m.getDescent();
	}
	
	private void setTextAndFont(String text, Font font) {
		this.text = text;
		
		//This line of code checks if the font needs to be scaled up and, if so, scales it
		this.font = DBConstants.isScalingEnabled() ? font.deriveFont(scalef(font.getSize())) : font;
	}
	
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	
	public Font getFont() {return font;}
	public void setFont(Font font) {this.font = font;}
	
	public Color getColor() {return color;}
	public void setColor(Color color) {this.color = color;}
	
	private String text;
	private Font font = Constants.DEFAULT_LABEL_FONT;
	private Color color = Constants.DEFAULT_LABEL_COLOR;
}
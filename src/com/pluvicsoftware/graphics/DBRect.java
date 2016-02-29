package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import com.pluvicsoftware.graphics.DBElement;

public class DBRect extends DBElement {
	/* Constructor is supplied with position, size, color, and a boolean
	 * -> If isFilled = true, creates a filled rectangle of the given color
	 * -> If isFilled = false, creates a rectangle frame of the given color */
	public DBRect(int x, int y, int width, int height, Color color, boolean isFilled) {
		super(x, y, width, height);
		borderColor = color;
		if (isFilled) fillColor = color;
		this.isFilled = isFilled;
	}
	
	/* Constructor is supplied with position, size, and two colors
	 * Creates a filled rectangle with the given fill color and border color */
	public DBRect(int x, int y, int width, int height, Color fillColor, Color borderColor) {
		super(x, y, width, height);
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		isFilled = true;
	}
	
	public void setFillColor(Color color) {this.fillColor = color;}
	public Color getFillColor() {return fillColor;}
	
	public void setBorderColor(Color color) {this.borderColor = color;}
	public Color getBorderColor() {return borderColor;}
	
	public void draw(Graphics2D g2d) {
		if (isFilled) {
			g2d.setColor(fillColor);
			g2d.fillRect(xpos, ypos, width, height);
		}
		g2d.setColor(borderColor);
		g2d.drawRect(xpos, ypos, width, height);
	}
	
	protected Color fillColor;
	protected Color borderColor;
	protected Boolean isFilled;
}
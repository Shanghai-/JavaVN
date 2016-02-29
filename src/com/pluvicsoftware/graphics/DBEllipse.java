package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

public class DBEllipse extends DBElement {
	public DBEllipse(int x, int y, int width, int height, Color color, boolean isFilled) {
		super(x, y, width, height);
		System.out.println("Creating Ellipse...");
		
		borderColor = color;
		if (isFilled) fillColor = color;
		this.isFilled = isFilled;
	}
	
	public DBEllipse(int x, int y, int width, int height, Color color, boolean isFilled, int thickness) {
		super(x, y, width, height);
		System.out.println("Creating Ellipse...");
		
		borderColor = color;
		if (isFilled) fillColor = color;
		this.thickness = thickness;
		this.isFilled = isFilled;
	}
	
	public DBEllipse(int x, int y, int width, int height, Color fillColor, Color borderColor) {
		super(x, y, width, height);
		System.out.println("Creating Ellipse...");
		
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		isFilled = true;
	}
	
	public void draw(Graphics2D g2d) {
		if (isFilled) {
			g2d.setColor(fillColor);
			g2d.fillOval(xpos, ypos, width, height);
		}
		g2d.setColor(borderColor);
		g2d.drawOval(xpos, ypos, width, height);
		//TODO: this thickness fix is fucking weak. Look into stroking with g2d.
		if (thickness > 1) {
			for (int i = 1; i < thickness; i++) {
				g2d.drawOval(xpos + i, ypos + i, width - 2*i, height - 2*i);
			}
		}
	}
	
	protected Color fillColor;
	protected Color borderColor;
	protected Boolean isFilled;
	protected int thickness = 1;
}
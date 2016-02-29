package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

public class DBRoundRect extends DBRect {
	public DBRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, Color color, boolean isFilled) {
		super(x, y, width, height, color, isFilled);
		System.out.println("Creating Rounded Rectangle...");
		
		assignArcValues(arcWidth, arcHeight);
	}
	
	public DBRoundRect(int x, int y, int width, int height, Color color, boolean isFilled) {
		super(x, y, width, height, color, isFilled);
		System.out.println("Creating Rounded Rectangle...");
		
		assignArcValues(height, height);
	}
	
	public DBRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight, Color fillColor, Color borderColor) {
		super(x, y, width, height, fillColor, borderColor);
		System.out.println("Creating Rounded Rectangle...");
		
		assignArcValues(arcWidth, arcHeight);
	}
	
	public DBRoundRect(int x, int y, int width, int height, Color fillColor, Color borderColor) {
		super(x, y, width, height, fillColor, borderColor);
		System.out.println("Creating Rounded Rectangle...");
		
		assignArcValues(height, height);
	}
	
	public void draw(Graphics2D g2d) {
		if (isFilled) {
			g2d.setColor(fillColor);
			g2d.fillRoundRect(xpos, ypos, width, height, arcWidth, arcHeight);
		}
		g2d.setColor(borderColor);
		g2d.drawRoundRect(xpos, ypos, width, height, arcWidth, arcHeight);
	}
	
	private void assignArcValues(int arcWidth, int arcHeight) {
		if (DBConstants.isScalingEnabled()) {
			this.arcWidth = scale(arcWidth);
			this.arcHeight = scale(arcHeight);
		} else {
			this.arcWidth = arcWidth;
			this.arcHeight = arcHeight;
		}
	}
	
	private int arcWidth;
	private int arcHeight;
}
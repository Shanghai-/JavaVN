package com.pluvicsoftware.graphics;

import java.awt.Graphics2D;

import com.pluvicsoftware.graphics.DBConstants;

//TODO: Add support for x and y positions by points. This may mean pointsWidth and pointsHeight methods
public class DBElement {
	public DBElement(int x, int y, int width, int height) {
		System.out.println("Creating Element...");
		if (DBConstants.isScalingEnabled()) {
			xpos = scale(x);
			ypos = scale(y);
			this.width = scale(width);
			this.height = scale(height);
		} else {
			xpos = x;
			ypos = y;
			this.width = width;
			this.height = height;
		}
	}
	
	public void drawElement(Graphics2D g2d) {
		if (isVisible) draw(g2d);
	}
	
	public void draw(Graphics2D g2d) {
		//Override this function to custom-draw elements
	}
	
	public boolean containsX(int x) {
		if(x >= xpos && x <= xpos + width) return true;
		return false;
	}
	
	public boolean containsY(int y) {
		if(y >= ypos && y <= ypos + height) return true;
		return false;
	}
	
	public boolean contains(int x, int y) {
		if(containsX(x) && containsY(y)) return true;
		return false;
	}
	
	//TODO: Getters and setters for all of the variables
	
	public void setWidth(int w) {width = w;}
	public void setHeight(int h) {height = h;}
	
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	public void setPos(int x, int y) {
		setXPos(x);
		setYPos(y);
	}
	
	public void setXPos(int x) {xpos = x;}
	public void setYPos(int y) {ypos = y;}
	
	public int getXPos() {return xpos;}
	public int getYPos() {return ypos;}
	
	public void center(int x, int y) {
		this.xpos = Math.round(x - width / 2);
		this.ypos = Math.round(y - height / 2);
	}
	
	public void setVisible(boolean flag) {isVisible = flag;}
	
	//Scales pixel values in the default resolution to the current resolution
	protected static int scale(int val) {return Math.round(val * DBConstants.getScaleFactor());}
	protected static float scalef(int val) {return val * DBConstants.getScaleFactor();}
	
	protected static int unscale(int val) {
		//System.out.println("Scaled value is: " + val);
		int result = Math.round(val / DBConstants.getScaleFactor());
		//System.out.println("Unscaled value is: " + result);
		return result;
	}
	
	protected int xpos;
	protected int ypos;
	protected int width;
	protected int height;
	protected boolean isVisible = true;
}
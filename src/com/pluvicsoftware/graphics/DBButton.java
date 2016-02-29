package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.pluvicsoftware.javaVN.Constants;

public class DBButton extends DBElement{
	public DBButton(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		System.out.println("Creating Button...");
		label = new DBLabel(x, y, text, Constants.DEFAULT_BUTTON_LABEL_FONT, Constants.DEFAULT_LABEL_COLOR);
		buttonColor = Constants.DEFAULT_BUTTON_COLOR;
		isSelected = false;
	}
	
	public DBButton(int x, int y, int width, int height, String text, Color btnColor, Color txtColor, Font txtFont) {
		super(x, y, width, height);
		System.out.println("Creating Button...");
		buttonColor = btnColor;
		isSelected = false;
	}
	
	//Checks if the bounds of the button contain the given coordinates
	/*public boolean contains(int x, int y) {
		if (x >= xpos && x <= xpos + width && 
				y >= ypos && y <= ypos + height) return true;
		return false;
	}*/
	
	//Returns whether or not the button is selected
	public boolean isSelected() {return isSelected;} 
	
	//Sets whether or not the button is selected
	public void setSelected(boolean val) {
		isSelected = val;
		if(val) label.setColor(Color.lightGray);
		if(!val) label.setColor(Color.white);
	}
	
	public void draw(Graphics2D g2d) {
		
	}
	
	protected DBLabel label;
	protected Color buttonColor;
	protected boolean isSelected;
}
/*
 * Much like MenuButton.java, this class holds variables for a menu object.
 * It exists to do calculations and set positions of the elements of a 
 * slider so that the draw loop can reference these variables and draw them.
 * The getter methods should be used in draw loops to draw accurate sliders.
 */

package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.pluvicsoftware.javaVN.Constants;

//TODO: The center method does NOT play nice with this thing. Find out why.
public class DBSlider extends DBElement {
	public DBSlider(String title, int x, int y, int width, int value) {
		super(x, y, width, 8);
		System.out.println("Creating Slider...");
		//TODO: More accurate and flexible position values
		this.title = new DBLabel(x, y + 20, title);
		sliderValue = value;
		slider = new DBRoundRect(x, y, width, 8, Color.white, true);
		marker = new DBEllipse(Math.round(x + value * increment) - 5, y - 1, 10, 10, Color.lightGray, true);
		increment = width / 100;
		label = new DBLabel(x + width + 10, y - 18, String.valueOf(value).concat("%"));
	}
	
	/*public DBSlider(String title, int x, int y, int width, int height, int value) {
		super(x, y, width, height);
		System.out.println("Creating Slider...");
		//TODO: More accurate and flexible position values
		this.title = new DBLabel(x, y + 20, title);
		sliderValue = value;
		sliderColor = Color.white;
		markerColor = Color.lightGray;
		increment = width / 100;
		markerX = Math.round(x + value * increment) - 5;
		markerY = y - 1;
		label = new DBLabel(x + width + 10, y - 18, String.valueOf(value).concat("%"));
	}*/
	
	public void draw(Graphics2D g2d) {
		/*Turns on antialiasing for text
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		
		g2d.setColor(sliderColor);
		g2d.drawRoundRect(xpos, ypos, width, height, height, height);
		g2d.fillRoundRect(xpos, ypos, width, height, height, height);
		g2d.setColor(markerColor);
		g2d.drawOval(markerX, markerY, 10, 10);
		g2d.fillOval(markerX, markerY, 10, 10);
		
		if (!labelsPositioned) {
			System.out.println("Positioning slider labels");
			g2d.setFont(title.getFont());
			FontMetrics fm = g2d.getFontMetrics();
			title.setPos(xpos + 4, ypos - fm.getDescent());
			label.setPos(xpos + width + height, ypos + 18);
			labelsPositioned = true;
		}
		title.draw(g2d);
		label.draw(g2d);*/
		/*g2d.setColor(Color.white);
		g2d.setFont(new Font("Gill Sans MT", Font.PLAIN, 36));
		FontMetrics fm = g2d.getFontMetrics();
		//TODO: FINISH THE POSITIONING, COLORING, ETC. FOR THIS LABEL
		g2d.setFont(title.getFont());
		title.draw(g2d);
		label.draw(g2d);
		g2d.drawString(sliderTitle, xpos + 4, ypos - fm.getDescent());
		g2d.drawString(String.valueOf(sliderValue).concat("%"), xpos + width + height, ypos + 18);*/
	}
	
	/*
	 * ====================
	 * Begin getter methods
	 * ====================
	 */
	public int getX() {return xpos;}
	public int getY() {return ypos;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	
	public int getMarkerX() {return marker.xpos;}
	public int getMarkerY() {return marker.ypos;}
	public float getIncrement() {return increment;}
	public int getValue() {return sliderValue;}
	
	/*
	 * ====================
	 * Begin setter methods
	 * ====================
	 */
	
	public void setMarkerX(int x) {
		//markerX = x - 5;
		sliderValue = Math.round((x - xpos) / increment);
		label.setText(String.valueOf(sliderValue).concat("%"));
	}
	
	private DBLabel title;
	private DBLabel label;
	private boolean labelsPositioned = false;
	private DBRect slider;
	private float increment;
	private int sliderValue;
	private DBEllipse marker;
}
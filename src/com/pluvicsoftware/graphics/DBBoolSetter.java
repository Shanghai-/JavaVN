/*
 * Option 1 is considered to be the "On" option
 * Option 2 is considered to be the "Off" option
 * Therefore the option boolean refers to Option 1 if true,
 * and Option 2 if false.
 */

package com.pluvicsoftware.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.pluvicsoftware.javaVN.Constants;

public class DBBoolSetter extends DBElement {
	public DBBoolSetter(int x, int y, String title, String option1, String option2, boolean option) {
		super(x, y, 0, 0);
		titleLabel = new DBLabel(x, y, title, Constants.DEFAULT_BUTTON_LABEL_FONT);
		option1Label = new DBLabel(x - 20, y + 20, option1);
		option2Label = new DBLabel(x + 20, y + 20, option2);
		this.option = option;
		option1Label.setColor((option) ? Color.white : Color.gray);
		option2Label.setColor((!option) ? Color.white : Color.gray);
	}
	
	public void draw(Graphics2D g2d) {
		titleLabel.draw(g2d);
		option1Label.draw(g2d);
		option2Label.draw(g2d);
		if (!labelsPositioned) {
			titleLabel.setXPos(xpos - titleLabel.width / 2);
			//TODO: Position options labels more accurately. Currently, they can overlap + are not centered.
			//int optionsCenter = titleLabel.xpos + (option1Label.width + option2Label.width + 16) / 2;
			option1Label.setPos(xpos - option1Label.width - 8, ypos + titleLabel.height + 8);
			option2Label.setPos(xpos + 8, ypos + titleLabel.height + 8);
			labelsPositioned = true;
			//draw(g2d);
		}
		g2d.setColor(Color.cyan);
		if (option) g2d.drawRect(option1Label.xpos - 2, option1Label.ypos - option1Label.height - 2, 
				option1Label.width + 2, option1Label.height + 3);
		if (!option) g2d.drawRect(option2Label.xpos - 2, option2Label.ypos - option2Label.height - 2, 
				option2Label.width + 2, option2Label.height + 3);
	}
	
	public void optionSelected(int mouseX, int mouseY) {
		if (option1Label.contains(mouseX, mouseY)) {
			System.out.println("Option 1 clicked");
			option1Label.setColor(Color.white);
			option2Label.setColor(Color.gray);
			option = true;
		} else if (option2Label.contains(mouseX, mouseY)) {
			System.out.println("Option 2 clicked");
			option2Label.setColor(Color.white);
			option1Label.setColor(Color.gray);
			option = false;
		}
	}
	
	public boolean option() {
		return option;
	}
	
	private DBLabel titleLabel;
	private DBLabel option1Label;
	private DBLabel option2Label;
	private boolean labelsPositioned = false;
	private boolean option;
}
package com.pluvicsoftware.javaVN;

import java.awt.Image;

import com.pluvicsoftware.graphics.DBImageAnimatable;

/**
 * Graphical element. This class represents a character sprite, which is
 * animatable and has a name. The only required constructor arguments are
 * the sprite's name, x, y position, and either the path to the image resource 
 * or an actual Image object. 
 *  
 * @author Brendan
 */
public class DBSprite extends DBImageAnimatable {
	public DBSprite(String name, int x, int y, int width, int height, Image img) {
		super(x, y, width, height, img);
		if (Constants.DEBUG) System.out.println("Creating Sprite...");
		center(x, y);
		this.name = name;
	}
	
	public DBSprite(String name, int x, int y, int width, int height, String path) {
		super(x, y, width, height, path);
		if (Constants.DEBUG) System.out.println("Creating Sprite...");
		center(x, y);
		this.name = name;
	}
	
	public DBSprite(String name, int x, int y, Image img) {
		super(x, y, img);
		if (Constants.DEBUG) System.out.println("Creating Sprite...");
		center(x, y);
		this.name = name;
	}
	
	public DBSprite(String name, int x, int y, String path) {
		super(x, y, path);
		if (Constants.DEBUG) System.out.println("Creating Sprite...");
		center(x, y);
		this.name = name;
	}
	
	String name;
}
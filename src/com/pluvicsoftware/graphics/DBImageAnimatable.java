package com.pluvicsoftware.graphics;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

public class DBImageAnimatable extends DBAnimatable {
	public DBImageAnimatable(int x, int y, int width, int height, Image img) {
		super(x, y, width, height);
		System.out.println("Creating Image...");
		image = img;
	}
	
	public DBImageAnimatable(int x, int y, int width, int height, String path) {
		super(x, y, width, height);
		System.out.println("Creating Image...");
		image = new ImageIcon(path).getImage();
	}
	
	public DBImageAnimatable(int x, int y, Image img) {
		super(x, y, 0, 0);
		System.out.println("Creating Image...");
		image = img;
	}
	public DBImageAnimatable(int x, int y, String path) {
		super(x, y, 0, 0);
		System.out.println("Creating Image...");
		image = new ImageIcon(path).getImage();
	}
	
	public void setImage(Image img) {
		image = img;
	}
	
	public void setImage(String path) {
		image = new ImageIcon(path).getImage();
	}
	
	public void draw(Graphics2D g2d) {
		if (width == 0 && height == 0) {
			g2d.drawImage(image, xpos, ypos, null);
			width = scale(image.getWidth(null));
			height = scale(image.getHeight(null));
			//System.out.println("Width is: " + width + " and height is: " + height);
			//draw(g2d);
		}
		if (alpha != 1.0f) {
			g2d.setComposite(AlphaComposite.getInstance
					(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(image, xpos, ypos, width, height, null);
			g2d.setComposite(AlphaComposite.getInstance
					(AlphaComposite.SRC_OVER, 1.0f));
		} else {
			g2d.drawImage(image, xpos, ypos, width, height, null);
		}	
	}
	
	public void update() {
		if (isWithin(alphaTarget, Math.abs(alphaInterval), alpha)){
			alphaInterval = 0;
		} else {
			alpha += alphaInterval;
		}
	}
	
	/* public boolean isDoneAnimating() {
		return alphaInterval == 0;
	} */
	
	public void animateAlpha(float interval, float target) {
		alphaInterval = interval;
		alphaTarget = target;
		System.out.println("Animating alpha...");
	}
	
	public float getAlpha() {return alpha;}
	public void setAlpha(float a) {alpha = a;}
	
	/* TODO: Change animatable internal representations so that
	 * position, width, etc. are stored as floats, but drawn rounded. 
	 * In other words, the internal measurement is precise, but the
	 * displayed one is fuzzed so it can show up right. */
	protected Image image;
	protected float alpha = 1.0f;
	protected float alphaInterval;
	protected float alphaTarget;
}
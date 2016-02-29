package com.pluvicsoftware.javaVN;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.pluvicsoftware.graphics.DBConstants;

/**
 * Displays the splash screen
 * 
 * @author Brendan
 */
public class SplashPanel extends JPanel {
	private Image splashImg;
	
	/**
	 * Constructor. Finds splash screen image.
	 */
	public SplashPanel() {
		splashImg = new ImageIcon("src/media/backgrounds/psoft splash.png").getImage();
	}
	
	// Low-level drawing method.
	private void doDrawing(Graphics g) {
		//System.out.println("Drawing splash panel from class file...");
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(splashImg, 0, 0, DBConstants.getWidth(), DBConstants.getHeight(), null);
	}
	
	// Low-level drawing method.
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent (g);
		doDrawing(g);
	}
}
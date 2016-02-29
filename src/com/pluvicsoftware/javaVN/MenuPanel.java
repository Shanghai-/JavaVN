package com.pluvicsoftware.javaVN;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import com.pluvicsoftware.graphics.DBAnimatable;
import com.pluvicsoftware.graphics.DBConstants;
import com.pluvicsoftware.graphics.DBElement;
import com.pluvicsoftware.graphics.DBImage;
import com.pluvicsoftware.graphics.DBLabel;
import com.pluvicsoftware.graphics.DBLayeredPane;

/**
 * A panel containing the main menu. 
 * 
 * @author Brendan
 */
public class MenuPanel extends DBLayeredPane { 
	//Images
	private DBImage characterImage;
	private ArrayList<Image> characters = new ArrayList<Image>();
	private int charCount;
	
	//Audio Clips
	DBSoundManager sm = new DBSoundManager(3);
	/*AudioClip buttonTouch;
	AudioClip buttonClick;
	AudioClip bgm; */
	
	//Timer to update the screen at 60 frames per second (capped, with dropping)
	protected Timer update;
	private boolean canUpdate;
	protected ArrayList<DBAnimatable> toUpdate = new ArrayList<DBAnimatable>();
	protected int frames = 0;
	
	//Buttons
	private MenuButton newGameButton;
	private MenuButton loadGameButton;
	private MenuButton optionsButton;
	private MenuButton statsButton;
	private MenuButton exitGameButton;
	private ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	/* TODO: Learn more about UI code. Is it more reasonable to put the buttons
	 * all in an array and do things like that? */
	
	//Mouse tracking variables
	private int lastMouseX;
	private int lastMouseY;
	
	/**
	 * Constructor. Builds a new MenuPanel.
	 */
	public MenuPanel() {
		if (Constants.DEBUG) System.out.println("Creating new MenuPanel...");
		
		//Adds background image and character images
		addElement(new DBImage(0, 0, DBConstants.getWidth(), DBConstants.getHeight(), 
				"src/media/backgrounds/menu placeholder.png"), 0);
		characters.add(new ImageIcon("src/media/sprites/Actbird.png").getImage());
		characters.add(new ImageIcon("src/media/sprites/Jockbird.png").getImage());
		characters.add(new ImageIcon("src/media/sprites/Artbird.png").getImage());
		characterImage = new DBImage(543, 0, characters.get(0));
		addElement(characterImage);
		
		sm.setSFX(0, "textBlip.wav");
		sm.setSFX(1, "selectBlip.wav");
		
		//Initializes each menu button
		newGameButton = new MenuButton(-30, 210, 300, 60, "New Game");
		loadGameButton = new MenuButton(-30, 275, 300, 60, "Load Game");
		optionsButton = new MenuButton(-30, 340, 300, 60, "Options");
		statsButton = new MenuButton(-30, 405, 300, 60, "Gallery / Stats");
		exitGameButton = new MenuButton(-30, 470, 300, 60, "Exit Game");
		buttons.add(newGameButton);
		buttons.add(loadGameButton);
		buttons.add(optionsButton);
		buttons.add(statsButton);
		buttons.add(exitGameButton);
		addElement(newGameButton);
		addElement(loadGameButton);
		addElement(optionsButton);
		addElement(statsButton);
		addElement(exitGameButton);
		
		acceptingInput = true;
		
		addElement(new DBLabel(5, 72, "Video Game", 
				new Font("Century Gothic", Font.PLAIN, 72)));
		addElement(new DBLabel(5, 571, "Copyright 2015 - 2016 Brendan Walsh", 
				new Font("Centry Gothic", Font.PLAIN, 20)));
		
		//Creates and adds a new mouse-click listener
		addMouseListener(new MouseAdapter() {
			//Overrides the default mousePressed function for custom actions on-click
			public void mousePressed(MouseEvent me) {
				if (acceptingInput) checkButtonPressed(me.getX(), me.getY());
				if (Constants.DEBUG) {
					System.out.println("Mouse down at x: " 
							+ me.getX() + " y: " + me.getY());
				}
			}
		});
		
		//Creates and adds a new mouse-movement listener
		addMouseMotionListener(new MouseAdapter() {
			//Overrides the default mouseMoved function for mouse position logging
			public void mouseMoved(MouseEvent me) {
				if (acceptingInput) {
					//Saves current mouse position
					lastMouseX = me.getX();
					lastMouseY = me.getY();					
				}
			}
		});
				
		//Creates a new ActionListener for the update loop timer to call
		ActionListener updateTimer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (canUpdate) {
					canUpdate = false;
					for (MenuButton btn : buttons) {
						//Checks if the button contains the mouse
						if (btn.contains(lastMouseX, lastMouseY)) {
							//If the button isn't already selected, set to selected & play sound
							if (!btn.isSelected()) {
								sm.playSFX(0);
								btn.setSelected(true);
							}
							//If button contains mouse and has space to expand, widen by 6px
							if (btn.canExpand()) btn.resizeButton(6);
						} else if (btn.isExpanded()){
							//If button does not contain mouse and is expanded, contract by 12px
							btn.setSelected(false);
							btn.resizeButton(-12);
						}
					}
					if (frames == 300) {
						charCount++;
						charCount = charCount % characters.size();
						characterImage.setImage(characters.get(charCount));
						frames = 0;
					}

					//Repaints the screen after the changes have been made to each object
					repaint();
					frames++;
					canUpdate = true;
				}
			}
		};
		update = new Timer(16, updateTimer);
		update.start();
		
		//System.out.println(getElements().toString());
		//bgm.play();
	}
	
	/**
	 * Checks if the mouse click was on any of our buttons
	 * 
	 * @param x - the x position of the mouse click
	 * @param y - the y position of the mouse click
	 */
	private void checkButtonPressed(int x, int y) {
		if (newGameButton.contains(x,y)) {
			sm.playSFX(1);
		}
		if (loadGameButton.contains(x,y)) sm.playSFX(1);;
		if (optionsButton.contains(x,y)) {
			sm.playSFX(1);
			showOptionsMenu();
		}
		if (statsButton.contains(x,y)) sm.playSFX(1);
		if (exitGameButton.contains(x,y)) {
			sm.playSFX(1);
			System.exit(1);
		}
	}
	
	/* private void checkButtonExpansions() {
		for (MenuButton btn : buttons) {
			//Checks if the button contains the mouse
			if (btn.contains(lastMouseX, lastMouseY)) {
				//If the button isn't already selected, set to selected & play sound
				if (!btn.isSelected()) {
					buttonTouch.play();
					btn.setSelected(true);
				}
				//If button contains mouse and has space to expand, widen by 6px
				if (btn.canExpand()) btn.resizeButton(6);
			} else if (btn.isExpanded()){
				//If button does not contain mouse and is expanded, contract by 12px
				btn.setSelected(false);
				btn.resizeButton(-12);
			}
		}
	} */
	
	//Custom drawing of menu
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		//Turns on antialiasing for text
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		
		for (DBElement el : getElements()) el.drawElement(g2d);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent (g);
		doDrawing(g);
	}
}
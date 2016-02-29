package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Timer;

import com.pluvicsoftware.graphics.DBBoolSetter;
import com.pluvicsoftware.graphics.DBButton;
import com.pluvicsoftware.graphics.DBElement;
import com.pluvicsoftware.graphics.DBLabel;
import com.pluvicsoftware.graphics.DBLayeredPane;
import com.pluvicsoftware.graphics.DBPanel;
import com.pluvicsoftware.graphics.DBSlider;

/**
 * OptionsPanel displays controls for all game options (volume, screen effects
 * on/off, etc.) in a panel layered over its parent panel. The parent must be
 * a DBLayeredPane, and must be passed to the OptionsPanel via the constructor.
 * 
 * @author Brendan
 */
public class OptionsPanel extends DBPanel {
	//Mouse tracking variables
	private int lastMouseX;
	private int lastMouseY;
	private boolean acceptingInput;
	
	// Audio Clips
	DBSoundManager sm = new DBSoundManager(2); 
	
	//Redraw timer, ~60 FPS
	private Timer updateTimer;
	
	//Buttons and sliders
	private DBSlider musicVolumeSlider;
	private DBSlider SFXVolumeSlider;
	private OptionsButton closeOptionsButton;
	private DBSlider lastSlider;
	private ArrayList<DBBoolSetter> setterList;
	/*private DBBoolSetter screenModeSetter;
	private DBBoolSetter screenEffectsSetter;*/
	
	//Parent layered pane. See class comment for more detail.
	private DBLayeredPane parent;
	
	//Singleton used to save and load options values
	private OptionsSingleton os;
	
	/**
	 * Constructor. Builds a new DBOptionsPanel with the given parent pane.
	 * 
	 * @param parent - The DBLayeredPane which contains this panel as a layer.
	 * 				   This is a necessary connection so that the panel can be
	 * 				   closed.
	 */
	public OptionsPanel(DBLayeredPane parent) {
		if (Constants.DEBUG) System.out.println("Creating new OptionsPanel...");
		this.parent = parent;
		setOpaque(false);
		setBounds(0, 0, parent.getWidth(), parent.getHeight());
		
		os = OptionsSingleton.getInstance();
		
		acceptingInput = true;
		
		sm.setSFX(0, "textBlip.wav");
		sm.setSFX(1, "selectBlip.wav");
		
		// Adds all the elements
		addElement(new DBLabel(360, 72, "Options", new Font("Century Gothic", Font.PLAIN, 72)));
		musicVolumeSlider = new DBSlider("Music Volume", 10, 10, 400, os.getMusicVolume());
		musicVolumeSlider.center(512, 140);
		addElement(musicVolumeSlider);
		SFXVolumeSlider = new DBSlider("SFX Volume", 50, 50, 400, os.getSFXVolume());
		SFXVolumeSlider.center(512, 240);
		addElement(SFXVolumeSlider);
		closeOptionsButton = new OptionsButton(100, 100, 180, 60, "Close");
		closeOptionsButton.center(341, 487);
		addElement(closeOptionsButton);
		
		setterList = new ArrayList<DBBoolSetter>();
		DBBoolSetter screenModeSetter = new DBBoolSetter(640, 350, "Screen Mode", "Fullscreen", "Windowed", os.isFullScreen());
		addElement(screenModeSetter);
		setterList.add(screenModeSetter);
		DBBoolSetter screenEffectsSetter = new DBBoolSetter(640, 450, "Screen Effects", "On", "Off", os.getScreenEffects());
		addElement(screenEffectsSetter);
		setterList.add(screenEffectsSetter);
		
		//Creates and adds a new mouse-click listener
		addMouseListener(new MouseAdapter() {
			//Overrides the default mousePressed function for custom actions on-click
			public void mousePressed(MouseEvent me) {
				if(acceptingInput) {
					checkSliderClicked(musicVolumeSlider);
					checkSliderClicked(SFXVolumeSlider);
					checkSetterClicked();
					checkButtonClicked();
				}
				if (Constants.DEBUG) System.out.println("Mouse Pressed");
			}
		});
				
		//Creates and adds a new mouse-movement listener
		addMouseMotionListener(new MouseAdapter() {
			//Overrides the default mouseMoved function for mouse position logging
			public void mouseMoved(MouseEvent me) {
				if(acceptingInput) {
					//Saves current mouse position
					lastMouseX = me.getX();
					lastMouseY = me.getY();
					checkButtonHighlighted(closeOptionsButton);
				}
			}
			
			public void mouseDragged(MouseEvent me) {
				if(acceptingInput) {
					lastMouseX = me.getX();
					lastMouseY = me.getY();
					checkSliderDragged(lastSlider);
				}
			}
		});
		
		//Creates a new ActionListener for the update loop timer to call
		ActionListener update = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//Repaints the screen after the changes have been made to each button
				repaint();
			}
		};
		updateTimer = new Timer(16, update);
		updateTimer.start();
	}
	
	/**
	 * Checks if the given slider was clicked, and if so, marks it as the last
	 * slider clicked.
	 * 
	 * @param sl - the slider to check
	 */
	private void checkSliderClicked(DBSlider sl) {
		if (sl.contains(lastMouseX, lastMouseY)) {
			sl.setMarkerX(lastMouseX);
			lastSlider = sl;
		}
	}
	
	/**
	 * Checks if the given slider's value thingie was dragged, and if so, sets
	 * its marker's X position to the mouse's X position.
	 * 
	 * @param sl - the slider to check
	 */
	private void checkSliderDragged(DBSlider sl) {
		if (sl.containsX(lastMouseX)) sl.setMarkerX(lastMouseX);
	}
	
	/**
	 * Checks which boolean setter was clicked and changes its value
	 */
	private void checkSetterClicked() {
		for (DBBoolSetter b : setterList) {
			b.optionSelected(lastMouseX, lastMouseY);
		}
	}
	
	/**
	 * Checks which button was clicked. If it was the close button, then
	 * ask the parent to close the options menu.
	 */
	private void checkButtonClicked() {
		if (closeOptionsButton.contains(lastMouseX, lastMouseY)) {
			sm.playSFX(1);
			closeOptionsMenu();
		}
	}
	
	/**
	 * Saves all the options in their current state and then asks the parent 
	 * DBLayeredPane to close the options panel.
	 */
	private void closeOptionsMenu() {
		updateTimer.stop();
		acceptingInput = false;
		os.setMusicVolume(musicVolumeSlider.getValue());
		os.setSFXVolume(SFXVolumeSlider.getValue());
		System.out.println(setterList.get(0).option());
		os.setFullScreen(setterList.get(0).option());
		os.setScreenEffects(setterList.get(1).option());
		os.saveOptions();
		parent.hideOptionsMenu();
	}
	
	/**
	 * Checks if the mouse is over the given button and highlights it if so
	 * 
	 * @param btn - The button to check
	 */
	private void checkButtonHighlighted(DBButton btn) {
		//Checks if the button contains the mouse
		if (btn.contains(lastMouseX, lastMouseY)) {
			//If the button isn't already selected, set to selected & play sound
			if (!btn.isSelected()) {
				sm.playSFX(0);
				btn.setSelected(true);
			}
		} else {
			btn.setSelected(false);
		}
	}
	
	//DRAWING CODE
	private void doDrawing(Graphics g) {
		//System.out.println("Options custom paint called");
		Graphics2D g2d = (Graphics2D) g;
		
		//Draws the translucent backing panel
		g2d.setColor(new Color(80, 80, 80, 180)); //0-255
		g2d.drawRect(0, 0, getWidth(), getHeight());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		//Turns on antialiasing for text
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHints(rh);
		
		for (DBElement el : getElements()) el.draw(g2d);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//System.out.println("Options default paint called");
		super.paintComponent (g);
		doDrawing(g);
	}
	
	public void setAcceptingInput(boolean flag) {
		acceptingInput = flag;
	}
}


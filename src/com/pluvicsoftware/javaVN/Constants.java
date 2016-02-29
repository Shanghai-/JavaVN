package com.pluvicsoftware.javaVN;

import java.awt.Color;
import java.awt.Font;

/**
 * Holds customizable constants which tell the app how to look and behave
 * 
 * @author Brendan
 */
public class Constants {
	// Debug mode switch. Turns on various status printouts.
	public static final boolean DEBUG = false;
	
	// These arrays specify the possible resolutions
	public static final int[] WIDTHS = {1024, 1280, 1600, 1920};
	public static final int[] HEIGHTS = {576, 720, 900, 1080};
	
	// Constants for panel layering
	public static final String SPLASH_PANEL = "Splash Screen";
	public static final String MENU_PANEL = "Main Menu";
	public static final String OPTIONS_PANEL = "Options";
	public static final String SAVE_PANEL = "Save Menu";
	
	// Specifies how buttons should look
	/* TODO: DEFAULT_BUTTON_COLOR gets used a lot as just a general accent
	 * color, not specifically on buttons - consider changing name to 
	 * ACCENT_COLOR. */
	public static final Color DEFAULT_BUTTON_COLOR = new Color(100, 156, 239);
	public static final Color DEFAULT_BUTTON_LABEL_COLOR = Color.white;
	public static final Font DEFAULT_BUTTON_LABEL_FONT = 
			new Font("Gill Sans MT", Font.PLAIN, 38);
	
	public static final Color DEFAULT_LABEL_COLOR = Color.white;
	public static final Font DEFAULT_LABEL_FONT = 
			new Font("Segoe UI", Font.PLAIN, 28);
	// Possible fonts: Gill Sans MT, Arial, Century Gothic, Helvetica World, Segoe UI

	/* Delay, in milliseconds, between each letter appearing on the screen
	 * Take 1000 and divide by the number of letters you wish to appear per second
	 * e.g. 1000ms / 5 letters = 200ms delay */
	public static final int DEFAULT_TYPE_DELAY = 50;
	public static final String DEFAULT_TYPE_SOUND = "textBlip.wav";
	
	/* The default number of sound channels. Any DBSoundManager initialized
	 * without a channel count argument will have this number of SFX channels
	 * available. */
	public static final int DEFAULT_NUM_CHANNELS = 5;
	
	// Paths to various files
	public static final String SCRIPT_PATH = "src/data/script.xml";
	public static final String SAVE_PATH = "src/data/saves/";
	public static final String BG_PATH = "src/media/backgrounds/";
	public static final String MUSIC_PATH = "src/media/music/";
	public static final String SOUNDS_PATH = "src/media/sounds/";
	public static final String SPRITES_PATH = "src/media/sprites/";
	public static final String ICON_PATH = "src/media/icons/";
}
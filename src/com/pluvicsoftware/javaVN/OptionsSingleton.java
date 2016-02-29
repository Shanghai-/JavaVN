package com.pluvicsoftware.javaVN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Singleton. Contains the options and settings of the player.
 * 
 * @author Brendan
 */
public class OptionsSingleton {
   private static OptionsSingleton instance = null;
   protected OptionsSingleton() {
      // Exists only to defeat instantiation.
   }
   public static OptionsSingleton getInstance() {
      if(instance == null) {
         instance = new OptionsSingleton();
      }
      return instance;
   }
   
   /**
    * Reads in the options from the saved options file, or creates and saves a
    * default options file if no options file is found.
    */
   public void loadOptions() {
	   try {
			BufferedReader rd = new BufferedReader(new FileReader("src/data/Options.txt"));
			if (Constants.DEBUG) System.out.println("Options file found.");
			resolution = Integer.parseInt(rd.readLine());
			musicVolume = Integer.parseInt(rd.readLine());
			SFXVolume = Integer.parseInt(rd.readLine());
			fullScreen = Boolean.parseBoolean(rd.readLine());
			screenEffects = Boolean.parseBoolean(rd.readLine());
			rd.close();
			if (Constants.DEBUG) System.out.println("Options file imported.");
		} catch (IOException ex) {
			if (Constants.DEBUG) System.out.println("No options file found. Writing...");
			//If no options.txt is found, writes a default one
			try {
				PrintWriter wr = new PrintWriter(new FileWriter("src/data/Options.txt"));
				wr.println(1); //Writes default resolution choice, 1280 x 720
				wr.println(75); //Writes default music volume, 75%
				wr.println(75); //Writes default SFX volume, 50%
				wr.println(false); //Writes default fullscreen value, false
				wr.println(false); //Writes default epileptic mode value, false
				wr.close();
				if (Constants.DEBUG) System.out.println("New options file written.");
				loadOptions();
			} catch (IOException e) {
				System.out.println("Could not write default options file.");
				e.printStackTrace();
			}
		}
   }
   
   /**
    * Saves the current options values to the options file.
    */
   public void saveOptions() {
	   try {
			PrintWriter wr = new PrintWriter(new FileWriter("src/data/Options.txt"));
			wr.println(resolution);
			wr.println(musicVolume); //Writes current music volume
			wr.println(SFXVolume); //Writes current sound effect volume
			wr.println(fullScreen); //Writes current fullscreen value
			wr.println(screenEffects); //Writes current epileptic mode value
			wr.close();
			if (Constants.DEBUG) System.out.println("Options saved.");
		} catch (IOException e) {
			System.out.println("Could not save options file.");
			e.printStackTrace();
		}
   }
   
   public int getResolution() {return resolution;}
   public void setResolution(int val) {resolution = val;}
   
   public int getMusicVolume() {return musicVolume;}
   public void setMusicVolume(int val) {musicVolume = val;}
   
   public int getSFXVolume() {return SFXVolume;}
   public void setSFXVolume(int val) {SFXVolume = val;}
   
   public boolean isFullScreen() {return fullScreen;}
   public void setFullScreen(boolean val) {fullScreen = val;}
   
   public boolean getScreenEffects() {return screenEffects;}
   public void setScreenEffects(boolean val) {screenEffects = val;}
   
   private int resolution;
   private int musicVolume;
   private int SFXVolume;
   private boolean fullScreen;
   private boolean screenEffects;
}
package com.pluvicsoftware.javaVN;

/**
 * Represents a Scene of lines. Mostly exists to move all of the 
 * tedious parsing and checking from the Script Reader methods.
 * 
 * @author Brendan
 */
public class DBScene {
	/**
	 * Constructor. Builds a new Scene with the given attributes. If any of
	 * them are null, it will result in an error.
	 * 
	 * @param backgroundName - The name of the first background to display.
	 * @param musicName - The name of the background music to begin playing.
	 * @param sceneName - The name of the scene (displays in the top left corner).
	 * @param sceneTime - The time of day at which the scene takes place.
	 */
	public DBScene(String backgroundName, String musicName, String sceneName, String sceneTime) {
		if (backgroundName != null) this.backgroundName = backgroundName;
		if (musicName != null) this.musicName = musicName;
		if (sceneName != null) this.sceneName = sceneName;
		if (sceneTime != null) this.sceneTime = sceneTime;
	}
	
	public String backgroundName;
	public String musicName;
	public String sceneName;
	public String sceneTime;
}
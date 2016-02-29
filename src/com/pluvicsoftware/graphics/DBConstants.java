package com.pluvicsoftware.graphics;

public class DBConstants {
	private static int defaultWidth;
	private static int defaultHeight;
	
	private static int screenWidth;
	private static int screenHeight;
	
	private static boolean scalingEnabled;
	private static float scaleFactor = 1.0f;
	
	private static int centerX;
	private static int centerY;
	private static int leftThirdX;
	private static int rightThirdX;
	private static int leftQuarterX;
	private static int rightQuarterX;
	
	public static void setDefaultWidth(int val) {defaultWidth = val;}
	public static void setDefaultHeight(int val) {defaultHeight = val;}
	
	public static int getDefaultWidth() {return defaultWidth;}
	public static int getDefaultHeight() {return defaultHeight;}
	
	public static boolean isScalingEnabled() {return scalingEnabled;}
	public static void setScalingEnabled(boolean val) {scalingEnabled = val;}
	
	public static void calculateScaleFactor() {scaleFactor = (float) screenWidth / defaultWidth;}
	public static float getScaleFactor() {return scaleFactor;}
	
	public static int getWidth() {return screenWidth;}
	public static void setWidth(int val) {screenWidth = val;}
	
	public static int getHeight() {return screenHeight;}
	public static void setHeight(int val) {screenHeight = val;}
	
	//This function must be called AFTER all the other variables have been set
	public static void calculatePositionPoints() {
		centerX = screenWidth / 2;
		centerY = screenHeight / 2;
		leftThirdX = Math.round(screenWidth / 3);
		rightThirdX = screenWidth - leftThirdX;
		leftQuarterX = Math.round(screenWidth / 4);
		rightQuarterX = screenWidth - leftQuarterX;
	}
}
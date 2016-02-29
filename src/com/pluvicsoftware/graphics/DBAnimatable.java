package com.pluvicsoftware.graphics;

public class DBAnimatable extends DBElement {
	public DBAnimatable(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
		xTarget = x;
		yTarget = y;
		widthTarget = width;
		heightTarget = height;
	}
	
	public void updateElement() {
		if (xInterval != 0) {
			if (isWithin(xTarget, Math.abs(xInterval), x)) {
				System.out.println("X animation target: " + xTarget);
				System.out.println("X animation final: " + xpos);
				xInterval = 0;
			} else {
				x += xInterval;
				System.out.println("Internal x: " + x);
			}
		}
		if (yInterval != 0) {
			if (isWithin(yTarget, Math.abs(yInterval), y)) {
				System.out.println("Y animation target: " + yTarget);
				System.out.println("Y animation final: " + ypos);
				yInterval = 0;
			} else {
				y += yInterval;
				System.out.println("Internal y: " + y);
			}
		}
		if (widthInterval != 0) {
			if (isWithin(widthTarget, Math.abs(widthInterval), width)) {
				widthInterval = 0; 
			} else {
				w += widthInterval;
			}
		}
		if (heightInterval != 0) {
			if (isWithin(heightTarget, Math.abs(heightInterval), height)) {
				heightInterval = 0;
			} else {
				h += heightInterval;
			}
		}
		update();
		roundVals();
	}
	
	protected void update() {
		//Override this to add more animation parameters
	}
	
	public void resetAnimations() {
		xInterval = 0;
		xTarget = 0;
		yInterval = 0;
		yTarget = 0;
		widthInterval = 0;
		widthTarget = 0;
		heightInterval = 0;
		heightTarget = 0;
	}
	
	public void animateX(float interval, int target) {
		//System.out.println("X animation initial position: " + x);
		//System.out.println("X animation target: " + target);
		//System.out.println("X animation interval: " + interval);
		xInterval = interval;
		xTarget = target;
	}
	
	public void animateY(float interval, int target) {
		//System.out.println("Y animation initial position: " + y);
		//System.out.println("Y animation target: " + target);
		//System.out.println("Y animation interval: " + interval);
		yInterval = interval;
		yTarget = target;
	}
	
	//TODO: Scales from corner rather than center. Any way to fix?
	public void animateWidth(float interval, int target) {
		widthInterval = interval;
		widthTarget = target;
	}
	
	public void animateHeight(float interval, int target) {
		heightInterval = interval;
		heightTarget = target;
	}
	
	protected boolean isWithin(int target, float tolerance, float cur) {
		return (cur > target - tolerance && cur < target + tolerance);
	}
	
	protected boolean isWithin(float target, float tolerance, float cur) {
		return (cur > target - tolerance && cur < target + tolerance);
	}
	
	/* protected boolean isDoneAnimatingI() {
		return(xInterval == 0 && yInterval == 0 && widthInterval == 0 
				&& heightInterval == 0 && isDoneAnimating());
	}
	
	//Override this to add more parameters
	public boolean isDoneAnimating() {
		return true;
	} */
	
	protected void roundVals() {
		xpos = Math.round(x);
		ypos = Math.round(y);
		width = Math.round(w);
		height = Math.round(h);
		roundVars();
	}
	
	public void roundVars() {
		//Override this to add more animation parameters
	}
	
	protected float x;
	protected float xInterval;
	protected int xTarget;
	protected float y;
	protected float yInterval;
	protected int yTarget;
	protected float w;
	protected float widthInterval;
	protected int widthTarget;
	protected float h;
	protected float heightInterval;
	protected int heightTarget;
}
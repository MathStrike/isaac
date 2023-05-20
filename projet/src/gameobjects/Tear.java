package gameobjects;

import libraries.StdDraw;
import libraries.Vector2;

public class Tear {
	private Vector2 tearPosition;
	public Vector2 direction;
	private double tearSpeed;
	private String imagePath;
	private Vector2 size;
	private double distance;

	public Tear(Vector2 tearPosition, double tearSpeed, Vector2 size, String imagePath) {
		this.tearPosition = tearPosition;
		this.direction = new Vector2();
		this.tearSpeed = tearSpeed;
		this.imagePath = imagePath;
		this.size = size;
		this.distance = 0;
	}
	
	public void drawGameObject() {
		StdDraw.picture(getPosition().getX(), getPosition().getY(), imagePath, getSize().getX(), getSize().getY(), 0);
	}
	
	/* GETTERS AND SETTERS */
	
	public Vector2 getPosition() {
		return tearPosition;
	}
	
	public Vector2 getSize() {
		return size;
	}
	
	public Vector2 getDirection() {
		return direction;
	}

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}
	
	public void setPosition(Vector2 position) {
		this.tearPosition = position;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getTearSpeed() {
		return tearSpeed;
	}

	public void setTearSpeed(double tearSpeed) {
		this.tearSpeed = tearSpeed;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}

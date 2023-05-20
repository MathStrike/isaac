package gameobjects;

import java.util.ArrayList;

import libraries.StdDraw;
import libraries.Vector2;

public class Monster {
	private Vector2 position;
	private Vector2 size;
	private String imagePath;
	private double speed;
	private Vector2 direction;
    private int pv;
	private static int damage;
	private int id;
	public ArrayList<Tear> tears;
	public ArrayList<Tear> outScopeTears;
	
	public Monster(Vector2 position, Vector2 size, double speed, String imagePath, int newPV, int newDamage, int newId) {
		this.position = position;
		this.size = size;
		this.speed = speed;
		this.imagePath = imagePath;
		this.direction = new Vector2();
        this.pv = newPV;
		damage = newDamage;
		this.id = newId;
		this.tears = new ArrayList<Tear>();
		this.outScopeTears = new ArrayList<Tear>();
	}

	public void updateGameObject() {
		move();
	}

	public void move() {
		Vector2 normalizedDirection = getNormalizedDirection();
		Vector2 positionAfterMoving = getPosition().addVector(normalizedDirection);
		if ((positionAfterMoving.getX() >= 0.13 && positionAfterMoving.getX() <= 0.87) && (positionAfterMoving.getY() >= 0.2 && positionAfterMoving.getY() <= 0.85)) {
			setPosition(positionAfterMoving);
		}
		setDirection(new Vector2());
    };
	
	public void drawGameObject() {
		StdDraw.picture(getPosition().getX(), getPosition().getY(), getImagePath(), getSize().getX(), getSize().getY(), 0);
	}

	/*
	 * Moving from key inputs. Direction vector is later normalised.
	 */
	public Vector2 getNormalizedDirection() {
		Vector2 normalizedVector = new Vector2(direction);
		normalizedVector.euclidianNormalize(speed);
		return normalizedVector;
	}

	/*
	 * Getters and Setters
	 */
	public Vector2 getPosition()
	{
		return position;
	}

	public void setPosition(Vector2 position)
	{
		this.position = position;
	}

	public Vector2 getSize()
	{
		return size;
	}

	public void setSize(Vector2 size)
	{
		this.size = size;
	}

	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
	}

	public double getSpeed()
	{
		return speed;
	}

	public void setSpeed(double speed)
	{
		this.speed = speed;
	}

	public Vector2 getDirection()
	{
		return direction;
	}

	public void setDirection(Vector2 direction)
	{
		this.direction = direction;
	}

    public int getPV() {
		return pv;
	}

	public void setPV(int newPV) {
		this.pv = newPV;
	}

	public int getId() {
		return id;
	}

	public ArrayList getTears() {
		return tears;
	}
}
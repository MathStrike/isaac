package gameobjects;

import java.util.ArrayList;
import java.util.Collection;
import gameWorld.Boss;
import gameWorld.FightRoom;
import gameWorld.GameWorld;
import gameWorld.Room;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.ImagePaths;
import resources.TearInfos;

public class Hero {
	private static Vector2 position;
	private Vector2 size;
	private String imagePath;
	private double speed;
	private Vector2 direction;
	private int maxPV;
	private static double pv;
	private int tearsDamage;
	private double tearScope;
	private double tearSpeed;
	public Collection<Tear> tears;
	public Collection<Tear> outScopeTears;
	private int lastShoot;
	private int lastMonsterHit;
	private int lastSpadeHit;
	private boolean invincible;
	private int coins;
	private ArrayList<String> inventory;
	private Room currentRoom;

	public Hero(Vector2 initPosition, Vector2 size, double speed, String imagePath, double initPv, int tearsDamage, double tearScope, double tearSpeed) {
		position = initPosition;
		this.size = size;
		this.speed = speed;
		this.imagePath = imagePath;
		this.direction = new Vector2();
		this.maxPV = 6;
		pv = initPv;
		this.tearsDamage = tearsDamage;
		this.tearScope = tearScope;
		this.tearSpeed = tearSpeed;
		this.tears = new ArrayList<Tear>();
		this.outScopeTears = new ArrayList<Tear>();
		this.lastShoot = 0;
		this.lastMonsterHit = 0;
		this.lastSpadeHit = 0;
		this.invincible = false;
		this.coins = 0;
		this.inventory = new ArrayList<String>();
		this.currentRoom = null;
	}

	public void updateGameObject() {
		move();
		tears();
	}

	private void move() {
		Vector2 normalizedDirection = getNormalizedDirection();
		Vector2 positionAfterMoving = getPosition().addVector(normalizedDirection);
		if ((positionAfterMoving.getX() >= 0.13 && positionAfterMoving.getX() <= 0.87) && (positionAfterMoving.getY() >= 0.14 && positionAfterMoving.getY() <= 0.9)) {
			setPosition(positionAfterMoving);
		}
		direction = new Vector2();
		
	}

	private void tears() {
		for (Tear tear : tears) {
			if (tear.getDistance() > getTearScope() || (tear.getPosition().getX() <= 0.13 || tear.getPosition().getX() >= 0.87) || (tear.getPosition().getY() <= 0.14 || tear.getPosition().getY() >= 0.85)) { // si larme est hors de la portée ou des limites de la salle alors on la supprime
				outScopeTears.add(tear);
			} else {
				Vector2 normalizedDirection = getNormalizedDirection(tear.getDirection());
				Vector2 positionAfterMoving = tear.getPosition().addVector(normalizedDirection);
				tear.setPosition(positionAfterMoving);
			}

			if (currentRoom instanceof FightRoom || currentRoom instanceof Boss) {
				for (Monster monster : currentRoom.getMonsters()) {
					if (Physics.rectangleCollision(monster.getPosition(), monster.getSize(), tear.getPosition(), tear.getSize())) { // vérifie la collision des larmes avec les monstres
						outScopeTears.add(tear);
						monster.setPV(monster.getPV()-getTearDamage());
						System.out.println(monster.getPV());
					}
				}
			}
		}
		tears.removeAll(outScopeTears);
	}

	public void drawGameObject() {
		StdDraw.picture(getPosition().getX(), getPosition().getY(), getImagePath(), getSize().getX(), getSize().getY(), 0);
	}

	/*
	 * Moving from key inputs. Direction vector is later normalised.
	 */
	public void shootUpNext() {
		if (GameWorld.getCdj() - lastShoot >= 20) {
			Vector2 position = new Vector2(getPosition().getX(), getPosition().getY());
			Tear tear = new Tear(position, tearSpeed, TearInfos.TEAR_SIZE, ImagePaths.TEAR);
			if (getInventory().contains(ImagePaths.BLOOD_OF_THE_MARTYR)) {
				tear.setImagePath(ImagePaths.BLOOD_TEAR);
			}
			tear.getDirection().addY(1);
			tears.add(tear);
			lastShoot = GameWorld.getCdj();
		}
	}
	
	public void shootDownNext() {
		if (GameWorld.getCdj() - lastShoot >= 20) {
			Vector2 position = new Vector2(getPosition().getX(), getPosition().getY());
			Tear tear = new Tear(position, tearSpeed, TearInfos.TEAR_SIZE, ImagePaths.TEAR);
			if (getInventory().contains(ImagePaths.BLOOD_OF_THE_MARTYR)) {
				tear.setImagePath(ImagePaths.BLOOD_TEAR);
			}
			tear.getDirection().addY(-1);
			tears.add(tear);
			lastShoot = GameWorld.getCdj();
		}
	}
	
	public void shootLeftNext() {
		if (GameWorld.getCdj() - lastShoot >= 20) {
			Vector2 position = new Vector2(getPosition().getX(), getPosition().getY());
			Tear tear = new Tear(position, tearSpeed, TearInfos.TEAR_SIZE, ImagePaths.TEAR);
			if (getInventory().contains(ImagePaths.BLOOD_OF_THE_MARTYR)) {
				tear.setImagePath(ImagePaths.BLOOD_TEAR);
			}
			tear.getDirection().addX(-1);
			tears.add(tear);
			lastShoot = GameWorld.getCdj();
		}
	}
	
	public void shootRightNext() {
		if (GameWorld.getCdj() - lastShoot >= 20) {
			Vector2 position = new Vector2(getPosition().getX(), getPosition().getY());
			Tear tear = new Tear(position, tearSpeed, TearInfos.TEAR_SIZE, ImagePaths.TEAR);
			if (getInventory().contains(ImagePaths.BLOOD_OF_THE_MARTYR)) {
				tear.setImagePath(ImagePaths.BLOOD_TEAR);
			}
			tear.getDirection().addX(1);
			tears.add(tear);
			lastShoot = GameWorld.getCdj();
		}
	}
	
	public void goUpNext() {
		getDirection().addY(1);
	}

	public void goDownNext() {
		getDirection().addY(-1);
	}

	public void goLeftNext() {
		getDirection().addX(-1);
	}

	public void goRightNext() {
		getDirection().addX(1);
	}

	public Vector2 getNormalizedDirection() {
		Vector2 normalizedVector = new Vector2(direction);
		normalizedVector.euclidianNormalize(speed);
		return normalizedVector;
	}

	public Vector2 getNormalizedDirection(Vector2 direction) {
		Vector2 normalizedVector = new Vector2(direction);
		normalizedVector.euclidianNormalize(speed);
		return normalizedVector;
	}


	/*
	 * Getters and Setters
	 */	
	public Vector2 getPosition() {
		return position;
	}

	public static void setPosition(Vector2 newPosition) {
		position = newPosition;
	}

	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Vector2 getDirection() {
		return direction;
	}

	public void setDirection(Vector2 direction) {
		this.direction = direction;
	}

	public double getPV() {
		return pv;
	}

	public void setPV(double newPv) {
		pv = newPv;
	}
	
	public double getTearScope() {
		return tearScope;
	}

	public void setTearScope(double tearScope) {
		this.tearScope = tearScope;
	}

	public int getTearDamage() {
		return tearsDamage;
	}

	public void setTearDamage(int tearsDamage) {
		this.tearsDamage = tearsDamage;
	}

	public boolean getInvincible() {
		return invincible;
	}

	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getCoins() {
		return coins;
	}

	public int getLastSpadeHit() {
		return lastSpadeHit;
	}

	public void setLastSpadeHit(int lastSpadeHit) {
		this.lastSpadeHit = lastSpadeHit;
	}

	public int getLastMonsterHit() {
		return lastMonsterHit;
	}

	public void setLastMonsterHit(int lastMonsterHit) {
		this.lastMonsterHit = lastMonsterHit;
	}

	public ArrayList<String> getInventory() {
		return inventory;
	}

	public int getMaxPV() {
		return maxPV;
	}

	public void setMaxPV(int maxPV) {
		this.maxPV = maxPV;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}
}

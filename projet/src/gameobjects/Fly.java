package gameobjects;

import gameWorld.GameWorld;
import libraries.Vector2;
import resources.ImagePaths;
import resources.TearInfos;

public class Fly extends Monster {
	private int shootDamage;
	private double shootScope;
	private double shootSpeed;
	private Hero target;
	private int lastShoot;

	public Fly(Vector2 position, Vector2 size, double speed, String imagePath, int pv, int damage, int shootDamage, double shootScope, double shootSpeed, int id, Hero target) {
		super(position,size,speed,imagePath,pv,damage,id);
		this.shootDamage = shootDamage;
		this.shootScope = shootScope;
		this.shootSpeed = shootSpeed;
		this.target = target;
		this.lastShoot = 0;
        // speed = 0.00125
	}

	public void updateGameObject() {
		super.updateGameObject();
		if (GameWorld.getCdj() - lastShoot >= 40) {
			shoot();
		}
		tears();
	}

	@Override
	public void move() {		
		//si fly a gauche de hero
		if(getPosition().getX() < target.getPosition().getX()) {
			getDirection().addX(1);
		}
		//si fly a droite de hero
		if(getPosition().getX() > target.getPosition().getX()) {
			getDirection().addX(-1);
		}
		//si fly au dessus de hero
		if(getPosition().getY() > target.getPosition().getY()) {
		    getDirection().addY(-1);
		}
		//si fly en dessous de hero
		if(getPosition().getY() < target.getPosition().getY()) {
			getDirection().addY(1);
		}

		super.move();
    }

	private void shoot() {
		Vector2 position = new Vector2(getPosition().getX(), getPosition().getY());
		Tear tear = new Tear(position, shootSpeed, TearInfos.TEAR_SIZE, ImagePaths.TEAR);
		//si fly a gauche de hero
		if(getPosition().getX() < target.getPosition().getX()) {
			tear.getDirection().addX(1);
		}
		//si fly a droite de hero
		if(getPosition().getX() > target.getPosition().getX()) {
			tear.getDirection().addX(-1);
		}
		//si fly au dessus de hero
		if(getPosition().getY() > target.getPosition().getY()) {
		    tear.getDirection().addY(-1);
		}
		//si fly en dessous de hero
		if(getPosition().getY() < target.getPosition().getY()) {
			tear.getDirection().addY(1);
		}
		tears.add(tear);
		lastShoot = GameWorld.getCdj();
	}

	private void tears() {
		for (Tear tear : tears) {
			if (tear.getDistance() > this.shootScope || (tear.getPosition().getX() <= 0.13 || tear.getPosition().getX() >= 0.87) || (tear.getPosition().getY() <= 0.14 || tear.getPosition().getY() >= 0.85)) { // si larme est hors de la portée ou des limites de la salle alors on la supprime
				outScopeTears.add(tear);
			} else {
				Vector2 normalizedDirection = getNormalizedDirection(tear.getDirection());
				Vector2 positionAfterMoving = tear.getPosition().addVector(normalizedDirection);
				tear.setPosition(positionAfterMoving);
			}
		}
		tears.removeAll(outScopeTears);
	}

	public Vector2 getNormalizedDirection(Vector2 direction) {
		Vector2 normalizedVector = direction;
		normalizedVector.euclidianNormalize(shootSpeed);
		return normalizedVector;
	}
}
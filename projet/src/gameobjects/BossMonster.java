package gameobjects;

import gameWorld.GameWorld;
import libraries.Vector2;
import resources.ImagePaths;
import resources.TearInfos;

public class BossMonster extends Monster {
    private int shootDamage;
	private double shootScope;
	private double shootSpeed;
	private Hero target;
	private int lastShoot;
    private String mode;
    private int lastMove;
    private int lastMode;

    public BossMonster(Vector2 position, Vector2 size, double speed, String imagePath, int pv, int damage, int shootDamage, double shootScope, double shootSpeed, int id, Hero target) {
        super(position, size, speed, imagePath, pv, damage, id);
        this.shootDamage = shootDamage;
		this.shootScope = shootScope;
		this.shootSpeed = shootSpeed;
		this.target = target;
		this.lastShoot = 0;
        this.lastMove = 0;
        this.lastMode = 0;

        int randomNumber = (int)(Math.random() * 2);
        if (randomNumber == 0) {
            this.mode = "fly";
        } else if (randomNumber == 1) {
            this.mode = "spider";
        }
    }

    public void updateGameObject() {
		super.updateGameObject();
		if (GameWorld.getCdj() - lastShoot >= 40 && this.mode == "fly") {
			shoot();
		}
		tears();
        if (GameWorld.getCdj() - lastMode >= 100) {
            modeChanger();
        }
	}

    public void modeChanger() {
        int randomNumber = (int)(Math.random() * 10);
        if (randomNumber == 5) {
            if (this.mode == "fly") {
                this.mode = "spider";
            } else {
                this.mode = "fly";
            }
        }
    }

    private void flyMove() {
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
    }

    private void spiderMove() {
        if (GameWorld.getCdj() - lastMove >= 40){ // peu avancer tous les 25 cdj (cycles de jeu)
            int randomDirection = (int) (Math.random()*8); // choix de la direction aléatoirement
        
            if (randomDirection == 0) { // up
                getDirection().addY(1);
            }
            if (randomDirection == 1) { // down
                getDirection().addY(-1);
            }
            if (randomDirection == 2) { // right
                getDirection().addX(1);
            }
            if (randomDirection == 3) { // left
                getDirection().addX(-1);
            }
            if (randomDirection == 4) { // up-right
                getDirection().addX(1);
                getDirection().addY(1);
            }
            if (randomDirection == 5) { // down-right
                getDirection().addX(1);
                getDirection().addY(-1);
            }
            if (randomDirection == 6) { // down-left
                getDirection().addX(-1);
                getDirection().addY(-1);
            }
            if (randomDirection == 7) { // up-left
                getDirection().addX(-1);
                getDirection().addY(1);
            }

            setLastMove(GameWorld.getCdj());
        }
    }

    @Override
    public void move() {
        if (this.mode == "fly") {
            flyMove();
        } else if (this.mode == "spider") {
            spiderMove();
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

    public void setLastMove(int lastMove) {
        this.lastMove = lastMove;
    }

    public String getMode() {
        return mode;
    }
}

package gameobjects;

import gameWorld.GameWorld;
import libraries.Vector2;

public class Spider extends Monster{
	private int lastMove;
	
	public Spider(Vector2 position, Vector2 size, double speed, String imagePath, int pv, int damage, int id) {
		super(position,size,speed,imagePath,pv,damage,id);
        this.lastMove = 0;
		// speed=0.02
	}
	
	@Override
	public void move() {//lastMove >=40
        if (GameWorld.getCdj() - lastMove >= 20){ // peu avancer tous les 25 cdj (cycles de jeu)
            int randomDirection = (int) (Math.random()*8); // choix de la direction aléatoirement
        
            if (randomDirection == 0) { // up
                getDirection().addY(2);
            }  
            if (randomDirection == 1) { // down
                getDirection().addY(-2);
            }
            if (randomDirection == 2) { // right
                getDirection().addX(2);
            }
            if (randomDirection == 3) { // left
                getDirection().addX(-2);
            }
            if (randomDirection == 4) { // up-right
                getDirection().addX(2);
                getDirection().addY(2);
            }
            if (randomDirection == 5) { // down-right
                getDirection().addX(2);
                getDirection().addY(-2);
            }
            if (randomDirection == 6) { // down-left
                getDirection().addX(-2);
                getDirection().addY(-2);
            }
            if (randomDirection == 7) { // up-left
                getDirection().addX(-2);
                getDirection().addY(2);
            }

            setLastMove(GameWorld.getCdj());

            super.move();
        }	
	}

    /* GETTERS AND SETTERS */
	public int getLastMove() {
		return lastMove;
	}

	public void setLastMove(int newLastMove) {
		lastMove = newLastMove;
	}
}
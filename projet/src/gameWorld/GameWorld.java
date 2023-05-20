package gameWorld;

import java.util.ArrayList;
import gameobjects.BossMonster;
import gameobjects.Fly;
import gameobjects.Hero;
import gameobjects.Monster;
import gameobjects.Spider;
import libraries.StdDraw;
import libraries.Vector2;
import resources.Controls;
import resources.DisplaySettings;
import resources.ImagePaths;
import resources.MonsterInfos;
import resources.RoomInfos;

public class GameWorld
{
	private static Room currentRoom;
	private static ArrayList<Room> rooms;
	private Hero hero;
	private static int cdj;

	// A world needs a hero
	public GameWorld(Hero hero) {
		this.hero = hero;
		
		rooms = new ArrayList<Room>();

		// salle de spawn
		int[] linksRoom1 = {0, 2, 0, 0}; // donne la salle a laquelle elle est lié (partant du haut dans sens horaire)
		rooms.add(new Spawn(hero, linksRoom1, 1));

		// première salle des monstres
		ArrayList<Monster> monsters1 = new ArrayList<Monster>();
		monsters1.add(0, new Spider(new Vector2(0.7, 0.5), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 2));
		monsters1.add(1, new Spider(new Vector2(0.5, 0.7), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 3));
		monsters1.add(2, new Spider(new Vector2(0.5, 0.3), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 3));
		int[] linksRoom2 = {0, 0, 3, 1};
		rooms.add(new FightRoom(hero, linksRoom2, 2, monsters1, 3, 3));

		// deuxième salle des monstres
		ArrayList<Monster> monsters2 = new ArrayList<Monster>();
		monsters2.add(0, new Fly(new Vector2(0.5, 0.3), MonsterInfos.FLY_SIZE, 0.00125, ImagePaths.FLY, 3, 1, 1, 0.2, 0.005, 2, hero));
		monsters2.add(1, new Spider(new Vector2(0.3, 0.5), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 3));
		monsters2.add(2, new Spider(new Vector2(0.7, 0.5), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 3));
		int[] linksRoom3 = {2, 0, 4, 0};
		rooms.add(new FightRoom(hero, linksRoom3, 3, monsters2, 2, 2));

		// salle magasin
		int[] linksRoom4 = {3, 0, 0, 5};
		rooms.add(new Shop(hero, linksRoom4, 4));

		// troisième salle des monstres
		ArrayList<Monster> monsters3 = new ArrayList<Monster>();
		monsters3.add(0, new Fly(new Vector2(0.7, 0.7), MonsterInfos.FLY_SIZE, 0.00125, ImagePaths.FLY, 3, 1, 1, 0.2, 0.005, 2, hero));
		monsters3.add(1, new Fly(new Vector2(0.3, 0.7), MonsterInfos.FLY_SIZE, 0.00125, ImagePaths.FLY, 3, 1, 1, 0.2, 0.005, 2, hero));
		monsters3.add(2, new Fly(new Vector2(0.3, 0.3), MonsterInfos.FLY_SIZE, 0.00125, ImagePaths.FLY, 3, 1, 1, 0.2, 0.005, 2, hero));
		monsters3.add(3, new Spider(new Vector2(0.5, 0.3), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 3));
		monsters3.add(4, new Spider(new Vector2(0.3, 0.3), MonsterInfos.SPIDER_SIZE, 0.02, ImagePaths.SPIDER, 5, 1, 3));
		int[] linksRoom5 = {0, 4, 6, 0};
		rooms.add(new FightRoom(hero, linksRoom5, 5, monsters3, 2, 2));

		// salle boss final
		BossMonster boss = new BossMonster(new Vector2(0.5, 0.5), MonsterInfos.BOSS_SIZE, 0.005, ImagePaths.BOSS, 12, 2, 1, 0.2, 0.003, 1, hero);
		int[] linksRoom6 = {5, 0, 0, 0};
		rooms.add(new Boss(hero, linksRoom6, 6, boss));

		currentRoom = rooms.get(0); 
		hero.setCurrentRoom(rooms.get(0));

		cdj = 0;
	}

	public void processUserInput() {
		processKeysForMovement();
		processKeysForShoot();
		specialCommands();
	}

	public boolean gameOver() {
		if (hero.getPV() <= 0) {
			System.out.println("[INFO JEU] : Game Over");
			return true;
		}
		return false;
	}

	public boolean gameWin() {
		if (currentRoom instanceof Boss && currentRoom.getMonsters().size() == 0) {
			System.out.println("[INFO JEU] : C'est gagné");
			return true;
		}
		return false;
	}

	public void updateGameObjects() {
		currentRoom.updateRoom();
		cdj++;
	}

	public void drawGameObjects() {
		if (gameOver()) {
			StdDraw.picture(0.5, 0.5, ImagePaths.LOSE_SCREEN, 1, 1);
		} else if (gameWin()) {
			StdDraw.picture(0.5, 0.5, ImagePaths.WIN_SCREEN, 1, 1);
		} else {
			currentRoom.drawRoom();
			if (!this.hero.getInvincible()) {
				drawPV();
			}
			drawCoins();
			drawInventory();
		}
	}

	public static void changeRoom(int roomId, Hero hero) {
		for (Room room : rooms) {
			if (room.getRoomId() == roomId) {
				currentRoom = room;
				hero.setCurrentRoom(rooms.get(room.getRoomId()-1));
				System.out.println("[INFO JEU] : Entrée dans le donjon " + roomId);
			}
		}
	}

	private void drawPV() {
		if (hero.getMaxPV() == 6) {
			if (hero.getPV() == 6) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 5) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 4) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 3) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 2) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 1) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 0) {
				StdDraw.picture(0.1, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			}
		} else if (hero.getMaxPV() == 8) {
			if (hero.getPV() == 8) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 7) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 6) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 5) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 4) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 3) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 2) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 1) {
				StdDraw.picture(0.1, 0.9, ImagePaths.HALF_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			} else if (hero.getPV() == 0) {
				StdDraw.picture(0.1, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.15, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.2, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
				StdDraw.picture(0.25, 0.9, ImagePaths.EMPTY_HEART_HUD, DisplaySettings.HEART_SIZE.getX(), DisplaySettings.HEART_SIZE.getY(), 0);
			}
		}
	}

	private void drawCoins() {
		StdDraw.picture(0.1, 0.85, ImagePaths.COIN, RoomInfos.CONSUMABLE_SIZE.getX(), RoomInfos.CONSUMABLE_SIZE.getY(), 90);
		StdDraw.setPenColor(StdDraw.WHITE);
		StdDraw.text(0.15, 0.85, Integer.toString(hero.getCoins()));
	}

	private void drawInventory() {
		ArrayList<Vector2> coordinates = new ArrayList<Vector2>();
		coordinates.add(new Vector2(0.1, 0.8));
		coordinates.add(new Vector2(0.15, 0.8));
		for (int i = 0; i < hero.getInventory().size(); i++) {
			StdDraw.picture(coordinates.get(i).getX(), coordinates.get(i).getY(), hero.getInventory().get(i));
		}
	}

	/*
	 * Keys processing
	 */

	private void processKeysForMovement() {
		if (StdDraw.isKeyPressed(Controls.goUp)) {
			hero.goUpNext();
		}
		if (StdDraw.isKeyPressed(Controls.goDown)) {
			hero.goDownNext();
		}
		if (StdDraw.isKeyPressed(Controls.goRight)) {
			hero.goRightNext();
		}
		if (StdDraw.isKeyPressed(Controls.goLeft)) {
			hero.goLeftNext();
		}
	}
	
	private void processKeysForShoot() {
		if (StdDraw.isKeyPressed(Controls.shootUp)) {
			hero.shootUpNext();
		}
		if (StdDraw.isKeyPressed(Controls.shootDown)) {
			hero.shootDownNext();
		}
		if (StdDraw.isKeyPressed(Controls.shootRight)) {
			hero.shootRightNext();
		}
		if (StdDraw.isKeyPressed(Controls.shootLeft)) {
			hero.shootLeftNext();
		}
	}

	private void specialCommands() {
		if (StdDraw.isKeyPressed(Controls.speed)) {
			this.hero.setSpeed(this.hero.getSpeed()*1.5);
			System.out.println("[INFO JEU] : Votre vitesse a été multiplié par 1,5. Votre vitesse actuelle : " + this.hero.getSpeed() + ".");
		}
		if (StdDraw.isKeyPressed(Controls.invincible)) {
			if (this.hero.getInvincible()) {
				this.hero.setInvincible(false);
				System.out.println("[INFO JEU] : Vous n'êtes maintenant plus invincible.");
			} else {
				this.hero.setInvincible(true);
				System.out.println("[INFO JEU] : Vous êtes maintenant invincible.");
			}	
		}
		if (StdDraw.isKeyPressed(Controls.god)) {
			this.hero.setTearDamage(1000);
			System.out.println("[INFO JEU] : Vous pouvez tuer tous les monstres d'une larme.");
		}
		if (StdDraw.isKeyPressed(Controls.money)) {
			this.hero.setCoins(this.hero.getCoins()+10);
			System.out.println("[INFO JEU] : Vous avez été crédité de 10 pièces. Vous avez : " + this.hero.getCoins() + " pièces.");
		}
		if (StdDraw.isKeyPressed(Controls.killAll) && (currentRoom instanceof FightRoom || currentRoom instanceof Boss)) {
			currentRoom.getMonsters().clear();
			System.out.println("[INFO JEU] : Tous les monstres ont été tués.");
		}

	}
	
	/* GETTERS AND SETTERS */
	public static int getCdj() {
		return cdj;
	}

	public static void setCdj(int cdj) {
		GameWorld.cdj = cdj;
	}

	public static ArrayList<Room> getRooms() {
		return rooms;
	}

	public static Room getCurrentRoom() {
		return currentRoom;
	}
}

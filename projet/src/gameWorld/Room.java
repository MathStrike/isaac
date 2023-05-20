package gameWorld;

import java.util.ArrayList;
import gameobjects.Hero;
import gameobjects.Monster;
import gameobjects.Tear;
import libraries.StdDraw;
import libraries.Vector2;
import resources.HeroInfos;
import resources.ImagePaths;
import resources.RoomInfos;

public class Room {
	protected Hero hero;
	private int[] links;
	private int roomId;
	private boolean locked;

	public Room(Hero hero, int[] links, int roomId) {
		this.hero = hero;
		this.links = links;
		this.roomId = roomId;
		this.locked = true;
	}

	/*
	 * Make every entity that compose a room process one step
	 */
	public void updateRoom() {
		makeHeroPlay();
		if (!locked) {
			changeRoom();
		}
	}

	private void makeHeroPlay() {
		hero.updateGameObject();
	}

	/*
	 * Drawing
	 */
	public void drawRoom() {
		// wall and floor
		StdDraw.picture(RoomInfos.POSITION_ROOM1.getX(), RoomInfos.POSITION_ROOM1.getY(), ImagePaths.ROOM1, RoomInfos.ROOM1_SIZE.getX(), RoomInfos.ROOM1_SIZE.getY(), 0);

		// tutorial images
		if (roomId == 1) {
			StdDraw.picture(RoomInfos.POSITION_TUTO_MOVE.getX(), RoomInfos.POSITION_TUTO_MOVE.getY(), ImagePaths.TUTO_MOVE, RoomInfos.TUTO_SIZE.getX(), RoomInfos.TUTO_SIZE.getY(), 0);
			StdDraw.picture(RoomInfos.POSITION_TUTO_ATTACK.getX(), RoomInfos.POSITION_TUTO_ATTACK.getY(), ImagePaths.TUTO_ATTACK, RoomInfos.TUTO_SIZE.getX(), RoomInfos.TUTO_SIZE.getY(), 0);
			StdDraw.picture(RoomInfos.POSITION_TUTO_ITEMS.getX(), RoomInfos.POSITION_TUTO_ITEMS.getY(), ImagePaths.TUTO_ITEMS, RoomInfos.TUTO_SIZE.getX(), RoomInfos.TUTO_SIZE.getY(), 0);
		}
		
		String doorImage;
		if (locked) { // portes fermées
			doorImage = ImagePaths.CLOSED_DOOR;
		} else { // portes ouvertes
			doorImage = ImagePaths.OPENED_DOOR;
		}

		if (links[0] > 0) { // top door
			StdDraw.picture(RoomInfos.POSITION_TOP_DOOR.getX(), RoomInfos.POSITION_TOP_DOOR.getY(), doorImage, RoomInfos.DOOR_SIZE.getX(), RoomInfos.DOOR_SIZE.getY(), 0);
		}
		if (links[1] > 0) { // right door
			StdDraw.picture(RoomInfos.POSITION_RIGHT_DOOR.getX(), RoomInfos.POSITION_RIGHT_DOOR.getY(), doorImage, RoomInfos.DOOR_SIZE.getX(), RoomInfos.DOOR_SIZE.getY(), -90);
		}
		if (links[2] > 0) { // bottom door
			StdDraw.picture(RoomInfos.POSITION_BOTTOM_DOOR.getX(), RoomInfos.POSITION_BOTTOM_DOOR.getY(), doorImage, RoomInfos.DOOR_SIZE.getX(), RoomInfos.DOOR_SIZE.getY(), 180);
		}
		if (links[3] > 0) { // left door
			StdDraw.picture(RoomInfos.POSITION_LEFT_DOOR.getX(), RoomInfos.POSITION_LEFT_DOOR.getY(), doorImage, RoomInfos.DOOR_SIZE.getX(), RoomInfos.DOOR_SIZE.getY(), 90);
		}
		
		hero.drawGameObject();
		for (Tear tear : hero.tears) {
			tear.drawGameObject();
		}
	}

	private void changeRoom() {
		if (links[0] > 0 && ((hero.getPosition().getY() >= RoomInfos.POSITION_TOP_DOOR.getY()-RoomInfos.TILE_HEIGHT) && (hero.getPosition().getX() >= RoomInfos.POSITION_BOTTOM_DOOR.getX()-RoomInfos.TILE_WIDTH/2 && hero.getPosition().getX() <= RoomInfos.POSITION_BOTTOM_DOOR.getX()+RoomInfos.TILE_WIDTH/2))) {
			GameWorld.changeRoom(links[0], hero);
			Hero.setPosition(new Vector2(RoomInfos.POSITION_BOTTOM_DOOR.getX(), RoomInfos.POSITION_BOTTOM_DOOR.getY()+HeroInfos.ISAAC_SIZE.getY()*1.5));
		} else if (links[1] > 0 && ((hero.getPosition().getX() >= RoomInfos.POSITION_RIGHT_DOOR.getX()-RoomInfos.TILE_WIDTH) && (hero.getPosition().getY() >= RoomInfos.POSITION_RIGHT_DOOR.getY()-RoomInfos.TILE_HEIGHT/2 && hero.getPosition().getY() <= RoomInfos.POSITION_RIGHT_DOOR.getY()+RoomInfos.TILE_HEIGHT/2))) {
			GameWorld.changeRoom(links[1], hero);
			Hero.setPosition(new Vector2(RoomInfos.POSITION_LEFT_DOOR.getX()+HeroInfos.ISAAC_SIZE.getX()*1.5, RoomInfos.POSITION_LEFT_DOOR.getY()));
		} else if (links[2] > 0 && ((hero.getPosition().getY() <= RoomInfos.POSITION_BOTTOM_DOOR.getY()+RoomInfos.TILE_HEIGHT) && (hero.getPosition().getX() >= RoomInfos.POSITION_BOTTOM_DOOR.getX()-RoomInfos.TILE_WIDTH/2 && hero.getPosition().getX() <= RoomInfos.POSITION_BOTTOM_DOOR.getX()+RoomInfos.TILE_WIDTH/2))) {
			GameWorld.changeRoom(links[2], hero);
			Hero.setPosition(new Vector2(RoomInfos.POSITION_TOP_DOOR.getX(), RoomInfos.POSITION_TOP_DOOR.getY()-HeroInfos.ISAAC_SIZE.getY()*1.5));
		} else if (links[3] > 0 && ((hero.getPosition().getX() <= RoomInfos.POSITION_LEFT_DOOR.getX()+RoomInfos.TILE_WIDTH) && (hero.getPosition().getY() >= RoomInfos.POSITION_LEFT_DOOR.getY()-RoomInfos.TILE_HEIGHT/2 && hero.getPosition().getY() <= RoomInfos.POSITION_LEFT_DOOR.getY()+RoomInfos.TILE_HEIGHT/2))) {
			GameWorld.changeRoom(links[3], hero);
			Hero.setPosition(new Vector2(RoomInfos.POSITION_RIGHT_DOOR.getX()-HeroInfos.ISAAC_SIZE.getX()*1.5, RoomInfos.POSITION_RIGHT_DOOR.getY()));
		}
	}

	/**
	 * Convert a tile index to a 0-1 position.
	 * 
	 * @param indexX
	 * @param indexY
	 * @return
	 */
	private static Vector2 positionFromTileIndex(int indexX, int indexY) {
		return new Vector2(indexX * RoomInfos.TILE_WIDTH + RoomInfos.HALF_TILE_SIZE.getX(), indexY * RoomInfos.TILE_HEIGHT + RoomInfos.HALF_TILE_SIZE.getY());
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public ArrayList<Monster> getMonsters() {
		return null;
	}

	public ArrayList<Vector2> getRocks() {
		return null;
	}

    public static Vector2[] getSpades() {
        return null;
    }
}

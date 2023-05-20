package resources;

import libraries.Vector2;

public class RoomInfos
{
	public static final int NB_TILES = 9;
	public static final double TILE_WIDTH = 1.0 / NB_TILES;
	public static final double TILE_HEIGHT = 1.0 / NB_TILES;
	public static final Vector2 TILE_SIZE = new Vector2(TILE_WIDTH, TILE_HEIGHT);
	public static final Vector2 HALF_TILE_SIZE = new Vector2(TILE_WIDTH, TILE_HEIGHT).scalarMultiplication(0.5);
	
	public static final Vector2 POSITION_START = new Vector2(0.5, 0.35);
	public static final Vector2 POSITION_TOP_DOOR = new Vector2(0.5, 0.93);
	public static final Vector2 POSITION_BOTTOM_DOOR = new Vector2(0.5, 0.07);
	public static final Vector2 POSITION_LEFT_DOOR = new Vector2(0.07, 0.5);
	public static final Vector2 POSITION_RIGHT_DOOR = new Vector2(0.93, 0.5);
	public static final Vector2 POSITION_ROOM1 = new Vector2(0.5, 0.5);
	public static final Vector2 POSITION_TUTO_ATTACK = new Vector2(0.5, 0.5);
	public static final Vector2 POSITION_TUTO_ITEMS = new Vector2(0.65, 0.5);
	public static final Vector2 POSITION_TUTO_MOVE = new Vector2(0.35, 0.5);

	public static final Vector2 DOOR_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(0.75);
	public static final Vector2 ROOM1_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(9);
	public static final Vector2 TUTO_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(1.5);
	public static final Vector2 ROCK_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(1);
	public static final Vector2 SPADE_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(0.7);

	public static final Vector2 CONSUMABLE_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(0.3);
	public static final Vector2 PASSIVE_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(0.5);
}

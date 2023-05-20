package resources;

import libraries.Vector2;

public class DisplaySettings
{
	public static final int PIXEL_PER_TILE = 65;
	public static final int FRAME_PER_SECOND = 40;
	public static final int MILLISECONDS_PER_FRAME_TO_MAINTAIN_FPS = 1000 / DisplaySettings.FRAME_PER_SECOND;

	public static final Vector2 HEART_SIZE = RoomInfos.TILE_SIZE.scalarMultiplication(0.4);
}

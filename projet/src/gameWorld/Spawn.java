package gameWorld;

import gameobjects.Hero;

public class Spawn extends Room {
    public Spawn(Hero hero, int[] links, int roomId) {
        super(hero, links, roomId);
        super.setLocked(false);
    }
}

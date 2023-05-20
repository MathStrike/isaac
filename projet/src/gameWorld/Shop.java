package gameWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gameobjects.Hero;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.HeroInfos;
import resources.ImagePaths;
import resources.RoomInfos;

public class Shop extends Room {
    private Map<Vector2, String> items;
	private Map<Vector2, String> pickUpItems;

    public Shop(Hero hero, int[] links, int roomId) {
        super(hero, links, roomId);
        super.setLocked(false);
        this.items = new HashMap<Vector2, String>();
		generateItems();
		while (!this.items.containsValue(ImagePaths.HP_UP) || !this.items.containsValue(ImagePaths.BLOOD_OF_THE_MARTYR)) {
			this.items.clear();
			generateItems();
		}
		this.pickUpItems = new HashMap<Vector2, String>();
    }

    public void drawRoom() {
        super.drawRoom();

        if (this.items.size() > 0) {
			for (Map.Entry<Vector2, String> item : this.items.entrySet()) {
				if (item.getValue() == ImagePaths.HEART_PICKABLE || item.getValue() == ImagePaths.HALF_HEART_PICKABLE) {
					StdDraw.picture(item.getKey().getX(), item.getKey().getY(), item.getValue(), RoomInfos.CONSUMABLE_SIZE.getX(), RoomInfos.CONSUMABLE_SIZE.getY(), 0);
                    StdDraw.picture(item.getKey().getX()+0.03, item.getKey().getY()-0.03, ImagePaths.PRICE_THREE, RoomInfos.CONSUMABLE_SIZE.getX(), RoomInfos.CONSUMABLE_SIZE.getY(), 0);
				} else {
					StdDraw.picture(item.getKey().getX(), item.getKey().getY(), item.getValue(), RoomInfos.PASSIVE_SIZE.getX(), RoomInfos.PASSIVE_SIZE.getY(), 0);
                    StdDraw.picture(item.getKey().getX()+0.03, item.getKey().getY()-0.03, ImagePaths.PRICE_FIFTEEN, RoomInfos.CONSUMABLE_SIZE.getX(), RoomInfos.CONSUMABLE_SIZE.getY(), 0);
				}
			}
		}
    }

    public void updateRoom() {
        super.updateRoom();
        if (this.items.size() > 0) {
			pickUpItem();
		}
    }

    private void generateItems() {
		ArrayList<Vector2> coordinates = new ArrayList<Vector2>();
		coordinates.add(new Vector2(0.35, 0.5));
		coordinates.add(new Vector2(0.45, 0.5));
		coordinates.add(new Vector2(0.55, 0.5));
		coordinates.add(new Vector2(0.65, 0.5));
		for (Vector2 coordinate : coordinates) {
			int chance = (int)(Math.random() * 100);
			if (chance < 30) {
				int randomIndex = (int)(Math.random() * 2);
				switch (randomIndex) {
					case 0:
						this.items.put(coordinate, ImagePaths.HP_UP);
						break;
					case 1:
						this.items.put(coordinate, ImagePaths.BLOOD_OF_THE_MARTYR);
				}
			} else {
				int randomIndex = (int)(Math.random() * 2);
				switch (randomIndex) {					
					case 0:
						this.items.put(coordinate, ImagePaths.HALF_HEART_PICKABLE);
						break;

					case 1:
						this.items.put(coordinate, ImagePaths.HEART_PICKABLE);
						break;
				}
			}
		}
	}

    public void pickUpItem() {
		for (Map.Entry<Vector2, String> item : this.items.entrySet()) {
			if (Physics.rectangleCollision(hero.getPosition(), HeroInfos.ISAAC_SIZE, item.getKey(), RoomInfos.CONSUMABLE_SIZE)) {
				if (item.getValue() == ImagePaths.HEART_PICKABLE) {
					if (hero.getPV() < 5 && hero.getCoins() >= 3) {
						hero.setPV(hero.getPV()+2);
                        hero.setCoins(hero.getCoins()-3);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.HALF_HEART_PICKABLE) {
					if (hero.getPV() < 6 && hero.getCoins() >= 3) {
						hero.setPV(hero.getPV()+1);
                        hero.setCoins(hero.getCoins()-3);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.HP_UP) {
					if (!hero.getInventory().contains(ImagePaths.HP_UP) && hero.getCoins() >= 15) {
						hero.getInventory().add(ImagePaths.HP_UP);
						hero.setMaxPV(8);
						hero.setPV(hero.getMaxPV());
                        hero.setCoins(hero.getCoins()-15);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.BLOOD_OF_THE_MARTYR) {
					if (!hero.getInventory().contains(ImagePaths.BLOOD_OF_THE_MARTYR) && hero.getCoins() >= 15) {
						hero.getInventory().add(ImagePaths.BLOOD_OF_THE_MARTYR);
						hero.setTearDamage(hero.getTearDamage()+1);
                        hero.setCoins(hero.getCoins()-15);
						this.pickUpItems.put(item.getKey(), item.getValue());
						hero.setImagePath(ImagePaths.BLOOD_ISAAC);
					}
				}
			}
		}
		for (Map.Entry<Vector2, String> item : this.pickUpItems.entrySet()) {
			items.remove(item.getKey());
		}
	}
}

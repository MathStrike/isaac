package gameWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import libraries.Physics;
import libraries.StdDraw;
import libraries.Vector2;
import resources.HeroInfos;
import resources.ImagePaths;
import resources.RoomInfos;
import gameobjects.Hero;
import gameobjects.Monster;
import gameobjects.Tear;

public class FightRoom extends Room {
    private ArrayList<Monster> monsters;
    private static ArrayList<Monster> deadMonsters;
    private int rockNumber;
	private int spadesNumber;
	private ArrayList<Vector2> rocks;
	private static ArrayList<Vector2> spades;
	private Map<Vector2, String> items;
	private Map<Vector2, String> pickUpItems;

    public FightRoom(Hero hero, int[] links, int roomId, ArrayList<Monster> newMonsters, int rockNumber, int spadesNumber) {
        super(hero, links, roomId);

        this.monsters = newMonsters;
        deadMonsters = new ArrayList<Monster>();
        this.rockNumber = rockNumber;
		this.spadesNumber = spadesNumber;

		rocks = new ArrayList<Vector2>();
		for (int i = 0; i < this.rockNumber; i++) {
			rocks.add(coordinateGenerator(rocks, new ArrayList<Vector2>()));
		}
		spades = new ArrayList<Vector2>();
		for (int i = 0; i < this.spadesNumber; i++) {
			spades.add(coordinateGenerator(spades, rocks));
		}

		this.items = new HashMap<Vector2, String>();
		generateItems();
		this.pickUpItems = new HashMap<Vector2, String>();
    }

    private Vector2 coordinateGenerator(ArrayList<Vector2> list, ArrayList<Vector2> olds) {
		ArrayList<Vector2> coordinates = new ArrayList<Vector2>();
		for (int i = 1; i < RoomInfos.NB_TILES-1; i++) {
			for (int j = 1; j < RoomInfos.NB_TILES-1; j++) {
				if ((i != 5 && j != 2) && (i != 5 && j != 8) && (i != 2 && j != 5) && (i != 8 && j != 5) && (i != 6 && j != 5) && (i != 4 && j != 5) && (i != 5 && j != 6) && (i != 5 && j != 4)) {
					Vector2 position = positionFromTileIndex(i, j);
					if (olds.size() > 0) {
						int isExist = 0;
						for (Vector2 old : olds) {
							if (old == position) {
								isExist++;
							}
						}
						if (isExist == 0) {
							coordinates.add(position);
						}
					} else {
						coordinates.add(position);
					}
				}
			}
		}

		for (Vector2 obstacle : list) {
			coordinates.remove(obstacle);
		}

		int randomIndex = (int)(Math.random() * coordinates.size());
		return coordinates.get(randomIndex);
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

    public void drawRoom() {
        super.drawRoom();

        for (Vector2 rock : rocks) {
			StdDraw.picture(rock.getX(), rock.getY(), ImagePaths.ROCK, RoomInfos.ROCK_SIZE.getX(), RoomInfos.ROCK_SIZE.getY(), 0);
		}

		for (Vector2 spade : spades) {
			StdDraw.picture(spade.getX(), spade.getY(), ImagePaths.SPIKES, RoomInfos.SPADE_SIZE.getX(), RoomInfos.SPADE_SIZE.getY(), 0);
		}

        for (Monster monster : monsters) {
            monster.drawGameObject();
        }

		if (!getLocked() && this.items.size() > 0) {
			for (Map.Entry<Vector2, String> item : this.items.entrySet()) {
				if (item.getValue() == ImagePaths.COIN || item.getValue() == ImagePaths.DIME || item.getValue() == ImagePaths.NICKEL || item.getValue() == ImagePaths.HEART_PICKABLE || item.getValue() == ImagePaths.HALF_HEART_PICKABLE) {
					StdDraw.picture(item.getKey().getX(), item.getKey().getY(), item.getValue(), RoomInfos.CONSUMABLE_SIZE.getX(), RoomInfos.CONSUMABLE_SIZE.getY(), 0);
				} else {
					StdDraw.picture(item.getKey().getX(), item.getKey().getY(), item.getValue(), RoomInfos.PASSIVE_SIZE.getX(), RoomInfos.PASSIVE_SIZE.getY(), 0);
				}
			}
		}

		for (Monster monster : monsters) {
			for (Tear tear : monster.tears) {
				tear.drawGameObject();
			}
		}

    }

    public void updateRoom() {
        super.updateRoom();
        makeMonstersMove();
		if (!getLocked() && this.items.size() > 0) {
			pickUpItem();
		}
		if (!this.hero.getInvincible()) {
			damage();
		}
    }

    private void makeMonstersMove() {
		if (monsters.size() == 0) {
			setLocked(false);
		}
        for (Monster monster : monsters) {
            monster.updateGameObject();
            if (monster.getPV() <= 0) {
                deadMonsters.add(monster);
            }
        }
        monsters.removeAll(deadMonsters);
    }

	private void generateItems() {
		ArrayList<Vector2> coordinates = new ArrayList<Vector2>();
		coordinates.add(new Vector2(0.4, 0.5));
		coordinates.add(new Vector2(0.6, 0.5));
		coordinates.add(new Vector2(0.5, 0.6));
		coordinates.add(new Vector2(0.5, 0.4));
		for (Vector2 coordinate : coordinates) {
			int chance = (int)(Math.random() * 100);
			if (chance < 10) {
				int randomIndex = (int)(Math.random() * 2);
				switch (randomIndex) {
					case 0:
						this.items.put(coordinate, ImagePaths.HP_UP);
						break;

					case 1:
						this.items.put(coordinate, ImagePaths.BLOOD_OF_THE_MARTYR);
						break;
				}
			} else {
				int randomIndex = (int)(Math.random() * 5);
				switch (randomIndex) {
					case 0:
						this.items.put(coordinate, ImagePaths.DIME);
						break;
					
					case 1:
						this.items.put(coordinate, ImagePaths.NICKEL);
						break;

					case 2:
						this.items.put(coordinate, ImagePaths.COIN);
						break;
					
					case 3:
						this.items.put(coordinate, ImagePaths.HALF_HEART_PICKABLE);
						break;

					case 4:
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
					if (hero.getPV() < 5) {
						hero.setPV(hero.getPV()+2);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.HALF_HEART_PICKABLE) {
					if (hero.getPV() < 6) {
						hero.setPV(hero.getPV()+1);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.COIN) {
					if (hero.getCoins() <= 89) {
						hero.setCoins(hero.getCoins()+10);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.NICKEL) {
					if (hero.getCoins() <= 94) {
						hero.setCoins(hero.getCoins()+5);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.DIME) {
					if (hero.getCoins() <= 98) {
						hero.setCoins(hero.getCoins()+1);
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.HP_UP) {
					if (!hero.getInventory().contains(ImagePaths.HP_UP)) {
						hero.getInventory().add(ImagePaths.HP_UP);
						hero.setMaxPV(8);
						hero.setPV(hero.getMaxPV());
						this.pickUpItems.put(item.getKey(), item.getValue());
					}
				} else if (item.getValue() == ImagePaths.BLOOD_OF_THE_MARTYR) {
					if (!hero.getInventory().contains(ImagePaths.BLOOD_OF_THE_MARTYR)) {
						hero.getInventory().add(ImagePaths.BLOOD_OF_THE_MARTYR);
						hero.setTearDamage(hero.getTearDamage()+1);
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

	private void damage() {
		// vérifie la collision avec un monstre
		for (Monster monster : monsters) {
			if (Physics.rectangleCollision(monster.getPosition(), monster.getSize(), this.hero.getPosition(), this.hero.getSize())) {
				if (GameWorld.getCdj() - this.hero.getLastMonsterHit() >= 20 && this.hero.getPV() >= 0) {
					this.hero.setPV(this.hero.getPV()-1);
					this.hero.setLastMonsterHit(GameWorld.getCdj());
				}
			}
		}

		for (Monster monster : monsters) {
			for (Tear tear : monster.tears) {
				if (Physics.rectangleCollision(hero.getPosition(), hero.getSize(), tear.getPosition(), tear.getSize())) {
					monster.outScopeTears.add(tear);
					hero.setPV(hero.getPV()-1);
				}
			}
		}

		// vérifie la collision avec des piques
		for (Vector2 spade : spades) {
			if (Physics.rectangleCollision(spade, RoomInfos.SPADE_SIZE, hero.getPosition(), hero.getSize())) {
				if (GameWorld.getCdj() - hero.getLastSpadeHit() >= 20 && hero.getPV() >= 0) {
					hero.setPV(hero.getPV()-1);
					hero.setLastSpadeHit(GameWorld.getCdj());
				}
			}
		}
	}

	public ArrayList<Vector2> getRocks() {
		return rocks;
	}

	public ArrayList<Monster> getMonsters() {
		return monsters;
	}

}

package gameWorld;

import java.util.ArrayList;

import gameobjects.BossMonster;
import gameobjects.Hero;
import gameobjects.Monster;
import gameobjects.Tear;
import libraries.Physics;

public class Boss extends Room {
    private ArrayList<Monster> monsters;
    private static ArrayList<Monster> deadMonsters;

    public Boss(Hero hero, int[] links, int roomId, BossMonster boss) {
        super(hero, links, roomId);
        this.monsters = new ArrayList<Monster>();
        this.monsters.add(boss);
        this.deadMonsters = new ArrayList<Monster>();
    }

    public void drawRoom() {
        super.drawRoom();

        for (Monster monster : monsters) {
            monster.drawGameObject();
            for (Tear tear : monster.tears) {
                tear.drawGameObject();
            }
        }
    }

    public void updateRoom() {
        super.updateRoom();
        if (monsters.size() > 0) {
            makeBossMove();
        }
        if (!this.hero.getInvincible()) {
			damage();
		}
    }

    public void makeBossMove() {
        for (Monster monster : monsters) {
            monster.updateGameObject();
            if (monster.getPV() <= 0) {
                deadMonsters.add(monster);
            }
        }
        monsters.removeAll(deadMonsters);
    }

    private void damage() {
        for (Monster monster : monsters) {
            // vérifie la collision avec un monstre
            if (Physics.rectangleCollision(monster.getPosition(), monster.getSize(), this.hero.getPosition(), this.hero.getSize())) {
                if (GameWorld.getCdj() - this.hero.getLastMonsterHit() >= 20 && this.hero.getPV() >= 0) {
                    this.hero.setPV(this.hero.getPV()-1);
                    this.hero.setLastMonsterHit(GameWorld.getCdj());
                }
            }

            for (Tear tear : monster.tears) {
                if (Physics.rectangleCollision(hero.getPosition(), hero.getSize(), tear.getPosition(), tear.getSize())) {
                    monster.outScopeTears.add(tear);
                    hero.setPV(hero.getPV()-1);
                }
            }
        }
	}

    public ArrayList<Monster> getMonsters() {
		return monsters;
	}
}

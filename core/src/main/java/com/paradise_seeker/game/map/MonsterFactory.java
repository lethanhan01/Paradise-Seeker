package com.paradise_seeker.game.map;

import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.monster.boss.*;
import com.paradise_seeker.game.entity.monster.creep.*;
import com.paradise_seeker.game.entity.monster.elite.*;

public class MonsterFactory {

    /**
     * Creates a Monster instance of the given class name, at (x, y), linked to the player.
     * The className string must match the "class" property in your Tiled map.
     */
    public static Monster create(String className, float x, float y) {
        Monster m = null;
        switch (className) {
            case "CyanBat":
                m = new CyanBat(x, y);
                break;
            case "DevilCreep":
                m = new DevilCreep(x, y);
                break;
            case "EvilPlant":
                m = new EvilPlant(x, y);
                break;
            case "FlyingCreep":
                m = new FlyingCreep(x, y);
                break;
            case "GhostStatic":
                m = new GhostStatic(x, y);
                break;
            case "RatCreep":
                m = new RatCreep(x, y);
                break;
            case "SkeletonEnemy":
                m = new SkeletonEnemy(x, y);
                break;
            case "YellowBat":
                m = new YellowBat(x, y);
                break;

            case "FirewormElite":
                m = new FirewormElite(x, y);
                break;
            case "FlyingDemon":
                m = new FlyingDemon(x, y);
                break;
            case "IceElite":
                m = new IceElite(x, y);
                break;
            case "MinotaurElite":
                m = new MinotaurElite(x, y);
                break;
            case "Necromancer":
          		m = new Necromancer(x, y);
				break;

            case "CyclopsBoss":
                m = new CyclopBoss(x, y);
                break;
            case "FireDemon":
                m = new FireDemon(x, y);
                break;
            case "Nyx":
                m = new Nyx(x, y);
                break;
            case "ParadiseKing":
                m = new ParadiseKing(x, y);
                break;

            default:
                System.err.println("Unknown monster class: " + className);
        }
        return m;
    }
}


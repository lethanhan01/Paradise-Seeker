package com.paradise_seeker.game.entity;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.map.GameMap;

public abstract class Character implements Collidable {
    public float hp;
    public float maxHp; // Thêm maxHp để dễ quản lý
    public float mp;
    public float maxMp; // Thêm maxMp để dễ quản lý
    public float atk;
    public float speed;
    protected Rectangle bounds = new Rectangle(); // Kích thước và vị trí của nhân vật
    public float x, y;

    public Character() {
    }

    public Character(Rectangle bounds, float hp, float mp, float maxHp, float maxMp, float atk, float speed, float x, float y) {
        this.bounds = bounds;
        this.hp = hp;
        this.mp = mp;
        this.atk = atk;
        this.speed = speed;
        this.maxHp = maxHp;
        this.maxMp = maxMp;
        this.x = x;
        this.y = y;
    }

    public abstract void act(float deltaTime, GameMap map);

    public abstract void takeHit(float dmg);

    @Override
    public abstract void onCollision(Collidable other);

    public abstract void onDeath();

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

}

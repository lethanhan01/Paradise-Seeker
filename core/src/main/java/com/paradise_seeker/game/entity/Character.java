package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.skill.Skill;

public abstract class Character implements Collidable {
    public float hp;
    public float maxHp; // Thêm maxHp để dễ quản lý
    public float mp;
    public float maxMp; // Thêm maxMp để dễ quản lý
    public float atk;
    public float speed;
    public Rectangle bounds = new Rectangle(); // Kích thước và vị trí của nhân vật
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

    // Nhận sát thương
    public void receiveDamage(float dmg) {
        hp = Math.max(0, hp - dmg);
        if (hp == 0) onDeath();
    }

    public abstract void onDeath();
    // Kiểm tra còn sống
    public boolean isAlive() {
        return hp > 0;
    }
    // Cập nhật trạng thái nhân vật
    public abstract void render(SpriteBatch batch);

    // Collidable
    public Rectangle getBounds() {
        return bounds;
    }

    public void onCollision(Collidable other) {
        // xử lý va chạm mặc định (vd: đạn bắn trúng)
    }

    public Rectangle getHitbox() {
		return bounds; // Trả về hitbox của nhân vật
	}

}

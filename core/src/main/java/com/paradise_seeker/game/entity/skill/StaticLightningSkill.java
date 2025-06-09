package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

public class StaticLightningSkill {
    public float x, y;
    public float damage;
    public String direction;
    public Animation<TextureRegion> animation;
    public Rectangle hitbox;
    public float stateTime = 0f;
    public boolean active = true;
    public boolean hasDealtDamage = false;
    public float scale = 0.02f;  // dùng chung cho render & hitbox

    public StaticLightningSkill(float x, float y, float damage, String direction, Animation<TextureRegion> animation) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.direction = direction;
        this.animation = animation;

        // Tính hitbox chính xác từ frame đầu tiên
        TextureRegion frame = animation.getKeyFrame(0f);
        float realWidth = frame.getRegionWidth() * scale;
        float realHeight = frame.getRegionHeight() * scale;

        this.hitbox = new Rectangle(
            x - realWidth / 2f,
            y - realHeight / 2f,
            realWidth,
            realHeight
        );
    }

    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
        if (animation.isAnimationFinished(stateTime)) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame = animation.getKeyFrame(stateTime, false);
        float realWidth = frame.getRegionWidth() * scale;
        float realHeight = frame.getRegionHeight() * scale;
        float drawX = x - realWidth / 2f;
        float drawY = y - realHeight / 2f;

        batch.draw(frame, drawX, drawY, realWidth, realHeight);
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getDamage() {
        return damage;
    }

    public void setInactive() {
        this.active = false;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        if (hitbox != null) {
            hitbox.setPosition(x - hitbox.getWidth() / 2f, y - hitbox.getHeight() / 2f);
        }
    }
    public boolean hasDealtDamage() {
        return hasDealtDamage;
    }
    public void markDamageDealt() {
        hasDealtDamage = true;
    }
}

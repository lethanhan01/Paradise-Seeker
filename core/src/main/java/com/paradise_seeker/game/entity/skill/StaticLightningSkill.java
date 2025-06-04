package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;

public class StaticLightningSkill {
    private float x, y;
    private float damage;
    private String direction;
    private Animation<TextureRegion> animation;
    private Rectangle hitbox;
    private float stateTime = 0f;
    private boolean active = true;

    public StaticLightningSkill(float x, float y, float damage, String direction, Animation<TextureRegion> animation) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.direction = direction;
        this.animation = animation;
        this.hitbox = new Rectangle(x - 0.25f, y - 0.25f, 0.5f, 1f); // chỉnh lại theo size hình ảnh
    }

    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
        if (animation.isAnimationFinished(stateTime)) {
            active = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame = animation.getKeyFrame(stateTime, false);
        float scale = 0.02f;
        float drawX = x - frame.getRegionWidth() * scale / 2f;
        float drawY = y - frame.getRegionHeight() * scale / 2f;
        batch.draw(frame, drawX, drawY, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
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
        this.hitbox.setPosition(x - 0.25f, y - 0.25f);
    }

}

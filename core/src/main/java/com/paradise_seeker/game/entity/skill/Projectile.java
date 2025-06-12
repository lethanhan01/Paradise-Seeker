package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface Projectile {
    void update();
    void render(SpriteBatch batch);
    boolean isActive();
    Rectangle getHitbox();
    float getDamage();
    void setInactive();
} 
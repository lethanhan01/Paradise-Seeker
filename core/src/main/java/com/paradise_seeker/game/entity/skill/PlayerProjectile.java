package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public interface PlayerProjectile {
    void update();
    void isRendered(SpriteBatch batch);
    boolean isActive();
    Rectangle getHitbox();
    float getDamage();
    void setInactive();
}
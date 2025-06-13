package com.paradise_seeker.game.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {
    public Rectangle getBounds();
    public void onCollision(Collidable other);

	public default boolean isSolid() {
        return false;
	}
}

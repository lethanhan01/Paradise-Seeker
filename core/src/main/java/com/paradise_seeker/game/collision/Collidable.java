package com.paradise_seeker.game.collision;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;

public interface Collidable {
    public Rectangle getBounds();
    public void onCollision(Collidable other);
}

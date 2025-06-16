package com.paradise_seeker.game.map;

import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.player.Player;


public class SolidObject implements Collidable {
    private Rectangle bounds;

    public SolidObject(Rectangle bounds) {
        this.bounds = bounds;
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void onCollision(Collidable other) {
    	if (other instanceof Player) {
    	    Player player = (Player) other;
    	    player.blockMovement();
    	}
    }
    public void onCollision(Player player) {

    }
}

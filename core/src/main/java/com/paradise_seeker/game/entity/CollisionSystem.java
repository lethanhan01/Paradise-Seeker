package com.paradise_seeker.game.entity;

import java.util.List;

import com.paradise_seeker.game.entity.player.Player;

public class CollisionSystem {

    public static void checkCollisions(Player player, List<Collidable> collidables) {
        for (Collidable c : collidables) {
            if (player.getBounds().overlaps(c.getBounds())) {
                c.onCollision(player);
            }
        }
    }
}

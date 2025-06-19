package com.paradise_seeker.game.collision;

import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;

/**
 * Responsible for handling all collision-related logic for monsters.
 * This class manages collision detection and response with other game entities.
 */
public class MonsterCollisionHandler {
    private float cleaveRange;
    private boolean pendingCleaveHit;
    private boolean cleaveDamageDealt;

    public MonsterCollisionHandler() {
        this.cleaveRange = 2.5f; // Default cleave range
        this.pendingCleaveHit = false;
        this.cleaveDamageDealt = false;
    }


    public void handleCollision(Collidable other, Monster monster) {
        if (other instanceof Player) {
            handlePlayerCollision((Player) other, monster);
        }
    }


    public void handlePlayerCollision(Player player, Monster monster) {
        if (monster.isDead() || player.statusManager.isInvulnerable()) {
            return;
        }

        // Calculate damage based on monster's attack value
        float damage = (float) monster.atk;

        if (!player.statusManager.isInvulnerable()) {
                player.takeHit(damage);
        }
    }

    /**
     * Check if player is within cleave range
     */
    public boolean isPlayerInCleaveRange(Player player, Monster monster) {
        float distanceX = Math.abs(player.getBounds().x - monster.getBounds().x);
        float distanceY = Math.abs(player.getBounds().y - monster.getBounds().y);

        return distanceX <= 5f+cleaveRange && distanceY <= 5f+cleaveRange / 2;
    }

    /**
     * Apply cleave damage to a player
     */
    public void applyCleaveHitToPlayer(Player player, Monster monster) {
        if (pendingCleaveHit && !cleaveDamageDealt) {
            // Apply damage if player is not invulnerable
            if (!player.statusManager.isInvulnerable()) {
                player.takeHit((float) monster.atk);
                player.statusManager.setHit(true);
                player.statusManager.setStateTime(0f);
            }

            cleaveDamageDealt = true;
        }
    }

    /**
     * Set the cleave range for this monster
     */
    public void setCleaveRange(float range) {
        this.cleaveRange = range;
    }

    /**
     * Mark a cleave attack as pending
     */
    public void setPendingCleaveHit(boolean pending) {
        this.pendingCleaveHit = pending;
        if (pending) {
            this.cleaveDamageDealt = false;
        }
    }

}

package com.paradise_seeker.game.entity.monster;

import com.paradise_seeker.game.entity.Collidable;
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
        if (monster.isDead() || player.isInvulnerable()) {
            return;
        }

        // Calculate damage based on monster's attack value
        int damage = (int) monster.atk;

        // If player is shielding, reduce damage and handle shield effects
        if (player.isShielding) {
            player.isShieldedHit = true;
            damage = Math.max(1, damage / 2); // Reduce damage if shielding
        } else {
            if (!player.isInvulnerable()) {
                player.takeHit(damage);
            }
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
            // Calculate cleave damage (could be higher than regular damage)
            float cleaveDamage = monster.atk * 1.5f;

            // Apply damage if player is not invulnerable
            if (!player.isInvulnerable()) {
                player.takeHit((int) cleaveDamage);
                player.isHit = true;
                player.stateTime = 0;
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

    /**
     * @return whether a cleave hit is pending
     */
    public boolean isPendingCleaveHit() {
        return pendingCleaveHit;
    }

    /**
     * @return whether cleave damage has been dealt
     */
    public boolean isCleaveDamageDealt() {
        return cleaveDamageDealt;
    }

    /**
     * Set whether cleave damage has been dealt
     */
    public void setCleaveDamageDealt(boolean dealt) {
        this.cleaveDamageDealt = dealt;
    }

    /**
     * @return the cleave range
     */
    public float getCleaveRange() {
        return cleaveRange;
    }
}

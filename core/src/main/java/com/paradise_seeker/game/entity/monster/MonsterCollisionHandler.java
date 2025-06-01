package com.paradise_seeker.game.entity.monster;

import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.Player;

/**
 * Responsible for handling all collision-related logic for monsters.
 * This class manages collision detection and response with other game entities.
 */
public class MonsterCollisionHandler {
    private Monster owner;
    private float cleaveRange;
    private boolean pendingCleaveHit;
    private boolean cleaveDamageDealt;

    public MonsterCollisionHandler(Monster monster) {
        this.owner = monster;
        this.cleaveRange = 4f; // Default cleave range
        this.pendingCleaveHit = false;
        this.cleaveDamageDealt = false;
    }

    /**
     * Handle collision with another collidable entity
     */
    public void handleCollision(Collidable other) {
        if (other instanceof Player) {
            handlePlayerCollision((Player) other);
        }
    }

    /**
     * Handle collision specifically with a player
     */
    public void handlePlayerCollision(Player player) {
        if (owner.isDead() || player.isInvulnerable()) {
            return;
        }

        // Calculate damage based on monster's attack value
        int damage = (int) owner.getAtk();

        // If player is shielding, reduce damage and handle shield effects
        if (player.isShielding) {
            player.isShieldedHit = true;
            damage = Math.max(1, damage / 2); // Reduce damage if shielding
        } else {
            player.isHit = true;
            player.stateTime = 0;
        }

        // Apply damage to player unless they're invulnerable from a recent hit
        if (!player.isInvulnerable()) {
            player.takeDamage(damage);
        }
    }

    /**
     * Check if player is within cleave range
     */
    public boolean isPlayerInCleaveRange(Player player) {
        float distanceX = Math.abs(player.bounds.x - owner.getBounds().x);
        float distanceY = Math.abs(player.bounds.y - owner.getBounds().y);

        return distanceX <= cleaveRange && distanceY <= cleaveRange / 2;
    }

    /**
     * Apply cleave damage to a player
     */
    public void applyCleaveHitToPlayer(Player player) {
        if (pendingCleaveHit && !cleaveDamageDealt) {
            // Calculate cleave damage (could be higher than regular damage)
            float cleaveDamage = owner.getAtk() * 1.5f;

            // Apply damage if player is not invulnerable
            if (!player.isInvulnerable()) {
                player.takeDamage((int) cleaveDamage);
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

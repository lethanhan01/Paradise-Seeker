package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Responsible for managing all monster animations.
 * Handles animation states, timers, and provides the appropriate animation frame based on monster state.
 */
public class MonsterAnimationManager {
    private Monster owner;

    // Animation sets
    private Animation<TextureRegion> idleLeft, idleRight;
    private Animation<TextureRegion> walkLeft, walkRight;
    private Animation<TextureRegion> takeHitLeft, takeHitRight;
    private Animation<TextureRegion> cleaveLeft, cleaveRight;
    private Animation<TextureRegion> deathLeft, deathRight;

    // Animation state
    private TextureRegion currentFrame;
    private float stateTime = 0f;
    private boolean facingRight = true;

    // Animation timers
    private boolean isTakingHit = false;
    private float takeHitTimer = 0f;
    private float takeHitDuration = 0.5f;

    private boolean isCleaving = false;
    private float cleaveTimer = 0f;
    private float cleaveDuration = 1.2f;

    public MonsterAnimationManager(Monster monster) {
        this.owner = monster;
    }

    /**
     * Sets all the animations for this monster
     */
    public void setAnimations(Animation<TextureRegion> idleLeft, Animation<TextureRegion> idleRight,
                            Animation<TextureRegion> walkLeft, Animation<TextureRegion> walkRight,
                            Animation<TextureRegion> takeHitLeft, Animation<TextureRegion> takeHitRight,
                            Animation<TextureRegion> cleaveLeft, Animation<TextureRegion> cleaveRight,
                            Animation<TextureRegion> deathLeft, Animation<TextureRegion> deathRight) {
        this.idleLeft = idleLeft;
        this.idleRight = idleRight;
        this.walkLeft = walkLeft;
        this.walkRight = walkRight;
        this.takeHitLeft = takeHitLeft;
        this.takeHitRight = takeHitRight;
        this.cleaveLeft = cleaveLeft;
        this.cleaveRight = cleaveRight;
        this.deathLeft = deathLeft;
        this.deathRight = deathRight;
    }

    /**
     * Update the animation state
     * @param deltaTime time since last frame
     * @param isMoving if the monster is moving
     * @param facingRight if the monster is facing right
     * @param isDead if the monster is dead
     */
    public void update(float deltaTime, boolean isMoving, boolean facingRight, boolean isDead, boolean isTakingHit) {
        stateTime += deltaTime;
        this.facingRight = facingRight;

        // Update hit animation timer
        if (isTakingHit) {
            this.isTakingHit = true;
            this.takeHitTimer = this.takeHitDuration;
        } else if (this.isTakingHit) {
            this.takeHitTimer -= deltaTime;
            if (this.takeHitTimer <= 0f) {
                this.isTakingHit = false;
            }
        }

        // Update cleave animation timer
        if (isCleaving) {
            cleaveTimer -= deltaTime;
            if (cleaveTimer <= 0f) {
                isCleaving = false;
            }
        }

        // Select appropriate animation frame based on state
        updateCurrentFrame(isDead, isMoving);
    }

    /**
     * Updates the current frame based on monster state
     */
    private void updateCurrentFrame(boolean isDead, boolean isMoving) {
        if (isDead) {
            currentFrame = facingRight ?
                deathRight.getKeyFrame(stateTime, false) :
                deathLeft.getKeyFrame(stateTime, false);
            return;
        }

        if (isCleaving) {
            currentFrame = facingRight ?
                cleaveRight.getKeyFrame(cleaveTimer, false) :
                cleaveLeft.getKeyFrame(cleaveTimer, false);
            return;
        }

        if (isTakingHit) {
            currentFrame = facingRight ?
                takeHitRight.getKeyFrame(takeHitTimer, false) :
                takeHitLeft.getKeyFrame(takeHitTimer, false);
            return;
        }

        if (isMoving) {
            currentFrame = facingRight ?
                walkRight.getKeyFrame(stateTime, true) :
                walkLeft.getKeyFrame(stateTime, true);
        } else {
            currentFrame = facingRight ?
                idleRight.getKeyFrame(stateTime, true) :
                idleLeft.getKeyFrame(stateTime, true);
        }
    }

    /**
     * Start cleave animation
     */
    public void startCleaveAnimation() {
        isCleaving = true;
        cleaveTimer = cleaveDuration;
    }

    /**
     * Start take hit animation
     */
    public void startTakeHitAnimation() {
        isTakingHit = true;
        takeHitTimer = takeHitDuration;
    }

    /**
     * @return the current animation frame
     */
    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    /**
     * @return true if the monster is currently in the cleave animation
     */
    public boolean isCleaving() {
        return isCleaving;
    }

    /**
     * Set the duration for the cleave animation
     */
    public void setCleaveDuration(float duration) {
        this.cleaveDuration = duration;
    }

    /**
     * Set the duration for the take hit animation
     */
    public void setTakeHitDuration(float duration) {
        this.takeHitDuration = duration;
    }

    /**
     * Reset the state time (e.g., when transitioning animations)
     */
    public void resetStateTime() {
        this.stateTime = 0f;
    }

    /**
     * @return the current state time of animations
     */
    public float getStateTime() {
        return stateTime;
    }

    /**
     * @return true if the monster is facing right
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * Set whether the monster is facing right
     */
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    /**
     * @return true if the monster is currently in the take hit animation
     */
    public boolean isTakingHit() {
        return isTakingHit;
    }
}

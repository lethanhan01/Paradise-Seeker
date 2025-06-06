package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.player.AnimationManager;

/**
 * Responsible for managing all monster animations.
 * Handles animation states, timers, and provides the appropriate animation frame based on monster state.
 */
public class MonsterAnimationManagerImpl implements AnimationManager {
    public Monster owner;

    // Animation sets
    public Animation<TextureRegion> idleLeft, idleRight;
    public Animation<TextureRegion> walkLeft, walkRight;
    public Animation<TextureRegion> takeHitLeft, takeHitRight;
    public Animation<TextureRegion> cleaveLeft, cleaveRight;
    public Animation<TextureRegion> deathLeft, deathRight;

    // Animation state
    public TextureRegion currentFrame;
    public float stateTime = 0f;
    public boolean facingRight = true;

    // Animation timers
    public boolean isTakingHit = false;
    public float takeHitTimer = 0f;
    public float takeHitDuration = 0.5f;

    public boolean isCleaving = false;
    public float cleaveTimer = 0f;
    public float cleaveDuration = 1.2f;

    public MonsterAnimationManagerImpl(Monster monster) {
        this.owner = monster;
    }
    @Override
	public void loadAnimations() {

	}

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

    public void update(float deltaTime, boolean isMoving, boolean facingRight, boolean isDead, boolean isTakingHit, float playerX) {
        stateTime += deltaTime;
        this.facingRight = playerX > owner.getBounds().x;

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
    public void setCleaveAnimations(Animation<TextureRegion> left, Animation<TextureRegion> right) {
        this.cleaveLeft = left;
        this.cleaveRight = right;
    }

	@Override
	public Animation<TextureRegion> getRunAnimation(String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation<TextureRegion> getIdleAnimation(String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation<TextureRegion> getAttackAnimation(String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation<TextureRegion> getHitAnimation(String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation<TextureRegion> getDeathAnimation(String direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}

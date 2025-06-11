package com.paradise_seeker.game.rendering.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.rendering.AnimationManager;

/**
 * Responsible for managing all monster animations.
 * Handles animation states, timers, and provides the appropriate animation frame based on monster state.
 */
public class MonsterAnimationManager implements AnimationManager {
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

    public MonsterAnimationManager(Monster monster) {
        this.owner = monster;
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

    public void update(float deltaTime, boolean isMoving, boolean isDead, boolean isTakingHit, float playerX) {
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
            cleaveTimer += deltaTime; // <-- Tăng dần!
            if (cleaveTimer >= cleaveDuration) {
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
    public void startCleaveAnimation() {
        isCleaving = true;
        cleaveTimer = 0f;
    }
    public void startTakeHitAnimation() {
        isTakingHit = true;
        takeHitTimer = takeHitDuration;
    }
    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }
    public boolean isCleaving() {
        return isCleaving;
    }
    public float getStateTime() {
        return stateTime;
    }
    public boolean isFacingRight() {
        return facingRight;
    }
    public void setCleaveAnimations(Animation<TextureRegion> left, Animation<TextureRegion> right) {
        this.cleaveLeft = left;
        this.cleaveRight = right;
    }

	@Override
	public Animation<TextureRegion> getRunAnimation(String direction) {
		switch (direction) {
			case "left": return walkLeft;
			case "right": return walkRight;
			default: return walkRight;
		}
	}

	@Override
	public Animation<TextureRegion> getIdleAnimation(String direction) {
		switch (direction) {
			case "left": return idleLeft;
			case "right": return idleRight;
			default: return idleRight;
		}
	}

	@Override
	public Animation<TextureRegion> getAttackAnimation(String direction) {
		switch (direction) {
			case "left": return cleaveLeft;
			case "right": return cleaveRight;
			default: return cleaveLeft;
		}
	}

	@Override
	public Animation<TextureRegion> getHitAnimation(String direction) {
		switch (direction) {
			case "left": return takeHitLeft;
			case "right": return takeHitRight;
			default: return takeHitRight;
		}
	}

	@Override
	public Animation<TextureRegion> getDeathAnimation(String direction) {
		switch (direction) {
			case "left": return deathLeft;
			case "right": return deathRight;
			default: return deathRight;
		}
	}

	@Override
	public void dispose() {

	}
}

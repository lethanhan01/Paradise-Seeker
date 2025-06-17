package com.paradise_seeker.game.entity.player.status;

import com.badlogic.gdx.math.Vector2;

public class PlayerStatusManager {
    public static final float INVULNERABILITY_DURATION = 0.7f;
	private String direction;
    private boolean isMoving;
    private boolean isAttacking;
    private boolean isDead;
    private boolean isHit;
    private boolean isInvulnerable;
    private boolean doNothing;
    private float invulnerabilityTimer;
    private float stateTime;
    private final Vector2 lastPosition = new Vector2();

    public PlayerStatusManager() {
    	 this.direction = "down";
    	 this.isMoving = false;
    	 this.isAttacking = false;
    	 this.isDead = false;
    	 this.isHit = false;
    	 this.isInvulnerable = false;
    	 this.doNothing = false;
    	 this.invulnerabilityTimer = 0f;
    	 this.stateTime = 0f;
	}

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public boolean isMoving() { return isMoving; }
    public void setMoving(boolean moving) { isMoving = moving; }

    public boolean isAttacking() { return isAttacking; }
    public void setAttacking(boolean attacking) { isAttacking = attacking; }

    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { isDead = dead; }

    public boolean isHit() { return isHit; }
    public void setHit(boolean hit) { isHit = hit; }

    public boolean isInvulnerable() { return isInvulnerable; }
    public void setInvulnerable(boolean invulnerable) { isInvulnerable = invulnerable; }

    public boolean isDoNothing() { return doNothing; }
    public void setDoNothing(boolean doNothing) { this.doNothing = doNothing; }

    public float getInvulnerabilityTimer() { return invulnerabilityTimer; }
    public void setInvulnerabilityTimer(float invulnerabilityTimer) { this.invulnerabilityTimer = invulnerabilityTimer; }

    public float getStateTime() { return stateTime; }
    public void setStateTime(float stateTime) { this.stateTime = stateTime; }
    public void addStateTime(float delta) { this.stateTime += delta; }
    public void resetStateTime() { this.stateTime = 0f; }

    public Vector2 getLastPosition() { return lastPosition; }
    public void setLastPosition(float x, float y) {
		this.lastPosition.set(x, y);
    }
}

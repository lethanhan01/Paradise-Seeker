package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.math.Vector2;

public class MonsterStatusManger {
    private float spawnX;
    private float spawnY;
    private boolean hasSetBounds ;
    private boolean isDead ;

    private float stateTime ;
    private boolean isTakingHit ;
    private boolean isCleaving ;
    private boolean isMoving ;

    private Vector2 lastPosition;
    private float cleaveRange;
    private boolean pendingCleaveHit;
    private boolean cleaveDamageDealt;

    public MonsterStatusManger() {
    	hasSetBounds = false;
        isDead = false;

        stateTime = 0f;
        isTakingHit = false;
        isCleaving = false;
        isMoving = false;

        lastPosition = new Vector2();
		this.cleaveRange = 2.5f; // Default cleave range
		this.pendingCleaveHit = false;
		this.cleaveDamageDealt = false;
	}

    public float getSpawnX() {
		return spawnX;
	}
	public void setSpawnX(float spawnX) {
		this.spawnX = spawnX;
	}
	public float getSpawnY() {
		return spawnY;
	}
	public void setSpawnY(float spawnY) {
		this.spawnY = spawnY;
	}
	public boolean isHasSetBounds() {
		return hasSetBounds;
	}
	public void setHasSetBounds(boolean hasSetBounds) {
		this.hasSetBounds = hasSetBounds;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	public float getStateTime() {
		return stateTime;
	}
	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}
	public boolean isTakingHit() {
		return isTakingHit;
	}
	public void setTakingHit(boolean isTakingHit) {
		this.isTakingHit = isTakingHit;
	}
	public boolean isCleaving() {
		return isCleaving;
	}
	public void setCleaving(boolean isCleaving) {
		this.isCleaving = isCleaving;
	}
	public boolean isMoving() {
		return isMoving;
	}
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	public Vector2 getLastPosition() {
		return lastPosition;
	}
	public void setLastPosition(Vector2 lastPosition) {
		this.lastPosition = lastPosition;
	}
	public float getCleaveRange() {
		return cleaveRange;
	}
	public void setCleaveRange(float cleaveRange) {
		this.cleaveRange = cleaveRange;
	}
	public boolean isPendingCleaveHit() {
		return pendingCleaveHit;
	}
	public void setPendingCleaveHit(boolean pendingCleaveHit) {
		this.pendingCleaveHit = pendingCleaveHit;
	}
	public boolean isCleaveDamageDealt() {
		return cleaveDamageDealt;
	}
	public void setCleaveDamageDealt(boolean cleaveDamageDealt) {
		this.cleaveDamageDealt = cleaveDamageDealt;
	}

}

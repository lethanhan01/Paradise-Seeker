package com.paradise_seeker.game.entity.npc.gipsy;

/**
 * Class quản lý trạng thái và stateTime của NPC Gipsy
 */
public class GipsyStateManager {
    // --- State flags ---
    public boolean isChestOpened;
    public boolean isOpeningChest;
    public boolean isTalking;
    public boolean hasTalked;

    // --- StateTimes cho từng trạng thái ---
    private float idleStateTime = 0f;
    private float talkingStateTime = 0f;
    private float openChestStateTime = 0f;

    public GipsyStateManager() {
        isChestOpened = false;
        isOpeningChest = false;
        isTalking = false;
        hasTalked = false;
    }

    // Getters/setters cho stateTime
    public float getIdleStateTime() { return idleStateTime; }
    public void setIdleStateTime(float t) { idleStateTime = t; }
    public float getTalkingStateTime() { return talkingStateTime; }
    public void setTalkingStateTime(float t) { talkingStateTime = t; }
    public float getOpenChestStateTime() { return openChestStateTime; }
    public void setOpenChestStateTime(float t) { openChestStateTime = t; }

    // Update stateTime theo trạng thái
    public void updateStateTime(float delta, String state) {
        switch (state) {
            case "idle": idleStateTime += delta; break;
            case "talking": talkingStateTime += delta; break;
            case "openChest": openChestStateTime += delta; break;
        }
    }

    // Reset stateTime khi chuyển trạng thái
    public void resetStateTime(String state) {
        switch (state) {
            case "idle": idleStateTime = 0f; break;
            case "talking": talkingStateTime = 0f; break;
            case "openChest": openChestStateTime = 0f; break;
        }
    }

    // ... các method về trạng thái giữ nguyên như cũ ...
    public boolean hasTalked() { return hasTalked; }
    public void setHasTalked(boolean value) { this.hasTalked = value; }
    public boolean isChestOpened() { return isChestOpened; }
    public void setChestOpened(boolean opened) { this.isChestOpened = opened; }
    public boolean isOpeningChest() { return isOpeningChest; }
    public void setOpeningChest(boolean opening) { this.isOpeningChest = opening; }
    public boolean isTalking() { return isTalking; }
    public void setTalking(boolean talking) { this.isTalking = talking; }

    public void startChestOpening() {
        if (isChestOpened || isOpeningChest) {
            return;
        }
        isOpeningChest = true;
        isTalking = false; // Stop talking when opening chest
    }

    public void completeChestOpening() {
        isChestOpened = true;
        isOpeningChest = false;
    }

    public boolean isChestOpenAndFinished() {
        return isChestOpened && !isOpeningChest;
    }
}

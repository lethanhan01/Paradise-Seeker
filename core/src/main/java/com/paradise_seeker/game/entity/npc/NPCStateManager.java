package com.paradise_seeker.game.entity.npc;

/**
 * Class quản lý trạng thái của NPC
 */
public class NPCStateManager {
    // --- State flags ---
    public boolean isChestOpened;
    public boolean isOpeningChest;
    public boolean isTalking;
    public boolean hasTalked;

    public NPCStateManager() {
        isChestOpened = false;
        isOpeningChest = false;
        isTalking = false;
        hasTalked = false;
    }

    // Getters and setters for state
    public boolean hasTalked() {
        return hasTalked;
    }

    public void setHasTalked(boolean value) {
        this.hasTalked = value;
    }

    public boolean isChestOpened() {
        return isChestOpened;
    }

    public void setChestOpened(boolean opened) {
        this.isChestOpened = opened;
    }

    public boolean isOpeningChest() {
        return isOpeningChest;
    }

    public void setOpeningChest(boolean opening) {
        this.isOpeningChest = opening;
    }

    public boolean isTalking() {
        return isTalking;
    }

    public void setTalking(boolean talking) {
        this.isTalking = talking;
    }

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

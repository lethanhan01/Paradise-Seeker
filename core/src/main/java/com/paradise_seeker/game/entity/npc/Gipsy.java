package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.map.GameMap;
import java.util.ArrayList;
import java.util.List;

public class Gipsy extends NPC {
    public NPCStateManager stateManager;
    protected DialogueManager dialogueManager;
    protected float spriteWidth = 1.9f;
    protected float spriteHeight = 1.8f;

    public Gipsy(float x, float y) {
        super();
        this.x = x;
        this.y = y;
        this.bounds = new Rectangle(x, y, spriteWidth, spriteHeight);

        this.stateManager = new NPCStateManager();
        this.dialogueManager = new DialogueManager();

        // Khởi tạo thoại mặc định
        List<String> defaultDialogue = new ArrayList<>();
        defaultDialogue.add("Hello, traveler!");
        defaultDialogue.add("I am Gipsy, a wandering merchant.");
        defaultDialogue.add("Would you like to open a chest?");
        dialogueManager.setDialogue(defaultDialogue);
    }

    public Gipsy() {
    	super();
        this.bounds = new Rectangle(x, y, spriteWidth, spriteHeight);

        this.stateManager = new NPCStateManager();
        this.dialogueManager = new DialogueManager();

        // Khởi tạo thoại mặc định
        List<String> defaultDialogue = new ArrayList<>();
        defaultDialogue.add("Hello, traveler!");
        defaultDialogue.add("I am Gipsy, a wandering merchant.");
        defaultDialogue.add("Would you like to open a chest?");
        dialogueManager.setDialogue(defaultDialogue);
    }

    public void updateBounds() {
        if (bounds != null) {
            bounds.setSize(spriteWidth, spriteHeight);
            bounds.setPosition(x, y);
        }
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void act(float deltaTime, GameMap map) {
        // Nếu đang mở rương thì chỉ update animation cho openChest
        if (stateManager.isOpeningChest()) {
            this.stateTime += deltaTime;
            if (animationManager.isAnimationFinished(animationManager.getOpenChestAnimation(), this.stateTime)) {
                stateManager.completeChestOpening();
            }
        } else {
            // Nếu không thì update animation cho idle hoặc talking (nếu có)
            this.update(deltaTime);
        }

        // Đồng bộ trạng thái cha
        super.isTalking = stateManager.isTalking();
        super.hasTalked = stateManager.hasTalked();

        updateBounds();
    }

    @Override
    public void setTalking(boolean talking) {
        stateManager.setTalking(talking);
        super.isTalking = talking;
    }

    public void openChest() {
        if (hasNextLine()) return;
        if (stateManager.isChestOpened() || stateManager.isOpeningChest()) return;
        if (stateManager.isTalking()) return; // Chưa kết thúc hội thoại, không mở
        stateManager.startChestOpening();
        animationManager.setOpenChestAnimation();
        this.stateTime = 0f; // Reset thời gian cho animation
    }


    // -------------- Dialogue management ---------------
    public boolean hasNextLine() {
        return dialogueManager.hasNextLine();
    }

    public void nextLine() {
        dialogueManager.nextLine();
    }

    public String getCurrentLine() {
        return dialogueManager.getCurrentLine();
    }

    public void resetDialogue() {
        dialogueManager.resetDialogue();
    }

    public boolean shouldShowOptions() {
        // Chỉ hiển thị tuỳ chọn khi đã nói xong và chưa mở rương
        return !hasNextLine() && stateManager.isTalking() && !stateManager.isChestOpened();
    }

    public boolean isChestOpened() {
        return stateManager.isChestOpened();
    }

    public boolean isChestOpenAndFinished() {
        return stateManager.isChestOpenAndFinished();
    }

    public boolean hasTalked() {
        return stateManager.hasTalked();
    }

    @Override
    protected void loadTexture() {
        texture = new Texture("images/Entity/characters/NPCs/npc1/act3/npc120.png");
    }
}

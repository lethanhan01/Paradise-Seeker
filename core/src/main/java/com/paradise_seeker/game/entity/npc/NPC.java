package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.entity.Collidable;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.rendering.animations.NPCAnimationManager;

public abstract class NPC extends Character implements Collidable {
    public String dialogue;
    public boolean isTalking;
    public boolean hasTalked;
    protected Texture texture;
    protected NPCAnimationManager animationManager = new NPCAnimationManager();
    protected float stateTime = 0f;

    public NPCAnimationManager getAnimationManager() { return animationManager; }
    public float getStateTime() { return stateTime; }

    public NPC() {
        super();
        this.bounds = new Rectangle(0, 0, 1, 1);
        this.atk = 0;
        this.speed = 2f;
        this.x = 0;
        this.y = 0;
        this.dialogue = "";
        this.isTalking = false;
        this.hasTalked = false;
        loadTexture();
        // KHÔNG cần gán lại animationManager hay renderer ở đây!
    }

    protected abstract void loadTexture();

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        if (animationManager != null) {
            animationManager.dispose();
        }
    }

    @Override
    public void takeHit(float damage) {
        // NPC không thể bị thương
    }

    @Override
    public void onCollision(Collidable other) {
        // NPC không xử lý va chạm
    }

    @Override
    public void act(float deltaTime, GameMap map) {
        // Nếu cần update state, subclass sẽ override
    }

    public Texture getTexture() {
        return this.texture;
    }

    public abstract void setTalking(boolean talking);

    public void interact(Player player) {
        if (dialogue != null && !dialogue.isEmpty()) {
            System.out.println("NPC says: " + dialogue);
        } else {
            System.out.println("NPC has nothing to say.");
        }
    }

    @Override
    public void onDeath() {
        // NPC không thể chết
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        // Nếu muốn animationManager update thì gọi thêm ở subclass
        // (Thường không cần nếu chỉ lấy animation khi render)
    }

    @Override
    public boolean isSolid() {
        return true; // NPCs are solid by default
    }
}

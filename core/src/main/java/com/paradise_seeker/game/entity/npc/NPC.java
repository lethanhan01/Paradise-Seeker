package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.entity.Collidable;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.rendering.renderer.NPCRenderer;
import com.paradise_seeker.game.rendering.renderer.NPCRendererManager;
import com.paradise_seeker.game.rendering.animations.NPCAnimationManager;

public abstract class NPC extends Character implements Collidable {
    public String dialogue;
    public boolean isTalking;
    public boolean hasTalked;
    protected Texture texture;
    private NPCRenderer renderer;
    private NPCAnimationManager animationManager;

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
        this.animationManager = new NPCAnimationManager();
        this.renderer = new NPCRendererManager(animationManager);
    }

    protected abstract void loadTexture();

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        if (renderer != null) {
            renderer.dispose();
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
        // Cập nhật trạng thái NPC nếu cần
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


    public void render(SpriteBatch batch) {
        renderer.render(this, batch);
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    public void update(float deltaTime) {
        if (animationManager != null) {
            animationManager.update(deltaTime, isTalking, false);
        }
    }
    @Override
    public  boolean isSolid() {
        return true; // NPCs are solid by default
   }
}

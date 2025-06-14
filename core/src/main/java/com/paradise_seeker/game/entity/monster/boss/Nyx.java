package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.map.GameMap;

public class Nyx extends Monster {
    // Boss-specific properties
    private float scaleMultiplier = 10f;

    public Nyx(float x, float y) {
        super(new Rectangle(x, y, 3f, 3f), 500f, 50f, 500f, 50f, 20f, 2f, x, y); // Tùy chỉnh stats phù hợp
        // Note: loadAnimations is already called in Monster constructor
        // No need to set spawnX, spawnY as they're now set in the parent constructor
        this.collisionHandler.setCleaveRange(3f);
    }

    @Override
    public void hasAnimations() {
        // WALK
        Animation<TextureRegion> walkRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/walk/phai/", "walk", 8, ".png", 0);
        Animation<TextureRegion> walkLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/walk/trai/", "walk", 8, ".png", 0);

        // IDLE
        Animation<TextureRegion> idleRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/idle/phai/", "idle", 8, ".png", 0);
        Animation<TextureRegion> idleLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/idle/trai/", "idle", 8, ".png", 0);

        // CLEAVE (có 2 đòn, mỗi đòn 8 frame)
        Animation<TextureRegion> cleaveRight = loadComboAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/cleave/phai/", "atk", 2, 8, ".png");
        Animation<TextureRegion> cleaveLeft  = loadComboAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/cleave/trai/", "atk", 2, 8, ".png");

        // TAKE HIT
        Animation<TextureRegion> takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/takehit/phai/", "takehit", 3, ".png", 0);
        Animation<TextureRegion> takeHitLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/takehit/trai/", "takehit", 3, ".png", 0);

        // DEATH
        Animation<TextureRegion> deathRight = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/death/phai/", "death", 7, ".png", 0);
        Animation<TextureRegion> deathLeft  = loadAnimation("images/Entity/characters/monsters/boss/map4/boss_3/Nyx/death/trai/", "death", 7, ".png", 0);

        // Set up all animations using the helper method from Monster
        setupAnimations(
            idleLeft, idleRight,
            walkLeft, walkRight,
            takeHitLeft, takeHitRight,
            cleaveLeft, cleaveRight,
            deathLeft, deathRight
        );
    }

    // Load animation cơ bản (frame đặt tên liên tục: walk0.png, walk1.png, ...)
    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIdx) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIdx) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    // Load animation cleave có nhiều đòn: .../atk0/atk0.png, .../atk1/atk0.png, ...
    private Animation<TextureRegion> loadComboAnimation(String folder, String comboPrefix, int comboCount, int framesPerCombo, String suffix) {
        TextureRegion[] frames = new TextureRegion[comboCount * framesPerCombo];
        int idx = 0;
        for (int combo = 0; combo < comboCount; combo++) {
            String subFolder = folder + comboPrefix + combo + "/";
            for (int frame = 0; frame < framesPerCombo; frame++) {
                String filename = subFolder + comboPrefix + frame + suffix;
                Texture texture = new Texture(Gdx.files.internal(filename));
                frames[idx++] = new TextureRegion(texture);
            }
        }
        return new Animation<>(0.12f, frames);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Add Boss3-specific death behavior if needed
        isDead = true; // Set the isDead flag to true

        bounds.set(0, 0, 0, 0); // Reset position on death


    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);
        // Add Boss3-specific collision behavior
        if (!isDead) {
            player.takeHit(20); // Assume 20 damage on collision
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add Boss3-specific update behavior here if needed
    }
}

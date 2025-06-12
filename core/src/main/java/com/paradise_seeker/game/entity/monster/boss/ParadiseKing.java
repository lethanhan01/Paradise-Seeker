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

public class ParadiseKing extends Monster {
    // Boss-specific properties
    private float scaleMultiplier = 12f;

    public ParadiseKing(float x, float y) {
        super(new Rectangle(x, y, 2f, 2f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y); // HP, speed tuỳ chỉnh theo game bạn
        // Note: loadAnimations is already called in Monster constructor
        // No need to set spawnX, spawnY as they're now set in the parent constructor
        this.collisionHandler.setCleaveRange(5f);
    }

    @Override
    public void hasAnimations() {
        // WALK (tên file là run0.png, run1.png, ...)
        Animation<TextureRegion> walkRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/walk/phai/", "run", 8, ".png", 0);
        Animation<TextureRegion> walkLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/walk/trai/", "run", 8, ".png", 0);

        // IDLE
        Animation<TextureRegion> idleRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/idle/phai/", "idle", 8, ".png", 0);
        Animation<TextureRegion> idleLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/idle/trai/", "idle", 8, ".png", 0);

        // CLEAVE: mỗi hướng có 3 đòn (atk1, atk2, atk3)
        Animation<TextureRegion> cleaveRight = loadBoss5CleaveCombo("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/cleave/phai/");
        Animation<TextureRegion> cleaveLeft  = loadBoss5CleaveCombo("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/cleave/trai/");

        // TAKEHIT
        Animation<TextureRegion> takeHitRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/takehit/phai/", "takehit", 4, ".png", 0);
        Animation<TextureRegion> takeHitLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/takehit/trai/", "takehit", 4, ".png", 0);

        // DEATH
        Animation<TextureRegion> deathRight = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/death/phai/", "death", 6, ".png", 0);
        Animation<TextureRegion> deathLeft  = loadAnimation("images/Entity/characters/monsters/boss/map5/boss_5/Paradise_king/death/trai/", "death", 6, ".png", 0);

        // Set up all animations using the helper method from Monster
        setupAnimations(
            idleLeft, idleRight,
            walkLeft, walkRight,
            takeHitLeft, takeHitRight,
            cleaveLeft, cleaveRight,
            deathLeft, deathRight
        );
    }

    // Load animation bình thường: run0.png, run1.png...
    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIdx) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = folder + prefix + (i + startIdx) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    // Load CLEAVE đặc biệt cho Boss5 (3 combo, tên file khác nhau)
    private Animation<TextureRegion> loadBoss5CleaveCombo(String folder) {
        int comboCount = 3;
        int framesPerCombo = 4;
        TextureRegion[] frames = new TextureRegion[comboCount * framesPerCombo];
        int idx = 0;
        for (int combo = 1; combo <= comboCount; combo++) {
            String subFolder = folder + "atk" + combo + "/";
            for (int frame = 0; frame < framesPerCombo; frame++) {
                String filename;
                // Phải xử lý đúng tên file cho từng combo
                if (combo == 1) {
                    // .../atk1/atk0.png, atk1.png, atk2.png, atk3.png
                    filename = subFolder + "atk" + frame + ".png";
                } else if (combo == 2) {
                    // .../atk2/atk1_0.png ... atk1_3.png
                    filename = subFolder + "atk1_" + frame + ".png";
                } else { // combo == 3
                    // .../atk3/atk2_0.png ... atk2_3.png
                    filename = subFolder + "atk2_" + frame + ".png";
                }
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
    public void isRendered(SpriteBatch batch) {
        // Use the renderer from parent class, which already handles drawing
        // No need to manually draw currentFrame as it's handled by MonsterRenderer
        super.isRendered(batch);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Add Boss5-specific death behavior if needed
    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);
        // Add Boss5-specific collision behavior
        if (!isDead) {
            player.takeHit(25); // Deal damage to the player on collision
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add Boss5-specific update behavior here if needed
    }
}

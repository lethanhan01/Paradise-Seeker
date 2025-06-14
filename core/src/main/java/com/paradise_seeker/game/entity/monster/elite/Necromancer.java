package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class Necromancer extends Monster {

    private float scaleMultiplier = 4f;


    public Necromancer(float x, float y) {
        super(new Rectangle(x, y, 1.6f, 1.8f), 250f, 40f, 200f, 60f, 25f, 2.2f, x, y);
        this.collisionHandler.setCleaveRange(2f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void hasAnimations() {
        // Idle
        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/idle/phai/idle", 8);
        Animation<TextureRegion> idleLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/idle/trai/idle", 8);

        // Run (walk)
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/run/phai/run", 8);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/run/trai/run", 8);

        // Hurt
        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/takehit/phai/hurt", 5);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/takehit/trai/hurt", 5);
        // Cleave (attack)
        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/cleave/phai/atk1_", 26); // Đổi atk1 hoặc atk2 tùy hiệu ứng
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/cleave/trai/atk1_", 26); // Đổi atk1 hoặc atk2 tùy hiệu ứng
        // Death
        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/death/phai/death", 9);
        Animation<TextureRegion> deathLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/death/trai/death", 9);

        setupAnimations(
        	idleLeftAnim, idleRightAnim,
            walkLeftAnim, walkRightAnim,
            takeHitLeftAnim, takeHitRightAnim,
            cleaveLeftAnim, cleaveRightAnim,
            deathLeftAnim, deathRightAnim
        );
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i + 1) + ".png"; // Bắt đầu từ 1
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Optional implementation for death effects
    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);

        if (!isDead) {
            // For example, apply some slow effect to player when ice monster touches them
            // player.applyStatusEffect("slow", 3.0f);  // Uncomment if you have status effects
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add IceElite-specific update behavior here if needed
    }
}

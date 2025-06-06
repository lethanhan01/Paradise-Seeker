package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;


public class IceElite extends Monster {
    private float scaleMultiplier = 5f;

    public IceElite(float x, float y) {
    	super(new Rectangle(x, y, 3f, 3f), 500f, 100f, 500f, 100f, 20f, 2f, x, y); // HP, speed, cleaveDamage, offset
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2.8f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Load all needed animations
        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/cleave/phai/1_atk_", 14);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/cleave/trai/1_atk_", 14);

        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/death/phai/death_", 16);
        Animation<TextureRegion> deathLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/death/trai/death_", 16);

        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/idle/phai/idle_", 6);
        Animation<TextureRegion> idleLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/idle/trai/idle_", 6);

        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/takehit/phai/take_hit_", 7);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/takehit/trai/take_hit_", 7);

        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/walk/phai/walk_", 10);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/ice_elite/walk/trai/walk_", 10);

        // Set all animations in the animation manager
        // The order needs to match the parameter list in setupAnimations:
        // idleLeft, idleRight, walkLeft, walkRight, takeHitLeft, takeHitRight, cleaveLeft, cleaveRight, deathLeft, deathRight
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
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Optional implementation for death effects
    }

    @Override
    public void render(SpriteBatch batch) {
        // Use the parent class's render method
        super.render(batch);
    }

    @Override
    public void onCollision(Player player) {
        // Use the parent class's collision handling
        super.onCollision(player);

        // Add ice-specific collision behavior if needed
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

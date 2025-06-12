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

public class MinotaurElite extends Monster {
    private float scaleMultiplier = 5f;

    public MinotaurElite(float x, float y) {
    	super(new Rectangle(x, y, 3f, 3f), 500f, 100f, 500f, 100f, 50f, 2f, x, y); // HP, speed, cleaveDamage, offset

        this.collisionHandler.setCleaveRange(2f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void hasAnimations() {
        // Load all needed animations
        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/cleave/phai/atk_1_", 16);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/cleave/trai/atk_1_", 16);

        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/idle/phai/idle_", 16);
        Animation<TextureRegion> idleLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/idle/trai/idle_", 16);

        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/walk/phai/walk_", 12);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/minotaur_elite/walk/trai/walk_", 12);

        // Since this entity doesn't have dedicated takehit/death animations, we'll reuse idle animations
        Animation<TextureRegion> takeHitRightAnim = idleRightAnim;
        Animation<TextureRegion> takeHitLeftAnim = idleLeftAnim;
        Animation<TextureRegion> deathRightAnim = idleRightAnim;
        Animation<TextureRegion> deathLeftAnim = idleLeftAnim;

        // Set up all animations using the helper method from Monster
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

    // Load animation: basePath + số thứ tự từ 1 đến frameCount + ".png"
    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i + 1) + ".png"; // Index bắt đầu từ 1
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true; // Set the monster as dead
        // Optional implementation for death effects
    }

    @Override
    public void isRendered(SpriteBatch batch) {
        // Use parent class's render method
        super.isRendered(batch);
    }

    @Override
    public void onCollision(Player player) {
        // Use parent class's collision handling
        super.onCollision(player);

        // Add minotaur-specific collision behavior if needed
        if (!isDead) {
            // Additional damage when colliding with player
            player.takeHit(20); // Example: strong minotaur charging damage
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add MinotaurElite-specific update behavior here if needed
    }
}

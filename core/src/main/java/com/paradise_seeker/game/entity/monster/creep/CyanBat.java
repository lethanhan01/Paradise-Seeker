package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class CyanBat extends Monster {
    private float scaleMultiplier = 2.0f;

    public CyanBat(float x, float y) {
    	super(new Rectangle(x, y, 1.2f, 1.2f), 1000f, 500f, 1000f, 500f, 50f, 1f, x, y); // HP, speed, cleaveDamage
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2.0f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Load animations with proper variable names
        Animation<TextureRegion> walkRightAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/right/walk/", "walk", 8, ".png", 1);
        Animation<TextureRegion> walkLeftAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/left/walk/", "walk", 8, ".png", 1);

        Animation<TextureRegion> idleRightAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/right/idle/", "idle", 11, ".png", 1);
        Animation<TextureRegion> idleLeftAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/left/idle/", "idle", 11, ".png", 1);

        Animation<TextureRegion> cleaveRightAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/right/atk/", "attack", 8, ".png", 1);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/left/atk/", "attack", 8, ".png", 1);

        Animation<TextureRegion> takeHitRightAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/right/hit/", "hit", 3, ".png", 1);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimationWithPadding("images/Entity/characters/monsters/creep/map2/cyan_bat/left/hit/", "hit", 3, ".png", 1);

        // Use idle animations for death since there are no dedicated death animations
        Animation<TextureRegion> deathRightAnim = idleRightAnim;
        Animation<TextureRegion> deathLeftAnim = idleLeftAnim;

        // Setup animations using the helper method from Monster
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

    private Animation<TextureRegion> loadAnimationWithPadding(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = String.format("%s%s%02d%s", folder, prefix, i + startIndex, suffix);
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true;
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

        // Add bat-specific collision behavior if needed
        if (!isDead) {
            // For example, apply some effect when bat touches player
            player.takeDamage(5); // Apply small damage
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add CyanBat-specific update behavior here if needed
    }
}

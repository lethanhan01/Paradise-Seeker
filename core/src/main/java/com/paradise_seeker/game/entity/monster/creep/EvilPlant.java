package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class EvilPlant extends Monster {
    private float scaleMultiplier = 2.0f;

    public EvilPlant(float x, float y) {
    	super(new Rectangle(x, y, 1f, 1.2f), 100f, 50f, 100f, 50f, 50f, 1f, x, y); // HP, speed, cleaveDamage
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2.5f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void hasAnimations() {
        // Load animations with proper variable names
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/idle/", "idle", 8, ".png", 1);
        Animation<TextureRegion> walkRightAnim = walkLeftAnim; // No dedicated walk animation, reuse idle

        Animation<TextureRegion> idleLeftAnim = walkLeftAnim;
        Animation<TextureRegion> idleRightAnim = walkRightAnim;

        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/atkleft/", "attack_left", 8, ".png", 1);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/atkright/", "attack_right", 8, ".png", 1);

        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/evil_plant/hit/", "hit", 3, ".png", 1);
        Animation<TextureRegion> takeHitRightAnim = takeHitLeftAnim;

        Animation<TextureRegion> deathLeftAnim = idleLeftAnim;
        Animation<TextureRegion> deathRightAnim = idleRightAnim;

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


    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = String.format("%s%s%02d%s", folder, prefix, i + startIndex, suffix);
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        statusManager.setDead(true);
    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);

        // Add EvilPlant-specific collision behavior if needed
        if (!statusManager.isDead()) {
            // For example, apply some poison effect when plant touches player
            player.takeHit(5); // Apply small poison damage
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add EvilPlant-specific update behavior here if needed
        // Plants typically don't move, so we might not need additional behavior
    }
}

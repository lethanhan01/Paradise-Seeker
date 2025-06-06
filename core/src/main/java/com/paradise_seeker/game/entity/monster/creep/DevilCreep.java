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


public class DevilCreep extends Monster {
    private float scaleMultiplier = 2f;

    public DevilCreep(float x, float y) {
    	super(new Rectangle(x, y, 1.2f, 1.6f), 100f, 50f, 100f, 50f, 10f, 2f, x, y); // HP, speed, cleaveDamage, offset
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2.5f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Cleave (attack) - 16 frame
        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_atk_", 16, ".png");
        Animation<TextureRegion> cleaveLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_atk_", 16, ".png");

        // Death - 13 frame
        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_death_", 13, ".png");
        Animation<TextureRegion> deathLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_death_", 13, ".png");

        // Idle - 5 frame
        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_idle_", 5, ".png");
        Animation<TextureRegion> idleLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_idle_", 5, ".png");

        // Move - 7 frame
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_move_", 7, ".png");
        Animation<TextureRegion> walkLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_move_", 7, ".png");

        // Take Hit - 4 frame
        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/right/vampire_creep_takeDamage_", 4, ".png");
        Animation<TextureRegion> takeHitLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/devil_creep/left/vampire_creep_takeDamage_", 4, ".png");

        // Set all animations in the animation manager
        // The order needs to match the parameter list in setupAnimations:
        // idleLeft, idleRight, walkLeft, walkRight, takeHitLeft, takeHitRight,
        // cleaveLeft, cleaveRight, deathLeft, deathRight
        setupAnimations(
            idleLeftAnim, idleRightAnim,
            walkLeftAnim, walkRightAnim,
            takeHitLeftAnim, takeHitRightAnim,
            cleaveLeftAnim, cleaveRightAnim,
            deathLeftAnim, deathRightAnim
        );
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount, String suffix) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i + 1) + suffix;
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Mark as dead
        this.isDead = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        // Use parent class's render method
        super.render(batch);
    }

    @Override
    public void onCollision(Player player) {
        // Use parent class's collision handling
        super.onCollision(player);

        // Add devil-specific collision behavior if needed
        if (!isDead) {
            // For example, apply some effect when devil touches player
            player.takeHit(10); // Apply additional damage
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add DevilCreep-specific update behavior here if needed
    }
}

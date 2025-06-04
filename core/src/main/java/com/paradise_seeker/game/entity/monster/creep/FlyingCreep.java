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


public class FlyingCreep extends Monster {
    private float scaleMultiplier = 2f;

    public FlyingCreep(float x, float y) {
    	super(new Rectangle(x, y, 1.8f, 1.8f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y); // HP, speed, cleaveDamage, offset
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
        // Walk (move) animation - 10 frames, start from 1
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/right/ball_monster_", 10);
        Animation<TextureRegion> walkLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/left/walk/ball_monster_", 10);

        // Death animation - 7 frames, start from 1
        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/right/ball_monster_death_", 7);
        Animation<TextureRegion> deathLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/left/death/ball_monster_death_", 7);

        // Reuse walk animation for idle, cleave, and takehit since they're not available
        Animation<TextureRegion> idleRightAnim = walkRightAnim;
        Animation<TextureRegion> idleLeftAnim = walkLeftAnim;
        Animation<TextureRegion> takeHitRightAnim = walkRightAnim;
        Animation<TextureRegion> takeHitLeftAnim = walkLeftAnim;
        Animation<TextureRegion> cleaveRightAnim = walkRightAnim;
        Animation<TextureRegion> cleaveLeftAnim = walkLeftAnim;

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

        // Add flying-specific collision behavior if needed
        if (!isDead) {
            player.takeDamage(8); // Apply additional damage on collision
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add FlyingCreep-specific update behavior here if needed
    }
}

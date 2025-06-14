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


public class RatCreep extends Monster {
    private float scaleMultiplier = 2f;

    public RatCreep(float x, float y) {
    	super(new Rectangle(x, y, 1.2f, 1f), 100f, 50f, 100f, 50f, 50f, 2f, x, y); // HP, speed, cleaveDamage, offset
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(1.5f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void hasAnimations() {
        // Run (walk) animation
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/run/rat_run", 6);
        Animation<TextureRegion> walkLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/run/rat_run", 6);

        // Idle animation
        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/idle/rat_idle", 6);
        Animation<TextureRegion> idleLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/idle/rat_idle", 6);

        // Attack (cleave) animation
        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/atk/rat_atk", 6);
        Animation<TextureRegion> cleaveLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/atk/rat_atk", 6);

        // Hurt (take hit) animation - single frame
        Animation<TextureRegion> takeHitRightAnim = loadSingleFrame("images/Entity/characters/monsters/creep/map3/rat_creep/right/hurt/rat-hurt-outline.png");
        Animation<TextureRegion> takeHitLeftAnim  = loadSingleFrame("images/Entity/characters/monsters/creep/map3/rat_creep/left/hurt/rat-hurt-outline.png");

        // Death animation
        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/right/death/rat_death", 6);
        Animation<TextureRegion> deathLeftAnim  = loadAnimation("images/Entity/characters/monsters/creep/map3/rat_creep/left/death/rat_death", 6);

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
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    private Animation<TextureRegion> loadSingleFrame(String filepath) {
        TextureRegion frame = new TextureRegion(new Texture(Gdx.files.internal(filepath)));
        return new Animation<>(0.12f, frame);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true;
    }

    @Override
    public void onCollision(Player player) {
        // Use parent class's collision handling
        super.onCollision(player);

        // Add rat-specific collision behavior if needed
        if (!isDead) {
            player.takeHit(7); // Apply small damage on collision
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add RatCreep-specific update behavior here if needed
    }
}

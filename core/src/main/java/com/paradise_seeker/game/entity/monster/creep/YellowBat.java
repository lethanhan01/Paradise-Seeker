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

public class YellowBat extends Monster {
    private float scaleMultiplier = 2f;

    public YellowBat(float x, float y) {
    	super(new Rectangle(x, y, 1.2f, 1.2f), 100f, 50f, 100f, 50f, 50f, 1f, x, y);
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Load all required animations
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/fly/", "fly", 7, ".png", 1);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/fly/", "fly", 7, ".png", 1);

        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/atk/", "attack", 10, ".png", 1);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/atk/", "attack", 10, ".png", 1);

        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/hit/", "hit", 3, ".png", 1);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/hit/", "hit", 3, ".png", 1);

        // Use fly animation for idle and death since they're not available
        Animation<TextureRegion> idleRightAnim = walkRightAnim;
        Animation<TextureRegion> idleLeftAnim = walkLeftAnim;
        Animation<TextureRegion> deathRightAnim = idleRightAnim;
        Animation<TextureRegion> deathLeftAnim = idleLeftAnim;

        // Setup all animations in the animation manager
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

    private Animation<TextureRegion> loadAnimation(String folder, String prefix, int frameCount, String suffix, int startIndex) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = String.format("%s%s%02d%s", folder, prefix, i + startIndex, suffix);
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void onCollision(Player player) {
        // Use parent class's collision handling
        super.onCollision(player);

        // Add bat-specific collision behavior if needed
        if (!isDead) {
            player.takeHit(5); // Apply small damage on collision
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add YellowBat-specific update behavior here if needed
    }
}

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


public class SkeletonEnemy extends Monster {
    private float scaleMultiplier = 2.1f;

    public SkeletonEnemy(float x, float y) {
    	super(new Rectangle(x, y, 1.2f, 1.2f), 100f, 50f, 100f, 50f, 50f, 1f, x, y); // HP, speed, cleaveDamage
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(1.6f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Load all required animations
        Animation<TextureRegion> walkRightAnim = loadRunAnimation();
        Animation<TextureRegion> walkLeftAnim = walkRightAnim;  // Same animation for both directions

        Animation<TextureRegion> idleRightAnim = loadIdleAnimation();
        Animation<TextureRegion> idleLeftAnim = idleRightAnim;  // Same animation for both directions

        Animation<TextureRegion> takeHitRightAnim = loadHitAnimation();
        Animation<TextureRegion> takeHitLeftAnim = takeHitRightAnim;  // Same animation for both directions

        Animation<TextureRegion> cleaveRightAnim = walkRightAnim;  // Using walk for cleave since cleave isn't available
        Animation<TextureRegion> cleaveLeftAnim = walkLeftAnim;

        Animation<TextureRegion> deathRightAnim = loadDeathAnimation();
        Animation<TextureRegion> deathLeftAnim = deathRightAnim;  // Same animation for both directions

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

    // WALK (RUN) - 12 frames: skel_enemy_run1.png ... skel_enemy_run12.png
    private Animation<TextureRegion> loadRunAnimation() {
        int frameCount = 12;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_run" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i-1] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    // IDLE: skel_enemy1.png ... skel_enemy4.png (hoặc skel_enemy_1.png ... skel_enemy_4.png)
    private Animation<TextureRegion> loadIdleAnimation() {
        int frameCount = 4;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename1 = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy" + i + ".png";
            String filename2 = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_" + i + ".png";
            Texture texture = null;
            if (Gdx.files.internal(filename1).exists()) {
                texture = new Texture(Gdx.files.internal(filename1));
            } else if (Gdx.files.internal(filename2).exists()) {
                texture = new Texture(Gdx.files.internal(filename2));
            }
            if (texture != null) {
                frames[i-1] = new TextureRegion(texture);
            } else {
                // Nếu không có ảnh, lặp lại frame đầu
                frames[i-1] = frames[0];
            }
        }
        return new Animation<>(0.14f, frames);
    }

    // HIT: skel_enemy_hit1.png ... skel_enemy_hit3.png
    private Animation<TextureRegion> loadHitAnimation() {
        int frameCount = 3;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_hit" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i-1] = new TextureRegion(texture);
        }
        return new Animation<>(0.14f, frames);
    }

    // DEATH: skel_enemy_death1.png ... skel_enemy_death13.png
    private Animation<TextureRegion> loadDeathAnimation() {
        int frameCount = 13;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = "images/Entity/characters/monsters/creep/map3/skeleton_enemy/skel_enemy_death" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i-1] = new TextureRegion(texture);
        }
        return new Animation<>(0.11f, frames);
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

        // Add skeleton-specific collision behavior if needed
        if (!isDead) {
            player.takeDamage(6); // Apply small damage on collision
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add SkeletonEnemy-specific update behavior here if needed
    }
}

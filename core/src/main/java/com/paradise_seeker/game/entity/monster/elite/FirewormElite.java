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

public class FirewormElite extends Monster {
    private float scaleMultiplier = 3.0f;

    public FirewormElite(float x, float y) {
        super(new Rectangle(x, y, 3.0f, 3.0f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y);
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(3.0f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Walk (9 frame, index 0)
        Animation<TextureRegion> walkRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_walk/right/walk", 9);
        Animation<TextureRegion> walkLeft = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_walk/left/walk", 9);

        // Idle (9 frame)
        Animation<TextureRegion> idleRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_idle/right/idle", 9);
        Animation<TextureRegion> idleLeft = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_idle/left/idle", 9);

        // Attack (16 frame)
        Animation<TextureRegion> cleaveRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_atk/right/fireworm", 16);
        Animation<TextureRegion> cleaveLeft = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_atk/left/fireworm-left/fireworm", 16);

        // Take Hit (3 frame)
        Animation<TextureRegion> takeHitRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_hit/right/hit", 3);
        Animation<TextureRegion> takeHitLeft = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_hit/left/hit", 3);

        // Death (8 frame)
        Animation<TextureRegion> deathRight = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_death/right/death", 8);
        Animation<TextureRegion> deathLeft = loadAnimation("images/Entity/characters/monsters/elite/map3/fireworm/fireworm_death/left/death", 8);

        // Set all animations in the animation manager
        setupAnimations(
            idleLeft, idleRight,
            walkLeft, walkRight,
            takeHitLeft, takeHitRight,
            cleaveLeft, cleaveRight,
            deathLeft, deathRight
        );
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + i + ".png"; // Index bắt đầu từ 0 (khớp tên file)
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Death effect can be implemented here
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

        // Add FirewormElite-specific collision behavior here if needed
        if (!isDead) {
            // Additional damage or effects when colliding with player
            player.takeDamage(15); // Example: extra fire damage
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add FirewormElite-specific update behavior here if needed
    }
}

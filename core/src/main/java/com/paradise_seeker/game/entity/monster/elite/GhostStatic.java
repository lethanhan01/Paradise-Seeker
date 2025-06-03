package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.map.GameMap;


public class GhostStatic extends Monster {
    private float scaleMultiplier = 2.0f;

    public GhostStatic(float x, float y) {
    	super(new Rectangle(x, y, 1.2f, 1.6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y);
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(1.2f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Dùng chung 1 animation cho tất cả trạng thái
        Animation<TextureRegion> ghostAnim = loadAnimation("images/Entity/characters/monsters/creep/map4/ghost_static/Dark VFX 2 (48x64)", 16);

        // Set up all animations using the helper method from Monster
        // The order needs to match the parameter list in the setupAnimations method:
        // idleLeft, idleRight, walkLeft, walkRight, takeHitLeft, takeHitRight,
        // cleaveLeft, cleaveRight, deathLeft, deathRight
        setupAnimations(
            ghostAnim, ghostAnim,   // idleLeft, idleRight
            ghostAnim, ghostAnim,   // walkLeft, walkRight
            ghostAnim, ghostAnim,   // takeHitLeft, takeHitRight
            ghostAnim, ghostAnim,   // cleaveLeft, cleaveRight
            ghostAnim, ghostAnim    // deathLeft, deathRight
        );
    }

    // Load animation: basePath + số thứ tự từ 1 đến frameCount + ".png"
    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i - 1] = new TextureRegion(texture);
        }
        return new Animation<>(0.14f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Optional implementation for death effects
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

        // Add ghost-specific collision behavior if needed
        if (!isDead) {
            // For example, apply some special effect when ghost touches player
            player.takeDamage(10); // Apply additional ghost damage
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Add GhostStatic-specific update behavior here if needed
        // Since this is a static ghost, we might not need additional behavior
    }
}

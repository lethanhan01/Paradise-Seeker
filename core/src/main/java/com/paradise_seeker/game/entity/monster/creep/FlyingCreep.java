package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.Player;


public class FlyingCreep extends Monster {

    public FlyingCreep(float x, float y) {
    	super(new Rectangle(x, y, 10f, 6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y); // HP, speed, cleaveDamage, offset
        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 1.8f;
        this.spriteHeight = 1.8f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2.0f;
        updateBounds();

    }

    public float getScaleMultiplier() {
        return 2f;
    }

    @Override
    public void loadAnimations() {
        // Walk (move) animation - 10 frames, start from 1
        walkRight = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/right/ball_monster_", 10);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/left/walk/ball_monster_", 10);

        // Idle animation = reuse walk
        idleRight = walkRight;
        idleLeft  = walkLeft;

        // Cleave (attack) = reuse walk
        cleaveRight = walkRight;
        cleaveLeft  = walkLeft;

        // Death animation - 7 frames, start from 1
        deathRight = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/right/ball_monster_death_", 7);
        deathLeft  = loadAnimation("images/Entity/characters/monsters/creep/map4/flying_creep/left/death/ball_monster_death_", 7);

        // Take Hit = reuse walk
        takeHitRight = walkRight;
        takeHitLeft = walkLeft;
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
        this.isDead = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        render(batch, null); // hoặc truyền player nếu có
    }

    public void render(SpriteBatch batch, Player player) {
        if (isDead) return;
        super.render(batch); // Fix: call the parent's render with only the batch parameter
        batch.draw(currentFrame, bounds.x, bounds.y, spriteWidth, spriteHeight);
    }

    @Override
    public void onCollision(Player player) {

    }
}

package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.Player;

public class YellowBat extends Monster {
    public YellowBat(float x, float y) {
    	super(new Rectangle(x, y, 10f, 6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y);        this.spawnX = x;
        this.spawnY = y;
        this.spriteWidth = 2f;
        this.spriteHeight = 2f;
        updateBounds(); // Đồng bộ lại bounds

        loadAnimations();
        this.currentFrame = walkRight.getKeyFrame(0f);
        this.cleaveRange = 2f; // Nhỏ hơn Boss
        updateBounds();

    }

    public float getScaleMultiplier() {
        return 2f;
    }

    @Override
    public void loadAnimations() {
        walkRight = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/fly/", "fly", 7, ".png", 1);
        walkLeft  = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/fly/", "fly", 7, ".png", 1);

        idleRight = walkRight;
        idleLeft  = walkLeft;

        cleaveRight = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/atk/", "attack", 10, ".png", 1);
        cleaveLeft  = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/atk/", "attack", 10, ".png", 1);

        takeHitRight = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/right/hit/", "hit", 3, ".png", 1);
        takeHitLeft  = loadAnimation("images/Entity/characters/monsters/creep/map2/yellow_bat/left/hit/", "hit", 3, ".png", 1);

        deathRight = idleRight;
        deathLeft  = idleLeft;
    }

    @Override
    public void onDeath() {

    }

    @Override
    public void render(SpriteBatch batch) {
        render(batch, null); // hoặc truyền player nếu có
    }

    public void render(SpriteBatch batch, Player player) {
        if (isDead) return;
        super.render(batch);
        batch.draw(currentFrame, bounds.x, bounds.y, spriteWidth, spriteHeight);
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

    }
}

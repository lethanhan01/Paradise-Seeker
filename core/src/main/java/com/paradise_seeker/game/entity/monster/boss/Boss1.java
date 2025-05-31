package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;

public class Boss1 extends Boss {
	public Boss1(float x, float y) {
		super(new Rectangle(x, y, 10f, 6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y);
	    updateBounds(); // Đồng bộ lại bounds
	    this.spawnX = x;
	    this.spawnY = y;
	    loadAnimations();
	    this.currentFrame = walkRight.getKeyFrame(0f);
	    this.cleaveRange = 5f;
	    updateBounds();

	}


    @Override
    protected void loadAnimations() {
        walkLeft  = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/walk/trai/spritesheet_left.png", 12);
        walkRight = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/walk/phai/spritesheet.png", 12);

        takeHitRight = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/phai/spritesheet.png", 5);
        takeHitLeft  = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/take_hit/trai/spritesheet.png", 5);

        idleRight = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/idle/phai/spritesheet.png", 6);
        idleLeft  = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/idle/trai/spritesheet.png", 6);

        deathLeft = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/death/trai/spritesheet.png", 20);
        deathRight = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/death/trai/spritesheet.png", 20);

        cleaveRight = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/phai/spritesheet.png", 15);
        cleaveLeft  = loadSheetAnimation("images/Entity/characters/monsters/boss/boss_1/cleave/trai/spritesheet.png", 15);
    }


    public Animation<TextureRegion> loadSheetAnimation(String sheetPath, int frameCols) {
        float frameDuration = 0.1f; // Default duration per frame
        Texture sheet = new Texture(Gdx.files.internal(sheetPath));
        int frameWidth = sheet.getWidth() / frameCols;
        int frameHeight = sheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCols];
        for (int i = 0; i < frameCols; i++) {
            frames[i] = tmp[0][i];
        }
        return new Animation<>(frameDuration, frames);
    }

    @Override
    protected float getScaleMultiplier() {
        return 10f;
    }
}

package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.Player;

import com.paradise_seeker.game.entity.Player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.render.Renderable;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;

public class Boss extends Monster {
	public Boss(Rectangle bounds,float hp, float mp, float maxHp, float maxMp, float atk, float speed, float x, float y) {
		super(bounds, hp, mp, maxHp, maxMp, atk, speed, x, y);
		// Initialize boss-specific properties here
	}
	public Boss(float x, float y) {
		this(new Rectangle(x, y, 10f, 6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y);

	}

    public Animation<TextureRegion> loadSheetAnimation(String sheetPath, int frameCols) {
		return cleaveLeft;
    }

    @Override
    public void onDeath() {
        // Implement boss-specific death logic here, or leave empty if not needed
    }

    @Override
    public void loadAnimations() {
        // Implement boss-specific animation loading here
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        // Optionally draw additional boss-specific effects here
    }

    @Override
    public void onCollision(com.paradise_seeker.game.entity.Player player) {
        // Implement boss-specific collision logic here, or leave empty if not needed
    }

}

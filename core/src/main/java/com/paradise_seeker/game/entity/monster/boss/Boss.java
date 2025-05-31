package com.paradise_seeker.game.entity.monster.boss;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.render.Renderable;
import com.badlogic.gdx.math.Rectangle;

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
	protected void loadAnimations() {
	}

	// Additional methods specific to Boss can be added here

}

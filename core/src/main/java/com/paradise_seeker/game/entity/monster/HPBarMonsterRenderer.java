package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Responsible for all rendering aspects of a monster.
 * This class handles drawing the monster sprite and its UI elements like health bars.
 */
public class HPBarMonsterRenderer {
    // Constants for HP bar rendering
    private static final float HP_BAR_WIDTH = 2.0f;
    private static final float HP_BAR_HEIGHT = 0.5f;
    private static final float HP_BAR_Y_OFFSET = 0.5f;

    private Texture[] hpBarFrames;

    public HPBarMonsterRenderer() {
        // Initialize HP bar textures
        loadHpBarTextures();
    }

    /**
     * Load all HP bar textures
     */
    private void loadHpBarTextures() {
        hpBarFrames = new Texture[30];
        for (int i = 0; i < 30; i++) {
            String filename = String.format("ui/HP_bar_monster/hpm/Hp_monster%02d.png", i);
            hpBarFrames[i] = new Texture(Gdx.files.internal(filename));
        }
    }

    /**
     * Render the monster and its UI elements
     *
     * @param batch The sprite batch to render with
     * @param bounds The monster's bounds for positioning
     * @param currentFrame The current animation frame to render
     * @param hp The monster's current HP
     * @param maxHp The monster's maximum HP
     * @param isDead Whether the monster is dead
     */
    public void render(SpriteBatch batch, Rectangle bounds, TextureRegion currentFrame,
                      float hp, float maxHp, boolean isDead) {
        // Only render if the monster is not dead or if we're still showing death animation
        if (currentFrame != null) {
            batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }

        // Only render HP bar if the monster is alive
        if (!isDead && currentFrame != null) {
            renderHpBar(batch, bounds, hp, maxHp);
        }
    }

    /**
     * Render just the monster's HP bar
     */
    private void renderHpBar(SpriteBatch batch, Rectangle bounds, float hp, float maxHp) {
    	float hpPercent = Math.max(0, Math.min(hp / (float) maxHp, 1f));
        int frameIndex = Math.round((1 - hpPercent) * 29);


        Texture hpBar = hpBarFrames[frameIndex];
        float barX = bounds.x + (bounds.width - HP_BAR_WIDTH) / 2f;
        float barY = bounds.y + bounds.height + HP_BAR_Y_OFFSET;

        batch.draw(hpBar, barX, barY, HP_BAR_WIDTH, HP_BAR_HEIGHT);
    }

    /**
     * Dispose of resources when no longer needed
     */
    public void dispose() {
        if (hpBarFrames != null) {
            for (Texture texture : hpBarFrames) {
                if (texture != null) {
                    texture.dispose();
                }
            }
        }
        
    }
}

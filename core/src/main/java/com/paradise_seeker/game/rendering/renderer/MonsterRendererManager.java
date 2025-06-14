// MonsterRendererImpl.java
package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.rendering.animations.MonsterAnimationManager;

public class MonsterRendererManager implements MonsterRenderer {
    private MonsterAnimationManager animationManager;
    private HPBarMonsterRenderer hpBarRenderer = new HPBarMonsterRenderer();

    public MonsterRendererManager(MonsterAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public void render(Monster monster, SpriteBatch batch) {
        if (monster.isDead()) {
            renderDeathAnimation(monster, batch);
        } else if (monster.isTakingHit) {
            renderHitAnimation(monster, batch);
        } else if (monster.isCleaving) {
            renderCleaveAnimation(monster, batch);
        } else if (monster.isMoving) {
            renderMovementAnimation(monster, batch);
        } else {
            renderIdleAnimation(monster, batch);
        }
    }



    private void renderDeathAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    private void renderHitAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            // Add hit flash effect
            batch.setColor(1f, 0.5f, 0.5f, 1f); // Red tint for hit
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
            batch.setColor(1f, 1f, 1f, 1f); // Reset color
        }
    }

    private void renderCleaveAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    private void renderMovementAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    private void renderIdleAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    @Override
    public void dispose() {
        if (animationManager != null) {
            animationManager.dispose();
        }
    }
}


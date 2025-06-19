// MonsterRendererImpl.java
package com.paradise_seeker.game.rendering.renderer.monsterrender;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.rendering.MonsterHPBarManager;
import com.paradise_seeker.game.rendering.animations.MonsterAnimationManager;

public class MonsterRendererManager implements MonsterRenderer {
    public MonsterAnimationManager animationManager;
    public MonsterHPBarManager hpBarRenderer = new MonsterHPBarManager();

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


    public void renderDeathAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    public void renderHitAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            // Add hit flash effect
            batch.setColor(1f, 0.5f, 0.5f, 1f); // Red tint for hit
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
            batch.setColor(1f, 1f, 1f, 1f); // Reset color
        }
    }

    public void renderCleaveAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    public void renderMovementAnimation(Monster monster, SpriteBatch batch) {
        TextureRegion currentFrame = animationManager.getCurrentFrame();
        if (currentFrame != null) {
            batch.draw(currentFrame, monster.getBounds().x, monster.getBounds().y,
                monster.getBounds().width, monster.getBounds().height);
        }
    }

    public void renderIdleAnimation(Monster monster, SpriteBatch batch) {
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


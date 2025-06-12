package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.npc.NPC;
import com.paradise_seeker.game.rendering.animations.NPCAnimationManager;

public class NPCRendererManager implements NPCRenderer {
    private NPCAnimationManager animationManager;
    private float stateTime = 0f; // Internal state time for animations
    public float indicatorX;
    public float indicatorY;

    public NPCRendererManager(NPCAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public void render(NPC npc, SpriteBatch batch) {
        if (npc.isTalking) {
            renderTalkingAnimation(npc, batch);
        } else {
            renderIdleAnimation(npc, batch);
        }

        // Render dialogue indicator if talking
        if (npc.isTalking) {
            renderDialogueIndicator(npc, batch);
        }
    }


    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    private void renderTalkingAnimation(NPC npc, SpriteBatch batch) {
        // Use idle animation for talking (can be customized)
        Animation<TextureRegion> idleAnimation = animationManager.getIdleAnimation("down");
        if (idleAnimation != null) {
            TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
            if (currentFrame != null) {
                batch.draw(currentFrame, npc.getBounds().x, npc.getBounds().y,
                    npc.getBounds().width, npc.getBounds().height);
            }
        } else {
            // Fallback to texture if no animation
            renderTexture(npc, batch);
        }
    }

    private void renderIdleAnimation(NPC npc, SpriteBatch batch) {
        Animation<TextureRegion> idleAnimation = animationManager.getIdleAnimation("down");
        if (idleAnimation != null) {
            TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
            if (currentFrame != null) {
                batch.draw(currentFrame, npc.getBounds().x, npc.getBounds().y,
                    npc.getBounds().width, npc.getBounds().height);
            }
        } else {
            // Fallback to texture if no animation
            renderTexture(npc, batch);
        }
    }

    private void renderTexture(NPC npc, SpriteBatch batch) {
        // Access texture through reflection or getter method
        // This is a fallback when no animation is available
        try {
            java.lang.reflect.Field textureField = npc.getClass().getDeclaredField("texture");
            textureField.setAccessible(true);
            Texture texture = (Texture) textureField.get(npc);
            if (texture != null) {
                batch.draw(texture, npc.getBounds().x, npc.getBounds().y,
                    npc.getBounds().width, npc.getBounds().height);
            }
        } catch (Exception e) {

        }
    }

    private void renderDialogueIndicator(NPC npc, SpriteBatch batch) {

        indicatorX = npc.getBounds().x + npc.getBounds().width / 2 - 0.1f;
        indicatorY = npc.getBounds().y + npc.getBounds().height + 0.1f;
    }

    @Override
    public void dispose() {
        // Dispose of any resources if needed
        if (animationManager != null) {
            animationManager.dispose();
        }
    }
}

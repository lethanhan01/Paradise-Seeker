package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.npc.NPC;
import com.paradise_seeker.game.entity.npc.gipsy.Gipsy;
import com.paradise_seeker.game.entity.npc.gipsy.GipsyStateManager;
import com.paradise_seeker.game.rendering.animations.NPCAnimationManager;

public class NPCRendererManager implements NPCRenderer {
    public float indicatorX;
    public float indicatorY;
    public NPCAnimationManager animationManager;

    public NPCRendererManager(NPCAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public void render(NPC npc, SpriteBatch batch) {
        NPCAnimationManager animationManager = npc.getAnimationManager();

        Animation<TextureRegion> anim;
        float stateTime = 0f;

        // Chỉ ví dụ với Gipsy, nếu nhiều loại NPC thì dùng instance of kiểm tra
        if (npc instanceof Gipsy) {
            Gipsy gipsy = (Gipsy) npc;
            GipsyStateManager stateManager = gipsy.stateManager;
            if (stateManager.isOpeningChest()) {
                anim = animationManager.getOpenChestAnimation();
                stateTime = stateManager.getOpenChestStateTime();
            } else if (stateManager.isTalking()) {
                anim = animationManager.getAttackAnimation("down");
                stateTime = stateManager.getTalkingStateTime();
            } else {
                anim = animationManager.getIdleAnimation("down");
                stateTime = stateManager.getIdleStateTime();
            }
        } else {
            // fallback: dùng idle
            anim = animationManager.getIdleAnimation("down");
            stateTime = 0f;
        }

        TextureRegion frame = (anim != null) ? anim.getKeyFrame(stateTime, true) : null;
        if (frame != null) {
            batch.draw(frame, npc.getBounds().x, npc.getBounds().y,
                npc.getBounds().width, npc.getBounds().height);
        } else {
            renderTexture(npc, batch); // fallback nếu không có animation
        }

        if (npc.isTalking) {
            renderDialogueIndicator(npc, batch);
        }
    }

    private void renderTexture(NPC npc, SpriteBatch batch) {
        Texture texture = npc.getTexture();
        if (texture != null) {
            batch.draw(texture, npc.getBounds().x, npc.getBounds().y,
                npc.getBounds().width, npc.getBounds().height);
        }
    }

    private void renderDialogueIndicator(NPC npc, SpriteBatch batch) {
        indicatorX = npc.getBounds().x + npc.getBounds().width / 2 - 0.1f;
        indicatorY = npc.getBounds().y + npc.getBounds().height + 0.1f;
        // vẽ hiệu ứng chỉ báo tại đây nếu cần (icon, ...), ví dụ: batch.draw(indicatorTexture, indicatorX, indicatorY, ...);
    }

    @Override
    public void dispose() {
        // Nếu có quản lý resource ở đây thì dispose
    }
}

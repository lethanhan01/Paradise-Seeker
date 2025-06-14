package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.npc.NPC;
import com.paradise_seeker.game.rendering.animations.NPCAnimationManager;

public class NPCRendererManager implements NPCRenderer {
    public float indicatorX;
    public float indicatorY;

    @Override
    public void render(NPC npc, SpriteBatch batch) {
        NPCAnimationManager animationManager = npc.getAnimationManager();
        float stateTime = npc.getStateTime();

        Animation<TextureRegion> anim;
        // Chọn animation dựa vào trạng thái
        if (npc.isTalking) {
            anim = animationManager.getAttackAnimation("down");
        } else {
            anim = animationManager.getIdleAnimation("down");
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

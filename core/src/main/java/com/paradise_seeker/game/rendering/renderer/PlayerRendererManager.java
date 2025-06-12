package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.rendering.animations.PlayerAnimationManager;

public class PlayerRendererManager implements PlayerRenderer {
	public PlayerAnimationManager animationManager;

    public PlayerRendererManager(PlayerAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public void render(Player player, SpriteBatch batch) {
        if (player.isDead()) {
            renderDeath(player, batch);
            return;
        }

        if (player.isHit) {
            renderHit(player, batch);
            return;
        }

        if (player.isAttacking) {
            renderAttack(player, batch);
        } else if (player.isMoving()) {
            renderMovement(player, batch);
        } else {
            renderIdle(player, batch);
        }

        renderSmoke(player, batch);
    }

    @Override
    public void renderMovement(Player player, SpriteBatch batch) {
        Animation<TextureRegion> runAnimation = animationManager.getRunAnimation(player.getDirection());
        TextureRegion currentFrame = runAnimation.getKeyFrame(player.getStateTime(), true);
        batch.draw(currentFrame, player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
    }

    @Override
    public void renderIdle(Player player, SpriteBatch batch) {
        Animation<TextureRegion> idleAnimation = animationManager.getIdleAnimation(player.getDirection());
        TextureRegion currentFrame = idleAnimation.getKeyFrame(player.getStateTime(), true);
        batch.draw(currentFrame, player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
    }

    @Override
    public void renderAttack(Player player, SpriteBatch batch) {
        Animation<TextureRegion> attackAnimation = animationManager.getAttackAnimation(player.getDirection());
        TextureRegion currentFrame = attackAnimation.getKeyFrame(player.getStateTime(), false);

        // Phóng to khi tấn công
        float scaledWidth = player.getBounds().width * 3.0f;
        float scaledHeight = player.getBounds().height * 3.0f;
        float drawX = player.getBounds().x - (scaledWidth - player.getBounds().width) / 2f;
        float drawY = player.getBounds().y - (scaledHeight - player.getBounds().height) / 2f;

        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
    }

    @Override
    public void renderHit(Player player, SpriteBatch batch) {
        Animation<TextureRegion> hitAnimation = animationManager.getHitAnimation(player.getDirection());
        TextureRegion currentFrame = hitAnimation != null
            ? hitAnimation.getKeyFrame(player.getStateTime(), false)
            : null;

        if (hitAnimation != null && hitAnimation.isAnimationFinished(player.getStateTime())) {
            player.isHit = false; // Reset trạng thái khi hoạt ảnh kết thúc
        }
        if (currentFrame == null) {
            System.out.println("WARNING: Player renderHit frame NULL! Using idle frame.");
            currentFrame = animationManager.getIdleAnimation(player.getDirection()).getKeyFrame(0, true);
        }
        batch.draw(currentFrame, player.getBounds().x, player.getBounds().y,
            player.getBounds().width, player.getBounds().height);
    }

    @Override
    public void renderDeath(Player player, SpriteBatch batch) {

    }

    @Override
    public void renderSmoke(Player player, SpriteBatch batch) {

    }
}

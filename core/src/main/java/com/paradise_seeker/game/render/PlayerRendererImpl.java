package com.paradise_seeker.game.render;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.animation.PlayerAnimationManager;
import com.paradise_seeker.game.entity.Player;

public class PlayerRendererImpl implements PlayerRenderer{
    private PlayerAnimationManager animationManager;

    // Shield textures
    private Texture shieldDown, shieldUp, shieldLeft, shieldRight;

    public PlayerRendererImpl(PlayerAnimationManager animationManager) {
        this.animationManager = animationManager;
        loadShieldTextures();
    }

    private void loadShieldTextures() {
        shieldDown = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_down.png"));
        shieldUp = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_up.png"));
        shieldLeft = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_left.png"));
        shieldRight = new Texture(Gdx.files.internal("images/Entity/characters/player/char_shielded_static_right.png"));
    }

    @Override
    public void render(Player player, SpriteBatch batch) {
        if (player.isDead()) {
            renderDeath(player, batch);
            return;
        }

        if (player.isShielding() && player.isShieldedHit()) {
            renderShieldedHit(player, batch);
            return;
        }

        if (player.isHit) {
            renderHit(player, batch);
            return;
        }

        if (player.isShielding()) {
            renderShield(player, batch);
            return;
        }

        if (player.isAttacking) {
            renderAttack(player, batch);
        } else if (player.isPushing()) {
            renderPushing(player, batch);
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
    public void renderShield(Player player, SpriteBatch batch) {
        Animation<TextureRegion> shieldAnimation = animationManager.getShieldAnimation(player.getDirection());
        TextureRegion currentFrame = shieldAnimation.getKeyFrame(player.getStateTime(), false);

        // Phóng to khi giơ khiên
        float scaledWidth = player.getBounds().width * 3.0f;
        float scaledHeight = player.getBounds().height * 3.0f;
        float drawX = player.getBounds().x - (scaledWidth - player.getBounds().width) / 2f;
        float drawY = player.getBounds().y - (scaledHeight - player.getBounds().height) / 2f;
        batch.draw(currentFrame, drawX, drawY, scaledWidth, scaledHeight);
    }


    @Override
    public void renderPushing(Player player, SpriteBatch batch) {

    }

    @Override
    public void renderHit(Player player, SpriteBatch batch) {

    }

    @Override
    public void renderShieldedHit(Player player, SpriteBatch batch) {

    }

    @Override
    public void renderDeath(Player player, SpriteBatch batch) {

    }

    @Override
    public void renderSmoke(Player player, SpriteBatch batch) {

    }
}

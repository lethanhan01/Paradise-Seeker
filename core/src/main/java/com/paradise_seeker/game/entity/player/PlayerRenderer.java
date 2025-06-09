package com.paradise_seeker.game.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface PlayerRenderer {

    void render(Player player, SpriteBatch batch);

    void renderMovement(Player player, SpriteBatch batch);

    void renderIdle(Player player, SpriteBatch batch);

    void renderAttack(Player player, SpriteBatch batch);

    void renderShield(Player player, SpriteBatch batch);

    void renderHit(Player player, SpriteBatch batch);

    void renderShieldedHit(Player player, SpriteBatch batch);

    void renderDeath(Player player, SpriteBatch batch);

    void renderSmoke(Player player, SpriteBatch batch);
}

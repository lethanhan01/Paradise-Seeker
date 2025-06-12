package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.player.Player;

public interface PlayerRenderer {

    void render(Player player, SpriteBatch batch);

    void renderMovement(Player player, SpriteBatch batch);

    void renderIdle(Player player, SpriteBatch batch);

    void renderAttack(Player player, SpriteBatch batch);

    void renderHit(Player player, SpriteBatch batch);

    void renderDeath(Player player, SpriteBatch batch);

    void renderSmoke(Player player, SpriteBatch batch);
}

package com.paradise_seeker.game.rendering.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.monster.Monster;

public interface MonsterRenderer {
    void render(Monster monster, SpriteBatch batch);
    void dispose();
}


package com.paradise_seeker.game.rendering.renderer.npcrender;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.npc.NPC;

public interface NPCRenderer {
    void render(NPC npc, SpriteBatch batch);
    void dispose();
}

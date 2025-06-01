package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.Texture;
import com.paradise_seeker.game.entity.Player;

public class MonsterRenderer {
    public void render(Monster monster, SpriteBatch batch) {
        // Vẽ quái
        TextureRegion frame = monster.getCurrentFrame();
        if (frame != null) {
            batch.draw(frame, monster.getBounds().x, monster.getBounds().y, monster.getBounds().width, monster.getBounds().height);
        }
        // Vẽ thanh máu
        renderHpBar(monster, batch);
    }

    private void renderHpBar(Monster monster, SpriteBatch batch) {
        float percent = monster.getHp() / monster.getMaxHp();
        int frameIdx = (int)(percent * (monster.hpBarFrames.length - 1));
        frameIdx = Math.max(0, Math.min(frameIdx, monster.hpBarFrames.length - 1));
        Texture hpBar = monster.hpBarFrames[frameIdx];
        float barX = monster.getBounds().x + (monster.getBounds().width - Monster.HP_BAR_WIDTH) / 2f;
        float barY = monster.getBounds().y + monster.getBounds().height + Monster.HP_BAR_Y_OFFSET;
        batch.draw(hpBar, barX, barY, Monster.HP_BAR_WIDTH, Monster.HP_BAR_HEIGHT);
    }
}


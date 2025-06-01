package com.paradise_seeker.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.paradise_seeker.game.animation.PlayerAnimationManager;

public class SmokeManager {
    private final List<Smoke> smokes = new LinkedList<>();

    public void addSmoke(float x, float y) {
        smokes.add(new Smoke(x, y));
    }

    public void update(float deltaTime, PlayerAnimationManager animationManager) {
        Iterator<Smoke> iter = smokes.iterator();
        while (iter.hasNext()) {
            Smoke s = iter.next();
            s.stateTime += deltaTime;
            if (animationManager.getSmokeAnimation().isAnimationFinished(s.stateTime)) {
                iter.remove();
            }
        }
    }

    public void render(SpriteBatch batch, PlayerAnimationManager animationManager) {
        Animation<TextureRegion> smokeAnim = animationManager.getSmokeAnimation();
        for (Smoke s : smokes) {
            TextureRegion frame = smokeAnim.getKeyFrame(s.stateTime, false);
            batch.draw(frame, s.x, s.y, 1f, 1f);
        }
    }
}


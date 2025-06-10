package com.paradise_seeker.game.entity.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DashTrailManager {
    private final List<DashTrail> smokes = new LinkedList<>();

    public void addSmoke(float x, float y) {
        smokes.add(new DashTrail(x, y));
    }

    public void update(float deltaTime, PlayerAnimationManagerImpl animationManager) {
        Iterator<DashTrail> iter = smokes.iterator();
        while (iter.hasNext()) {
            DashTrail s = iter.next();
            s.stateTime += deltaTime;
            if (animationManager.getSmokeAnimation().isAnimationFinished(s.stateTime)) {
                iter.remove();
            }
        }
    }

    public void render(SpriteBatch batch, PlayerAnimationManagerImpl animationManager) {
        Animation<TextureRegion> smokeAnim = animationManager.getSmokeAnimation();
        for (DashTrail s : smokes) {
            TextureRegion frame = smokeAnim.getKeyFrame(s.stateTime, false);
            batch.draw(frame, s.x, s.y, 1f, 1f);
        }
    }
}


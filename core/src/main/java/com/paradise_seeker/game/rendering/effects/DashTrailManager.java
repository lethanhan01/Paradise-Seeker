package com.paradise_seeker.game.rendering.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.rendering.animations.PlayerAnimationManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DashTrailManager {
    public float dashCooldown = 0f;
    public float dashDistance = 2f;
    public float speedMultiplier = 1f;
    public float dashTimer = 0f;

    private final List<DashTrail> smokes = new LinkedList<>();

    public void addSmoke(float x, float y) {
        smokes.add(new DashTrail(x, y));
    }

    public void update(float deltaTime, PlayerAnimationManager animationManager) {
        Iterator<DashTrail> iter = smokes.iterator();
        while (iter.hasNext()) {
            DashTrail s = iter.next();
            s.stateTime += deltaTime;
            if (animationManager.getSmokeAnimation().isAnimationFinished(s.stateTime)) {
                iter.remove();
            }
        }
    }

    public void render(SpriteBatch batch, PlayerAnimationManager animationManager) {
        Animation<TextureRegion> smokeAnim = animationManager.getSmokeAnimation();
        for (DashTrail s : smokes) {
            TextureRegion frame = smokeAnim.getKeyFrame(s.stateTime, false);
            batch.draw(frame, s.x, s.y, 1f, 1f);
        }
    }
    public float getDashTimer() {
        return dashTimer;
    }
    public void setDashTimer(float timer) {
        this.dashTimer = timer;
    }

	public float getDashCooldown() {
		return dashCooldown;
	}

	public float getDashDistance() {
		return dashDistance;
	}

}


package com.paradise_seeker.game.entity.monster.projectile;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.rendering.Renderable;

public class MonsterProjectile implements Renderable {
    private float x, y, vx, vy;
    private final float speed = 8f;
    public boolean finished = false;
    private final Animation<TextureRegion> anim;
    private final TextureRegion texture;
    private float stateTime = 0f;
    private final Rectangle hitbox;
    private boolean hitDealt = false;

    // Constructor cho animation (giữ nguyên)
    public MonsterProjectile(float x, float y, float dx, float dy, Animation<TextureRegion> anim) {
        this.x = x;
        this.y = y;
        this.vx = dx * speed;
        this.vy = dy * speed;
        this.anim = anim;
        this.texture = null;
        this.hitbox = new Rectangle(x, y, 1.1f, 1.1f);
    }
    // Constructor cho texture tĩnh
    public MonsterProjectile(float x, float y, float dx, float dy, TextureRegion texture) {
        this.x = x;
        this.y = y;
        this.vx = dx * speed;
        this.vy = dy * speed;
        this.anim = null;
        this.texture = texture;
        this.hitbox = new Rectangle(x, y, 1.1f, 1.1f);
    }


    public void update(float dt, GameMap map, Player player) {
        if (finished) return;
        x += vx * dt;
        y += vy * dt;
        stateTime += dt;
        hitbox.setPosition(x, y);
        // Va chạm player
        if (!hitDealt && hitbox.overlaps(player.getBounds()) && !player.statusManager.isInvulnerable()) {
            player.takeHit(38); // Damage skill
            hitDealt = true;
            finished = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, x, y, hitbox.width, hitbox.height);
        } else if (anim != null) {
            TextureRegion frame = anim.getKeyFrame(stateTime, true);
            batch.draw(frame, x, y, hitbox.width, hitbox.height);
        }
    }

}


package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CyclopBoss extends Monster {

    // Boss stats, có thể chỉnh lại cho phù hợp
    private final float scaleMultiplier = 2.5f;

    // List đạn skill đang bay
    private final List<Projectile> projectiles = new ArrayList<>();

    public CyclopBoss(float x, float y) {
        super(new Rectangle(x, y, 2.0f, 2.0f), 1500f, 100f, 1500f, 100f, 100f, 1.5f, x, y);
        this.collisionHandler.setCleaveRange(10.0f);
    }

    public float getScaleMultiplier() { return scaleMultiplier; }

    // ---- Animation loader ----
    @Override
    public void loadAnimations() {
        // Dãy ảnh đúng folder, không dùng cleave, skill = cleave!
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/walk/phai/", 15, 26);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/walk/trai/", 165, 176);

        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/idle/phai/", 0, 14);
        Animation<TextureRegion> idleLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/idle/trai/", 150, 164);

        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/phai/", 120, 125);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/trai/", 270, 275);

        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/takehit/phai/", 75, 79);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/takehit/trai/", 225, 229);

        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/death/phai/", 90, 98);
        Animation<TextureRegion> deathLeftAnim = loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/death/trai/", 240, 248);

        setupAnimations(
            idleLeftAnim, idleRightAnim,
            walkLeftAnim, walkRightAnim,
            takeHitLeftAnim, takeHitRightAnim,
            cleaveLeftAnim, cleaveRightAnim,
            deathLeftAnim, deathRightAnim
        );
    }

    // Helper load animation
    private Animation<TextureRegion> loadAnimation(String folder, int startIdx, int endIdx) {
        int frameCount = endIdx - startIdx + 1;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String file = folder + "cyclop" + (startIdx + i) + ".png";
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(file)));
        }
        return new Animation<>(0.13f, frames);
    }

    // --- Main update/act ---
    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Update đạn skill
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(deltaTime, map, player);
            if (p.finished) iter.remove();
        }
    }

    @Override
    public void isRendered(SpriteBatch batch) {
        super.isRendered(batch);
        for (Projectile p : projectiles) p.render(batch);
    }

    // -- Cơ chế cleave: override lại cleave để tạo skill projectile --
    @Override
    public void cleave(Player player) {
        // Chỉ trigger animation như thường, damage cận chiến do collisionHandler lo
        super.cleave(player);
        // Tạo skill projectile (1 viên bay về phía player lúc tấn công)
        float cx = bounds.x + bounds.width / 2f;
        float cy = bounds.y + bounds.height / 2f;
        float px = player.getBounds().x + player.getBounds().width / 2f;
        float py = player.getBounds().y + player.getBounds().height / 2f;
        float dx = px - cx, dy = py - cy;
        float len = (float) Math.sqrt(dx*dx + dy*dy);
        dx /= len; dy /= len;
        Animation<TextureRegion> projAnim = isFacingRight()
            ? loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/phai/projectile/", 135, 142)
            : loadAnimation("images/Entity/characters/monsters/boss/map2/boss_2/cyclop/skill/trai/projectile/", 285, 292);
        projectiles.add(new Projectile(cx, cy, dx, dy, projAnim, this));
    }

    // Nếu boss bị player chạm/cấn thì vẫn gọi collisionHandler như thường
    @Override
    public void onCollision(Player player) {
        super.onCollision(player); // logic trong MonsterCollisionHandler sẽ xử lý
    }

    // --- Class đạn skill của boss ---
    public static class Projectile {
        private float x, y, vx, vy;
        private final float speed = 8f;
        private boolean finished = false;
        private final Animation<TextureRegion> anim;
        private float stateTime = 0f;
        private final Rectangle hitbox;
        private final CyclopBoss owner;
        private boolean hitDealt = false;

        public Projectile(float x, float y, float dx, float dy, Animation<TextureRegion> anim, CyclopBoss owner) {
            this.x = x;
            this.y = y;
            this.vx = dx * speed;
            this.vy = dy * speed;
            this.anim = anim;
            this.owner = owner;
            this.hitbox = new Rectangle(x, y, 1.1f, 1.1f);
        }

        public void update(float dt, GameMap map, Player player) {
            if (finished) return;
            x += vx * dt;
            y += vy * dt;
            stateTime += dt;
            hitbox.setPosition(x, y);

            // Va chạm player
            if (!hitDealt && hitbox.overlaps(player.getBounds()) && !player.isInvulnerable()) {
                player.takeHit(38); // Damage skill
                hitDealt = true;
                finished = true;
            }
        }

        public void render(SpriteBatch batch) {
            TextureRegion frame = anim.getKeyFrame(stateTime, true);
            batch.draw(frame, x, y, hitbox.width, hitbox.height);
        }
    }
}

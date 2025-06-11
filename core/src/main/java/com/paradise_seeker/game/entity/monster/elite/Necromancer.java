package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class Necromancer extends Monster {

    private Array<Projectile> projectiles = new Array<>();
    private boolean cleaveDamageDealtToPlayer = false;
    private float scaleMultiplier = 4f;

    public Necromancer(float x, float y) {
        super(new Rectangle(x, y, 1.6f, 1.8f), 250f, 40f, 200f, 60f, 25f, 2.2f, x, y);
        this.collisionHandler.setCleaveRange(2.2f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Idle
        Animation<TextureRegion> idleAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/idle/idle", 8);
        // Run (walk)
        Animation<TextureRegion> runAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/run/run", 8);
        // Hurt
        Animation<TextureRegion> hurtAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/hurt/hurt", 5);
        // Cleave (attack)
        Animation<TextureRegion> cleaveAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/cleave/atk1", 13); // Đổi atk1 hoặc atk2 tùy hiệu ứng
        // Death
        Animation<TextureRegion> deathAnim = loadAnimation("images/Entity/characters/monsters/elite/map4/necromancer/death/death", 9);

        // Không có hướng trái/phải riêng biệt, sẽ dùng chung mọi hướng
        setupAnimations(
            idleAnim, idleAnim,        // idleLeft, idleRight
            runAnim, runAnim,          // walkLeft, walkRight
            hurtAnim, hurtAnim,        // takeHitLeft, takeHitRight
            cleaveAnim, cleaveAnim,    // cleaveLeft, cleaveRight
            deathAnim, deathAnim       // deathLeft, deathRight
        );
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = basePath + " (" + i + ").png"; // Đúng định dạng file của bạn: atk1 (1).png, run1.png, v.v.
            if (basePath.contains("idle") || basePath.contains("run") || basePath.contains("hurt")) {
                // idle1.png, run1.png, hurt1.png (không có " (i)")
                filename = basePath.substring(0, basePath.lastIndexOf("/")+1) + basePath.substring(basePath.lastIndexOf("/")+1, basePath.length()-1) + i + ".png";
            }
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i - 1] = new TextureRegion(texture);
        }
        return new Animation<>(0.11f, frames);
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);

        // Update projectiles
        for (Projectile p : projectiles) {
            p.update(deltaTime, player);
        }
        for (int i = projectiles.size - 1; i >= 0; i--) {
            if (!projectiles.get(i).active) {
                projectiles.removeIndex(i);
            }
        }

        // Cleave: bắn spell ra khi đủ phần trăm animation
        if (animationManager.isCleaving() && !cleaveDamageDealtToPlayer) {
            float cleaveProgress = animationManager.cleaveTimer / animationManager.cleaveDuration;
            if (cleaveProgress >= 0.23f) { // Tuỳ thời điểm bạn muốn spawn spell
                spawnProjectile(player);
                cleaveDamageDealtToPlayer = true;
            }
        } else if (!animationManager.isCleaving()) {
            cleaveDamageDealtToPlayer = false;
        }
    }

    private void spawnProjectile(Player player) {
        if (player == null) return;
        float bulletX = bounds.x + bounds.width / 2f;
        float bulletY = bounds.y + bounds.height / 2f;
        float targetX = bounds.x + bounds.width / 2;
        float targetY = bounds.y + bounds.height / 2;
        projectiles.add(new Projectile(bulletX, bulletY, targetX, targetY));
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true;
        projectiles.clear();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        for (Projectile p : projectiles) {
            p.render(batch);
        }
    }

    // ==== PROJECTILE - FIRE SPELL ====
    private class Projectile {
        Rectangle bounds;
        Animation<TextureRegion> fireSpellAnim;
        float stateTime = 0f;
        float speed = 3.7f;
        boolean active = true;
        float velocityX;
        float velocityY;

        Projectile(float x, float y, float targetX, float targetY) {
            fireSpellAnim = loadFireSpellAnimation();
            bounds = new Rectangle(x, y, 0.6f, 0.6f);

            float dx = targetX - x;
            float dy = targetY - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len != 0) {
                velocityX = (dx / len) * speed;
                velocityY = (dy / len) * speed;
            } else {
                velocityX = speed;
                velocityY = 0;
            }
        }

        Animation<TextureRegion> loadFireSpellAnimation() {
            TextureRegion[] frames = new TextureRegion[28];
            for (int i = 0; i < 28; i++) {
                String filename = "images/Entity/characters/monsters/elite/map4/necromancer/fire_spell/firespell" + i + ".png";
                Texture texture = new Texture(Gdx.files.internal(filename));
                frames[i] = new TextureRegion(texture);
            }
            return new Animation<>(0.04f, frames);
        }

        void update(float deltaTime, Player player) {
            if (!active) return;
            stateTime += deltaTime;
            bounds.x += velocityX * deltaTime;
            bounds.y += velocityY * deltaTime;
            if (player != null && bounds.overlaps(bounds)) {
                player.takeHit(30); // Sát thương spell
                active = false;
            }
            // Spell hết animation thì biến mất
            if (stateTime > fireSpellAnim.getAnimationDuration()) {
                active = false;
            }
        }

        void render(SpriteBatch batch) {
            if (!active) return;
            TextureRegion keyFrame = fireSpellAnim.getKeyFrame(stateTime, true);
            batch.draw(keyFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
}

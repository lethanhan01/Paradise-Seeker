package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.map.GameMap;

public class FlyingDemon extends Monster {

    private Array<Projectile> projectiles = new Array<>();
    private boolean cleaveDamageDealtToPlayer = false;
    private float scaleMultiplier = 5f;

    public FlyingDemon(float x, float y) {
        super(new Rectangle(x, y, 10f, 6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y);
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2.5f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void loadAnimations() {
        // Fly (walk)
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/fly/flyingdemon_fly", 4);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/fly/flyingdemon_fly", 4);

        // Idle
        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/idle/flyingdemon_idle", 4);
        Animation<TextureRegion> idleLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/idle/flyingdemon_idle", 4);

        // Attack (cleave)
        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/atk/flyingdemon_atk", 8);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/atk/flyingdemon_atk", 8);

        // Get hit
        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/gethit/flyingdemon_gethit", 4);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/gethit/flyingdemon_gethit", 4);

        // Death
        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/death/flyingdemon_death", 7);
        Animation<TextureRegion> deathLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/death/flyingdemon_death", 7);

        // Set all animations in the animation manager
        // The order needs to match the parameter list in the setupAnimations method:
        // idleLeft, idleRight, walkLeft, walkRight, takeHitLeft, takeHitRight,
        // cleaveLeft, cleaveRight, deathLeft, deathRight
        setupAnimations(
            idleLeftAnim, idleRightAnim,
            walkLeftAnim, walkRightAnim,
            takeHitLeftAnim, takeHitRightAnim,
            cleaveLeftAnim, cleaveRightAnim,
            deathLeftAnim, deathRightAnim
        );
    }

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + i + ".png"; // Index từ 0 -> ĐÚNG với file thực tế
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    @Override
    public void update(float deltaTime, Player player, GameMap map) {
        super.update(deltaTime, player, map);

        // Cập nhật các projectile với player
        for (Projectile p : projectiles) {
            p.update(deltaTime, player);
        }
        // Xóa projectile không active
        for (int i = projectiles.size - 1; i >= 0; i--) {
            if (!projectiles.get(i).active) {
                projectiles.removeIndex(i);
            }
        }

        // Check for cleave animation to create projectile
        if (animationManager.isCleaving() && !cleaveDamageDealtToPlayer) {
            // Only spawn projectile when 20% into the cleave animation
            float cleaveProgress = animationManager.cleaveTimer / animationManager.cleaveDuration;
            if (cleaveProgress >= 0.2f) {
                spawnProjectile(player);
                cleaveDamageDealtToPlayer = true;
            }
        } else if (!animationManager.isCleaving()) {
            // Reset the flag when not cleaving
            cleaveDamageDealtToPlayer = false;
        }
    }

    private void spawnProjectile(Player player) {
        if (player == null) return;
        float bulletX = isFacingRight() ? bounds.x + bounds.width : bounds.x - 0.5f;
        float bulletY = bounds.y + bounds.height / 2 - 0.25f;
        float targetX = player.bounds.x + player.bounds.width / 2;
        float targetY = player.bounds.y + player.bounds.height / 2;
        projectiles.add(new Projectile(bulletX, bulletY, targetX, targetY, isFacingRight()));
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true;
        // Clear projectiles when the demon dies
        projectiles.clear();
    }

    @Override
    public void render(SpriteBatch batch) {
        // Call parent render
        super.render(batch);

        // Render projectiles
        for (Projectile p : projectiles) {
            p.render(batch);
        }
    }

    private class Projectile {
        Rectangle bounds;
        TextureRegion texture;
        float speed = 3.5f;
        boolean active = true;
        float velocityX;
        float velocityY;

        Projectile(float x, float y, float targetX, float targetY, boolean facingRight) {
            String path = facingRight ?
                "images/Entity/characters/monsters/elite/map3/flying_demon/right/atk/projectile_right.png" :
                "images/Entity/characters/monsters/elite/map3/flying_demon/left/atk/projectile.png";
            texture = new TextureRegion(new Texture(Gdx.files.internal(path)));
            bounds = new Rectangle(x, y, 0.5f, 0.5f);

            // Tính hướng bay
            float dx = targetX - x;
            float dy = targetY - y;
            float len = (float) Math.sqrt(dx * dx + dy * dy);
            if (len != 0) {
                velocityX = (dx / len) * speed;
                velocityY = (dy / len) * speed;
            } else {
                velocityX = speed * (facingRight ? 1 : -1);
                velocityY = 0;
            }
        }

        void update(float deltaTime, Player player) {
            if (!active) return;
            bounds.x += velocityX * deltaTime;
            bounds.y += velocityY * deltaTime;
            if (player != null && bounds.overlaps(player.bounds)) {
                player.takeDamage(15); // Sát thương đạn
                active = false;
            }
        }

        void render(SpriteBatch batch) {
            if (active) batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
}

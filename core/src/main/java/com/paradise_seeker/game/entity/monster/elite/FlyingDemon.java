package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;
import com.paradise_seeker.game.entity.monster.HasProjectiles;
import com.paradise_seeker.game.entity.monster.MonsterProjectile;
import java.util.ArrayList;
import java.util.List;

public class FlyingDemon extends Monster implements HasProjectiles {

    private final List<MonsterProjectile> projectiles = new ArrayList<>();
    private boolean cleaveDamageDealtToPlayer = false;
    private final float scaleMultiplier = 5f;
    private final TextureRegion projectileTextureRight;
    private final TextureRegion projectileTextureLeft;


    public FlyingDemon(float x, float y) {
        super(new Rectangle(x, y, 2f, 1.6f), 200f, 50f, 200f, 50f, 20f, 2f, x, y);
        this.collisionHandler.setCleaveRange(2.5f);

        projectileTextureRight = new TextureRegion(new Texture(Gdx.files.internal(
            "images/Entity/characters/monsters/elite/map3/flying_demon/right/atk/projectile_right.png")));
        projectileTextureLeft = new TextureRegion(new Texture(Gdx.files.internal(
            "images/Entity/characters/monsters/elite/map3/flying_demon/left/atk/projectile.png")));
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void hasAnimations() {
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/fly/flyingdemon_fly", 4);
        Animation<TextureRegion> walkLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/fly/flyingdemon_fly", 4);

        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/idle/flyingdemon_idle", 4);
        Animation<TextureRegion> idleLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/idle/flyingdemon_idle", 4);

        Animation<TextureRegion> cleaveRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/atk/flyingdemon_atk", 8);
        Animation<TextureRegion> cleaveLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/atk/flyingdemon_atk", 8);

        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/gethit/flyingdemon_gethit", 4);
        Animation<TextureRegion> takeHitLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/gethit/flyingdemon_gethit", 4);

        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/right/death/flyingdemon_death", 7);
        Animation<TextureRegion> deathLeftAnim = loadAnimation("images/Entity/characters/monsters/elite/map3/flying_demon/left/death/flyingdemon_death", 7);

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
            String filename = basePath + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.12f, frames);
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);

        // Update and clean up projectiles
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            MonsterProjectile p = projectiles.get(i);
            p.update(deltaTime, map, player);
            if (p.finished) {
                projectiles.remove(i);
            }
        }

        // Check for cleave animation to create projectile
        if (animationManager.isCleaving() && !cleaveDamageDealtToPlayer) {
            float cleaveProgress = animationManager.cleaveTimer / animationManager.cleaveDuration;
            if (cleaveProgress >= 0.2f) {
                spawnProjectile(player);
                cleaveDamageDealtToPlayer = true;
            }
        } else if (!animationManager.isCleaving()) {
            cleaveDamageDealtToPlayer = false;
        }
    }

    private void spawnProjectile(Player player) {
        if (player == null) return;
        Rectangle bounds = getBounds();
        float bulletX = isFacingRight() ? bounds.x + bounds.width : bounds.x - 0.5f;
        float bulletY = bounds.y + bounds.height / 2 - 0.25f;
        Rectangle playerBounds = player.getBounds();
        float targetX = playerBounds.x + playerBounds.width / 2;
        float targetY = playerBounds.y + playerBounds.height / 2;
        float dx = targetX - bulletX;
        float dy = targetY - bulletY;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0) len = 1f; // tránh chia 0
        dx /= len; dy /= len;
        // Chọn anim phù hợp hướng
        TextureRegion projTexture = isFacingRight() ? projectileTextureRight : projectileTextureLeft;
        projectiles.add(new MonsterProjectile(bulletX, bulletY, dx, dy, projTexture));
    }

    @Override
    public void onDeath() {
        super.onDeath();
        projectiles.clear();
    }

    @Override
    public List<MonsterProjectile> getProjectiles() {
        return projectiles;
    }
}

package com.paradise_seeker.game.entity.monster.creep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class SkeletonEnemy extends Monster {
    private float scaleMultiplier = 2.1f;

    public SkeletonEnemy(float x, float y) {
        super(new Rectangle(x, y, 1.2f, 1.2f), 100f, 50f, 100f, 50f, 50f, 1f, x, y);
        this.collisionHandler.setCleaveRange(1.6f);
    }

    public float getScaleMultiplier() {
        return scaleMultiplier;
    }

    @Override
    public void hasAnimations() {
        Animation<TextureRegion> walkLeftAnim  = loadRunAnimation("left");
        Animation<TextureRegion> walkRightAnim = loadRunAnimation("right");

        Animation<TextureRegion> idleLeftAnim  = loadIdleAnimation("left");
        Animation<TextureRegion> idleRightAnim = loadIdleAnimation("right");

        Animation<TextureRegion> takeHitLeftAnim  = loadHitAnimation("left");
        Animation<TextureRegion> takeHitRightAnim = loadHitAnimation("right");

        Animation<TextureRegion> cleaveLeftAnim  = loadAttackAnimation("left");  // attack == cleave
        Animation<TextureRegion> cleaveRightAnim = loadAttackAnimation("right");

        Animation<TextureRegion> deathLeftAnim  = loadDeathAnimation("left");
        Animation<TextureRegion> deathRightAnim = loadDeathAnimation("right");

        setupAnimations(
                idleLeftAnim, idleRightAnim,
                walkLeftAnim, walkRightAnim,
                takeHitLeftAnim, takeHitRightAnim,
                cleaveLeftAnim, cleaveRightAnim,
                deathLeftAnim, deathRightAnim
        );
    }

    private Animation<TextureRegion> loadRunAnimation(String dir) {
        int frameCount = 12;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = String.format(
                "images/Entity/characters/monsters/creep/map3/skeleton_enemy/%s/run/skel_enemy_run%d.png", dir, i);
            frames[i - 1] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.12f, frames);
    }

    private Animation<TextureRegion> loadIdleAnimation(String dir) {
        int frameCount = 4;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = String.format(
                "images/Entity/characters/monsters/creep/map3/skeleton_enemy/%s/idle/skel_enemy%d.png", dir, i);
            frames[i - 1] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.14f, frames);
    }

    private Animation<TextureRegion> loadHitAnimation(String dir) {
        int frameCount = 3;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = String.format(
                "images/Entity/characters/monsters/creep/map3/skeleton_enemy/%s/takehit/skel_enemy_hit%d.png", dir, i);
            frames[i - 1] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.14f, frames);
    }

    private Animation<TextureRegion> loadAttackAnimation(String dir) {
        int frameCount = 13; // Đủ 13 frame
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = String.format(
                "images/Entity/characters/monsters/creep/map3/skeleton_enemy/%s/atk/skel_enemy_%d.png", dir, i);
            frames[i - 1] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.11f, frames);
    }

    private Animation<TextureRegion> loadDeathAnimation(String dir) {
        int frameCount = 13;
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 1; i <= frameCount; i++) {
            String filename = String.format(
                "images/Entity/characters/monsters/creep/map3/skeleton_enemy/%s/death/skel_enemy_death%d.png", dir, i);
            frames[i - 1] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.11f, frames);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true;
    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);
        if (!isDead) {
            player.takeHit(6);
        }
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);
        // Bổ sung hành vi cho SkeletonEnemy nếu cần
    }
}

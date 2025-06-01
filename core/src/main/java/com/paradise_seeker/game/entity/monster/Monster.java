package com.paradise_seeker.game.entity.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.collision.Collidable;
import com.paradise_seeker.game.entity.Character;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.render.Renderable;
import com.paradise_seeker.game.map.GameMap;


public abstract class Monster extends Character implements Renderable, Collidable {

    public boolean isAggressive = false;
    public float aggroTimer = 0f;
    public final float AGGRO_DURATION = 5f;
    public boolean isDead = false;

    public float spriteWidth;
    public float spriteHeight;

    public float spawnX;
    public float spawnY;

    public boolean pendingCleaveHit = false;
    public boolean cleaveDamageDealt = false;
    public Animation<TextureRegion> cleaveLeft, cleaveRight;
    public boolean isCleaving = false;
    public float cleaveDuration = 1.2f;
    public float cleaveTimer = 0f;
    protected float cleaveRange; // ❗Không gán = 4f

    public Animation<TextureRegion> deathLeft, deathRight;

    private MonsterRenderer renderer;
    private MonsterAI ai;

    public Animation<TextureRegion> idleLeft, idleRight;
    public Animation<TextureRegion> walkLeft, walkRight;
    public Animation<TextureRegion> takeHitLeft, takeHitRight;
    public TextureRegion currentFrame;
    public boolean facingRight = true;
    public boolean isTakingHit = false;
    public float takeHitDuration = 0.5f;
    public float takeHitTimer = 0f;
    public float stateTime = 0f;
    public Vector2 lastPosition = new Vector2();
    public boolean isMoving = false;
    public float OFFSET;
    public Texture[] hpBarFrames;
    public static final float HP_BAR_WIDTH = 2.0f;
    public static final float HP_BAR_HEIGHT = 0.5f;
    public static final float HP_BAR_Y_OFFSET = 0.5f;

    public Monster(Rectangle bounds,float hp, float mp, float maxHp, float maxMp, float atk, float speed, float x, float y ) {
        super(bounds, hp, mp, maxHp, maxMp, atk, speed, x, y);
        this.spriteWidth = bounds.width;
        this.spriteHeight = bounds.height;
        this.spawnX = x;
        this.spawnY = y;
        loadAnimations();
        this.lastPosition.set(x, y);
        this.currentFrame = null;
        hpBarFrames = new Texture[30];
        for (int i = 0; i < 30; i++) {
            String filename = String.format("ui/HP_bar_monster/hpm/Hp_monster%02d.png", i);
            hpBarFrames[i] = new Texture(Gdx.files.internal(filename));
        }
        this.bounds = new Rectangle(x, y, spriteWidth, spriteHeight);
        this.renderer = new MonsterRenderer();
        this.ai = new MonsterAI(this);
    }

    public void update(float deltaTime, Player player, GameMap map) {
        if (player == null || player.isDead) return;
        if (isDead) {
            stateTime += deltaTime;
            return;
        }
        ai.update(deltaTime, player, map);
        isMoving = lastPosition.dst(bounds.x, bounds.y) > 0.0001f;
        lastPosition.set(bounds.x, bounds.y);
        stateTime += deltaTime;
        if (isTakingHit) {
            takeHitTimer -= deltaTime;
            if (takeHitTimer <= 0f) isTakingHit = false;
        }
        // ...existing code...
    }

    public void render(SpriteBatch batch) {
        renderer.render(this, batch);
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isDead() {
        return isDead;
    }

    public void updateBounds() {
        bounds.setSize(spriteWidth, spriteHeight);
    }

    public float getHp() {
        return hp;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public float getAtk() {
        return atk;
    }

    public float getSpeed() {
        return speed;
    }

    public void takeDamage(float damage) {
        if (isDead) return;
        this.hp = Math.max(0, this.hp - damage);
        this.isTakingHit = true;
        this.takeHitTimer = this.takeHitDuration;
        if (this.hp == 0) {
            this.isDead = true;
            // Optionally, trigger death animation or logic here
        }
    }

    /**
     * Handle collision with a player. This applies damage to the player when they
     * collide with this monster, unless the player is invulnerable or shielding.
     */
    @Override
    public void onCollision(Collidable other) {
        if (other instanceof Player) {
            Player player = (Player) other;
            if (!isDead && !player.isInvulnerable()) {
                // Calculate damage based on monster's attack value
                int damage = (int) this.atk;

                // If player is shielding, reduce damage and handle shield effects
                if (player.isShielding) {
                    player.isShieldedHit = true;
                    damage = Math.max(1, damage / 2); // Reduce damage if shielding
                } else {
                    player.isHit = true;
                    player.stateTime = 0;
                }

                // Apply damage to player unless they're invulnerable from a recent hit
                if (!player.isInvulnerable()) {
                    player.takeDamage(damage);
                }
            }
        }
    }

    /**
     * Handle specific collision logic with player.
     * Subclasses can override this for custom behavior.
     */
    public void onCollision(Player player) {
        // Default implementation uses the Collidable version
        onCollision((Collidable)player);
    }

    // Add abstract method for animation loading
    public abstract void loadAnimations();
}

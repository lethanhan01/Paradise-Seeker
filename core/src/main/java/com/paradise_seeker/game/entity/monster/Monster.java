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
	
	protected GameMap gameMap;
	
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
    public boolean deathPlayed = false;
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
    public static final float HP_BAR_WIDTH = 2.0f; // Độ dài cố định (ví dụ: 2 đơn vị game)
    public static final float HP_BAR_HEIGHT = 0.5f; // Độ dày thanh HP
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

    public void update(float deltaTime) {
        if (gameMap.getPlayer() == null || gameMap.getPlayer().isDead) return;

        if (isDead) {
            stateTime += deltaTime;
            return;
        }

        isMoving = lastPosition.dst(bounds.x, bounds.y) > 0.0001f;
        lastPosition.set(bounds.x, bounds.y);

        stateTime += deltaTime;
        if (isTakingHit) {
            takeHitTimer -= deltaTime;
            if (takeHitTimer <= 0f) isTakingHit = false;
        }

        if (isCleaving) {
            if (pendingCleaveHit && !cleaveDamageDealt && stateTime >= cleaveDuration * 0.8f) {
                if (gameMap.getPlayer() != null && !gameMap.getPlayer().isDead) {
                    float dx = gameMap.getPlayer().bounds.x + gameMap.getPlayer().bounds.width / 2 - (bounds.x + bounds.width / 2);
                    float dy = gameMap.getPlayer().bounds.y + gameMap.getPlayer().bounds.height / 2 - (bounds.y + bounds.height / 2);
                    float dist = (float) Math.sqrt(dx * dx + dy * dy);
                    if (dist <= cleaveRange)gameMap.getPlayer().takeDamage(atk);
                }
                cleaveDamageDealt = true;
            }
            cleaveTimer -= deltaTime;
            if (cleaveTimer <= 0f) {
                isCleaving = false;
                pendingCleaveHit = false;
                cleaveDamageDealt = false;
            }
        }

        if (isAggressive) {
            aggroTimer -= deltaTime;
            if (aggroTimer <= 0f && !isNearPlayer()) isAggressive = false;
            else {
                if (!isCleaving) {
                    approachPlayer(deltaTime);
                }
                if (isPlayerInCleaveRange() && cleaveTimer <= 0f) attackPlayer();
                return;
            }
        }

        returnToSpawn(deltaTime);
    }

    private boolean isPlayerInCleaveRange() {
    	if (gameMap.getPlayer() == null || gameMap.getPlayer().isDead) return false;
    	
        float bossCenterX = bounds.x + bounds.width / 2;
        float bossCenterY = bounds.y + bounds.height / 2;
        float playerCenterX = gameMap.getPlayer().bounds.x + gameMap.getPlayer().bounds.width / 2;
        float playerCenterY = gameMap.getPlayer().bounds.y + gameMap.getPlayer().bounds.height / 2;

        float dx = bossCenterX - playerCenterX;
        float dy = bossCenterY - playerCenterY;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= cleaveRange;
    }

    public void attackPlayer() {
        useCleaveSkill();
    }

    public void takeDamage(float damage) {
        if (damage > 0) {
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
            hp = Math.max(0, hp - damage);
            if (hp == 0 && !isDead) onDeath();
            else {
                isTakingHit = true;
                takeHitTimer = takeHitDuration;
                if (!isCleaving) {
                    stateTime = 0f;
                }
            }
        }
    }

    public void useCleaveSkill() {
        if (!isCleaving && !isDead) {
            isCleaving = true;
            cleaveTimer = cleaveDuration;
            stateTime = 0f;
            isAggressive = true;
            aggroTimer = AGGRO_DURATION;
            pendingCleaveHit = true;
            cleaveDamageDealt = false;
        }
    }
    @Override
    public void render(SpriteBatch batch) {
    	if (gameMap.getPlayer() != null) facingRight = gameMap.getPlayer().getBounds().x > bounds.x; //sửa chỗ này để tránh lỗi null
        if (isDead) currentFrame = (facingRight ? deathRight : deathLeft).getKeyFrame(stateTime, false);
        else if (isCleaving) currentFrame = (facingRight ? cleaveRight : cleaveLeft).getKeyFrame(stateTime, false);
        else if (isTakingHit) currentFrame = (facingRight ? takeHitRight : takeHitLeft).getKeyFrame(stateTime, false);
        else if (!isMoving) currentFrame = (facingRight ? idleRight : idleLeft).getKeyFrame(stateTime, true);
        else currentFrame = (facingRight ? walkRight : walkLeft).getKeyFrame(stateTime, true);

        if (gameMap.getPlayer() != null) facingRight = gameMap.getPlayer().bounds.x > bounds.x;

        // Tính kích thước vẽ
        float scale = getScaleMultiplier();
        float playerArea = gameMap.getPlayer().bounds.width * gameMap.getPlayer().bounds.height;

        float frameAspect = currentFrame.getRegionWidth() / (float) currentFrame.getRegionHeight();

        float drawWidth, drawHeight;
        if (frameAspect >= 1f) { // Ngang
            drawWidth = gameMap.getPlayer().bounds.width * scale;
            drawHeight = drawWidth / frameAspect;
        } else { // Dọc
            drawHeight = gameMap.getPlayer().bounds.height * scale;
            drawWidth = drawHeight * frameAspect;
        }

        // Căn giữa
        float drawX = bounds.x + bounds.width / 2 - drawWidth / 2;
        float drawY = bounds.y + bounds.height / 2 - drawHeight / 2 + OFFSET;

        // HP bar
        float hpPercent = Math.max(0, Math.min(hp / (float) maxHp, 1f));
        int frameIndex = Math.round((1 - hpPercent) * 29);
        float hpBarX = bounds.x + (bounds.width - HP_BAR_WIDTH) / 2f;
        float hpBarY = bounds.y + bounds.height + HP_BAR_Y_OFFSET;

        batch.draw(hpBarFrames[frameIndex], hpBarX, hpBarY, HP_BAR_WIDTH, HP_BAR_HEIGHT);
        batch.draw(currentFrame, drawX, drawY, drawWidth, drawHeight);
    }


    protected float getScaleMultiplier() {
        return 1f; // Mặc định
    }


    public void onDeath() {
        isDead = true;
        stateTime = 0f;
        deathPlayed = true;
        bounds.set(0, 0, 0, 0); // Reset bounds để không va chạm nữa
    }


    @Override
    public void onCollision(Player player) {
        // Đẩy player ra xa khỏi monster
        float dx = gameMap.getPlayer().bounds.x + gameMap.getPlayer().bounds.width / 2 - (bounds.x + bounds.width / 2);
        float dy = gameMap.getPlayer().bounds.y + gameMap.getPlayer().bounds.height / 2 - (bounds.y + bounds.height / 2);
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist == 0) return;

        float pushAmount = 1f;
        gameMap.getPlayer().bounds.x += (dx / dist) * pushAmount;
        gameMap.getPlayer().bounds.y += (dy / dist) * pushAmount;

        // Cũng nên xử lý hiệu ứng bị hit
        if (gameMap.getPlayer().isShielding) {
        	gameMap.getPlayer().isShieldedHit = true;
        } else {
        	gameMap.getPlayer().isHit = true;
        	gameMap.getPlayer().stateTime = 0;
        }
    }


    private boolean isNearPlayer() {
        float thisCenterX = bounds.x + bounds.width / 2;
        float thisCenterY = bounds.y + bounds.height / 2;
        float playerCenterX = gameMap.getPlayer().bounds.x + gameMap.getPlayer().bounds.width / 2;
        float playerCenterY = gameMap.getPlayer().bounds.y + gameMap.getPlayer().bounds.height / 2;
        float dx = thisCenterX - playerCenterX;
        float dy = thisCenterY - playerCenterY;
        return Math.sqrt(dx * dx + dy * dy) < Math.max(bounds.width, bounds.height);
    }

    private void approachPlayer(float deltaTime) {
        float dx = gameMap.getPlayer().bounds.x - bounds.x;
        float dy = gameMap.getPlayer().bounds.y - bounds.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 0.1f) {
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            bounds.x += moveX;
            bounds.y += moveY;
        }
    }

    private void returnToSpawn(float deltaTime) {
        float dx = spawnX - bounds.x;
        float dy = spawnY - bounds.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance > 0.1f) {
            float moveX = (dx / distance) * speed * deltaTime;
            float moveY = (dy / distance) * speed * deltaTime;
            bounds.x += moveX;
            bounds.y += moveY;
        }
    }

    protected abstract void loadAnimations();
    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }

}

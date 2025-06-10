package com.paradise_seeker.game.entity.monster.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;

public class FireDemon extends Monster {
    private float cleaveRange = 5f;

    public FireDemon(float x, float y) {
        super(new Rectangle(x, y, 3.5f, 3.5f), 500f, 50f, 500f, 50f, 50f, 2f, x, y);
        this.collisionHandler.setCleaveRange(cleaveRange);
    }

    @Override
    public void loadAnimations() {
        // WALK: 1-12
        Animation<TextureRegion> walkRight = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/walk/phai/demon_walk_", 1, 12);
        Animation<TextureRegion> walkLeft  = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/walk/trai/demon_walk_", 1, 12);

        // IDLE: Phai (demon_idle_1 (0).png đến demon_idle_1 (5).png), Trai (demon_idle_1.png ... demon_idle_6.png)
        Animation<TextureRegion> idleRight = loadIdlePhai();
        Animation<TextureRegion> idleLeft  = loadIdleTrai();

        // CLEAVE: 1-15
        Animation<TextureRegion> cleaveRight = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/cleave/phai/demon_cleave_", 1, 15);
        Animation<TextureRegion> cleaveLeft  = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/cleave/trai/demon_cleave_", 1, 15);

        // TAKE HIT: 1-5
        Animation<TextureRegion> takeHitRight = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/take_hit/phai/demon_take_hit_", 1, 5);
        Animation<TextureRegion> takeHitLeft  = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/take_hit/trai/demon_take_hit_", 1, 5);

        // DEATH: 1-22
        Animation<TextureRegion> deathRight = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/death/phai/demon_death_", 1, 22);
        Animation<TextureRegion> deathLeft  = loadFrameSequence("images/Entity/characters/monsters/boss/map3/FireDemon/death/trai/demon_death_", 1, 22);

        setupAnimations(
                idleLeft, idleRight,
                walkLeft, walkRight,
                takeHitLeft, takeHitRight,
                cleaveLeft, cleaveRight,
                deathLeft, deathRight
        );
    }

    // Helper cho các action bình thường (tên liên tục)
    private Animation<TextureRegion> loadFrameSequence(String basePath, int from, int to) {
        int count = to - from + 1;
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 0; i < count; i++) {
            String filename = basePath + (i + from) + ".png";
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.12f, frames);
    }

    // idle/phai (tên lẻ kiểu demon_idle_1 (0).png ...)
    private Animation<TextureRegion> loadIdlePhai() {
        int count = 6;
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 0; i < count; i++) {
            String filename = "images/Entity/characters/monsters/boss/map3/FireDemon/idle/phai/demon_idle_1 (" + i + ").png";
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.12f, frames);
    }
    // idle/trai (tên demon_idle_1.png ... demon_idle_6.png)
    private Animation<TextureRegion> loadIdleTrai() {
        int count = 6;
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 1; i <= count; i++) {
            String filename = "images/Entity/characters/monsters/boss/map3/FireDemon/idle/trai/demon_idle_" + i + ".png";
            frames[i - 1] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        return new Animation<>(0.12f, frames);
    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);
        // Thêm logic boss nếu cần
    }

    @Override
    public void onDeath() {
        super.onDeath();
        // Logic boss khi chết
    }
}


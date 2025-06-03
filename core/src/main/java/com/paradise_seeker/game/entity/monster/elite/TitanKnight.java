package com.paradise_seeker.game.entity.monster.elite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.map.GameMap;

import java.util.ArrayList;
import java.util.Iterator;

public class TitanKnight extends Monster {

    // --- Animation fields ---
    private boolean cleaveTurn = true;
    private Animation<TextureRegion> cleaveAnim, spellAnim;
    private Animation<TextureRegion> fireSpellAnim;
    private final ArrayList<FireSpell> fireSpells = new ArrayList<>();

    private final float spellDamage = 20f; // Damage khi spell trúng player
    private final float fireSpellSpeed = 5f; // Tốc độ spell bay

    public TitanKnight(float x, float y) {
        super(new Rectangle(x, y, 10f, 6f), 1000f, 500f, 1000f, 500f, 50f, 2f, x, y); // HP, speed, cleaveDamage, offset
        // Note: spawnX and spawnY are now set in the parent constructor
        // Note: loadAnimations is already called in Monster constructor

        // Set cleave range through the collision handler
        this.collisionHandler.setCleaveRange(2.5f);
    }

    public float getScaleMultiplier() {
        return 2.4f;
    }

    @Override
    public void loadAnimations() {
        // Load all animations
        Animation<TextureRegion> walkRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/run/run", 8);
        Animation<TextureRegion> walkLeftAnim = walkRightAnim; // Same animation for both directions

        Animation<TextureRegion> idleRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/idle/idle", 8);
        Animation<TextureRegion> idleLeftAnim = idleRightAnim; // Same animation for both directions

        Animation<TextureRegion> takeHitRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/hurt/hurt", 5);
        Animation<TextureRegion> takeHitLeftAnim = takeHitRightAnim; // Same animation for both directions

        // CLEAVE = ảnh trong folder cleave (gồm 26 frame, atk1 & atk2)
        cleaveAnim = loadCleaveAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/cleave/");
        // SPELL = spell1.png ... spell17.png
        spellAnim = loadSpellAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/spell/spell", 17);

        Animation<TextureRegion> cleaveRightAnim = cleaveAnim;
        Animation<TextureRegion> cleaveLeftAnim = cleaveAnim; // Same animation for both directions

        Animation<TextureRegion> deathRightAnim = loadAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/death/death", 9);
        Animation<TextureRegion> deathLeftAnim = deathRightAnim; // Same animation for both directions

        // Spell projectile
        fireSpellAnim = loadFireSpellAnimation("images/Entity/characters/monsters/elite/map2/titan_knight/fire_spell/firespell", 28);

        // Set all animations in the animation manager
        // The order needs to match the parameter list in setupAnimations:
        // idleLeft, idleRight, walkLeft, walkRight, takeHitLeft, takeHitRight, cleaveLeft, cleaveRight, deathLeft, deathRight
        setupAnimations(
            idleLeftAnim, idleRightAnim,
            walkLeftAnim, walkRightAnim,
            takeHitLeftAnim, takeHitRightAnim,
            cleaveLeftAnim, cleaveRightAnim,
            deathLeftAnim, deathRightAnim
        );
    }

    // --- Logic: mỗi lần cast cleave gọi hàm này để chuyển xen kẽ cleave <-> spell ---
    public void switchCleaveTypeAndCastSpell(Player player) {
        cleaveTurn = !cleaveTurn;

        Animation<TextureRegion> leftAnim, rightAnim;
        if (cleaveTurn) {
            leftAnim = cleaveAnim;
            rightAnim = cleaveAnim;
        } else {
            leftAnim = spellAnim;
            rightAnim = spellAnim;
            castFireSpell(player); // Khi sang spell, bắn spell về phía player
        }

        // Update animations in animation manager
        animationManager.setCleaveAnimations(leftAnim, rightAnim);
    }

    // Tạo spell projectile bay đến vị trí player tại thời điểm cast
    private void castFireSpell(Player player) {
        if (player == null) return;
        Vector2 start = new Vector2(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        Vector2 target = new Vector2(player.getBounds().x + player.getBounds().width / 2,
                                    player.getBounds().y + player.getBounds().height / 2);
        fireSpells.add(new FireSpell(start, target));
    }

    // Update spell logic (movement, collision)
    public void updateFireSpells(float deltaTime, Player player) {
        if (player == null) return;

        Iterator<FireSpell> it = fireSpells.iterator();
        while (it.hasNext()) {
            FireSpell spell = it.next();
            spell.update(deltaTime);

            // Check collision with player
            if (spell.bounds.overlaps(player.getBounds())) {
                player.takeDamage((int)spellDamage);
                it.remove();
            }
            // Remove if out of screen
            else if (spell.lifetime > 5f) {
                it.remove();
            }
        }
    }

    // Render fire spells
    private void renderFireSpells(SpriteBatch batch, float stateTime) {
        for (FireSpell spell : fireSpells) {
            TextureRegion frame = fireSpellAnim.getKeyFrame(stateTime, true);
            batch.draw(frame, spell.bounds.x, spell.bounds.y, spell.bounds.width, spell.bounds.height);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        // Additionally render fire spells
        renderFireSpells(batch, animationManager.getStateTime());
    }

    @Override
    public void act(float deltaTime, Player player, GameMap map) {
        super.act(deltaTime, player, map);

        // Update fire spells
        updateFireSpells(deltaTime, player);

        // Check for special attack patterns
        if (animationManager.isCleaving() && !cleaveTurn) {
            // If in spell animation mode, we might want special behavior
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        this.isDead = true;

        // Clear all fire spells
        fireSpells.clear();
    }

    @Override
    public void onCollision(Player player) {
        super.onCollision(player);

        // Add titan-specific collision behavior if needed
        if (!isDead) {
            player.takeDamage(10); // Apply additional melee damage on collision
        }
    }

    // Helper Animation loading methods

    private Animation<TextureRegion> loadAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i+1) + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    private Animation<TextureRegion> loadCleaveAnimation(String folder) {
        // Assume total frames is 26, loading from atk1_1.png through atk2_13.png
        int frameCount = 26;
        TextureRegion[] frames = new TextureRegion[frameCount];
        int idx = 0;

        // Load atk1 (first 13 frames)
        for (int i = 1; i <= 13; i++) {
            String filename = folder + "atk1_" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[idx++] = new TextureRegion(texture);
        }

        // Load atk2 (next 13 frames)
        for (int i = 1; i <= 13; i++) {
            String filename = folder + "atk2_" + i + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[idx++] = new TextureRegion(texture);
        }

        return new Animation<>(0.08f, frames);
    }

    private Animation<TextureRegion> loadSpellAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i+1) + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.1f, frames);
    }

    private Animation<TextureRegion> loadFireSpellAnimation(String basePath, int frameCount) {
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            String filename = basePath + (i+1) + ".png";
            Texture texture = new Texture(Gdx.files.internal(filename));
            frames[i] = new TextureRegion(texture);
        }
        return new Animation<>(0.05f, frames);
    }

    // Inner class for fire spells
    private class FireSpell {
        Rectangle bounds;
        Vector2 velocity;
        float lifetime = 0;

        FireSpell(Vector2 start, Vector2 target) {
            bounds = new Rectangle(start.x - 1.0f, start.y - 1.0f, 2.0f, 2.0f);

            // Calculate direction to target
            Vector2 direction = new Vector2(target.x - start.x, target.y - start.y).nor();
            velocity = direction.scl(fireSpellSpeed);
        }

        void update(float deltaTime) {
            // Move spell
            bounds.x += velocity.x * deltaTime;
            bounds.y += velocity.y * deltaTime;
            lifetime += deltaTime;
        }
    }
}

package com.paradise_seeker.game.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class NPCAnimationManager {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> talkAnimation;
    private Animation<TextureRegion> openChestAnimation;
    private Animation<TextureRegion> chestOpenedAnimation;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion currentFrame;
    private float stateTime;

    public NPCAnimationManager() {
        loadAnimations();
        currentAnimation = idleAnimation;
        currentFrame = currentAnimation.getKeyFrame(0f);
        stateTime = 0f;
    }

    public void loadAnimations() {
        loadIdleAnimation();
        loadTalkAnimation();
        loadOpenChestAnimation();
        loadChestOpenedAnimation();
    }

    private void loadIdleAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 120; i <= 130; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act3/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        idleAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void loadTalkAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 10; i <= 19; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act1/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        talkAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        talkAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private void loadOpenChestAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 131; i <= 137; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act4/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        openChestAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        openChestAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    private void loadChestOpenedAnimation() {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 140; i <= 145; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act5/npc" + i + ".png";
            try {
                Texture texture = new Texture(Gdx.files.internal(path));
                texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Handle missing textures silently
            }
        }
        chestOpenedAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        chestOpenedAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float deltaTime, boolean isOpeningChest, boolean isChestOpened) {
        stateTime += deltaTime;
        currentFrame = currentAnimation.getKeyFrame(stateTime);

        // Handle chest opening animation completion
        if (isOpeningChest && openChestAnimation.isAnimationFinished(stateTime)) {
            setChestOpenedAnimation();
        }
    }

    public void setTalkingAnimation() {
        currentAnimation = talkAnimation;
        stateTime = 0f;
    }

    public void setIdleAnimation() {
        currentAnimation = idleAnimation;
        stateTime = 0f;
    }

    public void setOpenChestAnimation() {
        currentAnimation = openChestAnimation;
        stateTime = 0f;
    }

    public void setChestOpenedAnimation() {
        currentAnimation = chestOpenedAnimation;
        stateTime = 0f;
    }

    public void render(SpriteBatch batch, Rectangle bounds, float spriteWidth, float spriteHeight) {
        if (currentFrame != null) {
            batch.draw(currentFrame, bounds.x, bounds.y, spriteWidth, spriteHeight);
        }
    }

    public boolean isAnimationFinished() {
        return currentAnimation.isAnimationFinished(stateTime);
    }

    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }
}

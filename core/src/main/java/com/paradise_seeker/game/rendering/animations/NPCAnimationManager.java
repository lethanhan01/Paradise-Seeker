package com.paradise_seeker.game.rendering.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.paradise_seeker.game.rendering.AnimationManager;
import java.util.ArrayList;
import java.util.List;

public class NPCAnimationManager implements AnimationManager {
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
        for (int i = 130; i <= 140; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act4/npc" + i + ".png";
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
        for (int i = 130; i <= 140; i++) {
            String path = "images/Entity/characters/NPCs/npc1/act5/npc" + i + ".png";
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

    public void update(float deltaTime, boolean isTalking, boolean isChestOpened) {
        stateTime += deltaTime;

        // Update animation based on state
        if (isTalking) {
            setTalkingAnimation();
        } else if (isChestOpened) {
            setChestOpenedAnimation();
        } else {
            setIdleAnimation();
        }

        currentFrame = currentAnimation.getKeyFrame(stateTime);
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

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean isAnimationFinished() {
        return currentAnimation.isAnimationFinished(stateTime);
    }

    @Override
    public Animation<TextureRegion> getRunAnimation(String direction) {
        return idleAnimation;
    }

    @Override
    public Animation<TextureRegion> getIdleAnimation(String direction) {
        return idleAnimation;
    }

    @Override
    public Animation<TextureRegion> getAttackAnimation(String direction) {
        return talkAnimation;
    }

    @Override
    public Animation<TextureRegion> getHitAnimation(String direction) {
        return idleAnimation;
    }

    @Override
    public Animation<TextureRegion> getDeathAnimation(String direction) {
        return idleAnimation;
    }

    @Override
    public void dispose() {
        // Dispose of any resources if needed
    }
}

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

    public NPCAnimationManager() {
        loadAnimations();
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
            } catch (Exception e) {}
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
            } catch (Exception e) {}
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
            } catch (Exception e) {}
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
            } catch (Exception e) {}
        }
        chestOpenedAnimation = new Animation<>(0.2f, frames.toArray(new TextureRegion[0]));
        chestOpenedAnimation.setPlayMode(Animation.PlayMode.LOOP);
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
    public boolean isAnimationFinished(Animation<TextureRegion> anim, float stateTime) {
        if (anim == null) return true;
        return anim.isAnimationFinished(stateTime);
    }

    public Animation<TextureRegion> getOpenChestAnimation() {
        return openChestAnimation;
    }

    public void setOpenChestAnimation() {
    }

    @Override
    public void dispose() {
        // Dispose textures if you manage them elsewhere
    }
}

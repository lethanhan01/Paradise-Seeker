package com.paradise_seeker.game.entity.skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.paradise_seeker.game.entity.monster.Monster;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class PlayerSkill2 extends PlayerSkill {
	private List<StaticLightningSkill> activeLightnings = new ArrayList<>();
	private float currentX, currentY;
	private float offsetX = 0f; // có thể set phù hợp hướng, vị trí skill so với player
	private float offsetY = 0f;
    public PlayerSkill2() {
        super(20, 1000); // mana, cooldown
        loadSkillAnimations();
    }

    @Override
    protected void loadSkillAnimations() {
        try {
            TextureRegion[] upFrames = new TextureRegion[4];
            upFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/len/lightning_skill1_frame1.png")));
            upFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/len/lightning_skill1_frame2.png")));
            upFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/len/lightning_skill1_frame3.png")));
            upFrames[3] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/len/lightning_skill1_frame4.png")));
            skillAnimations.put("up", new Animation<>(0.07f, upFrames));

            TextureRegion[] downFrames = new TextureRegion[4];
            downFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/xuong/lightning_skill1_frame1.png")));
            downFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/xuong/lightning_skill1_frame2.png")));
            downFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/xuong/lightning_skill1_frame3.png")));
            downFrames[3] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/xuong/lightning_skill1_frame4.png")));
            skillAnimations.put("down", new Animation<>(0.07f, downFrames));

            TextureRegion[] leftFrames = new TextureRegion[4];
            leftFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/trai/lightning_skill1_frame1.png")));
            leftFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/trai/lightning_skill1_frame2.png")));
            leftFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/trai/lightning_skill1_frame3.png")));
            leftFrames[3] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/trai/lightning_skill1_frame4.png")));
            skillAnimations.put("left", new Animation<>(0.07f, leftFrames));

            TextureRegion[] rightFrames = new TextureRegion[4];
            rightFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/phai/lightning_skill1_frame1.png")));
            rightFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/phai/lightning_skill1_frame2.png")));
            rightFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/phai/lightning_skill1_frame3.png")));
            rightFrames[3] = new TextureRegion(new Texture(Gdx.files.internal("images/Entity/skills/PlayerSkills/Skill2/phai/lightning_skill1_frame4.png")));
            skillAnimations.put("right", new Animation<>(0.07f, rightFrames));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void castSkill(float atk, float x, float y, String direction) {
        // Không sử dụng hàm này
    }

    @Override
    public void castSkill(float atk, Rectangle bounds, String direction) {
        if (canUse(System.currentTimeMillis())) {
            float centerX = bounds.x + bounds.width / 2f;
            float centerY = bounds.y + bounds.height / 2f;
            float offset = 2.0f;  // Khoảng cách offset chiêu so với trung tâm nhân vật

            switch (direction) {
                case "up":
                    this.offsetY = offset;
                    this.offsetX = 0f;
                    break;
                case "down":
                    this.offsetY = -offset;
                    this.offsetX = 0f;
                    break;
                case "left":
                    this.offsetX = -offset;
                    this.offsetY = 0f;
                    break;
                case "right":
                    this.offsetX = offset;
                    this.offsetY = 0f;
                    break;
                default:
                    System.err.println("Unknown direction: " + direction);
                    break;
            }

            centerX += offsetX;
            centerY += offsetY;

            Animation<TextureRegion> anim = skillAnimations.get(direction);
            if (anim == null) {
                System.err.println("Animation missing for direction: " + direction);
                return;
            }

            StaticLightningSkill lightning = new StaticLightningSkill(centerX, centerY, atk * 2 * damageMultiplier, direction, anim);
            activeLightnings.add(lightning);
            
            setLastUsedTime(System.currentTimeMillis());
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        Iterator<StaticLightningSkill> iter = activeLightnings.iterator();
        while (iter.hasNext()) {
            StaticLightningSkill skill = iter.next();
            skill.update(); // Nếu cần cập nhật thời gian
            if (!skill.isActive()) {
                iter.remove();
            } else {
                skill.render(batch);
            }
        }
    }
    @Override
    public void updateSkill(float delta, List<Monster> monsters) {
        Iterator<StaticLightningSkill> iter = activeLightnings.iterator();
        while (iter.hasNext()) {
            StaticLightningSkill skill = iter.next();
            skill.update();

            for (Monster monster : monsters) {
                if (!monster.isDead() && !skill.hasDealtDamage() &&
                    skill.getHitbox().overlaps(monster.getBounds())) {

                    monster.takeHit(skill.getDamage());
                    skill.markDamageDealt(); // Ghi nhận đã gây sát thương


                }
            }

            if (!skill.isActive()) {
                iter.remove();
            }
        }
    }
    @Override
    public void updatePosition(float playerX, float playerY) {
        this.currentX = playerX + offsetX;
        this.currentY = playerY + offsetY;
        // Nếu bạn muốn, cập nhật các skill active (static lightning) vị trí tương ứng
        for (StaticLightningSkill skill : activeLightnings) {
            skill.setPosition(this.currentX, this.currentY);
        }
    }
}




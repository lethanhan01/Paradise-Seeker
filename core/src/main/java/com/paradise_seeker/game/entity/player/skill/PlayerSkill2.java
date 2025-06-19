package com.paradise_seeker.game.entity.player.skill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.*;
import com.paradise_seeker.game.entity.player.skill.skillanimation.PlayerSkill2Animation;
import com.paradise_seeker.game.entity.player.skill.skillrender.Skill2Renderer;

import java.util.List;

public class PlayerSkill2 extends PlayerSkill {
	private float posX, posY;
	private float offsetX = 1f;
	private float offsetY = 1f;
	private float stateTime = 0f;
	private boolean isCasting = false;
	private String direction;
	private Rectangle hitbox;
	private boolean hasDealtDamage = false;
	private float scale = 0.02f;
	private float skillDamage = 0f;
	private PlayerSkill2Animation playerSkill2Animation;
    private Skill2Renderer skill2Renderer;


	public PlayerSkill2() {
		super(20, 1000); // mana, cooldown
		this.playerSkill2Animation = new PlayerSkill2Animation(null);
		this.skill2Renderer = new Skill2Renderer();
	}

	@Override
	public void castSkill(float atk, Rectangle bounds, String direction) {
		if (canUse(System.currentTimeMillis())) {
			this.skillDamage = atk;
			float centerX = bounds.x + bounds.width / 2f;
			float centerY = bounds.y + bounds.height / 2f;
			float offset = 2.0f;
			switch (direction) {
				case "up": this.offsetY = offset+1f; this.offsetX = 0.5f; break;
				case "down": this.offsetY = -offset; this.offsetX = 0.5f; break;
				case "left": this.offsetX = -offset; this.offsetY = 0.5f; break;
				case "right": this.offsetX = offset+1f; this.offsetY = 0.5f; break;
				default: System.err.println("Unknown direction: " + direction); return;
			}
			centerX += offsetX;
			centerY += offsetY;
			this.posX = centerX;
			this.posY = centerY;
			this.direction = direction;
			this.stateTime = 0f;
			this.hasDealtDamage = false;
			this.isCasting = true;
			this.playerSkill2Animation.loadAnimation(direction);
			Animation<TextureRegion> anim = playerSkill2Animation.getSkillAnimation(direction);
			if (anim != null) {
				TextureRegion frame = anim.getKeyFrame(0f);
				float realWidth = frame.getRegionWidth() * scale;
				float realHeight = frame.getRegionHeight() * scale;
				this.hitbox = new Rectangle(centerX - realWidth / 2f, centerY - realHeight / 2f, realWidth, realHeight);
			}
			setLastUsedTime(System.currentTimeMillis());
		}
	}

	@Override
	public void updateSkill(float delta, List<Monster> monsters) {
		if (!isCasting) return;

		stateTime += Gdx.graphics.getDeltaTime();
		Animation<TextureRegion> anim = playerSkill2Animation.getSkillAnimation(direction);

		if (anim != null && anim.isAnimationFinished(stateTime)) {
			isCasting = false;
			return;
		}

		// Cập nhật hitbox
		if (hitbox != null) {
			hitbox.setPosition(posX - hitbox.getWidth() / 2f, posY - hitbox.getHeight() / 2f);
		}

		// Kiểm tra va chạm
		if (!hasDealtDamage) {
			for (Monster monster : monsters) {
				if (!monster.statusManager.isDead() && hitbox.overlaps(monster.getBounds())) {
					monster.takeHit(skillDamage * 2 * damageMultiplier);
					hasDealtDamage = true;
					break;
				}
			}
		}
	}

	@Override
	public void updatePosition(Player player) {
		if (isCasting) {
			this.posX = player.statusManager.getLastPosition().x + offsetX;
			this.posY = player.statusManager.getLastPosition().y + offsetY;
		}
	}


	@Override
	public void castSkill(float atk, float x, float y, String direction) {
		// Không sử dụng hàm này
	}

	public boolean isCasting() {
		return isCasting;
	}

	public PlayerSkill2Animation getPlayerSkill2Animation() {
		return playerSkill2Animation;
	}

	public Skill2Renderer getSkill2render(){
        return skill2Renderer;
    }

	public String getDirection() {
		return direction;
	}

	public float getStateTime() {
		return stateTime;
	}

	public float getScale() {
		return scale;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}
}




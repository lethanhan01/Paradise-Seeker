package com.paradise_seeker.game.entity.monster;

import com.paradise_seeker.game.entity.Player;
import com.paradise_seeker.game.map.GameMap;

public class MonsterAI {
    private final Monster monster;
    private float attackCooldown = 1.0f;
    private float attackTimer = 0f;
    private float attackRange = 1.2f;

    public MonsterAI(Monster monster) {
        this.monster = monster;
    }

    public void update(float deltaTime, Player player, GameMap map) {
        if (monster.isDead()) return;
        attackTimer -= deltaTime;
        if (player == null || player.isDead()) return;
        float dx = player.getBounds().x - monster.getBounds().x;
        float dy = player.getBounds().y - monster.getBounds().y;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        // Đuổi theo player nếu còn sống
        if (dist > attackRange) {
            float speed = monster.getSpeed() * deltaTime;
            float moveX = (dx / dist) * speed;
            float moveY = (dy / dist) * speed;
            monster.getBounds().x += moveX;
            monster.getBounds().y += moveY;
        } else if (attackTimer <= 0f) {
            // Tấn công nếu trong tầm
            player.takeDamage(monster.getAtk());
            attackTimer = attackCooldown;
        }
    }
}


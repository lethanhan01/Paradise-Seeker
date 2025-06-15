package com.paradise_seeker.game.entity.monster;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.entity.CollisionSystem;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class MonsterAI {
    private boolean isAggro = false;
    private float aggroTimer = 0f;
    private Vector2 originalPosition;

    private float attackCooldown = 5.0f;
    private float attackTimer = 0f;
    private float stopDistance;
    private final float AGGRO_DURATION = 5f;      // reset lại mỗi lần tấn công thành công
    private final float AGGRO_RANGE_MAX = 12f;    // phạm vi tối đa cho phép aggro tiếp tục

    public MonsterAI(Monster monster) {
        this.originalPosition = new Vector2(monster.getBounds().x, monster.getBounds().y);
    }

    public void onAggro() {
        isAggro = true;
        aggroTimer = AGGRO_DURATION;
    }

    public void checkAggro(Player player, Monster monster) {
        if (player == null || player.isDead()) return;
        float dx = player.getBounds().x - monster.getBounds().x;
        float dy = player.getBounds().y - monster.getBounds().y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 2f) {
            onAggro();
        }
    }

    public void update(float deltaTime, Player player, CollisionSystem collision, Monster monster) {
        if (monster.isDead() || player == null || player.isDead()) return;
        float stopDisplayer = (float) Math.sqrt(player.getBounds().width * player.getBounds().width + player.getBounds().height * player.getBounds().height) / 2f;
        float stopDisMonster = (float) Math.sqrt(monster.getBounds().width * monster.getBounds().width + monster.getBounds().height * monster.getBounds().height) / 2f;
        stopDistance = stopDisplayer + stopDisMonster + 0.1f;
        checkAggro(player, monster);

        float dx = player.getBounds().x - monster.getBounds().x;
        float dy = player.getBounds().y - monster.getBounds().y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        // Nếu player chạy ra quá xa, ngắt aggro ngay
        if (isAggro && dist > AGGRO_RANGE_MAX) {
            isAggro = false;
            return;
        }

        if (isAggro) {
            aggroTimer -= deltaTime;
            attackTimer -= deltaTime;
            if (attackTimer < 0f) attackTimer = 0f;

            // Đuổi theo player
            if (dist > stopDistance) {
                float speed = monster.getSpeed() * deltaTime;
                float moveX = (dx / dist) * speed;
                float moveY = (dy / dist) * speed;
                Rectangle bounds = monster.getBounds();

                Rectangle testX = new Rectangle(bounds);
                testX.x += moveX;
                if (!collision.isBlocked(testX, monster)) {
                    bounds.x += moveX;
                }
                Rectangle testY = new Rectangle(bounds);
                testY.y += moveY;
                if (!collision.isBlocked(testY, monster)) {
                    bounds.y += moveY;
                }
            }

            // Nếu tấn công được player
            if (dist <= stopDistance && attackTimer <= 0f) {
                monster.cleave(player);
                attackTimer = attackCooldown;
                aggroTimer = AGGRO_DURATION; // Reset lại aggro mỗi lần tấn công!
            }

            // Nếu hết aggro timer, dừng aggro (quái không tấn công được quá lâu)
            if (aggroTimer <= 0f) {
                isAggro = false;
                return;
            }
        } else {
            // Quay về vị trí gốc
            float dx0 = originalPosition.x - monster.getBounds().x;
            float dy0 = originalPosition.y - monster.getBounds().y;
            float dist0 = (float) Math.sqrt(dx0 * dx0 + dy0 * dy0);
            if (dist0 > 0.1f) {
                float speed = monster.getSpeed() * deltaTime;
                float moveX = (dx0 / dist0) * speed;
                float moveY = (dy0 / dist0) * speed;
                Rectangle bounds = monster.getBounds();

                Rectangle testX = new Rectangle(bounds);
                testX.x += moveX;
                if (!collision.isBlocked(testX, monster)) {
                    bounds.x += moveX;
                }
                Rectangle testY = new Rectangle(bounds);
                testY.y += moveY;
                if (!collision.isBlocked(testY, monster)) {
                    bounds.y += moveY;
                }
            }
        }
    }
}

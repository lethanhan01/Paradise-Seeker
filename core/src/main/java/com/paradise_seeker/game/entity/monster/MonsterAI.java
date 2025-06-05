package com.paradise_seeker.game.entity.monster;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.paradise_seeker.game.entity.player.Player;
import com.paradise_seeker.game.map.GameMap;

public class MonsterAI {
    private final Monster monster;

    // Trạng thái aggro
    private boolean isAggro = false;
    private float aggroTimer = 0f;
    private Vector2 originalPosition;

    private float attackCooldown = 5.0f;
    private float attackTimer = 0f;
    private float stopDistance;

    public MonsterAI(Monster monster) {
        this.monster = monster;
        // Lưu vị trí ban đầu
        this.originalPosition = new Vector2(monster.getBounds().x, monster.getBounds().y);
    }

    // Gọi hàm này mỗi lần bị player tấn công
    public void onAggro() {
        isAggro = true;
        aggroTimer = 5f;
    }

    public void checkAggro(Player player) {
		if (player == null || player.isDead()) return;

		float dx = player.getBounds().x - monster.getBounds().x;
		float dy = player.getBounds().y - monster.getBounds().y;
		float dist = (float) Math.sqrt(dx * dx + dy * dy);

		// Nếu player trong khoảng tầm nhìn thì kích hoạt aggro
		if (dist < 2f) {
			onAggro();
		}
	}

    public void update(float deltaTime, Player player, GameMap map) {
        if (monster.isDead() || player == null || player.isDead()) return;
        float stopDisplayer = (float) Math.sqrt(player.getBounds().width * player.getBounds().width + player.getBounds().height * player.getBounds().height) / 2f;
        float stopDisMonster = (float) Math.sqrt(monster.getBounds().width * monster.getBounds().width + monster.getBounds().height * monster.getBounds().height) / 2f;
        stopDistance = stopDisplayer + stopDisMonster + 0.1f;
        checkAggro(player);




        // Đang aggro
        if (isAggro) {
            aggroTimer -= deltaTime;
            attackTimer -= deltaTime;
            //System.out.println("attackTimer: " + attackTimer);
            if (attackTimer < 0f) attackTimer = 0f;

            float dx = player.getBounds().x - monster.getBounds().x;
            float dy = player.getBounds().y - monster.getBounds().y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            // Hết thời gian mà chưa tiếp cận thì quay về
            if (aggroTimer <= 0f) {
                isAggro = false;
                return;
            }

            // Đuổi theo player, dừng khi cách <= stopDistance
            if (dist > stopDistance) {
                float speed = monster.getSpeed() * deltaTime;
                float moveX = (dx / dist) * speed;
                float moveY = (dy / dist) * speed;
                Rectangle bounds = monster.getBounds();

             // kiểm tra trục X riêng
             Rectangle testX = new Rectangle(bounds);
             testX.x += moveX;
             if (!map.isBlocked(testX, monster)) {
                 bounds.x += moveX;
             }


             // kiểm tra trục Y riêng
             Rectangle testY = new Rectangle(bounds);
             testY.y += moveY;
             if (!map.isBlocked(testY, monster)) {
                 bounds.y += moveY;
             }
			}

            // Nếu tiếp cận rồi thì tấn công
            if (dist <= stopDistance && attackTimer <= 0f) {
                player.takeDamage(monster.getAtk());
                attackTimer = attackCooldown;
            }
        }
            else {
                // Quay về vị trí gốc
                float dx = originalPosition.x - monster.getBounds().x;
                float dy = originalPosition.y - monster.getBounds().y;
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist > 0.1f) {
                    float speed = monster.getSpeed() * deltaTime;
                    float moveX = (dx / dist) * speed;
                    float moveY = (dy / dist) * speed;

                Rectangle bounds = monster.getBounds();

                // kiểm tra trục X riêng
                Rectangle testX = new Rectangle(bounds);
                testX.x += moveX;
                if (!map.isBlocked(testX, monster)) {
                    bounds.x += moveX;
                }

                // kiểm tra trục Y riêng
                Rectangle testY = new Rectangle(bounds);
                testY.y += moveY;
                if (!map.isBlocked(testY, monster)) {
                    bounds.y += moveY;
                }
            }
        }

    }
}

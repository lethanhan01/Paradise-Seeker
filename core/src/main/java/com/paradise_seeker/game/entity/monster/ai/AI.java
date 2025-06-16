package com.paradise_seeker.game.entity.monster.ai;

import com.paradise_seeker.game.entity.monster.Monster;
import com.paradise_seeker.game.entity.player.Player;

public interface AI {
	 public void checkAggro(Player player, Monster monster);
	 public void onAggro();
}

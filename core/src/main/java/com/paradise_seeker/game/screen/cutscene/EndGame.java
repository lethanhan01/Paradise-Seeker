package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.paradise_seeker.game.main.Main;

public class EndGame extends CutScene {

	public EndGame(Main game) {
		super(
				game,
				// Hình ảnh cho cảnh kết thúc
				Arrays.asList(
						"cutscene/Ending/6.1.png",
						"cutscene/Ending/6.2.png",
						"cutscene/Ending/6.3.png",
						"cutscene/Ending/6.4.png"
				),
				// Lời thoại tương ứng với từng hình ảnh
				Arrays.asList(
						"",
						"",
						"",
						""
				),
				5f // mỗi cảnh hiển thị 5 giây
		);
	}

}

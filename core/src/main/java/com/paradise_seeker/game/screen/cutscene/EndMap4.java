package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.paradise_seeker.game.main.Main;

public class EndMap4 extends CutScene {

	public EndMap4(Main game) {
		super(
				game,
				// Hình ảnh cho cảnh kết thúc Map 4
				Arrays.asList(
						"cutscene/Chapter 4/4.4.1.png",
						"cutscene/Chapter 4/4.4.3.png",
						"cutscene/Chapter 5/5.1.png",
						"cutscene/Chapter 5/5.1.png",
						"cutscene/Chapter 5/5.1.png",
						"cutscene/Chapter 5/5.3.png",
						"cutscene/Chapter 5/5.3.png"
				),
				// Lời thoại tương ứng với từng hình ảnh
				Arrays.asList(
						"...",
						"Paradise... Paradise King...\n Whoever you are, I will end this.",
						"PARADISE KING: \nFinally someone makes it through...\n or what's left of them",
						"JACK: You're behind it all?\n Degeneration, Titan, Dragon King,\n Nyx… is it you?",
						"PARADISE KING: You think I want to? I was once \nlike you : longing to change the world. \nThen I found Paradise.",
						"Paradise is not a place,\n but a state - omnipotent, omniscient… and alone. \nI tried to share, to control, but it consumed me.",
						"The World Pieces…\n are a last ditch effort - to \nfind my successor, or my finisher."
						
				),
				5f // mỗi cảnh hiển thị 5 giây
		);
	}

}

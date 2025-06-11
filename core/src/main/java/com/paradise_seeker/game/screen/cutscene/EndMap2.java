package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.paradise_seeker.game.main.Main;

public class EndMap2 extends CutScene {

	public EndMap2(Main game) {
		super(
	            game,
	            // Hình ảnh cho cảnh kết thúc Map 2
	            Arrays.asList(
	            	"cutscene/Chapter 2/2.2.png",
	                "cutscene/Chapter 2/2.3.1.png",
	                "cutscene/Chapter 2/2.3.png",
	                "cutscene/Chapter 3/3.1.png",
	                "cutscene/Chapter 3/3.1.1.png"
	            ),
	            // Lời thoại tương ứng với từng hình ảnh
	            Arrays.asList(
	            	"You did it. The curse has\n been partially lifted... But the\n road ahead is even more brutal.",
	            	"There is a force manipulating\n everything... Regarding Paradise.",
	            	"Draconis... Fragment of Fire.",
	            	"Is this whole place affected too?",
	            	"Paradise King...\n who is he that causes\n so many tragedies?"
	            ),
	            5f // mỗi cảnh hiển thị 5 giây
	        );
	}

}

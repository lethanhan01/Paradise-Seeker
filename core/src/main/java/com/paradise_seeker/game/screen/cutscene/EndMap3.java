package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.paradise_seeker.game.main.Main;

public class EndMap3 extends CutScene {

	public EndMap3(Main game) {
		super(
	            game,
	            // Hình ảnh cho cảnh kết thúc Map 3
	            Arrays.asList(
	                "cutscene/Chapter 3/3.3.3.png",
	                "cutscene/Chapter 3/3.3.png",
	                "cutscene/Chapter 4/4.1.png"
	            ),
	            // Lời thoại tương ứng với từng hình ảnh
	            Arrays.asList(
	            	"Nyx... \nInfernia Castle... \nThe Dragon King's warning...",
	                "Corruption... \nI must find the ultimate truth.",
	                "This place... is so heavy.\n The darkness here is more than\n just the absence of light."
	            ),
	            5f // mỗi cảnh hiển thị 5 giây
	        );
	}

}

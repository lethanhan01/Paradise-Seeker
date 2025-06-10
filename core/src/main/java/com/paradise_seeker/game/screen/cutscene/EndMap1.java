package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.paradise_seeker.game.main.Main;

public class EndMap1 extends CutScene {

	public EndMap1(Main game) {
		super(
	            game,
	            Arrays.asList(
	                "cutscene/Chapter 1/1.4.1.png",
	                "cutscene/Chapter 1/1.4.2.png"
	            ),
	            Arrays.asList( // MAX 41 KY TU
	            	"JACK: Titania... The Land...",
	                "Whatever happened to you, Alistair\n I will find out the truth."
	            ),
	            5f // mỗi cảnh hiển thị 5 giây
	        );
	}

}

package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.paradise_seeker.game.main.Main;

public class IntroCutScene extends CutScene {

	public IntroCutScene(Main game) {
        super(
            game,
            Arrays.asList(
                "cutscene/Chapter 1/1.1.0.png",
                "cutscene/Chapter 1/1.1.1.png",
                "cutscene/Chapter 1/1.2.1.png"
            ),
            Arrays.asList( // MAX 41 KY TU
            	"This temple... where Professor Alistair\n disappeared",
                "This artifact is the last clue he left \n behind.",
                "There must be answers here."
            ),
            5f // mỗi cảnh hiển thị 5 giây
        );
    }
}

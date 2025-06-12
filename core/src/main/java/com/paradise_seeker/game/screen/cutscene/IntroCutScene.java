package com.paradise_seeker.game.screen.cutscene;

import java.util.Arrays;

import com.paradise_seeker.game.main.Main;

public class IntroCutScene extends CutScene {

	public IntroCutScene(Main game) {
        super(
            game,
            Arrays.asList(
                "cutscene/Chapter 1/1.0-m.png",
                "cutscene/Chapter 1/1.2.1.png",
                "cutscene/Chapter 1/1.2.2.png",
                "cutscene/Chapter 1/1.2-m.png"

            ),
            Arrays.asList( // MAX 41 KY TU
            	"This temple... where Professor Alistair\n disappeared",
                "This artifact is the last clue he left \n behind.",
                "I must find out what happened to him.",
                "There must be answers here."
            ),
            5f // mỗi cảnh hiển thị 5 giây
        );
    }
}

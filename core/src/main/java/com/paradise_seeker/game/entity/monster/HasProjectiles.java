package com.paradise_seeker.game.entity.monster;

import com.paradise_seeker.game.rendering.Renderable;
import java.util.List;

public interface HasProjectiles {
    List<? extends Renderable> getProjectiles();
}


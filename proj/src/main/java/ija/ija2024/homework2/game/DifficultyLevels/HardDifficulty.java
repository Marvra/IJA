package ija.ija2024.homework2.game.DifficultyLevels;

import java.util.*;
import java.util.random.*;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.homework2.common.geometry.Point;
import ija.ija2024.homework2.common.geometry.Segment;
import ija.ija2024.homework2.common.geometry.Vector;
import ija.ija2024.homework2.game.Game;

public class HardDifficulty extends GameDifficulty {
    public HardDifficulty() {
        super.difficulty = GeneralDifficulty.hard12x12;
        super.dimensions = 12;
    }

	@Override
    public Game generate() {
        return super.generate(5, 10);
    }

}
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

public class MediumDifficulty extends GameDifficulty {
    public MediumDifficulty() {
        super.difficulty = GeneralDifficulty.medium;
        super.dimensions = 8;
    }

    public Game generate() {
        return super.generate(2, 5);
    }

}
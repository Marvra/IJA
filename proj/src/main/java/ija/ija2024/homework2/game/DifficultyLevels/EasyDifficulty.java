package ija.ija2024.homework2.game.DifficultyLevels;

import java.util.Random;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.common.geometry.Point;
import ija.ija2024.homework2.common.geometry.Segment;
import ija.ija2024.homework2.game.Game;
import java.util.List;

public class EasyDifficulty extends GameDifficulty {
	public EasyDifficulty() {
		super.difficulty = GeneralDifficulty.easy;
		super.dimensions = 6;
	}

	@Override
	public Game generate() {
		return super.generate(1, 1);
	}
}
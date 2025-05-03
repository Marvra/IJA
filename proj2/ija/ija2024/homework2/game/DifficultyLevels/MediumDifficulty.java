package ija.ija2024.homework2.game.DifficultyLevels;

import java.util.Random;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.game.Game;
import java.util.List;

public class MediumDifficulty extends GameDifficulty {
	public MediumDifficulty() {
		super.difficulty = GeneralDifficulty.medium;
		super.dimensions = 8;
	}

	@Override
	public Game generate() {
		Game game = new Game(super.dimensions, super.dimensions);
		List<GameNode> nodes = new java.util.ArrayList<>();
        // creating power node with all sides
        Random rand = new Random();
        int row = rand.nextInt(super.dimensions - 2) + 1;
        int col = rand.nextInt(super.dimensions - 2) + 1;
        nodes.add(game.createPowerNode(new Position(row, col), Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST));

        // creating lightbulb node with all sides
        int lightbulbRow = row;
        int lightbulbCol = col;
        do {
            lightbulbRow = rand.nextInt(super.dimensions - 2) + 1;
            lightbulbCol = rand.nextInt(super.dimensions - 2) + 1;
        } while (Math.abs(lightbulbRow - row) < 3 && Math.abs(lightbulbCol - col) < 3);
        nodes.add(game.createBulbNode(new Position(lightbulbRow, lightbulbCol), Side.NORTH, Side.SOUTH, Side.WEST, Side.EAST));

		// TODO: interroute nodes with links
		
		return game;
	}

}
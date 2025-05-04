package com.example.proj.game.DifficultyLevels;

import com.example.proj.GameNode;
import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.game.Game;

import java.util.List;
import java.util.Random;

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
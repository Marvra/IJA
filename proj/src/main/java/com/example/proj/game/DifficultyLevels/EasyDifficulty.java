package com.example.proj.game.DifficultyLevels;

import com.example.proj.GameNode;
import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.game.Game;
import com.example.proj.geometry.Point;
import com.example.proj.geometry.Segment;

import java.util.List;
import java.util.Random;

public class EasyDifficulty extends GameDifficulty {
	public EasyDifficulty() {
		super.difficulty = GeneralDifficulty.easy;
		super.dimensions = 6;
	}

	@Override
	public Game generate() {
		Game game = Game.create(super.dimensions, super.dimensions);
		List<GameNode> nodes = new java.util.ArrayList<>();
		// creating power node with all sides
		Random rand = new Random();
		int option = rand.nextInt(4);
		int position = rand.nextInt(super.dimensions - 1) + 1;
		int row = (option % 2 == 0) ? position : ((option >> 1) ^ (option & 1)) * (super.dimensions - 1) + 1;
		int col = (option % 2 == 0) ? ((option >> 1) ^ (option & 1)) * (super.dimensions - 1) + 1 : position;
		nodes.add(game.createPowerNode(new Position(row, col), Side.values()[option]));

		// creating lightbulb node with all sides
		int lightbulbRow = row;
		int lightbulbCol = col;
		do {
			lightbulbRow = rand.nextInt(super.dimensions - 1) + 1;
			lightbulbCol = rand.nextInt(super.dimensions - 1) + 1;
		} while (!(Math.abs(lightbulbRow - row) > 3 || Math.abs(lightbulbCol - col) > 3));

		Segment s = new Segment(Point.fromPosition(nodes.get(0).getPosition()),
				new Point((double) lightbulbRow, (double)lightbulbCol));
		List<Position> raster = s.rasterize();

		super.convertToLinks(raster, game);
		boolean found = false;
		game.print();
		for (Side side : game.node(new Position(lightbulbRow, lightbulbCol)).sides) {
			Position pos = new Position(lightbulbRow, lightbulbCol);
			GameNode node = game.node(new Position((int) (pos.getRow() + Math.sin(side.ordinal() * Math.PI / 2)),
					(int) (pos.getCol() + Math.cos(side.ordinal() * Math.PI / 2))));
			if (node != null) {
				for (Side side2 : node.sides) {
					if (Side.values()[(side2.ordinal() + 2) % 4] == side) {
						game.createBulbNode(pos, side);
						found = true;
						break;
					}
				}
			}
			if (found) {
				break;
			}
		}
		game.print();

		return game;
	}
}
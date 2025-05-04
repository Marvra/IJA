package com.example.proj.game.DifficultyLevels;

import com.example.proj.GameNode;
import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.game.Game;

import java.util.List;

public class GameDifficulty {
	protected GeneralDifficulty difficulty;
	protected int dimensions = 4;

	public GeneralDifficulty getDifficulty() {
		return difficulty;
	}

	public int getDimensions() {
		return dimensions;
	}

	/**
	 * Generates a new game with a predefined configuration.
	 * The game is created on a square grid with size determined by the dimensions
	 * field.
	 * A power node, link nodes, and a bulb node are placed at specific positions
	 * with defined sides for connectivity.
	 * 
	 * @return A Game object with the predefined node setup.
	 */
	public Game generate() {
		Game game = Game.create(this.dimensions, this.dimensions);
		game.createPowerNode(new Position(2, 1), Side.EAST);
		game.createLinkNode(new Position(2, 2), Side.EAST, Side.WEST);
		game.createLinkNode(new Position(2, 3), Side.NORTH, Side.SOUTH);
		game.createBulbNode(new Position(2, 4), Side.WEST);
		return game;
	}

	/**
	 * Merges multiple lists of positions into one list without duplicates.
	 * 
	 * @param lists The lists of positions to merge.
	 * @return A new list containing all positions from the given lists
	 *         without duplicates.
	 */
	public static List<Position> merge(List<Position>... lists) {
		List<Position> l1 = new java.util.ArrayList<>();
		for (List<Position> l : lists) {
			for (Position p : l) {
				if (!l1.contains(p)) {
					l1.add(p);
				}
			}
		}
		return l1;
	}

	/**
	 * Converts a list of positions into link nodes in the game and returns the
	 * created nodes.
	 * For each position, determines the sides for connectivity based on neighboring
	 * positions.
	 * Ensures that each link node has at least two sides for proper linkage.
	 * 
	 * @param positions The list of positions to convert into link nodes.
	 * @param game      The game instance where the nodes will be created.
	 * @return A list of created GameNode objects representing the link nodes.
	 */
	public static List<GameNode> convertToLinks(List<Position> positions, Game game) {
		List<GameNode> nodes = new java.util.ArrayList<>();
		for (Position p : positions) {
			List<Side> sides = new java.util.ArrayList<>();
			for (int i = 0; i < 4; i++) {
				if (positions.contains(
						new Position((int) (p.getRow() + Math.sin(i * Math.PI / 2)),
								(int) (p.getCol() + Math.cos(i * Math.PI / 2))))) {
					sides.add(Side.values()[i]);
				}
			}
			if (sides.size() == 1) {
				sides.add(Side.values()[(sides.get(0).ordinal() + 2) % 4]);
			}
			GameNode node = game.createLinkNode(p, sides.toArray(new Side[0]));
			if(node != null) {
				nodes.add(node);
			}
		}
		return nodes;
	}
}

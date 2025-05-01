package ija.ija2024.homework2.game.DifficultyLevels;

import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.game.Game;

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
     * The game is created on a square grid with size determined by the dimensions field.
     * A power node, link nodes, and a bulb node are placed at specific positions
     * with defined sides for connectivity.
     * 
     * @return A Game object with the predefined node setup.
     */
	public Game generate() {
		Game game = new Game(this.dimensions, this.dimensions);
		game.createPowerNode(new Position(2, 1), Side.EAST);
		game.createLinkNode(new Position(2, 2), Side.EAST, Side.WEST);
		game.createLinkNode(new Position(2, 3), Side.NORTH, Side.SOUTH);
		game.createBulbNode(new Position(2, 4), Side.WEST);
		return game;
	}
}

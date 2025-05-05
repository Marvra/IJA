package ija.ija2024.homework2.game.DifficultyLevels;

import static ija.ija2024.homework2.common.Side.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.common.geometry.Point;
import ija.ija2024.homework2.common.geometry.Segment;
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
			if (node != null) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public List<Position> generatePositions(int minimum, int maximum) {
		if (minimum == 0) {
			minimum = 1;
		}
		if (maximum == 0) {
			maximum = 1;
		}
		Random random = new Random();
		List<Position> positions = new java.util.ArrayList<>();

		int lightbulbs = random.nextInt(maximum - minimum + 1) + minimum;
		for (int i = 0; i <= lightbulbs; i++) {
			Position pos;
			boolean exists;
			do {
				pos = new Position(
						random.nextInt(this.dimensions - 1) + 1,
						random.nextInt(this.dimensions - 1) + 1);
				exists = positions.contains(pos);
				if (positions.size() > 0) {
					Position power = positions.get(0);
					if (Math.hypot(pos.getRow() - power.getRow(), pos.getCol() - power.getCol()) < 2) {
						exists = true;
					}
				}
			} while (exists);
			positions.add(pos);
		}
		return positions;
	}

	/**
	 * Finds the minimum spanning tree of a set of points. The algorithm works by
	 * sorting all edges by their distance and then adding them to the minimum
	 * spanning tree if they do not form a cycle. Once all points are connected,
	 * the algorithm terminates. Defective, but effective.
	 *
	 * @param points the set of points to find the minimum spanning tree of
	 * @return the minimum spanning tree of the points
	 */
	public List<Segment> mst(List<Point> points) {
		List<Segment> mst = new ArrayList<>();
		HashSet<Point> visited = new HashSet<>();
		record Edge(Point a, Point b, double distance) {
		}
		List<Edge> edges = new ArrayList<>();
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				edges.add(new Edge(points.get(i), points.get(j), points.get(i).distance(points.get(j))));
			}
		}
		Collections.sort(edges, Comparator.comparingDouble(Edge::distance));
		for (Edge e : edges) {
			if (!visited.contains(e.a) || !visited.contains(e.b)) {
				mst.add(new Segment(e.a, e.b));
				visited.add(e.a);
				visited.add(e.b);
			}
			if (visited.size() == points.size()) {
				break;
			}
		}
		return mst;
	}

}

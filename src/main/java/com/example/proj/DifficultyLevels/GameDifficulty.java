package com.example.proj.DifficultyLevels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.example.proj.GameNode;
import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.Type;
import com.example.proj.common.geometry.Point;
import com.example.proj.common.geometry.Segment;
import com.example.proj.common.geometry.Vector;
import com.example.proj.game.Game;

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
		List<Position> l1 = new ArrayList<>();
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
		List<GameNode> nodes = new ArrayList<>();
		for (Position p : positions) {
			List<Side> sides = new ArrayList<>();
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

	/**
	 * Generates a list of random positions for placing nodes on a game grid.
	 * Ensures that generated positions maintain a minimum distance from the first
	 * position (assumed to be a power node) to prevent clustering.
	 * 
	 * The function generates a random number of positions between the specified
	 * minimum and maximum values. If either minimum or maximum is zero, it is
	 * reset to one. The positions are randomly generated within the grid's
	 * dimensions, ensuring they are not located on the grid's edge.
	 * 
	 * @param minimum The minimum number of positions to generate.
	 * @param maximum The maximum number of positions to generate.
	 * @return A list of unique Position objects representing node placements on
	 *         the grid.
	 */
	public List<Position> generatePositions(int minimum, int maximum) {
		if (minimum == 0) {
			minimum = 1;
		}
		if (maximum == 0) {
			maximum = 1;
		}
		Random random = new Random();
		List<Position> positions = new ArrayList<>();
		int distance = this.dimensions / 2;
		int lightbulbs = random.nextInt(maximum - minimum + 1) + minimum;
		int tries = 120;
		for (int i = 0; i <= lightbulbs; i++) {
			Position pos;
			boolean exists;
			tries = 120;
			do {
				pos = new Position(
						Math.abs(random.nextInt() % (this.dimensions - 3)) + 2,
						Math.abs(random.nextInt() % (this.dimensions - 3)) + 2);
				System.out.println("dims: " + this.dimensions);
				exists = positions.contains(pos);
				if (positions.size() > 0) {
					Position power = positions.get(0);
					if (Math.hypot(pos.getRow() - power.getRow(), pos.getCol() - power.getCol()) < distance) {
						exists = true;
						if (distance > 2) {
							distance--;
						}
					}
				}
				if (tries >= 0)
					tries--;
				else
					break;
			} while (exists);
			if (tries < 0) {
				break;
			}
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
		mst.add(new Segment(edges.get(0).a, edges.get(0).b));
		visited.add(edges.get(0).a);
		visited.add(edges.get(0).b);
		edges.remove(0);
		for (int i = 0; i < Math.pow(2, points.size()) && visited.size() < points.size(); i++) {

			for (Edge e : edges) {
				if ((visited.contains(e.a) && !visited.contains(e.b))
						|| (visited.contains(e.b) && !visited.contains(e.a))) {
					mst.add(new Segment(e.a, e.b));
					visited.add(e.a);
					visited.add(e.b);
				}
				if (visited.size() == points.size()) {
					break;
				}
			}
		}
		return mst;
	}

	/**
	 * Generates a new game with a predefined configuration.
	 * The game is created on a square grid with size determined by the dimensions
	 * field.
	 * A power node, link nodes, and a bulb node are placed at specific positions
	 * with defined sides for connectivity.
	 * 
	 * @param minimumBulbs The minimum number of bulbs to be placed in the game.
	 * @param maximumBulbs The maximum number of bulbs to be placed in the game.
	 * @return A Game object with the predefined node setup.
	 */
	public Game generate(int minimumBulbs, int maximumBulbs) {

		Game game;
		int lightbulbCount = 0;
		do {
			game = Game.create(this.dimensions, this.dimensions);
			List<Position> positions = this.generatePositions(minimumBulbs, maximumBulbs);
			List<Position> originals = new ArrayList<>(positions);
			lightbulbCount = originals.size() - 1;
			List<Segment> segments = new ArrayList<>();
			for (Position o : originals) {
				System.out.println(Point.fromPosition(o));
			}
			// make segments between nodes
			for (int i = 0; i < positions.size(); i++) {
				for (int j = i + 1; j < positions.size(); j++) {
					Point p1 = Point.fromPosition(positions.get(i));
					Point p2 = Point.fromPosition(positions.get(j));
					segments.add(new Segment(p1, p2));
				}
			}

			// add intersections of segments
			for (int i = 0; i < segments.size(); i++) {
				for (int j = i + 1; j < segments.size(); j++) {
					Point p1 = Segment.intersection(segments.get(i), segments.get(j));
					if (p1 != null) {
						if (!positions.contains(p1.toPosition())) {
							positions.add(p1.toPosition());
						}
					}
				}
			}
			segments.clear();
			List<Point> points = new ArrayList<>();
			for (Position p : positions) {
				points.add(Point.fromPosition(p));
			}
			segments = mst(points);
			List<Position> newPositions = new ArrayList<>();
			for (Segment s : segments) {
				newPositions = merge(newPositions, s.rasterize());
			}
			List<GameNode> nodes = GameDifficulty.convertToLinks(newPositions, game);
			game.print();
			Position position = originals.get(0);
			if (game.node(position).type == Type.EMPTY) {
				// if node is empty, add bulb there with link
				for (GameNode n : game.neighours(position)) {
					if (n.type == Type.LINK) {
						Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
						double roun = v.angle();
						System.out.println(v);
						System.out.println(-roun / Math.PI + " PI");
						game.createPowerNode(position, Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4]);
						Side[] newSides = n.sides.toArray(new Side[n.sides.size() + 1]);
						newSides[n.sides.size()] = Side.values()[(int) ((-roun / Math.PI) * 2 + 7) % 4];
						n.setSides(newSides);
						break;
					}
				}
			} else {
				List<GameNode> empties = new ArrayList<>(game.empties(position));
				Collections.shuffle(empties, new Random());
				for (GameNode n : empties) {
					Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
					System.out.println(v);
					double roun = v.angle();
					System.out.println(-roun / Math.PI + " PI");
					game.createPowerNode(n.getPosition(), Side.values()[(int) ((-roun / Math.PI) * 2 + 7) % 4]);
					Side[] newSides = game.node(position).sides.toArray(new Side[game.node(position).sides.size() + 1]);
					newSides[game.node(position).sides.size()] = Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4];
					game.node(position).setSides(newSides);
					break;
				}

			}
			originals.remove(0);

			while (!originals.isEmpty()) {
				position = originals.get(0);
				if (game.node(position).type == Type.EMPTY) {
					// if node is empty, add bulb there with link
					for (GameNode n : game.neighours(position)) {
						if (n.type == Type.LINK) {
							Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
							double roun = v.angle();
							System.out.println(v);
							System.out.println(-roun / Math.PI + " PI");
							game.createBulbNode(position, Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4]);
							Side[] newSides = n.sides.toArray(new Side[n.sides.size() + 1]);
							newSides[n.sides.size()] = Side.values()[(int) ((-roun / Math.PI) * 2 + 7) % 4];
							n.setSides(newSides);
							break;
						}
					}
				} else {
					List<GameNode> empties = new ArrayList<>(game.empties(position));
					Collections.shuffle(empties, new Random());
					for (GameNode n : empties) {
						Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
						double roun = v.angle();
						System.out.println(v);
						System.out.println(-roun / Math.PI + " PI");
						game.createBulbNode(n.getPosition(), Side.values()[(int) ((-roun / Math.PI) * 2 + 7) % 4]);
						Side[] newSides = game.node(position).sides
								.toArray(new Side[game.node(position).sides.size() + 1]);
						newSides[game.node(position).sides.size()] = Side.values()[(int) ((-roun / Math.PI) * 2 + 5)
								% 4];
						game.node(position).setSides(newSides);
						break;
					}
				}
				originals.remove(0);
			}
			reduceEmpty(game);
			reduceGrid(game);
			// reduceUnleaded(game);
			game.print();
		} while (accessibleBulbCount(game, null, null) != lightbulbCount);
		return game;
	}

	/**
	 * Iteratively reduces link nodes in the game grid that do not lead to
	 * meaningful paths. If a link node has only one connection after
	 * evaluating its empty neighbors, it is converted to an empty node.
	 * This process continues until no more nodes are converted.
	 * 
	 * @param game The game whose grid is to be reduced by removing redundant
	 *             link nodes and converting them to empty nodes.
	 */
	public void reduceEmpty(Game game) {
		boolean changed;
		do {
			changed = false;
			for (int i = 1; i <= game.rows(); i++) {
				for (int j = 1; j <= game.cols(); j++) {
					Position p = new Position(i, j);
					if (game.node(p).type == Type.LINK) {
						for (GameNode n : game.empties(p)) {
							Vector v = new Vector(Point.fromPosition(p), Point.fromPosition(n.getPosition()));
							double roun = v.angle();
							game.node(p).sides.remove((Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4]));
							// invalid route when not routing anywhere, rewrite to Empty
							if (game.node(p).sides.size() == 1) {
								changed = true;
								game.node(p).type = Type.EMPTY;
								game.node(p).sides.clear();
							}
						}
					}
				}
			}
		} while (changed);
	}

	/**
	 * Removes unconnected sides from link nodes. This is done by going through all
	 * link nodes and for each of them going through all its neighbors. If a
	 * neighbor
	 * is also a link node and the vector between the two nodes does not match any
	 * of the neighbor's sides, the corresponding side is removed from the original
	 * node.
	 * 
	 * @param game The game to reduce
	 */
	public void reduceUnleaded(Game game) {
		for (int i = 1; i <= game.rows(); i++) {
			for (int j = 1; j <= game.cols(); j++) {
				Position p = new Position(i, j);
				if (game.node(p).type == Type.LINK) {
					for (GameNode n : game.neighours(p)) {
						if (n.type == Type.LINK) {
							Vector v = new Vector(Point.fromPosition(p), Point.fromPosition(n.getPosition()));
							double roun = v.angle();
							if (game.node(p).sides.contains(Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4])
									&& !n.sides.contains(Side.values()[(int) ((-roun / Math.PI) * 2 + 7) % 4])) {
								System.out.println("ON " + game.node(p) + " removing "
										+ Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4]);
								game.node(p).sides.remove((Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4]));
								reduceEmpty(game);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Randomly removes one or more sides from link nodes in the grid, only if the
	 * number of accessible bulbs does not decrease. If the number of accessible
	 * bulbs does decrease, the removed side is put back and the loop continues.
	 * This is done to reduce the number of possible solutions.
	 * 
	 * @param game The game to reduce.
	 */
	protected void reduceGrid(Game game) {
		for (int i = 1; i <= game.rows(); i++) {
			for (int j = 1; j <= game.cols(); j++) {
				Position p = new Position(i, j);
				if (game.node(p).type == Type.LINK) {
					Random r = new Random();
					if (game.node(p).sides.size() == 4) {
						for (int k = 0; k < r.nextInt() % (game.node(p).sides.size() - 1) + 1; k++) {
							int before = accessibleBulbCount(game, null, null);
							List<Side> sideList = new ArrayList<>(game.node(p).sides);
							Side s = sideList.get(r.nextInt(sideList.size()));
							game.node(p).sides.remove(s);
							if (before != accessibleBulbCount(game, null, null)) {
								game.node(p).sides.add(s);
								k--;
								continue;
							}
							reduceEmpty(game);
							// reduceUnleaded(game);
						}
					}
				}
			}
		}
	}

	/**
	 * Recursively counts the number of accessible bulb nodes in the game starting
	 * 
	 * The function traverses the game grid by following connected nodes, ensuring
	 * that each node is only visited once to prevent cycles.
	 * 
	 * @param game     The game instance
	 * @param position The current position in the grid being checked. If null, the
	 *                 search begins from the power node.
	 * @param visited  A set of positions already checked to avoid cycles.
	 * @return The count of bulb nodes accessible from the power node.
	 */
	protected int accessibleBulbCount(Game game, Position position, Set<Position> visited) {
		if (position == null || visited == null) {
			// Find the power node
			for (int i = 1; i <= game.rows(); i++) {
				for (int j = 1; j <= game.cols(); j++) {
					Position p = new Position(i, j);
					if (game.node(p).type == Type.POWER) {
						Set<Position> visit = new HashSet<>();
						return accessibleBulbCount(game, p, visit);
					}
				}
			}
			// No power node
			return 0;
		}
		if (visited.contains(position)) {
			return 0;
		}
		visited.add(position);
		int count = 0;
		if (game.node(position).isBulb()) {
			return 1;
		}
		for (GameNode n : game.neighours(position)) {
			if (visited.contains(n))
				continue;

			Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
			double roun = v.angle();
			Side sid1 = Side.values()[(int) ((-roun / Math.PI) * 2 + 5) % 4];
			Side sid2 = Side.values()[(int) ((-roun / Math.PI) * 2 + 7) % 4];
			if (game.node(position).sides.contains(sid1) && n.sides.contains(sid2)) {
				count += accessibleBulbCount(game, n.getPosition(), visited);
			}

		}
		return count;

	}
}

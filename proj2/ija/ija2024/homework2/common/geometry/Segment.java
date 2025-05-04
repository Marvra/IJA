package ija.ija2024.homework2.common.geometry;

import java.util.List;

import ija.ija2024.homework2.common.Position;

public class Segment extends Line {
	public Segment(Point start, Point end) {
		super(start, end);
	}

	/**
	 * Returns a list of Positions representing the rasterized version of the
	 * segment.
	 * The algorithm walks along the segment in steps of 1 pixel (i.e. the length of
	 * the vector divided by the length of the segment), and at each step, the
	 * position is added to the list. If the position is not already in the list
	 * (to avoid duplicates), it is added. If the segment is vertical or horizontal,
	 * the position is added only once.
	 * 
	 * @return The list of positions.
	 */
	public List<Position> rasterize() {
		List<Position> positions = new java.util.ArrayList<>();
		double parameter = 0;
		Position pos;
		do {
			pos = this.getPoint(parameter).toPosition();
			if (!positions.contains(pos)) {
				if (!positions.isEmpty()) {
					Position lastPosition = positions.get(positions.size() - 1);
					if (lastPosition.getRow() != pos.getRow() && lastPosition.getCol() != pos.getCol()) {
						positions.add(new Position(lastPosition.getRow(), pos.getCol()));
					}
				}
				positions.add(pos);
			}
			parameter += 1 / Math.floor(this.vector.length());
		} while (parameter <= 1);
		return positions;
	}

	/**
	 * Gets a point on the segment, clamping the parameter to the range [0, 1].
	 * 
	 * @param parameter
	 *                  The parameter to determine the point on the segment.
	 *                  Values less than 0 are clamped to 0, and values greater
	 *                  than 1 are clamped to 1.
	 * @return The point on the segment at the clamped parameter.
	 */
	@Override
	public Point getPoint(double parameter) {
		if (parameter < 0 || parameter > 1) {
			return super.getPoint(parameter > 1 ? 1 : 0);
		}
		return super.getPoint(parameter);
	}

	public static Point intersection(Segment first, Segment second) {
		Point ret = Line.intersection(new Line(first), new Line(second));
		if (ret == null) {
			return null;
		}
		if (ret.distance(second.start) + ret.distance(second.getPoint(1)) - second.length() < 0.001
				&& ret.distance(first.start) + ret.distance(first.getPoint(1)) - first.length() < 0.001) {
			return ret;
		}
		return null;
	}

	/**
	 * Calculates the length of the segment using the vector from the start to end
	 * points.
	 *
	 * @return The length of the segment.
	 */
	public double length() {
		return vector.length();
	}
}

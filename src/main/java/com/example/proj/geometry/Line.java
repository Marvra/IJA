/**
 * Line.java
 * 
 * This class represents a line in 2D space.
 * 
 * Authors:
 * - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 */

package com.example.proj.common.geometry;

public class Line {
	protected final Point start;
	protected final Vector vector;

	public Line(Point start, Point end) {
		this.start = start;
		this.vector = new Vector(start, end);
	}

	public Line(Point start, Vector vector) {
		this.start = start;
		this.vector = vector;
	}

	public Line(Segment segment) {
		this.start = segment.start;
		this.vector = segment.vector;
	}

	/**
	 * Gets a point on the line, given by a parameter. The parameter is given as
	 * 
	 * @param parameter
	 *                  The parameter of the line.
	 * @return The point on the line at the given parameter.
	 */
	public Point getPoint(double parameter) {
		return new Point(start.getX() + vector.getX() * parameter, start.getY() + vector.getY() * parameter);
	}

	/**
	 * Gets a point on the line, given by its X coordinate. The parameter is given
	 * as
	 * 
	 * @param x
	 *          The X coordinate of the point on the line.
	 * @return The point on the line at the given parameter.
	 */
	public Point getPointByX(double x) {
		return getPoint((x - start.getX()) / vector.getX());
	}

	/**
	 * Gets a point on the line, given by its Y coordinate. The parameter is given
	 * as
	 * 
	 * @param y
	 *          The Y coordinate of the point on the line.
	 * @return The point on the line at the given parameter.
	 */
	public Point getPointByY(double y) {
		return getPoint((y - start.getY()) / vector.getY());
	}

	public static double distance(Line line, Point point) {
		try {
			Vector v0 = line.vector;
			Vector v1 = new Vector(line.start, point);
			double dot = Vector.dot(v0, v1);
			double len = v0.length();
			Vector v2 = new Vector(v0.getX() * dot / (len * len), v0.getY() * dot / (len * len));
			return point.distance(new Point(line.start.getX() + v2.getX(), line.start.getY() + v2.getY()));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return Double.POSITIVE_INFINITY;
		}
	}

	/**
	 * Calculates the distance between two lines. If the lines are parallel, this
	 * function returns the distance between one of the lines and the start point of
	 * the other line. If the lines are not parallel, this function returns 0.
	 * 
	 * @param line1
	 *            The first line.
	 * @param line2
	 *            The second line.
	 * @return The distance between the two lines.
	 */
	public static double distance(Line line1, Line line2) {
		try {
			if (line1.vector.clone().normalize().equals(line2.vector.clone().normalize())) {
				return distance(line1, line2.start);
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return Double.POSITIVE_INFINITY;
		}
	}

	/**
	 * Checks if a point is on a line. The point is considered to be on the line
	 * if the distance between the point and the line is less than or equal to 0.001.
	 * 
	 * @param line
	 *            The line to check.
	 * @param point
	 *            The point to check.
	 * @return True if the point is on the line, false otherwise.
	 */
	public static boolean isOn(Line line, Point point) {
		return distance(line, point) <= 0.001;
	}

	/**
	 * Calculates the intersection point between two lines. If the lines are
	 * parallel, this method returns null. The intersection point is calculated by
	 * solving the equation system that describes the two lines. If the lines are
	 * coincident, the method returns the start point of one of the lines.
	 * 
	 * @param first
	 *            The first line.
	 * @param second
	 *            The second line.
	 * @return The intersection point of the two lines.
	 */
	public static Point intersection(Line first, Line second) {
		if (distance(first, second) >= (Math.ulp(1.0) * 1024))
			return null;
		Point p0 = first.start;
		Point p1 = second.start;
		Vector v0 = first.vector.clone().normalize();
		Vector v1 = second.vector.clone().normalize();
		double parameter = (v1.getX() * (p1.getY() - p0.getY()) + v1.getY() * (p0.getX() - p1.getX()))
				/ (v1.getX() * v0.getY() - v1.getY() * v0.getX());
		return new Line(p0, v0).getPoint(parameter);
	}
}

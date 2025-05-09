/**
 * Point.java
 * 
 * This class represents a point in 2D space.
 * 
 * Authors:
 * - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 */

package com.example.proj.common.geometry;

import com.example.proj.Position;

public class Point {
	private final double x;
	private final double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Converts a Position object to a Point object.
	 *
	 * @param p The Position to convert.
	 * @return The converted Point object.
	 */
	public static Point fromPosition(Position p) {
		return new Point(p.getRow(), p.getCol());
	}

	/**
	 * Converts this Point object to a Position object by rounding the
	 * x and y coordinates to the nearest integers.
	 *
	 * @return The corresponding Position object.
	 */

	public Position toPosition() {
		return new Position((int) Math.round(this.x), (int) Math.round(this.y));
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	/**
	 * Calculates the Euclidean distance between this point and another point.
	 *
	 * @param other The other point.
	 * @return The distance between the two points.
	 */
	public double distance(Point other) {
		double dx = this.x - other.x;
		double dy = this.y - other.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Returns a string representation of the point in the format [x, y].
	 * 
	 * @return A string representation of the point.
	 */
	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}

package com.example.proj.geometry;

public class Line {
	protected final Point start;
	protected final Vector vector;

	public Line(Point start, Point end) {
		this.start = start;
		this.vector = new Vector(start, end);
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
	 * Gets a point on the line, given by its X coordinate. The parameter is given as
	 * 
	 * @param x
	 *                  The X coordinate of the point on the line.
	 * @return The point on the line at the given parameter.
	 */
	public Point getPointByX(double x) {
		return getPoint((x - start.getX()) / vector.getX());
	}

	/**
	 * Gets a point on the line, given by its Y coordinate. The parameter is given as
	 * 
	 * @param y
	 *                  The Y coordinate of the point on the line.
	 * @return The point on the line at the given parameter.
	 */
	public Point getPointByY(double y) {
		return getPoint((y - start.getY()) / vector.getY());
	}
}

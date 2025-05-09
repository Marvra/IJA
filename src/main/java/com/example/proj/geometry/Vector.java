/**
 * Vector.java
 * 
 * This class represents a vector in 2D space.
 * Contains some basic vector operations, like dot product, angle and normalization.
 * 
 * Authors:
 * - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 */

package com.example.proj.common.geometry;

public class Vector implements Cloneable {
	private double x;
	private double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector(Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}

	public Vector(Point start, Point end) {
		this.x = end.getX() - start.getX();
		this.y = end.getY() - start.getY();
	}

	/**
	 * Returns the X component of the vector.
	 *
	 * @return The X coordinate value.
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Returns the Y component of the vector.
	 *
	 * @return The Y coordinate value.
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Calculates the length (magnitude) of the vector, which is the
	 * Euclidean distance from the origin to the point (x, y) represented
	 * by the vector.
	 *
	 * @return The length of the vector.
	 */
	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	/**
	 * Normalizes the vector in-place. The vector is divided by its length, making
	 * it a unit vector with the same direction as the original vector.
	 *
	 * @return The normalized vector (this).
	 */
	public Vector normalize() {
		double length = this.length();
		this.x /= length;
		this.y /= length;
		return this;
	}

	/**
	 * Calculates the dot product of two vectors.
	 *
	 * @param a
	 *          The first vector.
	 * @param b
	 *          The second vector.
	 * @return The dot product of the two vectors.
	 */
	public static double dot(Vector a, Vector b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}

	/**
	 * Gets the angle of the vector in radians. The angle is measured from the
	 * positive x-axis to the vector in the counter-clockwise direction.
	 *
	 * @return The angle of the vector in radians.
	 */
	public double angle() {
		return Math.atan2(this.y, this.x);
	}

	/**
	 * Returns a copy of the vector.
	 *
	 * @return A copy of the vector.
	 */
	@Override
	public Vector clone() {
		return new Vector(this.x, this.y);
	}

	/**
	 * Returns a string representation of the vector, in the format (x, y).
	 *
	 * @return A string representation of the vector.
	 */
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}

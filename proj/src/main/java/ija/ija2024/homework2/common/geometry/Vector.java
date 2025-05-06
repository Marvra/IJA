package ija.ija2024.homework2.common.geometry;

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

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double length() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}

	public Vector normalize() {
		double length = this.length();
		this.x /= length;
		this.y /= length;
		return this;
	}

	public static double dot(Vector a, Vector b) {
		return a.getX() * b.getX() + a.getY() * b.getY();
	}

	public double angle() {
		return Math.atan2(this.y, this.x);
	}

	@Override
	public Vector clone() {
		return new Vector(this.x, this.y);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}

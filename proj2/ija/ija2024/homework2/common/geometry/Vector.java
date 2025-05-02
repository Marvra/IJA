package ija.ija2024.homework2.common.geometry;

public class Vector {
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
}

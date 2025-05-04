package com.example.proj.geometry;

import com.example.proj.Position;

public class Point {
	private final double x;
	private final double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Point fromPosition(Position p) {
		return new Point(p.getRow(), p.getCol());
	}

	public Position toPosition() {
		return new Position((int) Math.round(this.x), (int) Math.round(this.y));
	}

	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
}

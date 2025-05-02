package ija.ija2024.homework2.common.geometry;

import java.util.List;

import ija.ija2024.homework2.common.Position;

public class Segment extends Line {
	public Segment(Point start, Point end) {
		super(start, end);
	}

	public List<Position> rasterize() {
		List<Position> positions = new java.util.ArrayList<>();
		double parameter = 0;
		Position pos;
		do {
			pos = this.getPoint(parameter).toPosition();
			if(!positions.contains(pos))
				positions.add(pos);
			parameter += 1 / Math.floor(this.vector.length());
		} while (parameter <= 1);
		return positions;
	}

	@Override
	public Point getPoint(double parameter) {
		if (parameter < 0 || parameter > 1) {
			return super.getPoint(parameter > 1 ? 1 : 0);
		}
		return super.getPoint(parameter);
	}
}

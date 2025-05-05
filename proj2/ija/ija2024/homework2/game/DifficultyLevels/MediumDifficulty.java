package ija.ija2024.homework2.game.DifficultyLevels;

import java.util.random.*;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.common.Type;
import ija.ija2024.homework2.common.geometry.Point;
import ija.ija2024.homework2.common.geometry.Segment;
import ija.ija2024.homework2.common.geometry.Vector;
import ija.ija2024.homework2.game.Game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediumDifficulty extends GameDifficulty {
    public MediumDifficulty() {
        super.difficulty = GeneralDifficulty.medium;
        super.dimensions = 8;
    }

    @Override
    public Game generate() {
        Game game = Game.create(super.dimensions, super.dimensions);
        List<Position> positions = super.generatePositions(3, 5);
        List<Position> originals = new ArrayList<>(positions);
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
        List<GameNode> nodes = super.convertToLinks(newPositions, game);
        game.print();
        Position position = originals.get(0);
        if (game.node(position).type == Type.EMPTY) {
            // if node is empty, add bulb there with link
            for (GameNode n : game.neighours(position)) {
                if (n.type == Type.LINK) {
                    double roun = ((new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()))
                            .angle()));
                    if (roun < 0) {
                        roun = 2 * Math.PI + roun;
                    }
                    game.createPowerNode(position, Side.values()[(int) ((roun / Math.PI) * 2 + 2) % 4]);
                    Side[] newSides = Arrays.copyOf(n.sides, n.sides.length + 1);
                    newSides[n.sides.length] = Side.values()[(int) ((roun / Math.PI) * 2) % 4];
                    n.setSides(newSides);
                    break;
                }
            }
        } else {
            List<GameNode> empties = game.empties(position);
            for (GameNode n : empties) {
                Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
                double roun = v.angle();
                if (roun < 0) {
                    roun = 2 * Math.PI + roun;
                }
                game.createPowerNode(n.getPosition(), Side.values()[(int) ((roun / Math.PI) * 2 + 2) % 4]);
                Side[] newSides = Arrays.copyOf(n.sides, n.sides.length + 1);
                newSides[n.sides.length] = Side.values()[(int) ((roun / Math.PI) * 2) % 4];
                n.setSides(newSides);
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
                        double roun = ((new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()))
                                .angle()));
                        if (roun < 0) {
                            roun = 2 * Math.PI + roun;
                        }
                        game.createBulbNode(position, Side.values()[(int) ((roun / Math.PI) * 2 + 2) % 4]);
                        Side[] newSides = Arrays.copyOf(n.sides, n.sides.length + 1);
                        newSides[n.sides.length] = Side.values()[(int) ((roun / Math.PI) * 2) % 4];
                        n.setSides(newSides);
                        break;
                    }
                }
            } else {
                List<GameNode> empties = game.empties(position);
                for (GameNode n : empties) {
                    Vector v = new Vector(Point.fromPosition(position), Point.fromPosition(n.getPosition()));
                    double roun = v.angle();
                    if (roun < 0) {
                        roun = 2 * Math.PI + roun;
                    }
                    game.createBulbNode(n.getPosition(), Side.values()[(int) ((roun / Math.PI) * 2 + 2) % 4]);
                    Side[] newSides = Arrays.copyOf(n.sides, n.sides.length + 1);
                    newSides[n.sides.length] = Side.values()[(int) ((roun / Math.PI) * 2) % 4];
                    n.setSides(newSides);
                    break;
                }

            }
            originals.remove(0);
        }
        game.print();
        return game;
    }

}
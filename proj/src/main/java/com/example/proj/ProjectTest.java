/*
 * IJA 2024/25 Project tests
 * Author:  Jakub Ramaseuski, Martin Vrablec, VUT FIT
 * Created: 05/2025
 */
package com.example.proj;

import com.example.proj.game.DifficultyLevels.EasyDifficulty;
import com.example.proj.game.DifficultyLevels.GameDifficulty;
import com.example.proj.game.Game;
import com.example.proj.geometry.Point;
import com.example.proj.geometry.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.proj.Side.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ProjectTest {

    private Game game;

    private final Object def[][] = {
            { "L", 4, 5, NORTH, EAST, SOUTH },
            { "L", 5, 5, NORTH, EAST, WEST },
            { "L", 5, 4, EAST, SOUTH },
            { "L", 4, 6, EAST, SOUTH },
            { "L", 5, 6, NORTH, SOUTH },
            { "L", 3, 6, EAST, WEST },
            { "L", 3, 4, EAST, WEST },
            { "L", 5, 7, EAST, SOUTH },
            { "B", 6, 4, NORTH },
            { "B", 3, 3, NORTH },
            { "B", 2, 6, SOUTH },
            { "B", 4, 7, WEST },
            { "P", 3, 5, EAST, SOUTH }
    };

    // @BeforeEach
    public void setUp() {
        game = Game.create(10, 12);

        for (Object[] n : def) {
            String type = (String) n[0];
            int row = (Integer) n[1];
            int col = (Integer) n[2];
            Position p = new Position(row, col);
            Side sides[] = new Side[n.length - 3];
            for (int i = 3; i < n.length; i++) {
                sides[i - 3] = (Side) n[i];
            }
            switch (type) {
                case "L" -> game.createLinkNode(p, sides);
                case "B" -> game.createBulbNode(p, sides[0]);
                case "P" -> game.createPowerNode(p, sides);
            }
        }

        game.init();
    }

    /**
     * Test of Point to Position conversion.
     */
    @Test
    public void test01() {
        Point p = new Point(3.5, 4.25);
        Position pos = p.toPosition();
        assertEquals(4, pos.getRow(), "Test konverze Point na Position.");
        assertEquals(4, pos.getCol(), "Test konverze Point na Position.");

        p = Point.fromPosition(pos);
        assertEquals(4, p.getX(), "Test konverze Position na Point.");
        assertEquals(4, p.getY(), "Test konverze Position na Point.");
    }

    /**
     * Test of rasterization
     */
    @Test
    public void test02() {
        Segment s = new Segment(new Point(1, 1), new Point(3, 3));
        List<Position> raster = s.rasterize();
        List<Position> expected = Arrays.asList(new Position(1, 1), new Position(1, 2), new Position(2, 2),
                new Position(2, 3), new Position(3, 3));
        assertEquals(expected, raster, "2.1 Test rasterization.");

        s = new Segment(new Point(3, 3), new Point(1, 1));
        raster = s.rasterize();
        expected = Arrays.asList(new Position(3, 3), new Position(3, 2), new Position(2, 2), new Position(2, 1),
                new Position(1, 1));
        assertEquals(expected, raster, "2.2 Test rasterization.");

        s = new Segment(new Point(1, 1), new Point(1, 1));
        raster = s.rasterize();
        expected = Arrays.asList(new Position(1, 1));
        assertEquals(expected, raster, "2.3 Test rasterization.");

        s = new Segment(new Point(3, 3), new Point(15, 15));
        raster = s.rasterize();
        expected = new ArrayList<>();
        for (int i = 3; i <= 15; i++) {
            expected.add(new Position(i, i));
            if (i < 15)
                expected.add(new Position(i, i + 1));
        }

        assertEquals(expected, raster, "2.3 Test rasterization.");

        s = new Segment(new Point(3, 3), new Point(15, 3));
        raster = s.rasterize();
        expected = new ArrayList<>();
        for (int i = 3; i <= 15; i++) {
            expected.add(new Position(i, 3));
        }
        assertEquals(expected, raster, "2.4 Test rasterization.");

        s = new Segment(new Point(3, 3), new Point(3, 20));
        raster = s.rasterize();
        expected = new ArrayList<>();
        for (int i = 3; i <= 20; i++) {
            expected.add(new Position(3, i));
        }
        assertEquals(expected, raster, "2.5 Test rasterization.");

        s = new Segment(new Point(3, 3), new Point(1, 20));
        raster = s.rasterize();
        expected = new ArrayList<>();
        for (Position position : raster) {
            System.out.println(position.getRow() + " " + position.getCol());
        }
        for (int i = 3; i <= 8; i++) {
            expected.add(new Position(3, i));
        }
        for (int i = 8; i <= 16; i++) {
            expected.add(new Position(2, i));
        }
        for (int i = 16; i <= 20; i++) {
            expected.add(new Position(1, i));
        }
        assertEquals(expected, raster, "2.6 Test rasterization.");
    }

    @Test
    public void test03() {
        List<Position> l = new ArrayList<>();
        // single L shape
        Game game = Game.create(6, 6);
        l.add(new Position(2, 2));
        l.add(new Position(2, 3));
        l.add(new Position(3, 3));
        List<GameNode> nodes = GameDifficulty.convertToLinks(l, game);
        assertEquals(3, nodes.size(), "Nodes.size() mismatch. Incorrect number of nodes.");
        assertEquals(nodes.get(0).east() && nodes.get(0).west(), true, "Nodes.get(0) mismatch.");
        assertEquals(nodes.get(1).west() && nodes.get(1).south(), true, "Nodes.get(1) mismatch.");
        assertEquals(nodes.get(2).north() && nodes.get(2).south(), true, "Nodes.get(2) mismatch.");

        // single T shape
        game = Game.create(6, 6);
        l.clear();
        l.add(new Position(2, 3));
        l.add(new Position(3, 2));
        l.add(new Position(3, 3));
        l.add(new Position(4, 3));
        nodes = GameDifficulty.convertToLinks(l, game);
        assertEquals(4, nodes.size(), "Nodes.size() mismatch. Incorrect number of nodes.");
        assertEquals(nodes.get(0).north() && nodes.get(0).south(), true, "Nodes.get(0) mismatch.");
        assertEquals(nodes.get(1).west() && nodes.get(1).east(), true, "Nodes.get(1) mismatch.");
        assertEquals(nodes.get(2).west() && nodes.get(2).north() && nodes.get(2).south(), true,
                "Nodes.get(2) mismatch.");
        assertEquals(nodes.get(3).north() && nodes.get(3).south(), true, "Nodes.get(3) mismatch.");

        // single + shape
        game = Game.create(6, 6);
        l.clear();
        l.add(new Position(2, 3));
        l.add(new Position(3, 2));
        l.add(new Position(3, 3));
        l.add(new Position(4, 3));
        l.add(new Position(3, 4));
        nodes = GameDifficulty.convertToLinks(l, game);
        assertEquals(5, nodes.size(), "Nodes.size() mismatch. Incorrect number of nodes.");
        assertEquals(nodes.get(0).north() && nodes.get(0).south(), true, "Nodes.get(0) mismatch.");
        assertEquals(nodes.get(1).west() && nodes.get(1).east(), true, "Nodes.get(1) mismatch.");
        assertEquals(nodes.get(2).west() && nodes.get(2).east() && nodes.get(2).north() && nodes.get(2).south(), true,
                "Nodes.get(2) mismatch.");
        assertEquals(nodes.get(3).north() && nodes.get(3).south(), true, "Nodes.get(3) mismatch.");
        assertEquals(nodes.get(4).east() && nodes.get(4).west(), true, "Nodes.get(4) mismatch.");
    }

    @Test
    public void test04() {
        EasyDifficulty easyDifficulty = new EasyDifficulty();
        Game game = easyDifficulty.generate();
        game.init();
    }
}
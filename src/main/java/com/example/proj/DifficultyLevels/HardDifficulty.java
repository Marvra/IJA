/**
 * HardDifficulty.java
 * 
 * This class is responsible for generating a game with 5-10 lightbulbs.
 * 
 * Authors:
 * - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 */

package com.example.proj.DifficultyLevels;

import java.util.*;
import java.util.random.*;

import com.example.proj.GameNode;
import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.Type;
import com.example.proj.common.geometry.Point;
import com.example.proj.common.geometry.Segment;
import com.example.proj.common.geometry.Vector;
import com.example.proj.game.Game;

public class HardDifficulty extends GameDifficulty {
    public HardDifficulty() {
        super.difficulty = GeneralDifficulty.hard12x12;
        super.dimensions = 12;
    }

	@Override
    public Game generate() {
        return super.generate(5, 10);
    }

}
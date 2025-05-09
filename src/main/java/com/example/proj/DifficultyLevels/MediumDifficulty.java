/**
 * MediumDifficulty.java
 * 
 * This class is responsible for generating a game with 2-5 lightbulbs.
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

public class MediumDifficulty extends GameDifficulty {
    public MediumDifficulty() {
        super.difficulty = GeneralDifficulty.medium;
        super.dimensions = 8;
    }

    public Game generate() {
        return super.generate(2, 5);
    }

}
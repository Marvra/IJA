/**
 * GameGenerator.java
 * 
 * This class is responsible for generating game instances based on different difficulty levels.
 * It does so by passing the task to one of the difficulty classes.
 * 
 * Authors:
 * - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 * - Martin Vrablec (xvrabl06@stud.fit.vut.cz)
 */

package com.example.proj.game;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.DifficultyLevels.*;
import com.example.proj.game.Game;

import java.util.Random;

public class GameGenerator {
    private int rows, cols;
    private GameDifficulty difficulty;

    private static final Map<GeneralDifficulty, Supplier<GameDifficulty>> difficultyMap = new HashMap<>();

    static {
        difficultyMap.put(GeneralDifficulty.easy, () -> new EasyDifficulty());
        difficultyMap.put(GeneralDifficulty.medium, () -> new MediumDifficulty());
        difficultyMap.put(GeneralDifficulty.hard, () -> new HardDifficulty());
        // TODO: Add more difficulty levels and their corresponding constructors
    }

    public GameGenerator(int rows, int cols, GeneralDifficulty difficulty) {
        this.rows = rows;
        this.cols = cols;
        try {
            this.difficulty = difficultyMap.get(difficulty).get();
        } catch (Exception e) {
            this.difficulty = new GameDifficulty();
        }
    }

    /**
     * Generates a new game based on the chosen difficulty level.
     * @return A Game object with the predefined node setup.
     */
    public Game generate() {
        // creating map
        return this.difficulty.generate();
    }
}

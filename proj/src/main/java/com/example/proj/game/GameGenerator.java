package com.example.proj.game;

import com.example.proj.game.DifficultyLevels.EasyDifficulty;
import com.example.proj.game.DifficultyLevels.GameDifficulty;
import com.example.proj.game.DifficultyLevels.GeneralDifficulty;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GameGenerator {
    private int rows, cols;
    private GameDifficulty difficulty;

    private static final Map<GeneralDifficulty, Supplier<GameDifficulty>> difficultyMap = new HashMap<>();

    static {
        difficultyMap.put(GeneralDifficulty.easy, () -> new EasyDifficulty());
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

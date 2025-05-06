package ija.ija2024.homework2.game;

import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.homework2.game.DifficultyLevels.MediumDifficulty;
import ija.ija2024.homework2.game.Game;
import ija.ija2024.homework2.game.DifficultyLevels.GeneralDifficulty;
import ija.ija2024.homework2.game.DifficultyLevels.EasyDifficulty;
import ija.ija2024.homework2.game.DifficultyLevels.GameDifficulty;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import java.util.Random;

public class GameGenerator {
    private int rows, cols;
    private GameDifficulty difficulty;

    private static final Map<GeneralDifficulty, Supplier<GameDifficulty>> difficultyMap = new HashMap<>();

    static {
        difficultyMap.put(GeneralDifficulty.easy, () -> new EasyDifficulty());
        difficultyMap.put(GeneralDifficulty.medium, () -> new MediumDifficulty());
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

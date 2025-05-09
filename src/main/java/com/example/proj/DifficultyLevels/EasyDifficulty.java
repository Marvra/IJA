/**
 * EasyDifficulty.java
 * 
 * This class is responsible for generating a game with exactly 1 lightbulb.
 * 
 * Authors:
 * - Jakub Ramašeuski (xramas01@stud.fit.vut.cz)
 */

package com.example.proj.DifficultyLevels;

import java.util.Random;

import com.example.proj.GameNode;
import com.example.proj.Position;
import com.example.proj.Side;
import com.example.proj.common.geometry.Point;
import com.example.proj.common.geometry.Segment;
import com.example.proj.game.Game;

import java.util.List;

public class EasyDifficulty extends GameDifficulty {
	public EasyDifficulty() {
		super.difficulty = GeneralDifficulty.easy;
		super.dimensions = 6;
	}

	@Override
	public Game generate() {
		return super.generate(1, 1);
	}
}
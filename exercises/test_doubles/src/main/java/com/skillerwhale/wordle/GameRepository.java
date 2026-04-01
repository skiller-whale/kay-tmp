package com.skillerwhale.wordle;

import com.skillerwhale.wordle.core.GameState;

public interface GameRepository {
    void saveGame(GameState game);
    GameState getGame(String id);
}
package com.zeynep.game.handlers;

import com.zeynep.game.main.Game;
import com.zeynep.game.states.GameState;
import com.zeynep.game.states.Play;

import java.util.Stack;

public class GameStateManager {
    private Game game;
    private Stack<GameState> gameStates;
    public static final int PLAY = 912837;

    public Game getGame() {
        return game;
    }

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    private void pushState(int play) {

        gameStates.push(getState(play));
    }

    public void update(float dt){
        gameStates.peek().update(dt);
    }
    public void render() {
        gameStates.peek().render();
    }

    private GameState getState (int state) {
        if(state == PLAY) return new Play(this);
        return null;
    }


}

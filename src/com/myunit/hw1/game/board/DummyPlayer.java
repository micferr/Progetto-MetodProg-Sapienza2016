package com.myunit.hw1.game.board;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.Player;

public class DummyPlayer<P> implements Player<P> {
    public String name;
    public GameRuler<P> game;

    public DummyPlayer(String n) {
        name = n;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void setGame(GameRuler<P> g) {
        game = g.copy();
    }

    @Override
    public void moved(int i, Move<P> m) {
        game.move(m);
    }

    @Override
    public Move<P> getMove() {
        return (Move<P>)(game.validMoves().toArray()[0]);
    }
}

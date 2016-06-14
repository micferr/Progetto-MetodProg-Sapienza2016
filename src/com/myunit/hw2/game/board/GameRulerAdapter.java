package com.myunit.hw2.game.board;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;

import java.util.List;
import java.util.Set;

public class GameRulerAdapter<P> implements GameRuler<P> {
    @Override
    public String name() {
        return null;
    }

    @Override
    public <T> T getParam(String name, Class<T> c) {
        return null;
    }

    @Override
    public List<String> players() {
        return null;
    }

    @Override
    public String color(String name) {
        return null;
    }

    @Override
    public Board<P> getBoard() {
        return null;
    }

    @Override
    public int turn() {
        return 0;
    }

    @Override
    public boolean move(Move<P> m) {
        return false;
    }

    @Override
    public boolean unMove() {
        return false;
    }

    @Override
    public boolean isPlaying(int i) {
        return false;
    }

    @Override
    public int result() {
        return 0;
    }

    @Override
    public Set<Move<P>> validMoves() {
        return null;
    }

    @Override
    public GameRuler<P> copy() {
        return null;
    }
}

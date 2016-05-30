package com.myunit.hw1.game;

import com.myunit.hw1.game.board.DummyGU;
import gapp.ulg.game.GameFactory;
import gapp.ulg.game.Param;

import java.util.List;
import java.util.Objects;

public class DummyGUFactory implements GameFactory<DummyGU> {
    @Override
    public String name() { return "DummyGU"; }
    @Override
    public int minPlayers() { return 2; }
    @Override
    public int maxPlayers() { return 2; }
    @Override
    public List<Param<?>> params() { return null; }
    @Override
    public void setPlayerNames(String...names) {
        for (String n : names)
            Objects.requireNonNull(n);
        if (names.length != 2) throw new IllegalArgumentException();
        pNames = names;
    }
    @Override
    public DummyGU newGame() {
        if (pNames == null) throw new IllegalStateException();
        return new DummyGU(pNames[0], pNames[1]);
    }

    public String[] pNames;
}

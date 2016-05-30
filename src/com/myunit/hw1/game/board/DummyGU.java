package com.myunit.hw1.game.board;

//import hw1.game.board.
import gapp.ulg.game.board.*;

import java.util.*;

public class DummyGU implements GameRuler<PieceModel<PieceModel.Species>> {
    public DummyGU(String p1, String p2) {
        board = new DummyBoard<>();
        unModB = new Board<PieceModel<PieceModel.Species>>() {
            @Override
            public System system() { return board.system(); }
            @Override
            public int width() { return board.width(); }
            @Override
            public int height() { return board.height(); }
            @Override
            public Pos adjacent(Pos p, Dir d) { return board.adjacent(p, d); }
            @Override
            public List<Pos> positions() { return board.positions(); }
            @Override
            public PieceModel<PieceModel.Species> get(Pos p) { return board.get(p); }
        };
        pNames = Collections.unmodifiableList(Arrays.asList(p1, p2));
    }
    @Override
    public String name() { return "DummyGU"; }
    @Override
    public <T> T getParam(String name, Class<T> c) { return null; }
    @Override
    public List<String> players() { return pNames; }
    @Override
    public String color(String name) {
        Objects.requireNonNull(name);
        if (!pNames.contains(name)) throw new IllegalArgumentException();
        return "white";
    }
    @Override
    public Board<PieceModel<PieceModel.Species>> getBoard() { return unModB; }
    @Override
    public int turn() { return gRes == -1 ? cTurn : 0; }
    @Override
    public boolean move(Move<PieceModel<PieceModel.Species>> m) {
        Objects.requireNonNull(m);
        if (gRes != -1) throw new IllegalArgumentException();
        if (!validMoves().contains(m)) {
            gRes = 3 - cTurn;
            return false;
        }
        board.put(disc, m.actions.get(0).pos.get(0));
        if (validMoves().isEmpty()) gRes = 0;
        cTurn = 3 - cTurn;
        return true;
    }
    @Override
    public boolean unMove() { return false; }
    @Override
    public boolean isPlaying(int i) {
        if (i != 1 && i != 2) throw new IllegalArgumentException();
        if (gRes != -1) return false;
        return true;
    }
    @Override
    public int result() { return gRes; }
    @Override
    public Set<Move<PieceModel<PieceModel.Species>>> validMoves() {
        Set<Move<PieceModel<PieceModel.Species>>> vm = new HashSet<>();
        for (Object p : board.positions()) {
            if (board.get((Pos)p) == null) {
                Move<PieceModel<PieceModel.Species>> m = new Move<>(new Action<>((Pos)p, disc));
                vm.add(m);
            }
        }
        return Collections.unmodifiableSet(vm);
    }
    @Override
    public GameRuler<PieceModel<PieceModel.Species>> copy() {
        return new DummyGU(this);
    }

    private DummyGU(DummyGU g) {
        board = new com.myunit.hw1.game.board.DummyBoard<>(g.board);
        unModB = new Board<PieceModel<PieceModel.Species>>() {
            @Override
            public System system() { return board.system(); }
            @Override
            public int width() { return board.width(); }
            @Override
            public int height() { return board.height(); }
            @Override
            public Pos adjacent(Pos p, Dir d) { return board.adjacent(p, d); }
            @Override
            public List<Pos> positions() { return board.positions(); }
            @Override
            public PieceModel<PieceModel.Species> get(Pos p) { return board.get(p); }
        };
        List<String> nn = new ArrayList<>();
        nn.addAll(g.pNames);
        pNames = Collections.unmodifiableList(nn);
        gRes = g.gRes;
        cTurn = g.cTurn;
    }

    private final DummyBoard<PieceModel<PieceModel.Species>> board;
    private final Board<PieceModel<PieceModel.Species>> unModB;
    private final List<String> pNames;
    private final PieceModel<PieceModel.Species> disc = new PieceModel<>(PieceModel.Species.DISC, "white");
    private int gRes = -1, cTurn = 1;
}
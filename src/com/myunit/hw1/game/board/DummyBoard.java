package com.myunit.hw1.game.board;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.Pos;

import java.util.*;

public class DummyBoard<P> implements Board<P> {
    public DummyBoard() {
        List<Pos> pl = new ArrayList<>();
        for (int b = 0 ; b < 10 ; b++)
            for (int t = 0 ; t < 10 ; t++)
                pl.add(new Pos(b,t));
        posList = Collections.unmodifiableList(pl);
        pmMap = new HashMap<>();
    }
    @Override
    public System system() { return System.OCTAGONAL; }
    @Override
    public int width() { return 10; }
    @Override
    public int height() { return 10; }
    @Override
    public Pos adjacent(Pos p, Dir d) {
        Objects.requireNonNull(p);
        Objects.requireNonNull(d);
        if (!(p.b >= 0 && p.b < 10 && p.t >= 0 && p.t < 10)) return null;
        Disp ds = toDisp.get(d);
        int b = p.b + ds.db, t = p.t + ds.dt;
        if (!(b >= 0 && b < 10 && t >= 0 && t < 10)) return null;
        return new Pos(b, t);
    }
    @Override
    public List<Pos> positions() { return posList; }
    @Override
    public P get(Pos p) {
        Objects.requireNonNull(p);
        return pmMap.get(p);
    }
    @Override
    public boolean isModifiable() { return true; }
    @Override
    public P put(P pm, Pos p) {
        Objects.requireNonNull(pm);
        Objects.requireNonNull(p);
        check(p);
        return pmMap.put(p, pm);
    }
    @Override
    public P remove(Pos p) {
        Objects.requireNonNull(p);
        check(p);
        return pmMap.remove(p);
    }
    public DummyBoard(DummyBoard<P> b) {
        List<Pos> lp = new ArrayList<>();
        lp.addAll(b.posList);
        posList = Collections.unmodifiableList(lp);
        pmMap = new HashMap<>();
        pmMap.putAll(b.pmMap);
    }
    static class Disp {
        final int db, dt;
        Disp(int db, int dt) { this.db = db; this.dt = dt; }
    }
    static EnumMap<Dir, Disp> toDisp = new EnumMap<>(Dir.class);
    static {
        toDisp.put(Dir.UP, new Disp(0,1));
        toDisp.put(Dir.UP_R, new Disp(1,1));
        toDisp.put(Dir.RIGHT, new Disp(1,0));
        toDisp.put(Dir.DOWN_R, new Disp(1,-1));
        toDisp.put(Dir.DOWN, new Disp(0,-1));
        toDisp.put(Dir.DOWN_L, new Disp(-1,-1));
        toDisp.put(Dir.LEFT, new Disp(-1,0));
        toDisp.put(Dir.UP_L, new Disp(-1,1));
    }
    private void check(Pos p) {
        if (!(p.b >= 0 && p.b < 10 && p.t >= 0 && p.t < 10)) throw new IllegalArgumentException();
    }
    private final List<Pos> posList;
    private final Map<Pos,P> pmMap;
}

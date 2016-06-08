package com.myunit.hw2.game.board.util;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.util.Probe;
import gapp.ulg.games.MNKgame;
import gapp.ulg.games.Othello;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestProbe3 {
    public static void main(String[] args) {
        test(false, ()->new MNKgame(-1,3,3,3,"a","b"), 10);
        test(true, ()->new MNKgame(-1,3,3,3,"a","b"), 10);
    }

    public static void test(boolean parallel, Supplier<GameRuler<PieceModel<PieceModel.Species>>> gR, int depth) {
        GameRuler<PieceModel<PieceModel.Species>> game = gR.get();
        Function<Probe.EncS<PieceModel<PieceModel.Species>>,GameRuler.Situation<PieceModel<PieceModel.Species>>>
                dec = e -> e.decode(game.mechanics());

        Function<GameRuler.Situation<PieceModel<PieceModel.Species>>, Probe.EncS<PieceModel<PieceModel.Species>>>
                enc = s -> new Probe.EncS<>(game.mechanics(), s);

        Set<Probe.EncS<PieceModel<PieceModel.Species>>> encSet = new HashSet<>();
        encSet.add(enc.apply(game.mechanics().start));
        for (int i = 0; i < depth; i++) {
            Probe.NSResult<Probe.EncS<PieceModel<PieceModel.Species>>> nS = Probe.nextSituations(parallel, game.mechanics().next,dec,enc,encSet);
            encSet = nS.next;
            int finali = 0;
            int patte = 0;
            int nonFinite = 0;
            for (Probe.EncS<PieceModel<PieceModel.Species>> e : encSet) {
                GameRuler.Situation<PieceModel<PieceModel.Species>> s = dec.apply(e);
                if (s.turn < 0) finali++;
                else if (s.turn == 0) patte++;
                else nonFinite++;
            }
            System.out.println("Depth " + (i+1) + " : Size = " + encSet.size() + " | Finali: " + finali + " | Patte: " + patte + " | Non finite: " + nonFinite);
        }
        System.out.println();
    }
}

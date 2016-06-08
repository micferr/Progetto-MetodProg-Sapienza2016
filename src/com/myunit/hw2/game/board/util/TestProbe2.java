package com.myunit.hw2.game.board.util;

import gapp.ulg.game.board.*;
import gapp.ulg.game.util.Probe;
import gapp.ulg.games.Othello;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestProbe2 {
    public static void main(String[] args) {
        Othello o = new Othello(0,6,"a","b");
        Move<PieceModel<PieceModel.Species>> m = new Move<PieceModel<PieceModel.Species>>(
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,3), new PieceModel<>(PieceModel.Species.DISC, "nero")),
                new Action<>(new PieceModel<>(PieceModel.Species.DISC, "nero"), new Pos(2,3))
        );
        o.move(m);
        Set<Probe.EncS<PieceModel<PieceModel.Species>>> start = new HashSet<>();
        start.add(new Probe.EncS<>(o.mechanics(),o.toSituation()));
        Probe.NSResult<Probe.EncS<PieceModel<PieceModel.Species>>> NSR = Probe.nextSituations(
                false,
                o.mechanics().next,
                e -> e.decode(o.mechanics()),
                s -> new Probe.EncS<>(o.mechanics(), s),
                start
        );
        NSR = Probe.nextSituations(
                false,
                o.mechanics().next,
                e -> e.decode(o.mechanics()),
                s -> new Probe.EncS<>(o.mechanics(), s),
                NSR.next
        );
        for (Probe.EncS<PieceModel<PieceModel.Species>> e : NSR.next) {
            GameRuler.Situation s = e.decode(o.mechanics());
            printSituation8x8(s);
        }
    }

    public static void printSituation8x8(GameRuler.Situation<PieceModel<PieceModel.Species>> situation) {
        Map<Pos, PieceModel<PieceModel.Species>> pMap = situation.newMap();
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 6; i++) {
                if (pMap.get(new Pos(i,j)) == null) System.out.print(". ");
                else if (pMap.get(new Pos(i,j)).color.equals("nero")) System.out.print("N ");
                else System.out.print("B ");
            }
            System.out.println();
        }
        boolean gameEnded = situation.turn <= 0;
        System.out.println("gameEnded: " + gameEnded);
    }
}

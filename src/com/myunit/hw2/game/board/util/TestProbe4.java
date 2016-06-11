package com.myunit.hw2.game.board.util;

import gapp.ulg.game.board.*;
import gapp.ulg.game.util.Probe;
import gapp.ulg.games.MNKgame;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TestProbe4 {

    public static PieceModel<PieceModel.Species> blackPiece = new MNKgame(-1,3,3,3,"a","b").mechanics().pieces.get(0);
    public static PieceModel<PieceModel.Species> whitePiece = new MNKgame(-1,3,3,3,"a","b").mechanics().pieces.get(1);

    public static Move<PieceModel<PieceModel.Species>>
            moveX00 = new Move<>(new Action<>(new Pos(0,0), blackPiece)),
            moveX01 = new Move<>(new Action<>(new Pos(0,1), blackPiece)),
            moveX02 = new Move<>(new Action<>(new Pos(0,2), blackPiece)),
            moveX10 = new Move<>(new Action<>(new Pos(1,0), blackPiece)),
            moveX11 = new Move<>(new Action<>(new Pos(1,1), blackPiece)),
            moveX12 = new Move<>(new Action<>(new Pos(1,2), blackPiece)),
            moveX20 = new Move<>(new Action<>(new Pos(2,0), blackPiece)),
            moveX21 = new Move<>(new Action<>(new Pos(2,1), blackPiece)),
            moveX22 = new Move<>(new Action<>(new Pos(2,2), blackPiece)),
            moveO00 = new Move<>(new Action<>(new Pos(0,0), whitePiece)),
            moveO01 = new Move<>(new Action<>(new Pos(0,1), whitePiece)),
            moveO02 = new Move<>(new Action<>(new Pos(0,2), whitePiece)),
            moveO10 = new Move<>(new Action<>(new Pos(1,0), whitePiece)),
            moveO11 = new Move<>(new Action<>(new Pos(1,1), whitePiece)),
            moveO12 = new Move<>(new Action<>(new Pos(1,2), whitePiece)),
            moveO20 = new Move<>(new Action<>(new Pos(2,0), whitePiece)),
            moveO21 = new Move<>(new Action<>(new Pos(2,1), whitePiece)),
            moveO22 = new Move<>(new Action<>(new Pos(2,2), whitePiece));

    public static void main(String[] args) {
        /*MNKgame game = new MNKgame(-1,3,3,3,"a","b");
        Function<Probe.EncS<PieceModel<PieceModel.Species>>,GameRuler.Situation<PieceModel<PieceModel.Species>>>
                dec = e -> e.decode(game.mechanics());

        Function<GameRuler.Situation<PieceModel<PieceModel.Species>>, Probe.EncS<PieceModel<PieceModel.Species>>>
                enc = s -> new Probe.EncS<>(game.mechanics(), s);

        Set<Probe.EncS<PieceModel<PieceModel.Species>>> encSet = new HashSet<>();
        encSet.add(enc.apply(game.mechanics().start));
        for (int i = 0; i < 9; i++) {
            encSet = Probe.nextSituations(true, game.mechanics().next,dec,enc,encSet).next;
            System.out.println("Depth " + (i+1) + " : Size = " + encSet.size());
        }
        encSet.forEach(e -> printSit(dec.apply(e)));*/

        //--------------------

        //try {Thread.sleep(1000); } catch (Exception e) {}
        MNKgame g = new MNKgame(-1,3,3,3,"a","b");
        g.move(moveX12);
        g.move(moveO20);
        g.move(moveX22);
        g.move(moveO00);
        g.move(moveX10);
        g.move(moveO11);
        g.move(moveX21);
        g.move(moveO01);
        g.printBoard();
        //g.move(move)
    }

    public static void printSit(GameRuler.Situation<PieceModel<PieceModel.Species>> sit) {
        Map<Pos, PieceModel<PieceModel.Species>> map = sit.newMap();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                printPos(map, i, j);
            }
            System.out.println();
        }
        System.out.println("Turno: " + sit.turn + "\n");
    }

    public static void printPos(Map<Pos, PieceModel<PieceModel.Species>> map, int x, int y) {
        PieceModel<PieceModel.Species> piece = map.get(new Pos(x,y));
        if (piece == null) System.out.print(".");
        else if (piece.equals(blackPiece)) System.out.print("X");
        else System.out.print("O");
    }
}

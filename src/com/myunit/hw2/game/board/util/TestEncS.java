package com.myunit.hw2.game.board.util;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;
import gapp.ulg.game.util.Probe;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.Othello;
import gapp.ulg.games.OthelloFactory;
import gapp.ulg.play.RandPlayer;

import java.util.Map;

public class TestEncS {
    public static void main(String[] args) {
        OthelloFactory oF = OthelloFDefault();
        RandPlayer<PieceModel<PieceModel.Species>>
                p1 = new RandPlayer<>("a"),
                p2 = new RandPlayer<>("b");
        Othello othello = (Othello)Utils.play(oF, p1, p2);
        //othello.unMove();
        Probe.EncS<PieceModel<PieceModel.Species>> enc = new Probe.EncS<>(othello.mechanics(), othello.toSituation());
        printBoard(othello);
        printSituation(enc.decode(othello.mechanics()), othello.mechanics());
        System.out.println("Turno Situation: " + othello.toSituation().turn);
        System.out.println("Result Othello: " + othello.result());
    }

    private static OthelloFactory OthelloFDefault() {
        return new OthelloFactory();
    }

    public static void printBoard(Othello o) {
        int boardSize;
        switch (o.mechanics().positions.size()) {
            case 36: boardSize=6; break;
            case 64: boardSize=8; break;
            case 100: boardSize=10; break;
            case 144: boardSize=12; break;
            default: throw new IllegalArgumentException("Num Positions: " + o.mechanics().positions.size());
        }
        for (int i = boardSize-1; i >= 0; i--) {
            for (int j = 0; j < boardSize; j++) {
                if (o.getBoard().get(new Pos(j, i)) == null) System.out.print(". ");
                else if (o.getBoard().get(new Pos(j, i)).color.equals("nero")) System.out.print("N ");
                else System.out.print("B ");
            }
            System.out.println();
        }
        System.out.println("Turno: " + o.turn() + "\n");
    }

    public static void printSituation(
            GameRuler.Situation<PieceModel<PieceModel.Species>> situation,
            GameRuler.Mechanics<PieceModel<PieceModel.Species>> mechanics)
    {
        int boardSize;
        switch (mechanics.positions.size()) {
            case 36: boardSize=6; break;
            case 64: boardSize=8; break;
            case 100: boardSize=10; break;
            case 144: boardSize=12; break;
            default: throw new IllegalArgumentException("Num Positions: " + mechanics.positions.size());
        }
        Map<Pos, PieceModel<PieceModel.Species>> sitMap = situation.newMap();
        for (int i = boardSize-1; i >= 0; i--) {
            for (int j = 0; j < boardSize; j++) {
                if (sitMap.get(new Pos(j, i)) == null) System.out.print(". ");
                else if (sitMap.get(new Pos(j, i)).color.equals("nero")) System.out.print("N ");
                else System.out.print("B ");
            }
            System.out.println();
        }
        System.out.println("Situation.turn: " + situation.turn + "\n");
    }
}

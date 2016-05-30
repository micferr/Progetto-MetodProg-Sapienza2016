package com.myunit.hw2.play;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.MNKgameFactory;
import gapp.ulg.games.OthelloFactory;
import gapp.ulg.play.MCTSPlayer;
import gapp.ulg.play.RandPlayer;

public class TestMCTSPlayer {
    public static void main(String[] args) {
        doTest(false, 100, 100, "No limit");
        doTest(true,  100, 100, "No limit");
    }

    private static void doTest(boolean parallel, int rollouts, int times, String timePerMove) {
        System.out.println(parallel ? "PARALLEL" : "SEQUENTIAL");
        long startTime = System.currentTimeMillis();
        int win1 = 0, win2 = 0, tie = 0;
        try {
            System.out.print("Partite completate: ");
            for (int i = 0; i < times; i++) {
                MNKgameFactory mnkGF = new MNKgameFactory();
                mnkGF.params().get(0).set(timePerMove);
                mnkGF.params().get(1).set(10);
                mnkGF.params().get(2).set(10);
                mnkGF.params().get(3).set(6);
                GameRuler gR = Utils.play(
                        mnkGF,
                        new MCTSPlayer<PieceModel<PieceModel.Species>>("a", rollouts, parallel),
                        new RandPlayer<PieceModel<PieceModel.Species>>("a")
                );
                switch (gR.result()) {
                    case 0:
                        tie++;
                        break;
                    case 1:
                        win1++;
                        break;
                    case 2:
                        win2++;
                        break;
                    default:
                        throw new RuntimeException(gR.result() + "");
                }
                System.out.print((i+1) + " ");
            }
            System.out.println();
        } catch (Exception e) {System.out.println(e.getClass());/*e.printStackTrace();*/}
        System.out.println("Win1: " + win1);
        System.out.println("Win2: " + win2);
        System.out.println("Ties: " + tie);
        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + ((endTime-startTime)/1000.0) + " seconds");
    }
}

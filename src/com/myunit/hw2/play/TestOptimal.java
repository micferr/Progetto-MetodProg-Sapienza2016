package com.myunit.hw2.play;

import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Player;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.MNKgame;
import gapp.ulg.games.MNKgameFactory;
import gapp.ulg.games.Othello;
import gapp.ulg.games.OthelloFactory;
import gapp.ulg.play.OptimalPlayerFactory;
import gapp.ulg.play.RandPlayer;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class TestOptimal {

    public static void main(String[] args) {
        /*tryOrPrintStackTrace(() -> measureTime(() -> testOptimalOthello6x6(false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalOthello6x6(true, null)));*/
        //tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(4, 3, 4, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(5, 3, 5, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(4, 3, 4, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(5, 3, 5, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, false, "cartellainesistente")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, false, "cartellainesistente")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, true, "cartellainesistente")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, true, "cartellainesistente")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, false, "strategies")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, false, "strategies")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, true, "strategies")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, true, "strategies")));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(2, 2, 2, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(3, 3, 3, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(5, 5, 2, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(5, 5, 2, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(6, 6, 2, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(6, 6, 2, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(4, 4, 3, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(4, 4, 3, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(7, 7, 2, true, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(7, 7, 2, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(4, 4, 4, false, null)));
        tryOrPrintStackTrace(() -> measureTime(() -> testOptimalMNK(4, 4, 4, true, null)));
        measureTime(()->checkInterruptSequential(4, 4, 3, false));
        measureTime(()->checkInterruptSequential(4, 4, 3, true));
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    public static void tryOrPrintStackTrace(Runnable r) {
        try {
            r.run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void checkInterruptSequential(int m, int n, int k, boolean parallel) {
        MNKgameFactory gF = new MNKgameFactory();
        gF.setPlayerNames("a", "b");
        gF.params().get(1).set(m);
        gF.params().get(2).set(n);
        gF.params().get(3).set(k);
        MNKgame game = (MNKgame) gF.newGame();
        OptimalPlayerFactory<PieceModel<PieceModel.Species>> pF = new OptimalPlayerFactory<>();
        class MyInterrupt implements Supplier<Boolean> {
            Thread t;
            AtomicInteger i;

            MyInterrupt() {
                i = new AtomicInteger(0);
                t = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    i.incrementAndGet();
                });
                t.run();
            }

            @Override
            public Boolean get() {
                return i.get() == 1;
            }
        }
        pF.tryCompute(gF, parallel, new MyInterrupt());
    }

    private static void measureTime(Runnable r) {
        long startTime = System.currentTimeMillis();
        r.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo: " + ((endTime - startTime) / 1000.0) + " secondi");
    }

    public static void testOptimalMNK(int m, int n, int k, boolean parallel, String directory) {
        System.out.println("Testing " + m + "," + n + "," + k + "-game, " + (parallel ? "Parallel" : "Sequential") + ", "
                + (directory != null ? "Saving to/Loading from " + directory : "Without setting a directory"));
        MNKgameFactory gF = new MNKgameFactory();
        gF.setPlayerNames("a", "b");
        if ((m >= k || n >= k) && (m > 3 || n > 3)) {
            gF.params().get(1).set(m);
            gF.params().get(2).set(n);
            gF.params().get(3).set(k);
        } else {
            gF.params().get(3).set(k);
            gF.params().get(2).set(n);
            gF.params().get(1).set(m);
        }
        MNKgame game = (MNKgame) gF.newGame();
        OptimalPlayerFactory<PieceModel<PieceModel.Species>> pF = new OptimalPlayerFactory<>();
        if (directory != null) pF.setDir(Paths.get("strategies"));
        pF.tryCompute(gF, parallel, () -> false);
        Player oP = pF.newPlayer(gF, "a");
        int win = 0, tie = 0, loss = 0;
        for (int i = 0; i < 1000; ++i) {
            MNKgame res = (MNKgame) Utils.play(gF, Objects.requireNonNull(oP), new RandPlayer<PieceModel<PieceModel.Species>>("b"));
            if (res.result() == 0) tie++;
            else if (res.result() == 1) win++;
            else loss++;
        }
        System.out.println("Win: " + win + " - Loss: " + loss + " - Tie: " + tie);
    }

    public static void testOptimalOthello6x6(boolean parallel, String directory) {
        System.out.println("Testing Othello6x6, " + (parallel ? "Parallel" : "Sequential") + ", "
                + (directory != null ? "Saving to/Loading from " + directory : "Without setting a directory"));
        OthelloFactory gF = new OthelloFactory();
        gF.setPlayerNames("a", "b");
        gF.params().get(1).set("6x6");
        Othello game = (Othello) gF.newGame();
        OptimalPlayerFactory<PieceModel<PieceModel.Species>> pF = new OptimalPlayerFactory<>();
        if (directory != null) pF.setDir(Paths.get("strategies"));
        pF.tryCompute(gF, parallel, () -> false);
        Player oP = pF.newPlayer(gF, "a");
        int win = 0, tie = 0, loss = 0;
        for (int i = 0; i < 1000; ++i) {
            Othello res = (Othello) Utils.play(gF, Objects.requireNonNull(oP), new RandPlayer<PieceModel<PieceModel.Species>>("b"));
            if (res.result() == 0) tie++;
            else if (res.result() == 1) win++;
            else loss++;
        }
        System.out.println("Win: " + win + " - Loss: " + loss + " - Tie: " + tie);
    }
}

package com.myunit.hw2.game.board.util;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.util.Probe;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.Othello;
import gapp.ulg.games.OthelloFactory;
import gapp.ulg.play.RandPlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class TestProbe {

    public static void main(String[] args) {
        measureTime(()->test2(false));
        measureTime(()->test2(true));
        test3(false);
        test3(true);
    }

    public static void measureTime(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo di tutto il test: " + ((endTime-startTime)/1000.0));
    }

    public static void testWithTimeout(Runnable runnable, long millis) {
        Thread t = new Thread(runnable);
        t.start();
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {}
        t.interrupt();
    }

    public static void test1() {
        Othello o = new Othello("a","b");
        GameRuler.Situation<PieceModel<PieceModel.Species>> oSit = o.toSituation();
        Set<Probe.EncS<PieceModel<PieceModel.Species>>> start = new HashSet<>();
        start.add(new Probe.EncS<>(o.mechanics(),oSit));
        Othello endO = (Othello)Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
        endO.unMove();
        start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
        endO.unMove();
        start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
        endO.unMove();
        start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));

        Function<Probe.EncS<PieceModel<PieceModel.Species>>,GameRuler.Situation<PieceModel<PieceModel.Species>>>
                dec = e -> e.decode(o.mechanics());

        Function<GameRuler.Situation<PieceModel<PieceModel.Species>>, Probe.EncS<PieceModel<PieceModel.Species>>>
                enc = s -> new Probe.EncS<>(o.mechanics(), s);

        Probe.NSResult<Probe.EncS<PieceModel<PieceModel.Species>>> nextEncS =
                Probe.nextSituations(
                        false,
                        o.mechanics().next,
                        dec,
                        enc,
                        start
                );
        System.out.println("Prossime situazioni: " + nextEncS.next.size());
        System.out.println("Minimo grado: " + nextEncS.min);
        System.out.println("Massimo grado: " + nextEncS.max);
        System.out.println("Somma gradi: " + nextEncS.sum);
    }

    public static void test2(boolean parallel) {
        Set<Probe.EncS<PieceModel<PieceModel.Species>>> start = new HashSet<>();
        Othello o;

        for (int i = 0; i < 100; i++) {
            o = new Othello("a", "b");
            GameRuler.Situation<PieceModel<PieceModel.Species>> oSit = o.toSituation();
            start.add(new Probe.EncS<>(o.mechanics(), oSit));
            Othello endO = (Othello) Utils.play(
                    new OthelloFactory(),
                    new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                    new RandPlayer<PieceModel<PieceModel.Species>>("b")
            );
            start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
            while (endO.unMove()) start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
        }

        Function<Probe.EncS<PieceModel<PieceModel.Species>>,GameRuler.Situation<PieceModel<PieceModel.Species>>>
                dec = e -> e.decode(new Othello("a","b").mechanics());

        Function<GameRuler.Situation<PieceModel<PieceModel.Species>>, Probe.EncS<PieceModel<PieceModel.Species>>>
                enc = s -> new Probe.EncS<>(new Othello("a","b").mechanics(), s);

        long startTime = System.currentTimeMillis();
        Probe.NSResult<Probe.EncS<PieceModel<PieceModel.Species>>> nextEncS =
                Probe.nextSituations(
                        parallel,
                        new Othello("a","b").mechanics().next,
                        dec,
                        enc,
                        start
                );
        long endTime = System.currentTimeMillis();
        System.out.println(parallel ? "PARALLELO" : "SEQUENZIALE");
        System.out.println("Tempo impiegato: " + ((endTime-startTime)/1000.0) + " secondi");
        if (nextEncS != null) {
            System.out.println("Prossime situazioni: " + nextEncS.next.size());
            System.out.println("Minimo grado: " + nextEncS.min);
            System.out.println("Massimo grado: " + nextEncS.max);
            System.out.println("Somma gradi: " + nextEncS.sum);
        } else {
            System.out.println("Thread interrotto");
        }
        System.out.println();
    }

    public static void test3(boolean parallel) {
        Set<Probe.EncS<PieceModel<PieceModel.Species>>> start = new HashSet<>();
        Othello o;

        for (int i = 0; i < 100; i++) {
            o = new Othello("a", "b");
            GameRuler.Situation<PieceModel<PieceModel.Species>> oSit = o.toSituation();
            start.add(new Probe.EncS<>(o.mechanics(), oSit));
            Othello endO = (Othello) Utils.play(
                    new OthelloFactory(),
                    new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                    new RandPlayer<PieceModel<PieceModel.Species>>("b")
            );
            start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
            while (endO.unMove()) start.add(new Probe.EncS<>(o.mechanics(), endO.toSituation()));
        }

        Function<Probe.EncS<PieceModel<PieceModel.Species>>,GameRuler.Situation<PieceModel<PieceModel.Species>>>
                dec = e -> e.decode(new Othello("a","b").mechanics());

        Function<GameRuler.Situation<PieceModel<PieceModel.Species>>, Probe.EncS<PieceModel<PieceModel.Species>>>
                enc = s -> new Probe.EncS<>(new Othello("a","b").mechanics(), s);

        Thread t = new Thread(()->{Probe.NSResult<Probe.EncS<PieceModel<PieceModel.Species>>> nextEncS =
                Probe.nextSituations(
                        parallel,
                        new Othello("a","b").mechanics().next,
                        dec,
                        enc,
                        start
                );});
        t.start();
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(3000);
            t.interrupt();
            t.join();
        } catch (InterruptedException e) {}
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo: " + ((endTime-startTime)/1000.0) +" secondi");
    }
}

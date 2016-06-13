package com.myunit.hw2.games;

import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.game.board.*;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.Othello;
import gapp.ulg.games.OthelloFactory;
import gapp.ulg.play.RandPlayer;

import java.util.*;

import static com.myunit.assertion.Assert.*;

public class TestOthello {

    //getParam

    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void getParam_NameNull() {
        new Othello("a","b").getParam(null, String.class);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(10)
    public void getParam_CNull() {
        new Othello("a","b").getParam("Time", null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(20)
    public void getParam_EntrambiIParametriNull() {
        new Othello("a","b").getParam(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(40)
    public void getParam_NomeParametroNonValido() {
        new Othello("a","b").getParam("Board size", String.class);
    }

    @Test(expected = ClassCastException.class)
    @Sorted(50)
    public void getParam_ClasseErrataRispettoAlParametro1() {
        new Othello("a","b").getParam("Time", Long.class);
    }

    @Test(expected = ClassCastException.class)
    @Sorted(60)
    public void getParam_ClasseErrataRispettoAlParametro2() {
        new Othello("a","b").getParam("Board", int.class);
    }

    @Test
    @Sorted(70)
    public void getParam_ControllaNessunaEccezioneConParametriValidi() {
        new Othello("a","b").getParam("Time", String.class);
        new Othello("a","b").getParam("Board", String.class);
    }

    @Test
    @Sorted(80)
    public void getParam_ParametriCorretti() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        assertEquals(
                oF.newGame().getParam("Time", String.class),
                "No limit"
        );
        assertEquals(
                oF.newGame().getParam("Board", String.class),
                "8x8"
        );
        oF.params().get(0).set("2m");
        assertEquals(
                oF.newGame().getParam("Time", String.class),
                "2m"
        );
        oF.params().get(1).set("12x12");
        assertEquals(
                oF.newGame().getParam("Board", String.class),
                "12x12"
        );
    }

    // Mechanics

    @Test
    @Sorted(90)
    public void mechanics_ControllaNessunaEccezione() {
        new Othello("a","b").mechanics();
    }

    @Test
    @Sorted(100)
    public void mechanics_ControllaTime() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","a");
        assertEquals(
                oF.newGame().mechanics().time, -1,
                "Errore in time: non -1 ma " + oF.newGame().mechanics().time
        );
        oF.params().get(0).set("2m");
        assertEquals(
                oF.newGame().mechanics().time, 2*60*1000,
                "Errore in time: non " + 2*60*1000 + "ma " + oF.newGame().mechanics().time
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(110)
    public void mechanics_ControllaPiecesImmodificabile() {
        new Othello("a","b").mechanics().pieces.add(new PieceModel<>(PieceModel.Species.DISC, "nero"));
    }

    @Test
    @Sorted(120)
    public void mechanics_ControllaPieces() {
        assertEquals(
                new HashSet<PieceModel<PieceModel.Species>>(new Othello("a","a").mechanics().pieces),
                new HashSet<PieceModel<PieceModel.Species>>(Arrays.asList(
                        new PieceModel<>(PieceModel.Species.DISC, "bianco"),
                        new PieceModel<>(PieceModel.Species.DISC, "nero")
                ))
        );
    }

    @Test
    @Sorted(130)
    public void mechanics_ControllaPositions() {
        List<Pos> positions= new Othello("a","b").mechanics().positions;
        for (int i = 0; i < 64; i++) {
            assertEquals(
                    positions.get(i),
                    new Pos(i/8, i%8)
            );
        }
    }

    @Test
    @Sorted(140)
    public void mechanics_ControllaNumeroGiocatori() {
        assertEquals(
                new Othello("a","b").mechanics().np,
                2
        );
    }

    @Test
    @Sorted(150)
    public void mechanics_Situations_ControllaMappa() {
        //8x8
        HashMap<Pos,PieceModel<PieceModel.Species>> map8x8 = new HashMap<>();
        map8x8.put(new Pos(3,3), new PieceModel<>(PieceModel.Species.DISC, "nero"));
        map8x8.put(new Pos(4,4), new PieceModel<>(PieceModel.Species.DISC, "nero"));
        map8x8.put(new Pos(3,4), new PieceModel<>(PieceModel.Species.DISC, "bianco"));
        map8x8.put(new Pos(4,3), new PieceModel<>(PieceModel.Species.DISC, "bianco"));
        assertEquals(
                new Othello("a","a").mechanics().start.newMap(),
                map8x8
        );
        //6x6
        HashMap<Pos,PieceModel<PieceModel.Species>> map6x6 = new HashMap<>();
        map6x6.put(new Pos(2,2), new PieceModel<>(PieceModel.Species.DISC, "nero"));
        map6x6.put(new Pos(3,3), new PieceModel<>(PieceModel.Species.DISC, "nero"));
        map6x6.put(new Pos(2,3), new PieceModel<>(PieceModel.Species.DISC, "bianco"));
        map6x6.put(new Pos(3,2), new PieceModel<>(PieceModel.Species.DISC, "bianco"));
        assertEquals(
                new Othello(0,6,"a","a").mechanics().start.newMap(),
                map6x6
        );
    }

    @Test
    @Sorted(160)
    public void mechanics_Situations_ControllaTurno() {
        assertEquals(
                new Othello("a","a").mechanics().start.turn,
                1
        );
    }

    @Test
    @Sorted(180)
    public void mechanics_ControllaNextFinePartita() {
        PieceModel<PieceModel.Species> blackPiece = new PieceModel<>(PieceModel.Species.DISC, "nero");
        PieceModel<PieceModel.Species> whitePiece = new PieceModel<>(PieceModel.Species.DISC, "bianco");
        class X {
            public GameRuler.Situation<PieceModel<PieceModel.Species>> toSituation(Othello o) {
                Map<Pos, PieceModel<PieceModel.Species>> oPieceMap = new HashMap<>();
                for (Pos p : o.getBoard().get(blackPiece))
                    oPieceMap.put(p, blackPiece);
                for (Pos p : o.getBoard().get(whitePiece))
                    oPieceMap.put(p, whitePiece);
                int oTurn = (o.turn() == 1 || o.turn() == 2) ? o.turn() : -o.result();
                return new GameRuler.Situation<>(oPieceMap, oTurn);
            }
        }
        X x = new X();

        GameRuler.Next<PieceModel<PieceModel.Species>> next =
                new Othello("a","b").mechanics().next;
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        Othello o = (Othello) Utils.play(
                oF,
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        assertEquals(next.get(x.toSituation(o)), Collections.EMPTY_MAP);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(200)
    public void mechanics_ControllaRisultatoImmutabile() {
        new Othello("a","b").mechanics().positions.add(new Pos(10,10));
    }

    @Test
    @Sorted(230)
    public void mechanics_OggettoRitornatoSempreLoStesso() {
        Othello o = new Othello("a","b");
        assertTrue(o.mechanics() == o.mechanics(), "Test non necessario");
    }
}

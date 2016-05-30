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
    @Sorted(170)
    public void mechanics_ControllaRisultatoDiNextAnizioPartita() {
        PieceModel<PieceModel.Species> blackPiece = new PieceModel<>(PieceModel.Species.DISC, "nero");
        PieceModel<PieceModel.Species> whitePiece = new PieceModel<>(PieceModel.Species.DISC, "bianco");
        class X {
            public GameRuler.Situation<PieceModel<PieceModel.Species>> toSituation(Othello o) {
                Map<Pos, PieceModel<PieceModel.Species>> oPieceMap = new HashMap<>();
                for (Pos p : o.getBoard().get(blackPiece))
                    oPieceMap.put(p, blackPiece);
                for (Pos p : o.getBoard().get(whitePiece))
                    oPieceMap.put(p, whitePiece);
                int oTurn = o.turn();
                return new GameRuler.Situation<>(oPieceMap, oTurn);
            }
        }
        X x = new X();

        //Actual
        Othello o = new Othello("a","b");
        Map<Move<PieceModel<PieceModel.Species>>, GameRuler.Situation<PieceModel<PieceModel.Species>>> actualNext =
                o.mechanics().next.get(x.toSituation(o));

        //Expected
        Action<PieceModel<PieceModel.Species>> add_2_4 = new Action<>(new Pos(2,4), blackPiece);
        Action<PieceModel<PieceModel.Species>> add_3_5 = new Action<>(new Pos(3,5), blackPiece);
        Action<PieceModel<PieceModel.Species>> add_4_2 = new Action<>(new Pos(4,2), blackPiece);
        Action<PieceModel<PieceModel.Species>> add_5_3 = new Action<>(new Pos(5,3), blackPiece);
        Action<PieceModel<PieceModel.Species>> swap_3_4 = new Action<>(blackPiece, new Pos(3,4));
        Action<PieceModel<PieceModel.Species>> swap_4_3 = new Action<>(blackPiece, new Pos(4,3));
        Move<PieceModel<PieceModel.Species>> move1 = new Move<>(add_2_4, swap_3_4);
        Move<PieceModel<PieceModel.Species>> move2 = new Move<>(add_3_5, swap_3_4);
        Move<PieceModel<PieceModel.Species>> move3 = new Move<>(add_4_2, swap_4_3);
        Move<PieceModel<PieceModel.Species>> move4 = new Move<>(add_5_3, swap_4_3);
        o.move(move1);
        GameRuler.Situation<PieceModel<PieceModel.Species>> situation1 = x.toSituation(o);
        o.unMove();
        o.move(move2);
        GameRuler.Situation<PieceModel<PieceModel.Species>> situation2 = x.toSituation(o);
        o.unMove();
        o.move(move3);
        GameRuler.Situation<PieceModel<PieceModel.Species>> situation3 = x.toSituation(o);
        o.unMove();
        o.move(move4);
        GameRuler.Situation<PieceModel<PieceModel.Species>> situation4 = x.toSituation(o);
        Map<Move<PieceModel<PieceModel.Species>>, GameRuler.Situation<PieceModel<PieceModel.Species>>> expectedNext =
                new HashMap<>();
        expectedNext.put(move1, situation1);
        expectedNext.put(move2, situation2);
        expectedNext.put(move3, situation3);
        expectedNext.put(move4, situation4);
        assertEquals(
                actualNext.size(),
                expectedNext.size(),
                "Dimensioni mappa ritornata dall'oggetto Next a inizio partita errate: " + actualNext.size()
        );
        assertEquals(
                actualNext.keySet(),
                expectedNext.keySet(),
                "La mappa ritornata ha chiavi errate"
        );

        if (!expectedNext.equals(actualNext)) {
            int k = 0;
            for (Move<PieceModel<PieceModel.Species>> m : expectedNext.keySet()) {
                System.out.println("Mossa " + (++k));
                System.out.println("\tKind:" + m.kind);
                System.out.println("\tActions: ");
                int i = 0;
                for (Action<PieceModel<PieceModel.Species>> a : m.actions) {
                    System.out.println("\t\tAction " + (i++));
                    System.out.println("\t\t\tKind " + a.kind);
                    System.out.println("\t\t\tPos ");
                    for (Pos p : a.pos) System.out.println("\t\t\t\t" + p.b + " - " + p.t);
                }

                GameRuler.Situation<PieceModel<PieceModel.Species>> actS = actualNext.get(m), expS = expectedNext.get(m);
                System.out.println();
                System.out.println("\tActual:");
                Map<Pos, PieceModel<PieceModel.Species>> actSM = actS.newMap();
                for (Pos p : actSM.keySet()) {
                    String s = blackPiece.equals(actSM.get(p)) ? "B" : whitePiece.equals(actSM.get(p)) ? "W" : "Err";
                    System.out.println("\t\t" + s + " at " + "(" + p.b + ", " + p.t + ")");
                }
                System.out.println("\t\tTurn: " + actS.turn);
                System.out.println();
                System.out.println("\tExpected:");
                Map<Pos, PieceModel<PieceModel.Species>> expSM = actS.newMap();
                for (Pos p : expSM.keySet()) {
                    String s = blackPiece.equals(expSM.get(p)) ? "B" : whitePiece.equals(expSM.get(p)) ? "W" : "Err";
                    System.out.println("\t\t" + s + " at " + "(" + p.b + ", " + p.t + ")");
                }
                System.out.println("\t\tTurn: " + expS.turn);
            }
            fail("Guarda System.out");
        }
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

    @Test
    @Sorted(190)
    public void mechanics_ControllaNextUnTurnoPrimaDellaFinePuoFinireInUnaMossa() {
        /*PieceModel<PieceModel.Species> blackPiece = new PieceModel<>(PieceModel.Species.DISC, "nero");
        PieceModel<PieceModel.Species> whitePiece = new PieceModel<>(PieceModel.Species.DISC, "bianco");
        class X {
            public GameRuler.Situation<PieceModel<PieceModel.Species>> toSituation(Othello o) {
                Map<Pos, PieceModel<PieceModel.Species>> oPieceMap = new HashMap<>();
                for (Pos p : o.getBoard().get(blackPiece))
                    oPieceMap.put(p, blackPiece);
                for (Pos p : o.getBoard().get(whitePiece))
                    oPieceMap.put(p, whitePiece);
                int oTurn = o.result()!=-1 ? o.turn() : -o.result();
                return new GameRuler.Situation<>(oPieceMap, oTurn);
            }
        }
        X x = new X();
        int success=0;
        for (int t = 0; t < 50; t++) { //Test statistico
            OthelloFactory oF = new OthelloFactory();
            oF.setPlayerNames("a", "b");
            Othello o = (Othello) Utils.play(oF, new RandPlayer<>("a"), new RandPlayer<>("a"));
            int risultato1 = o.result();
            o.unMove();
            assertEquals(o.result(), -1);
            for (Move<PieceModel<PieceModel.Species>> m : o.validMoves()) {
                if (m.kind.equals(Move.Kind.RESIGN)) continue;
                o.move(m);
                int risultato2 = o.result();
                o.unMove();
                if (risultato2 == risultato1) {
                    success++;
                    break;
                }
            }
            GameRuler.Next<PieceModel<PieceModel.Species>> next = o.mechanics().next;
            assertEquals(next.get(x.toSituation(o)).size(), o.validMoves().size()-1);
            for (Move<PieceModel<PieceModel.Species>> m : o.validMoves()) {
                if (m.kind.equals(Move.Kind.RESIGN)) continue;
                Map<Pos, PieceModel<PieceModel.Species>> mapSit = next.get(x.toSituation(o)).get(m).newMap();
                assertEquals(
                        mapSit,
                        x.toSituation(o).newMap()
                );
                Othello o2 = (Othello)o.copy();
                o.move(m);
                assertEquals(
                        next.get(x.toSituation(o2)).get(m).turn,
                        o.result() == -1 ? o.turn() : -o.result(),
                        "Primo: " + next.get(x.toSituation(o2)).get(m).turn + "\tSecondo: " + (o.result() == -1 ? o.turn() : -o.result())
                );
                System.out.println(next.get(x.toSituation(o2)).get(m).turn + "\t" + (o.result() == -1 ? o.turn() : -o.result()));
                o.unMove();
            }
        }
        assertEquals(success, 50);*/
        fail("Test da implementare");
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(200)
    public void mechanics_ControllaRisultatoImmutabile() {
        new Othello("a","b").mechanics().positions.add(new Pos(10,10));
    }

    /*@Test
    @Sorted(210)
    public void mechanics_Situation_TurnInizioPartita() {
        PieceModel<PieceModel.Species> blackPiece = new PieceModel<>(PieceModel.Species.DISC, "nero");
        PieceModel<PieceModel.Species> whitePiece = new PieceModel<>(PieceModel.Species.DISC, "bianco");class X {
            public GameRuler.Situation<PieceModel<PieceModel.Species>> toSituation(Othello o) {
                Map<Pos, PieceModel<PieceModel.Species>> oPieceMap = new HashMap<>();
                for (Pos p : o.getBoard().get(blackPiece))
                    oPieceMap.put(p, blackPiece);
                for (Pos p : o.getBoard().get(whitePiece))
                    oPieceMap.put(p, whitePiece);
                int oTurn = o.result()==-1 ? o.turn() : -o.result();
                return new GameRuler.Situation<>(oPieceMap, oTurn);
            }
        }
        X x = new X();
        int expTurn = 1;
        Othello othello = new Othello("a","b");
        assertEquals(x.toSituation(othello).turn, expTurn, "Turn errato a inizio partita");
    }*/

    /*@Test
    @Sorted(220)
    public void mechanics_Situation_FinePartita() {
        PieceModel<PieceModel.Species> blackPiece = new PieceModel<>(PieceModel.Species.DISC, "nero");
        PieceModel<PieceModel.Species> whitePiece = new PieceModel<>(PieceModel.Species.DISC, "bianco");
        class X {
            public GameRuler.Situation<PieceModel<PieceModel.Species>> toSituation(Othello o) {
                Map<Pos, PieceModel<PieceModel.Species>> oPieceMap = new HashMap<>();
                for (Pos p : o.getBoard().get(blackPiece))
                    oPieceMap.put(p, blackPiece);
                for (Pos p : o.getBoard().get(whitePiece))
                    oPieceMap.put(p, whitePiece);
                int oTurn = o.result()!=-1 ? o.turn() : -o.result();
                return new GameRuler.Situation<>(oPieceMap, oTurn);
            }
        }
        X x = new X();
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        Othello o = (Othello)Utils.play(oF, new RandPlayer<PieceModel<PieceModel.Species>>("a"), new RandPlayer<PieceModel<PieceModel.Species>>("b"));
        assertEquals(
                x.toSituation(o).turn, o.turn(),
                "Actual: " + x.toSituation(o).turn + "\tExpected: " + o.turn() + "\tOthello.Result(): " + o.result()
        );
    }*/

    @Test
    @Sorted(230)
    public void mechanics_OggettoRitornatoSempreLoStesso() {
        fail("Test da implementare");
    }
}

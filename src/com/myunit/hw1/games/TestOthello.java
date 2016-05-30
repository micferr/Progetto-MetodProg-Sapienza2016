package com.myunit.hw1.games;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.*;
import gapp.ulg.play.RandPlayer;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.Othello;
import gapp.ulg.games.OthelloFactory;

import java.util.*;

public class TestOthello {

    // Costruttore

    @Test
    @Sorted(0)
    public void construttore_EntrambiIParametriNonNull() {
        new Othello("a", "b");
    }

    @Test(expected = NullPointerException.class)
    @Sorted(1)
    public void construttore_PrimoParametroNull() {
        new Othello(null, "a");
    }

    @Test(expected = NullPointerException.class)
    @Sorted(2)
    public void costruttore_SecondoParametroNull() {
        new Othello("a", null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(3)
    public void costruttore_EntrambiIParametriNull() {
        new Othello(null, null);
    }

    // Name

    @Test
    @Sorted(4)
    public void name() {
        assertEquals(
                new Othello("a", "b").name(),
                "Othello8x8"
        );
    }

    // GetParam

    /*@Test(expected = IllegalArgumentException.class)
    @Sorted(5)
    public void getParam() {
        new Othello("a","b").getParam("a", Object.class);
    }*/

    // Players

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(6)
    public void players_ControllaRisultatoImmodificabile() {
        Othello othello = new Othello("aaa", "bbb");
        othello.players().add("ccc");
    }

    @Test
    @Sorted(7)
    public void players_ControllaSempreLaStessaLista() {
        Othello othello = new Othello("a","b");
        assertTrue(
                othello.players() == othello.players(),
                "Players non ritorna sempre la stessa lista"
        );
    }

    @Test
    @Sorted(8)
    public void players_ControllaRisultatoCorretto() {
        Othello othello = new Othello("aaa", "bbb");
        assertEquals(
                othello.players(),
                Arrays.asList("aaa", "bbb"),
                "Players non restituisce il risultato corretto"
        );
    }

    // Color

    @Test(expected = NullPointerException.class)
    @Sorted(9)
    public void color_ParametroNullo() {
        new Othello("a","b").color(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(10)
    public void color_ParametroNonNomeDiUnGiocatore() {
        new Othello("a","b").color("c");
    }

    @Test
    @Sorted(11)
    public void color_PrimoGiocatore() {
        assertEquals(
                new Othello("a","b").color("a"),
                "nero",
                "Color non restituisce nero per il primo giocatore"
        );
    }

    @Test
    @Sorted(12)
    public void color_SecondoGiocatore() {
        assertEquals(
                new Othello("a","b").color("b"),
                "bianco",
                "Color non restituisce bianco per il secondo giocatore"
        );
    }

    // GetBoard

    @Test
    @Sorted(13)
    public void getBoard_ControllaValoreRitornatoSempreLoStesso() {
        Othello othello = new Othello("aa", "bb");
        Board b1 = othello.getBoard(), b2 = othello.getBoard();
        assertTrue(b1 == b2, "getBoard ritorna istanze diverse a chiamate diverse");
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(14)
    public void getBoard_ControllaRisultatoImmodificabile() {
        Othello othello = new Othello("a", "b");
        othello.getBoard().put(new PieceModel<>(PieceModel.Species.DISC, "bianco"), new Pos(1,1));
    }

    // Turn

    @Test
    @Sorted(15)
    public void turn_InizioPartita() {
        assertEquals(
                new Othello("a", "b").turn(),
                1,
                "Ad inizio partita turn non restituisce 1"
        );
    }

    @Test
    @Sorted(16)
    public void turn_DopoUnTurno() {
        Othello othello = new Othello("a", "b");
        //othello.move(othello.validMoves().iterator().next()); //Gioca la prima mossa
        othello.move(getNonResignMove(othello.validMoves()));
        int turn = othello.turn();
        assertEquals(
                turn,
                2,
                "Dopo un turno turn non restituisce 2 ma " + turn
        );
    }

    @Test
    @Sorted(17)
    public void turn_FinePartita() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a", "b");
        Player<PieceModel<PieceModel.Species>>
                p1 = new RandPlayer<>("a"),
                p2 = new RandPlayer<>("b");
        assertEquals(
                Utils.play(
                        oF,
                        p1,
                        p2
                ).turn(),
                0,
                "A fine partita il turn non è zero"
        );
    }

    // Move

    @Test(expected = IllegalStateException.class)
    @Sorted(18)
    public void move_PartitaFinita() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a", "b");
        Player<PieceModel<PieceModel.Species>>
                p1 = new RandPlayer<>("a"),
                p2 = new RandPlayer<>("b");
        GameRuler othello = Utils.play(oF, p1, p2);
        othello.move(new Move(new Action(new Pos(1,1), new Pos(1,2))));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(19)
    public void move_MossaNulla() {
        Othello othello = new Othello("a","b");
        othello.move(null);
    }

    @Test
    @Sorted(20)
    public void move_MossaNonValida() {
        Othello othello = new Othello("a","b");
        assertFalse(
                othello.move(new Move(new Action(new Pos(1,1), new Pos(1,2)))),
                "Move non ritorna false cercando di eseguire una mossa non valida"
        );
    }

    // UnMove

    @Test
    @Sorted(30)
    public void unMove_InizioPartita() {
        assertFalse(
                new Othello("a","b").unMove(),
                "unMove ad inizio partita non ritorna false"
        );
    }

    @Test
    @Sorted(31)
    public void unMove_UndoTreMosse() {
        Othello othello = new Othello("a","b");
        //othello.move(othello.validMoves().iterator().next());
        othello.move(getNonResignMove(othello.validMoves()));
        GameRuler othelloCopy1 = othello.copy();
        for (int i=0; i<3; ++i)
            //othello.move(othello.validMoves().iterator().next());
            othello.move(getNonResignMove(othello.validMoves()));
        for (int i=0; i<3; ++i)
            othello.unMove();
        GameRuler othelloCopy2 = othello.copy();
        for (Object p : othello.getBoard().positions()) {
            assertEquals(
                    othelloCopy1.getBoard().get((Pos)p),
                    othelloCopy2.getBoard().get((Pos)p),
                    "In un Pos accade che board.get(Pos) ritorna valori diversi"
            );
        }
        assertEquals(
                othelloCopy1.turn(),
                othelloCopy2.turn(),
                "Turn() ritorna valori diversi: Dopo gli unMove: " + othelloCopy2.turn() + " - Prima: " + othelloCopy1.turn()
        );
        //((Othello)othelloCopy1).printBoard();
        //((Othello)othelloCopy2).printBoard();
        assertEquals(
                othelloCopy1.score(1),
                othelloCopy2.score(1),
                0.01d,
                "Score(1) ritorna valori diversi: Dopo gli unMove: " + othelloCopy2.score(1) + " - Prima: " + othelloCopy1.score(2)
        );
        assertEquals(
                othelloCopy1.score(2),
                othelloCopy2.score(2),
                0.01d,
                "Score(2) ritorna valori diversi"
        );
    }


    @Test
    @Sorted(32)
    public void unMove_DallaFineAllInizio() {
        Othello othello = (Othello)Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a") ,
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
    }

    @Test
    @Sorted(33)
    public void unMove_DallaFineAllInizio_ControllaPartitaNonConsiderataFinita() {
        Othello othello = (Othello)Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        //othello.printBoard();
        while (othello.unMove());
        assertEquals(
                othello.turn(),
                1,
                "Il turno non è stato dato al primo giocatore"
        );
        assertEquals(
                othello.result(),
                -1,
                "result() non ritorna -1"
        );
        assertEquals(
                othello.score(1),
                2,
                0.01d,
                "Il primo giocatore non ha punteggio 2"
        );
        assertEquals(
                othello.score(2),
                2,
                0.01d,
                "Il secondo giocatore non ha punteggio 2"
        );
    }

    // IsPlaying

    @Test(expected = IllegalArgumentException.class)
    @Sorted(34)
    public void isPlaying_GiocatoreZero() {
        new Othello("a","b").isPlaying(0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(35)
    public void isPlaying_GiocatoreDue() {
        new Othello("a","b").isPlaying(3);
    }

    @Test
    @Sorted(36)
    public void isPlaying_GiocatoriInGioco() {
        Othello othello = new Othello("a","b");
        try {
            othello.isPlaying(1);
        } catch (Throwable t) { fail("isPlaying(1) lancia eccezione"); }
        try {
            othello.isPlaying(2);
        } catch (Throwable t) { fail("isPlaying(2) lancia eccezione"); }
    }

    @Test
    @Sorted(37)
    public void isPlaying_fineGioco() {
        Othello othello = (Othello)Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        assertFalse(
                othello.isPlaying(1),
                "isPlaying(1) non ritorna false quando il gioco è finito"
        );
        assertFalse(
                othello.isPlaying(2),
                "isPlaying(2) non ritorna false quando il gioco è finito"
        );
    }

    // Result

    @Test
    @Sorted(40)
    public void result_InizioGioco() {
        assertEquals(
                new Othello("a","b").result(),
                -1,
                "Result sbagliato a inizio partita"
        );
    }

    @Test
    @Sorted(41)
    public void result_ControllaNonMenoUnoAFineGioco() {
        assertNotEquals(
                Utils.play(
                        new OthelloFactory(),
                        new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                        new RandPlayer<PieceModel<PieceModel.Species>>("c")
                ).result(),
                -1,
                "Result indica una partita non finita (restituisce -1)"
        );
    }

    @Test
    @Sorted(42)
    public void result_ControllaRisultatoTraZeroEDue() {
        int result = Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("c")
        ).result();
        assertTrue(
                0 <= result && result <= 2
        );
    }

    @Test
    @Sorted(43)
    public void result_PrimaMossaNonValida() {
        Othello othello = new Othello("a","b");
        othello.move(new Move<PieceModel<PieceModel.Species>>(new Action<PieceModel<PieceModel.Species>>(new Pos(0,0), new Pos(1,1))));
        assertEquals(
                othello.result(),
                2,
                "Result non è 2 quando il giocatore 1 gioca una mossa non valida: Risultato ottenuto: " + othello.result()
        );
    }

    @Test
    @Sorted(44)
    public void result_SecondaMossaNonValida() {
        Othello othello = new Othello("a","b");
        //othello.move(othello.validMoves().iterator().next());
        othello.move(getNonResignMove(othello.validMoves()));
        othello.move(new Move<PieceModel<PieceModel.Species>>(new Action<PieceModel<PieceModel.Species>>(new Pos(0,0), new Pos(1,1))));
        assertEquals(
                othello.result(),
                1,
                "Result non è 1 quando il secondo giocatore gioca una mossa non valida"
        );
    }

    // ValidMoves

    @Test(expected = IllegalStateException.class)
    @Sorted(60)
    public void validMoves_GiocoFinitoRegolarmente() {
        Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        ).validMoves();
    }

    @Test(expected = IllegalStateException.class)
    @Sorted(61)
    public void validMoves_GiocoFinitoPerMossaInvalida() {
        Othello othello = new Othello("a","b");
        othello.move(new Move(new Action(new Pos(1,1), new Pos(2,2))));
        othello.validMoves();
    }

    @Test
    @Sorted(62)
    public void validMoves_InizioGioco() {
        Set<Move<PieceModel<PieceModel.Species>>> moves = new HashSet<>();
        PieceModel<PieceModel.Species> blackDisc =
                new PieceModel<>(PieceModel.Species.DISC, "nero");
        moves.addAll( Arrays.asList(
                new Move<>(Move.Kind.RESIGN),
                new Move<>(
                        new Action<>(new Pos(2,4), blackDisc),
                        new Action<>(blackDisc, new Pos(3,4))
                ),
                new Move<>(
                        new Action<>(new Pos(3,5), blackDisc),
                        new Action<>(blackDisc, new Pos(3,4))
                ),
                new Move<>(
                        new Action<>(new Pos(4,2), blackDisc),
                        new Action<>(blackDisc, new Pos(4,3))
                ),
                new Move<>(
                        new Action<>(new Pos(5,3), blackDisc),
                        new Action<>(blackDisc, new Pos(4,3))
                )
        ));
        Set<Move<PieceModel<PieceModel.Species>>> vm = new Othello("a","b").validMoves();
        int x = 0;
        assertEquals(
                new Othello("a","b").validMoves(),
                moves,
                "ValidMoves errato"
        );
    }

    // Score

    @Test(expected = IllegalArgumentException.class)
    @Sorted(150)
    public void score_IndiceTurnazioneZero() {
        Othello othello = new Othello("a", "b");
        othello.score(0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(151)
    public void score_IndiceTurnazioneNegativo() {
        Othello othello = new Othello("a", "b");
        othello.score(-1);
    }

    @Test
    @Sorted(152)
    public void score_ControllaNienteEccezioni() {
        Othello othello = new Othello("a", "b");
        othello.score(1);
        othello.score(2);
    }

    @Test
    @Sorted(153)
    public void score_ControllaRisultatiCorrettiAllInizio() {
        Othello othello = new Othello("a", "b");
        assertEquals(othello.score(1), 2, 0.1d);
        assertEquals(othello.score(2), 2, 0.1d);
    }

    @Test
    @Sorted(154)
    public void score_DopoLaPrimaMossa() {
        Othello othello = new Othello("a","b");
        RandPlayer<PieceModel<PieceModel.Species>>
                p1 = new RandPlayer<>("a"),
                p2 = new RandPlayer<>("b");
        p1.setGame(othello.copy());
        p2.setGame(othello.copy());
        othello.move(p1.getMove());
        assertEquals(
                othello.score(1),
                4,
                0.01d,
                "Il primo giocatore ha score errato"
        );
        assertEquals(
                othello.score(2),
                1,
                0.01d,
                "Il secondo giocatore ha score errato"
        );
    }

    @Test
    @Sorted(999)
    public void nomiuguali() {
        try {
            OthelloFactory oF = new OthelloFactory();
            //oF.setPlayerNames("a","a");
            Othello o = (Othello) Utils.play(
                    oF,
                    new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                    new RandPlayer<PieceModel<PieceModel.Species>>("a")
            );
            /*o.printBoard();
            while (o.unMove()) {
                o.printBoard();
            }*/
        } catch (Throwable t) { t.printStackTrace(); fail(t.getMessage()); }
    }

    @Test
    @Sorted(10000)
    public void unMoveEXTRA_Totale() {
        boolean doPrintBoards = false;
        boolean executeMultipleTimes = false;
        int times = 100, lastPercentage = 0;
        for (int k=0; k<(executeMultipleTimes ? times : 1); ++k) {
            if ((int)((k*100)/times) > lastPercentage && executeMultipleTimes) {
                lastPercentage = (k*100)/times;
                System.out.println(lastPercentage + "%");
            }
            Set<Set<Move<PieceModel<PieceModel.Species>>>> validMovesDuringPlay = new HashSet<>();
            Set<Set<Move<PieceModel<PieceModel.Species>>>> validMovesDuringUnMove = new HashSet<>();
            List<Board> boardsDuringPlay = new ArrayList<>();
            List<Board> boardsDuringUnmove = new ArrayList<>();
            Othello othello = (Othello) Utils.play(
                    new OthelloFactory(),
                    new RandPlayer<PieceModel<PieceModel.Species>>("a") {
                        @Override
                        public void moved(int i, Move m) {
                            validMovesDuringPlay.add(game.validMoves());
                            boardsDuringPlay.add(game.getBoard());
                            super.moved(i, m);
                            if (doPrintBoards) ((Othello) this.game).printBoard();
                        }
                    },
                    new RandPlayer<PieceModel<PieceModel.Species>>("b")
            );
            while (othello.unMove()) {
                if (doPrintBoards) othello.printBoard();
                validMovesDuringUnMove.add(othello.validMoves());
                boardsDuringUnmove.add(othello.getBoard());
            }
            assertEquals(
                    validMovesDuringPlay.size(),
                    validMovesDuringUnMove.size(),
                    "Test 1 : " + validMovesDuringPlay.size() + " != " + validMovesDuringUnMove.size()
            );
            assertEquals(
                    validMovesDuringPlay,
                    validMovesDuringUnMove,
                    "Test 2"
            );
            assertEquals(
                    boardsDuringPlay.size(),
                    boardsDuringUnmove.size(),
                    "Test 3 : " + boardsDuringPlay.size() + " != " + boardsDuringUnmove.size()
            );
            for (int i=0; i<boardsDuringPlay.size(); ++i) {
                for (Pos p : othello.getBoard().positions()) {
                    assertEquals(
                            boardsDuringPlay.get(i).get(p),
                            boardsDuringPlay.get(boardsDuringPlay.size()-1-i).get(p),
                            "Test 4"
                    );
                }
            }
        }
    }

    @Test
    @Sorted(10005)
    public void unMoveEXTRA_Resign() {
        boolean printBoard = false;
        Othello othello = (Othello)Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a") {
                    int i=6;
                    @Override
                    public Move getMove() {
                        if (i--==0) {
                            return new Move(Move.Kind.RESIGN);
                        } else {
                            return super.getMove();
                        }
                    }

                    @Override
                    public void moved(int i, Move m) {
                        assertTrue(this.game.isValid(m));
                        super.moved(i,m);
                        if (printBoard) ((Othello)this.game).printBoard();
                    }
                },
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        while (othello.unMove()) { if(printBoard) othello.printBoard(); }
    }

    @Test
    @Sorted(10010)
    public void playEXTRA_MossaResign() {
        boolean printBoard = false;
        Othello othello = (Othello)Utils.play(
                new OthelloFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a") {
                    int i=6;
                    @Override
                    public Move getMove() {
                        if (i--==0) {
                            return new Move(Move.Kind.RESIGN);
                        } else {
                            return super.getMove();
                        }
                    }

                    @Override
                    public void moved(int i, Move m) {
                        super.moved(i,m);
                        if (printBoard) ((Othello)this.game).printBoard();
                    }
                },
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        while (othello.unMove()) { if (printBoard) othello.printBoard(); }
    }

    @Test
    @Sorted(10020)
    public void playEXTRA_MossaNonValida() {
        /*try {
            boolean printBoard = false;
            Othello othello = (Othello) Utils.play(
                    new OthelloFactory(),
                    new RandPlayer<PieceModel<PieceModel.Species>>("a") {
                        int i = 3; //Un numero abbastanza maggiore di 0 che non dovrebbe rischiare la fine della partita

                        @Override
                        public Move getMove() {
                            if (i-- == 0) {
                                return new Move(new Action(new Pos(0, 0), new Pos(7, 7)));
                            } else {
                                return super.getMove();
                            }
                        }

                        @Override
                        public void moved(int i, Move m) {
                            super.moved(i, m);
                            ((Othello) this.game).printBoard();
                        }
                    },
                    new RandPlayer<PieceModel<PieceModel.Species>>("b")
            );
            while (othello.unMove()) { /*othello.printBoard();*//* }
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }*/
    }

    enum UpDown { UP, DOWN };
    @Test
    @Sorted(10030)
    public void unMoveEXTRA_AvantiEIndietroPiuVolte() {
        Set<Set<Move<PieceModel<PieceModel.Species>>>> validMovesDuringPlay = new HashSet<>();
        Set<Set<Move<PieceModel<PieceModel.Species>>>> validMovesDuringUnMove = new HashSet<>();
        List<Board> boardsDuringPlay = new ArrayList<>();
        List<Board> boardsDuringUnmove = new ArrayList<>();
        class x { boolean b; x(boolean bb) {b=bb;} boolean v() {return b;}}
        Player p1 =  new RandPlayer<PieceModel<PieceModel.Species>>("a") {
            @Override
            public void moved(int i, Move m) {
                validMovesDuringPlay.add(game.validMoves());
                boardsDuringPlay.add(game.getBoard());
                super.moved(i, m);
            }
            @Override
            public Move getMove() {
                return (Move) this.game.validMoves().iterator().next();
            }
        };
        Player p2 = new RandPlayer<PieceModel<PieceModel.Species>>("b") {
            @Override
            public Move getMove() {
                return (Move) this.game.validMoves().iterator().next();
            }
        };
        Othello othello = (Othello) Utils.play(
                new OthelloFactory(),
                p1, p2
        );
        while (othello.unMove()) {
            validMovesDuringUnMove.add(othello.validMoves());
            boardsDuringUnmove.add(othello.getBoard());
        }
        UpDown x = UpDown.UP;
        int times=10;
        for (int kkkk=0; kkkk<times; ++kkkk) {
            final Set<Set<Move<PieceModel<PieceModel.Species>>>> moves = new HashSet<>();
            final List<Board> boards = new ArrayList<>();
            if (x.equals(UpDown.UP)) {
                Player newp1 = new RandPlayer<PieceModel<PieceModel.Species>>("a") {
                    @Override
                    public void moved(int i, Move m) {
                        moves.add(this.game.validMoves());
                        boards.add(this.game.getBoard());
                        super.moved(i,m);
                    }
                    @Override
                    public Move getMove() {
                        return (Move) this.game.validMoves().iterator().next();
                    }
                };
                Player newp2 = new RandPlayer<PieceModel<PieceModel.Species>>("b") {
                    @Override
                    public Move getMove() {
                        return (Move) this.game.validMoves().iterator().next();
                    }
                };
                newp1.setGame(othello.copy());
                newp2.setGame(othello.copy());
                while (othello.result()==-1) {
                    int turn = othello.turn();
                    Move move = (turn==1?newp1:newp2).getMove();
                    othello.move(move);
                    newp1.moved(turn,move);
                    newp2.moved(turn,move);
                }
                assertEquals(
                        validMovesDuringPlay.size(),
                        moves.size(),
                        "Test moves: expected " + validMovesDuringPlay.size() + " got " + moves.size()
                );
                assertEquals(
                        validMovesDuringPlay,
                        moves,
                        "Test moves: different moves do"
                );
                for (int i=0; i<boardsDuringPlay.size(); ++i) {
                    for (Pos p : othello.getBoard().positions()) {
                        assertEquals(
                                boardsDuringPlay.get(i).get(p),
                                boards.get(i).get(p),
                                "Test 4"
                        );
                    }
                }
                x = UpDown.DOWN;
            }
            else {
                while (othello.unMove()) {
                    moves.add(othello.validMoves());
                    boards.add(othello.getBoard());
                }
                assertEquals(
                        validMovesDuringUnMove,
                        moves,
                        "Test moves: different moves undo"
                );
                for (int i=0; i<boardsDuringUnmove.size(); ++i) {
                    for (Pos p : othello.getBoard().positions()) {
                        assertEquals(
                                boardsDuringUnmove.get(i).get(p),
                                boards.get(i).get(p),
                                "Test 4"
                        );
                    }
                }
                x = UpDown.UP;
            }
        }
    }

    /*@Test
    @Sorted(1000)
    public void cercaVittoriaSecondo() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        int vinceuno = 0;
        int vincedue = 0;
        int patta = 0;
        Othello o;
        for (int i=0; i<1000; ++i) {
            o = (Othello)Utils.play(
                    oF,
                    new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                    new RandPlayer<PieceModel<PieceModel.Species>>("b")
            );
            switch (o.result()) {
                case 0: patta++; break;
                case 1: vinceuno++; break;
                case 2: vincedue++; break;
                default: fail("!!!");
            }
            System.out.print(i+"\t");
        }
        System.out.println();
        System.out.println("Vince uno: " + vinceuno);
        System.out.println("Vince due: " + vincedue);
        System.out.println("Patte: " + patta);
    }*/

    /*@Test
    @Sorted(99)
    public void controllaPlay() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        try {
            Utils.play(oF, new RandPlayer<PieceModel<PieceModel.Species>>("a"), new RandPlayer<PieceModel<PieceModel.Species>>("b"));
        } catch (Throwable T) { T.printStackTrace(); fail(":("); }
    }*/

    /*@Test
    @Sorted(100)
    public void cercaPartitaMenoDi60Mosse() {
        int i=0;
        do {
            OthelloFactory oF = new OthelloFactory();
            oF.setPlayerNames("a","b");
            try {
                GameRuler<PieceModel<PieceModel.Species>> g = Utils.play(oF, new RandPlayer<PieceModel<PieceModel.Species>>("a"), new RandPlayer<PieceModel<PieceModel.Species>>("b"));
                i = ((Othello)g).moveHistory.size();
                System.out.println(i);
            } catch (Throwable T) { T.printStackTrace(); fail(":("); }
        } while (i>=59);
    }*/

    private Move<PieceModel<PieceModel.Species>> getNonResignMove(Set<Move<PieceModel<PieceModel.Species>>> validMoves) {
        for (Move<PieceModel<PieceModel.Species>> move : validMoves) {
            if (move.kind != Move.Kind.RESIGN)
                return move;
        }
        return null;
    }
}

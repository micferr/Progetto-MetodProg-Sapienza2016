package com.myunit.hw1.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import java.util.Set;

public class TestGameRuler {

    // isValid

    @Test(expected = IllegalStateException.class)
    @Sorted(0)
    public void isValid_GiocoFinito() {
        new DummyGU("a", "b") {
            @Override
            public int result() {
                return 0;
            }
        }.isValid(new Move<>(new Action<>(new Pos(1,1), new Pos(1,3))));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(1)
    public void isValid_Null() {
        new DummyGU("a", "b").isValid(null);
    }

    @Test
    @Sorted(2)
    public void isValid_ControllaValoriCorretti() {
        DummyGU gR = new DummyGU("a", "b");
        for (Move move : gR.validMoves()) {
            assertTrue(gR.isValid(move), "isValid restituisce false per una mossa valida");
        }
    }

    // ValidMoves(Pos p)

    @Test(expected = IllegalStateException.class)
    @Sorted(3)
    public void validMoves_GiocoFinito() {
        new DummyGU("a", "b") {
            @Override
            public int result() {
                return 0;
            }
        }.validMoves(new Pos(1,1));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(4)
    public void validMoves_PosizioneNull() {
        new DummyGU("a", "b").validMoves(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(5)
    public void validMoves_PosFuoriTavola() {
        new DummyGU("a", "b").validMoves(new Pos(1000, 1000));
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(6)
    public void validMoves_ControllaRisultatoImmutabile() {
        DummyGU gR = new DummyGU("a", "b");
        Move<PieceModel<PieceModel.Species>> lastValidMove = null;
        for (Move move : gR.validMoves()) {
            lastValidMove = move;
        }
        gR.validMoves(lastValidMove.actions.get(0).pos.get(0)).add(null); //Lo fa il Silvestri, io mi fido
    }

    @Test
    @Sorted(7)
    public void validMoves_ControllaRisultatoCorretto() {
        DummyGU gR = new DummyGU("a", "b");
        Move<PieceModel<PieceModel.Species>> lastValidMove = null;
        for (Move move : gR.validMoves()) {
            lastValidMove = move;
        }
        Set<Move<PieceModel<PieceModel.Species>>> validMovesInPosition =
                gR.validMoves(lastValidMove.actions.get(0).pos.get(0));
        assertNotNull(validMovesInPosition, "validMoves(Pos p) ha ritornato null");
        assertEquals(
                validMovesInPosition.size(),
                1,
                "validMoves(Pos p) ha dimensione errata - Ottenuta: " + validMovesInPosition.size() + " - Richiesta: " + 1);
        assertTrue(validMovesInPosition.contains(lastValidMove), "validMoves ha contenuto errato");
    }

    @Test
    @Sorted(8)
    public void validMoves_ControllaRisultatoCorrettoDopoUnaMossa() {
        DummyGU gR = new DummyGU("a", "b");
        Move<PieceModel<PieceModel.Species>> lastValidMove = null;
        for (Move move : gR.validMoves()) {
            lastValidMove = move;
        }
        gR.move(lastValidMove);
        assertFalse(gR.isValid(lastValidMove), "La mossa appena giocata è risultata valida");
    }
}

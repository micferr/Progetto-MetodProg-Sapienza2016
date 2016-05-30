package com.myunit.hw1.game.util;

import com.myunit.hw1.game.DummyGUFactory;
import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Player;
import gapp.ulg.play.RandPlayer;
import gapp.ulg.game.util.Utils;

public class TestRandPlayer {

    // Costruttore

    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void costruttore_ArgomentoNullo() {
        new RandPlayer(null);
    }

    // Name

    @Test
    @Sorted(1)
    public void name() {
        assertEquals(new RandPlayer("aaa").name(), "aaa", "Nome non impostato correttamente");
    }

    //

    @Test
    @Sorted(2)
    public void Completo_ControllaUtilsPlayNonLanciaEccezioni() {
        RandPlayer player1 = new RandPlayer<>("p1");
        RandPlayer player2 = new RandPlayer<>("p2");
        Utils.play(new DummyGUFactory(), player1, player2);
    }

    @Test
    @Sorted(3)
    public void Completo_ControllaUtilsPlayRestituisceRisultatoCorretto() {
        DummyGUFactory gF = new DummyGUFactory();
        Player<PieceModel<PieceModel.Species>> py1 = new RandPlayer<>("A");
        Player<PieceModel<PieceModel.Species>> py2 = new RandPlayer<>("B");
        assertEquals(
                Utils.play(gF, py1, py2).result(),
                0,
                "Result del GameRuler non corretto"
        );
    }
}

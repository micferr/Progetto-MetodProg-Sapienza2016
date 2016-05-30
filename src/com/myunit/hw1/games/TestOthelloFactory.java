package com.myunit.hw1.games;

import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.games.Othello;
import gapp.ulg.games.OthelloFactory;

import java.util.Arrays;
import java.util.Collections;

import static com.myunit.assertion.Assert.*;

public class TestOthelloFactory {

    // Name

    @Test
    @Sorted(0)
    public void name() {
        assertEquals(
                new OthelloFactory().name(),
                "Othello",
                "name non va modificato"
        );
    }

    // MinPlayers

    @Test
    @Sorted(10)
    public void minPlayers() {
        assertEquals(
                new OthelloFactory().minPlayers(),
                2,
                "minPlayers non va modificato"
        );
    }

    // MaxPlayers

    @Test
    @Sorted(20)
    public void maxPlayers() {
        assertEquals(
                new OthelloFactory().maxPlayers(),
                2,
                "maxPlayers non va modificato"
        );
    }

    // Params

    /*@Test
    @Sorted(30)
    public void params() {
        assertEquals(
                new OthelloFactory().params(),
                Collections.EMPTY_LIST,
                "params non va modificato"
        );
    }*/

    // SetPlayerNames

    @Test(expected = NullPointerException.class)
    @Sorted(40)
    public void setPlayerNames_PrimoNomeNull() {
        new OthelloFactory().setPlayerNames(null, "b");
    }

    @Test(expected = NullPointerException.class)
    @Sorted(41)
    public void setPlayerNames_SecondoNomeNull() {
        new OthelloFactory().setPlayerNames("a", null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(42)
    public void setPlayerNames_EntrambiINomiNull() {
        new OthelloFactory().setPlayerNames(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(43)
    public void setPlayerNames_UnSoloNome() {
        new OthelloFactory().setPlayerNames("a");
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(44)
    public void setPlayerNames_TreNomi() {
        new OthelloFactory().setPlayerNames("a","b","c");
    }

    @Test
    @Sorted(45)
    public void setPlayerNames_DueNomi() {
        new OthelloFactory().setPlayerNames("a","b");
    }

    // NewGame

    @Test(expected = IllegalStateException.class)
    @Sorted(60)
    public void newGame_nomiNonImpostati() {
        new OthelloFactory().newGame();
    }

    @Test
    @Sorted(61)
    public void newGame_NomiImpostati_ControllaNessunaEccezione() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        oF.newGame();
    }

    @Test
    @Sorted(62)
    public void newGame_ControllaRisultatoCorretto() {
        OthelloFactory oF = new OthelloFactory();
        oF.setPlayerNames("a","b");
        Othello o = (Othello)oF.newGame();
        assertEquals(
                o.validMoves(),
                new Othello("a","b").validMoves(),
                "L'Othello della GameFactory non ha le mosse valide predefinite"
        );
        assertEquals(
                o.players(),
                Arrays.asList("a", "b"),
                "I nomi dei giocatori non sono impostati correttamente"
        );
    }
}

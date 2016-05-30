package com.myunit.hw1.game.util;

import com.myunit.hw1.game.DummyGUFactory;
import com.myunit.hw1.game.board.DummyPlayer;
import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Player;
import gapp.ulg.game.board.Pos;
import gapp.ulg.game.util.BoardOct;
import gapp.ulg.game.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    // UnmodifiableBoard

    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void unmodifiableBoard_ArgomentoNull() {
        Utils.UnmodifiableBoard(null);
    }

    @Test
    @Sorted(1)
    public void unmodifiableBoard_ControllaIsModifiable() {
        assertFalse(
                Utils.UnmodifiableBoard(
                        new BoardOct<PieceModel<PieceModel.Species>>(8,8)
                ).isModifiable(),
                "isModifiable non ritorna false"
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(2)
    public void unmodifiableBoard_ControllaPutPPosLanciaEccezione() {
        Utils.UnmodifiableBoard(new BoardOct<>(8,8)).put(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(1,1)
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(3)
    public void unmodifiableBoard_ControllaRemoveLanciaEccezione() {
        Utils.UnmodifiableBoard(new BoardOct<>(8,8)).remove(new Pos(1,1));
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(4)
    public void unmodifiableBoard_ControllaPutPPosDirILanciaEccezione() {
        Utils.UnmodifiableBoard(new BoardOct<>(8,8)).put(
                new PieceModel<>(PieceModel.Species.KING, "bianco"),
                new Pos(1,1),
                Board.Dir.UP_R,
                3
        );
    }

    // Play

    @Test(expected = NullPointerException.class)
    @Sorted(5)
    public void play_PrimoArgomentoNull() {
        Utils.play(null, new DummyPlayer<>("a"));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(6)
    public void play_UnPlayerNull() {
        Utils.play(
                new DummyGUFactory(),
                new DummyPlayer<>("a"),
                null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(7)
    public void play_NumeroPlayerMinoreDelMinimo() {
        DummyGUFactory gF = new DummyGUFactory();
        int minPlayers = gF.minPlayers();
        List<Player> players = new ArrayList<>();
        for (int i=0; i<minPlayers-1; ++i) {
            players.add(new DummyPlayer<>(""+(i+1)));
        }
        Utils.play(gF, Arrays.copyOf(players.toArray(), players.toArray().length, Player[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(8)
    public void play_NumeroPlayerMaggioreDelMinimo() {
        DummyGUFactory gF = new DummyGUFactory();
        int maxPlayers = gF.maxPlayers();
        List<Player> players = new ArrayList<>();
        for (int i=0; i<maxPlayers+1; ++i) {
            players.add(new DummyPlayer<>(""+(i+1)));
        }
        Utils.play(gF, Arrays.copyOf(players.toArray(), players.toArray().length, Player[].class));
    }

    @Test
    @Sorted(9)
    public void play_ControllaNomiPlayerImpostatiAllaGameFactoryCorrettamente() {
        DummyGUFactory gF = new DummyGUFactory();
        Utils.play(gF, new DummyPlayer<>("a"), new DummyPlayer<>("b"));
        assertTrue(
                Arrays.equals(gF.pNames, new String[]{"a","b"}),
                "GameFactory#setPlayerNames non chiamato correttamente in Utils.play"
        );
    }

    @Test
    @Sorted(10)
    public void play_ControllaPlayerRicevonoUnaCopiaDelGameRuler() {
        DummyPlayer player1 = new DummyPlayer<>("p1");
        DummyPlayer player2 = new DummyPlayer<>("p2");
        Utils.play(new DummyGUFactory(), player1, player2);
        assertTrue(
                player1.game != player2.game,
                "Play non passa ai player una copia del GameRuler ma il GameRuler stesso"
        );
    }

    @Test
    @Sorted(11)
    public void play_ControllaRisultatoCorretto() {
        DummyGUFactory gF = new DummyGUFactory();
        Player<PieceModel<PieceModel.Species>> py1 = new DummyPlayer<>("A");
        Player<PieceModel<PieceModel.Species>> py2 = new DummyPlayer<>("B");
        assertEquals(
                Utils.play(gF, py1, py2).result(),
                0,
                "Result del GameRuler non corretto"
        );
    }
}

package com.myunit.hw1.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class TestBoard {

    // isPos

    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void isPos_PosizioneNull() {
        new DummyBoard<PieceModel<PieceModel.Species>>().isPos(null);
    }

    @Test
    @Sorted(1)
    public void isPos_PosizioneValida() {
        assertTrue(
                new DummyBoard<PieceModel<PieceModel.Species>>().isPos(new Pos(1,1)),
                "isPos ritorna false, ma dovrebbe ritornare true"
        );
    }

    @Test
    @Sorted(2)
    public void isPos_PosizioneNonValida() {
        String msg = "isPos ritorna true, ma dovrebbe ritornare false";
        assertFalse(
                new DummyBoard<PieceModel<PieceModel.Species>>().isPos(new Pos(10,10)),
                msg
        );
        assertFalse(
                new DummyBoard<PieceModel<PieceModel.Species>>().isPos(new Pos(11,11)),
                msg
        );
    }

    // Get (senza argomenti)

    @Test
    @Sorted(3)
    public void getNoArgomenti_ControllaReturnNonNull() {
        assertNotNull(
                new DummyBoard<PieceModel<PieceModel.Species>>().get(),
                "get() ritorna null, ma per le specifiche non deve mai farlo"
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(4)
    public void getNoArgomenti_ControllaReturnImmutabile() {
        new DummyBoard<PieceModel<PieceModel.Species>>().get().add(new Pos(1,1));
        fail("get() ritorna un set mutabile, deve essere immutabile");
    }

    @Test
    @Sorted(5)
    public void getNoArgomenti_ControllaValoreReturnCorretto() {
        Set<Pos> expectedPieces = new HashSet<>();
        expectedPieces.add(new Pos(0,0));
        expectedPieces.add(new Pos(3,4));
        expectedPieces.add(new Pos(8,4));
        DummyBoard<PieceModel<PieceModel.Species>> board = new DummyBoard<>();
        board.put(new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), new Pos(0,0));
        board.put(new PieceModel<>(PieceModel.Species.KING, "nero"), new Pos(8,4));
        board.put(new PieceModel<>(PieceModel.Species.QUEEN, "blu"), new Pos(3,4));
        assertEquals(board.get(), expectedPieces, "get() non ritorna il set corretto");
    }

    // Get(P pm)

    @Test(expected = NullPointerException.class)
    @Sorted(6)
    public void getP_ArgomentoNull() {
        new DummyBoard<PieceModel<PieceModel.Species>>().get((PieceModel<PieceModel.Species>)null);
        fail("get(P pm) non ha lanciato una NullPointerException con input pm = null");
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(7)
    public void getP_ControllaImmodificabile() {
        new DummyBoard<PieceModel<PieceModel.Species>>().get(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco")
        ).add(new Pos(2,2));
    }

    @Test
    @Sorted(8)
    public void getP_ControllaValoreReturnCorretto() {
        DummyBoard<PieceModel<PieceModel.Species>> board = new DummyBoard<>();
        assertEquals(
                board.get(new PieceModel<>(PieceModel.Species.DAMA, "bianco")),
                Collections.EMPTY_SET,
                "get(P pm) non ritorna un valore valido con una board vuota"
        );
        Set<Pos> expectedPositions = new HashSet<>();
        expectedPositions.add(new Pos(5,4));
        expectedPositions.add(new Pos(7,7));
        board.put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"),
                new Pos(5,4)
        );
        board.put(
                new PieceModel<>(PieceModel.Species.KING, "nero"),
                new Pos(6,7)
        );
        board.put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"),
                new Pos(7,7)
        );
        assertEquals(
                board.get(new PieceModel<>(PieceModel.Species.BISHOP, "bianco")),
                expectedPositions,
                "get(P pm) non ritorna un valore valido con una board non vuota"
        );
        assertEquals(
                board.get(new PieceModel<>(PieceModel.Species.BISHOP, "nero")),
                Collections.EMPTY_SET,
                "get(P pm) non ritorna un valore valido con una board non vuota (bisogna controllare sia tipo che colore del pezzo, magari con equals dell'argomento)"
        );
        assertEquals(
                board.get(new PieceModel<>(PieceModel.Species.KING, "bianco")),
                Collections.EMPTY_SET,
                "get(P pm) non ritorna un valore valido con una board non vuota (bisogna controllare sia tipo che colore del pezzo, magari con equals dell'argomento)"
        );
    }

    // Put(P pm, Pos p, Dir d, int n)

    @Test(expected = NullPointerException.class)
    @Sorted(9)
    public void putPPosDirI_PrimoArgomentoNullo() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(null, new Pos(1,2), Board.Dir.DOWN, 5);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(10)
    public void putPPosDirI_SecondoArgomentoNullo() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), null, Board.Dir.DOWN, 5
        );
    }

    @Test(expected = NullPointerException.class)
    @Sorted(11)
    public void putPPosDirI_TerzoArgomentoNullo() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), new Pos(1,2), null, 5
        );
    }

    @Test(expected = NullPointerException.class)
    @Sorted(12)
    public void putPPosDirI_PrimoESecondoArgomentiNulli() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                null, null, Board.Dir.DOWN, 5
        );
    }

    @Test(expected = NullPointerException.class)
    @Sorted(13)
    public void putPPosDirI_PrimoETerzoArgomentiNulli() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                null, new Pos(1,2), null, 5
        );
    }

    @Test(expected = NullPointerException.class)
    @Sorted(14)
    public void putPPosDirI_SecondoETerzoArgomentiNulli() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), null, null, 5
        );
    }

    @Test(expected = NullPointerException.class)
    @Sorted(15)
    public void putPPosDirI_TuttiGliArgomentiNulli() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                null, null, null, 5
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(16)
    public void putPPosDirI_NNegativo() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), new Pos(0,0), Board.Dir.UP_R, -1
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(17)
    public void putPPosDirI_NZero() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), new Pos(0,0), Board.Dir.UP_R, 0
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(18)
    public void putPPosDirI_FuoriBordo() {
        new DummyBoard<PieceModel<PieceModel.Species>>().put(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), new Pos(8,8), Board.Dir.UP_R, 5
        );
    }

    @Test
    @Sorted(19)
    public void putPPosDirI_ControllaInserimento() {
        DummyBoard<PieceModel<PieceModel.Species>> board = new DummyBoard<>();
        PieceModel<PieceModel.Species> piece1 = new PieceModel<>(PieceModel.Species.BISHOP, "bianco");
        PieceModel<PieceModel.Species> piece2 = new PieceModel<>(PieceModel.Species.BISHOP, "nero");
        String msg = "Pezzi inseriti in maniera errata (NB: questo test assume che i test di get(Pm p) passino)";
        board.put(piece1, new Pos(2,2), Board.Dir.UP_R, 5);
        board.put(piece2, new Pos(2,3), Board.Dir.DOWN_R, 1);
        Set<Pos> pieces1 = new HashSet<>();
        for (int i=2; i<=6; ++i) {
            pieces1.add(new Pos(i,i));
        }
        assertEquals(board.get(piece1), pieces1, msg);
        Set<Pos> pieces2 = new HashSet<>();
        pieces2.add(new Pos(2,3));
        assertEquals(board.get(piece2), pieces2, msg);
    }
}

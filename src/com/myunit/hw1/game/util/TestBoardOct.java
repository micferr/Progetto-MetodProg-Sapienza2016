package com.myunit.hw1.game.util;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;
import gapp.ulg.game.util.BoardOct;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestBoardOct {

    // Costruttore Width Height

    @Test(expected = IllegalArgumentException.class)
    @Sorted(0)
    public void costruttoreWH_Larghezza0() {
        new BoardOct<PieceModel<PieceModel.Species>>(0,10);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(1)
    public void costruttoreWH_LarghezzaMinoreDiZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(-2, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(2)
    public void costruttoreWH_AltezzaZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(10,0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(3)
    public void costruttoreWH_AltezzaMinoreDiZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(10, -2);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(4)
    public void costruttoreWH_AltezzaELarghezzaZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(0,0);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(5)
    public void costruttoreWH_AltezzaELarghezzaMinoriDiZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(-1, -2);
    }

    // Costruttore Width Height Exc

    @Test(expected = IllegalArgumentException.class)
    @Sorted(6)
    public void costruttoreWHE_Larghezza0() {
        new BoardOct<PieceModel<PieceModel.Species>>(0,10, Collections.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(7)
    public void costruttoreWHE_LarghezzaMinoreDiZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(-2, 10, Collections.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(8)
    public void costruttoreWHE_AltezzaZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(10, 0, Collections.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(9)
    public void costruttoreWHE_AltezzaMinoreDiZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(10, -2, Collections.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(10)
    public void costruttoreWHE_AltezzaELarghezzaZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(0, 0, Collections.EMPTY_LIST);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(11)
    public void costruttoreWHE_AltezzaELarghezzaMinoriDiZero() {
        new BoardOct<PieceModel<PieceModel.Species>>(-1, -2, Collections.EMPTY_LIST);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(12)
    public void costruttoreWHE_ExcNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8,null);
    }

    // System

    @Test
    @Sorted(13)
    public void system() {
        assertEquals(
                new BoardOct<PieceModel<PieceModel.Species>>(8,8).system(),
                Board.System.OCTAGONAL,
                "System non è OCTAGONAL"
        );
    }

    // Width

    @Test
    @Sorted(14)
    public void width() {
        assertEquals(
                new BoardOct<PieceModel<PieceModel.Species>>(6,7).width(),
                6,
                "Width è errato"
        );
    }

    // Height

    @Test
    @Sorted(15)
    public void height() {
        assertEquals(
                new BoardOct<PieceModel<PieceModel.Species>>(6,7).height(),
                7,
                "Height è errato"
        );
    }

    // Adjacent

    @Test(expected = NullPointerException.class)
    @Sorted(16)
    public void adjacent_PosNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8).adjacent(null, Board.Dir.DOWN);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(17)
    public void adjacent_DirNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8).adjacent(new Pos(1,1), null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(18)
    public void adjacent_PosEDirEntrambiNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8).adjacent(null, null);
    }

    @Test
    @Sorted(19)
    public void adjacent_ControllaRisultatiCorrettiConExcVuoto() {
        BoardOct<PieceModel<PieceModel.Species>> board = new BoardOct<>(8,8);
        assertEquals(
                board.adjacent(new Pos(8,8), Board.Dir.DOWN_L),
                null,
                "Adjacent((width, height), DOWN_L) non ritorna null"
        );
        assertEquals(
                board.adjacent(new Pos(0,0), Board.Dir.DOWN),
                null,
                "Adjacent((0,0), DOWN) non ritorna null"
        );
        assertEquals(
                board.adjacent(new Pos(4,4), Board.Dir.UP_L),
                new Pos(3,5),
                "Adjacents((x,y), UP_L) non ritorna (x-1, y+1)"
        );
        assertEquals(
                board.adjacent(new Pos(3,3), Board.Dir.LEFT),
                new Pos(2,3),
                "Adjacents((x,y), LEFT) non ritorna (x-1, y)"
        );
        assertEquals(
                board.adjacent(new Pos(7,7), Board.Dir.UP_R),
                null,
                "Adjacents((width-1, height-1), UP_R) non ritorna null"
        );
    }

    @Test
    @Sorted(20)
    public void adjacents_ControllaRisultatiCorrettiConExcNonVuoto() {
        BoardOct<PieceModel<PieceModel.Species>> board =
                new BoardOct<PieceModel<PieceModel.Species>>(
                        3,3, Arrays.asList(new Pos(1,1), new Pos(1,2), new Pos(2,2))
                );
        assertEquals(
                board.adjacent(new Pos(1,0), Board.Dir.RIGHT),
                new Pos(2,0),
                "Adjacents((x,y), RIGHT) ritorna un valore nullo o errato"
        );
        assertEquals(
                board.adjacent(new Pos(1,0), Board.Dir.UP),
                null,
                "Adjacents((x,y), UP), con (x,y+1) escluso, non ritorna null"
        );
        assertEquals(
                board.adjacent(new Pos(0,2), Board.Dir.RIGHT),
                null,
                "Adjacents((x,y), RIGHT), con (x+1,y) escluso, non ritorna null"
        );
        assertEquals(
                board.adjacent(new Pos(1,2), Board.Dir.RIGHT),
                null,
                "Adjacents((x,y), RIGHT), con (x,y) e (x+1,y) esclusi, non ritorna null"
        );
        assertEquals(
                board.adjacent(new Pos(2,2), Board.Dir.DOWN),
                null,
                "Adjacents((x,y), DOWN), con (x,y) escluso, non ritorna null"
        );
    }

    // Positions

    @Test
    @Sorted(21)
    public void positions_ControllaListaOrdinata() {
        BoardOct<PieceModel<PieceModel.Species>> board =
                new BoardOct<>(8,8, Arrays.asList(new Pos(1,2), new Pos(6,3)));
        List<Pos> positions = board.positions();
        for (int i=0; i<positions.size()-1; ++i) {
            assertTrue(
                    positions.get(i).b < positions.get(i+1).b ||
                            (positions.get(i).b == positions.get(i+1).b &&
                            positions.get(i).t < positions.get(i+1).t),
                    "La lista ritornata non rispetta l'ordinamento"
            );
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(22)
    public void positions_ControllaListaImmutabile() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8).positions().add(new Pos(10,5));
    }

    @Test
    @Sorted(23)
    public void positions_ControllaSeRitornaSempreLoStessoValore() {
        BoardOct<PieceModel<PieceModel.Species>> board = new BoardOct<>(5,5);
        assertTrue(
                board.positions() == board.positions(),
                "Chiamate diverse a positions restituiscono valori diversi"
        );
    }

    @Test
    @Sorted(24)
    public void positions_ControllaRisultatoCorrettoConExcVuoto() {
        BoardOct<PieceModel<PieceModel.Species>> board =
                new BoardOct<>(3,3);
        assertEquals(
                board.positions(),
                Arrays.asList(
                        new Pos(0,0), new Pos(0,1), new Pos(0,2),
                        new Pos(1,0), new Pos(1,1), new Pos(1,2),
                        new Pos(2,0), new Pos(2,1), new Pos(2,2)
                ),
                "Positions ritorna un valore errato"
        );
    }

    @Test
    @Sorted(25)
    public void positions_ControllaRisultatoCorrettoConExcNonVuoto() {
        BoardOct<PieceModel<PieceModel.Species>> board =
                new BoardOct<PieceModel<PieceModel.Species>>(3,3, Arrays.asList(new Pos(0,0), new Pos(2,1)));
        assertEquals(
                board.positions(),
                Arrays.asList(
                        new Pos(0,1), new Pos(0,2),
                        new Pos(1,0), new Pos(1,1), new Pos(1,2),
                        new Pos(2,0), new Pos(2,2)
                ),
                "Positions ritorna un valore errato"
        );
    }

    // IsModifiable

    @Test
    @Sorted(26)
    public void isModifiable() {
        assertTrue(
                new BoardOct<PieceModel<PieceModel.Species>>(3,3).isModifiable(),
                "isModifiable restituisce false"
        );
    }

    // Put

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(27)
    public void put_BoardImmodificabile() {
        new BoardOct<PieceModel<PieceModel.Species>> (8,8) {
            public boolean isModifiable() { return false; }
        }.put(new PieceModel<>(PieceModel.Species.KNIGHT, "bianco"), new Pos(1,1));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(28)
    public void put_PezzoNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8).put(null, new Pos(1,1));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(29)
    public void put_PosNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(9,9).put(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(30)
    public void put_PezzoEPosEntrambiNull() {
        new BoardOct<PieceModel<PieceModel.Species>>(3,3).put(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(31)
    public void put_PezzoFuoriBoardSuBoardConExcVuoto() {
        new BoardOct<PieceModel<PieceModel.Species>>(5,5).put(
                new PieceModel<PieceModel.Species>(PieceModel.Species.DAMA, "nero"),
                new Pos(10,16)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(32)
    public void put_PezzoFuoriBoardSuBoardConExcNonVuoto() {
        new BoardOct<PieceModel<PieceModel.Species>>(5,5,Arrays.asList(new Pos(2,3))).put(
                new PieceModel<PieceModel.Species>(PieceModel.Species.DAMA, "nero"),
                new Pos(2,3)
        );
    }

    @Test
    @Sorted(33)
    public void put_ControllaRisultatiCorretti() {
        BoardOct<PieceModel<PieceModel.Species>> board = new BoardOct<>(5,5);
        PieceModel<PieceModel.Species> piece = new PieceModel<>(PieceModel.Species.KING, "nero");
        assertEquals(
                board.put(piece, new Pos(0,0)),
                null,
                "Put non ritorna il valore giusto"
        );
        assertEquals(
                board.put(
                        new PieceModel<>(PieceModel.Species.PAWN, "bianco"),
                        new Pos(0,0)
                ),
                piece,
                "Put non ritorna il valore giusto"
        );
        assertEquals(
                board.put(piece, new Pos(2,0)),
                null,
                "Put  non ritorna il valore giusto"
        );
    }

    // Remove

    @Test(expected = NullPointerException.class)
    @Sorted(34)
    public void remove_ArgomentoNullo() {
        new BoardOct<PieceModel<PieceModel.Species>>(9,9).remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(35)
    public void remove_ArgomentoPosFuoriBoard() {
        new BoardOct<PieceModel<PieceModel.Species>>(9,9).remove(new Pos(10,10));
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(36)
    public void remove_BoardImmmodificabile() {
        new BoardOct<PieceModel<PieceModel.Species>>(8,8){
            @Override
            public boolean isModifiable() { return false; }
        }.remove(new Pos(1,1));
    }

    @Test
    @Sorted(37)
    public void remove_ControllaRisultatiCorretti() {
        BoardOct<PieceModel<PieceModel.Species>> board = new BoardOct<>(8,8);
        PieceModel<PieceModel.Species> piece =
                new PieceModel<>(PieceModel.Species.QUEEN, "nero");
        String needPutCorrect = " (NB: la correttezza di questo test dipende dalla correttezza di put)";
        assertEquals(
                board.remove(new Pos(0,0)),
                null,
                "Remove su una casella vuota non restituisce null"
        );
        board.put(piece, new Pos(0,0));
        assertEquals(
                board.remove(new Pos(1,0)),
                null,
                "Remove su una casella vuota in una board non vuota non restituisce null" + needPutCorrect
        );
        assertEquals(
                board.remove(new Pos(0,0)),
                piece,
                "Remove su una casella non vuota ritorna un valore errato" + needPutCorrect
        );
        assertEquals(
                board.remove(new Pos(0,0)),
                null,
                "Remove su una cartella precedentemente occupata ma ora vuota non restituisce null" + needPutCorrect
        );
    }
}

package com.myunit.hw1.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import java.util.Arrays;

public class TestAction {

    // Costruttore Add

    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void costruttoreAdd_PrimoArgomentoNull() {
        new Action<>((Pos)null, new PieceModel<>(PieceModel.Species.BISHOP, "bianco"));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(1)
    public void costruttoreAdd_SecondoArgomentoNull() {
        new Action<PieceModel<PieceModel.Species>>(new Pos(1,1), (Pos)null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(2)
    public void costruttoreAdd_EntrambiGliArgomentiNull() {
        new Action<>((Pos)null, (PieceModel<PieceModel.Species>)null);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(3)
    public void costruttoreAdd_ControllaPosImmodificabile() {
        new Action<>(new Pos(1,1), new PieceModel<>(PieceModel.Species.BISHOP, "bianco")).pos.add(new Pos(1,2));
    }

    @Test
    @Sorted(4)
    public void costruttoreAdd_ControllaValoriInseriti() {
        PieceModel<PieceModel.Species> piece = new PieceModel<>(PieceModel.Species.BISHOP, "bianco");
        Action<PieceModel<PieceModel.Species>> action =
                new Action<>(new Pos(1,1), piece);
        assertEquals(action.kind, Action.Kind.ADD, "Kind è errato");
        assertEquals(action.piece, piece, "Piece è errato");
        assertEquals(action.pos.size(), 1, "Pos non ha dimensione uguale a 1");
        assertEquals(action.pos.get(0), new Pos(1,1), "Il valore inserito in Pos è errato");
        assertEquals(action.dir, null, "Dir non è null");
    }

    // Costruttore Remove

    @Test(expected = NullPointerException.class)
    @Sorted(5)
    public void costruttoreRemove_ArgomentoNull() {
        new Action<PieceModel<PieceModel.Species>>((Pos[])null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(6)
    public void costruttoreRemove_ArgomentoNullTraArgomentiNonNull() {
        Pos[] posList = new Pos[3];
        posList[0] = new Pos(1,3);
        posList[1] = null;
        posList[2] = new Pos(6,7);
        new Action<PieceModel<PieceModel.Species>>(posList);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(7)
    public void costruttoreRemove_ZeroArgomenti() {
        new Action<PieceModel<PieceModel.Species>>();
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(8)
    public void costruttoreRemove_ArgomentiDuplicati() {
        Pos[] posList = new Pos[3];
        posList[0] = new Pos(1,3);
        posList[1] = new Pos(1,3);
        posList[2] = new Pos(6,7);
        new Action<PieceModel<PieceModel.Species>>(posList);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(9)
    public void costruttoreRemove_ControllaPosImmodificabile() {
        Pos[] posList = new Pos[3];
        posList[0] = new Pos(1,3);
        posList[1] = new Pos(2,3);
        posList[2] = new Pos(6,7);
        new Action<PieceModel<PieceModel.Species>>(posList).pos.add(new Pos(4,5));
    }

    @Test
    @Sorted(10)
    public void costruttoreRemove_ControllaValoriInseriti() {
        Pos[] posList = new Pos[3];
        posList[0] = new Pos(1,3);
        posList[1] = new Pos(4,3);
        posList[2] = new Pos(6,7);
        Action<PieceModel<PieceModel.Species>> action = new Action<>(posList);
        assertEquals(action.kind, Action.Kind.REMOVE, "Kind è diverso da Remove");
        assertEquals(action.piece, null, "Piece non è null");
        assertEquals(action.pos, Arrays.asList(posList), "Pos errato");
        assertEquals(action.dir, null, "Dir non è null");
    }

    // Costruttore Move

    @Test(expected = NullPointerException.class)
    @Sorted(11)
    public void costruttoreMove_PrimoArgomentoNull() {
        new Action<PieceModel<PieceModel.Species>>(null, 3, new Pos(1,1), new Pos(3,4), new Pos(6,5));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(12)
    public void costruttoreMove_UnPosNull() {
        new Action<PieceModel<PieceModel.Species>>(Board.Dir.DOWN_L, 3, new Pos(1,1), new Pos(3,4), null, new Pos(6,5));
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(13)
    public void costruttoreMove_NumeroPassiMinoreDiUno() {
        new Action<PieceModel<PieceModel.Species>>(Board.Dir.DOWN, 0, new Pos(1,1), new Pos(3,4), new Pos(6,5));
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(14)
    public void costruttoreMove_NessunaPosizione() {
        new Action<PieceModel<PieceModel.Species>>(Board.Dir.DOWN, 3);
    }


    @Test(expected = IllegalArgumentException.class)
    @Sorted(14)
    public void costruttoreMove_PosizioniDuplicate() {
        new Action<PieceModel<PieceModel.Species>>(Board.Dir.DOWN, 5, new Pos(1,1), new Pos(2,2), new Pos(2,2));
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(15)
    public void costruttoreMove_NumeroPassiMinoreDiUnoENessunaPosizione() {
        new Action<PieceModel<PieceModel.Species>>(Board.Dir.DOWN, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(16)
    public void costruttoreMove_ControllaPosImmutabile() {
        new Action<>(Board.Dir.UP_R, 3, new Pos(1,1), new Pos(3,4), new Pos(6,5)).pos.add(new Pos(0,0));
    }

    @Test
    @Sorted(17)
    public void costruttoreMove_ControllaValoriInseriti() {
        Action<PieceModel<PieceModel.Species>> action =
                new Action<>(Board.Dir.UP_R, 3, new Pos(1,1), new Pos(3,4), new Pos(6,5));
        assertEquals(action.kind, Action.Kind.MOVE, "Kind non è MOVE");
        assertEquals(action.piece, null, "Piece non è null");
        assertEquals(
                action.pos,
                Arrays.asList(new Pos(1,1), new Pos(3,4), new Pos(6,5)),
                "Pos non contiene i valori giusti"
        );
        assertEquals(action.dir, Board.Dir.UP_R, "Dir è errato");
        assertEquals(action.steps, 3, "Steps è errato");
    }

    // Costruttore Jump

    @Test(expected = NullPointerException.class)
    @Sorted(18)
    public void costruttoreJump_PrimoArgomentoNullo() {
        new Action<PieceModel<PieceModel.Species>>((Pos)null, new Pos(3,4));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(19)
    public void costruttoreJump_SecondoArgomentoNullo() {
        new Action<PieceModel<PieceModel.Species>>(new Pos(3,4), (Pos)null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(20)
    public void costruttoreJump_EntrambiGliArgomentiNulli() {
        new Action<PieceModel<PieceModel.Species>>((Pos)null, (Pos)null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(21)
    public void costruttoreJump_ArgomentiUgualiNonNull() {
        new Action<PieceModel<PieceModel.Species>>(new Pos(3,4), new Pos(3,4));
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(22)
    public void costruttoreJump_ControllaPosImmutabile() {
        new Action<PieceModel<PieceModel.Species>>(new Pos(1,1), new Pos(2,3)).pos.add(new Pos(1,3));
    }

    @Test
    @Sorted(23)
    public void costruttoreJump_ControllaValoriInseriti() {
        Action<PieceModel<PieceModel.Species>> action = new Action<>(new Pos(2,3), new Pos(5,7));
        assertEquals(action.kind, Action.Kind.JUMP, "Kind non è Jump");
        assertEquals(action.piece, null, "Piece non è null");
        assertEquals(action.pos, Arrays.asList(new Pos(2,3), new Pos(5,7)), "Pos è errato");
        assertEquals(action.dir, null, "Dir non è null");
    }

    // Costruttore Swap

    @Test(expected = NullPointerException.class)
    @Sorted(24)
    public void costruttoreSwap_PrimoArgomentoNull() {
        new Action<PieceModel<PieceModel.Species>>((PieceModel<PieceModel.Species>)null, new Pos(1,2), new Pos(4,2));
    }

    @Test(expected = NullPointerException.class)
    @Sorted(25)
    public void costruttoreSwap_UnPosNull() {
        new Action<>(
                new PieceModel<>(PieceModel.Species.BISHOP, "bianco"),
                new Pos(1,3),
                new Pos(3,0),
                null
        );
    }

    @Test(expected = NullPointerException.class)
    @Sorted(26)
    public void costruttoreSwap_EntrambiGliArgomentiNull() {
        new Action<>(
                (PieceModel<PieceModel.Species>)null,
                (Pos[])null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(27)
    public void costruttoreSwap_NessunaPosizione() {
        new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(28)
    public void costruttoreSwap_PosizioniDuplicate() {
        new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4),
                new Pos(5,6)
        );
    }

    @Test
    @Sorted(29)
    public void costruttoreSwap_ControllaNessunEccezione() {
        new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4)
        );
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(30)
    public void costruttoreSwap_ControllaPosImmutabile() {
        new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4)
        ).pos.add(new Pos(1,1));
    }

    @Test
    @Sorted(31)
    public void costruttoreSwap_ControllaValoriInseriti() {
        Action<PieceModel<PieceModel.Species>> action = new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4)
        );
        assertEquals(action.kind, Action.Kind.SWAP, "Kind non è Swap");
        assertEquals(
                action.piece,
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                "Piece è errato"
        );
        assertEquals(
                action.pos,
                Arrays.asList(new Pos(3,3), new Pos(5,6), new Pos(5,4)),
                "Pos è errato"
        );
        assertEquals(action.dir, null, "Dir non è null");
    }

    // Equals

    @Test
    @Sorted(32)
    public void equals_ConfrontoConNull() {
        assertFalse(new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4)
        ).equals(null), "Confronto di uguaglianza con null ha restituito true");
    }

    @Test
    @Sorted(33)
    public void equals_ConfrontoConSuperclasseObject() {
        assertFalse(new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4)
        ).equals(new Object()), "Confronto di uguaglianza con un Object ha restituito true");
    }

    @Test
    @Sorted(34)
    public void equals_ConfrontoConAltraClasseString() {
        assertFalse(new Action<>(
                new PieceModel<>(PieceModel.Species.KNIGHT, "nero"),
                new Pos(3,3),
                new Pos(5,6),
                new Pos(5,4)
        ).equals("aaa"), "Confronto di uguaglianza con una String ha restituito true");
    }

    @Test
    @Sorted(35)
    public void equals_ConfrontoTraSwapActionsDiversePerPiecemodel() {
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)),
                new Action<>(new PieceModel<>(PieceModel.Species.BISHOP, "bianco"), new Pos(3,3), new Pos(5,6)),
                "Actions diverse per il PieceModel passato al costruttore sono risultate uguali" +
                        "(NB: la correttezza di questo test dipende dalla correttezza della creazione di Actions Swap)"
        );
    }

    @Test
    @Sorted(36)
    public void equals_ConfrontoTraSwapActionsDiversePerPos() {
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,7)),
                "Actions diverse per le Pos passate al costruttore sono risultate uguali" +
                        "(NB: la correttezza di questo test dipende dalla correttezza della creazione di Actions Swap)"
        );
    }

    @Test
    @Sorted(37)
    public void equals_ConfrontoPosIdentiche() {
        assertEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6))
        );
    }

    @Test
    @Sorted(38)
    public void equals_ConfrontoActionsStessePosInOrdineDiverso() {
        assertEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6))
        );
    }

    @Test
    @Sorted(39)
    public void equals_ConfrontoActionsAlcunePosizioniUguali() {
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(1,1), new Pos(5,6)),
                "Test 1"
        );
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(1,1), new Pos(5,6)),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)),
                "Test 2"
        );
    }

    // Hashcode

    @Test
    @Sorted(60)
    public void hashCode_Uguali() {
        assertEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)).hashCode(),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)).hashCode()
        );
    }

    @Test
    @Sorted(61)
    public void hashCode_Diversi() {
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)).hashCode(),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3)).hashCode(),
                "Swap Actions che differiscono per Pos hanno ritornato lo stesso hashcode"
        );
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)).hashCode(),
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,7)).hashCode(),
                "Swap Actions che differiscono per Pos hanno ritornato lo stesso hashcode"
        );
        assertNotEquals(
                new Action<>(new PieceModel<>(PieceModel.Species.KNIGHT, "nero"), new Pos(3,3), new Pos(5,6)).hashCode(),
                new Action<>(new PieceModel<>(PieceModel.Species.BISHOP, "nero"), new Pos(3,3), new Pos(5,6)).hashCode(),
                "Swap Actions che differiscono per PieceModel hanno ritornato lo stesso hashcode"
        );
    }
}

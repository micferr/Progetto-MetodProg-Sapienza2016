package com.myunit.hw1.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.PieceModel;

public class TestPieceModel {
    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void costruttore_PrimoParametroNull() {
        new PieceModel<PieceModel.Species>(null, "aaaa");
    }

    @Test(expected = NullPointerException.class)
    @Sorted(1)
    public void costruttore_SecondoParametroNull() {
        new PieceModel<PieceModel.Species>(PieceModel.Species.DISC, null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(2)
    public void costruttore_EntrambiIParametriNull() {
        new PieceModel<PieceModel.Species>(null, null);
    }

    @Test
    @Sorted(3)
    public void costruttore_EntrambiIParametriValidi() {
        new PieceModel<PieceModel.Species>(PieceModel.Species.DISC, "aaaa");
    }

    @Test
    @Sorted(4)
    public void costruttore_ControllaParametriImpostatiCorrettamente() {
        PieceModel<PieceModel.Species> piece = new PieceModel<>(PieceModel.Species.DISC, "aaaa");
        assertEquals(piece.species, PieceModel.Species.DISC, "Specie del pezzo non impostata correttamente");
        assertEquals(piece.color, "aaaa", "Colore del pezzo non impostato correttamente");
    }

    /// Equals
    
    @Test
    @Sorted(5)
    public void equals_ValoreNull() {
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa"),
                (PieceModel<PieceModel.Species>)null,
                "Un PieceModel non null è risultato uguale a un PieceModel null"
        );
    }

    @Test
    @Sorted(6)
    public void equals_EntrambiNull() {
        assertEquals(
                (PieceModel<PieceModel.Species>)null,
                (PieceModel<PieceModel.Species>)null,
                "Un PieceModel null è risultato diverso da un PieceModel null");
    }

    @Test
    @Sorted(7)
    public void equals_ValoriUguali() {
        assertEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa"),
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa"),
                "Due valori uguali sono risultati diversi"
        );
    }

    @Test
    @Sorted(8)
    public void equals_ValoriDiversi() {
        String msg = "Due valori diversi sono risultati uguali";
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa"),
                new PieceModel<PieceModel.Species>(PieceModel.Species.DAMA, "aaaa"),
                msg
        );
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa"),
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "bbbb"),
                msg
        );
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa"),
                new PieceModel<PieceModel.Species>(PieceModel.Species.DAMA, "bbbb"),
                msg
        );
    }

    /// Hashcode

    @Test
    @Sorted(9)
    public void hashcode_Uguali() {
        assertEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa").hashCode(),
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa").hashCode(),
                "Valori uguali hanno restituito hashcode diversi"
        );
    }

    @Test
    @Sorted(10)
    public void hashcode_Diversi() {
        String msg = "Valori diversi hanno restituito hashcode uguali";
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa").hashCode(),
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "bbbb").hashCode(),
                msg
        );
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa").hashCode(),
                new PieceModel<PieceModel.Species>(PieceModel.Species.DAMA, "bbbb").hashCode(),
                msg
        );
        assertNotEquals(
                new PieceModel<PieceModel.Species>(PieceModel.Species.BISHOP, "aaaa").hashCode(),
                new PieceModel<PieceModel.Species>(PieceModel.Species.DAMA, "aaaa").hashCode(),
                msg
        );
    }
}

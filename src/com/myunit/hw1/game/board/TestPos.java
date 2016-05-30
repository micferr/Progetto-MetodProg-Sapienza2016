package com.myunit.hw1.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Pos;

public class TestPos {

    /// Costruttore

    @Test(expected = IllegalArgumentException.class)
    @Sorted(0)
    public void costruttore_PrimoParametroNegativo() {
        new Pos(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(1)
    public void costruttore_SecondoParametroNegativo() {
        new Pos(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(2)
    public void costruttore_EntrambiIParametriNegativi() {
        new Pos(-1, -1);
    }

    @Test
    @Sorted(3)
    public void costruttore_EntrambiIParametriZero() {
        new Pos(0,0);
    }

    @Test
    @Sorted(4)
    public void costruttore_EntrambiIParametriPositivi() {
        new Pos(1,2);
    }

    @Test
    @Sorted(5)
    public void costruttore_ParametriImpostatiCorrettamente() {
        Pos p = new Pos(1,2);
        assertEquals(p.b, 1, "Il valore di b non viene assegnato correttamente nel costruttore");
        assertEquals(p.t, 2, "Il valore di t non viene assegnato correttamente nel costruttore");
    }

    /// Equals

    @Test
    @Sorted(6)
    public void equals_ValoreNull() {
        assertNotEquals(new Pos(1,3), (Pos)null, "Un Pos non null è risultato uguale a una Pos null");
    }

    @Test
    @Sorted(7)
    public void equals_EntrambiNull() {
        assertEquals((Pos)null, (Pos)null, "Una Pos null è risultata uguale a una Pos null");
    }

    @Test
    @Sorted(8)
    public void equals_ValoriUguali() {
        String msg = "Due valori uguali sono risultati diversi";
        assertEquals(new Pos(1,2), new Pos(1,2), msg);
        assertEquals(new Pos(1,1), new Pos(1,1), msg);
    }

    @Test
    @Sorted(9)
    public void equals_ValoriDiversi() {
        String msg = "Due valori diversi sono risultati uguali";
        assertNotEquals(new Pos(0,1), new Pos(0,2), msg);
        assertNotEquals(new Pos(1,0), new Pos(2,0), msg);
        assertNotEquals(new Pos(1,2), new Pos(3,4), msg);
    }

    /// Hashcode

    @Test
    @Sorted(10)
    public void hashcode_Uguali() {
        assertEquals(new Pos(1,3).hashCode(), new Pos(1,3).hashCode(), "Valori uguali hanno restituito hashcode diversi");
    }

    @Test
    @Sorted(11)
    public void hashcode_Diversi() {
        String msg = "Valori diversi hanno restituito hashcode uguali";
        assertNotEquals(new Pos(1,2).hashCode(), new Pos(1,3).hashCode(), msg);
        assertNotEquals(new Pos(1,1).hashCode(), new Pos(2,2).hashCode(), msg);
        assertNotEquals(new Pos(1,0).hashCode(), new Pos(2,0).hashCode(), msg);
        assertNotEquals(new Pos(3,2).hashCode(), new Pos(2,3).hashCode(), msg);
    }
}

package com.myunit.hw1.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;

import static com.myunit.assertion.Assert.*;

import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestMove {

    // Costruttore Non Action

    @Test(expected = NullPointerException.class)
    @Sorted(0)
    public void costruttoreNonAction_ParametroNull() {
        new Move<PieceModel<PieceModel.Species>>((Move.Kind)null);
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(1)
    public void costruttoreNonAction_ParametroAction() {
        new Move<PieceModel<PieceModel.Species>>(Move.Kind.ACTION);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(2)
    public void costruttoreNonAction_ControllaActionImmodificabile() {
        new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN).actions.add(null);
    }

    @Test
    @Sorted(3)
    public void costruttoreNonAction_ControllaValoriInseriti() {
        Move<PieceModel<PieceModel.Species>> move1 = new Move<>(Move.Kind.PASS);
        Move<PieceModel<PieceModel.Species>> move2 = new Move<>(Move.Kind.RESIGN);
        assertEquals(move1.kind, Move.Kind.PASS, "Kind è errato");
        assertEquals(move2.kind, Move.Kind.RESIGN, "Kind è errato");
        assertEquals(move1.actions, Collections.EMPTY_LIST, "Actions è errato");
        assertEquals(move2.actions, Collections.EMPTY_LIST, "Actions è errato");
    }

    // Costruttore Action Varargs

    /*@Test(expected = IllegalArgumentException.class)
    @Sorted(4)
    public void costruttoreActionVarargs_ArgomentoArrayNull() {
        new Move<>((Action[])null);
    }*/

    @Test(expected = NullPointerException.class)
    @Sorted(5)
    public void costruttoreActionVarargs_ArgomentoArrayConNull() {
        new Move<>(
                new Action<>(new Pos(0,1), new Pos(3,4)),
                null,
                new Action<>(new Pos(7,4), new Pos(4,4))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(6)
    public void costruttoreActionVarargs_ArgomentoArrayVuoto() {
        new Move<>();
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(7)
    public void costruttoreActionVarargs_ControllaActionsImmodificabile() {
        new Move<>(
                new Action<>(new Pos(0,1), new Pos(3,4)),
                new Action<>(new Pos(7,4), new Pos(4,4))
        ).actions.add(new Action<>(new Pos(4,5), new Pos(2,3)));
    }

    @Test
    @Sorted(8)
    public void costruttoreActionVarargs_ControllaValoriInseriti() {
        Action<PieceModel<PieceModel.Species>>[] actions = new Action[]{
                new Action<>(new Pos(3,3), new Pos(3,4)),
                new Action<>(new Pos(3,4), new PieceModel<>(PieceModel.Species.PAWN, "bianco"))
        };
        Move<PieceModel<PieceModel.Species>> move = new Move<>(actions);
        assertEquals(move.kind, Move.Kind.ACTION, "Kind è errato");
        assertEquals(move.actions, Arrays.asList(actions), "Actions è errato");
    }

    // Costruttore Action Lista

    @Test(expected = NullPointerException.class)
    @Sorted(9)
    public void costruttoreActionList_ArgomentoListaNull() {
        new Move<>((List)null);
    }

    @Test(expected = NullPointerException.class)
    @Sorted(10)
    public void costruttoreActionList_ArgomentoListaConNull() {
        new Move<>( Arrays.asList(
                new Action<>(new Pos(0,1), new Pos(3,4)),
                null,
                new Action<>(new Pos(7,4), new Pos(4,4))
        ));
    }

    @Test(expected = IllegalArgumentException.class)
    @Sorted(11)
    public void costruttoreActionList_ArgomentoListaVuota() {
        new Move<>(Collections.EMPTY_LIST);
    }

    @Test(expected = UnsupportedOperationException.class)
    @Sorted(12)
    public void costruttoreActionList_ControllaActionsImmodificabile() {
        new Move<>( Arrays.asList(
                new Action<>(new Pos(0,1), new Pos(3,4)),
                new Action<>(new Pos(7,4), new Pos(4,4))
        )).actions.add(new Action<>(new Pos(4,5), new Pos(2,3)));
    }

    @Test
    @Sorted(13)
    public void costruttoreActionList_ControllaListaNonMantenuta() {
        List<Action<PieceModel<PieceModel.Species>>> actions = new ArrayList<>();
        actions.add(new Action<>(new Pos(0,1), new Pos(3,4)));
        actions.add(new Action<>(new Pos(7,4), new Pos(4,4)));
        Move<PieceModel<PieceModel.Species>> move = new Move<>(actions);
        actions.add(null);
        assertEquals(move.actions.size(), 2, "La lista in input è stata mantenuta e non copiata");
    }

    @Test
    @Sorted(14)
    public void costruttoreActionList_ControllaValoriInseriti() {
        List<Action<PieceModel<PieceModel.Species>>> actions = Arrays.asList(
                new Action<>(new Pos(3,3), new Pos(3,4)),
                new Action<>(new Pos(3,4), new PieceModel<>(PieceModel.Species.PAWN, "bianco"))
        );
        Move<PieceModel<PieceModel.Species>> move = new Move<>(actions);
        assertEquals(move.kind, Move.Kind.ACTION, "Kind è errato");
        assertEquals(move.actions, actions, "Actions è errato");
    }

    // Equals

    @Test
    @Sorted(15)
    public void equals_Null() {
        assertNotEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN),
                null,
                "Equals(null) ha restituito true"
        );
    }

    @Test
    @Sorted(16)
    public void equals_Superclasse() {
        assertNotEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN),
                new Object(),
                "Equals(Object) ha restituito true"
        );
    }

    @Test
    @Sorted(17)
    public void equals_OggettoFuoriGerarchia() {
        assertNotEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN),
                "",
                "Equals(String) ha restituito true"
        );
    }

    @Test
    @Sorted(18)
    public void equals_DiversiPerKind() {
        assertNotEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN),
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.PASS),
                "Due move diversi per kind sono risultati uguali"
        );
    }

    @Test
    @Sorted(19)
    public void equals_DiversiPerActions() {
        assertNotEquals(
                new Move<PieceModel<PieceModel.Species>>(
                        new Action<PieceModel<PieceModel.Species>>(new Pos(1,1), new Pos(1,4))
                ),
                new Move<PieceModel<PieceModel.Species>>(
                        new Action<PieceModel<PieceModel.Species>>(new Pos(3,4), new Pos(4,5))
                ),
                "Due move diversi per actions sono risultati uguali"
        );
    }

    @Test
    @Sorted(20)
    public void equals_Uguali() {
        assertEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN),
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN),
                "Due move uguali sono risultati diversi"
        );
    }

    @Test
    @Sorted(21)
    public void equals_StessoOggetto() {
        Move<PieceModel<PieceModel.Species>> move = new Move<>(Move.Kind.RESIGN);
        assertEquals(
                move,
                move,
                "Un move è risultato diverso da se stesso"
        );
    }

    // Hashcode

    @Test
    @Sorted(22)
    public void hashcode_Diversi() {
        assertNotEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN).hashCode(),
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.PASS).hashCode(),
                "Move diversi hanno restituito lo stesso hashcode"
        );
    }

    @Test
    @Sorted(23)
    public void hashcode_Uguali() {
        assertEquals(
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN).hashCode(),
                new Move<PieceModel<PieceModel.Species>>(Move.Kind.RESIGN).hashCode(),
                "Move uguali non hanno restituito lo stesso hashcode"
        );
    }
}

package com.myunit.hw2.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.game.board.Action;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import static com.myunit.assertion.Assert.*;

public class TestAction {

    //Equals

    @Test
    @Sorted(0)
    public void equals_ActionsUgualiConPosUguali() {
        assertEquals(
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,3)),
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,3))
        );
    }

    @Test
    @Sorted(10)
    public void equals_ActionsUgualiConPosUgualiInOrdineDiverso() {
        assertEquals(
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,3)),
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,3), new Pos(1,2))
        );
    }

    // HashCode

    @Test
    @Sorted(20)
    public void hashCode_ActionsUguali() {
        assertEquals(
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,3)),
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,3))
        );
    }

    @Test
    @Sorted(30)
    public void hashCode_ActionsDiverse() {
        assertNotEquals(
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,3)),
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(1,4))
        );
    }

    @Test
    @Sorted(40)
    public void hashCode_ActionsUgualiPosOrdineDiverso() {
        assertEquals(
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,2), new Pos(3,3), new Pos(1,3)),
                new Action<PieceModel<PieceModel.Species>>(new Pos(1,3), new Pos(3,3), new Pos(1,2))
        );
    }
}
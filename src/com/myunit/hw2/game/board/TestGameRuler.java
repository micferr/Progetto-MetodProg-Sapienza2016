package com.myunit.hw2.game.board;

import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import static com.myunit.assertion.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by micfe on 11/05/2016.
 */
public class TestGameRuler {
    @Test
    @Sorted(0)
    public void Situations_ControllaMappaNonCopiataMaMantenuta() {
        Map<Pos, PieceModel<PieceModel.Species>> m = new HashMap<>();
        GameRuler.Situation<PieceModel<PieceModel.Species>> s = new GameRuler.Situation<>(m, 1);
        m.put(new Pos(0,0), new PieceModel<>(PieceModel.Species.DISC, "nero"));
        assertEquals(
                s.newMap().size(),
                m.size()
        );
    }
}

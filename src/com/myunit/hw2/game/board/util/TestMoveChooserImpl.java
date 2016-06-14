package com.myunit.hw2.game.board.util;

import com.myunit.hw2.game.board.GameRulerAdapter;
import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.game.board.*;
import gapp.ulg.game.util.MoveChooserImpl;
import gapp.ulg.game.util.Node;
import gapp.ulg.games.MNKgame;
import gapp.ulg.games.Othello;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.myunit.assertion.Assert.*;

public class TestMoveChooserImpl {
    @Test
    @Sorted(0)
    public static void testRadice1() {
        GameRuler<PieceModel<PieceModel.Species>> gR = new GameRulerAdapter<PieceModel<PieceModel.Species>>() {
            @Override
            public Set<Move<PieceModel<PieceModel.Species>>> validMoves() {
                Set<Move<PieceModel<PieceModel.Species>>> mosse = new HashSet<>();
                Action<PieceModel<PieceModel.Species>>
                        action1 = new Action<>(new Pos(2,0), new Pos(4,2)),
                        action2 = new Action<>(new Pos(4,2), new Pos(2,4)),
                        action3 = new Action<>(new Pos(2,4), new Pos(0,6)),
                        action4 = new Action<>(new Pos(3,1), new Pos(3,3), new Pos(1,5)),
                        action5 = new Action<>(new Pos(2,4), new Pos(0,2)),
                        action6 = new Action<>(new Pos(3,1), new Pos(3,3), new Pos(1,3)),
                        action7 = new Action<>(new Pos(4,2), new Pos(6,4)),
                        action8 = new Action<>(new Pos(6,4), new Pos(4,6)),
                        action9 = new Action<>(new Pos(3,1), new Pos(5,3), new Pos(5,5));
                mosse.add(new Move<>(action1,action2,action3,action4));
                mosse.add(new Move<>(action1,action2,action5,action6));
                mosse.add(new Move<>(action1,action7,action8,action9));
                return mosse;
            }
        };
        MoveChooserImpl<PieceModel<PieceModel.Species>> mC = new MoveChooserImpl<>(gR);
        Node<List<Action<PieceModel<PieceModel.Species>>>> root = mC.getRoot();
        assertEquals(root.value.size(), 1, "Dimensione radice errata: " + root.value.size());
        assertEquals(root.value.get(0), new Action<>(new Pos(2,0), new Pos(4,2)), "Azione errata");
    }

    @Test
    @Sorted(10)
    public static void testRadiceOthelloInizio() {
        Othello o = new Othello("a","b");
        MoveChooserImpl<PieceModel<PieceModel.Species>> mC = new MoveChooserImpl<>(o);
        Node<List<Action<PieceModel<PieceModel.Species>>>> root = mC.getRoot();
        assertEquals(root.value, Collections.EMPTY_LIST);
    }

    @Test
    @Sorted(20)
    public static void testRadiceMNKInizio() {
        MNKgame o = new MNKgame(-1,3,3,3,"a","b");
        MoveChooserImpl<PieceModel<PieceModel.Species>> mC = new MoveChooserImpl<>(o);
        Node<List<Action<PieceModel<PieceModel.Species>>>> root = mC.getRoot();
        assertEquals(root.value, Collections.EMPTY_LIST);
    }
}

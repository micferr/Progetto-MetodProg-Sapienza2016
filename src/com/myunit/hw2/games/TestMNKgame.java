package com.myunit.hw2.games;

import com.myunit.test.Sorted;
import com.myunit.test.Test;
import gapp.ulg.game.Param;
import gapp.ulg.game.board.*;
import gapp.ulg.game.util.Utils;
import gapp.ulg.games.MNKgame;
import gapp.ulg.games.MNKgameFactory;
import gapp.ulg.play.RandPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMNKgame {
    public static void printParams(List<Param<?>> params) {
        //Param 0
        System.out.println(params.get(0).name() + "\t" + params.get(0).get());
        //Param 1
        System.out.println(params.get(1).name() + "\t\t" + params.get(1).get());
        System.out.print("Values: ");
        for (Object o : params.get(1).values()) System.out.print(o + "\t");
        System.out.println();
        //Param 2
        System.out.println(params.get(2).name() + "\t\t" + params.get(2).get());
        System.out.print("Values: ");
        for (Object o : params.get(2).values()) System.out.print(o + "\t");
        System.out.println();
        //Param 3
        System.out.println(params.get(3).name() + "\t\t" + params.get(3).get());
        System.out.print("Values: ");
        for (Object o : params.get(3).values()) System.out.print(o + "\t");
        System.out.println();
        System.out.println();
    }

    /*@Test
    @Sorted(10000)*/
    public static void main(String[] args) {
        MNKgame game = (MNKgame)Utils.play(
                new MNKgameFactory(),
                new RandPlayer<PieceModel<PieceModel.Species>>("a"),
                new RandPlayer<PieceModel<PieceModel.Species>>("b")
        );
        game.unMove();
        Move<PieceModel<PieceModel.Species>> m=null;
        for (Move<PieceModel<PieceModel.Species>> mm : game.validMoves()) {
            if (mm.kind != Move.Kind.RESIGN) {
                m=mm;
                break;
            }
        }
        game.move(m);
    }

    public static GameRuler.Situation<PieceModel<PieceModel.Species>> toSituation(MNKgame mnkGame) {
        PieceModel<PieceModel.Species> whitePiece = new PieceModel<>(PieceModel.Species.DISC, "bianco");
        PieceModel<PieceModel.Species> blackPiece = new PieceModel<>(PieceModel.Species.DISC, "nero");
        int turn = mnkGame.result() == -1 ? mnkGame.turn() : -mnkGame.result();
        /**
         * La creazione di una mappa è in conflitto con il costruttore
         * di Situation (mappa letta senza copia)? La mappa creata qui
         * non influenza la board di this, cioè si comporta da copia,
         * ma GameRuler non prevede che la mappa di pezzi possa essere
         * esposta
         */
        Map<Pos, PieceModel<PieceModel.Species>> pieceMap = new HashMap<>();
        for (Pos p : mnkGame.getBoard().get(blackPiece))
            pieceMap.put(p, blackPiece);
        for (Pos p : mnkGame.getBoard().get(whitePiece))
            pieceMap.put(p, whitePiece);
        //Todo chi verifica la validità di pieceMap (non-null, solo pezzi giusti, solo posizioni in board)?
        return new GameRuler.Situation<>(pieceMap, turn);
    }

    public static void testPartita() {
        MNKgame game = new MNKgame(-1,5,7,3,"a","b");
        game.move(new Move<>(new Action<>(new Pos(4,0), new PieceModel<>(PieceModel.Species.DISC, "nero"))));
        game.move(new Move<>(new Action<>(new Pos(0,6), new PieceModel<>(PieceModel.Species.DISC, "bianco"))));
        game.move(new Move<>(new Action<>(new Pos(2,2), new PieceModel<>(PieceModel.Species.DISC, "nero"))));
        game.move(new Move<>(new Action<>(new Pos(4,6), new PieceModel<>(PieceModel.Species.DISC, "bianco"))));
        game.move(new Move<>(new Action<>(new Pos(3,1), new PieceModel<>(PieceModel.Species.DISC, "nero"))));
        System.err.println("Risultato finale: " + game.result());
    }

    public static void testMNKgameFactoryParams() {
        MNKgameFactory mgF = new MNKgameFactory();
        printParams(mgF.params());
        System.out.print("Set M 5\n\n");
        mgF.params().get(1).set(5);
        printParams(mgF.params());
        System.out.print("Set N 2\n\n");
        mgF.params().get(2).set(2);
        printParams(mgF.params());
        System.out.print("Set M 1\n\n");
        mgF.params().get(1).set("INVALIDO");
    }
}

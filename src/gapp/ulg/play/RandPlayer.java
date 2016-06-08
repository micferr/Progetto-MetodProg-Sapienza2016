package gapp.ulg.play;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto RandPlayer è un oggetto che può giocare un qualsiasi gioco regolato
 * da un {@link GameRuler} perché, ad ogni suo turno, sceglie in modo random una
 * mossa tra quelle valide esclusa {@link Move.Kind#RESIGN}.
 * @param <P>  tipo del modello dei pezzi */
public class RandPlayer<P> implements Player<P> {
    private String name;
    public GameRuler<P> game; //Todo set private

    /** Crea un giocatore random, capace di giocare a un qualsiasi gioco, che ad
     * ogni suo turno fa una mossa scelta in modo random tra quelle valide.
     * @param name  il nome del giocatore random
     * @throws NullPointerException se name è null */
    public RandPlayer(String name) {
        if (name != null) {
            this.name = name;
        } else {
            throw new NullPointerException("Player name must not be null");
        }
    }

    @Override
    public String name() { return this.name; }

    @Override
    public void setGame(GameRuler<P> g) {
        if (g != null) {
            this.game = g;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void moved(int i, Move<P> m) {
        if (game != null && game.result() == -1) {
            if (m != null) {
                if (/*i == game.turn()*/game.isPlaying(i) && game.isValid(m)) {
                    game.move(m);
                } else {
                    throw new IllegalArgumentException("Illegal turn or move.");
                }
            } else {
                throw new NullPointerException("m in RandPlayer#moved(int i, Move<P> m) must not be null.");
            }
        } else {
            throw new IllegalStateException("Game is either not set or finished.");
        }
    }

    @Override
    public Move<P> getMove() {
        if (game != null && game.result() == -1 && game.players().get(game.turn()-1).equals(name())) { //Todo ??????????????
            Set<Move<P>> validMovesSet = game.validMoves();
            List<Move<P>> validMovesList = new ArrayList<>();
            for (Move<P> m : validMovesSet) {
                if (m.kind != Move.Kind.RESIGN) {
                    validMovesList.add(m);
                }
            }
            return validMovesList.get(new Random().nextInt(validMovesList.size()));
        } else {
            throw new IllegalStateException();
        }
    }
}

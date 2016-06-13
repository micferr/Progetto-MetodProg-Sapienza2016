package gapp.ulg.game.util;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.Pos;

import java.util.*;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Gli oggetti BoardOct implementano l'interfaccia {@link Board} per rappresentare
 * board generali con sistema di coordinate {@link System#OCTAGONAL}
 * modificabili.
 * @param <P>  tipo del modello dei pezzi */
public class BoardOct<P> extends ModifiableBoard<P> {

    /** Crea una BoardOct con le dimensioni date (può quindi essere rettangolare).
     * Le posizioni della board sono tutte quelle comprese nel rettangolo dato e le
     * adiacenze sono tutte e otto, eccetto per le posizioni di bordo.
     * @param width  larghezza board
     * @param height  altezza board
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardOct(int width, int height) {
        this(width, height, new ArrayList<>());
    }

    /** Crea una BoardOct con le dimensioni date (può quindi essere rettangolare)
     * escludendo le posizioni in exc. Le adiacenze sono tutte e otto, eccetto per
     * le posizioni di bordo o adiacenti a posizioni escluse. Questo costruttore
     * permette di creare board per giochi come ad es.
     * <a href="https://en.wikipedia.org/wiki/Camelot_(board_game)">Camelot</a>
     * @param width  larghezza board
     * @param height  altezza board
     * @param exc  posizioni escluse dalla board
     * @throws NullPointerException se exc è null
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardOct(int width, int height, Collection<? extends Pos> exc) {
        super(width, height, exc);
    }

    @Override
    public System system() {
        return System.OCTAGONAL;
    }

    @Override
    public Pos computeAdjacent(Pos p, Dir d) {
        int b, t;
        switch (d) {
            case LEFT:
                b = p.b - 1;
                t = p.t;
                break;
            case RIGHT:
                b = p.b + 1;
                t = p.t;
                break;
            case UP:
                b = p.b;
                t = p.t + 1;
                break;
            case DOWN:
                b = p.b;
                t = p.t - 1;
                break;
            case UP_L:
                b = p.b - 1;
                t = p.t + 1;
                break;
            case UP_R:
                b = p.b + 1;
                t = p.t + 1;
                break;
            case DOWN_L:
                b = p.b - 1;
                t = p.t - 1;
                break;
            case DOWN_R:
                b = p.b + 1;
                t = p.t - 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
        if (b >= 0 && t >= 0) {
            Pos adj = new Pos(b, t);
            return isPos(adj) ? adj : null;
        } else {
            return null;
        }
    }
}

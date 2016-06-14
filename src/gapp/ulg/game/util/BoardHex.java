package gapp.ulg.game.util;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.Pos;

import java.util.*;

/** <b>L'IMPLEMENTAZIONE DI QUESTA CLASSE È LASCIATA COME FALCOLTATIVA.</b>
 * <br>
 * Gli oggetti BoardHex implementano l'interfaccia {@link Board} per rappresentare
 * board generali con sistema di coordinate {@link System#HEXAGONAL}
 * modificabili.
 * @param <P>  tipo del modello dei pezzi */
public class BoardHex<P> extends ConcreteBoard<P> {

    /** Crea una BoardHex con le dimensioni date (può quindi essere rettangolare).
     * Le posizioni della board sono tutte quelle comprese nel rettangolo dato e le
     * adiacenze sono le sei per il sistema {@link System#HEXAGONAL}, eccetto
     * per le posizioni di bordo.
     * @param width  larghezza board
     * @param height  altezza board
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardHex(int width, int height) {
        super(width, height);
    }

    /** Crea una BoardHex con le dimensioni date (può quindi essere rettangolare)
     * escludendo le posizioni in exc. Le adiacenze sono le sei per il sistema
     * {@link System#HEXAGONAL}, eccetto per le posizioni di bordo o adiacenti
     * a posizioni escluse. Questo costruttore permette di creare board per giochi
     * come ad es.
     * <a href="https://it.wikipedia.org/wiki/Abalone">Abalone</a>
     * @param width  larghezza board
     * @param height  altezza board
     * @param exc  posizioni escluse dalla board
     * @throws NullPointerException se exc è null
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardHex(int width, int height, Collection<? extends Pos> exc) {
        super(width, height, exc);
    }

    @Override
    public System system() {
        return System.HEXAGONAL;
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
                throw new IllegalArgumentException("Invalid direction: " + d + " (Should never happen)");
        }
        if (b >= 0 && t >= 0) {
            Pos adj = new Pos(b, t);
            return isPos(adj) ? adj : null;
        } else {
            return null;
        }
    }
}

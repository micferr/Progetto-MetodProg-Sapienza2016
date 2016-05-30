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
public class BoardOct<P> implements Board<P> {
    private int width, height;
    private List<Pos> positions;
    private Map<Pos, P> pieces;

    /** Crea una BoardOct con le dimensioni date (può quindi essere rettangolare).
     * Le posizioni della board sono tutte quelle comprese nel rettangolo dato e le
     * adiacenze sono tutte e otto, eccetto per le posizioni di bordo.
     * @param width  larghezza board
     * @param height  altezza board
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardOct(int width, int height) {
        this(width, height, Collections.EMPTY_LIST);
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
        //Todo: posizioni nulle? posizioni ripetute?
        if (exc != null) {
            if (width > 0 && height > 0) {
                this.width = width;
                this.height = height;
                List<Pos> positionsModifiable = new ArrayList<>();
                for (int b = 0; b < width; ++b) {
                    for (int t = 0; t < height; ++t) {
                        Pos p = new Pos(b, t);
                        if (!exc.contains(p)) {
                            positionsModifiable.add(p);
                        }
                    }
                }
                this.positions = Collections.unmodifiableList(positionsModifiable);
                this.pieces = new HashMap<>();
            } else {
                throw new IllegalArgumentException("Width and Height must both be positive.");
            }
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public System system() {
        return System.OCTAGONAL;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public Pos adjacent(Pos p, Dir d) {
        if (p != null && d != null) {
            if (isPos(p)) {
                int b, t;
                switch (d) {
                    case LEFT: b = p.b-1; t = p.t; break;
                    case RIGHT: b = p.b+1; t = p.t; break;
                    case UP: b = p.b; t = p.t+1; break;
                    case DOWN: b = p.b; t = p.t-1; break;
                    case UP_L: b = p.b-1; t = p.t+1; break;
                    case UP_R: b = p.b+1; t = p.t+1; break;
                    case DOWN_L: b = p.b-1; t = p.t-1; break;
                    case DOWN_R: b = p.b+1; t = p.t-1; break;
                    default: throw new IllegalArgumentException("Invalid direction");
                }
                if (b >= 0 && t >= 0) {
                    Pos adj = new Pos(b, t);
                    return isPos(adj) ? adj : null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public List<Pos> positions() {
        return positions;
    }

    @Override
    public P get(Pos p) {
        if (p != null) {
            return pieces.get(p);
        } else {
            throw new NullPointerException("p in BoardOct#get(Pos p) must not be null.");
        }
    }

    @Override
    public boolean isModifiable() { return true; }

    @Override
    public P put(P pm, Pos p) {
        if (isModifiable()) {
            if (pm != null && p != null) {
                if (positions().contains(p)) {
                    P oldP = pieces.get(p);
                    pieces.put(p, pm);
                    return oldP;
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new NullPointerException();
            }
        } else {
            throw new UnsupportedOperationException("Cannot put pieces on an unmodifiable Board.");
        }
    }

    @Override
    public P remove(Pos p) {
        if (isModifiable()) {
            if (p != null) {
                if (positions().contains(p)) {
                    return pieces.remove(p);
                } else {
                    throw new IllegalArgumentException("p must be a position in the Board");
                }
            } else {
                throw new NullPointerException("p in BoardOct#remove(Pos p) must not be null.");
            }
        } else {
            throw new UnsupportedOperationException("Cannot remove pieces from an unmodifiable Board.");
        }
    }
}

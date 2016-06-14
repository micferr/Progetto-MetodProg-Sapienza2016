package gapp.ulg.game.util;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.Pos;

import java.util.*;

public abstract class ConcreteBoard<P> implements Board<P> {
    protected int width, height;
    protected List<Pos> positions;
    protected Map<Pos, P> pieces;

    public ConcreteBoard(int width, int height) {
        this(width, height, new ArrayList<>());
    }

    public ConcreteBoard(int width, int height, Collection<? extends Pos> exc) {
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
                return computeAdjacent(p, d);
            } else {
                return null;
            }
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Ritorna la posizione adiacente alla posizione p nella direzione d. Si assume
     * che la posizione p appartenga alla board
     * @param p  una posizione
     * @param d  una direzione
     * @return la posizione adiacente alla posizione p nella direzione d o null
     */
    protected Pos computeAdjacent(Pos p, Dir d) {
        throw new UnsupportedOperationException("DA IMPLEMENTARE.");
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
            throw new NullPointerException("p in Board#get(Pos p) must not be null.");
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

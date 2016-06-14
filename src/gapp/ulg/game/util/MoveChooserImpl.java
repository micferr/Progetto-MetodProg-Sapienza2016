package gapp.ulg.game.util;

import gapp.ulg.game.board.*;

import java.util.*;

public class MoveChooserImpl<P> implements PlayerGUI.MoveChooser<P> {
    private Node<List<Action<P>>> root;

    public MoveChooserImpl(GameRuler<P> gR) {
        //todo controllo dmensione validMoves
        //todo null-check gR
        //todo pass e resign
        List<Move<P>> mosse = new ArrayList<>(gR.validMoves());
        List<Action<P>> radice = mosse.get(0).actions;
        int maxI = radice.size();
        for (Move<P> mossa : mosse) {
            List<Action<P>> azioni = mossa.actions;
            maxI = Math.min(azioni.size(), maxI);
            int i = 0;
            for (; i < maxI; ++i) {
                if (!Objects.equals(radice.get(i), azioni.get(i))) {
                    break;
                }
            }
            maxI = i;
        }
        radice = radice.subList(0,maxI);
        root = new Node<>(radice, null);
    }

    public Node<List<Action<P>>> getRoot() {
        return root;
    }

    @Override
    public Optional<Move<P>> subMove() {
        return null;
    }

    @Override
    public List<Move<P>> childrenSubMoves() {
        return null;
    }

    @Override
    public List<Move<P>> select(Pos... pp) {
        return null;
    }

    @Override
    public List<Move<P>> quasiSelected() {
        return null;
    }

    @Override
    public List<P> selectionPieces() {
        return null;
    }

    @Override
    public void clearSelection() {

    }

    @Override
    public Move<P> doSelection(P pm) {
        return null;
    }

    @Override
    public Move<P> jumpSelection(Pos p) {
        return null;
    }

    @Override
    public Move<P> moveSelection(Board.Dir d, int ns) {
        return null;
    }

    @Override
    public Move<P> back() {
        return null;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public void move() {

    }

    @Override
    public boolean mayPass() {
        return false;
    }

    @Override
    public void pass() {

    }

    @Override
    public void resign() {

    }
}

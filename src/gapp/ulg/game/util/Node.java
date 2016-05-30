package gapp.ulg.game.util;

import gapp.ulg.game.board.GameRuler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micfe on 22/05/2016.
 */
public class Node<P> {
    public P value;
    public List<Node<P>> children;

    public Node() {
        this(null);
    }

    public Node(P value) {
        this.value = value;
        children = new ArrayList<>();
    }
}

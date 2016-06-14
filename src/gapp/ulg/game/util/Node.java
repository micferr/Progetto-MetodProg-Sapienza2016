package gapp.ulg.game.util;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    public T value;
    public Node<T> parent;
    public List<Node<T>> children;

    public Node() {
        this(null, null);
    }

    public Node(T value, Node<T> parent) {
        this.value = value;
        this.parent = parent;
        children = new ArrayList<>();
    }
}
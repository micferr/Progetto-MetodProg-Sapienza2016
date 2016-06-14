package gapp.ulg.game.util;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    public T value;
    public List<Node<T>> children;

    public Node() {
        this(null);
    }

    public Node(T value) {
        this.value = value;
        children = new ArrayList<>();
    }
}
package gapp.ulg.play;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.Param;
import gapp.ulg.game.PlayerFactory;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Player;
import gapp.ulg.game.util.ConcreteParameter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Una MCTSPlayerFactory è una fabbrica di {@link MCTSPlayer}.
 * @param <P>  tipo del modello dei pezzi */
public class MCTSPlayerFactory<P> implements PlayerFactory<Player<P>,GameRuler<P>> {
    private List<Param<?>> params;

    public MCTSPlayerFactory() {
        Param<Integer> param1 = new ConcreteParameter<>(
                "Rollouts",
                "Number of rollouts per move",
                new Integer[] {1,10,50,100,200,500,1000},
                50
        );
        Param<String> param2 = new ConcreteParameter<>(
                "Execution",
                "Threaded execution",
                new String[] {"Sequential","Parallel"},
                "Sequential"
        );
        params = Collections.unmodifiableList(Arrays.asList(param1, param2));
    }

    @Override
    public String name() { return "Monte-Carlo Tree Search Player"; }

    @Override
    public void setDir(Path dir) { }

    /** Ritorna una lista con i seguenti due parametri:
     * <pre>
     * Primo parametro
     *     - name: "Rollouts"
     *     - prompt: "Number of rollouts per move"
     *     - values: [1,10,50,100,200,500,1000]
     *     - default: 50
     * Secondo parametro
     *     - name: "Execution"
     *     - prompt: "Threaded execution"
     *     - values: ["Sequential","Parallel"]
     *     - default: "Sequential"
     * </pre>
     * @return la lista con i due parametri */
    @Override
    public List<Param<?>> params() {
        return params;
    }

    @Override
    public Play canPlay(GameFactory<? extends GameRuler<P>> gF) {
        Objects.requireNonNull(gF);
        return Play.YES;
    }

    @Override
    public String tryCompute(GameFactory<? extends GameRuler<P>> gF, boolean parallel,
                             Supplier<Boolean> interrupt) {
        Objects.requireNonNull(gF);
        return null;
    }

    /** Ritorna un {@link MCTSPlayer} che rispetta i parametri impostati
     * {@link MCTSPlayerFactory#params()} e il nome specificato. */
    @Override
    public Player<P> newPlayer(GameFactory<? extends GameRuler<P>> gF, String name) {
        if (canPlay(gF) != Play.YES) throw new IllegalStateException();
        Objects.requireNonNull(name);
        return new MCTSPlayer<>(
                name,
                (Integer)params.get(0).get(),
                "Parallel".equals(params.get(1).get())
        );
    }
}

package gapp.ulg.play;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.Param;
import gapp.ulg.game.PlayerFactory;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Player;
import gapp.ulg.game.util.ConcreteParameter;
import gapp.ulg.game.util.Node;
import gapp.ulg.game.util.Probe;

import static gapp.ulg.game.board.GameRuler.Situation;
import static gapp.ulg.game.board.GameRuler.Next;
import static gapp.ulg.play.BinaryResult.FIRST_PLAYER;
import static gapp.ulg.play.BinaryResult.SECOND_PLAYER;
import static gapp.ulg.play.BinaryResult.TIE;

import java.io.*;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Una OptimalPlayerFactory è una fabbrica di {@link OptimalPlayer}
 *
 * @param <P> tipo del modello dei pezzi
 */
public class OptimalPlayerFactory<P> implements PlayerFactory<Player<P>, GameRuler<P>> {
    /**
     * Una {@code Strategy} rappresenta una strategia ottimale per uno specifico
     * gioco.
     *
     * @param <P> tipo del modello dei pezzi
     */
    interface Strategy<P> {
        /**
         * @return il nome del gioco di cui questa è una strategia ottimale
         */
        String gName();

        /**
         * Ritorna la mossa (ottimale) nella situazione di gioco specificata. Se
         * {@code s} o {@code next} non sono compatibili con il gioco di questa
         * strategia, il comportamento del metodo è indefinito.
         *
         * @param s    una situazione di gioco
         * @param next la funzione delle mosse valide e prossime situazioni del
         *             gioco, cioè quella di {@link GameRuler.Mechanics#next}.
         * @return la mossa (ottimale) nella situazione di gioco specificata
         */
        Move<P> move(Situation<P> s, Next<P> next);
    }

    @Override
    public String name() {
        return "Optimal Player";
    }

    /**
     * Se la directory non è null, in essa salva e recupera file che contengono le
     * strategie ottimali per giochi specifici. Ogni strategia è salvata nella
     * directory in un file il cui nome rispetta il seguente formato:
     * <pre>
     *     strategy_<i>gameName</i>.dat
     * </pre>
     * dove <code><i>gameName</i></code> è il nome del gioco, cioè quello ritornato
     * dal metodo {@link GameRuler#name()}. La directory di default non è impostata
     * e quindi è null.
     */
    @Override
    public void setDir(Path dir) {
        dirPath = dir;
    }

    /**
     * Ritorna una lista con il seguente parametro:
     * <pre>
     *     - name: "Execution"
     *     - prompt: "Threaded execution"
     *     - values: ["Sequential","Parallel"]
     *     - default: "Sequential"
     * </pre>
     *
     * @return la lista con il parametro
     */
    @Override
    public List<Param<?>> params() {
        return params;
    }

    /**
     * Ritorna {@link Play#YES} se conosce già la strategia ottimale per il gioco
     * specificato o perché è in un file (nella directory impostata con
     * {@link OptimalPlayerFactory#setDir(Path)}) o perché è in memoria, altrimenti
     * stima se può essere praticamente possibile imparare la strategia
     * ottimale e allora ritorna {@link Play#TRY_COMPUTE} altrimenti ritorna
     * {@link Play#NO}. Il gioco, cioè il {@link GameRuler}, valutato è quello
     * ottenuto dalla {@link GameFactory} specificata. Se non conosce già la
     * strategia ritorna sempre {@link Play#TRY_COMPUTE} eccetto che per i giochi
     * con i seguenti nomi che sa che è impossibile calcolarla:
     * <pre>
     *     Othello8x8, Othello10x10, Othello12x12
     * </pre>
     * Il controllo sull'esistenza di un file con la strategia è effettuato solamente
     * in base al nome (senza tentare di leggere il file, perché potrebbe richiedere
     * troppo tempo).
     */
    @Override
    public Play canPlay(GameFactory<? extends GameRuler<P>> gF) {
        Objects.requireNonNull(gF);
        try {
            String gameName;
            try {
                gameName = gF.newGame().name();
            } catch (IllegalStateException e) {
                gF.setPlayerNames("a", "b");
                gameName = gF.newGame().name();
            }

            if (gameName.equals("Othello8x8") || gameName.equals("Othello10x10") || gameName.equals("Othello12x12")) {
                return Play.NO;
            }
            if (strategies.containsKey(gameName)) return Play.YES;
            if (dirPath != null) {
                File f = new File(dirPath.toFile(), "strategy_" + gameName + ".dat");
                if (f.exists() && f.isFile()) {
                    return Play.YES;
                }
            }
            return Play.TRY_COMPUTE;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static class ExecutionInterruptedException extends RuntimeException {
    }

    /**
     * Tenta di calcolare la strategia ottimale per il gioco specificato. Ovviamente
     * effettua il calcolo solo se il metodo
     * {@link OptimalPlayerFactory#canPlay(GameFactory)} ritorna {@link Play#TRY_COMPUTE}
     * per lo stesso gioco. Il gioco, cioè il {@link GameRuler}, da valutare è quello
     * ottenuto dalla {@link GameFactory} specificata. Se il calcolo ha successo e
     * una directory ({@link OptimalPlayerFactory#setDir(Path)} ) è impostata, tenta
     * di salvare il file con la strategia calcolata, altrimenti la mantiene in
     * memoria.
     */
    @Override
    public String tryCompute(GameFactory<? extends GameRuler<P>> gF, boolean parallel,
                             Supplier<Boolean> interrupt) {
        switch (canPlay(gF)) {
            case YES:
                return null;
            case NO:
                return "CANNOT PLAY";
            default:
                String res = parallel ?
                        tryComputeParallel(gF, interrupt) :
                        tryComputeSequential(gF, interrupt);
                //String res = tryComputeSequential(gF, interrupt);
                //Save strategy to file
                if (res == null && dirPath != null) {
                    File f = dirPath.toFile();
                    if (f.exists() && f.isDirectory()) {
                        String gameName = gF.newGame().name();
                        OptimalStrategy.writeToFile((OptimalStrategy<P>) strategies.get(gameName), dirPath);
                    }
                }
                return res;
        }
    }

    private String tryComputeSequential(GameFactory<? extends GameRuler<P>> gF, Supplier<Boolean> interrupt) {
        GameRuler<P> game;
        try {
            game = gF.newGame();
        } catch (IllegalStateException e) {
            gF.setPlayerNames("a", "b");
            game = gF.newGame();
        }
        try {
            GameRuler.Mechanics<P> mechanics = game.mechanics();
            Situation<P> start = mechanics.start;
            Map<Probe.EncS<P>, BinaryResult> strategyMap = new ConcurrentHashMap<>();
            checkInterrupt(interrupt);
            buildStrategyMap(start, strategyMap, mechanics, interrupt);
            OptimalStrategy<P> optimalStrategy = new OptimalStrategy<>(game.name(), mechanics);
            optimalStrategy.setChoiceMap(strategyMap);
            strategies.put(game.name(), optimalStrategy);
        } catch (ExecutionInterruptedException e) {
            return "INTERRUPTED";
        } catch (OutOfMemoryError e) {
            return "OUT OF MEMORY";
        } catch (Throwable t) {
            String msg = t.getMessage();
            return "ERROR: " + (msg != null ? msg.toUpperCase() : "");
        }
        return null;
    }

    private void buildStrategyMap(
            Situation<P> start,
            Map<Probe.EncS<P>, BinaryResult> strategyMap,
            GameRuler.Mechanics<P> mechanics,
            Supplier<Boolean> interrupt
    ) {
        checkInterrupt(interrupt);
        Probe.EncS<P> encStart = new Probe.EncS<>(mechanics, start);
        if (strategyMap.containsKey(encStart)) {
            return;
        }
        if (start.turn <= 0) {
            switch (start.turn) {
                case 0:
                    strategyMap.put(encStart, TIE);
                    break;
                case -1:
                    strategyMap.put(encStart, FIRST_PLAYER);
                    break;
                case -2:
                    strategyMap.put(encStart, SECOND_PLAYER);
                    break;
            }
            checkInterrupt(interrupt);
        } else {
            //Get next situations
            Collection<Situation<P>> nextSituations = mechanics.next.get(start).values();
            BinaryResult winResult = start.turn == 1 ? FIRST_PLAYER : SECOND_PLAYER;
            checkInterrupt(interrupt);

            for (Situation<P> s : nextSituations) {
                if (start.turn == -s.turn) {
                    strategyMap.put(encStart, winResult);
                    strategyMap.put(new Probe.EncS<P>(mechanics, s), winResult);
                    return;
                }
            }

            //Compute all next and get for turn player (see docs)
            boolean tieFound = false;
            for (Situation<P> s : nextSituations) {
                buildStrategyMap(s, strategyMap, mechanics, interrupt);
                checkInterrupt(interrupt);
                BinaryResult r = strategyMap.get(new Probe.EncS<>(mechanics, s));
                if (r == winResult) {
                    strategyMap.put(encStart, winResult);
                    return;
                } else if (r == TIE) {
                    tieFound = true;
                }
                checkInterrupt(interrupt);
            }

            //If tie or loss:
            strategyMap.put(encStart, tieFound ? TIE : (start.turn == 1 ? SECOND_PLAYER : FIRST_PLAYER));
        }
        checkInterrupt(interrupt);
    }

    private void checkInterrupt(Supplier<Boolean> interrupt) {
        if (Thread.currentThread().isInterrupted() || (interrupt != null && interrupt.get())) throw new ExecutionInterruptedException();
    }

    private String tryComputeParallel(GameFactory<? extends GameRuler<P>> gF, Supplier<Boolean> interrupt) {
        GameRuler<P> game;
        try {
            game = gF.newGame();
        } catch (IllegalStateException e) {
            gF.setPlayerNames("a", "b");
            game = gF.newGame();
        }
        try {
            GameRuler.Mechanics<P> mechanics = game.mechanics();
            Situation<P> start = mechanics.start;
            Map<Probe.EncS<P>, BinaryResult> strategyMap = new ConcurrentHashMap<>();
            checkInterrupt(interrupt);
            /*ForkJoinPool fjPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
            ForkJoinTask fjMainTask = ForkJoinTask.adapt(
                    () -> */buildStrategyMapForkJoin(
                            start,
                            strategyMap,
                            mechanics,
                            interrupt
                    /*)
            */);
            /*fjPool.invoke(fjMainTask);
            fjMainTask.join();*/

            OptimalStrategy<P> optimalStrategy = new OptimalStrategy<>(game.name(), mechanics);
            optimalStrategy.setChoiceMap(strategyMap);
            strategies.put(game.name(), optimalStrategy);
        } catch (ExecutionInterruptedException e) {
            return "INTERRUPTED";
        } catch (OutOfMemoryError e) {
            return "OUT OF MEMORY";
        } catch (Throwable t) {
            String msg = t.getMessage();
            return "ERROR: " + (msg != null ? msg.toUpperCase() : "");
        }
        return null;
    }

    private void buildStrategyMapForkJoin(
            Situation<P> start, Map<Probe.EncS<P>, BinaryResult> strategyMap,
            GameRuler.Mechanics<P> mechanics,
            Supplier<Boolean> interrupt
    ) {
        if (checkInterruptForkJoin(interrupt)) return;
        Probe.EncS<P> encStart = new Probe.EncS<>(mechanics, start);
        if (strategyMap.containsKey(encStart)) {
            return;
        }
        if (start.turn <= 0) {
            switch (start.turn) {
                case 0:
                    strategyMap.put(encStart, TIE);
                    break;
                case -1:
                    strategyMap.put(encStart, FIRST_PLAYER);
                    break;
                case -2:
                    strategyMap.put(encStart, SECOND_PLAYER);
                    break;
            }
        } else {
            //Get next situations
            Collection<Situation<P>> nextSituations = mechanics.next.get(start).values();
            BinaryResult winResult = start.turn == 1 ? FIRST_PLAYER : SECOND_PLAYER;
            if (checkInterruptForkJoin(interrupt)) return;

            for (Situation<P> s : nextSituations) {
                if (start.turn == -s.turn) {
                    strategyMap.put(encStart, winResult);
                    strategyMap.put(new Probe.EncS<P>(mechanics, s), winResult);
                    return;
                }
            }
            List<ForkJoinTask> tasksToSubmit = new ArrayList<>();
            for (Situation<P> s : nextSituations) {
                tasksToSubmit.add(ForkJoinTask.adapt(() -> {
                    buildStrategyMapForkJoin(s, strategyMap, mechanics, interrupt);
                }));
            }
            boolean tieFound = false;
            int i = 0;
            List<ForkJoinTask> submitTasks = new ArrayList<>(ForkJoinTask.invokeAll(tasksToSubmit));
            try {
                for (Situation<P> s : nextSituations) {
                    submitTasks.get(i++).get();
                    if (checkInterruptForkJoin(interrupt)) throw new InterruptedException();
                    BinaryResult r = strategyMap.get(new Probe.EncS<>(mechanics, s));
                    if (r == winResult) {
                        strategyMap.put(encStart, winResult);
                        throw new InterruptedException();
                    } else if (r == TIE) {
                        tieFound = true;
                    }
                    if (checkInterruptForkJoin(interrupt)) throw new InterruptedException();
                }
            } catch (InterruptedException | ExecutionException e) {
                submitTasks.forEach(t -> t.cancel(true));
                return;
            }

            //If tie or loss:
            strategyMap.put(encStart, tieFound ? TIE : (start.turn == 1 ? SECOND_PLAYER : FIRST_PLAYER));
        }
    }

    private void buildStrategyMapForkJoin2(
            Situation<P> start, Map<Probe.EncS<P>,
            BinaryResult> strategyMap,
            GameRuler.Mechanics<P> mechanics,
            Supplier<Boolean> interrupt
    ) {
        Runtime.getRuntime().exit(-42);
    }

    private boolean checkInterruptForkJoin(Supplier<Boolean> interrupt) {
        return Thread.currentThread().isInterrupted() || (interrupt != null && interrupt.get());
    }

    /**
     * Se il metodo {@link OptimalPlayerFactory#canPlay(GameFactory)} ritorna
     * {@link Play#YES} tenta di creare un {@link OptimalPlayer} con la strategia
     * per il gioco specificato cercandola tra quelle in memoria e se la directory
     * è impostata ({@link OptimalPlayerFactory#setDir(Path)}) anche nel file.
     */
    @Override
    public Player<P> newPlayer(GameFactory<? extends GameRuler<P>> gF, String name) {
        if (canPlay(Objects.requireNonNull(gF)) == Play.YES) {
            GameRuler<P> newGame = gF.newGame();
            String gameName = newGame.name();
            if (!strategies.containsKey(gameName)) {
                OptimalStrategy<P> strategy = OptimalStrategy.loadFromFile(dirPath, gameName, newGame.mechanics());
                strategies.put(gameName, strategy);
            }
            return new OptimalPlayer<>(Objects.requireNonNull(name), strategies.get(gameName));
        } else {
            String playableGames = "";
            for (String s : strategies.keySet()) {
                playableGames += s + " ";
            }
            throw new IllegalStateException("Cannot play game: \""+gF.newGame().name()+"\" - ");
        }
    }

    private Path dirPath = null;
    private List<Param<?>> params = Collections.unmodifiableList(Arrays.asList(
            new ConcreteParameter<>(
                    "Execution",
                    "Threaded Execution",
                    new String[]{"Sequential", "Parallel"},
                    "Sequential"
            )
    ));
    private Map<String, Strategy<P>> strategies = new HashMap<>();
}


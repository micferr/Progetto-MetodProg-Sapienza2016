package gapp.ulg.play;

import gapp.ulg.game.board.*;
import gapp.ulg.games.MNKgame;
import gapp.ulg.games.Othello;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto {@code MCTSPlayer} è un giocatore che gioca seguendo una strategia
 * basata su Monte-Carlo Tree Search e può giocare a un qualsiasi gioco.
 * <br>
 * La strategia che usa è una MCTS (Monte-Carlo Tree Search) piuttosto semplificata.
 * Tale strategia si basa sul concetto di <i>rollout</i> (srotolamento). Un
 * <i>rollout</i> a partire da una situazione di gioco <i>S</i> è l'esecuzione di
 * una partita fino all'esito finale a partire da <i>S</i> facendo compiere ai
 * giocatori mosse random.
 * <br>
 * La strategia adottata da un {@code MCTSPlayer}, è la seguente. In ogni situazione
 * di gioco <i>S</i> in cui deve muovere, prima di tutto ottiene la mappa delle
 * possibili mosse valide da <i>S</i> con le corrispondenti prossime situazioni. Per
 * ogni prossima situazione <i>NS</i> esegue <i>R</i> rollouts e calcola un punteggio
 * di <i>NS</i> dato dalla somma degli esiti dei rollouts. L'esito di un rollout è
 * rappresentato da un intero che è 0 se finisce in una patta, 1 se finisce con la
 * vittoria del giocatore e -1 altrimenti. Infine sceglie la mossa che porta nella
 * prossima situazione con punteggio massimo. Il numero <i>R</i> di rollouts da
 * compiere è calcolato così <i>R = ceil(RPM/M)</i>, cioè la parte intera superiore
 * della divisione decimale del numero di rollout per mossa <i>RPM</i> diviso il
 * numero <i>M</i> di mosse possibili (è sempre esclusa {@link Move.Kind#RESIGN}).
 * @param <P>  tipo del modello dei pezzi */
public class MCTSPlayer<P> implements Player<P> {
    String name;
    int rpm;
    boolean parallel;
    GameRuler<P> game = null;
    long allowedTimePerMove = -1;

    /** Crea un {@code MCTSPlayer} con un limite dato sul numero di rollouts per
     * mossa.
     *
     * @param name  il nome del giocatore
     * @param rpm   limite sul numero di rollouts per mossa, se < 1 è inteso 1
     * @param parallel  se true la ricerca della mossa da fare è eseguita cercando
     *                  di sfruttare il parallelismo della macchina
     * @throws NullPointerException se {@code name} è null */
    public MCTSPlayer(String name, int rpm, boolean parallel) {
        Objects.requireNonNull(name);
        this.name = name;
        this.rpm = Math.max(rpm, 1);
        this.parallel = parallel;
    }

    @Override
    public String name() { return name; }

    @Override
    public void setGame(GameRuler<P> g) {
        game = Objects.requireNonNull(g);
        allowedTimePerMove = game.mechanics().time;
    }

    @Override
    public void moved(int i, Move<P> m) {
        if (game == null || game.result() != -1) throw new IllegalStateException();
        Objects.requireNonNull(m);
        if (game.isPlaying(i) && game.isValid(m)) {
            game.move(m);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private long startMillis; //Time when getMove() is called
    /**
     * Value assigned from getMoveSequential and getMoveParallel
     * (both called from getMove()
     */
    private Move<P> selectedMove;
    @Override
    public Move<P> getMove() { //todo a tempo scaduto mossa random o migliore finora?
        startMillis = System.currentTimeMillis();
        if (game != null && game.result() == -1 && game.players().get(game.turn()-1).equals(name())) {
            Set<Move<P>> validMovesSet = game.validMoves();
            List<Move<P>> validMovesList = new ArrayList<>();
            for (Move<P> m : validMovesSet) {
                if (m.kind != Move.Kind.RESIGN) {
                    validMovesList.add(m);
                }
            }
            Move<P> timeOutMove = validMovesList.get(new Random().nextInt(validMovesList.size()));
            selectedMove = null;
            if (parallel) {
                getMoveParallel();
            } else {
                getMoveSequential();
            }
            return selectedMove != null ? selectedMove : timeOutMove;
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean getMoveParallel() {
        class MaxValue {
            private int i=0;
            void set(int i) {this.i=i;}
            int get() {return i;}
        }
        final MaxValue maxValue = new MaxValue();
        Lock lock = new ReentrantLock();
        Semaphore s = new Semaphore(0);

        class EvaluateMove implements Callable<Void> {
            private GameRuler<P> game;
            private Move<P> move;
            private int playerTurn;
            private int r;

            public EvaluateMove(
                    GameRuler<P> gameCopy,
                    Move<P> move,
                    int playerTurn,
                    int r
            ) {
                this.game = gameCopy;
                this.move = move;
                this.playerTurn = playerTurn;
                this.r = r;
            }

            @Override
            public Void call() {
                int moveValue = 0;
                game.move(move);
                for (int i = 0; i < r; i++) {
                    if (outOfTime()) {
                        s.release();
                        return null;
                    }
                    int movesPlayedToEnd = playTillTheEnd(game);
                    if (game.result() == playerTurn) moveValue++;
                    else if (game.result() != 0) moveValue--;
                    for (int j=0; j<movesPlayedToEnd; j++) game.unMove();
                }
                game.unMove();
                if (outOfTime()) {
                    s.release();
                    return null;
                }
                lock.lock();
                if (moveValue > maxValue.get()) {
                    maxValue.set(moveValue);
                    selectedMove = move;
                }
                lock.unlock();
                s.release();
                return null;
            }
        }

        GameRuler.Situation<P> currentSituation = game.toSituation();
        Map<Move<P>, GameRuler.Situation<P>> next = game.mechanics().next.get(currentSituation);
        int r = ((rpm - 1) / next.size()) + 1;
        maxValue.set(-r);
        int numTasks = next.size();
        ExecutorService executor = Executors.newFixedThreadPool(numTasks);
        try {
            for (Move<P> move : next.keySet()) {
                executor.submit(new EvaluateMove(game.copy(), move, game.turn(), r));
            }
            s.acquire(numTasks);
        } catch (InterruptedException e) {}
        executor.shutdown();

        return true;
    }

    private boolean getMoveSequential() {
        /** gameRuler -> Situation S
         * gameRuler.mechanics + S -> Next's map
         * r = ceil(numRollouts / (moves-resign))
         * Move maxMove = null
         * max = -r      //Loses all rollouts
         * forEach k,v in Next's map
         *      if timeOut return false
         *      moveValue = 0
         *      r times
         *          Play gameRuler till the end and count moves (cm)
         *          if (win) moveValue++ else if (lose) moveValue-- else do nothing
         *          cm times: unMove gameRuler
         *      if (moveValue >= max)          // >= (not >) to give maxMove a value even on Max(moveValue)==-numMoves
         *          max = moveValue
         *          maxMove = k
         * selectedMove = maxMove
         * return True
         */
        GameRuler.Situation<P> currentSituation = game.toSituation();
        Map<Move<P>, GameRuler.Situation<P>> next = game.mechanics().next.get(currentSituation);
        int r = ((rpm - 1) / next.size()) + 1;
        Move<P> maxMove = null;
        int maxScore = -r;
        int currentPlayerTurn = game.turn();
        for (Move<P> move : next.keySet()) {
            int moveValue = 0;
            game.move(move);
            for (int i = 0; i < r; i++) {
                if (outOfTime()) { game.unMove(); return false; }
                int movesPlayedToEnd = playTillTheEnd(game);
                if (game.result() == currentPlayerTurn) moveValue++;
                else if (game.result() != 0) moveValue--;
                for (int j=0; j<movesPlayedToEnd; j++) game.unMove();
            }
            game.unMove();
            if (moveValue >= maxScore) {
                maxMove = move;
                maxScore = moveValue;
                selectedMove = maxMove;
            }
        }
        selectedMove = maxMove;
        return true;
    }

    private boolean outOfTime() {
        return Thread.currentThread().isInterrupted() ||
                (allowedTimePerMove != -1 && System.currentTimeMillis() - startMillis > allowedTimePerMove);
    }

    @SuppressWarnings("unchecked")
    private static int playTillTheEnd(GameRuler gR) {
        int numMoves = 0;
        while (gR.result() == -1) {
            Set<Move> validMoves = gR.validMoves();
            Move m;
            do {
                m = (Move) validMoves.toArray()[new Random().nextInt(validMoves.size())];
            } while (m.kind == Move.Kind.RESIGN);
            gR.move(m);
            numMoves++;
        }
        return numMoves;
    }
}

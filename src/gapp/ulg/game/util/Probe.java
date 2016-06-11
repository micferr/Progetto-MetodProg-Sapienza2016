package gapp.ulg.game.util;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.board.Pos;

import static gapp.ulg.game.board.GameRuler.Situation;
import static gapp.ulg.game.board.GameRuler.Next;
import static gapp.ulg.game.board.GameRuler.Mechanics;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/** <b>IMPLEMENTARE I METODI INDICATI CON "DA_IMPLEMENTARE" SECONDO LE SPECIFICHE
 * DATE NEI JAVADOC. Non modificare le intestazioni dei metodi.</b>
 * <br>
 * Metodi per analizzare giochi */
public class Probe {
    /** Un oggetto {@code EncS} è la codifica compatta di una situazione di gioco
     * {@link GameRuler.Situation}. È utile per mantenere in memoria insiemi con
     * moltissime situazioni minimizzando la memoria richiesta.
     * @param <P>  tipo del modello dei pezzi */
    public static class EncS<P> {
        public byte[] enc;

        /** Crea una codifica compatta della situazione data relativa al gioco la
         * cui meccanica è specificata. La codifica è compatta almeno quanto quella
         * che si ottiene codificando la situazione con un numero e mantenendo in
         * questo oggetto solamente l'array di byte che codificano il numero in
         * binario. Se i parametri di input sono null o non sono tra loro compatibili,
         * il comportamento è indefinito.
         * @param gM  la meccanica di un gioco
         * @param s  una situazione dello stesso gioco */
        public EncS(Mechanics<P> gM, Situation<P> s) {
            BigInteger encoding = new BigInteger("0");
            Map<Pos, P> boardMap = s.newMap();
            int numPieces = gM.pieces.size();
            BigInteger positionalPow = new BigInteger("1");
            for (Pos p : gM.positions) {
                P currentPiece = boardMap.get(p);
                int pieceValue = currentPiece != null ? gM.pieces.indexOf(currentPiece) : numPieces;
                encoding = encoding.add(new BigInteger(String.valueOf(pieceValue)).multiply(positionalPow));
                positionalPow = positionalPow.multiply(new BigInteger(String.valueOf(numPieces+1)));
            }
            /**
             * [0..np-1] : Result - {np} : Tie - [np+1..2np] : Player[i-(np-1)]'s turn
             */
            encoding = encoding.add(new BigInteger(String.valueOf(s.turn + gM.np)).multiply(positionalPow));
            enc = encoding.toByteArray();
        }

        /** Ritorna la situazione codificata da questo oggetto. Se {@code gM} è null
         * o non è la meccanica del gioco della situazione codificata da questo
         * oggetto, il comportamento è indefinito.
         * @param gM  la meccanica del gioco a cui appartiene la situazione
         * @return la situazione codificata da questo oggetto */
        public Situation<P> decode(Mechanics<P> gM) {
            BigInteger encoding = new BigInteger(enc);
            Map<Pos, P> sitMap = new HashMap<>();
            int numPieces = gM.pieces.size();
            BigInteger base = new BigInteger(String.valueOf(numPieces+1));
            for (Pos p : gM.positions) {
                BigInteger[] DivideAndRemainder = encoding.divideAndRemainder(base);
                encoding = DivideAndRemainder[0];
                int pieceIndex = DivideAndRemainder[1].intValueExact();
                if (0 <= pieceIndex && pieceIndex < numPieces) {
                    P piece = gM.pieces.get(pieceIndex);
                    sitMap.put(p, piece);
                }
            }
            int turn = encoding.intValueExact()-gM.np;
            return new Situation<>(sitMap, turn);
        }

        /** Questa oggetto è uguale a {@code x} se e solo se {@code x} è della stessa
         * classe e la situazione codificata è la stessa. Il test è effettuato senza
         * decodificare la situazione, altrimenti sarebbe troppo lento.
         * @param x  un oggetto
         * @return true se {@code x} rappresenta la stessa situazione di questo
         * oggetto */
        @Override
        public boolean equals(Object x) {
            if (x == null || !(x instanceof EncS)) return false;

            return Arrays.equals(this.enc, ((EncS)x).enc);
        }

        /** Ridefinito coerentemente con la ridefinizione di {@link EncS#equals(Object)}.
         * @return l'hash code di questa situazione codificata */
        @Override
        public int hashCode() { return Arrays.hashCode(enc); }
    }

    /** Un oggetto per rappresentare il risultato del metodo
     * {@link Probe#nextSituations(boolean, Next, Function, Function, Set)}.
     * Chiamiamo grado di una situazione <i>s</i> il numero delle prossime situazioni
     * a cui si può arrivare dalla situazione <i>s</i>.
     * @param <S>  tipo della codifica delle situazioni */
    public static class NSResult<S> {
        /** Insieme delle prossime situazioni */
        public final Set<S> next;
        /** Statistiche: il minimo e il massimo grado delle situazioni di partenza
         * e la somma di tutti gradi */
        public final long min, max, sum;

        public NSResult(Set<S> nx, long mn, long mx, long s) {
            next = nx;
            min = mn;
            max = mx;
            sum = s;
        }
    }

    /** Ritorna l'insieme delle prossime situazioni dell'insieme di situazioni date.
     * Per ogni situazione nell'insieme {@code start} ottiene le prossime situazioni
     * tramite {@code nextF}, previa decodifica con {@code dec}, e le aggiunge
     * all'insieme che ritorna, previa codifica con {@code cod}. La computazione può
     * richiedere tempi lunghi per questo è sensibile all'interruzione del thread
     * in cui il metodo è invocato. Se il thread è interrotto, il metodo ritorna
     * immediatamente o quasi, sia che l'esecuzione è parallela o meno, e ritorna
     * null. Se qualche parametro è null o non sono coerenti (ad es. {@code dec} non
     * è il decodificatore del codificatore {@code end}), il comportamento è
     * indefinito.
     * @param parallel  se true il metodo cerca di sfruttare il parallelismo della
     *                  macchina
     * @param nextF  la funzione che ritorna le prossime situazioni di una situazione
     * @param dec  funzione che decodifica una situazione
     * @param enc  funzione che codifica una situazione
     * @param start  insieme delle situazioni di partenza
     * @param <P>  tipo del modello dei pezzi
     * @param <S>  tipo della codifica delle situazioni
     * @return l'insieme delle prossime situazioni dell'insieme di situazioni date o
     * null se l'esecuzione è interrotta. */
    public static <P,S> NSResult<S> nextSituations(boolean parallel, Next<P> nextF,
                                                   Function<S,Situation<P>> dec,
                                                   Function<Situation<P>,S> enc,
                                                   Set<S> start) {
        return parallel ?
                nextSituationsParallel(nextF, dec, enc, start) :
                nextSituationsSequential(nextF, dec, enc, start);
    }

    private static <P,S> NSResult<S> nextSituationsSequential(
            Next<P> nextF,
            Function<S, Situation<P>> dec,
            Function<Situation<P>, S> enc,
            Set<S> start
    ) {
        Set<S> nextEncodedSituations = new HashSet<>();
        long min=0, max=0, sum=0;
        boolean firstProcessed = false;
        for (S sitEnc : start) {
            NSSingle<S> nextSingle = nextSituationsSingle(nextF, dec, enc, sitEnc);
            if (nextSingle == null) return null; // Controlla interruzione
            if (!firstProcessed) {
                firstProcessed = true;
                min = max = sum = nextSingle.size;
            } else {
                long numSituations = nextSingle.size;
                min = numSituations < min ? numSituations : min;
                max = numSituations > max ? numSituations : max;
                sum += numSituations;
            }
            for (S nextSit : nextSingle.next) {
                if (Thread.currentThread().isInterrupted()) return null;
                nextEncodedSituations.add(nextSit);
            }
        }
        return new NSResult<>(nextEncodedSituations, min, max, sum);
    }

    private static <P,S> NSResult<S> nextSituationsParallel(
            Next<P> nextF,
            Function<S, Situation<P>> dec,
            Function<Situation<P>, S> enc,
            Set<S> start
    ) {
        ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<NSSingle<S>>> tasks = new ArrayList<>(start.size());
        try {
            for (S sitEnc : start) {
                tasks.add(exec.submit(() -> nextSituationsSingle(nextF, dec, enc, sitEnc)));
                if (Thread.currentThread().isInterrupted()) return null;
            }
            Set<S> nextEncodedSituations = new HashSet<>();
            long min = -1, max = Long.MAX_VALUE, sum = 0;
            boolean firstProcessed = false;
            for (Future<NSSingle<S>> task : tasks) {
                NSSingle<S> nextSingle = task.get();
                if (Thread.currentThread().isInterrupted()) return null;
                if (!firstProcessed) {
                    firstProcessed = true;
                    min = max = sum = nextSingle.size;
                } else {
                    long numSituations = nextSingle.size;
                    min = numSituations < min ? numSituations : min;
                    max = numSituations > max ? numSituations : max;
                    sum += numSituations;
                }
                for (S nextSit : nextSingle.next) {
                    if (Thread.currentThread().isInterrupted()) return null;
                    nextEncodedSituations.add(nextSit);
                }
            }
            return new NSResult<S>(nextEncodedSituations,min,max,sum);
        } catch (InterruptedException | ExecutionException e) {
            return null;
        } finally {
            exec.shutdownNow();
        }
    }

    private static <P,S> NSSingle<S> nextSituationsSingle(
            Next<P> nextF,
            Function<S, Situation<P>> dec,
            Function<Situation<P>, S> enc,
            S start
    ) {
        Situation<P> decoded = dec.apply(start);
        Set<S> nextEncS = new HashSet<>();
        Collection<Situation<P>> situations = nextF.get(decoded).values();
        for (Situation<P> s : situations) {
            if (Thread.currentThread().isInterrupted()) return null;
            nextEncS.add(enc.apply(s));
        }
        return new NSSingle<S>(nextEncS, situations.size());
    }

    private static class NSSingle<S> {
        /** Insieme delle prossime situazioni */
        public final Set<S> next;
        /** grado della situazione */
        public final long size;

        public NSSingle(Set<S> next, long size) {
            this.next = next;
            this.size = next.size();
        }
    }
}

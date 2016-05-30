package gapp.ulg.games;

import gapp.ulg.game.Param;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.GameFactory;
import gapp.ulg.game.util.ConcreteParameter;

import static gapp.ulg.game.board.PieceModel.Species;

import java.util.*;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Una {@code MNKgameFactory} è una fabbrica di {@link GameRuler} per giocare a
 * (m,n,k)-game. I {@link GameRuler} fabbricati dovrebbero essere oggetti
 * {@link MNKgame}. */
public class MNKgameFactory implements GameFactory<GameRuler<PieceModel<Species>>> {
    private class VariableParameter extends ConcreteParameter<Integer> {
        VariableParameter(String name, String prompt, Integer[] values, Integer defaultValue) {
            super(name, prompt, values, defaultValue);
        }

        void setValues(Integer min, Integer max) {
            values = new ArrayList<>();
            for (Integer i=min; i<=max; ++i)
                values.add(i);
        }

        @Override
        public void set(Object value) {
            if (values.contains(value)) {
                this.value = (Integer)value;
            } else {
                throw new IllegalArgumentException("Invalid value for parameter " + name);
            }
            /*     minM <= M <= maxM
             *     minN <= N <= maxN
             *     minK <= K <= maxK
             *     minK = 1  AND  maxK = max{M,N}  AND  maxN = 20  AND  maxN = 20
             *     N >= K  IMPLICA  minM = 1
             *     N < K   IMPLICA  minM = K
             *     M >= K  IMPLICA  minN = 1
             *     M < K   IMPLICA  minN = K*/
            VariableParameter varM, varN, varK;
            varM = (VariableParameter)paramM;
            varN = (VariableParameter)paramN;
            varK = (VariableParameter)paramK;
            int minM, minN, minK=1, maxM=20, maxN=20, maxK;
            minM = (Integer)varN.get() >= (Integer)varK.get() ? 1 : (Integer)varK.get();
            minN = (Integer)varM.get() >= (Integer)varK.get() ? 1 : (Integer)varK.get();
            maxK = Math.max((Integer)varM.get(), (Integer)varN.get());
            varM.setValues(minM, maxM);
            varN.setValues(minN, maxN);
            varK.setValues(minK, maxK);
        }
    }

    private String p1, p2;
    private Param<String> paramTime;
    private Param<Integer> paramM, paramN, paramK;
    private List<Param<?>> params;

    public MNKgameFactory() {
        paramTime = new ConcreteParameter<>(
                "Time",
                "Time limit for a move",
                new String[] {"No limit","1s","2s","3s","5s","10s","20s","30s","1m","2m","5m"},
                "No limit"
        );
        paramM = new VariableParameter(
                "M",
                "Board width",
                new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
                3
        );
        paramN = new VariableParameter(
                "N",
                "Board height",
                new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},
                3
        );
        paramK = new VariableParameter(
                "K",
                "Length of line",
                new Integer[] {1,2,3},
                3
        );
        params = Collections.unmodifiableList(Arrays.asList(paramTime, paramM, paramN, paramK));
    }

    @Override
    public String name() { return "m,n,k-game"; }

    @Override
    public int minPlayers() { return 2; }

    @Override
    public int maxPlayers() { return 2; }

    /** Ritorna una lista con i seguenti quattro parametri:
     * <pre>
     * Primo parametro, valori di tipo String
     *     - name: "Time"
     *     - prompt: "Time limit for a move"
     *     - values: ["No limit","1s","2s","3s","5s","10s","20s","30s","1m","2m","5m"]
     *     - default: "No limit"
     * Secondo parametro, valori di tipo Integer
     *     - name: "M"
     *     - prompt: "Board width"
     *     - values: [1,2,3,...,20]
     *     - default: 3
     * Terzo parametro, valori di tipo Integer
     *     - name: "N"
     *     - prompt: "Board height"
     *     - values: [1,2,3,...,20]
     *     - default: 3
     * Quarto parametro, valori di tipo Integer
     *     - name: "K"
     *     - prompt: "Length of line"
     *     - values: [1,2,3]
     *     - default: 3
     * </pre>
     * Per i parametri "M","N" e "K" i valori ammissibili possono cambiare a seconda
     * dei valori impostati. Più precisamente occorre che i valori ammissibili
     * garantiscano sempre le seguenti condizioni
     * <pre>
     *     1 <= K <= max{M,N} <= 20   AND   1 <= min{M,N}
     * </pre>
     * dove M,N,K sono i valori impostati. Indicando con minX, maxX il minimo e il
     * massimo valore per il parametro X le condizioni da rispettare sono:
     * <pre>
     *     minM <= M <= maxM
     *     minN <= N <= maxN
     *     minK <= K <= maxK
     *     minK = 1  AND  maxK = max{M,N}  AND  maxN = 20  AND  maxN = 20
     *     N >= K  IMPLICA  minM = 1
     *     N < K   IMPLICA  minM = K
     *     M >= K  IMPLICA  minN = 1
     *     M < K   IMPLICA  minN = K
     * </pre>
     * @return la lista con i quattro parametri */
    @Override
    public List<Param<?>> params() { return params; }

    @Override
    public void setPlayerNames(String... names) {
        for (String name : names) {
            if (name == null) {
                throw new NullPointerException();
            }
        }
        if (names.length == 2) {
            p1 = names[0];
            p2 = names[1];
        } else {
            throw new IllegalArgumentException("There must be exactly 2 players");
        }
    }

    @Override
    public GameRuler<PieceModel<Species>> newGame() {
        if (p1 != null && p2 != null) {
            return new MNKgame(
                    timeParam(),
                    widthParam(),
                    heightParam(),
                    lineLengthParam(),
                    p1,
                    p2
            );
        }  else {
            throw new IllegalStateException("Players' names not set");
        }
    }

    private long timeParam() {
        return parseTimeString((String)params().get(0).get());
    }

    private int widthParam() {
        return (Integer)params().get(1).get();
    }

    private int heightParam() {
        return (Integer)params().get(2).get();
    }

    private int lineLengthParam() {
        return (Integer)params().get(3).get();
    }

    private long parseTimeString(String time) {
        if (time.equals("No limit")) {
            return -1;
        }
        long millis = Long.parseLong(time.substring(0, time.length()-1));
        switch (time.charAt(time.length()-1)) {
            case 'd': millis *= 24;
            case 'h': millis *= 60;
            case 'm': millis *= 60;
            case 's': millis *= 1000; break;
            default: throw new IllegalArgumentException("Invalid time unit in String \"" + time + "\"");
        }
        return millis;
    }
}

package gapp.ulg.games;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.Param;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;
import gapp.ulg.game.util.ConcreteParameter;

import static gapp.ulg.game.board.PieceModel.Species;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Una OthelloFactory è una fabbrica di {@link GameRuler} per giocare a Othello.
 * I {@link GameRuler} fabbricati dovrebbero essere oggetti {@link Othello}. */
public class OthelloFactory implements GameFactory<GameRuler<PieceModel<Species>>> {
    private String p1, p2;
    private List<Param<?>> params;

    /** Crea una fattoria di {@link GameRuler} per giocare a Othello */
    public OthelloFactory() {
        Param<String> param1 = new ConcreteParameter<>(
            "Time",
            "Time limit for a move",
            new String[] {"No limit","1s","2s","3s","5s","10s","20s","30s","1m","2m","5m"},
            "No limit"
        );
        Param<String> param2 = new ConcreteParameter<>(
                "Board",
                "Board size",
                new String[] {"6x6","8x8","10x10","12x12"},
                "8x8"
        );
        params = Collections.unmodifiableList(Arrays.asList(param1, param2));
    }

    @Override
    public String name() { return "Othello"; }

    @Override
    public int minPlayers() { return 2; }

    @Override
    public int maxPlayers() { return 2; }

    /** Ritorna una lista con i seguenti due parametri:
     * <pre>
     * Primo parametro, valori di tipo String
     *     - name: "Time"
     *     - prompt: "Time limit for a move"
     *     - values: ["No limit","1s","2s","3s","5s","10s","20s","30s","1m","2m","5m"]
     *     - default: "No limit"
     * Secondo parametro, valori di tipo String
     *     - name: "Board"
     *     - prompt: "Board size"
     *     - values: ["6x6","8x8","10x10","12x12"]
     *     - default: "8x8"
     * </pre>
     * @return la lista con i due parametri */
    @Override
    @SuppressWarnings("unchecked")
    public List<Param<?>> params() {
        return params;
    }

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
            return new Othello(timeParam(), boardParam(), p1, p2);
        }  else {
            throw new IllegalStateException("Players' names not set");
        }
    }

    private long timeParam() {
        return parseTimeString((String)params().get(0).get());
    }

    private int boardParam() {
        return parseBoardString((String)params().get(1).get());
    }

    private long parseTimeString(String time) {
        if (time.equals("No limit")) {
            return 0;
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

    private int parseBoardString(String board) {
        switch (board) {
            case "6x6": return 6;
            case "8x8": return 8;
            case "10x10": return 10;
            case "12x12": return 12;
            default: throw new IllegalArgumentException("Invalid board size unit:" + board);
        }
    }
}

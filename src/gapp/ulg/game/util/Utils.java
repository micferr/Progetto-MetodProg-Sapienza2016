package gapp.ulg.game.util;

import gapp.ulg.game.board.*;
import gapp.ulg.game.GameFactory;

import static gapp.ulg.game.board.PieceModel.Species;

import java.util.List;
import java.util.function.Function;

/** <b>IMPLEMENTARE I METODI INDICATI CON "DA IMPLEMENTARE" SECONDO LE SPECIFICHE
 * DATE NEI JAVADOC. Non modificare le intestazioni dei metodi.</b>
 * <br>
 * Metodi di utilità */
public class Utils {
    /** Ritorna una view immodificabile della board b. Qualsiasi invocazione di uno
     * dei metodi che tentano di modificare la view ritornata lancia
     * {@link UnsupportedOperationException} e il metodo {@link Board#isModifiable()}
     * ritorna false. Inoltre essendo una view qualsiasi cambiamento della board b è
     * rispecchiato nella view ritornata.
     * @param b  una board
     * @param <P>  tipo del modello dei pezzi
     * @return una view immodificabile della board b
     * @throws NullPointerException se b è null */
    public static <P> Board<P> UnmodifiableBoard(Board<P> b) {
        if (b != null) {
            return new Board<P>() {
                @Override
                public System system() {
                    return b.system();
                }

                @Override
                public int width() {
                    return b.width();
                }

                @Override
                public int height() {
                    return b.height();
                }

                @Override
                public Pos adjacent(Pos p, Dir d) {
                    return b.adjacent(p, d);
                }

                @Override
                public List<Pos> positions() {
                    return b.positions();
                }

                @Override
                public P get(Pos p) {
                    return b.get(p);
                }
            };
        } else {
            throw new NullPointerException("null passed to UnmodifiableBoard");
        }
    }

    /** Imposta i valori dei parametri specificati nella GameFactory gf, i nomi dei
     * giocatori pp poi ottiene il GameRuler dalla gf, passa a ogni giocatore una
     * copia del GameRuler e gioca la partita del GameRuler con i giocatori dati.
     * L'esito della partita sarà registrato nel GameRuler che è ritornato. Gli
     * eventuali parametri di gf non sono impostati.
     * @param gf  una GameFactory
     * @param pp  i giocatori
     * @param <P>  tipo del modello dei pezzi
     * @return il GameRuler usato per fare la partita
     * @throws NullPointerException se gf o uno degli elementi di pp è null
     * @throws IllegalArgumentException se il numero di giocatori in pp non è
     * compatibile con quello richiesto dalla GameFactory gf oppure se il valore di
     * un parametro è errato */
    @SafeVarargs
    public static <P> GameRuler<P> play(GameFactory<? extends GameRuler<P>> gf, Player<P>...pp) {
        if (gf != null && pp != null && !hasNull(pp)) {
            int numPlayers = pp.length;
            if (gf.minPlayers() <= numPlayers && numPlayers <= gf.maxPlayers()) {
                String[] names = new String[pp.length];
                for (int i=0; i<numPlayers; ++i) {
                    names[i] = pp[i].name();
                }
                gf.setPlayerNames(names);
                GameRuler game = gf.newGame();
                for (Player p : pp) {
                    p.setGame(game.copy());
                }
                while (game.result() == -1) {
                    int currentPlayerIndex = game.turn();
                    Move currentMove = pp[currentPlayerIndex-1].getMove();
                    game.move(currentMove); //If false, update games manually
                    for (Player p : pp) {
                        p.moved(currentPlayerIndex, currentMove);
                    }
                }
                return game;
            } else {
                throw new IllegalArgumentException("Invalid number of players.");
            }
        } else {
            //String msg = "GameFactory is null";
            throw new NullPointerException("GameFactory is null.");
        }
    }

    private static <P> boolean hasNull(Player<P>...pp) {
        for (Player<P> p : pp) {
            if (p == null) {
                return true;
            }
        }
        return false;
    }

    /** Ritorna un oggetto funzione che per ogni oggetto di tipo {@link PieceModel}
     * produce una stringa corta che lo rappresenta. Specificatamente la stringa
     * prodotta consiste di due caratteri il primo identifica la specie del pezzo e
     * il secondo il colore. Il primo carattere è determinato come segue per le
     * diverse specie:
     * <table>
     *     <tr><th>Specie</th><th>Carattere</th></tr>
     *     <tr><td>DISC</td><td>T</td></tr>
     *     <tr><td>DAMA</td><td>D</td></tr>
     *     <tr><td>PAWN</td><td>P</td></tr>
     *     <tr><td>KNIGHT</td><td>J</td></tr>
     *     <tr><td>BISHOP</td><td>B</td></tr>
     *     <tr><td>ROOK</td><td>R</td></tr>
     *     <tr><td>QUEEN</td><td>Q</td></tr>
     *     <tr><td>KING</td><td>K</td></tr>
     * </table>
     * Il secondo è il carattere iniziale del nome del colore. L'oggetto ritornato
     * dovrebbe essere sempre lo stesso.
     * @return un oggetto funzione per rappresentare tramite stringhe corte i
     * modelli dei pezzi di tipo {@link PieceModel} */
    public static Function<PieceModel<Species>,String> PieceModelToString() {
        throw new UnsupportedOperationException("OPZIONALE");
    }

    /** Ritorna un oggetto funzione che per ogni oggetto di tipo {@link Board} con
     * tipo del modello dei pezzi {@link PieceModel} produce una stringa rappresenta
     * la board. La stringa prodotta usa la funzione pmToStr per rappresentare i
     * pezzi sulla board.
     * @param pmToStr  funzione per rappresentare i pezzi
     * @return un oggetto funzione per rappresentare le board */
    public static Function<Board<PieceModel<Species>>,String> BoardToString(
            Function<PieceModel<Species>,String> pmToStr) {
        throw new UnsupportedOperationException("OPZIONALE");
    }

    /** Tramite UI testuale permette all'utente di scegliere dei valori per gli
     * eventuali parametri della GameFactory gf, chiede all'utente i nomi per i
     * giocatori che giocano tramite UI che sono np - pp.length, poi imposta tutti
     * gli np nomi nella gf e ottiene da gf il GameRuler. Infine usa il GameRuler
     * per giocare una partita visualizzando sulla UI testuale la board dopo ogni
     * mossa e chiedendo la mossa a ogni giocatore che gioca con la UI.
     * @param gf  una GameFactory
     * @param pToStr  funzione per rappresentare i pezzi
     * @param bToStr  funzione per rappresentare la board
     * @param np  numero totale di giocatori
     * @param pp  i giocatori che non giocano con la UI
     * @param <P>  tipo del modello dei pezzi
     * @return il GameRuler usato per fare la partita
     * @throws NullPointerException se gf, pToStr, bToStr o uno degli elementi di pp
     * è null
     * @throws IllegalArgumentException se np non è compatibile con il numero di
     * giocatori della GameFactory gf o se il numero di giocatori in pp è maggiore
     * di np */
    @SafeVarargs
    public static <P> GameRuler<P> playTextUI(GameFactory<GameRuler<P>> gf,
                                              Function<P,String> pToStr,
                                              Function<Board<P>,String> bToStr,
                                              int np, Player<P>...pp) {
        throw new UnsupportedOperationException("OPZIONALE");
    }
}

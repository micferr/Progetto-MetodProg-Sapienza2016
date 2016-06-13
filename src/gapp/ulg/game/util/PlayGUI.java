package gapp.ulg.game.util;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.PlayerFactory;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.Player;
import gapp.ulg.games.GameFactories;
import gapp.ulg.play.PlayerFactories;

import static gapp.ulg.game.util.PlayerGUI.MoveChooser;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Consumer;

/** Un {@code PlayGUI} è un oggetto che facilita la gestione di partite in una
 * applicazione controllata da GUI. Un {@code PlayGUI} segue lo svolgimento di una
 * partita dalla scelta della {@link GameFactory} e dei {@link PlayerFactory} e di
 * tutte le mosse fino alla fine naturale della partita o alla sua interruzione.
 * Inoltre, un {@code PlayGUI} aiuta sia a mantenere la reattività della GUI che a
 * garantire la thread-safeness usando un thread di confinamento per le invocazioni
 * di tutti i metodi e costruttori degli oggetti coinvolti in una partita.
 * @param <P>  tipo del modello dei pezzi */
public class PlayGUI<P> {
    /** Un {@code Observer} è un oggetto che osserva lo svolgimento di una o più
     * partite. Lo scopo principale è di aggiornare la GUI che visualizza la board
     * ed eventuali altre informazioni a seguito dell'inizio di una nuova partita e
     * di ogni mossa eseguita.
     * @param <P>  tipo del modello dei pezzi */
    public interface Observer<P> {
        /** Comunica allo {@code Observer} il gioco (la partita) che sta iniziando.
         * Può essere nello stato iniziale o in uno stato diverso, ad es. se la
         * partita era stata sospesa ed ora viene ripresa. L'oggetto {@code g} è
         * una copia del {@link GameRuler} ufficiale del gioco. Lo {@code Observer}
         * può usare e modificare {@code g} a piacimento senza che questo abbia
         * effetto sul {@link GameRuler} ufficiale. In particolare lo {@code Observer}
         * può usare {@code g} per mantenersi sincronizzato con lo stato del gioco
         * riportando in {@code g} le mosse dei giocatori, vedi
         * {@link Observer#moved(int, Move)}. L'uso di {@code g} dovrebbe avvenire
         * solamente nel thread in cui il metodo è invocato.
         * <br>
         * <b>Il metodo non blocca, non usa altri thread e ritorna velocemente.</b>
         * @param g  un gioco, cioè una partita
         * @throws NullPointerException se {@code g} è null */
        void setGame(GameRuler<P> g);

        /** Comunica allo {@code Observer} la mossa eseguita da un giocatore. Lo
         * {@code Observer} dovrebbe usare tale informazione per aggiornare la sua
         * copia del {@link GameRuler}. L'uso del GameRuler dovrebbe avvenire
         * solamente nel thread in cui il metodo è invocato.
         * <br>
         * <b>Il metodo non blocca, non usa altri thread e ritorna velocemente.</b>
         * @param i  indice di turnazione di un giocatore
         * @param m  la mossa eseguita dal giocatore
         * @throws IllegalStateException se non c'è un gioco impostato o c'è ma è
         * terminato.
         * @throws NullPointerException se {@code m} è null
         * @throws IllegalArgumentException se {@code i} non è l'indice di turnazione
         * di un giocatore o {@code m} non è una mossa valida nell'attuale situazione
         * di gioco */
        void moved(int i, Move<P> m);

        /** Comunica allo {@code Observer} che il giocatore con indice di turnazione
         * {@code i} ha violato un vincolo sull'esecuzione (ad es. il tempo concesso
         * per una mossa). Dopo questa invocazione il giocatore {@code i} è
         * squalificato e ciò produce gli stessi effetti che si avrebbero se tale
         * giocatore si fosse arreso. Quindi lo {@code Observer} per sincronizzare
         * la sua copia con la partita esegue un {@link Move.Kind#RESIGN} per il
         * giocatore {@code i}. L'uso del GameRuler dovrebbe avvenire solamente nel
         * thread in cui il metodo è invocato.
         * @param i  indice di turnazione di un giocatore
         * @param msg  un messaggio che descrive il tipo di violazione
         * @throws NullPointerException se {@code msg} è null
         * @throws IllegalArgumentException se {@code i} non è l'indice di turnazione
         * di un giocatore */
        void limitBreak(int i, String msg);

        /** Comunica allo {@code Observer} che la partita è stata interrotta. Ad es.
         * è stato invocato il metodo {@link PlayGUI#stop()}.
         * @param msg  una stringa con una descrizione dell'interruzione */
        void interrupted(String msg);
    }

    /** Crea un oggetto {@link PlayGUI} per partite controllate da GUI. L'oggetto
     * {@code PlayGUI} può essere usato per giocare più partite anche con giochi e
     * giocatori diversi. Per garantire che tutti gli oggetti coinvolti
     * {@link GameFactory}, {@link PlayerFactory}, {@link GameRuler} e {@link Player}
     * possano essere usati tranquillamente anche se non sono thread-safe, crea un
     * thread che chiamiamo <i>thread di confinamento</i>, in cui invoca tutti i
     * metodi e costruttori di tali oggetti. Il thread di confinamento può cambiare
     * solo se tutti gli oggetti coinvolti in una partita sono creati ex novo. Se
     * durante una partita un'invocazione (ad es. a {@link Player#getMove()}) blocca
     * il thread di confinamento per un tempo superiore a {@code maxBlockTime}, la
     * partita è interrotta.
     * <br>
     * All'inizio e durante una partita invoca i metodi di {@code obs}, rispettando
     * le specifiche di {@link Observer}, sempre nel thread di confinamento.
     * <br>
     * <b>Tutti i thread usati sono daemon thread</b>
     * @param obs  un osservatore del gioco
     * @param maxBlockTime  tempo massimo in millisecondi di attesa per un blocco
     *                      del thread di confinamento, se < 0, significa nessun
     *                      limite di tempo
     * @throws NullPointerException se {@code obs} è null */
    public PlayGUI(Observer<P> obs, long maxBlockTime) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Imposta la {@link GameFactory} con il nome dato. Usa {@link GameFactories}
     * per creare la GameFactory nel thread di confinamento. Se già c'era una
     * GameFactory impostata, la sostituisce con la nuova e se c'erano anche
     * PlayerFactory impostate le cancella. Però se c'è una partita in corso,
     * fallisce.
     * @param name  nome di una GameFactory
     * @throws NullPointerException se {@code name} è null
     * @throws IllegalArgumentException se {@code name} non è il nome di una
     * GameFactory
     * @throws IllegalStateException se la creazione della GameFactory fallisce o se
     * c'è una partita in corso. */
    public void setGameFactory(String name) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna i nomi dei parametri della {@link GameFactory} impostata. Se la
     * GameFactory non ha parametri, ritorna un array vuoto.
     * @return i nomi dei parametri della GameFactory impostata
     * @throws IllegalStateException se non c'è una GameFactory impostata */
    public String[] getGameFactoryParams() {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna il prompt del parametro con il nome specificato della
     * {@link GameFactory} impostata.
     * @param paramName  nome del parametro
     * @return il prompt del parametro con il nome specificato della GameFactory
     * impostata.
     * @throws NullPointerException se {@code paramName} è null
     * @throws IllegalArgumentException se la GameFactory impostata non ha un
     * parametro di nome {@code paramName}
     * @throws IllegalStateException se non c'è una GameFactory impostata */
    public String getGameFactoryParamPrompt(String paramName) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna i valori ammissibili per il parametro con nome dato della
     * {@link GameFactory} impostata.
     * @param paramName  nome del parametro
     * @return i valori ammissibili per il parametro della GameFactory impostata
     * @throws NullPointerException se {@code paramName} è null
     * @throws IllegalArgumentException se la GameFactory impostata non ha un
     * parametro di nome {@code paramName}
     * @throws IllegalStateException se non c'è una GameFactory impostata */
    public Object[] getGameFactoryParamValues(String paramName) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna il valore del parametro di nome dato della {@link GameFactory}
     * impostata.
     * @param paramName  nome del parametro
     * @return il valore del parametro della GameFactory impostata
     * @throws NullPointerException se {@code paramName} è null
     * @throws IllegalArgumentException se la GameFactory impostata non ha un
     * parametro di nome {@code paramName}
     * @throws IllegalStateException se non c'è una GameFactory impostata */
    public Object getGameFactoryParamValue(String paramName) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Imposta il valore del parametro di nome dato della {@link GameFactory}
     * impostata.
     * @param paramName  nome del parametro
     * @param value  un valore ammissibile per il parametro
     * @throws NullPointerException se {@code paramName} o {@code value} è null
     * @throws IllegalArgumentException se la GameFactory impostata non ha un
     * parametro di nome {@code paramName} o {@code value} non è un valore
     * ammissibile per il parametro
     * @throws IllegalStateException se non c'è una GameFactory impostata o è già
     * stato impostata la PlayerFactory di un giocatore */
    public void setGameFactoryParamValue(String paramName, Object value) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }


    /** Imposta un {@link PlayerGUI} con il nome e il master dati per il giocatore
     * di indice {@code pIndex}. Se c'era già un giocatore impostato per quell'indice,
     * lo sostituisce.
     * @param pIndex  indice di un giocatore
     * @param pName  nome del giocatore
     * @param master  il master
     * @throws NullPointerException se {@code pName} o {@code master} è null
     * @throws IllegalArgumentException se {@code pIndex} non è un indice di giocatore
     * valido per la GameFactory impostata
     * @throws IllegalStateException se non c'è una GameFactory impostata o se c'è
     * una partita in corso. */
    public void setPlayerGUI(int pIndex, String pName, Consumer<MoveChooser<P>> master) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }


    /** Imposta la {@link PlayerFactory} con nome dato per il giocatore di indice
     * {@code pIndex}. Usa {@link PlayerFactories} per creare la PlayerFactory nel
     * thread di confinamento. La PlayerFactory è impostata solamente se il metodo
     * ritorna {@link PlayerFactory.Play#YES}. Se c'era già un giocatore impostato
     * per quell'indice, lo sostituisce.
     * @param pIndex  indice di un giocatore
     * @param fName  nome di una PlayerFactory
     * @param pName  nome del giocatore
     * @param dir  la directory della PlayerFactory o null
     * @return un valore (vedi {@link PlayerFactory.Play}) che informa sulle
     * capacità dei giocatori di questa fabbrica di giocare al gioco specificato.
     * @throws NullPointerException se {@code fName} o {@code pName} è null
     * @throws IllegalArgumentException se {@code pIndex} non è un indice di giocatore
     * valido per la GameFactory impostata o se non esiste una PlayerFactory di nome
     * {@code fName}
     * @throws IllegalStateException se la creazione della PlayerFactory fallisce o
     * se non c'è una GameFactory impostata o se c'è una partita in corso. */
    public PlayerFactory.Play setPlayerFactory(int pIndex, String fName, String pName, Path dir) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna i nomi dei parametri della {@link PlayerFactory} di indice
     * {@code pIndex}. Se la PlayerFactory non ha parametri, ritorna un array vuoto.
     * @param pIndex  indice di un giocatore
     * @return i nomi dei parametri della PlayerFactory di indice dato
     * @throws IllegalArgumentException se non c'è una PlayerFactory di indice
     * {@code pIndex} */
    public String[] getPlayerFactoryParams(int pIndex) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna il prompt del parametro con il nome specificato della
     * {@link PlayerFactory} di indice {@code pIndex}.
     * @param pIndex  indice di un giocatore
     * @param paramName  nome del parametro
     * @return il prompt del parametro con il nome specificato della PlayerFactory
     * di indice dato
     * @throws NullPointerException se {@code paramName} è null
     * @throws IllegalArgumentException se la PlayerFactory non ha un parametro di
     * nome {@code paramName} o non c'è una PlayerFactory di indice {@code pIndex} */
    public String getPlayerFactoryParamPrompt(int pIndex, String paramName) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna i valori ammissibili per il parametro di nome dato della
     * {@link PlayerFactory} di indice {@code pIndex}.
     * @param pIndex  indice di un giocatore
     * @param paramName  nome del parametro
     * @return i valori ammissibili per il parametro di nome dato della PlayerFactory
     * di indice dato.
     * @throws NullPointerException se {@code paramName} è null
     * @throws IllegalArgumentException se la PlayerFactory non ha un parametro di
     * nome {@code paramName} o non c'è una PlayerFactory di indice {@code pIndex} */
    public Object[] getPlayerFactoryParamValues(int pIndex, String paramName) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Ritorna il valore del parametro di nome dato della {@link PlayerFactory} di
     * indice {@code pIndex}.
     * @param pIndex  indice di un giocatore
     * @param paramName  nome del parametro
     * @return il valore del parametro di nome dato della PlayerFactory di indice
     * dato
     * @throws NullPointerException se {@code paramName} è null
     * @throws IllegalArgumentException se la PlayerFactory non ha un parametro di
     * nome {@code paramName} o non c'è una PlayerFactory di indice {@code pIndex} */
    public Object getPlayerFactoryParamValue(int pIndex, String paramName) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Imposta il valore del parametro di nome dato della {@link PlayerFactory}
     * di indice {@code pIndex}.
     * @param pIndex  indice di un giocatore
     * @param paramName  nome del parametro
     * @param value  un valore ammissibile per il parametro
     * @throws NullPointerException se {@code paramName} o {@code value} è null
     * @throws IllegalArgumentException se la PlayerFactory non ha un parametro di
     * nome {@code paramName} o {@code value} non è un valore ammissibile per il
     * parametro o non c'è una PlayerFactory di indice {@code pIndex}
     * @throws IllegalStateException se c'è una partita in corso */
    public void setPlayerFactoryParamValue(int pIndex, String paramName, Object value) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }


    /** Inizia una partita con un gioco fabbricato dalla GameFactory impostata e i
     * giocatori forniti da {@link PlayerGUI} impostati o fabbricati dalle
     * PlayerFactory impostate. Se non c'è una GameFactory impostata o non ci sono
     * sufficienti giocatori impostati o c'è già una partita in corso, fallisce. Se
     * sono impostati dei vincoli sui thread per le invocazioni di
     * {@link Player#getMove}, allora prima di iniziare la partita invoca i metodi
     * {@link Player#threads(int, ForkJoinPool, ExecutorService)} di tutti i giocatori,
     * ovviamente nel thread di confinamento.
     * <br>
     * Il metodo ritorna immediatamente, non attende che la partita termini. Quindi
     * usa un thread per gestire la partita oltre al thread di confinamento usato
     * per l'invocazione di tutti i metodi del GameRuler e dei Player.
     * @param tol  massimo numero di millisecondi di tolleranza per le mosse, cioè se
     *             il gioco ha un tempo limite <i>T</i> per le mosse, allora il tempo di
     *             attesa sarà <i>T</i> + {@code tol}; se {@code tol} <= 0, allora
     *             nessuna tolleranza
     * @param timeout  massimo numero di millisecondi per le invocazioni dei metodi
     *                 dei giocatori escluso {@link Player#getMove()}, se <= 0,
     *                 allora nessun limite
     * @param minTime  minimo numero di millisecondi tra una mossa e quella successiva,
     *                 se <= 0, allora nessuna pausa
     * @param maxTh  massimo numero di thread addizionali permessi per
     *               {@link Player#getMove()}, se < 0, nessun limite è imposto
     * @param fjpSize  numero di thread per il {@link ForkJoinTask ForkJoin} pool,
     *                 se == 0, non è permesso alcun pool, se invece è < 0, non c'è
     *                 alcun vincolo e possono usare anche
     *                 {@link ForkJoinPool#commonPool() Common Pool}
     * @param bgExecSize  numero di thread permessi per esecuzioni in background, se
     *                    == 0, non sono permessi, se invece è < 0, non c'è alcun
     *                    vincolo
     * @throws IllegalStateException se non c'è una GameFactory impostata o non ci
     * sono sufficienti PlayerFactory impostate o la creazione del GameRuler o quella
     * di qualche giocatore fallisce o se già c'è una partita in corso. */
    public void play(long tol, long timeout, long minTime, int maxTh, int fjpSize, int bgExecSize) {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }

    /** Se c'è una partita in corso la termina immediatamente e ritorna true,
     * altrimenti non fa nulla e ritorna false.
     * @return true se termina la partita in corso, false altrimenti */
    public boolean stop() {
        throw new UnsupportedOperationException("PROGETTO: DA IMPLEMENTARE");
    }
}

package gapp.ulg.games;

import gapp.ulg.game.board.*;
import gapp.ulg.game.util.BoardOct;
import gapp.ulg.game.util.Utils;

import java.util.*;

import static gapp.ulg.game.board.PieceModel.Species;

/**
 * <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto {@code MNKgame} rappresenta un GameRuler per fare una partita a un
 * (m,n,k)-game, generalizzazioni del ben conosciuto Tris o Tic Tac Toe.
 * <br>
 * Un gioco (m,n,k)-game si gioca su una board di tipo {@link Board.System#OCTAGONAL}
 * di larghezza (width) m e altezza (height) n. Si gioca con pezzi o pedine di specie
 * {@link Species#DISC} di due colori "nero" e "bianco". All'inizio la board è vuota.
 * Poi a turno ogni giocatore pone una sua pedina in una posizione vuota. Vince il
 * primo giocatore che riesce a disporre almeno k delle sue pedine in una linea di
 * posizioni consecutive orizzontale, verticale o diagonale. Chiaramente non è
 * possibile passare il turno e una partita può finire con una patta.
 * <br>
 * Per ulteriori informazioni si può consultare
 * <a href="https://en.wikipedia.org/wiki/M,n,k-game">(m,n,k)-game</a>
 */
public class MNKgame implements GameRuler<PieceModel<Species>> {
    private static final PieceModel<Species> whitePiece = new PieceModel<>(Species.DISC, "bianco");
    private static final PieceModel<Species> blackPiece = new PieceModel<>(Species.DISC, "nero");

    private List<Pos> history;

    private long allowedTimePerMove; // Tempo permesso per mossa
    private int boardWidth, boardHeight; // Dimensioni board
    private int lineLengthToWin; //Lunghezza della linea per vincere

    private List<String> players;
    public int currentPlayerIndex; // { 0, 1 }
    private Set<Move<PieceModel<PieceModel.Species>>> currentPlayerValidMoves;
    private Board<PieceModel<Species>> board;
    private Board<PieceModel<Species>> boardView;
    private boolean gameEnded;
    private boolean invalidMovePlayed;
    private int result; //Valore ritornato da result()

    private Mechanics<PieceModel<Species>> mechanics;

    /**
     * Crea un {@code MNKgame} con le impostazioni date.
     *
     * @param time tempo in millisecondi per fare una mossa, se <= 0 significa nessun
     *             limite
     * @param m    larghezza (width) della board
     * @param n    altezza (height) della board
     * @param k    lunghezza della linea
     * @param p1   il nome del primo giocatore
     * @param p2   il nome del secondo giocatore
     * @throws NullPointerException     se {@code p1} o {@code p2} è null
     * @throws IllegalArgumentException se i valori di {@code m,n,k} non soddisfano
     *                                  le condizioni 1 <= {@code k} <= max{{@code M,N}} <= 20 e 1 <= min{{@code M,N}}
     */
    public MNKgame(long time, int m, int n, int k, String p1, String p2) {
        if (p1 != null && p2 != null) {
            if (1 <= k && k <= Math.max(m, n) && Math.max(m, n) <= 20 && 1 <= Math.min(m, n)) {
                allowedTimePerMove = time;
                boardWidth = m;
                boardHeight = n;
                lineLengthToWin = k;
                players = Collections.unmodifiableList(Arrays.asList(p1, p2));
                board = new BoardOct<>(boardWidth, boardHeight);
                boardView = Utils.UnmodifiableBoard(board);
                currentPlayerIndex = 0;
                currentPlayerValidMoves = currentPlayerValidMoves();
                gameEnded = false;
                invalidMovePlayed = false;
                result = -1;
                history = new ArrayList<>();
                mechanics = makeMechanics();
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Il nome rispetta il formato:
     * <pre>
     *     <i>M,N,K</i>-game
     * </pre>
     * dove <code><i>M,N,K</i></code> sono i valori dei parametri M,N,K, ad es.
     * "4,5,4-game".
     */
    @Override
    public String name() {
        return boardWidth + "," + boardHeight + "," + lineLengthToWin + "-game";
    }

    @Override
    public <T> T getParam(String name, Class<T> c) {
        // c.cast(Object) lancia ClassCastException se il cast fallisce
        if (name != null && c != null) {
            switch (name) {
                case "Time":
                    return c.cast(timeToParamString());
                case "M":
                    return c.cast(boardWidth);
                case "N":
                    return c.cast(boardHeight);
                case "K":
                    return c.cast(lineLengthToWin);
                default:
                    throw new IllegalArgumentException("Invalid parameter name");
            }
        } else throw new NullPointerException("null value passed to getParam");
    }

    private String timeToParamString() {
        if (allowedTimePerMove <= 0) return "No limit";
        if (allowedTimePerMove == 1000) return "1s";
        if (allowedTimePerMove == 2000) return "2s";
        if (allowedTimePerMove == 3000) return "3s";
        if (allowedTimePerMove == 5000) return "5s";
        if (allowedTimePerMove == 10_000) return "10s";
        if (allowedTimePerMove == 20_000) return "20s";
        if (allowedTimePerMove == 30_000) return "30s";
        if (allowedTimePerMove == 1_000 * 60) return "1m";
        if (allowedTimePerMove == 1_000 * 60 * 2) return "2m";
        if (allowedTimePerMove == 1_000 * 60 * 5) return "5m";
        else throw new IllegalStateException("Invalid value for Time parameter - millis: " + allowedTimePerMove);
    }

    @Override
    public List<String> players() {
        return players;
    }

    /**
     * @return il colore "nero" per il primo giocatore e "bianco" per il secondo
     */
    @Override
    public String color(String name) {
        if (name != null) {
            if (name.equals(players().get(0))) {
                return "nero";
            } else if (name.equals(players().get(1))) {
                return "bianco";
            } else {
                throw new IllegalArgumentException(name + " is not a player in this game");
            }
        } else {
            throw new NullPointerException("name in Othello#color(String name) is null");
        }
    }

    @Override
    public Board<PieceModel<Species>> getBoard() {
        return boardView;
    }

    @Override
    public int turn() {
        return gameEnded ? 0 : currentPlayerIndex + 1;
    }

    /**
     * Se la mossa non è valida termina il gioco dando la vittoria all'altro
     * giocatore.
     * Se dopo la mossa la situazione è tale che nessuno dei due giocatori può
     * vincere, si tratta quindi di una situazione che può portare solamente a una
     * patta, termina immediatamente il gioco con una patta. Per determinare se si
     * trova in una tale situazione controlla che nessun dei due giocatori può
     * produrre una linea di K pedine con le mosse rimanenti (in qualsiasi modo siano
     * disposte le pedine rimanenti di entrambi i giocatori).
     */
    @Override
    public boolean move(Move<PieceModel<Species>> m) {
        if (m != null) {
            if (!gameEnded) {
                if (validMoves().contains(m) && m.kind != Move.Kind.RESIGN) {
                    PieceModel<PieceModel.Species> currentPlayerPiece =
                            currentPlayerIndex == 0 ? blackPiece : whitePiece;
                    Pos pos = m.actions.get(0).pos.get(0);
                    board.put(currentPlayerPiece, pos);
                    history.add(pos);
                    if (currentPlayerWins()) {
                        gameEnded = true;
                        result = currentPlayerIndex + 1;
                    } else if (isForcedTie()) {
                        /*System.err.println("Pareggio!");
                        System.err.println("Forced tie: " + isForcedTie());
                        System.err.println("CantWin(0): " + CantWin(0));
                        System.err.println("CantWin(1): " + CantWin(1));
                        System.err.println("\n\n");*/
                        gameEnded = true;
                        result = 0;
                    } else {
                        //printBoard();
                        currentPlayerIndex = 1 - currentPlayerIndex;
                        currentPlayerValidMoves = currentPlayerValidMoves();
                        return true;
                    }
                    //printBoard();
                    currentPlayerIndex = 1 - currentPlayerIndex;
                    return true;
                } else {
                    gameEnded = true;
                    invalidMovePlayed = true;
                    currentPlayerIndex = 1 - currentPlayerIndex;
                    return m.kind.equals(Move.Kind.RESIGN); //Resign and invalid moves both cause the game to end, but Resign is valid
                }
            } else {
                throw new IllegalStateException("Cannot play a move in a finished game");
            }
        } else {
            throw new NullPointerException("Cannot execute a null move");
        }
    }

    public boolean currentPlayerWins() {
        PieceModel<PieceModel.Species> currentPlayerPiece =
                currentPlayerIndex == 0 ? blackPiece : whitePiece;
        Set<Pos> ownedByCurrentPlayer = board.get(currentPlayerPiece);
        for (Pos p : ownedByCurrentPlayer) {
            if (isPartOfCompleteLine(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPartOfCompleteLine(Pos p) {
        PieceModel<PieceModel.Species> piece = board.get(p);
        for (Board.Dir dir : Board.Dir.values()) {
            int consecutives = 0;
            Pos new_p = p;
            do {
                consecutives++;
                new_p = board.adjacent(new_p, dir);
            } while (new_p != null && piece.equals(board.get(new_p)));
            if (consecutives >= lineLengthToWin) {
                return true;
            }
        }
        return false;
    }

    public boolean isForcedTie() {
        /*if (boardWidth == 3 && boardHeight == 3 && lineLengthToWin == 3 && board.get().size()==5) {
            Pos p1=null, p2=null, p3=null, p4 = null;
            for (Pos p : board.positions()) {if (board.get(p)==null) if (p1==null) p1 = p; else if (p2 == null) p2 = p; else if (p3==null) p3 = p; else p4 = p;}
            // 1
            board.put(blackPiece, p1);
            board.put(blackPiece, p2);
            board.put(whitePiece, p3);
            board.put(whitePiece, p4);
            boolean tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3); board.remove(p4);
            if (!tie) return false;
            // 2
            board.put(blackPiece, p1);
            board.put(whitePiece, p2);
            board.put(blackPiece, p3);
            board.put(whitePiece, p4);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3); board.remove(p4);
            if (!tie) return false;
            // 3
            board.put(whitePiece, p1);
            board.put(blackPiece, p2);
            board.put(blackPiece, p3);
            board.put(whitePiece, p4);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3); board.remove(p4);
            if (!tie) return false;
            // 4
            board.put(blackPiece, p1);
            board.put(whitePiece, p2);
            board.put(whitePiece, p3);
            board.put(blackPiece, p4);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3); board.remove(p4);
            if (!tie) return false;
            // 5
            board.put(whitePiece, p1);
            board.put(blackPiece, p2);
            board.put(whitePiece, p3);
            board.put(blackPiece, p4);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3); board.remove(p4);
            if (!tie) return false;
            // 6
            board.put(whitePiece, p1);
            board.put(whitePiece, p2);
            board.put(blackPiece, p3);
            board.put(blackPiece, p4);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3); board.remove(p4);
            if (!tie) return false;
            else return true;
        }
        if (boardWidth == 3 && boardHeight == 3 && lineLengthToWin == 3 && board.get().size()==6) {
            Pos p1=null, p2=null, p3=null;
            for (Pos p : board.positions()) {if (board.get(p)==null) if (p1==null) p1 = p; else if (p2 == null) p2 = p; else p3 = p;}
            // 1
            board.put(blackPiece, p1);
            board.put(blackPiece, p2);
            board.put(whitePiece, p3);
            boolean tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3);
            if (!tie) return false;
            // 2
            board.put(blackPiece, p1);
            board.put(whitePiece, p2);
            board.put(blackPiece, p3);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3);
            if (!tie) return false;
            // 3
            board.put(whitePiece, p1);
            board.put(blackPiece, p2);
            board.put(blackPiece, p3);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2); board.remove(p3);
            return tie;
        }
        if (boardWidth == 3 && boardHeight == 3 && lineLengthToWin == 3 && board.get().size()==7) {
            Pos p1=null, p2=null;
            for (Pos p : board.positions()) {if (board.get(p)==null) if (p1==null) p1 = p; else p2 = p;}
            board.put(blackPiece, p1);
            board.put(whitePiece, p2);
            boolean tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2);
            if (!tie) return false;
            board.put(blackPiece, p2);
            board.put(whitePiece, p1);
            tie = true;
            for (Pos p : board.positions()) if (isPartOfCompleteLine(p)) tie = false;
            board.remove(p1); board.remove(p2);
            return tie;
        }*/
        return CantWin(0) && CantWin(1);
    }

    /**
     * Is, for the next player, a non-win
     * If it is for both players, the game will end in a tie.
     *
     * @param nextPlayerIndex The player who plays next, 0 or 1
     * @return Whether next player is unable to win
     */
    private boolean CantWin(int nextPlayerIndex) {
        //Cond ? floor(num of empty cells) : ceil(num of empty cells)
        int emptyPieces = boardWidth * boardHeight - board.get().size();
        int availablePieces =
                currentPlayerIndex == nextPlayerIndex ?
                        (emptyPieces) / 2 :
                        (emptyPieces + 2 - 1) / 2;
        //Piece to put
        PieceModel<PieceModel.Species> nextPiece =
                nextPlayerIndex == 0 ? blackPiece : whitePiece;
        //All the pos not belonging to the opponent (either of next player or null)
        Set<Pos> notTakenByOpponent = new HashSet<>();
        board.positions().forEach(pos -> {
            if (board.get(pos) == null || board.get(pos).equals(nextPiece)) {
                notTakenByOpponent.add(pos);
            }
        });

        //Check
        for (Pos p : notTakenByOpponent) {
            for (Board.Dir dir : Board.Dir.values()) {
                Pos new_p = p;
                int rowLength = 0;
                int piecesStillAvailable = availablePieces;

                while (notTakenByOpponent.contains(new_p)) {
                    if (board.get(new_p) == null) piecesStillAvailable--;
                    if (piecesStillAvailable < 0) break;
                    rowLength++;
                    new_p = board.adjacent(new_p, dir);
                }

                if (rowLength >= lineLengthToWin) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isForcedTie2() {
        if (boardWidth != 3 || boardHeight != 3 || lineLengthToWin != 3) return isForcedTie();

        class Checker {
            int maxEmpty;
            PieceModel<Species> p;
            boolean check(Pos... pp) { //True if line is available
                int e=0;
                for (Pos pos : pp) {
                    if (board.get(pos) == null) e++;
                    else if (!p.equals(board.get(pos))) return false;
                }
                return e<=maxEmpty;
            }
            boolean isTie() {
                Pos p00 = new Pos(0,0), p01 = new Pos(0,1), p02 = new Pos(0,2), p10 = new Pos(1,0), p11 = new Pos(1,1),
                        p12 = new Pos(1,2), p20 = new Pos(2,0), p21 = new Pos(2,1), p22 = new Pos(2,2);
                return !(
                        check(p00, p01, p02) || check(p10, p11, p12) || check(p20, p21, p22) || check(p00, p10, p20) ||
                                check(p01, p11, p21) || check(p02, p12, p22) || check(p00,p11,p22) || check(p02,p11,p20)
                        );
            }
        }

        int empty = boardWidth*boardHeight-board.get().size();
        //Check first player
        int availableBlackPosition = currentPlayerIndex == 0 ? (int)Math.floor(empty/2.0) : (int)Math.ceil(empty/2.0);
        Checker c = new Checker(); c.maxEmpty=availableBlackPosition; c.p = blackPiece;
        boolean tieB = c.isTie();
        if (!tieB) return false;
        int availableWhitePosition = currentPlayerIndex == 1 ? (int)Math.floor(empty/2.0) : (int)Math.ceil(empty/2.0);
        c.maxEmpty = availableWhitePosition; c.p = whitePiece;
        return c.isTie();
    }

    public void printBoard() {
        for (int y = boardHeight - 1; y >= 0; y--) {
            for (int x = 0; x < boardWidth; x++) {
                Pos p = new Pos(x, y);
                PieceModel<PieceModel.Species> piece = board.get(p);
                if (piece == null) System.err.print(". ");
                else if (piece.equals(blackPiece)) System.err.print("X ");
                else System.err.print("O ");
            }
            System.err.println();
        }
        currentPlayerWins();
        //System.err.println("Continuabile: " + !isForcedTie());
        System.err.println("CantWin(0): " + CantWin(0));
        System.err.println("CantWin(1): " + CantWin(1));
        System.err.println("Result: " + result());
        System.err.println("GameEnded: " + gameEnded);
        System.err.println("Turn(): " + turn());
    }

    @Override
    public boolean unMove() {
        if (history.size() > 0) {
            board.remove(history.remove(history.size() - 1));
            currentPlayerIndex = 1 - currentPlayerIndex;
            gameEnded = false;
            invalidMovePlayed = false;
            currentPlayerValidMoves = currentPlayerValidMoves();
            result = -1;
            return true;
        } else return false;
    }

    @Override
    public boolean isPlaying(int i) {
        if (i == 1 || i == 2) {
            return !gameEnded;
        } else {
            throw new IllegalArgumentException("1 and 2 are the only valid player indices");
        }
    }

    @Override
    public int result() {
        if (gameEnded) {
            if (!invalidMovePlayed) {
                return result;
            } else {
                return currentPlayerIndex + 1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Ogni mossa (diversa dall'abbandono) è rappresentata da una sola {@link Action}
     * di tipo {@link Action.Kind#ADD}.
     */
    @Override
    public Set<Move<PieceModel<Species>>> validMoves() {
        if (result() == -1) {
            return currentPlayerValidMoves;
        } else {
            throw new IllegalStateException("A finished game has no valid moves");
        }
    }

    private Set<Move<PieceModel<Species>>> currentPlayerValidMoves() {
        Set<Move<PieceModel<Species>>> moves = new HashSet<>();
        moves.add(new Move<>(Move.Kind.RESIGN));
        PieceModel<PieceModel.Species> playerPiece = currentPlayerIndex == 0 ? blackPiece : whitePiece;
        for (Pos p : board.positions()) {
            if (board.get(p) == null) {
                moves.add(new Move<>(new Action<PieceModel<Species>>(p, playerPiece)));
            }
        }
        return Collections.unmodifiableSet(moves);
    }

    @Override
    public GameRuler<PieceModel<Species>> copy() {
        MNKgame newGame = new MNKgame(
                this.allowedTimePerMove,
                this.boardWidth,
                this.boardHeight,
                this.lineLengthToWin,
                this.players().get(0),
                this.players().get(1)
        );
        newGame.history.addAll(this.history);
        newGame.currentPlayerIndex = this.currentPlayerIndex;
        newGame.currentPlayerValidMoves = this.currentPlayerValidMoves;
        for (Pos p : this.board.get()) {
            newGame.board.put(this.board.get(p), p);
        }
        newGame.gameEnded = this.gameEnded;
        newGame.invalidMovePlayed = this.invalidMovePlayed;
        newGame.result = this.result;
        return newGame;
    }

    @Override
    public Mechanics<PieceModel<Species>> mechanics() {
        return mechanics;
    }

    private Mechanics<PieceModel<Species>> makeMechanics() {
        return new Mechanics<>(
                this.allowedTimePerMove,
                Collections.unmodifiableList(Arrays.asList(blackPiece, whitePiece)),
                this.board.positions(),
                2,
                new Situation<>(new HashMap<>(), 1),
                getNext()
        );
    }

    private Next<PieceModel<PieceModel.Species>> getNext() {
        return situation -> {
            Objects.requireNonNull(situation);
            MNKgame game = new MNKgame(
                    this.allowedTimePerMove,
                    this.boardWidth,
                    this.boardHeight,
                    this.lineLengthToWin,
                    players.get(0),
                    players.get(1)
            );
            situation.newMap().forEach((pos, piece) -> {
                game.board.put(piece, pos);
            });
            game.gameEnded = situation.turn <= 0;
            game.currentPlayerIndex = situation.turn - 1;
            if (game.gameEnded /*|| game.currentPlayerWins()*/) {
                return Collections.EMPTY_MAP;
            }
            game.currentPlayerValidMoves = game.currentPlayerValidMoves();
            Set<Move<PieceModel<Species>>> validMoves = game.validMoves();
            Map<Move<PieceModel<PieceModel.Species>>, Situation<PieceModel<PieceModel.Species>>> nextSituations =
                    new HashMap<>();
            validMoves.forEach((move) -> {
                if (move.kind != Move.Kind.RESIGN) {
                    game.move(move);
                    nextSituations.put(move, game.toSituation());
                    game.unMove();
                }
            });
            return nextSituations;
        };
    }
}

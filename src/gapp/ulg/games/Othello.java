package gapp.ulg.games;

import gapp.ulg.game.board.*;
import gapp.ulg.game.util.BoardOct;
import gapp.ulg.game.util.Utils;

import static gapp.ulg.game.board.PieceModel.Species;

import java.util.*;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto Othello rappresenta un GameRuler per fare una partita a Othello. Il
 * gioco Othello si gioca su una board di tipo {@link Board.System#OCTAGONAL} 8x8.
 * Si gioca con pezzi o pedine di specie {@link Species#DISC} di due
 * colori "nero" e "bianco". Prima di inziare a giocare si posizionano due pedine
 * bianche e due nere nelle quattro posizioni centrali della board in modo da creare
 * una configurazione a X. Quindi questa è la disposzione iniziale (. rappresenta
 * una posizione vuota, B una pedina bianca e N una nera):
 * <pre>
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . B N . . .
 *     . . . N B . . .
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . . . . . .
 * </pre>
 * Si muove alternativamente (inizia il nero) appoggiando una nuova pedina in una
 * posizione vuota in modo da imprigionare, tra la pedina che si sta giocando e
 * quelle del proprio colore già presenti sulla board, una o più pedine avversarie.
 * A questo punto le pedine imprigionate devono essere rovesciate (da bianche a nere
 * o viceversa, azione di tipo {@link Action.Kind#SWAP}) e diventano
 * di proprietà di chi ha eseguito la mossa. È possibile incastrare le pedine in
 * orizzontale, in verticale e in diagonale e, a ogni mossa, si possono girare
 * pedine in una o più direzioni. Sono ammesse solo le mosse con le quali si gira
 * almeno una pedina, se non è possibile farlo si salta il turno. Non è possibile
 * passare il turno se esiste almeno una mossa valida. Quando nessuno dei giocatori
 * ha la possibilità di muovere o quando la board è piena, si contano le pedine e si
 * assegna la vittoria a chi ne ha il maggior numero. Per ulteriori informazioni si
 * può consultare
 * <a href="https://it.wikipedia.org/wiki/Othello_(gioco)">Othello</a> */
public class Othello implements GameRuler<PieceModel<Species>> {
    private static class Change {
        public final Pos position;
        public final PieceModel<Species> oldPiece;
        public Change(Pos pos, PieceModel<Species> pm) {
            position = pos;
            oldPiece = pm;
        }
    }

    private static final PieceModel<Species> whitePiece = new PieceModel<>(Species.DISC, "bianco");
    private static final PieceModel<Species> blackPiece = new PieceModel<>(Species.DISC, "nero");

    private List<String> players;
    private int currentPlayerIndex; // { 0, 1 }
    private Set<Move<PieceModel<PieceModel.Species>>> currentPlayerValidMoves;
    private Board<PieceModel<Species>> board;
    private Board<PieceModel<Species>> boardView;
    private List<List<Change>> moveHistory;
    private List<Integer> nextPlayer;
    private boolean gameEnded;
    private boolean invalidMovePlayed;

    private final long allowedTimePerMove;
    private final int boardSize;

    private Mechanics<PieceModel<PieceModel.Species>> mechanics;

    /** Crea un GameRuler per fare una partita a Othello, equivalente a
     * {@link Othello#Othello(long, int, String, String) Othello(0,8,p1,p2)}.
     * @param p1  il nome del primo giocatore
     * @param p2  il nome del secondo giocatore
     * @throws NullPointerException se p1 o p2 è null */
    public Othello(String p1, String p2) {
        this(0,8,p1,p2);
    }

    /** Crea un GameRuler per fare una partita a Othello.
     * @param time  tempo in millisecondi per fare una mossa, se <= 0 significa nessun
     *              limite
     * @param size  dimensione della board, sono accettati solamente i valori 6,8,10,12
     * @param p1  il nome del primo giocatore
     * @param p2  il nome del secondo giocatore
     * @throws NullPointerException se {@code p1} o {@code p2} è null
     * @throws IllegalArgumentException se size non è uno dei valori 6,8,10 o 12 */
    public Othello(long time, int size, String p1, String p2) { //Todo time, size
        if (p1 != null && p2 != null) {
            if (size == 6 || size == 8 || size == 10 || size == 12) {
                players = new ArrayList<>();
                players.add(p1);
                players.add(p2);
                players = Collections.unmodifiableList(players);
                currentPlayerIndex = 0;
                board = new BoardOct<PieceModel<Species>>(size, size);
                int halfSize = size/2;
                board.put(blackPiece, new Pos(halfSize-1, halfSize-1));
                board.put(blackPiece, new Pos(halfSize, halfSize));
                board.put(whitePiece, new Pos(halfSize-1, halfSize));
                board.put(whitePiece, new Pos(halfSize, halfSize-1));
                currentPlayerValidMoves = Collections.unmodifiableSet(currentPlayerValidMoves());
                boardView = Utils.UnmodifiableBoard(board);
                moveHistory = new ArrayList<>();
                nextPlayer = new ArrayList<>();
                gameEnded = false;
                invalidMovePlayed = false;
                allowedTimePerMove = time;
                boardSize = size;
                mechanics = makeMechanics();
            } else {
                throw new IllegalArgumentException("Othello board's size must be 6,8,10 or 12, it is " + size);
            }
        } else {
            throw new NullPointerException("p1 and p2 in Othello constructor must both be not null.");
        }
    }

    /** Il nome rispetta il formato:
     * <pre>
     *     Othello<i>Size</i>
     * </pre>
     * dove <code><i>Size</i></code> è la dimensione della board, ad es. "Othello8x8". */
    @Override
    public String name() {
        int size = board.width();
        return "Othello" + size + "x" + size;
    }

    @Override
    public <T> T getParam(String name, Class<T> c) {
        // c.cast(Object) lancia ClassCastException se il cast fallisce
        if (name != null && c != null) {
            switch (name) {
                case "Time":
                    return c.cast(timeToParamString());
                case "Board":
                    return c.cast(boardSize + "x" + boardSize);
                default: throw new IllegalArgumentException("Invalid parameter name");
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
    public List<String> players() { return players; }

    /** Assegna il colore "nero" al primo giocatore e "bianco" al secondo. */
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
    public Board<PieceModel<Species>> getBoard() { return boardView; }

    /** Se il giocatore di turno non ha nessuna mossa valida il turno è
     * automaticamente passato all'altro giocatore. Ma se anche l'altro giuocatore
     * non ha mosse valide, la partita termina. */
    @Override
    public int turn() { return gameEnded ? 0 : currentPlayerIndex+1; }

    /** Se la mossa non è valida termina il gioco dando la vittoria all'altro
     * giocatore. */
    @Override
    public boolean move(Move<PieceModel<Species>> m) {
        if (m != null) {
            if (!gameEnded) {
                if (validMoves().contains(m) && m.kind!=Move.Kind.RESIGN) {
                    List<Change> changesThisTurn = new ArrayList<>();
                    for (Action<PieceModel<Species>> action : m.actions) {
                        if (action.kind.equals(Action.Kind.ADD)) {
                            Pos pos = action.pos.get(0);
                            changesThisTurn.add(new Change(pos, board.put(action.piece, pos)));
                        } else if (action.kind.equals(Action.Kind.SWAP)) {
                            for (Pos p : action.pos) {
                                changesThisTurn.add(new Change(p, board.put(action.piece, p)));
                            }
                        } else {
                            return false;
                        }
                    }
                    moveHistory.add(changesThisTurn);
                    nextPlayer.add(currentPlayerIndex);
                    nextPlayer();
                    Set<Move<PieceModel<PieceModel.Species>>> nextValidMoves = currentPlayerValidMoves();
                    if (nextValidMoves.size() == 1) {
                        nextPlayer();
                        nextValidMoves = currentPlayerValidMoves();
                        if (nextValidMoves.size() == 1) {
                            gameEnded = true;
                        } else {
                            currentPlayerValidMoves = Collections.unmodifiableSet(nextValidMoves);
                        }
                    } else {
                        currentPlayerValidMoves = Collections.unmodifiableSet(nextValidMoves);
                    }
                    return true;
                } else {
                    gameEnded = true;
                    invalidMovePlayed = true;
                    moveHistory.add(Collections.EMPTY_LIST);
                    nextPlayer.add(currentPlayerIndex);
                    nextPlayer();
                    return m.kind.equals(Move.Kind.RESIGN); //Resign and invalid moves both cause the game to end, but Resign is valid
                }
            } else {
                throw new IllegalStateException("Cannot play a move in a finished game");
            }
        } else {
            throw new NullPointerException("Cannot execute a null move");
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
    }

    public void printBoard() {
        //PrintBoard Todo togliere e mettere moveHistory private
        for (int y=getBoard().height()-1; y>=0; --y) {
            for (int x=0; x<getBoard().width(); ++x) {
                PieceModel piece = (PieceModel)getBoard().get(new Pos(x,y));
                if (piece==null) {
                    System.out.print(". ");
                } else {
                    System.out.print(piece.color.charAt(0)+" ");
                }
            }
            System.out.println();
        }
        System.out.println(moveHistory.size() +
                "\tScore1: " + score(1) +
                "\tScore2: " + score(2) +
                "\tNext turn: " + turn() +
                "\tResult: " + result()
        );
        System.out.println();
    }

    @Override
    public boolean unMove() {
        if (moveHistory.size() > 0) {
            List<Change> lastTurnChanges = moveHistory.remove(moveHistory.size()-1);
            for (Change change : lastTurnChanges) {
                if (change.oldPiece != null) {
                    board.put(change.oldPiece, change.position);
                } else {
                    board.remove(change.position);
                }
            }
            currentPlayerIndex = nextPlayer.remove(nextPlayer.size()-1);
            gameEnded = false;
            invalidMovePlayed = false;
            currentPlayerValidMoves = currentPlayerValidMoves();
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
                double score1 = score(1);
                double score2 = score(2);
                if (score1 > score2) return 1;
                else if (score1 < score2) return 2;
                else return 0;
            } else {
                return currentPlayerIndex+1;
            }
        } else {
            return -1;
        }
    }

    /** Ogni mossa, eccetto l'abbandono, è rappresentata da una {@link Action} di tipo
     * {@link Action.Kind#ADD} seguita da una {@link Action} di tipo
     * {@link Action.Kind#SWAP}. */
    @Override
    public Set<Move<PieceModel<Species>>> validMoves() {
        if (result() == -1) {
            return currentPlayerValidMoves;
        } else {
            throw new IllegalStateException("A finished game has no valid moves");
        }
    }

    private Set<Move<PieceModel<Species>>> currentPlayerValidMoves() {
        Set<Move<PieceModel<Species>>> validMoves = new HashSet<>();
        validMoves.add(new Move<>(Move.Kind.RESIGN));
        PieceModel<Species> allyPiece = currentPlayerIndex == 0 ? blackPiece : whitePiece;
        int left = 0, bottom = 0, right = board.width()-1, top = board.height()-1;
        int numPiecesOnBoard = board.get().size();
        if (numPiecesOnBoard==4) {
            left = bottom = board.width()/2 -2;
            right = top = board.width()/2 +1;
        } else if (numPiecesOnBoard == 5) {
            left = bottom = board.width()/2 -3;
            right = top = board.width()/2 +2;
        } else if (numPiecesOnBoard == 6 && board.width()>=10) {
            left = bottom = board.width()/2 -4;
            right = top = board.width()/2 +3;
        } else if (numPiecesOnBoard == 7 && board.width()>=10) {
            left = bottom = board.width()/2 -5;
            right = top = board.width()/2 +4;
        } else if (numPiecesOnBoard == 8 && board.width()>=12) {
            left = bottom = board.width()/2 -6;
            right = top = board.width()/2 +5;
        }
        for (int y=bottom; y <= top; ++y) {
            for (int x = left; x <= right; ++x) {
                Pos p = new Pos(x, y);
                if (board.get(p) == null) { //If there is no piece in this position
                    Set<Pos> affectedEnemyPositionsAfterAdd = getAffectedEnemyPositionsAfterAdd(p);
                    if (affectedEnemyPositionsAfterAdd.size() > 0) {
                        Action<PieceModel<Species>> addAction = new Action<>(p, allyPiece);
                        Action<PieceModel<Species>> swapAction = new Action<>(
                                allyPiece,
                                affectedEnemyPositionsAfterAdd.toArray(new Pos[affectedEnemyPositionsAfterAdd.size()])
                        );
                        Move<PieceModel<Species>> move = new Move<>(addAction, swapAction);
                        validMoves.add(move);
                    }
                }
            }
        }
        return Collections.unmodifiableSet(validMoves);
    }

    private Set<Pos> getAffectedEnemyPositionsAfterAdd(Pos p) {
        Set<Pos> positions = new HashSet<>();
        PieceModel<Species> allyPiece = currentPlayerIndex == 0 ? blackPiece : whitePiece;
        PieceModel<Species> enemyPiece = currentPlayerIndex == 0 ? whitePiece : blackPiece;
        for (Board.Dir dir : Board.Dir.values()) {
            List<Pos> posBuffer = new ArrayList<>();
            Pos currentPos = new Pos(p.b, p.t);
            boolean validDirection;
            while (true) {
                currentPos = board.adjacent(currentPos, dir);
                if (currentPos == null) {
                    validDirection = false;
                    break;
                }
                PieceModel<Species> currentPiece = board.get(currentPos);
                if (currentPiece == null) {
                    validDirection = false;
                    break;
                } else if (currentPiece == enemyPiece) {
                    posBuffer.add(currentPos);
                } else if (currentPiece == allyPiece) {
                    validDirection = true;
                    break;
                }
            }
            if (validDirection && posBuffer.size() > 0) {
                positions.addAll(posBuffer);
            }
        }
        return positions;
    }

    @Override
    public double score(int i) {
        if (i == 1 || i == 2) {
            int score = 0;
            PieceModel<Species> whichPiece = i==1 ? blackPiece : whitePiece;
            for (Pos p : board.positions()) {
                if (whichPiece.equals(board.get(p))) {
                    ++score;
                }
            }
            return score;
        } else {
            throw new IllegalArgumentException("1 and 2 are the only valid player indices");
        }
    }

    @Override
    public GameRuler<PieceModel<Species>> copy() {
        Othello cpy = new Othello(allowedTimePerMove, boardSize, players.get(0), players.get(1));
        cpy.currentPlayerIndex = this.currentPlayerIndex;
        cpy.board = new BoardOct<>(this.boardSize,this.boardSize);
        for (Pos p : this.board.positions()) {
            PieceModel<PieceModel.Species> piece = this.board.get(p);
            if (piece != null) {
                cpy.board.put(this.board.get(p), p);
            }
        }
        cpy.boardView = Utils.UnmodifiableBoard(cpy.board);
        for (List<Change> changesInTurn : this.moveHistory) {
            List<Change> cpyChangesInTurn = new ArrayList<>();
            for (Change change : changesInTurn) {
                cpyChangesInTurn.add(change);
            }
            cpy.moveHistory.add(cpyChangesInTurn);
        }
        cpy.mechanics = this.mechanics;
        return cpy;
    }

    @Override
    public Mechanics<PieceModel<Species>> mechanics() {
        return this.mechanics;
    }

    private Mechanics<PieceModel<Species>> makeMechanics() {
        Map<Pos, PieceModel<PieceModel.Species>> startSituationMap = new HashMap<>();
        startSituationMap.put(new Pos(boardSize/2-1, boardSize/2-1), blackPiece);
        startSituationMap.put(new Pos(boardSize/2, boardSize/2), blackPiece);
        startSituationMap.put(new Pos(boardSize/2-1, boardSize/2), whitePiece);
        startSituationMap.put(new Pos(boardSize/2, boardSize/2-1), whitePiece);
        return new Mechanics<>(
                allowedTimePerMove != 0 ? allowedTimePerMove : -1,
                Collections.unmodifiableList(Arrays.asList(blackPiece, whitePiece)),
                board.positions(),
                2,
                new Situation<>(startSituationMap, 1),
                getNext()
        );
    }

    private Next<PieceModel<PieceModel.Species>> getNext() {
        class Fast {
            Othello o;
            int fCurrPlInd;
            List<Change> changesThisTurn;
            Change addChange, swapChange;
            int left, right, bottom, top;
            int oLeft, oRight, oBottom, oTop; // Old
            public Fast(Othello o, int l, int b, int r, int t) {
                this.o = o;
                this.fCurrPlInd = o.currentPlayerIndex;
                left=l; bottom=b; right=r; top=t;
                oLeft = left; oRight = right; oBottom = bottom; oTop = top;
            }
            void fastMove(Move<PieceModel<PieceModel.Species>> move) {
                changesThisTurn = new ArrayList<>(2);
                for (Action<PieceModel<Species>> action : move.actions) {
                    if (action.kind.equals(Action.Kind.ADD)) {
                        Pos pos = action.pos.get(0);
                        left = Math.min(left, pos.b);
                        right = Math.max(right, pos.b);
                        bottom = Math.min(bottom, pos.t);
                        top = Math.max(top, pos.t);
                        changesThisTurn.add(new Change(pos, o.board.put(action.piece, pos)));
                    } else {
                        for (Pos p : action.pos) {
                            changesThisTurn.add(new Change(p, o.board.put(action.piece, p)));
                        }
                    }
                }
                o.moveHistory.add(changesThisTurn);
                o.currentPlayerIndex = 1-o.currentPlayerIndex;
                if (fastValidMoves().size() == 1) {
                    o.currentPlayerIndex=1-o.currentPlayerIndex;
                    if (fastValidMoves().size() == 1) {
                        o.gameEnded = true;
                    }
                }
            }
            void fastUnmove() {
                for (Change change : changesThisTurn) {
                    if (change.oldPiece != null) {
                        o.board.put(change.oldPiece, change.position);
                    } else {
                        o.board.remove(change.position);
                    }
                }
                o.currentPlayerIndex = fCurrPlInd;
                o.gameEnded = false;
                o.invalidMovePlayed = false;
                left=oLeft; bottom=oBottom; right=oRight; top=oTop;
            }
            Set<Move<PieceModel<Species>>> fastValidMoves() {
                Set<Move<PieceModel<Species>>> validMoves = new HashSet<>();
                validMoves.add(new Move<>(Move.Kind.RESIGN));
                PieceModel<Species> allyPiece = o.currentPlayerIndex == 0 ? blackPiece : whitePiece;
                int l=Math.max(left-1,0);
                int b=Math.max(bottom-1,0);
                int r=Math.min(right+1, o.board.width()-1);
                int t=Math.min(top+1, o.board.height()-1);
                for (int y=b; y <= t; ++y) {
                    for (int x = l; x <= r; ++x) {
                        Pos p = new Pos(x, y);
                        if (o.board.get(p) == null) { //If there is no piece in this position
                            Set<Pos> affectedEnemyPositionsAfterAdd = o.getAffectedEnemyPositionsAfterAdd(p);
                            if (affectedEnemyPositionsAfterAdd.size() > 0) {
                                Action<PieceModel<Species>> addAction = new Action<>(p, allyPiece);
                                Action<PieceModel<Species>> swapAction = new Action<>(
                                        allyPiece,
                                        affectedEnemyPositionsAfterAdd.toArray(new Pos[affectedEnemyPositionsAfterAdd.size()])
                                );
                                Move<PieceModel<Species>> move = new Move<>(addAction, swapAction);
                                validMoves.add(move);
                            }
                        }
                    }
                }
                return Collections.unmodifiableSet(validMoves);
            }
        }
        return situation -> {
            Objects.requireNonNull(situation);
            Othello othello = new Othello(allowedTimePerMove, boardSize, players.get(0), players.get(1));
            int halfSize = othello.board.width()/2;
            int l=halfSize-1,r=halfSize,b=halfSize-1,t=halfSize;
            for (Map.Entry<Pos,PieceModel<PieceModel.Species>> pair : situation.newMap().entrySet()){
                Pos k = pair.getKey();
                PieceModel<PieceModel.Species> v = pair.getValue();
                othello.board.put(v, k);
                l=Math.min(l,k.b);
                r=Math.max(r,k.b);
                b=Math.min(b,k.t);
                t=Math.max(t,k.t);
            }
            othello.gameEnded = situation.turn <= 0;
            if (!othello.isGameEnded()) {
                othello.currentPlayerIndex = situation.turn - 1;
                othello.currentPlayerValidMoves = othello.currentPlayerValidMoves();
                Set<Move<PieceModel<Species>>> validMoves = othello.currentPlayerValidMoves();
                HashMap<Move<PieceModel<Species>>, Situation<PieceModel<Species>>> nextSituations =
                        new HashMap<>();
                Fast fast = new Fast(othello,l,b,r,t);
                for (Move<PieceModel<Species>> move : validMoves) {
                    if (move.kind == Move.Kind.RESIGN) {
                        continue;
                    }
                    //othello.move(move);
                    fast.fastMove(move);
                    nextSituations.put(move, othello.toSituation());
                    //othello.unMove();
                    fast.fastUnmove();
                    othello.currentPlayerValidMoves = validMoves;
                }
                return nextSituations;
            } else return Collections.EMPTY_MAP;
        };
    }

    private boolean isValidSituation(Situation<PieceModel<PieceModel.Species>> situation) {
        /**
         * Cause partita non valida:
         * 1 - isole
         * 2 - Turno > 0 e solo RESIGN giocabile per il giocatore di turno
         * 3 - Turno <= 0 e mosse giocabili
         * 4 - Quattro posizioni centrali vuote (Almeno una)
         */

        //Crea Othello corrispondente e ottieni le mosse valide
        Othello othello = new Othello(allowedTimePerMove, boardSize, players().get(0), players.get(1));
        othello.board.positions().forEach( pos -> {
            PieceModel<PieceModel.Species> piece = situation.get(pos);
            if (piece != null) {
                othello.board.put(piece, pos);
            }
        });

        //Condizione 2
        if (situation.turn > 0) {
            othello.currentPlayerIndex = situation.turn - 1;
            Set<Move<PieceModel<Species>>> validMoves = othello.currentPlayerValidMoves();
            if (validMoves.size() <= 1) {
                return false;
            }
        }

        //Condizione 3
        if (situation.turn <= 0) {
            othello.currentPlayerIndex = 0; //Primo giocatore
            Set<Move<PieceModel<Species>>> validMoves = othello.currentPlayerValidMoves();
            if (validMoves.size() > 1) {
                return false;
            }

            othello.currentPlayerIndex = 1; //Secondo giocatore
            validMoves = othello.currentPlayerValidMoves();
            if (validMoves.size() > 1) {
                return false;
            }
        }

        //Condizione 4
        Pos[] centerPositions = new Pos[]{
                new Pos(boardSize/2-1, boardSize/2-1),
                new Pos(boardSize/2, boardSize/2),
                new Pos(boardSize/2, boardSize/2-1),
                new Pos(boardSize/2-1, boardSize/2),
        };
        for (Pos p : centerPositions) {
            if (othello.board.get(p) == null) {
                return false;
            }
        }

        //Condizione 1
        Set<Pos> visited = new HashSet<>();
        Queue<Pos> toVisit = new LinkedList<>();
        visited.add(new Pos(boardSize / 2, boardSize / 2));
        toVisit.add(new Pos(boardSize / 2, boardSize / 2)); //Il controllo della condizione 4 ci assicura che è non-null
        //Visita grafo
        do {
            Pos p = toVisit.poll();
            for (Board.Dir dir : Board.Dir.values()) {
                Pos adj = othello.board.adjacent(p, dir);
                if (adj != null && othello.board.isPos(adj) && othello.board.get(adj) != null && !visited.contains(adj)) {
                    toVisit.add(adj);
                    visited.add(adj);
                }
            }
        } while (toVisit.size() > 0);

        if (othello.board.get().size() != visited.size()) {;
            return false;
        }

        return true;
    }

    private boolean isGameEnded() {
        return gameEnded;
    }
}

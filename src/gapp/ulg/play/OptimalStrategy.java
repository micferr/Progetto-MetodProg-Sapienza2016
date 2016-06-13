package gapp.ulg.play;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.util.Probe;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static gapp.ulg.play.BinaryGameResult.FIRST_PLAYER;
import static gapp.ulg.play.BinaryGameResult.SECOND_PLAYER;
import static gapp.ulg.play.BinaryGameResult.TIE;

class OptimalStrategy<P> implements OptimalPlayerFactory.Strategy<P> {
    private static class SituationEncoding<P> implements Serializable {
        byte[] enc;

        SituationEncoding(Probe.EncS<P> encS) {
            this.enc = encS.enc;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof SituationEncoding)) return false;
            return Arrays.equals(this.enc, ((SituationEncoding) obj).enc);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(enc);
        }
    }

    private final String name;
    private Map<Probe.EncS<P>, BinaryGameResult> choiceMap; //Situation - Situation's value
    private Map<SituationEncoding<P>, BinaryGameResult> nChoiceMap;
    private final GameRuler.Mechanics<P> mechanics;

    public OptimalStrategy(
            String name,
            GameRuler.Mechanics<P> mechanics
    ) {
        this.name = Objects.requireNonNull(name);
        this.mechanics = mechanics;
    }

    public void setChoiceMap(Map<Probe.EncS<P>, BinaryGameResult> map) {
        Objects.requireNonNull(map);
        choiceMap = map;
        nChoiceMap = new HashMap<>();
        for (Map.Entry<Probe.EncS<P>, BinaryGameResult> entry : map.entrySet()) {
            nChoiceMap.put(new SituationEncoding<>(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public String gName() {
        return name;
    }

    @Override
    public Move<P> move(GameRuler.Situation<P> s, GameRuler.Next<P> next) {
        if (choiceMap == null) {
            throw new IllegalStateException("ChoiceMap not set");
        }
        Map<Move<P>, GameRuler.Situation<P>> nextS = next.get(s);
        if (nextS.size() == 0 || s.turn <= 0) {
            throw new IllegalStateException("Should never happen.");
        }
        int turn = s.turn;
        boolean first = true;
        Move<P> bestMove = null;
        BinaryGameResult bestResult = null;
        for (Map.Entry<Move<P>, GameRuler.Situation<P>> nextPair : nextS.entrySet()) {
            SituationEncoding<P> sitEnc = new SituationEncoding<>(new Probe.EncS<>(mechanics, nextPair.getValue()));
            if (!nChoiceMap.containsKey(sitEnc)) continue;
            if (first) {
                bestMove = nextPair.getKey();
                //bestResult = choiceMap.get(new Probe.EncS<>(mechanics, nextPair.getValue()));
                bestResult = nChoiceMap.get(sitEnc);
                if (bestResult.ordinal() == turn) {
                    return bestMove;
                }
                first = false;
            } else {
                Move<P> currentMove = nextPair.getKey();
                //BinaryGameResult currentResult = choiceMap.get(new Probe.EncS<>(mechanics, nextPair.getValue()));
                BinaryGameResult currentResult =nChoiceMap.get(sitEnc);
                if (currentResult.ordinal() == turn) {
                    return currentMove;
                } else if (currentResult == TIE && bestResult == (turn == 1 ? SECOND_PLAYER : FIRST_PLAYER)) {
                    bestResult = currentResult;
                    bestMove = currentMove;
                }
            }
        }
        return bestMove;
    }

    static <T> void writeToFile(OptimalStrategy<T> optimalStrategy, Path dirPath) {
        try {
            String filePath = new File(dirPath.toFile(), "strategy_" + optimalStrategy.gName() + ".dat").toString();
            FileOutputStream fout = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            //oos.writeObject(optimalStrategy.choiceMap);
            oos.writeObject(optimalStrategy.nChoiceMap);
            fout.close();
            oos.close();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write to file - Exception caused by " + e.getClass() + "with message: " + e.getMessage());
        }
    }

    static <T> OptimalStrategy<T> loadFromFile(Path dirPath, String gameName, GameRuler.Mechanics<T> mechanics) {
        OptimalStrategy<T> strategy = new OptimalStrategy<T>(gameName, mechanics);
        try {
            String filePath = new File(dirPath.toFile(), "strategy_" + gameName + ".dat").toString();
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            @SuppressWarnings("unchecked")
            //Map<Probe.EncS<T>, BinaryGameResult> strategyMap = (Map<Probe.EncS<T>, BinaryGameResult>) ois.readObject();
            //strategy.setChoiceMap(strategyMap);
            Map<SituationEncoding<T>, BinaryGameResult> strategyMap = (Map<SituationEncoding<T>, BinaryGameResult>) ois.readObject();
            strategy.choiceMap = new HashMap<>();
            strategy.nChoiceMap = strategyMap;
            return strategy;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Exception thrown while reading strategy file: " + e.getClass() + " with message: " + e.getMessage());
        }
    }
}

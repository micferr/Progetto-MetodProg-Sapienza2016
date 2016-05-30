package gapp.ulg.play;

public enum Result {
    WIN,
    TIE,
    LOSS,
    NOT_CALCULATED;

    public static boolean isBetter(Result newR, Result oldR) {
        return ((newR == Result.WIN && (oldR == TIE || oldR == Result.LOSS)) || (newR == TIE && oldR == LOSS));
    }

    public static boolean isWorse(Result newR, Result oldR) {
        return ((newR == Result.LOSS && (oldR == TIE || oldR == Result.WIN)) || (newR == TIE && oldR == WIN));
    }
}

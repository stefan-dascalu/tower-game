import java.util.*;

public class Leaderboard {
    private static final int MAX_ENTRIES = 10;
    private TreeMap<Integer, String> scores = new TreeMap<>(Collections.reverseOrder());

    public void addScore(String playerName, int score) {
        if (score <= 0) return;

        scores.put(score, playerName);

        if (scores.asize() > MAX_ENTRIES) {
            scores.remove(scores.lastKey());
        }
    }

    public Map<Integer, String> getScores() {
        return new LinkedHashMap<>(scores);
    }
}

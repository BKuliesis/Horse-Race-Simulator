import java.util.*;

public class HorseStatistics {
    ArrayList<HashMap<String, Object>> statsHistory;

    public HorseStatistics() {
        statsHistory = new ArrayList<HashMap<String, Object>>();
    }

    public void addStats(int raceLength, int distanceTravelled, int raceTime, boolean fell) {
        HashMap<String, Object> stats = new HashMap<>();
        double averageSpeed = (double) distanceTravelled / ((double) raceTime / (10.0));

        boolean won = distanceTravelled == raceLength;

        stats.put("averageSpeed", averageSpeed);
        stats.put("won", won);
        stats.put("fell", fell);

        statsHistory.add(stats);
    }

    public double getAverageSpeed() {
        double totalSpeed = 0;
        int totalRaces = statsHistory.size();

        for (HashMap<String, Object> stats : statsHistory) {
            totalSpeed += (double) stats.get("averageSpeed");
        }

        return totalSpeed / (double) totalRaces;
    }

    public String getAverageSpeedAsString() {
        double averageSpeed = Math.round(getAverageSpeed() * 10.0) / 10.0;

        return String.valueOf(averageSpeed) + " m/s";
    }

    public double getWinRate() {
        int wins = 0;
        int totalRaces = statsHistory.size();

        for (HashMap<String, Object> stats : statsHistory) {
            if ((boolean) stats.get("won")) {
                wins++;
            }
        }

        return (double) wins / (double) totalRaces;
    }

    public String getWinPercentageAsString() {
        int winRatio = (int) (Math.round(getWinRate() * 100.0));
        
        return String.valueOf(winRatio) + "%";
    }

    public double getFallRate() {
        int falls = 0;
        int totalRaces = statsHistory.size();

        for (HashMap<String, Object> stats : statsHistory) {
            if ((boolean) stats.get("fell")) {
                falls++;
            }
        }

        return (double) falls / (double) totalRaces;
    }

    public String getFallPercentageAsString() {
        int fallRatio = (int) (Math.round(getFallRate() * 100.0));

        return String.valueOf(fallRatio) + "%";
    }
}

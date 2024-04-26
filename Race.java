import java.lang.Math;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 *
 * @author Benas Kuliesis
 * @version 2.0
 */
public class Race {
    private int raceLength;
    private int raceTime = 0;
    private Horse[] horses;

    public Race(int distance, Horse[] horsesArray) {
        raceLength = distance;
        horses = horsesArray;
    }

    public void resetRace() {
        for (Horse horse : horses) {
            horse.goBackToStart();
            horse.resetFall();
        }
    }

    public boolean[] updateRace() {
        boolean finished = false;

        boolean[] hasWon = new boolean[] {false, false, false};

        for (int i = 0; i < horses.length; i++) {
            moveHorse(horses[i]);
            if (raceWonBy(horses[i])) {
                hasWon[i] = true;
            }
        }

        if (hasWon[0] || hasWon[1] || hasWon[2] || (horses[0].hasFallen() && horses[1].hasFallen() && horses[2].hasFallen())) {
            finished = true;
        }

        return new boolean[] {finished, hasWon[0], hasWon[1], hasWon[2]};
    }

    public void updatePoints() {
        int[] distancesTravelled = new int[] {horses[0].getDistanceTravelled(), horses[1].getDistanceTravelled(), horses[2].getDistanceTravelled()};

        int tieCount = 0;

        Arrays.sort(distancesTravelled);

        for (int i = 1; i < distancesTravelled.length; i++) {
            if (distancesTravelled[i] == distancesTravelled[i - 1]) {
                tieCount++;
            }
        }
        
        if (tieCount == 1) {
            int[] uniqueDistances = new int[2];
            int index = 0;

            for (int i = 0; i < distancesTravelled.length; i++) {
                boolean duplicate = false;
                for (int j = 0; j < i; j++) {
                    if (distancesTravelled[i] == distancesTravelled[j]) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    uniqueDistances[index] = distancesTravelled[i];
                    index++;
                }
            }
            distancesTravelled = uniqueDistances;
        }

        for (Horse horse : horses) {
            if (tieCount == 0) {
                if (horse.getDistanceTravelled() == distancesTravelled[0]) {
                    horse.addPoints(1);
                } else if (horse.getDistanceTravelled() == distancesTravelled[1]) {
                    horse.addPoints(2);
                } else if (horse.getDistanceTravelled() == distancesTravelled[2]) {
                    horse.addPoints(3);
                }
            } else if (tieCount == 1) {
                if (horse.getDistanceTravelled() == distancesTravelled[0]) {
                    horse.addPoints(1);
                } else if (horse.getDistanceTravelled() == distancesTravelled[1]) {
                    horse.addPoints(2);
                }
            } else if (tieCount == 2) {
                horse.addPoints(1);
            }
        }
    }

    private void moveHorse(Horse theHorse) {
        if  (!theHorse.hasFallen()) {
            if (Math.random() < theHorse.getConfidence()) {
                theHorse.moveForward();
            }

            if (Math.random() < (0.1 * theHorse.getConfidence() * theHorse.getConfidence())) {
                double newConfidence = theHorse.getConfidence();
                if (newConfidence > 0.1) {
                    newConfidence = newConfidence - 0.1;
                }
                newConfidence = Math.round(newConfidence * 10.0) / 10.0;
                theHorse.setConfidence(newConfidence);
                theHorse.fall();
            }
        }
    }

    private boolean raceWonBy(Horse theHorse) {
        if (theHorse.getDistanceTravelled() == raceLength) {
            double newConfidence = theHorse.getConfidence();
            if (newConfidence < 1.0) {
                newConfidence = newConfidence + 0.1;
            }
            newConfidence = Math.round(newConfidence * 10.0) / 10.0;
            theHorse.setConfidence(newConfidence);
            return true;
        } else {
            return false;
        }
    }

    public int getRaceLength() {
        return raceLength;
    }

    public int getRaceTime() {
        return raceTime;
    }

    public int incrementRaceTime() {
        return raceTime++;
    }

    public double[] calculateBettingOdds() {
        double[] confidences = new double[] {horses[0].getConfidence(), horses[1].getConfidence(), horses[2].getConfidence()};

        int numSimulations = 10000;
        Map<Horse, Integer> winCounts = new HashMap<>();

        // Initialise win count for each horse
        for (Horse horse : horses) {
            winCounts.put(horse, 0);
        }

        // Simulate the race multiple times
        for (int i = 0; i < numSimulations; i++) {
            resetRace();
            boolean[] result;
            do {
                result = updateRace();
            } while (!result[0]);

            // Check which horses have won and update win counts
            for (int j = 1; j < result.length; j++) {
                if (result[j]) { // if horse j has won
                    Horse winningHorse = horses[j - 1];
                    winCounts.put(winningHorse, winCounts.get(winningHorse) + 1);
                }
            }

            // Reset confidences
            for (int horseIndex = 0; horseIndex < horses.length; horseIndex++) {
                horses[horseIndex].setConfidence(confidences[horseIndex]);
            }
        }

        // Calculate betting odds based on win counts
        double[] odds = new double[horses.length];
        for (int i = 0; i < horses.length; i++) {
            Horse horse = horses[i];
            odds[i] = (double) winCounts.get(horse) / numSimulations;
        }

        resetRace();

        return odds;
    }
}
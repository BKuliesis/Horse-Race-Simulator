import java.awt.image.BufferedImage;

/**
 * A horse participating in races
 * 
 * @author Benas Kuliesis
 * @version Version 2.0
 */
public class Horse {
    private String name;
    private BufferedImage image;
    private BufferedImage fallenImage;
    private int distanceTravelled = 0;
    private boolean fallen = false;
    private double confidence;
    private int points = 0;
    private HorseStatistics stats;
    
    public Horse(String horseName, BufferedImage horseImage, BufferedImage horseFallenImage, double horseConfidence) {
        name = horseName;
        image = horseImage;
        fallenImage = horseFallenImage;
        confidence = horseConfidence;
        stats = new HorseStatistics();
    }

    public Horse(String horseName, BufferedImage horseImage, BufferedImage horseFallenImage, double horseConfidence, HorseStatistics horseStats) {
        name = horseName;
        image = horseImage;
        fallenImage = horseFallenImage;
        confidence = horseConfidence;
        stats = horseStats;
    }

    public String getName() {
        return name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getFallenImage() {
        return fallenImage;
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public void moveForward() {
        distanceTravelled ++;
    }
    
    public void goBackToStart() {
        distanceTravelled = 0;
    }
    
    public void fall() {
        fallen = true;
    }

    public void resetFall() {
        fallen = false;
    }

    public boolean hasFallen() {
        return fallen;
    }
    
    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double newConfidence) {
        confidence = newConfidence;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int newPoints) {
        points += newPoints;
    }

    public void resetPoints() {
        points = 0;
    }

    public HorseStatistics getStats() {
        return stats;
    }

    public void addStats(int raceLength, int raceTime) {
        stats.addStats(raceLength, distanceTravelled, raceTime, fallen);
    }
}
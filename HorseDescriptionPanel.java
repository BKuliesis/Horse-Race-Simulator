import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class HorseDescriptionPanel extends JPanel {
    private Horse horse;
    private JLabel horse1ConfidenceLabel;
    private JLabel horse1StatsisticLabel;
    private JLabel horse2StatsisticLabel;
    private JLabel horse3StatsisticLabel;
    
    public HorseDescriptionPanel(Horse horse) {
        this.horse = horse;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        JLabel horseNameLabel = new JLabel(this.horse.getName());
        horseNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        horseNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(horseNameLabel);

        horse1ConfidenceLabel = new JLabel("Confidence: " + this.horse.getConfidence());
        horse1ConfidenceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        horse1ConfidenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(horse1ConfidenceLabel);

        horse1StatsisticLabel = new JLabel("Average Speed: " + this.horse.getStats().getAverageSpeedAsString());
        horse1StatsisticLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        horse1StatsisticLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        horse1StatsisticLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
        add(horse1StatsisticLabel);

        horse2StatsisticLabel = new JLabel("Win Percentage: " + this.horse.getStats().getWinPercentageAsString());
        horse2StatsisticLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        horse2StatsisticLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        horse2StatsisticLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        add(horse2StatsisticLabel);

        horse3StatsisticLabel = new JLabel("Fall Percentage: " + this.horse.getStats().getFallPercentageAsString());
        horse3StatsisticLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        horse3StatsisticLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        horse3StatsisticLabel.setBorder(new EmptyBorder(2, 0, 0, 0));
        add(horse3StatsisticLabel);

        add(Box.createVerticalGlue());
    }

    public void setConfidenceLabel(double confidence) {
        horse1ConfidenceLabel.setText("Confidence: " + confidence);
    }

    public void updateStats() {
        horse1StatsisticLabel.setText("Average Speed: " + this.horse.getStats().getAverageSpeedAsString());
        horse2StatsisticLabel.setText("Win Percentage: " + this.horse.getStats().getWinPercentageAsString());
        horse3StatsisticLabel.setText("Fall Percentage: " + this.horse.getStats().getFallPercentageAsString());
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The GUI for the horse race simulator
 * 
 * @author Benas Kuliesis
 * @version 2.0
 */
public class GUI {
    private final JFrame frame;
    private final JPanel panel;

    private boolean firstRunOfProgram = true;

    private final JPanel customiseLengthsPanel;
    private final List<JPanel> trackPanels = new ArrayList<>();
    private final int maxTracks = 16;
    private int numberOfTracks = 1;
    private int[] trackConfigurations;

    private final double startingConfidence = 0.5;
    private final Horse[] horses = new Horse[3];
    private final String[] horseNames = {"Horse 1", "Horse 2", "Horse 3"};
    private final String[] horseColours = {"Orange", "Orange", "Orange"};
    private final String[] horseSaddles = {"No Saddle", "No Saddle", "No Saddle"};

    private int raceNumber = 1;
    private Race race;

    private JPanel raceScreenPanel;
    private LanePanel[] lanePanels;
    private HorseDescriptionPanel[] horseDescriptionPanels;
    private JLabel winnerLabel;
    private CustomButton startRaceButton;

    private BettingPanel bettingPanel;
    private double balance = 100.0;

    public GUI() {
        frame = new JFrame("Horse Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setResizable(false);
        frame.setVisible(true);

        panel = new JPanel(new GridLayout(0, 1));
        frame.add(panel);
        
        customiseLengthsPanel = new JPanel(new FlowLayout(1, 800, 5));

        addTrack();
    }

    public void startRaceGUI() {
        trackCustomisation();
    }

    private void setupRace(int distance) {
        race = new Race(distance, horses);
        race.resetRace();

        raceScreenPanel = new JPanel(new BorderLayout());
        raceScreenPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 10, 25));
        panel.add(raceScreenPanel);

        JPanel textPanel = new JPanel(new GridLayout(0, 2, 100, 0));
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        raceScreenPanel.add(textPanel, BorderLayout.NORTH);

        JLabel raceNumberLabel = new JLabel("Race " + raceNumber + " of " + trackConfigurations.length);
        raceNumberLabel.setFont(new Font("Arial", Font.BOLD, 20));
        raceNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(raceNumberLabel);

        JLabel raceLength = new JLabel("Race length: " + distance + " m");
        raceLength.setFont(new Font("Arial", Font.BOLD, 20));
        raceLength.setHorizontalAlignment(JLabel.CENTER);
        textPanel.add(raceLength);

        JPanel contentPanel = new JPanel(new BorderLayout());
        raceScreenPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel racePanel = new JPanel(new BorderLayout());
        racePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 0));
        contentPanel.add(racePanel, BorderLayout.CENTER);
        
        JPanel trackPanel = new JPanel(new GridLayout(0, 1));
        racePanel.add(trackPanel, BorderLayout.CENTER);

        JPanel namesPanel = new JPanel(new GridLayout(0, 1));
        namesPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        racePanel.add(namesPanel, BorderLayout.EAST);

        winnerLabel = new JLabel(" ");
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        winnerLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        racePanel.add(winnerLabel, BorderLayout.SOUTH);

        lanePanels = new LanePanel[3];
        horseDescriptionPanels = new HorseDescriptionPanel[3];

        for (int i = 0; i < 3; i++) {
            lanePanels[i] = new LanePanel(horses[i], race.getRaceLength());
            trackPanel.add(lanePanels[i]);

            horseDescriptionPanels[i] = new HorseDescriptionPanel(horses[i]);
            namesPanel.add(horseDescriptionPanels[i]);
        }

        bettingPanel = new BettingPanel(horses, balance, frame);
        contentPanel.add(bettingPanel, BorderLayout.SOUTH);

        bettingPanel.updateOdds(race);

        startRaceButton = new CustomButton("Start Race");
        raceScreenPanel.add(startRaceButton, BorderLayout.SOUTH);

        startRaceButton.addActionListener(e -> {
            raceNumber++;
            bettingPanel.disableBetting();
            startRaceButton.setEnabled(false);
            runRace();
        });

        panel.revalidate();
        panel.repaint();
    }

    private void runRace() {
        Timer timer = new Timer(100, e -> {
            race.incrementRaceTime();

            boolean[] raceUpdate = race.updateRace();

            for (int i = 0; i < 3; i++) {
                lanePanels[i].updatePosition();
                horseDescriptionPanels[i].setConfidenceLabel(horses[i].getConfidence());
            }

            if (raceUpdate[0]) {  
                if (raceUpdate[1] && raceUpdate[2] && raceUpdate[3]) {
                    winnerLabel.setText("It's a tie between all horses!");
                } else if (raceUpdate[1] && raceUpdate[2]) {
                    winnerLabel.setText("It's a tie between " + horses[0].getName() + " and " + horses[1].getName() + "!");
                } else if (raceUpdate[1] && raceUpdate[3]) {
                    winnerLabel.setText("It's a tie between " + horses[0].getName() + " and " + horses[2].getName() + "!");
                } else if (raceUpdate[2] && raceUpdate[3]) {
                    winnerLabel.setText("It's a tie between " + horses[1].getName() + " and " + horses[2].getName() + "!");
                } else if (raceUpdate[1] && raceUpdate[2] && raceUpdate[3]) {
                    winnerLabel.setText("It's a tie between all horses!");
                } else if (raceUpdate[1]) {
                    winnerLabel.setText("The winner is " + horses[0].getName() + "!");
                } else if (raceUpdate[2]) {
                    winnerLabel.setText("The winner is " + horses[1].getName() + "!");
                } else if (raceUpdate[3]) {
                    winnerLabel.setText("The winner is " + horses[2].getName() + "!");
                } else {
                    winnerLabel.setText("No winner!");
                }
                resetRace();
                ((Timer)e.getSource()).stop();
            }

            panel.repaint();
        });
        timer.start();
    }

    private void resetRace() {
        for (int i = 0; i < 3; i++) {
            horses[i].addStats(race.getRaceLength(), race.getRaceTime());
            horseDescriptionPanels[i].updateStats();
        }

        race.updatePoints();

        raceScreenPanel.remove(startRaceButton);

        CustomButton resetRaceButton = new CustomButton("");
        raceScreenPanel.add(resetRaceButton, BorderLayout.SOUTH);
        
        if (raceNumber > trackConfigurations.length) {
            resetRaceButton.setText("Results");
            resetRaceButton.addActionListener(e -> {
                raceNumber = 1;
                panel.removeAll();
                resultsScreen();
            });
        } else {
            resetRaceButton.setText("Next Race");
            resetRaceButton.addActionListener(e -> {
                panel.removeAll();
                setupRace(trackConfigurations[raceNumber - 1]);
            });
        }

        bettingPanel.evaluateBets(race);
        balance = bettingPanel.getBalance();

        panel.revalidate();
        panel.repaint();
    }

    private void resultsScreen() {
        MenuPanel resultsScreenPanel = new MenuPanel("Results", true);
        panel.add(resultsScreenPanel);

        JPanel resultsPanel = new JPanel(new GridLayout(0, 1));
        resultsScreenPanel.getContentPanel().add(resultsPanel);

        Horse[] sortedHorses = new Horse[] {horses[0], horses[1], horses[2]};

        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 3; j++) {
                if (sortedHorses[j].getPoints() > sortedHorses[i].getPoints()) {
                    Horse temp = sortedHorses[i];
                    sortedHorses[i] = sortedHorses[j];
                    sortedHorses[j] = temp;
                }
            }
        }

        ArrayList<ArrayList<Horse>> horseRankings = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (i == 0 || sortedHorses[i].getPoints() == sortedHorses[i - 1].getPoints()) {
                if (horseRankings.size() == 0) {
                    horseRankings.add(new ArrayList<>());
                }
                horseRankings.get(horseRankings.size() - 1).add(sortedHorses[i]);
            } else {
                horseRankings.add(new ArrayList<>());
                horseRankings.get(horseRankings.size() - 1).add(sortedHorses[i]);
            }
        }

        JPanel horseRankingsPanel = new JPanel(new GridLayout(0, 1));
        horseRankingsPanel.setBorder(new EmptyBorder(50, 0, 50, 0));
        resultsPanel.add(horseRankingsPanel);

        for (int i = 0; i < horseRankings.size(); i++) {
            ArrayList<Horse> ranking = horseRankings.get(i);

            for (Horse horse : ranking) {
                JPanel horseRanking = new JPanel(new GridLayout(0, 3, 10, 0));
                horseRankingsPanel.add(horseRanking);

                JLabel rankingLabel = new JLabel("");
                rankingLabel.setFont(new Font("Arial", Font.BOLD, 24));
                rankingLabel.setHorizontalAlignment(JLabel.RIGHT);
                horseRanking.add(rankingLabel);

                if (i == 0) {
                    rankingLabel.setText("First Place");
                    rankingLabel.setForeground(new Color(225, 195, 0));
                } else if (i == 1) {
                    rankingLabel.setText("Second Place");
                    rankingLabel.setForeground(new Color(132, 132, 132));  
                } else if (i == 2) {
                    rankingLabel.setText("Third Place");
                    rankingLabel.setForeground(new Color(205, 127, 50));
                }

                JLabel horseName = new JLabel(horse.getName());
                horseName.setFont(new Font("Arial", Font.BOLD, 24));
                horseName.setHorizontalAlignment(JLabel.CENTER);
                horseRanking.add(horseName);

                JLabel horsePoints = new JLabel("Points: " + horse.getPoints());
                horsePoints.setFont(new Font("Arial", Font.PLAIN, 24));
                horsePoints.setHorizontalAlignment(JLabel.LEFT);
                horseRanking.add(horsePoints);
            }
        }

        for (Horse horse : horses) {
            horse.resetPoints();
        }

        resultsScreenPanel.getButtons()[0].setText("Repeat Races");
        resultsScreenPanel.getButtons()[1].setText("Create New Races");

        resultsScreenPanel.getButtons()[0].addActionListener(e -> {
            for (Horse horse : horses) {
                horse.setConfidence(startingConfidence);
            }

            panel.removeAll();
            setupRace(trackConfigurations[0]);
        });

        resultsScreenPanel.getButtons()[1].addActionListener(e -> {
            panel.removeAll();
            trackCustomisation();
        });
        
        panel.revalidate();
        panel.repaint();
    }

    private void trackCustomisation() {
        MenuPanel trackCustomisationPanel = new MenuPanel("Track Customisation", false);
        panel.add(trackCustomisationPanel);

        JPanel trackButtonsPanel = new JPanel(new GridLayout(0, 2));
        trackButtonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        trackCustomisationPanel.getContentPanel().add(trackButtonsPanel, BorderLayout.NORTH);

        CustomButton addTrackButton = new CustomButton("Add Track");
        CustomButton removeTrackButton = new CustomButton("Remove Track");
        trackButtonsPanel.add(addTrackButton);
        trackButtonsPanel.add(removeTrackButton);

        if (numberOfTracks == 1) {
            removeTrackButton.setEnabled(false);
        }

        if (numberOfTracks == maxTracks) {
            addTrackButton.setEnabled(false);
        }

        addTrackButton.addActionListener(e -> {
            if (numberOfTracks < maxTracks) {
                numberOfTracks++;
                addTrack();

                if (numberOfTracks == maxTracks) {
                    addTrackButton.setEnabled(false);
                }

                if (numberOfTracks == 2) {
                    removeTrackButton.setEnabled(true);
                }
            }
        });

        removeTrackButton.addActionListener(e -> {
            if (numberOfTracks > 1) {
                numberOfTracks--;
                removeTrack();

                if (numberOfTracks == 1) {
                    removeTrackButton.setEnabled(false);
                }

                if (numberOfTracks == maxTracks - 1) {
                    addTrackButton.setEnabled(true);
                }
            }
        });

        trackCustomisationPanel.getContentPanel().add(customiseLengthsPanel, BorderLayout.CENTER);

        trackCustomisationPanel.getButtons()[0].addActionListener(e -> {
            trackConfigurations = new int[numberOfTracks];
            boolean outOfBounds = false;
            for (int i = 0; i < numberOfTracks; i++) {
                JPanel trackPanel = trackPanels.get(i);
                JTextField lengthField = (JTextField) trackPanel.getComponent(1);
                try {
                    int trackLength = Integer.parseInt(lengthField.getText());
                    if (trackLength >= 5 && trackLength <= 100) {
                        trackConfigurations[i] = trackLength;
                    } else {
                        outOfBounds = true;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Track lengths must be numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (outOfBounds) {
                JOptionPane.showMessageDialog(frame, "Track lengths must be between 5 and 100!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            panel.removeAll();
            horsesCustomisation();
        });
        
        updateTrackDisplay();
    }

    private void addTrack() {
        JPanel newTrackPanel = new JPanel(new BorderLayout(10, 0));
        newTrackPanel.add(new JLabel("Track " + numberOfTracks + " Length:"), BorderLayout.WEST);
        JTextField lengthField = new JTextField("20", 3);
        lengthField.setHorizontalAlignment(JTextField.CENTER);
        newTrackPanel.add(lengthField);
        trackPanels.add(newTrackPanel);
        updateTrackDisplay();
    }

    private void removeTrack() {
        if (!trackPanels.isEmpty()) {
            JPanel toRemove = trackPanels.remove(trackPanels.size() - 1);
            customiseLengthsPanel.remove(toRemove);
            updateTrackDisplay();
        }
    }

    private void updateTrackDisplay() {
        customiseLengthsPanel.removeAll();
        for (JPanel trackPanel : trackPanels) {
            customiseLengthsPanel.add(trackPanel);
        }
        customiseLengthsPanel.revalidate();
        customiseLengthsPanel.repaint();
    }

    private void horsesCustomisation() {
        MenuPanel horsesCustomisationPanel = new MenuPanel("Horses Customisation", true);
        panel.add(horsesCustomisationPanel);

        horsesCustomisationPanel.getContentPanel().setLayout(new GridLayout(0, 1));

        HorseCustomisationPanel[] horsePanels = new HorseCustomisationPanel[3];

        for (int i = 0; i < 3; i++) {
            HorseCustomisationPanel horsePanel = new HorseCustomisationPanel(i + 1, horseNames[i], horseColours[i], horseSaddles[i]);
            horsesCustomisationPanel.getContentPanel().add(horsePanel);
            horsePanels[i] = horsePanel;
        }

        horsesCustomisationPanel.getButtons()[0].addActionListener(e -> {
            panel.removeAll();
            trackCustomisation();
        });

        horsesCustomisationPanel.getButtons()[1].addActionListener(e -> {
            String[] horseAppearanceStrings = new String[3];
            boolean[] outOfBounds = new boolean[3];
            for (int i = 0; i < 3; i++) {
                HorseCustomisationPanel horsePanel = horsePanels[i];
                horseNames[i] = horsePanel.getHorseName();
                horseAppearanceStrings[i] = horsePanel.getHorseAppearanceString();
                if (horseNames[i].length() < 1 || horseNames[i].length() > 12) {
                    outOfBounds[i] = true;
                }
                horseColours[i] = horsePanel.getHorseColour();
                horseSaddles[i] = horsePanel.getHorseSaddle();
            }
            if (outOfBounds[0] || outOfBounds[1] || outOfBounds[2]) {
                JOptionPane.showMessageDialog(frame, "Horse names must be between 1 and 12 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < 3; i++) {
                try {
                    BufferedImage horseImage = ImageIO.read(new File("images/horse" + horseAppearanceStrings[i] + ".png"));
                    BufferedImage horseFallenImage = ImageIO.read(new File("images/horse_fallen" + horseAppearanceStrings[i] + ".png"));

                    if (firstRunOfProgram) {
                        horses[i] = new Horse(horseNames[i], horseImage, horseFallenImage, startingConfidence);
                    } else {
                        HorseStatistics stats = horses[i].getStats();
                        horses[i] = new Horse(horseNames[i], horseImage, horseFallenImage, startingConfidence, stats);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
            }

            firstRunOfProgram = false;

            panel.removeAll();
            setupRace(trackConfigurations[0]);
        });

        panel.revalidate();
        panel.repaint();
    }
}
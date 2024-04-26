import javax.swing.*;
import java.awt.*;
import java.util.*;

public class BettingPanel extends JPanel {
    private Horse[] horses;

    private JFrame frame;

    private JLabel[] oddsHorseLabels;
    private JLabel[] oddsLabels;

    private SelectList horseSelectList;
    private JTextField amountField;
    private CustomButton betButton;

    private JLabel balanceLabel;
    private double balance;

    private ArrayList<HashMap<String, Object>> bets = new ArrayList<>();

    public BettingPanel(Horse[] horses, double balance, JFrame frame) {
        this.horses = horses;
        this.balance = balance;
        this.frame = frame;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        oddsHorseLabels = new JLabel[horses.length];
        oddsLabels = new JLabel[horses.length];


        JPanel betPanel = new JPanel(new GridLayout(0, 1, 0, 25));
        betPanel.setBorder(BorderFactory.createEmptyBorder(35, 25, 25, 25));
        add(betPanel, BorderLayout.CENTER);

        JPanel horsePanel = new JPanel(new BorderLayout(15, 0));
        horsePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 2));
        betPanel.add(horsePanel);

        JLabel horseLabel = new JLabel("Horse:");
        horseLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        horseLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        horsePanel.add(horseLabel, BorderLayout.WEST);

        String[] horseNames = new String[horses.length];

        for (int i = 0; i < horses.length; i++) {
            horseNames[i] = horses[i].getName();
        }

        horseSelectList = new SelectList(horseNames);
        horsePanel.add(horseSelectList, BorderLayout.CENTER);

        JPanel amountPanel = new JPanel(new BorderLayout(15, 0));
        amountPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 4));
        betPanel.add(amountPanel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        amountPanel.add(amountLabel, BorderLayout.WEST);

        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 16));
        amountPanel.add(amountField, BorderLayout.CENTER);

        betButton = new CustomButton("Place Bet");
        betPanel.add(betButton);


        JPanel oddsPanel = new JPanel();
        oddsPanel.setLayout(new BoxLayout(oddsPanel, BoxLayout.Y_AXIS));
        add(oddsPanel, BorderLayout.EAST);

        balanceLabel = new JLabel("");
        setBalance(balance);
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        balanceLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        oddsPanel.add(balanceLabel);

        JLabel oddsLabel = new JLabel("Betting Odds");
        oddsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        oddsLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        oddsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));
        oddsPanel.add(oddsLabel);

        for (int i = 0; i < horses.length; i++) {
            oddsHorseLabels[i] = new JLabel(horses[i].getName());
            oddsHorseLabels[i].setFont(new Font("Arial", Font.PLAIN, 16));
            oddsHorseLabels[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);

            oddsLabels[i] = new JLabel("0.0");
            oddsLabels[i].setFont(new Font("Arial", Font.PLAIN, 14));
            oddsLabels[i].setAlignmentX(JLabel.CENTER_ALIGNMENT);
            oddsLabels[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

            oddsPanel.add(oddsHorseLabels[i]);
            oddsPanel.add(oddsLabels[i]);
        }


        betButton.addActionListener(e -> {
            int selectedHorseIndex = horseSelectList.getSelectedIndex();

            double amount = 0.0;

            try {
                amount = Double.parseDouble(amountField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (amount > balance) {
                JOptionPane.showMessageDialog(frame, "You do not have enough balance to place this bet.", "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (amount <= 0) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (!isValidAmount(amount)) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount with no more than 2 decimal points.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                return;
            }

            placeBet(amount, selectedHorseIndex);

            amountField.setText("");

            revalidate();
            repaint();
        });

        revalidate();
        repaint();
    }

    public void placeBet(double amount, int horseIndex) {
        HashMap<String, Object> bet = new HashMap<>();
        bet.put("amount", amount);
        bet.put("horse", horses[horseIndex]);
        bet.put("odds", Double.parseDouble(oddsLabels[horseIndex].getText()));

        setBalance(this.balance - amount);

        String formattedAmount = String.format("%.2f", amount);

        JOptionPane.showMessageDialog(frame, "You have placed a bet of $" + formattedAmount + " on " + horses[horseIndex].getName() + ".", "Bet Placed", JOptionPane.INFORMATION_MESSAGE);

        bets.add(bet);
    }

    public void evaluateBets(Race race) {
        for (HashMap<String, Object> bet : bets) {
            double amount = (double) bet.get("amount");
            Horse horse = (Horse) bet.get("horse");
            double odds = (double) bet.get("odds");

            if (horse.getDistanceTravelled() == race.getRaceLength()) {
                double winnings = Math.round(amount * (1.0 / odds) * 100.0) / 100.0;

                setBalance(this.balance + winnings);

                String formattedWinnings = String.format("%.2f", winnings);

                JOptionPane.showMessageDialog(frame, "You have won $" + formattedWinnings + " on " + horse.getName() + ".", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String formattedLoss = String.format("%.2f", amount);

                JOptionPane.showMessageDialog(frame, "You have lost $" + formattedLoss + " on " + horse.getName() + ".", "Better Luck Next Time", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        bets.clear();
    }

    public void updateOdds(Race race) {
        double[] odds = race.calculateBettingOdds();

        for (int i = 0; i < horses.length; i++) {
            double roundedOdds = Math.round(odds[i] * 10000.0) / 10000.0;
            oddsLabels[i].setText(String.valueOf(roundedOdds));
        }

        revalidate();
        repaint();
    }

    private void setBalance(double balance) {
        this.balance = balance;

        String formattedBalance = String.format("%.2f", this.balance);

        balanceLabel.setText("Balance: $" + formattedBalance);

        revalidate();
        repaint();
    }

    public double getBalance() {
        return balance;
    }

    public void disableBetting() {
        horseSelectList.setEnabled(false);
        amountField.setEnabled(false);
        betButton.setEnabled(false);
    }

    public void enableBetting() {
        horseSelectList.setEnabled(true);
        amountField.setEnabled(true);
        betButton.setEnabled(true);
    }

    private boolean isValidAmount(double amount) {
        String amountString = String.valueOf(amount);
        int decimalIndex = amountString.indexOf(".");
        if (decimalIndex != -1 && amountString.length() - decimalIndex > 3) {
            return false;
        }
        return true;
    }
}
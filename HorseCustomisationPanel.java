import javax.swing.*;
import java.awt.*;

public class HorseCustomisationPanel extends JPanel {
    private JTextField nameField;
    private SelectList colourList;
    private SelectList saddleList;

    public HorseCustomisationPanel(int horseNumber, String defaultName, String defaultColour, String defaultSaddle) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JLabel titleLabel = new JLabel("Horse " + horseNumber);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(0, 3));
        add(contentPanel, BorderLayout.CENTER);

        JPanel field1Panel = new JPanel();
        contentPanel.add(field1Panel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        field1Panel.add(nameLabel);

        nameField = new JTextField(defaultName, 10);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        field1Panel.add(nameField);

        JPanel field2Panel = new JPanel();
        contentPanel.add(field2Panel);

        JLabel colourLabel = new JLabel("Colour:");
        colourLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        field2Panel.add(colourLabel);

        colourList = new SelectList(new String[] {"Orange", "Black", "White"});
        colourList.setSelectedItem(defaultColour);
        field2Panel.add(colourList);

        JPanel field3Panel = new JPanel();
        contentPanel.add(field3Panel);

        JLabel saddleLabel = new JLabel("Saddle:");
        saddleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        field3Panel.add(saddleLabel);

        saddleList = new SelectList(new String[] {"No Saddle", "Red Saddle", "Blue Saddle"});
        saddleList.setSelectedItem(defaultSaddle);
        field3Panel.add(saddleList);
    }

    public String getHorseName() {
        return nameField.getText();
    }

    public String getHorseAppearanceString() {
        String colour = (String) colourList.getSelectedItem();
        String saddle = (String) saddleList.getSelectedItem();
        String horseAppearanceString;

        if (colour.equals("Black")) {
            horseAppearanceString = "_black";
        } else if (colour.equals("White")) {
            horseAppearanceString = "_white";
        } else {
            horseAppearanceString = "_orange";
        }

        if (saddle.equals("Red Saddle")) {
            horseAppearanceString += "_red_saddle";
        } else if (saddle.equals("Blue Saddle")) {
            horseAppearanceString += "_blue_saddle";
        }

        return horseAppearanceString;
    }

    public String getHorseColour() {
        return (String) colourList.getSelectedItem();
    }

    public String getHorseSaddle() {
        return (String) saddleList.getSelectedItem();
    }
}

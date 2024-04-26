import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private JPanel contentPanel;
    private CustomButton[] buttons;

    public MenuPanel(String title, boolean twoButtons) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(25, 25, 10, 25));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        if (twoButtons) {
            buttons = new CustomButton[2];

            JPanel buttonPanel = new JPanel(new GridLayout(0, 2));
            add(buttonPanel, BorderLayout.SOUTH);

            buttons[0] = new CustomButton("Back");
            buttonPanel.add(buttons[0]);

            buttons[1] = new CustomButton("Next");
            buttonPanel.add(buttons[1]);
        } else {
            buttons = new CustomButton[1];
            buttons[0] = new CustomButton("Next");
            add(buttons[0], BorderLayout.SOUTH);
        }
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public CustomButton[] getButtons() {
        return buttons;
    }
}

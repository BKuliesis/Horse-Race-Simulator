import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.PLAIN, 16));
        setFocusPainted(false);
    }
}

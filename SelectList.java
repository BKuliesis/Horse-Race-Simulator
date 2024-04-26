import javax.swing.*;
import java.awt.*;

public class SelectList extends JComboBox<String> {
    public SelectList(String[] items) {
        super(items);
        setFont(new Font("Arial", Font.PLAIN, 16));
        setFocusable(false);
    }   
}

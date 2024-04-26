import javax.swing.*;
import java.awt.*;

class LanePanel extends JPanel {
    private Horse horse;
    private int raceLength;

    public LanePanel(Horse horse, int raceLength) {
        this.horse = horse;
        this.raceLength = raceLength; 
    }

    public void updatePosition() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int lineWidth = 3;
        int startLineX = horse.getImage().getWidth() + lineWidth;
        int finishLineX = getWidth() - lineWidth;

        g.setColor(Color.BLACK);
        g.fillRect(startLineX, 0, lineWidth, getHeight());
        g.fillRect(finishLineX, 0, lineWidth, getHeight());

        g2d.rotate(Math.PI / 2);
        g2d.drawString("Start", getHeight() / 2 - 13, -startLineX + 20);
        g2d.drawString("Finish", getHeight() / 2 - 17, -finishLineX + 20);

        g2d.dispose();

        int horseX = (int) (((double) horse.getDistanceTravelled() / (double) (raceLength)) * (getWidth() - horse.getImage().getWidth() - lineWidth));
        int horseY = getHeight() / 2 - horse.getImage().getHeight() / 2;

        if (horse.hasFallen()) {
            horseX += 5;
            horseY += 5;
            g.drawImage(horse.getFallenImage(), horseX, horseY, this);
        } else {
            g.drawImage(horse.getImage(), horseX, horseY, this);
        }      
    }
}
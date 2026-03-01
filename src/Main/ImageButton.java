package Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.InputStream;

public class ImageButton extends JButton {
    private Image backgroundImage;

    public ImageButton(String text) {
        super(text);
        try {
            InputStream is = getClass().getResourceAsStream("/Buttons/scoreboard.png");
            this.backgroundImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        super.paintComponent(g);
    }
}

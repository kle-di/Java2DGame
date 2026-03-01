package Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Main extends JFrame {

    private Image backgroundImage;
    private Font customFont;

    public Main() {
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1584, 912);
        setLocationRelativeTo(null);

        try {
            InputStream is = getClass().getResourceAsStream("/Environment/paper.jpg");
            backgroundImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream fontStream = getClass().getResourceAsStream("/Fonts/upheavtt.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(32f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 24);
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setLayout(null);
        JButton playButton = new ImageButton("Play Game");
        playButton.setFont(customFont);
        playButton.setBounds(667, 250, 250, 60);
        playButton.setForeground(Color.white);

        playButton.addActionListener(e -> {
            dispose();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setTitle("Game");

            GameFrame gameFrame = new GameFrame();
            frame.add(gameFrame);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            gameFrame.startGameThread();
        });

        JButton quitButton = new ImageButton("Quit Game");
        quitButton.setFont(customFont);
        quitButton.setBounds(667, 550, 250, 60);
        quitButton.setForeground(Color.white);
        quitButton.addActionListener(e -> System.exit(0));

        JButton loginButton = new ImageButton("Login");
        loginButton.setFont(customFont);
        loginButton.setBounds(667, 350, 250, 60);
        loginButton.setForeground(Color.white);
        loginButton.addActionListener(e -> {
            dispose();
            new LoginMenu();
        });

        JButton logoutButton = new ImageButton("Logout");
        logoutButton.setFont(customFont);
        logoutButton.setBounds(667, 350, 250, 60);
        logoutButton.setForeground(Color.white);
        logoutButton.addActionListener(e -> {
            Session.setLoggedIn(false);
            dispose();
            new Main().setVisible(true);
        });

        JButton registerButton = new ImageButton("Register");
        registerButton.setFont(customFont);
        registerButton.setBounds(667, 450, 250, 60);
        registerButton.setForeground(Color.white);
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterMenu();
        });

        JButton loadButton = new ImageButton("Load");
        loadButton.setFont(customFont);
        loadButton.setBounds(667, 450, 250, 60);
        loadButton.setForeground(Color.white);
        loadButton.addActionListener(e -> {
            dispose();
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setTitle("Terraria");

            GameFrame gameFrame = new GameFrame();
            gameFrame.loadLabyrinth();
            gameFrame.loadPlayer();
            frame.add(gameFrame);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            gameFrame.startGameThread();

        });

        if(Session.isLoggedIn()) {
            panel.add(logoutButton);
            panel.add(playButton);
            panel.add(loadButton);
        }
        if(!Session.isLoggedIn()) {
            panel.add(loginButton);
            panel.add(registerButton);
        }

        panel.add(quitButton);
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main menu = new Main();
            menu.setVisible(true);
        });
    }

}

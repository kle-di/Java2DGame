package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginMenu extends JFrame {
    private Image backgroundImage;
    private Font customFont;
    String url = "jdbc:mysql://127.0.0.1:3306/login_schema";
    String dbUsername = "root";
    String dbPassword = "fletorja09";

    public LoginMenu() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1584, 912);
        setLocationRelativeTo(null);

        try {
            InputStream is = getClass().getResourceAsStream("/Environment/paper1.jpg");
            backgroundImage = ImageIO.read(is);
            InputStream fontStream = getClass().getResourceAsStream("/Fonts/upheavtt.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(32f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
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

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(customFont);
        usernameLabel.setForeground(Color.white);
        usernameLabel.setBounds(550, 300, 200, 40);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Calibri",Font.BOLD, 18));
        usernameField.setBounds(750, 300, 250, 40);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(customFont);
        passwordLabel.setForeground(Color.white);
        passwordLabel.setBounds(550, 400, 300, 40);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Calibri",Font.BOLD, 12));
        passwordField.setBounds(750, 400, 250, 40);

        JButton submitButton = new ImageButton("Login");
        submitButton.setFont(customFont);
        submitButton.setBounds(567, 650, 250, 60);
        submitButton.setForeground(Color.white);

        JButton backButton = new ImageButton("Back");
        backButton.setFont(customFont);
        backButton.setBounds(867, 650, 150, 60); // Positioned next to Register Button
        backButton.setForeground(Color.white);
        backButton.addActionListener(e -> {
            dispose();
            new Main().setVisible(true);
        });

        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Session.setLoggedIn(true);
                Session.setUsername(username);
                dispose();
                new Main().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(backButton);
        panel.add(submitButton);

        add(panel);
        setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        boolean isValid = false;

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            isValid = resultSet.next(); 

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return isValid;
    }
}

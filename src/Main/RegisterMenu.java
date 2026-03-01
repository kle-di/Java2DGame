package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class RegisterMenu extends JFrame {
    private Image backgroundImage;
    private Font customFont;

    String url = "jdbc:mysql://127.0.0.1:3306/login_schema";
    String dbUsername = "root";
    String dbPassword = "fletorja09";

    public RegisterMenu() {
        setTitle("Register");
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
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(customFont);
        usernameLabel.setForeground(Color.white);
        usernameLabel.setBounds(550, 300, 200, 40);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Calibri", Font.BOLD, 18));
        usernameField.setBounds(750, 300, 250, 40);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(customFont);
        passwordLabel.setForeground(Color.white);
        passwordLabel.setBounds(550, 400, 300, 40);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Calibri", Font.BOLD, 12));
        passwordField.setBounds(750, 400, 250, 40);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(customFont);
        confirmPasswordLabel.setForeground(Color.white);
        confirmPasswordLabel.setBounds(400, 500, 400, 40);

        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Calibri", Font.BOLD, 12));
        confirmPasswordField.setBounds(750, 500, 250, 40);

        JButton submitButton = new ImageButton("Register");
        submitButton.setFont(customFont);
        submitButton.setBounds(567, 650, 250, 60);
        submitButton.setForeground(Color.white);


        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            }else if(password.length()<8){
                JOptionPane.showMessageDialog(null, "Password must be at least 8 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(checkUsername(username)) {
                JOptionPane.showMessageDialog(null, "Username is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                saveUserToDatabase(username, password);
                JOptionPane.showMessageDialog(null, "Registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton backButton = new ImageButton("Back");
        backButton.setFont(customFont);
        backButton.setBounds(867, 650, 150, 60);
        backButton.setForeground(Color.white);
        backButton.addActionListener(e -> {
            dispose();
            new Main().setVisible(true);
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(submitButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }

    private boolean checkUsername(String username){
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean saveUserToDatabase(String username, String password) {

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


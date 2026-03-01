package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class GameFrame extends JPanel implements Runnable {
    final int maxScreenCol = 33;
    final int maxScreenRow = 19;

    final int screenWidth = maxScreenCol * 48;
    final int screenHeight = maxScreenRow * 48;

    TileManager tileManager = new TileManager(this);
    Keyboard keyboard = new Keyboard();
    Thread gameThread;
    Player player = new Player(this,keyboard);
    Labyrinth labyrinth = new Labyrinth(maxScreenRow,maxScreenCol);
    int treasureGenerated = labyrinth.getTreasureGenerated();
    int grid[][]= labyrinth.getGrid();
    public CollisionChecker checker = new CollisionChecker(this);
    boolean gameEnd = false;
    boolean gameWon = false;
    boolean gameLost = false;
    boolean gamePaused = keyboard.escapePressed;

    int FPS=60;

    private Image scoreBorder;
    private Image coin;
    private Font customFont;
    private Font customFont2;

    private JButton playAgainButton;
    private JButton mainMenuButton;
    private JButton saveButton;
    private volatile boolean running = false;



    public GameFrame() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.GREEN);
        this.addKeyListener(keyboard);
        this.setFocusable(true);
        preloadResources();
        setupPlayAgainButton();
        setupMainMenuButton();
        setupSaveButton();
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void setupPlayAgainButton() {
        playAgainButton = new ImageButton("Play Again");

        int buttonWidth = 250;
        int buttonHeight = 50;
        int xPos = (screenWidth - buttonWidth) / 2;
        int yPos = (screenHeight / 2) + (screenHeight / 4) - (buttonHeight / 2);

        playAgainButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);
        playAgainButton.setFont(customFont);
        playAgainButton.setForeground(Color.WHITE);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(e -> {
            resetGame();
            mainMenuButton.setVisible(false);
        });

        this.setLayout(null);
        this.add(playAgainButton);
    }

    private void setupSaveButton() {
        saveButton = new ImageButton("Save");

        int buttonWidth = 250;
        int buttonHeight = 50;
        int xPos = (screenWidth - buttonWidth) / 2;
        int yPos = (screenHeight / 2) + (screenHeight / 4) - (buttonHeight / 2);

        saveButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);
        saveButton.setFont(customFont);
        saveButton.setForeground(Color.WHITE);
        saveButton.setVisible(false);
        saveButton.addActionListener(e -> {
            savePlayer();
            saveLabyrinth();
            JOptionPane.showMessageDialog(null, "Game Saved", "Finished", JOptionPane.INFORMATION_MESSAGE);
        });

        this.setLayout(null);
        this.add(saveButton);
    }

    public void loadLabyrinth() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/login_schema",
                "root",
                "fletorja09"
        )) {
            int id = getUserId(Session.user_name);
            String query = "SELECT labrow, labcol, labtile FROM labyrinthdata WHERE idlabyrinthdata = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    int maxRow = maxScreenRow;
                    int maxCol = maxScreenCol;

                    grid = new int[maxRow][maxCol];

                    while (resultSet.next()) {
                        int row = resultSet.getInt("labrow");
                        int col = resultSet.getInt("labcol");
                        int tile = resultSet.getInt("labtile");

                        grid[row][col] = tile;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveLabyrinth() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/login_schema",
                "root",
                "fletorja09"
        )) {
            int id = getUserId(Session.user_name);
            StringBuilder insertQuery = new StringBuilder("INSERT INTO labyrinthdata (idlabyrinthdata, labrow, labcol, labtile) VALUES ");
            String deleteQuery = "DELETE FROM labyrinthdata WHERE idlabyrinthdata=" + id;

            Statement delete = connection.createStatement();
            delete.execute(deleteQuery);

            int batchSize = 650;
            int count = 0;

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (count > 0) {
                        insertQuery.append(", ");
                    }
                    insertQuery.append("(")
                            .append(id).append(", ")
                            .append(i).append(", ")
                            .append(j).append(", ")
                            .append(grid[i][j]).append(")");
                    count++;

                    if (count >= batchSize) {
                        try (PreparedStatement statement = connection.prepareStatement(insertQuery.toString())) {
                            statement.executeUpdate();
                        }
                        insertQuery.setLength(0);
                        insertQuery.append("INSERT INTO labyrinthdata (idlabyrinthdata, labrow, labcol, labtile) VALUES ");
                        count = 0;
                    }
                }
            }
            if (count > 0) {
                try (PreparedStatement statement = connection.prepareStatement(insertQuery.toString())) {
                    statement.executeUpdate();
                }
            }

        } catch (SQLException g) {
            g.printStackTrace();
        }
    }

    private void savePlayer(){
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/login_schema",
                "root",
                "fletorja09"
        )) {String insertQuery = "INSERT INTO playerinfo (idplayerinfo ,posX, posY, score, treasures) VALUES (?,?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)){

                int id = getUserId(Session.user_name);
                String deleteQuery = "DELETE FROM playerinfo WHERE idplayerinfo=" + id;
                Statement delete = connection.createStatement();
                delete.execute(deleteQuery);

                statement.setInt(1,id);
                statement.setInt(2,player.playerX);
                statement.setInt(3,player.playerY);
                statement.setInt(4,player.score);
                statement.setInt(5,player.treasureCounter);

                statement.executeUpdate();
            }

    }catch (SQLException g) {
            g.printStackTrace();
        }
    }

    public void loadPlayer() {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/login_schema",
                "root",
                "fletorja09"
        )) {
            String query = "SELECT posX, posY, score, treasures FROM playerinfo WHERE idplayerinfo = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                int id = getUserId(Session.user_name);
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        player.playerX = resultSet.getInt("posX");
                        player.playerY = resultSet.getInt("posY");
                        player.score = resultSet.getInt("score");
                        player.treasureCounter = resultSet.getInt("treasures");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getUserId(String username) {
        String selectQuery = "SELECT idusers FROM users WHERE username = ?";
        int userId = -1;  //

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/login_schema",
                "root",
                "fletorja09"
        )) {
            try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("idusers");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }


    private void setupMainMenuButton() {
        mainMenuButton = new ImageButton("Main Menu");

        int buttonWidth = 250;
        int buttonHeight = 50;

        int xPos = (screenWidth - buttonWidth) / 2;
        int yPos = (screenHeight / 2) + (screenHeight / 4) + (buttonHeight / 2) + 10;

        mainMenuButton.setBounds(xPos, yPos, buttonWidth, buttonHeight);

        mainMenuButton.setFont(customFont);
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setVisible(false);

        mainMenuButton.addActionListener(e -> {
            stopGameThread();
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(GameFrame.this);
            parentFrame.dispose();
            Main mainMenu = new Main();
            mainMenu.setVisible(true);
        });

        this.add(mainMenuButton);
    }


    private void resetGame() {

        stopGameThread();
        gameWon = false;
        gameLost = false;
        player.reset();
        labyrinth = new Labyrinth(maxScreenRow, maxScreenCol);
        grid = labyrinth.getGrid();
        treasureGenerated = labyrinth.getTreasureGenerated();
        gameEnd = false;

        playAgainButton.setVisible(false);
        startGameThread();
    }

    public void stopGameThread() {
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameThread = null;
    }


    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        running = true;
        while (running) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(!keyboard.escapePressed) {
            player.move();
        }
    }

    public void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        tileManager.drawTiles(g2d);
        player.spawn(g2d);
        displayScore(g2d);
        mainMenuButton.setVisible(false);
        saveButton.setVisible(false);
        if (gameEnd) {
            endScreen(g2d);
        }
        if(keyboard.escapePressed){
            g2d.setColor(new Color(0, 0, 0, 128));
            g2d.fillRect(0, 0, screenWidth, screenHeight);
            mainMenuButton.setVisible(true);
            saveButton.setVisible(true);
        }
    }

    public void endScreen(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 128));
        g2d.fillRect(0, 0, screenWidth, screenHeight);

        g2d.setFont(customFont2);
        g2d.setColor(Color.white);
        playAgainButton.setVisible(true);
        mainMenuButton.setVisible(true);

        if(gameWon) {
            String endMessage = "You Win!";
            FontMetrics metrics = g2d.getFontMetrics(customFont2);
            int xMessage = (screenWidth - metrics.stringWidth(endMessage)) / 2;
            int yMessage = screenHeight / 2 - metrics.getHeight();

            g2d.drawString(endMessage, xMessage, yMessage);

            String scoreMessage = "Score: " + player.score;
            FontMetrics scoreMetrics = g2d.getFontMetrics(customFont2);
            int xScore = (screenWidth - scoreMetrics.stringWidth(scoreMessage)) / 2;
            int yScore = yMessage + metrics.getHeight() + 10;

            g2d.drawString(scoreMessage, xScore, yScore);

        }else if(gameLost){
            String endMessage = "You Lost!";
            FontMetrics metrics = g2d.getFontMetrics(customFont2);
            int xMessage = (screenWidth - metrics.stringWidth(endMessage)) / 2;
            int yMessage = screenHeight / 2 - metrics.getHeight();

            g2d.drawString(endMessage, xMessage, yMessage);

            String endMessage2 = "You hit a wall";
            FontMetrics scoreMetrics = g2d.getFontMetrics(customFont2);
            int xMessage2 = (screenWidth - scoreMetrics.stringWidth(endMessage2)) / 2;
            int yMessage2 = yMessage + metrics.getHeight() + 10;

            g2d.drawString(endMessage2, xMessage2, yMessage2);
        }
    }

    public void displayScore(Graphics2D g2d) {
        g2d.drawImage(scoreBorder, (screenWidth - 144) / 2, 0, this);
        g2d.drawImage(coin, (screenWidth - 116) / 2, 16, this);
        g2d.setFont(customFont);
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString(Integer.toString(player.score), (screenWidth - 48) / 2, 32);
    }

    private void preloadResources() {
        try {
            InputStream is = getClass().getResourceAsStream("/Fonts/upheavtt.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(32f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 18);
        }
        try {
            InputStream is = getClass().getResourceAsStream("/Fonts/upheavtt.ttf");
            customFont2 = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(40f);
        } catch (Exception e) {
            e.printStackTrace();
            customFont2 = new Font("Arial", Font.BOLD, 18);
        }
        try {
            scoreBorder = ImageIO.read(getClass().getResourceAsStream("/Buttons/scoreboard.png"));
            coin = ImageIO.read(getClass().getResourceAsStream("/Environment/coin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
















package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {
    public int playerX, playerY;
    public int playerSpeed;
    public int treasureCounter;
    public int score;

    GameFrame frame;
    Keyboard keyboard;
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4;
    public BufferedImage iUp1, iUp2, iDown1, iDown2, iRight1, iRight2, iLeft1, iLeft2;
    public String direction;
    public int mode;
    public int modeCounter;
    public int idleMode;
    public int idleCounter;
    public boolean isIdle;

    public Rectangle hitbox = new Rectangle();
    public boolean collision = true;

    public Player(GameFrame frame, Keyboard keyboard) {
        playerX = 36;
        playerY = 16;
        playerSpeed = 3;
        this.frame = frame;
        this.keyboard = keyboard;
        getPlayerImage();
        direction = "down";
        mode = 0;
        modeCounter = 0;
        idleMode = 0;
        idleCounter = 0;
        isIdle = true;
        hitbox = new Rectangle(35, 50, 1, 1);
        treasureCounter = 0;
        score = 0;
    }

    public void reset() {
        playerX = 36;
        playerY = 16;
        playerSpeed = 3;
        score = 0;
        treasureCounter = 0;
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/sprites/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/sprites/up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/sprites/up3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/sprites/up4.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/sprites/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/sprites/down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/sprites/down3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/sprites/down4.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/sprites/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/sprites/right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/sprites/right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/sprites/right4.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/sprites/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/sprites/left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/sprites/left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/sprites/left4.png"));

            iUp1 = ImageIO.read(getClass().getResourceAsStream("/sprites/iUp1.png"));
            iUp2 = ImageIO.read(getClass().getResourceAsStream("/sprites/iUp2.png"));
            iDown1 = ImageIO.read(getClass().getResourceAsStream("/sprites/iDown1.png"));
            iDown2 = ImageIO.read(getClass().getResourceAsStream("/sprites/iDown2.png"));
            iRight1 = ImageIO.read(getClass().getResourceAsStream("/sprites/iRight1.png"));
            iRight2 = ImageIO.read(getClass().getResourceAsStream("/sprites/iRight2.png"));
            iLeft1 = ImageIO.read(getClass().getResourceAsStream("/sprites/iLeft1.png"));
            iLeft2 = ImageIO.read(getClass().getResourceAsStream("/sprites/iLeft2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void move() {
        if (frame.gameEnd) {
            return;
        }
        if(keyboard.shiftPressed){
            playerSpeed = 2;
        } else if (!keyboard.shiftPressed) {
            playerSpeed = 3;

        }
        if ((keyboard.upPressed || keyboard.downPressed || keyboard.leftPressed || keyboard.rightPressed)&& !frame.gamePaused) {
            isIdle = false; // Player is moving
            int previousX = playerX;
            int previousY = playerY;

            if (keyboard.upPressed) {
                direction = "up";
                playerY -= playerSpeed;
            }
            if (keyboard.downPressed) {
                direction = "down";
                playerY += playerSpeed;
            }
            if (keyboard.leftPressed) {
                direction = "left";
                playerX -= playerSpeed;
            }
            if (keyboard.rightPressed) {
                direction = "right";
                playerX += playerSpeed;
            }

            collision = false;
            frame.checker.checkTile(this);

            if (collision) {
                playerX = previousX;
                playerY = previousY;
            }

            modeCounter++;
            if (modeCounter > 10) {
                mode = (mode + 1) % 4;
                modeCounter = 0;
            }
        } else {
            isIdle = true;
        }

        if (isIdle) {
            idleCounter++;
            if (idleCounter > 30) {
                idleMode = (idleMode + 1) % 2;
                idleCounter = 0;
            }
        }
    }

    public void spawn(Graphics2D g2d) {
        BufferedImage image = null;

        if (isIdle) {
            // Display idle animation
            switch (direction) {
                case "up":
                    if(idleMode == 0) image = iUp1;
                    else image = iUp2;
                    break;
                case "down":
                    if(idleMode == 0) image = iDown1;
                    else image = iDown2;
                    break;
                case "left":
                    if(idleMode == 0) image = iLeft1;
                    else image = iLeft2;
                    break;
                case "right":
                    if(idleMode == 0) image = iRight1;
                    else image = iRight2;
                    break;
            }
        } else {
            switch (direction) {
                case "up":
                    if (mode == 0) image = up1;
                    else if (mode == 1) image = up2;
                    else if (mode == 2) image = up3;
                    else if (mode == 3) image = up4;
                    break;
                case "down":
                    if (mode == 0) image = down1;
                    else if (mode == 1) image = down2;
                    else if (mode == 2) image = down3;
                    else if (mode == 3) image = down4;
                    break;
                case "left":
                    if (mode == 0) image = left1;
                    else if (mode == 1) image = left2;
                    else if (mode == 2) image = left3;
                    else if (mode == 3) image = left4;
                    break;
                case "right":
                    if (mode == 0) image = right1;
                    else if (mode == 1) image = right2;
                    else if (mode == 2) image = right3;
                    else if (mode == 3) image = right4;
                    break;
            }
        }

        g2d.drawImage(image, playerX, playerY, 74, 74, null);
        //g2d.setColor(Color.RED);
        //g2d.drawRect(playerX + hitbox.x, playerY + hitbox.y, hitbox.width, hitbox.height);
    }
}


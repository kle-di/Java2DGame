package Main;

public class CollisionChecker {
    GameFrame frame;

    public CollisionChecker(GameFrame frame) {
        this.frame = frame;
    }

    public void checkTile(Player player) {
        int playerLeftWorldX = player.playerX + player.hitbox.x;
        int playerRightWorldX = player.playerX + player.hitbox.x + player.hitbox.width;
        int playerTopWorldY = player.playerY + player.hitbox.y;
        int playerBottomWorldY = player.playerY + player.hitbox.y + player.hitbox.height;

        int playerLeftCol = playerLeftWorldX / frame.tileManager.tileSize;
        int playerRightCol = playerRightWorldX / frame.tileManager.tileSize;
        int playerTopRow = playerTopWorldY / frame.tileManager.tileSize;
        int playerBottomRow = playerBottomWorldY / frame.tileManager.tileSize;

        int tileNum1, tileNum2;
        switch (player.direction) {
            case "up":
                playerTopRow = (playerTopWorldY - player.playerSpeed) / frame.tileManager.tileSize;
                tileNum1 = frame.grid[playerTopRow][playerLeftCol];
                tileNum2 = frame.grid[playerTopRow][playerRightCol];
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    player.collision = true;
                }
                break;
            case "down":
                playerBottomRow = (playerBottomWorldY + player.playerSpeed) / frame.tileManager.tileSize;
                tileNum1 = frame.grid[playerBottomRow][playerLeftCol];
                tileNum2 = frame.grid[playerBottomRow][playerRightCol];
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    player.collision = true;
                }
                break;
            case "left":
                playerLeftCol = (playerLeftWorldX - player.playerSpeed) / frame.tileManager.tileSize;
                tileNum1 = frame.grid[playerTopRow][playerLeftCol];
                tileNum2 = frame.grid[playerBottomRow][playerLeftCol];
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    player.collision = true;
                }
                break;
            case "right":
                playerRightCol = (playerRightWorldX + player.playerSpeed) / frame.tileManager.tileSize;
                tileNum1 = frame.grid[playerTopRow][playerRightCol];
                tileNum2 = frame.grid[playerBottomRow][playerRightCol];
                if (isCollisionTile(tileNum1) || isCollisionTile(tileNum2)) {
                    player.collision = true;
                }
                break;
        }

        if (player.collision) {
            frame.gameLost = true;
            frame.gameEnd = true;
        }
        checkTreasure(player);
        checkEnd(player);
    }

    private boolean isCollisionTile(int tileNum) {
        return tileNum == Labyrinth.WALL;
    }

    public void checkTreasure(Player player) {
        int playerCenterX = player.playerX + player.hitbox.x + player.hitbox.width / 2;
        int playerCenterY = player.playerY + player.hitbox.y + player.hitbox.height / 2;

        int tileSize = frame.tileManager.tileSize;

        int col = playerCenterX / tileSize;
        int row = playerCenterY / tileSize;

        if (frame.grid[row][col] == Labyrinth.TRREASURE1) {
            frame.grid[row][col] = Labyrinth.PATH;

            player.treasureCounter++;
            player.score += 100;
        } else if (frame.grid[row][col] == Labyrinth.TRREASURE2) {
            frame.grid[row][col] = Labyrinth.PATH;

            player.treasureCounter++;
            player.score += 50;
        } else if (frame.grid[row][col] == Labyrinth.TRREASURE3) {
            frame.grid[row][col] = Labyrinth.PATH;

            player.treasureCounter++;
            player.score += 25;
        }
    }

    public void checkEnd(Player player) {
        int playerCenterX = player.playerX + player.hitbox.x + player.hitbox.width / 2;
        int playerCenterY = player.playerY + player.hitbox.y + player.hitbox.height / 2;

        int tileSize = frame.tileManager.tileSize;

        int col = playerCenterX / tileSize;
        int row = playerCenterY / tileSize;

        if (frame.grid[row][col] == Labyrinth.END) {
            frame.gameEnd = true;
            frame.gameWon = true;
        }
    }
}


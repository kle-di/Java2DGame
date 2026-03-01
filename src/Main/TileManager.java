package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class TileManager {
    GameFrame frame;
    Tile[] tile;
    public final int ogTileSize = 16;
    public final int scale = 3;

    public final int tileSize = ogTileSize * scale;

    public TileManager(GameFrame frame) {
        this.frame = frame;
        tile = new Tile[6];
        getTileImage();
    }
    public void getTileImage(){
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Environment/Grass_Middle.png"));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Environment/Tree.jpg"));
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Environment/treasure.jpg"));
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Environment/treasure1.jpg"));
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Environment/treasure2.jpg"));
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/Environment/End.png"));

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    public void drawTiles(Graphics g2d){
        int grid[][]=frame.grid;
        for (int row = 0; row < frame.maxScreenRow; row++) {
            for (int col = 0; col < frame.maxScreenCol; col++) {
                if ((grid[row][col] == 1 || row == 0 || row == frame.maxScreenRow - 1 || col == 0 || col == frame.maxScreenCol - 1) && grid[row][col] != 6) {
                    g2d.drawImage(tile[1].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                } else if (grid[row][col] == 0||grid[row][col]==5) {
                    g2d.drawImage(tile[0].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                } else if (grid[row][col] == 2) {
                    g2d.drawImage(tile[2].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                } else if (grid[row][col] == 3) {
                    g2d.drawImage(tile[3].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                } else if (grid[row][col] == 4) {
                    g2d.drawImage(tile[4].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                } else if (grid[row][col] == 6) {
                    g2d.drawImage(tile[5].image, col * tileSize, row * tileSize, tileSize, tileSize, null);
                }
            }
        }

    }
}

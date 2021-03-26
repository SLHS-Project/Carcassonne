/*
 * TODO
 * -Calculate score for each player
 * 		-Take a look at "Foreseeable problems" section of Project Plan
 * -Holds array/list of tiles -- Done
 * -Make board image by compositing tiles -- Done
 */

/*
    85x85
    middle  43
    *NOTE*
    Rotation is handled at Tile itself.
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CarcassonneMap {
    CarcassonneTile[][] map;

    public CarcassonneMap() {
        // 0, 0 is left, top
        this.map = new CarcassonneTile[85][85];
        // TODO change this, this mess below is simple start tile for test purposes.
        BufferedImage timg = null;
        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/37.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.map[43][43] = new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.River, TerrainType.Farm)  //E
        }, timg);
    }


    // Scoring
    public ArrayList<Orient> getConflicts(CarcassonneTile t, int x, int y) {
        // Check if it's possible to add tile t at x
        // Check according to tile T rotation
        ArrayList<Orient> ret = new ArrayList<Orient>();
        if(this.map[x][y] != null) return ret;

        if(this.map[x][y-1] != null && !this.map[x][y-1].fit(t, Orient.S)) ret.add(Orient.N); // up
        if(this.map[x][y+1] != null && !this.map[x][y+1].fit(t, Orient.N)) ret.add(Orient.S); // down
        if(this.map[x-1][y] != null && !this.map[x-1][y].fit(t, Orient.E)) ret.add(Orient.W); // left
        if(this.map[x+1][y] != null && !this.map[x+1][y].fit(t, Orient.W)) ret.add(Orient.E); // right

        return ret;
    }

    public boolean canAddAt(CarcassonneTile t, int x, int y) {
        if(this.map[x][y] != null) return false;
        return this.getConflicts(t, x, y).isEmpty();
    }

    public boolean tryAddAt(CarcassonneTile t, int x, int y) {
        if(!this.canAddAt(t, x, y)) return false;
        this.map[x][y] = t;
        return true;
    }

    class Coordinate {
        int x, y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int x() {
            return this.x;
        }
        public int y() {
            return this.y;
        }
    }

    // Get all coordinates of all possible location for placing tile
    public ArrayList<Coordinate> getPossibleLocations() {
        ArrayList<Coordinate> ret = new ArrayList<Coordinate>();

        Boundary bb = this.getBoundary();

        for(int x = bb.x1; x <= bb.x2; x++) {
            for (int y = bb.y1; y <= bb.y2; y++) {
                if(this.map[x][y] == null && (
                        this.map[x-1][y] != null ||
                        this.map[x+1][y] != null ||
                        this.map[x][y-1] != null ||
                        this.map[x][y+1] != null
                        )) ret.add(new Coordinate(x, y));
            }
        }
        return ret;
    }

    public int calcScore() {
        // Calculate score
        return -1;
    }

    // Rendering
    class Boundary {
        int x1, x2, y1, y2;
        public Boundary(int x1, int x2, int y1, int y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }
    }
    public Boundary getBoundary() {
        // x min
        int xmin = 43;
        for(int i = 43; i >= 0; i--) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[i][j] != null)
                    tile = true;
            if(!tile) break;
            xmin = i;
        }
        // x max
        int xmax = 43;
        for(int i = 43; i < 85; i++) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[i][j] != null)
                    tile = true;
            if(!tile) break;
            xmax = i;
        }
        // y min
        int ymin = 43;
        for(int i = 43; i >= 0; i--) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[j][i] != null)
                    tile = true;
            if(!tile) break;
            ymin = i;
        }
        // y max
        int ymax = 43;
        for(int i = 43; i < 85; i++) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[j][i] != null)
                    tile = true;
            if(!tile) break;
            ymax = i;
        }
        return new Boundary(xmin - 1, xmax + 1, ymin - 1, ymax + 1);
    }
    public BufferedImage render() {
        // get maximum/minimum x and y value
        Boundary bb = this.getBoundary();
        int width = bb.x2 - bb.x1 + 1;
        int height = bb.y2 - bb.y1 + 1;
        // composite
        // 75 by 75 normally
        BufferedImage r = new BufferedImage(width * 75, height * 75, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = r.createGraphics();
        for(int x = bb.x1; x <= bb.x2; x++) {
            for (int y = bb.y1; y <= bb.y2; y++) {
                System.out.println(x + " " + y);
                if(this.map[x][y] == null) continue;
                g2d.drawImage(this.map[x][y].getImage(), (x - bb.x1) * 75, (y - bb.y1) * 75, null);
            }
        }
        ArrayList<Coordinate> poss = this.getPossibleLocations();
        for(Coordinate coord: poss) {
           g2d.setPaint ( new Color ( 20,20,20 ) );
           // TODO Replace this black rectangle with proper placeholder.
           g2d.fillRect( (coord.x() - bb.x1) * 75, (coord.y() - bb.y1) * 75, 75, 75);
        }
        g2d.dispose();
        return r;
    }

    public static void main(String[] args) {
        // Following is a test data. These does not have effect to the main program
        CarcassonneMap map = new CarcassonneMap();
        System.out.println(map.getConflicts(new CarcassonneTile(new Side[] {
                new Side(TerrainType.City, TerrainType.City, TerrainType.Farm), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Road, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Road, TerrainType.Farm)  //E
        }), 44, 43));
        map.getBoundary();

        BufferedImage timg = null;
        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/8.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.tryAddAt(new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
        }, timg), 44, 44);

        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.tryAddAt(new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
        }, timg), 42, 43);

        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/42.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.tryAddAt(new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
        }, timg), 44, 43);
        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.tryAddAt(new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
        }, timg), 42, 44);
        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/40.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.tryAddAt(new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
        }, timg), 42, 42);

        try {
            File outputfile = new File("C:\\Users\\k1702639\\Desktop\\a\\map.png");
            ImageIO.write(map.render(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

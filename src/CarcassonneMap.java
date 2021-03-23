/*
 * TODO
 * -Calculate score for each player
 * 		-Take a look at "Foreseeable problems" section of Project Plan
 * -Holds array/list of tiles
 * -Make board image by compositing tiles
 */

/*
    85x85
    middle  43
    *NOTE*
    Rotation is handled at Tile itself.
 */

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CarcassonneMap {
    CarcassonneTile[][] map;

    public CarcassonneMap() {
        // 0, 0 is left, top
        this.map = new CarcassonneTile[85][85];
        // TODO change this, this mess below is simple start tile for test purposes.
        this.map[43][43] = new CarcassonneTile(new Side[] {
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.River, TerrainType.Farm)  //E
        });
    }

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
        return this.getConflicts(t, x, y).isEmpty();
    }

    public int calcScore() {
        // Calculate score
        return -1;
    }

    public BufferedImage render() {
        // get maximum/minimum x and y value
        // composite
        return null;
    }

    public static void main(String[] args) {
        CarcassonneMap map = new CarcassonneMap();
        System.out.println(map.getConflicts(new CarcassonneTile(new Side[] {
                new Side(TerrainType.City, TerrainType.City, TerrainType.Farm), //N
                new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
                new Side(TerrainType.Farm, TerrainType.Road, TerrainType.Farm), //S
                new Side(TerrainType.Farm, TerrainType.Road, TerrainType.Farm)  //E
        }), 44, 43));
    }
}

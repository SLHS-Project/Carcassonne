import tile.CarcassonneTile;
import tile.Orient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/*
 * TODO
 * -Holdes defined tiles for each id
 * 		*Use static variable for easy access
 */
public class Resources {
    HashMap<Integer, CarcassonneTile> tiles;

    public Resources(String tilelist) {
        TileParser parser = new TileParser();
        this.tiles = parser.loadTiles(tilelist);
    }

    public CarcassonneTile[] getRiverTiles() {
        ArrayList<CarcassonneTile> ret = new ArrayList<>();
        ret.add(this.tiles.get(37));
        ret.add(this.tiles.get(38));
        ret.add(this.tiles.get(40));
        ret.add(this.tiles.get(49));
        ret.add(this.tiles.get(50));
        ret.add(this.tiles.get(51));
        ret.add(this.tiles.get(52));
        ret.add(this.tiles.get(61));
        ret.add(this.tiles.get(73));
        ret.add(this.tiles.get(74));
        ret.add(this.tiles.get(75));
        return ret.toArray(new CarcassonneTile[ret.size()]);
    }

    //excluding river tiles
    public HashMap<Integer, CarcassonneTile> getTiles() {
        return this.tiles;
    }
}

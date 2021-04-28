import tile.CarcassonneTile;
import tile.Orient;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/*

 * TODO

 * -Holdes defined tiles for each id

 * 		*Use static variable for easy access

 */

public class Resources {
    public BufferedImage placeholder;
    public HashMap<Integer, CarcassonneTile> tiles;

    public Resources(String tilelist) {
        TileParser parser = new TileParser();
        this.tiles = parser.loadTiles(tilelist);
        this.placeholder= null;
        try {
            this.placeholder= ImageIO.read(CarcassonneTile.class.getResourceAsStream("/res/placeholder.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<CarcassonneTile> getRiverTiles() {
        ArrayList<CarcassonneTile> ret = new ArrayList<>();
        ret.add(this.tiles.get(37));
        ret.add(this.tiles.get(38));
        ret.add(this.tiles.get(39));
        ret.add(this.tiles.get(40));
        ret.add(this.tiles.get(49));
        ret.add(this.tiles.get(50));
        ret.add(this.tiles.get(51));
        ret.add(this.tiles.get(52));
        ret.add(this.tiles.get(61));
        ret.add(this.tiles.get(73));
        ret.add(this.tiles.get(74));
        ret.add(this.tiles.get(75));

        return ret;
    }

    //excluding river tiles

    public HashMap<Integer, CarcassonneTile> getTiles() {
        return this.tiles;
    }
}

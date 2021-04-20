import tile.*;

import javax.imageio.ImageIO;

import static tile.Orient.*;
import static tile.TerrainType.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * example format: ( Full list at google docs )
 * 37 [river: E] [city: ] [road: ] [monastery: ]
*/

public class TileParser {
    private Orient orientFromString(String o) {
        switch(o.toUpperCase()) {
            case "N": return N;
            case "W": return W;
            case "S": return S;
            case "E": return E;
            default: return null;
        }
    }
    private Orient[] getOrient(String l) {
        ArrayList<Orient> ret = new ArrayList<Orient>();
        String[] all = l.split("");
        for(String e: all) {
            Orient o = orientFromString(e);
            if (ret.indexOf(o) == -1)
                ret.add(o);
        }

        return ret.toArray(new Orient[ret.size()]);
    }
    private TerrainType typeFromString(String s) {
        switch(s) {
            case "city": return City;
            case "river": return River;
            case "road": return Road;
            case "farm": return Farm;
            default: return Farm;
        }
    }
    private Side getSide(TerrainType t) {
        if (t.equals(City)) return new Side(City, City, City);
        else if(t.equals(River)) return new Side(Farm, River, Farm);
        else if(t.equals(Road)) return new Side(Farm, Road, Farm);
        else if(t.equals(Farm)) return new Side(Farm, Farm, Farm);
        else return null; // impossible
    }
    private Side[] makeSides(HashMap<Orient, TerrainType> map) {
        if(map.get(N) == null) map.put(N, Farm);
        if(map.get(W) == null) map.put(W, Farm);
        if(map.get(S) == null) map.put(S, Farm);
        if(map.get(E) == null) map.put(E, Farm);

        if(map.get(N) == null
                || map.get(W) == null
                || map.get(S) == null
                || map.get(E) == null) {
            System.out.println("incomplete map");
            return null; // incomplete hashmap
        }

        return new Side[] { // N W S E
                getSide(map.get(N)),
                getSide(map.get(W)),
                getSide(map.get(S)),
                getSide(map.get(E)),
        };
    }
    private CarcassonneTile parseLine(String line) {
        int indx = Integer.parseInt(line.split(" ")[0]);
        Pattern p = Pattern.compile("\\[.*?]");
        Matcher m = p.matcher(line);
        HashMap<Orient, TerrainType> orients = new HashMap<>();
        boolean mono = false;
        boolean shield = false;
        while(m.find()){
            String parts = m.group();
            parts = parts.substring(1, parts.length()-1);
            String cate = parts.split(":")[0];
            parts = parts.split(":")[1].replaceAll("\\s","");

            if (cate.equals("monastery") && parts.equals("C")) {
                mono = true;
            } else {
                Orient[] os = getOrient(parts);
                for(Orient o: os) {
                    orients.put(o, typeFromString(cate));
                }
                if(cate.equals("city") && parts.contains("*"))
                    shield = true;
            }
        }

        // load image
        BufferedImage timg = null;
        try {
            timg = ImageIO.read(CarcassonneTile.class.getResource(String.format("/res/tileImg/%d.png", indx)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new CarcassonneTile(mono, shield, this.makeSides(orients), timg);
    }
    public HashMap<Integer, CarcassonneTile> loadTiles(String file) {
        HashMap<Integer, CarcassonneTile> ret = new HashMap<Integer, CarcassonneTile>();
        try {
            Scanner s = new Scanner(new File(file));
            while(s.hasNextLine()) {
                String line = s.nextLine();
                int indx = Integer.parseInt(line.split(" ")[0]);
                ret.put(indx, this.parseLine(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("failed to load file: " + e);
        }
        return ret;
    }
    public static void main(String args[]) {
        TileParser p = new TileParser();
        HashMap<Integer, CarcassonneTile> t = p.loadTiles("src/res/tileImg/tile_data.txt");
        System.out.println(t.size());
        t.get(50).rotate(Rotation.D90);
        System.out.println(t.get(48));
    }
}

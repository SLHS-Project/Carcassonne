import tile.*;
import static tile.Orient.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * example format: ( Full list at google docs )
 * 37 [river: E] [city: ] [road: ] [monastery: ]
*/

public class TileParser {
    private Orient orientFromString(String o) {
        switch(o.toLowerCase()) {
            case "N": return N;
            case "W": return W;
            case "S": return S;
            case "E": return E;
            default: return null;
        }
    }
    private Orient[] getOrient(String l) {
        List<Orient> ret = new LinkedList<Orient>();
        String[] all = l.split("");
        Orient o;
        for(String e: all)
            if ((o = orientFromString(e)) != null)
                ret.add(o);

        return ret.toArray(new Orient[ret.size()]);
    }
    private Side[] makeSides(HashMap<String, Orient[]> map) {
        if(map.get("river") == null || map.get("city") == null || map.get("road") == null) {
            System.out.println("incomplete map");
            return null; // incomplete
        }
        
        return null;
    }
    private CarcassonneTile parseLine(String line) {
        Pattern p = Pattern.compile("\\[.*?]");
        Matcher m = p.matcher(line);
        HashMap<String, Orient[]> orients = new HashMap<>();
        boolean mono;
        while(m.find()){
            String parts = m.group();
            parts = parts.substring(1, parts.length()-1);
            String cate = parts.split(":")[0];
            parts = parts.split(":")[1].replace(' ', '\0');

            if (cate.equals("monastery")) {
            } else {
                Orient[] os = getOrient(parts);
                orients.put(cate, os);

                System.out.print(cate + " " + parts);
                for(Orient o: os) System.out.print(o);
                System.out.println();
            }
        }
        return null;
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
    }
}

import tile.*;
import static tile.Orient.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
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
            default: return N;
        }
    }
    private Orient[] getOrient(String l) {
        ArrayList<Orient> ret = new ArrayList<Orient>();
        Pattern p = Pattern.compile("\\[.*?]");
        Matcher m = p.matcher(l);
        while (m.find()) {
            String o = m.group();
            System.out.println(o);
            if (!ret.contains(this.orientFromString(o)))
                ret.add(this.orientFromString(o));
        }

        return ret.toArray(new Orient[ret.size()]);
    }
    private CarcassonneTile parseLine(String line) {
        Pattern p = Pattern.compile("\\[.*?]");
        Matcher m = p.matcher(line);
        String[] splited = line.split(":\\s\\W*\\]");
        String river = splited[0];
        while(m.find)System.out.println();
        System.out.println(splited);
        System.out.println(this.getOrient(river)[0]);
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

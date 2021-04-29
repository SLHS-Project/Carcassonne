import tile.CarcassonneTile;
import tile.Orient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class CarcassonneScore {
    CarcassonneMap map;

    public CarcassonneScore(CarcassonneMap map) {
        this.map = map;
    }

    public CarcassonneTile getRelativeTile(Orient o, int x, int y) {
        int[] vec = o.getVector();
        return map.map[x + vec[0]][y + vec[1]];
    }

    public boolean appendToWeb(int x, int y, ArrayList<Orient> roads, ArrayList<RoadWeb> roadwebs) {
        ArrayList<RoadWeb> inclwebs = new ArrayList<>();
        for(Orient o: roads) {
            CarcassonneTile rt = this.getRelativeTile(o, x, y);
            for(RoadWeb rw : roadwebs) {
                if(rw.contains(rt)) {
                    inclwebs.add(rw);
                    //rw.add(this.map.map[x][y]);
                    //added = true;
                }
            }
        }

        if (inclwebs.isEmpty())
            return false;
        else if(inclwebs.size() == 1) {
            inclwebs.get(0).add(this.map.map[x][y]);
        } else {
            RoadWeb merged = new RoadWeb(inclwebs.get(0).iden);
            for(RoadWeb rw: inclwebs) {
                merged.set.addAll(rw.set);
                Iterator itr = roadwebs.iterator();
                while (itr.hasNext()) {
                    RoadWeb elem = (RoadWeb) itr.next();
                    if(elem.iden == rw.iden)
                        itr.remove();
                }
            }
            merged.add(this.map.map[x][y]);
            roadwebs.add(merged);
            System.out.println("merging: " + merged.set);
        }
        return true;
    }

    public int score() {
        CarcassonneMap.Boundary b = map.getBoundary();

        ArrayList<RoadWeb> roadwebs = new ArrayList<RoadWeb>();

        for(int x = b.x(); x < b.x2(); x++) {
            for(int y = b.y(); y < b.y2(); y++) {
                CarcassonneTile curr = map.map[x][y];
                if(curr == null) continue;
                ArrayList<Orient> cities = curr.getCities();
                ArrayList<Orient> roads = curr.getRoads();

                if(!roads.isEmpty()) {
                    if(!this.appendToWeb(x, y, roads, roadwebs))
                        roadwebs.add(new RoadWeb(this.map.map[x][y]));
                }
            }
        }

        for(RoadWeb rb: roadwebs) {
            System.out.println(rb.set);
        }
        System.out.println();
        return 0;
    }
}

class RoadWeb {
    CarcassonneTile iden;
    HashSet<CarcassonneTile> set;

    public RoadWeb(CarcassonneTile iden) {
        this.set = new HashSet<CarcassonneTile>();
        this.iden = iden;
        this.add(iden);
    }

    public void add(CarcassonneTile iden)  {
        this.set.add(iden);
    }

    public boolean contains(CarcassonneTile iden) {
        return this.set.contains(iden);
    }
}

import player.CarcassonnePlayer;
import tile.CarcassonneTile;
import tile.Orient;

import java.util.ArrayList;
import java.util.HashMap;
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

    public boolean appendRoadToWeb(int x, int y, ArrayList<Orient> roads, ArrayList<RoadWeb> roadwebs) {
        ArrayList<RoadWeb> inclwebs = new ArrayList<>();
        for(Orient o: roads) {
            CarcassonneTile rt = this.getRelativeTile(o, x, y);
            for(RoadWeb rw : roadwebs)
                if(rw.contains(rt))
                    inclwebs.add(rw);
        }

        if (inclwebs.isEmpty())
            return false;
        else if(inclwebs.size() == 1) {
            inclwebs.get(0).add(this.map.map[x][y]);
        } else {
            RoadWeb merged = new RoadWeb(inclwebs.get(0).iden, map);
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

    public boolean appendCityToChunck(int x, int y, ArrayList<Orient> cities, ArrayList<CityChunk> chuncks) {
        ArrayList<CityChunk> inccity = new ArrayList<>();
        for(Orient o: cities) {
            CarcassonneTile rt = this.getRelativeTile(o, x, y);
            for(CityChunk cc: chuncks)
                if (cc.contains(rt))
                    inccity.add(cc);
        }

        CarcassonneTile ctile = this.map.map[x][y];
        if(ctile.id == 19 ||
            ctile.id == 20||
            ctile.id == 24||
            ctile.id == 51||
            ctile.id == 54||
            ctile.id == 59){
            for(Orient o: cities) {
                CarcassonneTile rt = this.getRelativeTile(o, x, y);
                for(CityChunk cc: chuncks)
                    if (cc.contains(rt))
                        inccity.add(cc);
            }
            for(CityChunk ic: inccity) {
                ic.add(ctile);
            }
            return true;
        }

        if(inccity.isEmpty())
            return false;
        else if(inccity.size() == 1)
            inccity.get(0).add(this.map.map[x][y]);
        else {
            CityChunk merged = new CityChunk(inccity.get(0).iden, map);
            for(CityChunk cc: inccity) {
                merged.set.addAll(cc.set);
                Iterator itr = chuncks.iterator();
                while (itr.hasNext()) {
                    CityChunk elem = (CityChunk) itr.next();
                    if(elem.iden == cc.iden)
                        itr.remove();
                }
            }
            merged.add(this.map.map[x][y]);
            chuncks.add(merged);
        }
        return true;
    }

    public int score() {
        CarcassonneMap.Boundary b = map.getBoundary();

        ArrayList<RoadWeb> roadwebs = new ArrayList<RoadWeb>();
        ArrayList<CityChunk> citychunks = new ArrayList<CityChunk>();

        ArrayList<CarcassonneMap.Coordinate> later = new ArrayList<>();

        for(int x = b.x(); x < b.x2(); x++) {
            for(int y = b.y(); y < b.y2(); y++) {
                CarcassonneTile curr = map.map[x][y];
                if(curr == null) continue;
                ArrayList<Orient> cities = curr.getCities();
                ArrayList<Orient> roads = curr.getRoads();

                if(!roads.isEmpty())
                    if(!this.appendRoadToWeb(x, y, roads, roadwebs))
                        roadwebs.add(new RoadWeb(this.map.map[x][y], this.map));

                if(curr.id == 19 ||
                        curr.id == 20||
                        curr.id == 24||
                        curr.id == 51||
                        curr.id == 54||
                        curr.id == 59) {
                    later.add(new CarcassonneMap.Coordinate(x, y));
                    continue;
                }
                if(!cities.isEmpty())
                    if(!this.appendCityToChunck(x, y, cities, citychunks))
                        citychunks.add(new CityChunk(this.map.map[x][y], this.map));
            }
        }

        for(CarcassonneMap.Coordinate lc: later) {
            ArrayList<Orient> cities = this.map.map[lc.x][lc.y].getCities();
            this.appendCityToChunck(lc.x, lc.y, cities, citychunks);
        }

        System.out.println("roads: ");
        for(RoadWeb rb: roadwebs) {
            System.out.println(rb + " " + rb.isComplete());
            if(rb.isComplete()) {
                HashSet<CarcassonnePlayer> owners = rb.owner();
                for(CarcassonnePlayer p: owners) {
                    System.out.println(p);
                    p.addScore(rb.set.size());
                }
            }
        }
        System.out.println("cities");
        for(CityChunk cc: citychunks) {
            System.out.println(cc + " " + cc.isComplete());
            if(cc.isComplete()) {
                HashSet<CarcassonnePlayer> owners = cc.owner();
                for(CarcassonnePlayer p: owners) {
                    System.out.println(p);
                    p.addScore(cc.set.size());
                }
            }
        }
        System.out.println();
        return 0;
    }
}

class RoadWeb {
    CarcassonneTile iden;
    CarcassonneMap map;
    HashSet<CarcassonneTile> set;

    public RoadWeb(CarcassonneTile iden, CarcassonneMap map) {
        this.set = new HashSet<CarcassonneTile>();
        this.map = map;
        this.iden = iden;
        this.add(iden);
    }

    public HashSet<CarcassonnePlayer> owner() {
        HashSet<CarcassonnePlayer> ret = new HashSet<>();
        for(CarcassonneTile t: this.set) {
            if(t.getMeeple() != null)
                ret.add(t.getMeeple().getOwner());
        }

        return ret;
    }

    public void add(CarcassonneTile iden)  {
        this.set.add(iden);
    }

    public boolean contains(CarcassonneTile iden) {
        return this.set.contains(iden);
    }

    public boolean isComplete() {
        CarcassonneMap.Boundary b = map.getBoundary();

        for(int x = b.x(); x < b.x2(); x++) {
            for (int y = b.y(); y < b.y2(); y++) {
                CarcassonneTile curr = map.map[x][y];
                if(!this.set.contains(curr)) continue; // only check what's in the web

                ArrayList<Orient> roads = curr.getRoads();
                if(roads.isEmpty()) continue; // only check if it's a road tile

                for(Orient o: roads) {
                    if(map.getRelativeTile(o, x, y) == null) return false;
                }
            }
        }
        return true;
    }

    public ArrayList<RoadWeb> subroads() {
        // get sub road, a complete road that is ended with center roads.

        return null;
    }

    @Override
    public String toString() {
        return this.set.toString();
    }
}

class CityChunk {
    CarcassonneTile iden;
    CarcassonneMap map;
    HashSet<CarcassonneTile> set;

    public CityChunk(CarcassonneTile iden, CarcassonneMap map) {
        this.set = new HashSet<>();
        this.iden = iden;
        this.map = map;

        this.set.add(iden);
    }

    public HashSet<CarcassonnePlayer> owner() {
        HashSet<CarcassonnePlayer> ret = new HashSet<>();
        for(CarcassonneTile t: this.set) {
            if(t.getMeeple() != null)
                ret.add(t.getMeeple().getOwner());
        }

        return ret;
    }

    public void add(CarcassonneTile t)  {
        this.set.add(t);
    }
    public boolean contains(CarcassonneTile t) {
        return this.set.contains(t);
    }
    public boolean containsID(int tid) {
        for(CarcassonneTile t: set)
            if(t.id == tid)
                return true;
        return false;
    }

    public int totalShields() {
        int s = 0;
        for(CarcassonneTile t: this.set)
            if(t.hasShield())
                s++;
        return s;
    }

    public boolean isComplete() {
        CarcassonneMap.Boundary b = map.getBoundary();

        for(int x = b.x(); x < b.x2(); x++) {
            for (int y = b.y(); y < b.y2(); y++) {
                CarcassonneTile curr = map.map[x][y];
                if(!this.set.contains(curr)) continue; // only check what's in the web

                ArrayList<Orient> cities = curr.getCities();
                if (cities.isEmpty()) continue;

                for(Orient o: cities) {
                    if (this.map.getRelativeTile(o, x, y) == null)
                        return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        return this.set.toString();
    }
}
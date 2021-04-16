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

import tile.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class CarcassonneMap {
    public Resources resources;

	private CarcassonneTile[][] map;
	private TreeMap<CarcassonneTile, Point> Map;
  	private TreeMap<ArrayList<CarcassonneTile>, Boolean> cities;
  	private TreeMap<ArrayList<Integer>, String> farmlands;
  	private ArrayList<CarcassonneTile> completedTiles; //temporary storing
  	private CarcassonnePlayer red, yellow, blue, green;
  	private final int WIDTH=1600;
  	private final int HEIGHT=760;
  	private int tileSize;

    public CarcassonneMap(CarcassonnePlayer r, CarcassonnePlayer y, CarcassonnePlayer b, CarcassonnePlayer g)
  	{
  		red=r;
  		yellow=y;
  		blue=b;
  		green=g;
  		
  		Map=new TreeMap<>();
  		cities=new TreeMap<>();
  		farmlands=new TreeMap<>();
  		
  		tileSize=HEIGHT/4;
  		completedTiles=new ArrayList<>();

  		this.resources = new Resources("src/res/tileImg/tile_data.txt");

		// 0, 0 is left, top
        this.map = new CarcassonneTile[85][85];
        this.tryAddAt(resources.getTiles().get(37), 43, 43);
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

        // TODO fix

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
        public int tilex, tiley;
        public int x1, x2, y1, y2;
        public Boundary(int x1, int x2, int y1, int y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }
		public Boundary(int x1, int x2, int y1, int y2, int tilex, int tiley) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
			this.tilex = tilex;
			this.tiley = tiley;
		}
        public void translate(int dx, int dy) {
        	this.x1 += dx;
        	this.x2 += dx;
        	this.y1 += dy;
        	this.y2 += dy;
		}

		@Override
		public String toString() {
			return "Boundary{" +
					"x1=" + x1 +
					", x2=" + x2 +
					", y1=" + y1 +
					", y2=" + y2 +
					'}';
		}

		public int width() {
			return (this.x1>this.x2)?this.x1-this.x2:this.x2-this.x1;
		}

		public int height() {
			return (this.y1>this.y2)?this.y1-this.y2:this.y2-this.y1;
		}

		public int x() {
			return (this.x1>this.x2)?this.x2:this.x1;
		}
		public int x2() {
			return (this.x1<this.x2)?this.x2:this.x1;
		}
		public int y() {
			return (this.y1>this.y2)?this.y2:this.y1;
		}
		public int y2() {
			return (this.y1<this.y2)?this.y2:this.y1;
		}

		public boolean contains(int x, int y) {
        	if(x > this.x() && x < this.x2()
				&& y > this.y() && y < this.y2())
        		return true;
        	return false;
		}
	}
	public Boundary getBoundary() {
        // x min
        int xmin = 43;
        for(int i = 43; i >= 0; i--) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[i][j] != null) {
                    tile = true;
                    break;
                }
            if(!tile) break;
            xmin = i;
        }
        // x max
        int xmax = 43;
        for(int i = 43; i < 85; i++) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if (this.map[i][j] != null) {
                    tile = true;
                    break;
                }
            if(!tile) break;
            xmax = i;
        }
        // y min
        int ymin = 43;
        for(int i = 43; i >= 0; i--) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[j][i] != null) {
                    tile = true;
                    break;
                }
            if(!tile) break;
            ymin = i;
        }
        // y max
        int ymax = 43;
        for(int i = 43; i < 85; i++) {
            boolean tile = false;
            for(int j = 0; j < 85; j++)
                if(this.map[j][i] != null) {
                    tile = true;
                    break;
                }
            if(!tile) break;
            ymax = i;
        }
        return new Boundary(xmin - 1, xmax + 1, ymin - 1, ymax + 1);
    }

    class GameBoardGraphics {
        BufferedImage img;
        ArrayList<Boundary> boundaries;

        public GameBoardGraphics(BufferedImage img, ArrayList<Boundary> boundaries) {
            this.img = img;
            this.boundaries = boundaries;
		}

		public BufferedImage getImg() {
        	return img;
		}

		public ArrayList<Boundary> getBoundaries() {
        	return this.boundaries;
		}

		@Override
		public String toString() {
			return "GameBoardGraphics{" +
					"boundaries=" + boundaries +
					'}';
		}
	}

    public GameBoardGraphics render(int h, int w) {
        int tile_size = 75;
		Boundary bb = this.getBoundary();
		int width = bb.x2 - bb.x1 + 1;
		int height = bb.y2 - bb.y1 + 1;
		BufferedImage mapimg = new BufferedImage(width * tile_size, height * tile_size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = mapimg.createGraphics();
		for(int x = bb.x1; x <= bb.x2; x++) {
			for (int y = bb.y1; y <= bb.y2; y++) {
				if(this.map[x][y] == null) continue;
				g2d.drawImage(this.map[x][y].getImage(), (x - bb.x1) * tile_size, (y - bb.y1) * tile_size, null);
			}
		}
		ArrayList<Boundary> imgBoundPoss = new ArrayList<>();
		ArrayList<Coordinate> poss = this.getPossibleLocations();
		for(Coordinate coord: poss) {
			g2d.setPaint ( new Color ( 20,20,20 ) );
			if(resources.placeholder == null)
				g2d.fillRect( (coord.x() - bb.x1) * tile_size, (coord.y() - bb.y1) * tile_size, tile_size, tile_size);
			else
				g2d.drawImage(resources.placeholder, (coord.x() - bb.x1) * tile_size, (coord.y() - bb.y1) * tile_size, tile_size, tile_size, null);
			imgBoundPoss.add(new Boundary((coord.x() - bb.x1) * tile_size, (coord.x() - bb.x1) * tile_size + tile_size,
					(coord.y() - bb.y1) * tile_size, (coord.y() - bb.y1) * tile_size+tile_size, coord.x, coord.y));
		}
		g2d.dispose();
		for(Boundary b: imgBoundPoss)
		    b.translate(h/2 - mapimg.getWidth()/2, w/2 - mapimg.getHeight()/2);

		BufferedImage r = new BufferedImage(h, w, BufferedImage.TYPE_INT_ARGB);
		g2d = r.createGraphics();
		g2d.drawImage(mapimg, h/2 - mapimg.getWidth()/2, w/2 - mapimg.getHeight()/2, null);
		g2d.dispose();
        return new GameBoardGraphics(r, imgBoundPoss);
	}

  //checks if any feature is completed
  	public String complete (CarcassonneTile tile)
  	{
  		String s="";
  		if(completeRD(tile, ""))
  			s+="RD ";
  		if(completeC(tile))
  			s+="C ";
  		if(completeM(tile)!=null)
  			s+="M";
  		
  		return s;
  	}
  	private boolean completeRD (CarcassonneTile tile, String direction) //direction is which way the road extends on this tile
  	{
  		completedTiles.clear();
  		
  		Side[] s = tile.getSides();
  		String temp="";
  		for(int i=0; i<s.length; i++)
  		{
  			TerrainType[] ts=s[i].getSide();
  			if(ts[1].Road!=null) {
  				if(i==0)
  					temp+="N ";
  				else if(i==1)
  					temp+="W ";
  				else if(i==2)
  					temp+="S ";
  				else
  					temp+="E";
  			}
  		}
  		//consists of the direction of the road: "N W"
  		String[] rd=temp.split(" ");
  		//if the road has two exists, then it did not end a road
  			if(rd.length==2)
  				return false;
  			
  		Point tempPt;
  		Point loc=Map.get(tile);
  		String lineD="";
  		//if the ending tile ends the road with a city or monastery or etc.
  		for(String str: rd) {
  			if(rd[0].equals("N")) {
  				tempPt=new Point((int)loc.getX(), (int)loc.getY()-tileSize);
  				lineD="S";}
  			else if(rd[0].equals("W")) {
  				tempPt=new Point((int)loc.getX()-tileSize, (int)loc.getY());
  				lineD="E";}
  			else if(rd[0].equals("S")) {
  				tempPt=new Point((int)loc.getX(), (int)loc.getY()+tileSize);
  				lineD="W";}
  			else {
  				tempPt=new Point((int)loc.getX()+tileSize, (int)loc.getY());
  				lineD="N";}
  			
  			CarcassonneTile tile1=findLoc(tempPt);
  			if(tile1!=null) {
  				completedTiles.add(tile);
  				completeRd(findLoc(tempPt), lineD);
  			}
  			if(completedTiles.size()<2)
  				return false;
  			return true;
  		}
  		System.out.println("Some error with completeRd function");
  		return false;
  	}
  	private void completeRd(CarcassonneTile tile, String lineD)
  	{
  		Side[] s = tile.getSides();
  		String temp="";
  		Point loc=Map.get(tile);
  		completedTiles.add(tile);
  		
  		for(int i=0; i<s.length; i++)
  		{
  			String[] ts=s[i].getSideStr();
  			if(ts[1].equalsIgnoreCase("Rd")) {
  				if(i==0)
  					temp+="N ";
  				else if(i==1)
  					temp+="W ";
  				else if(i==2)
  					temp+="S ";
  				else
  					temp+="E";
  			}
  		}
  		//consists of the direction of the road: "N W"
  		String[] rd=temp.split(" ");
  		
  		//first check if this tile is where the road ends
  		if (rd.length==1 || tile.isCR())
  			return ;
  		
  		//the road continues
  		
  		//find the location of the tile the road leads to
  		Point tempPt;
  		int i=0;
  		if(rd[0].equals(lineD.substring(lineD.length()-1)))
  			i=1;
  		if(rd[i].equals("N")) {
  			tempPt=new Point((int)loc.getX(), (int)loc.getY()-tileSize);
  			lineD+="S";
  		}
  		else if(rd[i].equals("W")) {
  			tempPt=new Point((int)loc.getX()-tileSize, (int)loc.getY());
  			lineD+="E";}
  		else if(rd[i].equals("S")) {
  			tempPt=new Point((int)loc.getX(), (int)loc.getY()+tileSize);
  			lineD+="N";}
  		else {
  			tempPt=new Point((int)loc.getX()+tileSize, (int)loc.getY());
  			lineD+="W";}
  			
  		CarcassonneTile tile1=findLoc(tempPt);
  		//if no tile exist, the road is not complete, erase all items in the arraylist
  		if(tile1==null)
  			completedTiles.clear();
  		completeRd(tile1, lineD);
  	}
  	
  	private boolean completeC (CarcassonneTile tile)
  	{
  		Side[] s = tile.getSides();
  		String temp="";
  		for(int i=0; i<s.length; i++)
  		{
  			TerrainType[] ts=s[i].getSide();
  			if(ts[1].City!=null) {
  				if(i==0)
  					temp+="N ";
  				else if(i==1)
  					temp+="W ";
  				else if(i==2)
  					temp+="S ";
  				else
  					temp+="E";
  			}
  		}
  		//consists of the direction of the road: "N W"
  		String[] c=temp.split(" ");
  		
  		//change the value of the key in "cities" to true once completed
  		//check if this tile connects two different portion of cities, merge the arraylist in "cities" if so
  		return true;
  	}
  	//receive the just placed tile, return the center monastery tile if completed, else return null
  	private CarcassonneTile completeM (CarcassonneTile tile)
  	{
  		CarcassonneTile MTile=null;
  		return MTile;
  	}
  	
  	//assume the just-placed tile completed a road, do score calculation
  	public void roadScoring(CarcassonneTile tile, Point loc)
  	{
  		//tileCodes have all the tiles that the completed road is on
  		//check for meeples
  		String players=meepleCompare(completedTiles, "RD");
  		scoreCalc(players, 1, completedTiles.size());
  	}
  	
  	//assume the just-placed tile completed a city, do score calculation
  	public void cityScoring(CarcassonneTile tile)
  	{
  		ArrayList<CarcassonneTile> C2Score=new ArrayList<>();
  		for(ArrayList<CarcassonneTile> a: cities.keySet()) {
  			if(a.contains(tile.getCode()))
  				C2Score=a;
  		}
  		
  		String owners=meepleCompare(C2Score, "C");
  		if (owners.length()<2)
  			return;
  		
  		int shieldNum=0;
  		for(CarcassonneTile t: C2Score) {
  			if(t.hasShield())
  				shieldNum++;
  		}
  		int score=2*C2Score.size()+2*shieldNum;
  		
  		String[] p=owners.split(" ");
  		for(String s: p) {
  			callPlayer(s).addScore(score);
  		}
  	}

  	//assume the just-placed tile completed a monastery, do score calculation
  	//MTile is the tile with the monastery on it, this will be done via completeM
  	public void monasteryScoring(CarcassonneTile MTile, Point loc)
  	{
  		String name="";
  		Meeple mp=MTile.getMeeple();
  		if(mp!=null)
  			name=mp.getColor();
  		callPlayer(name).addScore(9);
  	}
  	
  	private CarcassonneTile findLoc(Point pt)
  	{
  		for(CarcassonneTile t: Map.keySet()) {
  			if(Map.get(t).equals(pt))
  				return t;
  		}
  		System.out.println("Didn't find the tile at this location "+pt);
  		return null;
  	}
  	
  	private String meepleCompare(ArrayList<CarcassonneTile> tiles, String type)
  	{
  		TreeMap<String, Integer> players=new TreeMap<>();
  		for(CarcassonneTile t: tiles)
  		{
  			Meeple meeple=t.getMeeple();
  			if(meeple!=null) {
  				if (meeple.getType().equals(type))
  				{
  					String owner=meeple.getColor();
  					//add the owner of meeple to the treemap, but check if it exist first
  					if(players.containsKey(owner))
  						players.replace(owner, players.get(owner)+1);
  					else
  						players.put(owner, 1);
  				}
  			}
  		}
  		String line="";
  		int i=0;
  		for(String key: players.keySet()) {
  			if(players.get(key)>i)
  				line=key+" ";
  			else if(players.get(key)==i)
  				line+=key+" ";
  		}
  		return line;
  	}
  	//add the score to the player(s)    //spt = score per tile
  	private void scoreCalc(String players, int spt, int tileCnt)
  	{
  		String[] p=players.split(" ");
  		for(String s: p) {
  			callPlayer(s).addScore(spt*tileCnt);
  		}
  	}
  	
  	private CarcassonnePlayer callPlayer(String name)
  	{
  		if(name.equals("red"))
  			return red;
  		else if(name.equals("yellow"))
  			return yellow;
  		else if(name.equals("blue"))
  			return blue;
  		else if(name.equals("green"))
  			return green;
  		System.out.println("DID NOT FIND A PLAYER WITH NAME "+name);
  		return null;
  	}

    
    
    public static void main(String[] args) {
        // Following is a test data. These does not have effect to the main program
		CarcassonnePlayer r=new CarcassonnePlayer("r");
		CarcassonnePlayer y=new CarcassonnePlayer("y");
		CarcassonnePlayer b=new CarcassonnePlayer("b");
		CarcassonnePlayer g=new CarcassonnePlayer("g");

        CarcassonneMap map = new CarcassonneMap(r, y, b, g);

		TileParser p = new TileParser();
		HashMap<Integer, CarcassonneTile> t = p.loadTiles("src/res/tileImg/tile_data.txt");

		System.out.println(map.tryAddAt(t.get(38), 44, 43));
		System.out.println(map.tryAddAt(t.get(43), 44, 44));

		try {
		    GameBoardGraphics gbg = map.render(1920, 1080);
            File outputfile = new File("C:\\Users\\k1702639\\Desktop\\a\\map.png");
            ImageIO.write(gbg.getImg(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

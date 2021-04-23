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
  //	private TreeMap<CarcassonneTile, Point> Map;
  	private ArrayList<ArrayList<CarcassonneTile>> cities;
  	private ArrayList<Boolean> citiesVal;
  	private ArrayList<ArrayList<CarcassonneTile>> farmlands;
  	private ArrayList<ArrayList<CarcassonneTile>> roads;  //decide if to use this or not, keeps track of all roads completed and incomplete //makes scoring much easier
  	private ArrayList<Boolean> roadsVal;
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
  		
  		//Map=new TreeMap<>();
  		cities=new ArrayList<>();
  		farmlands=new ArrayList<>();
  		roads=new ArrayList<>();
  		citiesVal=new ArrayList<>();
  		roadsVal=new ArrayList<>();
  		
  		tileSize=HEIGHT/4;

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
        System.out.println(t);
		System.out.println(this.map[x+1][y]);
		System.out.println(this.map[x][y+1]);

		return ret;
    }

    public boolean canAddAt(CarcassonneTile t, int x, int y) {
        if(this.map[x][y] != null) return false;
        return this.getConflicts(t, x, y).isEmpty();
    }

    public boolean tryAddAt(CarcassonneTile t, int x, int y) {
        if(!this.canAddAt(t, x, y)) return false;
        this.map[x][y] = t;
        addToVariables(t, x, y);
        return true;
    }
    
    private void addToVariables(CarcassonneTile t, int x, int y)
    {
    		
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

    public GameBoardGraphics render(int w, int h) {
    	// TODO fix this
    	System.out.println(h+ " " + w);
		int tile_size = 75;
		Boundary bb = this.getBoundary();
		int width = bb.x2 - bb.x1 + 1;
		int height = bb.y2 - bb.y1 + 1;

		// tw * height == h
		// tw = h/height
		if ((width) * tile_size > w) tile_size = w/(width);
		if ((height) * tile_size > h) tile_size = h/(height);
		System.out.println(h + tile_size + " " + ((height) * tile_size));

		BufferedImage mapimg = new BufferedImage(width * tile_size, height * tile_size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = mapimg.createGraphics();
		for(int x = bb.x1; x <= bb.x2; x++) {
			for (int y = bb.y1; y <= bb.y2; y++) {
				if(this.map[x][y] == null) continue;
				g2d.drawImage(this.map[x][y].getImage(), (x - bb.x1) * tile_size, (y - bb.y1) * tile_size, tile_size, tile_size,  null);
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
		    b.translate(w/2 - mapimg.getWidth()/2, h/2 - mapimg.getHeight()/2);

		BufferedImage r = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2d = r.createGraphics();
		g2d.drawImage(mapimg, w/2 - mapimg.getWidth()/2, h/2 - mapimg.getHeight()/2, null);
		g2d.dispose();
        return new GameBoardGraphics(r, imgBoundPoss);
	}

  //checks if any feature is completed
  	public String complete (CarcassonneTile tile, int x, int y)
  	{
  		String s="";
  		if(completeRD(tile, x, y))
  			s+="RD ";
  		if(completeC(tile, x, y))
  			s+="C ";
  		if(completeM(tile, x, y)!=null)
  			s+="M";
  		
  		return s;
  	}
  	private boolean completeRD (CarcassonneTile tile, int x, int y)
  	{
  		
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
  		if(temp.length()<1)
  			return false;
  		//consists of the direction of the road: "N W"
  		String[] rd=temp.split(" ");
  			
  		ArrayList<CarcassonneTile> list=new ArrayList<>();
  		boolean newRoad=true;
  		for(String direction: rd)
  		{
  			CarcassonneTile tempTile;
  				if(direction.equals("N"))
  					tempTile=map[x][y-1];
  					//pt.setLocation(pt.getX(), pt.getY()-tileSize);
  				else if(direction.equals("S"))
  					tempTile=map[x][y+1];
  					//pt.setLocation(pt.getX(), pt.getY()+tileSize);
  				else if(direction.equals("W"))
  					tempTile=map[x-1][y];
  					//pt.setLocation(pt.getX()-tileSize, pt.getY());
  				else //if(direction.equals("E"))
  					tempTile=map[x+1][y];
  					//pt.setLocation(pt.getX()+tileSize, pt.getY());
  				
  				//if the tile exist and has road 
  				if(tempTile!=null && tempTile.checkRdDirections()>0)
  				{
  						newRoad=false;
  						list=contains(tempTile, "RD");
  						list.add(tile); //add the new tile to the arraylist in roads
  		  			
  		  			for(CarcassonneTile t: list)
  		  			{
  		  					if(t.checkRdDirections()==1 || t.isCR())
  		  					{
  		  						for(int i=0; i<roads.size(); i++)
  		  						{
  		  							ArrayList<CarcassonneTile> tiles=roads.get(i);
  		  							if(tiles.equals(list))
  		  							{
  		  								roadsVal.set(i, true);
  		  								return true;
  		  							}
  		  						}
  		  					}
  		  			}	
  				}
  		}
  		//this tile starts a new road
  		if(newRoad)
  		{
  			 ArrayList<CarcassonneTile> tempList=new ArrayList<>();
  			 tempList.add(tile);
  			 roads.add(tempList);
  			 roadsVal.add(false);
  			 return false;
  		}
  		
  		//if the road on tile has two ends & finishes a road, then in roads there should be two arraylist containing this tile
  		if(rd.length==2)
  		{
  			ArrayList<CarcassonneTile> list1=new ArrayList<>();
  			ArrayList<CarcassonneTile> list2=new ArrayList<>();
  			int index1=0;
  			int index2=0;
  			for(ArrayList<CarcassonneTile> tempList: roads)
  			{
  				if(tempList.contains(tile) && list1.size()==0)
  					list1=tempList;
  				else if(tempList.contains(tile)) {
  					list2=tempList;
  					break;
  				}	
  				else 
  					index1++;
  				index2++;
  			}
  			if(list1==null || list2==null)
  				return false;
  			list2.remove(tile);
  			list1.addAll(list2);
  			roads.remove(list2);
  			roadsVal.remove(index2);
  			int cnt=0;
  			for(CarcassonneTile t: list1)
  			{
  				if(t.isCR() || t.checkRdDirections()==1)
  					cnt++;
  			}
  			if(cnt==2)
  			{
  				roadsVal.set(index1, true);
  				return true;
  			}
  		}
  		return false;
  		
  	}
  				  	
  	//DO THIS!
  	private boolean completeC (CarcassonneTile tile, int x, int y)
  	{
  		if(!tile.hasCity())
  			return false;
  		
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
  		ArrayList<CarcassonneTile> cityTiles=new ArrayList<>();
  		for(int i=0; i<c.length; i++)
  		{
  				String direction=c[i];
  				CarcassonneTile tempTile;
  				//Point pt=Map.get(tile);
  				if(direction.equals("N"))
  					tempTile=map[x][y-1];
  					//pt.setLocation(pt.getX(), pt.getY()-tileSize);
  				else if(direction.equals("S"))
  					tempTile=map[x][y+1];
  					//pt.setLocation(pt.getX(), pt.getY()+tileSize);
  				else if(direction.equals("W"))
  					tempTile=map[x-1][y];
  					//pt.setLocation(pt.getX()-tileSize, pt.getY());
  				else //if(direction.equals("E"))
  					tempTile=map[x+1][y];
  					//pt.setLocation(pt.getX()+tileSize, pt.getY());
  				
  				//CarcassonneTile tempTile=findTile(pt);
  				if(i==0)
  					cityTiles=contains(tempTile, "C");
  				else
  				{
  					ArrayList<CarcassonneTile> newCityTiles=contains(tempTile, "C");
  					//if the two arraylist doesn't equal
  					if(!cityTiles.equals(newCityTiles)) {
  						cityTiles.addAll(newCityTiles);
  						cityTiles.add(tile);
  						citiesVal.remove(cities.indexOf(newCityTiles));
  						cities.remove(newCityTiles);
  					}
  				}
  				
  		//change the value of the key in "cities" to true once completed
  		//check if this tile connects two different portion of cities, merge the arraylist in "cities" if so
  		}
  		
  		//if this city is a new city
  		if(cityTiles==null || cityTiles.size()==0)
  		{
  				ArrayList<CarcassonneTile> tempList=new ArrayList<>();
  				tempList.add(tile);
  				cities.add(tempList);
  				citiesVal.add(false);
  				return false;
  		}
  		
  		cityTiles=contains(tile, "C");
  		for(CarcassonneTile t: cityTiles)
  		{
  			s = t.getSides();
    		temp="";
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
    		c=temp.split(" ");
    		for(String direction: c)
    		{
    			//Point pt=Map.get(tile);
    			CarcassonneTile tempTile;
  				if(direction.equals("N"))
  					tempTile=map[x][y-1];
  					//pt.setLocation(pt.getX(), pt.getY()-tileSize);
  				else if(direction.equals("S"))
  					tempTile=map[x][y+1];
  					//pt.setLocation(pt.getX(), pt.getY()+tileSize);
  				else if(direction.equals("W"))
  					tempTile=map[x-1][y];
  					//pt.setLocation(pt.getX()-tileSize, pt.getY());
  				else //if(direction.equals("E"))
  					tempTile=map[x+1][y];
  					//pt.setLocation(pt.getX()+tileSize, pt.getY());
  				
  				//CarcassonneTile tempTile=findTile(pt);
  				if (!tempTile.hasCity())
  					return false;
    		}
  		}
  		citiesVal.set(cities.indexOf(cityTiles), true);
  		return true;
  	}
  	//receive the just placed tile, return the center monastery tile if completed, else return null
  	private ArrayList<CarcassonneTile> completeM (CarcassonneTile tile, int x, int y)
  	{
  		//Point loc=Map.get(tile);
  		ArrayList<CarcassonneTile> MTiles=new ArrayList<>();
  		ArrayList<Boolean> tilesComplete=new ArrayList<>();
  		for(int i=-1; i<2; i++)
  		{
  			for (int j=-1; j<2; j++)
  			{
  				CarcassonneTile t=map[x+i][y+j];
  				if(t!=null && t.isMonastery()) 
  					MTiles.add(t);
  			}
  		}
  		for(CarcassonneTile aTile: MTiles)
  		{
  			boolean complete=true;
  			for(int i=-1; i<2; i++)
    		{
    			for (int j=-1; j<2; j++)
    			{
    				CarcassonneTile t=map[x+i][y+j];
    				if(t==null) {
    					complete=false;
    					break;
    				}
    					
    			}
    			if(!complete)
    				break;
    		}
  			if(!complete)
  				tilesComplete.add(false);
  			else
  				tilesComplete.add(true);
  		}
  		
  		for(int i=MTiles.size()-1; i>=0; i--)
  		{
  			if(!tilesComplete.get(i))
  			{
  				MTiles.remove(i);
  				tilesComplete.remove(i);
  			}
  		}
  		/*int x=-tileSize;
  		int y=-tileSize;
  		for(int i=0; i<9; i++) {
  			Point newLoc=new Point((int)loc.getX()+x, (int)loc.getY()+y);
  			CarcassonneTile t= findTile(newLoc);
  			if(t!=null) {
  				if(t.isMonastery())
  					MTile=t;
  			}
  			if(MTile!=null)
  				break;
  			
  			if(x!=tileSize)
  				x+=tileSize;
  			else
  				y+=tileSize;
  		}
  		
  		x=-tileSize;
  		y=-tileSize;
  		if(MTile!=null){
  			loc=Map.get(MTile);
  			for(int i=0; i<9; i++) {
    			Point newLoc=new Point((int)loc.getX()+x, (int)loc.getY()+y);
    			CarcassonneTile t= findTile(newLoc);
    			if(t==null)
    				return null;
    				
    			if(x!=tileSize)
      			x+=tileSize;
      		else
      			y+=tileSize;}
  		}*/
  		
  		return MTiles;
  	}
  	
  	//assume the just-placed tile completed a road, do score calculation
  	public void roadScoring(CarcassonneTile tile, Point loc)
  	{
  		
  		//tileCodes have all the tiles that the completed road is on
  		//check for meeples
  		ArrayList<CarcassonneTile> temp=contains(tile, "RD");
  		String players=meepleCompare(temp, "Thief");
  		scoreCalc(players, 1, temp.size());
  	}
  	
  	//assume the just-placed tile completed a city, do score calculation
  	public void cityScoring(CarcassonneTile tile)
  	{
  		ArrayList<CarcassonneTile> C2Score=new ArrayList<>();
  		for(ArrayList<CarcassonneTile> a: cities) {
  			if(a.contains(tile.getCode())) {
  				C2Score=a;
  				break;
  			}
  				
  		}
  		
  		String owners=meepleCompare(C2Score, "Knight");
  		if (owners.length()<2)
  			return;
  		
  		int shieldNum=shieldNum(C2Score);
  		int score=2*C2Score.size()+2*shieldNum;
  		
  		String[] p=owners.split(" ");
  		for(String s: p) {
  			callPlayer(s).addScore(score);
  		}
  	}

  	//assume the just-placed tile completed a monastery, do score calculation
  	//MTile is the tile with the monastery on it, this will be done via completeM
  	public void monasteryScoring(ArrayList<CarcassonneTile> MTiles, Point loc)
  	{
  		for(CarcassonneTile MTile: MTiles) {
  				String name="";
  				Meeple mp=MTile.getMeeple();
  				if(mp!=null)
  					name=mp.getColor();
  				callPlayer(name).addScore(9);
  		}
  	}
  	
  	//end of game scoring
  	public void endOfGameScoring()
  	{
  		finalCityScoring();
  		finalMonasteryScoring();
  		finalRoadScoring();
  		farmlandScoring();
  	}
  	public void finalCityScoring()
  	{
  		for(int i=0; i<cities.size(); i++)
  		{
  			ArrayList<CarcassonneTile> list=cities.get(i);
  		//if city incomplete
  			if(!citiesVal.get(i)) {
  				int shields=shieldNum(list);
  				String owners=meepleCompare(list, "Knight");
  				String[] names=owners.split(" ");
  				for(String s: names) {
  					callPlayer(s).addScore(list.size()+shields);
  				}
  			}
  		}
  	}
  	
  	public void finalMonasteryScoring()
  	{
  			int x=0;
  			int y=0;
  			for(CarcassonneTile[] tiles: map)
  			{
  				for(CarcassonneTile tile: tiles)
  				{
  						if(tile.isMonastery()) {
  							Meeple mp=tile.getMeeple();
  							if(mp!=null) {
  								int totalTiles=0;
  								for(int i=-1; i<3; i++) {
  									for(int j=-1; j<3; j++)
  									{
  										CarcassonneTile t= map[x+i][y+j];
  										if(t!=null) 
  										totalTiles++;
  									}
  								}
  								callPlayer(mp.getColor()).addScore(totalTiles);
  							}
  		  		
  						}
  						y++;
  			}
  			x++;
  		}
  	}
  	
  	public void finalRoadScoring()
  	{
  		for(int i=0; i<roads.size(); i++) {
  			ArrayList<CarcassonneTile> list=roads.get(i);
  			//if road incomplete
  			if(!roadsVal.get(i)) {
  				String owners=meepleCompare(list, "Thief");
  				String[] names=owners.split(" ");
  				for(String s: names) {
  					callPlayer(s).addScore(list.size());
  				}
  			}
  		}
  	}
  	
  	public void farmlandScoring()
  	{
  		ArrayList<Integer> cityIndex=new ArrayList<>();
  		for(ArrayList<CarcassonneTile> farmland: farmlands)
  		{
  				for(CarcassonneTile tile: farmland)
  				{
  						int index=citiesContains(tile);
  						//if this tile is a city tile && the city is complete
  						if(index>=0 && citiesVal.get(index) && !cityIndex.contains(index))					
  							cityIndex.add(index);
  						
  				}
  				String player=meepleCompare(farmland, "farmer");
  				String[] players=player.split(" ");
  				for(String s: players)
  				{
  					callPlayer(s).addScore(cityIndex.size()*3);
  				}
  				cityIndex.clear();
  		}
  	}
  	
  	private int citiesContains(CarcassonneTile tile)
  	{
  		for(int i=0; i<cities.size(); i++)
  		{
  			ArrayList<CarcassonneTile> city=cities.get(i);
  			for (CarcassonneTile cityTile: city)
  			{
  					if (cityTile.getCode()==tile.getCode())
  						return i;
  			}
  		}	
  		return -1;
  	}
  	
  	private int shieldNum(ArrayList<CarcassonneTile> tiles)
  	{
  		int num=0;
  		for(CarcassonneTile t: tiles) {
  			if(t.hasShield())
  				num++;
  		}
  		return num;
  	}
  	
  	
  	private String meepleCompare(ArrayList<CarcassonneTile> tiles, String type)
  	{
  		TreeMap<String, Integer> players=new TreeMap<>();
  		for(CarcassonneTile t: tiles)
  		{
  			Meeple meeple=t.getMeeple();
  			if(meeple!=null) {
  				if (meeple.getType().equalsIgnoreCase(type))
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
  	
  	private ArrayList<CarcassonneTile> contains(CarcassonneTile tile, String type)
  	{
  			if(type.equals("RD"))
  			{
  				for(ArrayList<CarcassonneTile> list: roads) {
  					for(CarcassonneTile t: list)
  					{
  						if(t.getCode()==tile.getCode())
  							return list;
  					}
  				}
  				System.out.println("error, did not find tile in treemap roads");
  			}
  			
  			if(type.equals("C"))
  			{
  				for(ArrayList<CarcassonneTile> list: cities) {
  					for(CarcassonneTile t: list)
  					{
  						if(t.getCode()==tile.getCode())
  							return list;
  					}
  				}
  				System.out.println("error, did not find tile in treemap cities");
  			}
  			return null;
  	}
    
  	
    public static void main(String[] args) {
        // Following is a test data. These does not have effect to the main program
		CarcassonnePlayer r=new CarcassonnePlayer("red");
		CarcassonnePlayer y=new CarcassonnePlayer("yellow");
		CarcassonnePlayer b=new CarcassonnePlayer("blue");
		CarcassonnePlayer g=new CarcassonnePlayer("green");

    CarcassonneMap map = new CarcassonneMap(r, y, b, g);

		TileParser p = new TileParser();
		HashMap<Integer, CarcassonneTile> t = p.loadTiles("src/res/tileImg/tile_data.txt");

		t.get(49).rotate(Rotation.D180);
		System.out.println(map.tryAddAt(t.get(49), 44, 43));
		t.get(40).rotate(Rotation.D270);
		System.out.println(map.tryAddAt(t.get(40), 44, 42));
		t.get(73).rotate(Rotation.D270);
		System.out.println(map.tryAddAt(t.get(73), 45, 42));
		t.get(52).rotate(Rotation.D180);
		System.out.println(map.tryAddAt(t.get(52), 45, 41));

		t.get(17).rotate(Rotation.D180);
		System.out.println(map.tryAddAt(t.get(17), 44, 41));
		map.map[44][41] = t.get(17);

		try {
		    GameBoardGraphics gbg = map.render(1920, 1080);
            File outputfile = new File("C:\\Users\\k1702639\\Desktop\\a\\map.png");
            ImageIO.write(gbg.getImg(), "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

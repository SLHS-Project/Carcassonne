import tile.CarcassonneTile;
import tile.Rotation;
import tile.Meeple;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer.*;
import java.awt.geom.Ellipse2D;
import java.util.concurrent.Executor;
import java.util.*;
import java.util.Queue;
import tile.Rotation;

public class CarcassonnePanel extends JPanel implements MouseListener, ActionListener, KeyListener {
    private CarcassonneGraphic parent;
    private boolean addMeepleState;
    private String statusMessage;
    private CarcassonneMap.GameBoardGraphics gbg;
    private Rotation curr_rot;
    private ArrayList<Integer> river;
    private ArrayList<Integer> dec;
    private CarcassonneTile curr_tile;
    private Color brown = new Color(210, 161, 132);
    private Color yellow = new Color(255, 254, 185);
    private Color grey = new Color(206, 206, 206);
    private BufferedImage logo;
    private CarcassonnePlayer r, y, b, g;
    private CarcassonneMap map;
    private int tIndex=37;
    int tx, ty;
    private String where="";
    private BufferedImage redM, yellowM, blueM, greenM;
    private ArrayList<ArrayList<Point>> mpLoc; //arraylist has 4 arraylist corresponding to the locations of red meeples, yellow meeples, etc.
    private CarcassonnePlayer currPlayer;
    private boolean meepleAdded;

    public CarcassonnePanel(CarcassonneGraphic parent) throws IOException {
        this.parent = parent;
        initializeGame();
        initializeDeck();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addMouseListener(this);

        try {
            logo = ImageIO.read(CarcassonnePanel.class.getResource("/res/logo.jpg"));
            
            redM=ImageIO.read(CarcassonnePanel.class.getResourceAsStream("/meeple/red.jpg"));
            yellowM=ImageIO.read(CarcassonnePanel.class.getResourceAsStream("/meeple/yellow.jpg"));
            blueM=ImageIO.read(CarcassonnePanel.class.getResourceAsStream("/meeple/blue.jfif"));
            greenM=ImageIO.read(CarcassonnePanel.class.getResourceAsStream("/meeple/green.jfif"));
        }
        catch(Exception E) {
            E.printStackTrace();
        }
        
        currPlayer=r;
    }

    private void initializeGame() {
        r=new CarcassonnePlayer("red");
        y=new CarcassonnePlayer("yellow");
        b=new CarcassonnePlayer("blue");
        g=new CarcassonnePlayer("green");

        map = new CarcassonneMap(r, y, b, g);
        statusMessage = "";
        addMeepleState = false;
        meepleAdded=false;
        
        mpLoc=new ArrayList<>();
        mpLoc.add(new ArrayList<Point>());
        mpLoc.add(new ArrayList<Point>());
        mpLoc.add(new ArrayList<Point>());
        mpLoc.add(new ArrayList<Point>());
    }

    private void initializeDeck() {
        // Set up river deck and normal deck
        this.dec = new ArrayList<>();
        this.river = new ArrayList<>();
        this.river.add(38);
        this.river.add(39);
        this.river.add(40);
        this.river.add(49);
        this.river.add(50);
        this.river.add(51);
        this.river.add(52);
        this.river.add(61);
        this.river.add(73);
        this.river.add(74);
        this.river.add(75);
        for(int i = 1; i <= 84; i++) this.dec.add(i);
        for(int i: this.river) this.dec.removeIf(v -> v.equals(i));
        this.dec.removeIf(v -> v.equals(37));
        this.fetchNewTile();
    }

    private void restartGame() {
        initializeGame();
        initializeDeck();
    }

    public void paint(Graphics g) {
    	
    		System.out.println("current player is "+currPlayer.getName());
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(brown);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(yellow);
        g.fillRect(getWidth()*20/1920, getHeight()*20/1080, getWidth()-getWidth()*40/1920, getHeight()-getHeight()*40/1080);
        g.drawImage(logo, getWidth()*1610/1920, getHeight()*20/1080, (getWidth()-getWidth()*25/1920) - (getWidth()*1605/1920), (getHeight()*150/1080) - (getHeight()*20/1080), null);

        g.setColor(grey);
        g.fillRect(getWidth()*1610/1920, getHeight()*890/1080, (getWidth()-getWidth()*20/1920)-(getWidth()*1610/1920), (getHeight()-getHeight()*20/1080)-(getHeight()*890/1080));

        g2.setStroke(new BasicStroke(6));
        g.setColor(Color.black);
        g.drawRect(getWidth()*20/1920, getHeight()*20/1080, getWidth()-getWidth()*40/1920, getHeight()-getHeight()*40/1080);
        g.drawRect(0, 0, getWidth(), getHeight());

        // board location: getWidth()*20/1920, getHeight()*20/1080, getWidth()-getWidth()*25/1920, getHeight()*170/1080
        g.setColor(Color.black);
        g.drawLine(getWidth()*1610/1920, getHeight()*20/1080, getWidth()*1610/1920, getHeight()-getHeight()*25/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*150/1080, getWidth()-getWidth()*25/1920, getHeight()*150/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*280/1080, getWidth()-getWidth()*25/1920, getHeight()*280/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*390/1080, getWidth()-getWidth()*25/1920, getHeight()*390/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*500/1080, getWidth()-getWidth()*25/1920, getHeight()*500/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*610/1080, getWidth()-getWidth()*25/1920, getHeight()*610/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*830/1080, getWidth()-getWidth()*25/1920, getHeight()*830/1080);
        g.drawLine(getWidth()*1610/1920, getHeight()*890/1080, getWidth()-getWidth()*25/1920, getHeight()*890/1080);

        g.setColor(Color.black);

        Font f1 = new Font("Times New Roman", 0, getHeight()*20/1080);
        g.setFont(f1);
        g.setColor(Color.black);
        g.drawString("Player 1: " + r.getName(), getWidth()*1620/1920, getHeight()*210/1080);
        g.drawString("Score: " + r.getScore(), getWidth()*1620/1920, getHeight()*250/1080);

        g.drawString("Player 2: "+ y.getName(), getWidth()*1620/1920, getHeight()*320/1080);
        g.drawString("Score: " + y.getScore(), getWidth()*1620/1920, getHeight()*360/1080);

        g.drawString("Player 3: "+ b.getName(), getWidth()*1620/1920, getHeight()*430/1080);
        g.drawString("Score: " + b.getScore(), getWidth()*1620/1920, getHeight()*470/1080);

        g.drawString("Player 4: "+ this.g.getName(), getWidth()*1620/1920, getHeight()*540/1080);
        g.drawString("Score: " + this.g.getScore(), getWidth()*1620/1920, getHeight()*580/1080);

        Font f2 = new Font("Times New Roman", 0, getHeight()*35/1080);
        g.setFont(f2);
        g.drawString("Current Tile: ", getWidth()*1630/1920, getHeight()*660/1080);

        Font f3 = new Font("Times New Roman", 0, getHeight()*25/1080);
        g.setFont(f3);
        g.drawString("Tiles Remaining: " + (this.dec.size() + this.river.size()), getWidth()*1625/1920, getHeight()*870/1080);

        Font f4 = new Font("Times New Roman", 0, getHeight()*30/1080);
        g.setFont(f4);
        g.drawString("Press [h] To See", getWidth()*1670/1920, getHeight()*960/1080);
        g.drawString("Instructions", getWidth()*1677/1920, getHeight()*1010/1080);

        // Map
        this.gbg = map.render(getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080);
        g.drawImage(gbg.getImg(), getWidth()*20/1920, getHeight()*20/1080, getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080, yellow, null);

        // borderlines
        for(CarcassonneMap.Boundary b: gbg.getBoundaries()) {
            //b.translate(getWidth()*20/1920, getHeight()*20/1080);
            //g.drawRect(b.x(), b.y(), b.width(), b.height());
        }

        // tile preview
        g.drawImage(this.curr_tile.getImage(), getWidth()*1630/1920, getHeight()*660/1080, null);

        g.drawRect(tx-5,ty-5, 5, 5);
        
        //assign the player's meeple to a certain type: thief/warrior/farmer/monk
        //change that meeple to placed
        //assign the meeple to the tile
        if(where.length()>2 && !where.equals("meeple"))
        {
        		Meeple meeple=currPlayer.getMeeple();
        		if(meeple==null)
        			statusMessage="You don't have any meeples";
        		else {
        			meeple.place();
        			if(where.equals("road")) 
        				meeple.setType("thief");
        			else if(where.equals("city")) 
        				meeple.setType("warrior");
        			else if(where.equals("monastery")) 
        				meeple.setType("monk");
        			else if(where.equals("farmland")) 
        				meeple.setType("farmer");
        			
        			curr_tile.setMeeple(meeple);
        			meepleAdded=true;
        		}
        		
        }
        if(where.equals("meeple"))
        {
        	if(currPlayer.getName().equals("red"))
          	currPlayer=y;
          else if(currPlayer.getName().equals("yellow"))
          	currPlayer=b;
          else if(currPlayer.getName().equals("blue"))
          	currPlayer=this.g;
          else
          	currPlayer=r;
        }
        
        if(meepleAdded)
        {
        		String str="Click on "+where+" to place meeple";
        	 g.drawString(str, getWidth()*20/1920, getHeight()*20/1080 + getHeight() - 2 * getHeight()*20/1080 - 10);
        }
        where="";
        
        // Status message
        Font f5 = new Font("Times New Roman", 0, getHeight()*30/1080);
        g.setFont(f5);
        if(!meepleAdded)
        	g.drawString(this.statusMessage, getWidth()*20/1920, getHeight()*20/1080 + getHeight() - 2 * getHeight()*20/1080 - 10);
    
    
        //draw out all meeples
        for(int i=0; i<mpLoc.size(); i++) {
        	if(i==0)
        	{
        		for(Point p: mpLoc.get(i))
        			g.drawImage(redM, (int)p.getX(), (int)p.getY(), 15, 15, null);
        		
        	}
        	else if(i==1)
        	{
        		for(Point p: mpLoc.get(i))
        			g.drawImage(yellowM, (int)p.getX(), (int)p.getY(), 15, 15, null);
        		
        	}
        	else if(i==2)
        	{
        		for(Point p: mpLoc.get(i))
        			g.drawImage(blueM, (int)p.getX(), (int)p.getY(), 15, 15, null);
        		
        	}
        	else
        	{
        		for(Point p: mpLoc.get(i))
        			g.drawImage(greenM, (int)p.getX(), (int)p.getY(), 15, 15, null);
        		
        	}
        }
    }

    public void nextRot() {
        this.curr_rot = this.curr_rot.next();
        this.curr_tile.rotate(this.curr_rot);
    }

    public void fetchNewTile() {
        if (this.dec.isEmpty()) {
            System.out.println("All cards in Dec is used, Gamed ended");

            // Do Ending sequence here.
        }

        int tileindx = 0;
        if(!this.river.isEmpty()) {
            // 75 should be the last
            while(tileindx == 0 || (tileindx == 75 && this.river.size() != 1))
                tileindx = this.river.get((int) (Math.random() * this.river.size() - 1));
        }
        else
            tileindx = this.dec.get((int) (Math.random() * this.dec.size()-1));

        int ftileindx = tileindx;
        this.curr_tile = this.map.resources.getTiles().get(tileindx);
        this.dec.removeIf(v -> v.equals(ftileindx));
        this.river.removeIf(v -> v.equals(ftileindx));

        tIndex=tileindx;
        
        this.curr_rot = Rotation.D0;

        this.statusMessage = "press [s] to skip";
        
        
    }

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

        tx = x;
        ty = y;

        // check for adding tile
        if(!this.addMeepleState && !meepleAdded) {
            for (CarcassonneMap.Boundary b : gbg.getBoundaries()) {
                b.translate(getWidth() * 20 / 1920, getHeight() * 20 / 1080);
                if (b.contains(x, y)) {
                    if (map.tryAddAt(this.curr_tile, b.tilex, b.tiley, tIndex)) {
                        System.out.println("added at " + b.tilex + " " + b.tiley);
                        fetchNewTile();
                        this.statusMessage = "Add meeple at ([R]oad/[F]armland/[C]ity/[M]onastery/[N]one)? ";
                        this.addMeepleState = true;
                    } else {
                        // TODO Message this
                        this.statusMessage = "Cannot be added, conflict at " + map.getConflicts(this.curr_tile, b.tilex, b.tiley);
                        System.out.println("Cannot be added : " + map.getConflicts(this.curr_tile, b.tilex, b.tiley));
                    }
                }
            }
        }
        //add meeple to a tile, draw out meeple
        if(meepleAdded)
        {
        		if(currPlayer.getName().equals("red")) {
        			ArrayList<Point> list=mpLoc.get(0);
        			list.add(new Point(x, y));
        		}
        		else if(currPlayer.getName().equals("yellow")) {
        			ArrayList<Point> list=mpLoc.get(1);
        			list.add(new Point(x, y));
        		}
        		else if(currPlayer.getName().equals("blue")) {
        			ArrayList<Point> list=mpLoc.get(2);
        			list.add(new Point(x, y));
        		}
        		else{
        			ArrayList<Point> list=mpLoc.get(3);
        			list.add(new Point(x, y));
        		}
        		meepleAdded=false;
        		
        		if(currPlayer.getName().equals("red"))
            	currPlayer=this.y;
            else if(currPlayer.getName().equals("yellow"))
            	currPlayer=b;
            else if(currPlayer.getName().equals("blue"))
            	currPlayer=g;
            else
            	currPlayer=r;
        }
        

        // do instruction etc here.
        this.repaint();
    }

    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
        if(this.addMeepleState) {
            // Meeple adding here
            switch (e.getKeyChar()) {
                case 'R': case 'r': this.statusMessage = "Meeple added to the road";break;
                case 'F': case 'f': this.statusMessage = "Meeple added to the farmland";break;
                case 'C': case 'c': this.statusMessage = "Meeple added to the city";break;
                case 'M': case 'm': this.statusMessage = "Meeple added to the monastery";break;
                case 'N': case 'n': this.statusMessage = "skipped meeple";break;
                default: this.repaint();return;
            }
            String[] temp=statusMessage.split(" ");
            where=temp[temp.length-1];
            this.addMeepleState = false;
            
           
            
        } else {
            switch (e.getKeyChar()) {
                case 'r':
                    this.nextRot();
                    break;
                case 's':
                    this.fetchNewTile();
                    break;
                case 'R':
                    this.restartGame();
                    this.parent.change("menu");
                    break;
                case 'h':
                    this.parent.change("instr");
                    break;
            }
        }

		this.repaint();
	}

    @Override public void keyReleased(KeyEvent arg0) { }
    @Override public void keyTyped(KeyEvent arg0) { }
    @Override public void actionPerformed(ActionEvent arg0) { }
    @Override public void mouseEntered(MouseEvent arg0) { }
    @Override public void mouseExited(MouseEvent arg0) { }
    @Override public void mousePressed(MouseEvent arg0) { }
    @Override public void mouseReleased(MouseEvent arg0) { }
}

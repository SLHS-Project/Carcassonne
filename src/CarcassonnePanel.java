import tile.CarcassonneTile;
import tile.Rotation;

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

public class CarcassonnePanel extends JPanel implements MouseListener, ActionListener, KeyListener{
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

	public CarcassonnePanel() throws IOException {
		r=new CarcassonnePlayer("red");
		y=new CarcassonnePlayer("yellow");
		b=new CarcassonnePlayer("blue");
		g=new CarcassonnePlayer("green");


		map = new CarcassonneMap(r, y, b, g);

		this.dec = new ArrayList<>();
		this.river = new ArrayList<>();
		this.river.add(38);
		this.river.add(40);
		this.river.add(49);
		this.river.add(50);
		this.river.add(51);
		this.river.add(52);
		this.river.add(61);
		this.river.add(73);
		this.river.add(74);
		this.river.add(75);
		this.dec.removeIf(v -> v.equals(37));
		for(int i = 0; i < 84; i++) this.dec.add(i);
		for(int i: this.river) this.dec.removeIf(v -> v.equals(i));

		this.fetchNewTile();

		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		addMouseListener(this);
		
		try {
			logo = ImageIO.read(CarcassonnePanel.class.getResource("/Images/logo.jpg"));
		}
		catch(Exception E) {
		    E.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(brown);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(yellow);
		g.fillRect(getWidth()*20/1920, getHeight()*20/1080, getWidth()-getWidth()*40/1920, getHeight()-getHeight()*40/1080);
		g.drawImage(logo, getWidth()*1610/1920, getHeight()*20/1080, (getWidth()-getWidth()*25/1920) - (getWidth()*1605/1920), (getHeight()*170/1080) - (getHeight()*20/1080), null);
		
		g.setColor(grey);
		g.fillRect(getWidth()*1610/1920, getHeight()*890/1080, (getWidth()-getWidth()*20/1920)-(getWidth()*1610/1920), (getHeight()-getHeight()*20/1080)-(getHeight()*890/1080));
		
		g2.setStroke(new BasicStroke(6));
		g.setColor(Color.black);
		g.drawRect(getWidth()*20/1920, getHeight()*20/1080, getWidth()-getWidth()*40/1920, getHeight()-getHeight()*40/1080);
		g.drawRect(0, 0, getWidth(), getHeight());
		
		// board location: getWidth()*20/1920, getHeight()*20/1080, getWidth()-getWidth()*25/1920, getHeight()*170/1080
		g.setColor(Color.black);
		g.drawLine(getWidth()*1610/1920, getHeight()*20/1080, getWidth()*1610/1920, getHeight()-getHeight()*25/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*170/1080, getWidth()-getWidth()*25/1920, getHeight()*170/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*280/1080, getWidth()-getWidth()*25/1920, getHeight()*280/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*390/1080, getWidth()-getWidth()*25/1920, getHeight()*390/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*500/1080, getWidth()-getWidth()*25/1920, getHeight()*500/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*610/1080, getWidth()-getWidth()*25/1920, getHeight()*610/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*830/1080, getWidth()-getWidth()*25/1920, getHeight()*830/1080);
		g.drawLine(getWidth()*1610/1920, getHeight()*890/1080, getWidth()-getWidth()*25/1920, getHeight()*890/1080);
		g.drawLine(getWidth()*1755/1920, getHeight()*890/1080, getWidth()*1755/1920, getHeight()-getHeight()*25/1080);

		g.setColor(Color.black);
        this.gbg = map.render(getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080);
		g.drawImage(gbg.getImg(), getWidth()*20/1920, getHeight()*20/1080, getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080, yellow, null);

		Font f1 = new Font("Times New Roman", 0, getHeight()*20/1080);
		g.setFont(f1);
		g.setColor(Color.black);
		g.drawString("Player 1: ", getWidth()*1620/1920, getHeight()*210/1080);
		g.drawString("Score: ", getWidth()*1620/1920, getHeight()*250/1080);
		g.drawString("Player 2: ", getWidth()*1620/1920, getHeight()*320/1080);
		g.drawString("Score: ", getWidth()*1620/1920, getHeight()*360/1080);
		g.drawString("Player 3: ", getWidth()*1620/1920, getHeight()*430/1080);
		g.drawString("Score: ", getWidth()*1620/1920, getHeight()*470/1080);
		g.drawString("Player 4: ", getWidth()*1620/1920, getHeight()*540/1080);
		g.drawString("Score: ", getWidth()*1620/1920, getHeight()*580/1080);
		
		Font f2 = new Font("Times New Roman", 0, getHeight()*35/1080);
		g.setFont(f2);
		g.drawString("Current Tile: ", getWidth()*1630/1920, getHeight()*660/1080);

		g.drawImage(this.curr_tile.getImage(), getWidth()*1630/1920, getHeight()*660/1080, null);

		Font f3 = new Font("Times New Roman", 0, getHeight()*25/1080);
		g.setFont(f3);
		g.drawString("Tiles Remaining: ", getWidth()*1625/1920, getHeight()*870/1080);
		
		Font f4 = new Font("Times New Roman", 0, getHeight()*30/1080);
		g.setFont(f4);
		g.drawString("Click To See", getWidth()*1670/1920, getHeight()*960/1080);
		g.drawString("Instructions", getWidth()*1677/1920, getHeight()*1010/1080);
	}

	public void nextRot() {
	    this.curr_rot = this.curr_rot.next();
	    this.curr_tile.rotate(this.curr_rot);
	}

	public void fetchNewTile() {
	    int tileindx;
		if(!this.river.isEmpty())
			tileindx = this.river.get((int) (Math.random() * this.river.size() - 1));
		else
			tileindx = this.dec.get((int) (Math.random() * this.dec.size()-1));

		this.curr_tile = this.map.resources.getTiles().get(tileindx);
		this.dec.removeIf(v -> v.equals(tileindx));
		this.river.removeIf(v -> v.equals(tileindx));

		System.out.println(tileindx + " " + this.river);
		this.curr_rot = Rotation.D0;
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		for(CarcassonneMap.Boundary b: gbg.getBoundaries()) {
			b.translate(getWidth()*20/1920, getHeight()*20/1080);
			if(b.contains(x, y)) {
			    if(map.tryAddAt(this.curr_tile, b.tilex, b.tiley)) {
					System.out.println("added at " + b.tilex + " " + b.tiley);
					fetchNewTile();
				}
			}
		}

	    this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
	    switch(e.getKeyChar()) {
			case 'r':
			    this.nextRot();
		}

		System.out.println(this.curr_rot);

		this.repaint();
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}


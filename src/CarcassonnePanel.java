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

public class CarcassonnePanel extends JPanel implements MouseListener, ActionListener, KeyListener{
	
	private Color brown = new Color(210, 161, 132);
	private Color yellow = new Color(255, 254, 185);
	private Color grey = new Color(206, 206, 206);
	private BufferedImage logo;
	private CarcassonnePlayer r, y, b, g;
	private CarcassonneMap map;

	public CarcassonnePanel() throws IOException {
		r=new CarcassonnePlayer();
		y=new CarcassonnePlayer();
		b=new CarcassonnePlayer();
		g=new CarcassonnePlayer();

		map = new CarcassonneMap(r, y, b, g);
		System.out.println(map.tryAddAt(map.tile_ref.get(39), 44, 43));
		System.out.println(map.tryAddAt(map.tile_ref.get(31), 44, 44));
		System.out.println(map.tryAddAt(map.tile_ref.get(30), 43, 44));
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		addMouseListener(this);
		
		try {
			logo = ImageIO.read(CarcassonnePanel.class.getResource("/Images/logo.jpg"));
		}
		
		
		catch(Exception E) {
			System.out.println("Exception Error");
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
        CarcassonneMap.GameBoardGraphics boardgraphics = map.render(getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080);
		//g.drawImage(boardgraphics.getImg(), (getWidth()*20/1920+(getWidth()*1610/1920 - getWidth()*20/1920))/2 - boardgraphics.getImg().getWidth()/2,
		//		(getHeight()*20/1080+(getHeight() - 2 * getHeight()*20/1080))/2 - boardgraphics.getImg().getHeight()/2, null);
		g.drawImage(boardgraphics.getImg(), getWidth()*20/1920, getHeight()*20/1080, getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080, yellow, null);
		/* board area: *///g.fillRect(getWidth()*20/1920, getHeight()*20/1080, getWidth()*1610/1920 - getWidth()*20/1920, getHeight() - 2 * getHeight()*20/1080);
		
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
		Font f3 = new Font("Times New Roman", 0, getHeight()*25/1080);
		g.setFont(f3);
		g.drawString("Tiles Remaining: ", getWidth()*1625/1920, getHeight()*870/1080);
		
		Font f4 = new Font("Times New Roman", 0, getHeight()*30/1080);
		g.setFont(f4);
		g.drawString("Click To See", getWidth()*1670/1920, getHeight()*960/1080);
		g.drawString("Instructions", getWidth()*1677/1920, getHeight()*1010/1080);
	}
	

	public void mouseClicked(MouseEvent e) {

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


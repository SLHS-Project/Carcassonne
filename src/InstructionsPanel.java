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

public class InstructionsPanel extends JPanel implements MouseListener, ActionListener, KeyListener{
	
	private Color brown = new Color(210, 161, 132);
	private Color yellow = new Color(255, 254, 185);
	private Color grey = new Color(206, 206, 206);
	private Color lightorangebrown = new Color(236, 182, 100);
	private BufferedImage logo, inst;
	

	public InstructionsPanel() throws IOException
	{
		Scanner input = new Scanner(System.in);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		addMouseListener(this);
		
		try
		{
			logo = ImageIO.read(CarcassonnePanel.class.getResource("/Images/logo.jpg"));
			inst = ImageIO.read(CarcassonnePanel.class.getResource("/Images/inst.PNG"));
		}
		
		
		catch(Exception E)
		{
			System.out.println("Exception Error");
		}
	}
	
	public void paint(Graphics g)
	{ 
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(yellow);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.RED);
		g.fillRect(getWidth()*50/1920, getHeight()*850/1080, getWidth()*200/1920, getHeight()*200/1080);
		g.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(6));
		g2.drawRect(getWidth()*50/1920, getHeight()*850/1080, getWidth()*200/1920, getHeight()*200/1080);
		Font f2 = new Font("Times New Roman", 0, getHeight()*30/1080);
		g.setFont(f2);
		g.drawString("Press '?'", getWidth()*90/1920, getHeight()*955/1080);
		g.drawString("To Go Back", getWidth()*70/1920, getHeight()*1010/1080);
		g.drawImage(inst, getWidth()*500/1920, getHeight()*50/1080, getWidth()*920/1920, getHeight()*700/1080, null);
		
	}
	

	public void mouseClicked(MouseEvent e) 
	{
		
	
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


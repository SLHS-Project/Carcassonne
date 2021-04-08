import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CarcassonneGraphic extends JFrame{
	
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	
	public CarcassonneGraphic(String frameName) throws IOException
	{
		super(frameName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		add(new ScoreboardPanel());
		setVisible(true);
		
	}

}

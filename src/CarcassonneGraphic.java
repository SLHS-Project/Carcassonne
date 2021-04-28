import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import javax.swing.JFrame;
import java.awt.event.*;

public class CarcassonneGraphic extends JFrame {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;

	protected JPanel main;
	protected JPanel menu;
	protected JPanel instr;
	protected JPanel game;
	final public CardLayout layout;

	public void change(String s) {
	    this.layout.show(this.main, s);
	}

	public void changePrev() {
		this.layout.previous(this.main);
	}

	public CarcassonneGraphic(String frameName) throws IOException {
		super(frameName);

		this.menu = new BeginningPanel(this);
		this.instr = new InstructionsPanel(this);
		this.game = new CarcassonnePanel(this);

		this.layout = new CardLayout();
		this.main = new JPanel(layout);
		this.main.setPreferredSize(new Dimension(1920, 1080));
		this.main.add(menu, "menu");
		this.main.add(instr, "instr");
		this.main.add(game, "game");

		super.add(this.main);
		super.pack();
		super.setLocationRelativeTo(null);
		//super.setResizable(false);
		super.setVisible(true);

		this.change("menu");
		/*
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		add(new CarcassonnePanel());
		setVisible(true);
		 */
	}
}

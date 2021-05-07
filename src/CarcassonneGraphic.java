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
	protected JPanel end;
	protected CarcassonnePlayer winner;
	final public CardLayout layout;

	public void end(CarcassonnePlayer winner) {
		this.winner = winner;
		this.change("end");
	}
	public void change(String s) {
	    this.layout.show(this.main, s);
	}
	public void changePrev() {
		this.layout.previous(this.main);
	}

	public CarcassonneGraphic(String frameName) throws IOException {
		super(frameName);

		this.winner = null;
		this.menu = new BeginningPanel(this);
		this.instr = new InstructionsPanel(this);
		this.game = new CarcassonnePanel(this);
		this.end = new EndPanel(this);

		this.layout = new CardLayout();
		this.main = new JPanel(layout);
		this.main.setPreferredSize(new Dimension(1920, 1080));
		this.main.add(menu, "menu");
		this.main.add(instr, "instr");
		this.main.add(game, "game");
		this.main.add(end, "end");

		super.add(this.main);
		super.pack();
		super.setLocationRelativeTo(null);

		super.setVisible(true);
		this.change("menu");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
	}
}

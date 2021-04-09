package tile;
import java.awt.image.BufferedImage;

public class Meeple {

	private String color;
	private String type;
	private BufferedImage meeple;
	private boolean placed;
	
	public Meeple(String player)
	{
		color=player;
		placed=false;
		
		//SET MEEPLE IMAGE
	}
	
	public String getColor()
	{
		return color;
	}
	
	public String getType()
	{
		return type;
	}
	
	public BufferedImage getImg()
	{
		return meeple;
	}
	
	public boolean available()
	{
		return true;
	}
	
	public void place()
	{
		placed=true;
	}
	public void lift()
	{
		placed=false;
	}
}

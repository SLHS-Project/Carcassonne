package tile;
import player.CarcassonnePlayer;

import java.awt.image.BufferedImage;

public class Meeple {

    private CarcassonnePlayer owner;
	private String type;
	private BufferedImage meeple;
	private boolean placed;
	
	public Meeple(CarcassonnePlayer owner)
	{
		this.owner =owner;
		placed=false;
		
		//SET MEEPLE IMAGE
	}
	
	public CarcassonnePlayer getOwner()
	{
		return owner;
	}
	
	public String getType()
	{
		return type;
	}
	public void setType(String t)
	{
		type=t;
	}
	
	public BufferedImage getImg()
	{
		return meeple;
	}
	
	public boolean available()
	{
		if(placed)
			return false;
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

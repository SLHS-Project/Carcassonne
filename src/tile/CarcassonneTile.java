package tile;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * TODO
 * -Define properties:
 * 		river connection
 * 		road  connection
 * 		castle/farmland handling
 * -Meeples
 * -Connection validations   -- Done
 * 		ex) can this piece placed here? -- Done
 */
public class CarcassonneTile {
	public int id;
	boolean monastery;
	BufferedImage image;
	Rotation rotation;
	Side[] sides;
	private int code;
	private Meeple meeple;
	private boolean shield;

	public CarcassonneTile(Side[] sides) {
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}

	public CarcassonneTile(Side[] sides, BufferedImage img) {
		this.shield = false;
		this.monastery = false;
		this.image = img;
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}

	public CarcassonneTile(boolean monastery, Side[] sides, BufferedImage img) {
		this.shield = false;
	    this.monastery = monastery;
		this.image = img;
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}

	public CarcassonneTile(boolean monastery, boolean shield, Side[] sides, BufferedImage img) {
		this.shield = shield;
		this.monastery = monastery;
		this.image = img;
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}

	public CarcassonneTile(int indx, boolean monastery, boolean shield, Side[] sides, BufferedImage img) {
		this.id = indx;
		this.shield = shield;
		this.monastery = monastery;
		this.image = img;
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}
	
	public void setCode(int c)
	{
		code=c;
	}

	public BufferedImage getImage() {
		BufferedImage tileimg = this.rotateImageByDegrees(this.image, this.rotation.degree());
		BufferedImage r = new BufferedImage(75, 75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = r.createGraphics();
		g2d.drawImage(this.resize(tileimg.getSubimage(4, 4, tileimg.getWidth()-5, tileimg.getHeight()-5), 75, 75), 0, 0, null);
		g2d.dispose();

		return r;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();

		return rotated;
	}
	
	public Side[] getSides() {
		return this.sides;
	}

	public void rotate(Rotation r) {
		this.rotation = r;
	}

	public Side[] getRotatedSides() {
		return new Side[] {
			this.sides[Orient.N.rotate(this.rotation).iden()],
			this.sides[Orient.W.rotate(this.rotation).iden()],
			this.sides[Orient.S.rotate(this.rotation).iden()],
			this.sides[Orient.E.rotate(this.rotation).iden()],
		};
	}
	
	public boolean isMonastery()
	{
		return monastery;
	}
	
	public boolean fit(CarcassonneTile t, Orient o) {
		return this.getRotatedSides()[o.iden()].equals(t.getRotatedSides()[o.opposite().iden()].getReversedSide());
	}

	public ArrayList<Orient> getRoads() {
	    Side[] s = this.getRotatedSides();
	    ArrayList<Orient> ret = new ArrayList<>();

		if(s[0].getSide()[1] == TerrainType.Road) ret.add(Orient.N);
		if(s[1].getSide()[1] == TerrainType.Road) ret.add(Orient.W);
		if(s[2].getSide()[1] == TerrainType.Road) ret.add(Orient.S);
		if(s[3].getSide()[1] == TerrainType.Road) ret.add(Orient.E);

		return ret;
	}

	public ArrayList<Orient> getCities() {
		Side[] s = this.getRotatedSides();
		ArrayList<Orient> ret = new ArrayList<>();

		if(s[0].getSide()[1] == TerrainType.City) ret.add(Orient.N);
		if(s[1].getSide()[1] == TerrainType.City) ret.add(Orient.W);
		if(s[2].getSide()[1] == TerrainType.City) ret.add(Orient.S);
		if(s[3].getSide()[1] == TerrainType.City) ret.add(Orient.E);

		return ret;
	}

	public boolean isCR()
	{
		ArrayList<Integer> a=new ArrayList<>(Arrays.asList(13, 16, 33, 43, 45, 57, 69, 72));
		if(a.contains(code))
			return true;
		return false;
	}
	public boolean isRiver()
	{
		ArrayList<Integer> a=new ArrayList<>(Arrays.asList(37, 38, 39, 40, 49, 50, 51, 52, 61, 73, 74, 75));
		if(a.contains(code))
			return true;
		return false;
	}
	public boolean hasShield()
	{
	    if(this.shield)
			return true;
		return false;
	}
	
	
	public int checkRdDirections()
	{
		int num=0;
		for(Side s: sides)
		{
			TerrainType[] t=s.getSide();
			if(t[1].Road!=null) //check if the road exist on this side of the tile
				num++;
		}
		return num;
	}
//counterclockwise
	public String getRoadDirections() {
		String ret = "";
		String keys = "NWSE";
		System.out.println("tile rotation "+rotation.degree());
		for(int i = 0; i < 4; i++)
			if(this.sides[i].getSide()[1] == TerrainType.Road) 
			{
				if(rotation.degree()==0)
					ret += keys.charAt(i) + " ";
				else if(rotation.degree()==270) {
					int j=i+1;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==180) {
					int j=i+2;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==90) {
					int j=i+3;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
					
			}

		return ret;
	}

	public String getCityDirections() {
		String ret = "";
		String keys = "NWSE";
		for(int i = 0; i < 4; i++)
			if(this.sides[i].getSide()[1] == TerrainType.City) {
				if(rotation.degree()==0)
					ret += keys.charAt(i) + " ";
				else if(rotation.degree()==270) {
					int j=i+1;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==180) {
					int j=i+2;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==90) {
					int j=i+3;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
			}

		return ret;
	}
	
	public String blockFarm()
	{
		String ret = "";
		String keys = "NWSE";
		for(int i = 0; i < 4; i++)
			if(this.sides[i].getSide()[0] == TerrainType.Farm && 
				 this.sides[i].getSide()[2] == TerrainType.Farm && 
				 this.sides[i].getSide()[1] != TerrainType.Farm) {
				if(rotation.degree()==0)
					ret += keys.charAt(i) + " ";
				else if(rotation.degree()==270) {
					int j=i+1;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==180) {
					int j=i+2;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==90) {
					int j=i+3;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
			}

		return ret;
	}
	
	public String getFarmDirections()
	{
		String ret = "";
		String keys = "NWSE";
		for(int i = 0; i < 4; i++)
			if(this.sides[i].getSide()[0] == TerrainType.Farm) {
				if(rotation.degree()==0)
					ret += keys.charAt(i) + " ";
				else if(rotation.degree()==270) {
					int j=i+1;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==180) {
					int j=i+2;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
				else if(rotation.degree()==90) {
					int j=i+3;
					j=j%4;
					ret += keys.charAt(j) + " ";
				}
			}

		return ret;
	}
	
	public boolean hasCity()
	{
		if(getCityDirections().length()>0)
			return true;
		return false;
	}
	public Meeple getMeeple()
	{
		return meeple;
	}
	
	public int getCode()
	{
		return code;
	}

	public String toString() {
		/*
		Side[] rotated = this.getRotatedSides();
		return String.format(
				"      %s    %s    %s      \n" +
				"%s                          %s\n" +
				"%s                          %s\n" +
				"%s                          %s\n" +
				"      %s    %s    %s      \n",
				rotated[0].getSide()[0], rotated[0].getSide()[1], rotated[0].getSide()[2],
				rotated[1].getSide()[2], rotated[3].getSide()[0],
				rotated[1].getSide()[1], rotated[3].getSide()[1],
				rotated[1].getSide()[0], rotated[3].getSide()[2],
				rotated[2].getSide()[0], rotated[2].getSide()[1], rotated[2].getSide()[2]);
		 */

        return "" + this.id;
	}

	public static void main(String[] args) {
		// Following is a test data. These does not have effect to the main program
		CarcassonneTile t1 = new CarcassonneTile(new Side[] {
				
				new Side(TerrainType.City, TerrainType.City, TerrainType.Farm), //N
				new Side(TerrainType.City, TerrainType.City, TerrainType.Farm), //W
				new Side(TerrainType.Farm, TerrainType.Road, TerrainType.Farm), //S
				new Side(TerrainType.Farm, TerrainType.Road, TerrainType.Farm)  //E
		});
		
		CarcassonneTile t2 = new CarcassonneTile(new Side[] {
				new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
				new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
				new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
				new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
		});
		System.out.println(t1);
		System.out.println(t1.getRoadDirections());
		System.out.println(t1.getCityDirections());
		/*
		// T1
			  City    City    Farm
		Farm                          Farm
		City                          Road
		City                          Farm
			  Farm    Road    Farm

		// T2
			  Farm    City    City
		Farm                          Farm
		Farm                          Farm
		Farm                          Farm
			  Farm    Farm    Farm

		// Test fitting each rotation, connecting from West of T1
		// 90 Degrees should only work.
		 */
		/*
		System.out.println("t1: " + t1 + "\nt2: " + t2);


		System.out.println("match D0  ? :" + t1.fit(t2, Orient.W));
		t2.rotate(Rotation.D90);
		System.out.println("match D90 ? :" + t1.fit(t2, Orient.W));
		t2.rotate(Rotation.D180);
		System.out.println("match D180? :" + t1.fit(t2, Orient.W));
		t2.rotate(Rotation.D270);
		System.out.println("match D270? :" + t1.fit(t2, Orient.W));

		// Testing pretty printing edges
		System.out.println(t1);
		t2.rotate(Rotation.D180);
		System.out.println(t2);


		// image rotation test
		BufferedImage timg = null;
		try {
			timg = ImageIO.read(CarcassonneTile.class.getResource("/res/tileImg/1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		CarcassonneTile imgt = new CarcassonneTile(new Side[] {
				new Side(TerrainType.Farm, TerrainType.City, TerrainType.City), //N
				new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //W
				new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm), //S
				new Side(TerrainType.Farm, TerrainType.Farm, TerrainType.Farm)  //E
		}, timg);

		imgt.rotate(Rotation.D90);

		try {
			File outputfile = new File("C:\\Users\\k1702639\\Desktop\\a\\image90.png");
			ImageIO.write(imgt.getImage(), "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imgt.rotate(Rotation.D180);

		try {
			File outputfile = new File("C:\\Users\\k1702639\\Desktop\\a\\image180.png");
			ImageIO.write(imgt.getImage(), "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imgt.rotate(Rotation.D270);

		try {
			File outputfile = new File("C:\\Users\\k1702639\\Desktop\\a\\image270.png");
			ImageIO.write(imgt.getImage(), "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 */
	}
}

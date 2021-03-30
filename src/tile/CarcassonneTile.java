package tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	BufferedImage image;
	Rotation rotation;
	Side[] sides;

	public CarcassonneTile(Side[] sides) {
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}

	public CarcassonneTile(Side[] sides, BufferedImage img) {
		this.image = img;
		this.rotation = Rotation.D0;
		this.sides = new Side[4];
		this.sides = sides;
	}

	public BufferedImage getImage() {
		BufferedImage tileimg = this.rotateImageByDegrees(this.image, this.rotation.degree());
		BufferedImage r = new BufferedImage(75, 75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = r.createGraphics();
		g2d.setPaint ( new Color (12, 12, 12) );
		g2d.fillRect ( 0, 0, r.getWidth(), r.getHeight() );
		g2d.drawImage(tileimg, 0, 0, null);
		g2d.dispose();

		return r;
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
	
	public boolean fit(CarcassonneTile t, Orient o) {
		return this.sides[o.iden()].equals(t.getRotatedSides()[o.opposite().iden()].getReversedSide());
	}

	public String toString() {
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
	}
}

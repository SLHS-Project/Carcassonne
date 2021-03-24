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
 * -Connection validations
 * 		ex) can this piece placed here?
 */

/*
 * NW  N  NE
 * W   C   E
 * SW  S  SE
 *
 * More likely
 * EX) 1.png
 * Road: S, E
 * River: null
 * City: null
 * 
 * EX) 28.png
 * Road: null
 * River: null
 * City: N, E, S
 * 
 * EX) 40.png
 * Road: null
 * River: W, S
 * City: N, E
 * 
 * 
 * 
 * Maybe
 * EX) 1.png
 * G  G  G
 * G  R  R
 * G  R  G
 * 
 * EX) 28.png
 * G  C  C
 * G  C  C
 * G  C  C
 * 
 * EX) 40.png
 * C  C  C
 * R  R  C
 * G  R  C
 * 
 * Maybe 
 * EX) 1.png
 * N: F  F  F
 * W: F  F  F
 * S: F  R  F
 * E: F  R  F
 * 
 * EX) 28.png
 * N: C  C  C
 * W: F  F  F
 * S: C  C  C
 * E: C  C  C
 * 
 * EX) 40.png
 * N: C  C  C
 * W: F  R  F
 * S: F  R  F
 * E: C  C  C
 * 
 *    C  C  C
 * F  F  F  F  C
 * R  R  R  F  C
 * F  F  R  F  C
 *    F  R  F
 *    
 * 
 */
enum Rotation {
	D0(0),
	D90(3),
	D180(2),
	D270(1);

	private final int id;
	Rotation(int id) {
		this.id = id;
	}
	int iden() {
		return this.id;
	}
	int degree() {
		switch(this.iden()) {
			case 0: return 0;
			case 1: return 270;
			case 2: return 180;
			case 3: return 90;
			default: return 0;
		}
	}
}

enum Orient {
	N(0), W(1), S(2), E(3);

	private final int id;
	Orient(int id) {
		this.id = id;
	}
	int iden() {
		return this.id;
	}
	Orient fromId(int i) {
		for(Orient o: Orient.values())
			if(o.id == i)
				return o;
		return null;
	}
	Orient opposite() {
		return this.fromId((this.id + 2)%4);
	}
	/*
		Visualization
		  F  C  F            F  F  C
		 C    N    F        F    W    F
		 F  W   E  F -D90-> R  S   N  C
		 F    S    C        F    E    F
		   F  R  F            C  F  F

		N -> E
		W -> N
		...

		  F  C  F             F  R  F
		 C    N    F         C    S    F
		 F  W   E  F -D180-> F  E   W  F
		 F    S    C         F    N    C
		   F  R  F             F  C  F

		N -> S
		W -> E
		...

		  F  C  F             F  F  C
		 C    N    F         F    E    F
		 F  W   E  F -D270-> C  N   S  R
		 F    S    C         F    W    F
		   F  R  F             C  F  F

		N -> W
		W -> S
		...
	 */
	Orient rotate(Rotation r) {
		return this.fromId((this.id - r.iden() + 4) % 4);
	}
};

enum TerrainType {
	Farm, River, City, Road
};

class Side {
	TerrainType[] sidetypes;

	Side(TerrainType l, TerrainType m, TerrainType r) {
		sidetypes = new TerrainType[3];
		sidetypes[0] = l;
		sidetypes[1] = m;
		sidetypes[2] = r;
	}

	public TerrainType[] getSide() {
		return this.sidetypes;
	}

	public Side getReversedSide() {
		return new Side(
			this.sidetypes[2],
			this.sidetypes[1],
			this.sidetypes[0]
		);
	}

	public boolean equals(Object obj) {
		return Arrays.equals(this.sidetypes, ((Side) obj).getSide());
	}
}

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
		return this.rotateImageByDegrees(this.image, this.rotation.degree());
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
		g2d.setColor(Color.RED);
		g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
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

	public static void main(String[] args) { // TEST
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


		// image roation test
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

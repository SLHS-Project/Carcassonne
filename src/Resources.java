/*
 * TODO
 * -Holdes defined tiles for each id
 * 		*Use static variable for easy access
 */

import java.awt.image.*;
import javax.imageio.ImageIO;

import javax.imageio.ImageIO;
public class Resources {

		public Resources()
		{
			
		}
		public BufferedImage[] getRiverTiles()
		{
				BufferedImage[] imgs=new BufferedImage[12];
				try {
						BufferedImage img = ImageIO.read(Resources.class.getResource("/res.tileImg/37.PNG"));
						imgs[0]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/38.PNG"));
						imgs[1]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/39.PNG"));
						imgs[2]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/40.PNG"));
						imgs[3]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/49.PNG"));
						imgs[4]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/50.PNG"));
						imgs[5]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/51.PNG"));
						imgs[6]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/52.PNG"));
						imgs[7]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/61.PNG"));
						imgs[8]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/73.PNG"));
						imgs[9]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/74.PNG"));
						imgs[10]=img;
						
						img = ImageIO.read(Resources.class.getResource("/res.tileImg/75.PNG"));
						imgs[11]=img;
						
				}
				catch(Exception E)
				{
						System.out.println("Exception error for riverTiles in class Resources");
				}
				
				return imgs;
		}

		//excluding river tiles
		public BufferedImage[] getTiles()
		{
				BufferedImage[] imgs=new BufferedImage[72];
				for(int i=1; i<=84; i++)
				{
						if(i!=37 && i!=38 && i!=39 && i!=40 && i!=49 && i!=50 && i!=51 && i!=52 && i!=61 && i!=73 && i!=74 && i!=75 ) {
							try {
									BufferedImage img = ImageIO.read(Resources.class.getResource("i.PNG"));
									imgs[i]=img;
							}
							catch(Exception E)
							{}
						}
				}
				return imgs;
		}
}
